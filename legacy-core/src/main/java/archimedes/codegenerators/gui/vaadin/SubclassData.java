package archimedes.codegenerators.gui.vaadin;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class SubclassData {

	private String modelClassName;
	private String modelPackageName;
	private String detailsLayoutClassName;

}