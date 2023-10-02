package works.hop.entity;

import lombok.Getter;
import lombok.Setter;
import works.hop.json.NodeValue;
import works.hop.resultset.Columns;

import java.util.Arrays;

@Getter
@Setter
public class PropertyNode extends NodeValue<Object> {

    String[] columns;
    String property;

    public PropertyNode(String property) {
        this(property, property, null);
    }

    public PropertyNode(String property, String column) {
        this(property, column, null);
    }

    public PropertyNode(String property, String[] columns) {
        this(property, columns, null);
    }

    public PropertyNode(String property, String column, Object value) {
        this(property, new String[]{column}, value);
    }

    public PropertyNode(String property, String[] columns, Object value) {
        super(value);
        this.property = property;
        this.columns = columns;
    }

    public Columns.Column.Kind kind() {
        return Columns.Column.Kind.BASIC;
    }

    @Override
    public String toString() {
        return String.format("%s[%s] -> %s", getProperty(), Arrays.toString(getColumns()), getValue());
    }
}
