package works.hop.resultset;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public class ResultSetData implements ResultSetColumns {

    final Map<String, Columns.Column> columns = new HashMap<>();

    @Override
    public Map<String, Columns.Column> getColumns() {
        return this.columns;
    }
}