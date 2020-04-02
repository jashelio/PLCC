package myObjectLanguage.function;

import edu.rit.gec8773.laps.annotation.GrammarRule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@GrammarRule
public class Parameters implements Iterable<String> {

    @GrammarRule
    public static class RestOfParameters {
        public RestOfParameters(String comma, Parameters parameters) {}
        public RestOfParameters() {}
    }

    static void newList() {
        currentList = new ArrayList<>();
    }

    private static List<String> currentList = new ArrayList<>();

    private List<String> instanceList;

    public Parameters(String var, RestOfParameters rest) {
        this();
        instanceList.add(0, var);
    }

    public Parameters() {
        instanceList = currentList;
    }

    @Override
    public Iterator<String> iterator() {
        return instanceList.iterator();
    }

    public int size() {
        return instanceList.size();
    }
}
