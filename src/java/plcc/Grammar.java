package plcc;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.regex.Pattern;
import java.util.HashSet;

import plcc.Token;
import plcc.annotation.AnnotationUtils;

public abstract class Grammar implements Serializable {

	public static final Grammar EMPTY = Grammar.seq();

	public abstract Object parse(Scanner sc);

	protected ArrayList<Grammar> grammarRules;

	private Grammar(Object[] rules) {
		grammarRules = new ArrayList<>();
		for (Object rule : rules) {
			if (rule instanceof Parameter) {
				Parameter param = (Parameter)rule;
				if (param.getType().equals(String.class)) {
					String name = param.getName();
					rule = Resources.instance.getToken(name);
				} else
					rule = param.getType();
			}
			if (rule instanceof Grammar) {
				Grammar grammar = (Grammar) rule;
				grammarRules.add(grammar);
			} else if (rule instanceof Token) {
				grammarRules.add(Grammar.token((Token)rule));
			} else if (rule instanceof Class<?>) {
				grammarRules.add(Grammar.grammarRule((Class<?>)rule));
			} else
				throw new Error(rule + 
						" is not a token or " + 
						"another piece of grammar");
		}
	}

	// For BNFWriter

	public String getName() {
		return null;
	}

	public boolean isList() {
		return false;
	}

	public String getRuleString() {
		return null;
	}

	public void forEachChild(Consumer<Grammar> consumer) {
		grammarRules.forEach(consumer);
	}

	public boolean hasChildren() {
		return !grammarRules.isEmpty();
	}

	// Factory methods

	private static Grammar seq(Object... rules) {
		return new Grammar(rules) {
			@Override
			public Object parse(Scanner sc) {
//				System.out.println("SEQ rule");
				Object[] parsed = new Object[grammarRules.size()];
				for (int i = 0; i < grammarRules.size(); ++i) {
					parsed[i] = grammarRules.get(i).parse(sc);
					if (parsed[i] == null)
						return null;
				}
				return parsed;
			}

			@Override
			public String getRuleString() {
				if (grammarRules.isEmpty())
					return "e";
				StringBuilder sb = new StringBuilder();
				for (Grammar child : grammarRules) 
					sb.append(child.getName() == null ? 
							child.getRuleString() : 
							child.getName())
						.append(" ");
				return sb.substring(0, sb.length() - 1); 
			}
		};
	}

	private static Grammar token(Token token) {
		StringBuilder sb = new StringBuilder();
		sb.append("<")
			.append(token.getName().toUpperCase())
			.append(">");
		String name = sb.toString();
		Pattern pattern = token.getRegex();
		return new Grammar(new Object[0]) {
			@Override
			public Object parse(Scanner sc) {
//				System.out.print(name + " token = ");
				if (!sc.hasNextToken(pattern)) {
//					System.out.println("null");
					return null;
				}
				String val = sc.nextToken(pattern).getValue();
//				System.out.println('"' + val + '"');
				return val;
			}

			@Override
			public String getRuleString() {
				return name;
			}
		};
	}

	private static HashSet<Class<?>> processing = new HashSet<>();
	public static Grammar grammarRule(Class<?> cls) {
		boolean isHead = processing.size() == 0;
		if (!AnnotationUtils.isGrammarRule(cls))
			throw new Error(cls.getName() + " is not associated " + 
					"with a grammar rule");
		StringBuilder sb = new StringBuilder(cls.getName());
		sb.insert(0, "<")
			.append(">")
			.setCharAt(1, Character.toLowerCase(sb.charAt(1)));
		String name = sb.toString();

		if (processing.add(cls)) {
			ArrayList<Object> ruleList = new ArrayList<>();
			for (Constructor<?> ctr : cls.getConstructors()) {
				Object[] rules = ctr.getParameters();
				Grammar seqRule = Grammar.seq(rules);
				ruleList.add(seqRule);
			}
			Object[] ruleArray = new Object[ruleList.size()];
			ruleArray = ruleList.toArray(ruleArray);
			Grammar result = new Grammar(ruleArray) {
				@Override
				public String getName() {
					return name;
				}

				@Override
				public Object parse(Scanner sc) {
//					System.out.println(name + " rule");
					Constructor<?>[] ctrs = cls.getConstructors();
					for (int i = 0; i < ctrs.length; ++i) {
						Grammar rule = grammarRules.get(i);
						Object[] parsed = (Object[])rule.parse(sc);
						if (parsed == null) // FIXME allows for tokens to get skipped
							continue;
						try {
							return ctrs[i].newInstance(parsed);
						} catch (Exception e) {
							System.err.println(e.getMessage());
							e.printStackTrace();
						}
					}
					return null;
				}

				@Override
				public String getRuleString() {
					StringBuilder sb = new StringBuilder();
					for (Grammar child : grammarRules) 
						sb.append(child.getName() == null ? 
								child.getRuleString() : 
								child.getName())
							.append(" | ");
					return sb.substring(0, sb.length() - 3);
				}
			};
			if (isHead)
				processing.clear();
			Resources.instance.addGrammar(cls, result);
			return result;
		} else {
			return new Grammar(new Object[0]) {
				@Override
				public String getName() {
					return name;
				}

				@Override
				public Object parse(Scanner sc) {
//					System.out.println(name + " closure");
					return Resources.instance.getGrammar(cls).parse(sc);
				}
			};
		}
	}
}
