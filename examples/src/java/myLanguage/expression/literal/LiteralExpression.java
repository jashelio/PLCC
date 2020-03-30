package myLanguage.expression.literal;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Token;
import myLanguage.environment.Environment;
import myLanguage.expression.Expression;
import myLanguage.value.Value;

@GrammarRule
public class LiteralExpression extends Expression {

    @Token
    public static final String STRINGLITERAL = "\"[^\"]*\"";
    @Token
    public static final String CHARLITERAL = "'.'";
    @Token
    public static final String INTLITERAL = "[\\+\\-]?\\d+";
    @Token
    public static final String DOUBLELITERAL = "[\\+\\-]?(\\d+\\.\\d*|\\.\\d+)";

    private Value value;

    private LiteralExpression(Literal literal) {
        value = literal.getValue();
    }

    public LiteralExpression(StringLiteral literal) {
        this((Literal)literal);
    }

    public LiteralExpression(CharacterLiteral literal) {
        this((Literal)literal);
    }

    public LiteralExpression(IntegerLiteral literal) {
        this((Literal)literal);
    }

    public LiteralExpression(DoubleLiteral literal) {
        this((Literal)literal);
    }

    @Override
    public Value evaluate(Environment environment) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
