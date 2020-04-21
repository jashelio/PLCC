package jeh;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.RunAfterEachInit;
import edu.rit.gec8773.laps.annotation.RunBeforeEachInit;
import edu.rit.gec8773.laps.annotation.Token;

/**
 * @author James Heliotis
 * <p>
 * April 2020
 */
@GrammarRule
public class Prog {

    @Token public static final String BEGIN = "begin";
    @Token public static final String END = "end";
    @Token public static final String PASS = "pass";
    @Token public static final String IF = "if";
    @Token public static final String WHILE = "while";

    @Token( skip = true ) public static final String WHITESPACE = "\\s+";

    @RunBeforeEachInit
    public void bfe() {
        System.out.println( "before each init" );
    }

    @RunAfterEachInit
    public void afe() {
        System.out.println( "after each init" );
    }

    public Prog( String begin, String end ) {}
}
