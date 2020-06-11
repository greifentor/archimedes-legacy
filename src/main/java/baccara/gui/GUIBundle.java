/*
 * GUIBundle.java
 *
 * 17.01.2013
 *
 * (c) by HealhCarion
 *
 */

package baccara.gui;


import static corentx.util.Checks.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import corent.files.*;


/**
 * A container class which creates a bundle of all objects which are required to create a GUI
 * component.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.01.2013 - Added.
 */

public class GUIBundle {

    // private static final Logger LOG = Logger.getLogger(GUIBundle.class);

    private int hgap = 0;
    private ImageProvider imageProvider = null;
    private Inifile ini = null;
    private ResourceManager resourceManager = null;
    private int vgap = 0;

    /**
     * Creates a new GUI bundle with the passed parameters.
     *
     * @param ini The ini file which is used to store and read e. g. the components shape
     *         parameters.
     * @param resourceManager The resource manager which provides the localized resources for
     *         the component.
     * @param imageProvider The image provider which returns images for the GUI.
     * @param hgap The horizontal gap between the components.
     * @param vgap The vertical gap between the components.
     * @throws IllegalArgumentException In case of precondition violation.
     * @precondition ini != null
     * @precondition resourceManager != null
     * @precondition hgap &gt;= 0 
     * @precondition vgap &gt;= 0 
     *
     * @changed OLI 17.01.2013 - Added.
     */
    public GUIBundle(Inifile ini, ResourceManager resourceManager, ImageProvider imageProvider,
            int hgap, int vgap) throws IllegalArgumentException {
        super();
        ensure(hgap >= 0, "horizontal gap cannot be lesser zero.");
        ensure(imageProvider != null, "image manager cannot be null");
        ensure(ini != null, "ini file cannot be null.");
        ensure(resourceManager != null, "resource manager cannot be null");
        ensure(vgap >= 0, "vertical gap cannot be lesser zero.");
        this.hgap = hgap;
        this.imageProvider = imageProvider;
        this.ini = ini;
        this.resourceManager = resourceManager;
        this.vgap = vgap;
    }

    /**
     * Creates a new BorderLayout with the gaps defined by the GUI bundle.
     *
     * @return A new BorderLayout with the gaps defined by the GUI bundle.
     *
     * @changed OLI 23.05.2013 - Added.
     */
    public BorderLayout createBorderLayout() {
        return new BorderLayout(this.getHGap(), this.getVGap());
    }

    /**
     * Creates a new button with the passed resource and image id.
     *
     * @param resourceId A resource id for the button text.
     * @param imageId An id for the image which is to show on the button.
     * @param actionListener An action listener which the button is to add to (or
     *         <CODE>null</CODE>, if the button is not to add to an action listener). 
     * @param panel A panel which the button is to add to (or <CODE>null</CODE>, if the button
     *         should not be added to a panel).
     * @return The new button.
     * @throws IllegalArgumentException Passing a null pointer as resource id.
     */
    public JButton createButton(String resourceId, String imageId,
            ActionListener actionListener, JPanel panel) {
        ensure(resourceId != null, "resource id cannot be null.");
        resourceId = this.getResourceIdentifier(resourceId + ".text", "General.button."
                + imageId + ".text").replace(".text", "");
        JButton b = new JButton(this.getResourceText(resourceId + ".text"));
        if (imageId != null) {
            b.setIcon(this.getImageProvider().getImageIcon(imageId));
        }
        if (actionListener != null) {
            b.addActionListener(actionListener);
        }
        if (panel != null) {
            panel.add(b);
        }
        return b;
    }

    /**
     * Creates a new button with the passed resource and image id.
     *
     * @param resourceId A resource id for the button text (suffices for text, image and tooltip
     *         will be added automatically).
     * @param actionListener An action listener which the button is to add to (or
     *         <CODE>null</CODE>, if the button is not to add to an action listener). 
     * @param panel A panel which the button is to add to (or <CODE>null</CODE>, if the button
     *         should not be added to a panel).
     * @return The new button.
     * @throws IllegalArgumentException Passing a null pointer as resource id.
     */
    public JButton createButton(String resourceId, ActionListener actionListener, JPanel panel)
            {
        ensure(resourceId != null, "resource id cannot be null.");
        JButton b = new JButton(this.getResourceText(resourceId + ".text"));
        String imageId = this.getResourceText(resourceId + ".image");
        if (!imageId.equals(resourceId + ".image")) {
            b.setIcon(this.getImageProvider().getImageIcon(imageId));
        }
        if (actionListener != null) {
            b.addActionListener(actionListener);
        }
        if (panel != null) {
            panel.add(b);
        }
        String tt = this.getResourceText(resourceId + ".tooltip");
        if (!tt.equals(resourceId + ".tooltip")) {
            b.setToolTipText(tt);
        }
        return b;
    }

