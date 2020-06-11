/*
 * DefaultEditorDjinnCell.java
 *
 * 27.10.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.awt.*;

import javax.swing.*;
 


/** 
 * Diese Klasse h&auml;lt die zu einer im EditorDjinn gespeicherten Eingabekomponente 
 * geh&ouml;renden Daten zusammen.
 *
 * @author 
 *     O.Lieshoff
 *
 */

public class DefaultEditorDjinnCell {
    
    /* Der EditorDescriptor zur Komponente. */ 
    private EditorDescriptor ed = null;
    /* Die Nummer des Tabs, auf die Eingabekomponente abgebildet wird. */
    int tab = 0;
    /* Die eigentliche Eingabekomponente. */
    private JComponent component = null;
    /* Der Label zur Eingabekomponente, oder null, falls es keinen Label gibt. */
    private JLabel label = null;
    /* 
     * Das Panel, in dem die Komponente zwecks Rand eingestellt wird, oder null, wenn ohne 
     * Panel (dann aber auch keinen Rand bei Fokussierung). 
     */
    private JPanel panel = null;
    
    /**
     * Dieser Konstruktor generiert eine DefaultEditorCell anhand der &uuml;bergebenen 
     * Parameter.
     *
     * @param ed Der EditorDescriptor auf dessen Basis die Eingabecomponente generiert worden 
     *     ist.
     * @param component Die eigentliche Eingabekomponente.
     * @param label Ein Label zur Eingabekomponente.
     * @param panel Ein Panel, in das die Komponente zwecks Fokusmarkierung eingestellt wird.
     */
    public DefaultEditorDjinnCell(EditorDescriptor ed, JComponent component, JLabel label, 
            JPanel panel) {
        this(ed, component, label, panel, 0);
    }
    
    /**
     * Dieser Konstruktor generiert eine DefaultEditorCell anhand der &uuml;bergebenen 
     * Parameter.
     *
     * @param ed Der EditorDescriptor auf dessen Basis die Eingabecomponente generiert worden 
     *     ist.
     * @param component Die eigentliche Eingabekomponente.
     * @param label Ein Label zur Eingabekomponente.
     * @param panel Ein Panel, in das die Komponente zwecks Fokusmarkierung eingestellt wird.
     * @param tab Die Nummer des Tabs, auf dem die Eingabekomponente abgebildet werden soll.
     */
    public DefaultEditorDjinnCell(EditorDescriptor ed, JComponent component, JLabel label, 
            JPanel panel, int tab) {
        super();
        this.component = component;
        this.ed = ed;
        this.label = label;
        this.panel = panel;
        this.tab = tab;
        //* SubEditoren werden teilweise nicht richtig behandelt. So funktioniert's nicht ...
        if (Boolean.getBoolean("corent.djinn.DefaultEditorDjinnCell.use.minimum.height") 
                && (this.component != null) && (this.component.getPreferredSize().height > 0)
                && (!(this.component instanceof SubEditor))) {
            JButton b = new JButton("Dummy");
            int h = b.getPreferredSize().height;
            if (Integer.getInteger("corent.djinn.DefaultEditorDjinnCell.preferred.height", h) 
                    > this.component.getPreferredSize().height) {
                int w = this.component.getPreferredSize().width;
                this.component.setMinimumSize(new Dimension(
                        Integer.getInteger("corent.djinn.DefaultEditorDjinnCell.minimum.width", 
                        w), Integer.getInteger(
                        "corent.djinn.DefaultEditorDjinnCell.minimum.height", h)));
                this.component.setPreferredSize(new Dimension(Integer.getInteger(
                        "corent.djinn.DefaultEditorDjinnCell.preferred.width", w), 
                        Integer.getInteger(
                        "corent.djinn.DefaultEditorDjinnCell.preferred.height", h)));
            }
        }
        // */
    }
    
    
    /* Accessoren und Mutatoren. */
    
    /**
     * Accessor f&uuml;r die Eigenschaft EditorDescriptor
     *
     * @return Der Wert der Eigenschaft EditorDescriptor
     */
    public EditorDescriptor getEditorDescriptor() {
        return this.ed;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft EditorDescriptor
     *
     * @param ed Der neue Wert f&uuml;r die Eigenschaft EditorDescriptor.
     */
    public void setEditorDescriptor(EditorDescriptor ed) {
        this.ed = ed;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Tab
     *
     * @return Der Wert der Eigenschaft Tab
     */
    public int getTab() {
        return this.tab;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft Tab
     *
     * @param tab Der neue Wert f&uuml;r die Eigenschaft Tab.
     */
    public void setTab(int tab) {
        this.tab = tab;
    }
    
    /**
     * Accessor f&uuml;r die Eigenschaft Component
     *
     * @return Der Wert der Eigenschaft Component
     */
    public JComponent getComponent() {
        return this.component;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft Component
     *
     * @param component Der neue Wert f&uuml;r die Eigenschaft Component.
     */
    public void setComponent(JComponent component) {
        this.component = component;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Label
     *
     * @return Der Wert der Eigenschaft Label
     */
    public JLabel getLabel() {
        return this.label;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft Label
     *
     * @param label Der neue Wert f&uuml;r die Eigenschaft Label.
     */
    public void setLabel(JLabel label) {
        this.label = label;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Panel
     *
     * @return Der Wert der Eigenschaft Panel
     */
    public JPanel getPanel() {
        return this.panel;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft Panel
     *
     * @param panel Der neue Wert f&uuml;r die Eigenschaft Panel.
     */
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }
    
}
