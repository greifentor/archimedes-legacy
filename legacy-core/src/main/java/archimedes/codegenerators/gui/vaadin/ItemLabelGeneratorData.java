package archimedes.codegenerators.gui.vaadin;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ItemLabelGeneratorData {

	private String attributeName;
	private String typeName;
	private String typePackageName;

}