    /**
     * Creates a new EmtpyBorder with the gaps defined by the GUI bundle.
     *
     * @return A new EmptyBorder with the gaps defined by the GUI bundle.
     *
     * @changed OLI 13.10.2017 - Added.
     */
    public EmptyBorder createEmptyBorder() {
        return new EmptyBorder(this.getVGap(), this.getHGap(), this.getVGap(), this.getHGap());
    }

    /**
     * Creates a new FlowLayout with the gaps defined by the GUI bundle.
     *
     * @param align The align of the components of the layout.
     * @return A new FlowLayout with the gaps defined by the GUI bundle.
     *
     * @changed OLI 23.05.2013 - Added.
     */
    public FlowLayout createFlowLayout(int align) {
        return new FlowLayout(align, this.getHGap(), this.getVGap());
    }

    /**
     * Creates a new GridLayout with the gaps defined by the GUI bundle.
     *
     * @param rows The number of rows for the grid.
     * @param cols The number of columns for the grid.
     * @return A new GridLayout with the gaps defined by the GUI bundle.
     *
     * @changed OLI 11.09.2015 - Added.
     */
    public GridLayout createGridLayout(int rows, int cols) {
        return new GridLayout(rows, cols, this.getHGap(), this.getVGap());
    }

    /**
     * Returns a label with the content of the text resource assigned by the passed id.
     *
     * @param id The id which assigns the text resource whose content should be shown in the
     *         label.
     * @return A <CODE>JLabel</CODE> with the text of the assigned text resource.
     *
     * @changed OLI 11.09.2015 - Added.
     */
    public JLabel createLabel(String id) {
        return new JLabel(this.getResourceText(id));
    }

    /**
     * Returns a label with the content of the text resource assigned by the passed id.
     *
     * @param resourceId The id which assigns the text resource whose content should be shown in
     *         the label (only base id; ".text", ".image" and ".tooltip" will be added
     *         automatically.
     * @return A <CODE>JLabel</CODE> with the text of the assigned text resource.
     *
     * @changed OLI 20.05.2016 - Added.
     */
    public JLabel createLabel(String resourceId, boolean idForEmpty) {
        JLabel label = new JLabel();
        String s = this.getResourceText(resourceId + ".text");
        if (!idForEmpty && s.equals(resourceId + ".text")) {
            s = "";
        }
        label.setText(s);
        String imageId = this.getResourceText(resourceId + ".image");
        if (!imageId.equals(resourceId + ".image")) {
            label.setIcon(this.getImageProvider().getImageIcon(imageId));
        }
        String tt = this.getResourceText(resourceId + ".tooltip");
        if (!tt.equals(resourceId + ".tooltip")) {
            label.setToolTipText(tt);
        }
        return label;
    }

    /**
     * Returns a menu object formed by the passed parameters.
     *
     * @param menuBar A menu bar which the menu is to add to.
     * @param resourceId The id for the resource (base only; ".text", ".image" and ".mnemonic"
     *         will be added automatically.
     * @throws IllegalArgumentException Passing a null pointer as resource id for the menu text.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    public JMenu createMenu(JMenuBar menuBar, String resourceId) {
        JMenu menu = new JMenu(this.getResourceText(resourceId + ".text"));
        String imageId = this.getResourceText(resourceId + ".image");
        if (!imageId.equals(resourceId + ".image")) {
            menu.setIcon(this.getImageProvider().getImageIcon(imageId));
        }
        String m = this.getResourceText(resourceId + ".mnemonic");
        if (!m.equals(resourceId + ".mnemonic")) {
            menu.setMnemonic((m.length() > 0 ? m.charAt(0) : '\0'));
        }
        if (menuBar != null) {
            menuBar.add(menu);
        }
        return menu;
    }

    /**
     * Returns a menu item object formed by the passed parameters.
     *
     * @param menu A menu which the menu item is to add to.
     * @param resourceId The id for the resource (base only; ".text", ".image" and ".mnemonic"
     *         will be added automatically.
     * @param actionListener An action listener which is to link to the new menu item.
     * @throws IllegalArgumentException Passing a null pointer as resource id for the menu item
     *         text.
     *
     * @changed OLI 20.05.2016 - Added.
     */
    public JMenuItem createMenuItem(JMenu menu, String resourceId, ActionListener actionListener
            ) {
        JMenuItem menuItem = new JMenuItem(this.getResourceText(resourceId + ".text"));
        String imageId = this.getResourceText(resourceId + ".image");
        if (!imageId.equals(resourceId + ".image")) {
            menuItem.setIcon(this.getImageProvider().getImageIcon(imageId));
        }
        String m = this.getResourceText(resourceId + ".mnemonic");
        if (!m.equals(resourceId + ".mnemonic")) {
            menuItem.setMnemonic((m.length() > 0 ? m.charAt(0) : '\0'));
        }
        if (menu != null) {
            menu.add(menuItem);
        }
        if (actionListener != null) {
            menuItem.addActionListener(actionListener);
        }
        return menuItem;
    }

