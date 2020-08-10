/*
 * PostgreSQLConstraintNameGenerator.java
 *
 * 08.04.2014
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql.factories;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.util.NameGenerator;
import corent.db.DBExecMode;

/**
 * An implementation of the <CODE>ConstraintNameGenerator</CODE> interface for
 * PostgreSQL.
 * 
 * @author ollie
 * 
 * @changed OLI 08.04.2014 - Added.
 */

public class PostgreSQLConstraintNameGenerator implements ConstraintNameGenerator {

	private NameGenerator nameGenerator = new NameGenerator(DBExecMode.POSTGRESQL);

	/**
	 * @throws NullPointerException
	 *             If table is passed as <CODE>null</CODE>.
	 * 
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Override
	public String createForeignKeyConstraintName(ColumnModel column) {
		return this.nameGenerator.getForeignKeyName(column.getTable().getName(), column.getName());
	}

	/**
	 * @changed OLI 11.12.2015 - Added.
	 */
	@Override
	public String createIndexName(ColumnModel column) {
		return this.nameGenerator.getIndexName(column.getTable().getName(), column.getName());
	}

	/**
	 * @throws NullPointerException
	 *             If table is passed as <CODE>null</CODE>.
	 * 
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Override
	public String createPrimaryKeyConstraintName(TableModel table) {
		return table.getName() + "_pkey";
	}

	/**
	 * @throws ArrayIndexOutOfBoundsException
	 *             If no unique columns are passed.
	 * 
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Override
	public String createUniqueConstraintName(String tableName, String... uniqueColumns) {
		return tableName + "_" + uniqueColumns[0] + "_key";
	}

}