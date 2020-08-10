/*
 * DataDiagramModel.java
 *
 * 26.04.2008
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

import java.util.List;
import java.util.Vector;

import corent.base.SortedVector;
import corent.dates.PDate;
import corent.db.DBExecMode;
import corent.db.JDBCDataSourceRecord;
import corent.files.StructuredTextFile;

/**
 * Diese Interface definiert das Verhalten von Objekten, die von einer
 * Archimedes-Diagramm-Komponente ausgegeben werden k&ouml;nnen sollen.
 * 
 * <P>
 * Das DataDiagramModel repr&auml;sentiert in seiner Implementierung das
 * Diagramm auf dem die Tabellen (DataTableModels) abgebildet werden. Wird in
 * der Beschreibung der Methoden von Tabellen, vom Diagramm oder von den
 * Tabellenspalten gesprochen, so sind DataTableModels, DataDiagramModels bzw.
 * DataColumnModels gemeint.
 * 
 * <P>
 * Dieses Interface ist durch Sprachangleichung der Methoden des Interfaces
 * <TT>DataDiagramModel</TT> entstanden.
 * 
 * @author ollie
 * 
 * @changed <P>
 *          OLI 26.04.2008 - Hinzugef&uuml;gt.
 * 
 */

public interface DataDiagramModel {

	/**
	 * F&uuml;gt das angegebene DataTableModel an die Liste der DataTableModels
	 * des DataDiagramModels an.
	 * 
	 * @param dtm
	 *            Das anzuf&uuml;gende DataTableModel.
	 */
	public void addDataTable(DataTableModel dtm);

	/**
	 * F&uuml;gt die angegebene Domain an die Liste der Domains des Diagramms
	 * an.
	 * 
	 * @param dom
	 *            Die anzuf&uuml;gende Domain.
	 */
	public void addDomain(DomainModel dom);

	/**
	 * F&uuml;gt den angegebenen DefaultComment an die Liste der DefaultComments
	 * des Diagramms an.
	 * 
	 * @param dcm
	 *            Der anzuf&uuml;gende DefaultComment.
	 */
	public void addDefaultComment(DefaultCommentModel dcm);

	/**
	 * F&uuml;gt den &uuml;bergebenen DataDiagramModelListener an die Liste der
	 * das DataDiagramModel abh&ouml;renden Listener an.
	 * 
	 * @param ddml
	 *            Der anzuf&uuml;gende DataDiagramModelListener.
	 */
	public void addDataDiagramModelListener(DataDiagramModelListener ddml);

	/**
	 * F&uuml;gt die angegebene Stereotype an die Liste der Stereotypen des
	 * Diagramms an.
	 * 
	 * @param t
	 *            Die anzuf&uuml;gende Stereotype.
	 */
	public void addStereotype(StereotypeModel t);

	/**
	 * Erweitert die gecachte Tabellenspaltenliste des Modells um die angegebene
	 * Tabellenspalte.
	 * 
	 * @param dcm
	 *            Die Tabellenspalte, die dem Tabellenspaltencache des Modells
	 *            hinzuge&uuml;gt werden soll.
	 */
	public void addToFieldCache(DataColumnModel dcm);

	/**
	 * &Auml;ndert das Diagramm nach den Vorgaben der &uuml;bergebene Metadaten
	 * ab.
	 * 
	 * @param metadata
	 *            Ein Vector mit den Metadaten der Tabellen der abzugleichenden
	 *            Datenbank.
	 * @param domains
	 *            Diese Flagge ist gesetzt, wenn die Domains aus der
	 *            importierten Datenbank &uuml;bernommen werden sollen. Macht
	 *            nat&uuml;rlich nur Sinn, wenn das DBMS auch Domains
	 *            unterst&uuml;tzt ;o)
	 * @param references
	 *            Wird diese Flagge gesetzt, so versucht die Funktion Referenzen
	 *            in das Diagramm zu &uuml;bernehmen.
	 */
	public void alterDiagram(Vector metadata, boolean domains, boolean references);

