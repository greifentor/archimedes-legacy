/*
 * DefaultLineTextEditorComponentFactory.java
 *
 * 06.01.2004
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
 * LineTextEditor-Komponente.
 * 
 * @author O.Lieshoff
 *
 */

public class DefaultLineTextEditorComponentFactory extends DefaultComponentFactory {
    
    /** Statische Factory zur applikationsweiten Benutzung. */
    public static final DefaultLineTextEditorComponentFactory INSTANZ = 
            new DefaultLineTextEditorComponentFactory();
            
    /* 
     * Die LineTextEditorComponentFactory, aus der die produzierten Komponenten ihre 
     * Bestandteile beziehen sollen.
     */
    private LineTextEditorComponentFactory ltecf = null;
    /* Der Context-Name, den die Komponente tragen soll. */
    private String context = null;
    
    /* Generiert eine LineTextEditorComponentFactory. */
    private DefaultLineTextEditorComponentFactory() {
        super();
    }
    
    /**
     * Generiert eine DefaultLineTextEditorComponentFactory mit den angebenen Parametern.
     *
     * @param ltecf Die LineTextEditorComponentFactory aus der die von dieser Factory 
     *     produzierten Komponenten mit Unterkomponenten zu versorgen.
     * @param c Der Context-Name, unter dem der LineTextEditor generiert werden soll,
     */
    public DefaultLineTextEditorComponentFactory(LineTextEditorComponentFactory ltecf, String c)
            {
        super();
        this.context = c;
        this.ltecf = ltecf;
    }
    

    /* Implementierung des Interfaces ComponentFactory. */
    
    public JComponent createComponent(EditorDescriptor ed, Component owner, Inifile ini) 
            throws IllegalArgumentException {
        Attributed attr = ed.getObject();
        Object obj = attr.get(ed.getAttributeId());
        if (obj instanceof String) {
            if ((this.ltecf instanceof ModalLineTextEditorComponentFactory) 
                    && (owner instanceof Frame)) {
                ((ModalLineTextEditorComponentFactory) this.ltecf).setModalParent((Frame) owner
                        );
            }   
            LineTextEditor lte = new LineTextEditor(obj.toString(), this.ltecf, ini, 
                    this.context);
            if (ed.isDisabled()) {
                lte.setEnabled(false);
            }
            return lte;
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
        attr.get(ed.getAttributeId());
        if (comp instanceof LineTextEditor) {
            LineTextEditor lte = (LineTextEditor) comp;
            attr.set(ed.getAttributeId(), lte.getText());
            return;
        }
        super.transferValue(ed, comp);
    }
    
}
