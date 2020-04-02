package myObjectLanguage.object;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.Token;
import myObjectLanguage.environment.Environment;
import myObjectLanguage.expression.Expression;
import myObjectLanguage.value.ClassValue;
import myObjectLanguage.value.Value;

@GrammarRule
public class ClassDefinition extends Expression {
    @Token
    public static final String _CLASS = "class";
    @Token
    public static final String _EXTENDS = "extends";

    private String name;
    private SuperClass superClass;
    private ClassBody body;

    public ClassDefinition(String _class, String var, SuperClass superClass, ClassBody body) {
        name = var;
        this.superClass = superClass;
        this.body = body;
    }

    @Override
    public Value evaluate(Environment environment) {
        Value value = new ClassValue(superClass, body, environment);
        return environment.setNewBinding(name, value);
    }

    @Override
    public String toString() {
        return _CLASS + " " + name + " " + superClass + " " + body;
    }
}
