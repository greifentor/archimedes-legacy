package archimedes.codegenerators.gui.vaadin;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class GridData {

	private String fieldNameCamelCase;
	private int position;
	private String resourceName;
	private boolean simpleBoolean;

}