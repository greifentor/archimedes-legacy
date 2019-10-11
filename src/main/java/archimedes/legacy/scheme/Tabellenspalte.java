/*
 * Tabellenspalte.java
 *
 * 20.03.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;
import gengen.metadata.ClassMetaData;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import archimedes.acf.util.ParameterUtil;
import archimedes.legacy.Archimedes;
import archimedes.legacy.app.ArchimedesEditorDescriptor;
import archimedes.legacy.gui.CommentSubEditorFactory;
import archimedes.legacy.gui.HistoryOwnerSubEditorFactory;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;
import archimedes.model.OptionModel;
import archimedes.model.PanelModel;
import archimedes.model.ReferenceWeight;
import archimedes.model.RelationModel;
import archimedes.model.SequenceModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.scheme.Option;
import corent.base.Direction;
import corent.base.StrUtil;
import corent.djinn.DefaultComponentFactory;
import corent.djinn.DefaultEditorDescriptor;
import corent.djinn.DefaultEditorDescriptorList;
import corent.djinn.DefaultLabelFactory;
import corent.djinn.DefaultSubEditorDescriptor;
import corent.djinn.DefaultTabDescriptor;
import corent.djinn.DefaultTabbedPaneFactory;
import corent.djinn.EditorDescriptorList;
import corent.djinn.Selectable;
import corent.djinn.TabDescriptor;
import corent.djinn.TabbedPaneFactory;

/**
 * Diese Klasse repr&auml;sentiert eine Tabellenspalte innerhalb der
 * Archimedes-Applikation.
 * <P>
 * &Uuml;ber die Properties
 * <I>archimedes.scheme.Tabellenspalte.diagramm.writeable.prefix</I> und
 * <I>archimedes.scheme.Tabellenspalte.diagramm.writeable.suffix</I> kann die
 * Kennzeichnung konfiguriert werden. Die Voreinstellung h&auml;ngt dem
 * Spaltennamen einen Asterix ("*") an.
 * 
 * @author ollie
 * 
 * @changed OLI 11.05.2008 - Erweiterung der Implementierung des Interfaces
 *          <TT>TabbedEditable</TT> um die Methode <TT>isTabEnabled(int)</TT>.
 * @changed OLI 09.08.2008 - Erweiterung um die Implementierung der Methoden
 *          <TT>isTechnicalField()</TT> und <TT>setTechnicalField(boolean)</TT>,
 *          &uuml;ber die Tabellenspalten als technische Felder gekennzeichnet
 *          werden k&ouml;nnen. Erweiterung der Tabellenspaltenanzeige im
 *          Diagramm um einen Hinweis darauf, ob das Feld eine
 *          Gel&ouml;scht-Flagge ist.
 * @changed OLI 11.08.2008 - Absicherung der <TT>equals(Object)</TT>-Methode
 *          gegen Vergleiche mit Tabellenspalten, deren Tabellenreferenz noch
 *          nicht gesetzt ist.
 * @changed OLI 22.09.2008 - Erweiterung um das Attribut <TT>listItemField</TT>
 *          mit entsprechendem Getter, Setter und Pflegefunktion.
 * @changed OLI 09.03.2009 - Implementierung f&uuml;r die Attribute
 *          <TT>unique</TT> und <TT>parameter</TT>.
 * @changed OLI 18.11.2010 - Erweiterung um die Methoden <TT>getMaxLength()</TT>
 *          und <TT>isNotNull()</TT>.
 * @changed OLI 27.10.2014 - Entfernung der Referenzen auf die Klasse
 *          <CODE>Tabelle</CODE> und das Interface <CODE>TabellenModel</CODE>
 *          zugunsten des Interfaces <CODE>TableModel</CODE>.
 */

public class Tabellenspalte implements Selectable, TabellenspaltenModel {

