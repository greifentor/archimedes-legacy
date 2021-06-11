/*
 * SQLUtil.java
 *
 * 30.09.2009
 *
 * (c) O.Lieshoff
 *
 */

package gengen.util;


import java.util.List;

import gengen.metadata.AttributeMetaData;
import gengen.metadata.ClassMetaData;


/**
 * Eine Sammlung von Hilfsmethoden zur Anwendung in den Codegeneratoren.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 30.09.2009 - Hinzugef&uuml;gt.
 * @changed OLI 20.10.2010 - Erweiterung um die Verarbeitung von PTime-Objekten in der Methode
 *         <TT>getInsertStatement(ClassMetaData, String, String, boolean, int, boolean,
 *         String)</TT>
 */

public class SQLUtil {

    /**
     * Generiert eine Liste mit den durch Kommata abgesetzten Namen der Attribute der Klasse.
     *
     * @param cmd Die Klassenmetadaten, anhand derer der Attributblock generiert werden soll.
     * @return Eine Liste mit den durch Kommata abgesetzten Namen der Attribute der Klasse.
     * @throws NullPointerException Falls die Klassenmetadaten als <TT>null</TT>-Pointer
     *         &uuml;bergeben werden.
     * @precondition cmd != <TT>null</TT>
     *
     * @changed OLI 30.09.2009 - &Uuml;bernahme aus der Klasse <TT>AbstractCodeGenerator</TT>.
     *         Dabei Umsetzung auf <TT><B>static</B></TT>.
     */
    public static String getAttributeNameList(ClassMetaData cmd) throws NullPointerException {
        assert cmd != null : "class meta data can not be null for generating an attribute "
                + "definition block.";
        AttributeMetaData amd = null;
        int i = 0;
        int len = 0;
        List<AttributeMetaData> amds = cmd.getAttributes();
        StringBuffer sb = new StringBuffer();
        for (i = 0, len = amds.size(); i < len; i++) {
            amd = amds.get(i);
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(amd.getName());
        }
        return sb.toString();
    }

    /**
     * Pr&uuml;ft, ob der angegbene Java-Type ein elementarer Basistyp ist.
     *
     * @param typeName Der Name des zu pr&uuml;fenden Typs.
     * @return <TT>true</TT>, wenn es sich bei dem Typen um einen atomaren Basistypen handelt 
     *         (z. B. <TT>int</TT>, <TT>boolean</TT> etc.) und <TT>false</TT>falls der
     *         angegebenen Typ ein Referenztyp ist.
     * @throws NullPointerException Falls der angegebene Typname eine Null-Referenz ist.
     * @precondition typeName != <TT>null</TT>
     *
     * @changed OLI 30.09.2009 - &Uuml;bernahme aus der Klasse <TT>AbstractCodeGenerator</TT>.
     *         Dabei Umsetzung auf <TT><B>static</B></TT>.
     */
    public static boolean isElementaryType(String typeName) throws NullPointerException {
        assert typeName != null : "type name can not be null for reference type check.";
        if (typeName.equals("boolean") || typeName.equals("byte") || typeName.equals("char")
                || typeName.equals("double") || typeName.equals("float") 
                || typeName.equals("int") || typeName.equals("long") || typeName.equals("short")
                ) {
            return true;
        }
        return false;
    }

}
