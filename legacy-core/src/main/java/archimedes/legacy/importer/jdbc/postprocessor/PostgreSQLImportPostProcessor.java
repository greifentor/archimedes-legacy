package archimedes.legacy.importer.jdbc.postprocessor;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import archimedes.legacy.model.visitor.DataModelVisitor;
import archimedes.legacy.scheme.Domain;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A post processor for JDBC imports of PostgreSQL data schemes.
 *
 * @author ollie (12.04.2021)
 */
public class PostgreSQLImportPostProcessor extends DataModelVisitor {

	@Builder
	@Getter
	@Setter(AccessLevel.PRIVATE)
	public static class BitToBooleanTypeChange {
		private String columnName;
		private String tableName;
	}

	private List<BitToBooleanTypeChange> changes = new ArrayList<>();

	public PostgreSQLImportPostProcessor(DataModel model) {
		visit(model);
		DomainModel booleanDomain = getModelByType(model, Types.BOOLEAN);
		changes.forEach(change -> {
			TableModel table = model.getTableByName(change.getTableName());
			if (table != null) {
				ColumnModel column = table.getColumnByName(change.getColumnName());
				if (column != null) {
					column.setDomain(booleanDomain);
				}
			}
		});
	}

	private DomainModel getModelByType(DataModel model, int type) {
		return Arrays
				.asList(model.getAllDomains())
				.stream()
				.filter(domain -> domain.getDataType() == type)
				.findFirst()
				.orElse(createBooleanDomain(model));
	}

	private DomainModel createBooleanDomain(DataModel model) {
		DomainModel d = new Domain("BoolValue", Types.BOOLEAN, 0, 0);
		model.addDomain(d);
		return d;
	}

	@Override
	public void visitColumn(Column column) {
		if (column.getSqlType() == Types.BIT) {
			changes
					.add(
							BitToBooleanTypeChange
									.builder()
									.columnName(column.getName())
									.tableName(column.getTable().getName())
									.build());
		}
	}

}