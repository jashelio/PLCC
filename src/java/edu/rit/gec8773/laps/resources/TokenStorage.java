package edu.rit.gec8773.laps.resources;

import edu.rit.gec8773.laps.scanner.Token;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class TokenStorage implements Serializable {

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

}
