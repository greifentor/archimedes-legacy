/*
 * GUIRelationPoint.java
 *
 * 22.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model.gui;


import java.awt.*;


/**
 * A container for information about points of relations in the GUI.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 22.05.2013 - Added.
 */

public class GUIRelationPoint {

    public GUIViewModel view = null;
    public Point point = null;

    public GUIRelationPoint(GUIViewModel view, Point point) { 
        super();
        this.view = view;
        this.point = point;
    }

    public GUIRelationPoint(GUIViewModel view, int x, int y) { 
        this(view, new Point(x, y));
    }

}