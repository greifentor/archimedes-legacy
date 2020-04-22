/*
 * BaccaraLineTextEditorComponentFactory.java
 *
 * 15.09.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;

import java.awt.*;

import javax.swing.*;

import baccara.gui.*;
import corent.gui.*;

/**
 * A factory for line text editor components in Baccara applications.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 15.09.2016 - Added.
 */

public class BaccaraLineTextEditorComponentFactory implements LineTextEditorComponentFactory {

    private ButtonFactory buttonFactory = null;
    private GUIBundle guiBundle = null;
    private Frame owner = null;;
    private String resourceId = null;

    /**
     * Creates a new line text editor component factory for Baccara applications with the
     * passed parameters.
     *
     * @param guiBundle An access to the application resources.
     * @param resourceId A base resource id for the line editor.
     *
     * @changed OLI 15.09.2016 - Added.
     */
    public BaccaraLineTextEditorComponentFactory(GUIBundle guiBundle, String resourceId) {
        super();
        this.guiBundle = guiBundle;
        this.resourceId = resourceId;
        this.buttonFactory = new ButtonFactory(this.guiBundle);
    }

    /**
     * @changed OLI 15.09.2016 - Added.
     */
    @Override public JButton createAendernButton(LineTextEditor linetexteditor) {
        return this.buttonFactory.createButton(this.resourceId + ".button.edit.text", "edit");
    }

    /**
     * @changed OLI 15.09.2016 - Added.
     */
    @Override public JButton createButtonOk(TextAreaDialog textareadialog) {
        return this.buttonFactory.createButton(this.resourceId + ".button.ok.text", "ok");
    }

    /**
     * @changed OLI 15.09.2016 - Added.
     */
    @Override public JButton createButtonVerwerfen(TextAreaDialog textareadialog) {
        return this.buttonFactory.createButton(this.resourceId + ".button.cancel.text", "cancel"
                );
    }

    /**
     * @changed OLI 15.09.2016 - Added.
     */
    @Override public JTextArea createTextArea(String text, TextAreaDialog textareadialog) {
        JTextArea jta = new JTextArea(8, 40) {
            @Override public void paint(Graphics g) {
                int lineAfterColumn = Integer.getInteger(
                        "baccara.gui.BaccaraLineTextEditorComponentFactory.line.after.column",
                        80);
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
                "baccara.gui.BaccaraLineTextEditorComponentFactory.line.after.column", 0) > 0) {
            jta.setFont(new Font("monospaced", Font.PLAIN, 14));
        }
        return jta;
    }

    /**
     * @changed OLI 15.09.2016 - Added.
     */
    @Override public JTextField createTextField(LineTextEditor linetexteditor) {
        return new JTextField(50);
    }

    /**
     * @changed OLI 15.09.2016 - Added.
     */
    @Override public Frame getModalParent() {
        return this.owner;
    }

    /**
     * @changed OLI 15.09.2016 - Added.
     */
    @Override public String getTitel() {
        return this.guiBundle.getResourceText(this.resourceId + ".title");
    }

    public void setModalParent(Frame owner) {
        this.owner = owner;
    }

}