/*
 * DefaultCodeFactory.java
 *
 * 07.07.2004
 *
 * (c) by ollie
 *
 * Nutzung und &Auml;nderung durch die MediSys GmbH mit freundlicher Genehmigung des Autors.
 *
 */

package archimedes.legacy.scheme;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.event.CodeFactoryListener;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.legacy.Archimedes;
import archimedes.legacy.model.DefaultCommentModel;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.NReferenzModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.model.CodeFactory;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.NReferenceModel;
import archimedes.model.StereotypeModel;
import archimedes.model.ToStringContainerModel;
import archimedes.scheme.SelectionMember;
import baccara.gui.GUIBundle;
import corent.base.SortedVector;
import corent.base.StrUtil;
import corent.base.Utl;
import corent.dates.LongPTimestamp;
import corent.dates.PDate;
import corent.dates.PTime;
import corent.dates.PTimestamp;

/**
 * Diese Code-Factory generiert Java-Code aus dem Archimedes-Standard-Modell.
 * Der Code mu&szlig; zum Einsatz mit einer Datenbank erheblich erweitert
 * werden.
 * <P>
 * Die CodeFactory reagiert auf folgende Stereotypen: <BR>
 * &nbsp;
 * <TABLE BORDER=1>
 * <TR>
 * <TH ALIGN=LEFT>Stereotype</TH>
 * <TH>Effekt</TH>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>Cached</TD>
 * <TD>Es wird ein Cache als statische Variable in die Klasse eingebaut.
 * Au&szlig;erdem wird das Interface EditorDjinnMaster implementiert und damit
 * eine Cacheaktualisierung angeregt, wenn ein EditorDjinn, der ein Objekt
 * dieser Klasse anzeigt, mit &Uuml;bernahme ge&auml;ndert wird.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>ColumnViewable</TD>
 * <TD>Eine automatische Implementierung der Methodenr&uuml;pfe des
 * ColumnViewable-Interfaces.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>Deactivatable (Deaktivierbar)</TD>
 * <TD>Die eigentliche Implementierung dieses Interfaces findet im Udschebti
 * statt. Hier wird immer eine vorsorgliche Implementierung vorgehalten. Das
 * Interface Deactivatable kennzeichnet quasi nur noch die Klassen, die von der
 * DB-Schicht tats&auml;chlich entsprechend behandelt werden sollen (Setzen
 * einer Gel&ouml;schtflagge, statt eines physikalischen L&ouml;schens).</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>LastModificationTracker</TD>
 * <TD>Mit diesem Interface verh&auml;lt es sich ebenso, wie mit dem Interface
 * Deactivatable. Die Implementierung der Methoden sollte &uuml;ber den
 * Udschebti erledigt werden. Hei&szlig;t die Tabellenspalte zur Aufnahme des
 * Zeitstempels der letzten &Auml;nderung anders als "LastModificationDate"
 * m&uuml;ssen die beiden Methoden des Interfaces per Hand implementiert werden.
 * </TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>Traceable</TD>
 * <TD>&Uuml;ber dieses Interface wird eine Klasse kennzeichnet, die den Zustand
 * ihrer Datens&auml;tze nach jeder &Auml;nderung festhalten soll. Hierzu ist es
 * allerdings erforderlich, da&szlig; es sich bei der Applikation um eine
 * "ArchimedesTracingApplication" handelt, die in der Lage ist auf die "trace
 * events" zu reagieren.</TD>
 * </TR>
 * </TABLE>
 * 
 * <B>Properies:</B>
 * <TABLE BORDER=1>
 * <TR VALIGN=TOP>
 * <TH>Name</TH>
 * <TH>Typ</TH>
 * <TH>Default</TH>
 * <TH>Funktion</TH>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>archimedes.scheme.DefaultCodeFactory.ts.package</TD>
 * <TD>String</TD>
 * <TD>corentx.dates</TD>
 * <TD>&Uuml;ber diese Property kann der Name des Packages gesetzt werden, in
 * dem sich die Zeitstempelimplementierungen zum System befinden.</TD>
 * </TR>
 * </TABLE>
 * 
 * <P>
 * Zudem wird das Interface Ordered automatisch implementiert, wenn das
 * TabellenModel zur einer Tabelle wenigsten ein OrderMember hat.
 * 
 * @author ollie
 * 
 * @changed OLI 11.02.2008 - Implementierung der Beachtung einer alternativen
 *          Udschebti-Basisklasse, sofern eine solche angegeben ist.
 * @changed OLI 26.03.2008 - Erste Implementierungen zum Thema generierte
 *          Ressourcendateien.
 * @changed OLI 16.08.2008 - Anpassungen der generierten Klassenkommentare an
 *          das aktuelle Format.
 */

public class DefaultCodeFactory implements CodeFactory {

	/* Der Basis-Pfad, in den der Java-Code generiert werden soll. */
	private String path = null;
	/* Liste der zu importierenden Packages. */
	private Hashtable imports = new Hashtable();
	private GUIBundle guiBundle = null;
	private DataModel dataModel = null;

	/** Generiert eine CodeFactory mit Defaultwerten. */
	public DefaultCodeFactory() {
		super();
	}

	/* Implementierung des Interfaces CodeFactory. */

