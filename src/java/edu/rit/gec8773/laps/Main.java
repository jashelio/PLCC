package edu.rit.gec8773.laps;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

public class Main {
	private static Resources r;
	private static CustomScanner sc;
	private static Parser g;
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
			updateClassPath();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			r.setParserHead(Parser.grammarRule(
				loader.loadClass(args[++i])));
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

	private interface BiFunction<A,B,R> {
		R apply(A a, B b) throws Exception;
	}
	private static HashMap<String, 
		BiFunction<String[], Integer, Integer>> argParseMap;
	private static void parseArgs(String[] args) throws Exception {
		if (argParseMap == null)
			initArgParseMap();
		int i;
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
				Resources.save();
			else
				Resources.save(saveName);
		}
	}

	public static void main(String[] args) throws Throwable {
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
			parseArgs(args);
			saveIfSet();
			g = r.getParserHead();
			if (bnfWriter != null) {
				bnfWriter.writeGrammar(g);
				bnfWriter.close();
			}
			sc = new CustomScanner(System.in);
			Object AST = g.parse(sc);
			if (AST == null) {
				System.out.print("Could not parse input: \"");
				System.out.print(sc.getBufferString());
				System.out.println("\" on line " + sc.getLineNumber());
			}
		} catch (InvocationTargetException e) { // for user exceptions
			result = -3;
			Throwable t = e.getTargetException();
			System.err.println(t.getMessage());
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

	private static void updateClassPath() {
		URL url = null;
		try {
			url = new File(System.getProperty("user.dir")).toURL();
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
		ClassLoader urlCL = URLClassLoader.newInstance(new URL[] { url }, contextCL);
		Thread.currentThread().setContextClassLoader(urlCL);
	}
}
