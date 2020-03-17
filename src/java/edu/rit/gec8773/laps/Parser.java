package edu.rit.gec8773.laps;

import edu.rit.gec8773.laps.annotation.AnnotationUtils;
import edu.rit.gec8773.laps.annotation.RunBeforeEachInit;
import edu.rit.gec8773.laps.annotation.RunBeforeFirstInit;
import edu.rit.gec8773.laps.annotation.SemanticEntryPoint;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class Parser implements Serializable {

	public static final Parser EMPTY;

	static {
		Parser temp;
		try {
			temp = Parser.seq();
		} catch (InvocationTargetException e) {
			temp = null;
		}
		EMPTY = temp;
	}

	public abstract Object parse(Scanner sc) throws IOException, InvocationTargetException;

	public abstract void returnTokens(Scanner sc);

	protected ArrayList<Parser> parsingRules;

	private Parser(Object[] rules) throws InvocationTargetException {
		parsingRules = new ArrayList<>();
		for (Object rule : rules) {
			if (rule instanceof Parameter) {
				Parameter param = (Parameter)rule;
				if (param.getType().equals(String.class)) {
					String name = param.getName();
					rule = Resources.instance.getToken(name);
					if (rule == null)
						rule = name;
				} else
					rule = param.getType();
			}
			if (rule instanceof Parser) {
				Parser parser = (Parser) rule;
				parsingRules.add(parser);
			} else if (rule instanceof Token) {
				parsingRules.add(Parser.token((Token)rule));
			} else if (rule instanceof Class<?>) {
				parsingRules.add(Parser.grammarRule((Class<?>)rule));
			} else
				throw new InvocationTargetException(
						new Exception(rule + 
						" is not a defined token")); 
		}
	}

	// For Lookup 
	
	public Class<?> getGrammarClass() {
		return null;
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

	public void forEachChild(java.util.function.Consumer<Parser> consumer) {
		parsingRules.forEach(consumer);
	}

	public boolean hasChildren() {
		return !parsingRules.isEmpty();
	}

	// Factory methods

	private static Parser seq(Object... rules) throws InvocationTargetException {
		return new Parser(rules) {
//			private Object[] parsed = null; // TODO make Stack<Object[]>
			private Stack<Integer> lastAcceptedRuleIndex = new Stack<>();

			@Override
			public Object parse(Scanner sc) throws IOException, InvocationTargetException {
//	/*DEBUG*/		System.out.println("SEQ rule");
				Object[] parsed = new Object[parsingRules.size()];
				for (int i = 0; i < parsingRules.size(); ++i) {
					parsed[i] = parsingRules.get(i).parse(sc);
					if (parsed[i] == null) {
						lastAcceptedRuleIndex.push(i);
						return null;
					}
				}
				lastAcceptedRuleIndex.push(parsingRules.size());
				return parsed;
			}
			
			@Override
			public void returnTokens(Scanner sc) {
				if (this == EMPTY)
					return;
//	/*DEBUG*/		System.out.println("SEQ returning tokens");
				Integer i = lastAcceptedRuleIndex.isEmpty() ?
					null : lastAcceptedRuleIndex.pop();
				if (i == null)
					return;
				for (--i; i >= 0; --i) 
					parsingRules.get(i).returnTokens(sc);
			}

			@Override
			public String getRuleString() {
				if (parsingRules.isEmpty())
					return "e";
				StringBuilder sb = new StringBuilder();
				for (Parser child : parsingRules)
					sb.append(child.getName() == null ? 
							child.getRuleString() : 
							child.getName())
						.append(" ");
				return sb.substring(0, sb.length() - 1); 
			}
		};
	}

	private static Parser token(Token token) throws InvocationTargetException {
		StringBuilder sb = new StringBuilder();
		sb.append("<")
			.append(token.getName().toUpperCase())
			.append(">");
		String name = sb.toString();
		Pattern pattern = token.getRegex();
		return new Parser(new Object[0]) {
//			private Token tok = null; // TODO make Stack<Token>
			private Stack<Token> tokStack = new Stack<>();
			// TODO make use of callstack instead

			@Override
			public Object parse(Scanner sc) throws IOException {
				sc.skip();
//	/*DEBUG*/		System.out.print(name + " token = ");
				if (!sc.hasNextToken(pattern)) {
//	/*DEBUG*/			System.out.println("null");
					return null;
				}
				Token tok = sc.nextToken(pattern);
				String val = tok.getValue();
//	/*DEBUG*/		System.out.println('"' + val + '"');
				tokStack.push(tok);
				return tok;
			}
			
			@Override
			public void returnTokens(Scanner sc) {
				Token tok = tokStack.empty() ? 
					null : tokStack.pop();
				if (tok == null)
					return;
//	/*DEBUG*/		System.out.println("Returning: " + tok.getValue());
				sc.unreadToken(tok);
			}

			@Override
			public String getRuleString() {
				return name;
			}
		};
	}

	private static HashSet<Class<?>> processing = new HashSet<>();
	public static Parser grammarRule(Class<?> cls) throws InvocationTargetException {
		boolean isHead = processing.size() == 0;
		if (!AnnotationUtils.isGrammarRule(cls))
			throw new InvocationTargetException(
						new Exception((cls.getName() + " is not associated " + 
					"with a grammar rule")));

		TokenCollector tokenCollector = new TokenCollector(cls);
		tokenCollector.collect();

		StringBuilder sb = new StringBuilder(cls.getSimpleName());
		sb.insert(0, "<")
			.append(">")
			.setCharAt(1, Character.toLowerCase(sb.charAt(1)));
		String name = sb.toString();

		if (processing.add(cls)) {
			ArrayList<Parser> ruleList = new ArrayList<>();
			boolean isAcceptEmpty = false;
			int i = 0;
			for (Constructor<?> ctr : cls.getConstructors()) {
				Object[] rules = ctr.getParameters();
				if (rules.length == 0) {
					isAcceptEmpty = true;
					continue;
				}
				if (!isAcceptEmpty)
					++i;
				Parser seqRule = Parser.seq(rules);
				for (Parser rule : ruleList) // TODO make Parser.equals()
					if (rule.parsingRules
						.get(0)
						.getRuleString()
						.startsWith(
							seqRule.parsingRules
							       .get(0)
							       .getRuleString()
							))
						throw new InvocationTargetException(
						new Exception(("Two or more grammar" + 
								" rules have the same" + 
								" starting rule in " + cls)));
				ruleList.add(seqRule);
			}
			int emptyIndex = i;
			boolean acceptEmpty = isAcceptEmpty;
			if (acceptEmpty)
				ruleList.add(Parser.seq());
			int[] ctrToRule = new int[ruleList.size()];
			for (i = 0; i < ctrToRule.length; ++i) 
				if (i < emptyIndex)
					ctrToRule[i] = i;
				else if (i == emptyIndex)
					ctrToRule[i] = ruleList.size() - 1;
				else
					ctrToRule[i] = i - 1;
			Object[] ruleArray = new Object[ruleList.size()];
			ruleArray = ruleList.toArray(ruleArray);
			Parser result = new Parser(ruleArray) {
				private Stack<Parser> acceptedRulesStack = new Stack<>();
				// TODO make use of callstack instead
				private boolean ranOnce = false;

				@Override
				public String getName() {
					return name;
				}

				@Override
				public Object parse(Scanner sc) throws IOException, InvocationTargetException {
//	/*DEBUG*/			System.out.println(name + " rule");
					if (!ranOnce) {
						Consumer<Void> runOnce = AnnotationUtils.getStaticMethod(cls,
								RunBeforeFirstInit.class);
						runOnce.accept(null);
						ranOnce = true;
					}
					Consumer<Void> beforeEach = AnnotationUtils.getStaticMethod(cls,
							RunBeforeEachInit.class);
					Consumer<Object> runAfter = AnnotationUtils.getInstanceMethod(cls,
							SemanticEntryPoint.class);
					Constructor<?>[] ctrs = cls.getConstructors();
					beforeEach.accept(null);
					for (int i = 0; i < ctrs.length; ++i) {
						if (acceptEmpty && i == emptyIndex)
							continue;
						Parser rule = parsingRules.get(ctrToRule[i]);
						Object[] parsed = (Object[])rule.parse(sc);
						if (parsed == null) {
							rule.returnTokens(sc);
							continue;
						}
						acceptedRulesStack.push(rule);
						parsed = Stream.of(parsed)
							       .map( elm -> elm instanceof Token ? 
									((Token)elm).getValue() : 
									elm )
							       .toArray();
						try {
							Object AST = ctrs[i].newInstance(parsed);
							runAfter.accept(AST);
							return AST;
						} catch (Exception e) {
							// should never happen
							// unless exception in USER code TODO
							System.err.println(e.getMessage());
							e.printStackTrace();
						}
					}
					if (acceptEmpty) {
						acceptedRulesStack.push(EMPTY);
						try {
							return ctrs[emptyIndex].newInstance();
						} catch (Exception e) {
							// should never happen
							// unless exception in USER code TODO
							System.err.println(e.getMessage());
							e.printStackTrace();
						}
					}
					return null;
				}

				@Override
				public void returnTokens(Scanner sc) {
//	/*DEBUG*/			System.out.println(name + " returning tokens");
					Parser rule = acceptedRulesStack.empty() ?
						null : acceptedRulesStack.pop();
					if (rule == null)
						return;
					rule.returnTokens(sc);
				}

				@Override
				public String getRuleString() {
					StringBuilder sb = new StringBuilder();
					for (Parser child : parsingRules)
						sb.append(child.getName() == null ? 
								child.getRuleString() : 
								child.getName())
							.append(" | ");
					return sb.substring(0, sb.length() - 3);
				}

				@Override
				public Class<?> getGrammarClass() {
					return cls;
				}
			};
			if (isHead)
				processing.clear();
			Resources.instance.addParser(cls, result);
			return result;
		} else {
			return new Parser(new Object[0]) {
				@Override
				public String getName() {
					return name;
				}

				@Override
				public void returnTokens(Scanner sc) {
					Resources.instance.getParser(cls).returnTokens(sc);
				}

				@Override
				public Object parse(Scanner sc) throws IOException, InvocationTargetException {
//					System.out.println(name + " closure");
					return Resources.instance.getParser(cls).parse(sc);
				}
			};
		}
	}
}
