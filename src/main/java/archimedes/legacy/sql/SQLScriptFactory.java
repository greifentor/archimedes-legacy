/*
 * SQLScriptFactory.java
 *
 * 30.07.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import java.util.List;

import archimedes.legacy.metadata.SequenceMetaData;
import archimedes.legacy.model.ColumnMetaData;
import archimedes.legacy.model.TableMetaData;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.SequenceModel;
import archimedes.model.TableModel;
import archimedes.model.UniqueMetaData;
import corent.db.DBExecMode;

/**
 * This interface describes a class which is able to be used for SQL script
 * generation by the Archimedes application.
 * <P>
 * It allows to specialize generation of specific scripts for the different
 * database modes.
 * 
 * @author ollie
 * 
 * @changed OLI 30.07.2013 - Added.
 */

public interface SQLScriptFactory {

	/**
	 * Creates an alter table add column statement with the passed parameters.
	 * 
	 * @param column
	 *            The column which is to add.
	 * @param useDomains
	 *            Set this flag is the data scheme uses domains.
	 * @param notNull
	 *            Set this flag if the not null constraints should be included.
	 * @return An alter table add column statement for the passed parameters.
	 * 
	 * @changed OLI 01.08.2013 - Added.
	 */
	abstract public String alterTableAddColumnStatement(ColumnModel column, boolean useDomains, boolean notNull);

	/**
	 * Creates an alter table statement to add a complex unique statement.
	 * 
	 * @param table
	 *            The table whose complex unique constraint is to create.
	 * @param uniques
	 *            The unique meta data of the data scheme.
	 * @return An array of statements which add the missing constraints for the
	 *         table.
	 * 
	 * @changed OLI 05.08.2013 - Added.
	 */
	abstract public String[] alterTableAddConstraintForComplexUniques(TableModel table, List<UniqueMetaData> uniques);

	/**
	 * Creates a statement which adds a primary key for the passed table. It is
	 * useful to drop the primary key for the table before creating a new one.
	 * 
	 * @param table
	 *            The table whose primary key should be created.
	 * @return A statement which creates the primary key for the passed table.
	 * 
	 * @changed OLI 25.10.2013 - Added.
	 */
	abstract public String alterTableAddPrimaryKeyConstraint(TableModel table);

	/**
	 * Creates an alter table statement to add or drop a not null constraint for
	 * a column.
	 * 
	 * @param column
	 *            The column which the not null constraint is to add or drop.
	 * @return A statement which modifies the constraint for the column. The
	 *         modification is dependent to the state of the not null flag of
	 *         the column.
	 * 
	 * @changed OLI 05.08.2013 - Added.
	 */
	abstract public String alterTableModifyConstraintNotNull(ColumnModel column);

	/**
	 * Creates an alter table alter column statement with the passed parameters
	 * to change the data type of the column.
	 * 
	 * @param column
	 *            The column whose type is to change.
	 * @param useDomains
	 *            Set this flag is the data scheme uses domains.
	 * @return An alter table alter column statement for the passed parameters
	 *         to change the data type of the column.
	 * 
	 * @changed OLI 01.08.2013 - Added.
	 */
	abstract public String alterTableAlterColumnSetDataTypeStatement(ColumnModel column, boolean useDomains);

	/**
	 * Creates an alter table alter column statement with the passed parameters
	 * to change the default of the column.
	 * 
	 * @param column
	 *            The column whose default is to change.
	 * @return An alter table alter column statement for the passed parameters
	 *         to change the default of the column.
	 * 
	 * @changed OLI 01.08.2013 - Added.
	 */
	abstract public String alterTableAlterColumnSetDefaultStatement(ColumnModel column);

	/**
	 * Creates a statement which drops the passed column from the passed table.
	 * 
	 * @param table
	 *            The table whose column should be removed.
	 * @param column
	 *            The column meta data of the column which have to be dropped.
	 * @return A statement which drops the passed column from the passed table.
	 * 
	 * @changed OLI 25.10.2013 - Added.
	 */
	abstract public String alterTableDropColumn(TableModel table, ColumnMetaData column);

	/**
	 * Creates an array of alter table drop statements to clean up the complex
	 * unique constraints.
	 * 
	 * @param model
	 *            The data model which the statements are to create for.
	 * @param uniques
	 *            The unique constraint meta data.
	 * @return An array of alter table drop statements to clean up the complex
	 *         unique constraints.
	 * 
	 * @changed OLI 05.08.2013 - Added.
	 */
	abstract public String[] alterTableDropConstraintForComplexUniques(DataModel model, List<UniqueMetaData> uniques);

	/**
	 * Creates a statement which drops the primary key from the passed table.
	 * 
	 * @param table
	 *            The table whose primary key should be removed.
	 * @return A statement which drops the primary key from the passed table.
	 * 
	 * @changed OLI 25.10.2013 - Added.
	 */
	abstract public String alterTableDropPrimaryKeyConstraint(TableModel table);

