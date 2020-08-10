/*
 * ModelCheckerMessageListFrameModal.java
 *
 * 15.06.2016
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.legacy.gui.checker;

import archimedes.acf.checker.*;
import baccara.gui.*;

/**
 * This class opens a model checker message list frame in a semi modal way.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 15.06.2016 - Added.
 */

public class ModelCheckerMessageListFrameModal implements ModelCheckerMessageListFrameListener,
        Runnable {

    private ModelCheckerMessageListFrame frame = null;
    private Event.Type returnType = null;
    private boolean stopped = false;

    /**
     * Creates a new frame for model checker message view with the passed parameters.
     *
     * @param guiBundle A bundle with GUI information.
     * @param mcms The list of model checker messages to show.
     * @param checkBeforeGenerate Set this flag if the frame is called as check before
     *         generating the code. 
     * @param listeners Which should observe the frame. 
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 15.06.2016 - Added.
     */
    public ModelCheckerMessageListFrameModal(GUIBundle guiBundle, ModelCheckerMessage[] mcms,
            boolean checkBeforeGenerate, ModelCheckerMessageListFrameListener... listeners) {
        super();
        this.frame = new ModelCheckerMessageListFrame(guiBundle, mcms, checkBeforeGenerate,
                listeners);
        this.frame.addBaccaraFrameListener(this);
        Thread t = new Thread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException ie) {
        }
    }

    /**
     * @changed OLI 15.06.2016 - Added.
     */
    @Override public void eventDetected(Event event) {
        this.returnType = event.getType();
        this.stopped = true;
    }

    /**
     * Returns the event type of the event which closed the frame.
     *
     * @return The event type of the event which closed the frame.
     *
     * @changed OLI 15.06.2016 - Added.
     */
    public Event.Type getReturnType() {
        return this.returnType;
    }

    /**
     * @changed OLI 15.06.2016 - Added.
     */
    @Override public void run() {
        while (!this.stopped) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
            }
        }
    }

}