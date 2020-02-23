package test;

import plcc.*;

@TokenList
public class Test {
	@Token(skip=true)
	public static final String whitespace = "\\s+";
	public static void main(String[] args) {
		PLCC.start();
	}
}
