/*
 * LineTextEditorComponentFactory.java
 *
 * 05.10.2004
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.gui;


import javax.swing.*;


/**
 * Diese Factory stellt die Komponenten f&uuml;r einen LineTextEditor zur Verf&uuml;gung.
 * <BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface LineTextEditorComponentFactory extends TextAreaDialogComponentFactory {
    
    /**
     * @param owner Der LineTextEditor, f&uuml;r den der Button generiert werden soll. 
     * @return Der Button, der den TextEditor aufruft. 
     */
    public JButton createAendernButton(LineTextEditor owner);
    
    /** 
     * @param owner Der LineTextEditor, f&uuml;r den der Button generiert werden soll. 
     * @return Das TextField zur Komponente. 
     */
    public JTextField createTextField(LineTextEditor owner);
    
}
