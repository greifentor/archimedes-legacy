/*
 * OrderMember.java
 *
 * 19.07.2005
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import java.util.Vector;

import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.model.ColumnModel;
import archimedes.model.OrderMemberModel;
import corent.base.Attributed;
import corent.base.StrUtil;
import corent.db.OrderClauseDirection;
import corent.djinn.DefaultComponentFactory;
import corent.djinn.DefaultEditorDescriptor;
import corent.djinn.DefaultEditorDescriptorList;
import corent.djinn.DefaultLabelFactory;
import corent.djinn.Editable;
import corent.djinn.EditorDescriptorList;

/**
 * Diese Klasse stellt eine Musterimplementierung des OrderMemberModels zur
 * Nutzung von Archimedes in Verbindung mit der Standard-corent-Bibliothek zur
 * Verf&uuml;gung.<BR>
 * <HR>
 * 
 * @author ollie
 * 
 */

public class OrderMember implements Attributed, Editable, OrderMemberModel {

	/** Ein Bezeichner zum Zugriff auf die Tabellenspalte des OrderMembers. */
	public static final int ID_ORDERCOLUMN = 0;
	/** Ein Bezeichner zum Zugriff auf die Sortierungsrichtung. */
	public static final int ID_ORDERDIRECTION = 1;

	/* Die Sortierrichtung zum OrderMember. */
	private OrderClauseDirection direction = OrderClauseDirection.ASC;
	/* Die Tabellenspalte zum OrderMember. */
	private TabellenspaltenModel tsm = null;

	/** Generiert ein OrderMember mit Default-Werten. */
	public OrderMember() {
		super();
	}

	/** Generiert ein OrderMember mit der &uuml;bergebenen Tabellenspalte. */
	public OrderMember(TabellenspaltenModel tsm) {
		super();
		this.setOrderColumn(tsm);
	}

	/** Generiert ein OrderMember mit der &uuml;bergebenen Tabellenspalte. */
	public OrderMember(ColumnModel cm) {
		super();
		this.setOrderColumn((TabellenspaltenModel) cm);
	}

	/* Ueberschreiben von Methoden der Superklasse (Pflicht). */

	public boolean equals(Object o) {
		if (!(o instanceof OrderMember)) {
			return false;
		}
		OrderMember om = (OrderMember) o;
		return this.getOrderColumn().equals(om.getOrderColumn()) && this.getOrderDirection() == om.getOrderDirection();
	}

	public String toString() {
		return "" + (this.getOrderColumn() != null ? this.getOrderColumn().getFullName() : "-/-") + " ("
				+ this.getOrderDirection() + ")";
	}

	/* Implementierung des Interfaces Attributed */

	public Object get(int id) throws IllegalArgumentException {
		switch (id) {
		case ID_ORDERCOLUMN:
			return this.getOrderColumn();
		case ID_ORDERDIRECTION:
			return this.getOrderDirection();
		}
		throw new IllegalArgumentException("Klasse OrderMember verfuegt nicht ueber ein Attribut " + id + " (get)!");
	}

	public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
		switch (id) {
		case ID_ORDERCOLUMN:
			this.setOrderColumn((TabellenspaltenModel) value);
			return;
		case ID_ORDERDIRECTION:
			this.setOrderDirection((OrderClauseDirection) value);
			return;
		}
		throw new IllegalArgumentException("Klasse OrderMember verfuegt nicht ueber ein Attribut " + id + " (set)!");
	}

	/* Implementierung des Interfaces Editable. */

	public EditorDescriptorList getEditorDescriptorList() {
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		DefaultComponentFactory dcfcol = new DefaultComponentFactory(this.getOrderColumn().getTabelle().getDiagramm()
				.getAllFields());
		Vector v = new Vector();
		v.addElement(OrderClauseDirection.ASC);
		v.addElement(OrderClauseDirection.DESC);
		DefaultComponentFactory dcfdir = new DefaultComponentFactory(v);
		DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_ORDERCOLUMN, dlf, dcfcol, "Tabellenspalte", 'T', null,
				StrUtil.FromHTML("Die Tabellenspalte, &uuml;ber " + "die sortiert werden soll"), true));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_ORDERDIRECTION, dlf, dcfdir, "Richtung", 'R', null,
				StrUtil.FromHTML("Die Richtung, in der die Sortierung " + "durchgef&uuml;hrt werden soll")));
		return dedl;
	}

	public Object createObject() {
		return new OrderMember();
	}

	public Object createObject(Object blueprint) throws ClassCastException {
		OrderMember om = (OrderMember) blueprint;
		OrderMember newone = new OrderMember();
		newone.setOrderColumn(om.getOrderColumn());
		newone.setOrderDirection(om.getOrderDirection());
		return newone;
	}

	/* Implementierung des Interfaces NReferenzModel. */

	public TabellenspaltenModel getOrderColumn() {
		return this.tsm;
	}

	public void setOrderColumn(TabellenspaltenModel ts) {
		this.tsm = ts;
	}

	public OrderClauseDirection getOrderDirection() {
		return this.direction;
	}

	public void setOrderDirection(OrderClauseDirection d) {
		this.direction = d;
	}

	/**
	 * @changed OLI 02.06.2016 - Added.
	 */
	@Override
	public void setOrderColumn(ColumnModel c) {
		this.tsm = (TabellenspaltenModel) c;
	}

}