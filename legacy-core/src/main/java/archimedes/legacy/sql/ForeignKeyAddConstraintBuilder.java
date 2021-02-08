/*
 * ForeignKeyAddConstraintBuilder.java
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
import corent.db.DBExecMode;
import corentx.util.SortedVector;

/**
 * This class is able to extend a SQL script by adding those foreign key
 * constraints which are in the model but not in the scheme.
 * 
 * @author ollie
 * 
 * @changed OLI 25.04.2013 - Added.
 */

public class ForeignKeyAddConstraintBuilder extends AbstractSQLScriptBuilder {

	private List metaData = null;

	/**
	 * Creates a new foreign key constraint builder to add missing constraints.
	 * 
	 * @param mode
	 *            The db mode for which the script is build.
	 * @param model
	 *            The diagram model whose data are the base of the script.
	 * @param metaData
	 *            The meta data of the data scheme which is to update.
	 * @param quote
	 *            The character sequence to quote names.
	 * @throws IllegalArgumentException
	 *             In case of passing a null pointer.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	public ForeignKeyAddConstraintBuilder(DBExecMode mode, DataModel model, List metaData, String quote)
			throws IllegalArgumentException {
		super(mode, model, quote);
		ensure(metaData != null, "meta data cannot be null.");
		this.metaData = metaData;
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
		List<ColumnModel> fks = getAllForeignKeyColumnsFromModel();
		for (ColumnModel fk : fks) {
			if (fk.isTransient() || fk.isSuppressForeignKeyConstraint()) {
				continue;
			}
			String fkConstraintName = new ForeignConstraintNameCreator().create(fk);
			ColumnMetaData cmd = this.getColumnMetaDataWithConstraint(fkConstraintName);
			if (cmd == null) {
				if (!changed) {
					script.addReducingStatement("");
				}
				if (this.getDBMode() == DBExecMode.POSTGRESQL) {
					script.addReducingStatement("ALTER TABLE " + this.quote(fk.getTable().getName())
							+ " ADD CONSTRAINT " + this.quote(fkConstraintName) + " FOREIGN KEY ("
							+ this.quote(fk.getName()) + ") REFERENCES "
							+ this.quote(fk.getRelation().getReferenced().getTable().getName()) + ";");
				}
				changed = true;
			}
		}
		return changed;
	}

	private List<ColumnModel> getAllForeignKeyColumnsFromModel() {
		List<ColumnModel> l = new SortedVector<ColumnModel>();
		for (ColumnModel c : this.getModel().getAllColumns()) {
			if ((c.getRelation() != null) && (c.getRelation().getReferencer() == c)
					&& !c.getRelation().getReferenced().getTable().isExternalTable()) {
				l.add(c);
			}
		}
		return l;
	}

	private ColumnMetaData getColumnMetaDataWithConstraint(String fkConstraintName) {
		for (int i = 0, leni = this.metaData.size(); i < leni; i++) {
			TableMetaData tmd = (TableMetaData) this.metaData.get(i);
			for (Enumeration e = tmd.getColumns().elements(); e.hasMoreElements();) {
				ColumnMetaData cmd = (ColumnMetaData) e.nextElement();
				if (fkConstraintName.equals(cmd.getForeignKeyConstraintName())) {
					return cmd;
				}
			}
		}
		return null;
	}

}