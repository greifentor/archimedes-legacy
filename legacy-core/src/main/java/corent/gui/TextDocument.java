/*
 * TextDocument.java
 *
 * version 1.0.0
 *
 * 14.12.2001
 *
 */

package corent.gui;


import java.awt.*;
import javax.swing.text.*;


/**
 * Die Klasse <TT>TextDocument</TT> dient als Textdokument f&uuml;r die formatierte Eingaben in 
 * einem TextField.
 */
public class TextDocument extends PlainDocument {

    /* Attribute. */
    private JTextComponent tc;
    private int maxSize = 0;

    /**
     * Generiert ein TextDocument anhand der &uuml;bergebenen Parameter.
     *
     * @param tc die TextKomponente f&uuml;r dieses Dokument.
     * @param maxSize die maximale Eingabel&auml;nge f&uuml;r dieses Dokument.
     */
    public TextDocument(JTextComponent tc, int maxSize) {
        this.tc = tc;
        if (maxSize > 0) {
            this.maxSize = maxSize;
        }
    }

    /**
     * Generiert ein TextDocument anhand der &uuml;bergebenen Parameter.
     *
     * @param tc die TextKomponente f&uuml;r dieses Dokument.
     */
    public TextDocument(JTextComponent tc) {
        this(tc, 0);
    }

    /**
     * F&uuml;gt einen String an der angegebenen Stelle in das Document ein. 
     *
     * @param offset an welcher Stelle soll der String eingef&uuml;gt werden.
     * @param s der einzuf&uuml;gende String.
     * @param as AttributeSet zur Modifikation der Operation.
     * @throws BadLocationException.
     */
    public void insertString (int offset, String s, AttributeSet as) 
            throws BadLocationException {
        if (s != null) {
            if ((maxSize > 0) && (tc.getText().length() + s.length() > maxSize)) {
                Toolkit.getDefaultToolkit().beep();
                return;
            } else {
                super.insertString(offset, s, as);
            }
        }
    }

    /**
     * Setzt die maximale L&auml;nge f&uuml;r das TextDocument.
     *
     * @param maxSize die maximale L&auml;nge des Dokumentes.
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /** @return Die maximale L&auml;nge des TextDocuments. */
    public int getMaxSize() {
        return this.maxSize;
    }

}

