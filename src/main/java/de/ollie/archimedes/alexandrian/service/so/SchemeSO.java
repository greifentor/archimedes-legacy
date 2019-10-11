package de.ollie.archimedes.alexandrian.service.so;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * A container class for database schemes in the service environment.
 * 
 * @author ollie
 *
 */
@Generated
@Data
@Accessors(chain = true)
public class SchemeSO {

	private String name;
	private List<TableSO> tables = new ArrayList<>();

	public SchemeSO addTables(TableSO... tables) {
		for (TableSO table : tables) {
			if (table != null) {
				this.tables.add(table);
			}
		}
		return this;
	}

	public Optional<TableSO> getTableByName(String name) {
		for (TableSO table : this.tables) {
			if (table.getName().equalsIgnoreCase(name)) {
				return Optional.of(table);
			}
		}
		return Optional.empty();
	}

}