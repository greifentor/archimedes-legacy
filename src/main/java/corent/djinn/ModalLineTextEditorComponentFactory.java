/*
 * ModalLineTextEditorComponentFactory.java
 *
 * 17.08.2005
 *
 * (c) O.Lieshoff
 *
 */

package corent.djinn;


import corent.base.*;
import corent.gui.*;

import java.awt.*;

import javax.swing.*;


/**
 * Generiert eine spezielle LineTextEditorComponentFactory deren ModalParent-Komponente zur
 * Laufzeit konfiguriert werden kann.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 07.07.2009 - Formatanpassungen und Kommentarerg&auml;nzungen. Dabei Einbau einer
 *         ersten (wenn auch eher provisorischen) Implementierung einer Linie zur Begrenzung
 *         der Zeilenl&auml;nge des Testeingabefeldes.
 *
 */

public class ModalLineTextEditorComponentFactory implements LineTextEditorComponentFactory {

    private static PropertyRessourceManager prm = new PropertyRessourceManager();

    /* Referenz auf den Owner zum TextEditor. */
    private Frame owner = null;
    /* Ein Text f&uuml;r den Label. */
    private String labeltext = "";

    /**
     * Generiert eine neue ModalLineTextEditorComponentFactory anhand der &uuml;bergebenen 
     * Parameter.
     *
     * @param labeltext Ein Labeltext zum LineTextEditor.
     */
    public ModalLineTextEditorComponentFactory(String labeltext) {
        super();
        this.labeltext = labeltext;
    }

    /**
     * Setzt die angegebene Komponente als neue Owner-Referenz ein.
     *
     * @param owner Die Komponente, die als neue Owner-Referenz zum LineTextEditor eingesetzt
     *     werden soll.
     */
    public void setModalParent(Frame owner) {
        this.owner = owner;
    }


    /* Implementierung des Interfaces LineTextEditorComponentFactory. */

    public JButton createAendernButton(LineTextEditor owner) {
        return new JButton(Utl.GetProperty(
                "corent.djinn.ModalLineTextEditorComponentFactory.button.edit.text", 
                "&Auml;ndern"));
    }

    public JTextField createTextField(LineTextEditor owner) {
        return new JTextField(50);
    }

    public String getTitel() {
        String text = Utl.GetProperty("corent.djinn.ModalLineTextEditorComponentFactory." 
                + this.labeltext + ".title");
        text = (text == null ? this.labeltext : text);
        return Utl.GetProperty("corent.djinn.ModalLineTextEditorComponentFactory.title", 
                "Bearbeiten $LABELTEXT").replace("$LABELTEXT", text); 
    }

    /**
     * @changed OLI 07.07.2009 - Einbau einer provisorischen Methodik zum Einblenden einer
     *         Linie zur Begrenzung der Zeilenl&auml;nge.
     */
    public JTextArea createTextArea(String text, TextAreaDialog owner) {
        JTextArea jta = new JTextArea(8, 40) {
            public void paint(Graphics g) {
                int lineAfterColumn = Integer.getInteger(
                        "corent.djinn.ModelLineTextEditorComponentFactory.line.after.column", 0
                        );
                int w = 0;
                int y = 0;
                super.paint(g);
                if (lineAfterColumn > 0) {
                    y = this.getHeight();
                    w = g.getFontMetrics(this.getFont()).stringWidth("W");
                    w = w * lineAfterColumn;
                    g.setColor(Color.lightGray);
                    g.drawLine(w, 0, w, y);
                }
            }
        };
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);
        jta.setText(text);
        if (Integer.getInteger(
                "corent.djinn.ModelLineTextEditorComponentFactory.line.after.column", 0) > 0) {
            jta.setFont(new Font("monospaced", Font.PLAIN, 14));
        }
        return jta;
    }

    public JButton createButtonOk(TextAreaDialog owner) {
        COButton b = new COButton("Ok", "corent.djinn.ModalLineTextEditorComponentFactory."
                + "button.ok");
        b.setMnemonic('O');
        b.update(ModalLineTextEditorComponentFactory.prm);
        return b;
    }

    public JButton createButtonVerwerfen(TextAreaDialog owner) {
        COButton b = new COButton("Verwerfen", "corent.djinn."
                + "ModalLineTextEditorComponentFactory.button.cancel");
        b.setMnemonic('V');
        b.update(ModalLineTextEditorComponentFactory.prm);
        return b;
    }

    public Frame getModalParent() {
        return this.owner;
    }

}
