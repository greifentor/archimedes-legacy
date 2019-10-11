/*
 * ArchimedesCodeWriter.java
 * 
 * 07.01.2005
 *
 * (c) by MediSys
 *
 */

package archimedes.legacy.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.event.CodeFactoryListener;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.legacy.Archimedes;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.legacy.scheme.Diagramm;
import archimedes.model.CodeFactory;
import archimedes.model.DataModel;
import archimedes.model.RelationModel;
import baccara.gui.GUIBundle;
import corent.files.StructuredTextFile;

/**
 * Diese Klasse erm&ouml;glicht das Erzeugen von Fifth-Code f&uuml;r
 * Untersuchung einer DB auf referenzielle und inhaltliche Integrit&auml;t.
 * 
 * @author Volodymyr Medvid
 * 
 */

public class ArchimedesCodeWriter implements CodeFactory {

	private DataModel dataModel = null;

	/** Erzeugt ein CodeFactory-Objekt */
	public ArchimedesCodeWriter() {
		super();
	}

	/**
	 * Untersucht alle Tabellen, erzeugt Fifth-Code (checkref und checknull) und
	 * speichert ihn in eine Datei.
	 * 
	 * @param dataModel
	 *            DataModel
	 * @param out
	 *            Name der Ausgabedatei
	 * @return TRUE, wenn Fifth-Code erzeugt wurde.
	 */
	public boolean generate(String out) {
		DiagrammModel dm = (DiagrammModel) this.dataModel;
		Vector v = dm.getTabellen();
		String fifthCode = "";
		// Alle Tabellen durchgehen und Fifth-Code erzeugen
		for (int i = 0, len = v.size(); i < len; i++) {
			TabellenModel tm = (TabellenModel) v.elementAt(i);
			fifthCode += getCheckrefCode(tm);
			fifthCode += getChecknullCode(tm);
		}
		if (fifthCode.length() == 0) {
			System.out.println("\n\nEs wurde kein Fifth-Code erzeugt!\n");
			return false;
		}
		fifthCode = "check 'integd.upn.IntegDInterpreter'" + fifthCode;
		// Fifth-Code in eine Datei speichern
		try {
			FileOutputStream fos = new FileOutputStream(out, false);
			byte[] bytes = fifthCode.getBytes();
			fos.write(bytes);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Sucht alle Referezen im Tabellenmodell und erzeugt den Fifth-Code
	 * f&uuml;r die Pr&uuml;fung dieser Referenzen.
	 * 
	 * @param tm
	 *            ein Tabellenmodell, das durchgesucht werden soll
	 * @return der erzeugte Fifth-Code
	 */
	public String getCheckrefCode(TabellenModel tm) {
		String result = "";
		String refTab = tm.getName();
		String refIDField = "";
		// Nach ID-Spalte suchen
		for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
			TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
			if (tsm.isPrimarykey()) {
				refIDField = tsm.getName();
				break;
			}
		}
		// Alle Spalten durchgehen und nach Referenzen suchen
		for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
			TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
			RelationModel rm = null;
			if ((rm = tsm.getRelation()) != null) {
				String refField = tsm.getName();
				TabellenspaltenModel tsmReferenced = (TabellenspaltenModel) rm.getReferenced();
				String field = tsmReferenced.getName();
				String table = (tsmReferenced.getTabelle()).getName();
				result += "\n'" + refTab + "' '" + refField + "' '" + refIDField;
				result += "' '" + table + "' '" + field + "' checkref";
			}
		}
		return result;
	}

	/**
	 * Sucht alle NotNull-Spallten im Tabellenmodell und erzeugt den Fith-Code
	 * f&uuml;r die Pruefung dieser Spalten.
	 * 
	 * @param tm
	 *            ein Tabellenmodell, das durchgesucht werden soll
	 * @return der erzeugte Fifth-Code
	 */
	public String getChecknullCode(TabellenModel tm) {
		String result = "";
		String table = tm.getName();
		String idField = "";
		// Nach ID-Spalte suchen
		for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
			TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
			if (tsm.isPrimarykey()) {
				idField = tsm.getName();
				break;
			}
		}
		// Alle Spalten durchgehen und nach NotNull-Spalten suchen
		for (int i = 0, len = tm.getTabellenspaltenCount(); i < len; i++) {
			TabellenspaltenModel tsm = tm.getTabellenspalteAt(i);
			if (tsm.isNotNull() && !tsm.isPrimarykey()) {
				String field = tsm.getName();
				result += "\n'" + table + "' '" + field + "' '" + idField;
				result += "' checknull";
			}
		}
		return result;
	}

	/* Gibt die Hilfe zur Benutzung des Programms auf die Konsole aus. */
	static private void printHelp() {
		String strHelp = "";
		strHelp = "\nBENUTZUNG: ArchimedesCodeWriter [ADS-Datei] [Ausgabe-Datei]\n";
		System.out.println(strHelp);
	}

	/**
	 * Mit dieser Methode kann der ArchimedesCodeWriter gestartet werden.
	 * 
	 * Als erstes Element im args-Array wird der Name des ADS-Datei
	 * &uuml;bergeben. Als zweites - Ausgabedateiname.
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			printHelp();
			System.exit(0);
		}
		if (args[0].equals("-h") || args[0].equals("--help") || args[0].equals("/?")) {
			printHelp();
			System.exit(0);
		}
		if (args.length < 2) {
			System.out.println("\n\nKein ADS- oder Ausgabedateiname angegeben!");
			System.exit(1);
		}
		File file = new File(args[0]);
		if (!file.exists()) {
			System.out.println("\n\nDie angegebene ADS-Datei (" + args[0] + ") existiert nicht!\n");
			System.exit(1);
		}
		StructuredTextFile stf = new StructuredTextFile(args[0]);
		try {
			stf.setHTMLCoding(true);
			stf.load();
			DiagrammModel diagramm = new Diagramm().createDiagramm(stf);
			ArchimedesCodeWriter acw = new ArchimedesCodeWriter();
			acw.setDataModel(diagramm);
			acw.generate(args[1]);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * @changed OLI 31.03.2016 - Added.
	 */
	@Override
	public void setGUIBundle(GUIBundle guiBundle) {
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
		return new String[0];
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
		return "ArchimedesCodeWriter";
	}

	/**
	 * @changed OLI 18.04.2017 - Added.
	 */
	@Override
	public String getVersion() {
		return Archimedes.GetVersion();
	}

}