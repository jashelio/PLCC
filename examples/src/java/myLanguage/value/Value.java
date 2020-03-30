package myLanguage.value;

public class Value {
    public static final Value NULL = new Value() {
        @Override
        public String toString() {
            return "NULL";
        }
    };

    public Value apply(Value[] args) {
        throw new RuntimeException(getClass() + " is not a myLanguage.function");
    }

    public String stringValue() {
        throw new RuntimeException();
    }

    public Integer integerValue() {
        throw new RuntimeException();
    }

    public Double doubleValue() {
        throw new RuntimeException();
    }

    public Boolean booleanValue() {
        throw new RuntimeException();
    }

    public Character characterValue() {
        throw new RuntimeException();
    }
}
