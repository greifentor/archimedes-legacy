/*
 * NReferenceModel.java
 *
 * 03.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * A description of the methods for a nReference.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 03.05.2013 - Added.
 */

public interface NReferenceModel extends Comparable {

    /**
     * Returns the column which represents the nReference.
     *
     * @return The column which represents the nReference.
     *
     * @changed OLI 03.05.2013 - Added.
     */
    abstract public ColumnModel getColumn();

    /**
     * Returns the numeric identifier of the nReference (in case of more than one nReference is
     * available in the object).
     *
     * @return The numeric identifier of the nReference (in case of more than one nReference is
     *         available in the object).
     *
     * @changed OLI 03.05.2013 - Approved.
     */
    abstract public int getId();

    /**
     * Returns the type of the nReference panel which decides about the mode of showing it in
     * the GUI.
     *
     * @return The type of the nReference panel which decides about the mode of showing it in
     *         the GUI.
     *
     * @changed OLI 06.05.2013 - Approved.
     */
    abstract public NReferencePanelType getNReferencePanelType();

    /**
     * Returns the panel of a generated editor for the table, where the nReference is to show.
     * 
     * @return The panel of a generated editor for the table, where the nReference is to show.
     *
     * @changed OLI 06.05.2013 - Approved.
     */
    abstract public PanelModel getPanel();

    /**
     * Checks if the data values of the nReference is to alter in a generic view.
     *
     * @return <CODE>true</CODE> if the data values of the nReference is to alter in a generic
     *         view, <CODE>false</CODE> otherwise.
     *
     * @changed OLI 03.05.2013 - Approved.
     */
    abstract public boolean isAlterable();

    /**
     * Checks if the list GUI efforts a delete confirmation.
     *
     * @return <CODE>true</CODE> if deleting a list member requires a confirmation of the user.
     *
     * @changed OLI 30.09.2013 - Added.
     */
    abstract public boolean isDeleteConfirmationRequired();

    /**
     * Checks if the list of the nReference is extensible by the GUI.
     * 
     * @return <CODE>true</CODE>, in case of the list of the nReference is extensible by the
     *         GUI.
     *
     * @changed OLI 06.05.2013 - Approved.
     */
    abstract public boolean isExtensible();

    /**
     * Checks if a generated editor has to allow to create new records and add them to the list
     * which is represented by the list.
     *
     * @return <CODE>true</CODE> if a generated editor has to allow to create new records and
     *         add them to the list which is represented by the list.
     *
     * @changed OLI 06.05.2013 - Approved.
     */
    abstract public boolean isPermitCreate();

    /**
     * This flag have to be set if a generic editor should make it possible to alter the values
     * of the data records which are referenced by the nReferences.
     *
     * @param alterable The new state of the flag which decides if the values of the referenced
     *
     * @changed OLI 06.05.2013 - Approved.
     */
    abstract public void setAlterable(boolean alterable);

    /**
     * Sets the passed column as new column for the nReferences.
     *
     * @param column The new column for the nReference.
     *
     * @changed OLI 06.05.2013 - Added.
     */
    abstract public void setColumn(ColumnModel column);

    /**
     * Set this flag if a confirmation by the user is required while removing a list member via
     * GUI.
     *
     * @param b The new value of the flag.
     *
     * @changed OLI 30.09.2013 - Added.
     */
    abstract public void setDeleteConfirmationRequired(boolean b);

    /**
     * If this flag is set, a generated editor has to provide a method to extend the records in
     * the list which is represented by the nReference.
     *
     * @param extensible This flag have to be set to allow to add record in a generated GUI.
     *
     * @changed OLI 06.05.2013 - Approved..
     */
    abstract public void setExtensible(boolean extensible);

    /**
     * Sets the passed id as the new numeric identifier for the nReference.
     *
     * @param id The new numeric identifier for the nReference.
     *
     * @changed OLI 03.05.2013 - Approved.
     */
    abstract public void setId(int id);

    /**
     * Sets the panel of the generated editor where the nReference is to show.
     *
     * @param panel The panel of the generated editor where the nReference is to show.
     *
     * @changed OLI 06.05.2013 - Approved.
     */
    abstract public void setPanel(PanelModel panel);

    /**
     * If this flag is set, a generated editor has to provide a method to create and add records
     * to the list which is represented by the nReference.
     *
     * @param permitCreate Set this flag to force a generated editor to provide a method to
     *         create an add records to the list which is represented by the nReference.
     *
     * @changed OLI 06.05.2013 - Approved.
     */
    abstract public void setPermitCreate(boolean permitCreate);

    /**
     * Sets the passed panel type for the nReference. 
     *
     * @param panelType The new panel type for the nReference.
     *
     * @changed OLI 06.05.2013 - Approved
     */
    abstract public void setNReferencePanelType(NReferencePanelType panelType);

}