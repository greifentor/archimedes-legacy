package archimedes.legacy.updater;

import archimedes.model.TableModel;

/**
 * A interface for a check to ignore a table while converting the data model to CMO.
 *
 * @author ollie (10.03.2021)
 */
@FunctionalInterface
public interface TableIgnore {

	boolean ignore(TableModel tableModel);

}