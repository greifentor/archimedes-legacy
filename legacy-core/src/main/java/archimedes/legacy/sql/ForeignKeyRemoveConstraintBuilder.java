/*
 * ForeignKeyRemoveConstraintBuilder.java
 *
 * 25.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static corentx.util.Checks.ensure;

import java.util.Enumeration;
import java.util.List;

import archimedes.legacy.model.ColumnMetaData;
import archimedes.legacy.model.TableMetaData;
import archimedes.legacy.script.sql.SQLScript;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * This class is able to extend a SQL script by adding those foreign key
 * constraints which are in the model but not in the scheme.
 * 
 * @author ollie
 * 
 * @changed OLI 25.04.2013 - Added.
 */

public class ForeignKeyRemoveConstraintBuilder {

	private SQLScriptFactory factory = null;
	private List metaData = null;
	private DataModel model = null;

	/**
	 * Creates a new foreign key constraint builder to add missing constraints.
	 * 
	 * @param model
	 *            The diagram model whose data are the base of the script.
	 * @param factory
	 *            The SQL script factory which generates the SQL code.
	 * @param metaData
	 *            The meta data of the data scheme which is to update.
	 * @throws IllegalArgumentException
	 *             In case of passing a null pointer.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	public ForeignKeyRemoveConstraintBuilder(DataModel model, SQLScriptFactory factory, List metaData)
			throws IllegalArgumentException {
		super();
		ensure(factory != null, "SQL script factory cannot be null.");
		ensure(metaData != null, "meta data cannot be null.");
		ensure(model != null, "model cannot be null.");
		this.factory = factory;
		this.metaData = metaData;
		this.model = model;
	}

	/**
	 * Extends passed SQL script by adding foreign key constraints if there are
	 * some to add.
	 * 
	 * @param sql
	 *            The SQL script to extend.
	 * @return <CODE>true</CODE> if the script has been extended
	 *         <CODE>false</CODE> otherwise.
	 * @throws IllegalArgumentException
	 *             Passing a null pointer.
	 * 
	 * @changed OLI 25.04.2013 - Added.
	 */
	public boolean build(SQLScript script) throws IllegalArgumentException {
		boolean changed = false;
		for (int i = 0, leni = this.metaData.size(); i < leni; i++) {
			TableMetaData tmd = (TableMetaData) this.metaData.get(i);
			for (Enumeration e = tmd.getColumns().elements(); e.hasMoreElements();) {
				ColumnMetaData cmd = (ColumnMetaData) e.nextElement();
				if (cmd.getForeignKeyConstraintName() != null) {
					ColumnModel c = this.getColumnModel(tmd.name, cmd.name);
					if (c != null) {
						String fkConstraintName = this.factory.getForeignKeyConstraintName(c);
						if (c.isSuppressForeignKeyConstraint()) {
							fkConstraintName = ";op";
						}
						if (!fkConstraintName.equals(cmd.getForeignKeyConstraintName())) {
							if (!changed) {
								script.addReducingStatement("");
							}
							script.addReducingStatement(this.factory.dropForeignKeyConstraint(tmd.name, cmd
									.getForeignKeyConstraintName()));
							changed = true;
						}
					}
				}
			}
		}
		return changed;
	}

	private ColumnModel getColumnModel(String tableName, String columnName) {
		ColumnModel c = null;
		TableModel t = this.model.getTableByName(tableName);
		if (t != null) {
			c = t.getColumnByName(columnName);
		}
		return c;
	}

}