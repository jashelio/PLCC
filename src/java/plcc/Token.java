package plcc;

import java.util.regex.Pattern;
import java.util.function.Function;

public class Token {

	private String name;
	private Pattern regex;
	private String value;

	// TODO Make private after testing
	public Token(String name, Pattern regex, String value) {
		this.name = name;
		this.regex = regex;
		this.value = value;
	}

	public Token(String name, Pattern regex) {
		this(name, regex, null);
	}

	public Token(String name, String pattern) {
		this(name, Pattern.compile(pattern));
	}

	public Token(Token template, String value) {
		this(template.name, template.regex, value);
	}
	
	// Getters

	public String getName() {
		return name;
	}

	public Pattern getRegex() {
		return regex;
	}

	public String getValue() {
		return value;
	}

	// Comparisons: only based on name and regex pattern
	
	@Override
	public int hashCode() {
		return name.hashCode() + regex.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Token))
			return false;
		Token t = (Token) o;
		return t.name.equals(name) &&
		       t.regex.equals(regex);
	}
}
