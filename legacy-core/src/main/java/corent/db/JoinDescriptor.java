/*
 * JoinDescriptor.java
 *
 * 27.08.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


/**
 * Mit Hilfe dieses Interfaces wird das Verhalten einer Struktur definiert, die einen Join 
 * zwischen zwei Tabellen abbilden soll.<BR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface JoinDescriptor {
    
    /** Eine Bezeichnerliste zur Festlegung des Join-Typs. */
    public enum Type {LEFT_OUTER, RIGHT_OUTER, NATURAL, INNER};
    
    /** @return Der Typ des Joins. */
    public Type getType();
    
    /** @return Die linke Spalte des Joins. */
    public ColumnRecord getLeftColumn();
    
    /** @return Die rechte Spalte des Joins. */
    public ColumnRecord getRightColumn();
    
}
