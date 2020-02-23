package plcc;

import java.io.PrintWriter;
import java.io.PrintStream;
import java.util.List;

import plcc.Grammar;

public class BNFWriter {

	private PrintWriter writer;

	public BNFWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public BNFWriter(PrintStream stream) {
		this(new PrintWriter(stream));
	}

	public BNFWriter() {
		this(System.out);
	}

	/**
	 * Recursively writes grammar rules to the writer starting at the 
	 * argument grammar. If the grammar rule or the name of the grammar
	 * rule is null, this method does nothing.
	 */
	public void writeGrammar(Grammar grammar) {
		if (grammar == null)
			return;
		String name = grammar.getName();
		String ruleString = grammar.getRuleString();
		if (name == null || ruleString == null) {
			if (grammar.hasChildren())
				grammar.forEachChild(this::writeGrammar);
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(name)
		  .append(grammar.isList() ? " **= " : " ::= ")
		  .append(ruleString);
		writer.println(sb.toString());
		grammar.forEachChild(this::writeGrammar);
		writer.flush();
	}
}
