/*
 * Relation.java
 *
 * 21.03.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import java.awt.Point;
import java.util.Arrays;
import java.util.Vector;

import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.model.ColumnModel;
import archimedes.model.RelationModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.AbstractGUIRelation;
import archimedes.model.gui.GUIObjectModel;
import archimedes.model.gui.GUIViewModel;
import corent.base.Direction;

/**
 * Diese Klasse stellt eine Relation innerhalb der Archimedes-Applikation dar.
 * <P>
 * &Uuml;ber die Property <I>archimedes.gui.ComponentDiagramm.HIT_TOLERANCE</I>
 * kann die Entfernung beeinflu&szlig;t werden, in der eine Relation von der
 * Mouse-Position liegen mu&szlig;, um noch als getroffen zu gelten. Die
 * Defaulteinstellung liegt bei 10.
 * 
 * @author ollie
 * 
 * @changed OLI 30.09.2007 - Ersatz von Aufrufen aufgehobener Methoden in der
 *          Methode <TT>computeOffset(ViewModel, TabellenModel, int, int)</TT>.<BR>
 *          Beheben das Bugs in der Methode
 *          <TT>getPoint(ViewModel, int, int)</TT>, der zu dem nervt&ouml;tenden
 *          Verhalten in der graphischen Oberfl&auml;che f&uuml;hrte, da&szlig;
 *          man einen Punkte vor dem Endpunkt einer Relation immer erst im
 *          zweiten Anlauf gezogen bekam.<BR>
 * 
 */

public class Relation extends AbstractGUIRelation implements RelationModel {

	/** Radius in Pixels, in dem ein Punkt der Relation als getroffen gilt. */
	// public static int HIT_TOLERANCE = GUIRelationModel.HIT_TOLERANCE;

	/*
	 * Richtungen, in denen die Relation auf die Tabelle trifft
	 * (0=Referenzierende, 1=Referenzierte).
	 */
	// private Hashtable<ViewModel, Direction[]> direction = new
	// Hashtable<ViewModel, Direction[]>(
	// );
	/*
	 * Offset der Position des Auftreffens zur linken oberen Ecke
	 * (0=Referenzierende, 1=Referenzierte).
	 */
	// private Hashtable<ViewModel, int[]> offset = new Hashtable<ViewModel,
	// int[]>();
	/*
	 * Die Tabellenspalten, die von der Relation verbunden werden
	 * (0=Referenzierende, 1=Referenzierte).
	 */
	private TabellenspaltenModel[] tabellenspalte = new TabellenspaltenModel[2];

	/* Die Liste mit den Punkten, durch die die Relation laufen soll. */
	// private Hashtable<ViewModel, Vector> points = new Hashtable<ViewModel,
	// Vector>();

	/**
	 * Generiert eine Relation anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param view
	 *            Der View, auf dem die Relation erzeugt werden soll.
	 * @param t1
	 *            Die Tabellenspalte, von der die Relation ausgeht.
	 * @param direction1
	 *            Die Richtung, von der aus die Relation von der Tabelle abgehen
	 *            soll.
	 * @param offset1
	 *            Die Entfernung von der oberen bzw. linke Kante auf der Seite,
	 *            von der die Relation ausgehen soll.
	 * @param t2
	 *            Die Tabellespalte, auf die die Relation zeigt.
	 * @param direction2
	 *            Die Richtung, von der aus die Relation auf die Tabelle treffen
	 *            soll.
	 * @param offset2
	 *            Die Entfernung von der oberen bzw. linke Kante auf der Seite,
	 *            von der aus die Relation auf die Tabelle treffen soll.
	 */
	public Relation(ViewModel view, TabellenspaltenModel t1, Direction direction1, int offset1,
			TabellenspaltenModel t2, Direction direction2, int offset2) {
		super();
		this.setDirection(view, 0, direction1);
		this.setDirection(view, 1, direction2);
		this.setOffset(view, 0, offset1);
		this.setOffset(view, 1, offset2);
		this.tabellenspalte[0] = t1;
		this.tabellenspalte[1] = t2;
	}

