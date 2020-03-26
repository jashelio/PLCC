package edu.rit.gec8773.laps;

import edu.rit.gec8773.laps.resources.Resources;

import java.io.*;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * An implementation of a {@link Scanner} using {@link LineNumberReader} and
 * {@link Matcher} to handle regular expression matching
 *
 * @see edu.rit.gec8773.laps.Scanner
 * @see LineNumberReader
 * @see Matcher
 */
public class CustomScanner implements Scanner {
	private LineNumberReader lineReader;
	private StringBuffer buffer;

	/**
	 * Class constructor which uses an {@link InputStream} as input
	 * @param input the {@link InputStream}
	 */
	public CustomScanner(InputStream input) {
		this(new InputStreamReader(input));
	}

	/**
	 * Class constructor which uses a {@link Reader} as input
	 * @param input the input {@link Reader}
	 */
	public  CustomScanner(Reader input) {
		lineReader = new LineNumberReader(input);
		buffer = new StringBuffer();
	}

	/**
	 * Class constructor which uses a {@link File} as input
	 * @param input the input {@link File}
	 */
	public CustomScanner(File input) throws FileNotFoundException {
		this(new FileReader(input));
	}

	/**
	 * Class constructor which uses a {@link String} as input
	 * @param input the input {@link String}
	 */
	public CustomScanner(String input) {
		this(new StringReader(input));
	}

	/**
	 * Appends more character(s) into {@link CustomScanner#buffer}
	 * @throws IOException
	 */
	private void updateBuffer() throws IOException {
		int charCode = lineReader.read();
		if (charCode == -1)
			throw new IOException("Unexpected end of input");
		buffer.appendCodePoint(charCode);
	}

	/**
	 * Tries to find the longest match of a {@link Pattern} in the
	 * {@link CustomScanner#buffer}. If the end of the buffer is reached,
	 * more input will be read.
	 *
	 * @param pattern the {@link Pattern} to match
	 * @return last index of the matching region or null if there's no match
	 * @throws IOException
	 */
	private Integer findEnd(Pattern pattern) throws IOException {
		if (buffer.length() == 0)
			updateBuffer(); // ensure there is at least 1 char in buf
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

	/**
	 * Checks if the next {@link Pattern} is available from
	 * {@link CustomScanner#buffer}
	 * @param pattern the {@link Pattern} to check
	 * @return true if the {@link Pattern} matches
	 * @throws IOException
	 */
	private boolean hasNext(Pattern pattern) throws IOException {
//	DEBUG	System.out.println("hasNext(): " + pattern);
		Integer end = findEnd(pattern);
		return end != null;
	}

	/**
	 * Gets the next {@link Pattern} from {@link CustomScanner#buffer}
	 *
	 * @param pattern the {@link Pattern} to match
	 * @return the {@link String} matching the pattern
	 * @throws IOException
	 */
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
	}

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
					if (Resources.instance.debugEnabled())
						System.out.println("skipping: '" + value + "'");
				}
			}
		} while (run);
//	DEBUG	System.out.println("done");
	}

	@Override
	public int getLineNumber() {
		int result = lineReader.getLineNumber() + 1;
		for (char c : buffer.toString().toCharArray()) {
			if (c == '\n')
				--result;
		}
		return result;
	}

	@Override
	public void close() throws IOException {
		lineReader.close();
	}

	/**
	 * Gets the current contents of the {@link CustomScanner#buffer} as a
	 * {@link String} with non-printable characters escaped
	 * @return the {@link String}
	 */
	public String getBufferString() {
		return buffer.chars()
				.mapToObj(e -> {
					switch (e) {
						case '\n':
							return "\\n";
						case '\r':
							return "\\r";
						case '\'':
							return "\\'";
						case '"':
							return "\\\"";
						case '\\':
							return "\\\\";
						case '\t':
							return "\\t";
						case '\b':
							return "\\b";
						case '\f':
							return "\\f";
						default:
							return Character.toString(e);
					}
				})
				.collect(Collectors.joining());
	}
}
