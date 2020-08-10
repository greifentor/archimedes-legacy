/*
 * StereotypeModel.java
 *
 * 30.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package archimedes.legacy.model;


import corent.base.*;
import corent.djinn.*;


/**
 * Dieses Interface definiert den Funktionsumfang eines Stereotypen f&uuml;r Tabellen des 
 * Archimedes-Systems.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 29.08.2007 - Erweiterung um die Methode <TT>isHideTable</TT>.
 * @changed OLI 01.01.2008 - Erweiterung um die Methode <TT>isDoNotPrint</TT>, 
 *         <TT>setDoNotPrint(boolean)</TT> und <TT>setHideTable(boolean)</TT>.
 * @changed OLI 01.11.2011 - Erweiterung um den <TT>HistoryOwner</TT>.
 */
 
public interface StereotypeModel extends CommentOwner, Comparable, HistoryOwner, NamedObject,
        Selectable {

    /**
     * Zeigt an, ob Tabellen dieses Stereotyps in Ausdrucken erscheinen sollen.
     *
     * @return <TT>true</TT>, wenn die Tabelle in Ausdrucken und Grafikexporten nicht mit 
     *     angezeigt werden sollen, <TT>false</TT> sonst.
     *
     * @changed
     *     OLI 01.01.2008 - Hinzugef&uuml;gt.<BR>
     *
     */
    public boolean isDoNotPrint(); 
     
    /**
     * Kl&auml;rt die Frage, ob Tabellen dieses StereotypeModels im Diagramm versteckt werden
     * sollen.
     *
     * @return <TT>true</TT>, wenn die Tabelle im Diagramm nicht mit angezeigt werden soll.
     *     <TT>false</TT> sonst.
     */
    public boolean isHideTable(); 
     
    /** @return Eine Bezeichnung zur Stereotype. */
    public String getName();
    
    /**
     * &Uuml;ber diese Methode kann gesteuert werden, ob Tabellen dieses Stereotyps in 
     * Ausdrucken und Grafikexporten angezeigt werden sollen.
     *
     * @param dnp Setzen Sie diese Flagge, wenn Tabellen dieses Stereotyps in Ausdrucken und
     *     Grafikexporten nicht angezeigt werden sollen.
     *
     * @changed
     *     OLI 01.01.2008 - Hinzugef&uuml;gt.<BR>
     *
     */
    public void setDoNotPrint(boolean dnp);
    
    /**
     * &Uuml;ber diese Methode kann gesteuert werden, ob Tabellen dieses Stereotyps im Diagramm 
     * angezeigt werden sollen.
     *
     * @param dnp Setzen Sie diese Flagge, wenn Tabellen dieses Stereotyps im Diagramm nicht 
     *     angezeigt werden sollen.
     *
     * @changed
     *     OLI 01.01.2008 - Hinzugef&uuml;gt.<BR>
     *
     */
    public void setHideTable(boolean dnp); 
    
}
