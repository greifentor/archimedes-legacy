package de.ollie.archimedes.alexandrian.service.so;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * A container for table service objects.
 *
 * @author ollie
 *
 */
@Generated
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class TableSO {

	@NonNull
	private String name;
	@NonNull
	private List<ColumnSO> columns = new ArrayList<>();
	@NonNull
	private List<ForeignKeySO> foreignKeys = new ArrayList<>();
	@NonNull
	private List<IndexSO> indices = new ArrayList<>();
	private TableMetaInfo metaInfo = new TableMetaInfo();
	private TableGUIInfo guiInfo = new TableGUIInfo();

	public TableSO addColumns(ColumnSO... columns) {
		for (ColumnSO column : columns) {
			this.columns.add(column);
			column.setTable(this);
		}
		return this;
	}

	public TableSO addForeignKeys(ForeignKeySO... foreignKeys) {
		for (ForeignKeySO foreignKey : foreignKeys) {
			this.foreignKeys.add(foreignKey);
		}
		return this;
	}

	public TableSO addOptions(OptionSO... options) {
		for (OptionSO option : options) {
			this.metaInfo.getOptions().add(option);
		}
		return this;
	}

	public Optional<ColumnSO> getColumnByName(String name) {
		for (ColumnSO column : this.columns) {
			if (column.getName().equalsIgnoreCase(name)) {
				return Optional.of(column);
			}
		}
		return Optional.empty();
	}

	public Optional<OptionSO> getOptionByName(String name) {
		for (OptionSO option : this.getMetaInfo().getOptions()) {
			if (option.getName().equalsIgnoreCase(name)) {
				return Optional.of(option);
			}
		}
		return Optional.empty();
	}

	public Optional<ForeignKeySO> getForeignKeyByName(String name) {
		for (ForeignKeySO foreignKey : this.foreignKeys) {
			if (foreignKey.getName().equalsIgnoreCase(name)) {
				return Optional.of(foreignKey);
			}
		}
		return Optional.empty();
	}

}