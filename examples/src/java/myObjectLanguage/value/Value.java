package myObjectLanguage.value;

import myObjectLanguage.environment.Environment;

public class Value {
    public static final Value NULL = new Value() {
        @Override
        public String toString() {
            return "NULL";
        }
    };

    public Value apply(Value[] args) {
        throw new RuntimeException(getClass() + " is not a function");
    }

    public String stringValue() {
        throw new RuntimeException(getClass() + " is not a string");
    }

    public Integer integerValue() {
        throw new RuntimeException(getClass() + " is not a integer");
    }

    public Double doubleValue() {
        throw new RuntimeException(getClass() + " is not a double");
    }

    public Boolean booleanValue() {
        throw new RuntimeException(getClass() + " is not a boolean");
    }

    public Character characterValue() {
        throw new RuntimeException(getClass() + " is not a character");
    }

    public Environment getEnvironment() {
        throw new RuntimeException(getClass() + " is not an environment");
    }
}
