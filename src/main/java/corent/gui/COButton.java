/* 
 * COButton.java
 *
 * 17.04.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import java.awt.*;

import javax.swing.*;


/**
 * Erweitert einen JButton um das Verhalten eines ContextOwners.
 *
 * @author O.Lieshoff
 *
 */
 
public class COButton extends JButton implements Ressourced {
    
    /* Flagge f&uuml;r die harte Abblendung. */
    private boolean strongdisabled = false;
    /* Der Context zur Komponente. */
    private String context = null;

    /** 
     * Generiert einen COButton mit dem angegebenen Context.
     *
     * @param c Der Context zum Button.
     */
    public COButton(String c) {
        super();
        this.setContext(c);
    }

    /**
     * Generiert einen COButton mit Context und Action.
     *
     * @param a Die Action, die ein Buttonclick ausl&ouml;sen soll.
     * @param c Der Context zum Button.
     */
    public COButton(Action a, String c) {
        super(a);
        this.setContext(c);
    }

    /**
     * Generiert einen COButton mit Context und Icon.
     *
     * @param icon Das Icon, das auf dem Button abgebildet werden soll.
     * @param c Der Context zum Button.
     */
    public COButton(Icon icon, String c) {
        super(icon);
        this.setContext(c);
    }

    /**
     * Generiert einen COButton mit Context und Beschriftung.
     *
     * @param text Der Text, mit dem der Button beschriftet werden soll.
     * @param c Der Context zum Button.
     */
    public COButton(String text, String c) {
        super(text);
        this.setContext(c);
    }

    /**
     * Generiert einen COButton mit Context, Beschriftung und Icon.
     *
     * @param text Der Text, mit dem der Button beschriftet werden soll.
     * @param icon Das Icon, das auf dem Button abgebildet werden soll.
     * @param c Der Context zum Button.
     */
    public COButton(String text, Icon icon, String c) {
        super(text, icon);
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
        if (s.length() > 0) {
            this.setText(s);
        }
        s = rm.getToolTipText(c);
        if (s.length() > 0) {
            this.setToolTipText(s);
        }
        char ch = rm.getMnemonic(c);
        if (ch != '\u00FF') {
            this.setMnemonic(ch);
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
