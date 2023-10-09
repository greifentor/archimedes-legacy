package archimedes.codegenerators.gui.vaadin;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class SubclassData {

	private String detailsLayoutClassName;
	private String modelClassName;
	private String modelPackageName;
	private String itemLabelGeneratorCollectionAttributeName;
	private String itemLabelGeneratorCollectionClassName;
	private String itemLabelGeneratorCollectionPackageName;
	private List<SubclassReferenceData> references = new ArrayList<>();

}