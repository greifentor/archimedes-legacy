package archimedes.codegenerators.gui.vaadin;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class MasterDataData {

	private String modelClassName;
	private String pageViewName;
	private String resourceIdentifier;

}