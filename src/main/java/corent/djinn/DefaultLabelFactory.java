/*
 * DefaultLabelFactory.java
 *
 * 06.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.gui.*;

import javax.swing.*;


/**
 * Diese Klasse enth&auml;lt eine Standard-Implementierung der LabelFactory, die im 
 * Zusammenspiel mit DefaultEditorDescriptor einsetzbar ist. Sie kann als Vorlage f&uuml;r 
 * komplexere Implementierungen genutzt werden.
 * <P>Mit Hilfe der Properties <I>corent.djinn.DefaultLabelFactory.label.obligation.prefix</I>
 * und <I>corent.djinn.DefaultLabelFactory.label.obligation.suffix</I> (beide String) 
 * k&ouml;nnen Kennzeichen f&uuml;r Pflichtfelder angegeben werden, die an den Label zum Feld
 * gekoppelt sind. Analog lassen sich die ToolTipTexts zu Pflichtfeldern &uuml;ber die 
 * Properties <I>corent.djinn.DefaultLabelFactory.tooptiptext.obligation.prefix</I> und 
 * <I>corent.djinn.DefaultLabelFactory.tooptiptext.obligation.suffix</I> anpassen (ebenfalls 
 * beide String). F&uuml;hrende Leerzeichen m&uuml;ssen als "&amp;nbsp;" definiert werden.
 * 
 * @author O.Lieshoff
 *
 */

public class DefaultLabelFactory implements LabelFactory {
    
    /** Statische Factory zur applikationsweiten Benutzung. */
    public static final DefaultLabelFactory INSTANZ = new DefaultLabelFactory();
    
    /* Generiert eine DefaultLabelFactory. */
    private DefaultLabelFactory() {
        super();
    }
    
    
    /* Implementierung des Interfaces LabelFactory. */
    
    public JLabel createLabel(EditorDescriptor ed) throws IllegalArgumentException {
        if (!(ed instanceof DefaultEditorDescriptor)) {
            throw new IllegalArgumentException("DefaultEditorDescriptor-object expected!");
        }
        DefaultEditorDescriptor ded = (DefaultEditorDescriptor) ed;
        JLabel label = null;
        if ((ed.getName() != null) && (ed.getName().length() > 0)) {
            label = new COLabel(ed.getName() + ".label");
            ((COLabel) label).setObligation(ded.isObligation());
        } else {
            label = new JLabel();
        }
        if (ded.getMnemonic() != '\0') {
            label.setDisplayedMnemonic(ded.getMnemonic());
        }
        if (ded.getLabeltext() != null) {
            String s = ded.getLabeltext();
            if (ded.isObligation()) {
                s = System.getProperty(
                        "corent.djinn.DefaultLabelFactory.label.obligation.prefix", ""
                        ).concat(ded.getLabeltext()).concat(System.getProperty(
                        "corent.djinn.DefaultLabelFactory.label.obligation.suffix", "")
                        ).replace("&nbsp;", " ");
            }
            label.setText(s);
        }
        if (ded.getIcon() != null) {
            label.setIcon(ded.getIcon());
        }
        if (ded.getToolTipText() != null) {
            String s = ded.getToolTipText();
            if (ded.isObligation()) {
                s = System.getProperty(
                        "corent.djinn.DefaultLabelFactory.tooltiptext.obligation.prefix", ""
                        ).concat(ded.getToolTipText()).concat(System.getProperty(
                        "corent.djinn.DefaultLabelFactory.tooltiptext.obligation.suffix", ""
                        ).replace("&nbsp;", " "));
            }
            label.setToolTipText(s);
        }
        return label;
    }
    
}
