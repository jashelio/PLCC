package plcc;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.util.Stack;
import java.io.IOException;
import java.util.stream.Stream;

import plcc.Token;
import plcc.annotation.*;

public abstract class Grammar implements Serializable {

	public static final Grammar EMPTY = Grammar.seq();

	public abstract Object parse(Scanner sc) throws IOException;

	public abstract void returnTokens(Scanner sc);

	protected ArrayList<Grammar> grammarRules;

	private Grammar(Object[] rules) {
		grammarRules = new ArrayList<>();
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
			if (rule instanceof Grammar) {
				Grammar grammar = (Grammar) rule;
				grammarRules.add(grammar);
			} else if (rule instanceof Token) {
				grammarRules.add(Grammar.token((Token)rule));
			} else if (rule instanceof Class<?>) {
				grammarRules.add(Grammar.grammarRule((Class<?>)rule));
			} else
				throw new Error(rule + 
						" is not a defined token"); 
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

	public void forEachChild(Consumer<Grammar> consumer) {
		grammarRules.forEach(consumer);
	}

	public boolean hasChildren() {
		return !grammarRules.isEmpty();
	}

	// Factory methods

	private static Grammar seq(Object... rules) {
		return new Grammar(rules) {
//			private Object[] parsed = null; // TODO make Stack<Object[]>
			private Stack<Integer> acceptedRuleIndexes = new Stack<>();

			@Override
			public Object parse(Scanner sc) throws IOException {
//	/*DEBUG*/		System.out.println("SEQ rule");
				Object[] parsed = new Object[grammarRules.size()];
				for (int i = 0; i < grammarRules.size(); ++i) {
					parsed[i] = grammarRules.get(i).parse(sc);
					if (parsed[i] == null) {
						return null;
					}
					acceptedRuleIndexes.push(i);
				}
				return parsed;
			}
			
			@Override
			public void returnTokens(Scanner sc) {
//	/*DEBUG*/		System.out.println("SEQ returning tokens");
				Integer i = acceptedRuleIndexes.isEmpty() ?
					null : acceptedRuleIndexes.pop();
				if (i == null)
					return;
				for (;!acceptedRuleIndexes.isEmpty(); i = acceptedRuleIndexes.pop()) {
					grammarRules.get(i).returnTokens(sc);
					if (i == 0)
						break;
				}
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
			Consumer<Void> runOnce = AnnotationUtils.getStaticMethod(cls, 
					RunBeforeFirstInit.class);
			runOnce.accept(null);
			ArrayList<Grammar> ruleList = new ArrayList<>();
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
				Grammar seqRule = Grammar.seq(rules);
				for (Grammar rule : ruleList) // TODO make Grammar.equals()
					if (rule.grammarRules
						.get(0)
						.getRuleString()
						.startsWith(
							seqRule.grammarRules
							       .get(0)
							       .getRuleString()
							))
						throw new Error("Two or more grammar" + 
								" rules have the same" + 
								" starting rule in " + cls);
				ruleList.add(seqRule);
			}
			int emptyIndex = i;
			boolean acceptEmpty = isAcceptEmpty;
			if (acceptEmpty)
				ruleList.add(Grammar.seq());
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
			Grammar result = new Grammar(ruleArray) {
				private Stack<Grammar> acceptedRulesStack = new Stack<>();
				// TODO make use of callstack instead
				@Override
				public String getName() {
					return name;
				}

				@Override
				public Object parse(Scanner sc) throws IOException {
//	/*DEBUG*/			System.out.println(name + " rule");
					Consumer<Void> beforeEach = AnnotationUtils.getStaticMethod(cls,
							RunBeforeEachInit.class);
					Consumer<Object> runAfter = AnnotationUtils.getInstanceMethod(cls,
							SemanticEntryPoint.class);
					Constructor<?>[] ctrs = cls.getConstructors();
					beforeEach.accept(null);
					for (int i = 0; i < ctrs.length; ++i) {
						if (acceptEmpty && i == emptyIndex)
							continue;
						Grammar rule = grammarRules.get(ctrToRule[i]);
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
						int i = emptyIndex;
						acceptedRulesStack.push(EMPTY);
						try {
							return ctrs[i].newInstance();
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
					Grammar rule = acceptedRulesStack.empty() ? 
						null : acceptedRulesStack.pop();
					if (rule == null)
						return;
					rule.returnTokens(sc);
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

				@Override
				public Class<?> getGrammarClass() {
					return cls;
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
				public void returnTokens(Scanner sc) {
					Resources.instance.getGrammar(cls).returnTokens(sc);
				}

				@Override
				public Object parse(Scanner sc) throws IOException {
//					System.out.println(name + " closure");
					return Resources.instance.getGrammar(cls).parse(sc);
				}
			};
		}
	}
}