	public boolean generate(String out) {
		DiagrammModel dm = (DiagrammModel) this.dataModel;
		JOptionPane.showMessageDialog(null, StrUtil.FromHTML("Code wird generiert nach:\n" + out), StrUtil
				.FromHTML("Codegenerator"), JOptionPane.INFORMATION_MESSAGE);
		try {
			this.path = out;
			SortedVector sv = new SortedVector();
			SortedVector tabellen = new SortedVector();
			Vector<TabellenModel> tabs = dm.getTabellen();
			for (int i = 0, len = tabs.size(); i < len; i++) {
				tabellen.addElement(tabs.elementAt(i));
			}
			for (int i = 0, len = tabellen.size(); i < len; i++) {
				TabellenModel tm = (TabellenModel) tabellen.elementAt(i);
				if (tm.isGenerateCode()) {
					this.codeTable(tm, dm, out);
				}
			}
			for (int i = 0, len = tabellen.size(); i < len; i++) {
				TabellenModel tm = (TabellenModel) tabellen.elementAt(i);
				if (tm.isActiveInApplication()) {
					String packagename = this.makeDirAndPackage(tm, dm.getCodePfad().replace("~",
							System.getProperty("user.home")), dm.getBasePackageName());
					if (!sv.contains("import " + packagename)) {
						sv.addElement("import " + packagename);
						sv.addElement("import " + packagename + ".db");
					}
				}
			}
			sv.addElement("import archimedes.legacy.app");
			sv.addElement("import corent.db.xs");
			sv.addElement("import corent.djinn");
			sv.addElement("import corent.files");
			sv.addElement("import corent.gui");
			sv.addElement("import java.util");
			sv.addElement("import javax.swing");
			System.out.println("writing " + path + dm.getApplicationName() + "/" + dm.getApplicationName()
					+ "Core.java");
			Vector zeilen = new Vector();
			zeilen.addElement("/*");
			zeilen.addElement(" * " + dm.getApplicationName() + "Core.java");
			zeilen.addElement(" *");
			zeilen.addElement(" * AUTOMATISCH VON ARCHIMEDES GENERIERT!!!");
			zeilen.addElement(" *");
			zeilen.addElement(" * " + new PDate().toString());
			zeilen.addElement(" *");
			zeilen.addElement(" * (c) by " + dm.getAuthor());
			zeilen.addElement(" *");
			zeilen.addElement(" */");
			zeilen.addElement("");
			zeilen.addElement("package " + dm.getBasePackageName() + ";");
			zeilen.addElement("");
			zeilen.addElement("");
			String pn0 = "";
			for (int i = 0, len = sv.size(); i < len; i++) {
				String pn = (String) sv.elementAt(i);
				if ((pn0.indexOf(".") > 0) && !pn.startsWith(pn0.substring(0, pn0.indexOf(".") + 1))) {
					zeilen.addElement("");
				}
				zeilen.addElement(pn + ".*;");
				pn0 = pn;
			}
			zeilen.addElement("");
			zeilen.addElement("");
			zeilen.addElement("/**");
			zeilen.addElement(" * Abstrakte Basisklasse f&uuml;r die Applikation.");
			zeilen.addElement(" *");
			zeilen.addElement(" * @author");
			zeilen.addElement(" *     " + dm.getAuthor());
			zeilen.addElement(" *     <P>");
			zeilen.addElement(" *");
			zeilen.addElement(" * @changed");
			zeilen.addElement(" *     ?? " + new PDate().toString() + " - Hinzugef&uuml;gt.");
			zeilen.addElement(" *     <P>");
			zeilen.addElement(" *");
			zeilen.addElement(" */");
			zeilen.addElement("");
			zeilen.addElement("public abstract class " + dm.getApplicationName() + "Core "
					+ "extends JFrameWithInifile implements ArchimedesApplication, MenuSummoner" + " {");
			zeilen.addElement("");
			zeilen.addElement("    /**");
			zeilen.addElement("     * Erzeugt eine Instanz der " + dm.getApplicationName() + "-Basisklasse.");
			zeilen.addElement("     *");
			zeilen.addElement("     * @param title Der Titel des Hauptfensters.");
			zeilen.addElement("     * @param ini Die Inidatei, aus der die Anwendung ihre "
					+ "Voreinstellungen beziehen soll.");
			zeilen.addElement("     */");
			zeilen.addElement("    public " + dm.getApplicationName() + "Core(String title, " + "Inifile ini) {");
			zeilen.addElement("        super(title, ini);");
			zeilen.addElement("    }");
			zeilen.addElement("");
			zeilen.addElement("    /**");
			zeilen.addElement("     * Erzeugt eine Hashtable mit allen DBFactories zur " + "Anwendung.");
			zeilen.addElement("     *");
			zeilen.addElement("     * @param adf Die ArchimedesDescriptorFactory der Anwendung.");
			zeilen.addElement("     */");
			zeilen.addElement("    public Hashtable<Class, DBFactory> createFactories("
					+ "ArchimedesDescriptorFactory adf) {");
			zeilen
					.addElement("        Hashtable<Class, DBFactory> factories = new "
							+ "Hashtable<Class, DBFactory>();");
			for (int i = 0, len = tabellen.size(); i < len; i++) {
				TabellenModel tm = (TabellenModel) tabellen.elementAt(i);
				if (tm.isActiveInApplication() && !tm.isDeprecated()) {
					zeilen.addElement("        factories.put(" + tm.getName() + ".class, new " + "DBFactory"
							+ tm.getName() + "(adf));");
				}
			}
			zeilen.addElement("        return factories;");
			zeilen.addElement("    }");
			zeilen.addElement("");
			zeilen.addElement("    /**");
			zeilen.addElement("     * Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer "
					+ "eine der Stammdatenwartungspunkte");
			zeilen.addElement("     * aufruft.");
			zeilen.addElement("     *");
			zeilen.addElement("     * @param cls Die Class, deren Objekte stammgewartet werden " + "sollen.");
			zeilen.addElement("     * @param headline Die &Uuml;berschrift zum Auswahldialog " + "der Wartung.");
			zeilen.addElement("     */");
			zeilen.addElement("    public abstract void doWartungStammdaten(Class cls, String " + "headline);");
			zeilen.addElement("");
			zeilen.addElement("");
			zeilen.addElement("    /* Implementierung des Interfaces MenuSummoner. */");
			zeilen.addElement("");
			zeilen.addElement("    public void processMenuEvent(String context, String[] params" + ") {");
			boolean first = true;
			for (int i = 0, len = tabellen.size(); i < len; i++) {
				TabellenModel tm = (TabellenModel) tabellen.elementAt(i);
				if (tm.isActiveInApplication() && !tm.isDeprecated()) {
					String s = "} else ";
					if (first) {
						s = "";
					}
					zeilen.addElement("        " + s + "if (context.equals(\"wartung/" + tm.getName() + "\")) {");
					zeilen.addElement("            this.doWartungStammdaten(" + tm.getName() + ".class, \""
							+ tm.getName() + "\");");
					first = false;
				}
			}
			if (!first) {
				zeilen.addElement("        }");
			}
			zeilen.addElement("    }");
			zeilen.addElement("");
			zeilen.addElement("}");
			try {
				new File(path + dm.getBasePackageName().replace(".", "/")).mkdirs();
				System.out.println("directory " + path + dm.getBasePackageName().replace(".", "/") + " created");
			} catch (Exception e) {
			}
			FileWriter fw = new FileWriter(path + dm.getBasePackageName().replace(".", "/") + "/"
					+ dm.getApplicationName() + "Core.java", false);
			BufferedWriter writer = new BufferedWriter(fw);
			for (int i = 0, len = zeilen.size(); i < len; i++) {
				writer.write((String) zeilen.elementAt(i) + "\n");
			}
			writer.flush();
			writer.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @changed OLI 26.03.2008 - Erweiterung um die Erzeugung einer
	 *          Ressourcendatei zur Tabelle.
	 * 
	 */
	protected void codeTable(TabellenModel tm, DiagrammModel dm, String out) throws Exception {
		SortedVector res = new SortedVector();
		String classname = null;
		int nrCount = tm.getNReferences().length;
		String packagename = this.makeDirAndPackage(tm, dm.getCodePfad().replace("~", System.getProperty("user.home")),
				dm.getBasePackageName());
		Vector zeilenSkeleton = new Vector();
		Vector zeilenDBFactory = new Vector();
		Vector zeilen = new Vector();
		classname = packagename + "." + tm.getName();
		zeilen.addElement("/*");
		zeilen.addElement(" * " + tm.getName() + (tm.isDynamicCode() ? "Udschebti" : "") + ".java");
		zeilen.addElement(" *");
		zeilen.addElement(" * AUTOMATISCH VON ARCHIMEDES GENERIERT!!!");
		zeilen.addElement(" *");
		zeilen.addElement(" * " + new PDate().toString());
		zeilen.addElement(" *");
		zeilen.addElement(" * (c) by " + dm.getAuthor());
		zeilen.addElement(" *");
		zeilen.addElement(" */");
		zeilen.addElement("");
		zeilen.addElement("package " + packagename + (tm.isDynamicCode() ? ".udschebtis" : "") + ";");
		zeilen.addElement("");
		zeilen.addElement("");
		zeilen.addElement("$IMPORTS");
		zeilen.addElement("");
		zeilen.addElement("");
		zeilen.addElement("/**");
		zeilen.addElement(" * " + StrUtil.ToHTML(tm.getComment()));
		zeilen.addElement(" *");
		zeilen.addElement(" * @author " + dm.getAuthor());
		zeilen.addElement(" *");
		zeilen.addElement(" */");
		zeilen.addElement("");
		if (tm.isDynamicCode()) {
			// Eine Standardimplementierung der DBFactory.
			zeilenDBFactory.addElement("/*");
			zeilenDBFactory.addElement(" * DBFactory" + tm.getName() + ".java");
			zeilenDBFactory.addElement(" *");
			zeilenDBFactory.addElement(" * AUTOMATISCH VON ARCHIMEDES GENERIERT!!!");
			zeilenDBFactory.addElement(" *");
			zeilenDBFactory.addElement(" * ZUM UEBERSCHREIBEN GEEIGNET!!!");
			zeilenDBFactory.addElement(" *");
			zeilenDBFactory.addElement(" * " + new PDate().toString());
			zeilenDBFactory.addElement(" *");
			zeilenDBFactory.addElement(" * (c) by " + dm.getAuthor());
			zeilenDBFactory.addElement(" *");
			zeilenDBFactory.addElement(" */");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("package " + packagename + ".db;");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("import archimedes.legacy.app.*;");
			zeilenDBFactory.addElement("");
			if (nrCount > 0) {
				zeilenDBFactory.addElement("import corent.db.*;");
				zeilenDBFactory.addElement("import corent.db.xs.*;");
				zeilenDBFactory.addElement("");
				zeilenDBFactory.addElement("import java.sql.*;");
				zeilenDBFactory.addElement("import java.util.*;");
			}
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("import " + packagename + ".*;");
			if (nrCount > 0) {
				zeilenDBFactory.addElement("import " + packagename + ".scheme.*;");
			}
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("/**");
			zeilenDBFactory.addElement(" * Eine DBFactory-Implementierung zur Klasse " + tm.getName());
			zeilenDBFactory.addElement(" *");
			zeilenDBFactory.addElement(" * @author");
			zeilenDBFactory.addElement(" *     " + dm.getAuthor());
			zeilenDBFactory.addElement(" *     <P>");
			zeilenDBFactory.addElement(" *");
			zeilenDBFactory.addElement(" * @changed");
			zeilenDBFactory.addElement(" *     ?? " + new PDate().toString() + " - Hinzugef&uuml;gt.");
			zeilenDBFactory.addElement(" *     <P>");
			zeilenDBFactory.addElement(" *");
			zeilenDBFactory.addElement(" */");
			zeilenDBFactory.addElement("");
			// Der Stub fuer weitere Implementierungen.
			zeilenSkeleton.addElement("/*");
			zeilenSkeleton.addElement(" * " + tm.getName() + ".java");
			zeilenSkeleton.addElement(" *");
			zeilenSkeleton.addElement(" * AUTOMATISCH VON ARCHIMEDES GENERIERT!!!");
			zeilenSkeleton.addElement(" *");
			zeilenSkeleton.addElement(" * ZUM UEBERSCHREIBEN GEEIGNET!!!");
			zeilenSkeleton.addElement(" *");
			zeilenSkeleton.addElement(" * " + new PDate().toString());
			zeilenSkeleton.addElement(" *");
			zeilenSkeleton.addElement(" * (c) by " + dm.getAuthor());
			zeilenSkeleton.addElement(" *");
			zeilenSkeleton.addElement(" */");
			zeilenSkeleton.addElement("");
			zeilenSkeleton.addElement("package " + packagename + ";");
			zeilenSkeleton.addElement("");
			zeilenSkeleton.addElement("");
			zeilenSkeleton.addElement("$IMPORTS");
			zeilenSkeleton.addElement("");
			zeilenSkeleton.addElement("");
			zeilenSkeleton.addElement("/**");
			zeilenSkeleton.addElement(" * " + StrUtil.ToHTML(tm.getComment()));
			zeilenSkeleton.addElement(" *");
			zeilenSkeleton.addElement(" * @author");
			zeilenSkeleton.addElement(" *     " + dm.getAuthor());
			zeilenSkeleton.addElement(" *     <P>");
			zeilenSkeleton.addElement(" *");
			zeilenSkeleton.addElement(" * @changed");
			zeilenSkeleton.addElement(" *     ?? " + new PDate().toString() + " - Hinzugef&uuml;gt.");
			zeilenSkeleton.addElement(" *     <P>");
			zeilenSkeleton.addElement(" *");
			zeilenSkeleton.addElement(" */");
			zeilenSkeleton.addElement("");
		}
		boolean cached = false;
		boolean columnviewable = false;
		boolean deaktivierbar = false;
		boolean inherited = false;
		boolean lastmodificationtracker = false;
		// boolean listowner = false;
		boolean ordered = false;
		boolean tabbed = (tm.getPanelCount() > 1);
		boolean traceable = false;
		for (int i = 0, len = tm.getStereotypenCount(); i < len; i++) {
			StereotypeModel stm = tm.getStereotypeAt(i);
			if (stm.getName().equalsIgnoreCase("cached")) {
				cached = true;
			} else if (stm.getName().equalsIgnoreCase("columnviewable")) {
				columnviewable = true;
			} else if (stm.getName().equalsIgnoreCase("deaktivierbar")
					|| stm.getName().equalsIgnoreCase("deactivatable")) {
				deaktivierbar = true;
			} else if (stm.getName().equalsIgnoreCase("lastmodificationtracker")) {
				lastmodificationtracker = true;
			} else if (stm.getName().equalsIgnoreCase("listowner")) {
				// listowner = true;
			} else if (stm.getName().equalsIgnoreCase("traceable")) {
				traceable = true;
			}
		}
		int omCount = tm.getSelectionViewOrderMembers().length;
		if (omCount > 0) {
			ordered = true;
		}
		if (!tm.isDynamicCode()) {
			zeilen.addElement("public class " + tm.getName() + " implements Attributed, "
					+ (deaktivierbar ? "Deactivatable, " : "")
					+ (lastmodificationtracker ? "LastModificationTracker, " : "") + "PersistentEditable"
					+ (tabbed ? ", TabbedEditable" : "") + " {");
			zeilen.addElement("");
			this.imports.put("corent.base.*", "");
			this.imports.put("corent.db.*", "");
			this.imports.put("corent.db.xs.*", "");
			this.imports.put("corent.djinn.*", "");
		} else {
			String superklasse = (dm.getUdschebtiBaseClassName().length() > 0 ? dm.getUdschebtiBaseClassName()
					: "ApplicationObject");
			boolean ok = false;
			TabellenspaltenModel tsm = null;
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm0 = tm.getTabellenspalteAt(i);
				if (tsm0.isPrimarykey() && !ok) {
					tsm = tsm0;
					ok = true;
				} else if (tsm0.isPrimarykey() && ok) {
					tsm = null;
					ok = false;
					break;
				}
			}
			if (ok && tm.isInherited() && (tsm != null) && (tsm.getRelation() != null)) {
				superklasse = tsm.getRelation().getReferenced().getTable().getName();
				inherited = true;
			}
			String z = "public class " + tm.getName() + "Udschebti extends " + superklasse;
			if (tabbed) {
				z = z.concat(" implements ");
				boolean komma = false;
				if (tabbed) {
					z = z.concat((komma ? ", " : "")).concat("TabbedEditable");
					komma = true;
				}
			}
			z = z.concat(" {");
			zeilen.addElement(z);
			zeilen.addElement("");
			this.imports.put("archimedes.app.*", "");
			this.imports.put("corent.djinn.*", "");
			z = "public class " + tm.getName() + " extends " + tm.getName() + "Udschebti";
			if (cached || columnviewable || deaktivierbar || lastmodificationtracker) {
				z = z.concat(" implements ");
				boolean komma = false;
				if (columnviewable) {
					z = z.concat((komma ? ", " : "")).concat("ColumnViewable");
					this.imports.put("javax.swing.table.*", "");
					komma = true;
				}
				if (deaktivierbar) {
					z = z.concat((komma ? ", " : "")).concat("Deactivatable");
					komma = true;
					this.imports.put("corent.db.xs.*", "");
				}
				if (cached) {
					z = z.concat((komma ? ", " : "")).concat("EditorDjinnMaster");
					komma = true;
					this.imports.put("java.util.*", "");
				}
				if (lastmodificationtracker) {
					z = z.concat((komma ? ", " : "")).concat("LastModificationTracker");
					komma = true;
				}
				if (nrCount > 0) {
					z = z.concat((komma ? ", " : "")).concat("ListOwner");
					komma = true;
				}
				if (ordered) {
					z = z.concat((komma ? ", " : "")).concat("Ordered");
					komma = true;
					this.imports.put("corent.db.*", "");
				}
				if (traceable) {
					z = z.concat((komma ? ", " : "")).concat("Traceable");
					komma = true;
				}
			}
			z = z.concat(" {");
			zeilenSkeleton.addElement(z);
			zeilenSkeleton.addElement("");
			zeilenDBFactory.addElement("public class DBFactory" + tm.getName() + " extends " + "DefaultDBFactory<"
					+ tm.getName() + "> {");
		}
		// Konstantensammlung f&uuml;r das Attributed-Interface herstellen.
		// zeilen.addElement("    /* Einfacher Zugriff auf die Klassen-Daten. */");
		// zeilen.addElement("    public static final Class CLASS = new " +
		// tm.getName()
		// + (tm.isDynamicCode() ? "Udschebti" : "") + "().getClass();");
		if (!tm.isDynamicCode()) {
			zeilen.addElement("");
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
				zeilen.addElement("    /** Bezeichner f&uuml;r den Zugriff auf die Eigenschaft " + tsm.getName()
						+ ". */");
				zeilen.addElement("    public static final int ID_" + tsm.getName().toUpperCase() + " = " + i + ";");
			}
			zeilen.addElement("");
			// PersistenceDescriptor ...
			zeilen.addElement("    /* Der PersistenceDescriptor zur Klasse. */");
			if (deaktivierbar) {
				zeilen.addElement("    public static final ColumnRecord StatusColumn = new "
						+ "ColumnRecord(ID_GELOESCHT, \"" + tm.getName() + "\", \"Geloescht\");");
			}
			zeilen.addElement("    public static final PersistenceDescriptor PD = new "
					+ "DefaultPersistenceDescriptor(this.class,");
			zeilen.addElement("            new ColumnRecord[] {");
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
				String s = "            ";
				if (deaktivierbar && tsm.getName().equalsIgnoreCase("geloescht")) {
					s = s.concat("StatusColumn");
				} else {
					s = s.concat("new ColumnRecord(ID_" + tsm.getName().toUpperCase() + ", \"" + tm.getName()
							+ "\", \"" + tsm.getName())
							+ "\"";
					if (tsm.isPrimarykey()) {
						s = s.concat(", true");
					}
					s = s.concat(")");
				}
				if (i + 1 == len) {
					s = s.concat("});");
				} else {
					s = s.concat(",");
				}
				zeilen.addElement(s);
			}
			zeilen.addElement("");
			// Sortierte TabellenspaltenListe herstellen. */
			SortedVector tsms = new SortedVector();
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
				// if (!tsm.isPrimarykey()) {
				tsms
						.addElement(new TSMSortContainer(GetType(tsm.getDomain()) + " " + tsm.getName().toLowerCase(),
								tsm));
				// }
			}
			// Attribute definieren.
			for (int i = 0, len = tsms.size(); i < len; i++) {
				TabellenspaltenModel tsm = ((TSMSortContainer) tsms.elementAt(i)).tsm;
				zeilen.addElement("    /* " + StrUtil.Replace(this.getBeschreibung(tsm), "\n", "<BR>")
						+ (this.getBeschreibung(tsm).endsWith(".") ? "" : ".") + " */");
				zeilen.addElement("    private " + GetType(tsm.getDomain()) + " " + tsm.getName().toLowerCase() + " = "
						+ GetInitializer(tsm.getDomain()) + ";");
			}
			zeilen.addElement("");
		} else {
			// zeilenSkeleton.addElement("    /** Einfacher Zugriff auf die Klassen-Daten. */");
			// zeilenSkeleton.addElement("    public static final Class CLASS = new "
			// + tm.getName() + "().getClass();");
			if (cached) {
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    /** Ein Cache, z. B. zur Nutzung in " + "Tabellenansichten. */");
				zeilenSkeleton.addElement("    public static DBFactoryTableCache<" + tm.getName()
						+ "> Cache = new DBFactoryTableCache<" + tm.getName() + ">(STANDARDAPP.getADF(),");
				zeilenSkeleton.addElement("            new " + tm.getName() + "(" + "STANDARDAPP.getADF()), \""
						+ tm.getPrimaryKeyName() + "\");");
				zeilenSkeleton.addElement("");
			}
		}
		// Der parameterlose Constructor ...
		if (!tm.isDynamicCode()) {
			zeilen.addElement("");
			zeilen.addElement("    /** Generiert eine Instanz der Klasse mit Defaultwerten. */");
			zeilen.addElement("    public " + tm.getName() + "() {");
			zeilen.addElement("        super();");
			zeilen.addElement("    }");
			zeilen.addElement("");
			zeilen.addElement("");
		} else {
			zeilen.addElement("");
			zeilen.addElement("    private " + tm.getName() + "Udschebti() {");
			zeilen.addElement("        super(null" + (inherited ? ", \"" + tm.getName() + "\"" : ", \"\"") + ");");
			zeilen.addElement("    }");
			zeilen.addElement("");
			zeilen.addElement("    /**");
			zeilen.addElement("     * Generiert eine Instanz der Klasse mit Defaultwerten.");
			zeilen.addElement("     *");
			zeilen.addElement("     * @param adf Die ArchimedesDescriptorFactory, aus der die "
					+ "Instanz ihre Konfiguration ");
			zeilen.addElement("     *     beziehen soll.");
			zeilen.addElement("     * @param tn Der Name der Tabelle, auf die sich die Klasse " + "beziehen soll.");
			zeilen.addElement("     */");
			zeilen.addElement("    protected " + tm.getName() + "Udschebti("
					+ "ArchimedesDescriptorFactory adf, String tn) {");
			zeilen.addElement("        super(adf, tn);");
			zeilen.addElement("    }");
			zeilen.addElement("");
			zeilen.addElement("    /**");
			zeilen.addElement("     * Generiert eine Instanz der Klasse mit Defaultwerten.");
			zeilen.addElement("     *");
			zeilen.addElement("     * @param adf Die ArchimedesDescriptorFactory, aus der die "
					+ "Instanz ihre Konfiguration ");
			zeilen.addElement("     *     beziehen soll.");
			zeilen.addElement("     */");
			zeilen.addElement("    public " + tm.getName() + "Udschebti(" + "ArchimedesDescriptorFactory adf) {");
			zeilen.addElement("        super(adf, \"" + tm.getName() + "\");");
			zeilen.addElement("    }");
			zeilen.addElement("");
			zeilen.addElement("");
			zeilenSkeleton.addElement("");
			zeilenSkeleton.addElement("    private " + tm.getName() + "() {");
			zeilenSkeleton.addElement("        super(null" + (inherited ? ", \"" + tm.getName() + "\"" : "") + ");");
			zeilenSkeleton.addElement("    }");
			zeilenSkeleton.addElement("");
			zeilenSkeleton.addElement("    /**");
			zeilenSkeleton.addElement("     * Generiert eine Instanz der Klasse mit " + "Defaultwerten.");
			zeilenSkeleton.addElement("     *");
			zeilenSkeleton.addElement("     * @param adf Die ArchimedesDescriptorFactory, "
					+ "aus der die Instanz ihre Konfiguration ");
			zeilenSkeleton.addElement("     *     beziehen soll.");
			zeilenSkeleton.addElement("     * @param tn Der Name der Tabelle, auf die sich "
					+ "die Klasse beziehen soll.");
			zeilenSkeleton.addElement("     */");
			zeilenSkeleton.addElement("    protected " + tm.getName() + "("
					+ "ArchimedesDescriptorFactory adf, String tn) {");
			zeilenSkeleton.addElement("        super(adf, tn);");
			zeilenSkeleton.addElement("    }");
			zeilenSkeleton.addElement("");
			zeilenSkeleton.addElement("    /**");
			zeilenSkeleton.addElement("     * Generiert eine Instanz der Klasse mit " + "Defaultwerten.");
			zeilenSkeleton.addElement("     *");
			zeilenSkeleton.addElement("     * @param adf Die ArchimedesDescriptorFactory, "
					+ "aus der die Instanz ihre Konfiguration ");
			zeilenSkeleton.addElement("     *     beziehen soll.");
			zeilenSkeleton.addElement("     */");
			zeilenSkeleton.addElement("    public " + tm.getName() + "(" + "ArchimedesDescriptorFactory adf) {");
			zeilenSkeleton.addElement("        super(adf" + (inherited ? ", \"" + tm.getName() + "\"" : "") + ");");
			zeilenSkeleton.addElement("    }");
			zeilenSkeleton.addElement("");
			zeilenSkeleton.addElement("");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("    /**");
			zeilenDBFactory.addElement("     * Generiert eine Instanz der Klasse mit " + "Defaultwerten.");
			zeilenDBFactory.addElement("     *");
			zeilenDBFactory.addElement("     * @param adf Die ArchimedesDescriptorFactory, "
					+ "aus der die Instanz ihre Konfiguration ");
			zeilenDBFactory.addElement("     *     beziehen soll.");
			zeilenDBFactory.addElement("     */");
			zeilenDBFactory.addElement("    public DBFactory" + tm.getName() + "(ArchimedesDescriptorFactory adf) {");
			zeilenDBFactory.addElement("        super(adf);");
			zeilenDBFactory.addElement("    }");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("    public " + tm.getName() + " create() {");
			zeilenDBFactory.addElement("        return new " + tm.getName() + "(this.adf);");
			zeilenDBFactory.addElement("    }");
			zeilenDBFactory.addElement("");
			if (nrCount > 0) {
				NReferenzModel nr = (NReferenzModel) tm.getNReferences()[0];
				TabellenspaltenModel tsm = nr.getTabellenspalte();
				TabellenModel tmr = tsm.getTabelle();
				String tmn = tm.getName();
				String tmnr = tmr.getName();
				zeilenDBFactory.addElement("    public Vector<" + tm + "> read(String w, "
						+ "Connection c, OrderByDescriptor o)");
				zeilenDBFactory.addElement("            throws SQLException {");
				zeilenDBFactory.addElement("        return this.read(w, c);");
				zeilenDBFactory.addElement("    }");
				zeilenDBFactory.addElement("");
				zeilenDBFactory.addElement("    public Vector<" + tmn + "> read(String w, "
						+ "Connection c) throws SQLException {");
				zeilenDBFactory.addElement("        " + tmn + " o = new " + tmn + "(this.adf);");
				zeilenDBFactory.addElement("        " + tmnr + " r = new " + tmnr + "(this.adf);");
				zeilenDBFactory.addElement("        Vector<" + tmn + "> v = new Vector<" + tmn + ">();");
				zeilenDBFactory.addElement("        int ofieldcount = DBFactoryUtil."
						+ "GetSelectionFieldCount(o.getPersistenceDescriptor());");
				zeilenDBFactory.addElement("        int rfieldcount = DBFactoryUtil."
						+ "GetSelectionFieldCount(r.getPersistenceDescriptor());");
				zeilenDBFactory.addElement("        String select = DBFactoryUtil."
						+ "GetSelectionFields(o.getPersistenceDescriptor());");
				zeilenDBFactory.addElement("        select = select.concat(\", \").concat("
						+ "DBFactoryUtil.GetSelectionFields(");
				zeilenDBFactory.addElement("                r.getPersistenceDescriptor()));");
				zeilenDBFactory.addElement("        select = \"select \".concat(select)." + "concat(\" from " + tmn
						+ "\\n\"");
				zeilenDBFactory.addElement("                + \"left outer join " + tmnr + " on" + " "
						+ tsm.getFullName() + "=" + tmn + "." + tm.getPrimaryKeyName() + "\\n\");");
				zeilenDBFactory.addElement("        if ((w != null) && !w.equals(\"\")) {");
				zeilenDBFactory.addElement("            select = select.concat(\"where \" " + "+ w + \"\\n\");");
				zeilenDBFactory.addElement("        }");
				zeilenDBFactory.addElement("        select = select.concat(\"order by " + tmn + "."
						+ tm.getPrimaryKeyName() + ", " + tmnr + "." + tmr.getPrimaryKeyName() + "\\n\");");
				zeilenDBFactory.addElement("        ResultSet rs = DBExec.Query(c, select);");
				zeilenDBFactory.addElement("        while (rs.next()) {");
				zeilenDBFactory.addElement("            int oid = rs.getInt(1);");
				zeilenDBFactory.addElement("            if (oid != o.get" + tmn + "()) {");
				zeilenDBFactory.addElement("                o = (" + tmn
						+ ") DBFactoryUtil.GetFromRS(rs, o.getPersistenceDescriptor(), o, " + "1, 1);");
				zeilenDBFactory.addElement("                o.set" + tmn + "(oid);");
				zeilenDBFactory.addElement("                v.addElement(o);");
				zeilenDBFactory.addElement("            }");
				zeilenDBFactory.addElement("            int rid = rs.getInt(ofieldcount+1);");
				zeilenDBFactory.addElement("            if (rid != r.get" + tmnr + "()) {");
				zeilenDBFactory.addElement("                r = (" + tmnr
						+ ") DBFactoryUtil.GetFromRS(rs, r.getPersistenceDescriptor(), r,");
				zeilenDBFactory.addElement("                        ofieldcount+1, 1);");
				zeilenDBFactory.addElement("                r.set" + tmnr + "(rid);");
				zeilenDBFactory.addElement("                o.get" + tmnr + "().addElement(r);");
				zeilenDBFactory.addElement("            }");
				zeilenDBFactory.addElement("        }");
				zeilenDBFactory.addElement("        DBExec.CloseQuery(rs);");
				zeilenDBFactory.addElement("        return v;");
				zeilenDBFactory.addElement("    }");
				zeilenDBFactory.addElement("");
				zeilenDBFactory.addElement("    public void write(" + tmn + " o, "
						+ "Connection c) throws SQLException {");
				zeilenDBFactory.addElement("        super.write(o, c);");
				zeilenDBFactory.addElement("        DBExec.Update(c, \"delete from " + tmnr + "where " + tmn
						+ "=\" + o.get" + tmn + "());");
				zeilenDBFactory.addElement("        DBFactory" + tmnr + " dbfr = new " + "DBFactory" + tmnr
						+ "(this.adf);");
				zeilenDBFactory.addElement("        for (int i = 0, len = o.get" + tmnr + "().size(); i < len; i++) {");
				zeilenDBFactory.addElement("            " + tmnr + " r = (" + tmnr + ") o.get" + tmnr + "().get(i);");
				zeilenDBFactory.addElement("            r.setADF(this.adf);");
				zeilenDBFactory.addElement("            r.set" + tmnr + "(k.get" + tmn + "() * 1000 + i+1);");
				zeilenDBFactory.addElement("            dbfk.write(k, c);");
				zeilenDBFactory.addElement("        }");
				zeilenDBFactory.addElement("    }");
				zeilenDBFactory.addElement("");
				zeilenDBFactory.addElement("    public void remove(" + tmn + " o, boolean "
						+ "forced, Connection c) throws SQLException {");
				zeilenDBFactory.addElement("        super.remove(o, forced, c);");
				boolean rdeact = false;
				for (int i = 0, len = tmr.getStereotypenCount(); i < len; i++) {
					StereotypeModel stm = tmr.getStereotypeAt(i);
					if (stm.getName().equalsIgnoreCase("deaktivierbar")
							|| stm.getName().equalsIgnoreCase("deactivatable")) {
						rdeact = true;
						break;
					}
				}
				if (rdeact) {
					zeilenDBFactory.addElement("        if (forced) {");
					zeilenDBFactory.addElement("            DBExec.Update(c, \"delete from " + tmnr + " where " + tmn
							+ "=\" + o.get" + tmn + "());");
					zeilenDBFactory.addElement("        } else {");
					zeilenDBFactory.addElement("            DBExec.Update(c, \"update " + tmnr
							+ " set Geloescht=1 where " + tmn + "=\" + o.get" + tmn + "());");
					zeilenDBFactory.addElement("        }");
				} else {
					zeilenDBFactory.addElement("        DBExec.Update(c, \"delete from " + tmnr + " where " + tmn
							+ "=\" + o.get" + tmn + "());");
				}
				zeilenDBFactory.addElement("    }");
				zeilenDBFactory.addElement("");
			}
			zeilenDBFactory.addElement("}");
		}
		// Ressourcen.
		this.createResourcesFromTable(res, classname, tm);
		// Accessoren und Mutatoren. */
		if (!tm.isDynamicCode()) {
			zeilen.addElement("    /* Accessoren & Mutatoren. */");
			zeilen.addElement("");
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
				String n = tsm.getName();
				String t = GetType(tsm.getDomain());
				zeilen.addElement("    /** @return Der Wert der Eigenschaft " + n + ". */");
				zeilen.addElement("    public " + t + (t.equals("boolean") ? " is" : " get") + n + "() {");
				zeilen.addElement("        return this." + n.toLowerCase() + ";");
				zeilen.addElement("    }");
				zeilen.addElement("");
				zeilen.addElement("    /**");
				zeilen.addElement("     * Setzt den &uuml;bergebenen Wert als neuen Wert "
						+ "f&uuml;r die Eigenschaft " + n + ".");
				zeilen.addElement("     *");
				zeilen
						.addElement("     * @param " + n.toLowerCase() + " Der neue Wert der " + "Eigenschaft " + n
								+ ".");
				zeilen.addElement("     */");
				zeilen.addElement("    public void set" + n + "(" + t + " " + n.toLowerCase() + ") {");
				if (IsReference(tsm.getDomain())) {
					zeilen.addElement("        if (" + n.toLowerCase() + " == null) {");
					zeilen.addElement("            this." + n.toLowerCase() + " = " + GetInitializer(tsm.getDomain())
							+ ";");
					zeilen.addElement("        }");
				}
				zeilen.addElement("        this." + n.toLowerCase() + " = " + n.toLowerCase() + ";");
				zeilen.addElement("    }");
				if (i < len - 1) {
					zeilen.addElement("");
				}
			}
			zeilen.addElement("");
			zeilen.addElement("");
			// equals. hashCode & toString. */
			if (tm.getEqualsMembers().length + tm.getHashCodeMembers().length + tm.getToStringMembers().length > 0) {
				zeilen.addElement("    /* Ueberschreiben von Methoden der Superklasse " + "(Pflichtteil). */");
				zeilen.addElement("");
			}
			if (tm.getEqualsMembers().length > 0) {
				zeilen.addElement("    public boolean equals(Object o) {");
				zeilen.addElement("        if (!(o instanceof " + tm.getName() + ")) {");
				zeilen.addElement("            return false;");
				zeilen.addElement("        }");
				String fl = tm.getName().substring(0, 1).toLowerCase();
				zeilen.addElement("        " + tm.getName() + " " + fl + " = (" + tm.getName() + ") o;");
				String s = "";
				ColumnModel[] cs = tm.getEqualsMembers();
				for (int i = 0, len = cs.length; i < len; i++) {
					String n = cs[i].getName();
					String t = GetType(cs[i].getDomain());
					if (s.length() > 0) {
						s += " && ";
					}
					if (IsReference(cs[i].getDomain())) {
						s += "this.get" + n + "().equals(" + fl + ".get" + n + "())";
					} else {
						String accessor = (t.equals("boolean") ? "is" : "get") + n + "()";
						s += "(this." + accessor + " == " + fl + "." + accessor + ")";
					}
					if (i + 1 == len) {
						s += ";";
					}
				}
				zeilen.addElement("        return " + s);
				zeilen.addElement("    }");
				zeilen.addElement("");
			}
			if (tm.getHashCodeMembers().length > 0) {
				zeilen.addElement("    public int hashCode() {");
				zeilen.addElement("        int result = 17;");
				ColumnModel[] cs = tm.getEqualsMembers();
				for (int i = 0, len = cs.length; i < len; i++) {
					String n = cs[i].getName();
					String t = GetType(cs[i].getDomain());
					if (IsReference(cs[i].getDomain())) {
						zeilen.addElement("        result = 37 * result + this.get" + n + "().hashCode();");
					} else {
						String accessor = (t.equals("boolean") ? "is" : "get") + n + "()";
						String s = "        result = 37 * result + ";
						if (t.equals("boolean")) {
							s += "(this." + accessor + " ? 1 : 0);";
						} else {
							s += (t.equals("int") ? "" : "(int) ") + "this." + accessor + ";";
						}
						zeilen.addElement(s);
					}
				}
				zeilen.addElement("        return result;");
				zeilen.addElement("    }");
				zeilen.addElement("");
			}
			if (tm.getToStringMembers().length > 0) {
				zeilen.addElement("    public String toString() {");
				String s = "";
				for (ToStringContainerModel tsc : tm.getToStringMembers()) {
					String n = tsc.getColumn().getName();
					String t = GetType(tsc.getColumn().getDomain());
					if (s.length() > 0) {
						s += ").append(";
					} else {
						s = "        return new StringBuffer(\"\").append(";
					}
					if (tsc.getPrefix().length() > 0) {
						s += "\"" + tsc.getPrefix() + "\").append(";
					}
					if (IsReference(tsc.getColumn().getDomain())) {
						s += "this.get" + n + "().toString()";
					} else {
						String accessor = (t.equals("boolean") ? "is" : "get") + n + "()";
						s += "this." + accessor;
					}
					if (tsc.getSuffix().length() > 0) {
						s += ").append(\"" + tsc.getSuffix() + "\"";
					}
				}
				if (s.length() > 0) {
					s += ").toString();";
				}
				zeilen.addElement(s);
				zeilen.addElement("    }");
				zeilen.addElement("");
			}
			// Implementierung des Interfaces Attributed.
			zeilen.addElement("");
			zeilen.addElement("    /* Implementierung des Interfaces Attributed. */");
			zeilen.addElement("");
			zeilen.addElement("    public Object get(int id) throws IllegalArgumentException {");
			zeilen.addElement("        switch(id) {");
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
				zeilen.addElement("        case ID_" + tsm.getName().toUpperCase() + ":");
				String n = tsm.getName();
				String t = GetType(tsm.getDomain());
				String g = (t.equals("boolean") ? "is" : "get") + n + "()";
				zeilen.addElement("            return "
						+ (IsReference(tsm.getDomain()) ? "this." + g : "new " + GetWrapper(t) + "(this." + g + ")")
						+ ";");
			}
			zeilen.addElement("        }");
			zeilen.addElement("        throw new IllegalArgumentException(\"Class " + tm.getName()
					+ " hasn't an attribute no. \" + id + \" (get)!\");");
			zeilen.addElement("    }");
			zeilen.addElement("");
			zeilen.addElement("    public void set(int id, Object value) throws "
					+ "ClassCastException, IllegalArgumentException {");
			zeilen.addElement("        switch(id) {");
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
				zeilen.addElement("        case ID_" + tsm.getName().toUpperCase() + ":");
				String n = tsm.getName();
				String t = GetType(tsm.getDomain());
				String s = "this.set"
						+ n
						+ "("
						+ (IsReference(tsm.getDomain()) ? "(" + t + ") value" : "((" + GetWrapper(t) + ") value)." + t
								+ "Value()") + ")";
				zeilen.addElement("            " + s + ";");
				zeilen.addElement("            return;");
			}
			zeilen.addElement("        }");
			zeilen.addElement("        throw new IllegalArgumentException(\"Class " + tm.getName()
					+ " hasn't an attribute no. \" + id + \" (set)!\");");
			zeilen.addElement("    }");
			zeilen.addElement("");
			// Implementierung des Interfaces Deactivatable.
			if (deaktivierbar) {
				zeilen.addElement("");
				zeilen.addElement("    /* Implementierung des Interfaces Deactivatable. */");
				zeilen.addElement("");
				zeilen.addElement("    public ColumnRecord getStatusColumn() {");
				zeilen.addElement("        return StatusColumn;");
				zeilen.addElement("    }");
				zeilen.addElement("");
				zeilen.addElement("    public Object getDeactivatedValue() {");
				zeilen.addElement("        return Integer.getInteger(\"" + "application.Status.Deactivated\", 1);");
				zeilen.addElement("    }");
				zeilen.addElement("");
				zeilen.addElement("    public Object getActivatedValue() {");
				zeilen.addElement("        return Integer.getInteger(\"" + "application.Status.Activated\", 0);");
				zeilen.addElement("    }");
				zeilen.addElement("");
				String rcn = "Geloescht";
				for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
					TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
					if (tsm.isRemovedStateField()) {
						rcn = tsm.getName();
						break;
					}
				}
				zeilen.addElement("    public boolean isRemoved() {");
				zeilen.addElement("        return is" + rcn + "();");
				zeilen.addElement("    }");
			}
			// Implementierung des Interfaces Editable.
			zeilen.addElement("");
			zeilen.addElement("    /* Implementierung des Interfaces Editable. */");
			zeilen.addElement("");
			zeilen.addElement("    public PersistenceDescriptor getPersistenceDescriptor() {");
			zeilen.addElement("        return PD;");
			zeilen.addElement("    }");
			zeilen.addElement("");
			zeilen.addElement("    public EditorDescriptorList getEditorDescriptorList() {");
			zeilen.addElement("        DefaultComponentFactory dcf = " + "DefaultComponentFactory.INSTANZ;");
			zeilen.addElement("        DefaultEditorDescriptorList dedl = " + "new DefaultEditorDescriptorList();");
			zeilen.addElement("        DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;");
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
				if (tsm.isEditormember()) {
					String n = tsm.getName();
					zeilen.addElement("        dedl.addElement(new DefaultEditorDescriptor("
							+ tsm.getPanel().getPanelNumber() + ", this, ID_" + n.toUpperCase() + ", dlf, dcf, \""
							+ tsm.getLabelText() + "\", '"
							+ (tsm.getMnemonic().length() > 0 ? tsm.getMnemonic().charAt(0) : "\0") + "', null, \""
							+ tsm.getToolTipText() + "\"));");
				}
			}
			zeilen.addElement("        return dedl;");
			zeilen.addElement("    }");
			zeilen.addElement("");
			zeilen.addElement("    public Object createObject() {");
			zeilen.addElement("        return new " + tm.getName() + "();");
			zeilen.addElement("    }");
			zeilen.addElement("");
			zeilen.addElement("    public Object createObject(Object blueprint) throws " + "ClassCastException {");
			zeilen.addElement("        if (!(blueprint instanceof " + tm.getName() + ")) {");
			zeilen.addElement("            throw new ClassCastException(\"Instance of " + tm.getName()
					+ " required!\");");
			zeilen.addElement("        }");
			zeilen.addElement("        " + tm.getName() + " newone = (" + tm.getName() + ") this.createObject();");
			zeilen.addElement("        " + tm.getName() + " bp = (" + tm.getName() + ") blueprint;");
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
				String id = "ID_" + tsm.getName().toUpperCase();
				zeilen.addElement("        newone.set(" + id + ", bp.get(" + id + "));");
			}
			zeilen.addElement("        return newone;");
			zeilen.addElement("    }");
			zeilen.addElement("");
			SortedVector svi = new SortedVector();
			if (tabbed) {
				for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
					TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
					if (!svi.contains(new Integer(tsm.getPanel().getPanelNumber()))) {
						svi.addElement(new Integer(tsm.getPanel().getPanelNumber()));
					}
				}
				zeilen.addElement("    public TabbedPaneFactory getTabbedPaneFactory() {");
				String std = "        return new DefaultTabbedPaneFactory(new TabDescriptor[] " + "{";
				for (int i = 0, len = svi.size(); i < len; i++) {
					int id = ((Integer) svi.elementAt(i)).intValue() + 1;
					if (i > 0) {
						std += ", ";
					}
					std += "new DefaultTabDescriptor(\"" + id + ".Reiter\", '" + id + "', null)";
				}
				std += "});";
				zeilen.addElement(std);
				zeilen.addElement("    }");
				zeilen.addElement("");
			}
		} else {
			zeilen.addElement("    /* Accessoren & Mutatoren. */");
			zeilen.addElement("");
			for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
				String n = tsm.getName();
				String t = GetType(tsm.getDomain());
				String sf = "" + n.toLowerCase().charAt(0) + (n.length() == 1 ? "0" : "");
				zeilen.addElement("    /** @return Der Wert der Eigenschaft " + n + ". */");
				zeilen.addElement("    public " + t + (t.equals("boolean") ? " is" : " get") + n + "() {");
				zeilen.addElement("        " + GetWrapper(t) + " " + n.toLowerCase() + " = (" + GetWrapper(t)
						+ ") this.get(\"" + n + "\");");
				if (IsNumber(tsm.getDomain())) {
					zeilen.addElement("        " + t + " " + sf + " = " + GetInitializerValue(tsm.getDomain()) + ";");
					zeilen.addElement("        if (" + n.toLowerCase() + " != null) {");
					zeilen.addElement("            " + sf + " = ((" + (t.equals("boolean") ? "Boolean" : "Number")
							+ ")" + " " + n.toLowerCase() + ")." + t + "Value();");
					zeilen.addElement("        }");
					zeilen.addElement("        return " + sf + ";");
				} else {
					if (tsm.getDomain().getName().equalsIgnoreCase("PDate")
							|| tsm.getDomain().getName().equalsIgnoreCase("PTime")
							|| tsm.getDomain().getName().equalsIgnoreCase("PTimestamp")
							|| tsm.getDomain().getName().equalsIgnoreCase("LongPTimestamp")) {
						this.imports.put("corent.dates.*", "");
					}
					zeilen.addElement("        return " + n.toLowerCase() + ";");
				}
				zeilen.addElement("    }");
				zeilen.addElement("");
				zeilen.addElement("    /**");
				zeilen.addElement("     * Setzt den &uuml;bergebenen Wert als neuen Wert "
						+ "f&uuml;r die Eigenschaft " + n + ".");
				zeilen.addElement("     *");
				zeilen
						.addElement("     * @param " + n.toLowerCase() + " Der neue Wert der " + "Eigenschaft " + n
								+ ".");
				zeilen.addElement("     */");
				zeilen.addElement("    public void set" + n + "(" + t + " " + n.toLowerCase() + ") {");
				if (IsReference(tsm.getDomain()) && !t.equals("int")) {
					zeilen.addElement("        if (" + n.toLowerCase() + " == null) {");
					zeilen.addElement("            " + n.toLowerCase() + " = " + GetInitializer(tsm.getDomain()) + ";");
					zeilen.addElement("        }");
				}
				zeilen.addElement("        this.set(\"" + n + "\", " + n.toLowerCase() + ");");
				zeilen.addElement("    }");
				if (i < len - 1) {
					zeilen.addElement("");
				}
			}
			for (int i = 0, len = tm.getNReferenzModelCount(); i < len; i++) {
				if (i == 0) {
					this.imports.put("java.util.*", "");
				}
				NReferenzModel nrm = tm.getNReferenzModelAt(i);
				String n = nrm.getTabellenspalte().getTabelle().getName();
				String n0 = nrm.getTabellenspalte().getRelation().getReferenced().getName();
				zeilen.addElement("");
				zeilen.addElement("    /** @return Referenz auf die Liste " + n + ". */");
				zeilen.addElement("    public Vector get" + n + "() {");
				zeilen.addElement("        Vector " + n.toLowerCase() + " = (Vector) this.get(\"" + n + n0 + "\");");
				zeilen.addElement("        return " + n.toLowerCase() + ";");
				zeilen.addElement("    }");
			}
			zeilen.addElement("");
			zeilen.addElement("");
			zeilen.addElement("    /* Implementierung der abstrakten Methoden der Superklasse. " + "*/");
			zeilen.addElement("");
			zeilen.addElement("    public Object createObject() {");
			zeilen.addElement("        return new " + tm.getName() + (tm.isDynamicCode() ? "Udschebti" : "")
					+ "(this.adf);");
			zeilen.addElement("    }");
			zeilen.addElement("");
			if (tm.isDynamicCode()) {
				zeilenSkeleton.addElement("    /* Implementierung der abstrakten Methoden der " + "Superklasse. */");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public Object createObject() {");
				zeilenSkeleton.addElement("        return new " + tm.getName() + "(this.adf);");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
			}
			if (deaktivierbar) {
				zeilen.addElement("");
				zeilen.addElement("    /* Vorbereitende Implementierung des Interfaces" + " Deactivatable. */");
				zeilen.addElement("");
				String rcn = "Geloescht";
				for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
					TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
					if (tsm.isRemovedStateField()) {
						rcn = tsm.getName();
						break;
					}
				}
				zeilen.addElement("    public boolean isRemoved() {");
				zeilen.addElement("        return this.is" + rcn + "();");
				zeilen.addElement("    }");
				zeilen.addElement("");
			}
			/*
			 * SortedVector svi = new SortedVector(); if (tabbed) {
			 * zeilen.addElement("");
			 * zeilen.addElement("    /* Implementierung des Interfaces Tabbed. * /"
			 * ); zeilen.addElement(""); int panel = 0; for (int i = 0, len =
			 * tm.getTabellenspaltenCount(); i < len; i++) {
			 * TabellenspaltenModel tsm = tm.getTabellenspalteAt(i); if
			 * (!svi.contains(new Integer(tsm.getPanelNumber()))) {
			 * svi.addElement(new Integer(tsm.getPanelNumber())); if (panel <
			 * tsm.getPanelNumber()) { panel = tsm.getPanelNumber(); } } } for
			 * (int i = 0, len = tm.getNReferenzModelCount(); i < len; i++) {
			 * NReferenzModel nrm = tm.getNReferenzModelAt(i); panel++; if
			 * (!svi.contains(new Integer(panel))) { svi.addElement(new
			 * Integer(panel)); } }zeilen.addElement(
			 * "    public TabbedPaneFactory getTabbedPaneFactory() {"); String
			 * std =
			 * "        return new DefaultTabbedPaneFactory(new TabDescriptor[] "
			 * + "{"; for (int i = 0, len = svi.size(); i < len; i++) { int id =
			 * ((Integer) svi.elementAt(i)).intValue() + 1; if (i > 0) { std +=
			 * ", "; } String t = tm.getTabTitle(id); String ttt =
			 * tm.getTabToolTipText(id); std += "new DefaultTabDescriptor(\"" +
			 * (t != null ? t : "" + id + ".Reiter" )+ "\", '" + (t != null ?
			 * tm.getTabMnemonic(id) : "" + id) + "', " + (ttt != null ? "\"" +
			 * ttt + "\"" : "null") + ")"; } std += "});";
			 * zeilen.addElement(std); zeilen.addElement("    }");
			 * zeilen.addElement(""); }
			 */
		}
		zeilen.addElement("}");
		if (tm.isDynamicCode()) {
			if (columnviewable) {
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    /* Implementierung des Interfaces " + "ColumnViewable. */");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public int getColumnCount() {");
				zeilenSkeleton.addElement("        return 1;");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public Class getColumnclass(int col) {");
				zeilenSkeleton.addElement("        return super.getColumnclass(col);");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public String[] getColumnnames() {");
				zeilenSkeleton.addElement("        return new String[] {\"Spaltenname\"};");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public Object getValueAt(int col) {");
				zeilenSkeleton.addElement("        switch (col) {");
				zeilenSkeleton.addElement("        case 0:");
				zeilenSkeleton.addElement("            return this.toString();");
				zeilenSkeleton.addElement("        }");
				zeilenSkeleton.addElement("        return \"-/-\";");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public TableCellRenderer getCellRenderer(int " + "col) {");
				zeilenSkeleton.addElement("        return null;");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
			}
			if (cached) {
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    /* Implementierung des Interfaces " + "EditorDjinnMaster. */");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public boolean doAfterCleanUp(Hashtable<String, "
						+ "java.awt.Component> comps) {");
				zeilenSkeleton.addElement("        return true;");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public void doAfterDjinnSummoned(Hashtable"
						+ "<String, java.awt.Component> comps,");
				zeilenSkeleton.addElement("            EditorDjinnMode mode) {");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public boolean doAfterTransferValues() {");
				zeilenSkeleton.addElement("        Cache.clear();");
				zeilenSkeleton.addElement("        return true;");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public boolean doBeforeDelete(Hashtable<String, "
						+ "java.awt.Component> comps) {");
				zeilenSkeleton.addElement("        return true;");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public boolean doBeforeTransferValues("
						+ "Hashtable<String, java.awt.Component> comps) {");
				zeilenSkeleton.addElement("        return true;");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public boolean isDeleteConfirmSuppressed() {");
				zeilenSkeleton.addElement("        return false;");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public boolean isDiscardConfirmSuppressed() {");
				zeilenSkeleton.addElement("        return false;");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
			}
			if (nrCount > 0) {
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    /* Implementierung des Interfaces ListOwner. */");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public Object createElement(int nr) {");
				zeilenSkeleton.addElement("        switch (nr) {");
				for (NReferenceModel nr : tm.getNReferences()) {
					ColumnModel c = nr.getColumn();
					String tmnr = c.getTable().getName();
					String tmn = tm.getName();
					zeilenSkeleton.addElement("        case " + nr.getId() + ":");
					zeilenSkeleton.addElement("            " + tmnr + " r" + nr.getId() + " = new " + tmnr
							+ "(this.adf);");
					zeilenSkeleton.addElement("            r" + nr.getId() + ".set" + tmn + "(this.get" + tmn + "());");
					zeilenSkeleton.addElement("            return r" + nr.getId() + ";");
				}
				zeilenSkeleton.addElement("        }");
				zeilenSkeleton.addElement("        return null;");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public java.util.List getList(int nr) {");
				zeilenSkeleton.addElement("        switch (nr) {");
				for (NReferenceModel nr : tm.getNReferences()) {
					ColumnModel c = nr.getColumn();
					String tmnr = c.getTable().getName();
					zeilenSkeleton.addElement("        case " + nr.getId() + ":");
					zeilenSkeleton.addElement("            return this.get" + tmnr + "();");
				}
				zeilenSkeleton.addElement("        }");
				zeilenSkeleton.addElement("        return null;");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
			}
			zeilenSkeleton.addElement("}");
		}
		/*
		 * // und jetzt wird's geschrieben ... System.out.println("writing " +
		 * out + tm.getName() + ".java"); FileWriter fw = new FileWriter(out +
		 * tm.getName() + ".java", false); BufferedWriter writer = new
		 * BufferedWriter(fw); for (int i = 0, len = zeilen.size(); i < len;
		 * i++) { writer.write((String) zeilen.elementAt(i) + "\n"); }
		 * writer.flush(); writer.close(); fw.close();
		 */
		// Importklauseln eintragen.
		int at = -1;
		for (int i = 0, len = zeilen.size(); i < len; i++) {
			if (zeilen.elementAt(i).equals("$IMPORTS")) {
				at = i;
			}
		}
		SortedVector sv = new SortedVector();
		for (Iterator it = this.imports.keySet().iterator(); it.hasNext();) {
			String k = (String) it.next();
			sv.addElement(k);
		}
		zeilen.removeElementAt(at);
		String prefix0 = "";
		if (inherited) {
			sv.addElement(packagename + ".*");
		}
		for (int i = 0, len = sv.size(); i < len; i++) {
			String s = (String) sv.elementAt(i);
			int point = s.indexOf(".") - 1;
			String prefix = s.substring(0, (point < 0 ? s.length() : point));
			if (!prefix0.equals(prefix) && !prefix0.equals("")) {
				zeilen.insertElementAt("", at++);
			}
			zeilen.insertElementAt("import " + s + ";", at++);
			prefix0 = prefix;
		}
		if (tm.isDynamicCode()) {
			if (inherited) {
				sv.removeElement(packagename + ".*");
			}
			at = -1;
			prefix0 = "";
			for (int i = 0, len = zeilenSkeleton.size(); i < len; i++) {
				if (zeilenSkeleton.elementAt(i).equals("$IMPORTS")) {
					at = i;
				}
			}
			zeilenSkeleton.removeElementAt(at);
			sv.addElement(packagename + ".udschebtis.*");
			for (int i = 0, len = sv.size(); i < len; i++) {
				String s = (String) sv.elementAt(i);
				int point = s.indexOf(".") - 1;
				String prefix = s.substring(0, (point < 0 ? s.length() : point));
				if (!prefix0.equals(prefix) && !prefix0.equals("")) {
					zeilenSkeleton.insertElementAt("", at++);
				}
				zeilenSkeleton.insertElementAt("import " + s + ";", at++);
				prefix0 = prefix;
			}
		}
		// und jetzt wird's geschrieben ...
		out = dm.getCodePfad().replace('\\', '/').replace("~", System.getProperty("user.home"));
		if (!out.endsWith("/")) {
			out += "/";
		}
		this.writeResourceFile(out + tm.getName() + ".resource", res);
		out += packagename.replace('.', '/');
		if (!out.endsWith("/")) {
			out += "/";
		}
		String out0 = out;
		if (tm.isDynamicCode()) {
			// ...
			out0 += "udschebtis/";
			new File(out0).mkdirs();
			new File(out + "db/").mkdirs();
		}
		System.out.println("writing " + out0 + tm.getName() + (tm.isDynamicCode() ? "Udschebti" : "") + ".java");
		FileWriter fw = new FileWriter(out0 + tm.getName() + (tm.isDynamicCode() ? "Udschebti" : "") + ".java", false);
		BufferedWriter writer = new BufferedWriter(fw);
		for (int i = 0, len = zeilen.size(); i < len; i++) {
			writer.write((String) zeilen.elementAt(i) + "\n");
		}
		writer.flush();
		writer.close();
		fw.close();
		if (tm.isDynamicCode() && !tm.isFirstGenerationDone()) {
			System.out.println("writing " + out + tm.getName() + ".java");
			fw = new FileWriter(out + tm.getName() + ".java", false);
			writer = new BufferedWriter(fw);
			for (int i = 0, len = zeilenSkeleton.size(); i < len; i++) {
				writer.write((String) zeilenSkeleton.elementAt(i) + "\n");
			}
			writer.flush();
			writer.close();
			fw.close();
			System.out.println("writing " + out + "db/DBFactory" + tm.getName() + ".java");
			fw = new FileWriter(out + "db/DBFactory" + tm.getName() + ".java", false);
			writer = new BufferedWriter(fw);
			for (int i = 0, len = zeilenDBFactory.size(); i < len; i++) {
				writer.write((String) zeilenDBFactory.elementAt(i) + "\n");
			}
			writer.flush();
			writer.close();
			fw.close();
		}
	}

	/* Hilfsmethoden. */

	/**
	 * Generiert aus dem Datentypen der Dom&auml;ne einen Java-Datentyp.
	 * 
	 * @param d
	 *            Das DomainModel zu dem der Java-Datentyp generiert werden
	 *            soll.
	 * @return Der Name des Java-Datentypen bzw. 'UNKNOWN', wenn kein Datentyp
	 *         zur Domain gebildet werden kann.
	 * 
	 * @changed OLI 09.09.2009 - Umstellung auf Nutzung der entsprechenden
	 *          gengen-Methode.
	 * @changed OLI 05.10.2009 - Einbau einer M&ouml;glichkeit den Packagenamen
	 *          f&uuml;r die Zeitstempelklassen &uuml;ber die Property
	 *          <I>archimedes.scheme.DefaultCodeFactory.ts.package</I> zu
	 *          konfigurieren. Die Voreinstellung (bei undefinierter Property)
	 *          ist "corentx.dates".
	 */
	public static String GetType(DomainModel d) {
		return gengen.util.Converter.toJavaType(d.getDataType(), d.getName(), Utl.GetProperty(
				"archimedes.scheme.DefaultCodeFactory.ts.package", "corentx.dates"));
		/*
		 * int type = d.getDatatype(); if
		 * (d.getName().equalsIgnoreCase("Boolean")) { type = Types.BOOLEAN; }
		 * if (d.getName().equalsIgnoreCase("PDate") ||
		 * d.getName().equalsIgnoreCase("PTime") ||
		 * d.getName().equalsIgnoreCase("PTimestamp") ||
		 * d.getName().equalsIgnoreCase("LongPTimestamp")) { return
		 * "corent.dates." + d.getName(); } String s = "UNKNOWN"; switch (type)
		 * { case Types.BIGINT: s = "long"; break; case Types.BIT: case
		 * Types.BOOLEAN: s = "boolean"; break; case Types.BINARY: case
		 * Types.LONGVARBINARY: case Types.VARBINARY: s = "byte[]"; break; case
		 * Types.CHAR: case Types.LONGVARCHAR: case Types.VARCHAR: s = "String";
		 * break; case Types.DATE: s = "java.sql.Date"; break; case
		 * Types.DECIMAL: case Types.DOUBLE: case Types.FLOAT: case
		 * Types.NUMERIC: s = "double"; break; case Types.INTEGER: case
		 * Types.SMALLINT: case Types.TINYINT: s = "int"; break; case
		 * Types.REAL: s = "float"; break; case Types.TIME: s = "java.sql.Time";
		 * break; case Types.TIMESTAMP: s = "java.sql.Timestamp"; break; }
		 * return s;
		 */
	}

	/**
	 * Liefert einen Standardwert im Stringformat f&uuml;r die Initialisierung
	 * eines zum DomainModel passenden Java-Datentypen.
	 * 
	 * @param d
	 *            Das DomainModel, zu dem der Initializer generiert werden soll.
	 * @return Der Intialwert im Stringformat des Java-Datentypen bzw.
	 *         'UNKNOWN', wenn kein Datentyp zur Domain gefunden werden kann.
	 */
	public static String GetInitializer(DomainModel d) {
		int type = d.getDataType();
		if (d.getName().equalsIgnoreCase("Boolean")) {
			type = Types.BOOLEAN;
		}
		if (d.getName().equalsIgnoreCase("PDate")) {
			return "PDate.UNDEFINIERT";
		} else if (d.getName().equalsIgnoreCase("PTime")) {
			return "PTime.UNDEFINIERT";
		} else if (d.getName().equalsIgnoreCase("PTimestamp")) {
			return "PTimestamp.NULL";
		} else if (d.getName().equalsIgnoreCase("LongPTimestamp")) {
			return "LongPTimestamp.NULL";
		}
		String s = "UNKNOWN";
		switch (type) {
		case Types.BIGINT:
			s = "0";
			break;
		case Types.BIT:
		case Types.BOOLEAN:
			s = "false";
			break;
		case Types.BINARY:
		case Types.LONGVARBINARY:
		case Types.VARBINARY:
			s = "null";
			break;
		case Types.CHAR:
		case Types.LONGVARCHAR:
		case Types.VARCHAR:
			s = "\"\"";
			break;
		case Types.DATE:
			s = "null";
			break;
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.NUMERIC:
			s = "0.0d";
			break;
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.TINYINT:
			s = "0";
			break;
		case Types.REAL:
			s = "0.0";
			break;
		case Types.TIME:
			s = "null";
			break;
		case Types.TIMESTAMP:
			s = "null";
			break;
		}
		return s;
	}

	/**
	 * Liefert einen Standardwert f&uuml;r die Initialisierung eines zum
	 * DomainModel passenden Java-Datentypen.
	 * 
	 * @param d
	 *            Das DomainModel, zu dem der Initializer generiert werden soll.
	 * @return Der Intialwert des Java-Datentypen bzw. <TT>null</TT>, wenn kein
	 *         Datentyp zur Domain gefunden werden kann.
	 */
	public static Object GetInitializerValue(DomainModel d) {
		int type = d.getDataType();
		if (d.getName().equalsIgnoreCase("Boolean")) {
			type = Types.BOOLEAN;
		}
		if (d.getName().equalsIgnoreCase("PDate")) {
			return PDate.UNDEFINIERT;
		} else if (d.getName().equalsIgnoreCase("PTime")) {
			return PTime.UNDEFINIERT;
		} else if (d.getName().equalsIgnoreCase("PTimestamp")) {
			return PTimestamp.NULL;
		} else if (d.getName().equalsIgnoreCase("LongPTimestamp")) {
			return LongPTimestamp.NULL;
		}
		Object s = null;
		switch (type) {
		case Types.BIGINT:
			s = new Long(0);
			break;
		case Types.BIT:
		case Types.BOOLEAN:
			s = Boolean.FALSE;
			break;
		case Types.BINARY:
		case Types.LONGVARBINARY:
		case Types.VARBINARY:
			s = null;
			break;
		case Types.CHAR:
		case Types.LONGVARCHAR:
		case Types.VARCHAR:
			s = "";
			break;
		case Types.DATE:
			s = null;
			break;
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.NUMERIC:
			s = new Double(0.0d);
			break;
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.TINYINT:
			s = new Integer(0);
			break;
		case Types.REAL:
			s = new Float(0.0);
			break;
		case Types.TIME:
			s = null;
			break;
		case Types.TIMESTAMP:
			s = null;
			break;
		}
		return s;
	}

	/**
	 * Ermittelt die Wrapper-Klasse zu einem elementaren Java-Datentyp.
	 * 
	 * @param type
	 *            Der Name des elementaren Java-Datentyps.
	 * @return Der Name der zum elementaren Java-Datentyp bzw. der
	 *         &uuml;bergebene Typname, falls zu dem Typnamen kein Wrapper
	 *         gefunden werden kann.
	 */
	public static String GetWrapper(String type) {
		if (type.equals("boolean")) {
			type = "Boolean";
		} else if (type.equals("byte")) {
			type = "Byte";
		} else if (type.equals("char")) {
			type = "Char";
		} else if (type.equals("double")) {
			type = "Double";
		} else if (type.equals("float")) {
			type = "Float";
		} else if (type.equals("int")) {
			type = "Integer";
		} else if (type.equals("long")) {
			type = "Long";
		} else if (type.equals("short")) {
			type = "Short";
		}
		return type;
	}

	/**
	 * Pr&uuml;ft, ob der der &uuml;bergebenen Domain zugeordnete Java-Datentyp
	 * ein Referenztyp ist.
	 * 
	 * @param d
	 *            Die zu &uuml;berpr&uuml;fende Domain.
	 * @return <TT>true</TT>, falls der der Domain zugeordnete Java-Datentyp ein
	 *         Referenztyp ist.
	 */
	public static boolean IsReference(DomainModel d) {
		boolean erg = false;
		int type = d.getDataType();
		if ((d.getName().equalsIgnoreCase("PDate")) || (d.getName().equalsIgnoreCase("PTime"))
				|| (d.getName().equalsIgnoreCase("PTimestamp")) || (d.getName().equalsIgnoreCase("LongPTimestamp"))) {
			return true;
		}
		switch (type) {
		case Types.BINARY:
		case Types.CHAR:
		case Types.DATE:
		case Types.LONGVARBINARY:
		case Types.LONGVARCHAR:
		case Types.TIME:
		case Types.TIMESTAMP:
		case Types.VARBINARY:
		case Types.VARCHAR:
			erg = true;
			break;
		}
		return erg;
	}

	/**
	 * Pr&uuml;ft, ob der der &uuml;bergebenen Domain zugeordnete Java-Datentyp
	 * ein Zahlentyp ist.
	 * 
	 * @param d
	 *            Die zu &uuml;berpr&uuml;fende Domain.
	 * @return <TT>true</TT>, falls der der Domain zugeordnete Java-Datentyp ein
	 *         Referenztyp ist.
	 */
	public static boolean IsNumber(DomainModel d) {
		if ((d.getName().equalsIgnoreCase("PDate")) || (d.getName().equalsIgnoreCase("PTime"))
				|| (d.getName().equalsIgnoreCase("PTimestamp")) || (d.getName().equalsIgnoreCase("LongPTimestamp"))) {
			return false;
		}
		switch (d.getDataType()) {
		case Types.BIGINT:
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.INTEGER:
		case Types.NUMERIC:
		case Types.REAL:
		case Types.SMALLINT:
		case Types.TINYINT:
			return true;
		}
		return false;
	}

	protected String getBeschreibung(TabellenspaltenModel tsm) {
		DiagrammModel dm = tsm.getTabelle().getDiagramm();
		String s = tsm.getComment();
		if (s.length() == 0) {
			Vector vector = dm.getDefaultComments();
			for (int i = 0, len = vector.size(); i < len; i++) {
				DefaultCommentModel dcm = (DefaultCommentModel) vector.elementAt(i);
				String muster = dcm.getPattern();
				if (muster.equals("*")) {
					return dcm.getDefaultComment();
				}
				if (muster.startsWith("*") && (tsm.getName().endsWith(muster.substring(1, muster.length())))) {
					return dcm.getDefaultComment();
				}
				if (muster.endsWith("*") && (tsm.getName().startsWith(muster.substring(0, muster.length() - 1)))) {
					return dcm.getDefaultComment();
				}
				if (muster.equals(tsm.getName())) {
					return dcm.getDefaultComment();
				}
			}
			s = "-/-";
		}
		return s;
	}

	protected String makeDirAndPackage(TabellenModel tm, String basepackage, String basepackagename) {
		String cp = tm.getCodeVerzeichnis();
		String packagename = "";
		basepackage = basepackage.replace('\\', '/');
		if (!basepackage.endsWith("/")) {
			basepackage += "/";
		}
		if (cp.length() == 0) {
			return "";
		}
		cp = cp.replace('\\', '/');
		while (cp.indexOf("/") >= 0) {
			String s = cp.substring(0, cp.indexOf("/"));
			cp = cp.substring(cp.indexOf("/") + 1, cp.length());
			if (packagename.length() > 0) {
				packagename += ".";
			}
			packagename += s;
		}
		if (packagename.length() > 0) {
			packagename += ".";
		}
		packagename += cp;
		packagename = packagename.replace("$", basepackagename);
		new File(basepackage + packagename.replace('.', '/')).mkdirs();
		return packagename;
	}

	/**
	 * Schreibt den &uuml;bergebenen Vector in eine Ressourcendatei.
	 * 
	 * @param fn
	 *            Der Name, unter dem die Datei abgelegt werden soll.
	 * @param res
	 *            Der Vector mit den Eintr&auml;gen f&uuml;r die
	 *            Ressourcendatei.
	 * @throws IOException
	 *             falls beim Schreiben der Datei ein Fehler auftritt.
	 * 
	 * @changed OLI 28.03.2008 - Hinzugef&uuml;gt.
	 * 
	 */
	protected void writeResourceFile(String fn, Vector res) throws IOException {
		System.out.println("writing " + fn);
		FileWriter fw = new FileWriter(fn, false);
		BufferedWriter writer = new BufferedWriter(fw);
		for (int i = 0, len = res.size(); i < len; i++) {
			writer.write(res.elementAt(i) + "\n");
		}
		writer.flush();
		writer.close();
		fw.close();
	}

	/**
	 * Generiert alle notwendigen (Standard-)Ressourcen f&uuml;r die angegebene
	 * Tabelle.
	 * 
	 * @param res
	 *            Der Vector, an den die erzeugten Ressourcen angeh&auml;ngt
	 *            werden sollen.
	 * @param classname
	 *            Der Name, den die Klasse in der Applikation haben wird.
	 * @param tm
	 *            Das TabellenModel, zu dem die Ressourcen generiert werden
	 *            sollen.
	 * 
	 * @changed OLI 28.03.2008 - Hinzugef&uuml;gt.
	 * 
	 */
	protected void createResourcesFromTable(Vector res, String classname, TabellenModel tm) {
		DomainModel dm = null;
		String prefix = null;
		TabellenspaltenModel tsm = null;
		Vector v = null;
		res.addElement("archimedes.app.ArchimedesComponentFactory.mls.dialog.selection.title."
				+ classname
				+ "="
				+ System.getProperty("archimedes.scheme.DefaultCodeFactory." + "selection.title", "Auswahl $TABLENAME")
						.replace("$TABLENAME", tm.getName()));
		res
				.addElement("corent.djinn.InternalFrameEditorDjinn."
						+ classname
						+ ".title="
						+ System.getProperty("archimedes.scheme.DefaultCodeFactory.edit.dialog.title",
								"&Auml;ndern $TABLENAME").replace("$TABLENAME", tm.getName()));
		res.addElement("corent.djinn.InternalFrameSelectionEditorDjinnDBF." + classname + ".title=Auswahl&nbsp;"
				+ tm.getName());
		res.addElement("corent.djinn.DefaultEditorDjinnPanel.information.fields.text." + classname
				+ "=Die Pflichtfelder f&uuml;r " + tm.getName() + " sind: " + "$FIELDS");
		res.addElement("corent.djinn.DefaultEditorDjinnPanel.information.unique.text." + classname
				+ "=Es gibt bereits einen Datensatz mit diesen Merkmalen: $UNIQUE");
		v = tm.getAuswahlMembers();
		for (int i = 0, len = v.size(); i < len; i++) {
			tsm = (Tabellenspalte) ((SelectionMember) v.elementAt(i)).getColumn();
			res.addElement("archimedes.app.DefaultArchimedesDescriptorFactory." + classname + ".table.view.header."
					+ tsm.getFullName() + "="
					+ (tsm.isEditormember() ? StrUtil.ToHTML(tsm.getLabelText()) : tsm.getName()));
		}
		for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
			tsm = tm.getTabellenspalteAt(i);
			dm = tsm.getDomain();
			prefix = "TextField";
			if (dm.getName().equalsIgnoreCase("Boolean")) {
				prefix = "CheckBox";
			} else if (tsm.getRelation() != null) {
				prefix = "ComboBox";
			}
			if (tsm.isEditormember()) {
				res.addElement("corent.djinn.DefaultEditorDjinnPanel.information.fields.name.for." + tsm.getName()
						+ "." + classname + "=" + StrUtil.ToHTML(tsm.getLabelText()));
				res.addElement(classname /* + "." */+ prefix + tsm.getName() + ".label.text="
						+ StrUtil.ToHTML(tsm.getLabelText()));
				res.addElement(classname /* + "." */+ prefix + tsm.getName() + ".label.mnemonic="
						+ StrUtil.ToHTML(tsm.getMnemonic()));
				res.addElement(classname /* + "." */+ prefix + tsm.getName() + ".label.tooltiptext="
						+ StrUtil.ToHTML(tsm.getToolTipText()));
			} else {
				res.addElement("corent.djinn.DefaultEditorDjinnPanel.information.fields.name.for." + tsm.getName()
						+ "." + classname + "=" + tsm.getName());
			}
		}
	}

	/**
	 * @changed OLI 31.03.2016 - Added.
	 */
	@Override
	public void setGUIBundle(GUIBundle guiBundle) {
		this.guiBundle = guiBundle;
	}

	/**
	 * @changed OLI 18.05.2016 - Added.
	 */
	@Override
	public ModelChecker[] getModelCheckers() {
		return new ModelChecker[0];
	}

	/**
	 * @changed OLI 24.05.2016 - Added.
	 */
	@Override
	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	/**
	 * @changed OLI 10.06.2016 - Added.
	 */
	@Override
	public String[] getResourceBundleNames() {
		return new String[] { "archimedes" };
	}

	/**
	 * @changed OLI 15.06.2016 - Added.
	 */
	@Override
	public void setModelCheckerMessageListFrameListeners(ModelCheckerMessageListFrameListener... l) {
	}

	/**
	 * @changed OLI 07.12.2016 - Added.
	 */
	@Override
	public void addCodeFactoryListener(CodeFactoryListener cfe) {
	}

	/**
	 * @changed OLI 07.12.2016 - Added.
	 */
	@Override
	public void removeCodeFactoryListener(CodeFactoryListener cfe) {
	}

	/**
	 * @changed OLI 08.12.2016 - Added.
	 */
	@Override
	public GUIBundle getGUIBundle() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 18.04.2017 - Added.
	 */
	@Override
	public String getName() {
		return "DefaultCodeFactory";
	}

	/**
	 * @changed OLI 18.04.2017 - Added.
	 */
	@Override
	public String getVersion() {
		return Archimedes.GetVersion();
	}

}