	/**
	 * Erzeugt einen HTML-Glossar &uuml;ber das Datenmodell.
	 * 
	 * @return Ein Vector mit den Zeilen des Glossars zum Datenmodell im
	 *         HTML-Format.
	 */
	public Vector buildGlossary();

	/**
	 * Erzeugt einen HTML-Report zu den Domains des Datenmodells.
	 * 
	 * @return Ein Vector mit den Zeilen des Reports im HTML-Format.
	 */
	public Vector buildReportDomains();

	/**
	 * Erzeugt ein SQL-Script, das f&uuml;r ein Update der &uuml;bergebenen
	 * Metadaten auf das durch das Diagramm repr&auml;sentierte Datenschema
	 * n&ouml;tig ist.
	 * 
	 * @param md
	 *            Die Metadatenbeschreibung der zu aktualisierenden Datenbank.
	 * @param hasDomains
	 *            Diese Flagge kann gesetzt werden, wenn ein auf Domains
	 *            basierendes Script erzeugt werden soll.
	 * @param noticeFkNotNull
	 *            Wird diese Flagge gesetzt, wird die NotNull-Klausel bei
	 *            Foreignkey auch im Datenschema des DBMS gesetzt.
	 * @param setReferences
	 *            Wird diese Flagge gesetzt, werden die Referenz-Klauseln bei
	 *            Foreignkeys in das Datenschema des DBMS &uuml;bernommen.
	 * @param dbmode
	 *            Der Datenbankmodus, zu dem das Update-Script erstellt werden
	 *            soll.
	 * @param html
	 *            Ein Vector zur Aufnahme des HTML-Update-Reports.
	 * @param indices
	 *            Ein Vector mit Informationen &uuml;ber die Indices des
	 *            Datenmodells.
	 * @return Ein Vector mit einem SQL-Script, welches zum Aktualisieren auf
	 *         die betreffende Datenbank angewandt werden mu&szlig;.
	 */
	public Vector buildUpdateScript(SortedVector md, boolean hasDomains, boolean noticeFkNotNull,
			boolean setReferences, DBExecMode dbmode, Vector html, Vector indices);

	/**
	 * Hebt die Markierung, da&szlig; das Diagramm ge&auml;ndert worden ist,
	 * auf.
	 */
	public void clearAltered();

	/**
	 * Pr&uuml;ft, ob der angegebene Tabellenname im Diagramm vorkommt.
	 * 
	 * @param tablename
	 *            Der zu pr&uuml;fende Tabellenname
	 * @return <TT>true</TT>, falls der Tabellenname im Diagramm vorkommt;
	 *         <TT>false</TT> sonst.
	 */
	public boolean containsTablename(String tablename);

	/**
	 * Pr&uuml;ft, ob alle angegebenen Tabellennamen im Diagramm vorkommen.
	 * 
	 * @param tablenamesarray
	 *            Ein Array mit den zu pr&uuml;fenden Tabellennamen.
	 * @return <TT>true</TT>, falls alle angegebenen Tabellennamen im Modell
	 *         vorkommen, <TT>false</TT> sonst.
	 */
	public boolean containsTablenames(String[] tablenamesarray);

	/**
	 * Liefert eine Instanz des DataDiagramModels.
	 * 
	 * @return Eine Instanz des DataDiagramModels.
	 */
	public DataDiagramModel createDiagram();

	/**
	 * Liefert eine neu DefaultComment-Instanz zum DataDiagramModel.
	 * 
	 * @return Eine Instanz eines zum DataDiagramModel passenden
	 *         DefaultCommentModels.
	 */
	public DefaultCommentModel createDefaultComment();

	/**
	 * Generiert aus dem &uuml;bergebenen StructuredTextFile ein
	 * DataDiagramModel und gibt dessen Referenz zur&uuml;ck.
	 * 
	 * @param stf
	 *            Das StructuredTextFile, aus dem das DataDiagramModel generiert
	 *            werden soll.
	 */
	public DataDiagramModel createDiagram(StructuredTextFile stf);

	/**
	 * Liefert ein neues DomainModel.
	 * 
	 * @return Eine neues DomainModels.
	 */
	public DomainModel createDomain();

