/*
 * FrameArchimedes.java
 *
 * 20.03.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

import archimedes.acf.ReadyToGenerateChecker;
import archimedes.acf.checker.ModelChecker;
import archimedes.acf.checker.ModelCheckerDomainSetForAllColumns;
import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.acf.checker.ModelCheckerMessage.Level;
import archimedes.acf.checker.ModelCheckerThread;
import archimedes.acf.checker.ModelCheckerThreadObserver;
import archimedes.acf.event.CodeFactoryEvent;
import archimedes.acf.event.CodeFactoryEventType;
import archimedes.acf.event.CodeFactoryListener;
import archimedes.connections.DatabaseConnection;
import archimedes.gui.DatabaseConnectionRecord;
import archimedes.gui.DiagramComponentPanel;
import archimedes.gui.DiagramComponentPanelEvent;
import archimedes.gui.DiagramComponentPanelListener;
import archimedes.gui.DiagramPrinter;
import archimedes.gui.GraphicExporter;
import archimedes.gui.checker.ModelCheckerMessageListDialog;
import archimedes.gui.checker.ModelCheckerMessageListFrame;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.gui.diagram.ComponentDiagramListener;
import archimedes.gui.diagram.ComponentDiagramm;
import archimedes.gui.diagram.GUIObjectTypes;
import archimedes.legacy.Archimedes;
import archimedes.legacy.acf.event.CodeFactoryProgressionEventProvider;
import archimedes.legacy.acf.gui.CodeFactoryProgressionFrame;
import archimedes.legacy.acf.gui.StandardCodeFactoryProgressionFrameUser;
import archimedes.legacy.app.ApplicationUtil;
import archimedes.legacy.checkers.ModelCheckerDomainNotInuse;
import archimedes.legacy.checkers.ModelCheckerNoPrimaryKeySet;
import archimedes.legacy.checkers.ModelCheckerPotentialForeignKeyNotSet;
import archimedes.legacy.exporter.LiquibaseScriptCreator;
import archimedes.legacy.gui.codepath.CodePathProvider;
import archimedes.legacy.gui.comparision.DataModelComparison;
import archimedes.legacy.gui.configuration.BaseConfiguration;
import archimedes.legacy.gui.configuration.BaseConfigurationFrame;
import archimedes.legacy.gui.configuration.BaseConfigurationFrameEvent;
import archimedes.legacy.gui.connections.ConnectFrame;
import archimedes.legacy.gui.connections.ConnectionsMainFrame;
import archimedes.legacy.gui.diagram.DiagramGUIObjectCreator;
import archimedes.legacy.gui.indices.ComplexIndicesAdministrationFrame;
import archimedes.legacy.gui.table.TableModelFrame;
import archimedes.legacy.importer.JDBCModelExplorer;
import archimedes.legacy.importer.JDBCModelImporter;
import archimedes.legacy.importer.jdbc.JDBCImportConnectionData.Adjustment;
import archimedes.legacy.importer.jdbc.JDBCImportData;
import archimedes.legacy.importer.jdbc.JDBCImportManager;
import archimedes.legacy.model.DiagramSaveMode;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.DiagrammModelListener;
import archimedes.legacy.model.MainViewModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.legacy.scheme.DefaultIndexListCleaner;
import archimedes.legacy.scheme.DefaultUserInformation;
import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.Sequence;
import archimedes.legacy.scheme.Stereotype;
import archimedes.legacy.scheme.Tabelle;
import archimedes.legacy.scheme.View;
import archimedes.legacy.scripting.DialogScriptExecuter;
import archimedes.legacy.transfer.DefaultCopyAndPasteController;
import archimedes.legacy.updater.JDBCModelUpdater;
import archimedes.legacy.util.VersionIncrementer;
import archimedes.legacy.util.VersionStringBuilder;
import archimedes.model.CodeFactory;
import archimedes.model.ColumnModel;
import archimedes.model.CompoundPredeterminedOptionProvider;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.OptionType;
import archimedes.model.PredeterminedOptionProvider;
import archimedes.model.SequenceModel;
import archimedes.model.SimpleIndexMetaData;
import archimedes.model.StereotypeModel;
import archimedes.model.TableModel;
import archimedes.model.UniqueMetaData;
import archimedes.model.UserInformation;
import archimedes.model.ViewModel;
import archimedes.model.events.DataModelListener;
import archimedes.model.events.TableChangedEvent;
import archimedes.model.gui.GUIDiagramModelListener;
import archimedes.model.gui.GUIObjectModel;
import archimedes.model.gui.GUIViewModel;
import archimedes.scheme.xml.DiagramXMLBuilder;
import archimedes.scheme.xml.ModelXMLReader;
import archimedes.sql.generator.ScriptGenerator;
import baccara.files.PropertyFileManager;
import baccara.gui.GUIBundle;
import baccara.gui.PropertyResourceManager;
import baccara.gui.ResourceManager;
import baccara.gui.generics.EditorFrameEvent;
import baccara.gui.generics.EditorFrameEventType;
import baccara.gui.generics.EditorFrameListener;
import corent.base.Constants;
import corent.base.Semaphore;
import corent.base.SortedVector;
import corent.base.StrUtil;
import corent.db.ConnectionManager;
import corent.db.JDBCDataSourceRecord;
import corent.djinn.FrameEditorDjinn;
import corent.djinn.FrameSelectionDjinn;
import corent.djinn.FrameSelectionEditorDjinn;
import corent.djinn.SelectionDjinnAdapter;
import corent.event.GlobalEventManager;
import corent.event.GlobalListener;
import corent.files.ExtensionFileFilter;
import corent.files.Inifile;
import corent.files.StructuredTextFile;
import corent.gui.COUtil;
import corent.gui.DefaultFrameTextViewerComponentFactory;
import corent.gui.ExtendedColor;
import corent.gui.FrameTextViewer;
import corent.gui.JDialogThrowable;
import corent.gui.JFrameWithInifile;
import corent.gui.PropertyRessourceManager;
import corent.util.MemoryMonitor;
import corentx.io.FileUtil;
import gengen.generator.AbstractCodeGenerator;
import gengen.generator.CodeGenerator;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;
import logging.Logger;

/**
 * Diese Klasse bietet das Hauptfenster der Archimedes-Applikation.
 *
 * @author ollie
 * @changed OLI 30.09.2007 - Entfernung des Aufrufs einer aufgehobenen Methode auf dem Konstruktor.
 * @changed OLI 27.11.2007 - Erweiterung um die Methode <TT>raiseAltered()</TT>.
 * @changed OLI 01.01.2008 - Anpassungen an die Erweiterung der Signatur der paint-Methode der Klasse ComponentDiagramm.
 * @changed OLI 09.01.2008 - Korrektur der Index-Datengewinnung in der Methode
 *          <TT>getIndexMetadata(JDBCDataSourceRecord)</TT> (private).
 * @changed OLI 16.08.2008 - Erweiterung der Schreibroutine in der Form, das eine Datei f&uuml;r die Pflege des
 *          Datenmodells und eine kleinere zur Nutzung in der Applikationsschicht geschrieben werden.
 * @changed OLI 22.09.2008 - Austausch der Tilde im CodeGeneratoraufruf durch den Inhalt der System-Property
 *          "user.home". Ich bilde mir ein, hierdurch eine bessere Konfigurierbarkeit zu erreichen.
 * @changed OLI 02.10.2009 - Verbesserung des Fehlerhandlings beim Bau der Updatescripte und beim Import von
 *          Datenmodellen.
 */

