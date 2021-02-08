/*
 * EditorPanelConsistenceChecker.java
 *
 * 17.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;


/**
 * An interface which defines the methods for a consistence checker for editor panels. 
 *
 * @param <ID> The type of the id which is used to get access to the objects attributes.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 17.07.2016 - Added.
 */

public interface EditorPanelConsistencyChecker<ID> {

    /**
     * Allows to check the editor panel component contents before transfering the values to the
     * edited object.
     *
     * @param componentData The component data of the editor panel which are to check.
     * @return An editor panel consistence violation if the check has failed. A
     *         <CODE>null</CODE> value otherwise.
     *
     * @changed OLI 17.07.2016 - Added.
     */
    abstract public EditorPanelConsistencyViolation checkBeforeTransfer(
            ComponentData<ID>[] componenData);

}