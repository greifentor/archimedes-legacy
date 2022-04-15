package archimedes.codegenerators.gui.vaadin;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class GUIColumnData {

	public static final String TYPE_BOOLEAN = "BOOLEAN";
	public static final String TYPE_COMBOBOX = "COMBOBOX";
	public static final String TYPE_ENUM = "ENUM";
	public static final String TYPE_INTEGER = "INTEGER";
	public static final String TYPE_STRING = "STRING";

	private String fieldNameCamelCase;
	private String fieldTypeName;
	private String max;
	private String min;
	private int position;
	private String resourceName;
	private boolean simpleBoolean;
	private String type;
	private String typePackage;

}