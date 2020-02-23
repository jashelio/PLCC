package numlist;

import plcc.annotation.*;

@TokenList
public class TokenClass { // must be public
	@Token(skip=true) // must be public
	public static final String WHITESPACE = "\\s+";
	@Token // must be public
	public static final String NUMBER = "\\d+";
	@Token // must be public
	public static final String LPAREN = "\\(";
	@Token // must be public
	public static final String RPAREN = "\\)";

	// to get regex run RPAREN.toString()
	// TODO add regex_method parameter to @Token annotation
}
