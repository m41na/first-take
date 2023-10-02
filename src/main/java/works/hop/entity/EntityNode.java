package works.hop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import works.hop.resultset.Columns;
import works.hop.resultset.ResultSetColumns;
import works.hop.resultset.Select;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public abstract class EntityNode extends LinkedHashMap<String, PropertyNode> implements ResultSetColumns {

    final String tableName;
    final Map<String, Columns.Column> columns = new HashMap<>();

    @Override
    public Map<String, Columns.Column> getColumns() {
        return this.columns;
    }

    public abstract void initialize();

    public void resolve(Connection conn, Select selector) throws SQLException {
        Map<String, Columns.Column> fkColumns = columns.values().stream()
                .filter(col -> col.kind().equals(Columns.Column.Kind.FK))
                .collect(Collectors.toMap(Columns.Column::getColumn, col -> col));

        for (String property : keySet()) {
            PropertyNode propertyNode = get(property);
            if (propertyNode.getColumns().length == 1) {
                Columns.Column columnData = columns.getOrDefault(propertyNode.getColumns()[0].toUpperCase(),
                        columns.get(propertyNode.getColumns()[0].toLowerCase()));
                if (columnData.kind().equals(Columns.Column.Kind.FK)) {
                    EntityNode fkNode = ((FkPropertyNode) propertyNode).refEntity();
                    if (columnData.getValue() != null) {
                        String refColumn = ((Columns.FkColumn) columnData).getRefColumn();
                        selector.queryOne(conn, fkNode, Map.of(refColumn, columnData.getValue()));
                        //set property value
                        propertyNode.setValue(fkNode);
                    }
                } else {
                    //set property value
                    propertyNode.setValue(columnData.getValue());
                }
            } else {
                // MUST be having fk relationship
                String[] columns = propertyNode.getColumns();
                Map<String, Object> params = Arrays.stream(columns)
                        .map(colName -> fkColumns.getOrDefault(colName.toUpperCase(), fkColumns.get(colName.toLowerCase())))
                        .collect(Collectors.toMap(Columns.Column::getColumn, Columns.Column::getValue));
                EntityNode fkNode = ((FkPropertyNode) propertyNode).refEntity();
                selector.queryOne(conn, fkNode, params);
                //set property value
                propertyNode.setValue(fkNode);
            }
        }
    }
}
