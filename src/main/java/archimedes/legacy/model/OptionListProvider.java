/*
 * OptionListProvider.java
 *
 * 17.12.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;

/**
 * An interface for objects which provide a list of options.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.12.2013 - Added.
 */

public interface OptionListProvider {

    /**
     * Adds the passed option to the table. A already existing option will not be set twice.
     *
     * @param option the option which is to add.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 15.10.2013 - Added.
     */
    abstract public void addOption(OptionModel option) throws IllegalArgumentException;

    /**
     * Returns the option at the passed position in the option list of the table.
     *
     * @param i The position whose option is to return.
     * @return The option at the passed position in the option list of the table.
     *
     * @changed OLI 15.10.2013 - Added.
     */
    abstract public OptionModel getOptionAt(int i);

    /**
     * Returns the first option found with the passed name, if there is one.
     *
     * @param name The name of the option which is to return. 
     * @return The first option found with the passed name, if there is one. If not,
     *         <CODE>null</CODE> is returned.
     *
     * @changed OLI 10.03.2016 - Added.
     */
    abstract public OptionModel getOptionByName(String name);

    /**
     * Returns the count of the options of the table.
     *
     * @return The count of the options of the table.
     *
     * @changed OLI 15.10.2013 - Added.
     */
    abstract public int getOptionCount();

    /**
     * Returns an array with all options from the table. The array shows the options assorted
     * by their names.
     *
     * @return An array with the options of the table.
     *
     * @changed OLI 15.10.2013 - Added.
     */
    abstract public OptionModel[] getOptions();

    /**
     * Returns all options found with the passed name, if there is at least one. The array shows
     * the options assorted by their names.
     *
     * @param name The name of the option which is to return. 
     * @return All options found with the passed name, if there is at least one. Otherwise an
     *         empty array will be returned.
     *
     * @changed OLI 10.03.2016 - Added.
     */
    abstract public OptionModel[] getOptionsByName(String name);

    /**
     * Checks if at least one option with the passed name is set for the object.
     *
     * @param optionName The name of the option which is to check for being set.
     * @return <CODE>true</CODE> if at least one option is set with the passed name.
     *
     * @changed OLI 26.05.2016 - Added.
     */
    abstract public boolean isOptionSet(String optionName);

    /**
     * Removes the passed option from the list of the options of the table. 
     *
     * @param option The option which is to remove from the list of the options which the table
     *         belongs to.
     *
     * @changed OLI 15.10.2013 - Approved.
     */
    abstract public void removeOption(OptionModel option);

}