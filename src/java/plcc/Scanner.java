package plcc.Scanner;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import plcc.*;

class Scanner {
	static class Token {
		public final String value;
		public final Field type;

		Token(Field type, String value) {
			this.type = type;
			this.value = value;
		}
	}

	private java.util.Scanner sc;

	private Map<String, Field> tokens;
	private Set<String> skips; // I don't think Field is neccessary?

	private Scanner() {
		tokens = PLCC.getTokens();
		skips = PLCC.getSkips();
	}

	Scanner(InputStream input) {
		this();
		sc = new java.util.Scanner(input);
	}

	Scanner(File input) {
		this();
		sc = new java.util.Scanner(input);
	}

	Scanner(String input) {
		this();
		sc = new java.util.Scanner(input);
	}

	private boolean hasNext(Set<String> patterns) {
		for (String pattern : patterns)
			if (sc.hasNext(pattern))
				return true;
		return false;
	}
	
	boolean hasNextToken() {
		return hasNext(tokens.keySet());
	}

	boolean hasSkip() {
		return hasNext(skips.keySet());
	}

	Token nextToken() {
		String value = null;
		String pattern;
		for (pattern : tokens.keySet()) {
			if (!sc.hasNext(pattern))
				continue;
			value = sc.next(pattern);
			break;
		}
		if (value == null)
			return null;
		return new Token(tokens.get(pattern), value);
	}

	void skip() {
		for (String pattern : skips) {
			if (!sc.hasNext(pattern))
				continue;
			sc.skip(pattern);
		}
	}
}
