/* 
 * DefaultMassiveListSelectorComponentFactory.java
 *
 * 03.08.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import corent.base.*;

import java.awt.*;

import javax.swing.*;


/** 
 * Diese Standardimplementierung des MassiveListSelectorComponentFactory-Interfaces arbeitet
 * mit einfachen Komponenten und sollte im Normalfall seinen Zweck erf&uuml;llen.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 31.03.2008 - Anpassungen, um der Komponente die F&auml;higkeit einzur&auml;umen, mit
 *             Property-gest&uuml;tzten Ressourcen zu arbeiten.
 *
 */
 
public class DefaultMassiveListSelectorComponentFactory 
        implements MassiveListSelectorComponentFactory {
        
    private String alternateNullString = null; 
    private RessourceManager rm = new PropertyRessourceManager(); 
    
    /** Generiert eine DefaultMassiveListSelectorComponentFactory mit Defaultwerten. */
    public DefaultMassiveListSelectorComponentFactory() {
        super();
    }
    
    
    /* Implementierung des Interfaces MassiveListSelectorComponentFactory. */
    
    public JButton createButtonClear() {
        COButton cob = new COButton(StrUtil.FromHTML(System.getProperty(
                "corent.gui.massive.list.selector.button.clear.text", "C")), 
                ".corent.gui.massive.list.selector.button.clear");
        cob.update(this.rm);
        return cob;
    }
    
    public JTextField createDisplayField() {
        JTextField jtf = new JTextField(50);
        jtf.setEditable(false);
        // jtf.setEnabled(false);
        jtf.setForeground(Color.black);
        return jtf;
    }
    
    public JButton createButtonSelect() {
        COButton cob = new COButton(StrUtil.FromHTML(System.getProperty(
                "corent.gui.massive.list.selector.button.select.text", "Auswahl")), 
                ".corent.gui.massive.list.selector.button.select");
        cob.update(this.rm);
        return cob;
    }
    
    public String createNullString() {
        if (this.alternateNullString != null) {
            return this.alternateNullString;
        }
        return System.getProperty("corent.gui.massive.list.selector.box.nullstring.text", 
                "<...>");
    }
    
    public Class getCls() {
        throw new UnsupportedOperationException("Die getCls()-Methode wird nicht unterstuetzt!"
                );
    }
    
    public void setCls(Class cls) {
        throw new UnsupportedOperationException("Die setCls(Class)-Methode wird nicht "
                + "unterstuetzt!");
    }
    
    public void setNullString(String s) {
        this.alternateNullString = s;
    }
    
}
