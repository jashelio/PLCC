package myLanguage.value;

public class CharacterValue extends Value {
    private Character character;
    public CharacterValue(Character character) {
        this.character = character;
    }

    @Override
    public Character characterValue() {
        return character;
    }

    @Override
    public String toString() {
        return "'" + character + "'";
    }
}
