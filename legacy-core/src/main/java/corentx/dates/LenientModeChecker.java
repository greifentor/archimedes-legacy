/*
 * LenientModeChecker.java
 *
 * 03.07.2014
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;

import static corentx.util.Checks.*;


/**
 * This class checks if the lenient mode is not suppressed for a specific timestamp class.
 * This can be done by suppressing the mode in a global way or for a specific class only. The
 * used properties to configure the lenient mode suppression are:
 * 
 * <P><B><PRE>corentx.dates.lenient.suppressed.global</PRE></B> for a globel suppression and
 * <P><B><PRE>corentx.dates.lenient.suppressed.[simple class name]</PRE></B> for a specific
 *         suppression.
 *
 * The global flag is stronger than the specific one if set.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 03.07.2014 - Added.
 */

public class LenientModeChecker {

    /** The prefix for the properties to suppress the lenient mode. */
    public static final String PREFIX = "corentx.dates.lenient.suppressed.";

    /**
     * The constant to switch the global lenient mode (configured by a property on system
     * start).
     */
    public static boolean LENIENT_MODE = Boolean.getBoolean(PREFIX + "global");

    private Class cls = null;

    /**
     * Creates a new lenient mode checker with the passed parameters.
     *
     * @param cls The class which the lenient mode suppression is to check for.
     * @throws IllegalArgumentException Passing a null pointer as class to check.
     *
     * @changed OLI 03.07.2014 - Added.
     */
    public LenientModeChecker(Class cls) throws IllegalArgumentException {
        super();
        ensure(cls != null, "passed class cannot be null.");
        this.cls = cls;
    }

    /**
     * Checks if the lenient mode is not suppressed for a specific timestamp class.
     *
     * @return <CODE>true</CODE> if the lenient mode is neither suppressed global nor for the
     *         specific class.
     *
     * @changed OLI 03.07.2014 - Added.
     */
    public boolean isLenient() throws IllegalArgumentException {
        return LENIENT_MODE || Boolean.getBoolean(PREFIX + this.cls.getSimpleName());
    }

}