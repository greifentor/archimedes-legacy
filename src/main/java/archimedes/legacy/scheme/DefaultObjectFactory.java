/* 
 * DefaultObjectFactory.java
 *
 * 15.04.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import java.awt.Color;
import java.io.StringBufferInputStream;
import java.sql.Types;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.log4j.Logger;

import archimedes.legacy.Archimedes;
import archimedes.legacy.app.ApplicationUtil;
import archimedes.legacy.app.ArchimedesDescriptorFactory;
import archimedes.legacy.gui.ToStringContainer;
import archimedes.legacy.model.DefaultCommentModel;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.NReferenzModel;
import archimedes.legacy.model.ObjectFactory;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.model.CodeFactory;
import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;
import archimedes.model.OptionModel;
import archimedes.model.OrderMemberModel;
import archimedes.model.PanelModel;
import archimedes.model.ReferenceWeight;
import archimedes.model.RelationModel;
import archimedes.model.SelectionAttribute;
import archimedes.model.SelectionMemberModel;
import archimedes.model.SequenceModel;
import archimedes.model.StereotypeModel;
import archimedes.model.ViewModel;
import archimedes.scheme.Option;
import archimedes.scheme.SelectionMember;
import corent.base.Direction;
import corent.base.StrUtil;
import corent.db.DBType;

/**
 * Diese Implementierung der ObjectFactory liefert Objekte der mitgelieferten Klassen des Archimedes-Systems.
 * 
 * @author ollie
 *         <P>
 * 
 * @changed OLI 06.10.2007 - Anpassung der Methoden zur Generierung von DefaultComments an die
 *          Archimedes-Applikationslogik. Erweiterung der ObjectFactory um das Attribut <TT>adf</TT>. Erweiterung der
 *          Implementierung des Interfaces ObjectFactory um die Methoden <TT>setADF(ArchimedesDescriptorFactory)</TT>
 *          und <TT>getADF()</TT>.
 *          <P>
 *          OLI 06.11.2007 - Erweiterung der Methode <TT>createTabelle(ViewModel, int, int, 
 *             DiagrammModel, boolean)</TT> um die F&auml;higkeit in Templatedateien Referenzen zu verarzten.
 *          <P>
 *          OLI 27.07.2008 - Verenglischung der eingebauten Standard-Tabelle.
 *          <P>
 *          OLI 11.08.2008 - &Uuml;bernahme der technischen Felder in den Copy-&amp;-Paste-Mechanismus.
 * 
 */

public class DefaultObjectFactory implements ObjectFactory {

	/** Instanz zur Referenzierung in der Anwendung. */
	public static final DefaultObjectFactory INSTANCE = new DefaultObjectFactory();

	private static final Logger LOG = Logger.getLogger(DefaultObjectFactory.class);

	/*
	 * Referenz auf die ArchimedesDescriptorFactory mit der die ObjectFactory arbeiten soll.
	 */
	private ArchimedesDescriptorFactory adf = null;
	/* Der Name der Template-Datei. */
	private String templatefilename = null;

	public DefaultObjectFactory() {
		super();
	}

	/* Ueberschreiben von Methoden der Superklasse. */

	@Override
	public String toString() {
		return "Archimedes Default-Object-Factory";
	}

	/* Implementierung des Interfaces ObjectFactory. */

	public DiagrammModel createDiagramm() {
		return new Diagramm();
	}

	public DomainModel createDomain() {
		return new Domain();
	}

	public DomainModel createDomain(String name, int dt, int len, int nks) {
		return new Domain(name, dt, len, nks);
	}

	@Deprecated
	public RelationModel createRelation(ViewModel view, TabellenspaltenModel t1, Direction direction1, int offset1,
			TabellenspaltenModel t2, Direction direction2, int offset2) {
		return new Relation(view, t1, direction1, offset1, t2, direction2, offset2);
	}

	/**
	 * @changed OLI 29.04.2013 - Added.
	 */
	@Override
	public RelationModel createRelation(ViewModel view, ColumnModel t1, Direction direction1, int offset1,
			ColumnModel t2, Direction direction2, int offset2) {
		return new Relation(view, t1, direction1, offset1, t2, direction2, offset2);
	}

	public StereotypeModel createStereotype() {
		return new Stereotype();
	}

	public StereotypeModel createStereotype(String name, String kommentar) {
		return new Stereotype(name, kommentar);
	}