	/**
	 * Generiert eine zum DataDiagramModel passende Instanz des
	 * StereotypeModels.
	 * 
	 * @return Eine Instanz eines zum Diagramm passenden StereotypeModels.
	 */
	public StereotypeModel createStereotype();

	/** Meldet eine &Auml;nderung des DataDiagramModels an die Listener weiter. */
	public void fireDiagrammAltered();

	/**
	 * Liefert eine Liste aller Tabellenfelder des Modells. Diese Liste wird
	 * beim Aufruf der Methode aus den Tabellen des Modells generiert (im
	 * Gegensatz zu der gecachten Liste).
	 * 
	 * @return Eine Liste aller Tabellenspalten des Modells.
	 */
	public Vector getAllFields();

	/**
	 * Liefert den Namen der Applikation, die aus dem Datenmodell &uuml;ber
	 * einen Codewriter erzeugt werden soll.
	 * 
	 * @return Der Name der Applikation, f&uuml;r die der Code erzeugt werden
	 *         soll.
	 */
	public String getApplicationName();

	/**
	 * Liefert den Namen des Autors des DataDiagramModels.
	 * 
	 * @return Der Autor zum DataDiagramModel.
	 */
	public String getAuthor();

	/**
	 * Liefert den Basispfad f&uuml;r die Codegenerierung auf Basis des
	 * Datenmodells.
	 * 
	 * @return Der Basispfad f&uuml;r die Codegenerierung.
	 */
	public String getBaseCodePath();

	/**
	 * Liefert den Namen des Basis-Packages der Applikation.
	 * 
	 * @return Liefert den Namen des Basis-Package der Applikation, falls diese
	 *         &uuml;ber ein solches verf&uuml;gt.
	 */
	public String getBasePackageName();

	/**
	 * Liefert den Namen der Klasse, die als CodeFactory herhalten soll.
	 * 
	 * @return Der (qualifizierte) Name der Klasse, die als CodeFactory
	 *         herhalten soll.
	 */
	public String getCodeFactoryClassName();

	/**
	 * Liefert eine Liste der referenzierbaren Spalten der Tabellen des
	 * Diagramms au&szlig;er denen der angebenen Tabelle.
	 * 
	 * @param dtm
	 *            Die Tabelle deren referenzierbare Spalten nicht
	 *            zur&uuml;ckgeliefert werden sollen.
	 * @return Eine nach Namen sortierte Liste mit den referenzierbaren
	 *         Tabellenspalten des Diagramms (ausgenommen jener, die zu dem
	 *         angegebenen DataTableModel geh&ouml;ren).
	 */
	public SortedVector getColumnsToBeReferenced(DataTableModel dtm);

	/**
	 * Liefert ein Datum zum DataDiagramModel. Hier kann beispielsweise das
	 * Datum zur aktuellen Versionsangabe des Models hinterlegt sein.
	 * 
	 * @return Ein Datum zum DataDiagramModel.
	 */
	public PDate getDate();

	/**
	 * Liefert den Namen der Tabellenspalte zur Aufnahme einer Versionsnummer,
	 * falls eine solche Versionshistorientabelle im Datenmodell vorgesehen ist
	 * 
	 * @return Name der Versions-(Schl&uuml;ssel-)Spalte in der
	 *         DB-Versionen-Tabelle, falls eine solche im Modell vorgesehen ist.
	 */
	public String getDBVersionDBVersionColumn();

	/**
	 * Liefert den Namen der Tabellenspalte zur Aufnahme eines
	 * Versionskommentar, falls eine solche Versionshistorientabelle im
	 * Datenmodell vorgesehen ist
	 * 
	 * @return Name der Bemerkungsspalte der DB-Versionen-Tabelle, falls eine
	 *         solche im Modell vorgesehen ist.
	 */
	public String getDBVersionDescriptionColumn();

	/**
	 * Liefert den Namen einer Versionhistorientabelle, falls eine solche im
	 * Datenmodell vorgesehen ist. Die Methode kann beim Bau des Updatescriptes
	 * genutzt werden, um einen Historieneintrag in diese Tabelle zu generieren.
	 * 
	 * @return Name der DB-Versionen-Tabelle, falls eine solche im Modell
	 *         vorgesehen ist.
	 */
	public String getDBVersionTablename();

