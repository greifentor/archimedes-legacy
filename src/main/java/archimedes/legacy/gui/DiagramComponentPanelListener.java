/*
 * DiagramComponentPanelListener.java
 *
 * 18.05.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.gui;


/**
 * An interface which is to implement by classes whose objects should observe a
 * <CODE>DiagramComponentPanelListener</CODE>.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 18.05.2016 - Added.
 */

public interface DiagramComponentPanelListener {

    /**
     * This method will be called if the <CODE>DiagramComponentPanel</CODE> detects an event
     * which is to notify by the listener.
     *
     * @param event The event which is detected.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    abstract public void eventDetected(DiagramComponentPanelEvent event);

}