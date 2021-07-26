/*
 * Diagramm.java
 *
 * 20.03.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import archimedes.connections.ArchimedesImportJDBCDataSourceRecord;
import archimedes.connections.DatabaseConnection;
import archimedes.gui.diagram.ComponentDiagramm;
import archimedes.legacy.Archimedes;
import archimedes.legacy.gui.CommentSubEditorFactory;
import archimedes.legacy.gui.HistoryOwnerSubEditorFactory;
import archimedes.legacy.gui.ModelCheckerScriptSubEditorFactory;
import archimedes.legacy.gui.OptionListSubEditorFactory;
import archimedes.legacy.gui.ToStringContainer;
import archimedes.legacy.model.ColumnMetaData;
import archimedes.legacy.model.DefaultCommentModel;
import archimedes.legacy.model.DiagramSaveMode;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.DiagrammModelListener;
import archimedes.legacy.model.DomainShowMode;
import archimedes.legacy.model.MainViewModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.legacy.model.TableMetaData;
import archimedes.legacy.scheme.stf.AbstractSTFHandler;
import archimedes.legacy.scheme.stf.reader.STFAdditionalSQLCodeReader;
import archimedes.legacy.scheme.stf.reader.STFColorReader;
import archimedes.legacy.scheme.stf.reader.STFColumnReader;
import archimedes.legacy.scheme.stf.reader.STFDatabaseConnectionReader;
import archimedes.legacy.scheme.stf.reader.STFDiagrammParameterReader;
import archimedes.legacy.scheme.stf.reader.STFDomainReader;
import archimedes.legacy.scheme.stf.reader.STFNReferenceReader;
import archimedes.legacy.scheme.stf.reader.STFOptionReader;
import archimedes.legacy.scheme.stf.reader.STFRelationReader;
import archimedes.legacy.scheme.stf.reader.STFSequenceReader;
import archimedes.legacy.scheme.stf.reader.STFStereotypeReader;
import archimedes.legacy.scheme.stf.reader.STFViewReader;
import archimedes.legacy.scheme.stf.writer.STFAdditionalSQLCodeWriter;
import archimedes.legacy.scheme.stf.writer.STFColorWriter;
import archimedes.legacy.scheme.stf.writer.STFColumnWriter;
import archimedes.legacy.scheme.stf.writer.STFDatabaseConnectionWriter;
import archimedes.legacy.scheme.stf.writer.STFDiagrammParameterWriter;
import archimedes.legacy.scheme.stf.writer.STFDomainWriter;
import archimedes.legacy.scheme.stf.writer.STFNReferenceWriter;
import archimedes.legacy.scheme.stf.writer.STFOptionWriter;
import archimedes.legacy.scheme.stf.writer.STFSequenceWriter;
import archimedes.legacy.scheme.stf.writer.STFStereotypeWriter;
import archimedes.legacy.scheme.stf.writer.STFViewWriter;
import archimedes.legacy.script.sql.SQLScriptEvent;
import archimedes.legacy.script.sql.SQLScriptListener;
import archimedes.legacy.util.DescriptionGetter;
import archimedes.model.CodeFactory;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.IndexMetaData;
import archimedes.model.OptionModel;
import archimedes.model.OptionType;
import archimedes.model.OrderMemberModel;
import archimedes.model.PanelModel;
import archimedes.model.PredeterminedOptionProvider;
import archimedes.model.SelectionAttribute;
import archimedes.model.SelectionMemberModel;
import archimedes.model.SequenceModel;
import archimedes.model.StereotypeModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.model.events.DataModelEvent;
import archimedes.model.events.DataModelListener;
import archimedes.model.events.TableChangedEvent;
import archimedes.model.gui.AbstractGUIDiagramModel;
import archimedes.model.gui.GUIObjectModel;
import archimedes.model.gui.GUIViewModel;
import archimedes.scheme.SelectionMember;
import baccara.gui.GUIBundle;
import corent.base.SortedVector;
import corent.base.StrUtil;
import corent.dates.PDate;
import corent.db.JDBCDataSourceRecord;
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
import corent.djinn.TabDescriptor;
import corent.djinn.TabbedPaneFactory;
import corent.files.StructuredTextFile;
import corent.gui.ExtendedColor;
import corent.xml.ToXMLAttributes;
import corentx.util.Str;
import gengen.metadata.ClassMetaData;
import logging.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;

import static corentx.util.Checks.ensure;

/**
 * Diese Klasse stellt eine konkrete Auspr&auml;gung des DiagrammModels dar, die ein Diagramm innerhalb der
 * Archimedes-Applikation repr&auml;sentiert.
 * <p>
 * Die Property <B>archimedes.scheme.Diagramm.output</B> schaltet eine pr&auml;zise Angabe zu den gelesenen Tabellen zu.
 * Sonst wird lediglich die Anzahl der einzulesenden Tabellen nach System.out geschrieben und ein Punkt f&uuml;r jede
 * eingelesene Tabelle gesetzt.
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
 * <TD>archimedes.scheme.Diagramm.cout</TD>
 * <TD>Boolean</TD>
 * <TD>false</TD>
 * <TD>Wird diese Property gesetzt, so wird eine zus&auml;tzliche Konsolenausgabe zwecks Debugging bzw. Fehlerzuordnung.
 * Derzeit funktioniert das nur in der Methode <TT>toSTF()</TT>.</TD>
 * </TR>
 * </TABLE>
 *
 * @author ollie
 * @changed OLI 29.08.2007 - Erweiterung der Methode <TT>toSTF()</TT> um zus&auml;tzliche Konsolenausgaben.
 * @changed OLI 19.12.2007 - Anpassung der alter-table-Statements an PostgreSQL in der Methode
 * <TT>(buildUpdateScript(SortedVector, boolean, boolean, boolean, DBExecMode, Vector,
 * Vector)</TT>.
 * @changed OLI 09.01.2008 - Einbau einer Debugausgabe (&uuml;ber die Properties <I>archimedes.scheme.Diagramm.debug</I>
 * oder <I>archimedes.Archimedes.debug</I> zuschaltbar). Dabei Korrektur des Abgleiches mit der Datenbank
 * (Text-Felder, Indices) f&uuml;r PostgreSQL. Alles in der Methode <TT>buildUpdateScript(...)</TT> erfolgt.
 * @changed OLI 11.01.2008 - Arbeiten am Debugging des Abgleiches mit HSQL-Datenbanken (numeric-Problem) (ebenfalls in
 * der Methode <TT>buildUpdateScript(...)</TT>).
 * @changed OLI 20.01.2008 - Arbeiten am Debugging des Abgleiches der Indices zwischen Modell und Datenbank f&uuml;r
 * mySQL und PostgreSQL in der Methode <TT>buildUpdateScript(...)</TT>.
 * @changed OLI 11.02.2008 - Erweiterung um das Attribut <TT>UdschebtiBaseClassName</TT>.
 * @changed OLI 16.02.2008 - Korrektur des Drop-Index-Statements f&uuml;r PostgreSQL beim Bau von Update-Scripts.
 * @changed OLI 17.02.2008 - Umbau Feldtypenvergleich beim Bau eines Updatescriptes auf einen Namensvergleich. Die
 * Zahlenwerte f&uuml;r die SQL-Datentypen f&uuml;hrten zu Problemen.
 * @changed OLI 11.05.2008 - Erweiterung der Implementierung des Interfaces <TT>TabbedEditable</TT> um die Methode
 * <TT>isTabEnabled(int)</TT>.
 * @changed OLI 09.08.2008 - Erweiterung um die Implementierung der Methoden <TT>isPaintTechnicalFieldsInGray()</TT> und
 * <TT>setPaintTechnicalFieldsInGray(boolean)</TT>. Hierdurch k&ouml;nnen als technisch gekennzeichnete Felder
 * in den Diagrammen ausgegraut dargestellt werden.
 * @changed OLI 10.08.2008 - Erweiterung der Schreib- und Leseroutine nach Erweiterung des ViewModels um die
 * M&ouml;glichkeit eine Ausblendung technischer Felder anzugeben.
 * @changed OLI 16.08.2008 - Erweiterung der Schreibroutine um die M&ouml;glichkeit ein abgespeckte Version der
 * ads-Datei zu erzeugen, die nur die f&uuml;r die Applikation notwendigen Daten enth&auml;lt und auf den
 * grafischen Overhead verzichtet.
 * @changed OLI 04.09.2008 - Erweiterung um die Implementierung der Methoden <TT>getWriteChangeScript()</TT> und
 * <TT>setAfterWriteScript(String)</TT>.
 * @changed OLI 15.09.2008 - Einbau der Lese- und Schreibroutine f&uuml;r die Codegeneratoroptionen.
 * @changed OLI 16.09.2008 - Erweiterung der Debugausgaben.
 * @changed OLI 22.09.2008 - Erweiterung um das Schreiben und Lesen des Tabellenspaltenattributs <TT>listItemField</TT>.
 * @changed OLI 15.12.2008 - Erweiterung um die Ber&uuml;cksichtigung der Referenzdaten beim Import von Datenmodellen.
 * @changed OLI 08.03.2009 - Erweiterung um die Implementierung der Methode
 * <TT>getCodegeneratorOptionsListTag(String, String)</TT>.
 * @changed OLI 09.03.2009 - Erweiterung der Schreib- und Leseroutine f&uuml;r die Attribute Parameter und Unique.
 * @changed OLI 21.03.2009 - Erweiterung um die Implementierung der Methode <TT>generateCode(CodeFactory, String)</TT>.
 * @changed OLI 22.03.2009 - Einbindung der im Datenmodell definierten SQLScriptListener in der Methode
 * <TT>buildUpdateScript(SortedVector, boolean, boolean, boolean, DBExecMode,
 * Vector, Vector)</TT>. Erweiterung um die Implementierung der Methoden
 * <TT>getAdditionalSQLScriptListener()</TT> und <TT>setAdditionalSQLScriptListener(String)</TT>, sowie der
 * Logik zur Anbindung der Listener an das Datenmodell durch die Methoden
 * <TT>addSQLScriptListener(SQLScriptListener)</TT> and <TT>removeSQLScriptListener(SQLScriptListener)</TT>.
 * @changed OLI 25.03.2009 - Erweiterung der SQLScriptEvent-Konstruktoraufrufe um eine Referenz auf das aktuelle
 * SQL-Update-Script.
 * @changed OLI 26.03.2009 - Erweiterung des Event-Handlings beim Bau von SQL-Update-Script's auf ein Event, das vor dem
 * Einf&uuml;gen des DBVersion-Statements geworfen wird.
 * @changed OLI 01.06.2009 - Anpassung an die &Auml;nderungen des DiagrammModel-Interfaces
 * (<TT>isDifferentToScheme(List, List)</TT> und <TT>getKeyColumns()</TT>).
 * @changed OLI 30.09.2009 - Erweiterung um die Implementierung der Methode <TT>getProjectToken()</TT>.
 * @changed OLI 21.05.2010 - Die Angaben zur Version-Tabelle werden nun unabh&uml;ngig vom Modus geschrieben.
 * @changed OLI 01.11.2011 - Erweiterung um den <TT>HistoryOwner</TT>.
 * @changed OLI 16.12.2011 - Erweiterung um die Implementierung der Liste mit den komplexen Indices.
 * @changed OLI 20.12.2011 - Erweiterung um die Implementierung der Methode <CODE>getClass(String)</CODE>.
 * @changed OLI 23.04.2013 - Erweiterung um die Sequenzen.
 * @changed OLI 29.06.2020 - Erweiterung um das Feld mit den zus&auml;tzlichen Diagramminformationen.
 */

public class Diagramm extends AbstractGUIDiagramModel implements DiagrammModel {

