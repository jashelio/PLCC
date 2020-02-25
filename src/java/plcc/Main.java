package plcc;

import java.io.File;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import plcc.*;
import plcc.annotation.AnnotationUtils;

public class Main {
	private static Resources r;
	private static Scanner sc;
	private static Grammar g;

	private static void usage() {}

	private static void initArgParseMap() {
		argParseMap = new HashMap<>();
		argParseMap.put("l", (args, i) -> {
			try {
				Resources.load(args[++i]);
			} catch (Exception e) {
				usage();
				System.exit(1);
			}
			return i;
		});
		argParseMap.put("c", (args, i) -> {
			try {
				r.setGrammarHead(Grammar.grammarRule(
					Class.forName(args[++i])));
			} catch (Exception e) {
				usage();
				System.exit(2);
			}
			return i;
		});
		argParseMap.put("s", (args, i) -> {
			if (args[i + 1].startsWith("-"))
				setSave();
			else
				setSave(args[++i]);
			return i;
		});
	}

	private static HashMap<String, 
		BiFunction<String[], Integer, Integer>> argParseMap;
	private static void parseArgs(String[] args) {
		if (argParseMap == null)
			initArgParseMap();
		int i = 0;
		for (i = 0; args[i].startsWith("-") && 
				i < args.length; ++i) {
			String option = args[i].substring(1)
					       .toLowerCase();
			i = argParseMap.get(option).apply(args, i);
		}
	}

	private static boolean save = false;
	private static String saveName = null;
	private static void setSave() {
		save = true;
	}

	private static void setSave(String name) {
		save = true;
		saveName = name;
	}

	private static void saveIfSet() throws Exception {
		if (save) {
			if (saveName == null)
				r.save();
			else
				r.save(saveName);
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			r = Resources.instance;
			parseArgs(args);
			saveIfSet();
			g = r.getGrammarHead();
			sc = new BasicScanner(System.in);
			Object AST = g.parse(sc);
			Consumer<Object> entryPoint = AnnotationUtils.getSemanticEntryPoint();
			entryPoint.accept(AST);
		} catch (Exception e) {
			if (sc != null)
				sc.close();
			System.exit(-1);
		}
	}
}
