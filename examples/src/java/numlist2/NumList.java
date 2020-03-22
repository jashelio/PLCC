package numlist2;

import edu.rit.gec8773.laps.annotation.*;

import java.util.Objects;

@GrammarRule
public class NumList {

    //  Skips

    @Token(skip = true)
    public static String WHITESPACE = "\\s+";

    //  Tokens

    @Token public static String LPAREN = "\\(";
    @Token public static String RPAREN = "\\)";

    @Token public static String NUMBER = "\\d+";
    @Token public static String COMMA = ",";

    @Token public static String DONE = "done";

    /**
     * Prints out how to use the NumList language
     */
    @RunBeforeFirstInit // optional run before constructor
    public static void setupOnce() {
        System.out.print("Insert numbers in the form: ");
        System.out.println("( num1, num2, ..., numN )");
        System.out.println("Note: you can add any whitespace you want");
        System.out.println("When you're done, enter \"done\"");
    }

    /**
     * Prompts the user to enter another NumList or a DONE token, and updates
     * the count of NumList objects created.
     */
    @RunBeforeEachInit // optional run before constructor
    public static void setupAll() {
        ++initCount;
        System.out.print("Input? ");
    }

    /**
     * Storage for the amount of NumList objects created.
     */
    private static int initCount = 0;

    /**
     * Getter for {@code initCount}.
     * @return the value of {@code initCount}
     */
    public static int getInitCount() {
        return initCount;
    }

    private Numbers numbers = null;

    /**
     * Saves the {@link Numbers} collected in the list
     *
     * @param lparen (ignored) the LPAREN token
     * @param nums the numbers to save
     * @param rparen (ignored) the RPAREN token
     * @param numList (ignored) another {@link NumList}
     */
    public NumList(String lparen, Numbers nums, String rparen, NumList numList) {
        numbers = nums;
    }

    /**
     * Indicates that the program is done
     *
     * @param done (ignored) the DONE token
     */
    public NumList(String done) {}

    /**
     * Prints out the {@link NumList} to {@link System#out} ending in a newline
     */
    private void printList() {
        System.out.print('(');
        numbers.forEach(num -> System.out.print(num + ", "));
        if (numbers.size() != 0) {
            System.out.print("\b\b");
        }
        System.out.println(')');
    }

    /**
     * Prints out the sum of the {@link NumList} to {@link System#out} with a
     * newline
     */
    private void printSum() {
        int sum = 0;
        for (Number num : numbers) {
            sum += num.intValue();
        }
        System.out.println(sum);
    }

    /**
     * Prints the list then its sum when not done
     */
    @SemanticEntryPoint
    public void run() {
        if (numbers == null)
            return;
        printList();
        printSum();
    }
}
