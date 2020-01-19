package numlist;

import plcc.*;

@TokenList
public enum TokenEnum { // must be public
	@Token(skip=true) WHITESPACE("\\s+"),
	@Token NUMBER("\\d+"),
	@Token LPAREN("\\("),
	@Token RPAREN("\\)");

	private String regex;

	Token(String regex) {
		this.regex = regex;
	}

	// Default get method to get regular expression
	public String toString() { // must be public when used regex method
		return regex;
	}

	// TODO add @TokenRegex to mark method to get regex
	@TokenRegex		// takes prioity over toString
	public String regex() { // must be public
		return regex;
	}
}