	/**
	 * Ein Bezeichner zum Zugriff auf den NOT-NULL-Contraint der Tabellenspalte.
	 */
	public static final int ID_NOTNULL = 0;
	/** Ein Bezeichner zum Zugriff auf die Primarkey-Flagge der Tabellenspalte. */
	public static final int ID_PRIMARYKEY = 1;
	/** Ein Bezeichner zum Zugriff auf die Domain der Tabellenspalte. */
	public static final int ID_DOMAIN = 2;
	/**
	 * Ein Bezeichner zum Zugriff auf die Relation der Tabellenspalte, falls
	 * eine vorhanden ist.
	 */
	public static final int ID_RELATION = 3;
	/** Ein Bezeichner zum Zugriff auf den Namen der Tabellenspalte. */
	public static final int ID_NAME = 4;
	/**
	 * Ein Bezeichner zum Zugriff auf die Tabelle zu der die Tabellenspalte
	 * geh&ouml;rt.
	 */
	public static final int ID_TABELLE = 5;
	/** Ein Bezeichner zum Zugriff auf die Beschreibung zur Tabellenspalte. */
	public static final int ID_COMMENT = 6;
	/**
	 * Ein Bezeichner zum Zugriff auf die eventuell referenzierte
	 * Tabellenspalte.
	 */
	public static final int ID_REFERENZIERTESPALTE = 7;
	/** Ein Bezeichner zum Zugriff auf die Aufgehoben-Markierung des Objektes. */
	public static final int ID_AUFGEHOBEN = 8;
	/** Ein Bezeichner zum Zugriff auf die Editormember-Markierung des Objektes. */
	public static final int ID_EDITORMEMBER = 9;
	/**
	 * Ein Bezeichner zum Zugriff auf die Writeablemember-Markierung des
	 * Objektes.
	 */
	public static final int ID_WRITEABLEMEMBER = 10;
	/** Ein Bezeichner zum Zugriff auf den Labeltext zur Tabellenspalte. */
	public static final int ID_LABELTEXT = 11;
	/** Ein Bezeichner zum Zugriff auf das Mnemonic zur Tabellenspalte. */
	public static final int ID_MNEMONIC = 12;
	/** Ein Bezeichner zum Zugriff auf den ToolTipText zur Tabellenspalte. */
	public static final int ID_TOOLTIPTEXT = 13;
	/** Ein Bezeichner zum Zugriff auf das Panel zur Tabellenspalte. */
	public static final int ID_PANEL = 14;
	/**
	 * Ein Bezeichner zum Zugriff auf die Sortierung innerhalb des Editorpanels.
	 */
	public static final int ID_EDITORPOSITION = 15;
	/**
	 * Ein Bezeichner zum Zugriff auf die zu erwartende Anzahl der
	 * Auswahlm&ouml;glichkeiten.
	 */
	public static final int ID_REFERENCEWEIGHT = 16;
	/**
	 * Ein Bezeichner zum Zugriff auf den Ressourcenbezeichner zur
	 * Tabellenspalte.
	 */
	public static final int ID_RESSOURCENIDENTIFIER = 17;
	/** Ein Bezeichner zum Zugriff auf die Kodiertflagge zur Tabellenspalte. */
	public static final int ID_KODIERT = 18;
	/** Ein Bezeichner zum Zugriff auf die Disabledflagge zur Tabellenspalte. */
	public static final int ID_DISABLED = 19;
	/** Ein Bezeichner zum Zugriff auf die Indexedflagge zur Tabellenspalte. */
	public static final int ID_INDEXED = 20;
	/**
	 * Ein Bezeichner zum Zugriff auf die Maximaleingabebreite zur
	 * Tabellenspalte.
	 */
	public static final int ID_MAXCHARACTERS = 21;
	/** Ein Bezeichner zum Zugriff auf die Global-Flagge zur Tabellenspalte. */
	public static final int ID_GLOBAL = 22;
	/** Ein Bezeichner zum Zugriff auf die Global-Id-Flagge zur Tabellenspalte. */
	public static final int ID_GLOBALID = 23;
	/**
	 * Ein Bezeichner zum Zugriff auf die AlterInBatch-Flagge zur
	 * Tabellenspalte.
	 */
	public static final int ID_ALTERINBATCH = 24;
	/**
	 * Ein Bezeichner zum Zugriff auf die LastModificationField-Flagge zur
	 * Tabellenspalte.
	 */
	public static final int ID_LASTMODIFICATIONFIELD = 25;
	/**
	 * Ein Bezeichner zum Zugriff auf die RemovedStateField-Flagge zur
	 * Tabellenspalte.
	 */
	public static final int ID_REMOVEDSTATEFIELD = 26;
	/**
	 * Ein Bezeichner zum Zugriff auf die IndexSearchMember-Flagge zur
	 * Tabellenspalte.
	 */
	public static final int ID_INDEXSEARCHMEMBER = 27;
	/**
	 * Ein Bezeichner zum Zugriff auf die CanBeReferenced-Flagge zur
	 * Tabellenspalte.
	 */
	public static final int ID_CANBEREFERENCED = 28;
	/**
	 * Ein Bezeichner zum Zugriff auf die HideReference-Flagge zur
	 * Tabellenspalte.
	 */
	public static final int ID_HIDEREFERENCE = 29;
	/**
	 * Ein Bezeichner zum Zugriff auf die TechnicalField-Flagge zur
	 * Tabellenspalte.
	 */
	public static final int ID_TECHNICALFIELD = 30;
	/**
	 * Ein Bezeichner zum Zugriff auf die ListItemField-Flagge zur
	 * Tabellenspalte.
	 */
	public static final int ID_LISTITEMFIELD = 31;
	/** Ein Bezeichner zum Zugriff auf die Unique-Flagge der Tabellenspalte. */
	public static final int ID_UNIQUE = 32;
	/** Ein Bezeichner zum Zugriff auf die Parameter-Flagge der Tabellenspalte. */
	public static final int ID_PARAMETER = 33;
	/**
	 * Ein Bezeichner zum Zugriff auf den individuellen Defaultwert der
	 * Tabellenspalte.
	 */
	public static final int ID_INDIVIDUALDEFAULTVALUE = 34;
	/** Ein Bezeichner zum Zugriff auf die Historie zur Tabellenspalte. */
	public static final int ID_HISTORY = 35;
	/**
	 * Ein Bezeichner zum Zugriff auf die Sequenz zur Schl&uuml;sselgenerierung.
	 */
	public static final int ID_SEQUENCE_FOR_KEY_GENERATION = 36;
	/** Ein Bezeichner zum Zugriff auf die Transient-Flagge zur Tabellenspalte. */
	public static final int ID_TRANSIENT = 37;
	/**
	 * Ein Bezeichner zum Zugriff auf die SuppressForeignKeyContraint-Flagge zur
	 * Tabellenspalte.
	 */
	public static final int ID_SUPPRESS_FOREIGN_KEY_CONSTRAINT = 38;

	/**
	 * Creates a copy of the passed column model.
	 * 
	 * @param c
	 *            The column model to copy.
	 * 
	 * @changed OLI 05.08.2016 - Added.
	 */
	public Tabellenspalte(ColumnModel c) {
		this(c.getName(), c.getDomain(), c.isPrimaryKey());
		this.setCanBeReferenced(c.canBeReferenced());
		this.setComment(c.getComment());
		this.setDeprecated(c.isDeprecated());
		this.setEditorAlterInBatchView(c.isEditorAlterInBatchView());
		this.setEditorDisabled(c.isEditorDisabled());
		this.setEditorLabelText(c.getEditorLabelText());
		this.setEditorMaxLength(c.getEditorMaxLength());
		this.setEditorMember(c.isEditorMember());
		this.setEditorMnemonic(c.getEditorMnemonic());
		this.setEditorPosition(c.getEditorPosition());
		this.setEditorReferenceWeight(c.getEditorReferenceWeight());
		this.setEditorResourceId(c.getEditorResourceId());
		this.setEditorToolTipText(c.getEditorToolTipText());
		this.setEncrypted(c.isEncrypted());
		this.setHideReference(c.isHideReference());
		this.setHistory(c.getHistory());
		this.setIndex(c.hasIndex());
		this.setIndexSearchMember(c.isIndexSearchMember());
		this.setIndividualDefaultValue(c.getIndividualDefaultValue());
		this.setLastModificationField(c.isLastModificationField());
		this.setListItemField(c.isListItemField());
		this.setNotNull(c.isNotNull());
		this.setPanel(c.getPanel());
		this.setParameters(c.getParameters());
		this.setRelation(c.getRelation());
		this.setRemovedStateField(c.isRemovedStateField());
		this.setRequired(c.isRequired());
		this.setSequenceForKeyGeneration(c.getSequenceForKeyGeneration());
		this.setSuppressForeignKeyConstraint(c.isSuppressForeignKeyConstraint());
		this.setSynchronized(c.isSynchronized());
		this.setSynchronizeId(c.isSynchronizeId());
		this.setTable(c.getTable());
		this.setTechnicalField(c.isTechnicalField());
		this.setTransient(c.isTransient());
		this.setUnique(c.isUnique());
	}

	/**
	 * Generiert eine Tabellenspalte anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param n
	 *            Der Name der Tabellenspalte.
	 * @param dom
	 *            Die Domain zur Tabellenspalte.
	 */
	public Tabellenspalte(String n, DomainModel dom) {
		this(n, dom, false);
	}

	/**
	 * Generiert eine Tabellenspalte anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param n
	 *            Der Name der Tabellenspalte.
	 * @param dom
	 *            Die Domain zur Tabellenspalte.
	 * @param pk
	 *            Wird diese Flagge gesetzt, so handelt es sich bei der
	 *            Tabellenspalte um ein Member des Prim&auml;rschl&uuml;ssels.
	 */
	public Tabellenspalte(String n, DomainModel dom, boolean pk) {
		super();
		this.setName(n);
		this.setDomain(dom);
		this.setPrimarykey(pk);
	}

	@Deprecated
	public boolean isPrimarykey() {
		return this.isPrimaryKey();
	}

	@Deprecated
	public void setPrimarykey(boolean primaryKey) {
		this.setPrimaryKey(primaryKey);
	}

	/**
	 * @changed OLI 18.11.2010 - Hinzugef&uuml;gt.
	 */
	public int getMaxLength() {
		DomainModel d = this.getDomain();
		if (d != null) {
			return d.getLength();
		}
		return -1;
	}

