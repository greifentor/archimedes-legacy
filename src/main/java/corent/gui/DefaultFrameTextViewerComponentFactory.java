/*
 * DefaultFrameTextViewerComponentFactory.java
 *
 * 17.04.2004
 *
 * (c) by O.Lieshoff
 * 
 */
 
package corent.gui;


import corent.base.*;

import javax.swing.*;


/**
 * Diese Klasse ist eine Musterimplementierung f&uuml;r eine FrameTextViewerComponentFactory, 
 * die mit Standardkomponenten liefert.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public class DefaultFrameTextViewerComponentFactory implements FrameTextViewerComponentFactory {
    
    /** 
     * Eine Referenz auf eine statische Factory. Mehr als diese wird im Normalfall nicht 
     * n&ouml;tig sein.
     */
    public static final DefaultFrameTextViewerComponentFactory INSTANCE = 
            new DefaultFrameTextViewerComponentFactory();
            
    private DefaultFrameTextViewerComponentFactory() {
        super();
    }
    

    /* Implementierung des Interfaces FrameTextViewerComponentFactory. */
    
    public JEditorPane createEditorPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/plain");
        return editorPane;
        /*
        JTextArea textArea = new JTextArea(8, 40);
        textArea.setLineWrap(true);
        return textArea;
        */
    }
    
    public JButton createButtonBeenden() {
        JButton button = new JButton("Beenden");
        button.setMnemonic('B');
        button.setToolTipText(StrUtil.FromHTML("Schlie&szlig;t das Textfenster."));
        return button;
    }
    
    public JButton createButtonCopyAll() {
        JButton button = new JButton("Kopieren");
        button.setMnemonic('K');
        button.setToolTipText(StrUtil.FromHTML("Kopiert den gesamten Inhalt des Textfensters in"
                + " in die Zwischenablage."));
        return button;
    }
    
    public JButton createButtonSpeichern() {
        JButton button = new JButton("Speichern");
        button.setMnemonic('S');
        button.setToolTipText(StrUtil.FromHTML("Erm&ouml;glicht das Speichern des Textes in "
                + "eine Datei."));
        return button;
    }
    
    public String[] getFileExtensions() {
        return new String[] {"txt", "sql"};
    }
    
}
