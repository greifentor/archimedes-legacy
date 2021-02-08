/*
 * EditorDjinnButtonFactory.java
 *
 * 10.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import javax.swing.*;


/**
 * Dieses Interface definiert die notwendigen Methoden, um die Buttons eines EditorDjinns zu 
 * produzieren.
 *
 * @author
 *     O.Lieshoff
 *
 * @changed
 *     OLI 29.10.2007 - Erweiterung um die Spezifikation der Methode 
 *             <TT>createButtonSpeichern</TT>. Erg&auml;zung der Dokumentation.<BR>
 *
 */
 
public interface EditorDjinnButtonFactory {
    
    /**
     * Generiert einen Drucken-Button f&uuml;r das EditorDjinnPanel.
     *
     * @return Generiert einen Drucken-Button. 
     */
    public JButton createButtonDrucken();

    /**
     * Generiert einen Historien-Button f&uuml;r das EditorDjinnPanel.
     *
     * @return Generiert einen Historie-Button. 
     */
    public JButton createButtonHistorie();
    
    /**
     * Generiert einen L&ouml;schen-Button f&uuml;r das EditorDjinnPanel.
     *
     * @return Generiert einen Loeschen-Button. 
     */
    public JButton createButtonLoeschen();
    
    /**
     * Generiert einen Speichern-Button f&uuml;r das EditorDjinnPanel.
     *
     * @return Generiert einen Speichern-Button.
     *
     * @changed
     *     OLI 29.10.2007 - Hinzugef&uuml;gt.
     *
     */
    public JButton createButtonSpeichern();

    /**
     * Generiert einen &Uuml;bernehmen-Button f&uuml;r das EditorDjinnPanel.
     *
     * @return Generiert einen &Uuml;bernehmen-Button. 
     */
    public JButton createButtonUebernehmen();

    /**
     * Generiert einen Verwerfen-Button f&uuml;r das EditorDjinnPanel.
     *
     * @return Generiert einen Verwerfen-Button. 
     */
    public JButton createButtonVerwerfen();
        
}  
