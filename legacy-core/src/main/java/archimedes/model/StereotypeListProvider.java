/*
 * StereotypeListProvider.java
 *
 * 16.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.model;


/**
 * An interface for objects which provide a list of stereotypes.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 16.06.2016 - Added.
 */

public interface StereotypeListProvider {

    /**
     * Adds the passed stereotype to the table. A already existing stereotype will not be set
     * twice.
     *
     * @param stereotype The stereotype which is to add.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public void addStereotype(StereotypeModel stereotype)
            throws IllegalArgumentException;

    /**
     * Returns the stereotype at the passed position in the stereotype list of the table.
     *
     * @param i The position whose stereotype is to return.
     * @return The stereotype at the passed position in the stereotype list of the table.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public StereotypeModel getStereotypeAt(int i);

    /**
     * Returns the first stereotype found with the passed name, if there is one.
     *
     * @param name The name of the stereotype which is to return. 
     * @return The first stereotype found with the passed name, if there is one. If not,
     *         <CODE>null</CODE> is returned.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public StereotypeModel getStereotypeByName(String name);

    /**
     * Returns the count of the stereotypes of the table.
     *
     * @return The count of the stereotypes of the table.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public int getStereotypeCount();

    /**
     * Returns an array with all stereotypes from the table. The array shows the stereotypes
     * assorted by their names.
     *
     * @return An array with the stereotypes of the table.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public StereotypeModel[] getStereotypes();

    /**
     * Returns all stereotypes found with the passed name, if there is at least one. The array
     * shows the stereotypes assorted by their names.
     *
     * @param name The name of the stereotype which is to return. 
     * @return All stereotypes found with the passed name, if there is at least one. Otherwise
     *         an empty array will be returned.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public StereotypeModel[] getStereotypesByName(String name);

    /**
     * Checks if at least one stereotype with the passed name is set for the object.
     *
     * @param stereotypeName The name of the stereotype which is to check for being set.
     * @return <CODE>true</CODE> if at least one stereotype is set with the passed name.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public boolean isStereotypeSet(String stereotypeName);

    /**
     * Removes the passed stereotype from the list of the stereotypes of the table. 
     *
     * @param stereotype The stereotype which is to remove from the list of the stereotypes
     *         which the table belongs to.
     *
     * @changed OLI 16.06.2016 - Approved.
     */
    abstract public void removeStereotype(StereotypeModel stereotype);

}