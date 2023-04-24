package archimedes.codegenerators.gui.vaadin;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class PreferenceData {

	private String attributeName;
	private String attributeNameCamelCase;
	private String fieldTypeName;
	private String firstFieldType;
	private String firstFieldNameCamelCase;
	private String idColumnNameCamelCase;
	private String nextFieldType;
	private String nextFieldNameCamelCase;
	private String preferenceIdName;
	private String type;

}