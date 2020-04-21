/*
 * TextAreaDialogComponentFactory.java
 *
 * 06.10.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.gui;


import java.awt.*;

import javax.swing.*;


/**
 * Diese Factory erzeugt die Komponenten f&uuml;r einen TextAreaEditor.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 07.07.2009 - Formatanpassungen und Kommentarerg&auml;nzungen.
 *
 */

public interface TextAreaDialogComponentFactory {

    /**
     * Generiert die TextArea, in der die Texteingabe stattfinden soll.
     *
     * @param text Der Text, der in dem Dialog angezeigt und ge&auml;ndert werden soll.
     * @param owner Eine Referenz auf den TextAreaDialog, zu dem der Button geh&ouml;rt.
     * @return Die TextArea, in der der Text angezeigt und ge&auml;ndert werden soll.
     */
    public JTextArea createTextArea(String text, TextAreaDialog owner);

    /**
     * Generiert den OK-Button des Dialoges.
     *
     * @param owner Eine Referenz auf den TextAreaDialog, zu dem der Button geh&ouml;rt.
     * @return Der Ok-Button des Dialoges.
     */
    public JButton createButtonOk(TextAreaDialog owner);

    /**
     * Generiert den Verwerfen-Button (Cancel) des Dialoges.
     *
     * @param owner Eine Referenz auf den TextAreaDialog, zu dem der Button geh&ouml;rt.
     * @return Der Verwerfen-Button des Dialoges.
     */
    public JButton createButtonVerwerfen(TextAreaDialog owner);

    /**
     * Liefert das Window, zu dem sich der TextAreaEditor modal verhalten soll.
     *
     * @return Das Window, auf das sich der TextAreaEditor modal absetzen soll.
     */
    public Frame getModalParent();

    /**
     * Liefert den Titel f&uuml;r das Dialog-Fenster.
     *
     * @return Der Titel f&uuml;r das Dialog-Fenster.
     */
    public String getTitel();

}
