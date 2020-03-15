package plcc;

import java.io.File;
import java.util.HashMap;
import java.util.function.Consumer;
import java.io.IOException;

import java.util.Set;
import java.util.stream.Stream;

import plcc.*;
import plcc.annotation.AnnotationUtils;

public class Main {
	private static Resources r;
	private static Scanner sc;
	private static Grammar g;
	private static BNFWriter bnfWriter = null;

	private static void usage() {}

	private static void initArgParseMap() {
		argParseMap = new HashMap<>();
		argParseMap.put("l", (args, i) -> {
			if (i + 1 == args.length || args[i + 1].startsWith("-"))
				Resources.load();
			else
				Resources.load(args[++i]);
			return i;
		});
		argParseMap.put("c", (args, i) -> {
			r.setGrammarHead(Grammar.grammarRule(
				Class.forName(args[++i])));
			return i;
		});
		argParseMap.put("s", (args, i) -> {
			if (i + 1 == args.length || args[i + 1].startsWith("-"))
				setSave();
			else
				setSave(args[++i]);
			return i;
		});
		argParseMap.put("b", (args, i) -> {
			if (i + 1 == args.length || args[i + 1].startsWith("-"))
				bnfWriter = new BNFWriter();
			else
				bnfWriter = new BNFWriter(args[++i]);
			return i;
		});
	}

	private static interface BiFunction<A,B,R> {
		R apply(A a, B b) throws Exception;
	}
	private static HashMap<String, 
		BiFunction<String[], Integer, Integer>> argParseMap;
	private static void parseArgs(String[] args) throws Exception {
		if (argParseMap == null)
			initArgParseMap();
		int i = 0;
		for (i = 0; i < args.length; ++i) {
			if (!args[i].startsWith("-"))
				break;
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

	private static void saveIfSet() throws IOException {
		if (save) {
			if (saveName == null)
				r.save();
			else
				r.save(saveName);
		}
	}

	public static void main(String[] args) throws Exception {
		/*Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Set<Thread> runningThreads = Thread.getAllStackTraces().keySet();
				for (Thread th : runningThreads) {
					if (th != Thread.currentThread() 
							&& !th.isDaemon()) {
						System.out.println();
						System.out.println(th.getName() + " Stack:");
						Stream.of(Thread.getAllStackTraces().get(th))
						      .forEach(System.out::println);
						th.stop();
						}
				}
				Thread.currentThread().stop();
			}
		});*/
		int result = 0;
		try {
			r = Resources.instance;
			Token[] tokens = {
				new Token("LPAREN", "\\("),
				new Token("RPAREN", "\\)"),
				new Token("NUMBER", "\\d+"),
				new Token("COMMA", ",")
			};
			for (Token token : tokens)
				r.addToken(token);
			r.addSkip("\\s+");
			parseArgs(args);
			saveIfSet();
			g = r.getGrammarHead();
			if (bnfWriter != null) {
				bnfWriter.writeGrammar(g);
				bnfWriter.close();
			}
			sc = new CustomScanner(System.in);
			Object AST = g.parse(sc);
			if (AST == null) 
				System.out.println("Could not parse input");
		} catch (IOException ioe) {
			System.out.println("I/O Error: " + ioe.getMessage());
			ioe.printStackTrace();
			result = -2;
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
		} finally {
			if (sc != null)
				sc.close();
			System.exit(result);
		}
	}
}
