package works.hop.resultset;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultSetDataTest {

    Columns.Column managerColumn = generateFkColumn("fk_manager", "tbl_employee", "manager", "tbl_employee", "id", "Integer");
    Columns.Column empIdColumn = generatePkColumn("employee_pk", "tbl_employee", "id", "Integer");
    Columns.Column cityColumn = generatePkColumn("pk_tour_city", "tbl_tour_city", "city", "String");
    Columns.Column stateColumn = generatePkColumn("pk_tour_city", "tbl_tour_city", "state", "String");
    Columns.Column perfCityColumn = generateFkColumn("fk_perf_city", "tbl_performance", "city", "tbl_city", "city", "String");
    Columns.Column perfStateColumn = generateFkColumn("fk_perf_city", "tbl_performance", "state", "tbl_state", "state", "String");


    Columns.FkColumn generateFkColumn(String fkName, String fkTable, String fkColumn, String refTable, String refColumn, String columnType) {
        return new Columns.FkColumn(fkName, fkTable, fkColumn, refTable, refColumn, new Columns.Column() {
            @Override
            public String getTable() {
                return fkTable;
            }

            @Override
            public String getColumn() {
                return fkColumn;
            }

            @Override
            public String getType() {
                return columnType;
            }

            @Override
            public Kind kind() {
                return Kind.BASIC;
            }
        });
    }

    Columns.Column generatePkColumn(String pkName, String pkTable, String pkColumn, String columnType) {
        return new Columns.PkColumn(pkName, new Columns.Column() {
            @Override
            public String getTable() {
                return pkTable;
            }

            @Override
            public String getColumn() {
                return pkColumn;
            }

            @Override
            public String getType() {
                return columnType;
            }

            @Override
            public Kind kind() {
                return Kind.BASIC;
            }
        });
    }

    @Test
    void verify_single_fk_query_is_generated_correctly() {
        ResultSetData row = new ResultSetData();
        managerColumn.setValue(1);
        row.columns.put("manager", managerColumn);
        empIdColumn.setValue(2);
        row.columns.put("id", empIdColumn);
        QueryInfo fkQuery = row.fkQueryInfo("tbl_employee", "tbl_employee", new String[]{"manager"});
        assertThat(fkQuery.query).isEqualTo("select fk.*, ref.* from tbl_employee ref inner join tbl_employee fk on fk.manager = ref.id where ref.id = ?");
        assertThat(fkQuery.params).hasSize(1);
        assertThat(fkQuery.params.get(0)).isEqualTo(1);
    }

    @Test
    void verify_multiple_fk_query_is_generated_correctly() {
        ResultSetData row = new ResultSetData();

        cityColumn.setValue("Madison");
        row.columns.put("city", cityColumn);
        stateColumn.setValue("WI");
        row.columns.put("state", stateColumn);

        perfCityColumn.setValue("Madison");
        row.columns.put("city", perfCityColumn);
        perfStateColumn.setValue("WI");
        row.columns.put("state", perfStateColumn);

        QueryInfo fkQuery = row.fkQueryInfo("tbl_performance", "tbl_tour_city", new String[]{"city", "state"});
        assertThat(fkQuery.query).isEqualTo("select fk.*, ref.* from tbl_tour_city ref inner join tbl_performance fk on fk.city = ref.city and fk.state = ref.state where ref.city = ? and ref.state = ?");
        assertThat(fkQuery.params).hasSize(2);
        assertThat(fkQuery.params.get(0)).isEqualTo("Madison");
        assertThat(fkQuery.params.get(1)).isEqualTo("WI");
    }
}