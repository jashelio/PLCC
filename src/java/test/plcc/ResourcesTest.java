package test.plcc;

import plcc.Grammar;
import plcc.BNFWriter;
import plcc.Token;
import plcc.Resources;
import plcc.BasicScanner;
import plcc.Scanner;
import numlist.NumList;

public class ResourcesTest {
	public static void main(String[] args) {
		try {
			saveTest();
			loadTest();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void loadTest() throws Exception {
		Resources resources = Resources.instance;
		System.out.print("Loading...");
		resources.load("NumList.lang");
		System.out.println("done");
		Grammar head = resources.getGrammarHead();
		BNFWriter writer = new BNFWriter();
		writer.writeGrammar(head);
		Scanner sc = new BasicScanner(System.in);
		System.out.print("Test--> ");
	}

	private static void saveTest() throws Exception {
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
		Grammar head = Grammar.grammarRule(NumList.class);
		System.out.println();
		resources.setGrammarHead(head);
		System.out.print("Saving...");
		System.out.flush();
		resources.save("NumList.lang");
		System.out.println("done");
	}
}
