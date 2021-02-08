/*
 * COCheckBox.java
 *
 * 16.11.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import java.awt.*;

import javax.swing.*;


/**
 * Diese Spezialisierung der JCheckBox ist um einen Context erweitert, der die Komponente 
 * identifizieren kann.
 *
 * @author O.Lieshoff
 *
 * @todo OLI 20.10.2009 - Umstellung auf Ressourced-Interface (OLI 19.10.2009).
 */
 
public class COCheckBox extends JCheckBox implements ContextOwner {

    /* Flagge f&uuml;r die harte Abblendung. */
    private boolean strongdisabled = false;
    /* Der Context zur Komponente. */
    private String context = null;

    /**
     * Erzeugt eine COCheckBox mit dem angegebenen Context.
     *
     * @param c Der Context zur CheckBox.
     */
    public COCheckBox(String c) {
        super();
        this.setContext(c);
    }
    
    /**
     * Erzeugt eine COCheckBox anhand der &uuml;bergebenen Parameter.
     *
     * @param a Eine Action, aus deren Properties die CheckBox erzeugt wird.
     * @param c Der Context zur CheckBox.
     */
    public COCheckBox(Action a, String c) {
        super(a);
        this.setContext(c);
    }
    
    /**
     * Erzeugt eine COCheckBox anhand der &uuml;bergebenen Parameter.
     *
     * @param icon Das mit der CheckBox anzuzeigende Icon.
     * @param c Der Context zur CheckBox.
     */
    public COCheckBox(Icon icon, String c) {
        super(icon);
        this.setContext(c);
    }
    
    /**
     * Erzeugt eine COCheckBox anhand der &uuml;bergebenen Parameter.
     *
     * @param icon Das mit der CheckBox anzuzeigende Icon.
     * @param selected Wird diese Flagge gesetzt, so wird die CheckBox selektiert dargestellt.
     * @param c Der Context zur CheckBox.
     */
    public COCheckBox(Icon icon, boolean selected, String c) {
        super(icon, selected);
        this.setContext(c);
    }
    
    /**
     * Erzeugt eine COCheckBox anhand der &uuml;bergebenen Parameter.
     *
     * @param text Ein Text, der neben der CheckBox angezeigt wird.
     * @param c Der Context zur CheckBox.
     */
    public COCheckBox(String text, String c) {
        super(text);
        this.setContext(c);
    }
    
    /**
     * Erzeugt eine COCheckBox anhand der &uuml;bergebenen Parameter.
     *
     * @param text Ein Text, der neben der CheckBox angezeigt wird.
     * @param selected Wird diese Flagge gesetzt, so wird die CheckBox selektiert dargestellt.
     * @param c Der Context zur CheckBox.
     */
    public COCheckBox(String text, boolean selected, String c) {
        super(text, selected);
        this.setContext(c);
    }
    
    /**
     * Erzeugt eine COCheckBox anhand der &uuml;bergebenen Parameter.
     *
     * @param text Ein Text, der neben der CheckBox angezeigt wird.
     * @param icon Das mit der CheckBox anzuzeigende Icon.
     * @param c Der Context zur CheckBox.
     */
    public COCheckBox(String text, Icon icon, String c) {
        super(text, icon);
        this.setContext(c);
    }
    
    /**
     * Erzeugt eine COCheckBox anhand der &uuml;bergebenen Parameter.
     *
     * @param text Ein Text, der neben der CheckBox angezeigt wird.
     * @param icon Das mit der CheckBox anzuzeigende Icon.
     * @param selected Wird diese Flagge gesetzt, so wird die CheckBox selektiert dargestellt.
     * @param c Der Context zur CheckBox.
     */
    public COCheckBox(String text, Icon icon, boolean selected, String c) {
        super(text, icon, selected);
        this.setContext(c);
    }
    
    public void setEnabled(boolean b) {
        if (b && this.strongdisabled) {
            return;
        }
        super.setEnabled(b);
    }

    
    /* Implementierung des Interfaces Ressourced. */
    
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

    public void update(RessourceManager rm) {
        String c = COUtil.GetFullContext(this);
        String s = rm.getText(c);
        char ch = rm.getMnemonic(c);
        if (s.length() > 0) {
            this.setText(s);
        }
        if (ch != '\u00FF') {
            this.setMnemonic(ch);
        }
        s = rm.getToolTipText(c);
        if (s.length() > 0) {
            this.setToolTipText(s);
        }
        Icon icon = rm.getIcon(c);
        if (icon != null) {
            this.setIcon(icon);
        }
        Color color = rm.getBackground(c);
        if (color != null) {
            this.setBackground(color);
        }
        color = rm.getForeground(c);
        if (color != null) {
            this.setForeground(color);
        }
    }

}
