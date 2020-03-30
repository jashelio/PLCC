package myLanguage.expression;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import myLanguage.environment.Environment;
import myLanguage.value.Value;

import java.util.ArrayList;
import java.util.List;

@GrammarRule
public class ApplicationExpression extends Expression {

    private String applying;
    private FunctionArguments arguments;

    public ApplicationExpression(String var, String lparen, FunctionArguments functionArguments, String rparen) {
        applying = var;
        arguments = functionArguments;
    }

    @Override
    public Value evaluate(Environment environment) {
        System.out.println("Trying to apply: " + this);
//        System.out.println("With myLanguage.environment: " + myLanguage.environment);
        Value function = environment.get(applying);
//        System.out.println("With myLanguage.function: " + myLanguage.function);
        List<Value> args = new ArrayList<>();
        for (Expression expression : arguments) {
            args.add(expression.evaluate(environment));
        }
//        System.out.println("With arguments: " + args);
        Value[] vals = args.toArray(new Value[args.size()]);
        return function.apply(vals);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CALL ").append(applying)
                                                    .append("(");
        boolean ran = false;
        for (Expression expression : arguments) {
            ran = true;
            sb.append(expression)
              .append(", ");
        }
        if (ran)
            sb.setLength(sb.length() - 2);
        return sb.append(")").toString();
    }
}
