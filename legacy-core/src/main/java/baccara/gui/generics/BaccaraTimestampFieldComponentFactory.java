/*
 * BaccaraTimestampFieldComponentFactory.java
 *
 * 29.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;

import javax.swing.*;

import corent.files.*;
import corent.gui.*;

/**
 * A <CODE>TimestampFieldComponentFactory</CODE> implementation for Baccara.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 29.07.2016 - Added.
 */

public class BaccaraTimestampFieldComponentFactory extends DefaultTimestampFieldComponentFactory
        {

    /** 
     * Creates a new Baccara specific timestamp field component factoy.
     *
     * @param dateEnabled Set this flag to <CODE>false<CODE> to hide the date display.
     * @param timeEnabled Set this flag to <CODE>false<CODE> to hide the time display.
     * @param ini A ini file to pass to dialogs called from the timestamp field component.
     *
     * @changed OLI 29.07.2016 - Added.
     */     
    public BaccaraTimestampFieldComponentFactory(boolean dateEnabled, boolean timeEnabled, 
            Inifile ini) {
        super(dateEnabled, timeEnabled, ini);
    }

    /**
     * @changed OLI 29.07.2016 - Added.
     */
    @Override public JTextField createTextField() {
        return new JTextField("", 6);
    }

}