package edu.rit.gec8773.laps;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class Resources implements Serializable {
// TODO	TEMP
	public static final Resources instance = new Resources();

	private Resources() {}

	public static Resources getInstance() {
// TODO	TEMP	if (instance == null)
//			instance = new Resources();
		return instance;
	}

	public static final String FILE_EXTENSION = ".resources";

	public static void save(String fileName) throws IOException {
		File file = new File(fileName);
		if (file.exists()) {
			if (file.isDirectory())
				throw new IOException("Can't write to file: " +
						fileName + " is directory");
//			else
//				System.err.println("Warning: overwriting " + 
//						"existing file " + fileName);
		}
		try (ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(file))) {
			out.writeUnshared(instance);
		}
	}

	public static void save() throws IOException {
		DateFormat formatter = DateFormat.getInstance();
		save(formatter.format(new Date()) + FILE_EXTENSION);
	}

	public static void load() throws IOException {
		DateFormat formatter = DateFormat.getInstance();
		File dir = new File(".");
		if (!dir.exists() || !dir.isDirectory())
			throw new IOException(dir.getAbsolutePath() + " does " +
					"not exist as a directory");
		File loadFrom = null;
		Date newest = new Date(0);
		for (File file : Objects.requireNonNull(dir.listFiles()))
			try {
				Date date = formatter.parse(file.getName().
						substring(0, 
							file.getName().length() - 
							FILE_EXTENSION.length()));
				if (date.after(newest))
					newest = date;
			} catch (ParseException e) { }
		if (newest.equals(new Date(0)))
			throw new IOException("No resources file found");
		load(formatter.format(newest) + FILE_EXTENSION);
	}

	public static void load(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists())
			throw new IOException("File " + fileName + 
					" does not exist");
		if (file.isDirectory())
			throw new IOException(fileName + " is a directory");
		try (ObjectInputStream in = new ObjectInputStream(
					new FileInputStream(file))) {
			try {
				Object o = in.readUnshared();
/* TODO	TEMP*/			if (o instanceof Resources)
					instance.load((Resources) o);
//					instance = (Resources) o;
				// TODO add error handling
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
					}
	}

	private void load(Resources r) {
		this.tokens = r.tokens;
		this.skips = r.skips;
		this.patternMap = r.patternMap;
		this.nameMap = r.nameMap;
		this.parserHead = r.parserHead;
		this.classMap = r.classMap;
		patternMap.keySet().forEach( pattern -> pattern.matcher("for Java bug").matches());
	}

	// Token storage TODO move to own class
	private HashSet<Token> tokens = new HashSet<>();
	private HashSet<Pattern> skips = new HashSet<>();
	private HashMap<Pattern, Token> patternMap = new HashMap<>();
	private HashMap<String, Token> nameMap = new HashMap<>();

	public boolean hasToken(Token token) {
		return tokens.contains(token);
	}

	public boolean hasToken(Pattern pattern) {
		return patternMap.containsKey(pattern);
	}

	public boolean hasToken(String name) {
		return nameMap.containsKey(name.toUpperCase());
	}

	public Token getToken(Pattern pattern) {
		return patternMap.get(pattern);
	}

	public Token getToken(String name) {
		return nameMap.get(name.toUpperCase());
	}

	public Set<Pattern> getPatterns() {
		return Set.copyOf(patternMap.keySet());
	}

	public boolean addToken(Token token) {
		if (!tokens.add(token))
			return false;
		if (hasToken(token.getRegex())) {
			tokens.remove(token);
			return false;
		}
		if (hasToken(token.getName())) {
			tokens.remove(token);
			return false;
		}

		nameMap.put(token.getName().toUpperCase(), token);
		patternMap.put(token.getRegex(), token);
		return true;
	}

	public void forEachToken(Consumer<Token> consumer) {
		tokens.forEach(consumer);
	}

	public boolean addSkip(Pattern pattern) {
		return skips.add(pattern);
	}

	public boolean addSkip(String pattern) {
		return addSkip(Pattern.compile(pattern));
	}

	public void forEachSkip(Consumer<Pattern> consumer) {
		skips.forEach(consumer);
	}

	public Set<Pattern> getSkips() {
		return Set.copyOf(skips);
	}

	// Parser storage TODO move to own class
	private Parser parserHead = Parser.EMPTY;
	private HashMap<Class<?>, Parser> classMap = new HashMap<>();

	public Parser getParser(Class<?> c) {
		return classMap.get(c);
	}

	public boolean hasParser(Class<?> c) {
		return classMap.containsKey(c);
	}

	public boolean addParser(Class<?> c, Parser parser) {
		if (hasParser(c))
			return false;
		classMap.put(c, parser);
		return true;
	}

	public Parser getParserHead() {
		return parserHead;
	}

	public void setParserHead(Parser parser) {
		if (parser == null)
			return;
		parserHead = parser;
	}
}
