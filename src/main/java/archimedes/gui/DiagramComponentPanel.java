/*
 * DiagramComponentPanel.java
 *
 * 22.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.gui;


import static corentx.util.Checks.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import archimedes.gui.diagram.*;
import archimedes.model.gui.*;
import archimedes.transfer.*;

import javax.swing.*;
import javax.swing.border.*;

import org.apache.log4j.*;

import corent.base.*;


/**
 * The panel which contains a diagram component.
 *
 * @param <T> An enumeration which contains the identifiers for creating objects.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 22.05.2013 - Added.
 * @changed OLI 18.05.2016 - Renamed to "DiagramComponentPanel" and extended by the logic for
 *         the "DiagramComponentPanelListener".
 */

public class DiagramComponentPanel<T extends Enum<?>> extends JPanel
        implements MouseListener, StatusBarOwner {

    private static final Logger LOG = Logger.getLogger(DiagramComponentPanel.class);

    private ComponentDiagramm<T> componentDiagram = null;
    private JLabel labelStatus = null;
    private JLabel labelWarnings = null;
    private List<DiagramComponentPanelListener> listeners =
            new LinkedList<DiagramComponentPanelListener>();
    private StatusBarRenderer statusBarRenderer = null;

    /**
     * Creates a new panel with a diagram component.
     *
     * @param vm The view of the diagram which is to show in the component.
     * @param width The width of the draw panel.
     * @param height The height of the draw panel.
     * @param diagram The diagram model which is to show in the panel. 
     * @param guiObjectCreator A factory to create new objects for the draw panel.
     * @param componentDiagramListener A listener for the events of the component diagram.
     * @param statusBarRenderer A renderer for the status bar contents.
     * @param copyAndPasteController A controller for copy and paste operations.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public DiagramComponentPanel(GUIViewModel vm, int width, int height,
            GUIDiagramModel diagram, GUIObjectCreator<T> guiObjectCreator,
            ComponentDiagramListener componentDiagramListener,
            StatusBarRenderer statusBarRenderer, CopyAndPasteController copyAndPasteController)
            {
        super(new BorderLayout());
        this.statusBarRenderer = statusBarRenderer;
        this.componentDiagram = new ComponentDiagramm<T>(vm, width, height, diagram,
                guiObjectCreator, componentDiagramListener, this, copyAndPasteController);
        this.labelStatus = new JLabel(":o)");
        this.labelWarnings = new JLabel("");
        this.labelWarnings.addMouseListener(this);
        JPanel panelStatus = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
        panelStatus.setBorder(new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, 
                Constants.VGAP)); 
        panelStatus.add(this.labelStatus, BorderLayout.WEST);
        panelStatus.add(this.labelWarnings, BorderLayout.EAST);
        this.add(new JScrollPane(this.componentDiagram));
        this.add(panelStatus, BorderLayout.SOUTH);
    }

    /**
     * Adds the passed listener to the listeners which observe the component if not already
     * added.
     *
     * @param l The listener to add.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public void addDiagramComponentPanelListener(DiagramComponentPanelListener l) {
        ensure(l != null, "listener to add cannot be null.");
        if (!this.listeners.contains(l)) {
            this.listeners.add(l);
        }
    }

    /**
     * Repaints the diagram component.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public void doRepaint() {
        this.componentDiagram.doRepaint();
    }

    /**
     * Fires the passed event to the listeners which are observing the component.
     *
     * @param event The event which is to fire.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    protected void fireDiagramComponentPanelEvent(DiagramComponentPanelEvent event) {
        try {
            for (DiagramComponentPanelListener l : this.listeners) {
                try {
                    l.eventDetected(event);
                } catch (Exception e) {
                    LOG.error("error occured while processing event for listener: " + l
                            + ", event: " + event);
                }
            }
        } catch (Exception e) {
            LOG.error("error occured while processing event for listeners: " + event);
        }
    }

    /**
     * Returns the diagram component.
     *
     * @return The diagram component.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    public ComponentDiagramm<T> getComponentDiagram() {
        return this.componentDiagram;
    }

    /**
     * Returns the diagram which is the source of the GUI component.
     *
     * @return The diagram which is the source of the GUI component.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public GUIDiagramModel getDiagram() {
        return this.componentDiagram.getDiagramm();
    }

    /**
     * Returns the height of the draw panel.
     *
     * @return The height of the draw panel.
     *
     * @changed OLI 23.05.2013 - Added.
     */
    public int getImgHeight() {
        return this.componentDiagram.getHeight();
    }

    /**
     * Returns the width of the draw panel.
     *
     * @return The width of the draw panel.
     *
     * @changed OLI 23.05.2013 - Added.
     */
    public int getImgWidth() {
        return this.componentDiagram.getWidth();
    }

    /**
     * Returns the coordinates of the left upper corner of the page with the passed number.
     *
     * @param page The side which the coordinates are to return for.
     * @return The coordinates of the left upper corner of the page with the passed number.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public Point getLeftUpper(int page) {
        return this.componentDiagram.getLeftUpper(page);
    }

    /**
     * Returns the page to print with the passed number.
     *
     * @param page The page number to print.
     * @return The page to print with the passed number.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public int getPrintPage(int page) {
        return this.componentDiagram.getPrintPage(page);
    }

    /**
     * Returns the count of the pages to print.
     *
     * @return The count of the pages to print.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public int getPrintPageCount() {
        return this.componentDiagram.getPrintPageCount();
    }

    /**
     * Returns the raster width.
     *
     * @return The raster width.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public int getRasterWidth() {
        return this.componentDiagram.getRasterWidth();
    }

    /**
     * @changed OLI 22.05.2013 - Added.
     */
    @Override public StatusBarRenderer getRenderer() {
        return this.statusBarRenderer;
    }

    /**
     * Gets the current view from the component.
     *
     * @return The current view from the component.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public GUIViewModel getView() {
        return this.componentDiagram.getView();
    }

    /**
     * @changed OLI 18.05.2016 - Added.
     */
    @Override public void mouseClicked(MouseEvent e) {
        this.fireDiagramComponentPanelEvent(new DiagramComponentPanelEvent(
                DiagramComponentPanelEvent.Type.WARNINGS_CLICKED, e.getClickCount()));
    }

    /**
     * @changed OLI 18.05.2016 - Added.
     */
    @Override public void mouseEntered(MouseEvent e) {
    }

    /**
     * @changed OLI 18.05.2016 - Added.
     */
    @Override public void mouseExited(MouseEvent e) {
    }

    /**
     * @changed OLI 18.05.2016 - Added.
     */
    @Override public void mousePressed(MouseEvent e) {
    }

    /**
     * @changed OLI 18.05.2016 - Added.
     */
    @Override public void mouseReleased(MouseEvent e) {
    }

    /**
     * Paints the diagram in the passed graphic context (e. g. to print the diagram.
     *
     * @param g The graphics context to print into.
     * @param raster Set this flag to get the raster printed also.
     * @param printout Set this flag if the painting is required for printing or graphic export.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public void paint(Graphics g, boolean raster, boolean printout) {
        this.componentDiagram.paint(g, raster, printout);
    }

    /**
     * Removes all points from all relations shown in the current view.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public void removeAllAnglesFromRelations() {
        this.componentDiagram.removeAllAnglesFromRelations();
    }

    /**
     * Removes the passed listener from the listeners which observe the component.
     *
     * @param l The listener to add.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public void removeDiagramComponentPanelListener(DiagramComponentPanelListener l) {
        ensure(l != null, "listener to remove cannot be null.");
        this.listeners.remove(l);
    }

    /**
     * Sets the passed diagram as new source for the draw panel of the panel.
     *
     * @param diagram The diagram as new source for the draw panel of the panel.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public void setDiagram(GUIDiagramModel diagram) {
        this.componentDiagram.setDiagramm(diagram);
    }

    /**
     * Sets a new identifier for the type to create new objects.
     *
     * @param type The identifier for creating the next GUI object.
     *
     * @changed OLI 29.05.2013 - Added.
     */
    public void setGUIObjectToCreateIdentifier(T type) {
        this.componentDiagram.setGUIObjectToCreateIdentifier(type);
    }

    /**
     * Activates or deactivates the insert mode for the component.
     * 
     * @param standardMode The new state for the insert mode.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public void setInsertMode(boolean insertMode) {
        this.componentDiagram.setInsertMode(insertMode);
    }

    /**
     * Sets the raster width.
     *
     * @param rasterWidth The new raster width.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public void setRasterWidth(int rasterWidth) {
        this.componentDiagram.setRasterWidth(rasterWidth);
    }

    /**
     * Sets the smart positioner radius.
     *
     * @param smartPosRadius The new smart positioner radius.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public void setSmartPosRadius(int smartPosRadius) {
        this.componentDiagram.setSmartPosRadius(smartPosRadius);
    }

    /**
     * Activates or deactivates the standard mode for the component.
     * 
     * @param standardMode The new state for the standard mode.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public void setStandardMode(boolean standardMode) {
        this.componentDiagram.setStandardMode(standardMode);
    }

    /**
     * Sets the view as the currently shown view of the diagram.
     *
     * @param view The view which is to show in the component.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public void setView(GUIViewModel view) {
        this.componentDiagram.setView(view);
    }

    /**
     * Sets the passed color as foreground color for the warning label.
     *
     * @param color The new foreground color for the warning label.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    public void setWarningLabelForeGround(Color color) {
        this.labelWarnings.setForeground(color);
    }

    /**
     * Sets the passed zoom factor for the component.
     *
     * @param zoomFactor The new zoom factor for the component.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public void setZoomFactor(double zoomFactor) {
        this.componentDiagram.setZoomfactor(zoomFactor);
    }

    /**
     * Sets the view so, that the object with the passed name is shown.
     *
     * @param name The name of the object which is to show.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    public void setDiagramViewToObject(String name) {
        this.componentDiagram.setDiagramViewToTable(name);
    }

    /**
     * @changed OLI 22.05.2013 - Added.
     */
    @Override public void updateStatusMessage(String message) {
        this.labelStatus.setText((message != null ? message : ":op"));
    }

    /**
     * Updates the warning message.
     *
     * @param message The message which is to update.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public void updateWarnings(String message) {
        this.labelWarnings.setText((message != null ? message : ":op"));
    }

}