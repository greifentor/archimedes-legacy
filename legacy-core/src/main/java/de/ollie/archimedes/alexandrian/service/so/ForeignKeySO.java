package de.ollie.archimedes.alexandrian.service.so;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * A container for foreign key service objects.
 *
 * @author ollie
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class ForeignKeySO {

	@NonNull
	private String name;
	@NonNull
	private List<ReferenceSO> references = new ArrayList<>();

	public ForeignKeySO addReferences(ReferenceSO... references) {
		for (ReferenceSO reference : references) {
			this.references.add(reference);
		}
		return this;
	}

}