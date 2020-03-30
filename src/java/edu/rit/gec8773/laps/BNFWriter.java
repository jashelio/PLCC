package edu.rit.gec8773.laps;

import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

/**
 * A {@link BNFWriter} is used to print {@link Parser}s recursively in a
 * Backus–Naur form (BNF) style grammar definition
 */
public class BNFWriter implements AutoCloseable {

	/**
	 * A {@link PrintWriter} which writes to an appropriate destination
	 */
	private PrintWriter writer;

	/**
	 * Set to true if {@link System#out} is the {@link BNFWriter#writer}'s
	 * destination
	 */
	private boolean isStdout = false;

	/**
	 * Class constructor, print to a given {@link File}
	 *
	 * @param file the {@link File} to print to
	 */
	public BNFWriter(File file) throws FileNotFoundException {
		this(new PrintWriter(file));
	}

	/**
	 * Class constructor, print to a {@link File} with a given filename
	 *
	 * @param filename the name of the {@link File} to print to
	 */
	public BNFWriter(String filename) throws FileNotFoundException {
		this(new PrintWriter(filename));
	}

	/**
	 * Class constructor, print to the {@link PrintWriter} argument
	 *
	 * @param writer the {@link PrintWriter} to print to
	 */
	public BNFWriter(PrintWriter writer) {
		this.writer = Objects.requireNonNull(writer);
	}

	/**
	 * Class constructor, print to the {@link PrintStream} argument
	 *
	 * @param stream the {@link PrintStream} to print to
	 */
	public BNFWriter(PrintStream stream) {
		this(new PrintWriter(stream));
		isStdout = stream == System.out;
	}

	/**
	 * Class constructor, by default print to {@link System#out}
	 */
	public BNFWriter() {
		this(System.out);
	}

	/**
	 * Recursively writes parsing rules to the writer starting at the
	 * argument parser. If the parsing rule or the name of the parser
	 * rule is null, this method does nothing.
	 *
	 * @param parser the head of the parsing graph
	 */
	public void writeGrammar(Parser parser) {
		if (parser == null)
			return;
		String name = parser.getName();
		String ruleString = parser.toString();
		if (name == null || ruleString.equals("")) {
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

	/**
	 * Closes {@link BNFWriter#writer} unless it is {@link System#out}
	 *
	 * @throws IOException
	 */
	@Override
	public void close() throws IOException {
		if (!isStdout)
			writer.close();
	}
}
