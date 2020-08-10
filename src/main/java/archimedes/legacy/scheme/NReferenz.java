/*
 * NReferenz.java
 *
 * 19.10.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import java.util.Arrays;
import java.util.Vector;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.NReferenceModel;
import archimedes.legacy.model.NReferencePanelType;
import archimedes.legacy.model.NReferenzModel;
import archimedes.legacy.model.PanelModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import corent.base.StrUtil;
import corent.djinn.DefaultComponentFactory;
import corent.djinn.DefaultEditorDescriptor;
import corent.djinn.DefaultEditorDescriptorList;
import corent.djinn.DefaultLabelFactory;
import corent.djinn.EditorDescriptorList;

/**
 * Diese Klasse stellt eine Musterimplementierung des NReferenzModels zur
 * Nutzung von Archimedes in Verbindung mit der Standard-corent-Bibliothek zur
 * Verf&uuml;gung.<BR>
 * <HR>
 * 
 * @author ollie
 * 
 */

public class NReferenz implements Comparable, NReferenzModel, NReferenceModel {

	/**
	 * Ein Bezeichner zum Zugriff auf den numerischen Identifikator der
	 * NReferenz.
	 */
	public static final int ID_ID = 0;
	/**
	 * Ein Bezeichner zum Zugriff auf die durch die NReferenz referenzierte
	 * Tabellenspalte.
	 */
	public static final int ID_TABELLENSPALTE = 1;
	/** Ein Bezeichner zum Zugriff auf die Alterable-Flagge der NReferenz. */
	public static final int ID_ALTERABLE = 2;
	/** Ein Bezeichner zum Zugriff auf die Extensible-Flagge der NReferenz. */
	public static final int ID_EXTENSIBLE = 3;
	/** Ein Bezeichner zum Zugriff auf die PermitCreate-Flagge der NReferenz. */
	public static final int ID_PERMITCREATE = 4;
	/** Ein Bezeichner zum Zugriff auf das Panel der NReferenz. */
	public static final int ID_PANEL = 5;
	/** Ein Bezeichner zum Zugriff auf den NReferenzPanelType der NReferenz. */
	public static final int ID_NREFERENZPANELTYPE = 6;
	/** Ein Bezeichner zum Zugriff auf die DeleteConfirmationRequired-Flagge. */
	public static final int ID_DELETE_CONFIRMATION_REQUIRED = 7;

	/** Generiert eine NReferenz mit Default-Werten. */
	public NReferenz() {
		super();
	}

	public NReferenz(TabellenModel tm) {
		super();
		this.tm = tm;
	}

