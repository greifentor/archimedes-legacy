/*
 * ModelCheckerMessageListEventFirer.java
 *
 * 11.07.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.gui.checker;


/**
 * An interface which allows to fire <CODE>Event</CODE> for
 * <CODE>ModelCheckerMessageListFrameListeners</CODE>.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 11.07.2016 - Added.
 */

public interface ModelCheckerMessageListEventFirer {

    /**
     * Fires an action.
     *
     * @param event The event which linked to the action.
     *
     * @changed OLI 11.07.2016 - Added.
     */
    abstract public void fireAction(ModelCheckerMessageListFrameListener.Event event);

}