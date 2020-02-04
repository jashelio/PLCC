package plcc;

import java.util.List;
import java.util.ArrayList;
import java.util.StringBuilder;

import plcc.Token;
import plcc.annotation.GrammarRule;

public abstract class Grammar {

	public abstract Object parse(Scanner sc);

	private List<Grammar> grammarRules;

	private Grammar(Object[] rules) {
		grammarRules = new ArrayList<>();
		for (Object rule : rules) {
			if (rule instanceof Grammar) {
				Grammar grammar = (Grammar) rule;
				grammarRules.add(grammar);
			} else if (Resources.getTokens().has(rule)) {
				grammarRules.add(Grammar.token(
							Resources.getTokens()
							.get(rule)
							)
						);
			} else if (rule instanceof Class<?>) {
				grammarRules.add(Grammar.rule((Class<?>)rule));
			} else
				throw new Error(rule + 
						" is not a token or " + 
						"another piece of grammar");
		}
	}

	public static Grammar or(Object... rules) {
		return new Grammar(rules) {
			@Override
			public Object parse(Scanner sc) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < grammarRules.size(); ++i) 
					try {
						return grammarRules.get(i).parse(sc);
					} catch (Exception e) {
						sb.append(e.getMessage());
						sb.append("\n");
					}
				throw new Error(sb.toString());
			}
		};

	}

	public static Grammar seq(Object... rules) {
		return new Grammar(rules) {
			@Override
			public Object parse(Scanner sc) {
				Object[] parsed = new Object[grammarRules.size()];
				for (int i = 0; i < grammarRules.size(); ++i)
					parsed[i] = grammarRules.get(i).parse(sc);
				return parsed;
			}
		};
	}

	private static Grammar token(Token token) {
		return new Grammar(new Object[0]) {
			@Override
			public Object parse(Scanner sc) {
				while (sc.hasSkip())
					sc.skip();
				if (!sc.hasNext(token)) {
					int line = sc.getLineNumber();
					//Token read = sc.getCurrentToken();
					// below doesn't let Grammar.or to work
					Token read = sc.nextToken();
					throw new Error("Expected token: <" + 
							token.getType() + 
							"> ::= " + 
							token.getRegex() + 
							" Recieved token: <" + 
							read.getType() + 
							"> ::= " + 
							read.getValue() +
							" on line " + line);
				return sc.nextToken(token);
			}
		};
	}

	public static Grammar rule(Class<?> cls) {
		GrammarRule annotation = cls.getAnnotation(GrammarRule.class);
		if (annotation == null)
			throw new Error(cls.getName() + " is not associated " + 
					"with a grammar rule");
		Object[] rules = annotation.rules();
		Grammar seqRule = Grammar.seq(rules);
		Class[] types = new Class[rules.length];
		for (int i = 0; i < types.length; ++i) 
			if (rules[i] instanceof Class<?>)
				types[i] = rules[i];
			else
				types[i] = rules[i].getClass();
		Constructor<?> constructor = cls.getConstructor(types);
		return new Grammar(new Object[0]) {
			@Override
			public Object parse(Scanner sc) {
				Object[] seq = seqRule.parse(sc);
				return constructor.newInstance(seq);
			}
		}
	}
}
