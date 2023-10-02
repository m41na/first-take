package works.hop.dml;

import org.junit.jupiter.api.Test;
import works.hop.entity.EntityNodeFactory;
import works.hop.examples.Venue;
import works.hop.json.JNodeParser;
import works.hop.json.NodeValue;
import works.hop.resultset.Repository;

import java.sql.SQLException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class InsertTest {

    Insert insert = new Insert(Repository.getInstance());

    @Test
    void createInsertQuery() throws SQLException {
        JNodeParser.JObject performance = new JNodeParser.JObject();
        performance.put("artist", new NodeValue<>("John Bosch"));
        performance.put("dateTime", new NodeValue<>(new Date()));
        //create with foreign key entity
        JNodeParser.JObject venue = new JNodeParser.JObject();
        venue.put("city", new NodeValue<>("New York"));
        venue.put("state", new NodeValue<>("NY"));
        venue.put("location", new NodeValue<>("Metlife Stadium"));
        //associate with primary entity
        performance.put("venue", new NodeValue<>(venue));

        int affected = insert.insert(new Venue(), performance);
        assertThat(affected).isEqualTo(1);
    }
}