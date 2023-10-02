package works.hop.resultset;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import works.hop.examples.Employee;
import works.hop.examples.Performance;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Repository {

    private static Repository instance;
    @Getter
    private final Map<String, Map<String, Columns.Column>> rsMetadataCache = new HashMap<>();
    private final HikariDataSource ds;

    private Repository() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:tcp://localhost/~/.data/todos-db");
        config.setDriverClassName("org.h2.Driver");
        config.setUsername("sa");
        config.setPassword("sa");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    public static void main(String[] args) throws SQLException {
        Select selector = new Select(Repository.getInstance());

        System.out.println(selector.selectOne("select * from tbl_todos where completed = ? limit 1", new Object[]{true}));
        System.out.println(selector.select("select * from tbl_todos where completed = ?", new Object[]{false}));

        Employee employee = new Employee();
        selector.queryOne(employee, Map.of("id", UUID.fromString("a1a4c2d5-ac7c-46c8-bb93-00e5ff7c53a0")));
        System.out.println(employee);

        Performance performance = new Performance();
        selector.queryOne(performance, Map.of("tour_id", UUID.fromString("895daeae-7d36-4951-8713-a9facc7fec13")));
        System.out.println(performance);

        selector.query(Employee::new,
                Map.of("manager", UUID.fromString("90ba272e-7a27-4130-b8a7-73a5f3d5de3e")),
                (employees) -> employees.forEach(System.out::println));

        selector.query(Performance::new,
                Map.of("city", "Chicago", "state", "IL"),
                (venues) -> venues.forEach(System.out::println));
    }

    public static Repository getInstance() {
        synchronized (Repository.class) {
            if (instance == null) {
                instance = new Repository();
            }
            return instance;
        }
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}