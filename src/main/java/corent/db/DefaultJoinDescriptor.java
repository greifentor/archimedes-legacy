/* 
 * DefaultJoinDescriptor.java
 *
 * 27.08.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


/**
 * Diese Klasse stellt eine Musterimplementierung f&uuml;r eine einen Join beschreibende 
 * Datenstruktur dar.<BR>
 *
 * @author O.Lieshoff
 *
 */
 
public class DefaultJoinDescriptor implements JoinDescriptor {
    
    /* Die linke Seite des Joins. */
    private ColumnRecord columnLeft = null;
    /* Die rechte Seite des Joins. */
    private ColumnRecord columnRight = null;
    /* Der Typ des Joins. */
    private JoinDescriptor.Type typ = JoinDescriptor.Type.INNER;
    
    /** 
     * Generiert einen JoinDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param typ Der Typ des Joins.
     * @param columnLeft Die linke Seite des Joins.
     * @param columnRight Die rechte Seite des Joins.
     */
    public DefaultJoinDescriptor(Type typ, ColumnRecord columnLeft, ColumnRecord columnRight) {
        super();
        this.columnLeft = columnLeft;
        this.columnRight = columnRight;
        this.typ = typ;
    }
    
    
    /* Implementierung des Interfaces JoinDescriptor. */
    
    public Type getType() {
        return this.typ;
    }
    
    public ColumnRecord getLeftColumn() {
        return this.columnLeft;
    }
    
    public ColumnRecord getRightColumn() {
        return this.columnRight;
    }
    
}
