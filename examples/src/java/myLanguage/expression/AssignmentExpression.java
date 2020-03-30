package myLanguage.expression;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Token;
import myLanguage.environment.Environment;
import myLanguage.value.Value;

@GrammarRule
public class AssignmentExpression extends Expression {
    @Token
    public static final String ASSIGN = "=";

    private String name;
    private Expression valExpression;
    public AssignmentExpression(String var, String assign, Expression expression) {
        name = var;
        valExpression = expression;
    }

    @Override
    public Value evaluate(Environment environment) {
        return environment.set(name, valExpression.evaluate(environment));
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " " + ASSIGN + " " + valExpression;
    }
}
