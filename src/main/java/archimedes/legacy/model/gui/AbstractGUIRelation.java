/*
 * AbstractGUIRelation.java
 *
 * 17.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model.gui;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import corent.base.Direction;

/**
 * An abstract implementation of the <CODE>GUIRelationModel</CODE>.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.05.2013 - Added.
 */

abstract public class AbstractGUIRelation implements GUIRelationModel {

	private Hashtable<GUIViewModel, Direction[]> direction = new Hashtable<GUIViewModel, Direction[]>();
	private Hashtable<GUIViewModel, int[]> offset = new Hashtable<GUIViewModel, int[]>();
	private Hashtable<GUIViewModel, List> points = new Hashtable<GUIViewModel, List>();
	private int lastMode = -1;

	/**
	 * @changed OLI 21.05.2013 - Added.
	 */
	@Override
	public void addPoint(GUIViewModel view, Point p) {
		List<Point> ps = this.points.get(view);
		if (ps == null) {
			ps = new Vector();
			this.points.put(view, ps);
		}
		ps.add(p);
	}

	/**
	 * @changed OLI 22.05.2013 - Added.
	 */
	@Override
	public void addPoint(GUIViewModel view, int x, int y) {
		this.addPoint(view, new Point(x, y));
	}

	/**
	 * @changed OLI 17.05.2013 - Added.
	 */
	@Override
	public boolean collisionWithNeighbour(GUIViewModel view, Point p) {
		Rectangle r = new Rectangle((int) p.getX() - HIT_TOLERANCE / 2, (int) p.getY() - HIT_TOLERANCE / 2,
				HIT_TOLERANCE, HIT_TOLERANCE);
		List<Point> points = this.getPointsForViewList(view);
		int index = points.indexOf(p);
		if (index > -1) {
			if ((index > 0) && r.contains(points.get(index - 1))) {
				return true;
			}
			if ((index < points.size() - 1) && r.contains(points.get(index + 1))) {
				return true;
			}
		}
		return false;
	}

	private List<Point> getPointsForViewList(GUIViewModel view) {
		return this.getPointsForView(view);
	}

	/**
	 * @changed OLI 17.05.2013 - Added.
	 */
	@Override
	public void computeOffset(GUIViewModel view, GUIObjectModel object, int x, int y) {
		int mode = -1;
		if (this.getEndPoint(0) == this.getEndPoint(1)) {
			mode = this.lastMode;
			List<Point> lp = Arrays.asList(new Point[] { this.getPoint(view, 0), this.getPoint(view, 1) });
			for (Point p : lp) {
				int xo = Math.max(p.x, x) - Math.min(p.x, x);
				int yo = Math.max(p.y, y) - Math.min(p.y, y);
				if ((xo <= GUIRelationModel.HIT_TOLERANCE) && (yo <= GUIRelationModel.HIT_TOLERANCE)) {
					mode = (p == lp.get(0) ? 0 : 1);
					this.lastMode = mode;
					break;
				}
			}
		} else {
			if (object == this.getEndPoint(0)) {
				mode = 0;
			} else if (object == this.getEndPoint(1)) {
				mode = 1;
			}
		}
		if ((mode == 0) || (mode == 1)) {
			if (x < (int) object.getX(view)) {
				x = (int) object.getX(view);
			}
			if (y < (int) object.getY(view)) {
				y = (int) object.getY(view);
			}
			if (x > (int) object.getX(view) + (int) object.getWidth()) {
				x = (int) object.getX(view) + (int) object.getWidth();
			}
			if (y > (int) object.getY(view) + (int) object.getHeight()) {
				y = (int) object.getY(view) + (int) object.getHeight();
			}
			Polygon p = new Polygon(
					new int[] { (int) object.getX(view) + 1, (int) (object.getX(view) + object.getWidth() - 2),
							(int) (object.getX(view) + (object.getWidth() / 2)) },
					new int[] { (int) object.getY(view) + 1, (int) object.getY(view) + 1,
							(int) (object.getY(view) + (object.getHeight() / 2)) },
					3);
			if (p.contains(x, y)) {
				this.setDirection(view, mode, Direction.UP);
				this.setOffset(view, mode, x - (int) object.getX(view));
				return;
			}
			int h = (int) (object.getY(view) + object.getHeight());
			p = new Polygon(
					new int[] { (int) object.getX(view) + 1, (int) (object.getX(view) + object.getWidth() - 1),
							(int) (object.getX(view) + (object.getWidth() / 2)) },
					new int[] { h - 1, h - 1, h - (int) (object.getHeight() / 2) }, 3);
			if (p.contains(x, y)) {
				this.setDirection(view, mode, Direction.DOWN);
				this.setOffset(view, mode, x - (int) object.getX(view));
				return;
			}
			p = new Polygon(
					new int[] { (int) object.getX(view) + 1, (int) object.getX(view) + 1,
							(int) (object.getX(view) + (object.getWidth() / 2)) },
					new int[] { (int) object.getY(view) + 1, (int) (object.getY(view) + object.getHeight() - 2),
							(int) (object.getY(view) + (object.getHeight() / 2)) },
					3);
			if (p.contains(x, y)) {
				this.setDirection(view, mode, Direction.LEFT);
				this.setOffset(view, mode, y - (int) object.getY(view));
				return;
			}
			h = (int) (object.getX(view) + object.getWidth());
			p = new Polygon(new int[] { h - 1, h - 1, h - (int) (object.getWidth() / 2) },
					new int[] { (int) object.getY(view) + 1, (int) (object.getY(view) + object.getHeight() - 2),
							(int) (object.getY(view) + (object.getHeight() / 2)) },
					3);
			if (p.contains(x, y)) {
				this.setDirection(view, mode, Direction.RIGHT);
				this.setOffset(view, mode, y - (int) object.getY(view));
				return;
			}
		}
	}

