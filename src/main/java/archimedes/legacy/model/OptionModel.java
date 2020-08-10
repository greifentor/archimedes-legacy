/*
 * OptionModel.java
 *
 * 15.10.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * An option for a model object.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 15.10.2013 - Added.
 */

public interface OptionModel extends Comparable {

    /**
     * Returns the name of the option.
     *
     * @return The name of the option.
     *
     * @changed OLI 15.10.2013 - Added.
     */
    abstract public String getName();

    /**
     * Returns the parameter of the option.
     *
     * @return The parameter of the option.
     *
     * @changed OLI 15.10.2013 - Added.
     */
    abstract public String getParameter();

    /**
     * Sets a new name for the option.
     *
     * @return name The new name for the option.
     * @throws IllegalArgumentException If name is passed as empty or null string.
     *
     * @changed OLI 15.10.2013 - Added.
     */
    abstract public void setName(String name);

    /**
     * Sets a new parameter for the option.
     *
     * @return parameter The new parameter for the option.
     *
     * @changed OLI 15.10.2013 - Added.
     */
    abstract public void setParameter(String parameter);

}