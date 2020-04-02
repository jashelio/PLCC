package myObjectLanguage.value;

import myObjectLanguage.Program;
import myObjectLanguage.environment.Environment;
import myObjectLanguage.expression.Expression;
import myObjectLanguage.function.FunctionBody;
import myObjectLanguage.function.FunctionParameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class FunctionValue extends Value {
    private FunctionParameters parameters;
    private FunctionBody body;
    private Environment definedEnvironment;

    public FunctionValue(FunctionParameters parameters, FunctionBody body, Environment environment) {
        this.parameters = parameters;
        this.body = body;
        definedEnvironment = environment;
    }

    private FunctionValue() {}

    public static FunctionValue primitiveFunction(int paramNumber, Function<Environment, Value> function) {
        FunctionValue result = new FunctionValue();
        List<String> paramList = new ArrayList<>();
        for (int i = 0; i < paramNumber; ++i) {
            paramList.add(Character.toString(((int)'a') + i));
        }
        result.parameters = FunctionParameters.fromList(paramList);
        result.body = FunctionBody.fromFunction(function);
        result.definedEnvironment = Program.INIT_ENVIRONMENT;
        return result;
    }

    @Override
    public Value apply(Value[] args) {
        if (args.length != parameters.size()) {
            System.err.println(this);
            System.err.println("Arguments: " + Arrays.toString(args));
            System.err.println("Parameters: " + parameters);
            throw new IllegalArgumentException("Number of arguments: " + args.length +
                    " != number of parameters: " + parameters.size());
        }
        Environment environment = definedEnvironment.extendEnvironment();
        int i = 0;
        for (String param : parameters) {
            environment.setNewBinding(param, args[i++]);
        }
        environment = environment.extendEnvironment();
        Value result = Value.NULL;
        for (Expression expression : body) {
//            if (myLanguage.expression.isBreakFromExecution())
//                return myLanguage.expression.evaluate(myLanguage.environment);
            result = expression.evaluate(environment);
        }
        return result;
    }

    @Override
    public String toString() {
        return parameters + " " + body;
    }
}
