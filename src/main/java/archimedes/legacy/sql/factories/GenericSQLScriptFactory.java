/*
 * GenericSQLScriptFactory.java
 *
 * 30.07.2013
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
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.legacy.model.TableMetaData;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.UniqueMetaData;
import archimedes.legacy.sql.AlterStatementGenerator;
import archimedes.legacy.sql.CreateStatementGenerator;
import archimedes.legacy.sql.SQLScriptFactory;
import corent.db.DBExecMode;

/**
 * An implementation of the SQL script factory which creates code depending from
 * the database mode.
 * 
 * @author ollie
 * 
 * @changed OLI 30.07.2013 - Added.
 */

public class GenericSQLScriptFactory extends AbstractSQLScriptFactory {

	SQLScriptFactory sqlFactory = null;

	/**
	 * Creates a new factory with the passed parameters.
	 * 
	 * @param quotes
	 *            The quote character used to quote object names in the script.
	 * @param dbMode
	 *            The database mode which the factory should produce code for.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	public GenericSQLScriptFactory(String quotes, DataModel model, DBExecMode dbMode) {
		super(quotes, model, dbMode);
		if (dbMode == DBExecMode.HSQL) {
			this.sqlFactory = new HSQLScriptFactory(quotes, model);
		} else if (dbMode == DBExecMode.MSSQL) {
			this.sqlFactory = new MSSQLScriptFactory(quotes, model);
		} else if (dbMode == DBExecMode.MYSQL) {
			this.sqlFactory = new MYSQLScriptFactory(quotes, model);
		} else if (dbMode == DBExecMode.POSTGRESQL) {
			this.sqlFactory = new PostgreSQLScriptFactory(quotes, model);
		} else {
			this.sqlFactory = new StandardSQLScriptFactory(quotes, model, null);
		}
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String alterTableAddColumnStatement(ColumnModel column, boolean useDomains, boolean notNull) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.alterTableAddColumnStatement(column, useDomains, notNull);
		}
		return AlterStatementGenerator.getAlterStatement(column, this.getDBMode(), useDomains, notNull, this
				.getQuotes());
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Override
	public String[] alterTableAddConstraintForComplexUniques(TableModel table, List<UniqueMetaData> uniques) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.alterTableAddConstraintForComplexUniques(table, uniques);
		}
		return new String[0];
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String alterTableAlterColumnSetDataTypeStatement(ColumnModel column, boolean useDomains) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.alterTableAlterColumnSetDataTypeStatement(column, useDomains);
		}
		return AlterStatementGenerator.getAlterDataType((TabellenspaltenModel) column, this.getDBMode(), this
				.getQuotes());
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String alterTableAddPrimaryKeyConstraint(TableModel table) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.alterTableAddPrimaryKeyConstraint(table);
		}
		return null;
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String alterTableAlterColumnSetDefaultStatement(ColumnModel column) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.alterTableAlterColumnSetDefaultStatement(column);
		}
		return AlterStatementGenerator.getAlterDataType((TabellenspaltenModel) column, this.getDBMode(), this
				.getQuotes());
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String alterTableDropColumn(TableModel table, ColumnMetaData column) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.alterTableDropColumn(table, column);
		}
		return null;
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Override
	public String[] alterTableDropConstraintForComplexUniques(DataModel model, List<UniqueMetaData> uniques) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.alterTableDropConstraintForComplexUniques(model, uniques);
		}
		return new String[0];
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String alterTableDropPrimaryKeyConstraint(TableModel table) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.alterTableDropPrimaryKeyConstraint(table);
		}
		return null;
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Override
	public String alterTableModifyConstraintNotNull(ColumnModel column) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.alterTableModifyConstraintNotNull(column);
		}
		return AlterStatementGenerator.getAlterNull((TabellenspaltenModel) column, this.getDBMode(), false, this
				.getQuotes());
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public String createDomainStatement(DomainModel domain) {
		if (this.getDBMode() == DBExecMode.MSSQL) {
			String stmt = "sp_add_type $DOMAINNAME, \"$DOMAINTYPE\", \"$INITIALVALUE\";";
			stmt = stmt.replace("$DOMAINNAME", domain.getName());
			stmt = stmt.replace("$DOMAINTYPE", domain.getType());
			stmt = stmt.replace("$INITIALVALUE", domain.getInitialValue());
			return stmt;
		} else if (this.sqlFactory != null) {
			return this.sqlFactory.createDomainStatement(domain);
		}
		return null;
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public String createSequenceStatement(SequenceModel s) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.createSequenceStatement(s);
		}
		return null;
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String createSimpleIndexStatement(ColumnModel column) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.createSimpleIndexStatement(column);
		}
		return null;
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String createTableStatement(TableModel table, boolean useDomains, boolean ignoreNotNull) {
		return CreateStatementGenerator.getCreateStatement(table, this.getDBMode(), useDomains, this.getQuotes(),
				ignoreNotNull);
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public String dropForeignKeyConstraint(ColumnModel column) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.dropForeignKeyConstraint(column);
		}
		return "";
	}

	/**
	 * @changed OLI 10.09.2013 - Added.
	 */
	@Override
	public String dropForeignKeyConstraint(String tableName, String constraintName) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.dropForeignKeyConstraint(tableName, constraintName);
		}
		return "";
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public String dropSequenceStatement(SequenceMetaData smd) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.dropSequenceStatement(smd);
		}
		return null;
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String dropSimpleIndexStatement(ColumnModel column) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.dropSimpleIndexStatement(column);
		}
		return null;
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public String dropTableStatement(TableMetaData tmd) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.dropTableStatement(tmd);
		}
		return "DROP TABLE " + (this.getDBMode() == DBExecMode.MYSQL ? "IF EXISTS " : "") + this.quote(tmd.name)
				+ (this.getDBMode() == DBExecMode.HSQL ? " IF EXISTS" : "") + ";";
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String getForeignKeyConstraintName(ColumnModel column) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.getForeignKeyConstraintName(column);
		}
		return "";
	}

	/**
	 * @changed OLI 17.01.2014 - Added.
	 */
	@Override
	public String getForeignKeyConstraintName(TableMetaData table, ColumnMetaData column) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.getForeignKeyConstraintName(table, column);
		}
		return "";
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String getUniqueConstraintName(ColumnModel column) {
		if (this.sqlFactory != null) {
			return this.sqlFactory.getUniqueConstraintName(column);
		}
		return "";
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public boolean isPrimaryKeyIndex(String constraintName) {
		return (((this.getDBMode() == DBExecMode.MYSQL) && constraintName.equals("primary"))
				|| this.sqlFactory.isPrimaryKeyIndex(constraintName) || ((this.getDBMode() == DBExecMode.HSQL) && constraintName
				.startsWith("sys_pk")));
	}

	/**
	 * @changed OLI 28.08.2014 - Added.
	 */
	@Override
	public String setModelVersionStatement(String modelVersion, String schemeName) {
		if ((modelVersion == null) || modelVersion.isEmpty()) {
			return null;
		}
		if ((schemeName == null) || schemeName.isEmpty()) {
			return null;
		}
		if (this.sqlFactory != null) {
			return this.sqlFactory.setModelVersionStatement(modelVersion, schemeName);
		}
		throw new UnsupportedOperationException("set model version is not supported for DBMS: " + this.getDBMode());
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public String setSchemaStatement(String schemaName) {
		if ((schemaName == null) || schemaName.isEmpty()) {
			return null;
		}
		if (this.sqlFactory != null) {
			return this.sqlFactory.setSchemaStatement(schemaName);
		}
		throw new UnsupportedOperationException("change schema statement generation not " + "supported for DBMS: "
				+ this.getDBMode());
	}

}