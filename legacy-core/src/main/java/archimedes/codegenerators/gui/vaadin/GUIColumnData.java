package archimedes.codegenerators.gui.vaadin;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class GUIColumnData {

	public static final String TYPE_COMBOBOX = "COMBOBOX";
	public static final String TYPE_INTEGER = "INTEGER";
	public static final String TYPE_STRING = "STRING";

	private String fieldNameCamelCase;
	private String max;
	private String min;
	private int position;
	private String resourceName;
	private String type;

}