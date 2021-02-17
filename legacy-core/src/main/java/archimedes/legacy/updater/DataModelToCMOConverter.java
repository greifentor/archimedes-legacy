package archimedes.legacy.updater;

import java.util.Arrays;
import java.util.stream.Collectors;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import de.ollie.dbcomp.model.ColumnCMO;
import de.ollie.dbcomp.model.DataModelCMO;
import de.ollie.dbcomp.model.ForeignKeyCMO;
import de.ollie.dbcomp.model.ForeignKeyMemberCMO;
import de.ollie.dbcomp.model.SchemaCMO;
import de.ollie.dbcomp.model.TableCMO;
import de.ollie.dbcomp.model.TypeCMO;

/**
 * A converter which converts a DataModel object into a CMO.
 *
 * @author ollie (08.02.2021)
 */
public class DataModelToCMOConverter {

	private static final String DEFAULT_SCHEMA_NAME = "";

	public DataModelCMO convert(DataModel dataModel) {
		DataModelCMO cmo = DataModelCMO.of(SchemaCMO.of(DEFAULT_SCHEMA_NAME, getTables(dataModel)));
		addForeignKeys(cmo, dataModel);
		return cmo;
	}

	private TableCMO[] getTables(DataModel dataModel) {
		return Arrays
				.asList(dataModel.getTables())
				.stream()
				.map(table -> TableCMO.of(table.getName(), getColumns(table)))
				.collect(Collectors.toList())
				.toArray(new TableCMO[0]);
	}

	private ColumnCMO[] getColumns(TableModel table) {
		return Arrays
				.asList(table.getColumns())
				.stream()
				.map(
						column -> ColumnCMO
								.of(
										column.getName(),
										TypeCMO
												.of(
														column.getDomain().getDataType(),
														(column.getDomain().getLength() < 0
																? 0
																: column.getDomain().getLength()),
														(column.getDomain().getDecimalPlace() < 0
																? 0
																: column.getDomain().getDecimalPlace())),
										false,
										!column.isNotNull()))
				.collect(Collectors.toList())
				.toArray(new ColumnCMO[0]);
	}

	private void addForeignKeys(DataModelCMO cmo, DataModel dataModel) {
		cmo
				.getSchemata()
				.entrySet()
				.stream()
				.forEach(
						schemaValue -> schemaValue
								.getValue()
								.getTables()
								.entrySet()
								.stream()
								.forEach(
										tableValue -> addForeignKeys(
												tableValue.getValue(),
												cmo,
												dataModel)));
	}

	private void addForeignKeys(TableCMO table, DataModelCMO cmo, DataModel dataModel) {
		SchemaCMO schema = cmo.getSchemata().get(DEFAULT_SCHEMA_NAME);
		Arrays
				.asList(dataModel.getTableByName(table.getName()).getColumns())
				.stream()
				.filter(column -> column.getReferencedTable() != null)
				.forEach(
						column -> table
								.addForeignKeys(
										ForeignKeyCMO
												.of(
														"FK_TO_" + column.getReferencedTable().getName()
																+ "_"
																+ column.getReferencedColumn().getName(),
														ForeignKeyMemberCMO
																.of(
																		table,
																		getColumn(schema, column),
																		getTable(schema, column.getReferencedTable()),
																		getColumn(
																				schema,
																				column.getReferencedColumn())))));
	}

	private ColumnCMO getColumn(SchemaCMO schema, ColumnModel column) {
		return schema
				.getTableByName(column.getTable().getName())
				.map(table -> table.getColumnByName(column.getName()).orElse(null))
				.orElse(null);
	}

	private TableCMO getTable(SchemaCMO schema, TableModel table) {
		return schema.getTableByName(table.getName()).orElse(null);
	}

}