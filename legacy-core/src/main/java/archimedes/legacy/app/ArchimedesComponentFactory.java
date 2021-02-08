/*
 * ArchimedesComponentFactory.java
 *
 * 09.07.2005
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


import corent.base.*;
import corent.base.dynamic.*;
import corent.db.xs.*;
import corent.djinn.*;
import corent.files.*;
import corent.gui.*;

import java.awt.Component;
import java.awt.Frame;
import java.util.*;

import javax.swing.*;


/**
 * Diese Klasse enth&auml;lt eine spezielle Implementierung der ComponentFactory, die im 
 * Zusammenspiel mit dynamischen Archimedes-Application-KLassen einsetzbar ist.
 * 
 * @author
 *     ollie
 *
 * @changed
 *     OLI 20.02.2008 - Erweiterung um die Methode <TT>getMLSCF()</TT>.
 *     <P>OLI 07.05.2009 - Eliminierung seit Ende M&auml;rz auskommentierter Passagen aus der
 *             Methode <TT>createComponent(EditorDescriptor, Component, Inifile)</TT>.
 *     <P>
 *
 */

public class ArchimedesComponentFactory extends DefaultComponentFactory {
    
    /** Statische Factory zur applikationsweiten Benutzung. */
    public static final ArchimedesComponentFactory INSTANZ = new ArchimedesComponentFactory();
    
    /* Eine MassiveListSelectorComponentFactory, falls eine solche gebraucht werden soll. */ 
    private ArchimedesMassiveListSelectorComponentFactory mlscf = null;
    
    /* Generiert eine ArchimedesComponentFactory. */
    private ArchimedesComponentFactory() {
        super(null);
    }
    
    /**
     * Generiert eine ArchimedesComponentFactory mit dem &uuml;bergebenen als Grundlage f&uuml;r 
     * eine ComboBox.
     *
     * @param l Eine Liste, falls die Instanz der Factory eine Auswahlliste produzieren soll.
     */
    public ArchimedesComponentFactory(List l) {
        super(l);
        this.liste = l;
    }
    
    /**
     * Generiert eine ArchimedesComponentFactory mit dem &uuml;bergebenen als Grundlage f&uuml;r 
     * einen MassiveListSelector.
     *
     * @param mlscf Die ArchimedesMassiveListSelectorComponentFactory, aus der der 
     *     MassiveListSelector seine Komponenten beziehen soll.
     */
    public ArchimedesComponentFactory(ArchimedesMassiveListSelectorComponentFactory mlscf) {
        super(null);
        this.mlscf = mlscf;
    }
    
    /** 
     * @return <TT>true</TT>, falls die Factory eine MassiveListSelectorComponent beschreibt. 
     */
    public boolean isMassiveSelector() {
        return (this.mlscf != null);
    }
    
    /**
     * Liefert eine Referenz auf die angeschlossene MassiveListSelectorComponentFactory, falls
     * eine solche vorhanden ist, sonst <TT>null</TT>.
     *
     * @return Eine Referenz auf die angeschlossene MassiveListSelectorComponentFactory bzw. 
     *     <TT>null</TT>, wenn es keine solche gibt.
     *
     * @changed
     *     OLI 20.02.2008 - Hinzugef&uuml;gt.
     *
     */
    public MassiveListSelectorComponentFactory getMLSCF() {
        return this.mlscf;
    }
    
    
    /* Implementierung des Interfaces ComponentFactory. */
    
