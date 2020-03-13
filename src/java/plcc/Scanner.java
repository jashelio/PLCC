package plcc;

import java.util.Set;
import java.util.regex.Pattern;
import java.io.IOException;

import plcc.*;

public interface Scanner extends AutoCloseable {
	boolean hasNextToken(Pattern pattern) throws IOException;

	Token nextToken(Pattern pattern) throws IOException;
	
	default Token nextToken() throws IOException {
		Set<Pattern> tokenPatterns = Resources.instance
						      .getPatterns();
		for (Pattern pattern : tokenPatterns)
			if (hasNextToken(pattern))
				return nextToken(pattern);
		return null;
	}

	void skip() throws IOException;

	void unreadToken(Token token);

	int getLineNumber();
}
