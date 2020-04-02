package myObjectLanguage.object;

public interface ObjectFieldAccessibility {
    ObjectFieldAccessibility PUBLIC = () -> true;
    ObjectFieldAccessibility PROTECTED = () -> true;
    ObjectFieldAccessibility PRIVATE = () -> false;
    boolean isAccessible();
}
