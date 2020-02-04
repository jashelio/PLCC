package plcc;

import java.util.List;

import plcc.*;

public interface Scanner {
	boolean hasNextToken(Token token);

	boolean hasSkip();

	Token nextToken(Token token);
	
	default Token nextToken() {
		List<Token> tokens = Resources.getTokens();
		for (Token token : tokens)
			if (hasNextToken(token))
				return nextToken(token);
		return null;
	}

	void skip();

// TODO	Token getCurrentToken();

	int getLineNumber();
}
