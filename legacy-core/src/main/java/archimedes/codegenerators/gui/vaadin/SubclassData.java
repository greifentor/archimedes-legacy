package archimedes.codegenerators.gui.vaadin;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class SubclassData {

	private String modelClassName;
	private String modelPackageName;
	private String detailsLayoutClassName;
	private List<SubclassReferenceData> references = new ArrayList<>();

}