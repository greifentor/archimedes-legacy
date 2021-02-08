/*
 * COLabel.java
 *
 * 11.09.2006
 *
 * (C) by O.Lieshoff
 *
 */
 
package corent.gui;


import corent.base.*;

import java.awt.*;

import javax.swing.*;


/**
 * Erweitert einen JLabel um das Verhalten eines ContextOwners.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 26.05.2008 - Erweiterung um das Attribut Obligation.
 *     <P>
 *
 */

public class COLabel extends JLabel implements Ressourced {
    
    /* Flagge zur Kennzeichnung von Labeln f&uuml;r Pflichtfelder. */
    private boolean obligation = false;
    /* Flagge f&uuml;r die harte Abblendung. */
    private boolean strongdisabled = false;
    /* Der Context zur Komponente. */
    private String context = null;

    /** 
     * Generiert einen COLabel mit dem angegebenen Context.
     *
     * @param c Der Context zum Label.
     */
    public COLabel(String c) {
        super();
        this.setContext(c);
    }

    /** 
     * Generiert einen COLabel mit dem angegebenen Context und Icon.
     *
     * @param icon Das Icon, das in dem Label abgebildet werden soll.
     * @param c Der Context zum Label.
     */
    public COLabel(Icon icon, String c) {
        super(icon);
        this.setContext(c);
    }
    
    /** 
     * Generiert einen COLabel mit dem angegebenen Context, Icon und Ausrichtungsangabe.
     *
     * @param icon Das Icon, das in dem Label abgebildet werden soll.
     * @param horizontalAlignment Die Angabe zur horizontalen Ausrichtung des Icons innerhalb 
     *     des Labels.
     * @param c Der Context zum Label.
     */
    public COLabel(Icon icon, int horizontalAlignment, String c) {
        super(icon, horizontalAlignment);
        this.setContext(c);
    }
    
    /** 
     * Generiert einen COLabel mit dem angegebenen Context und Text.
     *
     * @param text Der Text, der in dem Label abgebildet werden soll.
     * @param c Der Context zum Label.
     */
    public COLabel(String text, String c) {
        super(text);
        this.setContext(c);
    }
    
    /** 
     * Generiert einen COLabel mit dem angegebenen Context, Text und Ausrichtungsangabe.
     *
     * @param text Der Text, der in dem Label abgebildet werden soll.
     * @param horizontalAlignment Die Angabe zur horizontalen Ausrichtung des Textes innerhalb 
     *     des Labels.
     * @param c Der Context zum Label.
     */
    public COLabel(String text, int horizontalAlignment, String c) {
        super(text, horizontalAlignment);
        this.setContext(c);
    }
    
    /** 
     * Generiert einen COLabel mit dem angegebenen Context, Text, Icon und Ausrichtungsangabe.
     *
     * @param text Der Text, der in dem Label abgebildet werden soll.
     * @param icon Das Icon, das in dem Label abgebildet werden soll.
     * @param horizontalAlignment Die Angabe zur horizontalen Ausrichtung des Icons und des 
     *     Textes innerhalb des Labels.
     * @param c Der Context zum Label.
     */
    public COLabel(String text, Icon icon, int horizontalAlignment, String c) {
        super(text, icon, horizontalAlignment);
        this.setContext(c);
    }

    public void setEnabled(boolean b) {
        if (b && this.strongdisabled) {
            return;
        }
        super.setEnabled(b);
    }

    /**
     * Pr&uuml;ft, ob der Label ein Pflichtfelde kennzeichnet.
     *
     * @return <TT>true</TT>, wenn der Label ein Pflichtfeld kennzeichnet, <TT>false</TT> sonst.
     */
    public boolean isObligation() {
        return this.obligation;
    }
    
    /**
     * Setzt bzw. l&ouml;scht die Flagge, die Aussage dar&uuml;ber gibt, ob der Label zu einem
     * Pflichtfeld geh&ouml;rt.
     *
     * @param b Wird der Wert <TT>true</TT> &uuml;bergeben, so handelt es sich um einen Label zu
     *     einem Pflichtfeld.
     */
    public synchronized void setObligation(boolean b) {
        this.obligation = b;
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

    /**
     * @changed
     *     OLI 26.05.2008 - Erweiterung um die Verarbeitung der Obligation-Flagge zur 
     *             Kennzeichnung von Pflichtfeldern.
     *     <P>
     */
    public void update(RessourceManager rm) {
        String c = COUtil.GetFullContext(this);
        String s = rm.getText(c);
        if (s.length() > 0) {
            if (this.isObligation()) {
                s = Utl.GetProperty("corent.djinn.DefaultLabelFactory.label.obligation.prefix", 
                        "").concat(s).concat(Utl.GetProperty(
                        "corent.djinn.DefaultLabelFactory.label.obligation.suffix", ""));
            }
            this.setText(s);
        }
        char ch = rm.getMnemonic(c);
        if (ch != '\u00FF') {
            this.setDisplayedMnemonic(ch);
        }
        s = rm.getToolTipText(c);
        if (s.length() > 0) {
            if (this.isObligation()) {
                s = Utl.GetProperty(
                        "corent.djinn.DefaultLabelFactory.tooltiptext.obligation.prefix", ""
                        ).concat(s).concat(Utl.GetProperty(
                        "corent.djinn.DefaultLabelFactory.tooltiptext.obligation.suffix", ""));
            }
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
