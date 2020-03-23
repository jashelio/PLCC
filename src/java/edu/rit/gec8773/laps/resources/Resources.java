package edu.rit.gec8773.laps.resources;

import edu.rit.gec8773.laps.Parser;
import edu.rit.gec8773.laps.Token;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static java.text.DateFormat.*;
import static java.util.Locale.Category.FORMAT;

public class Resources implements Serializable {
// TODO	TEMP
	public static final Resources instance = new Resources();

	private Resources() {}

	public static Resources getInstance() {
// TODO	TEMP	if (instance == null)
//			instance = new Resources();
		return instance;
	}

	public static final String FILE_EXTENSION = ".laps";

	public static void save(String fileName) throws IOException {
		File file = new File(fileName);
		if (file.exists()) {
			if (file.isDirectory())
				throw new IOException("Can't write to file: " +
						fileName + " is directory");
		} else
			file.createNewFile();
		try (FileOutputStream fileOut = new FileOutputStream(file);
			 ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
			out.writeUnshared(instance);
			out.flush();
		}
	}

	private static DateFormat formatter = DateFormat.getDateTimeInstance(SHORT, LONG, Locale.getDefault(FORMAT));
	public static void save() throws IOException {
		String filename = formatter.format(new Date())
				.replaceAll("[/, ]", "-")
				.replaceAll(":",".") +
				FILE_EXTENSION;
		save(filename);
	}

	public static void load() throws IOException {
		File dir = new File(".");
		if (!dir.exists() || !dir.isDirectory())
			throw new IOException(dir.getAbsolutePath() + " does " +
					"not exist as a directory");
		Date newest = null;
		int newestIndex = -1;
		newest = new Date(0);
		File[] files = Objects.requireNonNull(dir.listFiles());
		for (int i = 0; i < files.length; ++i)
			try {
				File file = files[i];
				if (!file.getAbsolutePath().endsWith(FILE_EXTENSION))
					continue;
				int fileNameLength = file.getName().length()
									 - FILE_EXTENSION.length();
				String dateString = file.getName()
						.substring(0, fileNameLength)
						.replaceFirst("-","/")
						.replaceFirst("-","/")
						.replaceFirst("-", ",")
						.replaceAll("-", " ")
						.replaceAll("\\.", ":");
				Date date = formatter.parse(dateString);
				if (date.after(newest)) {
					newest = date;
					newestIndex = i;
				}
			} catch (ParseException e) { }
		if (newestIndex == -1)
			throw new IOException("No language file (" + FILE_EXTENSION +
					") found  in " + dir.getAbsolutePath());
		load(files[newestIndex].getAbsolutePath());
	}

	public static void load(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists())
			throw new IOException("File " + fileName + 
					" does not exist");
		if (file.isDirectory())
			throw new IOException(fileName + " is a directory");
		if (!fileName.endsWith(FILE_EXTENSION))
			System.err.println("Warning: not loading from a file with \"" +
					FILE_EXTENSION + "\" as a file extension");
		try (ObjectInputStream in = new ObjectInputStream(
					new FileInputStream(file))) {
				Object o = in.readUnshared();
				if (o instanceof Resources)
					instance.load((Resources) o);
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} // TODO
	}

	private void load(Resources r) {
		this.tokenStorage = r.tokenStorage;
		this.parserStorage = r.parserStorage;
		tokenStorage.forEachToken( token -> token.getRegex()
				.matcher("for Java 13 bug").matches());
	}

	private boolean debug = false;

	public boolean debugEnabled() {
		return debug;
	}

	public void enableDebugOutput() {
		this.debug = true;
	}

	private TokenStorage tokenStorage = new TokenStorage();

	public boolean hasToken(Token token) {
		return tokenStorage.hasToken(token);
	}

	public boolean hasToken(Pattern pattern) {
		return tokenStorage.hasToken(pattern);
	}

	public boolean hasToken(String name) {
		return tokenStorage.hasToken(name);
	}

	public Token getToken(Pattern pattern) {
		return tokenStorage.getToken(pattern);
	}

	public Token getToken(String name) {
		return tokenStorage.getToken(name);
	}

	public Set<Pattern> getPatterns() {
		return tokenStorage.getPatterns();
	}

	public boolean addToken(Token token) {
		return tokenStorage.addToken(token);
	}

	public void forEachToken(Consumer<Token> consumer) {
		tokenStorage.forEachToken(consumer);
	}

	public boolean addSkip(Pattern pattern) {
		return tokenStorage.addSkip(pattern);
	}

	public boolean addSkip(String pattern) {
		return tokenStorage.addSkip(pattern);
	}

	public void forEachSkip(Consumer<Pattern> consumer) {
		tokenStorage.forEachSkip(consumer);
	}

	public Set<Pattern> getSkips() {
		return tokenStorage.getSkips();
	}

	private ParserStorage parserStorage = new ParserStorage();

	public Parser getParser(Class<?> c) {
		return parserStorage.getParser(c);
	}

	public boolean hasParser(Class<?> c) {
		return parserStorage.hasParser(c);
	}

	public boolean addParser(Class<?> c, Parser parser) {
		return parserStorage.addParser(c, parser);
	}

	public Parser getParserHead() {
		return parserStorage.getParserHead();
	}

	public void setParserHead(Parser parser) {
		parserStorage.setParserHead(parser);
	}
}
