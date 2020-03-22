package numlist2;

import edu.rit.gec8773.laps.annotation.GrammarRule;

@GrammarRule
public class RestOfNumbers {

    /**
     * Just a way to collect the numbers. No implementation needed
     *
     * @param comma (ignored) the COMMA token
     * @param numbers (ignored) another number
     */
    public RestOfNumbers(String comma, Numbers numbers) {}

    /**
     * Accepts an empty grammar rule as well
     */
    public RestOfNumbers() {}

}
