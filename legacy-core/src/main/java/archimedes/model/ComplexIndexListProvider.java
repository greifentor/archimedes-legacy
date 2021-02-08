/*
 * ComplexIndexListProvider.java
 *
 * 16.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.model;


/**
 * An interface of classes whose objects provide a list of complex indices.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 16.06.2016 - Added.
 */

public interface ComplexIndexListProvider {

    /**
     * Adds the passed complex index to the table. A already existing complexIndex will not be
     * set twice.
     *
     * @param complexIndex The complex index which is to add.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public void addComplexIndex(IndexMetaData complexIndex)
            throws IllegalArgumentException;

    /**
     * Returns the complex index at the passed position in the complexIndex list of the table.
     *
     * @param i The position whose complex index is to return.
     * @return The complex index at the passed position in the complex index list of the table.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public IndexMetaData getComplexIndexAt(int i);

    /**
     * Returns the first complex index found with the passed name, if there is one.
     *
     * @param name The name of the complex index which is to return. 
     * @return The first complex index found with the passed name, if there is one. If not,
     *         <CODE>null</CODE> is returned.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public IndexMetaData getComplexIndexByName(String name);

    /**
     * Returns the count of the complex indices of the table.
     *
     * @return The count of the complex indices of the table.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public int getComplexIndexCount();

    /**
     * Returns an array with all complex indices from the table. The array shows the complex
     * indices assorted by their names.
     *
     * @return An array with the complex indices of the table.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public IndexMetaData[] getComplexIndices();

    /**
     * Checks if at least one complex index with the passed name is set for the object.
     *
     * @param complexIndexName The name of the complex index which is to check for being set.
     * @return <CODE>true</CODE> if at least one complex index is set with the passed name.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public boolean isComplexIndexSet(String complexIndexName);

    /**
     * Removes the passed complex index from the list of the complexIndexs of the table. 
     *
     * @param complexIndex The complex index which is to remove from the list of the complex
     *         indices which the table belongs to.
     *
     * @changed OLI 16.06.2016 - Approved.
     */
    abstract public void removeComplexIndex(IndexMetaData complexIndex);

}