	/* Ueberschreiben von Methoden der Superklasse (Pflicht). */

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof NReferenz)) {
			return false;
		}
		NReferenz nr = (NReferenz) o;
		return (this.isAlterable() == nr.isAlterable()) && this.getColumn().equals(nr.getColumn())
				&& (this.isExtensible() == nr.isExtensible()) && (this.isPermitCreate() == nr.isPermitCreate());
	}

	/* Implementierung des Interfaces Attributed */

	public Object get(int id) throws IllegalArgumentException {
		switch (id) {
		case ID_ID:
			return new Integer(this.getId());
		case ID_TABELLENSPALTE:
			return this.getTabellenspalte();
		case ID_ALTERABLE:
			return new Boolean(this.isAlterable());
		case ID_EXTENSIBLE:
			return new Boolean(this.isExtensible());
		case ID_PERMITCREATE:
			return new Boolean(this.isPermitCreate());
		case ID_PANEL:
			return this.getPanel();
		case ID_NREFERENZPANELTYPE:
			return this.getNReferencePanelType();
		case ID_DELETE_CONFIRMATION_REQUIRED:
			return new Boolean(this.isDeleteConfirmationRequired());
		}
		throw new IllegalArgumentException("Klasse Tabelle verfuegt nicht ueber ein Attribut " + id + " (get)!");
	}

	public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
		switch (id) {
		case ID_ID:
			this.setId(((Integer) value).intValue());
			return;
		case ID_TABELLENSPALTE:
			this.setTabellenspalte((TabellenspaltenModel) value);
			return;
		case ID_ALTERABLE:
			this.setAlterable(((Boolean) value).booleanValue());
			return;
		case ID_EXTENSIBLE:
			this.setExtensible(((Boolean) value).booleanValue());
			return;
		case ID_PERMITCREATE:
			this.setPermitCreate(((Boolean) value).booleanValue());
			return;
		case ID_PANEL:
			this.setPanel((PanelModel) value);
			return;
		case ID_NREFERENZPANELTYPE:
			this.setNReferencePanelType((NReferencePanelType) value);
			return;
		case ID_DELETE_CONFIRMATION_REQUIRED:
			this.setDeleteConfirmationRequired(((Boolean) value).booleanValue());
			return;
		}
		throw new IllegalArgumentException("Klasse Tabelle verfuegt nicht ueber ein Attribut " + id + " (set)!");
	}

	/* Implementierung des Interfaces Comparable. */

	public int compareTo(Object o) {
		return ((Integer) this.get(ID_ID)).compareTo((Integer) ((NReferenz) o).get(ID_ID));
	}

	/* Implementierung des Interfaces Editable. */

	public EditorDescriptorList getEditorDescriptorList() {
		DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
		// Hier muss eigentlich eine Funktion hin, die nur die TSM's
		// zurueckliefert, die die
		// Tabelle referenzieren.
		DefaultComponentFactory dcfts = new DefaultComponentFactory(this.tm.getDiagramm().getReferencers(this.tm));
		DefaultComponentFactory dcfpanels = new DefaultComponentFactory(Arrays.asList(this.tm.getPanels()));
		if (this.get(ID_PANEL) == null) {
			this.set(ID_PANEL, this.tm.getPanels()[0]);
		}
		Vector paneltypes = new Vector();
		for (int i = 0; i < NReferencePanelType.values().length; i++) {
			paneltypes.addElement(NReferencePanelType.values()[i]);
		}
		DefaultComponentFactory dcfpaneltypes = new DefaultComponentFactory(paneltypes);
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_ID, dlf, dcf, "Numerischer " + "Identifikator", 'N',
				null, "Der numerische Identifikator der n-Referenz"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_TABELLENSPALTE, dlf, dcfts,
				"Referenzierte Tabellenspalte", 'T', null, "Die an die Referenzierung gebundene" + " Tabellenspalte"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_ALTERABLE, dlf, dcf, StrUtil
				.FromHTML("Bearbeitung erm&ouml;glichen"), 'A', null, StrUtil
				.FromHTML("Setzen Sie diese Flagge, um eine Bearbeitung der referenzierten "
						+ "Datens&auml;tze zu erm&ouml;glichen (Bearbeiten-Button)")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_EXTENSIBLE, dlf, dcf, StrUtil
				.FromHTML("Einf&uuml;gen erm&ouml;glichen"), 'E', null, StrUtil
				.FromHTML("Setzen Sie diese Flagge, wenn die Liste um ausw&auml;hlbare Datens&auml;tze "
						+ "erweitert werden k&ouml;nnen soll")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_PERMITCREATE, dlf, dcf, StrUtil
				.FromHTML("Neuanlage erm&ouml;glichen"), 'U', null, StrUtil
				.FromHTML("Wird diese Flagge gesetzt, so erlaubt der Editor eine "
						+ "Neuanlage von referenzierten Datens&auml;tzen")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_PANEL, dlf, dcfpanels, "Panel", 'P', null, StrUtil
				.FromHTML("W&auml;hlen Sie hier das Panel, auf dem die "
						+ "NReferenz dargestellt und manipuliert werden soll.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_NREFERENZPANELTYPE, dlf, dcfpaneltypes, "Paneltyp",
				'Y', null, StrUtil.FromHTML("W&auml;hlen Sie hier "
						+ "den Typ f&uuml;r das Panel aus, auf dem die NReferenz dargestellt und "
						+ "manipuliert werden soll.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_DELETE_CONFIRMATION_REQUIRED, dlf, dcf, StrUtil
				.FromHTML("L&ouml;schbest&auml;tigung notwendig"), 'L', null, StrUtil
				.FromHTML("Wird diese Flagge gesetzt, so ist eine Best&auml;tigung zum "
						+ "L&ouml;schen von Listeneintr&auml;gen notwendig.")));
		return dedl;
	}

	public Object createObject() {
		return new NReferenz();
	}

	public Object createObject(Object blueprint) throws ClassCastException {
		NReferenz nr = (NReferenz) blueprint;
		NReferenz newone = new NReferenz();
		newone.setId(nr.getId());
		newone.setTabellenspalte(nr.getTabellenspalte());
		newone.setAlterable(nr.isAlterable());
		newone.setDeleteConfirmationRequired(nr.isDeleteConfirmationRequired());
		newone.setExtensible(nr.isExtensible());
		newone.setPermitCreate(nr.isPermitCreate());
		newone.setPanel(nr.getPanel());
		newone.setNReferencePanelType(nr.getNReferencePanelType());
		return newone;
	}

	/* Implementierung des Interfaces NReferenzModel. */

	@Deprecated
	public TabellenspaltenModel getTabellenspalte() {
		return this.column;
	}

	@Deprecated
	public void setTabellenspalte(TabellenspaltenModel tsm) {
		this.column = tsm;
	}

	//
	// Approved methods and fields.
	//

	private boolean alterable = false;
	private TabellenspaltenModel column = null;
	private boolean deleteConfirmationRequired = false;
	private boolean extensible = true;
	private int id = 0;
	private NReferencePanelType panelType = null;
	private PanelModel panel = null;
	private boolean permitcreate = false;
	private TabellenModel tm = null;

	/**
	 * @changed OLI 06.05.2013 - Added.
	 */
	@Override
	public ColumnModel getColumn() {
		return this.column;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public int getId() {
		return this.id;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public NReferencePanelType getNReferencePanelType() {
		return this.panelType;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public PanelModel getPanel() {
		return this.panel;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public boolean isAlterable() {
		return this.alterable;
	}

	/**
	 * @changed OLI 30.09.2013 - Added.
	 */
	@Override
	public boolean isDeleteConfirmationRequired() {
		return this.deleteConfirmationRequired;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public boolean isExtensible() {
		return this.extensible;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public boolean isPermitCreate() {
		return this.permitcreate;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public void setAlterable(boolean alterable) {
		this.alterable = alterable;
	}

	/**
	 * @changed OLI 06.05.2013 - Added.
	 */
	@Override
	public void setColumn(ColumnModel column) {
		this.column = (TabellenspaltenModel) column;
	}

	/**
	 * @changed OLI 30.09.2013 - Added.
	 */
	@Override
	public void setDeleteConfirmationRequired(boolean b) {
		this.deleteConfirmationRequired = b;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public void setExtensible(boolean extensible) {
		this.extensible = extensible;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public void setNReferencePanelType(NReferencePanelType panelType) {
		this.panelType = panelType;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public void setPanel(PanelModel panel) {
		this.panel = panel;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public void setPermitCreate(boolean permitCreate) {
		this.permitcreate = permitCreate;
	}

	/**
	 * @changed OLI 06.05.2013 - Approved.
	 */
	@Override
	public String toString() {
		return (this.getColumn() != null ? this.getColumn().getFullName() : "-/-");
	}

}