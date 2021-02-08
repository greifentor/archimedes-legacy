/*
 * OptionsPopupMenu.java
 *
 * 02.10.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import archimedes.model.OptionType;
import archimedes.model.PredeterminedOptionProvider;

/**
 * A popup menu for options.
 * 
 * @author ollie
 * 
 * @changed OLI 02.10.2015 - Added.
 */

public class OptionsPopupMenu extends JPopupMenu {

	/**
	 * Creates and opens an option popup menu from the data of the passed
	 * predetermined option provider.
	 * 
	 * @param predeterminedOptionProvider
	 *            The predetermined option provider which delivers the option
	 *            which are to select in the popup menu.
	 * @param optionType
	 *            The type of options which the menu is created for.
	 * @param x
	 *            The x position of the popup menu.
	 * @param y
	 *            The y position of the popup menu.
	 * @param actionListener
	 *            The action listener which should be notified by a menu item
	 *            selection.
	 * @param parent
	 *            The component which acts as for the menu.
	 * 
	 * @changed OLI 02.10.2015 - Added.
	 */
	public OptionsPopupMenu(PredeterminedOptionProvider predeterminedOptionProvider, OptionType optionType, int x,
			int y, ActionListener actionListener, Component parent) {
		for (String s : predeterminedOptionProvider.getSelectableOptions(optionType)) {
			OptionsPopupMenuItem mi = new OptionsPopupMenuItem(s, actionListener);
			this.add(mi);
		}
		this.show(parent, x, y);
	}

	/**
	 * Creates and opens an option popup menu from the data of the passed
	 * predetermined option provider.
	 * 
	 * @param predeterminedOptionProvider
	 *            The predetermined option provider which delivers the option
	 *            which are to select in the popup menu.
	 * @param optionType
	 *            The type of options which the menu is created for.
	 * @param x
	 *            The x position of the popup menu.
	 * @param y
	 *            The y position of the popup menu.
	 * @param textField
	 *            A text field which the popup menu is linked to.
	 * @param selectionMode
	 *            The mode which the selected string is to add to the text
	 *            field.
	 * 
	 * @changed OLI 02.10.2015 - Added.
	 */
	public OptionsPopupMenu(PredeterminedOptionProvider predeterminedOptionProvider, OptionType optionType, int x,
			int y, JTextField textField, OptionsSelectionMode selectionMode) {
		this(predeterminedOptionProvider, optionType, x, y,
				new OptionsPopupMenuActionListener(textField, selectionMode), textField);
	}

}