package myObjectLanguage.function;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.RunBeforeEachInit;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

@GrammarRule
public class FunctionParameters implements Iterable<String> {
    @RunBeforeEachInit
    public static void setParameters() {
        Parameters.newList();
    }

    private Supplier<Iterator<String>> parameters;
    private int size;

    public FunctionParameters(String lparen, Parameters parameters, String rparen) {
        this.parameters = parameters::iterator;
        size = parameters.size();
    }

    private FunctionParameters() {}

    public static FunctionParameters fromList(List<String> stringList) {
        FunctionParameters result = new FunctionParameters();
        result.parameters = stringList::iterator;
        result.size = stringList.size();
        return result;
    }

    @Override
    public Iterator<String> iterator() {
        return parameters.get();
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (String param : this) {
            sb.append(param)
              .append(", ");
        }
        if (size > 0)
            sb.setLength(sb.length() - 2);
        sb.append(")");
        return sb.toString();
    }
}
