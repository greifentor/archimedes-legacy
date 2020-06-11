/*
 * DefaultOrderByDescriptor.java
 *
 * 17.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


import java.io.*;
import java.util.*;


/**
 * Diese Musterimplementierung des Interfaces OrderByDescriptor verwaltet eine Liste von 
 * OrderClause-Objekten.
 *
 * @author 
 *     <P>O.Lieshoff
 *     <P>
 *
 * @changed
 *     <P>OLI 20.08.2008 - Implementierung der Methode <TT>toSQL()</TT>.
 *
 */
 
public class DefaultOrderByDescriptor implements OrderByDescriptor, Serializable {
    
    /* Die Liste mit den OrderClause-Objekten. */
    private Vector<OrderClause> voc = new Vector<OrderClause>();
    
    /** Generiert einen Descriptor mit Defaultwerten. */
    public DefaultOrderByDescriptor() {
        super();
    }
    
    /** 
     * F&uuml;gt die &uuml;bergebene OrderClause an die Liste des Descriptors an.
     *
     * @param oc Die an die Liste anzuf&uuml;gende OrderClause.
     */
    public void addOrderClause(OrderClause oc) {
        this.voc.addElement(oc);
    }
    
    
    /* Implementierung des Interfaces OrderByDescriptor. */
    
    public int getOrderBySize() {
        return this.voc.size();
    }
    
    public OrderClause getOrderByAt(int n) {
        return this.voc.elementAt(n);
    }
    
    /**
     * @changed
     *     <P>OLI 20.08.2008 - Aus 
     *             <TT>DBFactoryUtil.OrderByDescriptorToString(OrderByDescriptor)</TT>
     *             &uuml;bernommen.
     */
    public String toSQL() {
        StringBuffer orderby = new StringBuffer();
        for (int i = 0, len = this.getOrderBySize(); i < len; i++) {
            OrderClause oc = this.getOrderByAt(i);
            if (oc.getColumn() != null) {
                if (orderby.length() > 0) {
                    orderby.append(", ");
                }
                orderby.append(oc.getColumn().getFullname()).append(" ").append((
                        oc.getDirection() == OrderClauseDirection.ASC ? "asc" : "desc"));
            }
        }
        return orderby.toString();
    }
    
}
