
import java.util.HashSet;
import java.util.HashMap;
import java.util.regex.Pattern;

import java.util.Date;
import java.util.DateFormat;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class Resources implements Serializable {
	private static Resources instance = new Resources();

	private Resources() {}

	public static Resources getInstance() {
		return instance;
	}

	public static final String FILE_EXTENSION = ".resources";

	public static void save(String fileName) throws IOException {
		File file = new File(fileName);
		if (file.exists()) {
			if (file.isDirectory())
				throw new IOException("Can't write to file: " +
						fileName + " is directory");
			else
				System.err.println("Warning: overwriting " + 
						"existing file " + fileName);
		}
		try (ObjectOuputStream out = new ObjectOutputStream(
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
		for (File file : dir.listFiles()) 
			try {
				Date date = formatter.parse(file.getName().
						substring(0, 
							file.getName().length - 
							FILE_EXTENSION.length));
				if (date.after(newest))
					newest = date;
			} catch (ParseException e) {
				continue;
			}
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
			instance = in.readUnshared();
		}
	

	// Token storage TODO move to own class
	private HashSet<Token> tokens = new HashSet<>();
	private HashMap<Pattern, Token> patternMap = new HashMap<>();

	public boolean hasToken(Token token) {
		return tokens.has(token);
	}

	public Token getToken(Pattern pattern) {
		return patternMap.get(pattern);
	}

	public Token getToken(String pattern) {
		return getToken(Pattern.compile(pattern));
	}

	public boolean addToken(Token token) {
		if (!tokens.add(token))
			return false;
		if (patternMap.get(token.getRegex()) != null) {
			tokens.remove(token);
			return false;
		}
		patternMap.put(token.getRegex(), token);
		return true;
	}

	// Grammar storage TODO move to own class
	private Grammar grammarHead = Grammar.EMPTY;

	public Grammar getGrammar() {
		return grammarHead;
	}

	public void setGrammar(Grammar grammar) {
		if (grammar == null)
			return;
		grammarHead = grammar;
	}
}
