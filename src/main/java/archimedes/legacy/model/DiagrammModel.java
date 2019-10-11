/*
 * DiagrammModel.java
 *
 * 20.03.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

import gengen.metadata.ModelMetaData;

import java.awt.Color;
import java.util.Map;
import java.util.Vector;

import archimedes.legacy.script.sql.SQLScriptEvent;
import archimedes.legacy.script.sql.SQLScriptListener;
import archimedes.model.ChangeObserver;
import archimedes.model.CodeFactory;
import archimedes.model.CommentOwner;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.HistoryOwner;
import archimedes.model.IndexMetaData;
import archimedes.model.PredeterminedOptionProvider;
import archimedes.model.StereotypeModel;
import archimedes.model.gui.GUIDiagramModel;
import baccara.gui.GUIBundle;
import corent.base.Attributed;
import corent.base.SortedVector;
import corent.dates.PDate;
import corent.db.JDBCDataSourceRecord;
import corent.djinn.TabbedEditable;
import corent.files.StructuredTextFile;

/**
 * Diese Interface definiert das Verhalten von Objekten, die von einer
 * Archimedes-Diagramm-Komponente ausgegeben werden k&ouml;nnen sollen.
 * 
 * @author ollie
 * 
 * @changed OLI 11.02.2008 - Erweiterung um die Methoden
 *          <TT>getUdschebtiBaseClassName()</TT> und
 *          <TT>setUdschebtiBaseClassName(String)</TT> zur Definition
 *          alternativer Applikationsgrundklassen.
 * @changed OLI 09.08.2008 - Erweiterung um die Methoden
 *          <TT>isPaintTechnicalFieldsInGray()</TT> und
 *          <TT>setPaintTechnicalFieldsInGray(boolean)</TT>. Hierdurch
 *          k&ouml;nnen als technisch gekennzeichnete Felder in den Diagrammen
 *          ausgegraut dargestellt werden.
 * @changed OLI 16.08.2008 - Erweiterung der Methode <TT>toSTF()</TT> um den
 *          Parameter zur Festlegung eines Speichermodus (Stichwort:
 *          Dateiverkleinerung).
 * @changed OLI 04.09.2008 - Erweiterung um die Methoden zum Setzen und Lesen
 *          des Speicherscripts. Dieses Script soll durch den
 *          ArchimedesCommandProcessor verarbeitet werden, nachdem angezeigte
 *          Datenmodell gespeichert worden ist. Hierdurch sollen beispielsweise
 *          zus&auml;tzliche Modelldateien mit automatisiert
 *          durchzuf&uuml;hrenden &Auml;nderungen erzeugt und gespeichert werden
 *          k&ouml;nnen.
 * @changed OLI 15.12.2008 - Erweiterung der Signatur der Methode
 *          <TT>alterDiagram</TT> um eine Liste Referenzinformationen.
 * @changed OLI 05.03.2009 - Erweiterung um die Signatur der Methode
 *          <TT>getCodegeneratorOptionsListTag(String, String)</TT>.
 * @changed OLI 21.03.2009 - Erweiterung um die Spezifikation der Methode
 *          <TT>generateCode(CodeFactory, String)</TT>. Anpassung der
 *          Spezifikation der Methoden <TT>getCodeFactoryClassName()</TT> und
 *          <TT>setCodeFactoryClassName(String)</TT>.
 * @changed OLI 22.03.2009 - Erweiterung um die Spezifikation der Methoden
 *          <TT>getAdditionalSQLScriptListener()</TT>,
 *          <TT>setAdditionalSQLScriptListener(String)</TT>,
 *          <TT>addSQLScriptListener(SQLScriptListener)</TT> und
 *          <TT>removeSQLScriptListener(SQLScriptListener)</TT>.
 * @changed OLI 01.06.2009 - Erweiterung um die Methode
 *          <TT>isDifferentToScheme(List,
 *         List)</TT>. Dabei: Sortierung der Methoden-Spezifikationen und
 *          Formatanpassungen.
 * @changed OLI 09.09.2009 - Erweiterung um die Implementierung des Interfaces
 *          <TT>gengen.metadata.ModelMetaData</TT>.
 * @changed OLI 01.11.2011 - Erweiterung um den <TT>HistoryOwner</TT>.
 * @changed OLI 23.04.2013 - Erweiterung um die Sequenzen.
 */

