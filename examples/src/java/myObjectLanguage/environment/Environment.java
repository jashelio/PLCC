package myObjectLanguage.environment;

import myObjectLanguage.value.Value;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Environment {
    public static final Environment NULL = new Environment();
    private final Environment parent;

    private Environment() {
        parent = null;
        noNewBindings = true;
    }

    protected Environment(Environment parent) {
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
        throw new RuntimeException("Can't set a binding which hasn't already " +
                "been defined (using a let expression) in the current environment");
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

    private static Set<Environment> processing = new HashSet<>();
    @Override
    public String toString() {
        boolean isHead = processing.size() == 0;
        if(!processing.add(this))
            return "";
        StringBuilder sb = new StringBuilder();
        if (this == NULL)
            sb.append("NULL");
        for (Environment currentEnvironment = this;
             currentEnvironment != Environment.NULL;
             currentEnvironment = currentEnvironment.parent)
            sb.append('\n').append(currentEnvironment.bindings);
        if (isHead)
            processing.clear();
        return sb.substring(1);
    }

    public Environment getParent() {
        return parent;
    }
}
