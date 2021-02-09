package archimedes.legacy.updater;

import java.util.Arrays;
import java.util.stream.Collectors;

import archimedes.model.DataModel;
import archimedes.model.TableModel;
import de.ollie.dbcomp.model.ColumnCMO;
import de.ollie.dbcomp.model.DataModelCMO;
import de.ollie.dbcomp.model.SchemaCMO;
import de.ollie.dbcomp.model.TableCMO;
import de.ollie.dbcomp.model.TypeCMO;

/**
 * A converter which converts a DataModel object into a CMO.
 *
 * @author ollie (08.02.2021)
 */
public class DataModelToCMOConverter {

	public DataModelCMO convert(DataModel dataModel) {
		return DataModelCMO.of(SchemaCMO.of("", getTables(dataModel)));
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

}