/*
 * Identifiable.java
 *
 * 26.06.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


/**
 * Dieses Interface definiert das f&uuml;r ein identifizierbares Objekt vorausgesetzte 
 * Verhalten.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface Identifiable {
    
    /** 
     * @return Ein eindeutiger Schl&uuml;ssel, &uuml;ber den das Objekt identifiziert werden
     *     kann.
     */
    public Key getId();
    
    /** 
     * Setzt den angegebenen Key als neuen Key f&uuml;r das Objekt ein.
     *
     * @param k Der neue Key zum Objekt.
     */
    public void setId(Key k);
    
    /** 
     * @return <TT>true</TT>, wenn das Objekt &uuml;ber einen g&uuml;ltigen Schl&uuml;ssel 
     *     verf&uuml;gt, <TT>false</TT> sonst.
     */
    public boolean hasValidKey();
    
}
