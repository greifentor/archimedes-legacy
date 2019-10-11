/*
 * PostgreSQLScriptFactory.java
 *
 * 30.07.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql.factories;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import archimedes.legacy.metadata.SequenceMetaData;
import archimedes.legacy.model.ColumnMetaData;
import archimedes.legacy.model.TableMetaData;
import archimedes.legacy.sql.SQLGeneratorUtil;
import archimedes.legacy.util.UniqueFormulaUtil;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.SequenceModel;
import archimedes.model.TableModel;
import archimedes.model.UniqueMetaData;
import archimedes.util.NameGenerator;
import corent.db.DBExecMode;
import corentx.util.SortedVector;

/**
 * An implementation of the SQL script factory interface which creates SQL
 * statements for PostgreSQL.
 * 
 * @author ollie
 * 
 * @changed OLI 30.07.2013 - Added.
 */

public class PostgreSQLScriptFactory extends StandardSQLScriptFactory {

	private static final String COMPLEX_UNIQUE_PREFIX = "AX_ComplexUnique";

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
	public PostgreSQLScriptFactory(String quotes, DataModel model) {
		super(quotes, model, new PostgreSQLConstraintNameGenerator(), DBExecMode.POSTGRESQL);
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String alterTableAddColumnStatement(ColumnModel column, boolean useDomains, boolean notNull) {
		TableModel tm = column.getTable();
		String s = "ALTER TABLE " + this.quote(tm.getName());
		s += " ADD " + this.quote(column.getName()) + " ";
		s += SQLGeneratorUtil.getTypeString(useDomains, column.getDomain(), this.getDBMode());
		s += (notNull && column.isNotNull() ? " NOT NULL" : "");
		if (column.getDefaultValue() != null) {
			s += " DEFAULT " + SQLGeneratorUtil.getDefaultValue(column, this.getDBMode());
		}
		s += ";";
		return s;
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Override
	public String[] alterTableAddConstraintForComplexUniques(TableModel table, List<UniqueMetaData> uniques) {
		List<String> l = new LinkedList<String>();
		for (String[] c : this.getUniques(table.getComplexUniqueSpecification())) {
			String name = this.getUniqueConstraintName(table.getName(), c);
			if (!this.isUniqueConstrainExists(name, uniques)) {
				String s = "ALTER TABLE " + this.quote(table.getName()) + " ADD CONSTRAINT " + this.quote(name)
						+ " UNIQUE (";
				for (int i = 0; i < c.length; i++) {
					if (i > 0) {
						s += ", ";
					}
					s += this.quote(c[i]);
				}
				s += ");";
				l.add(s);
			}
		}
		return l.toArray(new String[0]);
	}

	private List<String[]> getUniques(String s) {
		List<String[]> l = new LinkedList<String[]>();
		StringTokenizer st = new StringTokenizer(s, "|");
		while (st.hasMoreTokens()) {
			List<String> cols = new SortedVector<String>();
			StringTokenizer stc = new StringTokenizer(st.nextToken().trim(), "&");
			while (stc.hasMoreTokens()) {
				cols.add(stc.nextToken().trim());
			}
			l.add(cols.toArray(new String[0]));
		}
		return l;
	}

	private String getUniqueConstraintName(String tableName, String[] c) {
		String s = COMPLEX_UNIQUE_PREFIX + "_" + tableName;
		for (String cn : c) {
			s += "_" + cn;
		}
		return (s.length() > 63 ? s.substring(0, 63) : s);
	}

	private boolean isUniqueConstrainExists(String name, List<UniqueMetaData> uniques) {
		for (UniqueMetaData umd : uniques) {
			if (umd.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String alterTableAlterColumnSetDataTypeStatement(ColumnModel column, boolean useDomains) {
		return "ALTER TABLE ".concat(this.quote(column.getTable().getName())).concat(" ALTER COLUMN ").concat(
				this.quote(column.getName())).concat(" SET DATA TYPE ").concat(
				SQLGeneratorUtil.getTypeString(useDomains, column.getDomain(), this.getDBMode())).concat(";");
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String alterTableAlterColumnSetDefaultStatement(ColumnModel column) {
		String s = "ALTER TABLE " + this.quote(column.getTable().getName()) + " ALTER COLUMN "
				+ this.quote(column.getName());
		String defaultValue = SQLGeneratorUtil.getDefaultValue(column, this.getDBMode());
		if (defaultValue.equalsIgnoreCase("NULL")) {
			s += " DROP DEFAULT";
		} else {
			s += " SET DEFAULT " + defaultValue;
		}
		s += ";";
		return s;
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Override
	public String[] alterTableDropConstraintForComplexUniques(DataModel model, List<UniqueMetaData> uniques) {
		List<String> l = new LinkedList<String>();
		for (UniqueMetaData umd : uniques) {
			if (umd.getName().startsWith(COMPLEX_UNIQUE_PREFIX)) {
				String tableName = this.extractTableName(umd.getName());
				TableModel t = model.getTableByName(tableName);
				if (t != null) {
					if (!this.getComplexUniqueConstraintNames(t).contains(umd.getName())) {
						l.add("ALTER TABLE " + this.quote(t.getName()) + " DROP CONSTRAINT "
								+ this.quote(umd.getName()) + ";");
					}
				}
			}
		}
		return l.toArray(new String[0]);
	}

	private String extractTableName(String umdName) {
		String s = umdName.substring(COMPLEX_UNIQUE_PREFIX.length() + 1);
		return s.substring(0, s.indexOf("_"));
	}

	private List<String> getComplexUniqueConstraintNames(TableModel table) {
		List<String> l = new LinkedList<String>();
		for (String[] c : this.getUniques(table.getComplexUniqueSpecification())) {
			l.add(this.getUniqueConstraintName(table.getName(), c));
		}
		return l;
	}

	/**
	 * @changed OLI 17.01.2014 - Added.
	 */
	@Override
	public String alterTableDropPrimaryKeyConstraint(TableModel table) {
		return "ALTER TABLE " + this.quote(table.getName()) + " DROP CONSTRAINT "
				+ this.quote(this.constraintNameGenerator.createPrimaryKeyConstraintName(table)) + ";";
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Override
	public String alterTableModifyConstraintNotNull(ColumnModel column) {
		StringBuffer sb = new StringBuffer("ALTER TABLE ").append(this.quote(column.getTable().getName())).append(
				" ALTER COLUMN ").append(this.quote(column.getName()));
		if (!column.isNotNull()) {
			sb.append(" DROP");
		} else {
			sb.append(" SET");
		}
		sb.append(" NOT NULL;");
		return sb.toString();
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public String createDomainStatement(DomainModel domain) {
		return "CREATE DOMAIN " + this.quote(domain.getName()) + " AS " + domain.getType(this.getDBMode())
				+ " DEFAULT " + domain.getInitialValue() + ";";
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public String createSequenceStatement(SequenceModel s) {
		return "CREATE SEQUENCE " + this.quote(s.getName()) + " INCREMENT BY " + s.getIncrement() + " START WITH "
				+ s.getStartValue() + ";";
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String createTableStatement(TableModel table, boolean useDomains, boolean ignoreNotNull) {
		StringBuffer sb = new StringBuffer("CREATE TABLE ").append(this.quote(table.getName())).append(" (\n");
		StringBuffer uniques = new StringBuffer();
		ColumnModel[] columns = table.getColumns();
		for (ColumnModel c : columns) {
			sb.append("    ").append(this.quote(c.getName())).append(" ").append(
					SQLGeneratorUtil.getTypeString(useDomains, c.getDomain(), this.getDBMode()));
			if (c.getDefaultValue() != null) {
				sb.append(" DEFAULT ").append(SQLGeneratorUtil.getDefaultValue(c, this.getDBMode()));
			}
			if (!ignoreNotNull && (c.isNotNull() || c.isPrimaryKey())) {
				sb.append(" NOT NULL");
			}
			if (c.isUnique() && !c.isPrimaryKey()) {
				uniques.append(this.createUniqueConstraintForASingleColumn(c));
			}
			if (c != columns[columns.length - 1]) {
				sb.append(",\n");
			}
		}
		// sb.append(this.createPrimaryKeyConstraint(table));
		if (table.getAdditionalCreateConstraints().length() > 0) {
			sb.append(",\n    ").append(this.createAdditionalCreateConstraint(table.getAdditionalCreateConstraints()));
		}
		sb.append("\n);");
		sb.append(this.getPrimaryKeyConstraint(table));
		if (uniques.length() > 0) {
			sb.append(uniques);
		}
		// sb.append(this.createComplexUniqueConstraint(table));
		// sb.append(this.createForeignKeyConstraints(table));
		return sb.toString();
	}

	private String createComplexUniqueConstraint(TableModel table) {
		StringBuffer sb = new StringBuffer();
		String uniqueFormula = table.getComplexUniqueSpecification();
		if ((uniqueFormula != null) && !uniqueFormula.isEmpty()) {
			String[] uniqueColumnNames = new UniqueFormulaUtil(uniqueFormula).getFieldNames();
			if (uniqueColumnNames.length > 0) {
				String constraintName = this.constraintNameGenerator.createUniqueConstraintName(table.getName(),
						uniqueColumnNames)
						+ (table.getColumnByName(uniqueColumnNames[0]).isUnique() ? "1" : "");
				sb.append("\nALTER TABLE " + this.quote(table.getName()) + " ADD CONSTRAINT "
						+ this.quote(constraintName) + " UNIQUE (");
				for (int i = 0; i < uniqueColumnNames.length; i++) {
					if (i > 0) {
						sb.append(", ");
					}
					sb.append(this.quote(uniqueColumnNames[i]));
				}
				sb.append(");");
			}
		}
		return sb.toString();
	}

	private String createAdditionalCreateConstraint(String constraint) {
		constraint = constraint.replace("&", "AND");
		constraint = constraint.replace("IS_NOT_NULL", "IS NOT NULL");
		constraint = constraint.replace("IS_NULL", "IS NULL");
		constraint = constraint.replace("|", "OR");
		return constraint;
	}

	private String getPrimaryKeyConstraint(TableModel table) {
		ColumnModel[] pks = table.getPrimaryKeyColumns();
		String s = "";
		if (pks.length > 0) {
			s = "\nALTER TABLE " + this.quote(table.getName()) + " ADD CONSTRAINT "
					+ this.quote(this.constraintNameGenerator.createPrimaryKeyConstraintName(table)) + " PRIMARY KEY (";
			boolean setComma = false;
			for (ColumnModel pk : pks) {
				if (setComma) {
					s += ", ";
				}
				s += this.quote(pk.getName());
				setComma = true;
			}
			s += ");";
		}
		return s;
	}

	private String createUniqueConstraintForASingleColumn(ColumnModel c) {
		return "\nALTER TABLE "
				+ this.quote(c.getTable().getName())
				+ " ADD CONSTRAINT "
				+ this.quote(this.constraintNameGenerator.createUniqueConstraintName(c.getTable().getName(), c
						.getName())) + " UNIQUE (" + this.quote(c.getName()) + ");";
	}

	private String createForeignKeyConstraints(TableModel table) {
		String s = "";
		List<TableModel> referencedTables = new SortedVector<TableModel>();
		for (ColumnModel column : table.getColumns()) {
			if ((column.getReferencedTable() != null) && (!referencedTables.contains(column.getReferencedTable()))) {
				referencedTables.add(column.getReferencedTable());
			}
		}
		for (TableModel refTable : referencedTables) {
			s += "\nALTER TABLE " + this.quote(table.getName()) + " ADD FOREIGN KEY (";
			boolean setComma = false;
			for (ColumnModel column : table.getColumns()) {
				if (column.getReferencedTable() == refTable) {
					if (setComma) {
						s += ", ";
					}
					s += this.quote(column.getName());
					setComma = true;
				}
			}
			s += ") REFERENCES " + this.quote(refTable.getName()) + ";";
		}
		return s;
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public String dropForeignKeyConstraint(ColumnModel column) {
		return this.dropForeignKeyConstraint(column.getTable().getName(), this.getForeignKeyConstraintName(column));
	}

	/**
	 * @changed OLI 10.09.2013 - Added.
	 */
	@Override
	public String dropForeignKeyConstraint(String tableName, String constraintName) {
		return "ALTER TABLE " + this.quote(tableName) + " DROP CONSTRAINT " + this.quote(constraintName) + ";";
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public String dropSequenceStatement(SequenceMetaData smd) {
		return "DROP SEQUENCE IF EXISTS " + this.quote(smd.getName()) + ";";
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public String dropSimpleIndexStatement(ColumnModel column) {
		String indexName = this.createSimpleIndexName(column);
		return "DROP INDEX IF EXISTS " + indexName + ";";
	}

	/**
	 * @changed OLI 17.01.2014 - Added.
	 */
	@Override
	public String dropTableStatement(TableMetaData tmd) {
		return "DROP TABLE " + this.quote(tmd.name) + " CASCADE;";
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String getForeignKeyConstraintName(ColumnModel column) {
		return this.constraintNameGenerator.createForeignKeyConstraintName(column);
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String getForeignKeyConstraintName(TableMetaData table, ColumnMetaData column) {
		return new NameGenerator(DBExecMode.POSTGRESQL).getForeignKeyName(table.name, column.name);
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Override
	public String getUniqueConstraintName(ColumnModel column) {
		return this.constraintNameGenerator.createUniqueConstraintName(column.getTable().getName(), column.getName());
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public boolean isPrimaryKeyIndex(String constraintName) {
		return constraintName.endsWith("_pkey");
	}

	/**
	 * @changed OLI 28.08.2014 - Added.
	 */
	@Override
	public String setModelVersionStatement(String modelVersion, String schemeName) {
		return "COMMENT ON SCHEMA " + this.quote(schemeName) + " IS 'Version " + modelVersion + "';";
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public String setSchemaStatement(String schemaName) {
		return "SET search_path TO " + this.quote(schemaName) + ";";
	}

}