	/**
	 * Generates a relation with the passed parameters.
	 * 
	 * @param view
	 *            The view where the reference is created for.
	 * @param t1
	 *            The column which is referencing column t2.
	 * @param direction1
	 *            The direction which the relation meets the referencing table.
	 * @param offset1
	 *            The offset of the position of the relation line at the
	 *            referencing table.
	 * @param t2
	 *            The column of the referenced table.
	 * @param direction2
	 *            The direction which the relation meets the referenced table.
	 * @param offset2
	 *            The offset of the position of the relation line at the
	 *            referenced table.
	 * @return The new relation.
	 * 
	 * @changed OLI 29.04.2013 - Added.
	 */
	public Relation(ViewModel view, ColumnModel t1, Direction direction1, int offset1, ColumnModel t2,
			Direction direction2, int offset2) {
		super();
		this.setDirection(view, 0, direction1);
		this.setDirection(view, 1, direction2);
		this.setOffset(view, 0, offset1);
		this.setOffset(view, 1, offset2);
		this.tabellenspalte[0] = (TabellenspaltenModel) t1;
		this.tabellenspalte[1] = (TabellenspaltenModel) t2;
	}

	/*
	 * Ermittelt den Bildschirm Punkt an dem die Relation auf die angegebene
	 * Tabelle trifft.
	 */
	private Point getPoint(ViewModel view, int table) {
		return super.getPoint((GUIViewModel) view, table);
	}

	/**
	 * @changed OLI 17.05.2013 - Added.
	 */
	@Override
	public GUIObjectModel getEndPoint(int endPointNr) {
		return (GUIObjectModel) this.tabellenspalte[endPointNr].getTable();
	}

	@Deprecated
	public Direction getDirection(ViewModel view, int i) {
		return super.getDirection((GUIViewModel) view, i);
	}

	@Deprecated
	public void setDirection(ViewModel view, int i, Direction d) {
		super.setDirection((GUIViewModel) view, i, d);
	}

	@Deprecated
	public int getOffset(ViewModel view, int i) {
		return super.getOffset((GUIViewModel) view, i);
	}

	@Deprecated
	public void setOffset(ViewModel view, int i, int o) {
		super.setOffset((GUIViewModel) view, i, o);
	}

	/* Ueberschreiben der Methoden der Superklasse. */

	public String toString() {
		return ((this.tabellenspalte[0].getTabelle() != null ? this.tabellenspalte[0].getTabelle().toString() : "null")
				+ "_" + (this.tabellenspalte[1].getTabelle() != null ? this.tabellenspalte[1].getTabelle().toString()
				: "null"));
	}

	/* Implementierung des Interfaces RelationModel. */

	@Deprecated
	public Vector getPoints(ViewModel view) {
		return new Vector(super.getPointsForView((GUIViewModel) view));
		/*
		 * Vector v = new Vector(); v.addElement(this.getPoint(view, 0)); Vector
		 * ps = this.points.get(view); if (ps == null) { ps = new Vector(); }
		 * for (int i = 0, len = ps.size(); i < len; i++) {
		 * v.addElement(ps.elementAt(i)); } v.addElement(this.getPoint(view,
		 * 1)); return v;
		 */
	}

	@Deprecated
	public Vector getShapePoints(ViewModel view) {
		/*
		 * Vector v = new Vector(); v.addElement(this.getPoint(view, 0)); Vector
		 * ps = this.points.get(view); if (ps == null) { ps = new Vector(); }
		 * for (int i = 0, len = ps.size(); i < len; i++) {
		 * v.addElement(ps.elementAt(i)); } v.addElement(this.getPoint(view,
		 * 1)); for (int i = ps.size()-1; i >= 0; i--) {
		 * v.addElement(ps.elementAt(i)); } return v;
		 */
		return new Vector(Arrays.asList(this.getShapePointsForView((GUIViewModel) view)));
	}

	/**
	 * @changed OLI 30.09.2007 - Debugging des etwas nervt&ouml;tenden
	 *          Verhaltens beim Einf&uuml;gen von Punkten in eine Relation, wenn
	 *          diese unmittelbar vor dem Endpunkt liegen. <BR>
	 */
	@Deprecated
	public Point getPoint(ViewModel view, int x, int y) {
		return super.getPoint((GUIViewModel) view, x, y);
	}

	@Deprecated
	public void addPoint(ViewModel view, int x, int y) {
		super.addPoint((GUIViewModel) view, new Point(x, y));
	}

	@Deprecated
	public boolean collisionWithNeighbour(ViewModel view, Point p) {
		return super.collisionWithNeighbour((GUIViewModel) view, p);
	}

	@Deprecated
	public void removePoint(ViewModel view, Point p) {
		super.removePoint((GUIViewModel) view, p);
	}

	public boolean isEndPoint(ViewModel view, Point p) {
		return super.isEndPoint((GUIViewModel) view, p);
	}

