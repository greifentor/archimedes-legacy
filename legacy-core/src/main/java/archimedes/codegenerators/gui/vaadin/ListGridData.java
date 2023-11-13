package archimedes.codegenerators.gui.vaadin;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ListGridData {

	private String listDetailsLayoutClassName;
	private String listDetailsLayoutPackageName;

}
