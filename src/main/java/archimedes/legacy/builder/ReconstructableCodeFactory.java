/*
 * ReconstructableCodeFactory.java
 *
 * 21.06.2007
 *
 * (c) by ollie
 *
 * Nutzung und Aenderung durch die MediSys GmbH mit freundlicher Genehmigung des Autors.
 *
 */

package archimedes.legacy.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.NReferenzModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.legacy.scheme.DefaultCodeFactory;
import archimedes.legacy.scheme.TSMSortContainer;
import archimedes.model.ColumnModel;
import archimedes.model.NReferenceModel;
import archimedes.model.StereotypeModel;
import archimedes.model.ToStringContainerModel;
import corent.base.SortedVector;
import corent.base.StrUtil;
import corent.dates.PDate;

/**
 * Diese Variante der CodeFactory dient der Generierung von
 * Reconstructable-Objekten und Klassen.
 * 
 * @author ollie
 *         <P>
 * 
 * @changed OLI 16.08.2008 - Anpassungen der generierten Klassenkommentare an
 *          das aktuelle Format.
 *          <P>
 * 
 */

public class ReconstructableCodeFactory extends DefaultCodeFactory {

	/* Liste der zu importierenden Packages. */
	private Hashtable imports = new Hashtable();

	/** Generiert eine CodeFactory mit Defaultwerten. */
	public ReconstructableCodeFactory() {
		super();
	}

	/* Implementierung des Interfaces CodeFactory. */

