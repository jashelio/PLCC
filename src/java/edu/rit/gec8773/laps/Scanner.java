package edu.rit.gec8773.laps;

import edu.rit.gec8773.laps.resources.Resources;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * {@link Scanner} converts a stream of characters to a stream of
 * {@link Token}s while keeping track of the line number it is reading from
 *
 * @see AutoCloseable
 * @see Pattern
 */
public interface Scanner extends AutoCloseable {
	/**
	 * Checks if the next {@link Token} has a given {@link Pattern}
	 *
	 * @param pattern the given pattern
	 * @return true if there is the expected next {@link Token}
	 * @throws IOException
	 */
	boolean hasNextToken(Pattern pattern) throws IOException;

	/**
	 * Retrieves the next {@link Token} of a desired pattern
	 *
	 * @param pattern the desired pattern
	 * @return the next {@link Token} or {@code null}
	 * @throws IOException
	 */
	Token nextToken(Pattern pattern) throws IOException;

	/**
	 * Retrieves any next {@link Token}
	 * @return a {@link Token}
	 * @throws IOException
	 */
	default Token nextToken() throws IOException {
		Set<Pattern> tokenPatterns = Resources.instance
						      .getPatterns();
		for (Pattern pattern : tokenPatterns)
			if (hasNextToken(pattern))
				return nextToken(pattern);
		return null;
	}

	/**
	 * Skips all skip {@link Pattern}s until there is nothing left to skip
	 * @throws IOException
	 */
	void skip() throws IOException;

	/**
	 * Returns the {@link Token} to the {@link Scanner}
	 * @param token the {@link Token} to return
	 */
	void unreadToken(Token token);

	/**
	 * Gets the current line number the {@link Scanner} is reading from
	 * @return the line number
	 */
	int getLineNumber();
}
