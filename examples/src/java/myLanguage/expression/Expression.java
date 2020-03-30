package myLanguage.expression;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Priority;
import myLanguage.environment.Environment;
import myLanguage.expression.literal.LiteralExpression;
import myLanguage.value.Value;

import java.util.function.Function;

@GrammarRule
public class Expression {

    private Expression innerExpression;
    private boolean breakFromExecution = false;

    protected Expression() {}

    public static Expression functionExpression(Function<Environment, Value> function) {
        Expression result = new Expression();
        result.innerExpression = new Expression() {
            @Override
            public Value evaluate(Environment environment) {
                return function.apply(environment);
            }

            @Override
            public String toString() {
                return "<prim>";
            }
        };
        return result;
    }

    private Expression(ReturnExpression expression) { // Disabled
        breakFromExecution = true;
        innerExpression = expression;
    }

    @Priority(-5)
    public Expression(LetExpression expression) {
        innerExpression = expression;
    }

    @Priority(-4)
    public Expression(AssignmentExpression expression) {
        innerExpression = expression;
    }

    @Priority(-5)
    public Expression(LiteralExpression expression) {
        innerExpression = expression;
    }

    @Priority(-3)
    public Expression(ApplicationExpression expression) {
        innerExpression = expression;
    }

    @Priority(5)
    public Expression(VariableExpression expression) {
        innerExpression = expression;
    }

    public Value evaluate(Environment environment) {
        return innerExpression.evaluate(environment);
    }

    public boolean isBreakFromExecution() {
        return breakFromExecution;
    }

    @Override
    public String toString() {
        return innerExpression.toString();
    }
}
