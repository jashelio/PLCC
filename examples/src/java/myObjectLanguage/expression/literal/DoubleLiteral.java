package myObjectLanguage.expression.literal;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import myObjectLanguage.value.DoubleValue;

@GrammarRule
public class DoubleLiteral extends Literal {
    public DoubleLiteral(String doubleLiteral) {
        super(new DoubleValue(Double.parseDouble(doubleLiteral)));
    }
}
