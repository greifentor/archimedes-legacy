/*
 * Tabelle.java
 *
 * 20.03.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;
import gengen.metadata.AttributeMetaData;
import gengen.metadata.ModelMetaData;
import gengen.metadata.OrderDirection;
import gengen.metadata.OrderMetaData;
import gengen.metadata.SelectionViewMetaData;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.geom.GeneralPath;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import archimedes.gui.PaintMode;
import archimedes.gui.diagram.ComponentDiagramm;
import archimedes.gui.diagram.CoordinateConverter;
import archimedes.gui.diagram.ShapeContainer;
import archimedes.legacy.Archimedes;
import archimedes.legacy.gui.CommentSubEditorFactory;
import archimedes.legacy.gui.HistoryOwnerSubEditorFactory;
import archimedes.legacy.gui.NReferenzlistenSubEditorFactory;
import archimedes.legacy.gui.OptionListSubEditorFactory;
import archimedes.legacy.gui.PanellistenSubEditorFactory;
import archimedes.legacy.gui.StereotypenSubEditorFactory;
import archimedes.legacy.gui.TabellenspaltenSubEditorFactory;
import archimedes.legacy.gui.TabellenspaltenlistenSubEditorFactory;
import archimedes.legacy.gui.ToStringContainer;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.NReferenzModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.legacy.transfer.DefaultCopyAndPasteController;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.NReferenceModel;
import archimedes.model.OptionModel;
import archimedes.model.OptionType;
import archimedes.model.OrderMemberModel;
import archimedes.model.PanelModel;
import archimedes.model.RelationModel;
import archimedes.model.SelectionMemberModel;
import archimedes.model.StereotypeModel;
import archimedes.model.TableModel;
import archimedes.model.ToStringContainerModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.GUIRelationModel;
import archimedes.model.gui.GUIViewModel;
import corent.base.Direction;
import corent.base.StrUtil;
import corent.db.DBType;
import corent.db.OrderClauseDirection;
import corent.djinn.DefaultComponentFactory;
import corent.djinn.DefaultEditorDescriptor;
import corent.djinn.DefaultEditorDescriptorList;
import corent.djinn.DefaultLabelFactory;
import corent.djinn.DefaultSubEditorDescriptor;
import corent.djinn.DefaultTabDescriptor;
import corent.djinn.DefaultTabbedPaneFactory;
import corent.djinn.DefaultVectorPanelButtonFactory;
import corent.djinn.EditorDescriptorList;
import corent.djinn.Selectable;
import corent.djinn.TabDescriptor;
import corent.djinn.TabbedPaneFactory;
import corentx.util.SortedVector;

/**
 * Diese Klasse repr&auml;sentiert eine Tabelle der Archimedes-Applikation.
 * 
 * <P>
 * <H3>Properties:</H3>
 * <TABLE BORDER=1>
 * <TR VALIGN=TOP>
 * <TH>Property</TH>
 * <TH>Default</TH>
 * <TH>Typ</TH>
 * <TH>Beschreibung</TH>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>archimedes.scheme.Tabelle.suppress.<BR>
 * table.out.of.bounds</TD>
 * <TD>false</TD>
 * <TD>Boolean</TD>
 * <TD>
 * Wird diese Property gesetzt, so werden die Fehlermeldungen, die &uuml;ber
 * fehlende Views informieren, innerhalb der Methoden <TT>getX(ViewModel)</TT>
 * und <TT>getX(ViewModel)</TT> unterdr&uuml;ckt.</TD>
 * </TR>
 * </TABLE>
 * <P>
 * &nbsp;
 * 
 * @author ollie
 * 
 * @changed OLI 18.12.2007 - Erweiterung um die Implementierung des Interfaces
 *          Transferable. Einbau M&oumg;glichkeit die Fehlerausgabe bei
 *          fehlendem View in den Methoden <TT>getX(ViewModel)</TT> und
 *          <TT>getX(ViewModel)</TT>zu unterdr&uuml;cken (Property
 *          <I>archimedes.scheme.Tabelle.suppress.table.out.of.bounds</I>).
 *          Einf&uuml;gen der Methode <TT>toTemplate()</TT>.
 * @changed OLI 24.04.2008 - Der Bug aus der Methode <TT>toTemplate()</TT>, der
 *          zu einer <TT>NullPointerException</TT> beim Kopieren einer Tabelle
 *          f&uuml;hrte, wenn keine Farbe f&uuml;r den Hintergrund des
 *          Tabellenkopfes angegeben war, sollte nun behoben sein. Wenn keine
 *          Farben f&uuml;r Schrift und Tabellenhintergrund angegeben, werden
 *          nun die Defaults schwarz f&uuml;r die Schrift und wei&szlig;
 *          f&uuml;r den Hintergrund angenommen.
 * @changed OLI 11.05.2008 - Erweiterung der Implementierung des Interfaces
 *          <TT>TabbedEditable</TT> um die Methode <TT>isTabEnabled(int)</TT>.
 *          Entsprechende Erweiterung der Anzeigeroutine.
 * @changed OLI 10.08.2008 - Erweiterung um das Abgrauen bzw. Abblenden von
 *          technischen Feldern.
 * @changed OLI 11.08.2008 - Einbeziehung der TechnicalField-Flagge in das
 *          Template. Dies wird im Zusammenspiel mit der
 *          Copy-&amp;-Paste-Mechanik wichtig.
 * @changed OLI 15.09.2008 - Erweiterung um die Implementierung der Methoden
 *          <TT>getCodeGeneratorOptions()</TT> und
 *          <TT>setCodeGeneratorOptions(String)</TT>.
 * @changed OLI 11.10.2008 - Erweiterung um die Anzeige der Kuller um
 *          Relationsknicke.
 * @changed OLI 30.12.2008 - Erweiterung um die Ber&uuml;cksichtigung von
 *          Defaultwerten bei der Generierung von Create-Statements f&uuml;r die
 *          Tabelle (Methode
 *          <TT>makeCreateStatement(boolean, boolean, boolean)</TT>).
 * @changed OLI 12.05.2009 - Erweiterung um die Implementierung der Methode
 *          <TT>makeCreateStatement(boolean, boolean, boolean, String)</TT>.
 * @changed OLI 17.05.2009 - Erweiterung um die Implementierung der Methode
 *          <TT>makeInsertStatementCounted()</TT>.
 * @changed OLI 28.05.2009 - Debugging an der Methode
 *          <TT>getFieldnamesPKFirst(boolean,
 *         boolean)</TT>.
 * @changed OLI 06.10.2009 - Erweiterung um die Implementierung der Methode
 *          <TT>isOfStereotype(String)</TT>.
 * @changed OLI 04.06.2010 - Anpassung an die Erweiterungen des
 *          ClassMetaData-Interfaces. Erweiterung um die Implementierung des
 *          <TT>SelectionViewMetaData</TT>-Interfaces.
 * @changed OLI 20.12.2011 - Erweiterung um die Implementierung der Methode
 *          <CODE>getAttribute(String)</CODE>.
 */

public class Tabelle implements Selectable, SelectionViewMetaData, TabellenModel, Transferable {

	/** Ein Bezeichner zum Zugriff auf den Namen der Tabelle. */
	public static final int ID_NAME = 0;
	/** Ein Bezeichner zum Zugriff auf die Beschreibung zur Tabelle. */
	public static final int ID_BESCHREIBUNG = 1;
	/** Ein Bezeichner zum Zugriff auf die Stereotype zur Tabelle. */
	public static final int ID_STEREOTYPE = 2;
	/** Ein Bezeichner zum Zugriff auf die Aufgehoben-Markierung der Tabelle. */
	public static final int ID_DEPRECATED = 3;
	/** Ein Bezeichner zum Zugriff auf die Codieren-Markierung der Tabelle. */
	public static final int ID_CODIEREN = 4;
	/** Ein Bezeichner zum Zugriff auf das CodeVezeichnis zur Tabelle. */
	public static final int ID_CODEVERZEICHNIS = 5;
	/**
	 * Ein Bezeichner zum Zugriff auf die Liste mit den Equalsmembers der
	 * Tabelle.
	 */
	public static final int ID_EQUALSMEMBERS = 6;
	/**
	 * Ein Bezeichner zum Zugriff auf die Liste mit den Comparemembers der
	 * Tabelle.
	 */
	public static final int ID_COMPAREMEMBERS = 7;
	/**
	 * Ein Bezeichner zum Zugriff auf die Liste mit den ToStringmembers der
	 * Tabelle.
	 */
	public static final int ID_TOSTRINGMEMBERS = 8;
	/**
	 * Ein Bezeichner zum Zugriff auf die Liste mit den ToCombomembers der
	 * Tabelle.
	 */
	public static final int ID_TOCOMBOMEMBERS = 9;
	/**
	 * Ein Bezeichner zum Zugriff auf die Liste mit den HashCodemembers der
	 * Tabelle.
	 */
	public static final int ID_HASHCODEMEMBERS = 10;
	/**
	 * Ein Bezeichner zum Zugriff auf die Liste mit den Auswahlmembers der
	 * Tabelle.
	 */
	public static final int ID_AUSWAHLMEMBERS = 11;
	/**
	 * Ein Bezeichner zum Zugriff auf die Liste mit den Ordermembers der
	 * Tabelle.
	 */
	public static final int ID_ORDERMEMBERS = 12;
	/**
	 * Ein Bezeichner zum Zugriff auf den Kontextbezeichner der Tabelle
	 * (Editordescriptor).
	 */
	public static final int ID_KONTEXTNAME = 13;
	/** Ein Bezeichner zum Zugriff auf die Unique-Formel f&uuml;r die Tabelle. */
	public static final int ID_UNIQUEFORMULA = 14;
	/**
	 * Ein Bezeichner zum Zugriff auf die Liste mit den manipulierbaren
	 * NReferenzen der Tabelle.
	 */
	public static final int ID_NREFERENZEN = 15;
	/** Ein Bezeichner zum Zugriff auf die Beschriftungsfarbe der Tabelle. */
	public static final int ID_BESCHRIFTUNGSFARBE = 16;
	/** Ein Bezeichner zum Zugriff auf die Hintergrundfarbe der Tabelle. */
	public static final int ID_HINTERGRUNDFARBE = 17;
	/** Ein Bezeichner zum Zugriff auf die DynamicCode-Flagge der Tabelle. */
	public static final int ID_DYNAMICCODE = 18;
	/** Ein Bezeichner zum Zugriff auf die Liste mit den Panels der Tabelle. */
	public static final int ID_PANELS = 19;
	/** Ein Bezeichner zum Zugriff auf die die Inherited-Flagge. */
	public static final int ID_INHERITED = 20;
	/** Ein Bezeichner zum Zugriff auf die die ActiveInApplication-Flagge. */
	public static final int ID_ACTIVEINAPPLICATION = 21;
	/** Ein Bezeichner zum Zugriff auf die die NM-Relation-Flagge. */
	public static final int ID_NMRELATION = 22;
	/** Ein Bezeichner zum Zugriff auf die die FirstGenerationDone-Flagge. */
	public static final int ID_FIRSTGENERATIONDONE = 23;
	/** Ein Bezeichner zum Zugriff auf die die InDevelopmentProcess-Flagge. */
	public static final int ID_INDEVELOPMENTPROCESS = 24;
	/** Ein Bezeichner zum Zugriff auf die CodeGeneratorOptionen. */
	public static final int ID_CODEGENERATOROPTIONS = 25;
	/** Ein Bezeichner zum Zugriff auf die Historie. */
	public static final int ID_HISTORY = 26;
	/** Ein Bezeichner zum Zugriff auf die Optionen. */
	public static final int ID_OPTIONS = 27;
	/**
	 * Ein Bezeichner zum Zugriff auf die zus&auml;tzlichen Constraints f&uuml;r
	 * die Create-Statments der Tabelle.
	 */
	public static final int ID_ADDITIONAL_CREATE_CONSTRAINTS = 28;
	/** Ein Bezeichner zum Zugriff auf die Flagge f&uuml;r externe Tabellen. */
	public static final int ID_EXTERNAL_TABLE = 29;
	/**
	 * Ein Bezeichner zum Zugriff auf die komplexe
	 * Fremdschl&uuml;ssel-Definition.
	 */
	public static final int ID_COMPLEX_FOREIGN_KEY_DEFINITION = 30;

	private static int Count = 0;

