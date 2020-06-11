/*
 * ContextOwner.java 
 *
 * 26.02.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import java.awt.*;


/**
 * Dieses Interface stellt das Verhalten von Context-besitzenden Objekten sicher.
 * 
 * <P>ContextOwner-Komponenten k&ouml;nnen zudem als "hart abgeblendet" gekennzeichnet werden.
 * Solche Komponenten sind disabled und k&ouml;nnen auch durch einen Aufruf der Methode 
 * <TT>setEnabled(true)</TT> nicht mehr aufgeblendet werden. 
 *
 * @author 
 *     O.Lieshoff
 *
 * @changed
 *     OLI 16.02.2008 - Implementierung der Getter und Setter f&uuml;r die harte Abblendung.
 *     <P>
 *
 */
 
public interface ContextOwner {
    
    /** @return Der Context-Name des Objekts. */
    public String getContext();
    
    /**
     * Setzt den &uuml;bergebenen Namen als neuen Context-Namen f&uuml;r das Objekt ein.
     *
     * @param n Der neue Context-Name.
     */
    public void setContext(String n);
    
    /**
     * Liefert die Parent-Component zum aktuellen ContextOwner. Wird das ContextOwner-Interface
     * von einer Component implementiert, so braucht diese Methode nicht &uuml;berschrieben oder
     * implementiert zu werden, da sie in Component bereits hinreichend implementiert ist.
     *
     * @return Die Parent-Component zum ContextOwner oder <TT>null</TT> falls es eine solche 
     *     nicht gibt.
     */
    public Component getParent();
    
    /**
     * Liefert den Status der harten Abblendung der Komponente.
     *
     * @return <TT>true</TT>, wenn die Komponente hart abgeblendet ist, <TT>false</TT> sonst.
     *
     * @changed
     *     OLI 16.02.2008 - Hinzugef&uuml;gt.
     *
     */
    public boolean isStrongDisabled();
    
    /**
     * Setzt den Status der harten Abblendung der Komponente auf den angegebenen Wert.
     *
     * @param b Wird b mit <TT>true</TT> &uuml;bergeben, so wird die Komponente abgeblendet und
     *     kann die Komponente nicht mehr durch den Aufruf der Methode <TT>setEnabled(true)</TT>
     *     aufgeblendet werden. Die Implementierung der Methode sollte <TT>synchronized</TT> 
     *     sein.
     *
     * @changed
     *     OLI 16.02.2008 - Hinzugef&uuml;gt.
     *
     */
    public void setStrongDisabled(boolean b);
    
}
