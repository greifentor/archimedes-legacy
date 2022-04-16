package archimedes.codegenerators.gui.vaadin;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class SubclassDataCollection {

	private List<SubclassData> subclasses = new ArrayList<>();

	public SubclassDataCollection addSubclasses(SubclassData... subclassData) {
		subclasses.addAll(List.of(subclassData));
		return this;
	}

}