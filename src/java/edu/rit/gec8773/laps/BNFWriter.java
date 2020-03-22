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
	 * Recursively writes parser rules to the writer starting at the
	 * argument parser. If the parser rule or the name of the parser
	 * rule is null, this method does nothing.
	 */
	public void writeGrammar(Parser parser) {
		if (parser == null)
			return;
		String name = parser.getName();
		String ruleString = parser.toString();
		if (name == null || ruleString == null) {
			if (parser.hasChildren())
				parser.forEach(this::writeGrammar);
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(name)
		  .append(parser.isList() ? " **= " : " ::= ")
		  .append(ruleString);
		writer.println(sb.toString());
		parser.forEach(this::writeGrammar);
		writer.flush();
	}

	@Override
	public void close() throws IOException {
		if (!isStdout)
			writer.close();
	}
}
