package myLanguage.value;

import myLanguage.Program;
import myLanguage.environment.Environment;
import myLanguage.expression.Expression;
import myLanguage.function.FunctionBody;
import myLanguage.function.FunctionParameters;

import java.util.ArrayList;
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
        for (int i = 0; i < paramNumber; i += 26) {
            paramList.add(Character.toString('a' + i));
        }
        result.parameters = FunctionParameters.fromList(paramList);
        result.body = FunctionBody.fromFunction(function);
        result.definedEnvironment = Program.INIT_ENVIRONMENT;
        return result;
    }

    @Override
    public Value apply(Value[] args) {
        if (args.length != parameters.size())
            throw new RuntimeException();
        Environment environment = definedEnvironment.extendEnvironment();
        int i = 0;
        for (String param : parameters) {
            environment.setNewBinding(param, args[i++]);
        }
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