	private boolean activeInApplication = false;
	private Color beschriftungsfarbe = Color.black;
	private String generatorCodeOptions = "";
	private List<ToStringContainer> comboStringMembers = new Vector<ToStringContainer>();
	private List<ColumnModel> compareToMembers = new Vector<ColumnModel>();
	private String contextName = "";
	private boolean deprecated = false;
	private DiagrammModel diagramm = null;
	private boolean dynamicCode = true;
	private List<ColumnModel> equalsMembers = new Vector<ColumnModel>();
	private boolean firstGenerationDone = false;
	private boolean generateCode = true;
	private List<ColumnModel> hashCodeMembers = new Vector<ColumnModel>();
	private Color hintergrundfarbe = Color.white;
	private String historie = "";
	private boolean inherited = false;
	private String name = "Tabelle" + (Count++);
	private boolean manyToManyRelation = false;
	private List<OrderMemberModel> orderMembers = new Vector<OrderMemberModel>();
	private SortedVector<PanelModel> panels = new SortedVector<PanelModel>();
	private List<SelectionMemberModel> selectableColumns = new Vector<SelectionMemberModel>();
	private SortedVector<TabellenspaltenModel> spalten = new SortedVector<TabellenspaltenModel>();
	private List<ToStringContainer> toStringMembers = new Vector<ToStringContainer>();
	private String uniqueformula = "";
	private Hashtable<ViewModel, Integer> x = new Hashtable<ViewModel, Integer>();
	private Hashtable<ViewModel, Integer> y = new Hashtable<ViewModel, Integer>();

	/**
	 * Generiert eine neue Tabelle mit den angegebenen Parametern.
	 * 
	 * @param view
	 *            Der View, auf dem die Tabelle erstellt werden soll.
	 * @param x
	 *            Die X-Koordinate der linken, oberen Ecke der Tabelle.
	 * @param y
	 *            Die Y-Koordinate der linken, oberen Ecke der Tabelle.
	 * @param d
	 *            Das Diagramm, zu dem die Tabelle geh&ouml;rt.
	 */
	public Tabelle(ViewModel view, int x, int y, DiagrammModel d) {
		super();
		this.setXY(view, x, y);
		this.setDiagramm(d);
	}

	/* Mutatoren und Accessoren. */

	/**
	 * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r das Attribut
	 * Name.
	 * 
	 * @param name
	 *            Der neue Wert f&uuml;r das Attribut Name.
	 */
	@Override
	public void setName(String name) {
		if (name == null) {
			name = "";
		}
		this.name = name;
	}

	/**
	 * Diese Methode wandelt die Tabelle in ein Property-Template um.
	 * 
	 * @return Ein String mit der Property-Template-Repr&auml;sentation der
	 *         Tabelle.
	 * 
	 * @changed OLI 18.12.2007 - Hinzugef&uuml;gt.
	 *          <P>
	 *          OLI 22.12.2007 - Vervollst&auml;ndigung des Templates auf alle
	 *          Attribute.
	 *          <P>
	 *          OLI 24.04.2008 - Wenn keine Farben f&uuml;r Schrift und
	 *          Tabellenhintergrund angegeben, werden nun die Defaults schwarz
	 *          f&uuml;r die Schrift und wei&szlig; f&uuml;r den Hintergrund
	 *          angenommen.
	 *          <P>
	 *          OLI 11.08.2008 - Einbeziehung der TechnicalField-Flagge in das
	 *          Template.
	 * 
	 */
	public String toTemplate() {
		boolean b = false;
		DomainModel dom = null;
		OrderMemberModel omm = null;
		PanelModel pm = null;
		SortedVector svdom = new SortedVector();
		StereotypeModel stm = null;
		String ls = System.getProperty("line.separator");
		StringBuffer sb = new StringBuffer();
		TabellenspaltenModel tsm = null;
		ToStringContainer tsc = null;
		List<ToStringContainer> v = null;
		for (int i = 0, len = this.getTabellenspaltenCount(); i < len; i++) {
			tsm = this.getTabellenspalteAt(i);
			if (!svdom.contains(tsm.getDomain())) {
				svdom.addElement(tsm.getDomain());
			}
		}
		sb.append("template.active.in.application=" + this.isActiveInApplication() + ls);
		sb.append("template.code=" + this.isGenerateCode() + ls);
		sb.append("template.codedirectory=" + this.getCodeVerzeichnis() + ls);
		sb.append("template.color.foreground="
				+ (this.getSchriftfarbe() != null ? this.getSchriftfarbe().toString() : Color.black.toString()) + ls);
		sb.append("template.color.background="
				+ (this.getHintergrundfarbe() != null ? this.getHintergrundfarbe().toString() : Color.white.toString())
				+ ls);
		sb.append("template.contextname=" + this.getContextName() + ls);
		sb.append("template.complexForeignKey=" + this.getComplexForeignKeyDefinition() + ls);
		sb.append("template.deprecated=" + this.isDeprecated() + ls);
		sb.append("template.description=" + this.getComment().replace("\n", "$BR$") + ls);
		sb.append("template.dynamic.code=" + this.isDynamicCode() + ls);
		sb.append("template.first.generation.done=" + this.isFirstGenerationDone() + ls);
		sb.append("template.inherited=" + this.isInherited() + ls);
		sb.append("template.nmrelation=" + this.isNMRelation() + ls);
		sb.append("template.uniqueformula=" + this.getComplexUniqueSpecification() + ls);
		sb.append("template.additionalCreateConstraints=" + this.getAdditionalCreateConstraints() + ls);
		sb.append(ls);
		sb.append(ls);
		sb.append("template.panel.count=" + this.getPanelCount() + ls);
		sb.append(ls);
		for (int i = 0, len = this.getPanelCount(); i < len; i++) {
			pm = this.getPanelAt(i);
			sb.append("template.panel." + i + ".title=" + pm.getTabTitle() + ls);
			sb.append("template.panel." + i + ".mnemonic=" + pm.getTabMnemonic() + ls);
			sb.append("template.panel." + i + ".tooltiptext=" + pm.getTabToolTipText() + ls);
			sb.append(ls);
		}
		sb.append(ls);
		sb.append("template.domain.count=" + svdom.size() + ls);
		sb.append(ls);
		for (int i = 0, len = svdom.size(); i < len; i++) {
			dom = (DomainModel) svdom.elementAt(i);
			sb.append("template.domain." + i + ".name=" + dom.getName() + ls);
			sb.append("template.domain." + i + ".type=" + DBType.Convert(dom.getDataType()) + ls);
			sb.append("template.domain." + i + ".vks=" + dom.getLength() + ls);
			sb.append("template.domain." + i + ".nks=" + dom.getDecimalPlace() + ls);
			sb.append("template.domain." + i + ".description=" + dom.getComment().replace("\n", "$BR$") + ls);
			sb.append(ls);
		}
		sb.append(ls);
		sb.append("template.stereotype.count=" + this.getStereotypenCount() + ls);
		sb.append(ls);
		SortedVector sv = new SortedVector();
		for (int i = 0, len = this.getStereotypenCount(); i < len; i++) {
			stm = this.getStereotypeAt(i);
			sv.addElement(stm);
		}
		for (int i = 0, len = sv.size(); i < len; i++) {
			stm = (StereotypeModel) sv.elementAt(i);
			sb.append("template.stereotype." + i + ".name=" + stm.getName() + ls);
			sb.append("template.stereotype." + i + ".isdonotprint=" + stm.isDoNotPrint() + ls);
			sb.append("template.stereotype." + i + ".ishidetable=" + stm.isHideTable() + ls);
			sb.append("template.stereotype." + i + ".description=" + stm.getComment().replace("\n", "$BR$") + ls);
		}
		sb.append(ls);
		sb.append("template.column.count=" + this.getTabellenspaltenCount() + ls);
		sb.append(ls);
		sv = new SortedVector();
		for (int i = 0, len = this.getTabellenspaltenCount(); i < len; i++) {
			tsm = this.getTabellenspalteAt(i);
			sv.addElement(tsm);
		}
		for (int i = 0, len = sv.size(); i < len; i++) {
			tsm = (TabellenspaltenModel) sv.elementAt(i);
			sb.append("template.column." + i + ".name=" + tsm.getName() + ls);
			sb.append("template.column." + i + ".alterinbatch=" + tsm.isAlterInBatch() + ls);
			sb.append("template.column." + i + ".comparetomember=" + this.isCompareToMember(tsm) + ls);
			sb.append("template.column." + i + ".coded=" + tsm.isKodiert() + ls);
			sb.append("template.column." + i + ".deprecated=" + tsm.isDeprecated() + ls);
			sb.append("template.column." + i + ".disabled=" + tsm.isDisabled() + ls);
			sb.append("template.column." + i + ".description=" + tsm.getComment().replace("\n", "$BR$") + ls);
			sb.append("template.column." + i + ".domain=" + tsm.getDomain().getName() + ls);
			sb.append("template.column." + i + ".editormember=" + tsm.isEditormember() + ls);
			sb.append("template.column." + i + ".editorposition=" + tsm.getEditorPosition() + ls);
			sb.append("template.column." + i + ".equalsmember=" + this.isEqualsMember(tsm) + ls);
			sb.append("template.column." + i + ".global=" + tsm.isGlobal() + ls);
			sb.append("template.column." + i + ".globalid=" + tsm.isGlobalId() + ls);
			sb.append("template.column." + i + ".hashcodemember=" + this.isHashCodeMember(tsm) + ls);
			sb.append("template.column." + i + ".indexed=" + tsm.isIndexed() + ls);
			sb.append("template.column." + i + ".indexsearchmember=" + tsm.isIndexSearchMember() + ls);
			sb.append("template.column." + i + ".labeltext=" + tsm.getLabelText() + ls);
			sb.append("template.column." + i + ".lastmodificationfield=" + tsm.isLastModificationField() + ls);
			sb.append("template.column." + i + ".maxcharacters=" + tsm.getMaxCharacters() + ls);
			sb.append("template.column." + i + ".mnemonic=" + tsm.getMnemonic() + ls);
			sb.append("template.column." + i + ".notnull=" + tsm.isNotNull() + ls);
			sb.append("template.column." + i + ".panel=" + tsm.getPanel().getPanelNumber() + ls);
			sb.append("template.column." + i + ".primarykey=" + tsm.isPrimarykey() + ls);
			sb.append("template.column." + i + ".referenceweight=" + tsm.getReferenceWeight().toString() + ls);
			sb.append("template.column." + i + ".removedstatefield=" + tsm.isRemovedStateField() + ls);
			sb.append("template.column." + i + ".ressourceidentifier=" + tsm.getRessourceIdentifier() + ls);
			sb.append("template.column." + i + ".technicalfield=" + tsm.isTechnicalField() + ls);
			sb.append("template.column." + i + ".tooltiptext=" + tsm.getToolTipText() + ls);
			sb.append("template.column." + i + ".writeablemember=" + tsm.isWriteablemember() + ls);
			b = false;
			for (ToStringContainer tsc0 : this.getComboStringMembers()) {
				if (tsm.getFullName().equals(tsc0.getTabellenspalte().getFullName())) {
					tsc = tsc0;
					b = true;
					break;
				}
			}
			sb.append("template.column." + i + ".tocombostring.member=" + b + ls);
			sb.append("template.column." + i + ".tocombostring.prefix="
					+ (b ? tsc.getPrefix().replace(" ", "&nbsp;") : "") + ls);
			sb.append("template.column." + i + ".tocombostring.suffix="
					+ (b ? tsc.getSuffix().replace(" ", "&nbsp;") : "") + ls);
			b = false;
			for (ToStringContainer t : this.getToStringMembers()) {
				if (tsm.getFullName().equals(t.getColumn().getFullName())) {
					b = true;
					break;
				}
			}
			sb.append("template.column." + i + ".tostring.member=" + b + ls);
			sb.append("template.column." + i + ".tostring.prefix=" + (b ? tsc.getPrefix().replace(" ", "&nbsp;") : "")
					+ ls);
			sb.append("template.column." + i + ".tostring.suffix=" + (b ? tsc.getSuffix().replace(" ", "&nbsp;") : "")
					+ ls);
			sb.append("template.column." + i + ".selectionmember=" + this.getAuswahlMembers().contains(tsm) + ls);
			b = false;
			for (OrderMemberModel om : this.getSelectionViewOrderMembers()) {
				if (tsm.getFullName().equals(om.getOrderColumn().getFullName())) {
					b = true;
					break;
				}
			}
			sb.append("template.column." + i + ".orderbymember=" + b + ls);
			sb.append("template.column." + i + ".canbereferenced=" + tsm.isCanBeReferenced() + ls);
			sb.append("template.column." + i + ".hidereference=" + tsm.isHideReference() + ls);
			if ((tsm.getRelation() != null) && (((Relation) tsm.getRelation()).getReferencer() == tsm)) {
				sb.append("template.column." + i + ".referenceto.table="
						+ tsm.getRelation().getReferenced().getTable().getName() + ls);
				sb.append("template.column." + i + ".referenceto.column=" + tsm.getRelation().getReferenced().getName()
						+ ls);
			} else {
				sb.append("template.column." + i + ".referenceto.table=" + ls);
			}
			sb.append(ls);
		}
		return sb.toString();
	}

	/* Ueberschreiben der Methoden der Superklasse. */

	@Override
	public String toString() {
		return this.name;
	}

	/* Implementierung des Interfaces TabellenModel. */

	@Deprecated
	@Override
	public DiagrammModel getDiagramm() {
		return this.diagramm;
	}

	@Override
	public void setDiagramm(DiagrammModel d) {
		this.diagramm = d;
	}

	@Deprecated
	@Override
	public void addTabellenspalte(TabellenspaltenModel ts) {
		ts.setTabelle(this);
		this.spalten.add(ts);
		this.diagramm.addToFieldCache(ts);
	}

