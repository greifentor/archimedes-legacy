/*
 * DefaultOrderClause.java
 *
 * 17.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


import java.io.*;


/**
 * Diese Musterimplementierung des OrderClause-Interfaces kann zur Speicherung einfacher 
 * Order-By-Klausel-Bestandteile genutzt werden.
 *
 * @author O.Lieshoff
 *
 */
 
public class DefaultOrderClause implements OrderClause, Serializable {
    
    /* Die Tabellenspalte, &uuml;ber die sortiert werden soll. */
    private ColumnRecord column = null;
    /* Die Sortierungsrichtung der Order-By-Klausel. */
    private OrderClauseDirection direction = null;
    
    /** 
     * Generiert eine DefaultOrderClause aus den &uuml;bergebenen Parametern.
     *
     * @param column Die Tabellenspalte, &uuml;ber die sortiert werden soll.
     * @param direction Die Sortierungsrichtung, in die sortiert werden soll.
     */
    public DefaultOrderClause(ColumnRecord column, OrderClauseDirection direction) {
        super();
        this.setColumn(column);
        this.setDirection(direction);
    }
    
    /**
     * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r das Attribut ColumnRecord.
     *
     * @param column Der neue Wert f&uuml;r das Attribut ColumnRecord.
     */
    public void setColumn(ColumnRecord column) {
        this.column = column;
    }
    
    /**
     * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r das Attribut 
     * OrderClauseDirection.
     *
     * @param direction Der neue Wert f&uuml;r das Attribut OrderClauseDirection.
     */
    public void setDirection(OrderClauseDirection direction) {
        this.direction = direction;
    }
    
    
    /* Implementierung des Interfaces OrderClause. */
    
    /** @return Die Tabellenspalte, nach der sortiert werden soll. */
    public ColumnRecord getColumn() {
        return this.column;
    }
    
    /** @return Die Sortierungsrichtung der Order-By-Klausel. */
    public OrderClauseDirection getDirection() {
        return this.direction;
    }
    
}
