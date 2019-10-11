/*
 * DefaultArchimedesDescriptorFactory.java
 *
 * 05.07.2005
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.app;

import java.sql.Types;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Icon;

import org.apache.log4j.Logger;

import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.NReferenzModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.legacy.scheme.DefaultCodeFactory;
import archimedes.model.ColumnModel;
import archimedes.model.NReferencePanelType;
import archimedes.model.OrderMemberModel;
import archimedes.model.PanelModel;
import archimedes.model.ReferenceWeight;
import archimedes.model.TableModel;
import archimedes.model.ToStringContainerModel;
import corent.base.Attributed;
import corent.base.SortedVector;
import corent.base.StrUtil;
import corent.base.Utl;
import corent.base.dynamic.AttributeDescriptor;
import corent.base.dynamic.DefaultAttributeDescriptor;
import corent.dates.LongPTimestamp;
import corent.dates.PDate;
import corent.dates.PTime;
import corent.dates.PTimestamp;
import corent.db.ColumnRecord;
import corent.db.DefaultJoinDescriptor;
import corent.db.DefaultOrderByDescriptor;
import corent.db.DefaultOrderClause;
import corent.db.DefaultPersistenceDescriptor;
import corent.db.JoinDescriptor;
import corent.db.OrderByDescriptor;
import corent.db.PersistenceDescriptor;
import corent.djinn.ComponentFactory;
import corent.djinn.DefaultComponentFactory;
import corent.djinn.DefaultEditorDescriptor;
import corent.djinn.DefaultEditorDescriptorList;
import corent.djinn.DefaultFilenameSelectorDefaultComponentFactory;
import corent.djinn.DefaultLabelFactory;
import corent.djinn.DefaultLineTextEditorComponentFactory;
import corent.djinn.DefaultPasswordComponentFactory;
import corent.djinn.DefaultTabDescriptor;
import corent.djinn.DefaultTabbedPaneFactory;
import corent.djinn.DefaultVectorPanelButtonFactory;
import corent.djinn.EditorDescriptorList;
import corent.djinn.ModalLineTextEditorComponentFactory;
import corent.djinn.SubEditorFactory;
import corent.djinn.TabDescriptor;
import corent.djinn.TabbedPaneFactory;
import corent.gui.DefaultFilenameSelectorComponentFactory;
import corent.gui.PropertyRessourceManager;

/**
 * Diese Klasse liefert die Musterimplementierung einer
 * ArchimedesDescriptorFactory.
 * <P>
 * Die Property
 * <B>archimedes.app.DefautlArchimedesDescriptorFactory.inherited.fields=true
 * </B> (Boolean) aktiviert die &Uuml;bernahme von ererbten Feldern in die
 * PersistenceDescriptors.
 * 
 * <P>
 * Wird die Property
 * <I>archimedes.app.DefaultArchimedesDescriptorFactory.suppress
 * .contextconcat</I> gesetzt, so kann unterdr&uuml;ckt werden, da&szlig; dem
 * Komponentennamen der Kontextname der Tabelle vorangestellt wird.
 * 
 * @author ollie
 *         <P>
 * 
 * @changed OLI 17.01.2008 - Erweiterung um die Option den Kontextnamen der
 *          Tabelle als Pr&auml;fix in den Komponentennamen einfliessen zu
 *          lassen in der Methode
 *          <TT>getEditorDescriptor(Attributed, String)</TT>.
 *          <P>
 *          OLI 01.05.2009 - Einbau erster log4j-Ausgaben.
 *          <P>
 * 
 */

public class DefaultArchimedesDescriptorFactory implements ArchimedesDescriptorFactory {

	/* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
	private static Logger log = Logger.getLogger(ArchimedesDescriptorFactory.class);

	/*
	 * Referenz auf die ArchimedesApplication, innerhalb derer die Factory
	 * arbeiten soll.
	 */
	private ArchimedesApplication app = null;
	/*
	 * Referenz auf das Archimedes-Datenmodell, aus dem die Descriptoren
	 * generiert werden sollen.
	 */
	private DiagrammModel dm = null;

