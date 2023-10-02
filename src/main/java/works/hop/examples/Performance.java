package works.hop.examples;

import works.hop.entity.EntityNode;
import works.hop.entity.EntityNodeFactory;
import works.hop.entity.FkPropertyNode;
import works.hop.entity.PropertyNode;

public class Performance extends EntityNode {

    public Performance() {
        super("tbl_performance");
        initialize();
    }

    @Override
    public void initialize() {
        this.put("id", new PropertyNode("id", "tour_id", null));
        this.put("artist", new PropertyNode("artist"));
        this.put("dateTime", new PropertyNode("dateTime", "scheduled_date"));
        this.put("venue", new FkPropertyNode("venue", new String[]{"city", "state"}) {
            @Override
            public String[] refColumns() {
                return new String[]{"city", "state"};
            }

            @Override
            public EntityNode refEntity() {
                return new Venue();
            }
        });
        //register if not already registered
        EntityNodeFactory.register(Performance.class, Performance::new);
    }
}