	@Deprecated
	@Override
	public void removeTabellenspalte(TabellenspaltenModel ts) {
		// this.spalten.removeElement(ts);
		for (int i = 0, len = this.spalten.size(); i < len; i++) {
			if (this.spalten.elementAt(i) == ts) {
				this.spalten.removeElementAt(i);
				break;
			}
		}
		this.diagramm.removeFromFieldCache(ts);
		ts.setTabelle(null);
	}

	@Override
	public int getTabellenspaltenCount() {
		return this.spalten.size();
	}

	@Override
	public TabellenspaltenModel getTabellenspalteAt(int i) {
		return (TabellenspaltenModel) this.spalten.elementAt(i);
	}

	@Deprecated
	@Override
	public TabellenspaltenModel getTabellenspalte(String n) {
		for (int i = 0, len = this.spalten.size(); i < len; i++) {
			TabellenspaltenModel tsm = (TabellenspaltenModel) this.spalten.elementAt(i);
			if (tsm.getName().equals(n)) {
				return tsm;
			}
		}
		return null;
		/*
		 * throw new NoSuchElementException("Tabellespalte mit Namen \"" + n +
		 * "\" existiert " + "nicht in Tabelle \"" + this.getName() + "\"!");
		 */
	}

	@Deprecated
	@Override
	public Vector getTabellenspalten() {
		// return new Vector(this.spalten);
		return this.spalten;
	}

	/**
	 * @changed OLI 12.11.2008 - Erweiterung um den PaintMode.
	 *          <P>
	 *          OLI 16.12.2008 - Erweiterung um das ComponentDiagramm als
	 *          Parameter.
	 *          <P>
	 * 
	 */
	@Override
	public ShapeContainer paintTabelle(CoordinateConverter cd, ViewModel view, Graphics g, PaintMode pntm) {
		DiagrammModel dm = this.getDiagramm();
		Graphics2D g2d = (Graphics2D) g;
		Color dcolor = null;
		int fontsize = this.getDiagramm().getFontSizeTableContents();
		int hos = 0;
		int htmp = 0;
		int tl = 0;
		final int tmx = this.getX(view);
		final int tmy = this.getY(view);
		int w = 0;
		int w0 = 0;
		int w1 = 0;
		int wtmp = 0;
		int x = tmx;
		int y = tmy;
		int y0 = y;
		String s = null;
		// dcolor = g2d.getColor();
		dcolor = this.getSchriftfarbe();
		g2d.setColor(dcolor);
		g2d.setFont(new Font("sansserif", Font.BOLD, fontsize));
		hos = g2d.getFontMetrics().getHeight();
		String namestr = (this.isInherited() ? "* " : "") + this.name;
		if (this.isDraft()) {
			namestr = "(".concat(namestr).concat(")");
		}
		w = this.getHeaderWidth(g2d, fontsize) + 10;
		w1 = w;
		// w0 = w;
		tl = w;
		htmp = y + (int) ((double) hos * 1.8);
		htmp += ((int) ((double) hos * 1.2)) * this.getStereotypenCount();
		htmp += ((int) ((double) hos * 1.2)) * this.getOptionCount();
		g2d.setFont(new Font("sansserif", Font.PLAIN, fontsize));
		for (int i = 0, len = this.spalten.size(); i < len; i++) {
			Tabellenspalte ts = (Tabellenspalte) this.spalten.elementAt(i);
			if (ts.isTechnicalField() && view.isHideTechnicalFields()) {
				continue;
			}
			if (ts.isTransient() && view.isHideTransientFields()) {
				continue;
			}
			if (!ts.isDeprecated() || !dm.isDeprecatedTablesHidden()) {
				s = ts.toDiagrammString();
				w = g2d.getFontMetrics().stringWidth(s) + 8;
				wtmp = w;
				if (w1 < wtmp) {
					w1 = wtmp;
				}
				if (w > tl) {
					tl = w;
				}
				htmp += (int) ((double) hos * 1.2);
				if ((ts.getRelation() != null) && view.isShowReferencedColumns()) {
					s = "   -> " + ts.getRelation().getReferenced().getFullName();
					w = g2d.getFontMetrics().stringWidth(s);
					wtmp = g2d.getFontMetrics().stringWidth(s);
					if (w1 < wtmp) {
						w1 = wtmp;
					}
					if (w > tl) {
						tl = w;
					}
					htmp += (int) ((double) hos * 1.2);
				}
			}
		}
		htmp += (int) ((double) hos * 0.6);
		w1 = cd.convert(w);
		if (cd.getZoomFactor() < 0.95d) {
			do {
				g2d.setFont(new Font("sansserif", Font.BOLD, fontsize));
				for (int i = 0, len = this.spalten.size(); i < len; i++) {
					Tabellenspalte ts = (Tabellenspalte) this.spalten.elementAt(i);
					if (!ts.isAufgehoben() || !dm.isAufgehobeneAusblenden()) {
						s = ts.toDiagrammString();
						wtmp = g2d.getFontMetrics().stringWidth(s);
						if (w1 < wtmp) {
							fontsize--;
							break;
						}
					}
				}
			} while ((fontsize > 0) && (w1 < wtmp));
		}
		g2d.setFont(new Font("sansserif", Font.PLAIN, fontsize));
		y += (int) ((double) hos * 1.2);
		y0 = y;
		y += (int) ((double) hos * 0.6);
		y += ((int) ((double) hos * 1.2)) * this.getStereotypenCount();
		y += ((int) ((double) hos * 1.2)) * this.getOptionCount();
		for (int i = 0, len = this.spalten.size(); i < len; i++) {
			Tabellenspalte ts = (Tabellenspalte) this.spalten.elementAt(i);
			if (ts.isTechnicalField() && view.isHideTechnicalFields()) {
				continue;
			}
			if (ts.isTransient() && view.isHideTransientFields()) {
				continue;
			}
			if (!ts.isAufgehoben() || !dm.isAufgehobeneAusblenden()) {
				s = ts.toDiagrammString();
				y += (int) ((double) hos * 1.2);
				if (ts.isTechnicalField() && (this.getDiagramm() != null)
						&& (this.getDiagramm().isPaintTechnicalFieldsInGray())) {
					g2d.setColor(Color.gray);
				} else if (ts.isTransient() && (this.getDiagramm() != null)
						&& (this.getDiagramm().isPaintTransientFieldsInGray())) {
					g2d.setColor(Color.gray);
				} else {
					g2d.setColor(dcolor);
				}
				g2d.drawString(s, cd.convert(x + 5), cd.convert(y));
				if (ts.isAufgehoben()) {
					int off = hos / 2 - 1;
					int woff = g2d.getFontMetrics().stringWidth(s);
					g2d.setColor(Color.red);
					g2d.drawRect(cd.convert(x + 5), cd.convert(y - off), cd.convert(woff), cd.convert(1));
					g2d.setColor(dcolor);
				}
				if ((ts.getRelation() != null) && view.isShowReferencedColumns()) {
					s = "    -> " + ts.getRelation().getReferenced().getFullName();
					y += (int) ((double) hos * 1.2);
					g2d.drawString(s, cd.convert(x + 5), cd.convert(y));
					if (ts.isAufgehoben()) {
						int off = hos / 2 - 1;
						int woff = g2d.getFontMetrics().stringWidth(s);
						g2d.setColor(Color.red);
						g2d.drawRect(cd.convert(x + 5), cd.convert(y - off), cd.convert(woff), cd.convert(1));
						g2d.setColor(dcolor);
					}
				}
			}
		}
		y += (int) ((double) hos * 0.6);
		this.width = tl;
		this.height = htmp - tmy;
		int ty0 = y0;
		y0 = this.drawHeader(cd, g2d, x, tmy, this.width, y0, fontsize, hos, pntm);
		if (this.getHintergrundfarbe() != null) {
			g2d.setColor(this.getHintergrundfarbe());
			g2d.fillRect(cd.convert(tmx), cd.convert(tmy), cd.convert(this.width), cd
					.convert((y0 + (int) ((double) hos * 0.6)) - tmy));
			g2d.setColor(dcolor);
		}
		y0 = this.drawHeader(cd, g2d, x, tmy, this.width, ty0, fontsize, hos, pntm);
		g2d.drawLine(cd.convert(tmx), cd.convert(y0 + (int) ((double) hos * 0.6)), cd.convert(tmx + this.width), cd
				.convert(y0 + (int) ((double) hos * 0.6)));
		g.drawRect(cd.convert(tmx), cd.convert(tmy), cd.convert(this.width), cd.convert(this.height));
		g2d.setColor(Color.lightGray);
		g.fillRect(cd.convert(tmx + 3), cd.convert(tmy + this.height + 1), cd.convert(this.width + 1), cd.convert(3));
		g.fillRect(cd.convert(tmx + this.width + 1), cd.convert(tmy + 3), cd.convert(3), cd.convert(this.height + 1));
		if (this.isDeprecated()) {
			g2d.setColor(Color.red);
			Stroke stroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
			g.drawLine(cd.convert(tmx - 3), cd.convert(tmy - 3), cd.convert(tmx + this.width + 5), cd.convert(tmy
					+ this.height + 5));
			g.drawLine(cd.convert(tmx + this.width + 5), cd.convert(tmy - 3), cd.convert(tmx - 3), cd.convert(tmy
					+ this.height + 5));
			g2d.setStroke(stroke);
		}
		g2d.setColor(dcolor);
		return new ShapeContainer(new Rectangle(cd.convert(tmx), cd.convert(tmy), cd.convert(this.width), cd
				.convert(this.height)), this);
	}

	private int getHeaderWidth(Graphics2D g2d, int fontsize) {
		g2d.setFont(new Font("sansserif", Font.BOLD, fontsize));
		int w = g2d.getFontMetrics().stringWidth(this.getTableNameString());
		g2d.setFont(new Font("sansserif", Font.PLAIN, fontsize));
		for (StereotypeModel st : this.getStereotypes()) {
			w = Math.max(w, g2d.getFontMetrics().stringWidth(this.getStereoTypeString(st)) + 10);
		}
		for (OptionModel o : this.getOptions()) {
			w = Math.max(w, g2d.getFontMetrics().stringWidth(this.getOptionString(o)) + 10);
		}
		return w;
	}

	private int drawHeader(CoordinateConverter cd, Graphics2D g2d, int x, int yf, int w0, int y0, int fontsize,
			int hos, PaintMode pntm) {
		g2d.setFont(new Font("sansserif", Font.BOLD, fontsize - 2));
		if (this.isNMRelation()) {
			g2d.drawString("N:M", cd.convert(x + 2), cd.convert(yf + g2d.getFontMetrics().getHeight() - 2));
		}
		g2d.setFont(new Font("sansserif", Font.BOLD, fontsize));
		String namestr = this.getTableNameString();
		w0 = g2d.getFontMetrics().stringWidth(namestr) + 10;
		g2d.drawString(namestr, cd.convert(x + 5 + (this.width / 2) - (w0 / 2)), cd.convert(y0));
		// STEREOTYPEN
		for (int i = 0, len = this.getStereotypenCount(); i < len; i++) {
			g2d.setFont(new Font("sansserif", Font.PLAIN, fontsize));
			String stn = this.getStereoTypeString(this.getStereotypeAt(i));
			w0 = g2d.getFontMetrics().stringWidth(stn) + 10;
			y0 += (int) ((double) hos * 1.2);
			g2d.drawString(stn, cd.convert(x + 5 + (this.width / 2) - (w0 / 2)), cd.convert(y0));
		}
		// OPTIONS
		for (int i = 0, len = this.getOptionCount(); i < len; i++) {
			g2d.setFont(new Font("sansserif", Font.PLAIN, fontsize));
			String stn = this.getOptionString(this.getOptionAt(i));
			w0 = g2d.getFontMetrics().stringWidth(stn) + 10;
			y0 += (int) ((double) hos * 1.2);
			g2d.drawString(stn, cd.convert(x + 5 + (this.width / 2) - (w0 / 2)), cd.convert(y0));
		}
		return y0;
	}

	private String getTableNameString() {
		String n = (this.isInherited() ? "* " : "") + this.name;
		if (this.isDraft()) {
			n = "(".concat(n).concat(")");
		}
		return n;
	}

	private String getStereoTypeString(StereotypeModel stereotype) {
		return "<< " + stereotype.toString() + " >>";
	}

	private String getOptionString(OptionModel option) {
		return "-- " + this.renderOption(option) + " --";
	}

	private String renderOption(OptionModel option) {
		String s = option.getName();
		if ((option.getParameter() != null) && !option.getParameter().isEmpty()) {
			s += ":" + option.getParameter();
		}
		return s;
	}

