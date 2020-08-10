/*
 * GeneratorProvider.java
 *
 * 16.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.model;


/**
 * An interface which should be implemented by classes whose object provide a code generator or
 * with one.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 16.06.2016 - Added.
 */

public interface GeneratorSupport {

    /**
     * Returns the name of the application which is to generate.
     * 
     * @param name The new application name of the model. A <CODE>null</CODE> value will be
     *         changed to an empty string.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public String getApplicationName();

    /**
     * Returns the class name of the code factory which is to use.
     *
     * @return The class name of the code factory which is to use.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public String getCodeFactoryClassName();

    /**
     * Sets a new name for the application which is to generate.
     * 
     * @param applicationName The new name for the application.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setApplicationName(String applicationName);

    /**
     * Sets a new class name for the code factory class which is to use.
     * 
     * @param codeFactoryClassName The new code factory class name.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setCodeFactoryClassName(String codeFactoryClassName);

}