public interface DiagrammModel extends Attributed, ChangeObserver, Comparable, CommentOwner, DataModel,
		GUIDiagramModel, HistoryOwner, ModelMetaData, TabbedEditable {

	/**
	 * F&uuml;gt den &uuml;bergebenen SQLScriptListener an die Liste der
	 * SQLScriptListener an, die bei der Generierung eines
	 * SQL-Aktualisierungsscriptes &uuml;ber die &Auml;nderungen am Datenmodell
	 * benachrichtigt werden sollen.
	 * 
	 * @param l
	 *            Der hinzuzuf&uuml;gende SQLScriptListener.
	 * 
	 * @changed OLI 22.03.2009 - Hinzugef&uuml;gt
	 */
	public void addSQLScriptListener(SQLScriptListener l);

	/**
	 * F&uuml;gt die angegebene Tabelle an die Liste der Tabellen des Diagramms
	 * an.
	 * 
	 * @param t
	 *            Die anzuf&uuml;gende Tabelle.
	 */
	public void addTabelle(TabellenModel t);

	/**
	 * Erweitert die gecachte Tabellenspaltenliste des Modells um die angegebene
	 * Tabellenspalte.
	 * 
	 * @param tsm
	 *            Die Tabellenspalte, die dem Tabellenspaltencache des Modells
	 *            hinzuge&uuml;gt werden soll.
	 * 
	 * @changed OLI 02.02.2008 - Hinzugef&uuml;gt.
	 */
	public void addToFieldCache(TabellenspaltenModel tsm);

	/**
	 * Liefert eine Instanz des DiagrammModels.
	 * 
	 * @return Eine Instanz des DiagrammModels.
	 */
	public DiagrammModel createDiagramm();

	/**
	 * Generiert aus dem &uuml;bergebenen STF ein Diagramm und gibt dessen
	 * Referenz zur&uuml;ck.
	 * 
	 * @param stf
	 *            Das STF, aus dem das Diagramm generiert werden soll.
	 */
	public DiagrammModel createDiagramm(StructuredTextFile stf);

	/**
	 * Generiert eine zu Diagramm passende DomainModel-Instanz.
	 * 
	 * @return Eine Instanz eines zum Diagramm passenden DomainModels.
	 */
	public DomainModel createDomain();

	/**
	 * Regt eine Codegenerierung zum Datenmodell mit den &uuml;bergebenen
	 * Parametern an.
	 * <P>
	 * Die Methode sollte die angegebene Default-Codefactory nutzen, wenn im
	 * Diagramm entweder eine nichtexistente CodeFactory angegeben worden ist
	 * oder gar keine. Nur, wenn weder g&uuml;tige CodeFactories, noch eine
	 * Default-CodeFactory angegeben wird, kommt die
	 * Archimedes-DefaultCodeFactory zum Einsatz.
	 * 
	 * @param dcf
	 *            Die Default-Codefactory, die genutzt werden soll, wenn keine
	 *            andere CodeFactory im Datenmodell konfiguriert ist.
	 * @param path
	 *            Der Pfad, der als Basis f&uuml;r die Ausgabe der CodeFactory
	 *            dienen soll.
	 * @param guiBundle
	 *            Ein GUI-Bundle zur Nutzung durch grafische Komponenten.
	 * @return <TT>true</TT>, falls die CodeFactories fehlerlos durchgelaufen
	 *         sind, <BR>
	 *         <TT>false</TT> sonst.
	 * 
	 * @changed OLI 21.03.2009 - Hinzugef&uuml;gt.
	 */
	public boolean generateCode(CodeFactory dcf, String path, GUIBundle guiBundle);

	/**
	 * Liefert die Namen der zus&auml;tzlich zum Standard-SQL-Scriptgenerator
	 * bei der Generierung von SQL-Aktualisierungsscripten anzubindenden
	 * SQLScriptListener. Die Namen der Klassen sind qualifiziert. Es
	 * k&ouml;nnen mehrere Klassennamen angegeben werden, die durch Kommata
	 * getrennt sein m&uuml;ssen.
	 * 
	 * @return Die Namen der zus&auml;tzlich zum Standard-SQL-Scriptgenerator
	 *         anzubindenden SQLScriptListener.
	 * 
	 * @changed OLI 22.03.2009 - Hinzugef&uuml;gt
	 */
	public String getAdditionalSQLScriptListener();

	/**
	 * Liefert eine Liste mit den FElder aller Tabellen des Diagramms.
	 * 
	 * @return Eine Liste mit den Feldern aller Tabellen des Diagramms.
	 * 
	 * @see getAllFields()
	 * @see getFieldCache()
	 * 
	 * @deprecated OLI 02.02.2008 - Hierf&uuml;r gibt's jetzt die Methoden
	 *             <TT>getAllFields()</TT> und <TT>getFieldCache()</TT>
	 */
	@Deprecated
	public Vector getAlleFelder();

	/**
	 * Liefert eine Liste aller Tabellenfelder des Modells. Diese Liste wird
	 * beim Aufruf der Methode aus den Tabellen des Modells generiert (im
	 * Gegensatz zu der gecachten Liste).
	 * 
	 * @return Eine Liste aller Tabellenspalten des Modells.
	 * 
	 * @changed OLI 02.02.2008 - Hinzugef&uuml;gt.
	 */
	@Deprecated
	public Vector getAllFields();

	/**
	 * Wertet das Feld mit den Codegeneratoroptionen f&uuml;r jede Tabelle des
	 * Modells nach dem &uuml;bergebenen Tag aus und liefert eine Map
	 * zur&uuml;ck, die hinter dem Tabellennamen die Liste aller in dem Tag
	 * gefundenen Strings speichert.
	 * 
	 * <P>
	 * <I>Beispiel: <BR>
	 * <TT>&lt;SuppressListGeneration&gt;Adress,
	 * ApplicationUserGroup&lt;/SuppressListGeneration&gt;</TT> in der Tabelle
	 * <TT>ApplicationUser</TT> <BR>
	 * liefert eine Map [["ApplicationUser", List("Adress",
	 * "ApplicationUserGroup")]]. </I>
	 * 
	 * @param tagName
	 *            Der Name des Tags, auf das die Codegeneratoroptionenfelder
	 *            durchsucht werden sollen.
	 * @param delimiter
	 *            Ein Trennzeichenfolge zum Absetzen der einzelnen
	 *            Listeneintr&auml;ge voneinander.
	 * @return Eine Map mit den Namen der betroffenen Tabellen und einer Liste
	 *         der Namen der Tabellen, die in dem Tag hinterlegt sind.
	 */
	public Map<String, java.util.List<String>> getCodegeneratorOptionsListTag(String tagName, String delimiter);

	/**
	 * Ermittelt die Referenz der zum angegebenen Namen passenden Domain.
	 * 
	 * @param n
	 *            Der Name der gesuchten Domain.
	 * @return Die Referenz auf die gew&uuml;nschte Domain.
	 */
	public DomainModel getDomain(String n);

	/**
	 * Ermittelt die Referenz der ersten zum angegebenen SQL-Typ passenden
	 * Domain.
	 * 
	 * @param sqltype
	 *            Der SQL-Typ zu dem eine Domain gesucht werden soll, bzw.
	 *            <TT>null</TT>, wenn es keine Domain zum SQL-Typ gibt.
	 * @return Die Referenz auf die gew&uuml;nschte Domain.
	 */
	public DomainModel getDomainByType(String sqltype);

	/**
	 * Liefert eine Liste mit den DomainModels des Diagramms.
	 * 
	 * <P>
	 * <I><B>Hinweis:</B> Die Liste ist eine Kopie der Domainliste des Diagramms
	 * mit Referenzen auf die DomainObjekte. Sie k&ouml;nnen den Inhalt der
	 * Domain-Objekte modifizieren, nicht aber die Liste selbst. <BR>
	 * Es sei angeraten, Operationen auf der Liste und an den Domain-Objekten
	 * &uuml;ber die Methoden <TT>addDomain</TT> und <TT>removeDomain</TT> zu
	 * regeln.</I>
	 * 
	 * @return Eine Liste mit den DomainModels des Diagramms.
	 */
	public Vector getDomains();

	/**
	 * Liefert eine Liste mit den DomainModels des Diagramms.
	 * 
	 * <P>
	 * <I><B>Hinweis:</B> Es handelt sich hierbei um eine Referenz auf die
	 * Domainliste des Diagramms mit Referenzen auf die DomainObjekte. Sie
	 * k&ouml;nnen den Inhalt der Domain-Objekte modifizieren und die Liste
	 * selbst modifizieren. <BR>
	 * Es sei angeraten, Operationen auf der Liste und an den Domain-Objekten
	 * &uuml;ber die Methoden <TT>addDomain</TT> und <TT>removeDomain</TT> zu
	 * regeln.
	 * 
	 * @return Referenz auf die Liste mit den Domains des Diagramms.
	 */
	public Vector getDomainsReference();

	/**
	 * Liefert eine Liste der referenzierbaren Spalten der Tabellen des
	 * Diagramms au&szlig;er denen der angebenen Tabelle.
	 * 
	 * @param tm
	 *            Die Tabelle deren referenzierbare Spalten nicht
	 *            zur&uuml;ckgeliefert werden sollen.
	 * @return Eine sortierte Liste mit den referenzierbaren Tabellenspalten des
	 *         Diagramms.
	 */
	public SortedVector getColumnsToBeReferenced(TabellenModel tm);

	/**
	 * Liefert eine Liste aller Tabellenfelder des Modells. Diese Liste wird
	 * beim Aufruf der Methode nicht neu erzeugt, sondern liefert eine gecachte
	 * Liste zur&uuml;ck.
	 * 
	 * @return Eine Liste aller Tabellenspalten des Modells aus einem Cache.
	 * 
	 * @changed OLI 02.02.2008 - Hinzugef&uuml;gt.
	 */
	public Vector getFieldCache();

	/**
	 * Liefert eine Liste der Schl&uuml;sselspalten der Tabellen des Diagramms
	 * au&szlig;er denen der angebenen Tabelle.
	 * 
	 * @param tm
	 *            Die Tabelle deren Schl&uuml;sselspalten nicht
	 *            zur&uuml;ckgeliefert werden sollen.
	 * @return Eine sortierte Liste mit den Schl&uuml;ssel-Tabellenspalten des
	 *         Diagramms.
	 */
	public SortedVector getKeyColumns(TabellenModel tm);

	@Deprecated
	/**
	 * Liefert eine Liste der Schl&uuml;sselspalten der Tabellen des Diagramms au&szlig;er denen
	 * der angebenen Tabelle.
	 *
	 * @param tm Die Tabelle deren Schl&uuml;sselspalten nicht zur&uuml;ckgeliefert werden
	 *         sollen.
	 * @return Eine sortierte Liste mit den Schl&uuml;ssel-Tabellenspalten des Diagramms.
	 *
	 * @see getKeyColumns()
	 *
	 * @deprecated OLI 01.06.2009 - Wegen falscher Schreibweise.
	 */
	public SortedVector getKeycolumns(TabellenModel tm);

	/**
	 * Liefert das Diagramm als Metadaten-Liste.
	 * 
	 * @return Das Diagramm als Metadaten-Liste.
	 */
	public SortedVector getMetadaten();

	/**
	 * Liste mit den Tabellenspalten, die die angegebene Tabelle referenzieren.
	 * 
	 * @param t
	 *            Die Tabelle, die auf Referenzen untersucht werden soll.
	 * @return Die Liste der die angegebene Tabelle referenzierenden
	 *         Tabellenspalten.
	 */
	@Deprecated
	public Vector getReferencers(TabellenModel t);

	/**
	 * Returns the color for the regular relations.
	 * 
	 * @return The color for the regular relations.
	 * 
	 * @changed OLI 12.01.2015 - Added.
	 */
	abstract public Color getRelationColorRegular();

	/**
	 * Returns the color for the relations to external tables.
	 * 
	 * @return The color for the relations to external tables.
	 * 
	 * @changed OLI 12.01.2015 - Added.
	 */
	abstract public Color getRelationColorToExternalTables();

	/**
	 * Ermittelt die Referenz der zum angegebenen Namen passenden Tabelle.
	 * 
	 * @param n
	 *            Der Name der gesuchten Tabelle.
	 * @return Die Referenz auf die gew&uuml;nschte Tabelle.
	 */
	@Deprecated
	public TabellenModel getTabelle(String n);

	/**
	 * Eine Liste der TabellenModel-Instanzen des Diagramms.
	 * 
	 * <P>
	 * <I><B>Hinweis:</B> Die Liste ist eine Kopie der Tabellenliste des
	 * Diagramms mit Referenzen auf die TabellenObjekte. Sie k&ouml;nnen den
	 * Inhalt der Tabellen-Objekte modifizieren, nicht aber die Liste selbst. <BR>
	 * Es sei angeraten, Operationen auf der Liste und an den Tabellen-Objekten
	 * &uuml;ber die Methoden <TT>addTabelle</TT> und <TT>removeTabelle</TT> zu
	 * regeln.
	 * 
	 * @return Eine Liste mit den TabellenModels des Diagramms.
	 */
	@Deprecated
	public Vector getTabellen();

	/**
	 * L&ouml;scht die angegebene Domain aus der Liste der Domains des
	 * Diagramms.
	 * 
	 * @param dom
	 *            Die anzul&ouml;schende Domain.
	 */
	public void removeDomain(DomainModel dom);

	/**
	 * L&ouml;scht angegebene Tabellenspalte aus der gecachten
	 * Tabellenspaltenliste des Modells.
	 * 
	 * @param tsm
	 *            Die Tabellenspalte, die dem Tabellenspaltencache des Modells
	 *            entfernt werden soll.
	 * 
	 * @changed OLI 02.02.2008 - Hinzugef&uuml;gt.
	 */
	public void removeFromFieldCache(TabellenspaltenModel tsm);

	/**
	 * L&ouml;scht den &uuml;bergebenen SQLScriptListener aus der Liste der
	 * SQLScriptListener, die bei der Generierung eines
	 * SQL-Aktualisierungsscriptes &uuml;ber die &Auml;nderungen am Datenmodell
	 * benachrichtigt werden sollen.
	 * 
	 * @param l
	 *            Der zu l&ouml;schende SQLScriptListener.
	 * 
	 * @changed OLI 22.03.2009 - Hinzugef&uuml;gt
	 */
	public void removeSQLScriptListener(SQLScriptListener l);

	/**
	 * L&ouml;scht die angegebene Tabelle aus der Liste der Tabellen des
	 * Diagramms.
	 * 
	 * @param t
	 *            Die anzul&ouml;schende Tabelle.
	 */
	public void removeTabelle(TabellenModel t);

	/**
	 * Setzt neue Namen f&uuml;r die zus&auml;tzlich zum
	 * Standard-SQL-Scriptgenerator bei der Generierung von
	 * SQL-Aktualisierungsscripten anzubindenden SQLScriptListener. Die Namen
	 * der Klassen sind qualifiziert. Es k&ouml;nnen mehrere Klassennamen
	 * angegeben werden, die durch Kommata getrennt sein m&uuml;ssen.
	 * 
	 * @param clsn
	 *            Die Namen der zus&auml;tzlich zum Standard-SQL-Scriptgenerator
	 *            anzubindenden SQLScriptListener. Die Namen der Klassen sind
	 *            qualifiziert anzugeben. Es k&ouml;nnen mehrere Klassennamen
	 *            angegeben werden, die durch Kommata getrennt + sein
	 *            m&uuml;ssen.
	 * 
	 * @changed OLI 22.03.2009 - Hinzugef&uuml;gt
	 */
	public void setAdditionalSQLScriptListener(String clsn);

	/**
	 * Erzeugt ein StructuredTextFile zum Speichern des Datenmodells unter
	 * Ber&uuml;cksichtigung des angegebenen Speichermodus.
	 * 
	 * @param dsm
	 *            Der Modus, der beim Speichern des Datenmodells angewandt
	 *            werden soll.
	 * @return Das in ein STF umgewandelte Diagramm.
	 * 
	 * @changed OLI 16.08.2008 - Erweiterung um einen Modus-Parameter, der die
	 *          M&ouml;glichkeit er&ouml;ffnet, nur Teile des Datenmodells in
	 *          das StructuredTextFile zu &uuml;bernehmen (Stichwort:
	 *          Dateiverkleinerung).
	 */
	public StructuredTextFile toSTF(DiagramSaveMode dsm) throws Exception;

	/**
	 * Liefert einen XML-String mit den Daten des Diagramms.
	 * 
	 * @return Das in einen XML-String umgewandelte Diagramm.
	 */
	public String toXML();

	/*******************************************************************************************
	 * Bis hier hin ist sortiert.
	 *******************************************************************************************/

	/** @return Der Autor zum DiagrammModel. */
	public String getAuthor();

	/**
	 * Setzt einen neuen Autor f&uuml;r das DiagrammModel ein.
	 * 
	 * @param author
	 *            Der neue Autor zum DiagrammModel.
	 */
	public void setAuthor(String author);

	/** @return Das Datum zur Versionsangabe zum DiagrammModel. */
	@Deprecated
	public PDate getDate();

	/**
	 * Setzt ein neues Datum zur Versionsangaben f&uuml;r das DiagrammModel ein.
	 * 
	 * @param date
	 *            Das neue Datum.
	 */
	@Deprecated
	public void setDate(PDate date);

	/** @return Die Versionsnummer des Diagramms. */
	public String getVersion();

	/**
	 * Setzt eine neue Versionsnummer f&uuml;r das DiagrammModel ein.
	 * 
	 * @param version
	 *            Die neue Versionsnummer
	 */
	public void setVersion(String version);

	/** @return Ein kurzer Kommentar zur Version. */
	public String getVersionComment();

	/**
	 * Setzt ein kurzer Kommentar zur Version zum Eintrag in die Datenbank.
	 * 
	 * @param comment
	 *            Der Kurzkommentar zur Version.
	 */
	public void setVersionComment(String comment);

	/** @return Die Schriftgr&ouml;szlig;e f&uuml;r die Tabelleninhalte. */
	public int getFontSizeTableContents();

	/**
	 * Setzt eine neue Schriftgr&ouml;szlig;e f&uuml;r die Tabelleninhalte.
	 * 
	 * @param fontsize
	 *            Die neue Schriftgr&ouml;szlig;e f&uuml;r die Tabelleninhalte.
	 */
	public void setFontSizeTableContents(int fontsize);

	/**
	 * Setzt eine neue Schriftgr&ouml;szlig;e f&uuml;r die
	 * Diagramm&uuml;berschrift.
	 * 
	 * @param fontsize
	 *            Die neue Schriftgr&ouml;szlig;e f&uuml;r die
	 *            Diagramm&uuml;berschrift.
	 */
	public void setFontSizeDiagramHeadline(int fontsize);

	/**
	 * Setzt eine neue Schriftgr&ouml;szlig;e f&uuml;r die Versionsangaben.
	 * 
	 * @param fontsize
	 *            Die neue Schriftgr&ouml;szlig;e f&uuml;r die Versionsangaben.
	 */
	public void setFontSizeSubtitles(int fontsize);

	/**
	 * F&uuml;gt die angegebene Stereotype an die Liste der Stereotypen des
	 * Diagramms an.
	 * 
	 * @param t
	 *            Die anzuf&uuml;gende Stereotype.
	 */
	public void addStereotype(StereotypeModel t);

	/**
	 * L&ouml;scht die angegebene Stereotype aus der Liste der Stereotypen des
	 * Diagramms.
	 * 
	 * @param t
	 *            Die anzul&ouml;schende Stereotype.
	 */
	public void removeStereotype(StereotypeModel t);

	/** @return Eine Liste mit den StereotypenModels des Diagramms. */
	public Vector getStereotypen();

	/**
	 * Ermittelt die Referenz der zum angegebenen Namen passenden Stereotype.
	 * 
	 * @param n
	 *            Der Name der gesuchten Stereotype.
	 * @return Die Referenz auf die gew&uuml;nschte Stereotype.
	 */
	public StereotypeModel getStereotype(String n);

	/** @return Eine Instanz eines zum Diagramm passenden StereotypeModels. */
	public StereotypeModel createStereotype();

	/** @return Referenz auf die Liste mit den Stereotype des Diagramms. */
	public Vector getStereotypeReference();

	/**
	 * F&uuml;gt den angegebenen DefaultComment an die Liste der DefaultComments
	 * des Diagramms an.
	 * 
	 * @param t
	 *            Der anzuf&uuml;gende DefaultComment.
	 */
	public void addDefaultComment(DefaultCommentModel t);

	/**
	 * L&ouml;scht den angegebenen DefaultComment aus der Liste der
	 * DefaultComments des Diagramms.
	 * 
	 * @param t
	 *            Der zu l&ouml;schende DefaultComment.
	 */
	public void removeDefaultComment(DefaultCommentModel t);

	/** @return Eine Liste mit den DefaultCommentModels des Diagramms. */
	public Vector getDefaultComments();

	/**
	 * Ermittelt die Referenz des zum angegebenen Namen passende DefaultComment.
	 * 
	 * @param n
	 *            Der Name des gesuchten DefaultComment.
	 * @return Die Referenz auf den gew&uuml;nschte DefaultComment.
	 */
	public DefaultCommentModel getDefaultComment(String n);

	/** @return Eine Instanz eines zum Diagramm passenden DefaultCommentModels. */
	public DefaultCommentModel createDefaultComment();

	/** @return Referenz auf die Liste mit den DefaultComments des Diagramms. */
	public Vector getDefaultCommentsReference();

	/**
	 * @return <TT>true</TT>, wenn aufgehobene Objekte bei Anzeige und Druck
	 *         ausgeblendet werden sollen.
	 */
	@Deprecated
	public boolean isAufgehobeneAusblenden();

	/**
	 * Setzt bzw. L&ouml;scht die Flagge, die f&uuml;r das Ausblenden
	 * aufgehobener Objekte sorgt.
	 * 
	 * @param aufgehobeneAusblenden
	 *            Der neue Wert f&uuml;r die Flagge.
	 */
	@Deprecated
	public void setAufgehobeneAusblenden(boolean aufgehobeneAusblenden);

	/**
	 * &Uuml;bernimmt den &uuml;bergebenen JDBCDataSourceRecord als neuen
	 * DataSourceRecord f&uuml;r den Import von Datenbank-Schemata.
	 * 
	 * @param dsr
	 *            Der zu &uuml;bernehmende JDBCDataSourceRecord.
	 */
	public void setImportDataSourceRecord(JDBCDataSourceRecord dsr);

	/** @return Der Basispfad f&uuml;r die Codegenerierung. */
	public String getCodePfad();

	/**
	 * Setzt den &uuml;bergebenen String als neuen Basispfad f&uuml;r die
	 * Codegenerierung ein.
	 * 
	 * @param pfad
	 *            Der neue Basispfad f&uuml;r die Codegenerierung.
	 */
	public void setCodePfad(String pfad);

	/**
	 * @return Der Name der Application, f&uuml;r die der Code erzeugt werden
	 *         soll.
	 */
	public String getApplicationName();

	/**
	 * @return Name der DB-Versionen-Tabelle, falls eine solche im Modell
	 *         vorgesehen ist.
	 */
	public String getDBVersionTablename();

	/**
	 * @return Name der Versions-(Schl&uuml;ssel-)Spalte in der
	 *         DB-Versionen-Tabelle, falls eine solche im Modell vorgesehen ist.
	 */
	public String getDBVersionDBVersionColumn();

	/**
	 * @return Name der Bemerkungsspalte der DB-Versionen-Tabelle, falls eine
	 *         solche im Modell vorgesehen ist.
	 */
	public String getDBVersionDescriptionColumn();

	/**
	 * @return <TT>false</TT>, wenn die durch Foreignkeys referenzierten Spalten
	 *         auch im Diagramm angezeigt werden sollen.
	 */
	public boolean isShowReferencedColumns();

	/**
	 * @return Eine Referenz auf die Liste der Views des Diagramms. * / public
	 *         List<ViewModel> getViews();
	 * 
	 *         /** Ermittelt die Views, in denen die angegebene Tabelle
	 *         enthalten ist.
	 * 
	 * @param tm
	 *            Das TabellenModel zu dem die Views ermittelt werden sollen.
	 * @return Eine Liste mit den Views, in denen das TabellenModel enthalten
	 *         ist. / public List<ViewModel> getViews(TabellenModel tm);
	 * 
	 *         /** Ermittelt den View mit dem angegebenen Namen.
	 * 
	 * @param name
	 *            Der Name, zu dem der entsprechende View ermittelt werden soll.
	 * @return Der View mit dem angegebenen Namen bzw: <TT>null</TT>, wenn es
	 *         keinen solchen View gibt. / public ViewModel getView(String
	 *         name);
	 * 
	 *         /**
	 * @return Die minimale Zahl der Seiten pro Spalte, die zur Anzeige des
	 *         Diagrammes n&ouml;tig ist. / public int getPagesPerColumn();
	 * 
	 *         /**
	 * @return Die minimale Zahl der Seiten pro Zeile, die zur Anzeige des
	 *         Diagrammes n&ouml;tig ist. / public int getPagesPerRow();
	 */

	/**
	 * Pr&uuml;ft, ob alle Tabellennamen im Modell vorkommen.
	 * 
	 * <P>
	 * <B>Author:</B> M.Eckstein
	 * 
	 * @param tablenamesarray
	 *            Array mit den Tabellennamen
	 * @return <TT>true</TT>, falls alle Tabellennamen im Modell vorkommen,
	 *         <TT>false</TT> sonst.
	 */
	public boolean containsTablenames(String[] tablenamesarray);

	/**
	 * Pr&uuml;ft, ob der Tabellenname im Modell vorkommt.
	 * 
	 * <P>
	 * <B>Author:</B> M.Eckstein
	 * 
	 * @param tablename
	 *            der Tabellenname
	 * @return <TT>true</TT>, falls der Tabellenname im Modell vorkommt;
	 *         <TT>false</TT> sonst.
	 */
	public boolean containsTablename(String tablename);

	/**
	 * @return <TT>true</TT>, wenn Pflichtfelder im Diagramm gekennzeichnet
	 *         werden sollen, <TT>false</TT>.
	 */
	@Deprecated
	public boolean markWriteablemembers();

	/**
	 * Liefert den Namen der Klassen, die als CodeFactory herhalten soll.
	 * 
	 * @return Der (qualifizierte) Name bzw. die Namen der Klassen, die als
	 *         CodeFactory herhalten soll.
	 */
	public String getCodeFactoryClassName();

	/**
	 * Setzt den &uuml;bergebenen String als Namen f&uuml;r die Klasse ein, die
	 * als CodeFactory instanziert werden soll. Es k&ouml;nnen auch mehrere
	 * Factory-Namen angebenen werden, die durch Kommata getrennt werden
	 * m&uuml;ssen.
	 * 
	 * @param cfcn
	 *            Der (qualifizierte) Name der Klasse, die als CodeFactory
	 *            instanziert werden soll, bzw. eine Liste mit durch Kommata
	 *            getrennten Factory-Namen.
	 */
	public void setCodeFactoryClassName(String cfcn);

	/**
	 * F&uuml;gt den &uuml;bergebenen DiagrammModelListener an die Liste der das
	 * DiagrammModel abh&ouml;renden Listener an.
	 * 
	 * @param dml
	 *            Der anzuf&uuml;gende DiagrammModelListener.
	 */
	public void addDiagrammModelListener(DiagrammModelListener dml);

	/**
	 * Entfernt den angegebenen DiagrammModelListener aus der Liste der an dem
	 * DiagrammModel lauschenden Listener.
	 * 
	 * @param dml
	 *            Der aus der Liste zu entfernende Listener.
	 */
	public void removeDiagrammModelListener(DiagrammModelListener dml);

	/**
	 * Gibt das &uuml;bergebene SQLScriptEvent an die angebundenen
	 * SQLScriptListener weiter.
	 * 
	 * @param e
	 *            Das an die angebundenen SQLScriptListener weiterzugebende
	 *            SQLScriptEvent.
	 * 
	 * @changed OLI 22.03.2009 - Hinzugef&uuml;gt.
	 */
	abstract public void fireDataSchemeChanged(SQLScriptEvent e);

	/** Meldet eine &Auml;nderung des DiagrammModels an die Listener weiter. */
	public void fireDiagrammAltered();

	/**
	 * Liefert eine alternative Klasse als Basis f&uuml;r die Generierung der
	 * Udschebtis.
	 * 
	 * @return Der Name der Klasse, von der alle Udschebtis erben sollen.
	 * 
	 * @changed <P>
	 *          OLI 11.02.2008 - Hinzugef&uum;gt.<BR>
	 * 
	 */
	public String getUdschebtiBaseClassName();

	/**
	 * Setzt den Namen der alternativen Basisklasse f&uuml;r die Applikation.
	 * 
	 * @param ubcn
	 *            Der Name der alternativen Basisklasse f&uuml;r die
	 *            Datenobjekte.
	 * 
	 * @changed <P>
	 *          OLI 11.02.2008 - Hinzugef&uum;gt.<BR>
	 * 
	 */
	public void setUdschebtiBaseClassName(String ubcn);

	/**
	 * Liefert den Zustand der Ausgrauung von technischen Feldern.
	 * 
	 * @return <TT>true</TT>, wenn die als technisch gekennzeichneten Felder im
	 *         Diagramm ausgegraut angezeigt werden sollen; sonst <TT>false</TT>
	 * 
	 * @changed <P>
	 *          OLI 09.08.2008 - Hinzugef&uuml;gt.
	 * 
	 */
	public boolean isPaintTechnicalFieldsInGray();

	/**
	 * Liefert den Zustand der Ausgrauung von transienten Feldern.
	 * 
	 * @return <CODE>true</CODE>, wenn die als transient gekennzeichneten Felder
	 *         im Diagramm ausgegraut angezeigt werden sollen; sonst
	 *         <CODE>false</CODE>.
	 * 
	 * @changed OLI 10.06.2015 - Hinzugef&uuml;gt.
	 * 
	 */
	abstract public boolean isPaintTransientFieldsInGray();

	/**
	 * Setzt den Zustand der Ausgrauung von technischen Feldern auf den
	 * angegebenen Wert.
	 * 
	 * @param b
	 *            <TT>true</TT>, wenn die als technisch gekennzeichneten Felder
	 *            im Diagramm ausgegraut angezeigt werden sollen; sonst
	 *            <TT>false</TT>
	 * 
	 * @changed <P>
	 *          OLI 09.08.2008 - Hinzugef&uuml;gt.
	 * 
	 */
	public void setPaintTechnicalFieldsInGray(boolean b);

	/**
	 * Setzt den Zustand der Ausgrauung von transienten Feldern auf den
	 * angegebenen Wert.
	 * 
	 * @param b
	 *            <CODE>true</CODE>, wenn die als transient gekennzeichneten
	 *            Felder im Diagramm ausgegraut angezeigt werden sollen; sonst
	 *            <CODE>false</CODE>
	 * 
	 * @changed OLI 11.06.2015 - Hinzugef&uuml;gt.
	 * 
	 */
	public void setPaintTransientFieldsInGray(boolean b);

	/**
	 * Liefert das Speicherscript zum Archimedesmodell. Dieses Script wird durch
	 * die Archimedesapplikation nach dem Wechseln des Datenmodells oder dem
	 * Verlassen des Programmes angewandt.
	 * 
	 * @return Ein String mit dem Script, das Befehle f&uuml;r die
	 *         Archimedes-Kommandozeile enthalten mu&szlig;.
	 * 
	 * @changed OLI 04.09.2008 - Hinzugef&uuml;gt.
	 *          <P>
	 * 
	 */
	public String getAfterWriteScript();

	/**
	 * Setzt den &uuml;bergebenen String als neues Speicherscript zum
	 * Archimedesmodell ein. Dieses Script wird durch die Archimedesapplikation
	 * nach dem Wechseln des Datenmodells oder dem Verlassen des Programmes
	 * angewandt.
	 * 
	 * @param script
	 *            Ein String mit dem Script, das Befehle f&uuml;r die
	 *            Archimedes-Kommandozeile enthalten mu&szlig;.
	 * 
	 * @changed OLI 04.09.2008 - Hinzugef&uuml;gt.
	 *          <P>
	 * 
	 */
	public void setAfterWriteScript(String script);

	/**
	 * Returns the complex index with the passed name.
	 * 
	 * @param name
	 *            The name of the complex index which is to return.
	 * @return The complex index with the passed name or <CODE>null</CODE> if
	 *         there is no complex index with the passed name.
	 * 
	 * @changed OLI 21.12.2015 - Hinzugef&uuml;gt.
	 */
	abstract public IndexMetaData getComplexIndex(String name);

	/**
	 * Returns the color for the relations to external tables.
	 * 
	 * @return The color for the relations to external tables.
	 * 
	 * @changed OLI 12.01.2015 - Added.
	 */
	abstract public void setRelationColorExternalTables(Color color);

	/**
	 * Returns the color for the regular relations.
	 * 
	 * @return The color for the regular relations.
	 * 
	 * @changed OLI 12.01.2015 - Added.
	 */
	abstract public void setRelationColorRegular(Color color);

	/**
	 * Returns a <CODE>PredeterminedOptionProvider</CODE> or <CODE>null</CODE>
	 * if there is non defined.
	 * 
	 * @return A <CODE>PredeterminedOptionProvider</CODE> or <CODE>null</CODE>
	 *         if there is non defined.
	 * 
	 * @changed OLI 01.10.2015 - Added.
	 */
	abstract public PredeterminedOptionProvider getPredeterminedOptionProvider();

	/**
	 * Sets a new <CODE>PredeterminedOptionProvider</CODE> or <CODE>null</CODE>
	 * if there is non defined.
	 * 
	 * @param pop
	 *            A new <CODE>PredeterminedOptionProvider</CODE> or
	 *            <CODE>null</CODE> if there is non defined.
	 * 
	 * @changed OLI 01.10.2015 - Added.
	 */
	abstract public void setPredeterminedOptionProvider(PredeterminedOptionProvider pop);

}