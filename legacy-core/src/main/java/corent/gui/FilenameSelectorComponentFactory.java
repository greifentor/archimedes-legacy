/*
 * FilenameSelectorComponentFactory.java
 *
 * 07.07.2008
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.gui;


import javax.swing.*;


/**
 * Diese Factory stellt die Komponenten f&uuml;r einen FilenameSelector zur Verf&uuml;gung.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 07.07.2008 - Hinzugef&uuml;gt.
 *     <P>OLI 16.07.2008 - Erweiterung um den Clear-Button.
 *
 */
 
public interface FilenameSelectorComponentFactory {
    
    /**
     * Liefert einen Clear-Button zum FilenameSelector.
     *
     * @param owner Der FilenameSelector, f&uuml;r den der Button generiert werden soll. 
     * @return Der Button, der den FilenameSelector zur&uuml;cksetzt.
     *
     * @changed
     *     OLI 16.07.2008 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public JButton createClearButton(FilenameSelector owner);
    
    /**
     * Liefert einen Select-Button zum FilenameSelector. &Uuml;ber diesen Button wird der 
     * FileChooser aktiviert.
     *
     * @param owner Der FilenameSelector, f&uuml;r den der Button generiert werden soll. 
     * @return Der Button, der den FileChooser aufruft. 
     */
    public JButton createSelectButton(FilenameSelector owner);
    
    /** 
     * Liefert das TextField, in dem der Filename angezeigt werden soll.
     *
     * @param owner Der FilenameSelector, f&uuml;r den der Button generiert werden soll. 
     * @return Das TextField zur Komponente. 
     */
    public JTextField createTextField(FilenameSelector owner);
    
}
