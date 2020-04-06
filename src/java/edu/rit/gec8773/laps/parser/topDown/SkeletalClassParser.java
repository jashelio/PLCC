package edu.rit.gec8773.laps.parser.topDown;

import edu.rit.gec8773.laps.util.BNFWriter;
import edu.rit.gec8773.laps.util.MyConsumer;
import edu.rit.gec8773.laps.scanner.Scanner;
import edu.rit.gec8773.laps.scanner.Token;
import edu.rit.gec8773.laps.util.AnnotationUtils;
import edu.rit.gec8773.laps.annotation.RunAfterEachInit;
import edu.rit.gec8773.laps.annotation.RunBeforeEachInit;
import edu.rit.gec8773.laps.annotation.RunBeforeFirstInit;
import edu.rit.gec8773.laps.parser.Parser;
import edu.rit.gec8773.laps.resources.Resources;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SkeletalClassParser extends TopDownParser {

    private final String name;
    private final Class<?> cls;
    private final HashMap<List<Class<?>>, Parser> ruleMap;
    private Stack<Parser> acceptedRulesStack = new Stack<>();
    // TODO make use of callstack instead
    private boolean ranOnce = false;

    private static HashMap<List<Class<?>>, Parser> constructRuleMap(Class<?> cls, boolean isHead) throws InvocationTargetException {
        HashMap<List<Class<?>>, Parser> ruleMap = new HashMap<>();
        Map<Parser, Integer> priorityMap = new HashMap<>();
        Constructor<?>[] ctrs = cls.getConstructors();
        for (Constructor<?> ctr : ctrs) {
            Parameter[] rules = ctr.getParameters();
            if (rules.length == 0) {
                if (isHead)
                    throw new InvocationTargetException(
                            new Exception((cls + " cannot accept an empty" +
                                    " grammar rule since it's the top of " +
                                    "the context free grammar")));
                ruleMap.put(List.of(), EMPTY);
                continue;
            }
            Parser seqRule = TopDownParser.seq((Object[])rules);
            Type firstTypeSeq = seqRule.getStartingToken();
            int priority = AnnotationUtils.priority(ctr);
            for (Parser parser : ruleMap.values()) {
                if (parser == EMPTY)
                    continue;
                Type firstTypeTest = parser.getStartingToken();
                if (firstTypeSeq.equals(firstTypeTest) && priorityMap.get(parser) == priority) {
                    String otherCtrString = "non-existent constructor";
                    try {
                        otherCtrString = cls.getConstructor(
                            (Class<?>[]) (ruleMap.entrySet()
                                                 .parallelStream()
                                                 .filter(Predicate.isEqual(parser))
                                                 .findFirst()
                                                 .orElseThrow(NoSuchMethodException::new)
                                                 .getKey()
                                                 .parallelStream()
                                                 .map(Class.class::cast)
                                                 .toArray()))
                                            .toString();
                    } catch (NoSuchMethodException ignored) {}
                    throw new InvocationTargetException(new Exception(("Two " +
                            "or more grammar rule constructors have the same " +
                            "starting type in " + cls + "\n" +
                            "    Clashing Constructors: " + ctr + "and " +
                            otherCtrString
                    )));
                }
            }
            List<Class<?>> types = Arrays.stream(rules)
                                      .map(Parameter::getType)
                                      .collect(Collectors.toList());
            ruleMap.put(types, seqRule);
            priorityMap.put(seqRule, priority);
        }
        if (ruleMap.size() == 0)
            throw new InvocationTargetException(
                    new Exception((cls + " has no accessible grammar " +
                            "rules make sure at least one constructor " +
                            "is public")));
        return ruleMap;
    }

    /**
     *
     * {@inheritDoc}
     *
     */
    SkeletalClassParser(Class<?> cls, boolean isHead) throws InvocationTargetException {
        super(SkeletalClassParser.constructRuleMap(cls, isHead).values().toArray());
        StringBuilder sb = new StringBuilder(cls.getSimpleName());
        sb.insert(0, "<")
                .append(">");
        name = sb.toString();
        this.ruleMap = SkeletalClassParser.constructRuleMap(cls, isHead);
        this.cls = cls;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object parse(Scanner sc) throws IOException,
            InvocationTargetException, InstantiationException {
        if (!ranOnce) {
            MyConsumer<Void> runOnce = AnnotationUtils.getStaticMethod(cls,
                    RunBeforeFirstInit.class);
            runOnce.accept(null);
            ranOnce = true;
        }
        if (Resources.instance.debugEnabled())
            System.out.println("Trying to parse: " + name + " rule");
        MyConsumer<Void> beforeEach = AnnotationUtils.getStaticMethod(cls,
                RunBeforeEachInit.class);
        MyConsumer<Object> runAfter = AnnotationUtils.getInstanceMethod(cls,
                RunAfterEachInit.class);
        Constructor<?>[] ctrs = cls.getConstructors();
        beforeEach.accept(null);
        Arrays.sort(ctrs, Comparator.comparingInt(AnnotationUtils::priority));
        int emptyIndex = -1;
        for (int i = 0; i < ctrs.length; ++i) {
            Parameter[] rules = ctrs[i].getParameters();
            if (rules.length == 0) {
                emptyIndex = i;
                continue;
            }
            List<Class> types = Arrays.stream(rules)
                                      .map(Parameter::getType)
                                      .collect(Collectors.toList());
            Parser rule = ruleMap.get(types);
            Object[] parsed = (Object[])rule.parse(sc);
            if (parsed == null) {
                rule.returnTokens(sc);
                continue;
            }
            acceptedRulesStack.push(rule);
            parsed = Stream.of(parsed)
                           .map( elm ->
                                   elm instanceof Token ?
                                        ((Token)elm).getValue() :
                                        elm )
                           .toArray();
            Object AST = null;
            try {
                if (Resources.instance.debugEnabled())
                    System.out.println("Accepted " + name +
                            " rule with sequence: " + rule );
                AST = ctrs[i].newInstance(parsed);
            } catch (IllegalAccessException ignored) {}
            runAfter.accept(AST);
            return AST;
        }
        if (emptyIndex != -1) {
            acceptedRulesStack.push(EMPTY);
            Object AST = null;
            try {
                if (Resources.instance.debugEnabled())
                    System.out.println("Accepted " + name +
                            " rule with sequence: " + EMPTY );
                AST = ctrs[emptyIndex].newInstance();
            } catch (IllegalAccessException ignored) {}
            runAfter.accept(AST);
            return AST;
        }
        if (Resources.instance.debugEnabled())
            System.out.println("Failed to accept " + name + " rule");
        return null;
    }

    @Override
    public void returnTokens(Scanner sc) {
//	/*DEBUG*/			System.out.println(name + " returning tokens");
        Parser rule = acceptedRulesStack.empty() ?
                null : acceptedRulesStack.pop();
        if (rule == null)
            return;
        rule.returnTokens(sc);
    }

    @Override
    public Type getStartingToken() {
        return cls;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean addEmpty = false;
        for (BNFWriter.Writable child : this)
            if (child == EMPTY)
                addEmpty = true;
            else
                sb.append(child.getName() == null ?
                        child.toString() :
                        child.getName())
                        .append(" | ");
        if (addEmpty)
            sb.append("e")
                    .append(" | ");
        return sb.substring(0, sb.length() - 3);
    }
}
