/*
 * TabbedEditable.java
 *
 * 15.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Diese Erweiterung des Editable-Interfaces definiert zus&auml;tzliche Funktionalit&auml;t 
 * f&uuml;r den Umgang mit Editables, deren EditorDjinn mehrere Tabs enthalten soll.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 11.05.2008 - Erweiterung um die Methode <TT>isTabEnabled(int)</TT>.
 *
 */
 
public interface TabbedEditable extends Editable {
    
    /**
     * Liefert eine Referenz auf die TabbedPaneFactory, &uuml;ber die das TabbedPane erzeugt 
     * werden soll.
     *
     * @return Referenz auf eine TabbedPaneFactory, &uuml;ber die das TabbedPane des 
     *     EditorDjinns generiert werden soll.
     */
    public TabbedPaneFactory getTabbedPaneFactory();
    
    /**
     * Liefert den Enabled-Status des Tabs mit der angebenen Nummer.
     *
     * @param no Die Nummer des Tabs, f&uuml;r das der aktuelle Enabled-Status ermittelt werden
     *     soll.
     * @return <TT>true</TT>, wenn das Tab mit der angegebenen Nummer aktiv sein soll, 
     *     <TT>false</TT>, wenn es abgeblendet werden soll.
     */
    public boolean isTabEnabled(int no);
 
}
