/*
 * EditorDjinnButtonMaster.java
 *
 * 22.04.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Dieses Interface kann von Objekten implementiert werden, die &uuml;ber das EditorDjinnPanel
 * gewartet werden sollen. Es erm&ouml;glicht das zu&auml;tzliche Ausblenden von Buttons des
 * EditorDjinnPanels.
 * <I>Hinweis: Buttons, die durch die prim&auml;re Logik des EditorDjinnPanels abgeblendet sind,
 * k&ouml;nnen durch dieses Interface nicht eingeblendet werden.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 22.04.2008 - Hinzugef&uuml;gt.
 *
 */
 
public interface EditorDjinnButtonMaster {

    /**
     * Liefert den Enabled-Status des Abbruch-Buttons.
     *
     * @return <TT>true</TT>, wenn der Abbruch-Button gegebenenfalls angezeigt werden soll, 
     *     <TT>false</TT> sonst.
     */
    public boolean isButtonCancelEnabled();

    /**
     * Liefert den Enabled-Status des L&ouml;schen-Buttons.
     *
     * @return <TT>true</TT>, wenn der L&ouml;schen-Button gegebenenfalls angezeigt werden soll, 
     *     <TT>false</TT> sonst.
     */
    public boolean isButtonDeleteEnabled();
    
    /**
     * Liefert den Enabled-Status des Historie-Buttons. Der Button wird allerdings nur dann 
     * eingeblendet, wenn es sich bei dem durch den EditorDjinn angezeigten Objekt um ein
     * <TT>HistoryWriter</TT> handelt.
     *
     * @return <TT>true</TT>, wenn der Historie-Button gegebenenfalls angezeigt werden soll, 
     *     <TT>false</TT> sonst.
     */
    public boolean isButtonHistoryEnabled();
    
    /**
     * Liefert den Enabled-Status des &Uuml;bernehmen-Buttons. Der Button wird allerdings nur 
     * dann eingeblendet, wenn das Objekt nicht gesperrt ist.
     *
     * @return <TT>true</TT>, wenn der &Uuml;bernehmen-Button gegebenenfalls angezeigt werden 
     *     soll, <TT>false</TT> sonst.
     */
    public boolean isButtonOkEnabled();

    /**
     * Liefert den Enabled-Status des Drucken-Buttons. Der Button wird allerdings nur dann 
     * eingeblendet, wenn es sich bei dem durch den EditorDjinn angezeigten Objekt um ein
     * <TT>JasperReportable</TT> handelt.
     *
     * @return <TT>true</TT>, wenn der Drucken-Button gegebenenfalls angezeigt werden soll, 
     *     <TT>false</TT> sonst.
     */
    public boolean isButtonPrintEnabled();
    
    /**
     * Liefert den Enabled-Status des Speichern-Buttons. Der Button wird allerdings nur 
     * dann eingeblendet, wenn das Objekt nicht gesperrt ist.
     *
     * @return <TT>true</TT>, wenn der Speichern-Button gegebenenfalls angezeigt werden soll, 
     *     <TT>false</TT> sonst.
     */
    public boolean isButtonSaveEnabled();
    
}