	/**
	 * Ermittelt die Referenz des zum angegebenen Namen passende DefaultComment.
	 * 
	 * @param n
	 *            Der Name des gesuchten DefaultComment.
	 * @return Die Referenz auf den gew&uuml;nschte DefaultComment.
	 */
	public DefaultCommentModel getDefaultComment(String n);

	/**
	 * Liefert eine Kopie der Liste mit den DefaultComments des Diagramms. Die
	 * in der Liste gespeicherten DefaultComments werden jedoch nicht mit
	 * kopiert. Zugriffe auf die in dem Vector gespeicherten Objekte schlagen
	 * sich auf die DefaultComments des Diagramms durch.
	 * 
	 * @return Eine Liste mit den DefaultCommentModels des Diagramms.
	 */
	public Vector getDefaultComments();

	/**
	 * Eine Referenz auf die Liste der DefaultComments des Diagramms.
	 * 
	 * @return Referenz auf die Liste mit den DefaultComments des Diagramms.
	 */
	public Vector getDefaultCommentsReference();

	/**
	 * Ermittelt die Referenz des zum angegebenen Namen passenden DomainModels.
	 * 
	 * @param n
	 *            Der Name des gesuchten DomainModels.
	 * @return Die Referenz auf das gew&uuml;nschte DomainModel.
	 */
	public DomainModel getDomain(String n);

	/**
	 * Ermittelt die Referenz des ersten zum angegebenen SQL-Typ passenden
	 * DomainModel.
	 * 
	 * @param sqltype
	 *            Der SQL-Typ zu dem ein DomainModel gesucht werden soll, bzw.
	 *            <TT>null</TT>, wenn es kein DomainModel zum SQL-Typ gibt.
	 * @return Die Referenz auf das gew&uuml;nschte DomainModel.
	 */
	public DomainModel getDomainByType(String sqltype);

	/**
	 * Liefert eine Liste mit den DomainModels des DataDiagramModels. Hierbei
	 * handelt es sich um eine Kopie der DomainModel-Liste des DataDiagramModels
	 * mit Referenzen auf dessen Domains. &Auml;nderungen an den DomainModels
	 * der Liste schlagen sich also auf die DomainModels des DataDiagramModels
	 * durch.
	 * 
	 * @return Eine Liste mit den DomainModels des DataDiagramModels.
	 */
	public Vector getDomains();

	/**
	 * Liefert eine Referenz auf die Liste mit den Domains des
	 * DataDiagramModels.
	 * 
	 * @return Referenz auf die Liste mit den Domains des DataDiagramModels.
	 */
	public Vector getDomainsReference();

	/**
	 * Liefert eine Liste aller Tabellenfelder des Modells. Diese Liste wird
	 * beim Aufruf der Methode nicht neu erzeugt, sondern liefert eine gecachte
	 * Liste zur&uuml;ck.
	 * 
	 * @return Eine Liste aller Tabellenspalten des Modells aus einem Cache.
	 */
	public Vector getFieldCache();

	/**
	 * Liefert die Schriftgr&ouml;&szlig;e, die in der Diagrammansicht f&uuml;r
	 * den Diagrammtitel (Namen) genutzt werden soll.
	 * 
	 * @return Die Schriftgr&ouml;&szlig;e f&uuml;r den Diagrammtitel (Namen).
	 */
	public int getFontSizeDiagramHeadline();

	/**
	 * Liefert die Schriftgr&ouml;&szlig;e, die in der Diagrammansicht f&uuml;r
	 * die Versionsangabe genutzt werden soll.
	 * 
	 * @return Die Schriftgr&ouml;&szlig;e f&uuml;r die Versionsangabe.
	 */
	public int getFontSizeSubtitles();

	/**
	 * Liefert die Schriftgr&ouml;&szlig;e, die in der Diagrammansicht f&uuml;r
	 * Tabelleninhalte genutzt werden soll.
	 * 
	 * @return Die Schriftgr&ouml;&szlig;e f&uuml;r die Tabelleninhalte.
	 */
	public int getFontSizeTableContents();

