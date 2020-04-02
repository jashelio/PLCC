package myObjectLanguage.value;

public class DoubleValue extends Value {
    private Double aDouble;
    public DoubleValue(Double aDouble) {
        this.aDouble = aDouble;
    }

    @Override
    public Double doubleValue() {
        return aDouble;
    }

    @Override
    public String toString() {
        return aDouble.toString();
    }
}
