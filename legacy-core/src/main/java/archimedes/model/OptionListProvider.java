/*
 * OptionListProvider.java
 *
 * 17.12.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.model;

import java.util.Optional;
import java.util.function.Consumer;

import archimedes.util.OptionUtil;

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
	void addOption(OptionModel option) throws IllegalArgumentException;

	/**
	 * Returns the option at the passed position in the option list of the table.
	 *
	 * @param i The position whose option is to return.
	 * @return The option at the passed position in the option list of the table.
	 *
	 * @changed OLI 15.10.2013 - Added.
	 * @changed OLI 29.09.2023 - Made it default.
	 */
	default OptionModel getOptionAt(int i) {
		return OptionUtil.getOptionAt(this, i);
	}

	/**
	 * Returns the first option found with the passed name, if there is one.
	 *
	 * @param name The name of the option which is to return.
	 * @return The first option found with the passed name, if there is one. If not, <CODE>null</CODE> is returned.
	 *
	 * @changed OLI 10.03.2016 - Added.
	 * @changed OLI 29.09.2023 - Made it default.
	 */
	default OptionModel getOptionByName(String name) {
		return OptionUtil.getOptionByName(this, name);
	}

	/**
	 * Returns an optional with the first option found with the passed name, if there is one.
	 *
	 * @param name The name of the option which is to return.
	 * @return An optional with the first option found with the passed name, if there is one. If not, an empty optional
	 *         is returned.
	 *
	 * @changed OLI 29.09.2023 - Added.
	 * @changed OLI 29.09.2023 - Made it default.
	 */
	default Optional<OptionModel> findOptionByName(String name) {
		return OptionUtil.findOptionByName(this, name);
	}

	/**
	 * Returns the count of the options of the table.
	 *
	 * @return The count of the options of the table.
	 *
	 * @changed OLI 15.10.2013 - Added.
	 * @changed OLI 29.09.2023 - Made it default.
	 */
	default int getOptionCount() {
		return OptionUtil.getOptionCount(this);
	}

	/**
	 * Returns an array with all options from the table. The array shows the options assorted by their names.
	 *
	 * @return An array with the options of the table.
	 *
	 * @changed OLI 15.10.2013 - Added.
	 */
	OptionModel[] getOptions();

	/**
	 * Returns all options found with the passed name, if there is at least one. The array shows the options assorted by
	 * their names.
	 *
	 * @param name The name of the option which is to return.
	 * @return All options found with the passed name, if there is at least one. Otherwise an empty array will be
	 *         returned.
	 *
	 * @changed OLI 10.03.2016 - Added.
	 * @changed OLI 29.09.2023 - Made it default.
	 */
	default OptionModel[] getOptionsByName(String name) {
		return OptionUtil.getOptionsByName(this, name);
	}

	/**
	 * Checks if at least one option with the passed name is set for the object and has the passed value. In this case
	 * The passed runnable will be executed.
	 *
	 * @param optionName The name of the option which is to check for being set.
	 * @param value      The value the option should have.
	 * @param runnable   The runnable which should be executed if there is an option with the passed value.
	 *
	 * @changed OLI 29.09.2023 - Added.
	 * @changed OLI 29.09.2023 - Made it default.
	 */
	default void ifOptionSetWithValueDo(String optionName, String value, Consumer<OptionModel> consumer) {
		OptionUtil.ifOptionSetWithValueDo(this, optionName, value, consumer);
	}

	/**
	 * Checks if at least one option with the passed name is set for the object.
	 *
	 * @param optionName The name of the option which is to check for being set.
	 * @return <CODE>true</CODE> if at least one option is set with the passed name.
	 *
	 * @changed OLI 26.05.2016 - Added.
	 * @changed OLI 29.09.2023 - Made it default.
	 */
	default boolean isOptionSet(String optionName) {
		return OptionUtil.isOptionSet(this, optionName);
	}

	/**
	 * Checks if at least one option with the passed name is set for the object and has the passed value.
	 *
	 * @param optionName The name of the option which is to check for being set.
	 * @param value      The value the option should have.
	 * @return <CODE>true</CODE> if at least one option is set with the passed name and the passed value.
	 *
	 * @changed OLI 29.09.2023 - Added.
	 * @changed OLI 29.09.2023 - Made it default.
	 */
	default boolean isOptionSetWithValue(String optionName, String value) {
		return OptionUtil.isOptionSetWithValue(this, optionName, value);
	}

	/**
	 * Removes the passed option from the list of the options of the table.
	 *
	 * @param option The option which is to remove from the list of the options which the table belongs to.
	 *
	 * @changed OLI 15.10.2013 - Approved.
	 */
	void removeOption(OptionModel option);

}