	/**
	 * @changed OLI 11.11.2008 - Erweiterung um die Anzeige der Kuller um
	 *          Relationsknicke.
	 *          <P>
	 *          OLI 12.11.2008 - Erweiterung um den PaintMode.
	 *          <P>
	 *          OLI 16.12.2008 - Erweiterung um das ComponentDiagramm.
	 * 
	 */
	@Override
	public Vector paintRelationen(CoordinateConverter cd, ViewModel view, Graphics g, PaintMode pntm) {
		DiagrammModel dm = this.getDiagramm();
		Vector v = new Vector();
		for (int i = 0, len = this.spalten.size(); i < len; i++) {
			TabellenspaltenModel ts = (TabellenspaltenModel) this.spalten.elementAt(i);
			if (ts.isHideReference()) {
				continue;
			}
			if (!ts.isDeprecated() || !dm.isAufgehobeneAusblenden()) {
				if (ts.getRelation() != null) {
					if (!((Tabelle) ts.getRelation().getReferenced().getTable()).getViews().contains(view)) {
						continue;
					}
					Graphics2D g2d = (Graphics2D) g;
					GeneralPath gp = new GeneralPath();
					Point p = null;
					Vector points = ((Relation) ts.getRelation()).getShapePoints(view);
					int lenj = points.size();
					int hthalf = ComponentDiagramm.HIT_TOLERANCE / 2;
					if (pntm == PaintMode.EDITOR) {
						g2d.setColor(new Color(0, 153, 102));
						for (int j = 0; j < lenj; j++) {
							p = (Point) points.elementAt(j);
							g2d.drawArc((int) cd.convert(p.getX() - hthalf), (int) cd.convert(p.getY() - hthalf), cd
									.convert(ComponentDiagramm.HIT_TOLERANCE), cd
									.convert(ComponentDiagramm.HIT_TOLERANCE), 0, 360);
						}
					}
					if (ts.getReferencedTable().isExternalTable()) {
						g2d.setColor(dm.getRelationColorToExternalTables());
					} else {
						g2d.setColor(dm.getRelationColorRegular());
					}
					if (lenj > 0) {
						p = (Point) points.elementAt(0);
						int x = (int) p.getX() - 3;
						int y = (int) p.getY() - 3;
						if (((Relation) ts.getRelation()).getDirection(view, this) == Direction.LEFT) {
							x -= 3;
						}
						if (((Relation) ts.getRelation()).getDirection(view, this) == Direction.RIGHT) {
							x += 3;
						}
						if (((Relation) ts.getRelation()).getDirection(view, this) == Direction.UP) {
							y -= 3;
						}
						if (((Relation) ts.getRelation()).getDirection(view, this) == Direction.DOWN) {
							y += 3;
						}
						g.fillOval(cd.convert(x), cd.convert(y), cd.convert(7), cd.convert(7));
						gp.moveTo((float) cd.convert(p.getX()), (float) cd.convert(p.getY()));
						for (int j = 1; j < lenj; j++) {
							p = (Point) points.elementAt(j);
							gp.lineTo((float) cd.convert(p.getX()), (float) cd.convert(p.getY()));
						}
					}
					if (ts.isNotNull()) {
						g2d.setStroke(new BasicStroke(1));
					} else {
						g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f,
								new float[] { 8.0f, 12.0f }, 0.0f));
					}
					g2d.draw(gp);
					g2d.setStroke(new BasicStroke());
					v.addElement(new ShapeContainer(gp, ts.getRelation()));
				}
			}
		}
		return v;
	}

	/**
	 * @changed OLI 18.12.2007 - Einbau der M&oumg;glichkeit die Fehlerausgabe
	 *          bei fehlendem View zu unterdr&uuml;cken (Property
	 *          <I>archimedes.scheme.Tabelle.suppress.table.out.of.bounds</I>).<BR>
	 */
	@Override
	public int getX(ViewModel view) {
		Integer it = this.x.get(view);
		if (it != null) {
			return it;
		}
		if (!Boolean.getBoolean("archimedes.scheme.Tabelle.suppress.table.out.of.bounds")) {
			System.out.println("Warnung: MIN_VALUE fuer x-Koordinate in Tabelle " + this.getName() + " angefordert!");
		}
		return Integer.MIN_VALUE;
	}

	/**
	 * @changed OLI 18.12.2007 - Einbau der M&oumg;glichkeit die Fehlerausgabe
	 *          bei fehlendem View zu unterdr&uuml;cken (Property
	 *          <I>archimedes.scheme.Tabelle.suppress.table.out.of.bounds</I>).<BR>
	 */
	@Override
	public int getY(ViewModel view) {
		Integer it = this.y.get(view);
		if (it != null) {
			return it;
		}
		if (!Boolean.getBoolean("archimedes.scheme.Tabelle.suppress.table.out.of.bounds")) {
			System.out.println("Warnung: MIN_VALUE fuer y-Koordinate in Tabelle " + this.getName() + " angefordert!");
		}
		return Integer.MIN_VALUE;
	}

	public void setXY(ViewModel view, int x, int y) {
		this.x.put(view, x);
		this.y.put(view, y);
	}

	@Override
	public Object get(int id) throws IllegalArgumentException {
		switch (id) {
		case ID_NAME:
			return this.getName();
		case ID_BESCHREIBUNG:
			return this.getComment();
		case ID_DEPRECATED:
			return new Boolean(this.isDeprecated());
		case ID_CODIEREN:
			return new Boolean(this.isGenerateCode());
		case ID_CODEVERZEICHNIS:
			return this.getCodeVerzeichnis();
		case ID_EQUALSMEMBERS:
			return this.getEqualsMembersAsRef();
		case ID_COMPAREMEMBERS:
			return this.getCompareMembersAsRef();
		case ID_TOSTRINGMEMBERS:
			return this.getToStringMembersAsRef();
		case ID_TOCOMBOMEMBERS:
			return this.getComboStringMembersAsRef();
		case ID_HASHCODEMEMBERS:
			return this.getHashCodeMembersAsRef();
		case ID_AUSWAHLMEMBERS:
			return this.getAuswahlMembers();
		case ID_ORDERMEMBERS:
			return this.getSelectionViewOrderMembersAsRef();
		case ID_KONTEXTNAME:
			return this.getContextName();
		case ID_UNIQUEFORMULA:
			return this.getComplexUniqueSpecification();
		case ID_NREFERENZEN:
			return this.getNReferencesAsRef();
		case ID_BESCHRIFTUNGSFARBE:
			return this.getSchriftfarbe();
		case ID_HINTERGRUNDFARBE:
			return this.getHintergrundfarbe();
		case ID_DYNAMICCODE:
			return new Boolean(this.isDynamicCode());
		case ID_PANELS:
			return this.panels;
		case ID_INHERITED:
			return new Boolean(this.isInherited());
		case ID_ACTIVEINAPPLICATION:
			return new Boolean(this.isActiveInApplication());
		case ID_NMRELATION:
			return new Boolean(this.isNMRelation());
		case ID_FIRSTGENERATIONDONE:
			return new Boolean(this.isFirstGenerationDone());
		case ID_INDEVELOPMENTPROCESS:
			return new Boolean(this.isDraft());
		case ID_CODEGENERATOROPTIONS:
			return this.getGenerateCodeOptions();
		case ID_HISTORY:
			return this.getHistory();
		case ID_OPTIONS:
			return this.getOptions();
		case ID_ADDITIONAL_CREATE_CONSTRAINTS:
			return this.getAdditionalCreateConstraints();
		case ID_EXTERNAL_TABLE:
			return new Boolean(this.isExternalTable());
		case ID_COMPLEX_FOREIGN_KEY_DEFINITION:
			return this.getComplexForeignKeyDefinition();
		}
		throw new IllegalArgumentException("Klasse Tabelle verfuegt nicht ueber ein Attribut " + id + " (get)!");
	}

	@Override
	public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
		switch (id) {
		case ID_NAME:
			this.setName((String) value);
			return;
		case ID_BESCHREIBUNG:
			this.setComment((String) value);
			return;
		case ID_DEPRECATED:
			this.setDeprecated(((Boolean) value).booleanValue());
			return;
		case ID_CODIEREN:
			this.generateCode = ((Boolean) value).booleanValue();
			return;
		case ID_CODEVERZEICHNIS:
			this.codeFolder = (String) value;
			return;
		case ID_EQUALSMEMBERS:
			this.equalsMembers = (Vector) value;
			return;
		case ID_COMPAREMEMBERS:
			this.compareToMembers = (Vector) value;
			return;
		case ID_TOSTRINGMEMBERS:
			this.toStringMembers = (Vector) value;
			return;
		case ID_TOCOMBOMEMBERS:
			this.comboStringMembers = (Vector) value;
			return;
		case ID_HASHCODEMEMBERS:
			this.hashCodeMembers = (Vector) value;
			return;
		case ID_AUSWAHLMEMBERS:
			this.selectableColumns = (Vector<SelectionMemberModel>) value;
			return;
		case ID_ORDERMEMBERS:
			this.orderMembers = (Vector) value;
			return;
		case ID_KONTEXTNAME:
			this.contextName = (String) value;
			return;
		case ID_UNIQUEFORMULA:
			this.uniqueformula = (String) value;
			return;
		case ID_NREFERENZEN:
			this.nReferences = (SortedVector) value;
			return;
		case ID_BESCHRIFTUNGSFARBE:
			this.beschriftungsfarbe = (Color) value;
			return;
		case ID_HINTERGRUNDFARBE:
			this.hintergrundfarbe = (Color) value;
			return;
		case ID_DYNAMICCODE:
			this.dynamicCode = ((Boolean) value).booleanValue();
			return;
		case ID_PANELS:
			this.panels = (SortedVector) value;
			return;
		case ID_INHERITED:
			this.inherited = ((Boolean) value).booleanValue();
			return;
		case ID_ACTIVEINAPPLICATION:
			this.activeInApplication = ((Boolean) value).booleanValue();
			return;
		case ID_NMRELATION:
			this.manyToManyRelation = ((Boolean) value).booleanValue();
			return;
		case ID_FIRSTGENERATIONDONE:
			this.firstGenerationDone = ((Boolean) value).booleanValue();
			return;
		case ID_INDEVELOPMENTPROCESS:
			this.draft = ((Boolean) value).booleanValue();
			return;
		case ID_CODEGENERATOROPTIONS:
			this.generatorCodeOptions = (String) value;
			return;
		case ID_HISTORY:
			this.historie = (String) value;
			return;
		case ID_ADDITIONAL_CREATE_CONSTRAINTS:
			this.setAdditionalCreateConstraints((String) value);
			return;
		case ID_EXTERNAL_TABLE:
			this.externalTable = ((Boolean) value).booleanValue();
			return;
		case ID_COMPLEX_FOREIGN_KEY_DEFINITION:
			this.complexForeignKeyDefinition = (String) value;
			return;
		}
		throw new IllegalArgumentException("Klasse Tabelle verfuegt nicht ueber ein Attribut " + id + " (set)!");
	}

	@Override
	public int compareTo(Object o) {
		return this.getName().compareTo(((TableModel) o).getName());
	}

	@Override
	public EditorDescriptorList getEditorDescriptorList() {
		DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
		DefaultComponentFactory dcfcol = new DefaultComponentFactory(Archimedes.PALETTE.getColors());
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_NAME, dlf, dcf, "Name", 'M', null,
				"Der Name der Tabelle"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_DEPRECATED, dlf, dcf, "Aufgehoben", 'U', null,
				"Die Aufgehoben-Markierung der Tabelle"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_BESCHRIFTUNGSFARBE, dlf, dcfcol, "Beschriftungsfarbe",
				'B', null, "Die Beschriftungsfarbe der Tabelle"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_HINTERGRUNDFARBE, dlf, dcfcol, "Hintergrundfarbe", 'H',
				null, "Die Hintergrundfarbe der Tabelle"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_NMRELATION, dlf, dcf, "N:M-Zwischentabelle", 'Z', null,
				StrUtil.FromHTML("Setzen Sie diese Flagge, um"
						+ " die Tabelle als Zwischentabelle f&uuml;r eine N:M-Beziehung zu kennzeichnen")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_INDEVELOPMENTPROCESS, dlf, dcf, "In Entwicklung", 'E',
				null, StrUtil.FromHTML("L&ouml;schen Sie diese Flagge, "
						+ "um die Tabelle in den produktiven Betrieb zu &uuml;bernehmen.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_EXTERNAL_TABLE, dlf, dcf, "Externe Tabelle", 'X', null,
				StrUtil.FromHTML("Setzen Sie diese Flagge, um die" + " Tabelle als externe Tabelle zu markieren.")));
		dedl.addElement(new DefaultSubEditorDescriptor(1, this, new TabellenspaltenSubEditorFactory(
				DefaultVectorPanelButtonFactory.INSTANCE, Archimedes.guiBundle)));
		dedl.addElement(new DefaultSubEditorDescriptor(2, this, new StereotypenSubEditorFactory(
				DefaultVectorPanelButtonFactory.INSTANCE)));
		dedl.addElement(new DefaultSubEditorDescriptor(3, this, new CommentSubEditorFactory()));
		dedl.addElement(new DefaultSubEditorDescriptor(4, this, new HistoryOwnerSubEditorFactory()));
		dedl.addElement(new DefaultEditorDescriptor(5, this, ID_CODIEREN, dlf, dcf, "Kodieren", 'K', null, StrUtil
				.FromHTML("Setzen Sie diese Flagge, wenn f&uuml;r die " + "Tabelle Code generiert werden soll")));
		dedl.addElement(new DefaultEditorDescriptor(5, this, ID_CODEVERZEICHNIS, dlf, dcf, "Code-Verzeichnis", 'V',
				null, StrUtil.FromHTML("Hier kann ein Verzeichnis "
						+ "relativ zum eigentlichen Codepfad definiert werden in das der Code f&uuml;r "
						+ "die Tabelle generiert werden soll")));
		dedl.addElement(new DefaultEditorDescriptor(5, this, ID_KONTEXTNAME, dlf, dcf, "Kontextname", 'K', null,
				StrUtil.FromHTML("Hier kann ein Kontextbezeichner " + "f&uuml;r die Tabelle definiert werden (z. B. "
						+ "BIERBAUCHPFLEGE_GUMMIBAERCHENTABELLE)")));
		dedl.addElement(new DefaultEditorDescriptor(5, this, ID_UNIQUEFORMULA, dlf, dcf, "Unifikatsformel", 'U', null,
				StrUtil.FromHTML("Eine Formel f&uuml;r den Bau " + "von Unifikatspr&uuml;fungen")));
		dedl.addElement(new DefaultEditorDescriptor(5, this, ID_COMPLEX_FOREIGN_KEY_DEFINITION, dlf, dcf, StrUtil
				.FromHTML("Komplexer Fremdsch&uuml;ssel"), 'X', null, StrUtil
				.FromHTML("Eine Formel f&uuml;r komplexen (mehrspaltigen) " + "Fremdschl&uuml;ssel der Tabelle.")));
		dedl.addElement(new DefaultEditorDescriptor(5, this, ID_DYNAMICCODE, dlf, dcf, "Dynamic codieren", 'Y', null,
				"Setzen Sie diese Flagge, wenn der Codegenerator" + " eine dynamische Klasse generieren soll"));
		dedl.addElement(new DefaultEditorDescriptor(5, this, ID_INHERITED, dlf, dcf, "Ererbt", 'E', null,
				"Setzen Sie diese Flagge, um die Tabelle als ererbt zu kennzeichnen"));
		dedl.addElement(new DefaultEditorDescriptor(5, this, ID_ACTIVEINAPPLICATION, dlf, dcf, "Aktiv in Applikation",
				'P', null, "Setzen Sie diese Flagge, um die Tabelle als"
						+ " aktive Klasse in der Applikation zu kennzeichnen"));
		dedl.addElement(new DefaultEditorDescriptor(5, this, ID_FIRSTGENERATIONDONE, dlf, dcf,
				"Erstgenerierung erfolgt", 'G', null, StrUtil.FromHTML("Setzen Sie diese "
						+ "Flagge, um f&uuml;r die Tabelle zu kennzeichnen, da&szlig; die "
						+ "Codeerstgenerierung bereits erfolgt ist")));
		dedl.addElement(new DefaultEditorDescriptor(5, this, ID_CODEGENERATOROPTIONS, dlf, dcf,
				"Codegeneratoroptionen", 'O', null, StrUtil.FromHTML("Hier k&ouml;nnen Sie "
						+ "Optionen zum Codegenerator hinterlegen.")));
		dedl.addElement(new DefaultEditorDescriptor(5, this, ID_ADDITIONAL_CREATE_CONSTRAINTS, dlf, dcf, StrUtil
				.FromHTML("Zust&auml;tzliche Create-Constraints"), 'Z', null, StrUtil
				.FromHTML("Hier k&ouml;nnen Sie zus&auml;tzliche Constraints zum Create-" + "Statement hinterlegen.")));
		dedl.addElement(new DefaultSubEditorDescriptor(6, this, new TabellenspaltenlistenSubEditorFactory(
				DefaultVectorPanelButtonFactory.INSTANCE, ID_EQUALSMEMBERS, false)));
		dedl.addElement(new DefaultSubEditorDescriptor(7, this, new TabellenspaltenlistenSubEditorFactory(
				DefaultVectorPanelButtonFactory.INSTANCE, ID_HASHCODEMEMBERS, false)));
		dedl.addElement(new DefaultSubEditorDescriptor(8, this, new TabellenspaltenlistenSubEditorFactory(
				DefaultVectorPanelButtonFactory.INSTANCE, ID_COMPAREMEMBERS, false)));
		dedl.addElement(new DefaultSubEditorDescriptor(9, this, new TabellenspaltenlistenSubEditorFactory(
				DefaultVectorPanelButtonFactory.INSTANCE, ID_TOSTRINGMEMBERS, true, 1)));
		dedl.addElement(new DefaultSubEditorDescriptor(10, this, new TabellenspaltenlistenSubEditorFactory(
				DefaultVectorPanelButtonFactory.INSTANCE, ID_TOCOMBOMEMBERS, true, 2)));
		dedl.addElement(new DefaultSubEditorDescriptor(11, this, new TabellenspaltenlistenSubEditorFactory(
				DefaultVectorPanelButtonFactory.INSTANCE, ID_AUSWAHLMEMBERS, true, 4, true)));
		dedl.addElement(new DefaultSubEditorDescriptor(12, this, new TabellenspaltenlistenSubEditorFactory(
				DefaultVectorPanelButtonFactory.INSTANCE, ID_ORDERMEMBERS, true, 3, true)));
		dedl.addElement(new DefaultSubEditorDescriptor(13, this, new NReferenzlistenSubEditorFactory(
				DefaultVectorPanelButtonFactory.INSTANCE, ID_NREFERENZEN, true)));
		dedl.addElement(new DefaultSubEditorDescriptor(14, this, new PanellistenSubEditorFactory(
				DefaultVectorPanelButtonFactory.INSTANCE)));
		dedl.addElement(new DefaultSubEditorDescriptor(15, this, new OptionListSubEditorFactory(
				DefaultVectorPanelButtonFactory.INSTANCE, Archimedes.guiBundle, this.diagramm
						.getPredeterminedOptionProvider(), OptionType.TABLE)));
		return dedl;
	}

	@Override
	public Object createObject() {
		return Archimedes.Factory.createTabelle(null, 0, 0, null, false);
	}

	@Override
	public Object createObject(Object blueprint) throws ClassCastException {
		if (!(blueprint instanceof Tabelle)) {
			throw new ClassCastException("Instance of Tabelle required!");
		}
		return this.createObject();
	}

	@Override
	public TabbedPaneFactory getTabbedPaneFactory() {
		return new DefaultTabbedPaneFactory(new TabDescriptor[] { new DefaultTabDescriptor("1.Daten", '1', null),
				new DefaultTabDescriptor("2.Spalten", '2', null), new DefaultTabDescriptor("3.Stereotype", '3', null),
				new DefaultTabDescriptor("4.Beschreibung", '4', null),
				new DefaultTabDescriptor("5.Historie", '5', null),
				new DefaultTabDescriptor("6.Codegenerator", '6', null),
				new DefaultTabDescriptor("7.Equals-Members", '7', null),
				new DefaultTabDescriptor("8.HashCode-Members", '8', null),
				new DefaultTabDescriptor("9.CompareTo-Members", '9', null),
				new DefaultTabDescriptor("A.ToString-Members", 'A', null),
				new DefaultTabDescriptor("B.ToComboString-Members", 'B', null),
				new DefaultTabDescriptor("C.Auswahl-Members", 'C', null),
				new DefaultTabDescriptor("D.Order-Members", 'D', null),
				new DefaultTabDescriptor("E.N-Referenzen", 'E', null), new DefaultTabDescriptor("F.Panels", 'F', null),
				new DefaultTabDescriptor("G.Optionen", 'G', null) });
	}

	@Override
	public boolean isTabEnabled(int no) {
		return true;
	}

	@Override
	public void addStereotype(StereotypeModel st) {
		this.stereotype.addElement(st);
	}

	@Override
	public void removeStereotype(StereotypeModel st) {
		this.stereotype.removeElement(st);
	}

	@Override
	public int getStereotypenCount() {
		return this.stereotype.size();
	}

	@Deprecated
	@Override
	public String getCodeVerzeichnis() {
		if (this.codeFolder == null) {
			return "";
		}
		return this.codeFolder;
	}

	@Deprecated
	@Override
	public void setCodeVerzeichnis(String codeverzeichnis) {
		if (codeverzeichnis == null) {
			codeverzeichnis = "";
		}
		this.codeFolder = codeverzeichnis;
	}

	@Override
	public Vector getAuswahlMembers() {
		return (Vector<SelectionMemberModel>) this.selectableColumns;
	}

	@Override
	public void removeNReferenz(NReferenzModel nrm) {
		this.removeNReference(nrm);
	}

	@Override
	public int getNReferenzModelCount() {
		return this.nReferences.size();
	}

	@Override
	public NReferenzModel getNReferenzModelAt(int i) {
		return (NReferenzModel) this.nReferences.elementAt(i);
	}

	@Override
	public java.util.List<ViewModel> getViews() {
		java.util.List<ViewModel> l = new Vector<ViewModel>();
		for (Enumeration<ViewModel> e = this.x.keys(); e.hasMoreElements();) {
			l.add((ViewModel) e.nextElement());
		}
		return l;
	}

	@Override
	public void removeFromView(ViewModel view) {
		this.x.remove(view);
		this.y.remove(view);
	}

	@Override
	public Color getSchriftfarbe() {
		return this.beschriftungsfarbe;
	}

	@Override
	public void setSchriftfarbe(Color farbe) {
		this.beschriftungsfarbe = farbe;
	}

	@Override
	public Color getHintergrundfarbe() {
		return this.hintergrundfarbe;
	}

	@Override
	public void setHintergrundfarbe(Color farbe) {
		this.hintergrundfarbe = farbe;
	}

	@Override
	public boolean isDynamicCode() {
		return this.dynamicCode;
	}

	@Override
	public void setDynamicCode(boolean dyn) {
		this.dynamicCode = dyn;
	}

	@Override
	public int getPanelCount() {
		/*
		 * Vector v = new Vector(); int panel = 0; for (int i = 0, len =
		 * this.getTabellenspaltenCount(); i < len; i++) { TabellenspaltenModel
		 * tsm = this.getTabellenspalteAt(i); if (!v.contains(new
		 * Integer(tsm.getPanelNumber()))) { v.addElement(new
		 * Integer(tsm.getPanelNumber())); } } return v.size() +
		 * this.getNReferenzModelCount();
		 */
		return this.panels.size();
	}

	@Override
	public void addPanel(PanelModel p) {
		this.panels.add(p);
	}

	@Override
	public void removePanel(PanelModel p) {
		this.panels.removeElement(p);
	}

	@Override
	public PanelModel getPanelAt(int i) {
		Exception e = null;
		try {
			return (PanelModel) this.panels.elementAt(i);
		} catch (Exception ex) {
			e = ex;
		}
		throw new IllegalArgumentException("tried to access panel " + i + " of table " + this.getName()
				+ " via method 'getPanelAt(" + i + "). This throws an error: " + (e != null ? e.getMessage() : "???"));
	}

	@Override
	public boolean isInherited() {
		return this.inherited;
	}

	@Override
	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}

	@Deprecated
	@Override
	public java.util.List<TabellenspaltenModel> getAlleTabellenspalten() {
		return this.getAlleTabellenspalten(true);
	}

	@Override
	public java.util.List<TabellenspaltenModel> getAlleTabellenspalten(boolean doublepk) {
		java.util.List<TabellenspaltenModel> listTabellenspalten = new Vector<TabellenspaltenModel>();
		Vector vtsm = this.getTabellenspalten();
		for (int i = 0, len = vtsm.size(); i < len; i++) {
			TabellenspaltenModel tsm = (TabellenspaltenModel) vtsm.elementAt(i);
			if (tsm.isPrimarykey() && this.isInherited() && (tsm.getRelation() != null)
					&& (tsm.getRelation().getReferenced() != null)
					&& (tsm.getRelation().getReferenced().getTable() != null)) {
				TabellenModel tmr = (TabellenModel) tsm.getRelation().getReferenced().getTable();
				java.util.List<TabellenspaltenModel> listTabellenspaltenInherited = tmr.getAlleTabellenspalten();
				for (int j = 0, lenj = listTabellenspaltenInherited.size(); j < lenj; j++) {
					listTabellenspalten.add(listTabellenspaltenInherited.get(j));
				}
			}
			if (doublepk || !tsm.isPrimarykey()) {
				listTabellenspalten.add(tsm);
			}
		}
		return listTabellenspalten;
	}

	@Override
	public String getPrimaryKeyName() {
		Vector tabellenspalten = this.getTabellenspalten();
		boolean found = false;
		String uniquePrimaryKeyName = null;
		for (Enumeration elements = tabellenspalten.elements(); elements.hasMoreElements();) {
			TabellenspaltenModel spalte = (TabellenspaltenModel) elements.nextElement();
			if (spalte.isPrimarykey()) {
				if (found) {
					return null; // zweiter Prim&auml;rschl&uuml;ssel gefunden
				} else {
					found = true; // erster Prim&auml;rschl&uuml;ssel gefunden
					uniquePrimaryKeyName = spalte.getName();
				}
			}
		}
		if (found) {
			return uniquePrimaryKeyName;
		}
		return null;
	}

	@Override
	public boolean containsColumnname(String columnname) {
		String[] columnnames = this.getColumnnames();
		for (int i = 0; i < columnnames.length; i++) {
			if (columnnames[i].equals(columnname)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String[] getColumnnames() {
		Vector tabellenspalten = this.getTabellenspalten();
		Vector<String> columnnames = new Vector<String>();
		for (Enumeration elements = tabellenspalten.elements(); elements.hasMoreElements();) {
			TabellenspaltenModel spalte = (TabellenspaltenModel) elements.nextElement();
			columnnames.add(spalte.getName());
		}
		return (String[]) columnnames.toArray(new String[0]); // richtig? <
		// Jetzt, Ja!
	}

	@Override
	public boolean isActiveInApplication() {
		return this.activeInApplication;
	}

	@Override
	public void setActiveInApplication(boolean b) {
		this.activeInApplication = b;
	}

	@Override
	public boolean isFirstGenerationDone() {
		return this.firstGenerationDone;
	}

	@Override
	public void setFirstGenerationDone(boolean egd) {
		this.firstGenerationDone = egd;
	}

	@Override
	public boolean isAlterInBatch() {
		for (int i = 0, len = this.getTabellenspaltenCount(); i < len; i++) {
			TabellenspaltenModel tsm = this.getTabellenspalteAt(i);
			if (tsm.isAlterInBatch()) {
				return true;
			}
		}
		return false;
	}

	@Deprecated
	@Override
	public String getCreateStatement(boolean hasDomains, boolean referenzenSetzen, boolean fkNotNullBeachten) {
		return this.makeCreateStatement(hasDomains, referenzenSetzen, fkNotNullBeachten);
	}

	@Override
	public String getFieldnames(boolean pks, boolean nonpks) {
		return this.getFieldnames(pks, nonpks, false);
	}

	@Override
	public String getFieldnames(boolean pks, boolean nonpks, boolean qualifiedNames) {
		StringBuffer sb = new StringBuffer();
		for (int j = 0, lenj = this.getTabellenspaltenCount(); j < lenj; j++) {
			TabellenspaltenModel tsm = this.getTabellenspalteAt(j);
			if ((tsm.isPrimarykey() && pks) || (!tsm.isPrimarykey() && nonpks)) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append((qualifiedNames ? tsm.getFullName() : tsm.getName()));
			}
		}
		return sb.toString();
	}

	/**
	 * @changed OLI 22.05.2009 - Hinzugef&uuml;gt
	 *          <P>
	 *          OLI 28.05.2009 - Debugging: Beseitigung des Fehlers, wenn keine
	 *          Nichtprim&auml;rschl&uuml;sselattribute vorhanden sind.
	 *          <P>
	 * 
	 */
	@Override
	public String getFieldnamesPKFirst(boolean nonpks, boolean qualifiedNames) {
		String result = this.getFieldnames(true, false, qualifiedNames);
		String npks = null;
		if (nonpks) {
			npks = this.getFieldnames(false, true, qualifiedNames);
			if (npks.length() > 0) {
				result = result.concat((result.length() > 0 ? ", " : "")).concat(npks);
			}
		}
		return result;
	}

	@Override
	public Vector<TabellenspaltenModel> getPrimaryKeyTabellenspalten() {
		Vector spalten = this.getTabellenspalten();
		Vector<TabellenspaltenModel> pkSpalten = new Vector<TabellenspaltenModel>();
		for (int i = 0, n = spalten.size(); i < n; i++) {
			TabellenspaltenModel spalte = (TabellenspaltenModel) spalten.elementAt(i);
			if (spalte.isPrimarykey()) {
				pkSpalten.add(spalte);
			}
		}
		return pkSpalten;
	}

	@Override
	public TabellenModel getParentTabellenModel() {
		if (!this.isInherited()) { // Pruefung nur aus Effizienzgruenden
			return null;
		}
		Vector tabellen = this.getDiagramm().getTabellen();
		for (int i = 0, n = tabellen.size(); i < n; i++) {
			TabellenModel tabelle = (TabellenModel) tabellen.elementAt(i);
			if (tabelle.isParent(this)) {
				return tabelle;
			}
		}
		return null;
	}

	@Override
	public boolean isParent(TabellenModel kindTabelle) {
		// Pruefung vermutlich notwendig; jedenfalls nicht falsch.
		if (!kindTabelle.isInherited()) {
			return false;
		}
		return IsFullReferenced(kindTabelle.getPrimaryKeyTabellenspalten(), this.getPrimaryKeyTabellenspalten());
	}

	@Override
	public boolean isStereotype(String n) {
		for (int i = 0, len = this.getStereotypenCount(); i < len; i++) {
			StereotypeModel stm = this.getStereotypeAt(i);
			if (stm.getName().equals(n)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @changed OLI 30.12.2008 - Erweiterung um die Ber&uuml;cksichtigung von
	 *          Defaultwerten. Hat eine Domain den String "NULL" als
	 *          Initialwert, wird dieser in den Datenbanken NULL-Wert umgesetzt
	 *          und bleibt unber&uuml;cksichtigt.
	 *          <P>
	 * 
	 */
	@Override
	public String makeCreateStatement(boolean hasDomains, boolean setReferences, boolean noticeFkNotNull) {
		return this.makeCreateStatement(hasDomains, setReferences, noticeFkNotNull, null);
	}

	private String quoteString(String s, String quote) {
		if ((quote != null) && (quote.length() > 0)) {
			s = quote.concat(s).concat(quote);
		}
		return s;
	}

	/**
	 * @changed OLI 12.05.2009 - Hinzugef&uuml;gt (durch Signatur
	 *          &Auml;nderung).
	 */
	@Override
	public String makeCreateStatement(boolean hasDomains, boolean setReferences, boolean noticeFkNotNull,
			String nameQuotes) {
		boolean notnull = false;
		int pkcount = 0;
		String ls = System.getProperty("line.separator");
		StringBuffer sb = new StringBuffer("create table ").append(this.quoteString(this.getName(), nameQuotes))
				.append(" (").append(ls);
		TabellenspaltenModel tsm = null;
		for (int j = 0, lenj = this.getTabellenspaltenCount(); j < lenj; j++) {
			tsm = this.getTabellenspalteAt(j);
			if (tsm.isPrimarykey()) {
				pkcount++;
			}
		}
		for (int j = 0, lenj = this.getTabellenspaltenCount(); j < lenj; j++) {
			tsm = this.getTabellenspalteAt(j);
			sb.append("    ").append(this.quoteString(tsm.getName(), nameQuotes)).append(" ").append(
					(hasDomains ? tsm.getDomain().getName() : tsm.getDomain().getType()));
			notnull = (tsm.isNotNull() && (tsm.isPrimarykey() || ((tsm.getRelation() != null) && noticeFkNotNull)));
			if (tsm.isPrimarykey()) {
				if (pkcount == 1) {
					sb.append(" not null primary key");
				} else {
					sb.append(" not null");
				}
			} else if (notnull || tsm.isNotNull()) {
				sb.append(" not null");
			}
			if (!tsm.getDomain().getInitialValue().equals("NULL")) {
				sb.append(" default ");
				if (tsm.getDomain().getType().toLowerCase().contains("char")
						|| tsm.getDomain().getType().toLowerCase().contains("text")) {
					sb.append("'").append(tsm.getDomain().getInitialValue()).append("'");
				} else {
					sb.append(tsm.getDomain().getInitialValue());
				}
			}
			if (setReferences && (tsm.getRelation() != null)) {
				sb.append(" references ").append(
						this.quoteString(tsm.getRelation().getReferenced().getTable().getName(), nameQuotes)).append(
						" (").append(this.quoteString(tsm.getRelation().getReferenced().getName(), nameQuotes)).append(
						")");
			}
			if ((j < lenj - 1) || (pkcount > 1)) {
				sb.append(",");
			}
			sb.append(ls);
		}
		if (pkcount > 1) {
			sb.append("    primary key(");
			for (int j = 0, lenj = this.getTabellenspaltenCount(); j < lenj; j++) {
				tsm = this.getTabellenspalteAt(j);
				if (tsm.isPrimarykey()) {
					sb.append(this.quoteString(tsm.getName(), nameQuotes) + (--pkcount > 0 ? ", " : ""));
				}
			}
			sb.append(")").append(ls);
		}
		sb.append(");").append(ls);
		return sb.toString();
	}

	@Override
	public String makeInsertStatement() {
		StringBuffer sb = new StringBuffer("insert into ").append(this.getName()).append(" (");
		StringBuffer sbv = new StringBuffer();
		for (int j = 0, lenj = this.getTabellenspaltenCount(); j < lenj; j++) {
			TabellenspaltenModel tsm = this.getTabellenspalteAt(j);
			if (j != 0) {
				sb.append(", ");
				sbv.append(", ");
			}
			sb.append(tsm.getName());
			sbv.append("$").append(tsm.getName()).append("$");
		}
		sb.append(") values (").append(sbv.toString()).append(")");
		return sb.toString();
	}

	@Override
	public String makeInsertStatement(HashMap<String, Object> values) {
		String ins = this.makeInsertStatement();
		for (Iterator it = values.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			Object value = values.get(key);
			ins = ins.replace("$" + key + "$", value.toString());
		}
		return ins;
	}

	/**
	 * @changed OLI 17.05.2009 - Hinzugef&uuml;gt
	 *          <P>
	 * 
	 */
	@Override
	public String makeInsertStatementCounted() {
		int i = 0;
		int leni = 0;
		StringBuffer sb = new StringBuffer("insert into ").append(this.getName()).append(" (").append(
				this.getFieldnames(true, true)).append(") values (");
		for (i = 0, leni = this.getTabellenspaltenCount(); i < leni; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append("$").append(i + 1).append("$");
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public java.util.List<String> getCodeGeneratorOptionsListTag(String tagName, String delimiter) {
		int end = -1;
		int start = -1;
		String s = null;
		String tagEnd = "</".concat(tagName).concat(">");
		String tagStart = "<".concat(tagName).concat(">");
		java.util.List<String> l = new Vector<String>();
		s = this.getGenerateCodeOptions();
		start = s.indexOf(tagStart);
		end = s.indexOf(tagEnd);
		if ((start >= 0) && (end >= 0)) {
			s = s.substring(start + tagStart.length(), end);
			l = StrUtil.SplitToList(s, delimiter);
		}
		return l;
	}

	/* Implementierung des Interfaces Selectable. */

	@Override
	public boolean isSelected(Object[] criteria) throws IllegalArgumentException {
		String s = this.getName().concat("|").toLowerCase();
		if (criteria != null) {
			for (int i = 0; i < criteria.length; i++) {
				if (s.indexOf(criteria[i].toString().toLowerCase()) < 0) {
					return false;
				}
			}
		}
		return true;
	}

	/* Implementierung des Interfaces Transferable. */

	@Override
	public Object getTransferData(DataFlavor flavor) {
		if (flavor.equals(DataFlavor.stringFlavor)) {
			return new DefaultCopyAndPasteController().tableToTransferableString(this);
		}
		return null;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DataFlavor.stringFlavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(DataFlavor.stringFlavor);
	}

	/* Statische Methoden. */

	/**
	 * Pr&uuml;ft, ob jedes TabellenspaltenModel in referenced von einem
	 * TabellenspaltenModel in <TT>referencing</TT> referenziert wird und ob
	 * beide Vektoren gleich viele Elemente haben. Falls dies so ist, folgt
	 * daraus, dass jedes TabellenspaltenModel in <TT>referencing</TT> ein
	 * TabellenspaltenModel in <TT>referenced</TT> referenziert.
	 * 
	 * @param referencing
	 *            Die (potenziellen) Fremdschl&uuml;sselspalten
	 * @param referenced
	 *            Die (m&ouml;glicherweise) referenzierten Spalten.
	 * @return <TT>true</TT>, falls jedes TabellenspaltenModel in
	 *         <TT>referenced</TT> von einem TabellenspaltenModel in
	 *         <TT>referencing</TT> referenziert wird und falls beide Vektoren
	 *         au&szlig;erdem gleich viele Elemente haben. <TT>false</TT> sonst.
	 * @exception IllegalArgumentException
	 *                Wird geworfen, falls ein Parameter <TT>null</TT> ist.
	 */
	public static boolean IsFullReferenced(Vector<TabellenspaltenModel> referencing,
			Vector<TabellenspaltenModel> referenced) {
		if ((referencing == null) || (referenced == null)) {
			throw new IllegalArgumentException("Kein Parameter darf null sein.");
		} else if (referencing.size() != referenced.size()) {
			return false;
		}
		for (int i = 0, n = referenced.size(); i < n; i++) {
			TabellenspaltenModel spalte = referenced.elementAt(i);
			boolean spalteReferenziert = false;
			for (int j = 0, m = referencing.size(); j < m; j++) {
				TabellenspaltenModel fkSpalte = referencing.elementAt(j);
				RelationModel rm = fkSpalte.getRelation();
				if ((rm != null) && (rm.getReferenced() == spalte)) {
					spalteReferenziert = true;
					break;
				}
			}
			if (!spalteReferenziert) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Liefert eine Map mit den Tabellenspalten, die Felder der vorliegenden
	 * Tabelle referenzieren. Die Tabellenspalten bilden hierbei den
	 * Schl&uuml;sselwert f&uuml;r die Map. Der Wertteil des Tupels ist die
	 * Tabellenspalten der vorliegenden Tabelle, auf die referenziert wird.
	 * 
	 * @return Map&gt;Tabellenspalte, Tabellenspalte&lt; mit den
	 *         Tabellenspalten, die auf eine Spalte der Tabelle verweisen und
	 *         den Tabellenspalten, auf die verwiesen wird.
	 * 
	 * @changed OLI 05.09.2008 - Hinzugef&uuml;gt.
	 *          <P>
	 * 
	 */
	@Override
	public Map<TabellenspaltenModel, TabellenspaltenModel> getReferencingFields() {
		Hashtable<TabellenspaltenModel, TabellenspaltenModel> ht = new Hashtable<TabellenspaltenModel, TabellenspaltenModel>();
		int i = 0;
		int len = 0;
		RelationModel rm = null;
		TabellenspaltenModel tsm = null;
		TabellenspaltenModel tsm0 = null;
		Vector vtsm = this.getDiagramm().getAllFields();
		len = vtsm.size();
		for (i = 0; i < len; i++) {
			tsm = (TabellenspaltenModel) vtsm.elementAt(i);
			rm = tsm.getRelation();
			if (rm != null) {
				tsm0 = (TabellenspaltenModel) rm.getReferenced();
				if (this.getName().equals(tsm0.getTabelle().getName())) {
					ht.put(tsm, tsm0);
				}
			}
		}
		return ht;
	}

	/* Implementierungen zum Interfaca ClassMetaData. */

	@Override
	public AttributeMetaData getAttribute(int i) throws IndexOutOfBoundsException {
		return this.getTabellenspalteAt(i);
	}

	@Override
	public java.util.List<AttributeMetaData> getAttributes() {
		return this.getTabellenspalten();
	}

	@Override
	public String getAuthor() {
		return this.getDiagramm().getAuthor();
	}

	@Override
	public ModelMetaData getModel() {
		return this.getDiagramm();
	}

	@Override
	public String getPackageName() {
		String s = this.getCodeVerzeichnis().replace("\\", "/").replace("/", ".");
		while (s.startsWith(".")) {
			s = s.substring(1, s.length());
		}
		while (s.endsWith(".")) {
			s = s.substring(0, s.length() - 2);
		}
		return s;
	}

	/**
	 * @changed OLI 04.06.2010 - Hinzugef&uuml;gt.
	 */
	@Override
	public List<SelectionViewMetaData> getSelectionViewMetaData() {
		Vector<SelectionViewMetaData> vsvmd = new Vector<SelectionViewMetaData>();
		vsvmd.add(this);
		return vsvmd;
	}

	/**
	 * @changed OLI 06.10.2009 - Hinzugef&uuml;gt.
	 */
	@Deprecated
	@Override
	public boolean isOfStereotype(String sn) throws NullPointerException {
		assert sn != null : "null is not a valid name for stereotypes.";
		return this.isStereotype(sn);
	}

	@Override
	public boolean isReadyToCode() {
		return this.isGenerateCode();
	}

	/**
	 * @changed OLI 04.06.2010 - Hinzugef&uuml;gt.
	 */
	@Override
	public List<AttributeMetaData> getCheckedFields() {
		int i = 0;
		int leni = 0;
		List<AttributeMetaData> lamd = new Vector<AttributeMetaData>();
		List<TabellenspaltenModel> lsvm = this.getAuswahlMembers();
		for (i = 0, leni = lsvm.size(); i < leni; i++) {
			lamd.add(lsvm.get(i));
		}
		return lamd;
	}

	/**
	 * @changed OLI 04.06.2010 - Hinzugef&uuml;gt.
	 */
	@Override
	public List<OrderMetaData> getOrder() {
		int i = 0;
		int leni = 0;
		List<OrderMetaData> lomd = new Vector<OrderMetaData>();
		OrderDirection od = null;
		for (OrderMemberModel om : this.getSelectionViewOrderMembers()) {
			if (om.getOrderDirection() == OrderClauseDirection.ASC) {
				od = OrderDirection.ASC;
			} else {
				od = OrderDirection.DESC;
			}
			lomd.add(new ArchimedesOrderMetaData((TabellenspaltenModel) om.getOrderColumn(), od));
		}
		return lomd;
	}

	/**
	 * @changed OLI 04.06.2010 - Hinzugef&uuml;gt.
	 */
	@Override
	public List<AttributeMetaData> getSelectionViewMember() {
		return this.getCheckedFields();
	}

	@Override
	public String getHistory() {
		return this.historie;
	}

	@Override
	public void setHistory(String newHistory) {
		this.historie = newHistory;
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public AttributeMetaData getAttribute(String name) throws IllegalArgumentException {
		if ((name == null) || name.isEmpty()) {
			throw new IllegalArgumentException("name cannot be null or empty.");
		}
		for (AttributeMetaData amd : this.spalten) {
			if (amd.getName().equals(name)) {
				return amd;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public Vector<NReferenzModel> getNReferencesAsRef() {
		return this.nReferences;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public List<?> getEqualsMembersAsRef() {
		return this.equalsMembers;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public List<?> getCompareMembersAsRef() {
		return this.compareToMembers;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public List<?> getToStringMembersAsRef() {
		return this.toStringMembers;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public List<?> getComboStringMembersAsRef() {
		return this.comboStringMembers;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public List<?> getHashCodeMembersAsRef() {
		return this.hashCodeMembers;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public List<?> getSelectionViewOrderMembersAsRef() {
		return this.orderMembers;
	}

	/*
	 * TODO: Take the missing methods and attributes over to this
	 * implementation.
	 * 
	 * The following methods are approved by the current model interface
	 * (TableModel).
	 */

	private String additionalCreateConstraints = "";
	private String codeFolder = "";
	private String comment = "";
	private String complexForeignKeyDefinition = "";
	private boolean draft = true;
	private boolean externalTable = false;
	private int height = 0;
	private SortedVector<NReferenzModel> nReferences = new SortedVector<NReferenzModel>();
	private int width = 0;
	private SortedVector<OptionModel> options = new SortedVector<OptionModel>();
	private SortedVector<StereotypeModel> stereotype = new SortedVector<StereotypeModel>();

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Override
	public void addColumn(ColumnModel column) throws IllegalArgumentException {
		this.spalten.add((TabellenspaltenModel) column);
		column.setTable(this);
		this.diagramm.addToFieldCache((TabellenspaltenModel) column);
	}

	/**
	 * @changed OLI 01.03.2016 - Added.
	 */
	@Override
	public void addComboStringMember(ToStringContainerModel tsc) {
		ensure(tsc != null, "to string container cannot be null.");
		if (!this.comboStringMembers.contains(tsc)) {
			this.comboStringMembers.add((ToStringContainer) tsc);
		}
	}

	/**
	 * @changed OLI 26.02.2016 - Added.
	 */
	@Override
	public void addCompareMember(ColumnModel column) throws IllegalArgumentException {
		ensure(column != null, "column cannot be null.");
		ensure(this.getName().equals(column.getTable().getName()), "column have to be in the " + "same table.");
		if (!this.compareToMembers.contains(column)) {
			this.compareToMembers.add(column);
		}
	}

	/**
	 * @changed OLI 29.02.2016 - Added.
	 */
	@Override
	public void addEqualsMember(ColumnModel column) throws IllegalArgumentException {
		ensure(column != null, "column cannot be null.");
		ensure(this.getName().equals(column.getTable().getName()), "column have to be in the " + "same table.");
		if (!this.equalsMembers.contains(column)) {
			this.equalsMembers.add(column);
		}
	}

	/**
	 * @changed OLI 29.02.2016 - Added.
	 */
	@Override
	public void addHashCodeMember(ColumnModel column) throws IllegalArgumentException {
		ensure(column != null, "column cannot be null.");
		ensure(this.getName().equals(column.getTable().getName()), "column have to be in the " + "same table.");
		if (!this.hashCodeMembers.contains(column)) {
			this.hashCodeMembers.add(column);
		}
	}

	/**
	 * @changed OLI 06.05.2013 - Added.
	 */
	@Override
	public void addNReference(NReferenceModel nrm) {
		this.nReferences.addElement((NReferenzModel) nrm);
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public void addOption(OptionModel option) {
		ensure(option != null, "option to add cannot be null");
		if (!this.options.contains(option)) {
			this.options.addElement(option);
		}
	}

	/**
	 * @changed OLI 26.02.2016 - Added.
	 */
	@Override
	public void addSelectableColumn(SelectionMemberModel smm) {
		if (!this.selectableColumns.contains(smm)) {
			this.selectableColumns.add(smm);
		}
	}

	/**
	 * @changed OLI 01.03.2016 - Added.
	 */
	@Override
	public void addSelectionViewOrderMember(OrderMemberModel om) {
		if (!this.orderMembers.contains(om)) {
			this.orderMembers.add(om);
		}
	}

	/**
	 * @changed OLI 01.03.2016 - Added.
	 */
	@Override
	public void addToStringMember(ToStringContainerModel tsc) {
		ensure(tsc != null, "to string container cannot be null.");
		if (!this.toStringMembers.contains(tsc)) {
			this.toStringMembers.add((ToStringContainer) tsc);
		}
	}

	/**
	 * @changed OLI 08.11.2013 - Added.
	 */
	@Override
	public String getAdditionalCreateConstraints() {
		return this.additionalCreateConstraints;
	}

	/**
	 * @changed OLI 01.03.2016 - Added.
	 */
	@Override
	public void clearComboStringMembers() {
		this.comboStringMembers.clear();
	}

	/**
	 * @changed OLI 26.02.2016 - Added.
	 */
	@Override
	public void clearCompareToMembers() {
		this.compareToMembers.clear();
	}

	/**
	 * @changed OLI 29.02.2016 - Added.
	 */
	@Override
	public void clearEqualsMembers() {
		this.equalsMembers.clear();
	}

	/**
	 * @changed OLI 29.02.2016 - Added.
	 */
	@Override
	public void clearHashCodeMembers() {
		this.hashCodeMembers.clear();
	}

	/**
	 * @changed OLI 01.03.2016 - Added.
	 */
	@Override
	public void clearNReferences() {
		this.nReferences.clear();
	}

	/**
	 * @changed OLI 01.03.2016 - Added.
	 */
	@Override
	public void clearPanels() {
		this.panels.clear();
	}

	/**
	 * @changed OLI 01.03.2016 - Added.
	 */
	@Override
	public void clearSelectionViewOrderMembers() {
		this.orderMembers.clear();
	}

	/**
	 * @changed OLI 01.03.2016 - Added.
	 */
	@Override
	public void clearToStringMembers() {
		this.toStringMembers.clear();
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public String getCodeFolder() {
		return this.codeFolder;
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Override
	public ColumnModel getColumnByName(String name) {
		for (ColumnModel c : getColumns()) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Override
	public ColumnModel[] getColumns() {
		List<ColumnModel> columns = new corentx.util.SortedVector<ColumnModel>();
		for (TabellenspaltenModel tsm : this.spalten) {
			columns.add(tsm);
		}
		return columns.toArray(new ColumnModel[0]);
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public ColumnModel[] getColumnsWithOption(String optionName) {
		List<ColumnModel> l = new LinkedList<ColumnModel>();
		for (ColumnModel c : this.getColumns()) {
			for (OptionModel o : c.getOptions()) {
				if (o.getName().equals(optionName)) {
					l.add(c);
				}
			}
		}
		return l.toArray(new ColumnModel[0]);
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public ToStringContainer[] getComboStringMembers() {
		return this.comboStringMembers.toArray(new ToStringContainer[0]);
	}

	/**
	 * @changed OLI 24.10.2013 - Added.
	 */
	@Override
	public String getComment() {
		return this.comment;
	}

	@Override
	public ColumnModel[] getCompareMembers() {
		return this.compareToMembers.toArray(new ColumnModel[0]);
	}

	/**
	 * @changed OLI 22.09.2017 - Added.
	 */
	@Override
	public String getComplexForeignKeyDefinition() {
		if (this.complexForeignKeyDefinition == null) {
			return "";
		}
		return this.complexForeignKeyDefinition;
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Override
	public String getComplexUniqueSpecification() {
		return this.uniqueformula;
	}

	/**
	 * @changed OLI 20.08.2013 - Added.
	 */
	@Override
	public String getContextName() {
		return this.contextName;
	}

	/**
	 * @changed OLI 03.05.2013 - Added.
	 */
	@Override
	public DataModel getDataModel() {
		return this.diagramm;
	}

	@Override
	public ColumnModel[] getEqualsMembers() {
		return this.equalsMembers.toArray(new ColumnModel[0]);
	}

	/**
	 * @changed OLI 03.05.2013 - Added.
	 */
	@Override
	public int getHeight() {
		return this.height;
	}

	/**
	 * @changed OLI 23.08.2013 - Added.
	 */
	@Override
	public String getGenerateCodeOptions() {
		return this.generatorCodeOptions;
	}

	@Override
	public ColumnModel[] getHashCodeMembers() {
		return this.hashCodeMembers.toArray(new ColumnModel[0]);
	}

	/**
	 * @changed OLI 25.04.2013 - Approved.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @changed OLI 30.09.2013 - Added.
	 */
	@Override
	public NReferenceModel getNReferenceForPanel(PanelModel pm) {
		for (NReferenceModel nrm : this.getNReferences()) {
			if (nrm.getPanel() == pm) {
				return nrm;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 06.05.2013 - Added.
	 */
	@Override
	public NReferenzModel[] getNReferences() {
		return this.nReferences.toArray(new NReferenzModel[0]);
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public OptionModel getOptionAt(int i) {
		return this.options.get(i);
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public OptionModel getOptionByName(String name) {
		for (OptionModel o : this.options) {
			if (o.getName().equals(name)) {
				return o;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public int getOptionCount() {
		return this.options.size();
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public OptionModel[] getOptions() {
		return this.options.toArray(new OptionModel[0]);
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public OptionModel[] getOptionsByName(String name) {
		List<OptionModel> l = new LinkedList<OptionModel>();
		for (OptionModel o : this.options) {
			if (o.getName().equals(name)) {
				l.add(o);
			}
		}
		return l.toArray(new OptionModel[0]);
	}

	/**
	 * @changed OLI 28.10.2013 - Added.
	 */
	@Override
	public ColumnModel[] getPrimaryKeyColumns() {
		List<ColumnModel> l = new LinkedList<ColumnModel>();
		for (ColumnModel column : this.getColumns()) {
			if (column.isPrimaryKey()) {
				l.add(column);
			}
		}
		return l.toArray(new ColumnModel[0]);
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public GUIRelationModel[] getRelations() {
		List<GUIRelationModel> l = new Vector<GUIRelationModel>();
		for (ColumnModel c : this.getColumns()) {
			if (c.getRelation() != null) {
				l.add((GUIRelationModel) c.getRelation());
			}
		}
		return l.toArray(new GUIRelationModel[0]);
	}

	/**
	 * @changed OLI 09.08.2013 - Added.
	 */
	@Override
	public SelectionMemberModel[] getSelectableColumns() {
		return this.selectableColumns.toArray(new SelectionMemberModel[0]);
	}

	/**
	 * @changed OLI 12.01.2016 - Added.
	 */
	@Override
	public OrderMemberModel[] getSelectionViewOrderMembers() {
		return this.orderMembers.toArray(new OrderMemberModel[0]);
	}

	@Override
	public ToStringContainer[] getToStringMembers() {
		return this.toStringMembers.toArray(new ToStringContainer[0]);
	}

	/**
	 * @changed OLI 03.05.2013 - Added.
	 */
	@Override
	public int getWidth() {
		return this.width;
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public int getX(GUIViewModel view) {
		return this.getX((ViewModel) view);
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public int getY(GUIViewModel view) {
		return this.getY((ViewModel) view);
	}

	@Override
	public boolean isCompareToMember(ColumnModel c) {
		return this.compareToMembers.contains(c);
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Override
	public boolean isDeprecated() {
		return this.deprecated;
	}

	/**
	 * @changed OLI 24.10.2013 - Added.
	 */
	@Override
	public boolean isDraft() {
		return this.draft;
	}

	/**
	 * @changed OLI 29.02.2016 - Added.
	 */
	@Override
	public boolean isEqualsMember(ColumnModel c) {
		return this.equalsMembers.contains(c);
	}

	/**
	 * @changed OLI 07.01.2015 - Added.
	 */
	@Override
	public boolean isExternalTable() {
		return this.externalTable;
	}

	/**
	 * @changed OLI 23.08.2013 - Added.
	 */
	@Override
	public boolean isGenerateCode() {
		return this.generateCode;
	}

	/**
	 * @changed OLI 29.02.2016 - Added.
	 */
	@Override
	public boolean isHashCodeMember(ColumnModel c) {
		return this.hashCodeMembers.contains(c);
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public boolean isInView(GUIViewModel view) {
		return this.getViews().contains(view);
	}

	/**
	 * @changed OLI 21.11.2014 - Added.
	 */
	@Override
	public boolean isManyToManyRelation() {
		return this.manyToManyRelation;
	}

	/**
	 * @changed OLI 21.11.2014 - Added.
	 */
	@Override
	public boolean isNMRelation() {
		return this.isManyToManyRelation();
	}

	/**
	 * @changed OLI 26.05.2016 - Added.
	 */
	@Override
	public boolean isOptionSet(String optionName) {
		return this.getOptionByName(optionName) != null;
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public ShapeContainer paintObject(CoordinateConverter converter, GUIViewModel view, Graphics graphics,
			PaintMode paintMode) {
		return this.paintTabelle(converter, (ViewModel) view, graphics, paintMode);
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public ShapeContainer[] paintRelations(CoordinateConverter converter, GUIViewModel view, Graphics graphics,
			PaintMode paintMode) {
		Vector<ShapeContainer> v = this.paintRelationen(converter, (ViewModel) view, graphics, paintMode);
		return v.toArray(new ShapeContainer[0]);
	}

	/**
	 * @changed OLI 29.04.2013 - Added.
	 */
	@Override
	public void removeColumn(ColumnModel column) throws IllegalArgumentException {
		ensure(column != null, "column to remove cannot be null.");
		if (this.spalten.contains(column)) {
			this.spalten.remove(column);
			column.setTable(null);
			this.diagramm.removeFromFieldCache((TabellenspaltenModel) column);
		}
	}

	/**
	 * @changed OLI 01.03.2016 - Added.
	 */
	@Override
	public void removeComboStringMember(ToStringContainerModel tsc) {
		if (this.comboStringMembers.contains(tsc)) {
			this.comboStringMembers.remove(tsc);
		}
	}

	/**
	 * @changed OLI 26.02.2016 - Added.
	 */
	@Override
	public void removeCompareToMember(ColumnModel column) throws IllegalArgumentException {
		ensure(column != null, "column to remove cannot be null.");
		if (this.compareToMembers.contains(column)) {
			this.compareToMembers.remove(column);
		}
	}

	/**
	 * @changed OLI 29.02.2016 - Added.
	 */
	@Override
	public void removeEqualsMember(ColumnModel column) throws IllegalArgumentException {
		ensure(column != null, "column to remove cannot be null.");
		if (this.equalsMembers.contains(column)) {
			this.equalsMembers.remove(column);
		}
	}

	/**
	 * @changed OLI 29.02.2016 - Added.
	 */
	@Override
	public void removeHashCodeMember(ColumnModel column) throws IllegalArgumentException {
		ensure(column != null, "column to remove cannot be null.");
		if (this.hashCodeMembers.contains(column)) {
			this.hashCodeMembers.remove(column);
		}
	}

	/**
	 * @changed OLI 21.11.2014 - Added.
	 */
	@Override
	public void removeNReference(NReferenceModel nrm) {
		if (this.nReferences.contains(nrm)) {
			this.nReferences.remove(nrm);
		}
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public void removeOption(OptionModel option) {
		if (this.options.contains(option)) {
			this.options.remove(option);
		}
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void removeSelectableColumn(SelectionMemberModel selectionMember) {
		if (this.selectableColumns.contains(selectionMember)) {
			this.selectableColumns.remove(selectionMember);
		}
	}

	/**
	 * @changed OLI 01.03.2016 - Added.
	 */
	@Override
	public void removeSelectionViewOrderMember(OrderMemberModel selectionViewOrderMember) {
		if (this.orderMembers.contains(selectionViewOrderMember)) {
			this.orderMembers.remove(selectionViewOrderMember);
		}
	}

	/**
	 * @changed OLI 01.03.2016 - Added.
	 */
	@Override
	public void removeToStringMember(ToStringContainerModel tsc) {
		if (this.toStringMembers.contains(tsc)) {
			this.toStringMembers.remove(tsc);
		}
	}

	/**
	 * @changed OLI 08.11.2013 - Added.
	 */
	@Override
	public void setAdditionalCreateConstraints(String constraints) {
		this.additionalCreateConstraints = (constraints == null ? "" : constraints);

	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public void setCodeFolder(String codeFolder) {
		if (codeFolder == null) {
			codeFolder = "";
		}
		this.codeFolder = codeFolder;
	}

	/**
	 * @changed OLI 11.03.2016 - Added.
	 */
	@Override
	public void setComment(String comment) {
		if (comment == null) {
			comment = "";
		}
		this.comment = comment;
	}

	/**
	 * @changed OLI 22.09.2017 - Added.
	 */
	@Override
	public void setComplexForeignKeyDefinition(String cfk) {
		this.complexForeignKeyDefinition = cfk;
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Override
	public void setComplexUniqueSpecification(String specification) throws IllegalArgumentException {
		if (specification == null) {
			specification = "";
		}
		this.uniqueformula = specification;
	}

	/**
	 * @changed OLI 23.08.2013 - Added.
	 */
	@Override
	public void setContextName(String contextName) {
		if (contextName == null) {
			contextName = "NO_KONTEXT";
		}
		this.contextName = contextName;
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public void setDataModel(DataModel dataModel) {
		this.diagramm = (DiagrammModel) dataModel;
	}

	/**
	 * @changed OLI 23.08.2013 - Added.
	 */
	@Override
	public void setDeprecated(boolean b) {
		this.deprecated = b;
	}

	/**
	 * @changed OLI 02.03.2016 - Added.
	 */
	@Override
	public void setDraft(boolean b) {
		this.draft = b;
	}

	/**
	 * @changed OLI 07.01.2015 - Added.
	 */
	@Override
	public void setExternalTable(boolean externalTable) {
		this.externalTable = externalTable;
	}

	/**
	 * @changed OLI 23.08.2013 - Added.
	 */
	@Override
	public void setGenerateCode(boolean b) {
		this.generateCode = b;
	}

	/**
	 * @changed OLI 23.08.2013 - Added.
	 */
	@Override
	public void setGenerateCodeOptions(String options) {
		this.generatorCodeOptions = options;
	}

	/**
	 * @changed OLI 13.10.2017 - Added.
	 */
	@Override
	public void setManyToManyRelation(boolean b) {
		this.manyToManyRelation = b;
	}

	/**
	 * @changed OLI 21.11.2014 - Added.
	 */
	@Override
	public void setNMRelation(boolean b) {
		this.setManyToManyRelation(b);
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public void setXY(GUIViewModel view, int x, int y) {
		this.setXY((ViewModel) view, x, y);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public PanelModel getPanelByName(String name) {
		for (PanelModel pm : this.getPanels()) {
			if (pm.getTabTitle().equals(name)) {
				return pm;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public int getPanelPosition(PanelModel pm) {
		return this.panels.indexOf(pm);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public PanelModel[] getPanels() {
		return this.panels.toArray(new PanelModel[0]);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public PanelModel[] getPanelsByName(String name) {
		List<PanelModel> l = new LinkedList<PanelModel>();
		for (PanelModel pm : this.getPanels()) {
			if (pm.getTabTitle().equals(name)) {
				l.add(pm);
			}
		}
		return l.toArray(new PanelModel[0]);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public StereotypeModel getStereotypeAt(int index) {
		return this.stereotype.get(index);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public StereotypeModel getStereotypeByName(String name) {
		for (StereotypeModel stm : this.getStereotypes()) {
			if (stm.equals(name)) {
				return stm;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public int getStereotypeCount() {
		return this.stereotype.size();
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public StereotypeModel[] getStereotypes() {
		return this.stereotype.toArray(new StereotypeModel[0]);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public StereotypeModel[] getStereotypesByName(String name) {
		List<StereotypeModel> l = new LinkedList<StereotypeModel>();
		for (StereotypeModel stm : this.getStereotypes()) {
			if (stm.equals(name)) {
				l.add(stm);
			}
		}
		return l.toArray(new StereotypeModel[0]);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public boolean isStereotypeSet(String name) {
		return this.getStereotypeByName(name) != null;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public Color getBackgroundColor() {
		return this.getHintergrundfarbe();
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public Color getFontColor() {
		return this.getSchriftfarbe();
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public void setBackgroundColor(Color paramColor) {
		this.setHintergrundfarbe(paramColor);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public void setFontColor(Color paramColor) {
		this.setSchriftfarbe(paramColor);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public boolean isPanelSet(String panelName) {
		for (PanelModel p : this.getPanels()) {
			if ((p.getTabTitle() != null) && p.getTabTitle().equals(panelName)) {
				return true;
			}
		}
		return false;
	}

	public List<PanelModel> getPanelsByReference() {
		return this.panels;
	}

	/**
	 * @changed OLI 12.08.2016 - Added.
	 */
	@Override
	public PanelModel getPanelByNumber(int no) {
		for (PanelModel p : this.getPanels()) {
			if (p.getPanelNumber() == no) {
				return p;
			}
		}
		return null;
	}

}