	@Deprecated
	public String getBeschreibung() {
		return this.getComment();
	}

	@Deprecated
	public void setBeschreibung(String beschreibung) {
		this.setComment(beschreibung);
	}

	@Override
	public String getFullName() {
		return (this.getTable() != null ? this.getTable().getName() + "." : "") + this.getName();
	}

	private int getKeyValue() {
		if (this.isPrimaryKey()) {
			return 0;
		} else if (this.getRelation() != null) {
			return 1;
		}
		return 2;
	}

	/* Ueberschreiben der Methoden der Superklasse. */

	/**
	 * @changed OLI 11.08.2008 - Absicherung des Falles, da&szlig; eine
	 *          Tabellenspalte verglichen werden soll, deren Tabellenreferenz
	 *          noch nicht gesetzt worden ist (dies kann beim Einf&uuml;gen von
	 *          Tabellenspalten &uuml;ber die Oberfl&auml;che vorkommen).
	 * 
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Tabellenspalte)) {
			return false;
		}
		Tabellenspalte ts = (Tabellenspalte) o;
		if ((this.getTable() != null) && (ts.getTable() != null)) {
			return this.getTable().getName().equals(ts.getTable().getName()) && this.getName().equals(ts.getName());
		}
		return (this.getTable() == null) && (ts.getTable() == null) && this.getName().equals(ts.getName());
	}

	@Override
	public String toString() {
		return this.getFullName();
	}

	/* Implementierung des Interfaces TabellenspaltenModel. */

	@Deprecated
	public TabellenModel getTabelle() {
		return (TabellenModel) this.table;
	}

	@Deprecated
	public void setTabelle(TabellenModel t) {
		this.table = t;
	}

	public String toDiagrammString() {
		DiagrammModel dm = this.getTabelle().getDiagramm();
		return (this.isListItemField() ? "(#) " : "")
				+ (dm.markWriteablemembers() && this.isWriteablemember() ? System.getProperty(
						"archimedes.scheme.Tabellenspalte.diagramm.writeable.prefix", "") : "")
				+ this.getName()
				+ (dm.markWriteablemembers() && this.isWriteablemember() ? System.getProperty(
						"archimedes.scheme.Tabellenspalte.diagramm.writeable.suffix", "*") : "") + " :"
				+ this.getDomainAndDefaultValue() + (this.isPrimarykey() ? " (PK)" : "")
				+ (this.getRelation() != null ? " (FK)" : "") + (this.isIndexed() ? " (I)" : "")
				+ (this.isUnique() ? " (U)" : "") + (this.isRemovedStateField() ? " (RS)" : "")
				+ (this.isSynchronizeId() ? " (GID)" : "")
				+ (this.isNotNull() && !this.isPrimarykey() ? " NOT NULL" : "");
		/*
		 * Nette Variante. Machts Tabellchen aber zu breit. return
		 * this.getName() + " :" + this.getDomain() + (this.isPrimarykey() ?
		 * " (PK)" : "") + (this.getRelation() != null ? " (FK->" +
		 * this.getRelation().getReferenced( ).getFullName() + ")" : "") +
		 * (this.isNotNull() && !this.isPrimarykey() ? " NOT NULL" : "");
		 */
	}

	private String getDomainAndDefaultValue() {
		return new DomainStringBuilder(this).build();
	}

	public RelationModel getRelation() {
		return this.relation;
	}

	@Deprecated
	public boolean isAufgehoben() {
		return this.isDeprecated();
	}

	@Deprecated
	public void setAufgehoben(boolean aufgehoben) {
		this.setDeprecated(aufgehoben);
	}

	public int compareTo(Object o) {
		Tabellenspalte ts = (Tabellenspalte) o;
		int erg = this.getKeyValue() - ts.getKeyValue();
		if (erg == 0) {
			erg = this.getFullName().toLowerCase().compareTo(ts.getFullName().toLowerCase());
			if (erg == 0) {
				if ((this.getDomain() == null) && (ts.getDomain() == null)) {
					return 0;
				} else if ((this.getDomain() == null) && (ts.getDomain() != null)) {
					return 1;
				} else if ((this.getDomain() != null) && (ts.getDomain() == null)) {
					return -1;
				}
				return (this.getDomain().compareTo(ts.getDomain()));
			}
		}
		return erg;
	}

	public Object get(int id) throws IllegalArgumentException {
		switch (id) {
		case ID_NOTNULL:
			return new Boolean(this.isNotNull());
		case ID_PRIMARYKEY:
			return new Boolean(this.isPrimaryKey());
		case ID_DOMAIN:
			return this.getDomain();
		case ID_RELATION:
			return this.getRelation();
		case ID_NAME:
			return this.getName();
		case ID_TABELLE:
			return this.getTable();
		case ID_REFERENZIERTESPALTE:
			if (this.getRelation() != null) {
				return this.getRelation().getReferenced();
			}
			return null;
		case ID_AUFGEHOBEN:
			return new Boolean(this.isAufgehoben());
		case ID_EDITORMEMBER:
			return new Boolean(this.isEditormember());
		case ID_WRITEABLEMEMBER:
			return new Boolean(this.isWriteablemember());
		case ID_LABELTEXT:
			return this.getLabelText();
		case ID_MNEMONIC:
			return this.getMnemonic();
		case ID_TOOLTIPTEXT:
			return this.getToolTipText();
		case ID_PANEL:
			return this.getPanel();
		case ID_EDITORPOSITION:
			return new Integer(this.editorPosition);
		case ID_REFERENCEWEIGHT:
			return this.editorReferenceWeight;
		case ID_RESSOURCENIDENTIFIER:
			return this.getRessourceIdentifier();
		case ID_KODIERT:
			return new Boolean(this.isKodiert());
		case ID_DISABLED:
			return new Boolean(this.isDisabled());
		case ID_INDEXED:
			return new Boolean(this.isIndexed());
		case ID_MAXCHARACTERS:
			return new Integer(this.getMaxCharacters());
		case ID_GLOBAL:
			return new Boolean(this.isGlobal());
		case ID_GLOBALID:
			return new Boolean(this.isGlobalId());
		case ID_ALTERINBATCH:
			return new Boolean(this.isAlterInBatch());
		case ID_LASTMODIFICATIONFIELD:
			return new Boolean(this.isLastModificationField());
		case ID_REMOVEDSTATEFIELD:
			return new Boolean(this.isRemovedStateField());
		case ID_INDEXSEARCHMEMBER:
			return new Boolean(this.isIndexSearchMember());
		case ID_CANBEREFERENCED:
			return new Boolean(this.isCanBeReferenced());
		case ID_HIDEREFERENCE:
			return new Boolean(this.isHideReference());
		case ID_TECHNICALFIELD:
			return new Boolean(this.isTechnicalField());
		case ID_LISTITEMFIELD:
			return new Boolean(this.isListItemField());
		case ID_UNIQUE:
			return new Boolean(this.isUnique());
		case ID_PARAMETER:
			return this.getParameter();
		case ID_INDIVIDUALDEFAULTVALUE:
			return this.getIndividualDefaultValue();
		case ID_HISTORY:
			return this.getHistory();
		case ID_SEQUENCE_FOR_KEY_GENERATION:
			return this.getSequenceForKeyGeneration();
		case ID_SUPPRESS_FOREIGN_KEY_CONSTRAINT:
			return new Boolean(this.isSuppressForeignKeyConstraint());
		case ID_TRANSIENT:
			return new Boolean(this.isTransient());
		}
		throw new IllegalArgumentException("Klasse Tabellenspalte verfuegt nicht ueber ein " + "Attribut " + id
				+ " (get)!");
	}

