package edu.rit.gec8773.laps;

import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BNFWriter implements AutoCloseable {

	private PrintWriter writer;
	private boolean isStdout = false;

	public BNFWriter(File file) throws FileNotFoundException {
		this(new PrintWriter(file));
	}

	public BNFWriter(String filename) throws FileNotFoundException {
		this(new PrintWriter(filename));
	}

	public BNFWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public BNFWriter(PrintStream stream) {
		this(new PrintWriter(stream));
		isStdout = stream == System.out;
	}

	public BNFWriter() {
		this(System.out);
		isStdout = true;
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

	@Override
	public void close() throws IOException {
		if (!isStdout)
			writer.close();
	}
}