	private static final Logger LOG = Logger.getLogger(Diagramm.class);
	/**
	 * Ein Bezeichner zum Zugriff auf den Namen des Diagramms.
	 */
	private static final int ID_NAME = 0;
	/**
	 * Ein Bezeichner zum Zugriff auf den Autor des Diagramms.
	 */
	private static final int ID_AUTOR = 1;
	/**
	 * Ein Bezeichner zum Zugriff auf den Kommentar zum Diagramms.
	 */
	private static final int ID_COMMENT = 2;
	/**
	 * Ein Bezeichner zum Zugriff auf die Version des Diagramms.
	 */
	private static final int ID_VERSION = 3;
	/**
	 * Ein Bezeichner zum Zugriff auf das Versionsdatum des Diagramms.
	 */
	private static final int ID_VERSIONSDATUM = 4;
	/**
	 * Ein Bezeichner zum Zugriff auf den Versionskommentar des Diagramms.
	 */
	private static final int ID_VERSIONSKOMMENTAR = 5;
	/**
	 * Ein Bezeichner zum Zugriff auf die Schriftg&ouml;&szlig;e f&uuml;r die Tabelleninhalte.
	 */
	private static final int ID_SCHRIFTGROESSE_TABELLEN = 6;
	/**
	 * Ein Bezeichner zum Zugriff auf die Schriftg&ouml;&szlig;e f&uuml;r die &Uuml;berschrift.
	 */
	private static final int ID_SCHRIFTGROESSE_UEBERSCHRIFT = 7;
	/**
	 * Ein Bezeichner zum Zugriff auf die Schriftg&ouml;&szlig;e f&uuml;r die Untertitel.
	 */
	private static final int ID_SCHRIFTGROESSE_UNTERTITEL = 8;
	/**
	 * Ein Bezeichner zum Zugriff auf Flagge zu Druck und Anzeige von aufgehobenen Objekten.
	 */
	private static final int ID_HIDE_DEPRECATED = 9;
	/**
	 * Ein Bezeichner zum Zugriff auf den Zielpfad f&uuml;r die Code-Generierung.
	 */
	private static final int ID_CODEPFAD = 10;
	/**
	 * Ein Bezeichner zum Zugriff auf den Namen der zu generierenden Applikation.
	 */
	private static final int ID_APPLICATIONNAME = 11;
	/**
	 * Ein Bezeichner zum Zugriff auf den Namen DB-Versionen-Tabelle, falls eine solche existiert.
	 */
	private static final int ID_DBVERSIONTABLENAME = 12;
	/**
	 * Ein Bezeichner zum Zugriff auf die Flagge zur Anzeige von referenzierten Spaltennamen im Diagramm.
	 */
	private static final int ID_SHOWREFERENCEDCOLUMNAMES = 13;
	/**
	 * Ein Bezeichner zum Zugriff auf den Namen der Versionsspalte in der DB-Versionen-Tabelle, falls eine solche
	 * existiert.
	 */
	private static final int ID_DBVERSIONDBVERSIONCOLUMN = 14;
	/**
	 * Ein Bezeichner zum Zugriff auf den Namen der Beschreibungsspalte der DB-Versionen-Tabelle, falls eine solche
	 * existiert.
	 */
	private static final int ID_DBVERSIONDESCRIPTIONCOLUMN = 15;
	/**
	 * Ein Bezeichner zum Zugriff auf die View-Liste.
	 */
	private static final int ID_MARKUPWRITEABLEMEMBER = 16;
	/**
	 * Ein Bezeichner zum Zugriff auf den Basis-Packagenamen.
	 */
	private static final int ID_BASEPACKAGENAME = 17;
	/**
	 * Ein Bezeichner zum Zugriff auf den Namen der CodeFactory-Klasse.
	 */
	private static final int ID_CODEFACTORYCLASSNAME = 18;
	/**
	 * Ein Bezeichner zum Zugriff auf die MarkWriteablemembers-Flagge.
	 */
	private static final int ID_VIEWS = 19;
	/**
	 * Ein Bezeichner zum Zugriff auf das Attribut UdschebtiBaseClassName.
	 */
	private static final int ID_UDSCHEBTIBASECLASSNAME = 20;
	/**
	 * Ein Bezeichner zum Zugriff auf das Attribut PaintTechnicalFieldsInGray.
	 */
	private static final int ID_PAINTTECHNICALFIELDSINGRAY = 21;
	/**
	 * Ein Bezeichner zum Zugriff auf AfterWriteScript.
	 */
	private static final int ID_AFTERWRITESCRIPT = 22;
	/**
	 * Ein Bezeichner zum Zugriff auf das Feld AdditionalSQLScriptListener.
	 */
	private static final int ID_ADDITIONALSCRIPTLISTENER = 23;
	/**
	 * Ein Bezeichner zum Zugriff auf das Feld History.
	 */
	private static final int ID_HISTORY = 24;
	/**
	 * Ein Bezeichner zum Zugriff auf das Feld Schemaname.
	 */
	private static final int ID_SCHEMA_NAME = 25;
	/**
	 * Ein Bezeichner zum Zugriff auf die Optionen des Modells
	 */
	private static final int ID_OPTIONS = 26;
	/**
	 * Ein Bezeichner zum Zugriff auf die Farbe fuer regul&auml;re Relationen.
	 */
	private static final int ID_RELATION_COLOR_REGULAR = 27;
	/**
	 * Ein Bezeichner zum Zugriff auf die Farbe fuer Relationen auf externe Tabellen.
	 */
	private static final int ID_RELATION_COLOR_EXTERNAL_TABLES = 28;
	/**
	 * Ein Bezeichner zum Zugriff auf das Attribut PaintTransientFieldsInGray.
	 */
	private static final int ID_PAINTTRANSIENTFIELDSINGRAY = 29;
	/**
	 * Ein Bezeichner zum Zugriff auf das Attribut Owner.
	 */
	private static final int ID_OWNER = 30;
	/**
	 * Ein Bezeichner zum Zugriff auf das Attribut AdditionalDiagramInfo.
	 */
	private static final int ID_ADDITIONAL_DIAGRAM_INFO = 31;
	/**
	 * Ein Bezeichner zum Zugriff auf das Attribut DomainShowMode.
	 */
	private static final int ID_DOMAIN_SHOW_MODE = 32;
	/**
	 * Ein Bezeichner zum Zugriff auf das ModelCheckerScript.
	 */
	private static final int ID_MODEL_CHECKER_SCRIPT = 33;

	/* Der JDBCDataSourceRecord mit den Daten f&uuml;r den Datanschema-Import. */
	private ArchimedesImportJDBCDataSourceRecord importDSR = null;
	/* Der JDBCDataSourceRecord mit den Daten f&uuml;r den Datanschema-Update. */
	private ArchimedesJDBCDataSourceRecord updateDSR = null;
	/*
	 * Diese Flagge muss gesetzt werden, wenn technische Felder abgegraut dargestellt werden sollen.
	 */
	private boolean paintTechnicalFieldsInGray = false;
	/*
	 * Diese Flagge muss gesetzt werden, wenn transiente Felder abgegraut dargestellt werden sollen.
	 */
	private boolean paintTransientFieldsInGray = false;
	/*
	 * Wird diese Flagge gesetzt, so werden die Namen der durch Foreignkeys referenzierten Spalten im Diagramm
	 * angezeigt.
	 */
	private boolean showReferencedColumns = true;
	/* Die Schriftgr&ouml;&szlig;e f&uuml;r die Tabelleninhalte. */
	private int fontsizeTableContents = 12;
	/* Die Liste der an das Diagramm angebundenen SQLScriptListener. */
	private java.util.List<SQLScriptListener> listAdditionalSQLScriptListener = new Vector<SQLScriptListener>();
	/* Der Tabellenspaltencache. */
	private SortedVector cacheFieldModel = new SortedVector();
	/* Die Liste der in dem Diagramm verf&uuml;gbaren Default-Kommentare. */
	private SortedVector defaultComments = new SortedVector();
	/* Die Liste mit den komplexen Indices des Modells. */
	private SortedVector<IndexMetaData> complexIndices = new SortedVector<IndexMetaData>();
	/*
	 * Die Klassennamen der zusaetzlich zum Standard-SQL-Scriptgenerator fuer den Bau eines SQL-Aktualisierungsscripts
	 * an das Diagramm anzubindenden SQLScriptListener.
	 */
	private String additionalSQLScriptListener = "";
	/*
	 * Ein Script fuer den ArchimedesCommandProcessor, das nach dem Speichern eines Modells ausgefuehrt wird.
	 */
	private String afterwritescript = "";
	/* Der Name der Application, f&uuml;r die Code generiert werden soll. */
	private String applicationname = "";
	/* Der Name des Basis-Packages. */
	private String basepackagename = "";
	/* Der Name der CodeFactory-Klasse zum Diagramm. */
	private String codefactoryclassname = "";
	/* Basispfad f&uuml;r die Codegenerierung. */
	private String codepfad = "";
	/* Die Historie zum Diagramm. */
	private String history = "";
	/* Ein Schemaname zur Nutzung in der Kommunikation mit dem DBMS. */
	private String schemaName = "";
	/* Der alternative Name f&uuml;r die Basisklasse der Udschebtis. */
	private String ubcn = "";
	/* Ein Kurzkommentar zur Version. */
	private String versionskommentar = "";
	/* Die Liste der an dem Diagramm lauschenden DiagrammModelListener. */
	private Vector diagrammmodellistener = new Vector();
	/* The list of the database connections of the diagram. */
	private SortedVector<DatabaseConnection> connections = new SortedVector<DatabaseConnection>();
	private PredeterminedOptionProvider pop = null;
	private String owner = "";
	private String additionalDiagramInfo = "";
	private DomainShowMode domainShowMode = DomainShowMode.ALL;
	private String modelCheckerScript = "";

	/**
	 * Generiert ein leeres Diagramm.
	 */
	public Diagramm() {
		super();
	}

	/* Implementierung des Interfaces DiagrammModel. */

	@Override
	public Object get(int id) throws IllegalArgumentException {
		switch (id) {
			case ID_NAME:
				return this.getName();
			case ID_AUTOR:
				return this.getAuthor();
			case ID_COMMENT:
				return this.getComment();
			case ID_VERSION:
				return this.getVersion();
			case ID_VERSIONSDATUM:
				return this.getDate();
			case ID_VERSIONSKOMMENTAR:
				return this.getVersionComment();
			case ID_SCHRIFTGROESSE_TABELLEN:
				return new Integer(this.getFontSizeTableContents());
			case ID_SCHRIFTGROESSE_UEBERSCHRIFT:
				return new Integer(this.getFontSizeDiagramHeadline());
			case ID_SCHRIFTGROESSE_UNTERTITEL:
				return new Integer(this.getFontSizeSubtitles());
			case ID_HIDE_DEPRECATED:
				return new Boolean(this.isAufgehobeneAusblenden());
			case ID_CODEPFAD:
				return this.getCodePfad();
			case ID_APPLICATIONNAME:
				return this.getApplicationName();
			case ID_DBVERSIONTABLENAME:
				return this.getDBVersionTablename();
			case ID_SHOWREFERENCEDCOLUMNAMES:
				return new Boolean(this.isShowReferencedColumns());
			case ID_DBVERSIONDBVERSIONCOLUMN:
				return this.getDBVersionDBVersionColumn();
			case ID_DBVERSIONDESCRIPTIONCOLUMN:
				return this.getDBVersionDescriptionColumn();
			case ID_VIEWS:
				return this.getViews();
			case ID_MARKUPWRITEABLEMEMBER:
				return new Boolean(this.markUpRequiredFieldNames);
			case ID_BASEPACKAGENAME:
				return this.getBasePackageName();
			case ID_CODEFACTORYCLASSNAME:
				return this.getCodeFactoryClassName();
			case ID_UDSCHEBTIBASECLASSNAME:
				return this.getUdschebtiBaseClassName();
			case ID_PAINTTECHNICALFIELDSINGRAY:
				return this.isPaintTechnicalFieldsInGray();
			case ID_AFTERWRITESCRIPT:
				return this.getAfterWriteScript();
			case ID_ADDITIONALSCRIPTLISTENER:
				return this.getAdditionalSQLScriptListener();
			case ID_HISTORY:
				return this.getHistory();
			case ID_SCHEMA_NAME:
				return this.getSchemaName();
			case ID_OPTIONS:
				return this.getOptions();
			case ID_RELATION_COLOR_EXTERNAL_TABLES:
				return this.getRelationColorToExternalTables();
			case ID_RELATION_COLOR_REGULAR:
				return this.getRelationColorRegular();
			case ID_PAINTTRANSIENTFIELDSINGRAY:
				return this.isPaintTransientFieldsInGray();
			case ID_OWNER:
				return this.getOwner();
			case ID_ADDITIONAL_DIAGRAM_INFO:
				return this.getAdditionalDiagramInfo();
			case ID_DOMAIN_SHOW_MODE:
				return this.getDomainShowMode();
			case ID_MODEL_CHECKER_SCRIPT:
				return getModelCheckerScript();
		}
		throw new IllegalArgumentException("Klasse Domain verfuegt nicht ueber ein Attribut " + id + " (get)!");
	}

