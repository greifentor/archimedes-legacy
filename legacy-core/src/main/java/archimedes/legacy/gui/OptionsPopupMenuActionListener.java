/*
 * OptionsPopupMenuActionListener.java
 *
 * 02.10.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;


import corentx.util.*;

import java.awt.event.*;

import javax.swing.*;


/**
 * An action listener which 
 *
 * @author ollie
 *
 * @changed OLI 02.10.2015 - Added.
 */

public class OptionsPopupMenuActionListener implements ActionListener {

    private OptionsSelectionMode selectionMode = null;
    private JTextField textField = null;

    /**
     * Creates a new action listener for option popup menus for the passed text field.
     *
     * @param textField The text field which the selection is to make for.
     * @param selectionMode The mode which the selected item is to put to the text field.
     *
     * @changed OLI 02.10.2015 - Added.
     */
    public OptionsPopupMenuActionListener(JTextField textField,
            OptionsSelectionMode selectionMode) {
        super();
        this.selectionMode = selectionMode;
        this.textField = textField;
    }

    /**
     * @changed OLI 02.10.2015 - Added.
     */
    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof OptionsPopupMenuItem) {
            if (this.selectionMode == OptionsSelectionMode.APPEND) {
                this.textField.setText(this.textField.getText() + e.getActionCommand());
            } else if (this.selectionMode == OptionsSelectionMode.INSERT) {
                int pos = this.textField.getCaretPosition();
                this.textField.setText(Str.insert(this.textField.getText(), e.getActionCommand(
                        ), pos));
                this.textField.setCaretPosition(pos + e.getActionCommand().length());
            } else if (this.selectionMode == OptionsSelectionMode.REPLACE) {
                this.textField.setText(e.getActionCommand());
            }
        }
    }

}