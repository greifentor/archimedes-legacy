/*
 * ChangeOperation.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops;

import static corentx.util.Checks.ensure;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import archimedes.legacy.meta.MetaDataNamedObject;
import corentx.util.Str;

/**
 * An abstract base class for all change operations.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

abstract public class AbstractChangeOperation implements Comparable<AbstractChangeOperation> {

	private ScriptSectionType section = null;

	/**
	 * Creates a new change operation with the passed parameters.
	 *
	 * @param section The type of the section which the change operation is related to.
	 *
	 * @changed OLI 11.12.2015 - Added.
	 */
	public AbstractChangeOperation(ScriptSectionType section) {
		super();
		ensure(section != null, "section cannot be null.");
		this.section = section;
	}

	/**
	 * @changed OLI 11.12.2015 - Added.
	 */
	@Override
	public int compareTo(AbstractChangeOperation o) {
		return this.section.compareTo(o.getSection());
	}

	/**
	 * @changed OLI 18.12.2015 - Added.
	 */
	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	/**
	 * Returns the type of the script section which the change operation is related to.
	 *
	 * @return The type of the script section which the change operation is related to.
	 *
	 * @changed OLI 11.12.2015 - Added.
	 */
	public ScriptSectionType getSection() {
		return this.section;
	}

	/**
	 * @changed OLI 18.12.2015 - Added.
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * Quotes the name of a meta data object with a name.
	 *
	 * @param metaDataObject The meta data object with the name to quote.
	 *
	 * @changed OLI 19.02.2016 - Added.
	 */
	public String quote(MetaDataNamedObject metaDataObject) {
		return this.quote(metaDataObject.getName());
	}

	/**
	 * Quotes the names of the passed meta data objects with a name.
	 *
	 * @param metaDataObjects The meta data objects with the names to quote.
	 *
	 * @changed OLI 19.02.2016 - Added.
	 */
	public String quote(MetaDataNamedObject[] metaDataObjects) {
		String s = "";
		for (MetaDataNamedObject mdo : metaDataObjects) {
			if (s.length() > 0) {
				s += ", ";
			}
			s += this.quote(mdo);
		}
		return s;
	}

	/**
	 * Quotes a string.
	 *
	 * @param s The string to quote.
	 *
	 * @changed OLI 19.02.2016 - Added.
	 */
	public String quote(String s) {
		return Str.quote(s, "\"");
	}

}