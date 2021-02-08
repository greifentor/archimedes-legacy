/*
 * Selectable.java
 *
 * 25.02.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Dieses Interface definiert das Verhalten eines in einem SelectionDialog ausw&auml;hlbaren 
 * Objekts.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface Selectable {
    
    /**
     * Gibt Auskunft dar&uuml;ber, ob das implementierende Objekt durch die &uuml;bergebenden
     * Kriterien selektiert wird.
     *
     * @param criteria Die Kriterien, auf die das implementierende Objekt gepr&uuml;ft werden 
     *     soll.
     * @return <TT>true</TT>, falls das Objekt, den &uuml;bergebenen Kriterien entspricht,<BR>
     *     <TT>false</TT> sonst.
     * @throws IllegalArgumentException Falls das Objekt mit unvertr&auml;glichen Kriterien
     *     gef&uuml;ttert wird.
     */
    public boolean isSelected(Object[] criteria) throws IllegalArgumentException;
    
}
