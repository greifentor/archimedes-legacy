/*
 * GenericApplicationSupport.java
 *
 * 20.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.model;


/**
 * An interface which is to implement by structures which are used with the Archimedes
 * Application logic.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 20.06.2016 - Added.
 */

public interface GenericApplicationSupport {

    /**
     * Checks if the object is active in the application.
     *
     * @return <CODE>true</CODE> if the object is active in the application.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public boolean isActiveInApplication();

    /**
     * Sets a new value for the state of the flag which decides if the object is active in the
     * application.
     *
     * @param activeInApplication Set this flag to have the object active in application.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public void setActiveInApplication(boolean activeInApplication);

}