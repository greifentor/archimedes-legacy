package archimedes.legacy.importer.jdbc;

import java.util.List;

import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.IndexSO;
import de.ollie.archimedes.alexandrian.service.so.OptionSO;
import de.ollie.archimedes.alexandrian.service.so.SchemeSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import de.ollie.archimedes.alexandrian.service.so.TypeSO;

/**
 * A default implementation of the DB object factory.
 *
 * @author ollie
 *
 */
public class DefaultDBObjectFactory implements DBObjectFactory {

	@Override
	public ColumnSO createColumn(String columnName, TypeSO type, boolean nullable, boolean autoIncrement) {
		ColumnSO column = new ColumnSO().setName(columnName).setNullable(nullable).setType(type);
		if (autoIncrement) {
			column.addOptions(new OptionSO().setName("AUTO_INCREMENT").setValue("IDENTITY"));
		}
		return column;
	}

	@Override
	public SchemeSO createScheme(String name, List<TableSO> tables) {
		return new SchemeSO().setName(name).setTables(tables);
	}

	@Override
	public IndexSO createIndex(String indexName, List<ColumnSO> columns) {
		return new IndexSO().setColumns(columns).setName(indexName);
	}

	@Override
	public TableSO createTable(String tableName, List<ColumnSO> columns) {
		return new TableSO().setColumns(columns).setName(tableName);
	}

}