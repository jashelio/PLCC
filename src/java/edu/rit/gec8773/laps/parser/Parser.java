package edu.rit.gec8773.laps.parser;

import edu.rit.gec8773.laps.scanner.Scanner;
import edu.rit.gec8773.laps.annotation.RunAfterEachInit;
import edu.rit.gec8773.laps.annotation.RunBeforeEachInit;
import edu.rit.gec8773.laps.annotation.RunBeforeFirstInit;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public interface Parser extends Serializable {

    /**
     * Parses input tokens from a {@link Scanner} and runs user code on the
     * created abstract syntax tree.
     * @see Scanner
     * @param sc the {@link Scanner} to read from
     * @return the created abstract syntax tree
     * @throws IOException when the {@link Scanner} has an I/O error
     * @throws InvocationTargetException when there is an uncaught exception in
     * a method marked with {@link RunAfterEachInit},
     * {@link RunBeforeEachInit}, and {@link RunBeforeFirstInit}
     * @throws InstantiationException when there is an uncaught exception in a
     * constructor of a grammar rule
     */
    Object parse(Scanner sc) throws IOException,
            InvocationTargetException, InstantiationException;

    /**
     * Returns the tokens which have been consumed by the parser back to the
     * {@link Scanner} with intent to parse other grammar rules
     * @param sc the {@link Scanner} to return to
     */
    void returnTokens(Scanner sc);

    Type getStartingToken();
}
