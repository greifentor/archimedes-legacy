/*
 * UdschebtiSupport.java
 *
 * 17.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.model;


/**
 * An interface which should implemented by classes which supports Udschebti logic.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 17.06.2016 - Added.
 */

public interface UdschebtiSupport {

    /**
     * Returns the name of the Udschebti base class.
     *
     * @return The name of the Udschebti base class.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public String getUdschebtiBaseClassName();

    /**
     * Sets a new name for the Utschepti base class.
     *
     * @param utscheptiClassName The new name for the Utschepti base class.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setUdschebtiBaseClassName(String utscheptiClassName);

}