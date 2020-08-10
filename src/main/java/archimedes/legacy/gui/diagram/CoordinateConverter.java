/*
 * CoordinateConverter.java
 *
 * 14.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.gui.diagram;


/**
 * This interface provides the necessary methods for a class which is able to convert
 * coordinates by a zoom factor.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.05.2013 - Added.
 */

public interface CoordinateConverter {

    /**
     * Returns the zoom factor.
     *
     * @return The zoom factor.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public double getZoomFactor();

    /**
     * Converts the passed number by the zoom factor.
     *
     * @param n The which is to changed by the zoom factor.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public int convert(int n);

    /**
     * Converts the passed number by the zoom factor.
     *
     * @param n The which is to changed by the zoom factor.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public int convert(double n);

    /**
     * Converts the passed number by the zoom factor in the reverse way.
     *
     * @param n The which is to changed by the zoom factor.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public int convertReverse(int n);

}