package myObjectLanguage.expression;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Token;
import myObjectLanguage.environment.Environment;
import myObjectLanguage.value.Value;

@GrammarRule
public class AssignmentExpression extends Expression {
    @Token
    public static final String ASSIGN = "=";

    private Expression name;
    private Expression valExpression;
    public AssignmentExpression(VariableExpression var, String assign, Expression expression) {
        name = var;
        valExpression = expression;
    }

    public AssignmentExpression(AccessorExpression obj, String assign, Expression expression) {
        name = obj;
        valExpression = expression;
    }

    @Override
    public Value evaluate(Environment environment) {
        name.evaluate(environment);
        Environment environment1 = AccessorExpression.recentlyReadEnvironment();
        if (environment1 == null)
            return environment.set(name.toString(), valExpression.evaluate(environment));
        String name = AccessorExpression.ToAccess.recentlyReadBinding();
        if (name == null)
            throw new RuntimeException("Can't set 'this' or 'super' like this: " + this);
        return environment1.setNewBinding(name, valExpression.evaluate(environment));
    }

    public String getName() {
        return name.toString();
    }

    @Override
    public String toString() {
        return name + " " + ASSIGN + " " + valExpression;
    }
}
