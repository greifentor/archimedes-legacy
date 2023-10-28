package archimedes.codegenerators.gui.vaadin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@NoArgsConstructor
public class GUIColumnDataCollection {

	private List<GUIColumnData> columns = new ArrayList<>();

	public GUIColumnDataCollection addGUIColumnData(List<GUIColumnData> dataToAdd) {
		if (dataToAdd != null) {
			dataToAdd.forEach(data -> columns.add(data));
		}
		return this;
	}

	public boolean hasFieldType(String type) {
		return columns.stream().anyMatch(column -> column.getType().equals(type));
	}

	public List<GUIColumnData> getColumns() {
		return columns.stream().sorted((c0, c1) -> c0.getPosition() - c1.getPosition()).collect(Collectors.toList());
	}

	public List<GUIColumnData> getNotNullableColumns() {
		return getColumns().stream().filter(c -> !c.isNullable()).collect(Collectors.toList());
	}

}