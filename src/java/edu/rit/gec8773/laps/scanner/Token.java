package edu.rit.gec8773.laps.scanner;

import java.util.Objects;
import java.util.regex.Pattern;
import java.io.Serializable;

/**
 * Stores the name of a token, the regular expression associated with
 * that name, and, optionally, a value matched to
 * the regular expression
 */
public class Token implements Serializable {

	/**
	 * The name string
	 */
	private String name;

	/**
	 * The compiled regular expression pattern
	 */
	private Pattern regex;

	/**
	 * The optional string value which matched the regular expression
	 */
	private String value;

	/**
	 * The main Class constructor checks all the arguments for appropriate
	 * values, e.g. name and regex must be nonnull, and value must be matched
	 * by the regular expression if value is nonnull
	 *
	 * @param name the name of the token
	 * @param regex the compiled regular expression pattern
	 * @param value the optional value of the token
	 */
	private Token(String name, Pattern regex, String value) {
		this.name = Objects.requireNonNull(name);
		this.regex = Objects.requireNonNull(regex);
		this.value = value;
		if (value != null && !regex.matcher(value).matches())
			throw new IllegalArgumentException("\"" + value + "\"" +
					" must match the pattern " + regex);
	}

	/**
	 * Class constructor sets the token value to null
	 *
	 * @param name the name of the token
	 * @param regex the compiled regular expression pattern
	 *
	 * @see Token#Token(String, Pattern, String)
	 */
	public Token(String name, Pattern regex) {
		this(name, regex, null);
	}

	/**
	 * Class constructor sets the token value to null and compiles the regular
	 * expression pattern
	 *
	 * @param name the name of the token
	 * @param pattern the source string regular expression pattern
	 *
	 * @see Token#Token(String, Pattern, String)
	 */
	public Token(String name, String pattern) {
		this(name, Pattern.compile(pattern));
	}

	/**
	 * Class constructor sets the token to have the same name and regular
	 * expression pattern as a given template and sets the given value
	 *
	 * @param template the token of the same type
	 * @param value the value which matches the template's regular expression
	 *
	 * @see Token#Token(String, Pattern, String)
	 */
	public Token(Token template, String value) {
		this(template.name, template.regex, value);
	}
	
	// Getters

	/**
	 * Retrieves the token's name
	 *
	 * @return {@link Token#name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves the token's regular expression pattern
	 *
	 * @return {@link Token#regex}
	 */
	public Pattern getRegex() {
		return regex;
	}

	/**
	 * Retrieves the token's value
	 *
	 * @return {@link Token#value}
	 */
	public String getValue() {
		return value;
	}

	// Comparisons only based on name and regex pattern

	/**
	 *
	 * {@inheritDoc}
	 *
	 */
	@Override
	public int hashCode() {
		return name.hashCode() + regex.hashCode();
	}

	/**
	 *
	 * {@inheritDoc}
	 *
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Token))
			return false;
		Token t = (Token) o;
		return t.name.equals(name) &&
		       t.regex.equals(regex);
	}
}
