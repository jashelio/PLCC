package numlist2;

import edu.rit.gec8773.laps.annotation.*;

import java.util.Objects;

@GrammarRule
public class NumList {

    @Token(skip = true)
    public static String WHITESPACE = "\\s+";

    @Token public static String LPAREN = "\\(";
    @Token public static String RPAREN = "\\)";

    @Token public static String NUMBER = "\\d+";
    @Token public static String COMMA = ",";

    @Token public static String DONE = "done";

    @RunBeforeFirstInit // optional run before constructor
    public static void setupOnce() {
        System.out.print("Insert numbers in the form: ");
        System.out.println("( num1, num2, ..., numN )");
        System.out.println("Note: you can add any whitespace you want");
        System.out.println("When you're done, enter \"done\"");
    }

    @RunBeforeEachInit // optional run before constructor
    public static void setupAll() {
        ++initCount;
        System.out.print("Input? ");
    }

    private static int initCount = 0;
    public static int getInitCount() {
        return initCount;
    }

    private Numbers numbers = null;

    public NumList(String lparen, Numbers nums, String rparen, NumList numList) {
        numbers = nums;
    }

    public NumList(String done) {}

    private void printList() {
        System.out.print('(');
        numbers.forEach(num -> System.out.print(num + ", "));
        if (numbers.size() != 0) {
            System.out.print("\b\b");
        }
        System.out.println(')');
    }

    private void printSum() {
        int sum = 0;
        for (Number num : numbers) {
            sum += num.intValue();
        }
        System.out.println(sum);
    }

    @SemanticEntryPoint
    public void run() {
        if (numbers == null)
            return;
        printList();
        printSum();
    }
}
