package myObjectLanguage.value;

import myObjectLanguage.environment.Environment;

public class EnvironmentValue extends Value {
    private Environment environment;

    public EnvironmentValue(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public String toString() {
        return environment.toString();
    }
}