    /**
     * Returns the horizontal gap for the component.
     *
     * @return The horizontal gap for the component.
     *
     * @changed OLI 17.01.2013 - Added.
     */
    public int getHGap() {
        return this.hgap;
    }

    /**
     * Returns the image provider which provides images to show in the GUI.
     *
     * @return The image provider which provides images to show in the GUI.
     *
     * @changed OLI 24.01.2013 - Added.
     */
    public ImageProvider getImageProvider() {
        return this.imageProvider;
    }

    /**
     * Returns the ini file.
     *
     * @return The ini file.
     *
     * @changed OLI 17.01.2013 - Added.
     */
    public Inifile getInifile() {
        return this.ini;
    }

    /**
     * Returns the passed resource id if there is a resource stored for or also no resource is
     * found for the default id.
     *
     * @param resourceIdent The resource id which is to check.
     * @param defaultIdent A default id for the case nothing is stored for the passed id.
     * @return The passed resource id if there is a resource stored for or also no resource is
     *         found for the default id.
     *
     * @changed OLI 21.07.2016 - Added.
     */
    public String getResourceIdentifier(String resourceIdent, String defaultIdent) {
        String s = this.getResourceText(resourceIdent);
        if (s.equals(resourceIdent)) {
            if (!defaultIdent.equals(this.getResourceText(defaultIdent))) {
                return defaultIdent;
            }
        }
        return resourceIdent;
    }

    /**
     * Returns the resource manager which provides the localized resource for the component.
     *
     * @return The resource manager which provides the localized resource for the component.
     *
     * @changed OLI 17.01.2013 - Added.
     */
    public ResourceManager getResourceManager() {
        return this.resourceManager;
    }

    /**
     * Returns the text of the resource with the passed id from the resource manager if there
     * is a resource defined with the passed id (the id otherwise).
     *
     * @param id The id of the resource whose text is to return.
     * @return text of the resource with the passed id from the resource manager if there
     *         is a resource defined with the passed id (the id otherwise).
     *
     * @changed OLI 23.05.2013 - Added.
     */
    public String getResourceText(String id) {
        String r = this.resourceManager.getResource(id);
        return (r == null ? id : r);
    }

    /**
     * Returns the text of the resource with the passed id from the resource manager if there
     * is a resource defined with the passed id (the id otherwise).
     *
     * @param id The id of the resource whose text is to return.
     * @param replaces Some strings which will replace wild cards like "{0}" or "{1}" in the
     *         resource string.
     * @return text of the resource with the passed id from the resource manager if there
     *         is a resource defined with the passed id (the id otherwise).
     *
     * @changed OLI 31.03.2016 - Added.
     */
    public String getResourceText(String id, Object... replaces) {
        String s = this.resourceManager.getResource(id);
        if (s == null) {
            return id;
        }
        if (replaces != null) {
            for (int i = 0; i < replaces.length; i++) {
                String r = (replaces[i] instanceof ResourceId ? ((ResourceId) replaces[i]
                        ).getResourceText(this) : String.valueOf(replaces[i]));
                s = s.replace("{" + i + "}", r);
            }
        }
        return s;
    }

    /**
     * Returns the vertical gap for the component.
     *
     * @return The vertical gap for the component.
     *
     * @changed OLI 17.01.2013 - Added.
     */
    public int getVGap() {
        return this.vgap;
    }

}