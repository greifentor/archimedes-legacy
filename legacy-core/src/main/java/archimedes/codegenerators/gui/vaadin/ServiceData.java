package archimedes.codegenerators.gui.vaadin;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ServiceData {

	private String serviceAttributeName;
	private String serviceInterfaceName;
	private String serviceModelAttributeName;
	private String serviceModelClassName;
	private String serviceModelPackageName;
	private String servicePackageName;

}
