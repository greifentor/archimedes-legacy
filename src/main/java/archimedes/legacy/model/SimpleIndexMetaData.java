/*
 * SimpleIndexMetaData.java
 *
 * 19.12.2011
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * Ein Container zur Aufnahme von Informationen zu komplexen Indices aus dem Datanschema der
 * Datenbank. Hier werden nur die Namen von Tabellen und Tabellenspalten gespeichert.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
 */

public class SimpleIndexMetaData extends SimpleConstraintMetaData {

    /**
     * Erzeugt ein neues <CODE>SimpleIndexMetaData</CODE>-Objekt mit den angegebenen Parametern.
     *
     * @param indexName Der Name des Index.
     * @param tableName Der Name der Tabelle, f&uuml;r die der Index gelten soll.
     * @throws IllegalArgumentException Falls eine der vorbedingungen verletzt wird.
     * @precondition indexName != <CODE>null</CODE>
     * @precondition !indexName.isEmpty()
     * @precondition tableName != <CODE>null</CODE>
     * @precondition !tableName.isEmpty()
     *
     * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
     */
    public SimpleIndexMetaData(String indexName, String tableName) {
        super(indexName, tableName);
    }

}