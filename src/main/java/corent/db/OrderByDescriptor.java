/*
 * OrderByDescriptor.java
 *
 * 17.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


/**
 * Durch dieses Interface wird eine Datenstruktur beschrieben, die das Zusammenspiel zwischen
 * PersistenceFactory und Ordered-Objekten regelt.
 *
 * @author
 *     <P>O.Lieshoff
 *     <P>
 *
 * @changed
 *     <P>OLI 20.08.2008 - Erweiterung um die Methode <TT>toSQL()</TT>.
 *
 */
 
public interface OrderByDescriptor {
    
    /** @return Anzahl der Order-Klauseln. */
    public int getOrderBySize();
    
    /** 
     * Liefert den n-ten Bestandteil der Order-By-Klausel.
     *
     * @param n Der Index, zu dem der Order-By-Klauseln-Bestandteil geliefert werden soll.
     */
    public OrderClause getOrderByAt(int n);
    
    /**
     * Liefert eine SQL-Order-By-Klausel aus dem Inhalt des Descriptors.
     *
     * @return Die SQL-Order-By-Klausel aus dem Inhalt des Descriptors als String.
     *
     * @changed
     *     <P>OLI 20.08.2008 - Hinzugef&uuml;gt.
     *
     */
    public String toSQL();
    
}
