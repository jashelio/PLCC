package myLanguage.expression;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import myLanguage.function.FunctionDefinition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@GrammarRule
public class TopExpressions implements Iterable<Expression> {

    @GrammarRule
    public static class RestOfTopExpressions implements Iterable<Expression> {
        private Iterable<Expression> iterable;
        public RestOfTopExpressions(TopExpressions expressions) {
            iterable = expressions;
        }
        public RestOfTopExpressions() {
            iterable = List.of();
        }

        @Override
        public Iterator<Expression> iterator() {
            return iterable.iterator();
        }
    }

    private List<Expression> expressions = new ArrayList<>();

    public TopExpressions(Expression expression, String semicolon, RestOfTopExpressions rest) {
        if (expression == null)
            new Exception().printStackTrace();
        expressions.add(expression);
        rest.forEach(expressions::add);
    }

    public TopExpressions(FunctionDefinition expression, RestOfTopExpressions rest) {
        if (expression == null)
            new Exception().printStackTrace();
        expressions.add(expression);
        rest.forEach(expressions::add);
    }

    @Override
    public Iterator<Expression> iterator() {
        return expressions.iterator();
    }
}
