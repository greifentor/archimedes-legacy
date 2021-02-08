/*
 * NamedObject.java
 *
 * 23.12.2015
 *
 * (c) by O. Lieshoff
 *
 */

package corent.base;


/**
 * Defines the methods for an object which provides a name.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.12.2015 - Added.
 */

public interface NamedObject {

    /**
     * Returns the name of the object.
     *
     * @return The name of the object.
     *
     * @changed OLI 23.12.2015 - Added.
     */
    abstract public String getName();

}