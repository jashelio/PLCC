package intCalculator;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Priority;
import edu.rit.gec8773.laps.annotation.RunAfterEachInit;
import edu.rit.gec8773.laps.annotation.Token;

@GrammarRule
public class MathExpression implements Comparable<MathExpression> {
    @Token public static String NUMBER = "\\d+";

    private Integer value = null;

    private BinaryOperator operator;
    private MathExpression operandA;
    private MathExpression operandB;

    @Priority(-1)
    public MathExpression(String number, BinaryOperator op, MathExpression exp) {
        this.operandA = new MathExpression(number);
        this.operandB = exp;
        this.operator = op;
    }

    @Priority(1)
    public MathExpression(String number) {
        this.value = number == null ? null : Integer.parseInt(number);
        this.operator = new BinaryOperator(null);
    }

    private static final MathExpression temp =
            new MathExpression(null);
    @RunAfterEachInit
    public void orderTree() {
        if (value != null)
            return;
//        System.out.println("BEFORE");
//        print();
        if (compareTo(operandB) < 0) {
            this.copyTo(temp);
            operandB.copyTo(this);
            this.operandA.copyTo(temp.operandB);
            temp.copyTo(this.operandA);
        }
//        System.out.println("AFTER");
//        print();
    }

    private void print() {
        id = 0;
        printTree();
    }

    private static int id;
    private void printTree() {
        int myId = ++id;
        int depth = 10;
        printSpaces(depth);
        System.out.println(operator);
        for (int i = 0; i < depth / 2; i++) {
            printSpaces(depth - i);
            System.out.print('/');
            printSpaces(i*2);
            System.out.println('\\');
        }
        printSpaces(depth/2);
        System.out.print(operandA.value == null ? operandA.operator : operandA.value);
        printSpaces(depth);
        System.out.println(operandB.value == null ? operandB.operator : operandB.value);
        System.out.println();
        if (operandA.value == null) {
            System.out.println(myId);
            operandA.printTree();
        }
        if (operandB.value == null) {
            System.out.println(myId);
            operandB.printTree();
        }
    }

    private static void printSpaces(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print(' ');
        }
    }

    @Override
    public int compareTo(MathExpression other) {
        return operator.compareTo(other.operator);
    }

    private void copyTo(MathExpression to) {
        to.operator = this.operator;
        to.operandA = this.operandA;
        to.operandB = this.operandB;
        to.value = this.value;
    }

    public int evaluate() {
        if (value != null)
            return value;
        return operator.calculate(operandA.evaluate(), operandB.evaluate());
    }
}

