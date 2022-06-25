package archimedes.codegenerators;

import archimedes.model.ColumnModel;
import archimedes.model.TableModel;
import lombok.Data;
import lombok.experimental.Accessors;

public class Filters {

	@Accessors(chain = true)
	@Data
	public static class Filter {

		private ColumnModel column;
		private String fieldName;

	}

	public static final String FILTER = "FILTER";

	public boolean hasFilter(TableModel table) {
		return table.getColumnsWithOption(FILTER).length > 0;
	}

}