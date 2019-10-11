/*
 * ApplicationUtil.java
 *
 * 02.01.2007
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.app;

import static corentx.util.Checks.ensure;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.ColumnMetaData;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.TableMetaData;
import archimedes.legacy.scheme.Diagramm;
import archimedes.model.SimpleIndexMetaData;
import archimedes.model.UniqueMetaData;
import corent.Corent;
import corent.base.Constants;
import corent.base.SortedVector;
import corent.db.ConnectionManager;
import corent.db.DBExec;
import corent.db.DBExecMode;
import corent.db.DBType;
import corent.db.JDBCDataSourceRecord;
import corent.files.Inifile;
import corent.files.StructuredTextFile;
import corent.gui.COUtil;
import corent.gui.ContextOwner;
import corent.gui.UIMenuItem;

/**
 * Diese Klasse enth&auml;t Standardroutinen, die in Archimedes-Applikationen
 * zum Einsatz kommen k&ouml;nnen.
 * <P>
 * &Uuml;ber die Property
 * <I>archimedes.app.ApplicationUtil.GetMetaData.output</I> (Boolean) kann der
 * Konsolenoutput f&uuml;r die Methode
 * <TT>GetMetaData(JDBCDataSourceRecord, List&lt;
 * String&gt;)</TT> eingeschaltet werden.
 * 
 * <TABLE BORDER=1>
 * <TR VALIGN=TOP>
 * <TH>Property</TH>
 * <TH>Default</TH>
 * <TH>Typ</TH>
 * <TH>Beschreibung</TH>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>archimedes.app.ApplicationUtil.CreateADF.show.time</TD>
 * <TD>false</TD>
 * <TD>Boolean</TD>
 * <TD>
 * Das Setzen dieser Property sorgt f&uuml;r eine Ausgabe zur Dauer des
 * Einlesens eines Archimedesdatenmodells.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>archimedes.app.ApplicationUtil.GetMetaData.output</TD>
 * <TD>false</TD>
 * <TD>Boolean</TD>
 * <TD>
 * Schaltet eine Ausgabe der eingelesenen Metadaten f&uuml;r jede Tabelle bei
 * Aufruf der Methode
 * <TT>GetMetaData(JDBCDataSourceRecord, java.util.List&lt;String&gt;)
 *             </TT> ein.</TD>
 * </TR>
 * </TABLE>
 * 
 * @author ollie
 * 
 * @changed OLI 21.08.2008 - Erweiterung um die Methoden
 *          <TT>CreateADF(DiagrammModel)</TT> und <TT>CreateDiagram(String)</TT>
 *          .
 * @changed OLI 30.12.2008 - Korrektur der Zuweisung der Not-Null-Flagge in der
 *          Methode
 *          <TT>GetMetaData(JDBCDataSourceRecord, java.util.List&lt;String&gt;)</TT>
 *          .
 * @changed OLI 14.01.2009 - Erweiterung um die Methoden <TT>BuildBorder()</TT>,
 *          <TT>BuildFullBorder()</TT>, <TT>BuildBorderedPanel()</TT> und
 *          <TT>BuildFullBorderedPanel()</TT>.
 * @changed OLI 26.01.2009 - Erweiterung um die Methode
 *          <TT>ReadProperties(Properties,
 *         String)</TT>. In diesem Zusammenhang ist die Methode
 *          <TT>ReadProperties(String)</TT> auf die neue Methode umgelenkt
 *          worden. <BR>
 *          &Auml;nderung des R&uuml;ckgabewertes der <TT>EvaluateArgLine</TT>
 *          -Methoden auf <TT>Map</TT>.
 * @changed OLI 11.02.2009 - Entfernung der Konsolenausgaben aus der Methode
 *          <TT>ReadProperties(Properties, String)</TT>.
 * @changed OLI 02.04.2009 - Umstellung auf log4j.
 * @changed OLI 31.05.2010 - Erweiterung um die Methode
 *          <TT>GetMetadata(Connection, List,
 *         boolean)</TT>
 * @changed OLI 01.10.2010 - Erweiterung um die Methode
 *          <TT>GetIndexMetaData(Connection)</TT>.
 */

public class ApplicationUtil {

	/* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
	private static Logger log = Logger.getLogger(ApplicationUtil.class);

	/**
	 * Liefert eine Border, die aus einer Empty- und einer EtchedBorder besteht.
	 * Die Gaps und der Etch-Type werden &uuml;ber die Parameter festgelegt.
	 * 
	 * @return Eine CompoundBorder (EtchedBorder, EmptyBorder) mit den
	 *         Standardwerten aus der Klasse <TT>corent.base.Constants</TT>
	 *         (HGAP, VGAP und ETCH).
	 * 
	 * @changed OLI 14.01.2009 - Hinzugef&uuml;gt.
	 */
	public static Border BuildBorder() {
		return BuildBorder(Constants.HGAP, Constants.VGAP, Constants.ETCH);
	}

