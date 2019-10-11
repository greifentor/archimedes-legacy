/*
 * OptionsPopupMenuItem.java
 *
 * 02.10.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;


import java.awt.event.*;

import javax.swing.*;


/**
 * A specialized menu item for option popup menus.
 *
 * @author ollie
 *
 * @changed OLI 02.10.2015 - Added.
 */

public class OptionsPopupMenuItem extends JMenuItem {

    /**
     * Creates a new popup menu item for the passed option name.
     *
     * @param optionName The name of the option which is represented by the menu item.
     * @param actionListener An action listener which notices if the menu item is selected.
     *
     * @changed OLI 02.10.2015 - Added.
     */
    public OptionsPopupMenuItem(String optionName, ActionListener actionListener) {
        super(optionName);
        this.addActionListener(actionListener);
    }

}