/*
 * StandardSQLScriptFactory.java
 *
 * 25.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql.factories;

import java.util.List;

import archimedes.legacy.metadata.SequenceMetaData;
import archimedes.legacy.model.ColumnMetaData;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.SequenceModel;
import archimedes.legacy.model.TableMetaData;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.UniqueMetaData;
import archimedes.legacy.sql.CreateStatementGenerator;
import archimedes.legacy.sql.SQLGeneratorUtil;
import corent.db.DBExecMode;

/**
 * A SQL script factory implementation for standard SQL.
 * 
 * @author ollie
 * 
 * @changed OLI 25.10.2013 - Added.
 */

public class StandardSQLScriptFactory extends AbstractSQLScriptFactory {

	protected ConstraintNameGenerator constraintNameGenerator = null;

	/**
	 * Creates a new factory with the passed parameters.
	 * 
	 * @param quotes
	 *            The quote character used to quote object names in the script.
	 * @param model
	 *            The database model which the factory should produce code for.
	 * @param constraintNameGenerator
	 *            The constraint name generator for the factory.
	 * 
	 * @changed OLI 25.10.2013 - Added.
	 */
	public StandardSQLScriptFactory(String quotes, DataModel model, ConstraintNameGenerator constraintNameGenerator) {
		super(quotes, model, DBExecMode.STANDARDSQL);
		this.constraintNameGenerator = constraintNameGenerator;
	}

