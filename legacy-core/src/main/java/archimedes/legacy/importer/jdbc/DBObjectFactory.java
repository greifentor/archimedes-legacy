package archimedes.legacy.importer.jdbc;

import java.util.List;

import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.IndexSO;
import de.ollie.archimedes.alexandrian.service.so.SchemeSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import de.ollie.archimedes.alexandrian.service.so.TypeSO;

/**
 * An interface for DB object factories.
 *
 * @author ollie
 *
 */
public interface DBObjectFactory {

	/**
	 * Creates a database column object.
	 *
	 * @param columnName The name of the column.
	 * @param type       The type for the column.
	 * @param nullable   Set this flag to create a nullable column.
	 * @param autoIncrement Set this flag if the column is an auto increment.
	 */
	ColumnSO createColumn(String columnName, TypeSO type, boolean nullable, boolean autoIncrement);

	/**
	 * Creates a database scheme object.
	 *
	 * @param name   The name of the scheme.
	 * @param tables The tables of the data scheme.
	 */
	SchemeSO createScheme(String name, List<TableSO> tables);

	/**
	 * Creates a database index object.
	 *
	 * @param indexName The name of the index.
	 * @param columns   The columns which are members of the index.
	 */
	IndexSO createIndex(String indexName, List<ColumnSO> columns);

	/**
	 * Creates a database table object.
	 *
	 * @param tableName The name of the table.
	 * @param columns   The columns which are members of the table.
	 */
	TableSO createTable(String tableName, List<ColumnSO> columns);

}