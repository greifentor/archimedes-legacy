/*
 * EditorFrameConfiguration.java
 *
 * 05.08.2013
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;


/**
 * Some data to configure the editor frame.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 05.08.2013 - Added.
 */

public class EditorFrameConfiguration {

    private boolean showCancelButton = true;
    private boolean showDeleteButton = true;
    private boolean showOkButton = true;

    /**
     * Creates a new editor frame configuration with the passed parameters.
     *
     * @param showCancelButton Set this flag if the cancel button is to show.
     * @param showDeleteButton Set this flag if the delete button is to show.
     * @param showOkButton Set this flag if the ok button is to show.
     * @changed OLI 05.08.2013 - Added.
     */
    public EditorFrameConfiguration(boolean showCancelButton, boolean showDeleteButton,
            boolean showOkButton) {
        super();
        this.showCancelButton = showCancelButton;
        this.showDeleteButton = showDeleteButton;
        this.showOkButton = showOkButton;
    }

    /**
     * Checks if the cancel button is to show.
     *
     * @return <CODE>true</CODE> if the cancel button is to show.
     *
     * @changed OLI 05.08.2013 - Added.
     */
    public boolean isShowCancelButton() {
        return this.showCancelButton;
    }

    /**
     * Checks if the delete button is to show.
     *
     * @return <CODE>true</CODE> if the delete button is to show.
     *
     * @changed OLI 05.08.2013 - Added.
     */
    public boolean isShowDeleteButton() {
        return this.showDeleteButton;
    }

    /**
     * Checks if the ok button is to show.
     *
     * @return <CODE>true</CODE> if the ok button is to show.
     *
     * @changed OLI 05.08.2013 - Added.
     */
    public boolean isShowOkButton() {
        return this.showOkButton;
    }

}