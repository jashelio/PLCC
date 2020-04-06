package edu.rit.gec8773.laps.parser.topDown;

import edu.rit.gec8773.laps.util.AnnotationUtils;
import edu.rit.gec8773.laps.parser.Parser;
import edu.rit.gec8773.laps.resources.Resources;
import edu.rit.gec8773.laps.scanner.Scanner;
import edu.rit.gec8773.laps.scanner.Token;
import edu.rit.gec8773.laps.util.BNFWriter;
import edu.rit.gec8773.laps.util.TokenCollector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * {@link TopDownParser} builds parsing graphs (allowing cycles) of type
 * {@link TopDownParser}. These graphs work with {@link Scanner}s to parse input.
 *
 * @see Scanner
 */
public abstract class TopDownParser implements Parser, BNFWriter.Writable {

	/**
	 * A parser which accepts the empty string
	 */
	public static final TopDownParser EMPTY;

	static {
		TopDownParser temp;
		try {
			temp = TopDownParser.seq();
		} catch (InvocationTargetException e) {
			temp = null;
		}
		EMPTY = temp;
	}

	protected final ArrayList<Parser> parsingRules;

	/**
	 * Collects all the potential rules and recursively builds the parser tree.
	 * A list of potential rules include:
	 * 	<ul>
	 * 	 <li>{@link Parameter} describing a token or another grammar class</li>
	 * 	 <li>{@link TopDownParser} containing a constructed parser</li>
	 * 	 <li>{@link Token}</li>
	 * 	 <li>{@link Class} representing another grammar rule</li>
	 * 	</ul>
	 * @param rules 0 or more potential rules
	 * @throws InvocationTargetException when the user provides a token which
	 * is not recognized or when another exception generated by the user
	 */
	TopDownParser(Object[] rules) throws InvocationTargetException {
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
			if (rule instanceof TopDownParser) {
				TopDownParser topDownParser = (TopDownParser) rule;
				parsingRules.add(topDownParser);
			} else if (rule instanceof Token) {
				parsingRules.add(TopDownParser.token((Token)rule));
			} else if (rule instanceof Class<?>) {
				parsingRules.add(TopDownParser.grammarRule((Class<?>)rule));
			} else
				throw new InvocationTargetException(
						new Exception(rule + " is not a defined token; don't " +
								"forget to add the \"-parameters\" compilation" +
								" flag to javac"));
		}
	}

	// For BNFWriter

	/**
	 * Returns the name of the paring rule
	 * @return the name
	 */
	@Override
	public String getName() {
		return null;
	}

	@Override
	public String toString() {
		return "";
	}

	/**
	 * Returns an iterator over elements of type {@link TopDownParser}, the children
	 * of {@code this}
	 *
	 * @return an {@link Iterator}
	 */
	@Override
	public Iterator<BNFWriter.Writable> iterator() {
		return parsingRules.parallelStream()
						   .map(BNFWriter.Writable.class::cast)
						   .iterator();
	}

	/**
	 * Checks if the parser has any sub parsers
	 * @return true if the parser has any sub parsers
	 */
	@Override
	public boolean hasChildren() {
		return !parsingRules.isEmpty();
	}

	// Factory methods

	/**
	 * Creates a parser which accepts a sequence of tokens or other grammar
	 * rules. This grammar rules are typically created by
	 * {@link TopDownParser#grammarRule(Class)}.
	 * @see TopDownParser#TopDownParser(Object[]) for all acceptable types
	 * @param rules an array of 0 or more grammatical elements
	 * @return the parser
	 * @throws InvocationTargetException when the user causes an exception
	 */
	private static TopDownParser seq(Object... rules)
			throws InvocationTargetException {
		return new SequentialParser(rules);
	}

	/**
	 * Creates a parser which accepts tokens of the same type as the passed in
	 * token.
	 * @param token of the token type which should be accepted
	 * @return the parser
	 * @throws InvocationTargetException when the user causes an exception
	 */
	private static Parser token(Token token) throws InvocationTargetException {
		return new TokenParser(token);
	}

	private static final HashSet<Class<?>> processing = new HashSet<>();

	/**
	 * Creates a parser from the passed in Class using the class's constructors
	 * as acceptable rules for parsing.
	 * @param cls the class used as the skeleton
	 * @return the parser constructed
	 * @throws InvocationTargetException when the user causes an exception
	 */
	public static Parser grammarRule(Class<?> cls)
			throws InvocationTargetException {
		if (Resources.instance.hasParser(cls))
			return Resources.instance.getParser(cls);
		boolean isHead = processing.size() == 0;
		if (!AnnotationUtils.isGrammarRule(cls))
			throw new InvocationTargetException(
						new Exception((cls.getName() + " is not associated " + 
					"with a grammar rule")));

		if (processing.add(cls)) {
			TokenCollector.collect(cls);
			HashMap<List<Class>, Parser> ruleMap = new HashMap();
			for (Constructor<?> ctr : cls.getConstructors()) {
				Parameter[] rules = ctr.getParameters();
				if (rules.length == 0) {
					if (isHead)
						throw new InvocationTargetException(
								new Exception((cls + " cannot accept an empty" +
										" grammar rule since it's the top of " +
										"the context free grammar")));
					ruleMap.put(List.of(), EMPTY);
					continue;
				}
				TopDownParser seqRule = TopDownParser.seq((Object[])rules);
				// TODO add a better check for rules starting with same token
//				for (TopDownParser rule : ruleMap.values()) // TODO make TopDownParser.equals()
//					if (!(rule == EMPTY || seqRule == EMPTY) &&
//							rule.parsingRules
//								.get(0)
//								.toString()
//								.equals(
//									seqRule.parsingRules
//										   .get(0)
//										   .toString()
//							))
//						throw new InvocationTargetException(
//						new Exception(("Two or more grammar" +
//								" rules have the same" +
//								" starting rule in " + cls +
//								"\n    Found: " + rule.parsingRules
//								.get(0).toString() + " and " +
//								seqRule.parsingRules
//										.get(0)
//										.toString())));
				List<Class> types = Arrays.stream(rules)
									  .map(Parameter::getType)
									  .collect(Collectors.toList());
				ruleMap.put(types, seqRule);
			}
			if (ruleMap.size() == 0)
				throw new InvocationTargetException(
						new Exception((cls + " has no accessible grammar " +
								"rules make sure at least one constructor " +
								"is public")));
			Parser result = new SkeletalClassParser(cls, ruleMap);
			if (isHead)
				processing.clear();
			Resources.instance.addParser(cls, result);
			return result;
		} else
			return new SkeletalClassParserReference(cls);
	}
}