	/**
	 * Liefert eine Border, die aus einer Empty- und einer EtchedBorder besteht.
	 * Die Gaps und der Etch-Type werden &uuml;ber die Parameter festgelegt.
	 * 
	 * @param hgap
	 *            Horizontale Dicke der EmptyBorder (links und rechts).
	 * @param vgap
	 *            Vertikale Dicke der EmptyBorder (oben und unten).
	 * @param etchtype
	 *            Der Typ in dem die EtchedBorder dargestellt werden soll.
	 * @return Eine CompoundBorder (EtchedBorder, EmptyBorder) mit den
	 *         angegebenen Parametern.
	 */
	public static Border BuildBorder(int hgap, int vgap, int etchtype) {
		return new CompoundBorder(new EtchedBorder(etchtype), new EmptyBorder(hgap, vgap, hgap, vgap));
	}

	/**
	 * Liefert eine Border, die aus einer Empty-, einer Etched- und einer
	 * weiteren EmptyBorder besteht. Die Gaps und der Etch-Type werden &uuml;ber
	 * die Parameter festgelegt.
	 * 
	 * @return Eine CompoundBorder (EmptyBorder, EtchedBorder, EmptyBorder) mit
	 *         den Standardwerten aus der Klasse <TT>corent.base.Constants</TT>
	 *         (HGAP, VGAP und ETCH ).
	 * 
	 * @changed OLI 14.01.2009 - Hinzugef&uuml;gt.
	 */
	public static Border BuildFullBorder() {
		return BuildFullBorder(Constants.HGAP, Constants.VGAP, Constants.ETCH);
	}

	/**
	 * Liefert eine Border, die aus einer Empty-, einer Etched- und einer
	 * weiteren EmptyBorder besteht. Die Gaps und der Etch-Type werden &uuml;ber
	 * die Parameter festgelegt.
	 * 
	 * @param hgap
	 *            Horizontale Dicke der EmptyBorder (links und rechts).
	 * @param vgap
	 *            Vertikale Dicke der EmptyBorder (oben und unten).
	 * @param etchtype
	 *            Der Typ in dem die EtchedBorder dargestellt werden soll.
	 * @return Eine CompoundBorder (EmptyBorder, EtchedBorder, EmptyBorder) mit
	 *         den angegebenen Parametern.
	 */
	public static Border BuildFullBorder(int hgap, int vgap, int etchtype) {
		return new CompoundBorder(BuildBorder(hgap, vgap, etchtype), new EmptyBorder(hgap, vgap, hgap, vgap));
	}

	/**
	 * Liefert ein JPanel mit einer durch <TT>BuildBorder</TT> erzeugen Border.
	 * Die Gaps und der Etch-Type werden &uuml;ber die Parameter festgelegt.
	 * 
	 * @return Ein JPanel mit einer CompoundBorder (EmptyBorder, EtchedBorder)
	 *         mit den Standardwerten aus der Klasse
	 *         <TT>corent.base.Constants</TT> (HGAP, VGAP und ETCH ).
	 * 
	 * @changed OLI 14.01.2009 - Hinzugef&uuml;gt.
	 */
	public static JPanel BuildBorderedPanel() {
		return BuildBorderedPanel(Constants.HGAP, Constants.VGAP, Constants.ETCH);
	}

	/**
	 * Liefert ein JPanel mit einer durch <TT>BuildBorder</TT> erzeugen Border.
	 * Die Gaps und der Etch-Type werden &uuml;ber die Parameter festgelegt.
	 * 
	 * @param hgap
	 *            Horizontale Dicke der EmptyBorder (links und rechts).
	 * @param vgap
	 *            Vertikale Dicke der EmptyBorder (oben und unten).
	 * @param etchtype
	 *            Der Typ in dem die EtchedBorder dargestellt werden soll.
	 * @return Ein JPanel mit einer CompoundBorder (EtchedBorder, EmptyBorder)
	 *         mit den angegebenen Parametern.
	 */
	public static JPanel BuildBorderedPanel(int hgap, int vgap, int etchtype) {
		JPanel p = new JPanel(new BorderLayout(hgap, vgap));
		p.setBorder(BuildBorder(hgap, vgap, etchtype));
		return p;
	}

	/**
	 * Liefert ein JPanel mit einer durch <TT>BuildFullBorder</TT> erzeugen
	 * Border. Die Gaps und der Etch-Type werden &uuml;ber die Parameter
	 * festgelegt.
	 * 
	 * @return Ein JPanel mit einer CompoundBorder (EmptyBorder, EtchedBorder,
	 *         EmptyBorder) mit den Standardwerten aus der Klasse
	 *         <TT>corent.base.Constants</TT> (HGAP, VGAP und ETCH).
	 * 
	 * @changed OLI 14.01.2009 - Hinzugef&uuml;gt.
	 */
	public static JPanel BuildFullBorderedPanel() {
		return BuildFullBorderedPanel(Constants.HGAP, Constants.VGAP, Constants.ETCH);
	}

