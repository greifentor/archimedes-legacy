/*
 * DataModelToMetaDataConverter.java
 *
 * 10.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import java.util.List;
import java.util.Vector;

import archimedes.grammar.cfk.ComplexForeignKeyFactory;
import archimedes.grammar.cfk.ComplexForeignKeyModel;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.IndexMetaData;
import archimedes.legacy.model.SequenceModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.util.NameGenerator;
import corent.db.DBExecMode;
import corent.db.DBType;
import corentx.util.SortedVector;
import corentx.util.Str;
import gengen.metadata.AttributeMetaData;
import logging.Logger;

/**
 * This class converts information from the data model to the meta data format.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 10.12.2015 - Added.
 */

public class DataModelToMetaDataConverter {

	private static final Logger log = Logger.getLogger(DataModelToMetaDataConverter.class);

	private ComplexForeignKeyFactory complexForeignKeyfactory = new ComplexForeignKeyFactory();
	private MetaDataComplexForeignKeyFactory metaDataComplexForeignKeyFactory = new MetaDataComplexForeignKeyFactory();
	private NameGenerator nameGenerator = null;

	/**
	 * Creates a new data model to meta data converter which the passed parameters.
	 *
	 * @param dbMode The type of the DBMS which the meta data model is to create
	 *               for.
	 *
	 * @changed OLI 22.12.2015 - Added.
	 */
	public DataModelToMetaDataConverter(DBExecMode dbMode) {
		super();
		this.nameGenerator = new NameGenerator(dbMode);
	}

	/**
	 * Adds the complex indices of the data model to the meta data.
	 * 
	 * @param model          The meta data model which the table information are to
	 *                       add.
	 * @param complexIndices The complex index structures of the data model.
	 *
	 * @changed OLI 10.12.2015 - Added.
	 */
	public void addComplexIndices(MetaDataModel model, IndexMetaData[] complexIndices) {
		for (IndexMetaData i : complexIndices) {
			MetaDataTable mdt = this.getTable(model, i);
			List<MetaDataColumn> cols = new Vector<MetaDataColumn>();
			for (AttributeMetaData c : i.getColumns()) {
				cols.add(mdt.getColumn(c.getName()));
			}
			MetaDataIndex mdi = new MetaDataIndex(i.getName(), mdt, cols.toArray(new MetaDataColumn[0]));
			mdt.addIndex(mdi);
		}
	}

	private MetaDataTable getTable(MetaDataModel model, IndexMetaData index) {
		return this.getTable(model, index.getTable().getName());
	}

	/**
	 * Adds the sequences of the data model to the meta data.
	 * 
	 * @param model     The meta data model which the table information are to add.
	 * @param sequences The sequences of the data model.
	 *
	 * @changed OLI 11.12.2015 - Added.
	 */
	public void addSequences(MetaDataModel model, SequenceModel[] sequences) {
		for (SequenceModel s : sequences) {
			model.addSequence(new MetaDataSequence(s.getName(), s.getStartValue(), s.getIncrement()));
		}
	}

	/**
	 * Changes the table information of the data model to those of the meta data
	 * model and adds the table meta data to the meta data model.
	 *
	 * @param model  The meta data model which the table information are to add.
	 * @param tables The table models whose information are to convert to meta data.
	 *
	 * @changed OLI 10.12.2015 - Added.
	 */
	public void addTables(MetaDataModel model, TableModel[] tables) {
		for (TableModel t : tables) {
			MetaDataTable mdt = new MetaDataTable(t.getName(), t.isExternalTable());
			for (ColumnModel c : t.getColumns()) {
				if (c.isTransient()) {
					continue;
				}
				DomainModel d = c.getDomain();
				MetaDataColumn mdc = new MetaDataColumn(mdt, c.getName(), d.getName(), DBType.Convert(d.getDataType()),
						d.getLength(), d.getDecimalPlace(), c.isPrimaryKey(), c.isNotNull(), c.getDefaultValue(),
						c.isDeprecated());
				mdc.setUnique(c.isUnique());
				mdc.setIndexed(c.hasIndex());
				mdt.addColumn(mdc);
				if (c.isIndexSearchMember()) {
					mdt.addIndex(
							new MetaDataIndex(this.nameGenerator.getIndexName(t.getName(), c.getName()), mdt, mdc));
				}
			}
			model.addTable(mdt);
		}
		for (TableModel t : tables) {
			MetaDataTable mdt = this.getTable(model, t);
			for (ColumnModel c : t.getColumns()) {
				MetaDataColumn mdc = this.getColumn(mdt, c);
				if ((c.getReferencedColumn() != null) && !c.isSuppressForeignKeyConstraint()) {
					MetaDataTable rmdt = this.getTable(model, c.getReferencedTable());
					MetaDataColumn rmdc = this.getColumn(rmdt, c.getReferencedColumn());
					mdc.addForeignKeyConstraint(new MetaDataForeignKeyConstraint(
							this.nameGenerator.getForeignKeyName(t.getName(), c.getName()), rmdc));
				}
			}
		}
		for (TableModel t : tables) {
			MetaDataTable mdt = this.getTable(model, t);
			if ((t.getComplexForeignKeyDefinition() != null) && (t.getComplexForeignKeyDefinition().length() > 0)) {
				this.addComplexForeignKeys(model, mdt, t);
			}
		}
		for (TableModel t : tables) {
			MetaDataTable mdt = this.getTable(model, t);
			if ((t.getComplexUniqueSpecification() != null) && (t.getComplexUniqueSpecification().length() > 0)) {
				this.addComplexUniques(mdt, t.getComplexUniqueSpecification());
			}
		}
	}

