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
		sc = new java.util.Scanner(lineReader)
			.useDelimiter((String)null);
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
	public void skip() {
		System.out.print("skipping...");
		boolean run = false;
		do {
			for (Pattern pat : Resources.instance.getSkips()) {
				if (sc.hasNext(pat)) {
					run = true;
					break;
				}
			}
			if (!run)
				break;	
			Resources.instance.forEachSkip( pattern -> {
				if (sc.hasNext(pattern))
					sc.next(pattern);
			});
		} while (run);
		System.out.println("done");
	}

	@Override
	public int getLineNumber() {
		return lineReader.getLineNumber();
	}

	@Override
	public void close() {
		sc.close();
	}
}