	/**
	 * Creates a statement to create a domain with the passed data.
	 * 
	 * @param domain
	 *            The data of the domain which is to create.
	 * @return A statement to create the passed domain.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	abstract public String createDomainStatement(DomainModel domain);

	/**
	 * Creates a statement to create a simple index on the passed column.
	 * 
	 * @param column
	 *            The column which the create index statement is to return for.
	 * @return A statement to create a simple index on the passed column.
	 * 
	 * @changed OLI 25.10.2013 - Added.
	 */
	abstract public String createSimpleIndexStatement(ColumnModel column);

	/**
	 * Creates statements to create a table
	 * 
	 * @param table
	 *            The table model of the table which the code is to create for.
	 * @param useDomains
	 *            Set the flag if domains are used in the database.
	 * @param ignoreNotNull
	 *            Set this flag if the not null constraint should not be
	 *            generated.
	 * @return A create statement for the passed sequence.
	 * 
	 * @changed OLI 01.08.2013 - Added.
	 */
	abstract public String createTableStatement(TableModel table, boolean useDomains, boolean ignoreNotNull);

	/**
	 * Creates statements to create a sequence.
	 * 
	 * @param sequence
	 *            The data of the sequence which is to create.
	 * @return A create statement for the passed sequence.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	abstract public String createSequenceStatement(SequenceModel s);

	/**
	 * Creates a statement to drop a foreign key constraint from the data
	 * scheme.
	 * 
	 * @param column
	 *            The column whose foreign key constraint is to drop.
	 * @return A statement to drop a foreign key constraint from the data
	 *         scheme.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	abstract public String dropForeignKeyConstraint(ColumnModel column);

	/**
	 * Creates a statement to drop a foreign key constraint from the data
	 * scheme.
	 * 
	 * @param tableName
	 *            The name of the table which the constraint is linked to.
	 * @param constraintName
	 *            The name of the constraint which is to drop.
	 * @return A statement to drop a foreign key constraint from the data
	 *         scheme.
	 * 
	 * @changed OLI 10.09.2013 - Added.
	 */
	abstract public String dropForeignKeyConstraint(String tableName, String constraintName);

	/**
	 * Creates statements to drop a sequence.
	 * 
	 * @param smd
	 *            The data of the sequence which is to drop.
	 * @return A drop statement for the passed sequence.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	abstract public String dropSequenceStatement(SequenceMetaData smd);

	/**
	 * Creates a statement to drop a simple index for the passed column.
	 * 
	 * @param column
	 *            The column which the drop index statement is to return for.
	 * @return A statement to drop a simple index from the passed column.
	 * 
	 * @changed OLI 25.10.2013 - Added.
	 */
	abstract public String dropSimpleIndexStatement(ColumnModel column);

	/**
	 * Creates a drop statement for a single table.
	 * 
	 * @param tmd
	 *            The model of the table which is to drop.
	 * @return A drop statement for a single table.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	abstract public String dropTableStatement(TableMetaData tmd);

	/**
	 * Returns the data mode which SQL script is generated for.
	 * 
	 * @return The data mode which SQL script is generated for.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	abstract public DBExecMode getDBMode();

	/**
	 * Returns the name for a foreign key constraint for the passed column.
	 * 
	 * @param column
	 *            The column which the foreign key constraint name is to create
	 *            for.
	 * @return The foreign key constraint name for the passed column.
	 * 
	 * @changed OLI 01.08.2013 - Added.
	 */
	abstract public String getForeignKeyConstraintName(ColumnModel column);

	/**
	 * Returns the name for a foreign key constraint for the passed column.
	 * 
	 * @param table
	 *            The table which the foreign key constraint name is to create
	 *            for.
	 * @param column
	 *            The column which the foreign key constraint name is to create
	 *            for.
	 * @return The foreign key constraint name for the passed column.
	 * 
	 * @changed OLI 17.01.2014 - Added.
	 */
	abstract public String getForeignKeyConstraintName(TableMetaData table, ColumnMetaData column);

	/**
	 * Returns the name for a unique constraint for the passed column.
	 * 
	 * @param column
	 *            The column which the unique constraint name is to create for.
	 * @return The unique constraint name for the passed column.
	 * 
	 * @changed OLI 01.08.2013 - Added.
	 */
	abstract public String getUniqueConstraintName(ColumnModel column);

	/**
	 * Checks if the passed constraint is a primary key index.
	 * 
	 * @param constraintName
	 *            The name of the constraint to check.
	 * @return <CODE>true</CODE> if the constraint is a primary key index.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	abstract public boolean isPrimaryKeyIndex(String constraintName);

	/**
	 * Creates a statement to set the model version to the data scheme.
	 * 
	 * @param modelVersion
	 *            The model version to set in the data scheme.
	 * @param schemeName
	 *            The name of the scheme which the version is to set to.
	 * @return A statement to set the model version to the data scheme.
	 * 
	 * @changed OLI 28.08.2014 - Added.
	 */
	abstract public String setModelVersionStatement(String modelVersion, String schemeName);

	/**
	 * Creates a schema change statement which is set to the begin of the SQL
	 * script.
	 * 
	 * @param schemaName
	 *            The name of the schema which is to switch to.
	 * @return A schema change statement which is set to the begin of the SQL
	 *         script.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	abstract public String setSchemaStatement(String schemaName);

}