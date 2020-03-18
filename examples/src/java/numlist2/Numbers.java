package numlist2;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.RunBeforeEachInit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

@GrammarRule
public class Numbers implements Iterable<Number> {

    private static int listCount = 0;
    private static List<Number> currentList = null;

    @RunBeforeEachInit
    public static void updateIsNewList() {
        if (NumList.getInitCount() > listCount)
            currentList = new ArrayList<>();
    }

    public Numbers(String number, RestOfNumbers rest) {
        instanceList = Numbers.currentList;
        instanceList.add(0, Integer.parseInt(number));
    }

    private List<Number> instanceList = null;

    public void forEach(Consumer<? super Number> consumer) {
        instanceList.forEach(consumer);
    }

    @Override
    public Iterator<Number> iterator() {
        return instanceList.iterator();
    }

    public int size() {
        return instanceList.size();
    }
}
