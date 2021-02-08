/*
 * BaccaraFrame.java
 *
 * 19.05.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.components;

import static corentx.util.Checks.*;

import baccara.gui.*;
import corent.gui.*;


/**
 * A <CODE>JFrame</CODE> extension as base class for frames in Baccara application.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.05.2016 - Added.
 */

public class BaccaraFrame extends JFrameWithInifile {

    private GUIBundle guiBundle = null;

    /**
     * Creates a new Baccara frame with the passed parameters.
     *
     * @param guiBundle A bundle with GUI information.
     * @throws NullPointerException Passing a null pointer.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    public BaccaraFrame(GUIBundle guiBundle) {
        super(guiBundle.getInifile());
        this.guiBundle = guiBundle;
    }

    /**
     * Creates a new Baccara frame with the passed parameters.
     *
     * @param guiBundle A bundle with GUI information.
     * @param titleRedId The resource id for the frame title.
     * @param replaces Replaces for the title.
     * @throws NullPointerException Passing a null pointer.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    public BaccaraFrame(GUIBundle guiBundle, String titleResId, Object... replaces) {
        this(guiBundle);
        ensure(titleResId != null, new NullPointerException("title resource id cannot be null")
                );
        String s = guiBundle.getResourceText(titleResId, replaces);
        if (s != null) {
            this.setTitle(s);
        }
    }

    /**
     * Returns the GUI bundle of the frame.
     *
     * @return The GUI bundle of the frame.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    public GUIBundle getGUIBundle() {
        return this.guiBundle;
    }

}