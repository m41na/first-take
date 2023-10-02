package works.hop.resultset;

class SelectTest {

//    Select select = new Select(Repository.getInstance());

//    @Test
//    void verify_single_result_fk_query_retrieves_one_item() throws SQLException {
//        String searchEmail = "brian.kent@email.com";
//        String query = "select e.* from tbl_employee e where e.email_addr = ?";
//        List<ResultSetData> result = select.query(query, new Object[]{searchEmail}, "tbl_employee");
//        assertThat(result).hasSize(1);
//
//        ResultSetData data = result.get(0);
//        Optional<ColumnData.TblColumn> emailOptional = data.cols().stream().filter(col -> col.getColumn().equalsIgnoreCase("email_addr")).findFirst();
//        assertThat(emailOptional).isPresent();
//        assertThat(emailOptional.get().getValue()).isEqualTo(searchEmail);
//    }

//    @Test
//    void verify_multiple_result_fk_query_retrieves_two_items() throws SQLException {
//        String searchEmail = "brian.kent@email.com";
//        String query = "select fk.* from tbl_employee fk inner join tbl_employee ref on fk.manager = ref.id where ref.id = " +
//                "(select e.id from tbl_employee e where e.email_addr = ?)";
//        List<ResultSetData> result = select.query(query, new Object[]{searchEmail}, "tbl_employee");
//        assertThat(result).hasSize(2);
//
//        ResultSetData first = result.get(0);
//        Optional<ColumnData.TblColumn> firstEmail = first.cols().stream().filter(col -> col.getColumn().equalsIgnoreCase("email_addr")).findFirst();
//        assertThat(firstEmail).isPresent();
//        assertThat(firstEmail.get().getValue()).isEqualTo("donni.torn@email.com");
//
//        ResultSetData second = result.get(1);
//        Optional<ColumnData.TblColumn> secondEmail = second.cols().stream().filter(col -> col.getColumn().equalsIgnoreCase("email_addr")).findFirst();
//        assertThat(secondEmail).isPresent();
//        assertThat(secondEmail.get().getValue()).isEqualTo("festus.mare@email.com");
//    }

//    @Test
//    void verify_fk_query_is_fetch_based_on_provided_fk_column_names() throws SQLException {
//        String searchEmail = "brian.kent@email.com";
//        String query = "select e.* from tbl_employee e where e.email_addr = ?";
//        List<ResultSetData> result = select.query(query, new Object[]{searchEmail}, "tbl_employee");
//        assertThat(result).hasSize(1);
//
//        ResultSetData first = result.get(0);
//        Optional<ColumnData.TblColumn> firstEmail = first.cols().stream().filter(col -> col.getColumn().equalsIgnoreCase("email_addr")).findFirst();
//        assertThat(firstEmail).isPresent();
//
//        Optional<QueryInfo> queryInfo = first.fkQueryInfo(new String[]{"manager"});
//        assertThat(queryInfo).isPresent();
//
//        //TODO: using wildcard for column names gets columns with similar names not to get picked up in result-set
//        List<ResultSetData> result2 = select.query(queryInfo.get().query, queryInfo.get().params.toArray(new Object[0]), "tbl_employee");
//        assertThat(result2).hasSize(2);
//
//        ResultSetData second = result2.get(0);
//        Optional<ColumnData.TblColumn> secondEmail = second.cols().stream().filter(col -> col.getColumn().equalsIgnoreCase("email_addr")).findFirst();
//        assertThat(secondEmail).isPresent();
//    }
}