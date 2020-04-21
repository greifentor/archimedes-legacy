/*
 * DefaultTabDescriptor.java
 *
 * 15.01.2004
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.djinn;


import javax.swing.*;


/**
 * In dieser Klasse werden einige Daten zum Thema Intialisierung von Tabs zuammengefa&szlig;t.
 * <BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public class DefaultTabDescriptor implements TabDescriptor {
    
    /* Ein Mnemonic zum schnellen Umschalten zwischen den Tabs. */
    private char mnemonic = '\0';
    /* Ein Icon, das auf dem Tab abgebildet werden soll. */
    private Icon icon = null;
    /* Ein Text, der auf dem Tab dargestellt werden soll. */
    private String text = null;
    
    /** 
     * Generiert einen Tab-Datensatz anhand der vorgegebenen Parameter. 
     *
     * @param text Der Text zum Tab.
     * @param mnemonic Ein Mnemonic zum Tab.
     * @param icon Ein Icon, das auf dem Tab abgebildet werden soll.
     */
    public DefaultTabDescriptor(String text, char mnemonic, Icon icon) {
        super();
        this.icon = icon;
        this.mnemonic = mnemonic;
        this.text = text;
    }

    
    /* Implementierung des Interface TabDescriptor. */
    
    public String getText() {
        return this.text;
    }
    
    public char getMnemonic() {
        return this.mnemonic;
    }
    
    public Icon getIcon() {
        return this.icon;
    }
    
}
