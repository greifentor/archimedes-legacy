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
			dataToAdd.stream().filter(data -> !isAlreadyContained(data)).forEach(data -> references.add(data));
		}
		return this;
	}

	private boolean isAlreadyContained(GUIReferenceData guiReferenceData) {
		return references
				.stream()
				.anyMatch(e -> e.getServiceAttributeName().equals(guiReferenceData.getServiceAttributeName()));
	}

	public String getServiceAttributeNameList() {
		return references
				.stream()
				.map(reference -> reference.getServiceAttributeName())
				.sorted((s0, s1) -> s0.compareTo(s1))
				.reduce((s0, s1) -> s0 + ", " + s1)
				.orElse(null);
	}

}