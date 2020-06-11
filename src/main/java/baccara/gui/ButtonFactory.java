/*
 * ButtonFactory.java
 *
 * 04.09.2015
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui;

import static corentx.util.Checks.*;

import java.awt.event.*;

import javax.swing.*;


/**
 * A factory for buttons.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.09.2015 - Added.
 */

public class ButtonFactory {

    private GUIBundle guiBundle = null;

    /**
     * Creates a new button factory with the passed parameters.
     *
     * @param guiBundle A bundle with GUI informations (like resources and images).
     *
     * @changed OLI 04.09.2015 - Added.
     */
    public ButtonFactory(GUIBundle guiBundle) {
        super();
        ensure(guiBundle != null, "GUI bundle cannot be null.");
        this.guiBundle = guiBundle;
    }

    /**
     * Creates a new button for the passed id and image name.
     *
     * @param id An id for the button in relation to the resources.
     * @param imageId An id which is linked to an image from the image provider.
     * @return The button.
     *
     * @changed OLI 04.09.2015 - Added.
     */
    public JButton createButton(String id, String imageId) {
        return this.createButton(id, imageId, null);
    }

    /**
     * Creates a new button for the passed id and image name.
     *
     * @param id An id for the button in relation to the resources.
     * @param imageId An id which is linked to an image from the image provider.
     * @param actionListener The action listener which should process the button events.
     * @return The button.
     *
     * @changed OLI 04.09.2015 - Added.
     */
    public JButton createButton(String id, String imageId, ActionListener actionListener) {
        JButton b = new JButton(this.guiBundle.getResourceText(id));
        if ((imageId != null)
                && (this.guiBundle.getImageProvider().getImageIcon(imageId) != null)) {
            b.setIcon(this.guiBundle.getImageProvider().getImageIcon(imageId));
        }
        if (actionListener != null) {
            b.addActionListener(actionListener);
        }
        return b;
    }

}