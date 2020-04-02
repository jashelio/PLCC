package myObjectLanguage.expression.literal;

import myObjectLanguage.value.Value;

class Literal {
    private final Value value;

    protected Literal(Value value) {
        this.value = value;
    }

    public Value getValue() {
        return value;
    }
}
