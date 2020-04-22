/*
 * AbstractEditorFrame.java
 *
 * 30.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;


import baccara.events.*;
import baccara.gui.*;

import corent.gui.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;


/**
 * A basic frame for editors.
 *
 * <P>Using the <CODE>EditorFrameConfiguration</CODE> object the frame can be configured for
 * special purpose. So it is possible to hide one or more of the editors buttons.
 *
 * @param <T> The type of the edited object.
 * @param <WT> The type of the parent window.
 * @param <ET> The type of the editor frame events which are fired by the subclass
 *         implementation.
 * @param <ID> The type of the id's to access the edited attributes.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 30.05.2013 - Added.
 */

abstract public class AbstractEditorFrame<T, WT extends Window, ET extends EditorFrameEvent<T,
        WT>, ID> extends JFrameWithInifile implements ActionListener
        {

    protected T object = null;

    private JButton buttonCancel = null;
    private JButton buttonDelete = null;
    private JButton buttonOk = null;
    private Map<Component, ID> componentsByComponent = new Hashtable<Component, ID>();
    private Map<ID, Component> componentsByName = new Hashtable<ID, Component>();
    private EditorFrameConfiguration editorFrameConfiguration = null;
    private JPanel editorPanel = null;
    private EventManager<ET, EditorFrameListener> eventManager = new EventManager<ET,
            EditorFrameListener>();
    private GUIBundle guiBundle = null;

    /**
     * Creates a new frame for the passed object.
     *
     * @param object The object which is to edit in the frame.
     * @param title The title for the editor frame.
     * @param guiBundle A container with the required GUI parameters.
     *
     * @changed OLI 30.05.2013 - Added.
     */
    public AbstractEditorFrame(T object, String title, GUIBundle guiBundle) {
        this(object, title, guiBundle, new EditorFrameConfiguration(true, true, true));
    }

    /**
     * Creates a new frame for the passed object.
     *
     * @param object The object which is to edit in the frame.
     * @param title The title for the editor frame.
     * @param guiBundle A container with the required GUI parameters.
     *
     * @changed OLI 30.05.2013 - Added.
     */
    public AbstractEditorFrame(T object, String title, GUIBundle guiBundle,
            EditorFrameConfiguration editorFrameConfiguration) {
        super(title, guiBundle.getInifile());
        this.editorFrameConfiguration = editorFrameConfiguration;
        this.guiBundle = guiBundle;
        this.object = object;
        JPanel p = new JPanel(new BorderLayout(this.getHGap(), this.getVGap()));
        p.setBorder(new EmptyBorder(this.getVGap(), this.getHGap(), this.getVGap(),
                this.getHGap()));
        this.editorPanel = this.createMainPanel();
        p.add(this.editorPanel, BorderLayout.CENTER);
        p.add(this.createButtonPanel(), BorderLayout.SOUTH);
        this.setContentPane(p);
        this.pack();
    }

    private int getHGap() {
        return this.guiBundle.getHGap();
    }

    private int getVGap() {
        return this.guiBundle.getVGap();
    }

    private JPanel createMainPanel() {
        return this.createEditorPanel(this.getMainResourceIdentifierPrefix(),
                this.getComponentData(this.object));
    }

    private JPanel createEditorPanel(String resourcePrefix, ComponentData<ID>... components) {
        JPanel panel = new EditorPanelCreator<ID>(this.guiBundle, this, this).create(
                resourcePrefix, this.componentsByComponent, this.componentsByName, components);
        this.setKeyListeners(components);
        return panel;
    }

    private void setKeyListeners(ComponentData<ID>[] components) {
        for (int i = 0; i < components.length; i++) {
            Component c = this.getEditorComponent(components[i].getName());
            if (i > 0) {
                Component c0 = this.getEditorComponent(components[i-1].getName());
                c.addKeyListener(new FocusTransferByKeyPress(KeyEvent.VK_UP, c0));
            }
            if (i < components.length-1) {
                Component c0 = this.getEditorComponent(components[i+1].getName());
                c.addKeyListener(new FocusTransferByKeyPress(KeyEvent.VK_ENTER, c0));
                c.addKeyListener(new FocusTransferByKeyPress(KeyEvent.VK_DOWN, c0));
            }
        }
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, this.getHGap(), this.getVGap()));
        p.setBorder(new EmptyBorder(0, this.getHGap(), this.getVGap(), this.getHGap()));
        this.buttonCancel = this.createButton("cancel"); 
        this.buttonDelete = this.createButton("delete"); 
        this.buttonOk = this.createButton("ok");
        if (this.editorFrameConfiguration.isShowDeleteButton()) {
            p.add(this.buttonDelete);
            p.add(new JLabel("   "));
        }
        if (this.editorFrameConfiguration.isShowOkButton()) {
            p.add(this.buttonOk);
        }
        if (this.editorFrameConfiguration.isShowCancelButton()) {
            p.add(new JLabel("   "));
            p.add(this.buttonCancel);
        }
        return p;
    }

    private JButton createButton(String resourceName) {
        JButton b = new JButton(this.getResource(this.getMainResourceIdentifierPrefix()
                + ".button." + resourceName + ".label"));
        ImageIcon icon = this.guiBundle.getImageProvider().getImageIcon("button_" + resourceName
                );
        if (icon != null) {
            b.setIcon(icon);
        }
        b.addActionListener(this);
        return b;
    }

    private String getResource(String key) {
        return this.guiBundle.getResourceManager().getResource(key);
    }

    /**
     * @changed OLI 15.05.2013 - Added.
     */
    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.buttonCancel) {
            this.closeFrame();
            this.eventManager.fire(this.createEvent(EditorFrameEventType.CANCELED));
        } else if (e.getSource() == this.buttonDelete) {
            this.closeFrame();
            this.eventManager.fire(this.createEvent(EditorFrameEventType.DELETE));
        } else if (e.getSource() == this.buttonOk) {
            this.transferChangesToObject();
            this.closeFrame();
            this.eventManager.fire(this.createEvent(EditorFrameEventType.OK));
        }
    }

    /**
     * Creates a new editor event for the passed event type.
     *
     * @param type The editor event type which the event is created for.
     * @return A new editor event for the passed type.
     *
     * @changed OLI 30.05.2013 - Added.
     */
    abstract public ET createEvent(EditorFrameEventType type);

    private void closeFrame() {
        this.setVisible(false);
        this.dispose();
    }

    /**
     * Returns the text from the component with the passed name.
     *
     * @param name The name of the component whose text should be read.
     * @return The text from the component with the passed name.
     *
     * @changed OLI 30.05.2013 - Added.
     */
    protected String getTextFromComponent(String name) {
        return ((JTextField) this.componentsByName.get(name)).getText();
    }

    /**
     * Adds a listener to the frame which will be notified by frame events.
     *
     * @param l The listener to add.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 15.05.2013 - Added.
     */
    public void addEditorFrameListener(EditorFrameListener l) throws IllegalArgumentException {
        this.eventManager.add(l);
    }

    /**
     * Returns an array with the component data for building up the editor frame.
     *
     * @param object The object which the components are build up for.
     * @return An array with the component data for building up the editor frame.
     *
     * @changed OLI 30.05.2013 - Added.
     */
    abstract protected ComponentData[] getComponentData(T object);

    /**
     * Returns the editor component which is connected to the passed name.
     *
     * @param name The name which the component is linked to.
     * @return The editor component which is connected to the passed name.
     *
     * @changed OLI 30.05.2013 - Added.
     */
    protected Component getEditorComponent(ID name) {
        return this.componentsByName.get(name);
    }

    /**
     * Returns the main resource identifier prefix for the frame.
     *
     * @return The main resource identifier prefix for the frame.
     *
     * @changed OLI 30.05.2013 - Added.
     */
    abstract protected String getMainResourceIdentifierPrefix();

    /**
     * Removes a listener from the frame and will no longer be notified.
     *
     * @param l The listener to remove.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 15.05.2013 - Added.
     */
    public void removeEditorFrameListener(EditorFrameListener l) throws IllegalArgumentException
            {
        this.eventManager.remove(l);
    }

    /**
     * @changed OLI 15.05.2013 - Added.
     */
    @Override public void setVisible(boolean b) {
        super.setVisible(b);
    }

    /**
     * Transfers the contents of the editor frame to the edited object.
     *
     * @changed OLI 30.05.2013 - Added.
     */
    abstract protected void transferChangesToObject();

}