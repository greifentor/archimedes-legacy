/*
 * DefaultFilenameSelectorComponentFactory.java
 *
 * 07.07.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.base.*;
import corent.files.*;
import corent.gui.*;

import java.awt.*;
import javax.swing.*;


/**
 * Diese Erweiterung der DefaultComponentFactory erzeugt f&uuml;r String-Variablen eine 
 * FilenameSelector-Komponente.
 * 
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 07.07.2008 - Hinzugef&uuml;gt.
 *     <P>
 *
 */

public class DefaultFilenameSelectorDefaultComponentFactory extends DefaultComponentFactory {
    
    /** Statische Factory zur applikationsweiten Benutzung. */
    public static final DefaultFilenameSelectorDefaultComponentFactory INSTANZ = 
            new DefaultFilenameSelectorDefaultComponentFactory();
            
    /* 
     * Die FilenameSelectorComponentFactory, aus der die produzierten Komponenten ihre 
     * Bestandteile beziehen sollen.
     */
    private FilenameSelectorComponentFactory fscf = null;
    
    /* Generiert eine FilenameSelectorComponentFactory. */
    private DefaultFilenameSelectorDefaultComponentFactory() {
        super();
    }
    
    /**
     * Generiert eine DefaultFilenameSelectorComponentFactory mit den angebenen Parametern.
     *
     * @param fscf Die FilenameSelectorComponentFactory aus der die von dieser Factory 
     *     produzierten Komponenten mit Unterkomponenten zu versorgen.
     */
    public DefaultFilenameSelectorDefaultComponentFactory(FilenameSelectorComponentFactory fscf)
            {
        super();
        this.fscf = fscf;
    }
    

    /* Implementierung des Interfaces ComponentFactory. */
    
    public JComponent createComponent(EditorDescriptor ed, Component owner, Inifile ini) 
            throws IllegalArgumentException {
        Attributed attr = ed.getObject();
        Object obj = attr.get(ed.getAttributeId());
        if (obj instanceof String) {
            if ((this.fscf instanceof DefaultFilenameSelectorComponentFactory) 
                    && (owner instanceof Frame)) {
                // ((DefaultFilenameSelectorComponentFactory) this.fscf).setModalParent(
                //        (Frame) owner);
            }   
            FilenameSelector fs = new FilenameSelector(obj.toString(), this.fscf, ini, 
                    ed.getName());
            if (ed.isDisabled()) {
                fs.setEnabled(false);
            }
            return fs;
        }
        return super.createComponent(ed, owner, ini);
    }

    public void transferValue(EditorDescriptor ed, JComponent comp) 
            throws IllegalArgumentException {
        if (!(ed instanceof DefaultEditorDescriptor)) {
            throw new IllegalArgumentException("DefaultEditorDescriptor-object expected!");
        }
        if (ed.isDisabled()) {
            return;
        }
        Attributed attr = ed.getObject();
        if (comp instanceof FilenameSelector) {
            FilenameSelector fs = (FilenameSelector) comp;
            attr.set(ed.getAttributeId(), fs.getText());
            return;
        }
        super.transferValue(ed, comp);
    }
    
}
