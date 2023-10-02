package works.hop.dml;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import works.hop.entity.EntityNode;
import works.hop.json.JNodeParser;
import works.hop.resultset.Repository;
import works.hop.resultset.ResultSetVisitor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Considerations
 * 1. How is the primary key generated - manual, sequence, random
 * 2. Is the foreign entity associated with using a single or multiple fk keys
 * 3.
 */
@Getter
@RequiredArgsConstructor
public class Insert {

    final Repository repository;

    public int insert(EntityNode target, JNodeParser.JObject params) throws SQLException {
        try (Connection conn = repository.getConnection()) {
            String query = createInsertQuery(target, params);
            int affected = executeInsertStatement(conn, query, params);
            ResultSetVisitor visitor = new ResultSetVisitor();
            visitor.register(target);
//                cacheResultSetMetadata(conn, target.getTableName(), rs, visitor);
//            target.resolve(conn);
            return affected;
        }
    }

    private int executeInsertStatement(Connection conn, String query, JNodeParser.JObject params) {
        return 0;
    }

    public String createInsertQuery(EntityNode target, JNodeParser.JObject params) {
        List<String> columnNames = target.values().stream()
                .flatMap(col -> Arrays.stream(col.getColumns()))
                .collect(Collectors.toList());
        StringBuilder queryBld = new StringBuilder("insert into ");
        queryBld.append(target.getTableName()).append(" A (");
        columnNames.forEach(col -> queryBld.append(col).append(","));
        queryBld.deleteCharAt(queryBld.length() - 1).append(") values (");
        IntStream.range(0, columnNames.size()).forEach(i -> {
            queryBld.append("?").append(",");
            String col = columnNames.get(i);
//            params.put(col, target.get(col).getValue());
        });
        queryBld.deleteCharAt(queryBld.length() - 1).append(")");
        return queryBld.toString();
    }
}
