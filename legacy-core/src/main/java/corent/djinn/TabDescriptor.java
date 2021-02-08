/*
 * TabDescriptor.java
 *
 * 15.01.2004
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.djinn;


import javax.swing.*;


/**
 * In Implementierungen dieses Interfaces werden einige Daten zum Thema Intialisierung von Tabs
 * zuammengefa&szlig;t.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface TabDescriptor {
    
    /** @return Der Text zum Tab. */
    public String getText();
    
    /** @return Das Mnemonic zum schnellen ausw&auml;hlen des Tabs. */
    public char getMnemonic();
    
    /** @return ein Icon zum Tab. */
    public Icon getIcon();
    
}