	/**
	 * @changed OLI 28.03.2008 - Erweiterung um die F&auml;higkeit Ressourcen zu
	 *          den Tabellen zu produzieren.
	 */
	protected void codeTable(TabellenModel tm, DiagrammModel dm, String out) throws Exception {
		SortedVector res = new SortedVector();
		String classname = null;
		String packagename = this.makeDirAndPackage(tm, dm.getCodePfad(), dm.getBasePackageName());
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
		if (tm.isDynamicCode()) {
			// Eine Standardimplementierung der DBFactory.
			zeilenDBFactory.addElement("/*");
			zeilenDBFactory.addElement(" * DBFactory" + tm.getName() + ".java");
			zeilenDBFactory.addElement(" *");
			zeilenDBFactory.addElement(" * AUTOMATISCH VON ARCHIMEDES GENERIERT!!!");
			zeilenDBFactory.addElement(" *");
			zeilenDBFactory.addElement(" * ZUM �BERSCHREIBEN GEEIGNET!!!");
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
			zeilenDBFactory.addElement("import corent.db.xs.*;");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("import " + packagename + ".*;");
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
			// Der Stub f�r weitere Implementierungen.
			zeilenSkeleton.addElement("/*");
			zeilenSkeleton.addElement(" * " + tm.getName() + ".java");
			zeilenSkeleton.addElement(" *");
			zeilenSkeleton.addElement(" * AUTOMATISCH VON ARCHIMEDES GENERIERT!!!");
			zeilenSkeleton.addElement(" *");
			zeilenSkeleton.addElement(" * ZUM �BERSCHREIBEN GEEIGNET!!!");
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
		if (tm.getSelectionViewOrderMembers().length > 0) {
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
			String superklasse = "ApplicationObject";
			if (dm.getUdschebtiBaseClassName().length() > 0) {
				superklasse = dm.getUdschebtiBaseClassName();
			}
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
			if (ok && (tsm != null) && (tsm.getRelation() != null)) {
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
			z = "public class " + tm.getName() + " extends " + tm.getName() + "Udschebti" + " implements ";
			boolean komma = false;
			if (cached || columnviewable || deaktivierbar || lastmodificationtracker) {
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
				if (tm.getNReferences().length > 0) {
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
			z = z.concat((komma ? ", " : "")).concat("Reconstructable {");
			zeilenSkeleton.addElement(z);
			zeilenSkeleton.addElement("");
			zeilenDBFactory.addElement("public class DBFactory" + tm.getName() + " extends "
					+ "ReconstructableDBFactory<" + tm.getName() + "> implements " + "GenerateExpander {");
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
			zeilenDBFactory.addElement("    public String getAdditionalPreselection() {");
			zeilenDBFactory.addElement("        return \"(" + tm.getName() + ".ModifiedAt=-1 " + "or " + tm.getName()
					+ ".ModifiedAt is null)\";");
			zeilenDBFactory.addElement("    }");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("    public String getIdColumnname() {");
			zeilenDBFactory.addElement("        return \"Id\";");
			zeilenDBFactory.addElement("    }");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("    public String getModifiedAtColumnname() {");
			zeilenDBFactory.addElement("        return \"ModifiedAt\";");
			zeilenDBFactory.addElement("    }");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("    public String getReferenceColumnname() {");
			zeilenDBFactory.addElement("        return \"ObjectNo\";");
			zeilenDBFactory.addElement("    }");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("    public String getTablename() {");
			zeilenDBFactory.addElement("        return \"" + tm.getName() + "\";");
			zeilenDBFactory.addElement("    }");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("    public String getTimestampColumnname() {");
			zeilenDBFactory.addElement("        return \"Timestamp\";");
			zeilenDBFactory.addElement("    }");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("    /* Implementation of the interface " + "GenerateExpander. */");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("    public Object doChangeKeys(int id) {");
			zeilenDBFactory.addElement("        return new Long(id);");
			zeilenDBFactory.addElement("    }");
			zeilenDBFactory.addElement("");
			zeilenDBFactory.addElement("    public Object doGenerateExpansion(Object obj) {");
			zeilenDBFactory.addElement("        " + tm.getName() + " o = (" + tm.getName() + ") obj;");
			zeilenDBFactory.addElement("        if (o.getObjectNo() < 1) {");
			zeilenDBFactory.addElement("            o.setObjectNo(o.getId());");
			zeilenDBFactory.addElement("            o.setGLI(o.getId());");
			zeilenDBFactory.addElement("        }");
			zeilenDBFactory.addElement("        return o;");
			zeilenDBFactory.addElement("    }");
			zeilenDBFactory.addElement("");
			/*
			 * Vector vnr = tm.getNReferenzen(); if (vnr.size() > 0) {
			 * NReferenzModel nr = (NReferenzModel) vnr.elementAt(0);
			 * TabellenspaltenModel tsm = nr.getTabellenspalte(); TabellenModel
			 * tmr = tsm.getTabelle(); String tmn = tm.getName(); String tmnr =
			 * tmr.getName(); zeilenDBFactory.addElement("    public Vector<" +
			 * tm + "> read(String w, " + "Connection c, OrderByDescriptor o)");
			 * zeilenDBFactory.addElement("            throws SQLException {");
			 * zeilenDBFactory.addElement("        return this.read(w, c);");
			 * zeilenDBFactory.addElement("    }");
			 * zeilenDBFactory.addElement("");
			 * zeilenDBFactory.addElement("    public Vector<" + tmn +
			 * "> read(String w, " + "Connection c) throws SQLException {");
			 * zeilenDBFactory.addElement("        " + tmn + " o = new " + tmn +
			 * "(this.adf);"); zeilenDBFactory.addElement("        " + tmnr +
			 * " r = new " + tmnr + "(this.adf);");
			 * zeilenDBFactory.addElement("        Vector<" + tmn +
			 * "> v = new Vector<" + tmn + ">();");
			 * zeilenDBFactory.addElement("        int ofieldcount = DBFactoryUtil."
			 * + "GetSelectionFieldCount(o.getPersistenceDescriptor());");
			 * zeilenDBFactory
			 * .addElement("        int rfieldcount = DBFactoryUtil." +
			 * "GetSelectionFieldCount(r.getPersistenceDescriptor());");
			 * zeilenDBFactory
			 * .addElement("        String select = DBFactoryUtil." +
			 * "GetSelectionFields(o.getPersistenceDescriptor());");
			 * zeilenDBFactory
			 * .addElement("        select = select.concat(\", \").concat(" +
			 * "DBFactoryUtil.GetSelectionFields(");zeilenDBFactory.addElement(
			 * "                r.getPersistenceDescriptor()));" );
			 * zeilenDBFactory
			 * .addElement("        select = \"select \".concat(select)." +
			 * "concat(\" from " + tmn + "\\n\"");
			 * zeilenDBFactory.addElement("                + \"left outer join "
			 * + tmnr + " on" + " " + tsm.getFullName() + "=" + tmn + "." +
			 * tm.getPrimaryKeyName() + "\\n\");");zeilenDBFactory.addElement(
			 * "        if ((w != null) && !w.equals(\"\")) {");
			 * zeilenDBFactory.
			 * addElement("            select = select.concat(\"where \" " +
			 * "+ w + \"\\n\");"); zeilenDBFactory.addElement("        }");
			 * zeilenDBFactory
			 * .addElement("        select = select.concat(\"order by " + tmn +
			 * "." + tm.getPrimaryKeyName() + ", " + tmnr + "." +
			 * tmr.getPrimaryKeyName() + "\\n\");");zeilenDBFactory.addElement(
			 * "        ResultSet rs = DBExec.Query(c, select);" );
			 * zeilenDBFactory
			 * .addElement("            int oid = rs.getInt(1);");
			 * zeilenDBFactory.addElement("            if (oid != o.get" + tmn +
			 * "()) {"); zeilenDBFactory.addElement("                o = (" +
			 * tmn +
			 * ") DBFactoryUtil.GetFromRS(rs, o.getPersistenceDescriptor(), o, "
			 * + "1, 1);"); zeilenDBFactory.addElement("                o.set" +
			 * tmn + "(oid);");
			 * zeilenDBFactory.addElement("                v.addElement(o);");
			 * zeilenDBFactory.addElement("            }");
			 * zeilenDBFactory.addElement
			 * ("            int rid = rs.getInt(ofieldcount+1);" );
			 * zeilenDBFactory.addElement("            if (rid != r.get" + tmnr
			 * + "()) {"); zeilenDBFactory.addElement("                r = (" +
			 * tmnr +
			 * ") DBFactoryUtil.GetFromRS(rs, r.getPersistenceDescriptor(), r,"
			 * );zeilenDBFactory.addElement(
			 * "                        ofieldcount+1, 1);");
			 * zeilenDBFactory.addElement("                r.set" + tmnr +
			 * "(rid);"); zeilenDBFactory.addElement("                o.get" +
			 * tmnr + "().addElement(r);");
			 * zeilenDBFactory.addElement("            }");
			 * zeilenDBFactory.addElement("        }");
			 * zeilenDBFactory.addElement("        DBExec.CloseQuery(rs);");
			 * zeilenDBFactory.addElement("        return v;");
			 * zeilenDBFactory.addElement("    }");
			 * zeilenDBFactory.addElement("");
			 * zeilenDBFactory.addElement("    public void write(" + tmn +
			 * " o, " + "Connection c) throws SQLException {");
			 * zeilenDBFactory.addElement("        super.write(o, c);");
			 * zeilenDBFactory
			 * .addElement("        DBExec.Update(c, \"delete from " + tmnr +
			 * "where " + tmn + "=\" + o.get" + tmn + "());");
			 * zeilenDBFactory.addElement("        DBFactory" + tmnr +
			 * " dbfr = new " + "DBFactory" + tmnr + "(this.adf);");
			 * zeilenDBFactory.addElement("        for (int i = 0, len = o.get"
			 * + tmnr + "().size(); i < len; i++) {");
			 * zeilenDBFactory.addElement("            " + tmnr + " r = (" +
			 * tmnr + ") o.get" + tmnr + "().get(i);");
			 * zeilenDBFactory.addElement("            r.setADF(this.adf);");
			 * zeilenDBFactory.addElement("            r.set" + tmnr + "(k.get"
			 * + tmn + "() * 1000 + i+1);");
			 * zeilenDBFactory.addElement("            dbfk.write(k, c);");
			 * zeilenDBFactory.addElement("        }");
			 * zeilenDBFactory.addElement("    }");
			 * zeilenDBFactory.addElement("");
			 * zeilenDBFactory.addElement("    public void remove(" + tmn +
			 * " o, boolean " + "forced, Connection c) throws SQLException {");
			 * zeilenDBFactory
			 * .addElement("        super.remove(o, forced, c);"); boolean
			 * rdeact = false; for (int i = 0, len = tmr.getStereotypenCount();
			 * i < len; i++) { StereotypeModel stm = tmr.getStereotypeAt(i); if
			 * (stm.getName().equalsIgnoreCase("deaktivierbar") ||
			 * stm.getName().equalsIgnoreCase("deactivatable")) { rdeact = true;
			 * break; } } if (rdeact) {
			 * zeilenDBFactory.addElement("        if (forced) {");
			 * zeilenDBFactory
			 * .addElement("            DBExec.Update(c, \"delete from " + tmnr
			 * + " where " + tmn + "=\" + o.get" + tmn + "());");
			 * zeilenDBFactory.addElement("        } else {");
			 * zeilenDBFactory.addElement
			 * ("            DBExec.Update(c, \"update " + tmnr +
			 * " set Geloescht=1 where " + tmn + "=\" + o.get" + tmn + "());");
			 * zeilenDBFactory.addElement("        }"); } else {
			 * zeilenDBFactory.
			 * addElement("        DBExec.Update(c, \"delete from " + tmnr +
			 * " where " + tmn + "=\" + o.get" + tmn + "());"); }
			 * zeilenDBFactory.addElement("    }");
			 * zeilenDBFactory.addElement(""); }
			 */
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
				zeilen.addElement("    /* �berschreiben von Methoden der Superklasse " + "(Pflichtteil). */");
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
				ColumnModel[] cs = tm.getHashCodeMembers();
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
				zeilenSkeleton.addElement("    /* Implementation of the abstract methods of the" + " super class. */");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public Object createObject() {");
				zeilenSkeleton.addElement("        return new " + tm.getName() + "(this.adf);");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    /* Implementation (completion) of the abstract "
						+ "methods of the super class. */");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public long getObjectnumber() {");
				zeilenSkeleton.addElement("        return this.getObjectNo();");
				zeilenSkeleton.addElement("    }");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public void setObjectnumber(long r) {");
				zeilenSkeleton.addElement("        this.setObjectNo((int) r);");
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
						+ "<String, java.awt.Component> comps, boolean created) {");
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
			NReferenceModel[] nrs = tm.getNReferences();
			if (nrs.length > 0) {
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    /* Implementierung des Interfaces ListOwner. */");
				zeilenSkeleton.addElement("");
				zeilenSkeleton.addElement("    public Object createElement(int nr) {");
				zeilenSkeleton.addElement("        switch (nr) {");
				for (NReferenceModel nr : nrs) {
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
				for (NReferenceModel nr : nrs) {
					ColumnModel cm = nr.getColumn();
					String tmnr = cm.getTable().getName();
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
		out = dm.getCodePfad().replace('\\', '/');
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

}

/*
 * class TSMSortContainer implements Comparable {
 * 
 * public String sorter = null; public TabellenspaltenModel tsm = null;
 * 
 * public TSMSortContainer(String sorter, TabellenspaltenModel tsm) { super();
 * this.sorter = sorter; this.tsm = tsm; }
 * 
 * 
 * /* Implementierung des Interfaces Comparable. * /
 * 
 * public int compareTo(Object obj) { return
 * this.sorter.toLowerCase().compareTo(((TSMSortContainer)
 * obj).sorter.toLowerCase() ); }
 * 
 * }
 */
