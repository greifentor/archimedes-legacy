/*
 * OptionsSelectionMode.java
 *
 * 02.10.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;


/**
 * Identifiers for option selection modes.
 *
 * @author ollie
 *
 * @changed OLI 02.10.2015 - Added.
 */

public enum OptionsSelectionMode {

    /** The selected item will be appended. */
    APPEND,
    /** The selected item will be inserted at current cursor position. */
    INSERT,
    /** The selected item will replace the text. */
    REPLACE;

}