	/**
	 * Creates a new factory with the passed parameters.
	 * 
	 * @param quotes
	 *            The quote character used to quote object names in the script.
	 * @param model
	 *            The database model which the factory should produce code for.
	 * @param constraintNameGenerator
	 *            The constraint name generator for the factory.
	 * @param dbMode
	 *            A DBMS mode if the standard SQL factory is used as base for a
	 *            specific implementation.
	 * 
	 * @changed OLI 25.10.2013 - Added.
	 */
	protected StandardSQLScriptFactory(String quotes, DataModel model, ConstraintNameGenerator constraintNameGenerator,
			DBExecMode dbMode) {
		super(quotes, model, dbMode);
		this.constraintNameGenerator = constraintNameGenerator;
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String alterTableAddColumnStatement(ColumnModel column, boolean useDomains, boolean notNull) {
		String s = "ALTER TABLE " + this.quote(column.getTable().getName()) + " ADD COLUMN "
				+ this.quote(column.getName()) + " ";
		String type = SQLGeneratorUtil.getTypeString(useDomains, column.getDomain(), this.getDBMode());
		if (!useDomains) {
			type = type.toUpperCase();
		}
		s += type;
		if (notNull) {
			s += " NOT NULL";
		}
		s += ";";
		return s;
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String[] alterTableAddConstraintForComplexUniques(TableModel table, List<UniqueMetaData> uniques) {
		if (table.getComplexUniqueSpecification().length() > 0) {
			return new String[] { "/* WARNING (" + table.getName() + "): Complex unique "
					+ "constraints cannot be created for " + this.getDBMode() + "! */" };
		}
		return new String[0];
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String alterTableAddPrimaryKeyConstraint(TableModel table) {
		String pks = "";
		for (ColumnModel column : table.getColumns()) {
			if (column.isPrimaryKey()) {
				if (pks.length() > 0) {
					pks += ", ";
				}
				pks += this.quote(column.getName());
			}
		}
		if (pks.length() > 0) {
			return "ALTER TABLE " + this.quote(table.getName()) + " ADD PRIMARY KEY(" + pks + ");";
		}
		return "";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String alterTableAlterColumnSetDataTypeStatement(ColumnModel column, boolean useDomains) {
		String s = "ALTER TABLE " + this.quote(column.getTable().getName()) + " ALTER COLUMN "
				+ this.quote(column.getName()) + " ";
		String type = SQLGeneratorUtil.getTypeString(useDomains, column.getDomain(), this.getDBMode());
		if (!useDomains) {
			type = type.toUpperCase();
		}
		s += type + ";";
		return s;
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String alterTableAlterColumnSetDefaultStatement(ColumnModel column) {
		return "/* WARNING (" + column.getFullName() + "): Default constraints cannot be " + "created for standard "
				+ this.getDBMode() + "! */";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String alterTableDropColumn(TableModel table, ColumnMetaData column) {
		return "ALTER TABLE " + this.quote(table.getName()) + " DROP COLUMN " + this.quote(column.name) + ";";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String[] alterTableDropConstraintForComplexUniques(DataModel model, List<UniqueMetaData> uniques) {
		if (uniques.size() > 0) {
			return new String[] { "/* WARNING: Complex constraints cannot be dropped for " + this.getDBMode() + "! */" };
		}
		return new String[0];
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String alterTableDropPrimaryKeyConstraint(TableModel table) {
		return "ALTER TABLE " + this.quote(table.getName()) + " DROP PRIMARY KEY;";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String alterTableModifyConstraintNotNull(ColumnModel column) {
		return "/* WARNING (" + column.getFullName() + "): Statement to modify not null cannot " + "be created for "
				+ this.getDBMode() + "! */";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String createDomainStatement(DomainModel domain) {
		return "/* WARNING (" + domain.getName() + "): Domain cannot be created for " + this.getDBMode() + "! */";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String createSequenceStatement(SequenceModel s) {
		return "/* WARNING (" + s.getName() + "): Sequence cannot be created for " + this.getDBMode() + "! */";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String createSimpleIndexStatement(ColumnModel column) {
		String indexName = this.createSimpleIndexName(column);
		return "CREATE INDEX " + indexName + " ON " + this.quote(column.getTable().getName()) + " ("
				+ this.quote(column.getName()) + ");";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String createTableStatement(TableModel table, boolean useDomains, boolean ignoreNotNull) {
		return CreateStatementGenerator.getCreateStatement(table, this.getDBMode(), useDomains, this.getQuotes(),
				ignoreNotNull);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String dropForeignKeyConstraint(ColumnModel column) {
		return "/* WARNING (" + column.getFullName() + "): Foreign key contraint removing "
				+ "statement cannot be created for " + this.getDBMode() + "! */";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String dropForeignKeyConstraint(String tableName, String constraintName) {
		return "/* WARNING (" + tableName + "." + constraintName + "): Foreign key contraint "
				+ "removing statement cannot be created for " + this.getDBMode() + "! */";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String dropSequenceStatement(SequenceMetaData smd) {
		return "/* WARNING (" + smd.getName() + "): Sequence dropping statement cannot be " + "created for "
				+ this.getDBMode() + "! */";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String dropSimpleIndexStatement(ColumnModel column) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String dropTableStatement(TableMetaData tmd) {
		return "DROP TABLE " + this.quote(tmd.name) + ";";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String getForeignKeyConstraintName(ColumnModel column) {
		return null;
	}

	/**
	 * @changed OLI 17.01.2014 - Added.
	 */
	@Override
	public String getForeignKeyConstraintName(TableMetaData table, ColumnMetaData column) {
		return null;
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String getUniqueConstraintName(ColumnModel column) {
		return null;
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public boolean isPrimaryKeyIndex(String constraintName) {
		return true;
		/*
		 * throw newUnsupportedOperationException(
		 * "primary key indices are not a standard SQL " + "feature.");
		 */
	}

	/**
	 * @changed OLI 28.08.2014 - Added.
	 */
	@Override
	public String setModelVersionStatement(String modelVersion, String schemeName) {
		return "/* WARNING (" + modelVersion + "): Statement to set model version cannot be " + "created for "
				+ this.getDBMode() + "! */";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String setSchemaStatement(String schemaName) {
		return "/* WARNING (" + schemaName + "): Statement to change to schema cannot be " + "created for "
				+ this.getDBMode() + "! */";
	}

}