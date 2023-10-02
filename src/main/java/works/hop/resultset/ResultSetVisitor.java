package works.hop.resultset;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.BiConsumer;

public class ResultSetVisitor {

    private BiConsumer<Columns.Column, Object> consumer;

    public void register(BiConsumer<Columns.Column, Object> consumer) {
        this.consumer = consumer;
    }

    public void metadata(String tableName, DatabaseMetaData databaseMetaData, Map<String, Columns.Column> columnsInfo) throws SQLException {
        try (ResultSet tblColumns = databaseMetaData.getColumns(null, null, tableName, null)) {
            while (tblColumns.next()) {
                Columns.Column columnInfo = new Columns.TblColumn();
                columnInfo.setTable(tableName);
                //add column attributes
                String columnName = tblColumns.getString("COLUMN_NAME");
                columnInfo.setColumn(columnName);
                String datatype = tblColumns.getString("TYPE_NAME");
                columnInfo.setType(datatype);
                //cache column in map
                columnsInfo.put(columnName, columnInfo);
            }
        }

        try (ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(null, null, tableName)) {
            while (primaryKeys.next()) {
                String pkColumnName = primaryKeys.getString("COLUMN_NAME");
                String pkName = primaryKeys.getString("PK_NAME");
                if (columnsInfo.get(pkColumnName) != null) {
                    Columns.Column col = columnsInfo.get(pkColumnName);
                    Columns.Column pkColumn = new Columns.PkColumn(pkName, col);
                    //update column in cache
                    columnsInfo.put(pkColumnName, pkColumn);
                }
            }
        }

        try (ResultSet foreignKeys = databaseMetaData.getImportedKeys(null, null, tableName)) {
            while (foreignKeys.next()) {
                String pkTableName = foreignKeys.getString("PKTABLE_NAME");
                String fkTableName = foreignKeys.getString("FKTABLE_NAME");
                //expect fkTableName to equal tableName
                assert fkTableName.equals(tableName);
                String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
                String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
                String fkName = foreignKeys.getString("FK_NAME");
                if (columnsInfo.get(fkColumnName) != null) {
                    Columns.Column col = columnsInfo.get(fkColumnName);
                    Columns.Column fkColumn = new Columns.FkColumn(fkName, fkTableName, fkColumnName, pkTableName, pkColumnName, col);
                    //update column in cache
                    columnsInfo.put(fkColumnName, fkColumn);
                }
            }
        }
    }

    public void visit(ResultSet rs, Map<String, Columns.Column> columns) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int width = meta.getColumnCount();
        for (int i = 1; i <= width; i++) {
            String columnName = meta.getColumnName(i);
            Columns.Column tblColumn = columns.get(columnName);
            // retrieve column value from rs
            Object columnValue = rs.getObject(i);
            //notify whoever is listening
            consumer.accept(tblColumn, columnValue);
        }
    }
}