	/**
	 * Liefert eine Referenz auf den JDBCDataSoureRecord mit den Daten zum
	 * Zugriff auf die f&uuml;r den Import vorgesehene Datenbank.
	 * 
	 * @return Der JDBCDataSourceRecord, der f&uuml;r den Datenschema-Import
	 *         zust&auml;ndig ist.
	 */
	public JDBCDataSourceRecord getImportDataSourceRecord();

	/**
	 * Liefert eine Liste der Schl&uuml;sselspalten der Tabellen des Diagramms
	 * au&szlig;er denen der angebenen Tabelle.
	 * 
	 * @param dtm
	 *            Die Tabelle deren Schl&uuml;sselspalten nicht
	 *            zur&uuml;ckgeliefert werden sollen.
	 * @return Eine nach Namen sortierte Liste mit den
	 *         Schl&uuml;ssel-Tabellenspalten des Diagramms (ausgenommen jenen,
	 *         der angegebenen Tabelle).
	 */
	public SortedVector getKeyColumns(DataTableModel dtm);

	/**
	 * Liefert eine Liste mit den Metadaten des Diagramms. Diese Metadaten
	 * bestehen aus einem nach Tabellennamen sortierten Vector von
	 * TableMetaData-Objekten.
	 * 
	 * @return Das Diagramm als Metadaten-Liste.
	 */
	public SortedVector getMetadata();

	/**
	 * Liefert die Bezeichnung des DataDiagramModels.
	 * 
	 * @return Die Bezeichnung des DataDiagramModels.
	 */
	public String getName();

	/**
	 * Liste mit den Tabellenspalten, die die angegebene Tabelle referenzieren.
	 * 
	 * @param dtm
	 *            Die Tabelle, die auf Referenzen untersucht werden soll.
	 * @return Die Liste der die angegebene Tabelle referenzierenden
	 *         Tabellenspalten.
	 */
	public Vector getReferencers(DataTableModel dtm);

	/**
	 * Ermittelt die Referenz der zum angegebenen Namen passenden Stereotype.
	 * 
	 * @param n
	 *            Der Name der gesuchten Stereotype.
	 * @return Die Referenz auf die gew&uuml;nschte Stereotype.
	 */
	public StereotypeModel getStereotype(String n);

	/**
	 * Liefert eine Kopie der Liste mit den StereotypeModels des Diagramms. Die
	 * StereotypeModels werden hier bei jedoch nicht kopiert. &Auml;nderungen an
	 * den in dem Vector gespeicherten Referenzen schlagen sich auf die
	 * StereotypeModels des Diagramms durch.
	 * 
	 * @return Eine Liste mit den StereotypeModels des Diagramms.
	 */
	public Vector getStereotypes();

	/**
	 * Liefert eine Referenz auf die Liste der Stereotype des Diagramms.
	 * 
	 * @return Referenz auf die Liste mit den Stereotype des Diagramms.
	 */
	public Vector getStereotypesReference();

	/**
	 * Ermittelt die Referenz das zum angegebenen Namen passenden
	 * DataTableModel.
	 * 
	 * @param n
	 *            Der Name des gesuchten DataTableModel.
	 * @return Die Referenz auf das gew&uuml;nschte DataTableModel.
	 */
	public DataTableModel getTable(String n);

	/**
	 * Liefert eine Liste mit dem DataTableModels des DataDiagramModels.
	 * 
	 * @return Eine Liste mit den DataTableModels des DataDiagramModels.
	 */
	public Vector getTables();

	/**
	 * Liefert eine alternative Klasse als Basis f&uuml;r die Generierung der
	 * Udschebtis.
	 * <P>
	 * Udschebtis sind vereinfachte Zwischenklassen die prinzipiell nur
	 * Accessoren und Mutatoren enthalten sollte und durch den Codewriter
	 * jederzeit &uuml;berschrieben werden k&ouml;nnen.
	 * 
	 * @return Der Name der Klasse, von der alle Udschebtis erben sollen.
	 */
	public String getUdschebtiBaseClassName();

