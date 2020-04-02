package myObjectLanguage.value;

public class IntegerValue extends Value {
    private Integer integer;
    public IntegerValue(Integer integer) {
        this.integer = integer;
    }

    @Override
    public Integer integerValue() {
        return integer;
    }

    @Override
    public String toString() {
        return integer.toString();
    }
}
