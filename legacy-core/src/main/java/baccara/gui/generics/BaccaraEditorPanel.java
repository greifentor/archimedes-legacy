/*
 * BaccaraEditorPanel.java
 *
 * 31.05.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import baccara.gui.*;
import corent.gui.*;
import corentx.dates.*;
import corentx.util.*;

/**
 * A basic editor panel for Baccara applications.
 *
 * @param <ID> The type of the id which is used to get access to the objects attributes.
 * @param <T> The type of the edited object.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 31.05.2016 - Added.
 */

abstract public class BaccaraEditorPanel<ID, T> extends JPanel implements EditorPanel<T>,
        GUIValueGetter<ID> {

    private ActionListener actionListener = null;
    private ComponentData<ID>[] componentData = null;
    private Map<Component, ID> componentsByComponent = new Hashtable<Component, ID>();
    private Map<ID, Component> componentsByName = new Hashtable<ID, Component>();
    private GUIBundle guiBundle = null;
    private Container parent = null;
    private String resourcePrefix = null;

    /**
     * Creates a new editor panel for a Baccara application with the passed parameters.
     *
     * @param guiBundle A bundle with GUI information.
     * @param parent A reference to the component which the panel is a member of (usually the
     *         window or frame which contains the panel).
     * @param resourcePrefix A prefix for the resources of the panel.
     * @param actionListener An action listener to get a chance of reaction on combo box events.
     * @param object The object which is edited by the panel.
     * @param componentData The configuration of the fields which should be shown in the panel. 
     *
     * @changed OLI 31.05.2016 - Added.
     */
    public BaccaraEditorPanel(GUIBundle guiBundle, Container parent, String resourcePrefix,
            ActionListener actionListener, T object, ComponentData<ID>... componentData) {
        super(guiBundle.createGridLayout(1, 1));
        this.actionListener = actionListener;
        this.componentData = componentData;
        this.guiBundle = guiBundle;
        this.parent = parent;
        this.resourcePrefix = resourcePrefix;
        this.add(new EditorPanelCreator<ID>(this.guiBundle, this.actionListener, this.parent
                ).create(this.resourcePrefix, this.componentsByComponent, this.componentsByName,
                this.componentData));
    }

    /**
     * Returns a reference of the button with passed name in the panel.
     *
     * @param name The name of the button which is to return.
     * @return The button with the passed name from the panel or <CODE>null</CODE> if there is
     *         no button with the passed name in the panel.
     *
     * @changed OLI 01.08.2016 - Added.
     * @deprecated OLI 16.09.2016 - Use "getComponent(String)" instead of.
     */
    public JButton getButton(String name) {
        return (JButton) COUtil.FindComponent(name, this, null);
    }

    /**
     * Returns a reference of the component with passed name in the panel.
     *
     * @param id The id of the component which is to return.
     * @return The component with the passed name from the panel or <CODE>null</CODE> if there
     *         is no component with the passed name in the panel.
     *
     * @changed OLI 16.09.2016 - Added.
     */
    public Component getComponent(ID id) {
        return this.componentsByName.get(id);
    }

    /**
     * Returns a list of editor panel consistence checkers for the editor panel or
     * <CODE>null</CODE> if there are no checks required.
     *
     * @return A list of editor panel consistence checkers for the editor panel or
     *         <CODE>null</CODE> if there are no checks required.#
     *
     * @changed OLI 17.07.2016 - Added.
     */
    public List<EditorPanelConsistencyChecker<ID>> getEditorPanelConsistenceCheckers() {
        return null;
    }

    /**
     * This method will be called if an attribute has been changed and should be set in the
     * maintained object.
     *
     * @param t The object which this edited in the panel.
     * @param cd The component data record which describes the editor field.
     * @param guiValue The value which has been set by the GUI.
     *
     * @changed OLI 31.05.2016 - Added.
     */
    abstract public void setAttribute(T t, ComponentData<ID> cd, Object guiValue);

    /**
     * @changed OLI 18.07.2016 - Added.
     */
    @Override public Object getValue(ComponentData<ID> cd) {
        Component c = this.componentsByName.get(cd.getName());
        if (c instanceof FilenameSelector) {
            return ((FilenameSelector) c).getText();
        } else if (c instanceof LineTextEditor) {
                return ((LineTextEditor) c).getText();
        } else if (c instanceof JCheckBox) {
            return ((JCheckBox) c).isSelected();
        } else if (c instanceof JComboBox) {
            Object o = ((JComboBox<?>) c).getSelectedItem();
            if (o == NoSelection.OBJECT) {
                o = null;
            }
            return o;
        } else if (c instanceof JSpinner) {
            if (cd.getType() == Type.INTEGER) {
                return (Integer) ((JSpinner) c).getValue();
            }
            return (Double) ((JSpinner) c).getValue();
        } else if (c instanceof JTextField) {
            return ((JTextField) c).getText();
        } else if (c instanceof TimestampField) {
            long l = ((TimestampField) c).getTimestamp().toLong();
            if (cd.getType() == Type.DATE) {
                return new PDate(l / 1000000L);
            }
            return new PTimestamp(l);
        }
        return null;
    }

    /**
     * @changed OLI 31.05.2016 - Added.
     */
    @Override public EditorPanelConsistencyViolation[] transferChanges(T t) {
        List<EditorPanelConsistencyViolation> vl =
                new LinkedList<EditorPanelConsistencyViolation>();
        List<EditorPanelConsistencyChecker<ID>> checkers =
                this.getEditorPanelConsistenceCheckers(); 
        if (checkers != null) {
            for (EditorPanelConsistencyChecker<ID> c : checkers) {
                EditorPanelConsistencyViolation v = c.checkBeforeTransfer(this.componentData);
                if (v != null) {
                    vl.add(v);
                }
            }
        }
        if (vl.size() == 0) {
            this.transferChangesUnchecked(t);
        }
        return vl.toArray(new EditorPanelConsistencyViolation[0]);
    }

    /**
     * @changed OLI 31.05.2016 - Added.
     */
    @Override public void transferChangesUnchecked(T t) {
        for (ComponentData<ID> cd : this.componentData) {
            Object guiValue = this.getValue(cd);
            if (!Utl.equals(guiValue, cd.getValue())) {
                this.setAttribute(t, cd, guiValue);
            }
        }
    }

}