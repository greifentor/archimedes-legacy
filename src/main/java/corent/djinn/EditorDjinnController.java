/*
 * EditorDjinnController.java
 *
 * 27.09.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Durch Implementierung dieses Interfaces kann eine Owner-Komponente f&uuml;r EditorDjinns
 * erreichen, da&szlig; vor den Ausf&uuml;hren der Verwerfen- und L&ouml;schenaktionen eine
 * Methode aufgerufen wird, die einen boolschen Wert zur&uuml;ckliefert. Wird hier der Wert
 * <TT>true</TT> zur&uuml;ckgeliefert, so wird die entsprechende Aktion ausgef&uuml;hrt. Sonst
 * wird die Aktion verhindert.
 * <P>Hier&uuml;ber lassen sich Best&auml;tigungsdialoge in den Bearbeitungsprozess 
 * einf&uuml;gen
 * <HR>
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 05.01.2009 - Erweiterung um die Spezifikation der Methode 
 *             <TT>setEditable(Editable)</TT>.
 *     <P>OLI 12.01.2009 - R&uuml;ckbau der Methode <TT>setEditable(Editable)</TT>. Im Moment 
 *             ist nicht ausreichend Zeit, um das vern&uuml;nftig auszuprogrammieren (wegen
 *             Unhandlichkeiten im <TT>DefaultEditorDjinnPanel</TT>).
 *     <P>OLI 29.01.2009 - Erweiterung um die Spezifikation der Methode 
 *             <TT>getEditorDjinnMode()</TT>.
 *     <P>
 *
 */
 
public interface EditorDjinnController {
    
    /** 
     * Diese Methode wird vom EditorDjinnPanel aufgerufen, bevor die L&ouml;schenaktion 
     * gestartet wird. Wird hier nicht der Wert <TT>true</TT> zur&uuml;ckgeliefert, so wird die
     * Aktion abgebrochen.
     *
     * @return <TT>true</TT>, wenn die Aktion ausgef&uuml;hrt werden soll, <TT>false</TT> sonst.
     */
    public boolean isDeleteConfirmed();
    
    /** 
     * Diese Methode wird vom EditorDjinnPanel aufgerufen, bevor die Verwerfenaktion 
     * gestartet wird. Wird hier nicht der Wert <TT>true</TT> zur&uuml;ckgeliefert, so wird die
     * Aktion abgebrochen.
     *
     * @return <TT>true</TT>, wenn die Aktion ausgef&uuml;hrt werden soll, <TT>false</TT> sonst.
     */
    public boolean isDiscardConfirmed();
    
    /**
     * Liefert eine Referenz auf das Objekt, das in dem Editor bearbeitet wird.
     *
     * @return Referenz auf das in dem Editor bearbeitete Editable. 
     */
    public Editable getEditable();
    
    /**
     * Liefert den EditorDjinnMode, mit dem der EditorDjinnController aufgerufen worden ist.
     *
     * @return Der EditorDjinnMode, mit dem der EditorDjinnController aufgerufen worden ist.
     *
     * @changed
     *     OLI 29.01.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public EditorDjinnMode getEditorDjinnMode();
    
    /* *
     * Setzt das &uuml;bergebene Objekt als neues durch den Editor zu bearbeitende Objekt ein.
     *
     * <B>WICHTIG:</B> Es mu&szlig; <U>nicht</U> gesichert sein, da&szlig; der Inhalt bzw. die
     * Anzeige des EditorDjinn aktualisiert wird.
     *
     * @param e Das neue Objekt, das durch den Editor bearbeitet werden soll.
     *
     * @changed
     *     OLI 05.01.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     * /
    public void setEditable(Editable e);
    */
    
}
