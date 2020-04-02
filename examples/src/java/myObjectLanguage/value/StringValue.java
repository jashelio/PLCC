package myObjectLanguage.value;

public class StringValue extends Value {
    private String string;
    public StringValue(String string) {
        this.string = string;
    }

    @Override
    public String stringValue() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }
}
