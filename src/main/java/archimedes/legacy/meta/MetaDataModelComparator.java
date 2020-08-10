/*
 * MetaDataModelComparator.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import static corentx.util.Checks.ensure;

import java.util.List;

import archimedes.legacy.meta.chops.AbstractChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.meta.chops.addscripts.AddAdditionalScript;
import archimedes.legacy.meta.chops.columns.AddColumn;
import archimedes.legacy.meta.chops.columns.AlterColumnAddConstraint;
import archimedes.legacy.meta.chops.columns.AlterColumnDataType;
import archimedes.legacy.meta.chops.columns.AlterColumnDropConstraint;
import archimedes.legacy.meta.chops.columns.AlterColumnDropDefault;
import archimedes.legacy.meta.chops.columns.AlterColumnSetDefaultValue;
import archimedes.legacy.meta.chops.columns.ColumnConstraintType;
import archimedes.legacy.meta.chops.columns.DropColumn;
import archimedes.legacy.meta.chops.foreignkeys.AddForeignKeyConstraint;
import archimedes.legacy.meta.chops.foreignkeys.AddMultipleForeignKeyConstraint;
import archimedes.legacy.meta.chops.foreignkeys.DropForeignKeyConstraint;
import archimedes.legacy.meta.chops.foreignkeys.DropMultipleForeignKeyConstraint;
import archimedes.legacy.meta.chops.indices.CreateIndex;
import archimedes.legacy.meta.chops.indices.DropIndex;
import archimedes.legacy.meta.chops.primarykeys.AddPrimaryKeyConstraint;
import archimedes.legacy.meta.chops.primarykeys.DropPrimaryKeyConstraint;
import archimedes.legacy.meta.chops.sequences.AlterSequence;
import archimedes.legacy.meta.chops.sequences.CreateSequence;
import archimedes.legacy.meta.chops.sequences.DropSequence;
import archimedes.legacy.meta.chops.tables.CreateTable;
import archimedes.legacy.meta.chops.tables.DropTable;
import archimedes.legacy.meta.chops.uniques.AddComplexUniqueConstraint;
import archimedes.legacy.meta.chops.uniques.DropComplexUniqueConstraint;
import archimedes.legacy.util.NameGenerator;
import corent.db.DBExecMode;
import corentx.util.SortedVector;
import corentx.util.Utl;

/**
 * A comparator which is able to compare two meta data models and create a list of changes which are necessary to
 * equalize the two meta data model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class MetaDataModelComparator {

	private MetaUtil metaUtil = new MetaUtil("");
	private NameGenerator nameGenerator = null;
	private List<AddAdditionalScript> additionalScripts = new SortedVector<AddAdditionalScript>();

	/**
	 * Creates a new meta data model comparator with the passed parameters.
	 *
	 * @param nameGenerator A generator for database object names.
	 *
	 * @changed OLI 18.12.2015 - Added.
	 */
	public MetaDataModelComparator(NameGenerator nameGenerator) {
		super();
		ensure(nameGenerator != null, "name generator cannot be null.");
		this.nameGenerator = nameGenerator;
	}

	/**
	 * Adds the passed additional script fragment (in string format) which is added to the result of any compare
	 * operation.
	 *
	 * @param script  The script fragment which is to add to the model difference.
	 * @param section The section which the script fragment is to add to.
	 *
	 * @changed OLI 18.12.2015 - Added.
	 */
	public MetaDataModelComparator addScript(String script, ScriptSectionType section) {
		if ((script != null) && !script.isEmpty()) {
			this.additionalScripts.add(new AddAdditionalScript(script, section));
		}
		return this;
	}

	/**
	 * Compares the two models and returns a list of changes which have to be made to make second passed model equal to
	 * the first one. These change operations have to be executed to the second passed model.
	 *
	 * @param model The model which contains the valid meta information.
	 * @param copy  The model which should be equal to the first passed model.
	 * @return A list of change operations which have to be executed on the copy to make it equal to the model.
	 *
	 * @changed OLI 11.12.2015 - Added.
	 */
	public AbstractChangeOperation[] compare(MetaDataModel model, MetaDataModel copy) {
		ensure(copy != null, "copy cannot be null.");
		ensure(model != null, "model cannot be null.");
		List<AbstractChangeOperation> r = new SortedVector<AbstractChangeOperation>();
		this.compareSequences(model, copy, r);
		this.compareTables(model, copy, r);
		for (AddAdditionalScript aad : this.additionalScripts) {
			r.add(aad);
		}
		return r.toArray(new AbstractChangeOperation[0]);
	}

	public void compareSequences(MetaDataModel model, MetaDataModel copy, List<AbstractChangeOperation> r) {
		for (MetaDataSequence mds : model.getSequences()) {
			MetaDataSequence mdsc = copy.getSequence(mds.getName());
			if (mdsc == null) {
				r.add(new CreateSequence(mds));
			} else {
				if (!mds.equals(mdsc)) {
					r.add(new AlterSequence(mds));
				}
			}
		}
		for (MetaDataSequence mds : copy.getSequences()) {
			MetaDataSequence mdsm = model.getSequence(mds.getName());
			if (mdsm == null) {
				r.add(new DropSequence(mds));
			}
		}
	}

	public void compareTables(MetaDataModel model, MetaDataModel copy, List<AbstractChangeOperation> r) {
		for (MetaDataTable mdt : model.getTables()) {
			if (mdt.isExternalTable()) {
				continue;
			}
			MetaDataTable mdtc = copy.getTable(mdt.getName());
			if (mdtc == null) {
				r.add(new CreateTable(mdt));
				for (MetaDataColumn mdc : mdt.getColumns()) {
					this.addColumnConstraints(mdt, mdc, r, ScriptSectionType.ADD_CONSTRAINTS_FOR_NEW_TABLES);
				}
				this.addUniqueConstraints(mdt, r);
				this.addIndices(mdt, r);
			} else {
				for (MetaDataColumn mdc : mdt.getColumns()) {
					MetaDataColumn mdcc = mdtc.getColumn(mdc.getName());
					if (mdcc == null) {
						this.addColumn(mdt, mdc, r);
					} else {
						this.compareColumns(mdt, mdc, mdcc, r);
					}
				}
				this.compareUniqueConstraints(mdt, mdtc, r);
				this.compareIndices(mdt, mdtc, r);
				this.comparePrimaryKeys(mdt, mdtc, r);
				this.compareForeignKeyConstraintsComplex(mdt, mdtc, r);
			}
		}
		for (MetaDataTable mdt : copy.getTables()) {
			MetaDataTable mdtm = model.getTable(mdt.getName());
			if (mdtm == null) {
				r.add(new DropTable(mdt));
			} else {
				for (MetaDataColumn mdc : mdt.getColumns()) {
					if (mdc.isDeprecated()) {
						continue;
					}
					MetaDataColumn mdcm = mdtm.getColumn(mdc.getName());
					if (mdcm == null) {
						r.add(new DropColumn(mdt, mdc));
					}
				}
			}
		}
	}

	private void addUniqueConstraints(MetaDataTable model, List<AbstractChangeOperation> r) {
		for (MetaDataUniqueConstraint uc : model.getUniqueConstraints()) {
			if (uc.getColumns().length > 0) {
				r.add(new AddComplexUniqueConstraint(uc));
			}
		}
	}

	private void addIndices(MetaDataTable model, List<AbstractChangeOperation> r) {
		for (MetaDataIndex i : model.getIndices()) {
			if (i.getColumnNames().length > 0) {
				r.add(new CreateIndex(i, ScriptSectionType.UPDATE_CONSTRAINTS));
			}
		}
	}

	private void addColumn(MetaDataTable table, MetaDataColumn column, List<AbstractChangeOperation> r) {
		if (table.isDeprecated() || column.isDeprecated()) {
			return;
		}
		r.add(new AddColumn(table, column));
		this.addColumnConstraints(table, column, r, ScriptSectionType.ADD_COLUMNS_AND_DROP_CONSTRAINTS, true);
	}

	private void addColumnConstraints(MetaDataTable table, MetaDataColumn column, List<AbstractChangeOperation> r,
			ScriptSectionType section) {
		this.addColumnConstraints(table, column, r, section, false);
	}

	private void addColumnConstraints(MetaDataTable table, MetaDataColumn column, List<AbstractChangeOperation> r,
			ScriptSectionType section, boolean noPrimaryKeys) {
		// if (columns.length == 1) {
		// MetaDataColumn column = columns[0];
		if (table.isDeprecated() || column.isDeprecated()) {
			return;
		}
		if (column.isNotNull()) {
			r.add(new AlterColumnAddConstraint(table, column, ColumnConstraintType.NOT_NULL,
					ScriptSectionType.UPDATE_CONSTRAINTS));
		}
		if (column.isPrimaryKey() && !noPrimaryKeys) {
			r.add(new AlterColumnAddConstraint(table, table.getPrimaryKeyMembers()[0], ColumnConstraintType.PRIMARY_KEY,
					ScriptSectionType.UPDATE_CONSTRAINTS));
		}
		// Unique is handled by a separate method below.
		if (column.getForeignKeyConstraints().length > 0) {
			for (MetaDataForeignKeyConstraint fkc : column.getForeignKeyConstraints()) {
				if (!fkc.getReferencedColumn().getTable().isExternalTable()) {
					r.add(new AddForeignKeyConstraint(column, fkc));
				}
			}
		}
		// {
	}

	private void compareColumns(MetaDataTable table, MetaDataColumn model, MetaDataColumn copy,
			List<AbstractChangeOperation> r) {
		if (table.isDeprecated() || model.isDeprecated()) {
			return;
		}
		this.checkColumnType(table, model, copy, r);
		this.checkDefaultValue(table, model, copy, r);
		this.checkColumnIndices(table, model, copy, r);
		this.checkColumnNotNullConstraint(table, model, copy, r);
		this.checkColumnUniqueConstraint(table, model, copy, r);
		this.checkForeignKeyConstraints(table, model, copy, r);
	}

	private void checkColumnType(MetaDataTable table, MetaDataColumn model, MetaDataColumn copy,
			List<AbstractChangeOperation> r) {
		if (model.getTypeName().equalsIgnoreCase("boolean")
				&& model.getTypeName().equalsIgnoreCase(copy.getTypeName())) {
			return;
		}
		if (!model.getSQLType(DBExecMode.POSTGRESQL).equals(copy.getSQLType(DBExecMode.POSTGRESQL))) {
			r.add(new AlterColumnDataType(table, model, ScriptSectionType.ALTER_COLUMNS));
		}
	}

	private void checkDefaultValue(MetaDataTable table, MetaDataColumn model, MetaDataColumn copy,
			List<AbstractChangeOperation> r) {
		if (!Utl.equals(model.getDefaultValue(), copy.getDefaultValue())) {
			if (model.getDefaultValue() == null) {
				r.add(new AlterColumnDropDefault(table, copy, ScriptSectionType.ALTER_COLUMNS));
			} else {
				r.add(new AlterColumnSetDefaultValue(table, model, ScriptSectionType.ALTER_COLUMNS));
			}
		}
	}

	private void checkColumnIndices(MetaDataTable table, MetaDataColumn model, MetaDataColumn copy,
			List<AbstractChangeOperation> r) {
		if (model.isIndexed() != copy.isIndexed()) {
			if (model.isIndexed()) {
				r.add(new CreateIndex(
						new MetaDataIndex(this.nameGenerator.getIndexName(table.getName(), model.getName()), table,
								model),
						ScriptSectionType.UPDATE_CONSTRAINTS));
			} else {
				r.add(new DropIndex(new MetaDataIndex(this.nameGenerator.getIndexName(table.getName(), model.getName()),
						table, copy), ScriptSectionType.ADD_COLUMNS_AND_DROP_CONSTRAINTS));
			}
		}
	}

	private void checkColumnNotNullConstraint(MetaDataTable table, MetaDataColumn model, MetaDataColumn copy,
			List<AbstractChangeOperation> r) {
		if (model.isNotNull() != copy.isNotNull()) {
			if (model.isNotNull()) {
				r.add(new AlterColumnAddConstraint(table, model, ColumnConstraintType.NOT_NULL,
						ScriptSectionType.UPDATE_CONSTRAINTS));
			} else {
				r.add(new AlterColumnDropConstraint(table, copy, ColumnConstraintType.NOT_NULL,
						ScriptSectionType.ADD_COLUMNS_AND_DROP_CONSTRAINTS));
			}
		}
	}

	private void checkColumnUniqueConstraint(MetaDataTable table, MetaDataColumn model, MetaDataColumn copy,
			List<AbstractChangeOperation> r) {
		if (model.isUnique() != copy.isUnique()) {
			if (model.isUnique()) {
				r.add(new AlterColumnAddConstraint(table, model, ColumnConstraintType.UNIQUE,
						ScriptSectionType.UPDATE_CONSTRAINTS));
			} else {
				r.add(new AlterColumnDropConstraint(table, copy, ColumnConstraintType.UNIQUE,
						ScriptSectionType.ADD_COLUMNS_AND_DROP_CONSTRAINTS));
			}
		}
	}

	private void checkForeignKeyConstraints(MetaDataTable table, MetaDataColumn model, MetaDataColumn copy,
			List<AbstractChangeOperation> r) {
		for (MetaDataForeignKeyConstraint fkc : model.getForeignKeyConstraints()) {
			MetaDataForeignKeyConstraint fkcc = copy.getForeignKeyConstraint(fkc.getReferencedColumn());
			if (fkcc != null) {
				if (!fkc.getName().equals(fkcc.getName())) {
					r.add(new DropForeignKeyConstraint(model, fkcc));
					if (!fkc.getReferencedColumn().getTable().isExternalTable()) {
						r.add(new AddForeignKeyConstraint(model, fkc));
					}
				}
			} else {
				if (!fkc.getReferencedColumn().getTable().isExternalTable()) {
					r.add(new AddForeignKeyConstraint(model, fkc));
				}
			}
		}
		for (MetaDataForeignKeyConstraint fkc : copy.getForeignKeyConstraints()) {
			MetaDataForeignKeyConstraint fkcc = model.getForeignKeyConstraint(fkc.getReferencedColumn());
			if (fkcc == null) {
				r.add(new DropForeignKeyConstraint(copy, fkc));
			}
		}
	}

	private void compareForeignKeyConstraintsComplex(MetaDataTable model, MetaDataTable copy,
			List<AbstractChangeOperation> r) {
		for (MetaDataComplexForeignKey cfk : model.getComplexForeignKeys()) {
			MetaDataComplexForeignKey cfkc = copy.getComplexForeignKey(cfk.getName());
			if (cfkc == null) {
				r.add(new AddMultipleForeignKeyConstraint(cfk));
			} else {
				if (!cfk.equals(cfkc)) {
					r.add(new DropMultipleForeignKeyConstraint(cfkc));
					r.add(new AddMultipleForeignKeyConstraint(cfk));
				}
			}
		}
		for (MetaDataComplexForeignKey cfk : copy.getComplexForeignKeys()) {
			MetaDataComplexForeignKey cfkc = model.getComplexForeignKey(cfk.getName());
			if (cfkc == null) {
				r.add(new DropMultipleForeignKeyConstraint(cfk));
			}
		}
	}

	private void compareUniqueConstraints(MetaDataTable model, MetaDataTable copy, List<AbstractChangeOperation> r) {
		for (MetaDataUniqueConstraint uc : model.getUniqueConstraints()) {
			if (uc.getColumns().length > 0) {
				MetaDataUniqueConstraint ucc = copy.getUniqueConstraint(uc.getName());
				if (ucc == null) {
					r.add(new AddComplexUniqueConstraint(uc));
				} else {
					if (!uc.equals(ucc)) {
						r.add(new DropComplexUniqueConstraint(ucc));
						r.add(new AddComplexUniqueConstraint(uc));
					}
				}
			}
		}
		for (MetaDataUniqueConstraint uc : copy.getUniqueConstraints()) {
			if (uc.getColumns().length > 0) {
				MetaDataUniqueConstraint ucc = model.getUniqueConstraint(uc.getName());
				if (ucc == null) {
					r.add(new DropComplexUniqueConstraint(uc));
				}
			}
		}
	}

	private void compareIndices(MetaDataTable model, MetaDataTable copy, List<AbstractChangeOperation> r) {
		for (MetaDataIndex i : model.getIndices()) {
			if (i.getColumnNames().length > 0) {
				MetaDataIndex ic = copy.getIndex(i.getName());
				if (ic == null) {
					r.add(new CreateIndex(i, ScriptSectionType.UPDATE_CONSTRAINTS));
				} else {
					if (!i.equals(ic)) {
						r.add(new DropIndex(ic, ScriptSectionType.ADD_COLUMNS_AND_DROP_CONSTRAINTS));
						r.add(new CreateIndex(i, ScriptSectionType.UPDATE_CONSTRAINTS));
					}
				}
			}
		}
		for (MetaDataIndex i : copy.getIndices()) {
			if (i.getColumnNames().length > 0) {
				MetaDataIndex ic = model.getIndex(i.getName());
				if (ic == null) {
					r.add(new DropIndex(i, ScriptSectionType.ADD_COLUMNS_AND_DROP_CONSTRAINTS));
				}
			}
		}
	}

	private void comparePrimaryKeys(MetaDataTable model, MetaDataTable copy, List<AbstractChangeOperation> r) {
		String pkm = this.getCommaSeparatedPKs(model);
		String pkc = this.getCommaSeparatedPKs(copy);
		if (!pkm.equals(pkc)) {
			r.add(new DropPrimaryKeyConstraint(copy));
			r.add(new AddPrimaryKeyConstraint(model));
		}
	}

	private String getCommaSeparatedPKs(MetaDataTable t) {
		return this.metaUtil.getCommaSeparated(this.metaUtil.getNames(t.getPrimaryKeyMembers()), false);
	}

}