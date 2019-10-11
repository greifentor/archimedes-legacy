/*
 * View.java
 *
 * 11.02.2005
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import archimedes.legacy.Archimedes;
import archimedes.legacy.gui.CommentSubEditorFactory;
import archimedes.legacy.model.MainViewModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.GUIObjectModel;
import archimedes.model.gui.GUIViewModel;
import corent.base.Attributed;
import corent.djinn.DefaultComponentFactory;
import corent.djinn.DefaultEditorDescriptor;
import corent.djinn.DefaultEditorDescriptorList;
import corent.djinn.DefaultLabelFactory;
import corent.djinn.DefaultSubEditorDescriptor;
import corent.djinn.DefaultTabDescriptor;
import corent.djinn.DefaultTabbedPaneFactory;
import corent.djinn.Editable;
import corent.djinn.EditorDescriptorList;
import corent.djinn.TabDescriptor;
import corent.djinn.TabbedPaneFactory;

/**
 * Diese Klasse repr&auml;sentiert einen View f&uuml;r Tabellen des
 * Archimedes-Systems.
 * 
 * @author ollie
 *         <P>
 * 
 * @changed <P>
 *          OLI 11.05.2008 - Erweiterung der Implementierung des Interfaces
 *          <TT>TabbedEditable</TT> um die Methode <TT>isTabEnabled(int)</TT>.
 *          <P>
 *          OLI 10.08.2008 - Erweiterung um die M&ouml;glichkeit angeben zu
 *          k&ouml;nnen, ob technische Felder in der Diagrammanzeige versteckt
 *          werden sollen.
 * 
 */

public class View implements Attributed, Editable, GUIViewModel, ViewModel {

	/** Bezeichner f&uuml;r den Zugriff auf die Eigenschaft Bezeichnung. */
	public static final int ID_BEZEICHNUNG = 0;
	/** Bezeichner f&uuml;r den Zugriff auf die Eigenschaft Kommentar. */
	public static final int ID_COMMENT = 1;
	/** Bezeichner f&uuml;r den Zugriff auf die Eigenschaft Tabellen. */
	public static final int ID_TABELLEN = 2;
	/**
	 * Ein Bezeichner zum Zugriff auf die Flagge zur Anzeige von referenzierten
	 * Spaltennamen im Diagramm.
	 */
	public static final int ID_SHOWREFERENCEDCOLUMNAMES = 3;
	/**
	 * Ein Bezeichner zum Zugriff auf die Flagge zur Anzeige von technischen
	 * Spaltennamen im Diagramm.
	 */
	public static final int ID_HIDETECHNICALFIELDS = 4;

	/** Generiert eine Instanz der Klasse mit Defaultwerten. */
	public View() {
		super();
	}

	/**
	 * Generiert eine Instanz der Klasse mit Defaultwerten.
	 * 
	 * @param bezeichnung
	 *            Eine Bezeichnung f&uuml;r die View.
	 * @param kommentar
	 *            Ein Kommentar zur View.
	 * @param isShowReferencedColumns
	 *            Mit diesem Parameter wird angegeben, ob in dem View die
	 *            FK-Referenzen mit des referenzierten Spaltennamen ausgegeben
	 *            werden sollen.
	 */
	public View(String bezeichnung, String kommentar, boolean isShowReferencedColumns) {
		super();
		this.setBezeichnung(bezeichnung);
		this.setComment(kommentar);
		this.setShowReferencedColumns(isShowReferencedColumns);
	}

	/* Accessoren und Mutatoren. */

	/**
	 * Accessor f&uuml;r das Attribut Bezeichnung.
	 * 
	 * @return Der gegenw&auml;rtige Wert des Attributs Bezeichnung.
	 */
	@Deprecated
	public String getBezeichnung() {
		if (this.name == null) {
			return null;
		}
		return this.name;
	}

	/**
	 * Mutator f&uuml;r das Attribut Bezeichnung.
	 * 
	 * @param bezeichnung
	 *            Der neue Wert des Attributs Bezeichnung.
	 */
	public void setBezeichnung(String bezeichnung) {
		if (bezeichnung == null) {
			bezeichnung = "";
		}
		this.name = bezeichnung;
	}

	/**
	 * @changed OLI 11.03.2016 - Added.
	 */
	@Override
	public String getComment() {
		return this.comment;
	}

	/**
	 * @changed OLI 11.03.2016 - Added.
	 */
	@Override
	public void setComment(String comment) {
		if (comment == null) {
			comment = "-/-";
		}
		this.comment = comment;
	}

	/* Ueberschreiben von Methoden der Superklasse. */

	public boolean equals(Object o) {
		if (!(o instanceof View)) {
			return false;
		}
		View v = (View) o;
		return this.getName().equals(v.getName());
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.getName().hashCode();
		return result;
	}

	public String toString() {
		return this.getName();
	}

	/* Implementierung des Interfaces ViewModel. */

	public Object get(int id) throws IllegalArgumentException {
		switch (id) {
		case ID_BEZEICHNUNG:
			return this.getBezeichnung();
		case ID_COMMENT:
			return this.getComment();
		case ID_TABELLEN:
			return this.getTabellen();
		case ID_SHOWREFERENCEDCOLUMNAMES:
			return new Boolean(this.isShowReferencedColumns());
		case ID_HIDETECHNICALFIELDS:
			return new Boolean(this.isHideTechnicalFields());
		}
		throw new IllegalArgumentException("Klasse View verfuegt nicht ueber ein Attribut " + id + " (get)!");
	}

