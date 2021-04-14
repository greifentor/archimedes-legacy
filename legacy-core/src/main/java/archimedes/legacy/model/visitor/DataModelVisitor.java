package archimedes.legacy.model.visitor;

import java.util.Arrays;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A class which could be used as a base class for model visitors.
 *
 * @author ollie (09.04.2021)
 * 
 */
public class DataModelVisitor {

	@Builder
	@Getter
	@Setter(AccessLevel.PRIVATE)
	public static class Column {
		private int fieldLength;
		private int fieldPrecision;
		private String name;
		private boolean notNull;
		private boolean pkMember;
		private int sqlType;
		private Table table;
	}

	@Builder
	@Getter
	@Setter(AccessLevel.PRIVATE)
	public static class Table {
		private String name;
	}

	/**
	 * Starts visiting the models tables and columns.
	 *
	 * @param model The model whose tables and columns are to visit.
	 */
	public void visit(DataModel model) {
		Arrays.asList(model.getTables()).forEach(table -> {
			visitTable(convertToTable(table));
			Arrays.asList(table.getColumns()).forEach(column -> visitColumn(convertToColumn(column)));
		});
	}

	private Table convertToTable(TableModel table) {
		return Table.builder().name(table.getName()).build();
	}

	private Column convertToColumn(ColumnModel column) {
		DomainModel domain = column.getDomain();
		return Column
				.builder()
				.fieldLength(domain.getLength())
				.fieldPrecision(domain.getDecimalPlace())
				.name(column.getName())
				.notNull(column.isNotNull())
				.pkMember(column.isPrimaryKey())
				.sqlType(domain.getDataType())
				.table(convertToTable(column.getTable()))
				.build();
	}

	public void visitColumn(Column column) {
	}

	public void visitTable(Table table) {
	}

}