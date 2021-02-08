/*
 * OrderClause.java
 *
 * 17.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


/**
 * Dieses Interface beschreibt die notwendigen F&auml;higkeiten f&uuml;r einen Container, der
 * zur Aufnahme der Daten geeignet ist, die eine Order-Klausel beschreiben.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface OrderClause {
    
    /** @return Die Tabellenspalte, nach der sortiert werden soll. */
    public ColumnRecord getColumn();
    
    /** @return Die Sortierungsrichtung der Order-By-Klausel. */
    public OrderClauseDirection getDirection();
    
}
