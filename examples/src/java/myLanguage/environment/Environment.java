package myLanguage.environment;

import myLanguage.value.Value;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    public static final Environment NULL = new Environment();
    private final Environment parent;

    private Environment() {
        parent = null;
        noNewBindings = true;
    }

    private Environment(Environment parent) {
        this.parent = parent;
        noNewBindings = false;
    }

    private Map<String, Value> bindings = new HashMap<>();
    private boolean noNewBindings;

    public Environment extendEnvironment() {
        return new Environment(this);
    }

    public void setNoNewBindings() {
        noNewBindings = true;
    }

    public boolean contains(String name) {
        for (Environment currentEnvironment = this;
             currentEnvironment != Environment.NULL;
             currentEnvironment = currentEnvironment.parent)
            if (currentEnvironment.bindings.containsKey(name))
                return true;
        return false;
    }

    public Value set(String name, Value value) {
        for (Environment currentEnvironment = this;
             currentEnvironment != Environment.NULL;
             currentEnvironment = currentEnvironment.parent)
            if (currentEnvironment.bindings.containsKey(name)) {
                currentEnvironment.bindings.put(name, value);
                return value;
            }
        return Value.NULL;
    }

    public void addBinding(String name) {
        if (noNewBindings)
            return;
        bindings.put(name, Value.NULL);
    }

    public Value setNewBinding(String name, Value value) {
        if (noNewBindings)
            return Value.NULL;
        bindings.put(name, value);
        return value;
    }

    public Value get(String name) {
        for (Environment currentEnvironment = this;
             currentEnvironment != Environment.NULL;
             currentEnvironment = currentEnvironment.parent)
            if (currentEnvironment.bindings.containsKey(name))
                return currentEnvironment.bindings.get(name);
        return Value.NULL;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this == NULL)
            sb.append("NULL");
        for (Environment currentEnvironment = this;
             currentEnvironment != Environment.NULL;
             currentEnvironment = currentEnvironment.parent)
            sb.append(currentEnvironment.bindings);
        return sb.toString();
    }
}
