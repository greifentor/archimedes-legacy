/*
 * DataInputPanel.java
 *
 * 13.10.2017
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.table;

import java.awt.*;

/**
 * Methods of a panel to input data.
 *
 * @param <T> The type of the object whose data are shown in the panel.
 *
 * @author ollie
 *
 * @changed OLI 13.10.2017 - Added.
 */

public interface DataInputPanel<T> {

    /**
     * This method will be called if th panel is closed.
     *
     * @changed OLI 19.10.2017 - Added.
     */
    abstract public void onClosed();

    /**
     * Returns an array with the data input components of the panel.
     *
     * @return An array with the data input components of the panel.
     *
     * @changed OLI 13.10.2017 - Added.
     */
    abstract public Component[] getDataInputComponents();

    /**
     * Transfers the data from the panel to the passed object.
     *
     * @param o The object which the data of the panel are set to.
     *
     * @changed OLI 13.10.2017 - Added.
     */
    abstract public void transferData(T o);

}