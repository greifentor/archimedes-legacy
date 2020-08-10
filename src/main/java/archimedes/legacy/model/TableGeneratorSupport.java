/*
 * TableGeneratorSupport.java
 *
 * 20.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.model;


/**
 * An interface with method for generator supports of tables.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 20.06.2016 - Added.
 */

public interface TableGeneratorSupport {

    /**
     * Checks if the code in a dynamic way for the object.
     *
     * @return <CODE>true</CODE> if the code in a dynamic way for the object.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public boolean isDynamicCode();

    /**
     * Checks if the object is marked as inherited.
     *
     * @return <CODE>true</CODE> if the object is marked as inherited.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public boolean isInherited();

    /**
     * Sets a new state for the flag which decides if the code is to generate in a dynamic way
     * for the object.
     *
     * @param dynamicCode The new state of the dynamic code flag.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public void setDynamicCode(boolean dynamicCode);

    /**
     * Sets a new state for the inherited flag of the object.
     *
     * @param inherited The new state of the inherited code flag.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public void setInherited(boolean inherited);

}