    /**
     * @changed
     *     OLI 21.02.2008 - Die Titelzeilen der Auswahldialoge sind nun konfigurierbar.
     *     <P>OLI 07.05.2009 - Eliminierung seit Ende M&auml;rz auskommentierter Passagen.
     *     <P>
     *
     */
    public JComponent createComponent(EditorDescriptor ed, Component owner, Inifile ini) 
            throws IllegalArgumentException {
        if (!(ed instanceof DefaultEditorDescriptor)) {
            throw new IllegalArgumentException("DefaultEditorDescriptor-object expected!");
        }
        final Attributed attr = ed.getObject();
        DefaultEditorDescriptor ded = (DefaultEditorDescriptor) ed;
        JComponent comp = null;
        Object obj = attr.get(ed.getAttributeId());        
        if (this.liste != null) {
            Vector liste = new Vector(this.liste);
            liste.insertElementAt("<...>", 0);
            JComboBox jb = new JComboBox(liste);
            if (obj != null) {
                for (int i = 0, len = liste.size(); i < len; i++) {
                    Object o0 = liste.elementAt(i);
                    if ((o0 instanceof HasKey) && ((HasKey) o0).getKey().equals(obj)) {
                        jb.setSelectedItem(o0);
                        break;
                    }
                }
            } else {
                jb.setSelectedIndex(0);
            }
            comp = jb;
        } else if (this.mlscf != null) {
            final ArchimedesEditorDescriptor aded = (ArchimedesEditorDescriptor) ded;
            final ArchimedesMassiveListSelectorComponentFactory amlscf = 
                    (ArchimedesMassiveListSelectorComponentFactory) this.mlscf;
            final ArchimedesApplication app = amlscf.getApp();
            final Component parentwindow = owner; 
            AbstractCOMassiveListSelector amls = new AbstractCOMassiveListSelector(this.mlscf, 
                    null, ed.getName()) {
                private Class cls = null;
                public Object selectObject() {
                    String preselection = "";
                    if ((attr instanceof MassiveComboBoxMaster)) {
                        preselection = ((MassiveComboBoxMaster) attr).getPreselection(
                                aded.getName()); 
                    }
                    String titel = "";
                    if (attr instanceof ArchimedesDynamic) {
                        titel = ((ArchimedesDynamic) attr).getTablename();
                    }
                    if (Boolean.getBoolean(
                            "archimedes.app.ArchimedesComponentFactory.mls.allow.create."
                            + this.getCls().getName()) || Boolean.getBoolean(
                            "archimedes.app.ArchimedesComponentFactory.mls.allow.create")) {
                        DialogSelectionEditorDjinnDBF dsd = new DialogSelectionEditorDjinnDBF(
                                (parentwindow instanceof JFrame ? (JFrame) parentwindow : null),
                                getSelectionTitle(this.getCls(), titel),
                                app.getDFC(), this.getCls(), (app.getFrame() instanceof 
                                ComponentWithInifile ? ((ComponentWithInifile) app.getFrame()
                                ).getInifile() : null), 
                                true, true, true, true);
                        if (dsd.isSelected()) {
                            Vector selection = dsd.getSelection();
                            if (selection.size() > 0) {
                                return selection.elementAt(0);
                            }
                        }
                    } else {
                        DialogSelectionDjinn dsd = new DialogSelectionDjinn((parentwindow 
                                instanceof Frame ? (Frame) parentwindow : null), 
                                getSelectionTitle(this.getCls(), titel),
                                new DBFListViewComponentFactory(app.getDFC(), this.getCls(), 
                                (app.getFrame() instanceof ComponentWithInifile
                                ? ((ComponentWithInifile) app.getFrame()).getInifile() : null), 
                                preselection), (app.getFrame() instanceof ComponentWithInifile ? 
                                ((ComponentWithInifile) app.getFrame()).getInifile() : null), 
                                true);
                        if (dsd.isSelected()) {
                            Vector selection = dsd.getSelection();
                            if (selection.size() > 0) {
                                return selection.elementAt(0);
                            }
                        }
                    }
                    return this.getSelected();
                }
                public Class getCls() {
                    if (this.cls == null) {
                        this.cls = this.mlscf.getCls();
                    }
                    return this.cls;
                }
                public void setCls(Class cls) {
                    this.cls = cls;
                }
            };
            /*
            if (attr instanceof MassiveListSelectorListener) {
                amls.addMassiveListSelectorListener((MassiveListSelectorListener) attr);
            }
            */
            if (ed.isDisabled()) {
                amls.setEnabled(false);
            }
            if (((obj instanceof Integer) && (((Integer) obj).intValue() > 0))
                    || ((obj instanceof Long) && (((Long) obj).longValue() > 0))) {
                try {
                    Vector v = amlscf.getApp().getDFC().read(amlscf.getCls(), 
                            amlscf.getColumnname() + "=" + obj.toString(), null, false, true);
                    if (v.size() > 0) {
                        obj = v.elementAt(0);
                    } else {
                        obj = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    obj = null;
                }
            } else if ((obj instanceof Integer) || (obj instanceof Long)) {
                obj = null;
            }
            amls.setSelected(obj);
            return amls;
        } else {
            comp = super.createComponent(ed, owner, ini);
        }
        if (ed.isDisabled()) {
            comp.setEnabled(false);
        }
        return comp;
    }
    
    private String getSelectionTitle(Class cls, String title) {
        String s = Utl.GetProperty("archimedes.app.ArchimedesComponentFactory.mls.dialog."
                + "selection.title." + cls.getName());
        if (s == null) {
            s = Utl.GetProperty("archimedes.app.ArchimedesComponentFactory.mls.dialog."
                    + "selection.title", Utl.GetProperty("archimedes.app."
                    + "ArchimedesComponentFactory.mls.dialog.selection.title.prefix", 
                    "Auswahl&nbsp;") + title);
        }
        return s;
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
        DefaultEditorDescriptor ded = (DefaultEditorDescriptor) ed;
        Object obj = attr.get(ed.getAttributeId());
        if ((ded.getComponentFactory() instanceof ArchimedesComponentFactory) 
                && (((ArchimedesComponentFactory) ded.getComponentFactory()).isMassiveSelector()
                )) {
            obj = ((MassiveListSelector) comp).getSelected();
            if ((obj instanceof DynamicObject) && (ed.getObject() instanceof DynamicObject)) {
                String fn = this.mlscf.getColumnname();
                fn = fn.substring(fn.indexOf(".")+1, fn.length());
                Object key = ((DynamicObject) obj).get(fn);
                ((DynamicObject) ed.getObject()).set(this.mlscf.getReferenceField(), key);
                return;
            } else if (obj instanceof HasKey) {
                attr.set(ed.getAttributeId(), ((HasKey) obj).getKey());
                return;
            }
            attr.set(ed.getAttributeId(), obj);
            return;
        }
        super.transferValue(ed, comp);
    }
    
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
        if ((ded.getComponentFactory() instanceof ArchimedesComponentFactory) 
                && (((ArchimedesComponentFactory) ded.getComponentFactory()).isMassiveSelector()
                )) {
            obj = ((MassiveListSelector) comp).getSelected();
            if (ed.getObject() instanceof DynamicObject) {
                return ((DynamicObject) ed.getObject()).get(this.mlscf.getReferenceField());
            } else if (obj instanceof HasKey) {
                return ((HasKey) obj).getKey();
            }
            return obj;
        }
        return super.getValue(ed, comp);
    }
    
}
