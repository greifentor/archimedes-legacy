/*
 * GUISupport.java
 *
 * 17.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.model;

import java.awt.*;

/**
 * An interface which have to be implemented by object which want to be able to show diagrams
 * on the display.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 17.06.2016 - Added.
 */

public interface DiagramGUISupport {

    /**
     * Returns the font size of the diagram headline.
     * 
     * @return The font size of the diagram headline.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public int getFontSizeDiagramHeadline();

    /**
     * Returns the font size for the subtitles of the diagram.
     *
     * @return The font size for the subtitles of the diagram.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public int getFontSizeSubtitles();

    /**
     * Returns the font size for the table contents of the diagram.
     *
     * @return The font size for the table contents of the diagram.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public int getFontSizeTableContents();

    /**
     * Returns the name of the color which is used to paint relations between the tables.
     *
     * @return The name of the color which is used to paint relations between the tables.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public Color getRelationColorRegular();

    /**
     * Returns the name of the color which is used to paint relations to external tables.
     *
     * @return The name of the color which is used to paint relations to external tables.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public Color getRelationColorToExternalTables();

    /**
     * Checks if deprecated tables should be hidden in the diagram.
     *
     * @return <CODE>true</CODE> if the deprecated tables should be hidden in the diagram.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public boolean isDeprecatedTablesHidden();

    /**
     * Checks for marking writable members in the table view.
     *
     * @return <CODE>true</CODE> if writeable members should be marked in the table view.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public boolean isMarkUpRequiredFieldNames();

    /**
     * Checks if technical fields should be painted in gray color.
     *
     * @return <CODE>true</CODE> if technical fields should be painted in gray color.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public boolean isPaintTechnicalFieldsInGray();

    /**
     * Checks if transient fields should be painted in gray color.
     *
     * @return <CODE>true</CODE> if transient fields should be painted in gray color.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public boolean isPaintTransientFieldsInGray();

    /**
     * Checks if the reference data should be shown in the tables on the diagram.
     *
     * @return <CODE>true</CODE> if the reference data should be shown in the tables on the
     *         diagram.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public boolean isShowReferencedColumns();

    /**
     * Sets a new font size for the diagram headline.
     *
     * @param fontSize The new font size for the diagram headline.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setFontSizeDiagramHeadline(int fontSize);

    /**
     * Sets a new font size for the subtitles of the diagram.
     *
     * @param fontSize The new font size for the subtitles of the diagram.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setFontSizeSubtitles(int fontSize);

    /**
     * Sets a new font size for the table contents of the diagram.
     *
     * @param fontSize The new font size for the table contents of the diagram.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setFontSizeTableContents(int fontSize);

    /**
     * Sets or resets the flag for marking writable members in the table view.
     *
     * @param markUp The new value for the flag.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public void setMarkUpRequiredFieldNames(boolean markUp);

    /**
     * Sets a new state for the flag which is checked for painting technical fields in gray
     * color.
     * 
     * @param paintTechnicalFieldsInGray Set this flag to have technical fields painted in gray
     *         color.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setPaintTechnicalFieldsInGray(boolean paintTechnicalFieldsInGray);

    /**
     * Sets a new state for the flag which is checked for painting transient fields in gray
     * color.
     * 
     * @param paintTransientFieldsInGray Set this flag to have transient fields painted in gray
     *         color.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setPaintTransientFieldsInGray(boolean paintTransientFieldsInGray);

    /**
     * Sets a new name of the color which is used to paint relations to external tables.
     *
     * @param colorName Sets a new of the color which is used to paint relations to external
     *         tables.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setRelationColorToExternalTables(Color colorName);

    /**
     * Sets a new name of the color which is used to paint relations between the tables.
     *
     * @param colorName Sets a new of the color which is used to paint relations between the
     *         tables.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setRelationColorRegular(Color colorName);

    /**
     * Sets a new state for the flag which decides if the reference data should be shown in the
     * tables on the diagram.
     *
     * @param showReferences Set this flag to get the information about the references shown in
     *         the diagram. 
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setShowReferencedColumns(boolean showReferences);

}