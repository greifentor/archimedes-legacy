/*
 * SimpleTypeChecker.java
 *
 * 11.05.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.util;


/**
 * A class which is able to check for simple types.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.05.2017 - Added.
 */

public class SimpleTypeChecker {

    /**
     * Checks the passed type name for being a name of a simple type.
     *
     * @param typeName The type name to check.
     * @return <CODE>true</CODE> if the type with the passed name is a simple type.
     *
     * @changed OLI 11.05.2017 - Added.
     */
    public boolean isSimpleType(String typeName) {
        return  "boolean".equals(typeName) || "byte".equals(typeName) || "char".equals(typeName)
                || "double".equals(typeName) || "float".equals(typeName)
                || "int".equals(typeName) || "long".equals(typeName) || "short".equals(typeName
                );
    }

}