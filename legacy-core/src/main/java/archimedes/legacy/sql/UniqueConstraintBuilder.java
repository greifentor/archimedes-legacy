/*
 * UniqueConstraintBuilder.java
 *
 * 11.06.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import java.util.List;

import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.script.sql.SQLScript;
import archimedes.model.ColumnModel;
import archimedes.model.TableModel;
import archimedes.model.UniqueMetaData;
import corent.db.DBExecMode;

/**
 * Builds a statement for an unique constraint addition.
 * 
 * @author ollie
 * 
 * @changed OLI 11.06.2013 - Added.
 */

public class UniqueConstraintBuilder extends AbstractSQLScriptBuilder {

	/**
	 * Creates a new unique addition script builder with the passed parameters.
	 * 
	 * @param mode
	 *            The db mode for which the script is build.
	 * @param model
	 *            The diagram model whose data are the base of the script.
	 * @param quote
	 *            The character sequence to quote names.
	 * @throws IllegalArgumentException
	 *             In case of passing a null pointer.
	 * 
	 * @changed OLI 11.06.2013 - Added.
	 */
	public UniqueConstraintBuilder(DBExecMode mode, DiagrammModel model, String quote) throws IllegalArgumentException {
		super(mode, model, quote);
	}

	/**
	 * Adds SQL statements for the simple indices of the passed column to the
	 * passed script.
	 * 
	 * @param table
	 *            The table whose columns are to check for unique constraint
	 *            changes.
	 * @param script
	 *            The script where the change statements are to add to.
	 * @param uniques
	 *            The unique constraint meta data.
	 * @throws IllegalArgumentException
	 *             Passing a null pointer.
	 * 
	 * @changed OLI 11.06.2013 - Added.
	 */
	public void addUniqueChanges(TableModel table, SQLScript script, List<UniqueMetaData> uniques)
			throws IllegalArgumentException {
		for (ColumnModel column : table.getColumns()) {
			if (column.isTransient()) {
				continue;
			}
			String genName = column.getTable().getName() + "_" + column.getName() + "_key";
			String tableName = column.getTable().getName();
			if (!this.isUniqueMetaData(genName, uniques) && column.isUnique()) {
				if (this.getDBMode() == DBExecMode.POSTGRESQL) {
					script.addReducingStatement(this.getAlterStatementPrfix(tableName) + " ADD CONSTRAINT "
							+ this.quote(genName) + " UNIQUE(" + this.quote(column.getName()) + ");");
				}
			} else if (this.isUniqueMetaData(genName, uniques) && !column.isUnique()) {
				if (this.getDBMode() == DBExecMode.POSTGRESQL) {
					script.addExtendingStatement(this.getAlterStatementPrfix(tableName) + " DROP CONSTRAINT "
							+ this.quote(genName) + ";");
				}
			}
		}
	}

	private boolean isUniqueMetaData(String genName, List<UniqueMetaData> uniques) {
		for (UniqueMetaData umd : uniques) {
			if (genName.equals(umd.getName())) {
				return true;
			}
		}
		return false;
	}

	private String getAlterStatementPrfix(String tableName) {
		return "ALTER TABLE " + this.quote(tableName);
		// return "ALTER TABLE \"" + this.getModel().getName() + "\".\"" +
		// tableName + "\"";
	}

}