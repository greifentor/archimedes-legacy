package de.ollie.archimedes.alexandrian.service.so;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	private List<OptionSO> options = new ArrayList<>();
	private List<SchemeSO> schemes = new ArrayList<>();

	/**
	 * Adds the passed options to the options of the database service object.
	 * 
	 * @param options The options to add ("null" values will not be added).
	 * @return The database service object.
	 */
	public DatabaseSO addOptions(OptionSO... options) {
		for (OptionSO option : options) {
			if (option != null) {
				this.options.add(option);
			}
		}
		return this;
	}

	/**
	 * Adds the passed schemes to the schemes of the database service object.
	 * 
	 * @param schemes The schemes to add ("null" values will not be added).
	 * @return The database service object.
	 */
	public DatabaseSO addSchemes(SchemeSO... schemes) {
		for (SchemeSO scheme : schemes) {
			if (scheme != null) {
				this.schemes.add(scheme);
			}
		}
		return this;
	}

	/**
	 * Adds the passed schemes to the schemes of the database service object.
	 * 
	 * @param schemes The schemes to add ("null" values will not be added).
	 * @return The database service object.
	 */
	public DatabaseSO addSchemes(List<SchemeSO> schemes) {
		this.schemes.addAll(schemes);
		return this;
	}

	public Optional<OptionSO> getOptionByName(String name) {
		for (OptionSO option : this.options) {
			if (option.getName().equals(name)) {
				return Optional.of(option);
			}
		}
		return Optional.empty();
	}

}