	/**
	 * Liefert eine Referenz auf die Zugriffsdaten f&uuml;r die Datenbank, die
	 * zur Erstellung eines Datenbankupdatescriptes vorgesehen ist.
	 * 
	 * @return Der JDBCDataSourceRecord, der f&uuml;r den Datenschema-Update
	 *         zust&auml;ndig ist.
	 */
	public JDBCDataSourceRecord getUpdateDataSourceRecord();

	/**
	 * Liefert eine Versionsangabe zum DataDiagramModel.
	 * 
	 * @return Die Versionsangabe des DataDiagramModels.
	 */
	public String getVersion();

	/**
	 * Liefert einen Kommentar zur aktuellen Version des DataDiagramModels.
	 * 
	 * @return Ein kurzer Kommentar zur Version.
	 */
	public String getVersionComment();

	/**
	 * Ermittelt den View mit dem angegebenen Namen.
	 * 
	 * @param name
	 *            Der Name, zu dem der entsprechende View ermittelt werden soll.
	 * @return Der View mit dem angegebenen Namen bzw: <TT>null</TT>, wenn es
	 *         keinen solchen View gibt.
	 */
	public ViewModel getView(String name);

	/**
	 * Eine Liste mit den Views des Diagramms.
	 * 
	 * @return Eine Referenz auf die Liste der Views des Diagramms.
	 */
	public List<ViewModel> getViews();

	/**
	 * Ermittelt die Views, in denen die angegebene Tabelle enthalten ist.
	 * 
	 * @param dtm
	 *            Das DataTableModel zu dem die Views ermittelt werden sollen.
	 * @return Eine Liste mit den Views, in denen das TabellenModel enthalten
	 *         ist.
	 */
	public List<ViewModel> getViews(DataTableModel dtm);

	/**
	 * Signalisiert, ob das Diagramm als ge&auml;ndert markiert worden ist.
	 * 
	 * @return <TT>true</TT>, falls das Diagramm ge&auml;ndert worden ist, sonst
	 *         <TT>false</TT>.
	 */
	public boolean isAltered();

	/**
	 * Pr&uuml;ft, ob die Spaltennamen der durch einen Foreignkey referenzierten
	 * Spalten in der Diagrammansicht angezeigt werden soll.
	 * 
	 * @return <TT>true</TT>, wenn die durch Foreignkeys referenzierten Spalten
	 *         auch im Diagramm angezeigt werden sollen.
	 */
	public boolean isShowReferencedColumns();

	/**
	 * Pr&uuml;ft, ob die aufgehobenen Objekte in der Diagrammansicht
	 * ausgeblendet werden sollen.
	 * 
	 * @return <TT>true</TT>, wenn aufgehobene Objekte bei Anzeige und Druck
	 *         ausgeblendet werden sollen.
	 */
	public boolean isSuppressShowDeprecated();

	/**
	 * Liefert den aktuellen Status, ob Pflichtfelder in der Diagrammansicht
	 * visuell gekennzeichnet werden sollen oder nicht.
	 * 
	 * @return <TT>true</TT>, wenn Pflichtfelder in der Diagrammansicht
	 *         gekennzeichnet werden sollen, <TT>false</TT> sonst.
	 */
	public boolean markWriteablemembers();

	/** Markiert das Diagramm als ge&auml;ndert. */
	public void raiseAltered();

	/**
	 * L&ouml;scht den angegebenen DefaultComment aus der Liste der
	 * DefaultComments des Diagramms.
	 * 
	 * @param dcm
	 *            Der zu l&ouml;schende DefaultComment.
	 */
	public void removeDefaultComment(DefaultCommentModel dcm);

	/**
	 * Entfernt den angegebenen DataDiagramModelListener aus der Liste der an
	 * dem DataDiagramModel lauschenden Listener.
	 * 
	 * @param ddml
	 *            Der aus der Liste zu entfernende Listener.
	 */
	public void removeDataDiagramModelListener(DataDiagramModelListener ddml);

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
	 * @param dcm
	 *            Die Tabellenspalte, die dem Tabellenspaltencache des Modells
	 *            entfernt werden soll.
	 */
	public void removeFromFieldCache(DataColumnModel dcm);

