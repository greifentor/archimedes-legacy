/*
 * DefaultPasswordComponentFactory.java
 *
 * 07.10.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.base.*;
import corent.files.*;
import corent.gui.*;

import java.awt.Component;

import javax.swing.*;


/**
 * Diese Klasse enth&auml;lt eine Standard-Implementierung der ComponentFactory, die zur 
 * Generierung von Passwortfeldern eingesetzt werden kann.
 * 
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 02.12.2007 - Hinweis auf m&ouml;glichen Null-Pointer in Exception hinterlegt in 
 *             Methode <TT>createComponent(EditorDescriptor, Component, Inifile)</TT>.
 *     <P>OLI 22.04.2008 - Beseitigung des Bugs, der die Abblendung bei gesetzter 
 *             Disabled-Flagge verhindert hat.
 *     <P>OLI 28.11.2008 - Die Methode <TT>createComponent(EditorDescriptor, Component, Inifile)
 *            </TT> bei Angabe eines benannten EditorDescriptor ein COPasswordField mit 
 *             entsprechendem Kontextnamen zu erzeugen.
 *     <P>
 *
 */

public class DefaultPasswordComponentFactory implements ComponentFactory {
    
    /** Statische Factory zur applikationsweiten Benutzung. */
    public static final DefaultPasswordComponentFactory INSTANZ = 
            new DefaultPasswordComponentFactory();
    
    /* Generiert eine DefaultPasswordComponentFactory. */
    protected DefaultPasswordComponentFactory() {
        super();
    }


    /* Implementierung des Interfaces ComponentFactory. */

    /**
     * @changed
     *     OLI 02.12.2007 - Hinweis auf m&ouml;glichen Null-Pointer in Exception hinterlegt.
     *     <P>OLI 22.04.2008 - Die Disabled-Flagge des EditorDescriptors wird nun korrekt 
     *             gesetzt.
     *     <P>OLI 28.11.2008 - Erweiterung um die Variante bei Angabe eines benamten 
     *             EditorDescriptor ein COPasswordField mit entsprechendem Kontextnamen zu 
     *             erzeugen.
     *     <P>
     *
     */
    public JComponent createComponent(EditorDescriptor ed, Component owner, Inifile ini) 
            throws IllegalArgumentException {
        if (!(ed instanceof DefaultEditorDescriptor)) {
            throw new IllegalArgumentException("DefaultEditorDescriptor-object expected!");
        }
        Attributed attr = ed.getObject();
        Object obj = attr.get(ed.getAttributeId());
        if (!(obj instanceof String)) {
            throw new IllegalArgumentException("Passwordfields can only be created for String "
                    + "attributes!\nThere maybe a null value in field " + ed.getName() + ".");
        }
        JPasswordField jpf = null;
        if ((ed.getName() != null) && (ed.getName().length() > 0)) {
            jpf = new COPasswordField(obj.toString(), 25, ed.getName());
        } else {
            jpf = new JPasswordField(obj.toString(), 25);
        }
        jpf.setEnabled(!ed.isDisabled());
        return jpf;
    }

    public void transferValue(EditorDescriptor ed, JComponent comp) 
            throws IllegalArgumentException {
        if (ed.isDisabled()) {
            return;
        }
        Attributed attr = ed.getObject();
        attr.set(ed.getAttributeId(), this.getValue(ed, comp));
    }
    
    public Object getValue(EditorDescriptor ed, JComponent comp) 
            throws IllegalArgumentException {
        if (!(ed instanceof DefaultEditorDescriptor)) {
            throw new IllegalArgumentException("DefaultEditorDescriptor-object expected!");
        }
        Attributed attr = ed.getObject();
        Object obj = attr.get(ed.getAttributeId());
        if (obj instanceof String) {
            if (!(comp instanceof JPasswordField)) {
                throw new IllegalArgumentException("Component not in valid type: " 
                        + comp.getClass().toString());
            }
            return new String(((JPasswordField) comp).getPassword());
        }
        throw new IllegalArgumentException("Attribute not valid for ComponentFactory: " 
                + (obj != null ? obj.getClass().toString() : "null"));
    }
    
}
