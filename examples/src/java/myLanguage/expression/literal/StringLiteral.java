package myLanguage.expression.literal;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import myLanguage.value.StringValue;

@GrammarRule
public class StringLiteral extends Literal {
    public StringLiteral(String stringLiteral) {
        super(new StringValue(stringLiteral.substring(1, stringLiteral.length() - 1)));
    }
}