	/**
	 * Liefert ein JPanel mit einer durch <TT>BuildFullBorder</TT> erzeugen
	 * Border. Die Gaps und der Etch-Type werden &uuml;ber die Parameter
	 * festgelegt.
	 * 
	 * @param hgap
	 *            Horizontale Dicke der EmptyBorder (links und rechts).
	 * @param vgap
	 *            Vertikale Dicke der EmptyBorder (oben und unten).
	 * @param etchtype
	 *            Der Typ in dem die EtchedBorder dargestellt werden soll.
	 * @return Ein JPanel mit einer CompoundBorder (EmptyBorder, EtchedBorder,
	 *         EmptyBorder) mit den angegebenen Parametern.
	 */
	public static JPanel BuildFullBorderedPanel(int hgap, int vgap, int etchtype) {
		JPanel p = new JPanel(new BorderLayout(hgap, vgap));
		p.setBorder(BuildFullBorder(hgap, vgap, etchtype));
		return p;
	}

	/**
	 * Diese Methode &Ouml;ffnet einen Informationsdialog und gibt dort den
	 * vollst&auml;ndigen Namen der &uuml;bergebenen Komponente aus. Es ist mehr
	 * als hilfreich, wenn die Komponente das Interface ContextOwner
	 * implementiert.
	 * <P>
	 * Durch Setzen der Property
	 * <I>archimedes.app.ApplicationUtil.ShowComponentFullName.dialog</I> kann
	 * die Ausgabe des Komponentennamens in einen Informationsdialog umgeleitet
	 * werden, der allerdings modal und zu quittieren ist. Sonst erfolgt die
	 * Ausgabe aus die Konsole.
	 * 
	 * @param c
	 *            Die Komponente, deren Namen ausgegeben werden soll.
	 */
	public static void ShowComponentFullName(Component c) {
		String s = COUtil.GetFullContext(c);
		if (Boolean.getBoolean("archimedes.app.ApplicationUtil.ShowComponentFullName.dialog")) {
			JOptionPane.showMessageDialog(null, (c instanceof ContextOwner ? "Der ContextOwner" : "Die Komponente")
					+ " traegt den Namen:\n" + s + "\nComponent-Classname: " + c.getClass().getName(),
					"Komponentenname", JOptionPane.INFORMATION_MESSAGE);
		} else {
			System.out.println("context-name=" + s + (c instanceof ContextOwner ? " (ContextOwner)" : ""));
		}
	}

