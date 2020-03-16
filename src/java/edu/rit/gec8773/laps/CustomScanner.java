package edu.rit.gec8773.laps;

import java.io.*;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomScanner implements Scanner {
	private LineNumberReader lineReader;
	private StringBuffer buffer;

	public CustomScanner(InputStream input) {
		this(new InputStreamReader(input));
	}

	public  CustomScanner(Reader input) {
		lineReader = new LineNumberReader(input);
		buffer = new StringBuffer();
	}

	public CustomScanner(File input) throws FileNotFoundException {
		this(new FileReader(input));
	}

	public CustomScanner(String input) {
		this(new StringReader(input));
	}

	private void updateBuffer() throws IOException {
		buffer.appendCodePoint(lineReader.read());
	}

	private Integer findEnd(Pattern pattern) throws IOException {
		if (buffer.length() == 0)
			updateBuffer(); // ensure there is atleast 1 char in buf
//	DEBUG	System.out.println("Contents of buffer: " + buffer);
		Matcher matcher = pattern.matcher(buffer);
		int end = 1;
		do {
			matcher.region(0, end++).matches();
			if (end > buffer.length())
				updateBuffer();
		} while (matcher.hitEnd());
		if (matcher.lookingAt()) {
//	DEBUG		System.out.println("end: " + matcher.end());
			return matcher.end();
		} else
			return null;
	}

	private boolean hasNext(Pattern pattern) throws IOException {
//	DEBUG	System.out.println("hasNext(): " + pattern);
		Integer end = findEnd(pattern);
		return end != null;
	}

	private String next(Pattern pattern) throws IOException {
		Integer end = findEnd(pattern);
		if (end == null)
			return null;
		String value = buffer.substring(0, end);
		buffer.delete(0, end);
		return value;
	}

	@Override
	public void unreadToken(Token token) {
		buffer.insert(0, token.getValue());
	}

	@Override
	public boolean hasNextToken(Pattern pattern) throws IOException {
		return hasNext(pattern);
	}

	@Override
	public Token nextToken(Pattern pattern) throws IOException {
		String value = next(pattern);
		if (value == null)
			return null;
		Token tokenType = Resources.instance.getToken(pattern);
		return new Token(tokenType, value);
	} // Parser.or(token1, token2).parse(this) may run into
	// not having the first token but having the second token.
	// This doesn't currently work due to the Scanner. Should
	// I save the current token? How would I know when it is used?

	@Override
	public void skip() throws IOException {
//	DEBUG	System.out.print("skipping...");
		boolean run;
		Set<Pattern> patterns = Resources.instance.getSkips();
		do {
			run = false;
			for (Pattern pattern : patterns) {
				if (hasNext(pattern)) {
					run = true;
					String value = next(pattern);
//	DEBUG				System.out.println("skipping: '" + 
//	DEBUG						value + "'");
				}
			}
		} while (run);
//	DEBUG	System.out.println("done");
	}

	@Override
	public int getLineNumber() {
		return lineReader.getLineNumber() + 1;
	}

	@Override
	public void close() throws IOException {
		lineReader.close();
	}

	public String getBufferString() {
		return buffer.toString();
	}
}
