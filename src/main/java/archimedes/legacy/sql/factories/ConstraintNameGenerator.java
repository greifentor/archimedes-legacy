/*
 * ConstraintNameGenerator.java
 *
 * 08.04.2014
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql.factories;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.TableModel;

/**
 * Implementations of this interface are able to create constraint names by
 * defined conventions.
 * 
 * @author ollie
 * 
 * @changed OLI 08.04.2014 - Added.
 */

public interface ConstraintNameGenerator {

	/**
	 * Returns the name for a foreign key constraint.
	 * 
	 * @param column
	 *            The column which the foreign key constraint name is created
	 *            for.
	 * @return The name for a foreign key constraint.
	 * @throws IllegalStateException
	 *             If no foreign key is defined for table referencing the
	 *             referenced table.
	 * 
	 * @changed OLI 08.04.2014 - Added.
	 * @changed OLI 12.05.2014 - Changed to column model parameter.
	 */
	abstract public String createForeignKeyConstraintName(ColumnModel column);

	/**
	 * Returns the name for simple index of one field.
	 * 
	 * @param column
	 *            The column which the index name is created for.
	 * @return The name for an index of the field with the passed data.
	 * 
	 * @changed OLI 11.12.2015 - Added.
	 */
	abstract public String createIndexName(ColumnModel column);

	/**
	 * Returns the name for a primary key constraint.
	 * 
	 * @param table
	 *            The table which the primary key constraint name is created
	 *            for.
	 * @return The name for a primary key constraint.
	 * 
	 * @changed OLI 08.04.2014 - Added.
	 */
	abstract public String createPrimaryKeyConstraintName(TableModel table);

	/**
	 * Returns the name for an unique constraint.
	 * 
	 * @param tableName
	 *            The name of the table which the unique constraint name is
	 *            created for.
	 * @param uniqueColumns
	 *            The names of the columns which are members of the unique
	 *            constraint.
	 * @return The name for an unique constraint.
	 * 
	 * @changed OLI 08.04.2014 - Added.
	 */
	abstract public String createUniqueConstraintName(String tableName, String... uniqueColumns);

}