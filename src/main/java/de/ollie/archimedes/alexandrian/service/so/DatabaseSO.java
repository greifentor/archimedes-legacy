package de.ollie.archimedes.alexandrian.service.so;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * A container class for databases in the service environment.
 * 
 * @author ollie
 *
 */
@Generated
@Data
@Accessors(chain = true)
public class DatabaseSO {

	private String name;
	private List<SchemeSO> schemes = new ArrayList<>();

	/**
	 * Adds the passed scheme to the schemes of the database service object.
	 * 
	 * @param scheme The scheme to add ("null" values will not be added).
	 * @return The database service object.
	 */
	public DatabaseSO addScheme(SchemeSO scheme) {
		if (scheme != null) {
			this.schemes.add(scheme);
		}
		return this;
	}

}