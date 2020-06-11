/*
 * DefaultComponentFactory.java
 *
 * 06.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.base.*;
import corent.dates.*;
import corent.files.*;
import corent.gui.*;

import java.awt.Component;
import java.text.*;
import java.util.*;

import javax.swing.*;


/**
 * Diese Klasse enth&auml;lt eine Standard-Implementierung der ComponentFactory, die im 
 * Zusammenspiel mit DefaultEditorDescriptor einsetzbar ist. Sie kann als Vorlage f&uuml;r 
 * komplexere Implementierungen genutzt werden.
 * 
 * <P><H3>Properties:</H3>
 * <TABLE BORDER=1>
 *     <TR VALIGN=TOP>
 *         <TH>Property</TH>
 *         <TH>Default</TH>
 *         <TH>Typ</TH>
 *         <TH>Beschreibung</TH>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.djinn.DefaultComponentFactory.change.komma.to.point</TD>
 *         <TD>false</TD>
 *         <TD>boolean</TD>
 *         <TD>
 *             Wenn diese Flagge gesetzt wird, werden im Inhalt eines Zahlenfeldes vor dem 
 *             Umwandeln in eine Zahl die Kommata gegen Punkte umgewandelt.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>
 *             corent.djinn.DefaultComponentFactory.error.<BR>[date|number].format.[text|title]
 *         </TD>
 *         <TD></TD>
 *         <TD>String</TD>
 *         <TD>
 *             &Uuml;ber diese Properties werden die Ressourcen f&uuml;r die Hinweisdialoge 
 *             konfiguriert, die angezeigt werden, wenn ein Zahlen- bzw. Datumsfeld nicht 
 *             korrekt umgewandelt werden kann.<BR>
 *             Die <TT>text</TT>-Strings k&ouml;nnen den Platzhalter <TT>$VALUE</TT> enthalten,
 *             der vor der Anzeige des jeweiligen Dialoges durch den Inhalt des umzuwandelnden
 *             Dialogfeldes ersetzt wird.
 *         </TD>
 *     </TR>
 * </TABLE>
 * <P>&nbsp;
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 29.05.2008 - Einbau der M&ouml;glichkeit Kommata in Zahlenfeldern automatisch in
 *             Punkte umwandeln zu lassen.
 *     <P>
 *
 */

public class DefaultComponentFactory implements ComponentFactory {
    
    /** Statische Factory zur applikationsweiten Benutzung. */
    public static final DefaultComponentFactory INSTANZ = new DefaultComponentFactory();
    
    /* Eine eventuell &uuml;bergebenen Liste. */
    protected List liste = null;
    
    /* Generiert eine DefaultComponentFactory. */
    protected DefaultComponentFactory() {
        super();
    }
    
    /**
     * Generiert eine DefaultComponentFactory mit dem &uuml;bergebenen als Grundlage f&uuml;r 
     * eine ComboBox. 
     */
    public DefaultComponentFactory(List l) {
        super();
        this.liste = l;
    }
    
    
    /* Implementierung des Interfaces ComponentFactory. */
    
