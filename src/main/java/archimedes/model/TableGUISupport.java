/*
 * TableGUISupport.java
 *
 * 20.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.model;

import java.awt.*;

/**
 * An interface with necessary method for table GUI support.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 20.06.2016 - Added.
 */

public interface TableGUISupport {

    /**
     * Returns the color for the background of the object in the diagram.
     *
     * @return The color for the background of the object in the diagram.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public Color getBackgroundColor();

    /**
     * Returns the color for the font of the object in the diagram.
     *
     * @return The color for the font of the object in the diagram.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public Color getFontColor();

    /**
     * Sets a new color for the background of the object in the diagram.
     *
     * @param backgroundColor The new background color.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public void setBackgroundColor(Color backgroundColor);

    /**
     * Sets a new color for the font of the object in the diagram.
     *
     * @param fontColor The new font color.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public void setFontColor(Color fontColor);

}