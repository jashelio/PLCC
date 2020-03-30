package myLanguage.expression.literal;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import myLanguage.value.CharacterValue;

@GrammarRule
public class CharacterLiteral extends Literal {
    public CharacterLiteral(String charLiteral) {
        super(new CharacterValue(charLiteral
                .substring(1, charLiteral.length() - 1).charAt(0)));
    }
}