    public JComponent createComponent(EditorDescriptor ed, Component owner, Inifile ini) 
            throws IllegalArgumentException {
        if (!(ed instanceof DefaultEditorDescriptor)) {
            throw new IllegalArgumentException("DefaultEditorDescriptor-object expected!");
        }
        Attributed attr = ed.getObject();
        DefaultEditorDescriptor ded = (DefaultEditorDescriptor) ed;
        JComponent comp = null;
        Object obj = attr.get(ed.getAttributeId());        
        if (this.liste != null) {
            Vector liste = new Vector(this.liste);
            liste.insertElementAt("<...>", 0);
            JComboBox jb = new JComboBox(liste);
            if (obj != null) {
                jb.setSelectedItem(obj);
            } else {
                jb.setSelectedIndex(0);
            }
            comp = jb;
        } else if ((obj instanceof Double) || (obj instanceof Float) || (obj instanceof Integer) 
                || (obj instanceof Long) || (obj instanceof Short) || (obj instanceof String)) {
            if ((ed.getName() != null) && (ed.getName().length() > 0)) {
                comp = new COTextField(obj.toString(), 25, ed.getMaxSize(), ed.getName());
            } else { 
                comp = new SizedTextField(obj.toString(), 25, ed.getMaxSize());
            }
        } else if (obj instanceof Boolean) {
            if ((ed.getName() != null) && (ed.getName().length() > 0)) { 
                comp = new COCheckBox(ed.getName());
            } else { 
                comp = new JCheckBox();
            }
            ((JCheckBox) comp).setSelected(((Boolean) obj).booleanValue());
        } else if (obj instanceof PDate) {
            PDate pd = (PDate) obj;
            try {
                PTimestamp pts = null;
                if (pd.toInt() < 1) {
                    pts = PTimestamp.NULL;
                } else {
                    pts = new PTimestamp((long) pd.toInt() * 1000000);
                }
                if ((ed.getName() != null) && (ed.getName().length() > 0)) { 
                    comp = new COTimestampField(null, new DefaultTimestampFieldComponentFactory(
                            true, false, ini), pts, ed.getName());
                } else { 
                    comp = new TimestampField(null, new DefaultTimestampFieldComponentFactory(
                            true, false, ini), pts);
                }
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        } else if (obj instanceof PTimestamp) {
            PTimestamp pts = (PTimestamp) obj;
            if ((ed.getName() != null) && (ed.getName().length() > 0)) { 
                comp = new COTimestampField(null, new DefaultTimestampFieldComponentFactory(
                        true, true, ini), pts, ed.getName());
            } else { 
                comp = new TimestampField(null, new DefaultTimestampFieldComponentFactory(
                        true, true, ini), pts);
            }
        } else {
            if ((ed.getName() != null) && (ed.getName().length() > 0)) {
                comp = new COLabel(ed.getName());
            } else {
                comp = new JLabel();
            }
            ((JLabel) comp).setText((obj != null ? obj.toString() : "null"));
            if (obj == null) {
                System.out.println("WARNING: object is null. Maybe you have a null-value in "
                        + "the corresponding datafield (" + ded.getLabeltext() + ")");
            }
        }
        if (ed.isDisabled()) {
            comp.setEnabled(false);
        }
        return comp;
    }

    public void transferValue(EditorDescriptor ed, JComponent comp) 
            throws IllegalArgumentException {
        if (ed.isDisabled()) {
            return;
        }
        Attributed attr = ed.getObject();
        attr.set(ed.getAttributeId(), this.getValue(ed, comp));
        /*
        if (!(ed instanceof DefaultEditorDescriptor)) {
            throw new IllegalArgumentException("DefaultEditorDescriptor-object expected!");
        }
        if (ed.isDisabled()) {
            return;
        }
        Attributed attr = ed.getObject();
        DefaultEditorDescriptor ded = (DefaultEditorDescriptor) ed;
        Object obj = attr.get(ed.getAttributeId());
        if ((this.liste != null) && (comp instanceof JComboBox)) {
            JComboBox jb = (JComboBox) comp;
            if (jb.getSelectedIndex() > 0) {
                attr.set(ed.getAttributeId(), jb.getSelectedItem());
            } else {
                attr.set(ed.getAttributeId(), null);
            }
            return;
        } else if ((obj instanceof Double) || (obj instanceof Float) || (obj instanceof Integer) 
                || (obj instanceof Long) || (obj instanceof Short) || (obj instanceof String)) {
            if (!(comp instanceof JTextField)) {
                throw new IllegalArgumentException("Component not in valid type: " 
                        + comp.getClass().toString());
            }
            String value = ((JTextField) comp).getText();
            try {
                if (obj instanceof Double) {
                    attr.set(ed.getAttributeId(), new Double(value));
                    return;
                } else if (obj instanceof Float) {
                    attr.set(ed.getAttributeId(), new Float(value));
                    return;
                } else if (obj instanceof Integer) { 
                    attr.set(ed.getAttributeId(), new Integer(value));
                    return;
                } else if (obj instanceof Long) {
                    attr.set(ed.getAttributeId(), new Long(value));
                    return;
                } else if (obj instanceof Short) {
                    attr.set(ed.getAttributeId(), new Short(value));
                    return;
                } else if (obj instanceof String) {
                    attr.set(ed.getAttributeId(), value);
                    return;
                }
            } catch (DateFormatException dfe) {
                dfe.printStackTrace();
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        } else if (obj instanceof Boolean) {
            if (!(comp instanceof JCheckBox)) {
                throw new IllegalArgumentException("Component not in valid type: " 
                        + comp.getClass().toString());
            }
            boolean b = ((JCheckBox) comp).isSelected();
            attr.set(ed.getAttributeId(), (b ? Boolean.TRUE : Boolean.FALSE));
            return;
        } else if (obj instanceof PDate) {
            TimestampField tsf = (TimestampField) comp;
            attr.set(ed.getAttributeId(), tsf.getPDate());
            return;
        } else if (obj instanceof PTimestamp) {
            TimestampField tsf = (TimestampField) comp;
            attr.set(ed.getAttributeId(), tsf.getTimestamp());
            return;
        }
        throw new IllegalArgumentException("Attribute not valid for ComponentFactory: " 
                + (obj != null ? obj.getClass().toString() : "null"));
        */
    }

    /**
     * @changed
     *     OLI 29.05.2008 - Einbau der M&ouml;glichkeit Kommata in Zahlenfeldern automatisch in
     *             Punkte umwandeln zu lassen. Zudem wird nun ein Warnhinweis ausgegeben, wenn
     *             ein Zahlenfeld nicht in eine Zahl umwandelbar ist.
     *     <P>OLI 02.06.2008 - Anpassung der Kommataanpassung, soda&szlig; sie nur noch auf 
     *             Zahlenfelder wirkt ;o).
     *     <P>
     *
     */
    public Object getValue(EditorDescriptor ed, JComponent comp) 
            throws IllegalArgumentException {
        if (!(ed instanceof DefaultEditorDescriptor)) {
            throw new IllegalArgumentException("DefaultEditorDescriptor-object expected!");
        }
        if (ed.isDisabled()) {
            return null;
        }
        Attributed attr = ed.getObject();
        DefaultEditorDescriptor ded = (DefaultEditorDescriptor) ed;
        Object obj = attr.get(ed.getAttributeId());
        if ((this.liste != null) && (comp instanceof JComboBox)) {
            JComboBox jb = (JComboBox) comp;
            if (jb.getSelectedIndex() > 0) {
                return jb.getSelectedItem();
            }
            return null;
        } else if ((obj instanceof Double) || (obj instanceof Float) || (obj instanceof Integer) 
                || (obj instanceof Long) || (obj instanceof Short) || (obj instanceof String)) {
            if (!(comp instanceof JTextField) && !(comp instanceof FilenameSelector) 
                    && !(comp instanceof LineTextEditor)) {
                throw new IllegalArgumentException("Component not in valid type: " 
                        + comp.getClass().toString() + "\n>>> " + ded + "\n>>> " + 
                        comp.getClass().getName());
            }
            String value = "";
            if (comp instanceof FilenameSelector) {
                value = ((FilenameSelector) comp).getText();
            } else if (comp instanceof LineTextEditor) {
                value = ((LineTextEditor) comp).getText();
            } else {   
                value = ((JTextField) comp).getText();
            }
            try {
                if (Boolean.getBoolean(
                        "corent.djinn.DefaultComponentFactory.change.komma.to.point") 
                        && (obj instanceof Number)) {
                    value = value.replace(",", ".");
                }
                if (obj instanceof Double) {
                    return new Double(value);
                } else if (obj instanceof Float) {
                    return new Float(value);
                } else if (obj instanceof Integer) { 
                    return new Integer(value);
                } else if (obj instanceof Long) {
                    return new Long(value);
                } else if (obj instanceof Short) {
                    return new Short(value);
                } else if (obj instanceof String) {
                    return value;
                }
            } catch (DateFormatException dfe) {
                dfe.printStackTrace();
                JOptionPane.showMessageDialog(null, Utl.GetProperty(
                        "corent.djinn.DefaultComponentFactory.error.date.format.text", 
                        "Datum $VALUE konnte nicht umgewandelt werden!").replace("$VALUE", value
                        ), Utl.GetProperty(
                        "corent.djinn.DefaultComponentFactory.error.date.format.title", 
                        "Fehler"), JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                JOptionPane.showMessageDialog(null, Utl.GetProperty(
                        "corent.djinn.DefaultComponentFactory.error.number.format.text", 
                        "Nummernwert $VALUE konnte nicht umgewandelt werden!").replace("$VALUE",
                        value), Utl.GetProperty(
                        "corent.djinn.DefaultComponentFactory.error.number.format.title", 
                        "Fehler"), JOptionPane.ERROR_MESSAGE);
            }
        } else if (obj instanceof Boolean) {
            if (!(comp instanceof JCheckBox)) {
                throw new IllegalArgumentException("Component not in valid type: " 
                        + comp.getClass().toString());
            }
            boolean b = ((JCheckBox) comp).isSelected();
            return (b ? Boolean.TRUE : Boolean.FALSE);
        } else if (obj instanceof PDate) {
            TimestampField tsf = (TimestampField) comp;
            return tsf.getPDate();
        } else if (obj instanceof PTimestamp) {
            TimestampField tsf = (TimestampField) comp;
            return tsf.getTimestamp();
        }
        throw new IllegalArgumentException("Attribute not valid for ComponentFactory: " 
                + (obj != null ? obj.getClass().toString() : "null"));
    }
    
}
