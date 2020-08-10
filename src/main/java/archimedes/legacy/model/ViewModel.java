/*
 * ViewModel.java
 *
 * 11.02.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package archimedes.legacy.model;


/**
 * A view is another sight to the diagram. Table can be ordered in another shape or the view
 * could show only a part of the tables of the diagram.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 10.08.2008 - Extended by the flag to hide technical fields
 */
 
public interface ViewModel extends CommentOwner, Comparable, NamedObject {

    /**
     * Adds the passed table to the view.
     *
     * @param table The table which is to add to the view.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 08.05.2013 - Added.
     */
    abstract public void addTable(TableModel table) throws IllegalArgumentException;

    /**
     * Returns the name of the view.
     * 
     * @return The name of teh view.
     *
     * @changed OLI 08.05.2013 - Approved.
     */
    abstract public String getName();

    /**
     * Returns the tables which are connected to the view.
     *
     * @return The tables which are connected to the view.
     *
     * @changed OLI 08.05.2013 - Added.
     */
    abstract public TableModel[] getTables();

    /**
     * Checks if the technical field should be hidden in the view.
     *
     * @return <CODE>true</CODE> if the technical fields should be hidden in the view.
     *
     * @changed OLI 10.08.2008 - Added.
     */
    abstract public boolean isHideTechnicalFields();

    /**
     * Checks if the transient field should be hidden in the view.
     *
     * @return <CODE>true</CODE> if the transient fields should be hidden in the view.
     *
     * @changed OLI 11.06.2015 - Added.
     */
    abstract public boolean isHideTransientFields();

    /**
     * Checks if the current view is the main view. The diagram / data model should have only
     * one main view.
     *
     * @return <CODE>true</CODE> if the current view is the main view.
     *
     * @changed OLI 09.06.2016 - Added.
     */
    abstract public boolean isMainView();

    /**
     * Checks if the referenced columns should be shown in the view.
     * 
     * @return <CODE>true</CODE> if the foreign key columns should be shown in the tables.
     *
     * @changed OLI 08.05.2013 - Approved.
     */
    abstract public boolean isShowReferencedColumns();

    /**
     * Sets or resets the technical field flag.
     *
     * @param b <CODE>true</CODE>, if the field should be treated as a technical field,
     *        <CODE>false</CODE> otherwise.
     *
     * @changed OLI 11.06.2015 - Approved.
     *
     */
    abstract public void setHideTechnicalFields(boolean b);

    /**
     * Sets or resets the transient field flag.
     *
     * @param b <CODE>true</CODE>, if the field should be treated as a transient field,
     *        <CODE>false</CODE> otherwise.
     *
     * @changed OLI 11.06.2015 - Added.
     *
     */
    abstract public void setHideTransientFields(boolean b);

    /**
     * Sets a new state for the main view flag. Not that a diagram / data model should have only
     * one main view.
     *
     * @param mainView The new state for the main view.
     *
     * @changed OLI 09.06.2016 - Added.
     */
    abstract public void setMainView(boolean mainView);

    /** 
     * Sets a new name for the view.
     *
     * @param name The new name for the view.
     */
    abstract public void setName(String name);

    /**
     * Sets a new state for the flag which decides if referenced columns should be shown.
     *
     * @param showReferencedColumns The new state for the show referenced columns flag.
     */
    abstract public void setShowReferencedColumns(boolean showReferencedColumns);

}