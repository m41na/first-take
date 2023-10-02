package works.hop.resultset;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class QueryInfo {

    final String[] fks;
    final List<Object> params = new LinkedList<>();
    String query;
}
