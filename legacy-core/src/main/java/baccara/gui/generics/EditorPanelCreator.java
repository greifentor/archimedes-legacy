/*
 * EditorPanelCreator.java
 *
 * 30.01.2013
 *
 * (c) by HealhCarion
 *
 */

package baccara.gui.generics;


import static corentx.util.Checks.*;

import baccara.gui.*;

import corent.files.*;
import corent.gui.*;

import corentx.dates.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import logging.Logger;


/**
 * This class is able to generate editor panels with labels on the left and input fields on the
 * right side.
 *
 * @param <T> The type of the id of the attribute which are edited.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 30.01.2013 - Added.
 * @changed OLI 16.05.2013 - Extended by the combo boxes.
 */

public class EditorPanelCreator<T> {

    static Logger log = Logger.getLogger(EditorPanelCreator.class);

    private ActionListener actionListener = null;
    private GUIBundle guiBundle = null;
    private Container parent = null; 

    /**
     * Creates a new editor panel creator with the passed parameters.
     *
     * @param guiBundle The GUI specific informations and helpers.
     * @param actionListener The action listener which should be notified by action events send
     *         by the editor panel components.
     * @param parent The container to which the editor panel is to add to.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 30.01.2013 - Added.
     */
    public EditorPanelCreator(GUIBundle guiBundle, ActionListener actionListener,
            Container parent) {
        super();
        ensure(actionListener != null, "the action listener to be notified by action events of "
                + "the panel cannot be null.");
        ensure(parent != null, "parent container cannot be null.");
        this.actionListener = actionListener;
        this.guiBundle = guiBundle;
        this.parent = parent;
    }

