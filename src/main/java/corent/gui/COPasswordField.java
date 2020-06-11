/*
 * COPasswordField.java
 *
 * 28.11.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import javax.swing.*;
import javax.swing.text.*;


/**
 * Diese Spezialisierung eines JPasswordFields erm&ouml;glicht die Benennung einer Komponente 
 * mit einem Contextnamen.
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 * 
 * @changed
 *     OLI 28.11.2008 - Hinzugef&uuml;gt.
 *     <P>
 *
 */
 
public class COPasswordField extends JPasswordField implements ContextOwner {
    
    /* Flagge f&uuml;r die harte Abblendung. */
    private boolean strongdisabled = false;
    /* Der Context zur Komponente. */
    private String context = null;
    
    /** 
     * Generiert ein COPasswordField anhand der &uuml;bergebenen Parameter.
     *
     * @param c Der Context zum PasswordField.
     */
    public COPasswordField(String c) {
        super();
        this.setContext(c);
    }
    
    /** 
     * Generiert ein COPasswordField anhand der &uuml;bergebenen Parameter.
     *
     * @param doc Der zu benutzende Textspeicher. Wird an dieser Stelle eine 
     *     <TT>null</TT>-Referenz &uuml;bergeben, so wird ein Defaultmodel benutzt.
     * @param text Ein initialer Text oder <TT>null</TT>.
     * @param columns Zeichenzahl zur Berechnung der gew&uuml;nschten Breite des Textfeldes.
     * @param c Der Context zum PasswordField.
     */
    public COPasswordField(Document doc, String text, int columns, String c) {
        super(doc, text, columns);
        this.setContext(c);
    }

    /** 
     * Generiert ein COPasswordField anhand der &uuml;bergebenen Parameter.
     *
     * @param columns Zeichenzahl zur Berechnung der gew&uuml;nschten Breite des Textfeldes.
     * @param c Der Context zum PasswordField.
     */
    public COPasswordField(int columns, String c) {
        super(columns);
        this.setContext(c);
    }
    
    /** 
     * Generiert ein COPasswordField anhand der &uuml;bergebenen Parameter.
     *
     * @param text Ein initialer Text oder <TT>null</TT>.
     * @param c Der Context zum PasswordField.
     */
    public COPasswordField(String text, String c) {
        super(text);
        this.setContext(c);
    }
    
    /** 
     * Generiert ein COPasswordField anhand der &uuml;bergebenen Parameter.
     *
     * @param text Ein initialer Text oder <TT>null</TT>.
     * @param columns Zeichenzahl zur Berechnung der gew&uuml;nschten Breite des Textfeldes.
     * @param c Der Context zum PasswordField.
     */
    public COPasswordField(String text, int columns, String c) {
        super(text, columns);
        this.setContext(c);
    }

    public void setEnabled(boolean b) {
        if (b && this.strongdisabled) {
            return;
        }
        super.setEnabled(b);
    }


    /* Implementierung des Interfaces ContextOwner. */
    
    public String getContext() {
        return this.context;
    }
    
    public boolean isStrongDisabled() {
        return this.strongdisabled;
    }
    
    public void setContext(String n) {
        this.context = n;
    }
    
    public synchronized void setStrongDisabled(boolean b) {
        this.setEnabled(false);
        this.strongdisabled = b;
    }

}
