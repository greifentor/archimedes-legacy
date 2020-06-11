/*
 * NoSelection.java
 *
 * 16.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;


/**
 * This enumeration provides an object which can be shown and selected for no selection.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 16.05.2013 - Added.
 */

public enum NoSelection {

    OBJECT;

    /**
     * @changed OLI 16.05.2013 - Added.
     */
    @Override public String toString() {
        return "<...>";
    }

}