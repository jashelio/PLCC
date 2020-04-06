package edu.rit.gec8773.laps.parser.topDown;

import edu.rit.gec8773.laps.scanner.Scanner;
import edu.rit.gec8773.laps.scanner.Token;
import edu.rit.gec8773.laps.resources.Resources;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;
import java.util.regex.Pattern;

public class TokenParser extends TopDownParser {

    private final String name;
    private final Pattern pattern;

    public TokenParser(Token token) throws InvocationTargetException {
        super(new Object[0]);
        StringBuilder sb = new StringBuilder();
        sb.append("<")
                .append(token.getName().toUpperCase())
                .append(">");
        name = sb.toString();
        pattern = token.getRegex();
    }

    private Stack<Token> tokStack = new Stack<>();
    // TODO make use of callstack instead

    @Override
    public Object parse(Scanner sc) throws IOException {
        sc.skip();
        if (Resources.instance.debugEnabled())
            System.out.print(name + " token = ");
        if (!sc.hasNextToken(pattern)) {
            if (Resources.instance.debugEnabled())
                System.out.println("null");
            return null;
        }
        Token tok = sc.nextToken(pattern);
        String val = tok.getValue();
        if (Resources.instance.debugEnabled())
            System.out.println('"' + val + '"');
        tokStack.push(tok);
        return tok;
    }

    @Override
    public void returnTokens(Scanner sc) {
        Token tok = tokStack.empty() ?
                null : tokStack.pop();
        if (tok == null)
            return;
//	/*DEBUG*/		System.out.println("Returning: " + tok.getValue());
        sc.unreadToken(tok);
    }

    @Override
    public Token getStartingToken() {
        return Resources.instance.getToken(pattern);
    }

    @Override
    public String toString() {
        return name;
    }
}
