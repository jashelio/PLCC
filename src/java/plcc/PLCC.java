package plcc;

import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.*;

import plcc.*;

public class PLCC {
	private static Map<Class<? extends Annotation>, List<Class<?>>> classMap = new HashMap<>();

	private static List<Class<? extends Annotation>> plccClassTypes = new ArrayList<>();
	static {
		plccClassTypes.add(GrammarRule.class);
		plccClassTypes.add(TokenList.class);
	}
	public static void start() { 
		initializeAnnotations();
		for (Class<?> key : classMap.keySet()) { 
			String type = key.getName();
			for (Class<?> cls : classMap.get(key))
				System.out.println(type + " -> " + cls.getName());
		}
	}

	private static void initializeAnnotations() {
		for (Class<? extends Annotation> classType : plccClassTypes)
			classMap.put(classType, new ArrayList<>());

		for (Class<?> plccClass : ClassLoader.instance.getPlccClasses()) 
			for (Class<? extends Annotation> classType : classMap.keySet())
				if (plccClass.getAnnotation(classType) != null)
					classMap.get(classType).add(plccClass);
	}

	// TODO
	public static Map<String, String> getTokens() {
		Map<String, String> result = new HashMap<>();
		Token token = null;
		for (Class<?> tokenList : classMap.get(TokenList.class)) {
			for (Field field : tokenList.getFields()) {
				if ((token = field.getAnnotation(Token.class)) != null) {
					if (field.isEnumConstant()) { // TODO
					} else if (Modifier.isStatic(field.getModifiers())) {
					} //else
//						throw new Exception("Token must be defined as an enum constant or a static field"); // TODO
				}
			}
		}
		return result;
	}

	// TODO
	public static Set<String> getSkips() {
		return null;
	}
}
