/*
 * PersistenceDescriptor.java
 *
 * 23.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


import java.util.*;


/**
 * Durch dieses Interface wird eine Datenstruktur beschrieben, die das Zusammenspiel zwischen
 * PersistenceFactory und Persistent-Objekten regelt.
 *
 * @author O.Lieshoff
 *
 */
 
public interface PersistenceDescriptor {
    
    /**
     * Gibt eine Referenz auf das Klassenobjekt zur&uuml;ck, deren persistente Instanzen durch 
     * den Descriptor beschrieben werden.
     *
     * @return Die Referenz auf die Klasse, deren Objekte durch den Descriptor beschrieben 
     *     werden.
     */
    public Class getFactoryClass();
    
    /**
     * @return Eine Liste der Attribute, die durch den PersistenceDescriptor erfa&szlig;t 
     *     werden.
     */
    public Vector<Integer> getAttributes();
    
    /**
     * @return Eine Liste der Attribute, die Mitglieder des Prim&auml;rschl&uuml;ssels sind.
     */
    public Vector<ColumnRecord> getKeys();
    
    /** 
     * Liefert zu einem Attributbezeichner eine Angabe zu Tabelle und Feld (Spalte), in dem der
     * Wert des Attributs gespeichert wird.
     *
     * @param attr Der Bezeichner zum Attribut, zu dem die gew&uuml;nschten Daten geliefert 
     *     werden sollen, bzw. <TT>null</TT>, wenn dem Attributbezeichner kein ColumnRecord
     *     zugeordnet ist.
     */
    public ColumnRecord getColumn(int attr);
    
    /** 
     * Liefert zu einem Spaltennamen eine Angabe zu Tabelle und Feld (Spalte).
     *
     * @param tsn Der volle Name der Tabellenspalte, zu der der Datensatz bestimmt werden soll.
     */
    public ColumnRecord getColumn(String tsn);
    
    /**
     * Generiert eine Auswahl-Klausel anhand der &uuml;bergebenen criteria.
     *
     * @param criteria Die Auswahl-Kriterien, die in der Auswahl-Klausel zum Tragen kommen 
     *     sollen.
     */
    public String createFilter(Object[] criteria);
    
    /**
     * @return Eine Liste von ColumnRecords, die in einer Selektionsanzeige zum Descriptor 
     * angezeigt werden sollen.
     */
    public Vector<ColumnRecord> getSelectionViewMembers();
    
    /** @return Eine Liste mit Spalten&uuml;berschriften zum SelectionView. */
    public Vector<String> getSelectionViewColumnnames();
    
    /**
     * @return Eine Liste von JoinDescriptoren, die in einer Selektionsanzeige zum Descriptor 
     *     zum Tragen kommen.
     */
    public Vector<JoinDescriptor> getSelectionJoins();
    
    /** 
     * @return Eine Liste mit den Namen der Felder, die beim Speichern nicht leer sein 
     *     d&uuml;rfen. 
     */ 
    public Vector<String> getNotEmptyColumnnames();
    
    /**
     * Diese Methode kann eine Einzigartigkeitsklausel liefern, die aus den Spaltennamen, 
     * '&amp;', '|' und den runden Klammern bestehen kann. 
     *
     * @return Eine wie oben beschriebene Einzigartigkeitsklausel oder <TT>null</TT>, falls eine
     *     solche f&uuml;r das Objekt nicht notwendig ist.
     */
    public String getUniqueClause();
    
}
