package edu.rit.gec8773.laps.parser.topDown;

import edu.rit.gec8773.laps.util.BNFWriter;
import edu.rit.gec8773.laps.scanner.Scanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Stack;

public class SequentialParser extends TopDownParser {


    private Stack<Integer> lastAcceptedRuleIndex = new Stack<>();

    /**
     *
     * {@inheritDoc}
     *
     */
    SequentialParser(Object[] rules) throws InvocationTargetException {
        super(rules);
    }

    @Override
    public Object parse(Scanner sc) throws IOException,
            InvocationTargetException, InstantiationException {
//	/*DEBUG*/		System.out.println("SEQ rule");
        Object[] parsed = new Object[parsingRules.size()];
        for (int i = 0; i < parsingRules.size(); ++i) {
            parsed[i] = parsingRules.get(i).parse(sc);
            if (parsed[i] == null) {
                lastAcceptedRuleIndex.push(i);
                return null;
            }
        }
        lastAcceptedRuleIndex.push(parsingRules.size());
        return parsed;
    }

    @Override
    public void returnTokens(Scanner sc) {
        if (this == EMPTY)
            return;
//	/*DEBUG*/		System.out.println("SEQ returning tokens");
        Integer i = lastAcceptedRuleIndex.isEmpty() ?
                null : lastAcceptedRuleIndex.pop();
        if (i == null)
            return;
        for (--i; i >= 0; --i)
            parsingRules.get(i).returnTokens(sc);
    }

    @Override
    public Type getStartingToken() {
        return parsingRules.get(0).getStartingToken();
    }

    @Override
    public String toString() {
        if (parsingRules.isEmpty())
            return "e";
        StringBuilder sb = new StringBuilder();
        for (BNFWriter.Writable child : this)
            sb.append(child.getName() == null ?
                    child.toString() :
                    child.getName())
                    .append(" ");
        return sb.substring(0, sb.length() - 1);
    }
}