	public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
		switch (id) {
		case ID_BEZEICHNUNG:
			this.setBezeichnung((String) value);
			return;
		case ID_COMMENT:
			this.setComment((String) value);
			return;
		case ID_SHOWREFERENCEDCOLUMNAMES:
			this.setShowReferencedColumns(((Boolean) value).booleanValue());
			return;
		case ID_HIDETECHNICALFIELDS:
			this.setHideTechnicalFields(((Boolean) value).booleanValue());
			return;
		}
		throw new IllegalArgumentException("Klasse View verfuegt nicht ueber ein Attribut " + id + " (set)!");
	}

	public int compareTo(Object o) {
		View v = (View) o;
		if (this instanceof MainViewModel) {
			return -1;
		}
		return this.getBezeichnung().compareTo(v.getBezeichnung());
	}

	public EditorDescriptorList getEditorDescriptorList() {
		DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_BEZEICHNUNG, dlf, dcf, "Bezeichnung", 'B', null,
				"Der Name des View"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_SHOWREFERENCEDCOLUMNAMES, dlf, dcf,
				"Referenzierte Spalten anzeigen", 'R', null, "Setzen Sie diese Flagge, um "
						+ "die Namen der durch Foreignkeys referenzierten Spalten\nim Diagramm "
						+ "angezeigt zu bekommen"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_HIDETECHNICALFIELDS, dlf, dcf,
				"Technische Spalten nicht anzeigen", 'T', null, "Setzen Sie diese Flagge, "
						+ "um die Anzeige technischer Spalten im Diagramm zu unterbinden."));
		dedl.addElement(new DefaultSubEditorDescriptor(1, this, new CommentSubEditorFactory()));
		return dedl;
	}

	public Object createObject() {
		return Archimedes.Factory.createView();
	}

	public Object createObject(Object blueprint) throws ClassCastException {
		if (!(blueprint instanceof View)) {
			throw new ClassCastException("Instance of View required!");
		}
		View st = (View) this.createObject();
		st.setBezeichnung(this.getBezeichnung());
		st.setComment(this.getComment());
		List l = st.getTabellen();
		for (int i = 0, len = this.getTabellen().size(); i < len; i++) {
			l.add(this.getTabellen().get(i));
		}
		return st;
	}

	public TabbedPaneFactory getTabbedPaneFactory() {
		return new DefaultTabbedPaneFactory(new TabDescriptor[] { new DefaultTabDescriptor("Daten", 'A', null),
				new DefaultTabDescriptor("Beschreibung", 'B', null) });
	}

	public boolean isTabEnabled(int no) {
		return true;
	}

	public boolean isSelected(Object[] criteria) throws IllegalArgumentException {
		if (((criteria != null) && (criteria.length > 0) && !(criteria[0] instanceof String))
				|| ((criteria != null) && (criteria.length > 1))) {
			throw new IllegalArgumentException("View does only except one String-criteria!");
		} else if (criteria == null) {
			return true;
		}
		StringTokenizer st = new StringTokenizer((String) criteria[0]);
		while (st.hasMoreTokens()) {
			String c = st.nextToken();
			String s = this.getBezeichnung().toLowerCase();
			if ((c == null) || (s.indexOf(c.toLowerCase()) < 0)) {
				return false;
			}
		}
		return true;
	}

	public void setName(String name) {
		this.setBezeichnung(name);
	}

	@Deprecated
	public List<TabellenModel> getTabellen() {
		return this.tables;
	}

	public void setShowReferencedColumns(boolean b) {
		this.showReferencedColumns = b;
	}

	public void setHideTechnicalFields(boolean b) {
		this.hideTechnicalFields = b;
	}

	public void setHideTransientFields(boolean b) {
		this.hideTransientFields = b;
	}

	//
	// Approved methods
	//

	private String comment = "-/-";
	private boolean hideTechnicalFields = false;
	private boolean hideTransientFields = false;
	private String name = "";
	private boolean showReferencedColumns = false;
	private Vector<TabellenModel> tables = new Vector<TabellenModel>();

	/**
	 * @changed OLI 08.05.2013 - Added.
	 */
	@Override
	public void addTable(TableModel table) throws IllegalArgumentException {
		ensure(table != null, "table to add cannot be null.");
		if (!this.tables.contains(table)) {
			this.tables.add((TabellenModel) table);
		}
	}

	/**
	 * @changed OLI 08.05.2013 - Approved.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @changed OLI 08.05.2013 - Added.
	 */
	@Override
	public TableModel[] getTables() {
		Vector<TableModel> tables = new Vector<TableModel>();
		for (TabellenModel table : this.tables) {
			tables.add((TableModel) table);
		}
		return tables.toArray(new TableModel[0]);
	}

	/**
	 * @changed OLI 08.05.2013 - Approved.
	 */
	@Override
	public boolean isHideTechnicalFields() {
		return this.hideTechnicalFields;
	}

	/**
	 * @changed OLI 11.06.2015 - Added.
	 */
	@Override
	public boolean isHideTransientFields() {
		return this.hideTransientFields;
	}

	/**
	 * @changed OLI 08.05.2013 - Approved.
	 */
	@Override
	public boolean isShowReferencedColumns() {
		return this.showReferencedColumns;
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public GUIObjectModel[] getObjects() {
		List<GUIObjectModel> l = new Vector<GUIObjectModel>();
		for (TableModel t : this.getTables()) {
			l.add((GUIObjectModel) t);
		}
		return l.toArray(new GUIObjectModel[0]);
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public void addObject(GUIObjectModel object) {
		this.addTable((TableModel) object);
	}

	/**
	 * @changed OLI 09.06.2016 - Added.
	 */
	@Override
	public void setMainView(boolean arg0) {
		// TODO Should be implemented later.
	}

	/**
	 * @changed OLI 09.06.2016 - Added.
	 */
	@Override
	public boolean isMainView() {
		return (this instanceof MainView);
	}

}