/*
 * SizedTextField.java
 *
 * 21.11.2006
 *
 * (c) by O.Lieshoff
 *
 */

package corent.gui;


import javax.swing.*;
import javax.swing.text.*;


/**
 * Dieses TextField ist in der Art erweitert, da&szlig; es nur eine bestimmte Anzahl von Zeichen
 * als Eingabe akzeptiert und weitere Zeichen dann ablehnt.
 *
 * @author O.Lieshoff
 */

public class SizedTextField extends JTextField {

    /* Maximale Anzahl der Zeichen, die in das TextField eingetragen werden k&ouml;nnen. */
    private int maxSize = 65535;
    /* 
     * Ist Flagge gesetzt, so lehnt das TextField die Eingabe von Zeichen oberhalb der 
     * vereinbarten Grenzen ab. 
     */
    private boolean lengthControlEnabled = true;

    /**
     * Generiert ein SizedTextField mit einer maximalen Eingabel&auml;nge von 65535 Zeichen.  
     */
    public SizedTextField() {
        super();
        this.initDocument();
    }

    /** 
     * Generiert ein SizedTextField anhand der &uuml;bergebenen Parameter.
     *
     * @param doc Der zu benutzende Textspeicher. Wird an dieser Stelle eine 
     *     <TT>null</TT>-Referenz &uuml;bergeben, so wird ein Defaultmodel benutzt.
     * @param text Ein initialer Text oder <TT>null</TT>.
     * @param columns Zeichenzahl zur Berechnung der gew&uuml;nschten Breite des Textfeldes.
     */
    public SizedTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        this.initDocument();
        this.setText(text);
    }

    /** 
     * Generiert ein SizedTextField anhand der &uuml;bergebenen Parameter.
     *
     * @param columns Zeichenzahl zur Berechnung der gew&uuml;nschten Breite des Textfeldes.
     */
    public SizedTextField(int columns) {
        super(columns);
        this.initDocument();
    }
    
    /** 
     * Generiert ein SizedTextField anhand der &uuml;bergebenen Parameter.
     *
     * @param text Ein initialer Text oder <TT>null</TT>.
     */
    public SizedTextField(String text) {
        super(text);
        this.initDocument();
    }
    
    /** 
     * Generiert ein SizedTextField anhand der &uuml;bergebenen Parameter.
     *
     * @param text Ein initialer Text oder <TT>null</TT>.
     * @param columns Zeichenzahl zur Berechnung der gew&uuml;nschten Breite des Textfeldes.
     */
    public SizedTextField(String text, int columns) {
        super(text, columns);
        this.initDocument();
        this.setText(text);
    }
    
    /** 
     * Generiert ein SizedTextField anhand der &uuml;bergebenen Parameter.
     *
     * @param text Ein initialer Text oder <TT>null</TT>.
     * @param columns Zeichenzahl zur Berechnung der gew&uuml;nschten Breite des Textfeldes.
     * @param maxSize Die maximale Anzahl der eingebbaren Zeichen.
     */
    public SizedTextField(String text, int columns, int maxSize) {
        super(text, columns);
        this.initDocument(maxSize);
        this.setText(text);
    }

    private void initDocument() {
        this.initDocument(0);
    }
    
    private void initDocument(int maxSize) {
        if (maxSize > 0) {
            this.maxSize = maxSize;
        }    
        this.setDocument(new TextDocument(this, this.maxSize));
    }
    
    /** @return Die Anzahl der maximalen Eingabel&auml;nge f&uuml;r das TextField. */
    public int getMaxSize() {
        return this.maxSize;
    }
       

    /* Ueberschreiben von Methoden der Superklasse. */
    
    /**
     * Setzt den angegebenen Text in das TextField ein.
     *
     * @param text Der neue Text des TextField.
     */
    public void setText(String text) {
        if (this.lengthControlEnabled) {
            if (text == null) {
                text = "";
            } else if (text.length() < 1) {
                text = "";
            }
            try {
                Document doc = getDocument();
                doc.remove(0, doc.getLength());
                doc.insertString(0, text, null);
            } catch (BadLocationException e) {
                getToolkit().beep();
            }
        } else {
            super.setText(text);
        }
    }

    /**
     * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r die Eigenschaft 
     * EnableLengthControl. Wird diese Eigenschaft gesetzt, wird bei der Eingabe neuer Zeichen
     * auf die maximale Eingabel&auml;nge gepr&uuml;ft.
     *
     * @param b Der neue Wert f&uuml;r die Eigenschaft EnableLengthControl. 
     */
    public void setEnableLengthControl(boolean b) {
        this.lengthControlEnabled = b;
    }

}
