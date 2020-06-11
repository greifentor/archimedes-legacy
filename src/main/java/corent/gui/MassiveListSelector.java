/*
 * MassiveListSelector.java
 *
 * 03.08.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


/**
 * Dieses Interface beschreibt das Verhalten einer Selector-Komponente, mit Hilfe derer ein 
 * Element aus einer gro&szlig;en Liste ausgew&auml;hlt werden kann.
 *
 * @author
 *     <P>O.Lieshoff
 *     <P>
 *
 * @changed
 *     <P>OLI 20.02.2008 - Erweiterung um die Methoden <TT>getCls()</TT> und 
 *             <TT>setClass(Class)</TT>.
 *     <P>OLI 12.08.2008 - Erweiterung um die Spezifikation der Methode 
 *             <TT>setNullString(String)</TT>. 
 *
 */
 
public interface MassiveListSelector {
    
    /** 
     * F&uuml;gt den &uuml;bergebenen Listener an die Liste der die Komponente abh&ouml;renden
     * Listener an.
     *
     * @param l Der neu an die Liste anzuf&uuml;gende MassiveListSelectorListener.
     */
    public void addMassiveListSelectorListener(MassiveListSelectorListener l);
    
    /**
     * Liefert die Klasse der Objekte, die durch den MassiveListSelector ausgew&auml;hlt werden
     * k&ouml;nnen.
     *
     * @return Die Klasse der durch den MassiveListSelector ausw&auml;hlbaren Objekte.
     *
     * @changed
     *     OLI 20.02.2008 - Hinzugef&uuml;gt.
     *
     */
    public Class getCls();
    
    /** 
     * Liefert das selektierte Objekt.
     *
     * @return Das durch den Selektor gew&auml;hlte Objekt bzw. <TT>null</TT>, wenn kein Objekt
     *     gew&auml;hlt worden ist.
     */
    public Object getSelected();
    
    /**
     * L&ouml;scht den &uuml;bergebenen Listener aus der Liste der die Komponente 
     * abh&ouml;renden MassiveListSelectorListener.
     *
     * @param l Der aus der MassiveListSelectorListener-Liste zu entfernende Listener.
     */
    public void removeMassiveListSelectorListener(MassiveListSelectorListener l);
    
    /**
     * Setzt die Klasse der durch den MassiveListSelector zu selektierenden Objekte.
     *
     * @param cls Die Klasse, der durch den MassiveListSelector auszuw&auml;hlenden Objekte.
     *
     * @changed
     *     OLI 20.02.2008 - Hinzugef&uuml;gt.
     *
     */
    public void setCls(Class cls);
    
    /**
     * Setzt einen neuen String als Null-String f&uuml;r den MassiveListSelector ein.
     *
     * @param s Der neue Null-String f&uuml;r den MassiveListSelector.
     *
     * @changed
     *     <P>OLI 12.08.2008 - Hinzugef&uuml;gt.
     *
     */
    public void setNullString(String s); 
    
    /**
     * Setzt das &uuml;bergebene Objekt als neue Auswahl des Selektor ein.
     *
     * @param selected Das Objekt, das als durch den Selektor ausgew&auml;hlt gelten soll.
     */
    public void setSelected(Object selected);
    
}
