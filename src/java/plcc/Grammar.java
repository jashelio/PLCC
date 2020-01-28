package plcc;

import java.util.List;
import java.util.ArrayList;
import java.util.StringBuilder;

import plcc.Token;
import plcc.AST;

public abstract class Grammar {

	public abstract AST parse(Scanner sc);

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
			} else
				throw new Error(rule + 
						" is not a token or " + 
						"another piece of grammar");
		}
	}

	public static Grammar or(Object... rules) {
		return new Grammar(rules) {
			@Override
			public AST parse(Scanner sc) {
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
			public AST parse(Scanner sc) {
				List<AST> parsed = new ArrayList<>();
				for (int i = 0; i < grammarRules.size(); ++i)
					parsed.add(grammarRules.get(i).parse(sc));
				return new AST(parsed);
			}
		};
	}

	private static Grammar token(Token token) {
		return new Grammar(new Object[0]) {
			@Override
			public AST parse(Scanner sc) {
				Token read = sc.nextToken();
				if (!token.isSameType(read))
					throw new Error("Expected token: <" + 
							token.getType() + 
							"> ::= " + 
							token.getRegex() + 
							" Recieved token: <" + 
							read.getType() + 
							"> ::= " + 
							read.getValue());
				return new AST(read);
			}
		};
	}
}
