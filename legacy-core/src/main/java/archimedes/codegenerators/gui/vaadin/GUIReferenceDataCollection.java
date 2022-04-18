package archimedes.codegenerators.gui.vaadin;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@NoArgsConstructor
public class GUIReferenceDataCollection {

	private List<GUIReferenceData> references = new ArrayList<>();

	public GUIReferenceDataCollection addGUIReferenceData(List<GUIReferenceData> dataToAdd) {
		if (dataToAdd != null) {
			dataToAdd.forEach(data -> references.add(data));
		}
		return this;
	}

	public String getServiceAttributeNameList() {
		return references
				.stream()
				.map(reference -> reference.getServiceAttributeName())
				.reduce((s0, s1) -> s0 + ", " + s1)
				.orElse(null);
	}

}