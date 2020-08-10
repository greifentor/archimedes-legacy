/*
 * Option.java
 *
 * 15.10.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;

import archimedes.legacy.model.OptionModel;
import corentx.util.Utl;

/**
 * An option for a model object.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 15.10.2013 - Added.
 */

public class Option implements OptionModel {

	private String name = null;
	private String parameter = null;

	/**
	 * Creates a new option with the passed parameters.
	 *
	 * @param name The name for the option.
	 * @throws IllegalArgumentException If the name is passed empty or as null pointer.
	 *
	 * @changed OLI 15.10.2013 - Added.
	 */
	public Option(String name) {
		super();
		this.setName(name);
	}

	/**
	 * Creates a new option with the passed parameters.
	 *
	 * @param name      The name for the option.
	 * @param parameter A parameter for the option, if there is one.
	 * @throws IllegalArgumentException If the name is passed empty or as null pointer.
	 *
	 * @changed OLI 15.10.2013 - Added.
	 */
	public Option(String name, String parameter) {
		this(name);
		this.setParameter(parameter);
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public int compareTo(Object o) {
		Option option = (Option) o;
		return this.getName().compareTo(option.getName());
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Option)) {
			return false;
		}
		Option option = (Option) o;
		return Utl.equals(this.getName(), option.getName()) && Utl.equals(this.getParameter(), option.getParameter());
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public String getParameter() {
		return this.parameter;
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public int hashCode() {
		int result = 31 + this.getName().hashCode();
		result = 31 * result + (this.getParameter() != null ? this.getParameter().hashCode() : 0);
		return result;
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public void setName(String name) {
		ensure(name != null, "name cannot be null.");
		ensure(!name.isEmpty(), "name cannot be empty.");
		this.name = name;
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public String toString() {
		return "Name=" + this.getName() + ",Parameter=" + this.getParameter();
	}

}