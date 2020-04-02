package myObjectLanguage;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.RunAfterEachInit;
import edu.rit.gec8773.laps.annotation.RunBeforeFirstInit;
import edu.rit.gec8773.laps.annotation.Token;
import myObjectLanguage.environment.Environment;
import myObjectLanguage.expression.Expression;
import myObjectLanguage.expression.TopExpressions;
import myObjectLanguage.value.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        INIT_ENVIRONMENT.setNewBinding("input", FunctionValue.primitiveFunction(0, environment -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                return new StringValue(reader.readLine());
            } catch (IOException e) {
                return Value.NULL;
            }
        }));
        INIT_ENVIRONMENT.setNewBinding("repeat", FunctionValue.primitiveFunction(2, environment -> {
            int count = environment.get("a").integerValue();
            Value function = environment.get("b");
            Value result = Value.NULL;
            while (count-- != 0)
                result = function.apply(new Value[0]);
            return result;
        }));
        INIT_ENVIRONMENT.setNewBinding("isZero", FunctionValue.primitiveFunction(1, environment -> {
            if(environment.get("a").integerValue() == 0)
                return environment.get("true");
            return environment.get("false");
        }));
        INIT_ENVIRONMENT.setNewBinding("isNull", FunctionValue.primitiveFunction(1, environment -> {
            if(environment.get("a") == Value.NULL)
                return environment.get("true");
            return environment.get("false");
        }));
        INIT_ENVIRONMENT.setNewBinding("garbageCollect", FunctionValue.primitiveFunction(0, environment -> {
            System.gc();
            return Value.NULL;
        }));
        INIT_ENVIRONMENT.setNewBinding("sub", FunctionValue.primitiveFunction(2, environment ->
                new IntegerValue(environment.get("a").integerValue() - environment.get("b").integerValue())));
        INIT_ENVIRONMENT.setNewBinding("mult", FunctionValue.primitiveFunction(2, environment ->
                new IntegerValue(environment.get("a").integerValue() * environment.get("b").integerValue())));
        INIT_ENVIRONMENT.setNewBinding("null", Value.NULL);
        INIT_ENVIRONMENT.setNewBinding("globalThis", new EnvironmentValue(INIT_ENVIRONMENT));
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