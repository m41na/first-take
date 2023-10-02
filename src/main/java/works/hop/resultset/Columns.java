package works.hop.resultset;

import lombok.*;

@Getter
public class Columns {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static abstract class Column implements Cloneable {

        private String table;
        private String column;
        private String type;
        private Object value; //TODO: can this be removed, Clonable removed, and then use PropertyNode to hold the column value?

        public abstract Kind kind();

        @Override
        public Column clone() {
            try {
                Column clone = (Column) super.clone();
                clone.value = null;
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }

        public enum Kind {BASIC, PK, FK}
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class TblColumn extends Column {

        @Override
        public Kind kind() {
            return Kind.BASIC;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class PkColumn extends Column {

        private String pkName;

        public PkColumn(String pkName, Column basic) {
            super(basic.table, basic.column, basic.type, basic.value);
            this.pkName = pkName;
        }

        @Override
        public Kind kind() {
            return Kind.PK;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class FkColumn extends Column {

        private String fkName;
        private String fkTable;
        private String fkColumn;
        private String refTable;
        private String refColumn;

        public FkColumn(String fkName, String fkTable, String fkColumn, String refTable, String refColumn, Column basic) {
            super(basic.table, basic.column, basic.type, basic.value);
            this.fkName = fkName;
            this.fkTable = fkTable;
            this.fkColumn = fkColumn;
            this.refTable = refTable;
            this.refColumn = refColumn;
        }

        @Override
        public Kind kind() {
            return Kind.FK;
        }
    }
}