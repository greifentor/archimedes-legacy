/*
 * ParameterUtilOnwer.java
 *
 * 25.06.2015
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.acf.util;

import archimedes.acf.*;

/**
 * A class which owns a parameter util (as superclass for other classes).
 *
 * @author O.Lieshoff
 *
 * @changed OLI 25.06.2015 - Added.
 */

public class ParameterUtilOnwer {

    protected ParameterUtil parameterUtil = null;

    /**
     * Creates a new parameter util owner with the passed parameters.
     *
     * @param parameterUtil A utility class for working with parameters.
     *
     * @changed OLI 25.06.2015 - Added.
     */
    public ParameterUtilOnwer(ParameterUtil parameterUtil) {
        super();
        this.parameterUtil = parameterUtil;
    }

}