	/**
	 * @changed OLI 17.05.2013 - Added.
	 */
	@Override
	public Point getPoint(GUIViewModel view, int x, int y) {
		Rectangle r = new Rectangle(x - HIT_TOLERANCE / 2, y - HIT_TOLERANCE / 2, HIT_TOLERANCE, HIT_TOLERANCE);
		GeneralPath gp = new GeneralPath();
		List<Point> ps = this.getPointsForViewList(view);
		int len = ps.size();
		for (Point point : ps) {
			if (r.contains(point)) {
				return point;
			}
		}
		Point p = null;
		if (len > 0) {
			p = ps.get(0);
			gp.moveTo((float) p.getX(), (float) p.getY());
			for (int i = 1; i < len; i++) {
				p = ps.get(i);
				gp.lineTo((float) p.getX(), (float) p.getY());
				if (gp.intersects((double) x - HIT_TOLERANCE / 2, (double) y - HIT_TOLERANCE / 2, HIT_TOLERANCE,
						HIT_TOLERANCE)) {
					p = new Point(x, y);
					List<Point> ps0 = this.points.get(view);
					if (ps0 != null) {
						ps0.add(i - 1, p);
					} else {
						ps0 = new Vector<Point>();
						ps0.add(p);
						this.points.put(view, ps0);
					}
					break;
				}
			}
		}
		return p;
	}

	private List<Point> getPoints(GUIViewModel view) {
		List<Point> l = new Vector<Point>();
		l.add(this.getPoint(view, 0));
		List<Point> ps = this.points.get(view);
		if (ps == null) {
			ps = new Vector<Point>();
		}
		for (int i = 0, len = ps.size(); i < len; i++) {
			l.add(ps.get(i));
		}
		l.add(this.getPoint(view, 1));
		return l;
	}

	protected Map<GUIViewModel, List> getAllPoints() {
		return this.points;
	}

	/**
	 * Returns all points of the relation with the view information.
	 *
	 * @return A list with all information about the points of all views.
	 *
	 * @changed OLI 22.05.2013 - Added.
	 */
	@Override
	public GUIRelationPoint[] getAllPointInformation() {
		List<GUIRelationPoint> l = new Vector<GUIRelationPoint>();
		List points = null;
		for (Iterator<GUIViewModel> i = this.getAllPoints().keySet().iterator(); i.hasNext();) {
			GUIViewModel view = i.next();
			points = this.getAllPoints().get(view);
			for (int j = 0, len = points.size(); j < len; j++) {
				Point p = (Point) points.get(j);
				l.add(new GUIRelationPoint(view, p));
			}
		}
		return l.toArray(new GUIRelationPoint[0]);
	}

	/**
	 * @changed OLI 17.05.2013 - Added.
	 */
	@Override
	public Direction getDirection(GUIViewModel view, int i) {
		Direction[] da = this.direction.get(view);
		if (da == null) {
			return Direction.UP;
		}
		return da[i];
	}

	/**
	 * Returns the end point of the relation (0 = referencer, 1 = referenced).
	 *
	 * @param entPointNr The number of the end point (0 = referencer, 1 = referenced).
	 * @return The end point of the relation (0 = referencer, 1 = referenced).
	 *
	 * @changed OLI 17.05.2013 - Added.
	 */
	abstract public GUIObjectModel getEndPoint(int endPointNr);

