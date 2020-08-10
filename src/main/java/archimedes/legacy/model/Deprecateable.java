/*
 * Deprecateable.java
 *
 * 11.03.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * An interface which is to implement by classes whose objects can be deprecated.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.03.2016 - Added.
 */

public interface Deprecateable {

    /**
     * Checks if the object is marked as deprecated.
     *
     * @return <CODE>true</CODE> if the object is marked as deprecated.
     *
     * @changed OLI 11.03.2016 - Added.
     */
    abstract public boolean isDeprecated();

    /**
     * Sets the passed value to the deprecated flag of the object. 
     *
     * @param b The new value for the deprecated flag of the object.
     *
     * @changed OLI 11.03.2016 - Added.
     */
    abstract public void setDeprecated(boolean b);

}