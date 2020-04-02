package myObjectLanguage.expression;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Token;
import myObjectLanguage.environment.Environment;
import myObjectLanguage.value.EnvironmentValue;
import myObjectLanguage.value.Value;

@GrammarRule
public class ThisExpression extends Expression {
    @Token
    public static final String _THIS = "this";

    public ThisExpression(String _this) {}

    @Override
    public Value evaluate(Environment environment) {
        return new EnvironmentValue(environment);
    }

    @Override
    public String toString() {
        return "this";
    }
}
