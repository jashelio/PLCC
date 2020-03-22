package numlist2;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.RunBeforeEachInit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@GrammarRule
public class Numbers implements Iterable<Number> {

    /**
     * The amount of lists created
     */
    private static int listCount = 0;

    /**
     * The current list to add new numbers to
     */
    private static List<Number> currentList = null;

    /**
     * Replaces {@link Numbers#currentList} with a new list when a new
     * {@link NumList} is created
     */
    @RunBeforeEachInit
    public static void updateIsNewList() {
        if (NumList.getInitCount() > listCount) {
            currentList = new ArrayList<>();
            ++listCount;
        }
    }

    /**
     * Sets the instanceList to the currentList and inserts at the beginning
     * to keep order of the numbers the same as the input
     *
     * @param number a {@link String} to be parsed to an {@link Integer}
     * @param rest (ignored) the rest of the numbers
     */
    public Numbers(String number, RestOfNumbers rest) {
        instanceList = Numbers.currentList;
        instanceList.add(0, Integer.parseInt(number));
    }

    /**
     * The list the number and the {@link RestOfNumbers} is a part of
     */
    private List<Number> instanceList;

    /**
     * Method of accessing the list of numbers
     *
     * @return the list's {@link Iterator}
     */
    @Override
    public Iterator<Number> iterator() {
        return instanceList.iterator();
    }

    /**
     * Method of accessing the list's size
     *
     * @return the size of the list
     */
    public int size() {
        return instanceList.size();
    }
}
