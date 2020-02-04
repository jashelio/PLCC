package plcc;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import plcc.*;

class BasicScanner implements Scanner {
	private java.util.Scanner sc;

	private LineNumberReader lineReader;

	private Map<String, Field> tokens;
	private Set<String> skips; // I don't think Field is neccessary?

	private Token currentToken;

	private BasicScanner() {
		tokens = PLCC.getTokens();
		skips = PLCC.getSkips();
	}

	BasicScanner(InputStream input) {
		this();
		InputStreamReader reader = new InputStreamReader(input);
		lineReader = new LineNumberReader(reader);
		sc = new java.util.Scanner(lineReader);
		currentToken = nextToken();
	}

	BasicScanner(Reader input) {
		this();
		lineReader = new LineNumberReader(input);
		sc = new java.util.Scanner(lineReader);
		currentToken = nextToken();
	}

	BasicScanner(File input) {
		this();
		FileReader reader = new FileReader(input);
		lineReader = new LineNumberReader(reader);
		sc = new java.util.Scanner(lineReader);
		currentToken = nextToken();
	}

	BasicScanner(String input) {
		this();
		StringReader reader = new StringReader(input);
		lineReader = new LineNumberReader(reader);
		sc = new java.util.Scanner(lineReader);
		currentToken = nextToken();
	}

	private boolean hasNext(Set<String> patterns) {
		for (String pattern : patterns)
			if (sc.hasNext(pattern))
				return true;
		return false;
	}
	
	@Override
	boolean hasNextToken(Token token) {
		return sc.hasNext(token.getRegex());
	}

	@Override
	boolean hasSkip() {
		return hasNext(skips);
	}

	@Override
	Token nextToken(Token token) {
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
		return new Token(token, sc.next(token.getRegex()));
	} // Grammar.or(token1, token2).parse(this) may run into 
	  // not having the first token but having the second token.
	  // This doesn't currently work due to the Scanner. Should
	  // I save the current token? How would I know when it is used?

	@Override
	void skip() {
		for (String pattern : skips) {
			if (!sc.hasNext(pattern))
				continue;
			sc.skip(pattern);
		}
	}

	@Override
	int getLineNumber() {
		return lineReader.getLineNumber();
	}
}
