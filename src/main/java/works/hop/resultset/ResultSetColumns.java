package works.hop.resultset;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public interface ResultSetColumns extends BiConsumer<Columns.Column, Object> {

    Map<String, Columns.Column> getColumns();

    default void accept(Columns.Column column, Object columnValue) {
        Columns.Column columnClone = column.clone();
        columnClone.setValue(columnValue);
        getColumns().put(columnClone.getColumn(), columnClone);
    }

    default List<Columns.PkColumn> pks() {
        return getColumns().values().stream()
                .filter(col -> col.kind().equals(Columns.Column.Kind.PK))
                .map(col -> (Columns.PkColumn) col)
                .collect(Collectors.toList());
    }

    default List<Columns.FkColumn> fks() {
        return getColumns().values().stream()
                .filter(col -> col.kind().equals(Columns.Column.Kind.FK))
                .map(col -> (Columns.FkColumn) col)
                .collect(Collectors.toList());
    }

    default List<Columns.TblColumn> cols() {
        return getColumns().values().stream()
                .filter(col -> !col.kind().equals(Columns.Column.Kind.FK) &&
                        !col.kind().equals(Columns.Column.Kind.PK))
                .map(col -> (Columns.TblColumn) col)
                .collect(Collectors.toList());
    }

    default QueryInfo fkQueryInfo(String fkTable, String refTable, String[] fkColumns) {
        final QueryInfo queryInfo = new QueryInfo(fkColumns);
        List<String> columnNames = Arrays.asList(fkColumns);
        List<Columns.FkColumn> columns = fks().stream().filter(
                col -> columnNames.contains(col.getFkColumn())
        ).collect(Collectors.toList());

        if (getColumns().isEmpty()) {
            return queryInfo;
        }

        StringBuilder innerJoin = columns.stream().reduce(new StringBuilder("inner join ").append(fkTable).append(" fk on "), (bld, col) -> {
            bld.append("fk.").append(col.getFkColumn()).append(" = ").append("ref.").append(col.getRefColumn()).append(" and ");
            return bld;
        }, (x, y) -> x);

        StringBuilder where = columns.stream().reduce(new StringBuilder("where "), (bld, col) -> {
            bld.append("ref.").append(col.getRefColumn()).append(" = ?").append(" and ");
            queryInfo.params.add(col.getValue());
            return bld;
        }, (x, y) -> x);

        queryInfo.query = String.format("select fk.*, ref.* from %s ref %s %s",
                refTable,
                innerJoin.delete(innerJoin.length() - 5, innerJoin.length()),
                where.delete(where.length() - 5, where.length()));

        return queryInfo;
    }

    default List<QueryInfo> fkQueries() {
        Map<String, List<Columns.Column>> fksMap = this.fks().stream().reduce(new HashMap<>(),
                (map, col) -> {
                    if (map.containsKey(col.getFkName())) {
                        map.get(col.getFkName()).add(col);
                    } else {
                        List<Columns.Column> cols = new ArrayList<>();
                        cols.add(col);
                        map.put(col.getFkName(), cols);
                    }
                    return map;
                },
                (x, y) -> null);

        return fksMap.values().stream().map(list -> {
            String[] fks = list.stream().map(Columns.Column::getColumn).toArray(String[]::new);
            Columns.FkColumn fkCol = (Columns.FkColumn) list.get(0);
            return fkQueryInfo(fkCol.getFkTable(), fkCol.getRefTable(), fks);
        }).collect(Collectors.toList());
    }

    default Optional<QueryInfo> fkQueryInfo(String[] fks) {
        return this.fkQueries().stream()
                .filter(qry -> Arrays.stream(qry.fks).allMatch(col -> {
                    for (String fk : fks) {
                        if (!fk.equalsIgnoreCase(col)) {
                            return false;
                        }
                    }
                    return true;
                }))
                .findFirst();
    }
}