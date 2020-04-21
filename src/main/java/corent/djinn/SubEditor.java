/**
 * SubEditorDescriptor.java
 *
 * 20.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import javax.swing.*;


/** 
 * Dieses Interface erweitert den EditorDescriptor zum Aufnahme ganzer Panels in den 
 * EditorDjinn.
 * 
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 09.01.2009 - Erweiterung um die Spezifikation der Methode 
 *             <TT>setObject(Attributed)</TT>. Dabei Formatanpassungen und Sortierung der 
 *             Methoden. Aus Effizenzgr&uuml;nden habe ich die Methode erstmal wieder 
 *             zur&uuml;ckgenommen.
 *     <P> 
 *
 */
 
public interface SubEditor {

    /** 
     * R&auml;mt nach einem Abbruch des EditorDjinns gegebenenfalls auf. 
     */
    public void cleanupData();
    
    /** 
     * Liefert eine Referenz auf die n-te Komponente des SubEditors.
     *
     * @param n Die Nummer der Komponente, deren Referenz geliefert werden soll.
     * @return Die Referenz auf die n-te Komponente des SubEditors.
     */
    public JComponent getComponent(int n);
    
    /**
     * Liefert die Anzahl der in dem SubEditor angezeigten Komponenten.
     *
     * @return Die Anzahl der in dem SubEditor angezeigten Komponenten.
     */
    public int getComponentCount();

    /** 
     * Liefert eine Referenz auf das Komponentenpanel der n-ten Komponente des SubEditors. 
     * Dieses sollte gesetzt werden, wenn die Komponente beispielsweise als ganze mit einem 
     * Rahmen versehen werden soll, wenn sie fokussiert wird (o.&auml;.)
     *
     * @param n Die Nummer der Komponente, deren Panel-Referenz geliefert werden soll.
     * @return Die Referenz auf das Komponentenpanel der n-ten Komponente des SubEditors.
     */
    public JPanel getComponentPanel(int n);
    
    /** 
     * Liefert eine Referenz auf den Label zur n-ten Komponente des SubEditors.
     *
     * @param n Die Nummer der Komponente, zu deren Label die Referenz geliefert werden soll.
     * @return Die Referenz auf den Label der n-ten Komponente des SubEditors.
     */
    public JLabel getLabel(int n);
    
    /**
     * Liefert das Panel des SubEditors.
     *
     * @return Eine Referenz auf das Panel des SubEditors. 
     */
    public JPanel getPanel();
    
    /**
     * Setzt das &uuml;bergebene Objekt als neue Grundlage des SubEditors.
     *
     * Das wird erst Gegenstand zuk&uuml;nftiger Entwicklungen sein ...
     *
     * @param attr Das Objekt, das in dem EditorDjinn manipuliert wird, in dem sich der 
     *         SubEditor befindet.
     *
     * @changed
     *     OLI 09.01.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    // public void setObject(Attributed attr);
    
    /** 
     * &Uuml;bertr&auml;gt die Daten aus dem SubEditor in das bearbeitete Objekt.
     */
    public void transferData();
    
}
