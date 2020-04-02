package myObjectLanguage.expression;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Token;
import myObjectLanguage.environment.Environment;
import myObjectLanguage.value.Value;

@GrammarRule
public class LetExpression extends Expression {
    @Token
    public static final String LET = "let";

    private String name;
    private AssignmentExpression assignment;
    public LetExpression(String let, AssignmentExpression expression) {
        name = expression.getName();
        assignment = expression;
    }

    @Override
    public Value evaluate(Environment environment) {
        environment.addBinding(name);
        return assignment.evaluate(environment);
    }

    @Override
    public String toString() {
        return LET + " " + assignment;
    }
}
