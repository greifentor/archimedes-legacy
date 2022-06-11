package archimedes.codegenerators.gui.vaadin;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class GUIReferenceData {

	private String fieldNameCamelCase;
	private String findAllMethodNameExtension;
	private String referencedModelClassName;
	private String referencedModelNameFieldName;
	private String referencedModelPackageName;
	private String resourceName;
	private String serviceAttributeName;
	private String serviceInterfaceName;
	private String servicePackageName;
	private String tableName;

}