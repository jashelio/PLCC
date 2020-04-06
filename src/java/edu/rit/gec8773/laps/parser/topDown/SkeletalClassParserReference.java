package edu.rit.gec8773.laps.parser.topDown;

import edu.rit.gec8773.laps.scanner.Scanner;
import edu.rit.gec8773.laps.resources.Resources;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class SkeletalClassParserReference extends TopDownParser {

    private final Class<?> cls;
    private final String name;

    SkeletalClassParserReference(Class<?> cls) throws InvocationTargetException {
        super(new Object[0]);
        this.cls = cls;
        StringBuilder sb = new StringBuilder(cls.getSimpleName());
        sb.insert(0, "<")
                .append(">");
        name = sb.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void returnTokens(Scanner sc) {
        Resources.instance.getParser(cls).returnTokens(sc);
    }

    @Override
    public Object parse(Scanner sc) throws IOException,
            InvocationTargetException, InstantiationException {
//					System.out.println(name + " closure");
        return Resources.instance.getParser(cls).parse(sc);
    }
}
