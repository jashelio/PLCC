package myLanguage.expression.literal;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import myLanguage.value.IntegerValue;

@GrammarRule
public class IntegerLiteral extends Literal {
    public IntegerLiteral(String intLiteral) {
        super(new IntegerValue(Integer.parseInt(intLiteral)));
    }
}