public class FrameArchimedes extends JFrameWithInifile implements ActionListener,
		ComponentDiagramListener<GUIObjectTypes>, DataModelListener, DiagramComponentPanelListener,
		DiagrammModelListener, EditorFrameListener<EditorFrameEvent<?, ? extends Window>>, GlobalListener,
		GUIDiagramModelListener, ModelCheckerMessageListFrameListener, ModelCheckerThreadObserver, CodeFactoryListener {
	private static final Logger LOG = Logger.getLogger(FrameArchimedes.class);
	private static final String RES_TABLE_FRAME_EDIT_TITLE = "TableFrame.edit.title";
	private static final String RES_WARNING_MESSAGES_ERRORS_DETECTED = "warning.messages.errors.detected.label";
	private static final String RES_WARNING_MESSAGES_NO_WARNINGS = "warning.messages.no.warning.label";
	private static final String RES_WARNING_MESSAGES_WARNINGS_DETECTED = "warning.messages.warnings.detected.label";

	private boolean serverMode = false;

	/* Der ActionListener f&uuml;r das Laden durch DateiCaches. */
	private ActionListener actionListenerDateiCache = null;

	/* Referenz auf die das Diagramm anzeigende Komponente. */
	private DiagramComponentPanel<GUIObjectTypes> component = null;

	/*
	 * Referenz auf das Diagramm, das mit der Applikation bearbeitet werden soll.
	 */
	private DiagrammModel diagramm = null;

	/* Referenz auf ein eventuell ge&ouml;ffnetes &Uuml;bersichtsfenster. */
	private FrameOverview fov = null;
	private GUIBundle guiBundle = null;

	/*
	 * Der Men&uuml;punkt zum Zu-/Abschalten der Referenzanzeige im aktuellen View.
	 */
	private JCheckBoxMenuItem menuitemswitchrelationtoview = null;

	/*
	 * Der Men&uuml;punkt zum Zu-/Abschalten der technischen Felder im aktuellen View.
	 */
	private JCheckBoxMenuItem menuitemswitchtechnicalfieldstoview = null;

	/*
	 * Der Men&uuml;punkt zum Zu-/Abschalten der transienten Felder im aktuellen View.
	 */
	private JCheckBoxMenuItem menuitemswitchtransientfieldstoview = null;

	/* Referenz auf das Datei-Men&uuml; zwecks Dateinamencache-Erweiterung. */
	private JMenu dateimenu = null;

	/* Referenz auf das View-Men&uuml; zwecks View-Erweiterung. */
	private JMenu viewmenu = null;

	/* Cache der Namen der letzten gespeicherten Dateien. */
	private JMenuItem[] dateinamencache = new JMenuItem[20];

	/*
	 * MenuItem, das die Aktion zum Einf&uuml;gen einer Tabelle in das Datenmodell ausl&ouml;st.
	 */
	private JMenuItem menuitemneutabelle = null;

	/*
	 * MenuItem, das die Aktion zum Einf&uuml;gen einer Standardtabelle in das Datenmodell ausl&ouml;st.
	 */
	private JMenuItem menuitemneustandardtabelle = null;

	/*
	 * MenuItem, das die Aktion zum Einf&uuml;gen einer Tabelle in eine View ausl&ouml;st.
	 */
	private JMenuItem menuitemtabletoview = null;

	/* Semaphore zur Absicherung der Speicherns und des Beendens. */
	private Semaphore semSpeichern = new Semaphore();

	/* Der Dateiname des aktuell bearbeiteten Diagramms. */
	private String dateiname = "unbenannt.ads";

	/* Der Pfad, in dem die Diagramme gespeichert werden sollen. */
	private String datenpfad = ".";

	/*
	 * Die Benutzerinformationen des Benutzer der Installation, z. B. fuer Generatoren.
	 */
	private UserInformation userInformation = null;
	private DiagramGUIObjectCreator guiObjectCreator = null;

	private ModelCheckerMessage[] lastModelCheckerMessages = null;

	private JMenuItem menuItemFileSave = null;
	private JMenu menuGenerate = null;
	private ModelCheckerMessageListFrame modelCheckerMessageListFrame = null;

	private VersionStringBuilder versionStringBuilder = new VersionStringBuilder();

	private List<ModelChecker> buildInModelCheckers = null;

	/**
	 * Erzeugt einen neuen FrameArchimedes anhand der &uuml;bergebenen Parameter.
	 *
	 * @param guiBundle  A bundle with GUI information.
	 * @param serverMode Set this flag if the application is started in server mode.
	 *
	 * @changed OLI 30.09.2007 - Ersetzen des Aufrufs der aufgehobenen Methode <TT>setRasterwidth(int)</TT> des
	 *          <TT>component</TT>-Objekts gegen den der Methode <TT>setRasterWidth(int)</TT>.
	 */
	public FrameArchimedes(final GUIBundle guiBundle, final boolean serverMode) {
		super("", guiBundle.getInifile());
		this.serverMode = serverMode;
		GlobalEventManager.AddGlobalListener(this);
		this.guiBundle = guiBundle;
		buildInModelCheckers = Arrays
				.asList(
						new ModelCheckerDomainNotInuse(guiBundle),
						new ModelCheckerDomainSetForAllColumns(guiBundle),
						new ModelCheckerPotentialForeignKeyNotSet(guiBundle),
						new ModelCheckerNoPrimaryKeySet(guiBundle));
		int i = 0;
		JMenuItem menuItem = null;

		if (ini.readBool("MemoryMonitor", "Show", false)) {
			MemoryMonitor.showMemoryMonitor();
		}

		this.updateHeadline();
		this.diagramm = Archimedes.Factory.createDiagramm();
		this.diagramm.addDataModelListener(this);
		this.diagramm.addDiagrammModelListener(this);
		this.diagramm.addGUIDiagramModelListener(this);
		this.diagramm
				.getViews()
				.add(
						(GUIViewModel) Archimedes.Factory
								.createMainView("Main", "Diese Sicht beinhaltet alle Tabellen des Schemas", true));
		this.guiObjectCreator = new DiagramGUIObjectCreator(this.diagramm);
		this.component = new DiagramComponentPanel<GUIObjectTypes>(
				this.diagramm.getViews().get(0),
				ComponentDiagramm.DOTSPERPAGEX * ComponentDiagramm.PAGESPERROW,
				ComponentDiagramm.DOTSPERPAGEY * ComponentDiagramm.PAGESPERCOLUMN,
				this.diagramm,
				this.guiObjectCreator,
				this,
				new ArchimedesStatusBarRenderer(),
				new DefaultCopyAndPasteController());
		this.component.updateWarnings(this.guiBundle.getResourceText(RES_WARNING_MESSAGES_NO_WARNINGS));
		this.component.addDiagramComponentPanelListener(this);
		this.datenpfad = this.getInifile().readStr("Datei", "Default", this.datenpfad);
		this.component.setRasterWidth(this.getInifile().readInt("Diagram", "Raster", 50));
		this.component.setSmartPosRadius(this.getInifile().readInt("Diagram", "SmartPosRadius", 100));
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				doBeenden();
			}

			@Override
			public void windowClosed(final WindowEvent e) {
				doBeenden();
			}
		});

		final JMenuBar menuBar = new JMenuBar();
		JMenu menu = this.createMenu("menu.file", "fileopen");
		this.dateimenu = menu;
		menu.add(this.createMenuItem("menu.file.item.open", "fileopen", e -> doDateiOeffnen()));
		this.menuItemFileSave = this.createMenuItem("menu.file.item.save", "filesave", this);
		menu.add(this.menuItemFileSave);
		menu.add(this.createMenuItem("menu.file.item.save.as", "save_as", e -> doDateiSpeichernUnter()));
		menu.add(new JSeparator());
		menu.add(this.createMenuItem("menu.file.item.print", "fileprint", e -> doDateiDrucken()));
		menu.add(this.createMenuItem("menu.file.item.export.graphic", null, e -> doDateiGrafikexport()));
		menu.add(new JSeparator());
		menu.add(this.createMenuItem("menu.file.item.quit", "exit", e -> doBeenden()));
		menu.add(new JSeparator());
		int j = 0;
		final int anzahl = ini.readInt("Dateicache", "Anzahl", 0);
		this.actionListenerDateiCache = e -> {
			final JMenuItem item = (JMenuItem) e.getSource();
			final String s = item.getText();
			doDateiOeffnen(s);
		};
		for (i = 0; i < anzahl; i++) {
			final String s = ini.readStr("Dateicache", "Datei" + i, null);
			if (s != null) {
				j++;
				menuItem = new JMenuItem("" /* + j + " " */ + s);
				menuItem.addActionListener(this.actionListenerDateiCache);
				menu.add(menuItem);
				this.dateinamencache[j - 1] = menuItem;
			}
		}
		menuBar.add(menu);
		menu = this.createMenu("menu.import", "import");
		menu.add(this.createMenuItem("menu.import.item.import.jdbc", "database", e -> doImportFromJDBC()));
		menu.add(this.createMenuItem("menu.import.item.import.liquibase", "xml-file", e -> doImportFromLiquibase()));
		menu.add(new JSeparator());
		menu.add(this.createMenuItem("menu.import.item.update.jdbc", "database", e -> doUpdateFromJDBC()));
		menuBar.add(menu);
		menu = this.createMenu("menu.export", "export");
		menu.add(this.createMenuItem("menu.export.item.export.liquibase", "xml-file", e -> doExportToLiquibase()));
		menu.add(new JSeparator());
		menu.add(this.createMenuItem("menu.export.item.explore.jdbc", "explore", e -> doExploreJDBC()));
		menuBar.add(menu);
		menu = this.createMenu("menu.new", "filenew");
		this.menuitemneutabelle = this.createMenuItem("menu.new.item.table", "tablenew", e -> doNeuTabelle(false));
		menu.add(this.menuitemneutabelle);
		this.menuitemneustandardtabelle =
				this.createMenuItem("menu.new.item.table.standard", null, e -> doNeuTabelle(true));
		menu.add(this.menuitemneustandardtabelle);
		menu.add(this.createMenuItem("menu.new.item.read.template", "tableimport", e -> doNeuTemplateEinlesen()));
		menu.add(new JSeparator());
		menu.add(this.createMenuItem("menu.new.item.create.domains", null, e -> doDomainsAnlegen()));
		menuBar.add(menu);
		menu = this.createMenu("menu.edit", "edit");
		menu.add(this.createMenuItem("menu.edit.item.domains", null, e -> doBearbeitenDomains()));
		menu.add(this.createMenuItem("menu.edit.item.sequences", null, e -> doEditSequences()));
		menu.add(this.createMenuItem("menu.edit.item.complex.indices", null, e -> doBearbeitenKomplexeIndices()));
		menu.add(this.createMenuItem("menu.edit.item.stereotypes", null, e -> doBearbeitenStereotype()));
		JMenuItem menuItemEditViews = this.createMenuItem("menu.edit.item.views", null, e -> doBearbeitenViews());
		menuItemEditViews.setEnabled(isViewLogicEnabled());
		menu.add(menuItemEditViews);
		menu.add(new JSeparator());
		menu.add(this.createMenuItem("menu.edit.item.diagramm.parameters", null, e -> doBearbeitenDiagrammparameter()));
		menu
				.add(
						this
								.createMenuItem(
										"menu.edit.item.database.connections",
										"database",
										e -> doBearbeitenDatenbankverbindungen()));
		menu
				.add(
						this
								.createMenuItem(
										"menu.edit.item.base.configuration",
										"configure",
										e -> doBearbeitenBenutzerParameter()));
		menu.add(this.createMenuItem("menu.edit.item.new.version", null, e -> {
			final VersionIncrementer.State rc = new VersionIncrementer().increment(diagramm);
			if (rc == VersionIncrementer.State.SCRIPTS_CLEANED_ONLY) {
				final Object version = JOptionPane
						.showInputDialog(
								null,
								StrUtil.ToHTML("Version konnte nicht erh&ouml;ht werden!"),
								StrUtil.ToHTML("Problem: Versionserh&ouml;hung"),
								JOptionPane.QUESTION_MESSAGE,
								null,
								null,
								diagramm.getVersion());
				if (version != null) {
					diagramm.setVersion(String.valueOf(version));
				}
			}
			raiseAltered();
			component.doRepaint();
		}));
		menu.add(new JSeparator());
		JMenuItem menuItemUpdateScript = this
				.createMenuItem(
						"menu.edit.item.create.update.script",
						"generate",
						e -> doBearbeitenUpdateScriptErstellenNeu());
		menuItemUpdateScript.setEnabled(isSQLLogicEnabled());
		menu.add(menuItemUpdateScript);
		menu.addSeparator();
		JMenuItem menuItemCompareModels = this
				.createMenuItem("menu.edit.item.compare.models", "compare", e -> doBearbeitenDatenmodellVergleichen());
		menuItemCompareModels.setEnabled(isSQLLogicEnabled());
		menu.add(menuItemCompareModels);
		menu.addSeparator();
		menu.add(this.createMenuItem("menu.edit.item.run.javascript", "run", e -> doBearbeitenRunJavaScript()));
		menuBar.add(menu);
		menu = this.createMenu("menu.zoom", "zoom");
		for (i = 1; i < 11; i++) {
			menuItem = new JMenuItem(
					this.guiBundle.getResourceText("menu.zoom.item.zoom.title").replace("{0}", String.valueOf(i * 10)));
			menuItem.setMnemonic(new Integer(i).toString().charAt(0));
			if (i == 10) {
				menuItem.setMnemonic('0');
			}

			class ZoomActionListener implements ActionListener {
				private double faktor = 1.0;

				public ZoomActionListener(final double faktor) {
					super();
					this.faktor = faktor;
				}

				@Override
				public void actionPerformed(final ActionEvent e) {
					doZoom(this.faktor);
				}
			}
			;

			menuItem.addActionListener(new ZoomActionListener(0.1d * i));
			menu.add(menuItem);
		}

		menu.add(new JSeparator());
		menu.add(this.createMenuItem("menu.zoom.item.overview", "overview", e -> doOpenOverview()));
		menu.add(new JSeparator());
		menu.add(this.createMenuItem("menu.zoom.item.find.table", "find", e -> doTabelleFinden()));
		menuBar.add(menu);
		this.menuGenerate = this.createMenu("menu.generate", "generate");
		this.menuGenerate.add(this.createMenuItem("menu.generate.item.generate", "generate", e -> doGenerateCode()));
		this.menuGenerate
				.add(this.createMenuItem("menu.generate.item.generator.options", null, e -> doGeneratorOptionen()));
		menuBar.add(this.menuGenerate);
		this.viewmenu = this.createMenu("menu.views", "views");
		this.viewmenu.setEnabled(isViewLogicEnabled());
		this.menuitemtabletoview =
				this.createMenuItem("menu.views.item.add.table", "tableinsert", e -> doViewsEinfuegen());
		this.menuitemswitchrelationtoview = new JCheckBoxMenuItem(
				this.guiBundle.getResourceText("menu.views.item.show.relations.title"),
				((ViewModel) this.component.getView()).isShowReferencedColumns());
		this.menuitemswitchrelationtoview.addActionListener(e -> {
			((ViewModel) component.getView()).setShowReferencedColumns(((JCheckBoxMenuItem) e.getSource()).getState());
			raiseAltered();
			doRepaint();
		});
		this.menuitemswitchtechnicalfieldstoview = new JCheckBoxMenuItem(
				this.guiBundle.getResourceText("menu.views.item.hide.technical.fields.title"),
				((ViewModel) this.component.getView()).isHideTechnicalFields());
		this.menuitemswitchtechnicalfieldstoview.addActionListener(e -> {
			((ViewModel) component.getView()).setHideTechnicalFields(((JCheckBoxMenuItem) e.getSource()).getState());
			raiseAltered();
			doRepaint();
		});
		this.menuitemswitchtransientfieldstoview = new JCheckBoxMenuItem(
				this.guiBundle.getResourceText("menu.views.item.hide.transient.fields.title"),
				((ViewModel) this.component.getView()).isHideTransientFields());
		this.menuitemswitchtransientfieldstoview.addActionListener(e -> {
			((ViewModel) component.getView()).setHideTransientFields(((JCheckBoxMenuItem) e.getSource()).getState());
			raiseAltered();
			doRepaint();
		});
		this.updateViewMenu(viewmenu, this.diagramm.getViews());
		menuBar.add(viewmenu);
		menu = this.createMenu("menu.tools", "tools");
		menu
				.add(
						this
								.createMenuItem(
										"menu.tools.item.remove.reference.angles",
										null,
										e -> component.removeAllAnglesFromRelations()));
		menu = this.createMenu("menu.info", "clipboard");
		menu.add(this.createMenuItem("menu.info.item.versionsToClipboard", "copy", e -> doInfoVersionsToClipboard()));
		menuBar.add(menu);
		this.setJMenuBar(menuBar);

		final JPanel panelStatus = new JPanel(new GridLayout(1, 5, Constants.HGAP, Constants.VGAP));
		panelStatus.setBorder(new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP));

		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.component);
		this.setContentPane(panel);
		this.setVisible(true);
		this.createUserInformations(ini);
		this
				.setBounds(
						ini.readInt("MainWindow", "x", 100),
						ini.readInt("MainWindow", "y", 100),
						ini.readInt("MainWindow", "width", 600),
						ini.readInt("MainWindow", "height", 480));
	}

	private boolean isViewLogicEnabled() {
		return Boolean.getBoolean("archimedes.diagram.view.logic.enabled");
	}

	private boolean isSQLLogicEnabled() {
		return Boolean.getBoolean("archimedes.diagram.sql.logic.enabled");
	}

	private JMenu createMenu(final String resourceId, final String imageId) {
		final JMenu m = new JMenu(this.guiBundle.getResourceText(resourceId + ".title"));
		final String mnemonic = this.guiBundle.getResourceText(resourceId + ".mnemonic");

		if ((mnemonic != null) && !mnemonic.isEmpty()) {
			m.setMnemonic(mnemonic.charAt(0));
		}

		if (imageId != null) {
			m.setIcon(this.guiBundle.getImageProvider().getImageIcon(imageId));
		}

		return m;
	}

	private JMenuItem createMenuItem(final String resourceId, final String imageId, final ActionListener al) {
		final JMenuItem m = new JMenuItem(this.guiBundle.getResourceText(resourceId + ".title"));
		final String mnemonic = this.guiBundle.getResourceText(resourceId + ".mnemonic");

		if ((mnemonic != null) && !mnemonic.isEmpty()) {
			m.setMnemonic(mnemonic.charAt(0));
		}

		if (imageId != null) {
			m.setIcon(this.guiBundle.getImageProvider().getImageIcon(imageId));
		}

		if (al != null) {
			m.addActionListener(al);
		}

		return m;
	}

	private void createUserInformations(final Inifile ini) {
		this.userInformation = new DefaultUserInformation(
				System.getProperty("archimedes.user.name", "UNKNOWN"),
				System.getProperty("archimedes.user.token", "N/A"),
				System.getProperty("archimedes.user.company", "UNKNOWN"));
	}

	/**
	 * Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer den Men&uuml;punkt &Ouml;ffnen w&auml;hlt.
	 */
	public void doDateiOeffnen() {
		if (this.diagramm.isAltered()) {
			final int option = JOptionPane
					.showConfirmDialog(
							null,
							StrUtil
									.FromHTML(
											"Das Diagramm "
													+ "ist ge&auml;ndert worden!\nM&ouml;chten Sie es speichern?"),
							"Diagramm nicht" + " gespeichert",
							JOptionPane.YES_NO_CANCEL_OPTION);

			if (option == JOptionPane.YES_OPTION) {
				if ((this.dateiname.length() == 0) || (this.dateiname.equalsIgnoreCase("unbenannt.ads"))) {
					this.doDateiSpeichernUnter();
				} else {
					LOG.info("writing " + this.dateiname + " ...");
					this.doDateiSpeichern(this.dateiname, true);
				}
			} else if (option == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}

		String dn = "";
		final JFileChooser fc = new JFileChooser(datenpfad);
		fc.setAcceptAllFileFilterUsed(false);

		final ExtensionFileFilter eff = new ExtensionFileFilter(new String[] { "ads", "xml" });
		fc.setFileFilter(eff);
		fc.setCurrentDirectory(new File(this.datenpfad));

		final int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dn = fc.getSelectedFile().getAbsolutePath();
			this.doDateiOeffnen(dn, false);
		}
	}

	/**
	 * Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer den Men&uuml;punkt &Ouml;ffnen w&auml;hlt.
	 *
	 * @param dn Der Name der einzulesenden Datei.
	 */
	public void doDateiOeffnen(final String dn) {
		this.doDateiOeffnen(dn, true);
	}

	/**
	 * This method is called if the user clicks the menu item "file / open".
	 *
	 * @param fileName The name of the file to read.
	 * @param ask      If this flag is set in case of an altered data model, it will be asked for saving the current
	 *                 model.
	 *
	 * @changed OLI 18.03.2016 - Comment to English. Introduced XML files.
	 */
	public void doDateiOeffnen(final String fileName, final boolean ask) {
		if (ask && this.diagramm.isAltered()) {
			final boolean errors = this.isErrorsFound(this.diagramm, false);
			final int option = JOptionPane
					.showConfirmDialog(
							null,
							this.guiBundle
									.getResourceText(
											(errors
													? "archimedes.open.file.altered.warning.with.errors.text"
													: "archimedes.open.file.altered.warning.text")),
							this.guiBundle.getResourceText("archimedes.open.file.altered.warning.title"),
							JOptionPane.YES_NO_CANCEL_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				if ((this.dateiname.length() == 0) || (this.dateiname.equalsIgnoreCase("unbenannt.ads"))) {
					this.doDateiSpeichernUnter();
				} else {
					LOG.info("writing " + this.dateiname + " ...");
					this.doDateiSpeichern(this.dateiname, true);
				}
			} else if (option == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}

		try {
			this.dateiname = fileName;

			if (fileName.toLowerCase().endsWith(".ads")) {
				final StructuredTextFile stf = new StructuredTextFile(fileName);
				stf.setHTMLCoding(true);
				stf.load();
				this.diagramm = this.diagramm.createDiagramm(stf);
			} else if (fileName.toLowerCase().endsWith(".xml")) {
				final ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
				this.diagramm = (Diagramm) reader.read(fileName);
			}

			this.diagramm.addDataModelListener(this);
			this.diagramm.addDiagrammModelListener(this);
			this.diagramm.addGUIDiagramModelListener(this);
			this.guiObjectCreator.setDiagram(this.diagramm);
			this.component.setView(this.diagramm.getViews().get(0));
			this.component.setDiagram(this.diagramm);

			if (this.fov != null) {
				this.fov.setDiagramm(this.diagramm);
			}

			this.updateViewMenu(this.viewmenu, this.diagramm.getViews());
			menuitemneustandardtabelle.setEnabled(true);
			menuitemneutabelle.setEnabled(true);
			this.diagramm.clearAltered();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		this.updateHeadline();
		this.doDateinamenCacheAktualisieren(fileName);

		for (final TableModel tm : this.diagramm.getTables()) {
			LOG.info("# ");

			for (int ii = 0, lenii = ((Tabelle) tm).getTabellenspaltenCount(); ii < lenii; ii++) {
				LOG.info(((Tabelle) tm).getTabellenspalteAt(ii) + ", ");
			}

			LOG.info("");
		}

		this.startChecker();
	}

	private void startChecker() {
		final String path = this.diagramm.getCodePfad().replace("~", System.getProperty("user.home"));
		List<ModelChecker> modelCheckers = new ArrayList<>(buildInModelCheckers);
		for (Object cf : this.getCodeFactories(path)) {
			if (cf instanceof CodeFactory) {
				modelCheckers.addAll(Arrays.asList(((CodeFactory) cf).getModelCheckers()));
			}
		}
		new ModelCheckerThread(this, this.diagramm, modelCheckers);
	}

	/**
	 * Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer den Men&uuml;punkt Speichern unter... w&auml;hlt.
	 */
	public void doDateiSpeichernUnter() {
		String dn = "";
		final JFileChooser fc = new JFileChooser(datenpfad);
		fc.setAcceptAllFileFilterUsed(false);

		final ExtensionFileFilter eff = new ExtensionFileFilter(new String[] { "ads", "xml" });
		fc.setFileFilter(eff);
		fc.setCurrentDirectory(new File(this.datenpfad));
		fc.setSelectedFile(new File(this.dateiname));

		final int returnVal = fc.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dn = fc.getSelectedFile().getAbsolutePath();
			this.doDateiSpeichern(dn, false);
		}
	}

	/**
	 * Stores the data model in the file system.
	 *
	 * @param fileName     The name of the file which the data model is to store into.
	 * @param ignoreErrors Set this flag if a model with errors is to be stored also.
	 *
	 * @changed OLI 16.08.2008 - There will be two different versions of the model stored: one is for editing the data
	 *          model, the other for using as application layer in Archimedes applications (this works for ADS files
	 *          only).
	 * @changed OLI 11.07.2016 - Refactored.
	 */
	public void doDateiSpeichern(final String name, final boolean ignoreErrors) {
		if (!ignoreErrors && this.isErrorsFound(this.diagramm, true)) {
			return;
		}

		try {
			this.semSpeichern.P();

			try {
				if (name.endsWith("ads")) {
					final int option = JOptionPane
							.showConfirmDialog(
									null,
									this.guiBundle.getResourceText("archimedes.save.file.old.format.warning.text"),
									this.guiBundle.getResourceText("archimedes.save.file.old.format.warning.title"),
									JOptionPane.YES_NO_OPTION);

					if (option == JOptionPane.YES_OPTION) {
						StructuredTextFile stf = this.diagramm.toSTF(DiagramSaveMode.REGULAR);
						stf.setFilename(name);
						stf.save();
						stf = this.diagramm.toSTF(DiagramSaveMode.APPLICATION);
						stf.setFilename(name.concat("app"));
						stf.save(false);
						this.dateiname = name;
						this.diagramm.clearAltered();
					}
				} else {
					final FileWriter fw = new FileWriter(name, false);
					final BufferedWriter writer = new BufferedWriter(fw);
					writer
							.write(
									new DiagramXMLBuilder()
											.buildXMLContentForDiagram(
													this.diagramm,
													Archimedes.PALETTE.getColors().toArray(new ExtendedColor[0])));
					writer.close();
					fw.close();
					this.dateiname = name;
					this.diagramm.clearAltered();
					LOG.info("Written data model to file: " + name);
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("Error while writing date model to file: " + name);
				JOptionPane
						.showMessageDialog(
								null,
								this.guiBundle.getResourceText("archimedes.save.file.error.text"),
								this.guiBundle.getResourceText("archimedes.save.file.error.title"),
								JOptionPane.YES_OPTION);
			}

			this.updateHeadline();
			this.doDateinamenCacheAktualisieren(name);
		} catch (java.lang.InterruptedException ie) {
			ie.printStackTrace();
		}

		this.semSpeichern.V();
	}

	private boolean isErrorsFound(final DataModel dm, final boolean showErrors) {
		final String path = this.diagramm.getCodePfad().replace("~", System.getProperty("user.home"));
		final List<ModelCheckerMessage> messages = new LinkedList<>();
		for (ModelChecker buildInModelChecker : buildInModelCheckers) {
			processModelChecker(this.diagramm, buildInModelChecker, messages);
		}
		for (Object cfo : this.getCodeFactories(path)) {
			if (cfo instanceof CodeFactory) {
				try {
					final CodeFactory cf = (CodeFactory) cfo;
					cf.setDataModel(this.diagramm);
					cf.setGUIBundle(getGUIBundle(this.guiBundle, cf.getResourceBundleNames()));
					cf.setModelCheckerMessageListFrameListeners(this);
					for (final ModelChecker mc : cf.getModelCheckers()) {
						processModelChecker(this.diagramm, mc, messages);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (!messages.isEmpty()) {
			if (showErrors) {
				new ModelCheckerMessageListDialog(
						this,
						this.guiBundle,
						messages.toArray(new ModelCheckerMessage[0]),
						false);
			}
			return true;
		}
		return false;
	}

	private void processModelChecker(DataModel dm, ModelChecker mc, List<ModelCheckerMessage> messages) {
		final ModelCheckerMessage[] mcms = mc.check(dm);
		if (mcms.length > 0) {
			for (final ModelCheckerMessage mcm : mcms) {
				if (mcm.getLevel() == Level.ERROR) {
					messages.add(mcm);
				}
			}
		}
	}

	private void doDateinamenCacheAktualisieren(final String name) {
		int index = -1;
		int len = 0;
		for (len = 0; len < this.dateinamencache.length; len++) {
			if (this.dateinamencache[len] == null) {
				break;
			}
		}
		for (int i = 0; i < len; i++) {
			final String s = this.dateinamencache[i].getText();
			if (s.equalsIgnoreCase(name)) {
				index = i;
				break;
			}
		}
		if (index > -1) {
			for (int i = index; i > 0; i--) {
				final String s = this.dateinamencache[i - 1].getText();
				this.dateinamencache[i].setText( /* "" + (i+1) + " " + */s);
			}
			this.dateinamencache[0].setText( /* "1 " + */name);
			return;
		}
		if (len < this.dateinamencache.length) {
			this.dateinamencache[len] = new JMenuItem("");
			this.dateinamencache[len].addActionListener(this.actionListenerDateiCache);
			this.dateimenu.add(this.dateinamencache[len]);
		} else {
			len--;
		}
		if (len > 0) {
			for (int i = len; i > 0; i--) {
				final String s = this.dateinamencache[i - 1].getText();
				this.dateinamencache[i].setText( /* "" + (i+1) + " " + */s);
			}
		}
		this.dateinamencache[0].setText( /* "" + 1 + " " + */name);
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Men&uuml;punkt Datei / Drucken bet&auml;tigt hat.
	 */
	public void doDateiDrucken() {
		new DiagramPrinter()
				.print(
						this,
						this.getInifile(),
						this.component,
						this.getInifile().readBool("Print", "PDF", false),
						this.getInifile().readStr("Print", "Printername", "normal_gray"),
						this.getInifile().readStr("Print", "Printfilename", "archimedes"));
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Men&uuml;punkt Datei / Grafikexport aufruft.
	 *
	 * @changed OLI 01.01.2008 - Anpassung an die neue Signatur des paint-Aufrufes der ComponentDiagramm zur
	 *          Kennzeichnung von Ausdrucken oder Grafikexporten.<BR>
	 */
	public void doDateiGrafikexport() {
		new GraphicExporter().export(this, this.dateiname, this.datenpfad, this.component);
	}

	/**
	 * Diese Methode wird aufgerufen, wenn die Applikation beendet worden ist.
	 *
	 * @changed OLI 13.05.2008 - Korrektur Umlaut (ge&auml;ndert ...).
	 */
	public void doBeenden() {
		try {
			this.semSpeichern.P();
			final boolean errorsFound = this.isErrorsFound(this.diagramm, false);
			if (this.diagramm.isAltered()) {
				final int answer = JOptionPane
						.showConfirmDialog(
								null,
								StrUtil
										.FromHTML(
												"Das Diagramm ist ge&auml;ndert worden!\n"
														+ (errorsFound ? "DAS MODELL ENTH&Auml;LT FEHLER!\n" : "")
														+ "M&ouml;chten Sie es " + (errorsFound ? "trotzdem " : "")
														+ "speichern?"),
								"Diagramm nicht gespeichert",
								JOptionPane.YES_NO_CANCEL_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					this.semSpeichern.V();
					if ((this.dateiname.length() == 0) || (this.dateiname.equalsIgnoreCase("unbenannt.ads"))) {
						this.doDateiSpeichernUnter();
					} else {
						this.doDateiSpeichern(this.dateiname, true);
					}
				} else if (answer == JOptionPane.CANCEL_OPTION) {
					this.semSpeichern.V();
					return;
				}
			}
			try {
				int len = 0;
				for (len = 0; len < this.dateinamencache.length; len++) {
					if (this.dateinamencache[len] == null) {
						break;
					}
					final String s = this.dateinamencache[len].getText();
					// s = s.substring(2, s.length());
					this.ini.writeStr("Dateicache", "Datei" + len, s);
				}
				this.setExtendedState(JFrame.NORMAL);
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
				this.ini.writeInt("Dateicache", "Anzahl", len);
				this.ini.writeInt("MainWindow", "x", this.getX());
				this.ini.writeInt("MainWindow", "y", this.getY());
				this.ini.writeInt("MainWindow", "width", this.getWidth());
				this.ini.writeInt("MainWindow", "height", this.getHeight());
			} catch (IOException ioe) {
				LOG.error("\n\nDateinamencache konnte nicht gespeichert werden!\n\n", ioe);
			}
			try {
				this.ini.save();
			} catch (Exception e) {
				LOG.error("\n\nInidatei konnte nicht gespeichert werden!\n\n", e);
			}
			this.setVisible(false);
			this.dispose();
			System.exit(0);
		} catch (java.lang.InterruptedException ie) {
			ie.printStackTrace();
		}
		this.semSpeichern.V();
	}

	/**
	 * Imports a diagram from a JDBC connection.
	 * 
	 * @changed OLI 28.09.2019 - Added.
	 */
	public void doImportFromJDBC() {
		new JDBCModelImporter().importModel(diagramm, guiBundle, this);
	}

	/**
	 * @changed OLI 17.02.2021 - Added.
	 */
	public void doImportFromLiquibase() {
		try {
			String fileName = "change-log-master.xml"; // getFileName();
			if (fileName == null) {
				return;
			}
			new File("tmp-db").mkdir();
			final Connection connection = ConnectionManager
					.GetConnection(
							new JDBCDataSourceRecord("org.h2.Driver", "jdbc:h2:mem:updatedb;MODE=PostgreSQL", "", ""));
			Database database =
					DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
			Liquibase liquibase = new liquibase.Liquibase(
					fileName,
					new FileSystemResourceAccessor(
							new File("/home/ollie/eclipse-workspace/library/src/main/resources/db/change-log")),
					database);
			liquibase.update(new Contexts(), new LabelExpression());
			liquibase.close();
			JDBCImportData importData = new JDBCImportData()
					.setAdjustment(Adjustment.LEFT_BY_NAME)
					.setConnection(connection)
					.setIgnoreIndices(false)
					.setImportOnlyTablePatterns("*")
					.setPassword("")
					.setSchema("PUBLIC");
			final FrameArchimedes frameArchimedes = this;
			final Thread t = new Thread(() -> {
				ModelReaderProgressMonitor mrpm = new ModelReaderProgressMonitor(guiBundle, 5);
				try {
					Diagramm d = (Diagramm) new JDBCImportManager().importDiagram(importData, mrpm::update);
					if (d != null) {
						diagramm = d;
						diagramm.addDataModelListener(frameArchimedes);
						diagramm.addDiagrammModelListener(frameArchimedes);
						diagramm.addGUIDiagramModelListener(frameArchimedes);
						guiObjectCreator.setDiagram(diagramm);
						component.setView(diagramm.getViews().get(0));
						component.setDiagram(diagramm);
						if (fov != null) {
							fov.setDiagramm(diagramm);
						}
						updateViewMenu(viewmenu, diagramm.getViews());
						diagramm.clearAltered();
						mrpm.enableCloseButton();
					}
				} catch (Exception e) {
					mrpm.setVisible(false);
					LOG.error("error detected while importing from JDBC connection: " + e.getMessage());
					new ExceptionDialog(
							e,
							guiBundle.getResourceText("Exception.ImportModel.text", e.getMessage()),
							guiBundle);
				} finally {
					if (connection != null) {
						try {
							connection.close();
						} catch (SQLException e) {
							new ExceptionDialog(
									e,
									guiBundle.getResourceText("Exception.ImportModel.text", e.getMessage()),
									guiBundle);
						}
					}
				}
			});
			t.start();
		} catch (Exception e) {
			new ExceptionDialog(e, guiBundle.getResourceText("Exception.ImportModel.text", e.getMessage()), guiBundle);
		}
	}

	private String getFileName() {
		String fileName = null;
		JFileChooser fc = new JFileChooser(datenpfad);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new ExtensionFileFilter(new String[] { "xml" }));
		fc.setCurrentDirectory(new File(this.datenpfad));
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			fileName = fc.getSelectedFile().getAbsolutePath();
		}
		return fileName;
	}

	/**
	 * Updates the current diagram by a selected JDBC source.
	 * 
	 * @changed OLI 09.02.2021 - Added.
	 */
	public void doUpdateFromJDBC() {
		new JDBCModelUpdater().updateModel(diagramm, guiBundle, this, component);
	}

	/**
	 * Creates a Liquibase script to update a database based on the current diagram.
	 * 
	 * @changed OLI 24.02.2021 - Added.
	 */
	public void doExportToLiquibase() {
		new LiquibaseScriptCreator().createScript(diagramm, guiBundle);
	}

	/**
	 * Creates a report with the structure of a selected JDBC data source.
	 * 
	 * @changed OLI 26.02.2021 - Added.
	 */
	public void doExploreJDBC() {
		new JDBCModelExplorer().createStructureReport(diagramm.getDatabaseConnections(), guiBundle);
	}

	/**
	 * This method will be called if the menu item "info / versions to clip board" is selected.
	 *
	 * @changed OLI 18.04.2017 - Added.
	 */
	public void doInfoVersionsToClipboard() {
		String versions = "";
		for (Object cf : this.getCodeFactories("")) {
			versions += this.versionStringBuilder.getVersions(((cf instanceof CodeFactory) ? (CodeFactory) cf : null))
					+ "\n";
		}
		final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(new StringSelection(versions), null);
		LOG.info("sent versions to clipboard");
		JOptionPane
				.showMessageDialog(
						this,
						this.guiBundle.getResourceText("versions.copiedToClipboard.message"),
						this.guiBundle.getResourceText("versions.copiedToClipboard.title"),
						JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Men&uuml;punkt Neu / Tabelle bet&auml;tigt.
	 *
	 * @param filled Wird diese Flagge gesetzt, so wird eine Tabelle mit den &uuml;blichen Standard-Einstellungen und
	 *               -Spalten generiert.
	 */
	public void doNeuTabelle(final boolean filled) {
		this.component.setInsertMode(true);
		this.component.setStandardMode(filled);
		this.component.setGUIObjectToCreateIdentifier(GUIObjectTypes.TABLE);
	}

	/**
	 * Diese Methode &auml;ndert den Namen der Standard-Template-Datei.
	 */
	public void doNeuTemplateEinlesen() {
		String fn = "";
		final JFileChooser fc = new JFileChooser(datenpfad);
		fc.setAcceptAllFileFilterUsed(false);

		final ExtensionFileFilter eff = new ExtensionFileFilter(new String[] { "properties" });
		fc.setFileFilter(eff);
		fc.setCurrentDirectory(new File(this.datenpfad));

		final int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fn = fc.getSelectedFile().getAbsolutePath();
			Archimedes.Factory.setTemplateFilename(fn);
			this.menuitemneustandardtabelle.setText("Standard-Tabelle (" + fn + ")");
		}
	}

	/**
	 * Diese Methode &ouml;ffnet ein zus&auml;tzliches Fenster mit einer 10%-Darstellung des Datenmodells.
	 *
	 * @changed OLI 16.12.2008 - Hinzugef&uuml;gt.
	 */
	public void doOpenOverview() {
		if (this.fov == null) {
			this.fov = new FrameOverview(StrUtil.FromHTML("&Uuml;bersicht"), this.getInifile(), this.diagramm, this);
		} else {
			this.fov.toFront();
			this.fov.setVisible(true);
		}
	}

	/**
	 * Diese Methode erzeugt oder aktualisiert (falls bereits vorhanden) einen Satz von Domains, der immer wieder gern
	 * genutzt wird :o).
	 */
	public void doDomainsAnlegen() {
		String dn = "";

		try {
			final JFileChooser fc = new JFileChooser(datenpfad);
			fc.setAcceptAllFileFilterUsed(false);

			final ExtensionFileFilter eff = new ExtensionFileFilter(new String[] { "ini" });
			fc.setFileFilter(eff);
			fc.setCurrentDirectory(new File("Archimedes-Domain-Export.ini"));

			final int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				dn = fc.getSelectedFile().getAbsolutePath();

				final Inifile ini = new Inifile(dn);
				ini.load();

				for (int i = 0, len = ini.readInt("Domains", "Number", 0); i < len; i++) {
					final String name = ini.readStr("Domains", "Name" + i, "NEUEDOMAIN");
					final int type = ini.readInt("Domains", "Type" + i, Types.INTEGER);
					final int length = ini.readInt("Domains", "Length" + i, 0);
					final int nks = ini.readInt("Domains", "Precision" + i, 0);
					final String desc = ini.readStr("Domains", "Description" + i, "-");
					final String initialValue = ini.readStr("Domains", "InitialValue" + i, "NULL");
					final String parameters = ini.readStr("Domains", "Parameter" + i, "");
					this.createDomain(name, type, length, nks, desc, initialValue, parameters);
				}
			}
		} catch (Exception e) {
			LOG.error("\n\nFehler beim Einlesen der Standarddomain-Datei -> " + dn + "!", e);
		}
	}

	private void createDomain(final String name, final int dt, final int len, final int nks, final String beschr,
			final String initialValue, final String parameters) {
		DomainModel dom = null;

		try {
			dom = this.diagramm.getDomain(name);
		} catch (NoSuchElementException nsee) {
		}

		if (dom != null) {
			dom.setName(name);
			dom.setDataType(dt);
			dom.setLength(len);
			dom.setDecimalPlace(nks);
			dom.setInitialValue(initialValue);
			dom.setParameters(parameters);
		} else {
			dom = Archimedes.Factory.createDomain(name, dt, len, nks);
			dom.setInitialValue(initialValue);
			dom.setParameters(parameters);
			this.diagramm.addDomain(dom);
		}

		dom.setComment(beschr);
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Men&uuml;punkt Bearbeiten / Domains bet&auml;tigt.
	 */
	public void doBearbeitenDomains() {
		final DiagrammModel dm = this.diagramm;
		final FrameSelectionEditorDjinn fsd = new FrameSelectionEditorDjinn(
				"Auswahl Domain",
				dm.getDomainsReference(),
				(Domain) dm.createDomain(),
				this.getInifile(),
				true) {
			@Override
			public boolean permitDelete(final Object obj) {
				final TableModel[] tables = dm.getTables();

				for (final TableModel tm : tables) {
					for (final ColumnModel cm : tm.getColumns()) {
						if (cm.getDomain() == obj) {
							JOptionPane
									.showMessageDialog(
											null,
											StrUtil
													.FromHTML(
															"Die Domain " + "wird durch " + cm.getFullName()
																	+ " genutzt!\n"
																	+ "L&ouml;schen nicht m&ouml;glich!"),
											"Referenzproblem!",
											JOptionPane.YES_OPTION);
							LOG.warn("Domain wird benutzt durch " + cm.getFullName());

							return false;
						}
					}
				}

				return true;
			}
		};
		fsd.addSelectionDjinnListener(new SelectionDjinnAdapter() {
			@Override
			public void selectionUpdated() {
				diagramm.raiseAltered();
				component.repaint();
			};
		});
	}

	/**
	 * This method will be called if the user selects the edit / sequences menu item.
	 */
	public void doEditSequences() {
		final DiagrammModel dm = this.diagramm;
		final FrameSelectionEditorDjinn fsd = new FrameSelectionEditorDjinn(
				this.guiBundle.getResourceText("sequences.maintenance.title"),
				dm.getSequencesByReference(),
				(Sequence) dm.createSequence(),
				this.getInifile(),
				true) {
			@Override
			public boolean permitDelete(final Object obj) {
				for (final TableModel tm : dm.getTables()) {
					for (final ColumnModel cm : tm.getColumns()) {
						if (cm.getSequenceForKeyGeneration() == obj) {
							JOptionPane
									.showMessageDialog(
											null,
											guiBundle
													.getResourceText("sequences.maintenance.permit.delete.text")
													.replace("$NAME$", ((SequenceModel) obj).getName())
													.replace("$COLUMNNAME$", cm.getFullName()),
											guiBundle.getResourceText("sequences.maintenance.permit.delete.title"),
											JOptionPane.YES_OPTION);

							return false;
						}
					}
				}

				return true;
			}
		};
		fsd.addSelectionDjinnListener(new SelectionDjinnAdapter() {
			@Override
			public void selectionUpdated() {
				diagramm.raiseAltered();
			};
		});
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Men&uuml;punkt Bearbeiten / Komplexe Indices aufruft.
	 */
	public void doBearbeitenKomplexeIndices() {
		new ComplexIndicesAdministrationFrame(
				"Complex index administration",
				this.getInifile(),
				this.diagramm,
				this.diagramm.getTabellen(),
				this.diagramm);
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Men&uuml;punkt Bearbeiten / Stereotype bet&auml;tigt.
	 */
	public void doBearbeitenStereotype() {
		final DiagrammModel dm = this.diagramm;
		final FrameSelectionEditorDjinn fsd = new FrameSelectionEditorDjinn(
				"Auswahl Stereotypen",
				dm.getStereotypeReference(),
				(Stereotype) dm.createStereotype(),
				this.getInifile(),
				true) {
			@Override
			public boolean permitDelete(final Object obj) {
				for (final TableModel tm : dm.getTables()) {
					for (final StereotypeModel stm : tm.getStereotypes()) {
						if (stm == obj) {
							JOptionPane
									.showMessageDialog(
											null,
											StrUtil
													.FromHTML(
															"Die " + "Stereotype wird durch " + tm.getName()
																	+ " genutzt!\n"
																	+ "L&ouml;schen nicht m&ouml;glich!"),
											"Referenzproblem!",
											JOptionPane.YES_OPTION);
							LOG.warn("Stereotype wird benutzt durch " + tm.getName());

							return false;
						}
					}
				}

				return true;
			}
		};
		fsd.addSelectionDjinnListener(new SelectionDjinnAdapter() {
			@Override
			public void selectionUpdated() {
				diagramm.raiseAltered();
				component.repaint();
			};
		});
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Men&uuml;punkt Bearbeiten / Views bet&auml;tigt.
	 */
	public void doBearbeitenViews() {
		final DiagrammModel dm = this.diagramm;
		final FrameSelectionEditorDjinn fsd = new FrameSelectionEditorDjinn(
				"Auswahl Views",
				(Vector<?>) dm.getViews(),
				(View) Archimedes.Factory.createView(),
				this.getInifile(),
				true) {
			@Override
			public boolean permitDelete(final Object obj) {
				return true;
			}
		};
		fsd.addSelectionDjinnListener(new SelectionDjinnAdapter() {
			@Override
			public void selectionUpdated() {
				diagramm.raiseAltered();
				component.repaint();
				updateViewMenu(viewmenu, diagramm.getViews());
			};
		});
	}

	/**
	 * @changed OLI 25.11.2014 - Added.
	 */
	public void doBearbeitenBenutzerParameter() {
		final BaseConfiguration bc = new BaseConfiguration();
		bc.setCompany(System.getProperty("archimedes.user.company", ""));
		bc.setDBName(System.getProperty("archimedes.user.database.name", ""));
		bc.setDBServerName(System.getProperty("archimedes.user.database.server.name", ""));
		bc.setDBUserName(System.getProperty("archimedes.user.database.user.name", ""));
		bc.setLanguage(System.getProperty("archimedes.user.language", ""));
		bc.setUserName(System.getProperty("archimedes.user.name", ""));
		bc.setUserToken(System.getProperty("archimedes.user.token", ""));

		final BaseConfigurationFrame bcf = new BaseConfigurationFrame(
				bc,
				this.guiBundle.getResourceText("BaseconfigurationFrame.title"),
				this.guiBundle);
		bcf.addEditorFrameListener(this);
		bcf.setVisible(true);
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Men&uuml;punkt Bearbeiten / Diagrammparameter bet&auml;tigt.
	 */
	public void doBearbeitenDiagrammparameter() {
		this.setPredeterminedOptionProviderForDiagram();
		new FrameEditorDjinn("Diagrammparameter", this.diagramm, false, this.getInifile()) {
			@Override
			public void doChanged(final boolean saveOnly) {
				diagramm.raiseAltered();
				component.repaint();
				// updateUserPingModelName();
			}
		};
	}

	private void setPredeterminedOptionProviderForDiagram() {
		CompoundPredeterminedOptionProvider compoundOptionProvider = new CompoundPredeterminedOptionProvider();
		compoundOptionProvider
				.addOptions(
						OptionType.COLUMN,
						new String[] { ModelCheckerPotentialForeignKeyNotSet.SUPPRESS_POTENTIAL_FK_WARNING });
		compoundOptionProvider
				.addOptions(OptionType.TABLE, new String[] { ModelCheckerNoPrimaryKeySet.SUPPRESS_NO_PK_WARNING });
		for (Object cf : getCodeFactories("")) {
			if (cf instanceof PredeterminedOptionProvider) {
				for (OptionType optionType : OptionType.values()) {
					String[] options = ((PredeterminedOptionProvider) cf).getSelectableOptions(optionType);
					if (options != null) {
						compoundOptionProvider.addOptions(optionType, options);
					}
				}
			}
		}
		this.diagramm.setPredeterminedOptionProvider(compoundOptionProvider);
	}

	/**
	 * Sets the passed user ping as new user ping for the frame.
	 *
	 * @param userPing The new user ping for the frame.
	 *
	 * @changed OLI 22.03.2016 - Added. / public void setUserPing(UserPing userPing) { this.userPing = userPing; }
	 *
	 *          <p>
	 *          /** Starts the database connections maintenance.
	 *          </p>
	 * @changed OLI 20.01.2015 - Added.
	 */
	public void doBearbeitenDatenbankverbindungen() {
		new ConnectionsMainFrame(this, this.guiBundle, this.ini, this.diagramm);
	}

	/**
	 * Starts a comparision with another data model.
	 *
	 * @changed OLI 19.02.2016 - Added.
	 */
	public void doBearbeitenDatenmodellVergleichen() {
		new DataModelComparison(this.diagramm, this.getInifile());
	}

	public void doBearbeitenRunJavaScript() {
		new DialogScriptExecuter(this.getInifile(), this, this.diagramm, "JavaScripts");
	}

	/**
	 * Creates a SQL update script for a selected database connection (new way).
	 *
	 * @changed OLI 16.12.2015 - Added.
	 */
	public void doBearbeitenUpdateScriptErstellenNeu() {
		final DatabaseConnection dc = this.diagramm
				.getDatabaseConnection(
						this
								.getInifile()
								.readStr(
										"Database-Connections",
										"Update-Preselection-" + this.diagramm.getName(),
										null));
		final DatabaseConnectionRecord dcr =
				new DatabaseConnectionRecord(dc, this.diagramm.getDatabaseConnections(), "");
		final ConnectFrame cf = new ConnectFrame(dcr, this.guiBundle);
		cf.addEditorFrameListener(new EditorFrameListener<EditorFrameEvent<DatabaseConnectionRecord, ConnectFrame>>() {
			@Override
			public void eventFired(final EditorFrameEvent<DatabaseConnectionRecord, ConnectFrame> event) {
				if (event.getEventType() == EditorFrameEventType.OK) {
					try {
						final DatabaseConnectionRecord dcr = event.getEditedObject();
						if ((dcr != null) && (dcr.getDatabaseConnection() != null)) {
							getInifile()
									.writeStr(
											"Database-Connections",
											"Update-Preselection-" + diagramm.getName(),
											dcr.getDatabaseConnection().getName());
						}
						createSQLUpdateScript(dcr);
					} catch (Exception e) {
						new JDialogThrowable(
								e,
								guiBundle.getResourceText("archimedes.ConnectFrame.error.connection.failed.title"),
								getInifile(),
								new PropertyRessourceManager());
					}
				}
			}
		});
		cf.setVisible(true);
	}

	private void createSQLUpdateScript(final DatabaseConnectionRecord dcr) throws Exception {
		final String script = new ScriptGenerator().generate(this.diagramm, dcr, this.userInformation);
		if (script != null) {
			final Vector<String> s = new Vector<String>();
			s.add(script);
			new FrameTextViewer(
					s,
					DefaultFrameTextViewerComponentFactory.INSTANCE,
					getInifile(),
					"SQL Update Script (new)",
					datenpfad);
		}
	}

	/**
	 * Frischt den Frame auf.
	 */
	public void doRepaint() {
		this.repaint();
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Men&uuml;punkt Zoom / Zoom n% bet&auml;tigt.
	 *
	 * @param factor Der neue Zoomfaktor.
	 */
	public void doZoom(final double factor) {
		this.component.setZoomFactor(factor);
		this.component.doRepaint();
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Men&uuml;punkt Zoom / Tabelle finden anclickt.
	 *
	 * @changed OLI 27.02.2009 - Die &Auml;nderungsflagge wird nach der Tabellenauswahl nicht mehr gesetzt (ist ja auch
	 *          nicht sinnvoll).
	 */
	public void doTabelleFinden() {
		final SortedVector<TableModel> sv = new SortedVector<TableModel>();

		for (final TableModel tm : this.diagramm.getTables()) {
			sv.add(tm);
		}

		new FrameSelectionDjinn("Tabellenauswahl", sv, true, this.getInifile()) {
			@Override
			public void doSelected(final Vector values) {
				if (values.size() > 0) {
					final TabellenModel tm = (TabellenModel) values.elementAt(0);
					component.setDiagramViewToObject(tm.getName());
				}
			}
		};
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Men&uuml;punkt Genrieren / Codegeneratoroptionen
	 * (&Uuml;bersicht) anclickt.
	 */
	public void doGeneratorOptionen() {
		new DialogTableGeneratorOptions(this, this.guiBundle, this.diagramm);
	}

	/**
	 * This Method is called if the user clicks on the menu item "Generate" / "Code".
	 */
	public void doGenerateCode() {
		final CodePathProvider codePathProvider = new CodePathProvider(this.guiBundle, this.diagramm);
		final String path = codePathProvider.getCodePath();
		if (!path.isEmpty()) {
			List<Object> codeFactories = getCodeFactories(path);
			final FrameArchimedes fa = this;
			final CodeFactoryProgressionFrame progressionFrame =
					(isAtLeastOneStandardCodeFactoryProgressionFrameUser(codeFactories) //
							? new CodeFactoryProgressionFrame(guiBundle) //
							: null);
			final Counter factoryCount = new Counter(0);
			if (progressionFrame != null) {
				new Thread(() -> {
					progressionFrame.updateFactory(factoryCount.getValue(), codeFactories.size(), null, null);
				}).start();
			}
			for (Object cf : codeFactories) {
				if (cf instanceof CodeGenerator) {
					try {
						((CodeGenerator) cf).generate(this.diagramm);
					} catch (Exception e) {
						new JDialogThrowable(
								e,
								"Error while running code generator " + cf.getClass().getSimpleName() + "!",
								this.getInifile(),
								new PropertyRessourceManager());
					}
				} else {
					final Thread t = new Thread(() -> {
						LOG.info("Called code factory: " + cf.getClass().getSimpleName());
						((CodeFactory) cf).addCodeFactoryListener(fa);
						((CodeFactory) cf).setDataModel(fa.diagramm);
						if (cf instanceof CodeFactoryProgressionEventProvider) {
							((CodeFactoryProgressionEventProvider) cf).addCodeFactoryProgressionListener(event -> {
								if (progressionFrame != null) {
									progressionFrame.processEvent(event);
								}
							});
						}
						if (progressionFrame != null) {
							progressionFrame.updateFactory(null, null, getFactoryName(cf), null);
						} else {
							((CodeFactory) cf)
									.setGUIBundle(getGUIBundle(guiBundle, ((CodeFactory) cf).getResourceBundleNames()));
						}
						((CodeFactory) cf).generate(path);
						try {
							codePathProvider.storePath(path);
						} catch (IOException e) {
							LOG.error("error while setting code path to ini file: " + e.getMessage());
						}
						factoryCount.inc();
						if (progressionFrame != null) {
							progressionFrame
									.updateFactory(
											factoryCount.getValue(),
											null,
											getFactoryName(cf),
											factoryCount.getValue() <= codeFactories.size() //
													? "\n\n" + cf.getClass().getSimpleName() + " finished.\n"
															+ "--------------------------------------------------------------------------------\n\n\n"
													: null);
						}
					});
					t.start();
				}
			}
			if (progressionFrame != null) {
				progressionFrame.enableCloseButton();
			}
		}
	}

	private String getFactoryName(Object cf) {
		if (cf instanceof CodeFactory) {
			return ((CodeFactory) cf).getName() + " (" + ((CodeFactory) cf).getVersion() + ")";
		}
		return "Class - " + cf.getClass().getSimpleName();
	}

	private List<Object> getCodeFactories(String path) {
		List<Object> codeFactories = new ArrayList<>();
		for (String cfcn : StringUtils.split(diagramm.getCodeFactoryClassName(), ';')) {
			codeFactories.add(getCodeFactory(path, cfcn));
		}
		return codeFactories;
	}

	private boolean isAtLeastOneStandardCodeFactoryProgressionFrameUser(List<Object> codeFactories) {
		for (Object object : codeFactories) {
			if (object instanceof StandardCodeFactoryProgressionFrameUser) {
				return true;
			}
		}
		return false;
	}

	private Object getCodeFactory(final String path, String cfcn) {
		CodeFactory cf = null;
		CodeGenerator cg = null;
		try {
			if ((cfcn == null) || (cfcn.length() == 0)) {
				cfcn = this.ini
						.readStr(
								"CodeGenerator",
								"Class",
								System.getProperty("archimedes.default.codefactory.class", "CODEFACTORYCLASS"));
			}
			if (cfcn.startsWith("gengen:")) {
				try {
					cfcn = cfcn.substring(7, cfcn.length());
					cg = (CodeGenerator) Class.forName(cfcn).newInstance();
					if (cg instanceof AbstractCodeGenerator) {
						((AbstractCodeGenerator) cg).setBasePath(path);
					}
					return cg;
				} catch (Exception e) {
					new JDialogThrowable(
							e,
							"Klasse " + cfcn + " kann nicht als Code-Generator " + "instanziert werden!",
							this.getInifile(),
							new PropertyRessourceManager());
				}
			}
			cf = Archimedes.Factory.createCodeFactory(cfcn);
			cf.setDataModel(this.diagramm);
			cf.setGUIBundle(getGUIBundle(this.guiBundle, cf.getResourceBundleNames()));
			cf.setModelCheckerMessageListFrameListeners(this);
		} catch (Exception e) {
			LOG
					.error(
							"Error while creating code generator/factory for class: " + cfcn + ", exception: "
									+ e.getMessage());
			e.printStackTrace();
		}
		return cf;
	}

	public static GUIBundle getGUIBundle(GUIBundle guiBundle, String... applicationNames) {
		GUIBundle agb = guiBundle;
		String lang = System.getProperty("archimedes.user.language", "en");
		ResourceManager rm = null;
		for (int i = 0; i < applicationNames.length; i++) {
			for (String confPath : getConfPaths()) {
				String fn = confPath + applicationNames[i] + "_resource_" + lang + ".properties";
				if (new File(fn).exists()) {
					if (rm == null) {
						rm = new PropertyResourceManager(new PropertyFileManager().open(fn));
					} else {
						((PropertyResourceManager) rm).addResources(new PropertyFileManager().open(fn));
					}
				} else {
					LOG.warn("Ignored. File not existing: " + fn);
				}
			}
		}
		return new GUIBundle(agb.getInifile(), rm, agb.getImageProvider(), agb.getHGap(), agb.getVGap());
	}

	private static List<String> getConfPaths() {
		List<String> l = new ArrayList<>();
		StringTokenizer st =
				new StringTokenizer(System.getProperty("archimedes.resource.paths", Archimedes.CONF_PATH), ",");
		while (st.hasMoreTokens()) {
			l.add(FileUtil.completePath(st.nextToken().trim()));
		}
		return l;
	}

	/**
	 * Generiert eine neue Kopfzeile f&uuml;r das Hauptfenster der Anwendung.
	 */
	public void updateHeadline() {
		this
				.setTitle(
						"Archimedes (Version " + Archimedes.GetVersion() + ")  -  " + this.dateiname
								+ (((this.diagramm != null) && this.diagramm.isAltered())
										? StrUtil.FromHTML(" (Ge&auml;ndert)")
										: ""));
	}

	/**
	 * Liefert eine Liste mit den Index-Metadaten aus der angegebenen Datenbank.
	 *
	 * @param dsr        Die Daten zur Verbindung mit der Datenbank.
	 * @param schemaName Ein Schemaname, falls das Modell auf ein spezielles Schema der Datenbank abgestimmt werden
	 *                   soll. Soll das Defaultschema genutzt werden, kann der Name leer &uuml;bergeben werden.
	 *
	 * @return Eine Liste mit den Index-Metadaten aus der angegebenen Datenbank.
	 *
	 * @changed OLI 01.06.2009 - Hinzugef&uuml;gt.
	 * @todo OLI - Methode nach ApplicationUtil oder so &uuml;bertragen.
	 */
	public static SortedVector<SimpleIndexMetaData> GetIndexMetaData(final JDBCDataSourceRecord dsr,
			final String schemaName) {
		SortedVector<SimpleIndexMetaData> svt = new SortedVector<SimpleIndexMetaData>();

		try {
			final Connection c = ConnectionManager.GetConnection(dsr);
			svt = ApplicationUtil.GetIndexMetaData(c, schemaName);
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return svt;
	}

	/**
	 * Liefert eine Liste mit den Unique-Metadaten aus der angegebenen Datenbank.
	 *
	 * @param dsr        Die Daten zur Verbindung mit der Datenbank.
	 * @param schemaName Ein Schemaname, falls das Modell auf ein spezielles Schema der Datenbank abgestimmt werden
	 *                   soll. Soll das Defaultschema genutzt werden, kann der Name leer &uuml;bergeben werden.
	 *
	 * @return Eine Liste mit den Unique-Metadaten aus der angegebenen Datenbank.
	 *
	 * @changed OLI 011.0.6.2013 - Hinzugef&uuml;gt.
	 */
	public static SortedVector<UniqueMetaData> GetUniqueMetaData(final JDBCDataSourceRecord dsr,
			final String schemaName) {
		SortedVector<UniqueMetaData> svt = new SortedVector<UniqueMetaData>();

		try {
			final Connection c = ConnectionManager.GetConnection(dsr);
			svt = ApplicationUtil.GetUniqueMetaData(c, schemaName);
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return svt;
	}

	/**
	 * Updates the model name in the user ping.
	 *
	 * @param modelName The new model name for the user ping.
	 * @param menu      Referenz auf das Men&uuml;, dessen Inhalt aus der Viewliste gebildet werden soll.
	 * @param views     Die Liste der Views, die in das Men&uuml; &uuml;bernommen werden sollen.
	 *
	 * @changed OLI 22.03.2016 - Added. / public void updateUserPingModelName() { if ((this.diagramm != null) &&
	 *          (this.userPing != null)) { this.userPing.setCurrentModelName(this.diagramm.getName()); } }
	 *
	 *          <p>
	 *          /** Aktualisiert das View-Men&uuml; anhand der Viewliste des &uuml;bergebenen Diagramms.
	 *          </p>
	 */
	public void updateViewMenu(final JMenu menu, final java.util.List<GUIViewModel> views) {
		menu.removeAll();
		menu.add(this.menuitemtabletoview);
		menu.add(new JSeparator());
		menu.add(this.menuitemswitchrelationtoview);
		menu.add(this.menuitemswitchtechnicalfieldstoview);
		menu.add(this.menuitemswitchtransientfieldstoview);
		menu.add(new JSeparator());

		for (int i = 0, len = views.size(); i < len; i++) {
			final JMenuItem menuItem = new JMenuItem(new ViewAction(views.get(i)));
			menuItem.setText(views.get(i).getName());
			menu.add(menuItem);
		}
	}

	/**
	 * Updates the warning message.
	 *
	 * @param message The message which is to update.
	 * @param warning Set this flag if the message notifies a warning.
	 * @param error   Set this flag if the message notifies an error.
	 *
	 * @changed OLI 18.05.2016 - Added.
	 */
	public void updateWarnings(String message, boolean warning, boolean error) {
		this.component.updateWarnings(message);
		if (warning && !error) {
			this.component.setWarningLabelForeGround(new Color(203, 166, 18));
		} else if (error) {
			this.component.setWarningLabelForeGround(Color.RED);
		} else {
			this.component.setWarningLabelForeGround(new Color(94, 132, 84));
		}
	}

	/**
	 * Aktualisiert die Komponente zum Anzeigen des Diagramms auf den angegebenen View.
	 *
	 * @param view Der View, den die Komponente anzeigen soll.
	 */
	public void doChangeView(final GUIViewModel view) {
		this.component.setView(view);
		this.component.doRepaint();
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Meb&uuml;punkt Views / Tabelle einf&uuml;gen anclickt.
	 */
	public void doViewsEinfuegen() {
		final corentx.util.SortedVector<TableModel> sv = new corentx.util.SortedVector<TableModel>();

		for (final TableModel tm : this.diagramm.getTables()) {
			sv.add(tm);
		}

		new FrameSelectionDjinn("Tabellenauswahl", sv, false, this.getInifile()) {
			@Override
			public void doSelected(final Vector values) {
				for (int i = 0, len = values.size(); i < len; i++) {
					final TabellenModel tm = (TabellenModel) values.elementAt(i);

					if (!tm.getViews().contains(component.getView())) {
						tm
								.setXY(
										component.getView(),
										tm.getX(diagramm.getViews().get(0)),
										tm.getY(diagramm.getViews().get(0)));
					}

					diagramm.raiseAltered();
					component.doRepaint();
				}
			}
		};
	}

	/**
	 * Shows a monitor with the running Archimedes instances.
	 *
	 * @changed OLI 15.03.2016 - Added. / public void doServerMonitor() { new UserMonitorFrame(this.guiBundle); }
	 *
	 *          <p>
	 *          /** Setzt die Ge&auml;ndert-Flagge des aktuellen Diagramms.
	 *          </p>
	 * @changed OLI 27.11.2007 - Hinzugef&uuml;gt.
	 */
	public void raiseAltered() {
		this.diagramm.raiseAltered();
	}

	/**
	 * @changed OLI 26.11.2014 - Added.
	 */
	@Override
	public void eventFired(final EditorFrameEvent<?, ? extends Window> e) {
		if (e instanceof BaseConfigurationFrameEvent) {
			final BaseConfigurationFrameEvent bcfe = (BaseConfigurationFrameEvent) e;

			if (bcfe.getEventType() == EditorFrameEventType.OK) {
				final BaseConfiguration bc = bcfe.getEditedObject();
				final String propfn = corentx.io.FileUtil
						.completePath(System.getProperty("user.home"))
						.concat(".archimedes.properties");
				System.setProperty("archimedes.user.company", bc.getCompany());
				System.getProperty("archimedes.user.database.name", bc.getDBName());
				System.getProperty("archimedes.user.database.server.name", bc.getDBServerName());
				System.getProperty("archimedes.user.database.user.name", bc.getDBUserName());
				System.setProperty("archimedes.user.language", bc.getLanguage());
				System.setProperty("archimedes.user.name", bc.getUserName());
				System.setProperty("archimedes.user.token", bc.getUserToken());

				final Properties p = new Properties();
				p.setProperty("archimedes.user.company", bc.getCompany());
				p.setProperty("archimedes.user.database.name", bc.getDBName());
				p.setProperty("archimedes.user.database.server.name", bc.getDBServerName());
				p.setProperty("archimedes.user.database.user.name", bc.getDBUserName());
				p.setProperty("archimedes.user.language", bc.getLanguage());
				p.setProperty("archimedes.user.name", bc.getUserName());
				p.setProperty("archimedes.user.token", bc.getUserToken());

				try {
					corentx.io.FileUtil.writeProperties(p, propfn, "Archimedes base " + "configurations");
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				final BaseConfigurationFrame bcf = bcfe.getSource();
				bcf.removeEditorFrameListener(this);
				bcf.dispose();
				this.raiseAltered();
			}
		}
	}

	/**
	 * Liefert die Benutzerinformationen der Applikation.
	 *
	 * @return Die Benutzerinformationen der Applikation.
	 *
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	public UserInformation getUserInformation() {
		return this.userInformation;
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public void objectDoubleClicked(final GUIObjectModel object, final ComponentDiagramm<GUIObjectTypes> component,
			final int buttonId) {
		if (buttonId == 1) {
			this.editTable(object, component);
		} else {
			if (object instanceof TableModel) {
				new TableModelFrame(this.guiBundle, (TableModel) object, this);
			}
		}
	}

	private void editTable(final GUIObjectModel object, final ComponentDiagramm component) {
		this.setPredeterminedOptionProviderForDiagram();

		final TabellenModel tab = (TabellenModel) object;
		new FrameEditorDjinn(
				StrUtil.FromHTML(this.guiBundle.getResourceText(RES_TABLE_FRAME_EDIT_TITLE, object.getName())),
				tab,
				ini) {
			@Override
			public void doChanged(final boolean saveOnly) {
				diagramm.raiseAltered();
				component.doRepaint();
			}

			@Override
			public void doDeleted() {
				deleteTable(tab);
			}
		};
	}

	public void deleteTable(final TableModel tab) {
		final ColumnModel[] rcms = tab.getDataModel().getReferencers(tab);

		if (!(component.getView() instanceof MainViewModel)) {
			final String[] options = new String[] { "View", "Modell" };

			if (options[JOptionPane
					.showOptionDialog(
							null,
							StrUtil
									.FromHTML(
											"Soll die " + "Tabelle nur aus dem View\noder aus dem Modell gel&ouml;scht "
													+ "werden?"),
							StrUtil.FromHTML("R&uuml;ckfrage L&ouml;schen"),
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							options,
							options[0])].equals("View")) {
				tab.removeFromView((ViewModel) component.getView());
				component.doRepaint();

				return;
			}
		}

		if (rcms.length > 0) {
			JOptionPane
					.showMessageDialog(
							null,
							StrUtil
									.FromHTML(
											"Die Tabellen wird " + "durch " + rcms[0].getFullName() + " referenziert!\n"
													+ "L&ouml;schen nicht m&ouml;glich!"),
							"Referenzproblem!",
							JOptionPane.YES_OPTION);
			LOG.warn("Wird Referenziert durch " + rcms[0].getFullName());

			return;
		}

		tab.getDataModel().removeTable(tab);
		new DefaultIndexListCleaner().clean(tab.getDataModel());
		raiseAltered();
		component.doRepaint();
	}

	/**
	 * @changed OLI 23.05.2013 - Added.
	 */
	@Override
	public void diagrammModelChanged() {
		this.stateChanged(this.diagramm.isAltered());
	}

	/**
	 * @changed OLI 23.05.2013 - Added.
	 */
	@Override
	public void stateChanged(final boolean newState) {
		this.startChecker();
		this.updateHeadline();

		if (this.fov != null) {
			this.fov.updateView();
			this.component.doRepaint();
		}
	}

	/**
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public void tableChanged(final TableChangedEvent e) {
		LOG.debug("table changed event detected: " + e);
		this.raiseAltered();
		this.diagrammModelChanged();
	}

	/**
	 * @changed OLI 01.10.2015 - Added.
	 */
	@Override
	public boolean accept(final EventObject e) {
		return (e.getSource() instanceof JTextField);
	}

	/**
	 * @changed OLI 11.07.2016 - Added.
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == this.menuItemFileSave) {
			this.doDateiSpeichern(this.dateiname, false);
		}
	}

	/**
	 * @changed OLI 01.10.2015 - Added.
	 */
	@Override
	public void eventDispatched(final EventObject e) {
		if (e.getSource() instanceof JTextField) {
			final String co = COUtil.GetFullContext((Component) e.getSource());
			final OptionType ot = this.getOptionType(co);

			if (ot != null) {
				final JTextField tf = (JTextField) e.getSource();

				if (e instanceof MouseEvent) {
					final MouseEvent me = (MouseEvent) e;

					if (me.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
						this.setPredeterminedOptionProviderForDiagram();
						new OptionsPopupMenu(
								this.diagramm.getPredeterminedOptionProvider(),
								ot,
								me.getX(),
								me.getY(),
								tf,
								OptionsSelectionMode.INSERT);
					}
				} else if (e instanceof KeyEvent) {
					final KeyEvent ke = (KeyEvent) e;

					if ((ke.getKeyCode() == KeyEvent.VK_S) && ke.isControlDown()) {
						this.setPredeterminedOptionProviderForDiagram();
						new OptionsPopupMenu(
								this.diagramm.getPredeterminedOptionProvider(),
								ot,
								tf.getX(),
								tf.getY(),
								tf,
								OptionsSelectionMode.INSERT);
					}
				}
			}
		}
	}

	private OptionType getOptionType(final String co) {
		if (co.endsWith("Tabellenspalte.Parameter")) {
			return OptionType.COLUMN;
		} else if (co.endsWith("Domain.Parameter")) {
			return OptionType.DOMAIN;
		} else if (co.endsWith("Panel.Parameter")) {
			return OptionType.PANEL;
		}

		return null;
	}

	/**
	 * @changed OLI 18.05.2016 - Added.
	 */
	@Override
	public void eventDetected(final DiagramComponentPanelEvent event) {
		if ((event.getClickCount() == 2) && (this.lastModelCheckerMessages != null)) {
			if (this.modelCheckerMessageListFrame == null) {
				this.modelCheckerMessageListFrame =
						new ModelCheckerMessageListFrame(this.guiBundle, this.lastModelCheckerMessages, false);
				this.modelCheckerMessageListFrame.addBaccaraFrameListener(new ModelCheckerMessageListFrameListener() {
					@Override
					public void eventDetected(final Event event) {
						if (event.getMessage().getObject() instanceof Tabelle) {
							editTable((GUIObjectModel) event.getMessage().getObject(), component.getComponentDiagram());
						}
					}
				});
			} else {
				this.modelCheckerMessageListFrame.setVisible(true);
				this.modelCheckerMessageListFrame.toFront();
				this.modelCheckerMessageListFrame.updateMessages(this.lastModelCheckerMessages);
			}
		}
	}

	/**
	 * @changed OLI 18.05.2016 - Added.
	 */
	@Override
	public void notifyModelCheckerResult(final ModelCheckerMessage[] mcms) {
		if (mcms.length > 0) {
			final int errorCount = this.getMessageCount(mcms, ModelCheckerMessage.Level.ERROR);
			final int warnCount = this.getMessageCount(mcms, ModelCheckerMessage.Level.WARNING);
			String s = "";
			if (errorCount > 0) {
				s += this.guiBundle.getResourceText(RES_WARNING_MESSAGES_ERRORS_DETECTED, errorCount);
			}
			if (warnCount > 0) {
				s += ((s.length() > 0) ? ", " : "")
						+ this.guiBundle.getResourceText(RES_WARNING_MESSAGES_WARNINGS_DETECTED, warnCount);
			}
			this.updateWarnings(s, warnCount > 0, errorCount > 0);
		} else {
			this.updateWarnings(this.guiBundle.getResourceText(RES_WARNING_MESSAGES_NO_WARNINGS), false, false);
		}
		this.menuGenerate.setEnabled(isReadyToGenerate());
		this.lastModelCheckerMessages = mcms;
		if (this.modelCheckerMessageListFrame != null) {
			this.modelCheckerMessageListFrame.updateMessages(mcms);
		}
	}

	private boolean isReadyToGenerate() {
		final String path = this.diagramm.getCodePfad().replace("~", System.getProperty("user.home"));
		for (Object cf : this.getCodeFactories(path)) {
			if ((cf instanceof ReadyToGenerateChecker)
					&& !((ReadyToGenerateChecker) cf).isReadyToGenerate(this.diagramm)) {
				return false;
			}
		}
		return true;
	}

	private int getMessageCount(final ModelCheckerMessage[] mcms, final ModelCheckerMessage.Level level) {
		int c = 0;
		for (final ModelCheckerMessage mcm : mcms) {
			if (mcm.getLevel() == level) {
				c++;
			}
		}
		return c;
	}

	/**
	 * @changed OLI 15.06.2016 - Added.
	 */
	@Override
	public void eventDetected(final Event e) {
		if (e.getType() == Event.Type.MESSAGE_SELECTED) {
			editTable((GUIObjectModel) e.getMessage().getObject(), this.component.getComponentDiagram());
		}
	}

	/**
	 * @changed OLI 07.12.2016 - Added.
	 */
	@Override
	public void eventFired(final CodeFactoryEvent cfe) {
		if (cfe.getType() == CodeFactoryEventType.GENERATION_FINISHED) {
			cfe.getFactory().removeCodeFactoryListener(this);
		}
	}

	/**
	 * @changed OLI 07.12.2016 - Added.
	 */
	@Override
	public void exceptionDetected(final CodeFactory cf, final Throwable e) {
//		String name = "n/a";
//		String userMessage = null;
//
//		if (e instanceof CodeGeneratorException) {
//			final CodeGeneratorException cge = (CodeGeneratorException) e;
//			name = cge.getCodeFactoryName();
//			userMessage = cf.getGUIBundle().getResourceText(cge.getMessageResourceId(), cge.getAdditionalParameters());
//		} else {
//			e.printStackTrace();
//		}
//
//		new JDialogThrowable(e,
//				((userMessage != null) ? userMessage : ("Error while running code " + "generator " + name + "!")),
//				this.getInifile(),
//				((this.guiBundle.getResourceManager() instanceof PropertyRessourceManager)
//						? (PropertyRessourceManager) this.guiBundle.getResourceManager()
//						: new PropertyRessourceManager()));
	}

	/* Klasse f&uuml;r die View-Men&uuml;punkte. */
	class ViewAction extends AbstractAction {
		private GUIViewModel view = null;

		public ViewAction(final GUIViewModel view) {
			super();
			this.view = view;
		}

		@Override
		public String toString() {
			return ((this.view != null) ? this.view.getName() : "<null>");
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			if (this.view instanceof MainViewModel) {
				menuitemneustandardtabelle.setEnabled(true);
				menuitemneutabelle.setEnabled(true);
			} else {
				menuitemneustandardtabelle.setEnabled(false);
				menuitemneutabelle.setEnabled(false);
			}

			doChangeView(this.view);
		}
	}

	public void setDiagramm(DiagrammModel model) {
		diagramm = model;
		diagramm.addDataModelListener(this);
		diagramm.addDiagrammModelListener(this);
		diagramm.addGUIDiagramModelListener(this);
		guiObjectCreator.setDiagram(diagramm);
		component.setView(diagramm.getViews().get(0));
		component.setDiagram(diagramm);
		if (fov != null) {
			fov.setDiagramm(diagramm);
		}
		this.updateViewMenu(viewmenu, diagramm.getViews());
		diagramm.clearAltered();
	}

} // 2842