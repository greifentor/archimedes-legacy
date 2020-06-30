/*
 * DataModel.java
 *
 * 25.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.model;

import java.util.Vector;

import archimedes.connections.DatabaseConnectionProvider;
import archimedes.model.events.DataModelEvent;
import archimedes.model.events.DataModelListener;
import archimedes.model.gui.GUIDiagramModel;
import corent.dates.PDate;
import corent.db.JDBCDataSourceRecord;

/**
 * An interface which describes the methods of the data model (which covers the table models).
 *
 * @author O.Lieshoff
 *
 * @changed OLI 25.04.2013 - Added.
 */

public interface DataModel extends CommentOwner, ComplexIndexListProvider, DatabaseConnectionProvider,
		DiagramGUISupport, GeneratorSupport, GUIDiagramModel, HistoryOwner, ImportSupport, OptionListProvider,
		SQLGeneratorSupport, StereotypeListProvider, UdschebtiSupport, ViewProvider {

	/**
	 * Adds the passed data model listener.
	 *
	 * @param l The listener to add.
	 *
	 * @changed OLI 23.10.2013 - Added.
	 */
	abstract public void addDataModelListener(DataModelListener l);

	/**
	 * Adds the passed domain to the model.
	 *
	 * @param domain The domain to add.
	 * @throws IllegalArgumentException Passing a null pointer as domain.
	 *
	 * @changed OLI 26.04.2013 - Added.
	 */
	abstract public void addDomain(DomainModel domain) throws IllegalArgumentException;

	/**
	 * Adds the passed sequence to the model.
	 *
	 * @param sequence The sequence to add.
	 * @throws IllegalArgumentException Passing a null pointer as sequence.
	 *
	 * @changed OLI 23.04.2013 - Added.
	 */
	abstract public void addSequence(SequenceModel sequence) throws IllegalArgumentException;

	/**
	 * Adds the passed table to the model.
	 *
	 * @param table The table to add.
	 * @throws IllegalArgumentException Passing a null pointer as table.
	 *
	 * @changed OLI 25.10.2013 - Added.
	 */
	abstract public void addTable(TableModel table) throws IllegalArgumentException;

	/**
	 * Creates a new sequence.
	 *
	 * @return The new sequence
	 *
	 * @changed OLI 23.04.2013 - Added.
	 */
	abstract public SequenceModel createSequence();

	/**
	 * Fires the passed event to the listeners which are connected to the data model.
	 *
	 * @param e The event which is to fire to the listeners.
	 *
	 * @changed OLI 23.10.2013 - Added.
	 */
	abstract public void fireDataModelEvent(DataModelEvent e);

	/**
	 * Returns the additional diagram info.
	 * 
	 * @return The additional diagram info.
	 * 
	 * @changed OLI 29.06.2020 - Added.
	 */
	String getAdditionalDiagramInfo();

	/**
	 * Returns a script which is executed after a data model has been written to the file system.
	 *
	 * @return A script which is executed after a data model has been written to the file system.
	 *
	 * @changed OLI 17.06.2016 - Added.
	 */
	abstract public String getAfterWriteScript();

	/**
	 * Returns an array with all columns from all the tables of the model.
	 *
	 * @return An array with all columns from all the tables of the model.
	 *
	 * @changed OLI 25.04.2013 - Added.
	 */
	abstract public ColumnModel[] getAllColumns();

	/**
	 * Returns an array with all domains of the model.
	 *
	 * @return An array with all domains of the model.
	 *
	 * @changed OLI 26.04.2013 - Added.
	 */
	abstract public DomainModel[] getAllDomains();

	/**
	 * Returns the name of the author of the data model.
	 *
	 * @return The name of the author of the data model.
	 *
	 * @changed OLI 17.06.2016 - Added.
	 */
	abstract public String getAuthor();

	/**
	 * Returns the basic package name for the data model.
	 *
	 * @return The basic package name for the application which should be created by the data model. If nothing is
	 *         defined an empty string should be returned.
	 *
	 * @changed OLI 09.06.2016 - Added.
	 */
	abstract public String getBasePackageName();

	/**
	 * Returns the basic code path for the code which is create from the model.
	 *
	 * @return The basic code path for the code which is create from the model.
	 *
	 * @changed OLI 09.06.2016 - Added.
	 */
	abstract public String getBasicCodePath();

	/**
	 * Returns the domain for the passed name.
	 *
	 * @param name The name of the domain which is to return.
	 * @return The domain for the passed name or <CODE>null</CODE> if there is no domain for the passed name.
	 *
	 * @changed OLI 11.12.2013 - Added.
	 */
	abstract public DomainModel getDomainByName(String name);

	/**
	 * Returns the name of the data model.
	 *
	 * @return The name of the data model.
	 *
	 * @changed OLI 11.06.2013 - Added.
	 */
	abstract public String getName();

	/**
	 * Returns the JDBC data for a database import.
	 *
	 * @return The JDBC data for a database import.
	 *
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Deprecated
	abstract JDBCDataSourceRecord getImportDataSourceRecord();

	/**
	 * Returns the name of the data models owner.
	 *
	 * @return The name of the data models owner.
	 *
	 * @changed OLI 05.07.2016 - Added.
	 */
	abstract public String getOwner();

	/**
	 * Returns an array with the columns which are referencing the passed table.
	 * 
	 * @param table The table which is to check for referencing columns.
	 * @return An array with the columns which are referencing the passed table.
	 *
	 * @changed OLI 06.05.2013 - Added.
	 */
	abstract public ColumnModel[] getReferencers(TableModel table);

	/**
	 * Returns the name of the schema which the data mode represents.
	 *
	 * @return The name of the schema which the data mode represents.
	 *
	 * @changed OLI 15.06.2016 - Added.
	 */
	abstract public String getSchemaName();

	/**
	 * Returns the sequence with the passed name.
	 *
	 * @param name The name of the sequence which should be returned.
	 * @return The sequence with the passed name or <CODE>null</CODE> if there is no sequence with the passed name.
	 *
	 * @changed OLI 25.04.2013 - Added.
	 */
	abstract public SequenceModel getSequenceByName(String name);

	/**
	 * Returns an array with the sequences of the data model.
	 *
	 * @return An array with all sequences of the data model.
	 *
	 * @changed OLI 25.04.2013 - Added.
	 */
	abstract public SequenceModel[] getSequences();

	/**
	 * Returns the reference to the sequence list of the model.
	 *
	 * @return The reference to the sequence list of the model.
	 *
	 * @changed OLI 23.04.2013 - Added.
	 */
	abstract public Vector<SequenceModel> getSequencesByReference();

	/**
	 * Returns the table model with the passed name or <CODE>null</CODE> if there is no table with the passed name.
	 *
	 * @param name The name of the table which is to return.
	 * @return the table model with the passed name or <CODE>null</CODE> if there is no table with the passed name.
	 *
	 * @changed OLI 25.04.2013 - Added.
	 */
	abstract public TableModel getTableByName(String name);

	/**
	 * Returns an array with all tables of the model. In this array the tables are assorted alphabetically by their
	 * names.
	 *
	 * @return An array with all tables of the model (assorted alphabetically by their names).
	 *
	 * @changed OLI 25.04.2013 - Added.
	 */
	abstract public TableModel[] getTables();

	/**
	 * Returns the current version of the data model.
	 *
	 * @return The current version of the data model.
	 *
	 * @changed OLI 13.06.2013 - Added.
	 */
	abstract public String getVersion();

	/**
	 * Returns the comment for the current version.
	 *
	 * @return The comment for the current version.
	 *
	 * @changed OLI 17.06.2016 - Added.
	 */
	abstract public String getVersionComment();

	/**
	 * Returns the date of the current version of the data model.
	 *
	 * @return The date of the current version of the data model.
	 *
	 * @changed OLI 28.04.2014 - Added.
	 */
	abstract public PDate getVersionDate();

	/**
	 * Removes the passed data model listener.
	 *
	 * @param l The listener to remove.
	 *
	 * @changed OLI 23.10.2013 - Added.
	 */
	abstract public void removeDataModelListener(DataModelListener l);

	/**
	 * Removes the passed sequence from the data model.
	 *
	 * @param sequence The sequence to remove.
	 *
	 * @changed OLI 23.04.2013 - Added.
	 */
	abstract public void removeSequence(SequenceModel sequence);

	/**
	 * Removes the passed table from the model.
	 *
	 * @param table The table which is to remove from the model.
	 *
	 * @changed OLI 21.06.2016 - Added.
	 */
	abstract public void removeTable(TableModel table);

	/**
	 * Sets a new value for the additional diagram info.
	 * 
	 * @param additionalDiagramInfo The new value for the additional diagram info.
	 * 
	 * @changed oli 29.06.2020 - Added.
	 */
	void setAdditionalDiagramInfo(String additionalDiagramInfo);

	/**
	 * Sets a new script which is executed after a data model has been written to the file system.
	 *
	 * @return script The new script which is executed after a data model has been written to the file system.
	 *
	 * @changed OLI 17.06.2016 - Added.
	 */
	abstract public void setAfterWriteScript(String script);

	/**
	 * Sets a new name for the author of the data model.
	 *
	 * @return authorName The new name for the author of the data model.
	 *
	 * @changed OLI 17.06.2016 - Added.
	 */
	abstract public void setAuthor(String authorName);

	/**
	 * Sets a new value for the basic package name.
	 *
	 * @param basePackageName The new base package name for the model.
	 */
	abstract public void setBasePackageName(String basePackageName);

	/**
	 * Sets a new basic code path for the code which is create from the model.
	 *
	 * @param basicCodePath The new basic code path for the code which is create from the model.
	 *
	 * @changed OLI 09.06.2016 - Added.
	 */
	abstract public void setBasicCodePath(String basicCodePath);

	/**
	 * Sets a new name for the data model.
	 *
	 * @param name The new name for the data model.
	 *
	 * @changed OLI 09.06.2016 - Added.
	 */
	abstract public void setName(String name);

	/**
	 * Sets a new name for the data models owner.
	 *
	 * @return name The new name of the data models owner.
	 *
	 * @changed OLI 05.07.2016 - Added.
	 */
	abstract public void setOwner(String owner);

	/**
	 * Sets a new schema name for the data model.
	 *
	 * @param schemaName The new schema name for the data model.
	 *
	 * @changed OLI 15.06.2016 - Added.
	 */
	abstract public void setSchemaName(String schemaName);

	/**
	 * Sets a new current version for the data model.
	 *
	 * @param version A new currant version for the data model
	 *
	 * @changed OLI 28.04.2014 - Added.
	 */
	abstract public void setVersion(String version);

	/**
	 * Sets a new comment for the current version.
	 *
	 * @param comment The new comment for the current version.
	 *
	 * @changed OLI 17.06.2016 - Added.
	 */
	abstract public void setVersionComment(String comment);

	/**
	 * Sets a new date for the current version for the data model.
	 *
	 * @param date A new date for the current version for the data model
	 *
	 * @changed OLI 28.04.2014 - Added.
	 */
	abstract public void setVersionDate(PDate date);

}