	private void addComplexForeignKeys(MetaDataModel model, MetaDataTable mdt, TableModel t) {
		for (ComplexForeignKeyModel cfk : this.complexForeignKeyfactory.create(t)) {
			String fkName = this.nameGenerator.getForeignKeyName(cfk.getTableName(), cfk.getColumnNames());
			mdt.addComplexForeignKey(this.metaDataComplexForeignKeyFactory.create(fkName,
					this.getTable(model, cfk.getTableName()), this.getTable(model, cfk.getTargetTableName()), cfk));
		}
	}

	private void addComplexUniques(MetaDataTable mdt, String uniqueSpecification) {
		if (uniqueSpecification.contains("?")) {
			for (String us : Str.splitToList(uniqueSpecification, "|")) {
				MetaDataColumn nullable = null;
				String[] columnNames = this.getColumnNames(us);
				List<MetaDataColumn> columns = new SortedVector<MetaDataColumn>();
				for (String cn : columnNames) {
					boolean n = cn.startsWith("?");
					cn = n ? (cn.length() > 0 ? cn.substring(1) : "") : cn;
					MetaDataColumn c = mdt.getColumn(cn);
					if (n) {
						nullable = c;
					}
					columns.add(c);
				}
				mdt.addUniqueConstraint(new MetaDataUniqueWithNullableConstraint(mdt.getName() + "_unique_index", mdt,
						nullable, columns.toArray(new MetaDataColumn[0])));
				columns.remove(nullable);
				mdt.addUniqueConstraint(
						new MetaDataUniqueWithNullableConstraint(mdt.getName() + "_unique_index_nullable", mdt,
								nullable, columns.toArray(new MetaDataColumn[0])));
			}
		} else {
			for (String us : Str.splitToList(uniqueSpecification, "|")) {
				String[] columnNames = this.getColumnNames(us);
				List<MetaDataColumn> columns = new SortedVector<MetaDataColumn>();
				for (String cn : columnNames) {
					cn = cn.startsWith("?") ? (cn.length() > 0 ? cn.substring(1) : "") : cn;
					columns.add(mdt.getColumn(cn));
				}
				mdt.addUniqueConstraint(
						new MetaDataUniqueConstraint(this.nameGenerator.getUniqueName(mdt.getName(), columnNames), mdt,
								columns.toArray(new MetaDataColumn[0])));
				log.info(this.nameGenerator.getUniqueName(mdt.getName(), columnNames));
			}
		}
	}

	private String[] getColumnNames(String s) {
		List<String> a = Str.splitToList(s, "&");
		List<String> l = new SortedVector<String>();
		for (String h : a) {
			l.add(h.trim());
		}
		return l.toArray(new String[0]);
	}

	private MetaDataTable getTable(MetaDataModel model, TableModel table) {
		return this.getTable(model, table.getName());
	}

	private MetaDataTable getTable(MetaDataModel model, String tableName) {
		return model.getTable(tableName);
	}

	private MetaDataColumn getColumn(MetaDataTable table, ColumnModel column) {
		return table.getColumn(column.getName());
	}

}