	protected Point getPoint(GUIViewModel view, int endPoint) {
		Point p = new Point();
		GUIObjectModel o = this.getEndPoint(endPoint);
		if (this.getDirection(view, endPoint) == Direction.UP) {
			p.move(o.getX(view) + this.getOffset(view, endPoint), o.getY(view));
		} else if (this.getDirection(view, endPoint) == Direction.RIGHT) {
			p.move(o.getX(view) + o.getWidth(), o.getY(view) + this.getOffset(view, endPoint));
		} else if (this.getDirection(view, endPoint) == Direction.DOWN) {
			p.move(o.getX(view) + this.getOffset(view, endPoint), o.getY(view) + o.getHeight());
		} else if (this.getDirection(view, endPoint) == Direction.LEFT) {
			p.move(o.getX(view), o.getY(view) + this.getOffset(view, endPoint));
		}
		return p;
	}

	/**
	 * @changed OLI 22.05.2013 - Added.
	 */
	@Override
	public int getOffset(GUIViewModel view, int i) {
		int[] oa = this.offset.get(view);
		if (oa == null) {
			return 0;
		}
		return oa[i];
	}

	/**
	 * @changed OLI 17.05.2013 - Added.
	 */
	@Override
	public List<Point> getPointsForView(GUIViewModel view) {
		List<Point> l = new Vector<Point>();
		for (Object o : this.getPoints(view)) {
			l.add((Point) o);
		}
		return l;
	}

	/**
	 * @changed OLI 17.05.2013 - Added.
	 */
	@Override
	public GUIObjectModel getReferencedObject() {
		return this.getEndPoint(1);
	}

	/**
	 * @changed OLI 17.05.2013 - Added.
	 */
	@Override
	public GUIObjectModel getReferencerObject() {
		return this.getEndPoint(0);
	}

	/**
	 * @changed OLI 21.05.2013 - Added.
	 */
	@Override
	public Point[] getShapePointsForView(GUIViewModel view) {
		List<Point> v = new Vector<Point>();
		v.add(this.getPoint(view, 0));
		List<Point> ps = this.points.get(view);
		if (ps == null) {
			ps = new Vector<Point>();
		}
		for (int i = 0, len = ps.size(); i < len; i++) {
			v.add(ps.get(i));
		}
		v.add(this.getPoint(view, 1));
		for (int i = ps.size() - 1; i >= 0; i--) {
			v.add(ps.get(i));
		}
		return v.toArray(new Point[0]);
	}

	/**
	 * @changed OLI 21.05.2013 - Added.
	 */
	@Override
	public List<GUIViewModel> getViews() {
		List<GUIViewModel> l = new Vector<GUIViewModel>();
		for (Enumeration<GUIViewModel> e = this.direction.keys(); e.hasMoreElements();) {
			l.add(e.nextElement());
		}
		for (Enumeration<GUIViewModel> e = this.offset.keys(); e.hasMoreElements();) {
			GUIViewModel vm = e.nextElement();
			if (!l.contains(vm)) {
				l.add(vm);
			}
		}
		for (Enumeration<GUIViewModel> e = this.points.keys(); e.hasMoreElements();) {
			GUIViewModel vm = e.nextElement();
			if (!l.contains(vm)) {
				l.add(vm);
			}
		}
		return l;
	}

	/**
	 * @changed OLI 17.05.2013 - Added.
	 */
	@Override
	public boolean isEndPoint(GUIViewModel view, Point p) {
		List<Point> ps = this.points.get(view);
		if (ps != null) {
			return !ps.contains(p);
		}
		return true;
	}

	/**
	 * @changed OLI 17.05.2013 - Added.
	 */
	@Override
	public void removePoint(GUIViewModel view, Point p) {
		List<Point> ps = this.points.get(view);
		if (ps != null) {
			ps.remove(p);
		}
	}

	/**
	 * @changed OLI 17.05.2013 - Added.
	 */
	@Override
	public void setDirection(GUIViewModel view, int i, Direction d) {
		Direction[] da = this.direction.get(view);
		if (da == null) {
			da = new Direction[] { Direction.UP, Direction.UP };
			this.direction.remove(view);
			this.direction.put(view, da);
		}
		da[i] = d;
	}

	/**
	 * @changed OLI 17.05.2013 - Added.
	 */
	@Override
	public void setOffset(GUIViewModel view, int i, int o) {
		int[] oa = this.offset.get(view);
		if (oa == null) {
			oa = new int[] { 0, 0 };
			this.offset.remove(view);
			this.offset.put(view, oa);
		}
		oa[i] = o;
	}

}