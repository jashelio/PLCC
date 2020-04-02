package myObjectLanguage.value;

import myObjectLanguage.environment.Environment;
import myObjectLanguage.environment.ObjectEnvironment;

public class ObjectValue extends EnvironmentValue {
    public ObjectValue(Environment environment) {
        super(new ObjectEnvironment(environment));
    }

    @Override
    public ObjectEnvironment getEnvironment() {
        return (ObjectEnvironment)super.getEnvironment();
    }
}
