package plcc;

import java.util.Set;
import java.util.regex.Pattern;

import plcc.*;

public interface Scanner {
	boolean hasNextToken(Pattern pattern);

	Token nextToken(Pattern pattern);
	
	default Token nextToken() {
		Set<Pattern> tokenPatterns = Resources.getInstance()
						      .getPatterns();
		for (Pattern pattern : tokenPatterns)
			if (hasNextToken(pattern))
				return nextToken(pattern);
		return null;
	}

// TODO	Token getCurrentToken();

	int getLineNumber();
}
