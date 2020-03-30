package myLanguage;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.RunAfterEachInit;
import edu.rit.gec8773.laps.annotation.RunBeforeFirstInit;
import edu.rit.gec8773.laps.annotation.Token;
import myLanguage.environment.Environment;
import myLanguage.expression.Expression;
import myLanguage.expression.TopExpressions;
import myLanguage.value.FunctionValue;
import myLanguage.value.IntegerValue;
import myLanguage.value.Value;

@GrammarRule
public class Program {
    @Token(skip = true)
    public static final String WHITESPACE = "\\s+";
    @Token
    public static final String VAR = "[a-zA-Z]\\w*";
    @Token
    public static final String OPTIONALVAR = "([a-zA-Z]\\w*)|";

    @Token
    public static final String LPAREN = "\\(";
    @Token
    public static final String RPAREN = "\\)";
    @Token
    public static final String LBRACE = "\\{";
    @Token
    public static final String RBRACE = "\\}";

    @Token
    public static final String SEMICOLON = ";";
    @Token
    public static final String COMMA = ",";

    private TopExpressions expressions;

    public Program(TopExpressions expressions) {
        this.expressions = expressions;
    }

    @RunBeforeFirstInit
    public static void setupEnvironment() {
        INIT_ENVIRONMENT.setNewBinding("print", FunctionValue.primitiveFunction(1, environment -> {
            System.out.println(environment.get("a"));
            return environment.get("a");
        }));
        INIT_ENVIRONMENT.setNewBinding("printEnv", FunctionValue.primitiveFunction(0, environment -> {
            System.out.println(environment);
            return Value.NULL;
        }));
        INIT_ENVIRONMENT.setNewBinding("isZero", FunctionValue.primitiveFunction(1, environment -> {
            if(environment.get("a").integerValue() == 0)
                return environment.get("true");
            return environment.get("false");
        }));
        INIT_ENVIRONMENT.setNewBinding("sub", FunctionValue.primitiveFunction(2, environment ->
                new IntegerValue(environment.get("a").integerValue() - environment.get("b").integerValue())));
        INIT_ENVIRONMENT.setNewBinding("mult", FunctionValue.primitiveFunction(2, environment ->
                new IntegerValue(environment.get("a").integerValue() * environment.get("b").integerValue())));
        INIT_ENVIRONMENT.setNewBinding("null", Value.NULL);
    }

    public static final Environment INIT_ENVIRONMENT = Environment.NULL
                                                        .extendEnvironment();

    @RunAfterEachInit
    public void run() {
        for (Expression expression : expressions) {
            expression.evaluate(INIT_ENVIRONMENT);
        }
    }
}