	public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
		switch (id) {
		case ID_NOTNULL:
			this.setNotNull(((Boolean) value).booleanValue());
			return;
		case ID_PRIMARYKEY:
			this.setPrimaryKey(((Boolean) value).booleanValue());
			return;
		case ID_DOMAIN:
			this.setDomain((DomainModel) value);
			return;
		case ID_RELATION:
			this.setRelation((RelationModel) value);
			return;
		case ID_NAME:
			this.setName((String) value);
			return;
		case ID_TABELLE:
			this.setTable((TableModel) value);
			return;
		case ID_REFERENZIERTESPALTE:
			if (value == null) {
				this.setRelation(null);
			} else if (this.getRelation() != null) {
				this.getRelation().setReferenced((TabellenspaltenModel) value);
			} else {
				ViewModel view = (ViewModel) this.getTabelle().getDiagramm().getViews().get(0);
				this.setRelation(Archimedes.Factory.createRelation(view, this, Direction.UP, 0,
						(TabellenspaltenModel) value, Direction.UP, 0));
			}
			return;
		case ID_AUFGEHOBEN:
			this.setAufgehoben(((Boolean) value).booleanValue());
			return;
		case ID_EDITORMEMBER:
			this.setEditormember(((Boolean) value).booleanValue());
			return;
		case ID_WRITEABLEMEMBER:
			this.setWriteablemember(((Boolean) value).booleanValue());
			return;
		case ID_LABELTEXT:
			this.setLabelText((String) value);
			return;
		case ID_MNEMONIC:
			this.setMnemonic((String) value);
			return;
		case ID_TOOLTIPTEXT:
			this.setToolTipText((String) value);
			return;
		case ID_PANEL:
			this.setPanel((PanelModel) value);
			return;
		case ID_EDITORPOSITION:
			this.setEditorPosition(((Integer) value).intValue());
			return;
		case ID_REFERENCEWEIGHT:
			this.setReferenceWeight((ReferenceWeight) value);
			return;
		case ID_RESSOURCENIDENTIFIER:
			this.setRessourceIdentifier(value.toString());
			return;
		case ID_KODIERT:
			this.setKodiert(((Boolean) value).booleanValue());
			return;
		case ID_DISABLED:
			this.setDisabled(((Boolean) value).booleanValue());
			return;
		case ID_INDEXED:
			this.setIndexed(((Boolean) value).booleanValue());
			return;
		case ID_MAXCHARACTERS:
			this.setMaxCharacters(((Integer) value).intValue());
			return;
		case ID_GLOBAL:
			this.setGlobal(((Boolean) value).booleanValue());
			return;
		case ID_GLOBALID:
			this.setGlobalId(((Boolean) value).booleanValue());
			return;
		case ID_ALTERINBATCH:
			this.setAlterInBatch(((Boolean) value).booleanValue());
			return;
		case ID_LASTMODIFICATIONFIELD:
			this.setLastModificationField(((Boolean) value).booleanValue());
			return;
		case ID_REMOVEDSTATEFIELD:
			this.setRemovedStateField(((Boolean) value).booleanValue());
			return;
		case ID_INDEXSEARCHMEMBER:
			this.setIndexSearchMember(((Boolean) value).booleanValue());
			return;
		case ID_CANBEREFERENCED:
			this.setCanBeReferenced(((Boolean) value).booleanValue());
			return;
		case ID_HIDEREFERENCE:
			this.setHideReference(((Boolean) value).booleanValue());
			return;
		case ID_TECHNICALFIELD:
			this.setTechnicalField(((Boolean) value).booleanValue());
			return;
		case ID_LISTITEMFIELD:
			this.setListItemField(((Boolean) value).booleanValue());
			return;
		case ID_UNIQUE:
			this.setUnique(((Boolean) value).booleanValue());
			return;
		case ID_PARAMETER:
			this.setParameter(value.toString());
			return;
		case ID_INDIVIDUALDEFAULTVALUE:
			this.setIndividualDefaultValue((String) value);
			return;
		case ID_HISTORY:
			this.setHistory((String) value);
			return;
		case ID_SEQUENCE_FOR_KEY_GENERATION:
			this.sequenceForKeyGeneration = (SequenceModel) value;
			return;
		case ID_SUPPRESS_FOREIGN_KEY_CONSTRAINT:
			this.setSuppressForeignKeyConstraint(((Boolean) value).booleanValue());
			return;
		case ID_TRANSIENT:
			this.setTransient(((Boolean) value).booleanValue());
			return;
		}
		throw new IllegalArgumentException("Klasse Tabellenspalte verfuegt nicht ueber ein " + "Attribut " + id
				+ " (set)!");
	}

	public EditorDescriptorList getEditorDescriptorList() {
		DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
		DefaultComponentFactory dcfdom = new DefaultComponentFactory(this.getTabelle().getDiagramm().getDomains());
		/*
		 * DefaultComponentFactory dcfkey = new
		 * DefaultComponentFactory(this.getTabelle(
		 * ).getDiagramm().getKeycolumns(this.getTabelle()));
		 */
		/*
		 * Alte Version. DefaultComponentFactory dcfkey = new
		 * DefaultComponentFactory(this.getTabelle(
		 * ).getDiagramm().getKeycolumns(null));
		 */
		DefaultComponentFactory dcfkey = new DefaultComponentFactory(this.getTabelle().getDiagramm()
				.getColumnsToBeReferenced(null));
		Vector refweights = new Vector();
		for (int i = 0; i < ReferenceWeight.values().length; i++) {
			refweights.addElement(ReferenceWeight.values()[i]);
		}
		DefaultComponentFactory dcfrws = new DefaultComponentFactory(refweights);
		DefaultComponentFactory dcfseq = new DefaultComponentFactory(Arrays.asList(this.getTable().getDataModel()
				.getSequences()));
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		DefaultComponentFactory dcfpanels = new DefaultComponentFactory(((Tabelle) this.getTabelle())
				.getPanelsByReference());
		if (this.get(ID_PANEL) == null) {
			this.set(ID_PANEL, this.getTable().getPanels()[0]);
		}
		DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_NAME, dlf, dcf, "Name", 'N', null,
				"Der Name der Tabellenspalte"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_DOMAIN, dlf, dcfdom, "Domain", 'D', null,
				"Die Domain zur Tabellenspalte"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_INDIVIDUALDEFAULTVALUE, dlf, dcf,
				"Individueller Defaultwert", '\0', null, "Ein individueller Defaultwert " + "zur Tabellenspalte"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_PRIMARYKEY, dlf, dcf, StrUtil
				.FromHTML("Prim&auml;rschl&uuml;ssel"), 'P', null, StrUtil
				.FromHTML("Setzen Sie diese Flagge um ein Prim&auml;rschl&uuml;sselmitglied zu " + "kennzeichnen.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_NOTNULL, dlf, dcf, "Not-Null", 'O', null, StrUtil
				.FromHTML("Setzen Sie diese Flagge, wenn f&uuml;r"
						+ " das Attribut ein Not-Null-Constraint gesetzt werden soll.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_REFERENZIERTESPALTE, dlf, dcfkey, "Referenziert", 'R',
				null, "Eine durch die Tabellenspalte referenzierte" + " Tabellenspalte"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_SEQUENCE_FOR_KEY_GENERATION, dlf, dcfseq,
				"Key-Generator-Sequenz", 'Q', null, StrUtil.FromHTML("W&auuml;hlen"
						+ " Sie hier eine Sequenz zur Schl&uuml;sselerzeugung, falls sie dies " + "w&uuml;nschen.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_CANBEREFERENCED, dlf, dcf, "Referenzierbar", 'Z', null,
				"Setzen Sie diese Flagge, um die " + "Tabellenspalte in Referenzauswahldialogen anzuzeigen."));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_HIDEREFERENCE, dlf, dcf, "Referenzanzeige verstecken",
				'\0', null, StrUtil.FromHTML("Setzen Sie "
						+ "diese Flagge, um die Referenzanzeige f&uuml;r die Tabellenspalte zu " + "deaktivieren.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_AUFGEHOBEN, dlf, dcf, "Aufgehoben", 'A', null,
				"Die Aufgehoben-Flagge der Tabellenspalte"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_KODIERT, dlf, dcf, "Kodiert", 'K', null, StrUtil
				.FromHTML("Setzen Sie diese Flagge, um die "
						+ "Kodierung f&uuml;r die Inhalte der Spalte zu initiieren (VORSICHT: Das kann "
						+ "zu Problemen bei Suchanfragen auf die Spalte f&uuml;hren).")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_UNIQUE, dlf, dcf, "Unique", 'U', null,
				"Setzen Sie diese Flagge, um die Tabellenspalte als " + "einzigartig zu kennzeichnen."));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_INDEXED, dlf, dcf, "Indiziert", 'I', null,
				"Setzen Sie diese Flagge, um die Tabellenspalte im " + "Datenmodell als indiziert zu kennzeichnen."));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_GLOBAL, dlf, dcf, "Global", 'G', null,
				"Setzen Sie diese Flagge, um die Tabellenspalte als " + "globales Attribut zu kennzeichnen."));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_GLOBALID, dlf, dcf, "Globale Id", 'L', null, StrUtil
				.FromHTML("Setzen Sie diese Flagge, um die "
						+ "Tabellenspalte als globales Schl&uuml;sselattribut zu kennzeichnen.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_ALTERINBATCH, dlf, dcf, StrUtil
				.FromHTML("Stapel&auml;nderung"), 'S', null, StrUtil.FromHTML("Setzen "
				+ "Sie diese Flagge, um die Tabellenspalte als &auml;nderbar im Stapel zu " + "kennzeichnen.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_LASTMODIFICATIONFIELD, dlf, dcf,
				"Letzte-Modifikation-Feld", 'M', null, StrUtil.FromHTML("Setzen Sie diese "
						+ "Flagge, um die Tabellenspalte als Feld f&uuml;r die Aufnahme des letzten "
						+ "Modifikationszeitpunktes zu kennzeichnen.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_REMOVEDSTATEFIELD, dlf, dcf, StrUtil
				.FromHTML("Gel&ouml;scht-Status-Feld"), 'U', null, StrUtil
				.FromHTML("Setzen Sie diese Flagge, um die Tabellenspalte als Feld f&uuml;r die Aufnahme "
						+ "eines Gel&ouml;schtstatus zu kennzeichnen.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_TECHNICALFIELD, dlf, dcf, StrUtil
				.FromHTML("Technisches Feld"), 'T', null, StrUtil
				.FromHTML("Setzen Sie diese Flagge, um die Tabellenspalte als Feld mit rein technischem "
						+ "Charakter zu kennzeichnen.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_LISTITEMFIELD, dlf, dcf, StrUtil
				.FromHTML("Positionsattribut"), 'P', null, StrUtil
				.FromHTML("Setzen Sie diese Flagge als Kennzeichen f&uuml;r ein Positionsattribut im "
						+ "Falle einer \"flachgespeicherten\" Liste.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_INDEXSEARCHMEMBER, dlf, dcf,
				"Bei Indexsuche einbeziehen", 'X', null, StrUtil.FromHTML("Setzen Sie diese "
						+ "Flagge, um die Tabellenspalte in eine tabellen&uuml;bergreifende Indexsuche "
						+ "aufzunehmen.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_TRANSIENT, dlf, dcf, StrUtil
				.FromHTML("Transientes Feld"), '\0', null, StrUtil
				.FromHTML("Setzen Sie diese Flagge, um die Tabellenspalte als transient zu kennzeichnen.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_SUPPRESS_FOREIGN_KEY_CONSTRAINT, dlf, dcf, StrUtil
				.FromHTML("FOREIGN-KEY-Constraint unterdr&uuml;cken"), '\0', null, StrUtil
				.FromHTML("Setzen Sie diese Flagge, um f&uuml;r die Tabellenspalte"
						+ " die FOREIGN-KEY-Constraints zu deaktivieren.")));
		dedl.addElement(new DefaultSubEditorDescriptor(1, this, new CommentSubEditorFactory()));
		dedl.addElement(new DefaultSubEditorDescriptor(2, this, new HistoryOwnerSubEditorFactory()));
		dedl.addElement(new DefaultEditorDescriptor(3, this, ID_EDITORMEMBER, dlf, dcf, "Editormember", 'M', null,
				StrUtil.FromHTML("Setzen Sie diese Flagge um Code " + "f&uuml;r den Editor zu produzieren")));
		dedl.addElement(new DefaultEditorDescriptor(3, this, ID_DISABLED, dlf, dcf, "Abblenden", 'D', null,
				"Setzen Sie diese Flagge, um die Tabellenspalte in der " + "Editorsicht abzublenden."));
		dedl.addElement(new DefaultEditorDescriptor(3, this, ID_LABELTEXT, dlf, dcf, "Labeltext", 'T', null,
				"Der Text des Labels im Editor"));
		dedl.addElement(new DefaultEditorDescriptor(3, this, ID_MNEMONIC, dlf, dcf, "Mnemonic", 'C', null,
				"Ein Mnemonic zum Label"));
		dedl.addElement(new DefaultEditorDescriptor(3, this, ID_TOOLTIPTEXT, dlf, dcf, "ToolTipText", 'T', null,
				"Ein ToolTip zum Editorfeld"));
		dedl.addElement(new DefaultEditorDescriptor(3, this, ID_PANEL, dlf, dcfpanels, "Panel", 'P', null, StrUtil
				.FromHTML("W&auml;hlen Sie hier das Panel aus, auf "
						+ "dem das Control zur Tabellenspalte abgebildet werden soll")));
		dedl.addElement(new DefaultEditorDescriptor(3, this, ID_EDITORPOSITION, dlf, dcf, "Editor-Position", 'E', null,
				StrUtil.FromHTML("Hier m&uuml;ssen Sie die "
						+ "Position des Editorfeldes innerhalb des Panels festlegen!")));
		dedl.addElement(new DefaultEditorDescriptor(3, this, ID_REFERENCEWEIGHT, dlf, dcfrws, "Auswahl", 'A', null,
				StrUtil.FromHTML("Legen Sie hier die Menge der "
						+ "zuerwartenden Auswahlm&ouml;glichkeiten fest (nur bei Referenzfeldern)")));
		dedl.addElement(new DefaultEditorDescriptor(3, this, ID_RESSOURCENIDENTIFIER, dlf, dcf, "Ressourcebezeichner",
				'R', null, StrUtil.FromHTML("Hier k&ouml;nnen Sie einen "
						+ "Bezeichner (z. B. zum Bezug von Ressourcen) f&uuml;r die Komponente zur "
						+ "Tabellenspalte setzen.")));
		dedl.addElement(new DefaultEditorDescriptor(3, this, Tabellenspalte.ID_MAXCHARACTERS, dlf, dcf,
				"Maximalbreite (Eingabefeld)", 'M', null, StrUtil
						.FromHTML("Hier k&ouml;nnen Sie eine maximale Breite f&uuml;r das mit der Tabellenspalte "
								+ "zusammenh&auml;ngende Eingabefeld definieren.")));
		dedl.addElement(new ArchimedesEditorDescriptor("Parameter", 3, this, Tabellenspalte.ID_PARAMETER, dlf, dcf,
				"Parameter", '\0', null, StrUtil.FromHTML("Hier k&ouml;nnen Sie einen freidefinierbaren Parameter zur "
						+ "Tabellenspalte.")));
		dedl.addElement(new DefaultEditorDescriptor(4, this, ID_WRITEABLEMEMBER, dlf, dcf, "Writeable", 'W', null,
				StrUtil.FromHTML("Setzen Sie diese Flagge um die "
						+ "Tabellenspalte im Code f&uuml;r die Schreibbarkeitspr&uuml;fung " + "wiederzufinden.")));
		return dedl;
	}

	public Object createObject() {
		return Archimedes.Factory.createTabellenspalte("", null);
	}

	public Object createObject(Object blueprint) throws ClassCastException {
		if (!(blueprint instanceof Tabellenspalte)) {
			throw new ClassCastException("Instance of Tabellenspalte required!");
		}
		return null;
	}

	public TabbedPaneFactory getTabbedPaneFactory() {
		return new DefaultTabbedPaneFactory(new TabDescriptor[] { new DefaultTabDescriptor("1.Daten", '1', null),
				new DefaultTabDescriptor("2.Beschreibung", '2', null),
				new DefaultTabDescriptor("3.Historie", '3', null), new DefaultTabDescriptor("4.Editor", '4', null),
				new DefaultTabDescriptor("5.Konsistenz", '5', null) });
	}

	public boolean isTabEnabled(int no) {
		return true;
	}

	/* Implementierung des Interfaces Selectable. */

	public boolean isSelected(Object[] criteria) throws IllegalArgumentException {
		if (((criteria != null) && (criteria.length > 0) && !(criteria[0] instanceof String))
				|| ((criteria != null) && (criteria.length > 1))) {
			throw new IllegalArgumentException("Tabellenspalte does only except one " + "String-criteria!");
		} else if (criteria == null) {
			return true;
		}
		StringTokenizer st = new StringTokenizer((String) criteria[0]);
		while (st.hasMoreTokens()) {
			String c = st.nextToken();
			String s = this.getName().concat("|" + this.getTable().getName()).toLowerCase();
			if ((c == null) || (s.indexOf(c.toLowerCase()) < 0)) {
				return false;
			}
		}
		return true;
	}

	/* Implementierung des Interfaces TabellenModel. */

	@Deprecated
	public boolean isEditormember() {
		return this.isEditorMember();
	}

	@Deprecated
	public void setEditormember(boolean editormember) {
		this.setEditorMember(editormember);
	}

	@Deprecated
	public String getLabelText() {
		return this.getEditorLabelText();
	}

	@Deprecated
	public void setLabelText(String labeltext) {
		this.setEditorLabelText(labeltext);
	}

	@Deprecated
	public String getToolTipText() {
		return this.getEditorToolTipText();
	}

	@Deprecated
	public void setToolTipText(String toolTipText) {
		this.setEditorToolTipText(toolTipText);
	}

	@Deprecated
	public String getMnemonic() {
		return this.getEditorMnemonic();
	}

	@Deprecated
	public void setMnemonic(String mnemonic) {
		this.setEditorMnemonic(mnemonic);
	}

	@Deprecated
	public boolean isWriteablemember() {
		return this.isRequired();
	}

	@Deprecated
	public void setWriteablemember(boolean writeablemember) {
		this.setRequired(writeablemember);
	}

	/**
	 * @changed OLI 07.01.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public ClassMetaData getReferencedClass() {
		ClassMetaData referencedClass = null;
		if ((this.getRelation() != null) && (((Relation) this.getRelation()).getReferencer() == this)) {
			if (this.getRelation().getReferenced() != null) {
				referencedClass = (ClassMetaData) this.getRelation().getReferenced().getTable();
			}
		}
		return referencedClass;
	}

	@Deprecated
	public ReferenceWeight getReferenceWeight() {
		return this.getEditorReferenceWeight();
	}

	@Deprecated
	public void setReferenceWeight(ReferenceWeight rw) {
		this.setEditorReferenceWeight(rw);
	}

	@Deprecated
	public String getRessourceIdentifier() {
		return this.getEditorResourceId();
	}

	@Deprecated
	public void setRessourceIdentifier(String ri) {
		this.setEditorResourceId(ri);
	}

	@Deprecated
	public boolean isKodiert() {
		return this.isEncrypted();
	}

	@Deprecated
	public void setKodiert(boolean b) {
		this.setEncrypted(b);
	}

	@Deprecated
	public boolean isDisabled() {
		return this.isEditorDisabled();
	}

	@Deprecated
	public void setDisabled(boolean b) {
		this.setEditorDisabled(b);
	}

	@Deprecated
	public boolean isIndexed() {
		return this.hasIndex();
	}

	@Deprecated
	public void setIndexed(boolean index) {
		this.setIndex(index);
	}

	@Deprecated
	public int getMaxCharacters() {
		return this.getEditorMaxLength();
	}

	@Deprecated
	public void setMaxCharacters(int max) {
		this.setEditorMaxLength(max);
	}

	@Deprecated
	public boolean isGlobal() {
		return this.synchronizedField;
	}

	@Deprecated
	public void setGlobal(boolean g) {
		this.synchronizedField = g;
	}

	@Deprecated
	public boolean isGlobalId() {
		return this.synchronizeId;
	}

	@Deprecated
	public void setGlobalId(boolean g) {
		this.synchronizeId = g;
	}

	@Deprecated
	public boolean isAlterInBatch() {
		return this.isEditorAlterInBatchView();
	}

	@Deprecated
	public void setAlterInBatch(boolean aib) {
		this.setEditorAlterInBatchView(aib);
	}

	@Deprecated
	public boolean isCanBeReferenced() {
		return this.canBeReferenced();
	}

	@Deprecated
	public boolean isHideReference() {
		return this.isReferenceHidden();
	}

	/**
	 * @changed OLI 09.03.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 * 
	 */
	@Deprecated
	public String getParameter() {
		return this.getParameters();
	}

	/**
	 * @changed OLI 09.03.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 * 
	 */
	@Deprecated
	public void setParameter(String s) {
		this.setParameters(s);
	}

	/* Implementierungen zum Interface AttributeMetaData. */

	/**
	 * @changed OLI 14.06.2011 - Ber&uuml;cksichtigung des individuellen
	 *          Defaultwertes zur Tabellenspalte.
	 */
	@Override
	public String getDefaultValue() {
		String s = null;
		if (this.getDomain() != null) {
			s = this.getDomain().getInitialValue();
			if (!this.getIndividualDefaultValue().equals("")) {
				s = this.getIndividualDefaultValue();
			}
			if (s.equalsIgnoreCase("null")) {
				s = null;
			}
		} else {
			s = "<null>";
		}
		return s;
	}

	public String getJavaType() {
		return DefaultCodeFactory.GetType(this.getDomain());
	}

	// public String getName(); // Implementierung: siehe oben.

	/**
	 * @changed OLI 30.09.2009 - Hinzugef&uuml;gt.
	 */
	public boolean isPrimaryKeyMember() {
		return this.isPrimarykey();
	}

	/**
	 * @changed OLI 02.10.2009 - Hinzugef&uuml;gt.
	 */
	@Deprecated
	public boolean isTechnicalAttribute() {
		return this.isTechnicalField();
	}

	/*
	 * The following methods are approved by the current model interface
	 * (TableModel).
	 */

	private boolean alterInBatch = false;
	private boolean canBeReferenced = false;
	private String comment = "";
	private boolean deprecated = false;
	private boolean disabled = false;
	private DomainModel domain = null;
	private String editorLabelText = "";
	private int editorMaxLength = 0;
	private boolean editorMember = false;
	private String editorMnemonic = "";
	private int editorPosition = 0;
	private ReferenceWeight editorReferenceWeight = ReferenceWeight.NONE;
	private String editorToolTipText = "";
	private boolean encrypted = false;
	private boolean hasIndex = false;
	private boolean hideReference = false;
	private String history = "";
	private boolean indexSearchMember = false;
	private String individualDefaultValue = "";
	private boolean lastModificationField = false;
	private boolean listItemField = false;
	private String name = "";
	private boolean notNull = false;
	private PanelModel panel = null;
	private String parameters = "";
	private boolean primaryKey = false;
	private RelationModel relation = null;
	private boolean removedStateField = false;
	private boolean required = false;
	private String ressourceIdentifier = "";
	private SequenceModel sequenceForKeyGeneration = null;
	private boolean suppressForeignKeyConstraint = false;
	private boolean synchronizedField = false;
	private boolean synchronizeId = false;
	private TableModel table = null;
	private boolean technicalField = false;
	private boolean transientField = false;
	private boolean unique = false;

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public boolean canBeReferenced() {
		return (this.canBeReferenced || this.isPrimaryKey());
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public ColumnModel createCopy() {
		ColumnModel c = new Tabellenspalte(this.getName(), this.getDomain(), this.isPrimaryKey());
		c.setCanBeReferenced(this.canBeReferenced());
		c.setComment(this.getComment());
		c.setDeprecated(this.isDeprecated());
		c.setEditorAlterInBatchView(this.isEditorAlterInBatchView());
		c.setEditorDisabled(this.isEditorDisabled());
		c.setEditorLabelText(this.getEditorLabelText());
		c.setEditorMaxLength(this.getEditorMaxLength());
		c.setEditorMember(this.isEditorMember());
		c.setEditorMnemonic(this.getEditorMnemonic());
		c.setEditorPosition(this.getEditorPosition());
		c.setEditorReferenceWeight(this.getEditorReferenceWeight());
		c.setEditorResourceId(this.getEditorResourceId());
		c.setEditorToolTipText(this.getEditorToolTipText());
		c.setEncrypted(this.isEncrypted());
		c.setHideReference(this.isHideReference());
		c.setHistory(this.getHistory());
		c.setIndex(this.hasIndex());
		c.setIndexSearchMember(this.isIndexSearchMember());
		c.setIndividualDefaultValue(this.getIndividualDefaultValue());
		c.setLastModificationField(this.isLastModificationField());
		c.setListItemField(this.isListItemField());
		c.setNotNull(this.isNotNull());
		c.setPanel(this.getPanel());
		c.setParameters(this.getParameters());
		c.setRelation(this.getRelation());
		c.setRemovedStateField(this.isRemovedStateField());
		c.setRequired(this.isRequired());
		c.setSequenceForKeyGeneration(this.getSequenceForKeyGeneration());
		c.setSuppressForeignKeyConstraint(this.isSuppressForeignKeyConstraint());
		c.setSynchronized(this.isSynchronized());
		c.setSynchronizeId(this.isSynchronizeId());
		c.setTable(this.getTable());
		c.setTechnicalField(this.isTechnicalField());
		c.setTransient(this.isTransient());
		c.setUnique(this.isUnique());
		for (OptionModel o : this.getOptions()) {
			c.addOption(new Option(o.getName(), o.getParameter()));
		}
		return c;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public String getComment() {
		return this.comment;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public DomainModel getDomain() {
		return this.domain;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public String getEditorLabelText() {
		return this.editorLabelText;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public int getEditorMaxLength() {
		return this.editorMaxLength;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public String getEditorMnemonic() {
		return this.editorMnemonic;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public int getEditorPosition() {
		return this.editorPosition;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public ReferenceWeight getEditorReferenceWeight() {
		return this.editorReferenceWeight;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public String getEditorResourceId() {
		return this.ressourceIdentifier;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public String getEditorToolTipText() {
		return this.editorToolTipText;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public String getHistory() {
		return this.history;
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Override
	public String getIndividualDefaultValue() {
		return this.individualDefaultValue;
	}

	/**
	 * @changed OLI 25.04.2013 - Approved.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public PanelModel getPanel() {
		return this.panel;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public void setPanel(PanelModel p) {
		this.panel = p;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public String getParameters() {
		return this.parameters;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public String getQualifiedName() {
		String n = "";
		// TODO - Schema fehlt noch ...
		if (this.getTable() != null) {
			n = this.getTable().getName().concat(".");
		}
		return n.concat(this.getName());
	}

	/**
	 * @changed OLI 13.09.2013 - Added.
	 */
	@Override
	public ColumnModel getReferencedColumn() {
		return (this.getRelation() != null ? this.getRelation().getReferenced() : null);
	}

	/**
	 * @changed OLI 26.08.2013 - Added.
	 */
	@Override
	public TableModel getReferencedTable() {
		return (this.getRelation() != null ? this.getRelation().getReferenced().getTable() : null);
	}

	/**
	 * @changed OLI 16.10.2013 - Added.
	 */
	@Override
	public SequenceModel getSequenceForKeyGeneration() {
		return this.sequenceForKeyGeneration;
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Override
	public TableModel getTable() {
		return this.table;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public boolean hasIndex() {
		return this.hasIndex;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public boolean isDeprecated() {
		return this.deprecated;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public boolean isEditorAlterInBatchView() {
		return this.alterInBatch;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public boolean isEditorDisabled() {
		return this.disabled;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public boolean isEditorMember() {
		return this.editorMember;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public boolean isEncrypted() {
		return this.encrypted;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public boolean isIndexSearchMember() {
		return this.indexSearchMember;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public boolean isLastModificationField() {
		return this.lastModificationField;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public boolean isListItemField() {
		return this.listItemField;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public boolean isNotNull() {
		return this.notNull;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public boolean isPrimaryKey() {
		return this.primaryKey;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public boolean isReferenceHidden() {
		return this.hideReference;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public boolean isRemovedStateField() {
		return this.removedStateField;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public boolean isRequired() {
		return this.required;
	}

	/**
	 * @changed OLI 07.12.2015 - Added.
	 */
	@Override
	public boolean isSuppressForeignKeyConstraint() {
		return this.suppressForeignKeyConstraint;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public boolean isSynchronizeId() {
		return this.synchronizeId;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public boolean isSynchronized() {
		return this.synchronizedField;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public boolean isTechnicalField() {
		return this.technicalField;
	}

	/**
	 * @changed OLI 10.06.2015 - Approved.
	 */
	@Override
	public boolean isTransient() {
		return this.transientField;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public boolean isUnique() {
		return this.unique;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setCanBeReferenced(boolean canBeReferenced) {
		this.canBeReferenced = canBeReferenced;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setComment(String comment) {
		this.comment = (comment != null ? comment : "");
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public void setDomain(DomainModel domain) {
		this.domain = domain;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setEditorAlterInBatchView(boolean alterInBatchView) {
		this.alterInBatch = alterInBatchView;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setEditorDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setEditorLabelText(String labelText) {
		this.editorLabelText = (labelText != null ? labelText : "");
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setEditorMaxLength(int maxLength) {
		this.editorMaxLength = maxLength;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setEditorMember(boolean editorMember) {
		this.editorMember = editorMember;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setEditorMnemonic(String mnemonic) {
		this.editorMnemonic = (mnemonic != null ? mnemonic : "");
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setEditorPosition(int position) {
		this.editorPosition = position;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setEditorReferenceWeight(ReferenceWeight referenceWeight) {
		this.editorReferenceWeight = referenceWeight;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setEditorResourceId(String resourceId) {
		this.ressourceIdentifier = (resourceId != null ? resourceId : "");
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setEditorToolTipText(String toolTipText) {
		this.editorToolTipText = (toolTipText != null ? toolTipText : "");
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setIndex(boolean index) {
		this.hasIndex = index;
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Override
	public void setIndividualDefaultValue(String value) throws IllegalArgumentException {
		ensure(value != null, "value cannot be null.");
		this.individualDefaultValue = value;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public void setHistory(String newHistory) {
		this.history = (newHistory != null ? newHistory : "");
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setHideReference(boolean hideReference) {
		this.hideReference = hideReference;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public void setIndexSearchMember(boolean b) {
		this.indexSearchMember = b;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public void setLastModificationField(boolean b) {
		this.lastModificationField = b;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public void setListItemField(boolean b) {
		this.listItemField = b;
	}

	/**
	 * @changed OLI 03.05.2013 - Approved.
	 */
	@Override
	public void setName(String name) {
		this.name = (name != null ? name : "");
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
		if (primaryKey) {
			this.setNotNull(true);
		}
	}

	/**
	 * @changed OLI 29.04.2013 - Approved.
	 */
	@Override
	public void setRelation(RelationModel relation) {
		this.relation = relation;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public void setRemovedStateField(boolean b) {
		this.removedStateField = b;
	}

	/**
	 * @changed OLI 29.04.2013 - Approved.
	 */
	@Override
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * @changed OLI 16.10.2013 - Added.
	 */
	@Override
	public void setSequenceForKeyGeneration(SequenceModel sequence) {
		this.sequenceForKeyGeneration = sequence;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setSynchronizeId(boolean synchronizeId) {
		this.synchronizeId = synchronizeId;
	}

	/**
	 * @changed OLI 02.05.2013 - Added.
	 */
	@Override
	public void setSynchronized(boolean synchrnzd) {
		this.synchronizedField = synchrnzd;
	}

	/**
	 * @changed OLI 07.12.2015 - Added.
	 */
	@Override
	public void setSuppressForeignKeyConstraint(boolean suppressed) {
		this.suppressForeignKeyConstraint = suppressed;
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Override
	public void setTable(TableModel table) {
		this.table = table;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public void setTechnicalField(boolean b) {
		this.technicalField = b;
	}

	/**
	 * @changed OLI 10.06.2015 - Approved.
	 */
	@Override
	public void setTransient(boolean b) {
		this.transientField = b;
	}

	/**
	 * @changed OLI 02.05.2013 - Approved.
	 */
	@Override
	public void setUnique(boolean b) {
		this.unique = b;
	}

	/**
	 * @changed OLI 24.05.2016 - Added.
	 */
	@Override
	public OptionModel[] getOptions() {
		List<OptionModel> l = new corentx.util.SortedVector<OptionModel>();
		ParameterUtil pu = new ParameterUtil();
		for (String p : pu.getParameters(this)) {
			if (p.contains(":")) {
				String v = p.substring(p.indexOf(":") + 1).trim();
				p = p.substring(0, p.indexOf(":")).trim();
				l.add(new Option(p, v));
			} else {
				l.add(new Option(p.trim()));
			}
		}
		return l.toArray(new OptionModel[0]);
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public OptionModel[] getOptionsByName(String name) {
		List<OptionModel> l = new corentx.util.SortedVector<OptionModel>();
		for (OptionModel o : this.getOptions()) {
			if (o.getName().equals(name)) {
				l.add(o);
			}
		}
		return l.toArray(new OptionModel[0]);
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public void addOption(OptionModel option) {
		ensure(option != null, "option to add cannot be null.");
		for (OptionModel o : this.getOptions()) {
			if (o.equals(option)) {
				return;
			}
		}
		this.setParameters(this.getParameters()
				+ (this.getParameters().length() > 0 ? "|" : "")
				+ option.getName()
				+ ((option.getParameter() != null) && !option.getParameter().isEmpty() ? ":" + option.getParameter()
						: ""));
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public OptionModel getOptionAt(int i) {
		return this.getOptions()[i];
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public OptionModel getOptionByName(String name) {
		for (OptionModel o : this.getOptions()) {
			if (o.getName().equals(name)) {
				return o;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public int getOptionCount() {
		return this.getOptions().length;
	}

	/**
	 * @changed OLI 26.05.2016 - Added.
	 */
	@Override
	public boolean isOptionSet(String optionName) {
		return this.getOptionByName(optionName) != null;
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public void removeOption(OptionModel option) {
		OptionModel[] os = this.getOptions();
		this.setParameters("");
		for (OptionModel o : os) {
			if (!o.equals(option)) {
				this.addOption(o);
			}
		}
	}

}