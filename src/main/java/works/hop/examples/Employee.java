package works.hop.examples;

import works.hop.entity.EntityNode;
import works.hop.entity.EntityNodeFactory;
import works.hop.entity.FkPropertyNode;
import works.hop.entity.PropertyNode;

public class Employee extends EntityNode {

    public Employee() {
        super("tbl_employee");
        initialize();
    }

    @Override
    public void initialize() {
        this.put("id", new PropertyNode("id", "id", null));
        this.put("first_name", new PropertyNode("firstName", "first_name", null));
        this.put("last_name", new PropertyNode("firstName", "last_name", null));
        this.put("email_addr", new PropertyNode("emailAddr", "email_addr", null));
        this.put("manager", new FkPropertyNode("manager", "manager") {

            @Override
            public String[] refColumns() {
                return new String[]{"id"};
            }

            @Override
            public EntityNode refEntity() {
                return new Employee();
            }
        });
        //register if not already registered
        EntityNodeFactory.register(Employee.class, Employee::new);
    }
}