	/**
	 * L&ouml;scht die angegebene Stereotype aus der Liste der Stereotypen des
	 * Diagramms.
	 * 
	 * @param t
	 *            Die anzul&ouml;schende Stereotype.
	 */
	public void removeStereotype(StereotypeModel t);

	/**
	 * L&ouml;scht das angegebene DataTableModel aus der Liste der
	 * DataTableModels des DataDiagramModels.
	 * 
	 * @param dtm
	 *            Das anzul&ouml;schende DataTableModel.
	 */
	public void removeTable(DataTableModel dtm);

	/**
	 * Setzt einen neuen Autor f&uuml;r das DataDiagramModel ein.
	 * 
	 * @param author
	 *            Der neue Autor zum DataDataDiagramModel.
	 */
	public void setAuthor(String author);

	/**
	 * Setzt den &uuml;bergebenen String als neuen Basispfad f&uuml;r die
	 * Codegenerierung ein.
	 * 
	 * @param path
	 *            Der neue Basispfad f&uuml;r die Codegenerierung.
	 */
	public void setBaseCodePath(String path);

	/**
	 * Setzt den &uuml;bergebenen String als neuen Namen f&uuml;r das
	 * Basis-Package ein, falls ein solcher gebraucht wird.
	 * 
	 * @param bpn
	 *            Der neue Name f&uuml;r das Basis-Package.
	 */
	public void setBasePackageName(String bpn);

	/**
	 * Setzt den &uuml;bergebenen String als Namen f&uuml;r die Klasse ein, die
	 * als CodeFactory instanziert werden soll.
	 * 
	 * @param cfcn
	 *            Der (qualifizierte) Name der Klasse, die als CodeFactory
	 *            instanziert werden soll.
	 */
	public void setCodeFactoryClassName(String cfcn);

	/**
	 * Setzt ein neues Datum zum DataDiagramModel. Hier kann z. B. ein Datum zur
	 * aktuellen Versionsangabe gesetzt werden.
	 * 
	 * @param date
	 *            Das neue Datum.
	 */
	public void setDate(PDate date);

	/**
	 * Setzt eine neue Schriftgr&ouml;szlig;e f&uuml;r die in der
	 * Diagrammansicht zu nutzende Schriftgr&ouml;&szlig;e f&uuml;r den
	 * Diagrammtitel (Namen).
	 * 
	 * @param fontsize
	 *            Die neue Schriftgr&ouml;szlig;e f&uuml;r die in der
	 *            Diagrammansicht zu nutzende Schriftgr&ouml;&szlig;e f&uuml;r
	 *            den Diagrammtitel (Namen).
	 */
	public void setFontSizeDiagramHeadline(int fontsize);

	/**
	 * Setzt eine neue Schriftgr&ouml;szlig;e f&uuml;r die in der
	 * Diagrammansicht zu nutzende Schriftgr&ouml;&szlig;e f&uuml;r die
	 * Versionsangabe.
	 * 
	 * @param fontsize
	 *            Die neue Schriftgr&ouml;szlig;e f&uuml;r die in der
	 *            Diagrammansicht zu nutzende Schriftgr&ouml;&szlig;e f&uuml;r
	 *            die Versionsangabe.
	 */
	public void setFontSizeSubtitles(int fontsize);

	/**
	 * Setzt eine neue Schriftgr&ouml;szlig;e f&uuml;r die in der
	 * Diagrammansicht zu nutzende Schriftgr&ouml;&szlig;e f&uuml;r die
	 * Tabelleninhalte.
	 * 
	 * @param fontsize
	 *            Die neue Schriftgr&ouml;szlig;e f&uuml;r die in der
	 *            Diagrammansicht zu nutzende Schriftgr&ouml;&szlig;e f&uuml;r
	 *            die Tabelleninhalte.
	 */
	public void setFontSizeTableContents(int fontsize);