	@Override
	public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
		switch (id) {
			case ID_NAME:
				this.setName((String) value);
				return;
			case ID_AUTOR:
				this.setAuthor((String) value);
				return;
			case ID_COMMENT:
				this.setComment((String) value);
				return;
			case ID_VERSION:
				this.setVersion((String) value);
				return;
			case ID_VERSIONSDATUM:
				this.setDate((PDate) value);
				return;
			case ID_VERSIONSKOMMENTAR:
				this.setVersionComment((String) value);
				return;
			case ID_SCHRIFTGROESSE_TABELLEN:
				this.setFontSizeTableContents(((Integer) value).intValue());
				return;
			case ID_SCHRIFTGROESSE_UEBERSCHRIFT:
				this.setFontSizeDiagramHeadline(((Integer) value).intValue());
				return;
			case ID_SCHRIFTGROESSE_UNTERTITEL:
				this.setFontSizeSubtitles(((Integer) value).intValue());
				return;
			case ID_HIDE_DEPRECATED:
				this.setAufgehobeneAusblenden(((Boolean) value).booleanValue());
				return;
			case ID_CODEPFAD:
				this.setCodePfad((String) value);
				return;
			case ID_APPLICATIONNAME:
				this.setApplicationName((String) value);
				return;
			case ID_DBVERSIONTABLENAME:
				this.setDBVersionTablename((String) value);
				return;
			case ID_SHOWREFERENCEDCOLUMNAMES:
				this.setShowReferencedColumns(((Boolean) value).booleanValue());
				return;
			case ID_DBVERSIONDBVERSIONCOLUMN:
				this.setDBVersionDBVersionColumn((String) value);
				return;
			case ID_DBVERSIONDESCRIPTIONCOLUMN:
				this.setDBVersionDescriptionColumn((String) value);
				return;
			case ID_MARKUPWRITEABLEMEMBER:
				this.markWriteablemembers(((Boolean) value).booleanValue());
				return;
			case ID_BASEPACKAGENAME:
				this.setBasePackageName((String) value);
				return;
			case ID_CODEFACTORYCLASSNAME:
				this.setCodeFactoryClassName((String) value);
				return;
			case ID_UDSCHEBTIBASECLASSNAME:
				this.setUdschebtiBaseClassName((String) value);
				return;
			case ID_PAINTTECHNICALFIELDSINGRAY:
				this.setPaintTechnicalFieldsInGray(((Boolean) value).booleanValue());
				return;
			case ID_AFTERWRITESCRIPT:
				this.setAfterWriteScript((String) value);
				return;
			case ID_ADDITIONALSCRIPTLISTENER:
				this.setAdditionalSQLScriptListener((String) value);
				return;
			case ID_HISTORY:
				this.setHistory((String) value);
				return;
			case ID_SCHEMA_NAME:
				this.setSchemaName((String) value);
				return;
			case ID_RELATION_COLOR_EXTERNAL_TABLES:
				this.setRelationColorExternalTables((Color) value);
				return;
			case ID_RELATION_COLOR_REGULAR:
				this.setRelationColorRegular((Color) value);
				return;
			case ID_PAINTTRANSIENTFIELDSINGRAY:
				this.setPaintTransientFieldsInGray(((Boolean) value).booleanValue());
				return;
			case ID_OWNER:
				this.setOwner((String) value);
				return;
			case ID_ADDITIONAL_DIAGRAM_INFO:
				this.setAdditionalDiagramInfo((String) value);
				return;
			case ID_DOMAIN_SHOW_MODE:
				this.setDomainShowMode((DomainShowMode) value);
				return;
			case ID_MODEL_CHECKER_SCRIPT:
				setModelCheckerScript((String) value);
				return;
		}
		throw new IllegalArgumentException("Klasse Domain verfuegt nicht ueber ein Attribut " + id + " (set)!");
	}

	@Override
	public int compareTo(Object o) {
		Diagramm d = (Diagramm) o;
		int erg = this.getName().compareTo(d.getName());
		if (erg == 0) {
			return this.getVersion().compareTo(d.getVersion());
		}
		return erg;
	}

	@Override
	public EditorDescriptorList getEditorDescriptorList() {
		DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
		DefaultComponentFactory dcfcol = new DefaultComponentFactory(Archimedes.PALETTE.getColors());
		DefaultComponentFactory dcfdsm = new DefaultComponentFactory(Arrays.asList(DomainShowMode.values()));
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								0,
								this,
								ID_NAME,
								dlf,
								dcf,
								"Name",
								'N',
								null,
								"Der Name des Diagramms"));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								0,
								this,
								ID_AUTOR,
								dlf,
								dcf,
								"Autor",
								'A',
								null,
								"Der Autor des Diagramms"));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								0,
								this,
								ID_OWNER,
								dlf,
								dcf,
								StrUtil.FromHTML("Eigent&uuml;mer"),
								'E',
								null,
								StrUtil.FromHTML("Der Eigent&uuml;mer des Diagramms.")));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								1,
								this,
								ID_VERSION,
								dlf,
								dcf,
								"Versionsnummer",
								'\0',
								null,
								"Die aktuelle Versionsnummer des Diagramms",
								true));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								1,
								this,
								ID_VERSIONSDATUM,
								dlf,
								dcf,
								"Datum",
								'\n',
								null,
								"Das Datum, an dem die aktuelle Version erstellt wurde.",
								true));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								1,
								this,
								ID_VERSIONSKOMMENTAR,
								dlf,
								dcf,
								"Kommentar",
								'K',
								null,
								"Ein kurzer Versionskommentar zur Aufnahme in die " + "Datenbank"));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								1,
								this,
								ID_HIDE_DEPRECATED,
								dlf,
								dcf,
								"Aufgehoben ausblenden",
								'A',
								null,
								"Setzen Sie diese Flagge, um "
										+ "aufgehobene Objekte aus Anzeige und Druck auszublenden"));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								1,
								this,
								ID_PAINTTECHNICALFIELDSINGRAY,
								dlf,
								dcf,
								"Technische Felder ausgrauen",
								'A',
								null,
								"Setzen Sie diese Flagge, um " + "technische Felder abzugrauen."));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								1,
								this,
								ID_PAINTTRANSIENTFIELDSINGRAY,
								dlf,
								dcf,
								"Transiente Felder ausgrauen",
								'T',
								null,
								"Setzen Sie diese Flagge, um " + "transiente Felder abzugrauen."));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								1,
								this,
								ID_ADDITIONAL_DIAGRAM_INFO,
								dlf,
								dcf,
								StrUtil.FromHTML("Zus&auml;tzliche Diagramminfo"),
								'Z',
								null,
								"Hier k&ouml;nnen Sie zus&auml;tzliche Informationen zur Ausgabe auf dem Arbeitsblatt " +
								 "angeben."));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								2,
								this,
								ID_SCHRIFTGROESSE_TABELLEN,
								dlf,
								dcf,
								StrUtil.FromHTML("Schriftgr&ouml;&szlig;e Tabelleninhalte"),
								'T',
								null,
								StrUtil
										.FromHTML(
												"Schriftgr&ouml;&szlig;e f&uuml;r die Tabelleninhalte des "
														+ "Diagramms")));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								2,
								this,
								ID_SCHRIFTGROESSE_UEBERSCHRIFT,
								dlf,
								dcf,
								StrUtil.FromHTML("Schriftgr&ouml;&szlig;e &Uuml;berschrift"),
								'F',
								null,
								StrUtil
										.FromHTML(
												"Schriftgr&ouml;&szlig;e f&uuml;r die &Uuml;berschrift "
														+ "des Diagramms")));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								2,
								this,
								ID_SCHRIFTGROESSE_UNTERTITEL,
								dlf,
								dcf,
								StrUtil.FromHTML("Schriftgr&ouml;&szlig;e Untertitel"),
								'U',
								null,
								StrUtil.FromHTML("Schriftgr&ouml;&szlig;e f&uuml;r die Untertitel des Diagramms")));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								2,
								this,
								ID_SHOWREFERENCEDCOLUMNAMES,
								dlf,
								dcf,
								"Referenzierte Spalten anzeigen (Default)",
								'R',
								null,
								"Setzen Sie diese "
										+ "Flagge, um die Namen der durch Foreignkeys referenzierten Spalten\nim "
										+ "Diagramm angezeigt zu bekommen (Defaultwert)"));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								2,
								this,
								ID_MARKUPWRITEABLEMEMBER,
								dlf,
								dcf,
								"Pflichtfelder markieren",
								'P',
								null,
								"Setzen Sie diese Flagge, um die " + "Plichtfelder im Diagramm zu kennzeichnen."));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								2,
								this,
								ID_RELATION_COLOR_EXTERNAL_TABLES,
								dlf,
								dcfcol,
								Str.fromHTML("Farbe externe Relationen"),
								'\0',
								null,
								Str.fromHTML("Setzen sie hier die Farbe f&uuml;r externe Relationen.")));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								2,
								this,
								ID_RELATION_COLOR_REGULAR,
								dlf,
								dcfcol,
								Str.fromHTML("Farbe regul&auml;re Relationen"),
								'\0',
								null,
								Str.fromHTML("Setzen sie hier die Farbe f&uuml;r regul&auml;re Relationen.")));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								2,
								this,
								ID_DOMAIN_SHOW_MODE,
								dlf,
								dcfdsm,
								Str.fromHTML("Farbe regul&auml;re Relationen"),
								'\0',
								null,
								Str.fromHTML("Setzen sie hier die Farbe f&uuml;r regul&auml;re Relationen.")));
		dedl.addElement(new DefaultSubEditorDescriptor(3, this, new CommentSubEditorFactory()));
		dedl.addElement(new DefaultSubEditorDescriptor(4, this, new HistoryOwnerSubEditorFactory()));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								5,
								this,
								ID_CODEFACTORYCLASSNAME,
								dlf,
								dcf,
								"CodeFactory-Klassenname",
								'C',
								null,
								"Der (qualifizierte) Name der " + "CodeFactory-Klasse zum Diagramm."));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								5,
								this,
								ID_CODEPFAD,
								dlf,
								dcf,
								"Code-Basis-Pfad",
								'B',
								null,
								StrUtil.FromHTML("Basispfad f&uuml;r den " + "Codegenerator")));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								5,
								this,
								ID_APPLICATIONNAME,
								dlf,
								dcf,
								"Applikationsname",
								'N',
								null,
								StrUtil
										.FromHTML(
												"Der Name der Applikation, "
														+ "f&uuml;r die der Code generiert werden soll.")));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								5,
								this,
								ID_BASEPACKAGENAME,
								dlf,
								dcf,
								"Basis-Packagename",
								'B',
								null,
								"Der Name des Basis-Packages der Applikation"));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								5,
								this,
								ID_UDSCHEBTIBASECLASSNAME,
								dlf,
								dcf,
								"Udschebti-Basisklasse",
								'U',
								null,
								StrUtil
										.FromHTML(
												"Der Name der "
														+ "Basisklasse f&uuml;r die Datenobjekte der Applikation.")));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								6,
								this,
								ID_DBVERSIONTABLENAME,
								dlf,
								dcf,
								"DB-Versionen-Tabellenname",
								'D',
								null,
								"Der Name der DB-Versionen-Tabelle, " + "falls eine solche existiert."));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								6,
								this,
								ID_DBVERSIONDBVERSIONCOLUMN,
								dlf,
								dcf,
								"DB-Versionen-Versionsspalte",
								'V',
								null,
								"Der Name der Versionsspalte in "
										+ "der DB-Versionen-Tabelle, falls eine solche existiert."));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								6,
								this,
								ID_DBVERSIONDESCRIPTIONCOLUMN,
								dlf,
								dcf,
								"DB-Versionen-Beschreibungsspalte",
								'B',
								null,
								"Der Name der " + "Beschreibungsspalte in der DB-Versionen-Tabelle, falls eine solche "
										+ "existiert."));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								6,
								this,
								ID_ADDITIONALSCRIPTLISTENER,
								dlf,
								dcf,
								StrUtil.FromHTML("Zus&auml;tzliche SQLScriptListener"),
								'Q',
								null,
								StrUtil
										.FromHTML(
												"Der Name der Klasse, die als SQLScripListener &uuml;ber "
														+
														"&Auml;nderungen bei Bau von SQL-Aktualisierungscripten " +
														 "benachrichtigt werden"
														+ "sollen.")));
		dedl
				.addElement(
						new DefaultEditorDescriptor(
								6,
								this,
								ID_SCHEMA_NAME,
								dlf,
								dcf,
								StrUtil.FromHTML("Schemaname (falls erforderlich)"),
								'S',
								null,
								StrUtil
										.FromHTML(
												"Ein Schemaname, falls das Modell f&uuml;r ein Schema "
														+ "innerhalb der Datenbank gedacht ist.")));
		dedl
				.addElement(
						new DefaultSubEditorDescriptor(
								7,
								this,
								new OptionListSubEditorFactory(
										DefaultVectorPanelButtonFactory.INSTANCE,
										Archimedes.guiBundle,
										this.getPredeterminedOptionProvider(),
										OptionType.MODEL)));
		dedl.addElement(new DefaultSubEditorDescriptor(8, this, new ModelCheckerScriptSubEditorFactory()));
		return dedl;
	}

	@Override
	public Object createObject() {
		// return new Diagramm();
		return Archimedes.Factory.createDiagramm();
	}

	@Override
	public Object createObject(Object blueprint) throws ClassCastException {
		if (!(blueprint instanceof Diagramm)) {
			throw new ClassCastException("Instance of Diagramm required!");
		}
		Diagramm d = (Diagramm) this.createObject();
		d.setName(this.getName());
		d.setComment(this.getComment());
		d.setAuthor(this.getAuthor());
		d.setVersion(this.getVersion());
		d.setDate(this.getDate());
		d.setVersionComment(this.getVersionComment());
		d.setAufgehobeneAusblenden(this.isAufgehobeneAusblenden());
		d.setApplicationName(this.getApplicationName());
		d.setDBVersionTablename(this.getDBVersionTablename());
		return d;
	}

	@Override
	public TabbedPaneFactory getTabbedPaneFactory() {
		return new DefaultTabbedPaneFactory(
				new TabDescriptor[]{
						new DefaultTabDescriptor("Allgemeines", 'A', null),
						new DefaultTabDescriptor("Version", 'V', null),
						new DefaultTabDescriptor("Schrift", 'S', null),
						new DefaultTabDescriptor("Beschreibung", 'B', null),
						new DefaultTabDescriptor("Historie", 'H', null),
						new DefaultTabDescriptor("Codegenerator", 'C', null),
						new DefaultTabDescriptor("SQL-Generator", 'Q', null),
						new DefaultTabDescriptor("Optionen", 'O', null),
						new DefaultTabDescriptor("Model-Checker-Script", 'M', null)
				});
	}

	@Override
	public boolean isTabEnabled(int no) {
		return true;
	}

	@Override
	public DiagrammModel createDiagramm() {
		// return new Diagramm();
		return Archimedes.Factory.createDiagramm();
	}

	@Override
	public DomainModel createDomain() {
		return Archimedes.Factory.createDomain();
	}

	@Override
	public void removeDomain(DomainModel dom) {
		this.domains.removeElement(dom);
	}

	@Override
	public Vector getDomains() {
		return new Vector(this.domains);
	}

	@Override
	public Vector getDomainsReference() {
		return this.domains;
	}

	@Override
	public DomainModel getDomain(String n) {
		for (int i = 0, len = this.domains.size(); i < len; i++) {
			DomainModel dm = this.domains.elementAt(i);
			if (dm.getName().equals(n)) {
				return dm;
			}
		}
		throw new NoSuchElementException("Domain mit Namen \"" + n + "\" existiert nicht!");
	}

	@Override
	public DomainModel getDomainByType(String sqltype) {
		for (int i = 0, len = this.domains.size(); i < len; i++) {
			DomainModel dm = this.domains.elementAt(i);
			if (dm.getType().equals(sqltype)) {
				return dm;
			}
		}
		return null;
	}

	@Override
	public void addTabelle(TabellenModel t) {
		this.tables.addElement(t);
	}

	@Override
	public void removeTabelle(TabellenModel t) {
		this.tables.removeElement(t);
	}

	@Override
	@Deprecated
	public Vector getTabellen() {
		return new Vector(this.tables);
	}

	@Override
	@Deprecated
	public Vector getAlleFelder() {
		return getAllFields();
	}

	@Override
	@Deprecated
	public Vector getAllFields() {
		int i = 0;
		int j = 0;
		int len = 0;
		int lenj = 0;
		SortedVector fields = new SortedVector();
		TabellenModel tm = null;
		Vector<TabellenspaltenModel> v = null;
		for (i = 0, len = this.tables.size(); i < len; i++) {
			tm = this.tables.elementAt(i);
			if (!tm.isDeprecated()) {
				v = tm.getTabellenspalten();
				for (j = 0, lenj = v.size(); j < lenj; j++) {
					fields.addElement(v.elementAt(j));
				}
			}
		}
		return fields;
	}

	@Override
	public Vector getFieldCache() {
		return this.cacheFieldModel;
	}

	@Override
	public void addToFieldCache(TabellenspaltenModel tsm) {
		this.cacheFieldModel.addElement(tsm);
	}

	@Override
	public void removeFromFieldCache(TabellenspaltenModel tsm) {
		this.cacheFieldModel.removeElement(tsm);
	}

	@Override
	@Deprecated
	public TabellenModel getTabelle(String n) {
		for (int i = 0, len = this.tables.size(); i < len; i++) {
			TabellenModel tm = this.tables.elementAt(i);
			if (tm.getName().equals(n)) {
				return tm;
			}
		}
		return null;
		// throw new NoSuchElementException("Tabelle mit Namen \"" + n +
		// "\" existiert nicht!");
	}

	@Override
	public SortedVector getKeyColumns(TabellenModel t) {
		int i = 0;
		int j = 0;
		int leni = 0;
		int lenj = 0;
		SortedVector sv = new SortedVector();
		TabellenModel tm = null;
		TabellenspaltenModel tsm = null;
		for (i = 0, leni = this.tables.size(); i < leni; i++) {
			tm = this.tables.elementAt(i);
			if (tm != t) {
				for (j = 0, lenj = tm.getTabellenspaltenCount(); j < lenj; j++) {
					tsm = tm.getTabellenspalteAt(j);
					if (tsm.isPrimarykey()) {
						sv.addElement(tsm);
					}
				}
			}
		}
		return sv;
	}

	@Override
	public SortedVector getKeycolumns(TabellenModel t) {
		return this.getKeyColumns(t);
	}

	@Override
	public SortedVector getColumnsToBeReferenced(TabellenModel t) {
		SortedVector sv = new SortedVector();
		TabellenModel tm = null;
		TabellenspaltenModel tsm = null;
		Vector v = null;
		for (int i = 0, len = this.tables.size(); i < len; i++) {
			tm = this.tables.elementAt(i);
			if (tm != t) {
				v = tm.getTabellenspalten();
				for (int j = 0, lenj = v.size(); j < lenj; j++) {
					tsm = (TabellenspaltenModel) v.elementAt(j);
					if (tsm.isPrimarykey() || tsm.isCanBeReferenced()) {
						sv.addElement(tsm);
					}
				}
			}
		}
		return sv;
	}

	private String toHTML(String s) {
		return AbstractSTFHandler.toHTML(s);
	}

	private String fromHTML(String s) {
		return AbstractSTFHandler.fromHTML(s);
	}

	/**
	 * @changed OLI 29.08.2007 - Erweiterung um Konsolenausgaben zwecks Zuordnung von Fehlermeldungen beim Speichern
	  * von
	 * Diagrammen. Diese werden allerdings nur dann angezeigt, wenn die Property
	 * <TT>archimedes.scheme.Diagramm</TT> (Boolean) gesetzt wird.
	 * @changed OLI 21.05.2010 - Die Daten zur Versionstabelle werden immer geschrieben.
	 */
	@Override
	public StructuredTextFile toSTF(DiagramSaveMode dsm) throws Exception {
		boolean cout = Boolean.getBoolean("archimedes.scheme.Diagramm.cout");
		if (cout) {
			LOG.info("storing diagram to stf");
		}
		java.util.List<ViewModel> tvms = null;
		StructuredTextFile stf = new StructuredTextFile("");
		GUIViewModel mvm = null;
		stf.setHTMLCoding(true);
		if (dsm == DiagramSaveMode.REGULAR) {
			if (this.importDSR != null) {
				this.importDSR.toSTF(
						stf,
						new String[]{
								"Diagramm",
								"DataSource",
								"Import"
						});
				if (cout) {
					LOG.info("    import dsr written.");
				}
			}
			/*
			 * if (this.updateDSR != null) { this.updateDSR.toSTF(stf, new String[] {"Diagramm", "DataSource",
			 * "Update"}); if (cout) { LOG.info("    update dsr written."); } }
			 */
			new STFColorWriter().write(stf, Archimedes.PALETTE.getColors().toArray(new ExtendedColor[0]));
			for (int i = 0, len = this.getViews().size(); i < len; i++) {
				GUIViewModel vm = this.getViews().get(i);
				if (vm instanceof MainViewModel) {
					mvm = vm;
					break;
				}
			}
		}
		new STFDatabaseConnectionWriter().write(stf, this.getDatabaseConnections(), dsm);
		new STFDomainWriter().write(stf, this.getAllDomains(), dsm);
		new STFSequenceWriter().write(stf, this.getSequences(), dsm);
		new STFAdditionalSQLCodeWriter().write(stf, this);
		new STFStereotypeWriter().write(stf, this.getAllStereotypes(), dsm);
		int len = this.defaultComments.size();
		stf.writeStr(new String[]{
				"Diagramm",
				"Factories",
				"Object"
		}, Archimedes.Factory.getClass().getName());
		stf.writeLong(new String[]{
				"Diagramm",
				"Pages",
				"PerColumn"
		}, ComponentDiagramm.PAGESPERCOLUMN);
		stf.writeLong(new String[]{
				"Diagramm",
				"Pages",
				"PerRow"
		}, ComponentDiagramm.PAGESPERROW);
		if (dsm == DiagramSaveMode.REGULAR) {
			stf.writeLong(new String[]{
					"Diagramm",
					"DefaultComment",
					"Anzahl"
			}, len);
			if (cout) {
				LOG.info("    default comments");
			}
			for (int i = 0; i < len; i++) {
				DefaultCommentModel dcm = (DefaultCommentModel) this.defaultComments.elementAt(i);
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"DefaultComment",
										"DefaultComment" + i,
										"Muster"
								},
								this.toHTML(dcm.getPattern()));
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"DefaultComment",
										"DefaultComment" + i,
										"Kommentar"
								},
								this.toHTML(dcm.getDefaultComment()));
				if (cout) {
					LOG.info("        " + dcm.getPattern() + " written.");
				}
			}
		}
		new STFDiagrammParameterWriter().write(stf, this, dsm);
		len = this.tables.size();
		stf.writeLong(new String[]{
				"Diagramm",
				"Tabellen",
				"Anzahl"
		}, len);
		if (cout) {
			LOG.info("    tables:");
		}
		for (int i = 0; i < len; i++) {
			TabellenModel tm = this.tables.elementAt(i);
			stf.writeStr(new String[]{
					"Diagramm",
					"Tabellen",
					"Tabelle" + i,
					"Name"
			}, this.toHTML(tm.getName()));
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Aufgehoben"
							},
							new Boolean(tm.isDeprecated()).toString());
			if (dsm == DiagramSaveMode.REGULAR) {
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Kommentar"
								},
								this.toHTML(tm.getComment()));
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Farben",
										"Schrift"
								},
								this.toHTML(tm.getSchriftfarbe().toString()));
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Farben",
										"Hintergrund"
								},
								this
										.toHTML(
												(tm.getHintergrundfarbe() != null
														? tm.getHintergrundfarbe().toString()
														: "null")));
			}
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"NMRelation"
							},
							new Boolean(tm.isNMRelation()).toString());
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"InDevelopment"
							},
							new Boolean(tm.isDraft()).toString());
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"ExternalTable"
							},
							new Boolean(tm.isExternalTable()).toString());
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"FirstGenerationDone"
							},
							new Boolean(tm.isFirstGenerationDone()).toString());
			/*
			 * stf.writeLong(new String[] {"Diagramm", "Tabellen", "Tabelle" + i, "X"}, tm.getX(
			 * this.getViews().get(0))); stf.writeLong(new String[] {"Diagramm", "Tabellen", "Tabelle" + i, "Y"},
			 * tm.getY( this.getViews().get(0)));
			 */
			if (dsm == DiagramSaveMode.REGULAR) {
				tvms = tm.getViews();
				stf.writeLong(new String[]{
						"Diagramm",
						"Tabellen",
						"Tabelle" + i,
						"Views",
						"Anzahl"
				}, tvms.size());
				if (cout) {
					LOG.info("        views for table:");
				}
				for (int j = 0, lenj = tvms.size(); j < lenj; j++) {
					ViewModel vm = tvms.get(j);
					stf
							.writeStr(
									new String[]{
											"Diagramm",
											"Tabellen",
											"Tabelle" + i,
											"Views",
											"View" + j,
											"Name"
									},
									this.toHTML(vm.getName()));
					stf
							.writeLong(
									new String[]{
											"Diagramm",
											"Tabellen",
											"Tabelle" + i,
											"Views",
											"View" + j,
											"X"
									},
									tm.getX(vm));
					stf
							.writeLong(
									new String[]{
											"Diagramm",
											"Tabellen",
											"Tabelle" + i,
											"Views",
											"View" + j,
											"Y"
									},
									tm.getY(vm));
					if (cout) {
						LOG.info("            " + vm.getName() + " written.");
					}
				}
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"History"
								},
								this.toHTML(tm.getHistory()));
			}
			stf
					.writeLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Stereotype",
									"Anzahl"
							},
							tm.getStereotypenCount());
			for (int j = 0, lenj = tm.getStereotypenCount(); j < lenj; j++) {
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Stereotype" + j
								},
								this.toHTML(tm.getStereotypeAt(j).toString()));
			}
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Spalten",
									"Codegenerator",
									"Codieren"
							},
							new Boolean(tm.isGenerateCode()).toString());
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Spalten",
									"Codegenerator",
									"Codegeneratoroptionen"
							},
							tm.getGenerateCodeOptions());
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Spalten",
									"Codegenerator",
									"Codeverzeichnis"
							},
							tm.getCodeVerzeichnis());
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Spalten",
									"Codegenerator",
									"Kontextname"
							},
							tm.getContextName());
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Spalten",
									"Codegenerator",
									"ComplexForeignKey"
							},
							tm.getComplexForeignKeyDefinition());
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Spalten",
									"Codegenerator",
									"UniqueFormula"
							},
							tm.getComplexUniqueSpecification());
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Spalten",
									"Codegenerator",
									"DynamicCode"
							},
							new Boolean(tm.isDynamicCode()).toString());
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Spalten",
									"Codegenerator",
									"Inherited"
							},
							new Boolean(tm.isInherited()).toString());
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Spalten",
									"Codegenerator",
									"ActiveInApplication"
							},
							new Boolean(tm.isActiveInApplication()).toString());
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Spalten",
									"Codegenerator",
									"AdditionalCreateConstraints"
							},
							tm.getAdditionalCreateConstraints());
			stf
					.writeLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Spalten",
									"Anzahl"
							},
							tm.getTabellenspaltenCount());
			if (cout) {
				LOG.info("        columns of the table:");
			}
			for (int j = 0, lenj = tm.getTabellenspaltenCount(); j < lenj; j++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(j);
				try {
					new STFColumnWriter().write(stf, tsm, i, j, (ViewModel) mvm, dsm);
				} catch (Exception etsm) {
					throw new Exception("Problems while saving column " + tsm.getFullName() + "!", etsm);
				}
			}
			ColumnModel[] equalsMembers = tm.getEqualsMembers();
			int lenj = equalsMembers.length;
			stf
					.writeLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"Equalsmembers",
									"Anzahl"
							},
							lenj);
			for (int j = 0; j < lenj; j++) {
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"Equalsmembers",
										"Member" + j,
										"Spalte"
								},
								equalsMembers[j].getName());
			}
			ColumnModel[] cs = ((TableModel) tm).getCompareMembers();
			stf
					.writeLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"CompareMembers",
									"Anzahl"
							},
							cs.length);
			for (int j = 0; j < cs.length; j++) {
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"CompareMembers",
										"Member" + j,
										"Spalte"
								},
								cs[j].getName());
			}
			ColumnModel[] hashCodeMembers = tm.getHashCodeMembers();
			lenj = hashCodeMembers.length;
			stf
					.writeLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"HashCodeMembers",
									"Anzahl"
							},
							lenj);
			for (int j = 0; j < lenj; j++) {
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"HashCodeMembers",
										"Member" + j,
										"Spalte"
								},
								hashCodeMembers[j].getName());
			}
			ToStringContainer[] tscs = (ToStringContainer[]) tm.getToStringMembers();
			lenj = tscs.length;
			stf
					.writeLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"ToStringMembers",
									"Anzahl"
							},
							lenj);
			for (int j = 0; j < lenj; j++) {
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"ToStringMembers",
										"Member" + j,
										"Spalte"
								},
								tscs[j].getTabellenspalte().getName());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"ToStringMembers",
										"Member" + j,
										"Prefix"
								},
								tscs[j].getPrefix());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"ToStringMembers",
										"Member" + j,
										"Suffix"
								},
								tscs[j].getSuffix());
			}
			tscs = (ToStringContainer[]) tm.getComboStringMembers();
			lenj = tscs.length;
			stf
					.writeLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"ToComboStringMembers",
									"Anzahl"
							},
							lenj);
			for (int j = 0; j < lenj; j++) {
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"ToComboStringMembers",
										"Member" + j,
										"Spalte"
								},
								tscs[j].getTabellenspalte().getName());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"ToComboStringMembers",
										"Member" + j,
										"Prefix"
								},
								tscs[j].getPrefix());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"ToComboStringMembers",
										"Member" + j,
										"Suffix"
								},
								tscs[j].getSuffix());
			}
			Vector v = tm.getAuswahlMembers();
			lenj = v.size();
			stf
					.writeLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"AuswahlMembers",
									"Anzahl"
							},
							lenj);
			for (int j = 0; j < lenj; j++) {
				// TabellenspaltenModel tsm0 = (TabellenspaltenModel)
				// v.elementAt(j);
				SelectionMemberModel smm = (SelectionMemberModel) v.elementAt(j);
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"AuswahlMembers",
										"Member" + j,
										"Spalte"
								},
								smm.getColumn().getName());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"AuswahlMembers",
										"Member" + j,
										"Tabelle"
								},
								smm.getColumn().getTable().getName());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"AuswahlMembers",
										"Member" + j,
										"Attribute"
								},
								smm.getAttribute().toString());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"AuswahlMembers",
										"Member" + j,
										"PrintExpression"
								},
								(smm.getPrintExpression() == null
										? ""
										: smm.getPrintExpression()));
			}
			OrderMemberModel[] oms = tm.getSelectionViewOrderMembers();
			lenj = oms.length;
			stf
					.writeLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"OrderMembers",
									"Anzahl"
							},
							lenj);
			if (cout) {
				LOG.info("        order members:");
			}
			for (int j = 0; j < lenj; j++) {
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"OrderMembers",
										"Member" + j,
										"Spalte"
								},
								oms[j].getOrderColumn().getName());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"OrderMembers",
										"Member" + j,
										"Tabelle"
								},
								oms[j].getOrderColumn().getTable().getName());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"OrderMembers",
										"Member" + j,
										"Richtung"
								},
								(oms[j].getOrderDirection() != null
										? oms[j].getOrderDirection().toString()
										: "ASC"));
				if (cout) {
					LOG.info("            " + oms[j].toString() + " written.");
				}
			}
			new STFNReferenceWriter().write(stf, i, tm);
			new STFOptionWriter().write(stf, tm, i);
			stf
					.writeLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Panels",
									"Anzahl"
							},
							tm.getPanelCount());
			lenj = tm.getPanelCount();
			if (cout) {
				LOG.info("        panels:");
			}
			for (int j = 0; j < lenj; j++) {
				PanelModel pm = tm.getPanelAt(j);
				stf
						.writeLong(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Panels",
										"Panel" + j,
										"PanelNumber"
								},
								pm.getPanelNumber());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Panels",
										"Panel" + j,
										"TabTitle"
								},
								pm.getTabTitle());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Panels",
										"Panel" + j,
										"TabMnemonic"
								},
								pm.getTabMnemonic());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Panels",
										"Panel" + j,
										"TabToolTipText"
								},
								pm.getTabToolTipText());
				stf
						.writeStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Panels",
										"Panel" + j,
										"PanelClass"
								},
								pm.getPanelClass());
				if (cout) {
					LOG.info("        " + pm.getPanelNumber() + " written.");
				}
			}
		}
		new STFViewWriter().write(stf, this.getViews().toArray(new ViewModel[0]), dsm);
		new ComplexIndexWriter(new DefaultIndexListCleaner(), new DefaultComplexIndicesToSTFWriter()).store(stf, this);
		return stf;
	}

	@Override
	public String toXML() {
		// Hier l&ouml;schen, wenn fertig ...
		StructuredTextFile stf = new StructuredTextFile("");
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
		sb.append("<diagram>").append("\n");
		sb.append("    <datasources>").append("\n");
		;
		if (this.importDSR != null) {
			sb.append("        <import ").append(importDSR.toXMLAttributes()).append("/>").append("\n");
		}
		if (this.updateDSR != null) {
			sb.append("        <update ").append(updateDSR.toXMLAttributes()).append("/>").append("\n");
		}
		sb.append("    </datasources>").append("\n");
		;
		// TODO OLI 04.06.2010 - Das folgende Fragment scheint unsinnig. Dabei
		// gleich mal
		// pruefen, ob das MainViewModel irgendwo gebraucht wird.
		/*
		 * for (int i = 0, len = this.getViews().size(); i < len; i++) { ViewModel vm = this.getViews().get(i); if (vm
		 * instanceof MainViewModel) { break; } }
		 */
		int len = this.domains.size();
		sb.append("    <domains count=\"").append(len).append(" >").append("\n");
		;
		for (int i = 0; i < len; i++) {
			ToXMLAttributes dm = (ToXMLAttributes) this.domains.elementAt(i);
			sb.append("        <domain id=\"").append(i).append("\" ").append(dm.toXMLAttributes()).append(" />\n");
		}
		sb.append("    </domains>").append("\n");
		;
		len = this.stereotypes.size();
		stf.writeLong(new String[]{
				"Diagramm",
				"Stereotype",
				"Anzahl"
		}, len);
		for (int i = 0; i < len; i++) {
			StereotypeModel stm = this.stereotypes.elementAt(i);
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Stereotype",
									"Stereotype" + i,
									"Name"
							},
							this.toHTML(stm.getName()));
			stf
					.writeStr(
							new String[]{
									"Diagramm",
									"Stereotype",
									"Stereotype" + i,
									"Kommentar"
							},
							this.toHTML(stm.getComment()));
		}
		len = this.defaultComments.size();
		return sb.toString();
	}

	@Override
	public DiagrammModel createDiagramm(StructuredTextFile stf) {
		String history = null;
		boolean debug = Boolean.getBoolean("archimedes.debug") || Boolean.getBoolean("archimedes.scheme.debug")
				|| Boolean.getBoolean("archimedes.scheme.Diagramm.debug");
		boolean suppressDots = Boolean.getBoolean("archimedes.scheme.Diagramm.suppress.dots");
		String ofn = stf
				.readStr(new String[]{
						"Diagramm",
						"Factories",
						"Object"
				}, Archimedes.Factory.getClass().getName());
		if (!Archimedes.Factory.getClass().getName().equals(ofn)) {
			JOptionPane
					.showMessageDialog(
							null,
							"Die aktuelle ObjectFactory (" + Archimedes.Factory.getClass().getName()
									+ ") unterscheidet sich von\n"
									+ "der ObjectFactory, unter das Modell zuletzt bearbeitet wurde (" + ofn + ")!",
							"ObjectFactory unterschiedlich",
							JOptionPane.WARNING_MESSAGE);
		}
		boolean output = Boolean.getBoolean("archimedes.scheme.Diagramm.output");
		Diagramm d = (Diagramm) Archimedes.Factory.createDiagramm();
		if (d.importDSR == null) {
			d.importDSR = new ArchimedesImportJDBCDataSourceRecord();
		}
		d.importDSR.fromSTF(
				stf,
				new String[]{
						"Diagramm",
						"DataSource",
						"Import"
				});
		if (d.updateDSR == null) {
			d.updateDSR = new ArchimedesJDBCDataSourceRecord();
		}
		ArchimedesJDBCDataSourceRecord dsr = new ArchimedesJDBCDataSourceRecord();
		dsr.fromSTF(
				stf,
				new String[]{
						"Diagramm",
						"DataSource",
						"Update"
				});
		if (dsr.getDBName().length() > 0) {
			d
					.addDatabaseConnection(
							new DatabaseConnection(
									"Productive database",
									dsr.getDriver(),
									dsr.getDBName(),
									dsr.getUser(),
									dsr.getMode(),
									dsr.hasDomains(),
									dsr.isFkNotNullBeachten(),
									dsr.isReferenzenSetzen(),
									dsr.getQuoteCharacter()));
		}
		int len = (int) stf.readLong(new String[]{
				"Diagramm",
				"Colors",
				"Anzahl"
		}, 0);
		if (len > 0) {
			Archimedes.PALETTE.removeColors();
			new STFColorReader().read(stf, d);
		} else {
			Archimedes.InitPalette();
		}
		new STFDatabaseConnectionReader().read(stf, d);
		new STFDomainReader().read(stf, d);
		new STFSequenceReader().read(stf, d);
		new STFAdditionalSQLCodeReader().read(stf, d);
		int pp = (int) stf.readLong(new String[]{
				"Diagramm",
				"Pages",
				"PerColumn"
		}, 0);
		if (pp > ComponentDiagramm.PAGESPERCOLUMN) {
			LOG.error("\n\n**************************************************");
			LOG.error("\nZuwenig Seiten pro Spalte " + pp + " (" + ComponentDiagramm.PAGESPERCOLUMN + ")");
			LOG.error("\n\n**************************************************");
			System.exit(1);
		}
		pp = (int) stf.readLong(new String[]{
				"Diagramm",
				"Pages",
				"PerRow"
		}, 0);
		if (pp > ComponentDiagramm.PAGESPERROW) {
			LOG.error("\n\n**************************************************");
			LOG.error("\nZuwenig Seiten pro Zeile" + pp + " (" + ComponentDiagramm.PAGESPERROW + ")");
			LOG.error("\n\n**************************************************");
			System.exit(1);
		}
		ComponentDiagramm.MAXPAGECOUNT = ComponentDiagramm.PAGESPERROW * ComponentDiagramm.PAGESPERCOLUMN;
		new STFStereotypeReader().read(stf, d);
		len = (int) stf.readLong(new String[]{
				"Diagramm",
				"DefaultComment",
				"Anzahl"
		}, 0);
		for (int i = 0; i < len; i++) {
			String n = this
					.fromHTML(
							stf
									.readStr(
											new String[]{
													"Diagramm",
													"DefaultComment",
													"DefaultComment" + i,
													"Muster"
											},
											null));
			String k = this
					.fromHTML(
							stf
									.readStr(
											new String[]{
													"Diagramm",
													"DefaultComment",
													"DefaultComment" + i,
													"Kommentar"
											},
											""));
			DefaultCommentModel dcm = Archimedes.Factory.createDefaultComment(n, k);
			d.addDefaultComment(dcm);
		}
		d
				.setShowReferencedColumns(
						new Boolean(
								stf
										.readStr(
												new String[]{
														"Diagramm",
														"Parameter",
														"ReferenzierteSpaltenAnzeigen"
												},
												"TRUE")).booleanValue());
		GUIViewModel vm = null;
		GUIViewModel mvm = (GUIViewModel) new STFViewReader().read(stf, d);
		new STFDiagrammParameterReader().read(stf, d);
		len = (int) stf.readLong(new String[]{
				"Diagramm",
				"Tabellen",
				"Anzahl"
		}, 0);
		if (!suppressDots) {
			LOG.info("found " + len + " tables.");
		}
		for (int i = 0; i < len; i++) {
			String n = this.fromHTML(stf.readStr(new String[]{
					"Diagramm",
					"Tabellen",
					"Tabelle" + i,
					"Name"
			}, null));
			String k =
					this.fromHTML(stf.readStr(new String[]{
							"Diagramm",
							"Tabellen",
							"Tabelle" + i,
							"Kommentar"
					}, ""));
			history = this.fromHTML(stf.readStr(new String[]{
					"Diagramm",
					"Tabellen",
					"Tabelle" + i,
					"History"
			}, ""));
			boolean aufgh = new Boolean(
					stf.readStr(new String[]{
							"Diagramm",
							"Tabellen",
							"Tabelle" + i,
							"Aufgehoben"
					}, "FALSE"))
					.booleanValue();
			String fs = this
					.fromHTML(
							stf
									.readStr(
											new String[]{
													"Diagramm",
													"Tabellen",
													"Tabelle" + i,
													"Farben",
													"Schrift"
											},
											"schwarz"));
			String fhg = this
					.fromHTML(
							stf
									.readStr(
											new String[]{
													"Diagramm",
													"Tabellen",
													"Tabelle" + i,
													"Farben",
													"Hintergrund"
											},
											StrUtil.FromHTML("wei&szlig;")));
			boolean nmrelation = new Boolean(
					stf.readStr(new String[]{
							"Diagramm",
							"Tabellen",
							"Tabelle" + i,
							"NMRelation"
					}, "FALSE"))
					.booleanValue();
			boolean indevelopmentprocess = new Boolean(
					stf.readStr(new String[]{
							"Diagramm",
							"Tabellen",
							"Tabelle" + i,
							"InDevelopment"
					}, "FALSE"))
					.booleanValue();
			boolean externalTable = new Boolean(
					stf.readStr(new String[]{
							"Diagramm",
							"Tabellen",
							"Tabelle" + i,
							"ExternalTable"
					}, "FALSE"))
					.booleanValue();
			boolean firstGenerationDone = new Boolean(
					stf.readStr(new String[]{
							"Diagramm",
							"Tabellen",
							"Tabelle" + i,
							"FirstGenerationDone"
					}, "FALSE"))
					.booleanValue();
			int x = (int) stf.readLong(new String[]{
					"Diagramm",
					"Tabellen",
					"Tabelle" + i,
					"X"
			}, 0);
			int y = (int) stf.readLong(new String[]{
					"Diagramm",
					"Tabellen",
					"Tabelle" + i,
					"Y"
			}, 0);
			vm = mvm;
			TabellenModel tm = Archimedes.Factory.createTabelle((ViewModel) vm, x, y, d, false);
			tm.setSchriftfarbe(Archimedes.PALETTE.get(fs, Color.black));
			tm.setHintergrundfarbe(Archimedes.PALETTE.get(fhg, null));
			tm.setNMRelation(nmrelation);
			tm.setDraft(indevelopmentprocess);
			tm.setFirstGenerationDone(firstGenerationDone);
			tm.setHistory(history);
			tm.setExternalTable(externalTable);
			vm.addObject(tm);
			int lenj =
					(int) stf.readLong(new String[]{
							"Diagramm",
							"Tabellen",
							"Tabelle" + i,
							"Panels",
							"Anzahl"
					}, 0);
			if (lenj > 0) {
				tm.clearPanels();
				for (int j = 0; j < lenj; j++) {
					PanelModel pm = Archimedes.Factory.createPanel();
					pm
							.setPanelNumber(
									(int) stf
											.readLong(
													new String[]{
															"Diagramm",
															"Tabellen",
															"Tabelle" + i,
															"Panels",
															"Panel" + j,
															"PanelNumber"
													},
													0));
					pm
							.setTabTitle(
									stf
											.readStr(
													new String[]{
															"Diagramm",
															"Tabellen",
															"Tabelle" + i,
															"Panels",
															"Panel" + j,
															"TabTitle"
													},
													"" + (j + 1) + ".Tab"));
					pm
							.setTabMnemonic(
									stf
											.readStr(
													new String[]{
															"Diagramm",
															"Tabellen",
															"Tabelle" + i,
															"Panels",
															"Panel" + j,
															"TabMnemonic"
													},
													"" + (j + 1)));
					pm
							.setTabToolTipText(
									stf
											.readStr(
													new String[]{
															"Diagramm",
															"Tabellen",
															"Tabelle" + i,
															"Panels",
															"Panel" + j,
															"TabToolTipText"
													},
													""));
					pm
							.setPanelClass(
									stf
											.readStr(
													new String[]{
															"Diagramm",
															"Tabellen",
															"Tabelle" + i,
															"Panels",
															"Panel" + j,
															"PanelClass"
													},
													""));
					tm.addPanel(pm);
				}
			} else {
				PanelModel pm = Archimedes.Factory.createPanel();
				pm.setPanelNumber(0);
				pm.setTabTitle("data");
				pm.setTabMnemonic("1");
				tm.addPanel(pm);
			}
			lenj = (int) stf.readLong(new String[]{
					"Diagramm",
					"Tabellen",
					"Tabelle" + i,
					"Views",
					"Anzahl"
			}, 0);
			for (int j = 0; j < lenj; j++) {
				String viewname = this
						.fromHTML(
								stf
										.readStr(
												new String[]{
														"Diagramm",
														"Tabellen",
														"Tabelle" + i,
														"Views",
														"View" + j,
														"Name"
												},
												""));
				vm = d.getView(viewname);
				if (vm != null) {
					x = (int) stf
							.readLong(
									new String[]{
											"Diagramm",
											"Tabellen",
											"Tabelle" + i,
											"Views",
											"View" + j,
											"X"
									},
									0);
					y = (int) stf
							.readLong(
									new String[]{
											"Diagramm",
											"Tabellen",
											"Tabelle" + i,
											"Views",
											"View" + j,
											"Y"
									},
									0);
					tm.setXY(vm, x, y);
					vm.addObject(tm);
				} else {
					LOG.warn("View-Informationen fuer Tabelle " + n + " View " + viewname + " nicht laenger gueltig!");
				}
			}
			tm.setName(n);
			tm.setComment(k);
			tm.setDeprecated(aufgh);
			lenj = (int) stf
					.readLong(new String[]{
							"Diagramm",
							"Tabellen",
							"Tabelle" + i,
							"Stereotype",
							"Anzahl"
					}, 0);
			for (int j = 0; j < lenj; j++) {
				String st = this
						.fromHTML(
								stf
										.readStr(
												new String[]{
														"Diagramm",
														"Tabellen",
														"Tabelle" + i,
														"Stereotype" + j
												},
												""));
				try {
					tm.addStereotype(d.getStereotype(st));
				} catch (NoSuchElementException nsee) {
				}
			}
			boolean codieren = new Boolean(
					stf
							.readStr(
									new String[]{
											"Diagramm",
											"Tabellen",
											"Tabelle" + i,
											"Spalten",
											"Codegenerator",
											"Codieren"
									},
									"FALSE")).booleanValue();
			tm.setGenerateCode(codieren);
			tm
					.setGenerateCodeOptions(
							stf
									.readStr(
											new String[]{
													"Diagramm",
													"Tabellen",
													"Tabelle" + i,
													"Spalten",
													"Codegenerator",
													"Codegeneratoroptionen"
											},
											""));
			tm
					.setCodeVerzeichnis(
							stf
									.readStr(
											new String[]{
													"Diagramm",
													"Tabellen",
													"Tabelle" + i,
													"Spalten",
													"Codegenerator",
													"Codeverzeichnis"
											},
											""));
			tm
					.setContextName(
							stf
									.readStr(
											new String[]{
													"Diagramm",
													"Tabellen",
													"Tabelle" + i,
													"Spalten",
													"Codegenerator",
													"Kontextname"
											},
											""));
			tm
					.setComplexForeignKeyDefinition(
							stf
									.readStr(
											new String[]{
													"Diagramm",
													"Tabellen",
													"Tabelle" + i,
													"Spalten",
													"Codegenerator",
													"ComplexForeignKey"
											},
											""));
			tm
					.setComplexUniqueSpecification(
							stf
									.readStr(
											new String[]{
													"Diagramm",
													"Tabellen",
													"Tabelle" + i,
													"Spalten",
													"Codegenerator",
													"UniqueFormula"
											},
											""));
			boolean dynamicCode = new Boolean(
					stf
							.readStr(
									new String[]{
											"Diagramm",
											"Tabellen",
											"Tabelle" + i,
											"Spalten",
											"Codegenerator",
											"DynamicCode"
									},
									"FALSE")).booleanValue();
			tm.setDynamicCode(dynamicCode);
			boolean inherited = new Boolean(
					stf
							.readStr(
									new String[]{
											"Diagramm",
											"Tabellen",
											"Tabelle" + i,
											"Spalten",
											"Codegenerator",
											"Inherited"
									},
									"FALSE")).booleanValue();
			tm.setInherited(inherited);
			boolean activeInApplication = new Boolean(
					stf
							.readStr(
									new String[]{
											"Diagramm",
											"Tabellen",
											"Tabelle" + i,
											"Spalten",
											"Codegenerator",
											"ActiveInApplication"
									},
									"FALSE")).booleanValue();
			tm.setActiveInApplication(activeInApplication);
			tm
					.setAdditionalCreateConstraints(
							stf
									.readStr(
											new String[]{
													"Diagramm",
													"Tabellen",
													"Tabelle" + i,
													"Spalten",
													"Codegenerator",
													"AdditionalCreateConstraints"
											},
											""));
			lenj = (int) stf.readLong(new String[]{
					"Diagramm",
					"Tabellen",
					"Tabelle" + i,
					"Spalten",
					"Anzahl"
			}, 0);
			for (int j = 0; j < lenj; j++) {
				new STFColumnReader().read(stf, d, tm, i, j);
			}
			d.addTabelle(tm);
			if (output) {
				LOG.info(tm.toString() + " added");
			} else if (!suppressDots) {
				System.out.print(".");
			}
		}
		if (!output && !suppressDots) {
			System.out.println();
		}
		len = (int) stf.readLong(new String[]{
				"Diagramm",
				"Tabellen",
				"Anzahl"
		}, 0);
		for (int i = 0; i < len; i++) {
			String n = this.fromHTML(stf.readStr(new String[]{
					"Diagramm",
					"Tabellen",
					"Tabelle" + i,
					"Name"
			}, null));
			TabellenModel tm = d.getTabelle(n);
			int lenj =
					(int) stf.readLong(new String[]{
							"Diagramm",
							"Tabellen",
							"Tabelle" + i,
							"Spalten",
							"Anzahl"
					}, 0);
			for (int j = 0; j < lenj; j++) {
				n = this
						.fromHTML(
								stf
										.readStr(
												new String[]{
														"Diagramm",
														"Tabellen",
														"Tabelle" + i,
														"Spalten",
														"Spalte" + j,
														"Name"
												},
												null));
				TabellenspaltenModel tsm = tm.getTabellenspalte(n);
				boolean fk = new Boolean(
						stf
								.readStr(
										new String[]{
												"Diagramm",
												"Tabellen",
												"Tabelle" + i,
												"Spalten",
												"Spalte" + j,
												"ForeignKey"
										},
										"false")).booleanValue();
				if (fk) {
					new STFRelationReader().read(stf, d, tsm, i, j, (ViewModel) mvm);
				}
			}
			tm.clearEqualsMembers();
			lenj = (int) stf
					.readLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"Equalsmembers",
									"Anzahl"
							},
							0);
			for (int j = 0; j < lenj; j++) {
				String spaltenname = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"Equalsmembers",
										"Member" + j,
										"Spalte"
								},
								"");
				ColumnModel c = tm.getColumnByName(spaltenname);
				tm.addEqualsMember(c);
			}
			((TableModel) tm).clearCompareToMembers();
			lenj = (int) stf
					.readLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"CompareMembers",
									"Anzahl"
							},
							0);
			for (int j = 0; j < lenj; j++) {
				String columnName = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"CompareMembers",
										"Member" + j,
										"Spalte"
								},
								"");
				ColumnModel c = tm.getColumnByName(columnName);
				((TableModel) tm).addCompareMember(c);
			}
			tm.clearHashCodeMembers();
			lenj = (int) stf
					.readLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"HashCodeMembers",
									"Anzahl"
							},
							0);
			for (int j = 0; j < lenj; j++) {
				String spaltenname = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"HashCodeMembers",
										"Member" + j,
										"Spalte"
								},
								"");
				ColumnModel c0 = tm.getColumnByName(spaltenname);
				tm.addHashCodeMember(c0);
			}
			tm.clearToStringMembers();
			lenj = (int) stf
					.readLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"ToStringMembers",
									"Anzahl"
							},
							0);
			for (int j = 0; j < lenj; j++) {
				String spaltenname = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"ToStringMembers",
										"Member" + j,
										"Spalte"
								},
								"");
				String prefix = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"ToStringMembers",
										"Member" + j,
										"Prefix"
								},
								"");
				String suffix = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"ToStringMembers",
										"Member" + j,
										"Suffix"
								},
								"");
				TabellenspaltenModel tsm0 = tm.getTabellenspalte(spaltenname);
				ToStringContainer tsc = new ToStringContainer(tsm0, tm);
				tsc.setPrefix(prefix);
				tsc.setSuffix(suffix);
				tm.addToStringMember(tsc);
			}
			tm.clearComboStringMembers();
			lenj = (int) stf
					.readLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"ToComboStringMembers",
									"Anzahl"
							},
							0);
			for (int j = 0; j < lenj; j++) {
				String spaltenname = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"ToComboStringMembers",
										"Member" + j,
										"Spalte"
								},
								"");
				String prefix = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"ToComboStringMembers",
										"Member" + j,
										"Prefix"
								},
								"");
				String suffix = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"ToComboStringMembers",
										"Member" + j,
										"Suffix"
								},
								"");
				TabellenspaltenModel tsm0 = tm.getTabellenspalte(spaltenname);
				ToStringContainer tsc = new ToStringContainer(tsm0, tm);
				tsc.setPrefix(prefix);
				tsc.setSuffix(suffix);
				tm.addComboStringMember(tsc);
			}
			tm.getAuswahlMembers().removeAllElements();
			lenj = (int) stf
					.readLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"AuswahlMembers",
									"Anzahl"
							},
							0);
			for (int j = 0; j < lenj; j++) {
				String spaltenname = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"AuswahlMembers",
										"Member" + j,
										"Spalte"
								},
								"");
				String tabellenname = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"AuswahlMembers",
										"Member" + j,
										"Tabelle"
								},
								tm.getName());
				String attribute = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"AuswahlMembers",
										"Member" + j,
										"Attribute"
								},
								"OPTIONAL");
				String printExpression = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"AuswahlMembers",
										"Member" + j,
										"PrintExpression"
								},
								"");
				TableModel table = ((DataModel) d).getTableByName(tabellenname);
				ColumnModel column = table.getColumnByName(spaltenname);
				SelectionMemberModel smm =
						new SelectionMember(column, SelectionAttribute.valueOf(attribute), printExpression);
				tm.getAuswahlMembers().addElement(smm);
			}
			tm.clearSelectionViewOrderMembers();
			lenj = (int) stf
					.readLong(
							new String[]{
									"Diagramm",
									"Tabellen",
									"Tabelle" + i,
									"Codegenerator",
									"OrderMembers",
									"Anzahl"
							},
							0);
			for (int j = 0; j < lenj; j++) {
				String spaltenname = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"OrderMembers",
										"Member" + j,
										"Spalte"
								},
								"");
				String tabellenname = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"OrderMembers",
										"Member" + j,
										"Tabelle"
								},
								tm.getName());
				String richtung = stf
						.readStr(
								new String[]{
										"Diagramm",
										"Tabellen",
										"Tabelle" + i,
										"Codegenerator",
										"OrderMembers",
										"Member" + j,
										"Richtung"
								},
								OrderClauseDirection.ASC.toString());
				TabellenModel tm0 = d.getTabelle(tabellenname);
				TabellenspaltenModel tsm0 = tm0.getTabellenspalte(spaltenname);
				if (tsm0 != null) {
					OrderMemberModel omm = Archimedes.Factory.createOrderMember(tsm0);
					tm.addSelectionViewOrderMember(omm);
					omm.setOrderDirection(OrderClauseDirection.valueOf(richtung));
				}
			}
			tm.clearNReferences();
			new STFNReferenceReader().read(stf, tm, i);
			new STFOptionReader().read(stf, tm, i);
		}
		/*
		 * ViewModel vm = null; for (int i = 0, len = this.getViews().size(); i < len; i++) { int lenj = (int)
		 * stf.readLong(new String[] {"Diagramm", "Views", "View" + i, "Tabellenanzahl"}, 0); for (int j = 0; j < lenj;
		 * j++) { String tabellenname = stf.readStr(new String[] {"Diagramm", "Views", "View" + i, "Tabelle" + j}, "");
		 * TabellenModel tm = d.getTabelle(tabellenname); if (tm != null) { vm.getTabellen().add(tm); } } }
		 */
		new DefaultComplexIndicesFromSTFReader().read(stf, d.getComplexIndicesReference(), d);
		return d;
	}

	/**
	 * @changed OLI 19.02.2016 - Added.
	 */
	@Override
	public GUIViewModel getMainView() {
		for (GUIViewModel vm : this.getViews()) {
			if (vm instanceof MainViewModel) {
				return vm;
			}
		}
		return null;
	}

	@Override
	public SortedVector getMetadaten() {
		Vector tabellen = this.getTabellen();
		SortedVector sv = new SortedVector();
		for (int i = 0, len = tabellen.size(); i < len; i++) {
			TabellenModel tm = (TabellenModel) tabellen.elementAt(i);
			TableMetaData tmd = new TableMetaData(tm.getName());
			for (int j = 0, lenj = tm.getTabellenspaltenCount(); j < lenj; j++) {
				TabellenspaltenModel tsm = tm.getTabellenspalteAt(j);
				ColumnMetaData cmd = new ColumnMetaData(
						tsm.getName(),
						tsm.getDomain().getName(),
						tsm.getDomain().getType(),
						tsm.isPrimarykey(),
						tsm.isNotNull());
				tmd.addColumn(cmd);
			}
			sv.addElement(tmd);
		}
		return sv;
	}

	@Override
	@Deprecated
	public Vector getReferencers(TabellenModel t) {
		Vector erg = new Vector();
		Vector v = this.getTabellen();
		for (int i = 0, len = v.size(); i < len; i++) {
			TabellenModel tm0 = (TabellenModel) v.elementAt(i);
			for (int j = 0, lenj = tm0.getTabellenspaltenCount(); j < lenj; j++) {
				TabellenspaltenModel tsm = tm0.getTabellenspalteAt(j);
				if (tsm.getReferencedTable() != null) {
					if (tsm.getReferencedTable() == t) {
						erg.addElement(tsm);
					}
				}
			}
		}
		return erg;
	}

	private String getBeschreibung(TabellenspaltenModel tsm) {
		return new DescriptionGetter(this.getDefaultComments()).getDescription(tsm);
	}

	@Override
	public void setAuthor(String author) {
		if (author == null) {
			author = "";
		}
		this.author = author;
	}

	@Override
	@Deprecated
	public PDate getDate() {
		return this.getVersionDate();
	}

	@Override
	@Deprecated
	public void setDate(PDate date) {
		this.setVersionDate(date);
	}

	@Override
	public void setVersion(String version) {
		if (version == null) {
			version = "";
		}
		this.version = version;
	}

	@Override
	public String getVersionComment() {
		return this.versionskommentar;
	}

	@Override
	public void setVersionComment(String comment) {
		if (comment == null) {
			comment = "";
		}
		this.versionskommentar = comment;
	}

	@Override
	public int getFontSizeTableContents() {
		return this.fontsizeTableContents;
	}

	@Override
	public void setFontSizeTableContents(int fontsize) {
		this.fontsizeTableContents = fontsize;
	}

	@Override
	public void setFontSizeDiagramHeadline(int fontsize) {
		this.fontsizeHeadline = fontsize;
	}

	@Override
	public void setFontSizeSubtitles(int fontsize) {
		this.fontsizeSubtitles = fontsize;
	}

	@Override
	public void removeStereotype(StereotypeModel st) {
		this.stereotypes.removeElement(st);
	}

	@Override
	public Vector getStereotypen() {
		return new Vector(this.stereotypes);
	}

	@Override
	public StereotypeModel getStereotype(String n) {
		for (int i = 0, len = this.stereotypes.size(); i < len; i++) {
			StereotypeModel stm = this.stereotypes.elementAt(i);
			if (stm.getName().equals(n)) {
				return stm;
			}
		}
		throw new NoSuchElementException("Stereotype mit Namen \"" + n + "\" existiert nicht!");
	}

	@Override
	public StereotypeModel createStereotype() {
		return Archimedes.Factory.createStereotype();
	}

	@Override
	public Vector getStereotypeReference() {
		return this.stereotypes;
	}

	@Override
	public void addDefaultComment(DefaultCommentModel t) {
		this.defaultComments.addElement(t);
	}

	@Override
	public void removeDefaultComment(DefaultCommentModel t) {
		this.defaultComments.removeElement(t);
	}

	@Override
	public Vector getDefaultComments() {
		return new Vector(this.defaultComments);
	}

	@Override
	public DefaultCommentModel getDefaultComment(String n) {
		for (int i = 0, len = this.defaultComments.size(); i < len; i++) {
			DefaultCommentModel dcm = (DefaultCommentModel) this.defaultComments.elementAt(i);
			if (dcm.getPattern().equals(n)) {
				return dcm;
			}
		}
		throw new NoSuchElementException("DefaultComment mit Namen \"" + n + "\" existiert " + "nicht!");
	}

	@Override
	public DefaultCommentModel createDefaultComment() {
		return Archimedes.Factory.createDefaultComment();
	}

	@Override
	public Vector getDefaultCommentsReference() {
		return this.defaultComments;
	}

	@Override
	@Deprecated
	public boolean isAufgehobeneAusblenden() {
		return this.hideDeprecatedTables;
	}

	@Override
	@Deprecated
	public void setAufgehobeneAusblenden(boolean aufgehobeneAusblenden) {
		this.hideDeprecatedTables = aufgehobeneAusblenden;
	}

	/**
	 * @changed OLI 01.11.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public String getHistory() {
		return this.history;
	}

	/**
	 * @changed OLI 01.11.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void setHistory(String newHistory) {
		this.history = newHistory;
	}

	@Override
	public JDBCDataSourceRecord getImportDataSourceRecord() {
		return this.importDSR;
	}

	@Override
	public void setImportDataSourceRecord(JDBCDataSourceRecord dsr) {
		if (!(dsr instanceof ArchimedesImportJDBCDataSourceRecord)) {
			throw new IllegalArgumentException("ArchimedesImportJDBCDataSourceRecord required " + "for Diagramm!");
		}
		this.importDSR = (ArchimedesImportJDBCDataSourceRecord) dsr;
	}

	@Override
	public String getCodePfad() {
		return this.codepfad;
	}

	@Override
	public void setCodePfad(String pfad) {
		if (pfad == null) {
			pfad = "." + File.separator;
		}
		this.codepfad = pfad;
	}

	@Override
	public String getApplicationName() {
		return this.applicationname;
	}

	@Override
	public String getDBVersionTablename() {
		return this.dbVersionTableName;
	}

	@Deprecated
	public void setDBVersionTablename(String name) {
		this.setDBVersionTableName(name);
	}

	@Override
	public String getDBVersionDBVersionColumn() {
		return this.dbVersionVersionColumnName;
	}

	@Deprecated
	public void setDBVersionDBVersionColumn(String name) {
		this.setDBVersionVersionColumnName(name);
	}

	@Override
	public String getDBVersionDescriptionColumn() {
		return this.dbVersionDescriptionColumnName;
	}

	@Deprecated
	public void setDBVersionDescriptionColumn(String name) {
		this.setDBVersionDescriptionColumnName(name);
	}

	@Override
	public boolean isShowReferencedColumns() {
		return this.showReferencedColumns;
	}

	@Override
	public void setShowReferencedColumns(boolean b) {
		this.showReferencedColumns = b;
	}

	/**
	 * @changed OLI 01.10.2015 - Added.
	 */
	@Override
	public PredeterminedOptionProvider getPredeterminedOptionProvider() {
		return this.pop;
	}

	/**
	 * @changed OLI 01.10.2015 - Added.
	 */
	@Override
	public void setPredeterminedOptionProvider(PredeterminedOptionProvider pop) {
		this.pop = pop;
	}

	/*
	 * public java.util.List<ViewModel> getViews() { return this.views; }
	 *
	 * public java.util.List<ViewModel> getViews(TabellenModel tm) { java.util.List l = new Vector(); for (int i = 0,
	 * len = this.getViews().size(); i < len; i++) { ViewModel vm = this.getViews().get(i); if
	 * (vm.getTabellen().contains(tm)) { l.add(vm); } } return l; }
	 *
	 * public ViewModel getView(String name) { ViewModel vm = null; for (int i = 0, len = this.getViews().size(); i <
	 * len; i++) { vm = this.getViews().get(i); if (vm.getName().equals(name)) { break; } } return vm; }
	 */

	@Override
	public boolean containsTablenames(String[] tablenamesarray) {
		Vector<String> tablenames = new Vector<String>();
		Vector tables = this.getTabellen();
		int size = tables.size();
		for (int i = 0; i < size; i++) {
			tablenames.add(((TabellenModel) tables.elementAt(i)).getName());
		}
		return (tablenames.containsAll(Arrays.asList(tablenamesarray)));
	}

	@Override
	public boolean containsTablename(String tablename) {
		return this.containsTablenames(new String[]{tablename});
	}

	@Override
	@Deprecated
	public boolean markWriteablemembers() {
		return this.markUpRequiredFieldNames;
	}

	@Deprecated
	public void markWriteablemembers(boolean b) {
		this.markUpRequiredFieldNames = b;
	}

	@Override
	public String getBasePackageName() {
		return this.basepackagename;
	}

	@Override
	public void setBasePackageName(String bpn) {
		if (bpn == null) {
			bpn = "";
		}
		this.basepackagename = bpn;
	}

	@Override
	public String getCodeFactoryClassName() {
		return this.codefactoryclassname;
	}

	@Override
	public void setCodeFactoryClassName(String cfcn) {
		this.codefactoryclassname = cfcn;
	}

	@Override
	public void addDiagrammModelListener(DiagrammModelListener dml) {
		this.diagrammmodellistener.addElement(dml);
	}

	@Override
	public void removeDiagrammModelListener(DiagrammModelListener dml) {
		this.diagrammmodellistener.removeElement(dml);
	}

	@Override
	public void fireDiagrammAltered() {
		for (int i = 0, len = this.diagrammmodellistener.size(); i < len; i++) {
			try {
				((DiagrammModelListener) this.diagrammmodellistener.elementAt(i)).diagrammModelChanged();
			} catch (Exception e) {
				LOG.error("an error occured while notifing diagramm model listeners: " + e.getMessage());
			}
		}
		super.fireGUIDiagramModelListenerStateChanged();
	}

	@Override
	public String getUdschebtiBaseClassName() {
		return this.ubcn;
	}

	@Override
	public void setUdschebtiBaseClassName(String ubcn) {
		this.ubcn = ubcn;
	}

	@Override
	public boolean isPaintTechnicalFieldsInGray() {
		return this.paintTechnicalFieldsInGray;
	}

	/**
	 * @changed OLI 11.06.2015 - Added.
	 */
	@Override
	public boolean isPaintTransientFieldsInGray() {
		return this.paintTransientFieldsInGray;
	}

	@Override
	public void setPaintTechnicalFieldsInGray(boolean b) {
		this.paintTechnicalFieldsInGray = b;
	}

	@Override
	public void setPaintTransientFieldsInGray(boolean b) {
		this.paintTransientFieldsInGray = b;
	}

	@Override
	public String getAfterWriteScript() {
		return this.afterwritescript;
	}

	@Override
	public void setAfterWriteScript(String script) {
		this.afterwritescript = script;
	}

	@Override
	public Map<String, java.util.List<String>> getCodegeneratorOptionsListTag(String tagName, String delimiter) {
		Hashtable<String, java.util.List<String>> m = new Hashtable<String, java.util.List<String>>();
		int i = 0;
		int leni = 0;
		java.util.List<String> l = null;
		TabellenModel tm = null;
		Vector vtm = this.getTabellen();
		for (i = 0, leni = vtm.size(); i < leni; i++) {
			tm = (TabellenModel) vtm.get(i);
			l = tm.getCodeGeneratorOptionsListTag(tagName, delimiter);
			if (l.size() > 0) {
				m.put(tm.getName(), l);
			}
			/*
			 * s = tm.getCodeGeneratorOptions(); start = s.indexOf(tagStart); end = s.indexOf(tagEnd); if ((start >= 0)
			 * && (end >= 0)) { s = s.substring(start + tagStart.length(), end); // m.put(tm.getName(),
			 * StrUtil.SplitToList(s, delimiter)); m.put(tm.getName(), tm.getCodegeneratorOptionsListTag(tagName,
			 * delimiter)); }
			 */
		}
		return m;
	}

	/**
	 * @changed OLI 21.03.2009 - Hinzugef&uuml;gt.
	 * <p>
	 */
	@Override
	public boolean generateCode(CodeFactory dcf, String path, GUIBundle guiBundle) {
		boolean ok = false;
		CodeFactory cf = null;
		int leni = 0;
		java.util.List<String> lcfn = StrUtil.SplitToList(this.getCodeFactoryClassName(), ",");
		String cfn = null;
		try {
			leni = lcfn.size();
			if (leni > 0) {
				ok = true;
				for (int i = 0; i < leni; i++) {
					cfn = lcfn.get(i).trim();
					cf = Archimedes.Factory.createCodeFactory(cfn);
					if ((cf instanceof DefaultCodeFactory) && (dcf != null)) {
						cf = dcf;
					}
					cf.setGUIBundle(guiBundle);
					cf.setDataModel(this);
					ok = ok && cf.generate(path);
				}
			} else {
				if (dcf != null) {
					dcf.setDataModel(this);
					ok = dcf.generate(path);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return ok;
	}

	@Override
	public void addSQLScriptListener(SQLScriptListener l) {
		this.listAdditionalSQLScriptListener.add(l);
	}

	@Override
	public String getAdditionalSQLScriptListener() {
		return this.additionalSQLScriptListener;
	}

	@Override
	public void removeSQLScriptListener(SQLScriptListener l) {
		this.listAdditionalSQLScriptListener.remove(l);
	}

	@Override
	public void setAdditionalSQLScriptListener(String clsn) {
		this.additionalSQLScriptListener = clsn;
	}

	/**
	 * @changed OLI 22.03.2009 - Hinzugef&uuml;gt.
	 */
	@Override
	public void fireDataSchemeChanged(SQLScriptEvent e) {
		for (int i = 0, leni = this.listAdditionalSQLScriptListener.size(); i < leni; i++) {
			this.listAdditionalSQLScriptListener.get(i).dataSchemeChanged(e);
		}
	}

	/* Implementierungen zum Interface ModelMetaData. */

	// public String getBasePackageName(); // Implementierung: siehe oben.

	@Override
	public ClassMetaData getClass(int i) throws IndexOutOfBoundsException {
		return this.getClasses().get(i);
	}

	@Override
	public java.util.List<ClassMetaData> getClasses() {
		return this.getTabellen();
	}

	// public String getAuthor(); // Implementierung: siehe oben.

	/**
	 * Die Methode liefert im Prinzip den Applikationsnamen ( <TT>getApplicationName()</TT> zur&uuml;ck. Allerdings
	 * werden Leerzeichen, Punkte, Minus-Zeichen und Ausrufezeichen entfernt.
	 *
	 * @changed OLI 30.09.2009 - Hinzugef&uuml;gt.
	 */
	@Override
	public String getProjectToken() {
		return this.getApplicationName().replace(" ", "").replace(".", "").replace("-", "").replace("!", "");
	}

	@Override
	public String getVendor() {
		return this.getAuthor();
	}

	/**
	 * @changed OLI 21.12.2015 - Added.
	 */
	@Override
	public IndexMetaData getComplexIndex(String name) {
		for (IndexMetaData i : this.getComplexIndices()) {
			if (i.getName().equals(name)) {
				return i;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt. /
	 * @Override public List<IndexMetaData> getComplexIndices() { return this.complexIndices; }
	 * <p>
	 * <p>
	 * /**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public ClassMetaData getClass(String name) throws IllegalArgumentException {
		if ((name == null) || name.isEmpty()) {
			throw new IllegalArgumentException("name cannot be null or empty.");
		}
		for (ClassMetaData cmd : this.tables) {
			if (cmd.getName().equals(name)) {
				return cmd;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 11.12.2013 - Added.
	 */
	@Override
	public DomainModel getDomainByName(String name) {
		for (DomainModel d : this.getAllDomains()) {
			if (d.getName().equals(name)) {
				return d;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Override
	public String getSchemaName() {
		return this.schemaName;
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Override
	public void setSchemaName(String newSchemaName) {
		ensure(newSchemaName != null, "schema name cannot be null.");
		this.schemaName = newSchemaName;
	}

	/*
	 * The following methods are approved by the current model interface (TableModel).
	 */

	private String additionalPostChangingSQLCode = "";
	private String additionalPostReducingSQLCode = "";
	private String additionalPreChangingSQLCode = "";
	private String additionalPreExtendingSQLCode = "";
	private String comment = "";
	private String dbVersionDescriptionColumnName = "";
	private String dbVersionVersionColumnName = "";
	private String dbVersionTableName = "";
	private SortedVector<DomainModel> domains = new SortedVector<DomainModel>();
	/*
	 * If this flag is set the required fields of the table in das diagram will be marked up. The characters which are
	 * set to mark the fields can be defined by the properties:
	 * "archimedes.scheme.Tabellenspalte.diagramm.writeable.prefix"
	 * "archimedes.scheme.Tabellenspalte.diagramm.writeable.suffix" The content of the first property will be displayed
	 * before the field name, the second thereafter.
	 */
	private boolean markUpRequiredFieldNames = false;
	private SortedVector<OptionModel> options = new SortedVector<OptionModel>();
	private SortedVector<SequenceModel> sequences = new SortedVector<SequenceModel>();
	private SortedVector<StereotypeModel> stereotypes = new SortedVector<StereotypeModel>();
	private Vector<TabellenModel> tables = new Vector<TabellenModel>();

	/**
	 * @changed OLI 26.04.2013 - Approved.
	 */
	@Override
	public void addDomain(DomainModel domain) {
		ensure(domain != null, "domain cannot be null.");
		this.domains.addElement(domain);
	}

	/**
	 * @changed OLI 17.12.2013 - Added.
	 */
	@Override
	public void addOption(OptionModel option) {
		this.options.addElement(option);
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public void addSequence(SequenceModel sequence) throws IllegalArgumentException {
		ensure(sequence != null, "sequence cannot be null.");
		if (!this.sequences.contains(sequence)) {
			this.sequences.addElement(sequence);
		}
	}

	/**
	 * @changed OLI 26.04.2013 - Approved.
	 */
	@Override
	public void addStereotype(StereotypeModel st) {
		this.stereotypes.addElement(st);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Override
	public void addTable(TableModel table) throws IllegalArgumentException {
		ensure(table != null, "table to add cannot be null.");
		if (!this.tables.contains(table)) {
			this.tables.add((TabellenModel) table);
			table.setDataModel(this);
		}
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public SequenceModel createSequence() {
		return Archimedes.Factory.createSequence("Sequenz", 100, 100);
	}

	/**
	 * @changed OLI 29.06.2020 - Added.
	 */
	@Override
	public String getAdditionalDiagramInfo() {
		return this.additionalDiagramInfo;
	}

	/**
	 * @changed OLI 13.12.2013 - Added.
	 */
	@Override
	public String getAdditionalSQLCodePostChangingCode() {
		return this.additionalPostChangingSQLCode;
	}

	/**
	 * @changed OLI 26.04.2013 - Added.
	 */
	@Override
	public String getAdditionalSQLCodePostReducingCode() {
		return this.additionalPostReducingSQLCode;
	}

	/**
	 * @changed OLI 13.12.2013 - Added.
	 */
	@Override
	public String getAdditionalSQLCodePreChangingCode() {
		return this.additionalPreChangingSQLCode;
	}

	/**
	 * @changed OLI 13.12.2013 - Added.
	 */
	@Override
	public String getAdditionalSQLCodePreExtendingCode() {
		return this.additionalPreExtendingSQLCode;
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Override
	public ColumnModel[] getAllColumns() {
		corentx.util.SortedVector<ColumnModel> columns = new corentx.util.SortedVector<ColumnModel>();
		for (int i = 0, len = this.tables.size(); i < len; i++) {
			TableModel tm = this.tables.elementAt(i);
			if (!tm.isDeprecated()) {
				for (ColumnModel c : tm.getColumns()) {
					columns.add(c);
				}
			}
		}
		return columns.toArray(new ColumnModel[0]);
	}

	/**
	 * @changed OLI 26.04.2013 - Added.
	 */
	@Override
	public DomainModel[] getAllDomains() {
		return this.domains.toArray(new DomainModel[0]);
	}

	/**
	 * @changed OLI 26.04.2013 - Added.
	 */
	public StereotypeModel[] getAllStereotypes() {
		return this.stereotypes.toArray(new StereotypeModel[0]);
	}

	/**
	 * @changed OLI 11.03.2016 - Added.
	 */
	@Override
	public String getComment() {
		return this.comment;
	}

	/**
	 * @changed OLI 26.12.2015 - Added.
	 */
	@Override
	public SequenceModel getSequenceByName(String name) {
		for (SequenceModel s : this.getSequences()) {
			if (s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public SequenceModel[] getSequences() {
		return this.sequences.toArray(new SequenceModel[0]);
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public Vector<SequenceModel> getSequencesByReference() {
		return this.sequences;
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Override
	public TableModel getTableByName(String name) {
		for (TableModel t : this.getTables()) {
			if (t.getName().equals(name)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Override
	public TableModel[] getTables() {
		List<TableModel> tables = new corentx.util.SortedVector<TableModel>();
		for (TableModel t : this.tables) {
			tables.add(t);
		}
		return tables.toArray(new TableModel[0]);
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public void removeSequence(SequenceModel sequence) {
		if (this.sequences.contains(sequence)) {
			this.sequences.remove(sequence);
		}
	}

	/**
	 * @changed OLI 26.04.2013 - Approved.
	 */
	@Override
	public void setApplicationName(String name) {
		if (name == null) {
			name = "";
		}
		this.applicationname = name;
	}

	/**
	 * @changed OLI 11.03.2016 - Added.
	 */
	@Override
	public void setComment(String comment) {
		this.comment = (comment == null
				? ""
				: comment);
	}

	/**
	 * @changed OLI 26.04.2013 - Added.
	 */
	@Override
	public void setDBVersionDescriptionColumnName(String name) {
		if (name == null) {
			name = "";
		}
		this.dbVersionDescriptionColumnName = name;
	}

	/**
	 * @changed OLI 26.04.2013 - Added.
	 */
	@Override
	public void setDBVersionVersionColumnName(String name) {
		if (name == null) {
			name = "";
		}
		this.dbVersionVersionColumnName = name;
	}

	/**
	 * @changed OLI 26.04.2013 - Added.
	 */
	@Override
	public void setDBVersionTableName(String name) {
		if (name == null) {
			name = "";
		}
		this.dbVersionTableName = name;
	}

	/**
	 * @changed OLI 26.04.2013 - Added.
	 */
	@Override
	public void setMarkUpRequiredFieldNames(boolean b) {
		this.markUpRequiredFieldNames = b;
	}

	/**
	 * @changed OLI 06.05.2013 - Added.
	 */
	@Override
	public ColumnModel[] getReferencers(TableModel table) {
		List<ColumnModel> result = new Vector<ColumnModel>();
		for (TableModel t : this.getTables()) {
			for (ColumnModel column : t.getColumns()) {
				if (column.getRelation() != null) {
					if (column.getRelation().getReferenced().getTable() == table) {
						result.add(column);
					}
				}
			}
		}
		return result.toArray(new ColumnModel[0]);
	}

	//
	// Approved methods and fields.
	//

	private String author = "";
	private List<DataModelListener> dataModelListeners = new Vector<DataModelListener>();
	private corentx.dates.PDate dateOfCreation = new corentx.dates.PDate();
	private int fontsizeHeadline = 24;
	private int fontsizeSubtitles = 12;
	private boolean hideDeprecatedTables = false;
	private String name = "DIAGRAMMNAME";
	private Color relationColorExternalTables = Color.lightGray;
	private Color relationColorRegular = Color.black;
	private String version = "1";

	/**
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public void addDataModelListener(DataModelListener l) {
		this.dataModelListeners.add(l);
		LOG.debug("data model listener added: " + l);
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public void addGUIObject(GUIObjectModel guiObject) {
		this.addTabelle((TabellenModel) guiObject);
	}

	/**
	 * Call this method to fire a table changed event.
	 *
	 * @param e The table which has been changed.
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public void fireDataModelEvent(DataModelEvent e) {
		LOG.debug("firing data model event: " + e);
		LOG.debug("number of listeners to process: " + this.dataModelListeners.size());
		for (DataModelListener l : this.dataModelListeners) {
			try {
				if (e instanceof TableChangedEvent) {
					l.tableChanged((TableChangedEvent) e);
					LOG.debug("table change fired: " + e);
				} else {
					LOG.warn("unprocessed event: " + e);
				}
			} catch (Exception ex) {
				LOG.error("an error occured while firing table changed event: " + ex.getMessage());
			}
		}
	}

	/**
	 * @changed OLI 14.05.2013 - Approved.
	 */
	@Override
	public String getAuthor() {
		return this.author;
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public corentx.dates.PDate getDateOfCreation() {
		return this.dateOfCreation;
	}

	/**
	 * @changed OLI 14.05.2013 - Approved.
	 */
	@Override
	public int getFontSizeDiagramHeadline() {
		return this.fontsizeHeadline;
	}

	/**
	 * @changed OLI 14.05.2013 - Approved.
	 */
	@Override
	public int getFontSizeSubtitles() {
		return this.fontsizeSubtitles;
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public TabellenModel getGUIObject(String name) {
		return this.getTabelle(name);
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public TabellenModel[] getGUIObjects() {
		return this.tables.toArray(new TabellenModel[0]);
	}

	/**
	 * @changed OLI 14.05.2013 - Approved.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @changed OLI 17.12.2013 - Added.
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
	 * @changed OLI 17.12.2013 - Added.
	 */
	@Override
	public int getOptionCount() {
		return this.options.size();
	}

	/**
	 * @changed OLI 17.12.2013 - Added.
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
	 * @changed OLI 12.01.2015 - Added.
	 */
	@Override
	public Color getRelationColorToExternalTables() {
		return this.relationColorExternalTables;
	}

	/**
	 * @changed OLI 12.01.2015 - Added.
	 */
	@Override
	public Color getRelationColorRegular() {
		return this.relationColorRegular;
	}

	/**
	 * @changed OLI 14.05.2013 - Approved.
	 */
	@Override
	public String getVersion() {
		return this.version;
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public boolean isDeprecatedTablesHidden() {
		return this.hideDeprecatedTables;
	}

	/**
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public void removeDataModelListener(DataModelListener l) {
		this.dataModelListeners.remove(l);
		LOG.debug("data model listener removed: " + l);
	}

	/**
	 * @changed OLI 30.05.2013 - Added.
	 */
	@Override
	public void removeGUIObject(GUIObjectModel guiObject) {
		this.tables.removeElement(guiObject);
	}

	/**
	 * @changed OLI 17.12.2013 - Added.
	 */
	@Override
	public void removeOption(OptionModel option) {
		this.options.remove(option);
	}

	/**
	 * @changed OLI 29.06.2020 - Added.
	 */
	@Override
	public void setAdditionalDiagramInfo(String additionalDiagramInfo) {
		this.additionalDiagramInfo = additionalDiagramInfo;
	}

	/**
	 * @changed OLI 11.06.2013 - Approved.
	 */
	@Override
	public void setName(String name) {
		if (name == null) {
			name = "<null>";
		}
		this.name = name;
	}

	/**
	 * @changed OLI 13.12.2013 - Added.
	 */
	@Override
	public void setAdditionalSQLCodePostChangingCode(String sql) throws IllegalArgumentException {
		this.additionalPostChangingSQLCode = sql;
	}

	/**
	 * @changed OLI 26.04.2013 - Added.
	 */
	@Override
	public void setAdditionalSQLCodePostReducingCode(String sql) throws IllegalArgumentException {
		ensure(sql != null, "additional SQL code cannot be null.");
		this.additionalPostReducingSQLCode = sql;
	}

	/**
	 * @changed OLI 13.12.2013 - Added.
	 */
	@Override
	public void setAdditionalSQLCodePreChangingCode(String sql) throws IllegalArgumentException {
		this.additionalPreChangingSQLCode = sql;
	}

	/**
	 * @changed OLI 13.12.2013 - Added.
	 */
	@Override
	public void setAdditionalSQLCodePreExtendingCode(String sql) throws IllegalArgumentException {
		this.additionalPreExtendingSQLCode = sql;
	}

	/**
	 * @changed OLI 11.03.2016 - Added.
	 */
	@Override
	public void setDeprecatedTablesHidden(boolean b) {
		this.hideDeprecatedTables = b;
	}

	/**
	 * @changed OLI 12.01.2015 - Added.
	 */
	@Override
	public void setRelationColorExternalTables(Color color) {
		this.relationColorExternalTables = color;
	}

	/**
	 * @changed OLI 12.01.2015 - Added.
	 */
	@Override
	public void setRelationColorRegular(Color color) {
		this.relationColorRegular = color;
	}

	/**
	 * @changed OLI 28.04.2014 - Added.
	 */
	@Override
	public PDate getVersionDate() {
		if (this.getDateOfCreation() != null) {
			return new PDate((int) this.getDateOfCreation().toLong());
		}
		return null;
	}

	/**
	 * @changed OLI 28.04.2014 - Added.
	 */
	@Override
	public void setVersionDate(PDate date) {
		if (date == null) {
			date = new PDate();
		}
		this.dateOfCreation = new corentx.dates.PDate(date.toInt());
	}

	/**
	 * @changed OLI 15.01.2015 - Added.
	 */
	@Override
	public void addDatabaseConnection(DatabaseConnection dc) {
		if (!this.connections.contains(dc)) {
			this.connections.add(dc);
		}
	}

	/**
	 * @changed OLI 19.02.2015 - Added.
	 */
	@Override
	public DatabaseConnection getDatabaseConnection(String name) {
		for (DatabaseConnection dc : this.connections) {
			if (dc.getName().equals(name)) {
				return dc;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 15.01.2015 - Added.
	 */
	@Override
	public DatabaseConnection[] getDatabaseConnections() {
		return this.connections.toArray(new DatabaseConnection[0]);
	}

	/**
	 * @changed OLI 15.01.2015 - Added.
	 */
	@Override
	public boolean removeDatabaseConnection(String name) {
		for (int i = this.connections.size() - 1; i >= 0; i--) {
			if (this.connections.get(i).getName().equals(name)) {
				this.connections.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * @changed OLI 11.12.2015 - Added.
	 */
	public IndexMetaData[] getAllComplexIndices() {
		return this.complexIndices.toArray(new IndexMetaData[0]);
	}

	/**
	 * @changed OLI 26.05.2016 - Added.
	 */
	@Override
	public boolean isOptionSet(String optionName) {
		return this.getOptionByName(optionName) != null;
	}

	/**
	 * @changed OLI 09.06.2016 - Added.
	 */
	@Override
	public String getBasicCodePath() {
		return this.codepfad;
	}

	/**
	 * @changed OLI 09.06.2016 - Added.
	 */
	@Override
	public void setBasicCodePath(String arg0) {
		this.codepfad = arg0;
	}

	/**
	 * @changed OLI 09.06.2016 - Added.
	 */
	@Override
	public ViewModel getViewByName(String name) {
		for (ViewModel v : this.getViews().toArray(new ViewModel[0])) {
			if (v.getName().equals(name)) {
				return v;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public void addComplexIndex(IndexMetaData imd) throws IllegalArgumentException {
		if (!this.complexIndices.contains(imd)) {
			this.complexIndices.add(imd);
		}
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public IndexMetaData getComplexIndexAt(int index) {
		return this.complexIndices.get(index);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public IndexMetaData getComplexIndexByName(String name) {
		for (IndexMetaData imd : this.getComplexIndices()) {
			if (imd.getName().equals(name)) {
				return imd;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public int getComplexIndexCount() {
		return this.complexIndices.size();
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public IndexMetaData[] getComplexIndices() {
		return this.complexIndices.toArray(new IndexMetaData[0]);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public boolean isComplexIndexSet(String name) {
		return this.getComplexIndexByName(name) != null;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public void removeComplexIndex(IndexMetaData imd) {
		this.complexIndices.remove(imd);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public boolean isMarkUpRequiredFieldNames() {
		return this.markUpRequiredFieldNames;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public void setRelationColorToExternalTables(Color c) {
		this.relationColorExternalTables = c;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public String getDBVersionDescriptionColumnName() {
		return this.getDBVersionDescriptionColumn();
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public String getDBVersionTableName() {
		return this.getDBVersionTablename();
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public String getDBVersionVersionColumnName() {
		return this.getDBVersionDBVersionColumn();
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public StereotypeModel getStereotypeAt(int index) {
		return this.stereotypes.get(index);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public StereotypeModel getStereotypeByName(String name) {
		for (StereotypeModel stm : this.getStereotypes()) {
			if (stm.getName().equals(name)) {
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
		return this.stereotypes.size();
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public StereotypeModel[] getStereotypes() {
		return this.stereotypes.toArray(new StereotypeModel[0]);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public StereotypeModel[] getStereotypesByName(String name) {
		List<StereotypeModel> l = new LinkedList<StereotypeModel>();
		for (StereotypeModel stm : this.getStereotypes()) {
			if (stm.getName().equals(name)) {
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

	public Vector<IndexMetaData> getComplexIndicesReference() {
		return this.complexIndices;
	}

	/**
	 * @changed OLI 21.06.2016 - Added.
	 */
	@Override
	public void removeTable(TableModel t) {
		this.removeTabelle((TabellenModel) t);
	}

	/**
	 * @changed OLI 05.07.2016 - Added.
	 */
	@Override
	public String getOwner() {
		return this.owner;
	}

	/**
	 * @changed OLI 05.07.2016 - Added.
	 */
	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @changed OLI 30.06.2020 - Added.
	 */
	@Override
	public Map<String, String> getProperties() {
		Map<String, String> m = new HashMap<>();
		for (OptionModel o : this.getOptions()) {
			m.put(o.getName(), o.getParameter());
		}
		return m;
	}

	@Override
	public DomainShowMode getDomainShowMode() {
		return this.domainShowMode;
	}

	@Override
	public void setDomainShowMode(DomainShowMode newMode) {
		this.domainShowMode = newMode;
	}

	@Override
	public String getModelCheckerScript() {
		return modelCheckerScript;
	}

	@Override
	public void setModelCheckerScript(String script) {
		modelCheckerScript = script;
	}

} // 3875