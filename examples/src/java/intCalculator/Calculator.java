package intCalculator;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.RunAfterEachInit;
import edu.rit.gec8773.laps.annotation.Token;

@GrammarRule
public class Calculator {
    @Token(skip=true)
    public static String WHITESPACE = "\\s+";

    private MathExpression exp;
    public Calculator(MathExpression exp) {
        this.exp = exp;
    }

    @RunAfterEachInit
    public void calculate() {
        System.out.println(this.exp.evaluate());
    }
}
