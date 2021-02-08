/*
 * ParamIdGetter.java
 *
 * 01.10.2015
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.acf.util;

import corentx.util.*;

import java.lang.reflect.*;
import java.util.*;


/**
 * Creates string arrays of all public static final String fields of a class. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 01.10.2015 - Added.
 */

public class ParamIdGetter {

    /**
     * Creates a string array of public static final String fields of the passed class.
     *
     * @param cls The class whose public static final String fields should be returned.
     * @return A string array of public static final String fields of the passed class.
     *
     * @changed OLI 01.10.2015 - Added.
     */
    public String[] getPublicStaticFinalStringsForClass(Class<?> cls) {
        List<String> l = new SortedVector<String>();
        for (Field f : cls.getFields()) {
            int mod = f.getModifiers();
            if ((f.getType() == String.class) && Modifier.isFinal(mod) && Modifier.isStatic(mod)
                    ) {
                try {
                    Object value = f.get(null);
                    l.add((value != null ? value.toString() : ""));
                } catch (IllegalAccessException e) {
                    System.out.println("error while accessing field " + cls.getSimpleName()
                            + "." + f.getName() + ": " + e.getMessage());
                }
            } 
        }
        return l.toArray(new String[0]);
    }

}