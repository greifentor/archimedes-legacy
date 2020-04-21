/*
 * HasKey.java
 *
 * 09.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


/**
 * Dieses Interface stellt Methoden sicher, die den Zugriff auf ein Schl&uuml;sselattribut 
 * gestatten.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface HasKey {
    
    /** @return Der Wert des Schl&uuml;sselattributes. */
    public Object getKey();
    
    /** 
     * Setzt den Wert des Schl&uuml;sselattributes auf den &uuml;bergebenen Wert.
     * 
     * @param k Der neue Wert f&uuml;r das Schl&uuml;sselattribut.
     */
    public void setKey(Object k);
    
}
