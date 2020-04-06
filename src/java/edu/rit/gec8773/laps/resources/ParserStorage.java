package edu.rit.gec8773.laps.resources;

import edu.rit.gec8773.laps.parser.Parser;
import edu.rit.gec8773.laps.parser.topDown.TopDownParser;

import java.io.Serializable;
import java.util.HashMap;

public class ParserStorage implements Serializable {
    private Parser parserHead = TopDownParser.EMPTY;
    private HashMap<Class<?>, Parser> classMap = new HashMap<>();

    public Parser getParser(Class<?> c) {
        return classMap.get(c);
    }

    public boolean hasParser(Class<?> c) {
        return classMap.containsKey(c);
    }

    public boolean addParser(Class<?> c, Parser parser) {
        if (hasParser(c))
            return false;
        classMap.put(c, parser);
        return true;
    }

    public Parser getParserHead() {
        return parserHead;
    }

    public void setParserHead(Parser parser) {
        if (parser == null)
            return;
        parserHead = parser;
    }
}
