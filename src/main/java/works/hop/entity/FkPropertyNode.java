package works.hop.entity;

import lombok.Getter;
import lombok.Setter;
import works.hop.resultset.Columns;

@Getter
@Setter
public abstract class FkPropertyNode extends PropertyNode {

    public FkPropertyNode(String property) {
        super(property);
    }

    public FkPropertyNode(String property, String column) {
        super(property, column);
    }

    public FkPropertyNode(String property, String[] columns) {
        super(property, columns);
    }

    public FkPropertyNode(String property, String column, Object value) {
        super(property, column, value);
    }

    public FkPropertyNode(String property, String[] columns, Object value) {
        super(property, columns, value);
    }

    @Override
    public Columns.Column.Kind kind() {
        return Columns.Column.Kind.FK;
    }

    public abstract EntityNode refEntity();

    public abstract String[] refColumns();
}
