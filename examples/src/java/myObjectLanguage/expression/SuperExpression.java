package myObjectLanguage.expression;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Token;
import myObjectLanguage.environment.Environment;
import myObjectLanguage.value.EnvironmentValue;
import myObjectLanguage.value.Value;

@GrammarRule
public class SuperExpression extends Expression {
    @Token
    public static final String _SUPER = "super";

    public SuperExpression(String _super) {}

    @Override
    public Value evaluate(Environment environment) {
        return new EnvironmentValue(environment.getParent());
    }

    @Override
    public String toString() {
        return "super";
    }
}