	/**
	 * Generiert eine neue ArchimedesDescriptorFactory mit dem &uuml;bergebenen
	 * Archimedes-Datenmodell.
	 * 
	 * @param dm
	 *            Das Archimedes-Datenmodell, das als Grundlage der
	 *            Descriptor-Generierungen genutzt werden soll.
	 */
	public DefaultArchimedesDescriptorFactory(DiagrammModel dm) {
		super();
		this.dm = dm;
	}

	/**
	 * Liefert den vollst&auml;ndige Spaltennamen zur angegebenen
	 * Klassen-Tabellennamen-Tupel.
	 * 
	 * @param adf
	 *            Die ArchimedesDescriptorFactory, auf der die Operation
	 *            ausgef&uuml;hrt werden soll.
	 * @param cls
	 *            Die Klasse der Objekte, zu der die Schl&uuml;sselspalte
	 *            gesucht werden soll.
	 * @param tn
	 *            Der Name der Tabelle des Archimedes-Modells, zu dem die
	 *            Schl&uuml;ssel geliefert werden soll.
	 * @return Der vollst&auml;ndige Spaltenname der ersten
	 *         Prim&auml;rschl&uuml;sselspalte.
	 */
	public static String GetFirstPKColName(ArchimedesDescriptorFactory adf, Class cls, String tn) {
		try {
			Vector<ColumnRecord> pks = adf.getPersistenceDescriptor(cls, tn).getKeys();
			if (pks.size() > 0) {
				return pks.elementAt(0).getFullname();
			}
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
		return null;
	}

	/* Implementierung des Interfaces ArchimedesDescriptorFactory. */

	public void setApplication(ArchimedesApplication app) {
		this.app = app;
	}

	public ArchimedesApplication getApplication() {
		return this.app;
	}

	public Hashtable<String, AttributeDescriptor> getDynamicDescriptor(String tn) {
		Hashtable<String, AttributeDescriptor> ht = new Hashtable<String, AttributeDescriptor>();
		TabellenModel tm = this.dm.getTabelle(tn);
		if (tm == null) {
			System.out.println("\nERROR: table " + tn + " not found in model!\n");
		}
		for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
			TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
			String name = tsm.getName();
			DefaultAttributeDescriptor ad = new DefaultAttributeDescriptor();
			ad.setAttributeName(name);
			ad.setAttributeInitialValue(DefaultCodeFactory.GetInitializerValue(tsm.getDomain()));
			String clsname = "java.lang.".concat(DefaultCodeFactory.GetWrapper(DefaultCodeFactory.GetType(tsm
					.getDomain())));
			if (tsm.getDomain().getName().equals("PDate")) {
				ad.setAttributeInitialValue(PDate.UNDEFINIERT);
				clsname = "corent.dates.PDate";
			} else if (tsm.getDomain().getName().equals("PTime")) {
				ad.setAttributeInitialValue(new PTime(0));
				clsname = "corent.dates.PTime";
			} else if (tsm.getDomain().getName().equals("PTimestamp")) {
				ad.setAttributeInitialValue(PTimestamp.NULL);
				clsname = "corent.dates.PTimestamp";
			} else if (tsm.getDomain().getName().equals("LongPTimestamp")) {
				ad.setAttributeInitialValue(LongPTimestamp.NULL);
				clsname = "corent.dates.LongPTimestamp";
			}
			Class cls = null;
			try {
				cls = Class.forName(clsname);
			} catch (Exception e) {
				cls = new Object().getClass();
			}
			ad.setAttributeClass(cls);
			ad.setReference((tsm.getRelation() != null));
			ht.put(name, ad);
		}
		if (tm.isInherited()) {
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
				if (tsm.isPrimarykey() && (tsm.getRelation() != null)) {
					Hashtable<String, AttributeDescriptor> ht0 = this.getDynamicDescriptor(tsm.getRelation()
							.getReferenced().getTable().getName());
					for (Iterator it = ht0.keySet().iterator(); it.hasNext();) {
						String key = (String) it.next();
						AttributeDescriptor ad0 = ht0.get(key);
						if (ad0 != null) {
							ht.put(key, ad0);
						}
					}
				}
			}
		}
		for (int i = 0, len = tm.getNReferenzModelCount(); i < len; i++) {
			NReferenzModel nrm = tm.getNReferenzModelAt(i);
			TabellenspaltenModel tsm = nrm.getTabellenspalte();
			DefaultAttributeDescriptor ad = new DefaultAttributeDescriptor();
			String name = StrUtil.Replace(tsm.getFullName(), ".", "");
			ad.setAttributeName(name);
			ad.setAttributeInitialValue(new SortedVector());
			String clsname = "java.util.SortedVector";
			Class cls = null;
			try {
				cls = Class.forName(clsname);
			} catch (Exception e) {
				cls = new Object().getClass();
			}
			ad.setAttributeClass(cls);
			ad.setReference((tsm.getRelation() != null));
			ht.put(name, ad);
		}
		return ht;
	}

	public PersistenceDescriptor getPersistenceDescriptor(Class cls, String tn) {
		DefaultPersistenceDescriptor pd = null;
		TabellenModel tm = this.dm.getTabelle(tn);
		if (tm != null) {
			java.util.List<TabellenspaltenModel> ltsm = null;
			if (tm.isInherited()
					&& Boolean.getBoolean("archimedes.app.DefautlArchimedesDescriptorFactory.inherited.fields")) {
				ltsm = tm.getAlleTabellenspalten();
			} else {
				ltsm = tm.getTabellenspalten();
			}
			ColumnRecord[] crs = new ColumnRecord[ltsm.size()];
			for (int i = 0, len = ltsm.size(); i < len; i++) {
				TabellenspaltenModel tsm = ltsm.get(i);
				ColumnRecord cr = new ColumnRecord(i, tsm.getTabelle().getName(), tsm.getName(), tsm.isPrimarykey());
				cr.setKodiert(tsm.isKodiert());
				if (tsm.getDomain().getName().equals("PDate")) {
					cr.setConvertTo(new PDate().getClass());
				} else if (tsm.getDomain().getName().equals("PTime")) {
					cr.setConvertTo(new PTime().getClass());
				} else if (tsm.getDomain().getName().equals("PTimestamp")) {
					cr.setConvertTo(new PTimestamp().getClass());
				} else if (tsm.getDomain().getName().equals("LongPTimestamp")) {
					cr.setConvertTo(new LongPTimestamp().getClass());
				}
				crs[i] = cr;
			}
			Vector<ColumnRecord> vsvm = new Vector<ColumnRecord>();
			Vector<String> vsvcn = new Vector<String>();
			Vector auswahlmembers = tm.getAuswahlMembers();
			for (int i = 0, len = auswahlmembers.size(); i < len; i++) {
				TabellenspaltenModel tsm = (TabellenspaltenModel) auswahlmembers.elementAt(i);
				ColumnRecord cr = new ColumnRecord(i, tsm.getTabelle().getName(), tsm.getName(), tsm.isPrimarykey());
				cr.setKodiert(tsm.isKodiert());
				vsvm.addElement(cr);
				String cn = tsm.getName();
				if (!tsm.getTabelle().getName().equals(tm.getName())) {
					cn = cn.concat(" (").concat(tsm.getTabelle().getName()).concat(")");
				}
				// System.out.println("archimedes.app.DefaultArchimedesDescriptorFactory."
				// + cls.getName()
				// + ".table.view.header." + tsm.getFullName());
				if (Utl.GetProperty("archimedes.app.DefaultArchimedesDescriptorFactory." + cls.getName()
						+ ".table.view.header." + tsm.getFullName()) != null) {
					cn = Utl.GetProperty("archimedes.app.DefaultArchimedesDescriptorFactory." + cls.getName()
							+ ".table.view.header." + tsm.getFullName());
				}
				vsvcn.addElement(cn);
			}
			Vector<JoinDescriptor> vjd = new Vector<JoinDescriptor>();
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
				if (tsm.getRelation() != null) {
					TabellenspaltenModel ref = (TabellenspaltenModel) tsm.getRelation().getReferenced();
					vjd.addElement(new DefaultJoinDescriptor(JoinDescriptor.Type.LEFT_OUTER, new ColumnRecord(i, ref
							.getTabelle().getName(), ref.getName(), ref.isPrimarykey()), new ColumnRecord(i, tm
							.getName(), tsm.getName(), tsm.isPrimarykey())));
				}
			}
			if (tm.isInherited()) {
				for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
					TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
					if (tsm.isPrimarykey() && (tsm.getRelation() != null)) {
						PersistenceDescriptor pd0 = this.getPersistenceDescriptor(cls, tsm.getRelation()
								.getReferenced().getTable().getName());
						Vector<JoinDescriptor> vjd0 = pd0.getSelectionJoins();
						for (int j = 0, lenj = vjd0.size(); j < lenj; j++) {
							vjd.addElement(vjd0.elementAt(j));
						}
					}
				}
			}
			Vector<String> vnecn = new Vector<String>();
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
				if (tsm.isWriteablemember()) {
					vnecn.addElement(tsm.getName());
				}
			}
			if (tm.isInherited()) {
				for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
					TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
					if (tsm.isPrimarykey() && (tsm.getRelation() != null)) {
						PersistenceDescriptor pd0 = this.getPersistenceDescriptor(cls, tsm.getRelation()
								.getReferenced().getTable().getName());
						Vector<String> vnecn0 = pd0.getNotEmptyColumnnames();
						for (int j = 0, lenj = vnecn0.size(); j < lenj; j++) {
							vnecn.addElement(vnecn0.elementAt(j));
						}
					}
				}
			}
			pd = new DefaultPersistenceDescriptor(cls, crs, vsvm.toArray(new ColumnRecord[] {}), vsvcn
					.toArray(new String[] {}), vjd.toArray(new JoinDescriptor[] {}), vnecn.toArray(new String[] {}), tm
					.getComplexUniqueSpecification());
		}
		return pd;
	}

	/**
	 * @changed OLI 17.01.2008 - Erweiterung um die Option den Kontextnamen der
	 *          Tabelle als Pr&auml;fix in den Komponentennamen einfliessen zu
	 *          lassen.<BR>
	 * 
	 */
	public EditorDescriptorList getEditorDescriptor(Attributed attr, String tn) {
		boolean contextconcat = !Boolean
				.getBoolean("archimedes.app.DefaultArchimedesDescriptorFactory.suppress.contextconcat");
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		DefaultComponentFactory stddcf = DefaultComponentFactory.INSTANZ;
		DefaultLabelFactory stddlf = DefaultLabelFactory.INSTANZ;
		SortedVector sv = new SortedVector();
		TabellenModel tm = this.dm.getTabelle(tn);
		String namepath = tm.getContextName();
		if (tm != null) {
			int panel = 0;
			/*
			 * for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++)
			 * { TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
			 */
			java.util.List<TabellenspaltenModel> ltsm = null;
			if (tm.isInherited()
					&& Boolean.getBoolean("archimedes.app.DefautlArchimedesDescriptorFactory.inherited.fields")) {
				ltsm = tm.getAlleTabellenspalten(false);
			} else {
				ltsm = tm.getTabellenspalten();
			}
			for (int i = 0, len = ltsm.size(); i < len; i++) {
				TabellenspaltenModel tsm = ltsm.get(i);
				if (tsm.isEditormember()) {
					ComponentFactory dcf = stddcf;
					DefaultLabelFactory dlf = stddlf;
					String n = tsm.getName();
					if (tsm.getRessourceIdentifier().length() > 0) {
						n = tsm.getRessourceIdentifier();
					}
					if ((namepath != null) && (namepath.length() > 0) && contextconcat) {
						n = namepath.concat(n);
					}
					if (tsm.getRelation() != null) {
						TabellenspaltenModel rtsm = (TabellenspaltenModel) tsm.getRelation().getReferenced();
						TabellenModel rtm = rtsm.getTabelle();
						String p = rtm.getCodeVerzeichnis();
						if (!p.endsWith(".")) {
							p = p.concat(".");
						}
						String classname = p.concat(rtm.getName());
						classname = StrUtil.Replace(classname, "/", ".");
						classname = StrUtil.Replace(classname, "\\", ".");
						try {
							Class cls = Class.forName(classname);
							if (tsm.getReferenceWeight() == ReferenceWeight.MASSIVE) {
								dcf = new ArchimedesComponentFactory(new ArchimedesMassiveListSelectorComponentFactory(
										this.app, cls, rtsm.getFullName(), tsm.getName()));
							} else {
								Vector v = app.getDFC().read(cls, null);
								dcf = new ArchimedesComponentFactory(v);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (tsm.getDomain().getName().equalsIgnoreCase("dateiname")
							|| tsm.getDomain().getName().equalsIgnoreCase("filename")) {
						DefaultFilenameSelectorComponentFactory dfscf = new DefaultFilenameSelectorComponentFactory(
								":o)");
						// dfscf.setModalParent(app.getFrame());
						dcf = new DefaultFilenameSelectorDefaultComponentFactory(dfscf);
					} else if ((tsm.getDomain().getDataType() == Types.LONGVARCHAR)
							|| tsm.getDomain().getName().equalsIgnoreCase("langtext")
							|| tsm.getDomain().getName().equalsIgnoreCase("longtext")) {
						// final TabellenspaltenModel tsm0 = tsm;
						// OLI 04.07.2008 - tsm.getName() ist zwar eigentlich
						// gepfuscht, bringt
						// aber im Zusammenspiel mit den PropertyRessourcen die
						// besseren
						// Ergebnisse. ES DARF ALSO NICHT EINFACH GEAENDERT
						// WERDEN !!!
						ModalLineTextEditorComponentFactory mltecf = new ModalLineTextEditorComponentFactory(tsm
								.getName());
						mltecf.setModalParent(app.getFrame());
						dcf = new DefaultLineTextEditorComponentFactory(mltecf, n);
					} else if (tsm.getDomain().getName().equalsIgnoreCase("passwort")
							|| tsm.getDomain().getName().equalsIgnoreCase("password")) {
						dcf = DefaultPasswordComponentFactory.INSTANZ;
					}
					int aid = this.getAttributenames(tm.getName()).indexOf(tsm.getName());
					ArchimedesEditorDescriptor ded = new ArchimedesEditorDescriptor(n, tsm.getPanel().getPanelNumber(),
							attr, aid, dlf, dcf, tsm.getLabelText(), (tsm.getMnemonic().length() > 0 ? tsm
									.getMnemonic().charAt(0) : '\0'), null, tsm.getToolTipText());
					ded.setDisabled(tsm.isDisabled());
					ded.setObligation(tsm.isWriteablemember());
					ded.setAlterInBatch(tsm.isAlterInBatch());
					if ((tsm.getDomain().getLength() > 0) || (tsm.getMaxCharacters() > 0)) {
						if (tsm.getDomain().getLength() < tsm.getMaxCharacters()) {
							tsm.setMaxCharacters(tsm.getDomain().getLength());
						}
						ded.setMaxSize((tsm.getMaxCharacters() == 0 ? tsm.getDomain().getLength() : tsm
								.getMaxCharacters()));
					}
					sv.addElement(new SortedDED(tsm.getEditorPosition(), ded, tsm));
				}
			}
			ArchimedesSubEditorDescriptor ased = null;
			int id = tm.getTabellenspaltenCount();
			for (int i = 0, len = tm.getNReferenzModelCount(); i < len; i++) {
				NReferenzModel nrm = tm.getNReferenzModelAt(i);
				PanelModel pm = nrm.getPanel();
				id = i + tm.getTabellenspaltenCount();
				panel++;
				if (nrm.getNReferencePanelType() == NReferencePanelType.SELECTABLE) {
					ased = new ArchimedesSubEditorDescriptor(pm.getTabTitle(), pm.getPanelNumber(), attr,
							new SelectableSortedListSubEditorFactory(this.app, nrm, nrm.getId()));
				} else if (nrm.getNReferencePanelType() == NReferencePanelType.EDITABLE) {
					ased = new ArchimedesSubEditorDescriptor(nrm.getPanel().getTabTitle(), pm.getPanelNumber(), attr,
							new EditableSortedListSubEditorFactory(this.app, nrm,
									DefaultVectorPanelButtonFactory.INSTANCE, nrm.getId(), Boolean
											.getBoolean("archimedes.app.DefaultArchimedesDescriptorFactory.split")));
				} else {
					ased = new ArchimedesSubEditorDescriptor(nrm.getPanel().getTabTitle(), pm.getPanelNumber(), attr,
							new SortedListSubEditorFactory(this.app, nrm, DefaultVectorPanelButtonFactory.INSTANCE, nrm
									.getId(), Boolean
									.getBoolean("archimedes.app.DefaultArchimedesDescriptorFactory.split"), (nrm
									.getNReferencePanelType() == NReferencePanelType.STANDALONE)));
				}
				sv.addElement(new SortedDED(id, ased, nrm.getTabellenspalte()));
			}
			for (int i = 0, len = tm.getPanelCount(); i < len; i++) {
				PanelModel pm = (PanelModel) tm.getPanelAt(i);
				if ((pm.getPanelClass() != null) && (pm.getPanelClass().length() > 0)) {
					try {
						id++;
						panel++;
						Class cls = Class.forName(pm.getPanelClass());
						SubEditorFactory sef = (SubEditorFactory) cls.newInstance();
						ased = new ArchimedesSubEditorDescriptor(pm.getTabTitle(), pm.getPanelNumber() /* panel */,
								attr, sef);
						sv.addElement(new SortedDED(id, ased, null));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (tm.isInherited()) {
				/*
				 * for (int i = 0, len = dedl.size(); i < len; i++) {
				 * DefaultEditorDescriptor ded = (DefaultEditorDescriptor)
				 * dedl.elementAt(i); if (ded.getTab() > 0) {
				 * ded.setTab(tm.getPanelCount()+ded.getTab()-2); } } for (int i
				 * = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				 * TabellenspaltenModel tsm = tm.getTabellenspalteAt(i); String
				 * name = tsm.getName(); if (tsm.isPrimarykey() &&
				 * (tsm.getRelation() != null)) { EditorDescriptorList edl =
				 * this.getEditorDescriptor(attr,
				 * tsm.getRelation().getReferenced().getTabelle().getName());
				 * for (int j = 0, lenj = edl.size(); j < lenj; j++) {
				 * ArchimedesEditorDescriptor aed = (ArchimedesEditorDescriptor)
				 * edl.elementAt(j); aed.setAttributeId(len +
				 * aed.getAttributeId()); sv.addElement(new
				 * SortedDED(tsm.getEditorPosition(), aed, tsm)); } } }
				 */
			}
			for (int i = 0, len = sv.size(); i < len; i++) {
				dedl.addElement(((SortedDED) sv.elementAt(i)).ded);
			}
		}
		return dedl;
	}

	public Vector<String> getAttributenames(String tn) {
		Vector<String> v = new Vector<String>();
		TabellenModel tm = this.dm.getTabelle(tn);
		if (tm != null) {
			java.util.List<TabellenspaltenModel> ltsm = null;
			if (tm.isInherited()
					&& Boolean.getBoolean("archimedes.app.DefautlArchimedesDescriptorFactory.inherited.fields")) {
				ltsm = tm.getAlleTabellenspalten(false);
			} else {
				ltsm = tm.getTabellenspalten();
			}
			for (int i = 0, len = ltsm.size(); i < len; i++) {
				TabellenspaltenModel tsm = ltsm.get(i);
				v.addElement(tsm.getName());
			}
			/*
			 * for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++)
			 * { TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
			 * v.addElement(tsm.getName()); } if (tm.isInherited()) { for (int i
			 * = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
			 * TabellenspaltenModel tsm = tm.getTabellenspalteAt(i); String name
			 * = tsm.getName(); if (tsm.isPrimarykey() && (tsm.getRelation() !=
			 * null)) { Vector<String> v0 =
			 * this.getAttributenames(tsm.getRelation(
			 * ).getReferenced().getTabelle().getName()); for (int j = 0, lenj =
			 * v0.size(); j < lenj; j++) { v.addElement(v0.elementAt(j)); } } }
			 * }
			 */
		}
		return v;
	}

	/**
	 * @changed OLI 01.05.2009 - Erweiterung um ein paar log-Ausgaben.
	 *          <P>
	 * 
	 */
	public boolean equalsTo(ArchimedesDynamic ad0, ArchimedesDynamic ad1) {
		if (!ad0.getClass().isInstance(ad1)) {
			return false;
		}
		TableModel tm = this.dm.getTableByName(ad0.getTablename());
		if (tm != null) {
			for (ColumnModel c : tm.getEqualsMembers()) {
				Object attr0 = ad0.get(c.getName());
				Object attr1 = ad1.get(c.getName());
				log.debug("check equality of column/attribute " + c.getName() + " (" + attr0 + "==" + attr1);
				if ((attr0 != null) && !attr0.equals(attr1)) {
					return false;
				}
			}
		} else {
			log.info("no valid table model found in method equalsTo for objects class " + ad0.getClass().getName());
		}
		return true;
	}

	public String generateString(ArchimedesDynamic ad) {
		StringBuffer sb = new StringBuffer();
		TableModel tm = this.dm.getTableByName(ad.getTablename());
		if (tm != null) {
			for (ToStringContainerModel tsc : tm.getToStringMembers()) {
				ColumnModel c = tsc.getColumn();
				Object attr = ad.get(c.getName());
				sb.append(tsc.getPrefix()).append((attr != null ? attr.toString() : "null")).append(tsc.getSuffix());
			}
		}
		return sb.toString();
	}

	public TabbedPaneFactory getTabbedPaneFactory(String tn) {
		TabellenModel tm = this.dm.getTabelle(tn);
		TabbedPaneFactory tpf = null;
		boolean panelowner = (tm.getPanelCount() > 1);
		for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
			TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
			if (tsm.isPrimarykey() && (tsm.getRelation() != null)) {
				TabellenModel tm0 = (TabellenModel) tsm.getRelation().getReferenced().getTable();
				if (tm0.getPanelCount() > 0) {
					panelowner = true;
				}
			}
		}
		if (panelowner || tm.isInherited()) {
			boolean resourced = System.getProperty("archimedes.app.resource.type", "none").equalsIgnoreCase(
					"properties");
			Icon icon = null;
			PropertyRessourceManager prm = new PropertyRessourceManager();
			PanelModel[] panels = tm.getPanels();
			String cn = "tab.resource.for.table." + tm.getName();
			TabDescriptor[] tda = new TabDescriptor[panels.length];
			for (int i = 0, len = panels.length; i < len; i++) {
				PanelModel panel = (PanelModel) panels[i];
				String t = panel.getTabTitle();
				char m = (t != null ? panel.getTabMnemonic() : "" + i).charAt(0);
				String ttt = panel.getTabToolTipText();
				if (resourced) {
					t = Utl.GetProperty(cn + ".tab." + i + ".title", t);
					ttt = Utl.GetProperty(cn + ".tab." + i + ".tooltiptext", ttt);
					m = Utl.GetProperty(cn + ".tab." + i + ".mnemonic", "" + m).charAt(0);
					icon = prm.getIcon(cn + ".tab." + i);
				} else {
					icon = null;
				}
				tda[i] = new DefaultTabDescriptor((t != null ? t : "" + i + ".Reiter"), m, icon);
			}
			if (tm.isInherited()) {
				Vector<TabDescriptor> vtd = new Vector<TabDescriptor>();
				for (int i = 0; i < tda.length; i++) {
					vtd.addElement(tda[i]);
				}
				for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
					TabellenspaltenModel tsm0 = tm.getTabellenspalteAt(i);
					if (!tsm0.isPrimarykey() || (tsm0.getRelation() == null)) {
						continue;
					}
					TabellenModel tm0 = (TabellenModel) tsm0.getRelation().getReferenced().getTable();
					TabbedPaneFactory tpf0 = this.getTabbedPaneFactory(tm0.getName());
					if (tpf0 == null) {
						continue;
					}
					Vector<TabDescriptor> vtd0 = tpf0.getTabDescriptors();
					if (vtd0.size() == 0) {
						continue;
					}
					for (int j = 0, lenj = vtd.size(); j < lenj; j++) {
						boolean found = false;
						TabDescriptor td0 = vtd.elementAt(j);
						for (int k = 0, lenk = vtd0.size(); k < lenk; k++) {
							if (vtd0.elementAt(k).getText().equals(td0.getText())) {
								found = true;
								break;
							}
						}
						if (!found) {
							vtd0.addElement(td0);
						}
					}
					for (int j = 0, lenj = vtd0.size(); j < lenj; j++) {
						boolean found = false;
						TabDescriptor td0 = vtd0.elementAt(j);
						for (int k = 0, lenk = vtd.size(); k < lenk; k++) {
							if (vtd.elementAt(k).getText().equals(td0.getText())) {
								found = true;
								break;
							}
						}
						if (!found) {
							vtd.addElement(td0);
						}
					}
					tda = vtd.toArray(new TabDescriptor[] {});
				}
			}
			tpf = new DefaultTabbedPaneFactory(tda);
		}
		/*
		 * if (panelowner) { Vector panels = tm.getPanels(); TabDescriptor[] tda
		 * = new TabDescriptor[panels.size()]; for (int i = 0, len =
		 * panels.size(); i < len; i++) { PanelModel panel = (PanelModel)
		 * panels.elementAt(i); String t = panel.getTabTitle(); char m = (t !=
		 * null ? panel.getTabMnemonic() : "" + i).charAt(0); String ttt =
		 * panel.getTabToolTipText(); tda[i] = new DefaultTabDescriptor((t !=
		 * null ? t : "" + i + ".Reiter"), m, null ); } if (tm.isInherited()) {
		 * for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
		 * TabellenspaltenModel tsm = tm.getTabellenspalteAt(i); String name =
		 * tsm.getName(); if (tsm.isPrimarykey() && (tsm.getRelation() != null))
		 * { TabellenModel tm0 = tsm.getRelation().getReferenced().getTabelle();
		 * TabbedPaneFactory tpf0 = this.getTabbedPaneFactory(tm0.getName());
		 * Vector<TabDescriptor> vtd = tpf0.getTabDescriptors(); if (vtd.size()
		 * > 0) { TabDescriptor[] tda0 = new TabDescriptor[/*tda.length +
		 *//*
			 * vtd.size()]; /* for (int j = 0; j < tda.length; j++) { tda0[j] =
			 * tda[j]; }
			 *//*
				 * for (int j = 0, lenj = vtd.size(); j < lenj; j++) {
				 * tda0[/*tda.length+
				 *//*
					 * j] = vtd.elementAt(j); } tda = tda0; } } } } tpf = new
					 * DefaultTabbedPaneFactory(tda); }
					 */
		return tpf;
	}

	public OrderByDescriptor getOrderByDescriptor(PersistenceDescriptor pd, String tn) {
		DefaultOrderByDescriptor dobd = new DefaultOrderByDescriptor();
		TabellenModel tm = this.dm.getTabelle(tn);
		if (tm != null) {
			for (OrderMemberModel om : tm.getSelectionViewOrderMembers()) {
				ColumnRecord cr = pd.getColumn(om.getOrderColumn().getFullName());
				if (cr == null) {
					TabellenspaltenModel tsm = (TabellenspaltenModel) om.getOrderColumn();
					cr = new ColumnRecord(Integer.MAX_VALUE, tsm.getTable().getName(), tsm.getName());
				}
				DefaultOrderClause oc = new DefaultOrderClause(cr, om.getOrderDirection());
				dobd.addOrderClause(oc);
			}
		}
		return dobd;
	}

	/**
	 * Im Gegensatz zur Standarddefinition der Methode sei hier angemerkt,
	 * da&szlig; die Spaltennamen einen vorangestellten Asterix erhalten, wenn
	 * es sich um kodierte Spalten handelt.
	 */
	public String[] createFilter(String tn) {
		TabellenModel tm = this.dm.getTabelle(tn);
		Vector v = tm.getAuswahlMembers();
		int len = v.size();
		String[] sarr = new String[len];
		for (int i = 0; i < len; i++) {
			sarr[i] = (((TabellenspaltenModel) v.elementAt(i)).isKodiert() ? "*" : "")
					.concat(v.elementAt(i).toString());
		}
		return sarr;
	}

	public DiagrammModel getDiagrammModel() {
		return this.dm;
	}

}

class SortedDED implements Comparable {

	public int sort = 0;
	public DefaultEditorDescriptor ded = null;
	public TabellenspaltenModel tsm = null;

	public SortedDED(int sort, DefaultEditorDescriptor ded, TabellenspaltenModel tsm) {
		super();
		this.ded = ded;
		this.sort = sort;
		this.tsm = tsm;
	}

	/* Implementierung des Interfaces Comparable. */

	public int compareTo(Object o) {
		SortedDED sded = (SortedDED) o;
		int erg = new Integer(this.sort).compareTo(new Integer(sded.sort));
		if ((erg == 0) && (this.tsm != null) && (sded != null)) {
			return this.tsm.compareTo(sded.tsm);
		}
		return erg;
	}

}
