/*
 * SQLUtil.java
 *
 * 30.09.2009
 *
 * (c) O.Lieshoff
 *
 */

package gengen.util;


import corentx.util.*;

import gengen.metadata.*;

import java.util.*;


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
     * Liefert eine Insert Anweisung f&uuml;r das angegebene Klassenmodell.
     *
     * @param cmd Die Klassenmetadaten, zu denen das CodeFragment f&uuml;r die Insertanweisung
     *         erzeugt werden soll.
     * @param vn Der Name der Variable, aus der die Daten f&uuml;r das Insertstatement ermittelt
     *         werden sollen (das ist keine echte Variable, sondern ein Name, unter der ein
     *         passendes Objekt in dem erzeugten Codefragment genutzt wird).
     * @param insertPK Ein alternativer String, der das Einf&uuml;gen der als
     *         Prim&auml;rschl&uuml;sselmitglieder gekennzeichneten Attribute ersetzt (bzw.
     *         <TT>null</TT>, wenn keine Alternative gew&uuml;nscht wird). Es empfiehlt sich,
     *         diese Option nur in Verbindung mit Einstelligen Prim&auml;rschl&uuml;sseln zu
     *         nutzen.
     * @param formated Diese Flagge kann gesetzt werden, um den ausgegebenen String ein wenig
     *         mehr zu formatieren.
     * @param indent Die Anzahl der Spaces, die die formatierten Passagen einger&uumL;ckt werden
     *         soll (diese Angabe ist nur wirksam, wenn die <TT>formated</TT>-Flagge gesetzt
     *         wird.
     * @return Ein Codefragment mit dem Insertstatement.
     * @throws IllegalArgumentException Falls <TT>indent</TT> kleiner als null oder
     *         <TT>indent</TT> ungleich null, ohne da&szlig; die <TT>formated</TT> Flagge
     *         gesetzt ist.
     * @throws NullPointerException Falls Klassenmetadaten oder Variablenname als
     *         <TT>null</TT>-Referenz &uuml;bergeben werden.
     * @precondition cmd != <TT>null</TT>.
     * @precondition vn != <TT>null</TT>.
     * @precondition indent &gt;= 0;
     * @precondition indent != 0, falls formated == <TT>false</TT>.
     */
    public static String getInsertStatement(ClassMetaData cmd, String vn, String insertPK,
            boolean formated, int indent) throws IllegalArgumentException, NullPointerException
            {
        return SQLUtil.getInsertStatement(cmd, vn, insertPK, formated, indent, false, null);
    }

    /**
     * Liefert eine Insert Anweisung f&uuml;r das angegebene Klassenmodell.
     *
     * @param cmd Die Klassenmetadaten, zu denen das CodeFragment f&uuml;r die Insertanweisung
     *         erzeugt werden soll.
     * @param vn Der Name der Variable, aus der die Daten f&uuml;r das Insertstatement ermittelt
     *         werden sollen (das ist keine echte Variable, sondern ein Name, unter der ein
     *         passendes Objekt in dem erzeugten Codefragment genutzt wird).
     * @param insertPK Ein alternativer String, der das Einf&uuml;gen der als
     *         Prim&auml;rschl&uuml;sselmitglieder gekennzeichneten Attribute ersetzt (bzw.
     *         <TT>null</TT>, wenn keine Alternative gew&uuml;nscht wird). Es empfiehlt sich,
     *         diese Option nur in Verbindung mit Einstelligen Prim&auml;rschl&uuml;sseln zu
     *         nutzen.
     * @param formated Diese Flagge kann gesetzt werden, um den ausgegebenen String ein wenig
     *         mehr zu formatieren.
     * @param indent Die Anzahl der Spaces, die die formatierten Passagen einger&uumL;ckt werden
     *         soll (diese Angabe ist nur wirksam, wenn die <TT>formated</TT>-Flagge gesetzt
     *         wird.
     * @param idCtrlForObjectNo Diese Flagge mu&szlig; gesetzt werden, wenn in einem
     *         Objektnummern basierten Datenmodell eine id-Kontrolle implementiert werden soll.
     *         Diese Option funktioniert nur in Verbindung mit gesetztem
     *         <TT>insertPK</TT>-Parameter.
     * @param alternateObjectNo Hier kann ein alternativer Spaltenname f&uuml;r die Objektnummer
     *         gesetzt werden. Dieser Parameter ist nur dann von Interesse, wenn die
     *         <TT>idCtrlForObjectNo</TT> gesetzt ist.
     * @return Ein Codefragment mit dem Insertstatement.
     * @throws IllegalArgumentException Falls <TT>indent</TT> kleiner als null oder
     *         <TT>indent</TT> ungleich null, ohne da&szlig; die <TT>formated</TT> Flagge
     *         gesetzt ist. Die Exception wird auch geworfen, falls die Option
     *         <TT>idCtrlForObjectNo</TT> gesetzt wird, ohne da&szlig; der Parameter
     *         <TT>insertPK</TT> &uuml;bergeben wird.
     * @throws NullPointerException Falls Klassenmetadaten oder Variablenname als
     *         <TT>null</TT>-Referenz &uuml;bergeben werden.
     * @precondition cmd != <TT>null</TT>
     * @precondition vn != <TT>null</TT>
     * @precondition insertPK != null, falls idCtrlForObjectNo gesetzt ist
     * @precondition indent &gt;= 0
     * @precondition indent == 0, falls formated == <TT>false</TT>
     *
     * @changed OLI 20.10.2010 - Erweiterung um die Verarbeitung von PTime-Objekten.
     */
    public static String getInsertStatement(ClassMetaData cmd, String vn, String insertPK,
            boolean formated, int indent, boolean idCtrlForObjectNo, String alternateObjectNo)
            throws IllegalArgumentException, NullPointerException {
         assert cmd != null : "getting an insert statement doesn't works with null class meta "
                + "data.";
         assert vn != null : "source variable name can not be null for getting an insert "
                + "statement.";
         assert indent >= 0 : "indent can not be lesser than zero.";
         assert formated || (!formated && (indent == 0)) : "it makes no sense to set indent "
                + "with out setting formated mode.";
         assert !idCtrlForObjectNo || (insertPK != null) : "id control option does not works "
                + "in combination with null for insertPK.";
         AttributeMetaData amd = null;
         boolean wasString = false;
         int i = 0;
         int leni = 0;
         String indstr = Str.spaces(indent);
         String objectNo = (alternateObjectNo != null ? alternateObjectNo : "ObjectNo");
         StringBuffer sb = new StringBuffer();
         if (!formated  && (indent != 0)) {
             throw new IllegalArgumentException("it makes no sense to set indent with out "
                    + "setting formated mode.");
         }
         if (idCtrlForObjectNo && (insertPK == null)) {
             throw new IllegalArgumentException("id control option does not works in "
                    + "combination with null for insertPK.");
         }
         sb.append("insert into ").append(cmd.getName()).append(" (");
         leni = cmd.getAttributes().size();
         sb.append(SQLUtil.getAttributeNameList(cmd));
         sb.append((formated ? "\"\n" + indstr + "+ \"": "")).append(") values (").append(
                (formated ? "\"\n" + indstr : "\" ")).append("+ ");
         for (i = 0; i < leni; i++) {
             amd = cmd.getAttribute(i);
             if (i != 0) {
                 sb.append(" + \"").append((wasString ? "'" : "")).append(",").append((formated
                        ? " \"\n" + indstr + ((amd.getJavaType().equals("String")
                        ? "+ \"" : "")) : " ")).append((amd.getJavaType().equals("String")
                        ? "'" : "")).append((!formated || (formated && amd.getJavaType().equals(
                        "String")) ? "\" " : "")).append("+ ");
             }
             wasString = false;
             if (idCtrlForObjectNo && amd.getName().equals(objectNo)) {
                 sb.append("(").append(vn).append(".get").append(objectNo).append("() < 1 ? "
                        ).append(insertPK).append(" : ").append(vn).append(".get").append(
                        objectNo).append("())");
             } else if ((insertPK != null) && amd.isPrimaryKeyMember()) {
                 sb.append(insertPK);
             } else if (amd.getJavaType().equals("boolean")) {
                 sb.append("(").append(vn).append(".is").append(amd.getName()).append(
                        "() ? \"1\" : \"0\")");
             } else if (SQLUtil.isElementaryType(amd.getJavaType())) {
                 sb.append(vn).append(".get").append(amd.getName()).append("()");
             } else if (amd.getJavaType().endsWith("PDate")
                    || amd.getJavaType().endsWith("PTime")
                    || amd.getJavaType().endsWith("PTimestamp")
                    || amd.getJavaType().endsWith("LongPTimestamp")) {
                 sb.append("DBDataSourceUtil.toDBString(").append("").append(vn).append(".get"
                        ).append(amd.getName()).append("())");
             } else {
                 sb.append(vn).append(".get").append(amd.getName()).append("()");
                 if (amd.getJavaType().equals("String")) {
                     wasString = true;
                 }
             }
         }
         sb.append(" + \"").append((wasString ? "'" : "")).append(");");
         return sb.toString();
    }

    /**
     * Liefert den Typ-Suffix f&uuml;r numerische Zahlenangaben zum angegebenen Typ. Hiermit ist
     * der Buchstabe gemeint, der beispielsweise hinter einer <TT>long</TT>-Zahl angegeben
     * wird ('l' oder 'L', z. B. '4711L').
     *
     * @param typeName Der Name des Typs, zu dem das Typsuffix ermittelt werden soll.
     * @throws IllegalArgumentException Falls der angegebene Typ kein numerischr Basistyp ist.
     * @throws NullPointerException Falls der Typname als <TT>null</TT>-Referenz &uuml;bergeben
     *         wird.
     * @precondition typ ist der Name eines numerischen Basistypen und typeName !=
     *         <TT>null</TT>.
     *
     * @changed OLI 02.10.2009 - Hinzugef&uuml;gt.
     */
    public static String getTypeSuffix(String typeName) throws IllegalArgumentException,
            NullPointerException {
        assert typeName != null : "type suffix can not be calculated for null reference type "
                + "name.";
        assert SQLUtil.isElementaryNumberType(typeName) : "type suffix can be calculated for "
                + "elementary numeric types only (" + typeName + " is not such type).";
        if (!SQLUtil.isElementaryNumberType(typeName)) {
            throw new IllegalArgumentException("type suffix can be calculated for elementary "
                    + "numeric types only (" + typeName + " is not such type).");
        }
        if (typeName.equals("double")) {
            return "D";
        } else if (typeName.equals("float")) {
            return "F";
        } else if (typeName.equals("long")) {
            return "L";
        }
        return "";
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

    /**
     * Pr&uuml;ft, ob der angegbene Java-Type ein elementarer Basistyp mit numerischem Inhalt
     * ist.
     *
     * @param typeName Der Name des zu pr&uuml;fenden Typs.
     * @return <TT>true</TT>, wenn es sich bei dem Typen um einen atomaren Basistypen handelt
     *         mit numerischem Inhalt handelt (z. B. <TT>int</TT>, <TT>long</TT> etc.) und 
     *         <TT>false</TT>falls der angegebenen Typ ein Referenztyp ist.
     * @throws NullPointerException Falls der angegebene Typname eine Null-Referenz ist.
     * @precondition typeName != <TT>null</TT>
     *
     * @changed OLI 02.10.2009 - Hinzugef&uuml;gt.
     */
    public static boolean isElementaryNumberType(String typeName) throws NullPointerException {
        assert typeName != null : "type name can not be null for reference type check.";
        if (typeName.equals("byte") || typeName.equals("double") || typeName.equals("float")
                || typeName.equals("int") || typeName.equals("long") || typeName.equals("short")
                ) {
            return true;
        }
        return false;
    }

}
