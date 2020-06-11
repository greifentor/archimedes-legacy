/*
 * PanelListProvider.java
 *
 * 16.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.model;


/**
 * An interface of object which provide lists of panels.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 16.06.2016 - Added.
 */

public interface PanelListProvider {

    /**
     * Adds the passed panel to the table. A already existing panel will not be set twice.
     *
     * @param panel The panel which is to add.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public void addPanel(PanelModel panel)
            throws IllegalArgumentException;

    /**
     * Returns the panel at the passed position in the panel list of the table.
     *
     * @param i The position whose panel is to return.
     * @return The panel at the passed position in the panel list of the table.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public PanelModel getPanelAt(int i);

    /**
     * Returns the first panel found with the passed name, if there is one.
     *
     * @param name The name of the panel which is to return. 
     * @return The first panel found with the passed name, if there is one. If not,
     *         <CODE>null</CODE> is returned.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public PanelModel getPanelByName(String name);

    /**
     * Returns the count of the panels of the table.
     *
     * @return The count of the panels of the table.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public int getPanelCount();

    /**
     * Returns the position of the passed panel in the list of panel.
     *
     * @param panel The panel whose position is to return.
     * @return The position of the passed panel in the list of panel or <CODE>-1</CODE> if the
     *         panel is not a member of the panel list.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public int getPanelPosition(PanelModel panel);

    /**
     * Returns an array with all panels from the table. The array shows the panels assorted by
     * their names.
     *
     * @return An array with the panels of the table.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public PanelModel[] getPanels();

    /**
     * Returns all panels found with the passed name, if there is at least one. The array shows
     * the panels assorted by their names.
     *
     * @param name The name of the panel which is to return. 
     * @return All panels found with the passed name, if there is at least one. Otherwise an
     *         empty array will be returned.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public PanelModel[] getPanelsByName(String name);

    /**
     * Checks if at least one panel with the passed name is set for the object.
     *
     * @param panelName The name of the panel which is to check for being set.
     * @return <CODE>true</CODE> if at least one panel is set with the passed name.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public boolean isPanelSet(String panelName);

    /**
     * Removes the passed panel from the list of the panels of the table. 
     *
     * @param panel The panel which is to remove from the list of the panels which the table
     *         belongs to.
     *
     * @changed OLI 16.06.2016 - Approved.
     */
    abstract public void removePanel(PanelModel panel);

}