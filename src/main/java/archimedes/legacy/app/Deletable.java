/*
 * Deletable.java
 *
 * 21.01.2008
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


/**
 * Dieses Interface mu&szlig; von Klassen implementiert, die mit der ReconstructableDBFactory
 * zusammenarbeiten sollen und &uuml;ber eine Flagge als gel&ouml;scht kennzeichbar sind.
 *
 * @author
 *     ollie
 *     <P>
 *
 * @changed
 *     OLI 21.01.2008 - Hinzugef&uuml;gt.
 * 
 */
 
public interface Deletable extends corent.db.xs.Reconstructable {
    
    public boolean isDeleted();
    
    public void setDeleted(boolean b);

}
