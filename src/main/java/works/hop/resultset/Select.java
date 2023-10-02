package works.hop.resultset;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import works.hop.entity.EntityNode;
import works.hop.json.JNodeParser;
import works.hop.json.NodeValue;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class Select {

    final Repository repository;

    private JNodeParser.JObject expectOne(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        if (rs.next()) {
            return mapRow(meta, rs);
        }
        return null;
    }

    private List<JNodeParser.JObject> expectMany(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        List<JNodeParser.JObject> resultList = new LinkedList<>();
        while (rs.next()) {
            JNodeParser.JObject result = mapRow(meta, rs);
            resultList.add(result);
        }
        return resultList;
    }

    private JNodeParser.JObject mapRow(ResultSetMetaData meta, ResultSet rs) throws SQLException {
        JNodeParser.JObject result = new JNodeParser.JObject();
        for (int column = 1; column <= meta.getColumnCount(); column++) {
            result.put(meta.getColumnName(column), new NodeValue<>(rs.getObject(column)));
        }
        return result;
    }

    public String createSelectQuery(EntityNode target, Map<String, Object> params) {
        List<String> columnNames = target.values().stream()
                .flatMap(col -> Arrays.stream(col.getColumns()))
                .collect(Collectors.toList());
        StringBuilder queryBld = new StringBuilder("select ");
        columnNames.forEach(col -> queryBld.append("A.").append(col).append(","));
        queryBld.deleteCharAt(queryBld.length() - 1).append(" from ").append(target.getTableName()).append(" A");
        if (!params.isEmpty()) {
            queryBld.append(" where ");
            params.forEach((key, value) -> queryBld.append("A.").append(key).append(" = ?").append(" and "));
        }
        queryBld.delete(queryBld.length() - 5, queryBld.length());
        return queryBld.toString();
    }

    private ResultSet executeSelectStatement(Connection conn, String query, Object[] params) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            pst.setObject(i + 1, params[i]);
        }
        return pst.executeQuery();
    }

    private ResultSet executeSelectStatement(Connection conn, String query, Map<String, Object> params) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(query);
        int i = 0;
        for (Map.Entry<String, Object> entries : params.entrySet()) {
            pst.setObject(++i, entries.getValue());
        }
        return pst.executeQuery();
    }

    public void queryOne(EntityNode target, Map<String, Object> params) throws SQLException {
        try (Connection conn = repository.getConnection()) {
            queryOne(conn, target, params);
        }
    }

    public void queryOne(Connection conn, EntityNode target, Map<String, Object> params) throws SQLException {
        String query = createSelectQuery(target, params);
        ResultSet rs = executeSelectStatement(conn, query, params);
        if (rs.next()) {
            ResultSetVisitor visitor = new ResultSetVisitor();
            visitor.register(target);
            cacheResultSetMetadata(conn, target.getTableName(), rs, visitor);
            target.resolve(conn, this);
        }
    }

    public void query(Supplier<EntityNode> supplier, Map<String, Object> params, Consumer<List<EntityNode>> consumer) throws SQLException {
        EntityNode target = supplier.get();
        String query = createSelectQuery(target, params);
        try (Connection conn = repository.getConnection()) {
            ResultSet rs = executeSelectStatement(conn, query, params);
            List<EntityNode> entities = new LinkedList<>();
            while (rs.next()) {
                EntityNode entity = supplier.get();
                ResultSetVisitor visitor = new ResultSetVisitor();
                visitor.register(entity);
                cacheResultSetMetadata(conn, entity.getTableName(), rs, visitor);
                entity.resolve(conn, this);
                entities.add(entity);
            }
            //send to consumer
            consumer.accept(entities);
        }
    }

    private void cacheResultSetMetadata(Connection conn, String tableName, ResultSet resultSet, ResultSetVisitor visitor) throws SQLException {
        String tableNameKey = tableName.toUpperCase();
        if (!repository.getRsMetadataCache().containsKey(tableNameKey)) {
            Map<String, Columns.Column> metadata = new LinkedHashMap<>();
            visitor.metadata(tableName.toUpperCase(), conn.getMetaData(), metadata); //inspect db table
            repository.getRsMetadataCache().put(tableNameKey, metadata);
        }
        visitor.visit(resultSet, repository.getRsMetadataCache().get(tableNameKey)); //inflate rs data into result-set
    }

    public JNodeParser.JObject selectOne(String query, Object[] params) throws SQLException {
        try (Connection conn = repository.getConnection()) {
            ResultSet rs = executeSelectStatement(conn, query, params);
            return expectOne(rs);
        }
    }

    public List<JNodeParser.JObject> select(String query, Object[] params) throws SQLException {
        try (Connection conn = repository.getConnection()) {
            ResultSet rs = executeSelectStatement(conn, query, params);
            return expectMany(rs);
        }
    }
}