	/**
	 * &Uuml;ber diese Methode kann ein UIManager eingestellt werden. Wird das
	 * angegebene Theme nicht gefunden, so wird das Defaulttheme eingestellt.
	 * 
	 * @param themename
	 *            Der Name des einzustellenden Themes.
	 * @param w
	 *            Das Window, das als Wurzel f&uuml;r die Aktualisierung der
	 *            dienen soll.
	 */
	public static void SelectUIManager(String themename, Window w) {
		try {
			UIManager.setLookAndFeel(themename);
			SwingUtilities.updateComponentTreeUI(w);
		} catch (Exception exc) {
			log.error("Error loading L&F: " + exc);
			log.error("trying to set MetalLookAndFeel.");
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
				SwingUtilities.updateComponentTreeUI(w);
			} catch (Exception exc0) {
				log.error("Error loading L&F: " + exc0, exc0);
			}
		}
	}

	/**
	 * Diese Methode generiert ein Men&uuml;, aus dem der Benutzer einen
	 * UIManager ausw&auml;hlen kann, der dann f&uuml;r die Anwendung
	 * eingestellt wird.
	 * 
	 * @param menutext
	 *            Der Text, den Das MenmenuItem&uuml; tragen soll.
	 * @param w
	 *            Das Window, das als Wurzel f&uuml;r die Aktualisierung der
	 *            dienen soll.
	 * 
	 * @todo OLI - Hier mu&szlig; ein COMenu herauskommen (Mehrsprachigkeit,
	 *       Icon) (OLI 17.02.2009).
	 */
	public static JMenu CreateUIMenu(String menutext, Window w) {
		JMenu menu = new JMenu(menutext);
		try {
			UIManager.LookAndFeelInfo[] lafi = UIManager.getInstalledLookAndFeels();
			for (int i = 0, len = lafi.length; i < len; i++) {
				UIMenuItem menuItem = new UIMenuItem(lafi[i].getName(), lafi[i].getClassName(), w);
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UIMenuItem uimi = (UIMenuItem) e.getSource();
						SelectUIManager(uimi.getLookAndFeelClassname(), uimi.getRootwindow());
					}
				});
				menuItem.setRootwindow(w);
				menu.add(menuItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menu;
	}

	/**
	 * Diese Methode wertet eine Zeile von standardisierten
	 * Kommandozeilenparametern aus und gibt entsprechende Werte in einer
	 * Hashtable an die Applikation zur&uuml;ck.
	 * <P>
	 * Die ber&uuml;cksichtigten Parameter sind:
	 * 
	 * <PRE>
	 * Option                Parameter   Beschreibung
	 * --------------------- ----------- ---------------------------------------------------
	 * --ads <I>name</I>            ads         Der Name der ADS-Datei.
	 * -h o. --help          help        Anforderung einer Hilfe Datei.
	 * -i <I>inidateiname</I>       ini         Eine Inidateioption mit Dateinamen. Die Datei wird
	 *                                   eingelesen und als Referenz &uuml;bergeben.
	 * -f <I>filename</I>           file        Ein Dateiname.
	 * --name, -n <I>name</I>       dbname      Der Name der Datenbank (default: SPMiLocalOperational).
	 * --password, -p <I>pw</I>     dbpassword  Das Passwort zum angegebenen Benutzeraccount.
	 * --server, -s <I>name</I>     dbserver    Der Name des Servers, auf dem die umzukopierende 
	 *                                   Datenbank liegt.
	 * --user, -u <I>name</I>       dbuser      Der Name des Datenbankenbenutzers, mit dem die 
	 *                                   Operation durchgef&uuml;hrt werden soll.
	 * </PRE>
	 * 
	 * @param args
	 *            Das String-Array mit den Kommandozeilenparametern.
	 * @param ht
	 *            Ein Dictionary mit den Defaulteinstellungen zu den Parametern.
	 * @return Hashtable&lt;String, Object&gt; mit den Optionsnamen (z. B. "-i")
	 *         und einem eventuell existierenden Parameter bzw. einem leeren
	 *         String, falls kein Parameter f&uuml;r die Option erforderlich
	 *         ist.
	 * @throws Eine
	 *             eventuell w&auml;hrend der Auswertung der Zeile aufgetretene
	 *             Exception.
	 */
	public static Map<String, Object> EvaluateArgLine(String[] args, Map<String, Object> ht) throws Exception {
		for (int i = 0; i < args.length; i++) {
			try {
				if (args[i].equalsIgnoreCase("-h") || args[i].equalsIgnoreCase("--help")) {
					ht.put("help", "");
				} else if (args[i].equalsIgnoreCase("--ads")) {
					ht.put("ads", args[++i]);
				} else if (args[i].equalsIgnoreCase("-f")) {
					ht.put("file", args[++i]);
				} else if (args[i].equalsIgnoreCase("--name") || args[i].equalsIgnoreCase("-n")) {
					ht.put("dbname", args[++i]);
				} else if (args[i].equalsIgnoreCase("--password") || args[i].equalsIgnoreCase("-p")) {
					ht.put("dbpassword", args[++i]);
				} else if (args[i].equalsIgnoreCase("--server") || args[i].equalsIgnoreCase("-s")) {
					ht.put("dbserver", args[++i]);
				} else if (args[i].equalsIgnoreCase("--user") || args[i].equalsIgnoreCase("-u")) {
					ht.put("dbuser", args[++i]);
				} else if (args[i].equalsIgnoreCase("-i")) {
					ht.put("ini", args[++i]);
				}
			} catch (Exception e) {
				log.error("\nProblem beim Lesen der Kommandozeilenparameter (nr. " + i + ")", e);
				throw e;
			}
		}
		return ht;
	}

	/**
	 * Diese Methode wertet eine Zeile von standardisierten
	 * Kommandozeilenparametern aus und gibt entsprechende Werte in einer
	 * Hashtable an die Applikation zur&uuml;ck.
	 * 
	 * <P>
	 * <I>Hinweis:</I> Die m&ouml;glichen Parameter k&ouml;nnen der Beschreibung
	 * der Methode
	 * <TT>EvaluateArgLine(String[], HashMap&lt;String, String&gt;)</TT>
	 * entnommen werden.
	 * 
	 * @param args
	 *            Das String-Array mit den Kommandozeilenparametern.
	 * @param inifiledefault
	 *            Der Name einer Default-Inidatei.
	 * @return Hashtable&lt;String, Object&gt; mit den Optionsnamen (z. B. "-i")
	 *         und einem eventuell existierenden Parameter bzw. einem leeren
	 *         String, falls kein Parameter f&uuml;r die Option erforderlich
	 *         ist.
	 * @throws Eine
	 *             eventuell w&auml;hrend der Auswertung der Zeile aufgetretene
	 *             Exception.
	 */
	public static Map<String, Object> EvaluateArgLine(String[] args, String inifiledefault) throws Exception {
		Map<String, Object> ht = new Hashtable<String, Object>();
		Inifile ini = null;
		String inifilename = null;
		ht.put("ini", inifiledefault);
		ht = EvaluateArgLine(args, ht);
		inifilename = (String) ht.get("ini");
		if (inifilename != null) {
			ini = new Inifile(inifilename);
			try {
				ini.load();
				log.info("inifile (" + inifilename + ") red ...");
			} catch (IOException ioe) {
				log.error("infile (" + inifilename + ") cannot be red!", ioe);
			}
			ht.put("ini", ini);
		} else {
			ht.put("ini", null);
		}
		return ht;
	}

	@Deprecated
	/**
	 * Liest die angegebene Property-Datei in die Systemproperties ein. Es wird entsprechender
	 * Output auf der Konsole erzeugt.
	 *
	 * @param pfn Der Name der Datei, aus der die Properties gelesen werden sollen.
	 *
	 * @changed OLI 26.01.2009 - R&uuml;ckbau zu Gunsten der Methode <TT>ReadProperties(
	 *         Properties, String)</TT>.
	 *
	 * @todo OLI - Bei Gelegenheit sollten die Exceptions auch hier weitergereicht werden
	 *         (OLI 26.01.2009).
	 *
	 * @deprecated OLI 14.10.2009 - Nutzen Sie die Methode <TT>readProperties(String)</TT> aus
	 *         der Klasse <TT>corent.io.FileUtil</TT>.
	 */
	public static void ReadProperties(String pfn) {
		String s = null;
		// System.out.print("reading properties (" + pfn + ") ... ");
		try {
			s = "reading properties (" + pfn + ") ... ";
			ReadProperties(System.getProperties(), pfn);
			/*
			 * File f = new File(pfn); FileInputStream fis = new
			 * FileInputStream(f); System.getProperties().load(fis);
			 * fis.close(); `
			 */
			log.info(s + "ok");
		} catch (FileNotFoundException fnfe) {
			log.error("Fehler (Die Datei konnte nicht gefunden werden)", fnfe);
		} catch (IOException ioe) {
			log.error("Fehler (Beim Einlesen der Datei)", ioe);
		}
	}

	@Deprecated
	/**
	 * Liest die angegebene Property-Datei in die angegebenen Properties ein. Es wird
	 * entsprechender Output auf der Konsole erzeugt.
	 *
	 * @param p Die Properties, in die die Propertiesdatei eingelesen werden soll.
	 * @param pfn Der Name der Datei, aus der die Properties gelesen werden sollen.
	 * @throws FileNotFoundException Falls es keine Datei unter dem angegebenen Namen gibt.
	 * @throws IOException Falls es beim Einlesen der Datei zu einem Fehler kommt.
	 *
	 * @changed OLI 26.01.2009 - Hinzugef&uuml;gt.
	 * @changed OLI 11.02.2009 - Konsolenausgaben entfernt.
	 *
	 * @deprecated OLI 14.10.2009 - Nutzen Sie die Methode <TT>readProperties(String)</TT> aus
	 *         der Klasse <TT>corent.io.FileUtil</TT>.
	 */
	public static void ReadProperties(Properties p, String pfn) throws FileNotFoundException, IOException {
		File f = new File(pfn);
		FileInputStream fis = new FileInputStream(f);
		p.load(fis);
		fis.close();
	}

	/**
	 * Generiert eine DefaultArchimedesDescriptorFactory zum angegebenen
	 * Dateinamen.
	 * 
	 * @param dn
	 *            Name der Datei, in der das Archimedes-Datenschema hinterlegt
	 *            ist, auf das sich die Applikation beziehen soll.
	 * 
	 * @changed OLI 21.08.2008 - Umziehen der Implementierung in die Methode
	 *          <TT>CreateADF(
	 *         String, DiagrammModel)</TT>.
	 */
	public static DefaultArchimedesDescriptorFactory CreateADF(String dn) {
		DiagrammModel dm = CreateDiagram(dn);
		return CreateADF(dm);
	}

	/**
	 * Generiert eine DefaultArchimedesDescriptorFactory zum angegebenen
	 * Dateinamen.
	 * 
	 * @param dm
	 *            Das DiagrammModel, aus dem die
	 *            DefaultArchimedesDescriptorFactory generiert werden soll.
	 * 
	 * @changed OLI 21.08.2008 - Hinzugef&uuml;gt.
	 */
	public static DefaultArchimedesDescriptorFactory CreateADF(DiagrammModel dm) {
		return new DefaultArchimedesDescriptorFactory(dm);
	}

	/**
	 * Generiert eine DefaultArchimedesDescriptorFactory zum angegebenen
	 * Dateinamen.
	 * 
	 * @param dn
	 *            Name der Datei, in der das Archimedes-Datenschema hinterlegt
	 *            ist, auf das sich die Applikation beziehen soll.
	 * 
	 * @changed OLI 21.08.2008 - &Uuml;bernahme aus der urspr&uuml;nglichen
	 *          Implementierung der Methode <TT>CreateADF(String)</TT>. Einbau
	 *          einer M&ouml;glichkeit, die zum Einlesen des Modells
	 *          ben&ouml;tigte Zeit zu ermitteln.
	 */
	public static DiagrammModel CreateDiagram(String dn) {
		boolean show = Boolean.getBoolean("archimedes.app.ApplicationUtil.CreateADF.show.time");
		Diagramm d = null;
		long millis = 0;
		StructuredTextFile stf = new StructuredTextFile(dn);
		if (show) {
			millis = System.currentTimeMillis();
		}
		log.info("reading ads file " + dn);
		try {
			stf.load();
			d = new Diagramm();
			d = (Diagramm) d.createDiagramm(stf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (show) {
			System.out.println("ads file red in " + (System.currentTimeMillis() - millis) + "ms");
		}
		return d;
	}

	/**
	 * Diese Methode schreibt die Versionsnummern von Corent und Archimedes auf
	 * die Konsole sofern die Properties <TT>corent.Corent.showversion</TT>
	 * und/oder <TT>archimedes.Archimedes.showversion</TT> gesetzt sind (beide
	 * Boolean).
	 */
	public static void ShowLibVersions() {
		StringBuffer sb = new StringBuffer();
		if (Boolean.getBoolean("corent.Corent.showversion")) {
			sb.append("Corent version " + Corent.GetVersion() + "\n");
		}
		if (Boolean.getBoolean("archimedes.Archimedes.showversion")) {
			sb.append("Archimedes version " + Archimedes.GetVersion() + "\n");
		}
		if (sb.length() > 0) {
			System.out.println("\n" + sb.toString() + "\n");
		}
	}

	/**
	 * Liefert eine Liste mit den Index-Metadaten aus der angegebenen Datenbank.
	 * 
	 * @param c
	 *            Die Connection, &uuml;ber die die Index-Information gelesen
	 *            werden sollen.
	 * @param schemaName
	 *            The name of a schema in case the model is not made for the
	 *            default schema of the data base.
	 * @return Eine Liste mit den Index-Metadaten aus der angegebenen Datenbank.
	 * @throws SQLException
	 *             Falls w&auml;hrend des Zugriffs auf die Datenbank ein Fehler
	 *             auftritt.
	 * 
	 * @changed OLI 01.10.2010 - Hinzugef&uuml;gt.
	 */
	public static SortedVector<SimpleIndexMetaData> GetIndexMetaData(Connection c, String schemaName)
			throws SQLException {
		ensure(c != null, "connection cannot be null.");
		boolean uniqueConstraint = false;
		Map<String, SimpleIndexMetaData> m = new Hashtable<String, SimpleIndexMetaData>();
		ResultSet rs = null;
		SortedVector<SimpleIndexMetaData> svt = new SortedVector<SimpleIndexMetaData>();
		SortedVector sv = new SortedVector();
		String s = null;
		String schemaPattern = processSchemaPattern(schemaName);
		rs = c.getMetaData().getTables(null, schemaPattern, "%", new String[] { "TABLE" });
		while (rs.next()) {
			s = rs.getString("TABLE_NAME");
			if (s != null) {
				sv.addElement(s);
			}
		}
		DBExec.CloseQuery(rs);
		for (int i = 0, leni = sv.size(); i < leni; i++) {
			String tableName = sv.elementAt(i).toString();
			rs = c.getMetaData().getIndexInfo(null, schemaPattern, tableName, false, false);
			while (rs.next()) {
				s = rs.getString("INDEX_NAME");
				uniqueConstraint = !rs.getBoolean("NON_UNIQUE");
				if ((s != null) && (!uniqueConstraint)) {
					SimpleIndexMetaData imd = m.get(s);
					if (imd == null) {
						imd = new SimpleIndexMetaData(s, tableName);
						m.put(s, imd);
					}
					imd.addColumn(rs.getString("COLUMN_NAME"));
				}
			}
			DBExec.CloseQuery(rs);
		}
		for (SimpleIndexMetaData imd : m.values()) {
			svt.addElement(imd);
		}
		return svt;
	}

	private static String processSchemaPattern(String schemaName) {
		return ((schemaName == null) || schemaName.isEmpty() ? null : schemaName);
	}

	/**
	 * Liefert eine Struktur aus TableMetaData- und ColumnMetaData-Objekten, die
	 * die Metadaten der durch den &uuml;bergebenen JDBCDataSourceRecord
	 * erreichbaren Datenbank beschreiben.
	 * 
	 * @param c
	 *            Die Connection zu der Datenbank, aus der die Metadaten gelesen
	 *            werden sollen.
	 * @param excl
	 *            Liste mit den Namen der auszuschliessenden Tabellen.
	 * @param schemaName
	 *            The name of a schema in case the model is not made for the
	 *            default schema of the data base.
	 * @param dbExecMode
	 *            The db mode which matches to the used dbms.
	 * @return Eine Liste mit den TableMetaData-Objekten, die die Tabellen der
	 *         Datenbank beschreiben.
	 * 
	 * @changed OLI 30.12.2008 - Korrektur der Zuweisung der Not-Null-Flagge.
	 */
	public static SortedVector GetMetadata(Connection c, java.util.List<String> excl, String schemaName,
			DBExecMode dbExecMode) throws SQLException {
		return ApplicationUtil.GetMetadata(c, excl, true, schemaName, dbExecMode);
	}

	/**
	 * Liefert eine Struktur aus TableMetaData- und ColumnMetaData-Objekten, die
	 * die Metadaten der durch den &uuml;bergebenen JDBCDataSourceRecord
	 * erreichbaren Datenbank beschreiben.
	 * 
	 * @param c
	 *            Die Connection zu der Datenbank, aus der die Metadaten gelesen
	 *            werden sollen.
	 * @param excl
	 *            Liste mit den Namen der auszuschliessenden Tabellen.
	 * @param closeConnection
	 *            Wird dieser Parameter gesetzt, so wird die Connection nach
	 *            getaner Arbeit freigegeben.
	 * @param schemaName
	 *            The name of a schema in case the model is not made for the
	 *            default schema of the data base.
	 * @param dbExecMode
	 *            The db mode which matches to the used dbms.
	 * @return Eine Liste mit den TableMetaData-Objekten, die die Tabellen der
	 *         Datenbank beschreiben.
	 * 
	 * @changed OLI 31.05.2010 - Hinzugef&uuml;gt.
	 */
	public static SortedVector GetMetadata(Connection c, java.util.List<String> excl, boolean closeConnection,
			String schemaName, DBExecMode dbExecMode) throws SQLException {
		boolean exclude = false;
		boolean nn = false;
		ColumnMetaData cmd = null;
		DatabaseMetaData dmd = null;
		DBType dbtype = null;
		Enumeration e = null;
		Hashtable tables = new Hashtable();
		int colsize = 0;
		int i = 0;
		int len = 0;
		int nks = 0;
		ResultSet rs = null;
		short d = 0;
		SortedVector svt = new SortedVector();
		String n = null;
		String s = null;
		String schemaPattern = processSchemaPattern(schemaName);
		String t = null;
		TableMetaData tmd = null;
		dmd = c.getMetaData();
		rs = dmd.getTables(null, schemaPattern, "%", new String[] { "TABLE" });
		while (rs.next()) {
			n = rs.getString("TABLE_NAME");
			exclude = false;
			for (i = 0, len = excl.size(); i < len; i++) {
				s = excl.get(i);
				if ((s != null) && s.equalsIgnoreCase(n)) {
					exclude = true;
					break;
				}
			}
			if (!exclude) {
				tables.put(n, new TableMetaData(n));
			}
		}
		DBExec.CloseQuery(rs);
		for (e = tables.elements(); e.hasMoreElements();) {
			tmd = (TableMetaData) e.nextElement();
			svt.addElement(tmd);
			// c = ConnectionManager.GetConnection(dsr);
			rs = dmd.getColumns(null, schemaPattern, tmd.name, "%");
			while (rs.next()) {
				n = rs.getString("COLUMN_NAME");
				d = rs.getShort("DATA_TYPE");
				t = rs.getString("TYPE_NAME");
				colsize = rs.getInt("COLUMN_SIZE");
				nks = rs.getInt("DECIMAL_DIGITS");
				nn = (rs.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls);
				if (dbExecMode == DBExecMode.POSTGRESQL) {
					if (d == -2) {
						d = Types.BLOB;
					}
				}
				dbtype = DBType.Convert(d);
				if (!dbtype.hasLength()) {
					colsize = 0;
				}
				if (!dbtype.hasNKS()) {
					nks = 0;
				}
				cmd = new ColumnMetaData(n, t, dbtype, colsize, nks, false);
				cmd.setNotNull(nn);
				tmd.addColumn(cmd);
			}
			DBExec.CloseQuery(rs);
			// c.close();
			// c = ConnectionManager.GetConnection(dsr);
			try {
				rs = dmd.getPrimaryKeys(null, null, tmd.name);
				while (rs.next()) {
					n = rs.getString("COLUMN_NAME");
					cmd = (ColumnMetaData) tmd.getColumn(n);
					if (cmd != null) {
						cmd.primaryKey = true;
					}
				}
				DBExec.CloseQuery(rs);
			} catch (java.sql.SQLException sqle) {
				sqle.printStackTrace();
				log.info("HINWEIS: Diese Exception ist nicht problematisch!");
			}
			// c.close();
		}
		for (i = 0, len = svt.size(); i < len; i++) {
			tmd = (TableMetaData) svt.elementAt(i);
			if (Boolean.getBoolean("archimedes.app.ApplicationUtil.GetMetaData.output")) {
				System.out.println(tmd + "\n");
			}
		}
		DBExec.CloseQuery(rs);
		for (e = tables.elements(); e.hasMoreElements();) {
			tmd = (TableMetaData) e.nextElement();
			rs = dmd.getExportedKeys(null, schemaName, tmd.name);
			len = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				String pkTableName = rs.getString("PKTABLE_NAME");
				String fkTableName = rs.getString("FKTABLE_NAME");
				String fkColumName = rs.getString("FKCOLUMN_NAME");
				String fkName = rs.getString("FK_NAME");
				setReferenceTo(svt, pkTableName, fkTableName, fkColumName, fkName);
			}
		}
		DBExec.CloseQuery(rs);
		if (closeConnection) {
			c.close();
		}
		return svt;
	}

	private static void setReferenceTo(SortedVector svt, String pkTableName, String fkTableName, String fkColumName,
			String fkName) {
		for (int i = 0, leni = svt.size(); i < leni; i++) {
			TableMetaData tmd = (TableMetaData) svt.get(i);
			if (tmd.name.equals(fkTableName)) {
				ColumnMetaData cmd = tmd.getColumn(fkColumName);
				if (cmd != null) {
					cmd.setForeignKeyConstraintName(fkName);
					cmd.setReferencedTableName(pkTableName);
					System.out.println("setting reference " + tmd.name + "." + cmd.name + " -> "
							+ cmd.getReferencedTableName() + " (" + fkName + ")");
				}
			}
		}
	}

	/**
	 * Liefert eine Struktur aus TableMetaData- und ColumnMetaData-Objekten, die
	 * die Metadaten der durch den &uuml;bergebenen JDBCDataSourceRecord
	 * erreichbaren Datenbank beschreiben.
	 * 
	 * @param dsr
	 *            Der JDBCDataSourceRecord, &uuml;ber den die Datenbank
	 *            angesprochen werden kann, deren Metadaten ermittelt werden
	 *            sollen.
	 * @param excl
	 *            Liste mit den Namen der auszuschliessenden Tabellen.
	 * @param schemaName
	 *            The name of a schema in case the model is not made for the
	 *            default schema of the data base.
	 * @param dbExecMode
	 *            The db mode which matches to the used dbms.
	 * @return Eine Liste mit den TableMetaData-Objekten, die die Tabellen der
	 *         Datenbank beschreiben.
	 * @throws SQLException
	 *             Falls beim Auslesen der Metadaten ein Fehler auftritt.
	 * 
	 * @changed OLI 30.12.2008 - Korrektur der Zuweisung der Not-Null-Flagge.
	 */
	public static SortedVector GetMetadata(JDBCDataSourceRecord dsr, java.util.List<String> excl, String schemaName,
			DBExecMode dbExecMode) throws SQLException {
		Connection c = ConnectionManager.GetConnection(dsr);
		return ApplicationUtil.GetMetadata(c, excl, schemaName, dbExecMode);
	}

	/**
	 * Liefert eine Liste mit den Unique-Constraint-Metadaten aus der
	 * angegebenen Datenbank.
	 * 
	 * @param c
	 *            Die Connection, &uuml;ber die die
	 *            Unique-Constraint-Information gelesen werden sollen.
	 * @param schemaName
	 *            The name of a schema in case the model is not made for the
	 *            default schema of the data base.
	 * @return Eine Liste mit den Unique-Constraint-Metadaten aus der
	 *         angegebenen Datenbank.
	 * @throws SQLException
	 *             Falls w&auml;hrend des Zugriffs auf die Datenbank ein Fehler
	 *             auftritt.
	 * 
	 * @changed OLI 11.06.2013 - Hinzugef&uuml;gt.
	 */
	public static SortedVector<UniqueMetaData> GetUniqueMetaData(Connection c, String schemaName) throws SQLException {
		ensure(c != null, "connection cannot be null.");
		boolean uniqueConstraint = false;
		Map<String, UniqueMetaData> m = new Hashtable<String, UniqueMetaData>();
		ResultSet rs = null;
		SortedVector<UniqueMetaData> svt = new SortedVector<UniqueMetaData>();
		SortedVector sv = new SortedVector();
		String s = null;
		String schemaPattern = processSchemaPattern(schemaName);
		rs = c.getMetaData().getTables(null, schemaPattern, "%", new String[] { "TABLE" });
		while (rs.next()) {
			s = rs.getString("TABLE_NAME");
			if (s != null) {
				sv.addElement(s);
			}
		}
		DBExec.CloseQuery(rs);
		for (int i = 0, leni = sv.size(); i < leni; i++) {
			String tableName = sv.elementAt(i).toString();
			rs = c.getMetaData().getIndexInfo(null, schemaPattern, tableName, false, false);
			while (rs.next()) {
				s = rs.getString("INDEX_NAME");
				uniqueConstraint = !rs.getBoolean("NON_UNIQUE");
				if ((s != null) && (uniqueConstraint)) {
					UniqueMetaData umd = m.get(s);
					if (umd == null) {
						umd = new UniqueMetaData(s, tableName);
						m.put(s, umd);
					}
					umd.addColumn(rs.getString("COLUMN_NAME"));
				}
			}
			DBExec.CloseQuery(rs);
		}
		for (UniqueMetaData imd : m.values()) {
			svt.addElement(imd);
		}
		return svt;
	}

}