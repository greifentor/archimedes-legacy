/*
 * DefaultTabbedPaneFactory.java
 *
 * 15.01.2004
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.djinn;


import java.awt.*;
import java.util.*;

import javax.swing.*;


/**
 * Diese Factory produziert ein JTabbedPane, das zur Aufnahme von Panels eines EditorDjnns 
 * dient. Sie ist eine Defaultimplementierung, die als Vorlage f&uuml;r komplexere Varianten 
 * dienen kann.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public class DefaultTabbedPaneFactory implements TabbedPaneFactory {
    
    /* Die Tab-&Uuml;berschriften der zu produzierenden TabbedPanes. */
    private TabDescriptor[] tabs = null;
    
    /**
     * Generiert eine DefaultLabelFactory anhand der &uuml;bergebenen Parameter.
     *
     * @param tabs Ein TabDescriptor-Array mit den &Uuml;berschriften der Tabs.
     */
    public DefaultTabbedPaneFactory(TabDescriptor[] tabs) {
        super();
        this.tabs = tabs;
    }

    
    /* Implementierung des Interfaces TabbedPaneFactory. */
    
    public JTabbedPane createTabbedPane() {
        JTabbedPane jtp = new JTabbedPane();
        for (int i = 0; i < tabs.length; i++) {
            TabDescriptor td = this.tabs[i];
            jtp.add((td.getText() != null ? td.getText() : ""), new JPanel(new GridLayout(1, 1))
                    );
            if (td.getMnemonic() != '\0') {
                // ab Version 1.4.1
                jtp.setMnemonicAt(i, td.getMnemonic());
            }
            if (td.getIcon() != null) {
                jtp.setIconAt(i, td.getIcon());
            }
        }
        return jtp;
    }
    
    /** @return Ein Vector mit den TabDescriptoren der Factory. */
    public Vector<TabDescriptor> getTabDescriptors() {
        Vector<TabDescriptor> vtd = new Vector<TabDescriptor>();
        for (int i = 0; i < tabs.length; i++) {
            vtd.addElement(tabs[i]);
        }
        return vtd;
    }
    
}
