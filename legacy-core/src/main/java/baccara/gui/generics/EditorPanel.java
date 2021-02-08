/*
 * EditorPanel.java
 *
 * 07.07.2016
 *
 * (c) by HealthCarion
 *
 */

package baccara.gui.generics;


/**
 * An interface which describes the methods for an editor panel.
 *
 * @param <T> The type of the object which is edited by the panel.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 07.07.2016 - Added.
 */

public interface EditorPanel<T> {

    /**
     * Transfers the values of the panel into the object, if they had been changed.
     *
     * @param t The object which the values should be transfered to.
     * @return An array of consistence violations or <CODE>null</CODE> if there are no
     *         violations.
     *
     * @changed OLI 31.05.2016 - Added.
     */
    abstract public EditorPanelConsistencyViolation[] transferChanges(T t);

    /**
     * Transfers the values of the panel into the object, if they had been changed without any
     * consistency violation check.
     *
     * @param t The object which the values should be transfered to.
     *
     * @changed OLI 31.05.2016 - Added.
     */
    abstract public void transferChangesUnchecked(T t);

}