package myObjectLanguage.environment;

import myObjectLanguage.object.ObjectFieldAccessibility;
import myObjectLanguage.value.Value;

import java.util.HashMap;
import java.util.Map;

public class ObjectEnvironment extends Environment {
    private Map<String, ObjectFieldAccessibility> accessibilityMap = new HashMap<>();
    public ObjectEnvironment(Environment parent) {
        super(parent);
    }

    @Override
    public ObjectEnvironment extendEnvironment() {
        return new ObjectEnvironment(super.extendEnvironment());
    }

    @Override
    public void setNoNewBindings() {
        super.setNoNewBindings();
    }

    @Override
    public Value set(String name, Value value) {
        return super.set(name, value);
    }

    @Override
    public void addBinding(String name) {
        super.addBinding(name);
    }

    @Override
    public Value setNewBinding(String name, Value value) {
        return super.setNewBinding(name, value);
    }

    @Override
    public Value get(String name) {
        return super.get(name);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