	public TabellenModel getTablePointed(ViewModel view, Point p) {
		Vector v = this.getPoints(view);
		int index = v.indexOf(p);
		if (index == 0) {
			return this.tabellenspalte[0].getTabelle();
		} else if (index == v.size() - 1) {
			return this.tabellenspalte[1].getTabelle();
		}
		return null;
	}

	/**
	 * @changed OLI 30.09.2007 - Ersetzen der aufgehobenen
	 *          <TT>inside(int, int)</TT>-Aufrufe durch solche der Methode
	 *          <TT>contains(int, int)</TT>.<BR>
	 */
	@Deprecated
	public void computeOffset(ViewModel view, TabellenModel t, int x, int y) {
		super.computeOffset((GUIViewModel) view, t, x, y);
	}

	@Deprecated
	public Direction getDirection(ViewModel view, TabellenModel t) throws IllegalArgumentException {
		if (this.tabellenspalte[0].getTabelle() == t) {
			return this.getDirection(view, 0);
		} else if (this.tabellenspalte[1].getTabelle() == t) {
			return this.getDirection(view, 1);
		}
		throw new IllegalArgumentException("Tabelle nicht mit Relation verbunden!");
	}

	/**
	 * @changed OLI 29.04.2013 - Added.
	 */
	public Direction getDirection(ViewModel view, TableModel t) throws IllegalArgumentException {
		if (this.tabellenspalte[0].getTable() == t) {
			return this.getDirection(view, 0);
		} else if (this.tabellenspalte[1].getTable() == t) {
			return this.getDirection(view, 1);
		}
		throw new IllegalArgumentException("Table " + t.getName() + " is not connected with " + "relation!");
	}

	@Deprecated
	public int getOffset(ViewModel view, TabellenModel t) throws IllegalArgumentException {
		if (this.tabellenspalte[0].getTabelle() == t) {
			return this.getOffset(view, 0);
		} else if (this.tabellenspalte[1].getTabelle() == t) {
			return this.getOffset(view, 1);
		}
		throw new IllegalArgumentException("Tabelle nicht mit Relation verbunden!");
	}

	/**
	 * @changed OLI 29.04.2013 - Added.
	 */
	public int getOffset(ViewModel view, TableModel t) throws IllegalArgumentException {
		if (this.tabellenspalte[0].getTable() == t) {
			return this.getOffset(view, 0);
		} else if (this.tabellenspalte[1].getTable() == t) {
			return this.getOffset(view, 1);
		}
		throw new IllegalArgumentException("Table " + t.getName() + " is not connected with " + "relation!");
	}

	public TabellenspaltenModel getTabellenspalte(TabellenModel t) {
		if (this.tabellenspalte[0].getTabelle() == t) {
			return this.tabellenspalte[0];
		} else if (this.tabellenspalte[1].getTabelle() == t) {
			return this.tabellenspalte[1];
		}
		throw new IllegalArgumentException("Tabelle nicht mit Relation verbunden!");
	}

	@Deprecated
	public TabellenspaltenModel getReferenced() {
		return (TabellenspaltenModel) this.tabellenspalte[1];
	}

	@Deprecated
	public TabellenspaltenModel getReferencer() {
		return (TabellenspaltenModel) this.tabellenspalte[0];
	}

	private void setTabellenspalte(TabellenspaltenModel tsm, int table) {
		if (tsm != null) {
			if (tsm.getTabelle() != this.tabellenspalte[table].getTabelle()) {
				this.tabellenspalte[table] = tsm;
			}
		}
	}

	public void setReferencer(TabellenspaltenModel tsm) {
		this.setTabellenspalte(tsm, 0);
	}

	public void setReferenced(TabellenspaltenModel tsm) {
		this.setTabellenspalte(tsm, 1);
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public GUIObjectModel getObjectPointed(GUIViewModel view, Point p) {
		return this.getTablePointed((ViewModel) view, p);
	}

	/**
	 * @changed OLI 02.06.2016 - Added.
	 */
	@Override
	public ColumnModel getTabellenspalte(TableModel t) {
		return (ColumnModel) this.getTabellenspalte((TabellenModel) t);
	}

	/**
	 * @changed OLI 02.06.2016 - Added.
	 */
	@Override
	public void setReferenced(ColumnModel c) {
		this.setReferenced((TabellenspaltenModel) c);
	}

	/**
	 * @changed OLI 02.06.2016 - Added.
	 */
	@Override
	public void setReferencer(ColumnModel c) {
		this.setReferencer((TabellenspaltenModel) c);
	}

}