package myLanguage.expression;

import edu.rit.gec8773.laps.annotation.GrammarRule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@GrammarRule
public class FunctionArguments implements Iterable<Expression> {

    @GrammarRule
    public static class RestOfFunctionArguments implements Iterable<Expression> {
        private Iterable<Expression> iterable;
        public RestOfFunctionArguments(String comma, FunctionArguments functionArguments) {
            iterable = functionArguments;
        }
        public RestOfFunctionArguments() {
            iterable = List.of();
        }

        @Override
        public Iterator<Expression> iterator() {
            return iterable.iterator();
        }
    }

    private List<Expression> expressions = new ArrayList<>();

    public FunctionArguments(Expression expression, RestOfFunctionArguments rest) {
        expressions.add(expression);
        rest.forEach(expressions::add);
    }

    public FunctionArguments() {}

    @Override
    public Iterator<Expression> iterator() {
        return expressions.iterator();
    }

}
