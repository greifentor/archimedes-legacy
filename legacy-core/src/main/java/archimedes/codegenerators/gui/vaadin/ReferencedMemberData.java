package archimedes.codegenerators.gui.vaadin;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ReferencedMemberData {

	private String attributeName;
	private String modelClassName;
	private String modelPackageName;

}
