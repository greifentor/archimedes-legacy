/*
 * ProgressObserver.java
 *
 * 23.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.progress;


/**
 * Classes which implement this interface are able to observe progress increments.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 23.07.2016 - Added.
 */

public interface ProgressObserver {

    /**
     * Returns the current value of the progress.
     *
     * @return The current value of the progress.
     *
     * @changed OLI 23.07.2016 - Added.
     */
    abstract public int getValue();

    /**
     * Increments the progress by one step.
     *
     * @changed OLI 23.07.2016 - Added.
     */
    abstract public void inc();

}