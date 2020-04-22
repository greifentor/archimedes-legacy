/*
 * GUIValueGetter.java
 *
 * 18.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;


/**
 * An interface with methods for a class which is able to get a value from the GUI for a
 * component data object.
 *
 * @param <ID> The type of the id or name attribute.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.07.2016 - Added.
 */

public interface GUIValueGetter<ID> {

    /**
     * Returns the GUI value for the passed component data object.
     *
     * @param componentData The component data object which the GUI value should be get for.
     * @return The current value from the GUI for the passed component data object.
     *
     * @changed OLI 18.07.2016 - Added.
     */
    abstract public Object getValue(ComponentData<ID> componentData); 

}