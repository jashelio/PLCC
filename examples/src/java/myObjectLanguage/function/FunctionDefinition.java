package myObjectLanguage.function;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Token;
import myObjectLanguage.environment.Environment;
import myObjectLanguage.expression.Expression;
import myObjectLanguage.value.FunctionValue;
import myObjectLanguage.value.Value;

@GrammarRule
public class FunctionDefinition extends Expression {
    @Token
    public static final String FUNCTION = "function";

    private String name;
    private FunctionParameters parameters;
    private FunctionBody body;

    public FunctionDefinition(String function, String optionalVar, FunctionParameters parameters, FunctionBody body) {
        name = optionalVar;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public Value evaluate(Environment environment) {
        Value value = new FunctionValue(parameters, body, environment);
        if (name.isEmpty())
            return value;
        environment.setNewBinding(name, value);
        return environment.set(name, value);
    }

    @Override
    public String toString() {
        return FUNCTION + " " + name + parameters + " " + body;
    }
}
