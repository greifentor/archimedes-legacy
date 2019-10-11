/*
 * ComplexIndicesUtil.java
 *
 * 20.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;


/**
 * Utility-Methoden zur Arbeit mit den komplexen Indices.
 *
 * @author ollie
 *
 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
 */

public class ComplexIndicesUtil {

    /**
     * Liefert den Pfad zur Anzahl der komplexen Indices.
     *
     * @return Der Pfad zur Anzahl der komplexen Indices.
     *
     * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
     */
    public static String[] indexCount() {
        String[] result = createPath(1);
        result[2] = "IndexCount";
        return result;
    }

    public static String[] createPath(int addFields) {
        String[] result = new String[addFields+2];
        result[0] = "Diagramm";
        result[1] = "ComplexIndices";
        return result;
    }

    /**
     * Liefert den Pfad zur Sektion des StructuredTextFiles mit den komplexen Indices.
     *
     * @return Der Pfad zur Sektion des StructuredTextFiles mit den komplexen Indices.
     *
     * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
     */
    public static String[] path() {
        return createPath(0);
    }

    /**
     * Setzt den Pr&auml;fix-Pfad f&uuml;r den angegeben Pfad und den Index an Position 'pos'.
     *
     * @param pos Die Position des Index, f&uuml;r den der Pfad erzeugt werden soll.
     * @param s Der Suffix des zu erzeugenden Pfades.
     * @return Der komplette Pfad.
     *
     * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
     */
    public static String[] params(int pos, String... s) {
        String[] result = createPath(s.length+1);
        result[0] = path()[0];
        result[1] = path()[1];
        result[2] = "Index" + pos;
        for (int i = 0, leni = s.length; i < leni; i++) {
            result[i+3] = s[i];
        }
        return result;
    }

}