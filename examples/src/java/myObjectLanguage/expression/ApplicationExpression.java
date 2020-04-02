package myObjectLanguage.expression;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Priority;
import myObjectLanguage.environment.Environment;
import myObjectLanguage.value.Value;

import java.util.ArrayList;
import java.util.List;

@GrammarRule
public class ApplicationExpression extends Expression {

    private String applying;
    private FunctionArguments arguments;
    private AccessorExpression accessor;

    public ApplicationExpression(String var, String lparen, FunctionArguments functionArguments, String rparen) {
        applying = var;
        arguments = functionArguments;
    }

    @Priority(-1)
    public ApplicationExpression(AccessorExpression accessorExpression, String lparen, FunctionArguments functionArguments, String rparen) {
        accessor = accessorExpression;
        arguments = functionArguments;
    }

    @Override
    public Value evaluate(Environment environment) {
//        System.out.println("Trying to apply: " + this);
//        System.out.println("With environment: " + environment);
        Value function;
        if (applying != null)
            function = environment.get(applying);
        else
            function = accessor.evaluate(environment);
//        System.out.println("With function: " + function);
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
        String name = applying == null ? accessor.toString() : applying;
        StringBuilder sb = new StringBuilder("CALL ").append(name)
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
