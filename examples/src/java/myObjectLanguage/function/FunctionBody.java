package myObjectLanguage.function;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import myObjectLanguage.environment.Environment;
import myObjectLanguage.expression.Expression;
import myObjectLanguage.expression.TopExpressions;
import myObjectLanguage.value.Value;

import java.util.Iterator;
import java.util.function.Function;

@GrammarRule
public class FunctionBody implements Iterable<Expression> {
    private Iterable<Expression> expressions;

    public FunctionBody(String lbrace, TopExpressions expressions, String rbrace) {
        this.expressions = expressions::iterator;
    }

    private FunctionBody() {}

    public static FunctionBody fromFunction(Function<Environment, Value> function) {
        FunctionBody result = new FunctionBody();
        result.expressions = () -> new Iterator<>() {
            private boolean ran = false;
            @Override
            public boolean hasNext() {
                if (ran)
                    return false;
                ran = true;
                return true;
            }

            @Override
            public Expression next() {
                return Expression.functionExpression(function);
            }
        };
        return result;
    }

        @Override
    public Iterator<Expression> iterator() {
        return expressions.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Expression expression : this) {
            sb.append("\n\t")
              .append(expression);
        }
        return sb.append("\n}").toString();
    }
}
