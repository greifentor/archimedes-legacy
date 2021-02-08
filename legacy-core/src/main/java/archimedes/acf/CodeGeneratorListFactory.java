/*
 * CodeGeneratorListFactory.java
 *
 * 18.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf;


import java.util.*;


/**
 * Provides a list of the generators which are to use by the <CODE>CodeFactory</CODE>.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.04.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public interface CodeGeneratorListFactory {

    /**
     * Returns a list of generators which are to call by the <CODE>CodeFactory</CODE>.
     *
     * @return A list of generators which are to call by the <CODE>CodeFactory</CODE>.
     *
     * @changed OLI 18.04.2013 - Added.
     */
    abstract public List<CodeGenerator> getCodeGenerators();

    /**
     * Returns the resource updater.
     *
     * @return The resource updater.
     *
     * @changed OLI 06.07.2016 - Added.
     */
    abstract public ResourceUpdater getResourceUpdater();

}