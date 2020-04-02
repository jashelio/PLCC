package myObjectLanguage.expression;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Priority;
import edu.rit.gec8773.laps.annotation.Token;
import myObjectLanguage.environment.Environment;
import myObjectLanguage.value.Value;

@GrammarRule
public class AccessorExpression extends Expression {
    @Token
    public static final String DOT = "\\.";

    @GrammarRule
    public static class ToAccess extends Expression implements myObjectLanguage.expression.ToAccess {

        private static String binding = "";
        private static boolean readBinding = true;

        public static String recentlyReadBinding() {
            if (readBinding)
                return null;
            readBinding = true;
            return binding;
        }

        private Expression toAccess;

        private ToAccess(Expression toAccess) {
            this.toAccess = toAccess;
        }

        @Priority(-1)
        public ToAccess(AccessorExpression toAccess) {
            this((Expression)toAccess);
        }

        public ToAccess(SuperExpression toAccess) {
            this((Expression)toAccess);
        }

        public ToAccess(ThisExpression toAccess) {
            this((Expression)toAccess);
        }

        @Priority(1)
        public ToAccess(VariableExpression toAccess) {
            this((Expression)toAccess);
        }

        @Override
        public Value evaluate(Environment environment) {
            if (toAccess instanceof VariableExpression) {
                readBinding = false;
                binding = toAccess.toString();
            }
            return toAccess.evaluate(environment);
        }

        @Override
        public String toString() {
            return toAccess.toString();
        }
    }

    private Expression expression;
    private ToAccess toAccess;

    private AccessorExpression(Expression expression, ToAccess toAccess) {
        this.expression = expression;
        this.toAccess = toAccess;
    }

    public AccessorExpression(SuperExpression obj, String dot, ToAccess toAccess) {
        this(obj, toAccess);
    }

    public AccessorExpression(ThisExpression obj, String dot, ToAccess toAccess) {
        this(obj, toAccess);
    }

    @Priority(1)
    public AccessorExpression(VariableExpression obj, String dot, ToAccess toAccess) {
        this(obj, toAccess);
    }

    private static Environment storageEnvironment = Environment.NULL;
    private static boolean readStorageEnvironment = true;

    public static Environment recentlyReadEnvironment() {
        if (readStorageEnvironment)
            return null;
        readStorageEnvironment = true;
        return storageEnvironment;
    }

    @Override
    public Value evaluate(Environment environment) {
        Value envVal = expression.evaluate(environment);
        readStorageEnvironment = false;
        return toAccess.evaluate(storageEnvironment = envVal.getEnvironment());
    }

    @Override
    public String toString() {
        return expression + "." + toAccess;
    }
}
