/*
 * ComponentData.java
 *
 * 18.01.2013
 *
 * (c) by HealhCarion
 *
 */

package baccara.gui.generics;


import static corentx.util.Checks.*;

import java.util.*;

import javax.swing.*;

import baccara.gui.*;
import corentx.util.*;


/**
 * A container for component informations.
 *
 * @param <T> The type of the id or name attribute.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.01.2013 - Added.
 * @changed OLI 16.05.2013 - Extended by the selection of objects.
 */

public class ComponentData<T> {

    private boolean actionButton = false;
    private boolean disabled = false;
    private T name = null;
    private boolean noSelection = false;
    private ListCellRenderer renderer = null;
    private List selectionList = null;
    private Type type = null;
    private Object value = null;
    private ResourceId resourceId = null;

    /**
     * Creates a new component data with the passed parameters.
     *
     * @param name The name of the component data.
     * @param type The type of the component data.
     * @param value The initial value which is to set while components construction.
     * @throws IllegalArgumentException In case of precondition violation.
     * @precondition key != null
     * @precondition !key.isEmpty()
     * @precondition type != null
     *
     * @changed OLI 18.01.2013 - Added.
     */
    public ComponentData(T name, Type type, Object value) throws IllegalArgumentException {
        super();
        ensure(name != null, "name cannot be null.");
        ensure(!name.equals(""), "name cannot be empty.");
        ensure(type != null, "type cannot be null (" + name + ").");
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * Creates a new component data with the passed parameters.
     *
     * @param name The name of the component data.
     * @param type The type of the component data.
     * @param value The initial value which is to set while components construction.
     * @param resourceId A resource id for the component.
     * @throws IllegalArgumentException In case of precondition violation.
     * @precondition key != null
     * @precondition !key.isEmpty()
     * @precondition type != null
     *
     * @changed OLI 20.07.2016 - Added.
     */
    public ComponentData(T name, Type type, Object value, ResourceId resourceId)
            throws IllegalArgumentException {
        super();
        ensure(name != null, "name cannot be null.");
        ensure(!name.equals(""), "name cannot be empty.");
        ensure(type != null, "type cannot be null (" + name + ").");
        this.name = name;
        this.resourceId = resourceId;
        this.type = type;
        this.value = value;
    }

    /**
     * Creates a new component data with the passed parameters for a selection component.
     *
     * @param name The name of the component data.
     * @param selectionList The list of values which are allowed to select of.
     * @param value The initial value which is to set while components construction.
     * @param noSelection Set this flag if a no selection item should be added.
     * @throws IllegalArgumentException In case of precondition violation.
     * @precondition key != null
     * @precondition !key.isEmpty()
     * @precondition type != null
     *
     * @changed OLI 16.05.2013 - Added.
     */
    public ComponentData(T name, List selectionList, Object value, boolean noSelection)
            throws IllegalArgumentException {
        super();
        ensure(name != null, "name cannot be null.");
        ensure(!name.equals(""), "name cannot be empty.");
        ensure(selectionList != null, "selection list cannot be null (" + name + ").");
        this.name = name;
        this.noSelection = noSelection;
        this.selectionList = selectionList;
        this.type = Type.OBJECT;
        this.value = value;
    }

    /**
     * Creates a new component data with the passed parameters for a selection component.
     *
     * @param name The name of the component data.
     * @param selectionList The list of values which are allowed to select of.
     * @param value The initial value which is to set while components construction.
     * @param noSelection Set this flag if a no selection item should be added.
     * @param resourceId A resource id for the component.
     * @throws IllegalArgumentException In case of precondition violation.
     * @precondition key != null
     * @precondition !key.isEmpty()
     * @precondition type != null
     *
     * @changed OLI 20.07.2016 - Added.
     */
    public ComponentData(T name, List selectionList, Object value, boolean noSelection,
            ResourceId resourceId) throws IllegalArgumentException {
        super();
        ensure(name != null, "name cannot be null.");
        ensure(!name.equals(""), "name cannot be empty.");
        ensure(selectionList != null, "selection list cannot be null (" + name + ").");
        this.name = name;
        this.noSelection = noSelection;
        this.resourceId = resourceId;
        this.selectionList = selectionList;
        this.type = Type.OBJECT;
        this.value = value;
    }

    /**
     * Creates a new component data with the passed parameters for a selection component.
     *
     * @param name The name of the component data.
     * @param selectionList The list of values which are allowed to select of.
     * @param value The initial value which is to set while components construction.
     * @param renderer A renderer for displaying the list objects in the combo box.
     * @param noSelection Set this flag if a no selection item should be added.
     * @throws IllegalArgumentException In case of precondition violation.
     * @precondition key != null
     * @precondition !key.isEmpty()
     * @precondition type != null
     *
     * @changed OLI 16.05.2013 - Added.
     */
    public ComponentData(T name, List selectionList, Object value,
            ListCellRenderer renderer, boolean noSelection) throws IllegalArgumentException {
        super();
        ensure(name != null, "name cannot be null.");
        ensure(!name.equals(""), "name cannot be empty.");
        ensure(selectionList != null, "selection list cannot be null (" + name + ").");
        this.name = name;
        this.noSelection = noSelection;
        this.renderer = renderer;
        this.selectionList = selectionList;
        this.type = Type.OBJECT;
        this.value = value;
    }

    /**
     * Creates a new component data with the passed parameters for a selection component.
     *
     * @param name The name of the component data.
     * @param selectionList The list of values which are allowed to select of.
     * @param value The initial value which is to set while components construction.
     * @param renderer A renderer for displaying the list objects in the combo box.
     * @param noSelection Set this flag if a no selection item should be added.
     * @param resourceId A resource id for the component.
     * @throws IllegalArgumentException In case of precondition violation.
     * @precondition key != null
     * @precondition !key.isEmpty()
     * @precondition type != null
     *
     * @changed OLI 20.07.2016 - Added.
     */
    public ComponentData(T name, List selectionList, Object value, ListCellRenderer renderer,
            boolean noSelection, ResourceId resourceId) throws IllegalArgumentException {
        super();
        ensure(name != null, "name cannot be null.");
        ensure(!name.equals(""), "name cannot be empty.");
        ensure(selectionList != null, "selection list cannot be null (" + name + ").");
        this.name = name;
        this.noSelection = noSelection;
        this.renderer = renderer;
        this.resourceId = resourceId;
        this.selectionList = selectionList;
        this.type = Type.OBJECT;
        this.value = value;
    }

    protected ComponentData(ComponentData<T> cd) {
        super();
        this.actionButton = cd.actionButton;
        this.name = cd.name;
        this.noSelection = cd.noSelection;
        this.renderer = cd.renderer;
        this.resourceId = cd.resourceId;
        this.selectionList = cd.selectionList;
        this.type = cd.type;
        this.value = cd.value;
    }

    /**
     * @changed OLI 18.01.2013 - Added.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof ComponentData)) {
            return false;
        }
        ComponentData co = (ComponentData) o;
        return this.getName().equals(co.getName())
                && (this.getType() == co.getType())
                && Utl.equals(this.getValue(), co.getValue());
    }

    /**
     * Returns the name of the component data.
     *
     * @return The name of the component data.
     *
     * @changed OLI 18.01.2013 - Added.
     */
    public T getName() {
        return this.name;
    }

    /**
     * Returns a renderer for displaying the list objects.
     *
     * @return A renderer for displaying the list objects.
     *
     * @changed OLI 23.05.2013 - Added.
     */
    public ListCellRenderer getRenderer() {
        return this.renderer;
    }

    /**
     * Returns the resource id for the component or <CODE>null</CODE> if the resource id should
     * be generated automatically.
     *
     * @return The resource id for the component or <CODE>null</CODE> if the resource id should
     *         be generated automatically.
     *
     * @changed OLI 20.07.2016 - Added.
     */
    public ResourceId getResourceId() {
        return this.resourceId;
    }

    /**
     * Returns the selection list if there is one defined.
     *
     * @return The selection list if there is one defined.
     *
     * @changed OLI 16.05.2013 - Added.
     */
    public Object[] getSelectionList() {
        return this.selectionList.toArray();
    }

    /**
     * Returns the type of the component data.
     *
     * @return The type of the component data.
     *
     * @changed OLI 18.01.2013 - Added.
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Returns the initial value which is to set during components initialization.
     *
     * @return The initial value which is to set during components initialization.
     *         <CODE>null</CODE> sets nothing.
     *
     * @changed OLI 18.01.2013 - Added.
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Checks if an action button is required for the field.
     *
     * @return <CODE>true</CODE> if an action button is required for the field.
     *
     * @changed OLI 01.08.2016 - Added.
     */
    public boolean isActionButton() {
        return this.actionButton;
    }

    /**
     * Checks if the field should be diabled.
     *
     * @return <CODE>true</CODE> if the field should be disabled.
     *
     * @changed OLI 01.08.2016 - Added.
     */
    public boolean isDisabled() {
        return this.disabled;
    }

    /**
     * Returns the state of the no selection flag.
     *
     * @return The state of the no selection flag.
     *
     * @changed OLI 06.07.2016 - Added.
     */
    public boolean isNoSelection() {
        return this.noSelection;
    }

    /**
     * @changed OLI 18.01.2013 - Added.
     */
    @Override public int hashCode() {
        int result = 31 + this.getName().hashCode();
        result = 31 * result + this.getType().hashCode();
        result = 31 * result + (this.getValue() != null ? this.getValue().hashCode() : 0);
        return result;
    }

    /**
     * Sets the action button flag for the field.
     *
     * @param actionButton The new value of the action button flag.
     * @return The changes component data.
     *
     * @changed OLI 01.08.2016 - Added.
     */
    public ComponentData<T> setActionButton(boolean actionButton) {
        ComponentData<T> cd = new ComponentData<T>(this);
        cd.actionButton = actionButton;
        return cd;
    }

    /**
     * Sets the disabled flag for the field.
     *
     * @param disabled The new value of the disabled flag.
     * @return The changes component data.
     *
     * @changed OLI 01.08.2016 - Added.
     */
    public ComponentData<T> setDisabled(boolean disabled) {
        ComponentData<T> cd = new ComponentData<T>(this);
        cd.disabled = disabled;
        return cd;
    }

    /**
     * @changed OLI 18.01.2013 - Added.
     */
    @Override public String toString() {
        return "Name=" + this.getName() + ",Type=" + this.getType() + ",Value=" + this.getValue(
                );
    }

}