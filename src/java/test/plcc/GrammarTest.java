package test.plcc;

import plcc.Grammar;
import plcc.BNFWriter;
import plcc.Token;
import plcc.Resources;
import plcc.BasicScanner;
import numlist.NumList;

public class GrammarTest {
	public static void main(String[] args) {
		Resources resources = Resources.instance;
		Token[] tokens = {
			new Token("LPAREN", "\\("),
			new Token("RPAREN", "\\)"),
			new Token("NUMBER", "\\d+"),
			new Token("COMMA" , ",")
		};
		for (int i = 0; i < tokens.length; ++i)
			resources.addToken(tokens[i]);
		resources.addSkip("\\s+");
		try {
			Grammar head = Grammar.grammarRule(NumList.class);
			BNFWriter writer = new BNFWriter();
			writer.writeGrammar(head);
			System.out.println();
			BasicScanner sc = new BasicScanner(System.in);
			System.out.print("Test--> ");
			NumList nums = (NumList) head.parse(sc);
			nums.run();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
