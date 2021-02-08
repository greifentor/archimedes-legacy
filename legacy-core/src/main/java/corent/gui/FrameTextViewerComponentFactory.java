/*
 * FrameTextViewerComponentFactory.java
 *
 * 17.04.2004
 *
 * (c) by O.Lieshoff
 * 
 */
 
package corent.gui;


import javax.swing.*;


/**
 * Dieses Interface definiert die notwendigen Methoden f&uuml;r eine Factory, die die 
 * erforderlichen Komponenten f&uuml;r einen FrameTextViewer produzieren kann.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public interface FrameTextViewerComponentFactory {

    /** 
     * @return Die EditorPane, auf der der Text dargestellt und eventuell editiert werden kann. 
     */
    public JEditorPane createEditorPane();

    /** @return Der Beenden-Button des Frames. */
    public JButton createButtonBeenden();

    /** @return Der Copy-All-Button des Frames. */
    public JButton createButtonCopyAll();

    /** @return Der Speichern-Button des Frames. */
    public JButton createButtonSpeichern();

    /** @return Liste mit den zum Speichern g&uuml;ltigen Dateinamenserweiterungen. */
    public String[] getFileExtensions();

}