	/**
	 * @changed OLI 18.12.2007 - Hinzugef&uuml;gt.
	 *          <P>
	 * 
	 */
	public TabellenModel createTabelle(ViewModel view, int x, int y, DiagrammModel d, String s) {
		TabellenModel tm = new Tabelle(view, x, y, d);
		try {
			Properties p = new Properties();
			p.load(new StringBufferInputStream(s));
			this.loadTable(p, tm, d, view);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 3) Fertig und freuen :o)
		return tm;
	}

	private int getInteger(Properties p, String s, int dv) {
		try {
			return Integer.parseInt(p.getProperty(s, "" + dv));
		} catch (NumberFormatException nfe) {
		}
		return dv;
	}

	private void loadTable(Properties p, TabellenModel tm, DiagrammModel d, ViewModel view) {
		tm.setGenerateCode(Boolean.parseBoolean(p.getProperty("template.code")));
		tm.setActiveInApplication(Boolean.parseBoolean(p.getProperty("template.active.in.application")));
		tm.setCodeVerzeichnis(p.getProperty("template.codedirectory", ""));
		tm.setSchriftfarbe(Archimedes.PALETTE.get(p.getProperty("template.color.foreground"), Color.black));
		tm.setHintergrundfarbe(Archimedes.PALETTE.get(p.getProperty("template.color.background"), Color.white));
		tm.setContextName(p.getProperty("template.contextname", ""));
		tm.setDeprecated(Boolean.parseBoolean(p.getProperty("template.deprecated")));
		tm.setComment(p.getProperty("template.description", "").replace("$BR$", "\n"));
		tm.setDynamicCode(Boolean.parseBoolean(p.getProperty("template.dynamic.code")));
		tm.setFirstGenerationDone(Boolean.parseBoolean(p.getProperty("template.first.generation.done")));
		tm.setInherited(Boolean.parseBoolean(p.getProperty("template.inherited")));
		tm.setNMRelation(Boolean.parseBoolean(p.getProperty("template.nmrelation")));
		tm.setComplexUniqueSpecification(p.getProperty("template.uniqueformula", ""));
		int len = this.getInteger(p, "template.stereotype.count", 0);
		for (int i = 0; i < len; i++) {
			String name = p.getProperty("template.stereotype." + i + ".name", "");
			if (name.length() > 0) {
				StereotypeModel stm = null;
				try {
					stm = d.getStereotype(name);
				} catch (NoSuchElementException nsee) {
					stm = this.createStereotype(name, p.getProperty("template.stereotype." + i + ".description", "-"));
					stm.setDoNotPrint(
							Boolean.parseBoolean(p.getProperty("template.stereotype." + i + ".isdonotprint")));
					stm.setHideTable(Boolean.parseBoolean(p.getProperty("template.stereotype." + i + ".ishidetable")));
					d.addStereotype(stm);
				}
				tm.addStereotype(stm);
			}
		}
		len = this.getInteger(p, "template.panel.count", 0);
		for (int i = 0; i < len; i++) {
			String name = p.getProperty("template.panel." + i + ".title", "");
			if (name.length() > 0) {
				PanelModel pm = this.createPanel();
				pm.setPanelNumber(i);
				pm.setTabTitle(name);
				pm.setTabMnemonic(p.getProperty("template.panel." + i + ".mnemonic", ""));
				pm.setTabToolTipText(p.getProperty("template.panel." + i + ".tooltiptext", "-"));
				tm.addPanel(pm);
			}
		}
		PanelModel[] panels = tm.getPanels();
		/*
		 * len = this.getInteger(p, "template.stereotype.count", 0); for (int i = 0; i < len; i++) { String name =
		 * p.getProperty("template.stereotype." + i + ".name", ""); if (name.length() > 0) { StereotypeModel stm = null;
		 * try { stm = d.getStereotype(p.getProperty("template.stereotype." + i + ".name", "" )); } catch
		 * (NoSuchElementException nsee) { } if (stm == null) { String description =
		 * p.getProperty("template.stereotype." + i + ".description", "-").replace("$BR$", "\n"); stm =
		 * this.createStereotype(name, description); if (Boolean.parseBoolean(p.getProperty("template.stereotype." + i +
		 * ".ishidetable"))) { ((Stereotype) stm).setHideTable(true); } } tm.addStereotype(stm); } }
		 */
		len = this.getInteger(p, "template.domain.count", 0);
		for (int i = 0; i < len; i++) {
			String name = p.getProperty("template.domain." + i + ".name", "");
			DBType dbt = DBType.valueOf(p.getProperty("template.domain." + i + ".type", ""));
			if ((dbt != null) && (name.length() > 0)) {
				DomainModel dom = null;
				try {
					dom = d.getDomain(p.getProperty("template.column." + i + ".domain", ""));
				} catch (NoSuchElementException nsee) {
				}
				if (dom == null) {
					dom = this.getDomain(name, DBType.Convert(dbt),
							this.getInteger(p, "template.domain." + i + ".vks", 0),
							this.getInteger(p, "template.domain." + i + ".nks", 0), d);
					String description = p.getProperty("template.domain." + i + ".description", "-");
					dom.setComment(description);
				}
			}
		}
		len = this.getInteger(p, "template.column.count", 0);
		for (int i = 0; i < len; i++) {
			String name = p.getProperty("template.column." + i + ".name", "");
			DomainModel dom = null;
			try {
				dom = d.getDomain(p.getProperty("template.column." + i + ".domain", ""));
			} catch (NoSuchElementException nsee) {
			}
			if ((name.length() > 0) && (dom != null)) {
				TabellenspaltenModel tsm = this.createTabellenspalte(name, dom,
						Boolean.parseBoolean(p.getProperty("template.column." + i + ".primarykey")));
				tsm.setComment(p.getProperty("template.column." + i + ".description", "").replace("$BR$", "\n"));
				tsm.setNotNull(Boolean.parseBoolean(p.getProperty("template.column." + i + ".notnull")));
				tsm.setEditormember(Boolean.parseBoolean(p.getProperty("template.column." + i + ".editormember")));
				tsm.setDisabled(Boolean.parseBoolean(p.getProperty("template.column." + i + ".disabled")));
				tsm.setEditorPosition(this.getInteger(p, "template.column." + i + ".editorposition", 0));
				tsm.setLabelText(p.getProperty("template.column." + i + ".labeltext", ""));
				tsm.setMaxCharacters(this.getInteger(p, "template.column." + i + ".maxcharacters", 0));
				tsm.setMnemonic(p.getProperty("template.column." + i + ".mnemonic", ""));
				tsm.setReferenceWeight(
						ReferenceWeight.valueOf2(p.getProperty("template.column." + i + ".referenceweight", "keine")));
				tsm.setToolTipText(p.getProperty("template.column." + i + ".tooltiptext", "-"));
				tsm.setWriteablemember(
						Boolean.parseBoolean(p.getProperty("template.column." + i + ".writeablemember")));
				tsm.setPanel((PanelModel) panels[this.getInteger(p, "template.column." + i + ".panel", 0)]);
				tsm.setRessourceIdentifier(p.getProperty("template.column." + i + ".ressourceidentifier", ""));
				tsm.setTechnicalField(Boolean.parseBoolean(p.getProperty("template.column." + i + ".technicalfield")));
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".comparetomember"))) {
					tm.addCompareMember(tsm);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".equalsmember"))) {
					tm.addEqualsMember(tsm);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".hashcodemember"))) {
					tm.addHashCodeMember(tsm);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".tostring.member"))) {
					tm.addToStringMember(new ToStringContainer(tsm, tm,
							p.getProperty("template.column." + i + ".tostring.prefix", "").replace("&nbsp;", " "),
							p.getProperty("template.column." + i + ".tostring.suffix", "").replace("&nbsp;", " ")));
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".tocombostring.member"))) {
					tm.addComboStringMember(new ToStringContainer(tsm, tm,
							p.getProperty("template.column." + i + ".tocombostring.prefix", "").replace("&nbsp;", " "),
							p.getProperty("template.column." + i + ".tocombostring.suffix", "").replace("&nbsp;",
									" ")));
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".selectionmember"))) {
					tm.getAuswahlMembers().addElement(tsm);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".orderbymember"))) {
					tm.addSelectionViewOrderMember(createOrderMember(tsm));
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".canbereferenced"))) {
					tsm.setCanBeReferenced(true);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".hidereference"))) {
					tsm.setHideReference(true);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".deprecated"))) {
					tsm.setDeprecated(true);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".coded"))) {
					tsm.setKodiert(true);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".indexed"))) {
					tsm.setIndexed(true);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".global"))) {
					tsm.setGlobal(true);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".globalid"))) {
					tsm.setGlobalId(true);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".alterinbatch"))) {
					tsm.setAlterInBatch(true);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".lastmodificationfield"))) {
					tsm.setLastModificationField(true);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".removedstatefield"))) {
					tsm.setRemovedStateField(true);
				}
				if (Boolean.parseBoolean(p.getProperty("template.column." + i + ".indexsearchmember"))) {
					tsm.setIndexSearchMember(true);
				}
				if (p.getProperty("template.column." + i + ".referenceto.table", "").length() > 0) {
					TabellenModel tm0 = d.getTabelle(p.getProperty("template.column." + i + ".referenceto.table"));
					if (tm0 != null) {
						TabellenspaltenModel tsm0 = tm0
								.getTabellenspalte(p.getProperty("template.column." + i + ".referenceto.column"));
						if (tsm0 != null) {
							RelationModel rm = this.createRelation(view, tsm, Direction.UP, 0, tsm0, Direction.UP, 0);
							tsm.setRelation(rm);
						}
					}
				}
				tm.addTabellenspalte(tsm);
			}
		}
	}

	/**
	 * @changed OLI 06.11.2007 - Erweiterung um die M&ouml;glichkeit &uuml;ber Templatedateien auch Referenzen zu
	 *          definieren.<BR>
	 * 
	 */
	public TabellenModel createTabelle(ViewModel view, int x, int y, DiagrammModel d,
			boolean filled /*
						    * , boolean template
						    */) {
		TabellenModel tm = new Tabelle(view, x, y, d);
		if (!filled) {
			PanelModel pm = this.createPanel();
			pm.setPanelNumber(0);
			pm.setTabTitle("data");
			pm.setTabMnemonic("1");
			pm.setTabToolTipText(StrUtil.FromHTML("Hier k&ouml;nnen Sie die Daten des Objekt " + "warten"));
			tm.addPanel(pm);
		}
		if (this.templatefilename == null) {
			if (filled) {
				PanelModel pm = this.createPanel();
				pm.setPanelNumber(0);
				pm.setTabTitle("data");
				pm.setTabMnemonic("1");
				pm.setTabToolTipText(StrUtil.FromHTML("Hier k&ouml;nnen Sie die Daten des " + "Objekt warten"));
				tm.addPanel(pm);
				DomainModel domainIdent = this.getDomain("Ident", Types.INTEGER, 0, 0, d);
				DomainModel domainBezeichnung = this.getDomain("Description", Types.VARCHAR, 50, 0, d);
				DomainModel domainBoolean = this.getDomain("Boolean", Types.INTEGER, 0, 0, d);
				DomainModel domainKuerzel = this.getDomain("Token", Types.VARCHAR, 10, 0, d);
				TabellenspaltenModel tsmIdent = this.createTabellenspalte(tm.getName(), domainIdent, true);
				tsmIdent.setEditormember(false);
				tsmIdent.setLabelText("");
				tsmIdent.setToolTipText("");
				tsmIdent.setMnemonic("");
				tsmIdent.setWriteablemember(false);
				tsmIdent.setPanel((PanelModel) tm.getPanels()[0]);
				tm.addTabellenspalte(tsmIdent);
				TabellenspaltenModel tsmAktiv = this.createTabellenspalte("Deleted", domainBoolean, false);
				tsmAktiv.setEditormember(false);
				tsmAktiv.setLabelText("");
				tsmAktiv.setToolTipText("");
				tsmAktiv.setMnemonic("");
				tsmAktiv.setWriteablemember(false);
				tsmAktiv.setPanel((PanelModel) tm.getPanels()[0]);
				tm.addTabellenspalte(tsmAktiv);
				TabellenspaltenModel tsmBezeichnung = this.createTabellenspalte("Description", domainBezeichnung,
						false);
				tsmBezeichnung.setEditormember(true);
				tsmBezeichnung.setLabelText("Bezeichnung");
				tsmBezeichnung.setEditorPosition(1);
				tsmBezeichnung.setRessourceIdentifier("TextFieldDescription");
				tsmBezeichnung.setToolTipText(
						StrUtil.FromHTML("Geben Sie hier eine eindeutige," + "aussagekr&auml;ftige Bezeichnung ein."));
				tsmBezeichnung.setMnemonic("B");
				tsmBezeichnung.setWriteablemember(true);
				tsmBezeichnung.setPanel((PanelModel) tm.getPanels()[0]);
				tm.addTabellenspalte(tsmBezeichnung);
				TabellenspaltenModel tsmKuerzel = this.createTabellenspalte("Token", domainKuerzel, false);
				tsmKuerzel.setLabelText(StrUtil.FromHTML("K&uuml;rzel"));
				tsmKuerzel.setToolTipText(StrUtil
						.FromHTML("Geben Sie hier ein eindeutiges, " + "aussagekr&auml;ftiges K&uuml;rzel ein."));
				tsmKuerzel.setMnemonic("K");
				tsmKuerzel.setWriteablemember(true);
				tsmKuerzel.setEditorPosition(2);
				tsmKuerzel.setRessourceIdentifier("TextFieldToken");
				tsmKuerzel.setPanel((PanelModel) tm.getPanels()[0]);
				tm.addTabellenspalte(tsmKuerzel);
				tm.setGenerateCode(true);
				tm.addEqualsMember(tsmIdent);
				tm.addEqualsMember(tsmBezeichnung);
				tm.addEqualsMember(tsmAktiv);
				tm.addEqualsMember(tsmKuerzel);
				tm.addCompareMember(tsmBezeichnung);
				tm.addHashCodeMember(tsmIdent);
				tm.addToStringMember(new ToStringContainer(tsmBezeichnung, tm));
				tm.addToStringMember(new ToStringContainer(tsmKuerzel, tm, " (", ")"));
				tm.addComboStringMember(new ToStringContainer(tsmBezeichnung, tm));
				tm.addComboStringMember(new ToStringContainer(tsmKuerzel, tm, " (", ")"));
				tm.addSelectableColumn(new SelectionMember(tsmBezeichnung, SelectionAttribute.IMPORTANT, ""));
				tm.addSelectableColumn(new SelectionMember(tsmKuerzel, SelectionAttribute.IMPORTANT, ""));
				tm.addSelectionViewOrderMember(createOrderMember(tsmBezeichnung));
				tm.setComplexUniqueSpecification("Description & Deleted | Token & Deleted");
				StereotypeModel stm = null;
				try {
					stm = d.getStereotype("Deactivatable");
				} catch (NoSuchElementException nsee) {
					stm = this.createStereotype("Deactivatable",
							StrUtil.FromHTML("Diese " + "Stereotype kennzeichnet eine Tabelle, deren Datens&auml;tze "
									+ "nicht gel&ouml;scht, sondern lediglich deaktiviert werden " + "sollen."));
					d.addStereotype(stm);
				}
				tm.addStereotype(stm);
			}
		} else if (filled) {
			ApplicationUtil.ReadProperties(this.templatefilename);
			tm.setGenerateCode(Boolean.getBoolean("template.code"));
			tm.setComplexUniqueSpecification(System.getProperty("template.uniqueformula", ""));
			int len = Integer.getInteger("template.stereotype.count", 0);
			for (int i = 0; i < len; i++) {
				String name = System.getProperty("template.stereotype." + i + ".name", "");
				if (name.length() > 0) {
					StereotypeModel stm = null;
					try {
						stm = d.getStereotype(name);
					} catch (NoSuchElementException nsee) {
						stm = this.createStereotype(name,
								System.getProperty("template.stereotype." + i + ".description", "-"));
						d.addStereotype(stm);
					}
					tm.addStereotype(stm);
				}
			}
			len = Integer.getInteger("template.panel.count", 0);
			for (int i = 0; i < len; i++) {
				String name = System.getProperty("template.panel." + i + ".title", "");
				if (name.length() > 0) {
					PanelModel pm = this.createPanel();
					pm.setPanelNumber(i);
					pm.setTabTitle(name);
					pm.setTabMnemonic(System.getProperty("template.panel." + i + ".mnemonic", ""));
					pm.setTabToolTipText(System.getProperty("template.panel." + i + ".tooltiptext", "-"));
					tm.addPanel(pm);
				}
			}
			PanelModel[] panels = tm.getPanels();
			len = Integer.getInteger("template.domain.count", 0);
			for (int i = 0; i < len; i++) {
				String name = System.getProperty("template.domain." + i + ".name", "");
				DBType dbt = DBType.valueOf(System.getProperty("template.domain." + i + ".type", ""));
				if ((dbt != null) && (name.length() > 0)) {
					DomainModel dom = this.getDomain(name, DBType.Convert(dbt),
							Integer.getInteger("template.domain." + i + ".vks", 0),
							Integer.getInteger("template.domain." + i + ".nks", 0), d);
					String description = System.getProperty("template.domain." + i + ".description", "-");
					dom.setComment(description);
				}
			}
			len = Integer.getInteger("template.column.count", 0);
			for (int i = 0; i < len; i++) {
				String name = System.getProperty("template.column." + i + ".name", "");
				DomainModel dom = null;
				try {
					dom = d.getDomain(System.getProperty("template.column." + i + ".domain", ""));
				} catch (NoSuchElementException nsee) {
				}
				if ((name.length() > 0) && (dom != null)) {
					TabellenspaltenModel tsm = this.createTabellenspalte(name, dom,
							Boolean.getBoolean("template.column." + i + ".primarykey"));
					tsm.setEditormember(Boolean.getBoolean("template.column." + i + ".editormember"));
					tsm.setEditorPosition(Integer.getInteger("template.column." + i + ".editorposition", 0));
					tsm.setLabelText(System.getProperty("template.column." + i + ".labeltext", ""));
					tsm.setMnemonic(System.getProperty("template.column." + i + ".mnemonic", ""));
					tsm.setToolTipText(System.getProperty("template.column." + i + ".tooltiptext", "-"));
					tsm.setWriteablemember(Boolean.getBoolean("template.column." + i + ".writeablemember"));
					tsm.setPanel((PanelModel) panels[Integer.getInteger("template.column." + i + ".panel", 0)]);
					tsm.setRessourceIdentifier(System.getProperty("template.column." + i + ".ressourceidentifier", ""));
					if (Boolean.getBoolean("template.column." + i + ".comparetomember")) {
						tm.addCompareMember(tsm);
					}
					if (Boolean.getBoolean("template.column." + i + ".equalsmember")) {
						tm.addEqualsMember(tsm);
					}
					if (Boolean.getBoolean("template.column." + i + ".hashcodemember")) {
						tm.addHashCodeMember(tsm);
					}
					if (Boolean.getBoolean("template.column." + i + ".tostring.member")) {
						tm.addToStringMember(new ToStringContainer(tsm, tm,
								System.getProperty("template.column." + i + ".tostring.prefix", "").replace("&nbsp;",
										" "),
								System.getProperty("template.column." + i + ".tostring.suffix", "").replace("&nbsp;",
										" ")));
					}
					if (Boolean.getBoolean("template.column." + i + ".tocombostring.member")) {
						tm.addComboStringMember(new ToStringContainer(tsm, tm,
								System.getProperty("template.column." + i + ".tocombostring.prefix", "")
										.replace("&nbsp;", " "),
								System.getProperty("template.column." + i + ".tocombostring.suffix", "")
										.replace("&nbsp;", " ")));
					}
					if (Boolean.getBoolean("template.column." + i + ".selectionmember")) {
						tm.getAuswahlMembers().addElement(tsm);
					}
					if (Boolean.getBoolean("template.column." + i + ".orderbymember")) {
						tm.addSelectionViewOrderMember(createOrderMember(tsm));
					}
					if (Boolean.getBoolean("template.column." + i + ".canbereferenced")) {
						tsm.setCanBeReferenced(true);
					}
					if (Boolean.getBoolean("template.column." + i + ".hidereference")) {
						tsm.setHideReference(true);
					}
					if (System.getProperty("template.column." + i + ".referenceto.table", "").length() > 0) {
						TabellenModel tm0 = d
								.getTabelle(System.getProperty("template.column." + i + ".referenceto.table"));
						if (tm0 != null) {
							TabellenspaltenModel tsm0 = tm0.getTabellenspalte(
									System.getProperty("template.column." + i + ".referenceto.column"));
							if (tsm0 != null) {
								RelationModel rm = this.createRelation(view, tsm, Direction.UP, 0, tsm0, Direction.UP,
										0);
								tsm.setRelation(rm);
							}
						}
					}
					tm.addTabellenspalte(tsm);
				}
			}
		}
		return tm;
	}

	public TabellenspaltenModel createTabellenspalte(String n, DomainModel dom) {
		return new Tabellenspalte(n, dom);
	}

	public TabellenspaltenModel createTabellenspalte(String n, DomainModel dom, boolean pk) {
		return new Tabellenspalte(n, dom, pk);
	}

	public DefaultCommentModel createDefaultComment() {
		return new DefaultComment(this.getADF());
	}

	public DefaultCommentModel createDefaultComment(String name, String kommentar) {
		DefaultComment dc = new DefaultComment(this.getADF());
		dc.setComment(kommentar);
		dc.setPattern(name);
		return dc;
	}

	public CodeFactory createCodeFactory(String cls) {
		CodeFactory cf = new DefaultCodeFactory();
		if ((cls != null) && !cls.isEmpty()) {
			try {
				cf = (CodeFactory) Class.forName(cls).newInstance();
			} catch (Exception e) {
				/*
				 * System.out.println("\n\n*******************************************************" +
				 * "********************"); System.out.println("\nInstanzierung der CodeFactory " + cls +
				 * " fehlgeschlagen!"); System.out.println("\nexception: " + e.getMessage());
				 * e.printStackTrace();System.out.println( "\nArbeit wird mit DefaultCodeFactory fortgesetzt.\n");
				 * System.out.println( "***********************************************************" +
				 * "****************");
				 */
				LOG.error("error while code factory instantiation: " + cls + ", exception: "
						+ e.getClass().getSimpleName() + " - " + e.getMessage());
			}
		} else {
			LOG.warn("Code factory cannot be instantiated for null or empty name!");
			LOG.warn("Returned alternate code factory: " + cf.getClass().getName());
		}
		return cf;
	}

	public NReferenzModel createNReferenz() {
		return new NReferenz();
	}

	public NReferenzModel createNReferenz(TabellenModel tm) {
		return new NReferenz(tm);
	}

	public ViewModel createView() {
		return new View();
	}

	public ViewModel createMainView(String name, String beschreibung, boolean isShowReferencedColumns) {
		return new MainView(name, beschreibung, isShowReferencedColumns);
	}

	public PanelModel createPanel() {
		return new Panel();
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public OptionModel createOption() {
		return new Option("option");
	}

	public OrderMemberModel createOrderMember() {
		return new OrderMember();
	}

	public OrderMemberModel createOrderMember(TabellenspaltenModel tsm) {
		return new OrderMember(tsm);
	}

	public ArchimedesDescriptorFactory getADF() {
		return this.adf;
	}

	public void setADF(ArchimedesDescriptorFactory adf) {
		this.adf = adf;
	}

	@Override
	public void setTemplateFilename(String fn) {
		this.templatefilename = fn;
	}

	/* Hilfsmethoden. */

	/**
	 * Pr&uumL;ft, ob das angegebene Diagramm &uuml;ber eine Domain mit dem angegebenen Namen verf&uuml;gt und legt
	 * diese gegebenenfalls an.
	 * 
	 * @param name Der Name der gesuchten Domain.
	 * @param dt   Der Types-Datentyp f&uuml;r den Fall, da&szlig; die Domain angelegt werden mu&szlig;.
	 * @param len  Die L&auml;nge des Datenfeldes, falls erforderlich.
	 * @param nks  Eine Angabe zu den Nachkommastellen der Domain.
	 * @param dm   Eine Referenz auf das DiagrammModel, in dem Gesucht werden soll.
	 * @return Eine bereits vorhandene Domain mit dem angegebenen Namen bzw. eine neuerzeugte, falls keine mit dem Namen
	 *         im DiagrammModel gefunden worden ist.
	 */
	protected DomainModel getDomain(String name, int dt, int len, int nks, DiagrammModel dm) {
		DomainModel dom = null;
		try {
			dom = dm.getDomain(name);
		} catch (NoSuchElementException nsee) {
			dom = this.createDomain(name, dt, len, nks);
			dm.addDomain(dom);
		}
		return dom;
	}

	/**
	 * @changed OLI 20.08.2013 - Added.
	 */
	@Override
	public SelectionMemberModel createSelectionMember(TabellenspaltenModel tsm) {
		SelectionMemberModel smm = new SelectionMember(null, SelectionAttribute.IMPORTANT, "");
		smm.setAttribute(SelectionAttribute.OPTIONAL);
		smm.setColumn(tsm);
		return smm;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public SequenceModel createSequence(String name, long increment, long startValue) {
		Sequence s = new Sequence();
		s.setIncrement(increment);
		s.setName(name);
		s.setStartValue(startValue);
		return s;
	}

}