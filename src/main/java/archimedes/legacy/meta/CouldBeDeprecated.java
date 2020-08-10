/*
 * CouldBeDeprecated.java
 *
 * 16.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;


/**
 * An interface which defines the necessary methods for an object which could be marked as
 * deprecated. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 16.12.2015 - Added.
 */

public interface CouldBeDeprecated {

    /**
     * Checks if the object is marked is deprecated.
     *
     * @return <CODE>true</CODE> if the object is deprecated.
     *
     * @changed OLI 16.12.2015 - Added.
     */
    abstract public boolean isDeprecated();

    /**
     * Sets a new value for the deprecated flag of the object.
     *
     * @param isDeprecated The new value for the deprecated flag.
     *
     * @changed OLI 16.12.2015 - Added.
     */
    abstract public void setDeprecated(boolean isDeprecated);

}