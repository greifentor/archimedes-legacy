/*
 * OrderMemberModel.java
 *
 * 19.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package archimedes.legacy.model;

import corent.db.*;


/**
 * An interface which defines the behavior of an order member.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 02.06.2016 - Took from "Archimedes" project.
 */
 
public interface OrderMemberModel {

    /**
     * Returns the column which is to order.
     *
     * @return The column which is to order.
     */
    abstract public ColumnModel getOrderColumn();

    /**
     * Sets the passed column as new column for the order member model.
     *
     * @param c The new column.
     */
    abstract public void setOrderColumn(ColumnModel c);

    /**
     * Returns the assorting direction.
     * 
     * @return The assorting direction.
     */
    abstract public OrderClauseDirection getOrderDirection();

    /**
     * Sets a new direction for the order.
     *
     * @param direction The new direction for the order member.
     */
    abstract public void setOrderDirection(OrderClauseDirection direction);

}