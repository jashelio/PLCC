package plcc;

import java.io.*;
import java.lang.reflect.*;
import java.util.regex.*;
import java.util.Set;

import plcc.*;

public class BasicScanner implements Scanner {
	private java.util.Scanner sc;

	private LineNumberReader lineReader;

	public BasicScanner(InputStream input) {
		this(new InputStreamReader(input));
	}

	public BasicScanner(Reader input) {
		lineReader = new LineNumberReader(input);
		sc = new java.util.Scanner(lineReader).useDelimiter("");
	}

	public BasicScanner(File input) throws FileNotFoundException {
		this(new FileReader(input));
	}

	public BasicScanner(String input) {
		this(new StringReader(input));
	}

	private boolean hasNext(Set<Pattern> patterns) {
		for (Pattern pattern : patterns)
			if (sc.hasNext(pattern))
				return true;
		return false;
	}
	
	@Override
	public boolean hasNextToken(Pattern pattern) {
		return sc.hasNext(pattern);
	}

	@Override
	public boolean hasSkip() {
		return hasNext(Resources.instance.getSkips());
	}

	@Override
	public Token nextToken(Pattern pattern) {
/*		String value = null;
		String pattern;
		for (pattern : tokens.keySet()) {
			if (!sc.hasNext(pattern))
				continue;
			value = sc.next(pattern);
			break;
		}
		if (value == null)
			return null;
		return new Token(tokens.get(pattern), value);
		*/
		return new Token(Resources.instance.getToken(pattern), sc.next(pattern));
	} // Grammar.or(token1, token2).parse(this) may run into 
	  // not having the first token but having the second token.
	  // This doesn't currently work due to the Scanner. Should
	  // I save the current token? How would I know when it is used?

	@Override
	public void skip() {
		Resources.instance.forEachSkip( pattern -> {
			if (sc.hasNext(pattern))
				sc.skip(pattern);
		});
	}

	@Override
	public int getLineNumber() {
		return lineReader.getLineNumber();
	}
}
