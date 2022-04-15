package archimedes.codegenerators.gui.vaadin;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@Data
public class GUIColumnDataCollection {

	private List<GUIColumnData> columns;

	public boolean hasFieldType(String type) {
		return columns.stream().anyMatch(column -> column.getType().equals(type));
	}

}