    /**
     * Creates an editor panel dependent from the passed parameters.
     *
     * @changed OLI 30.01.2013 - Added.
     */
    public JPanel create(String resourcePrefix, Map<Component, T> componentsByComponent,
            Map<T, Component> componentsByName, ComponentData<T>... components) {
        ensure(componentsByComponent != null, "map to store created component references cannot"
                + " be null.");
        ensure(componentsByName != null, "map to store created component by name cannot be null"
                + ".");
        JPanel p = new JPanel(new BorderLayout(this.getHGap(), this.getVGap()));
        JPanel fields = new JPanel(new GridLayout(components.length, 1, this.getHGap(),
                this.getVGap()));
        JPanel labels = new JPanel(new GridLayout(components.length, 1, this.getHGap(),
                this.getVGap()));
        p.setBorder(new EmptyBorder(this.getVGap(), this.getHGap(), this.getVGap(),
                this.getHGap()));
        fields.setBorder(new EmptyBorder(this.getVGap(), this.getHGap(), this.getVGap(),
                this.getHGap()));
        labels.setBorder(new EmptyBorder(this.getVGap(), this.getHGap(), this.getVGap(),
                this.getHGap()));
        for (ComponentData<T> cd : components) {
            String crid = resourcePrefix + "." + cd.getName();
            if (cd.getResourceId() == null) {
                labels.add(new JLabel(this.getResource(crid + ".label")));
            } else {
                labels.add(new JLabel(this.getResource(cd.getResourceId())));
            }
            Component c = null;
            if (cd.isDisabled()) {
                c = new JTextField();
                ((JTextField) c).setText(String.valueOf(cd.getValue()));
            } else if (cd.getType() == Type.BOOLEAN) {
                c = new JCheckBox();
                boolean value = false;
                if (cd.getValue() != null) {
                    value = ((Boolean) cd.getValue()).booleanValue();
                }
                if (cd.getValue() instanceof Boolean) {
                    ((JCheckBox) c).setSelected(value);
                }
                ((JCheckBox) c).addActionListener(this.actionListener);
            } else if (cd.getType() == Type.DATE) {
                TimestampFieldComponentFactory tfcf = new BaccaraTimestampFieldComponentFactory(
                        true, false, this.getInifile());
                corent.dates.PTimestamp value = corent.dates.PTimestamp.NULL;
                try {
                    if (cd.getValue() != null) {
                        value = new corent.dates.PTimestamp(((PDate) cd.getValue()).toLong()
                                * 1000000);
                    }
                    c = new TimestampField(this.parent, tfcf, value);
                } catch (Exception e) {
                    log.error("error while creating timestamp field: " + e);
                }
            } else if (cd.getType() == Type.DOUBLE) {
                c = new JSpinner(new SpinnerNumberModel(0.0, Integer.MIN_VALUE,
                        Integer.MAX_VALUE, 0.1));
                double value = 0.0;
                if (cd.getValue() != null) {
                    value = ((Double) cd.getValue()).doubleValue();
                }
                if (cd.getValue() instanceof Double) {
                    ((JSpinner) c).setValue(value);
                }
            } else if (cd.getType() == Type.INTEGER) {
                c = new JSpinner();
                int value = 0;
                if (cd.getValue() != null) {
                    value = ((Integer) cd.getValue()).intValue();
                }
                if (cd.getValue() instanceof Integer) {
                    ((JSpinner) c).setValue(value);
                }
            } else if (cd.getType() == Type.OBJECT) {
                c = new JComboBox(cd.getSelectionList());
                if (cd.isNoSelection()) {
                    ((JComboBox) c).insertItemAt(NoSelection.OBJECT, 0);
                }
                ((JComboBox<?>) c).setSelectedItem(cd.getValue());
                if (cd.getRenderer() != null) {
                    ((JComboBox<?>) c).setRenderer(cd.getRenderer());
                }
            } else if (cd.getType() == Type.STRING) {
                c = new JTextField(40);
                if (cd.getValue() != null) {
                    ((JTextField) c).setText(cd.getValue().toString());
                }
            } else if (cd.getType() == Type.TEXT) {
                LineTextEditorComponentFactory ltecf =
                        new BaccaraLineTextEditorComponentFactory(guiBundle, crid);
                String text = (String) cd.getValue();
                if (text == null) {
                    text = "";
                }
                c = new LineTextEditor(text, ltecf, this.guiBundle.getInifile(), crid);
            } else if (cd.getType() == Type.TIMESTAMP) {
                TimestampFieldComponentFactory tfcf = new BaccaraTimestampFieldComponentFactory(
                        true, true, this.getInifile());
                corent.dates.PTimestamp value = corent.dates.PTimestamp.NULL;
                try {
                    if (cd.getValue() != null) {
                        value = new corent.dates.PTimestamp(((PTimestamp) cd.getValue()).toLong(
                                ));
                    }
                    c = new TimestampField(this.parent, tfcf, value);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("error while creating timestamp field: " + e);
                }
            }
            c.setName(cd.getName().toString());
            if (cd.isDisabled()) {
                c.setEnabled(false);
            }
            componentsByComponent.put(c, cd.getName());
            componentsByName.put(cd.getName(), c);
            if (cd.isActionButton()) {
                JButton b = new JButton();
                b.setName(cd.getName() + ".button");
                JPanel p0 = new JPanel(guiBundle.createBorderLayout());
                p0.add(c, BorderLayout.CENTER);
                p0.add(b, BorderLayout.EAST);
                c = p0;
            }
            fields.add(new BorderToRedWrapper(c));
        }
        p.add(labels, BorderLayout.WEST);
        p.add(fields, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout(this.getHGap(), this.getVGap()));
        JPanel p0 = new JPanel(new BorderLayout());
        p0.add(p, BorderLayout.NORTH);
        panel.add(new JScrollPane(p0));
        return panel;
    }

    private int getHGap() {
        return this.guiBundle.getHGap();
    }

    private int getVGap() {
        return this.guiBundle.getVGap();
    }

    private String getResource(String key) {
        return this.guiBundle.getResourceManager().getResource(key);
    }

    private String getResource(ResourceId key) {
        return this.guiBundle.getResourceText(key.getResourceId());
    }

    private Inifile getInifile(){
        return this.guiBundle.getInifile();
    }

}