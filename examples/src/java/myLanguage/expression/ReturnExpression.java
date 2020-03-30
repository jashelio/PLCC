package myLanguage.expression;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Token;
import myLanguage.environment.Environment;
import myLanguage.value.Value;

@GrammarRule
public class ReturnExpression extends Expression {
    @Token
    public static final String RETURN = "return";

    private Expression returnExpression;

    public ReturnExpression(String Return, Expression expression) {
        if (expression instanceof ReturnExpression)
            throw new RuntimeException();
        returnExpression = expression;
    }

    @Override
    public Value evaluate(Environment environment) {
        return returnExpression.evaluate(environment);
    }

    @Override
    public String toString() {
        return RETURN + " " + returnExpression;
    }
}
