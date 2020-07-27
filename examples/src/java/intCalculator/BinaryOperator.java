package intCalculator;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Token;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrammarRule
public class BinaryOperator implements Comparable<BinaryOperator> {
    @Token public static final String OPERATOR = "[\\+\\-\\/\\*]";

    private enum BinaryOperation {
        ADDITION("+", 2, Integer::sum),
        SUBTRACT("-", 2, (a,b) -> a-b),
        MULTIPLY("*", 1, (a,b) -> a*b),
        DIVIDE("/", 1, (a,b) -> a/b),
        NUMBER(null, 0, null);

        public static final Map<String, BinaryOperation> operations =
                Arrays.stream(BinaryOperation.values())
                      .collect(Collectors.toMap(
                              binaryOperation -> binaryOperation.symbol,
                              Function.identity()
                      ));

        public final String symbol;
        public final int priority;
        public final BiFunction<Integer, Integer, Integer> computer;

        BinaryOperation(String symbol, int priority, BiFunction<Integer, Integer, Integer> computer) {
            this.symbol = symbol;
            this.priority = priority;
            this.computer = computer;
        }
    }

    private final BinaryOperation operation;

    public BinaryOperator(String operator) {
        this.operation = BinaryOperation.operations.get(operator);
    }

    @Override
    public String toString() {
        return operation.symbol;
    }

    @Override
    public int compareTo(BinaryOperator other) {
        return operation.priority - other.operation.priority;
    }

    public int calculate(int operandA, int operandB) {
        return this.operation.computer.apply(operandA, operandB);
    }
}
