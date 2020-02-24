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
		StringBuilder sb = new StringBuilder("[");
		Resources.instance.forEachSkip( pattern -> {
			sb.append("[")
			  .append(pattern)
			  .append("]");
		});
		sb.append("]+");
		String delimiter = sb.toString();
		if (delimiter.length() == 3)
			delimiter = "";
		sc = new java.util.Scanner(lineReader)
			.useDelimiter(delimiter);
	}

	public BasicScanner(File input) throws FileNotFoundException {
		this(new FileReader(input));
	}

	public BasicScanner(String input) {
		this(new StringReader(input));
	}

	@Override
	public boolean hasNextToken(Pattern pattern) {
		return sc.hasNext(pattern);
	}

	@Override
	public Token nextToken(Pattern pattern) {
		return new Token(Resources.instance.getToken(pattern), sc.next(pattern));
	} // Grammar.or(token1, token2).parse(this) may run into 
	  // not having the first token but having the second token.
	  // This doesn't currently work due to the Scanner. Should
	  // I save the current token? How would I know when it is used?

	@Override
	public int getLineNumber() {
		return lineReader.getLineNumber();
	}
}
