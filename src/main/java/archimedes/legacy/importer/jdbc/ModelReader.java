package archimedes.legacy.importer.jdbc;

import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;

/**
 * An interface for model readers.
 *
 * @author Oliver.Lieshoff
 *
 */
public interface ModelReader {

	/**
	 * Adds the passed listener to the listeners observing the model reader.
	 * 
	 * @param listener The listener to add.
	 */
	ModelReader addModelReaderListener(ModelReaderListener listener);

	/**
	 * Read the data model which is representing the database linked to the passed
	 * connection.
	 *
	 * @return The data model for the database which is linked by the passed
	 *         connection.
	 * @throws Exception If an error occurs while accessing the database.
	 */
	DatabaseSO readModel() throws Exception;

	/**
	 * Removes the passed listener from the listeners observing the model reader.
	 * 
	 * @param listener The listener to remove.
	 */
	void removeModelReaderListener(ModelReaderListener listener);

}