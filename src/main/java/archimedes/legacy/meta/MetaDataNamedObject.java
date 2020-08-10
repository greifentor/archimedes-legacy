/*
 * NamedDBObject.java
 *
 * 16.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import static corentx.util.Checks.ensure;

import archimedes.legacy.model.NamedObject;

/**
 * A base class for meta data objects with name attributes.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 16.12.2015 - Added.
 */

public class MetaDataNamedObject implements Comparable<MetaDataNamedObject>, NamedObject {

	private String name = null;

	/**
	 * Creates a new meta data object with the passed name.
	 *
	 * @param name The name of the sequence.
	 *
	 * @changed OLI 16.12.2015 - Added.
	 */
	public MetaDataNamedObject(String name) {
		super();
		this.setName(name);
	}

	/**
	 * @changed OLI 16.12.2015 - Added.
	 */
	@Override
	public int compareTo(MetaDataNamedObject o) {
		return this.getName().compareTo(o.getName());
	}

	/**
	 * @changed OLI 16.12.2015 - Added.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Sets a new name for the meta data object
	 *
	 * @param name The new name for the meta data object.
	 *
	 * @changed OLI 16.12.2015 - Added.
	 */
	public void setName(String name) {
		ensure(name != null, "name cannot be null.");
		ensure(!name.isEmpty(), "name cannot be nullempty.");
		this.name = name;
	}

}