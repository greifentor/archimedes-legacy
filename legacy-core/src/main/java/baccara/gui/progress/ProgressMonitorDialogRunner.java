/*
 * ProgressMonitorDialogRunner.java
 *
 * 25.09.2013
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.progress;

import static corentx.util.Checks.*;

import java.awt.*;


/**
 * A progress monitor dialog runner which creates and starts a progress dialog in an own thread.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 25.09.2013 - Added.
 */

public class ProgressMonitorDialogRunner implements Runnable {

    private Window caller = null;
    private String initialComment = null;
    private ProgressMonitorFrame pmd = null;
    private String title = null;

    /**
     * Creates a progress dialog which shows a progress bar and, as option, a string above the
     * bar.
     *
     * @param caller A window which acts a caller for the progress bar. This window will not be
     *         locked but the progress bar will be kept in front of the window. Pass null here
     *         if no window is known as caller.
     * @param title A title for the progress monitor dialog.
     * @param initialComment A string which is shown above the progress bar. Pass null here if
     *         no string should be shown there.
     *
     * @changed OLI 25.09.2013 - Added.
     */
    public ProgressMonitorDialogRunner(Window caller, String title, String initialComment) {
        super();
        this.caller = caller;
        this.initialComment = initialComment;
        this.title = title;
        Thread t = new Thread(this);
        t.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
        }
    }

    /**
     * Closes the progress monitor dialog and ends the runner.
     * 
     * @changed OLI 25.09.2013 - Added.
     */
    public void close() {
        ensure(this.pmd != null, new IllegalStateException("Progress monitor dialog runner is "
                + "not ready to access!"));
        this.pmd.setVisible(false);
    }

    /**
     * @changed OLI 25.09.2013 - Added.
     */
    @Override public void run() {
        this.pmd = new ProgressMonitorFrame(this.caller, title, this.initialComment);
        while (this.pmd.isVisible()) {
            this.pmd.invalidate();
            this.pmd.validate();
            this.pmd.repaint();
            try {
                Thread.sleep(250);
            } catch (InterruptedException ie) {
            }
        }
        this.pmd.dispose();
        this.pmd = null;
    }

    /**
     * Checks if the progress monitor is ready.
     *
     * @return <CODE>true</CODE> if the progress monitor is ready.
     *
     * @changed OLI 27.11.2013 - Added.
     */
    public boolean isReady() {
        return (this.pmd != null);
    }

    /**
     * Sets the passed value as new value for the progress bar. 
     * 
     * @param n The new value for the progress bar
     *
     * @changed OLI 25.09.2013 - Added.
     */
    public void update(int n) {
        ensure(this.pmd != null, new IllegalStateException("Progress monitor dialog runner is "
                + "not ready to access!"));
        this.pmd.update(n);
    }

    /**
     * Sets the passed comment as new comment for the progress bar. 
     * 
     * @param comment The new comment for the progress bar
     *
     * @changed OLI 25.09.2013 - Added.
     */
    public void update(String comment) {
        ensure(this.pmd != null, new IllegalStateException("Progress monitor dialog runner is "
                + "not ready to access!"));
        this.pmd.update(comment);
    }

}