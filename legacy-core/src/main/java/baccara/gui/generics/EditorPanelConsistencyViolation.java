/*
 * EditorPanelConsistenceViolation.java
 *
 * 17.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;


/**
 * A definition for consistence violations for editor panels.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 17.07.2016 - Added.
 */

public interface EditorPanelConsistencyViolation {

    /**
     * Returns the message id of the consistency violation.
     *
     * @return The message id of the consistency violation. This could be an id or a resource
     *         id for the message.
     *
     * @changed OLI 17.07.2016 - Added.
     */
    abstract public String getMessageId();

    /**
     * Returns the objects which are involved in the violation.
     *
     * @return The objects which are involved in the violation. Could be an empty array if there
     *         are no involved objects.
     *
     * @changed OLI 17.07.2016 - Added.
     */
    abstract public Object[] getMessageObjects();

}