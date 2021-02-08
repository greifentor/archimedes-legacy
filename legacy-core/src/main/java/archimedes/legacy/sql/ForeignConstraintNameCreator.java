/*
 * ForeignConstraintNameCreator.java
 *
 * 25.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static corentx.util.Checks.ensure;
import archimedes.model.ColumnModel;

/**
 * A creator for foreignn key constraint names.
 * 
 * @author ollie
 * 
 * @changed OLI 25.04.2013 - Added.
 */

public class ForeignConstraintNameCreator {

	/**
	 * Creates a new name for the passed column.
	 * 
	 * @param column
	 *            The column which the foreign key constraint name is to create
	 *            for.
	 * @throws IllegalArgumentException
	 *             Passing a null pointer.
	 * 
	 * @changed OLI 25.04.2013 - Added.
	 */
	public String create(ColumnModel column) throws IllegalArgumentException {
		ensure(column != null, "column cannot be null.");
		return column.getTable().getName().concat("_").concat(column.getName()).concat("_fkey");
	}

}