	/**
	 * &Uuml;bernimmt den &uuml;bergebenen JDBCDataSourceRecord als neuen
	 * DataSourceRecord f&uuml;r den Import von Datenbank-Schemata.
	 * 
	 * @param dsr
	 *            Der zu &uuml;bernehmende JDBCDataSourceRecord.
	 */
	public void setImportDataSourceRecord(JDBCDataSourceRecord dsr);

	/**
	 * Setzt einen neuen Namen f&uuml;r das DataDiagramModel ein.
	 * 
	 * @param name
	 *            Der neue Name zum DataDiagramModel.
	 */
	public void setName(String name);

	/**
	 * Setzt einen neuen Status f&uuml;r die Anzeige der Spaltennamen der durch
	 * Foreignkeys referenzierten Felder. In der Ansicht wird bei gesetzter
	 * Flagge hinter dem Spaltenname der Referenz eine Angabe zur Zielspalte der
	 * Referenz gemacht.
	 * 
	 * @param showReferencedColumns
	 *            Dieser Parameter mu&szlig; mit dem Wert <TT>true</TT> versehen
	 *            werden, wenn die durch Foreignkeys referenzierten Spalten auch
	 *            in der Diagrammansicht angezeigt werden sollen.
	 */
	public void setShowReferencedColumns(boolean showReferencedColumns);

	/**
	 * Setzt bzw. L&ouml;scht die Flagge, die f&uuml;r das Ausblenden
	 * aufgehobener Objekte sorgt.
	 * 
	 * @param showDeprecated
	 *            Der neue Wert f&uuml;r die Flagge.
	 */
	public void setSuppressShowDeprecated(boolean showDeprecated);

	/**
	 * Setzt den Namen der alternativen Basisklasse f&uuml;r die Applikation.
	 * <P>
	 * Udschebtis sind vereinfachte Zwischenklassen die prinzipiell nur
	 * Accessoren und Mutatoren enthalten sollte und durch den Codewriter
	 * jederzeit &uuml;berschrieben werden k&ouml;nnen.
	 * 
	 * @param ubcn
	 *            Der Name der alternativen Basisklasse f&uuml;r die
	 *            Datenobjekte.
	 */
	public void setUdschebtiBaseClassName(String ubcn);

	/**
	 * &Uuml;bernimmt den &uuml;bergebenen JDBCDataSourceRecord als neuen
	 * DataSourceRecord f&uuml;r den Update von Datenbank-Schemata.
	 * 
	 * @param dsr
	 *            Der zu &uuml;bernehmende JDBCDataSourceRecord.
	 */
	public void setUpdateDataSourceRecord(JDBCDataSourceRecord dsr);

	/**
	 * Setzt eine neue Versionsangabe f&uuml;r das DataDiagramModel ein.
	 * 
	 * @param version
	 *            Die neue Versionsangabe
	 */
	public void setVersion(String version);

	/**
	 * Setzt einen Kommentar zur aktuellen Version des DataDiagramModels. Dieser
	 * Kommentar sollte zum Eintrag in eine Historie der aus dem Diagramm
	 * resultierenden Datenbanken geeignet sein (d. h. er sollte kurz und
	 * pr&aum;zise sein).
	 * 
	 * @param comment
	 *            Der Kurzkommentar zur Version.
	 */
	public void setVersionComment(String comment);

	/**
	 * Wandelt das DataDiagramModel in ein StructuredTextFile-Objekt um. In
	 * diesem Format werden die Archimedesdateien durch das Archimedes-Programm
	 * abgelegt.
	 * 
	 * @return Das in ein STF umgewandelte DataDiagramModel.
	 * @throws Exception
	 *             Hier&uuml;ber wird eine beim Umwandeln oder abspeichern
	 *             auftretende Exception an die aufrufende Softwareschicht
	 *             weitergeleitet.
	 */
	public StructuredTextFile toSTF() throws Exception;

	/**
	 * Wandelt das DataDiagramModel in einen XML-String um. Diese Methode ist
	 * zur alternativen Speicherung von Archimedesmodellen gedacht.
	 * 
	 * @return Das in einen XML-String umgewandelte DataDiagramModel.
	 */
	public String toXML();

}
