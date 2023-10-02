package works.hop.examples;

import works.hop.entity.EntityNode;
import works.hop.entity.EntityNodeFactory;
import works.hop.entity.PropertyNode;

public class Venue extends EntityNode {

    public Venue() {
        super("tbl_tour_city");
        initialize();
    }

    @Override
    public void initialize() {
        this.put("city", new PropertyNode("city"));
        this.put("state", new PropertyNode("state"));
        this.put("address", new PropertyNode("address", "location"));
        this.put("capacity", new PropertyNode("capacity"));
        //register if not already registered
        EntityNodeFactory.register(Venue.class, Venue::new);
    }
}
