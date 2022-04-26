package archimedes.codegenerators.gui.vaadin;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class SubclassReferenceData {

	private String serviceAttributeName;
	private String serviceInterfaceName;
	private String servicePackageName;

}