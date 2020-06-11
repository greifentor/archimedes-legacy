/*
 * DBFactoryController.java
 *
 * 24.02.2005
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db.xs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import corent.dates.PTimestamp;
import corent.db.OrderByDescriptor;
import corent.print.JasperReportable;
import net.sf.jasperreports.engine.*;

/**
 * Mit Hilfe dieses Interfaces werden Funktionalit&auml;ten f&uuml;r einen Controller (DBFC) festgelegt, der in der Lage
 * ist, mehrere unterschiedliche DBFactories zu klammern und ihre Funktion klassemanh&auml;ngig zur Verf&uuml;gung zu
 * stellen.
 *
 * @author O.Lieshoff
 *         <P>
 *
 * @changed OLI 27.08.2007 - Erweiterung um die Methoden zum Setzen und Lesen der Variablenspeichers
 *          (<TT>getControllerProperty(String)</TT> und <TT>setControllerProperty(String, 
 *             Object)</TT>). Zudem sind alle Methoden alphabetisch geordnet worden.
 *          <P>
 *          OLI 24.01.2008 - Erweiterung um die Methode <TT>getTransactionNumber()</TT>.
 *          <P>
 *          OLI 29.01.2009 - Erweiterung um die Methode <TT>getSelectionView(String, String, 
 *             Connection, boolean)</TT>. Daf&uuml;r ist die Methode <TT>getSelectionView(
 *             String, String, Connection)</TT> zur&uuml;ckgestellt worden.
 *          <P>
 *          OLI 27.03.2009 - Erweiterung um die Spezifikation der Methode <TT>resetConnection()</TT>.
 *          <P>
 *
 */

public interface DBFactoryController extends Remote {

	/** Bezeichner zur Typung von Events. */
	public enum MessageTyp {
		OBJECT_CHANGED, OBJECT_CREATED, OBJECT_REMOVED, OBJEKT_LOCKED, OBJEKT_UNLOCKED
	};

	/**
	 * H&auml;ngt den angegebenen DBFactoryControllerListener an die Liste der durch den DBFactoryController &uuml;ber
	 * Ereignisse zu informierenden Listener an.
	 *
	 * @param dbfcl Der an die Liste der Listener anzuh&auml;ngende DBFactoryControllerListener.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public void addDBFactoryControllerListener(DBFactoryControllerListener dbfcl) throws RemoteException;

	/**
	 * L&ouml;scht alle Sperren f&uuml;r die Objekte der angegebenen Klasse cls, ohne auf etwaige Benutzerrechte zu
	 * achten.
	 *
	 * @param cls Die Klasse, deren Objekt entsperrt werden soll. Wird hier der Wert <TT>null</TT> angegeben, so werden
	 *            alle Objekte entsperrt.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public void clearLocks(Class cls) throws RemoteException;

	/**
	 * Generiert eine neue Instanz zur angegeben Klasse.
	 *
	 * @param c Die Klasse zu der eine neue Instanz generiert werden soll.
	 * @return Die neue Instanz zur angegebenen Klasse.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public Object create(Class c) throws RemoteException;

	/**
	 * Generiert einen Filter zur Beschr&auml;nkung der Objekte einer Listenauswahl.
	 *
	 * @param c        Die Klasse zu der ein Filter generiert werden soll.
	 * @param criteria Die Kriterien, nach denen ausgefiltert werden soll.
	 * @return Ein Where-Klausel zur Filterung einer Liste von Datens&auml;tzen nach den angegebenen Kriterien.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public String createFilter(Class c, Object[] criteria) throws RemoteException;

	/**
	 * F&uuml;hrt eine Aktion zur angegeben Id durch und liefert ein Object mit einem eventuellen Ergebnis zur&uuml;ck.
	 * Hierdurch k&ouml;nnen Aktionen auf die Datenbank implementiert werden, die mit den obenstehenden Operationen
	 * nicht korrespondieren.
	 *
	 * @param c  Die Klasse, zu der Objekte die Aktion durchgef&uuml;hrt werden soll.
	 * @param id Die Id der Aktion, die ausgef&uuml;hrt werden soll. Sie wird von der jeweiligen Implementierung
	 *           festgelegt.
	 * @param p  Eine Liste mit Parametern zur Aktion.
	 * @return Ein eventuelles Ergebnis, oder <TT>null</TT>, wenn es kein solches geben sollte.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 * @throws SQLException    Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public Object doAction(Class c, int id, Object... p) throws RemoteException, SQLException;

	/**
	 * Dupliziert das angegeben Objekt in der Datenbank (und im Speicher).
	 *
	 * @param o Das Objekt, das dupliziert werden soll.
	 * @return Ein Duplikat des angegebenen Objektes, das allerdings eigene Datenbankschl&uuml;ssel besitzt (hierin
	 *         unterscheidet es sich von seiner Vorlage).
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 * @throws SQLException    Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public Object duplicate(Object o) throws RemoteException, SQLException;

	/**
	 * Sendet eine Nachricht an die Listener. In der Nachricht sind gegebenenfalls die Klasse eines durch die Nachricht
	 * betroffenen Objektes und dessen Schl&uuml;ssel zu finden, eine Angabe zum Benutzer, durch dessen Aktion die
	 * Nachricht ausgel&ouml;st worden ist und ein Typ, der die Nachricht typt.
	 *
	 * @param cls    Die Klasse, deren durch den Schl&uuml;ssel k das Objekt, zu dem die Nachricht gesendet worden ist,
	 *               identifiziert wird.
	 * @param k      Der Schl&uuml;ssel, zu dem Objekt, zu dem die Nachricht versendet werden soll.
	 * @param userid Die Kombination aus Login des Benutzers und Name des Rechners, von dem die Aktion, zu der die
	 *               Nachricht versendet werden soll, ausgef&uuml;hrt worden ist.
	 * @param mt     Eine Angabe zum Typ der Operation, der die Benachrichtigung der Listener erforderlich macht.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public void fireEvent(Class cls, Object k, String userid, DBFactoryController.MessageTyp mt) throws RemoteException;

	/**
	 * Generiert ein Objekt zur angegebenen Klasse in die Datenbank.
	 *
	 * @param c Die Klasse, zu der ein Objekt generiert werden soll.
	 * @return Das generierte Objekt zur angegebenen Klasse.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 * @throws SQLException    Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public Object generate(Class c) throws RemoteException, SQLException;

	/**
	 * Lie&szlig;t die durch den angegebenen Namen spezifizierte Controllerproperty aus und liefert den gespeicherten
	 * Wert.
	 *
	 * @param pn Der Name, unter dem die Controllerproperty gespeichert ist.
	 * @return Der Wert der Controllerproperty oder <TT>null</TT>, falls zu dem angegebenen Namen keine
	 *         Controllerproperty hinterlegt ist.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public Object getControllerProperty(String pn) throws RemoteException;

	/**
	 * @return Die Connection, mit der der DBFC arbeitet bzw. <TT>null</TT>, wenn der DBFC f&uuml;r jede Aktion eine
	 *         neue Connection generieren soll.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public Connection getHoldConnection() throws RemoteException;

	/**
	 * Liefert eine Id, die den DBFactoryController im Clusterbetrieb eindeutig identifiziert.
	 *
	 * @return Die Id des DBFactoryControllers.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public int getId() throws RemoteException;

	/**
	 * @return Eine Liste mit Datens&auml;tzen, die Angaben zu den gesperrten Objekten enthalten.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public Vector<DBFactoryControllerLock> getLocks() throws RemoteException;

	@Deprecated
	/**
	 * Liefert ein SelectionTableModel zum angegebenen Objekt.
	 *
	 * @param o Das Objekt, das als Basis f&uuml;r die Erstellung des SelectionViews dienen soll.
	 * @param w Eine einschr&auml;nkende Where-Klausel, die die Anzahl der Elemente im View verringert.
	 * @return Ein SelectionTableModel mit dem sich aus den &uuml;bergebenen Parametern ergebenden Daten.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 * @throws SQLException    Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 *
	 * @deprecated OLI 29.01.2009 - Auf Grund der Einf&uuml;hrung der Methode
	 *             <TT>getSelectionView(Object, String, boolean)</TT> zur&uuml;ckgesetzt.
	 *             <P>
	 *
	 */
	public SelectionTableModel getSelectionView(Object o, String w) throws RemoteException, SQLException;

	/**
	 * Liefert ein SelectionTableModel zum angegebenen Objekt.
	 *
	 * @param o               Das Objekt, das als Basis f&uuml;r die Erstellung des SelectionViews dienen soll.
	 * @param w               Eine einschr&auml;nkende Where-Klausel, die die Anzahl der Elemente im View verringert.
	 * @param suppressFilling Diese Flagge kann gesetzt werden, um den Datenbankenzugriff zu umgehen. Aus
	 *                        Performanzgr&uuml;nden kann es in bestimmten Konstellationen n&ouml;tig sein,
	 *                        zun&auml;chst eine leere Auswahltabelle zu erzeugen.
	 * @return Ein SelectionTableModel mit dem sich aus den &uuml;bergebenen Parametern ergebenden Daten.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 * @throws SQLException    Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public SelectionTableModel getSelectionView(Object o, String w, boolean suppressFilling)
			throws RemoteException, SQLException;

	/**
	 * Liefert die aktuelle Zeit des Computers auf dem der DBFactoryController l&auml;uft.
	 *
	 * @return Ein Zeitstempel mit der aktuellen Serverzeit.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public PTimestamp getServerTime() throws RemoteException;

	/**
	 * Liefert die n&auml;chste, freie Transaktionsnummer des DBFactoryControllers.
	 *
	 * @param type Eine Angabe zum Typ der angeforderten Transaktionsnummer.
	 * @return Die n&auml;chste, freie Transaktionsnummer des DBFactoryControllers bzw. <TT>-1</TT>, wenn der
	 *         DBFactoryController eine Transaktionsnummernvergabe nicht unterst&uuml;tzt.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 *
	 * @changed OLI 24.01.2008 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	public long getTransactionNumber(long type) throws RemoteException;

	/**
	 * @return <TT>true</TT>, falls die Connection &uuml;ber mehrere Anweisungen gehalten werden soll.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public boolean isHoldConnection() throws RemoteException;

	/**
	 * Liefert den Status der Zugriffszeitanzeige f&uuml;r Operationen des DBFactoryControllers.
	 *
	 * @return <TT>true</TT>, wenn der Controller die Zugriffszeiten f&uuml;r die einzelnen Aktionen anzeigt,
	 *         <TT>false</TT> sonst.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public boolean isShowAccesstimes() throws RemoteException;

	/**
	 * Pr&uuml;ft, ob das &uuml;bergebene Objekt in seiner gegenw&auml;rtigen Konstellation in der Datenbank einzigartig
	 * ist.
	 *
	 * @param o Das auf Einzigartigkeit zu pr&uuml;fende Objekt.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 * @throws SQLException    Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public boolean isUnique(Object o) throws RemoteException, SQLException;

	/**
	 * Sperrt das &uuml;ber die Klasse und den Key identifizierte Objekt f&uuml;r die angegebenen Benutzerkennung, falls
	 * das Objekt nicht bereits von einem anderen Benutzer gesperrt wird.
	 * <P>
	 * Ein gesperrtes Objekt wird nach einer durch die Property corent.db.xs.lock.time konfigurierten Anzahl von
	 * Millisekunden automatisch entsperrt, falls der Benutzerproze&szlig; die Sperre nicht verl&auml;ngert.
	 * <P>
	 * Die Verwaltung von Sperren funktioniert nur mit Objekten die das Interface HasKey implementieren.
	 *
	 * @param cls    Die Klasse, deren durch den Schl&uuml;ssel k identifiziertes Objekt gesperrt werden soll.
	 * @param k      Der Schl&uuml;ssel, zu dem die Sperre durchgef&uuml;hrt werden soll.
	 * @param userid Die Kombination aus Login des Benutzers und Name des Rechners, von dem aus der Datensatz mit einer
	 *               Sperre versehen werden soll.
	 * @return Die User-Login-Rechnernamen-Kombination des Benutzer, der den Datensatz gesperrt hat bzw. <TT>null</TT>
	 *         wenn die Sperre f&uuml;r den &uuml;bergebenen Benutzer eingerichtet worden ist.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public String lock(Class cls, Object k, String userid) throws RemoteException;

	/**
	 * Druckt das angegebene Objekt als JasperReport aus (sofern die notwendigen jasper-Dateien vorhanden sind). Die an
	 * die JasperReportables &uuml;bergebene Reportnumber ist 0.
	 *
	 * @param jr Das Objekt, das gedruckt werden soll.
	 * @return Der JasperPrint zum angegebenen JasperReportable.
	 * @throws JRException     Falls beim Erzeugen des Reports ein Problem diagnostiziert wird.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 * @throws SQLException    Falls beim Zugriff auf die Datenbank w&auml;hrend des Erstellens des Reports ein Fehler
	 *                         auftritt.
	 */
	public JasperPrint print(JasperReportable jr) throws JRException, RemoteException, SQLException;

	/**
	 * Druckt das angegebene Objekt als JasperReport aus (sofern die notwendigen jasper-Dateien vorhanden sind).
	 * <P>
	 * Die Methode f&uuml;gt dem JasperReportable einen Report-Parameter mit dem Namen "CSVTMPFILENAME" hinzu, in dem
	 * der tempor&auml;re Dateiname der CSV-Datei hinterlegt ist. Hat das JasperReportable bereits einen solchen
	 * Report-Parameter, so wird dieser &uuml;berschrieben.
	 *
	 * @param jr           Das Objekt, das gedruckt werden soll.
	 * @param reportnumber &Uuml;bergibt die angegebene Reportnumber an die Methoden des JasperReportable.
	 * @return Der JasperPrint zum angegebenen JasperReportable.
	 * @throws JRException     Falls beim Erzeugen des Reports ein Problem diagnostiziert wird.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 * @throws SQLException    Falls beim Zugriff auf die Datenbank w&auml;hrend des Erstellens des Reports ein Fehler
	 *                         auftritt.
	 */
	public JasperPrint print(JasperReportable jr, int reportnumber) throws JRException, RemoteException, SQLException;

	/**
	 * Liest einen Vector von Objekten der angegebenen Klasse aus der Datenbank, dessen Elemente durch die angegebene
	 * Where-Klausel eingeschr&auml;nkt sind.
	 *
	 * @param c Die Klasse, zu der Objekte aus der Datenbank gelesen werden sollen.
	 * @param w Die Where-Klausel, durch die die Liste der einzulesenden Objekte beschr&auml;nkt werden kann.
	 * @return Der Vector der mit den eingelesenen Objekten.
	 * @throws IllegalArgumentException Wenn zur angegebenen Klasse keine DBFactory gefunden werden kann.
	 * @throws RemoteException          Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                                  Client-/Server-Kommunikation auftritt.
	 * @throws SQLException             Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public Vector read(Class c, String w) throws IllegalArgumentException, RemoteException, SQLException;

	/**
	 * Liest einen Vector von Objekten der angegebenen Klasse aus der Datenbank, dessen Elemente durch die angegebene
	 * Where-Klausel eingeschr&auml;nkt sind.
	 *
	 * @param c Die Klasse, zu der Objekte aus der Datenbank gelesen werden sollen.
	 * @param w Die Where-Klausel, durch die die Liste der einzulesenden Objekte beschr&auml;nkt werden kann.
	 * @param o Ein Alternativer OrderByDescriptor, anhand dessen die Sortierung der Ausgabeliste vorgenommen wird.
	 * @return Der Vector der mit den eingelesenen Objekten.
	 * @throws IllegalArgumentException Wenn zur angegebenen Klasse keine DBFactory gefunden werden kann.
	 * @throws RemoteException          Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                                  Client-/Server-Kommunikation auftritt.
	 * @throws SQLException             Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public Vector read(Class c, String w, OrderByDescriptor o)
			throws IllegalArgumentException, RemoteException, SQLException;

	/**
	 * Liest einen Vector von Objekten der angegebenen Klasse aus der Datenbank, dessen Elemente durch die angegebene
	 * Where-Klausel eingeschr&auml;nkt sind.
	 *
	 * @param c  Die Klasse, zu der Objekte aus der Datenbank gelesen werden sollen.
	 * @param w  Die Where-Klausel, durch die die Liste der einzulesenden Objekte beschr&auml;nkt werden kann.
	 * @param o  Ein Alternativer OrderByDescriptor, anhand dessen die Sortierung der Ausgabeliste vorgenommen wird.
	 * @param sl Wenn diese Flagge gesetzt wird, wird bei eingeschaltetem Statementlogging statt des Statements nur eine
	 *           Meldung auf der Konsole ausgegeben, das das Logging f&uuml;r dieses Statement deaktiviert ist. Diese
	 *           Einstellung sollte bei der &Uuml;bertragung von Passworten Anwendung finden.
	 * @return Der Vector der mit den eingelesenen Objekten.
	 * @throws IllegalArgumentException Wenn zur angegebenen Klasse keine DBFactory gefunden werden kann.
	 * @throws RemoteException          Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                                  Client-/Server-Kommunikation auftritt.
	 * @throws SQLException             Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public Vector read(Class c, String w, OrderByDescriptor o, boolean sl)
			throws IllegalArgumentException, RemoteException, SQLException;

	/**
	 * Liest einen Vector von Objekten der angegebenen Klasse aus der Datenbank, dessen Elemente durch die angegebene
	 * Where-Klausel eingeschr&auml;nkt sind.
	 *
	 * @param c              Die Klasse, zu der Objekte aus der Datenbank gelesen werden sollen.
	 * @param w              Die Where-Klausel, durch die die Liste der einzulesenden Objekte beschr&auml;nkt werden
	 *                       kann.
	 * @param o              Ein Alternativer OrderByDescriptor, anhand dessen die Sortierung der Ausgabeliste
	 *                       vorgenommen wird.
	 * @param sl             Wenn diese Flagge gesetzt wird, wird bei eingeschaltetem Statementlogging statt des
	 *                       Statements nur eine Meldung auf der Konsole ausgegeben, das das Logging f&uuml;r dieses
	 *                       Statement deaktiviert ist. Diese Einstellung sollte bei der &Uuml;bertragung von Passworten
	 *                       Anwendung finden.
	 * @param includeRemoved Wird diese Flagge gesetzt, werden auch gel&ouml;schte Datens&auml;tze ber&uuml;cksichtigt,
	 *                       falls es sich bei der angegebenen Klasse um ein Deactivatable handelt.
	 * @return Der Vector der mit den eingelesenen Objekten.
	 * @throws IllegalArgumentException Wenn zur angegebenen Klasse keine DBFactory gefunden werden kann.
	 * @throws RemoteException          Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                                  Client-/Server-Kommunikation auftritt.
	 * @throws SQLException             Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public Vector read(Class c, String w, OrderByDescriptor o, boolean sl, boolean includeRemoved)
			throws IllegalArgumentException, RemoteException, SQLException;

	/**
	 * Liest das erste Objekt der angegebenen Klasse aus der Datenbank, auf das die angegebene Where-Klausel zutrifft.
	 *
	 * @param c Die Klasse, zu der ein Objekt aus der Datenbank gelesen werden sollen.
	 * @param w Die Where-Klausel, durch die die Liste der einzulesenden Objekte beschr&auml;nkt werden kann.
	 * @return Das erste Objekt der angegebenen Klasse, auf das die Where-Klausel zutrifft oder <TT>null</TT>.
	 * @throws IllegalArgumentException Wenn zur angegebenen Klasse keine DBFactory gefunden werden kann.
	 * @throws RemoteException          Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                                  Client-/Server-Kommunikation auftritt.
	 * @throws SQLException             Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public Object readFirst(Class c, String w) throws IllegalArgumentException, RemoteException, SQLException;

	/**
	 * L&ouml;scht das angegebene Objekt aus der Datenbank.
	 *
	 * @param o      Das aus der Datenbank zu l&ouml;schende Objekt.
	 * @param forced Diese Flagge ist zu setzen, wenn das Objekt in jedem Fall physisch aus der der Datenbank entfernt
	 *               werden soll (auch wenn es Deactivatable implementiert).
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 * @throws SQLException    Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public void remove(Object o, boolean forced) throws RemoteException, SQLException;

	/**
	 * L&ouml;scht eine Liste von Objekten aus der Datenbank.
	 *
	 * @param cls    Die Klasse, &uuml;ber deren DBFactory die Objekte aus der Datenbank werden sollen.
	 * @param k      Ein Vector mit den Schl&uuml;sselwerten, die aus der Datenbank entfernt werden sollen.
	 * @param forced Diese Flagge ist zu setzen, wenn das Objekt in jedem Fall physisch aus der der Datenbank entfernt
	 *               werden soll (auch wenn es Deactivatable implementiert).
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 * @throws SQLException    Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public void removeBatch(Class cls, Vector k, boolean forced) throws RemoteException, SQLException;

	/**
	 * L&ouml;scht den angegebenen DBFactoryControllerListener aus der Liste der durch den DBFactoryController &uuml;ber
	 * Ereignisse zu informierenden Listener.
	 *
	 * @param dbfcl Der an die Liste der Listener zul&ouml;schende DBFactoryControllerListener.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public void removeDBFactoryControllerListener(DBFactoryControllerListener dbfcl) throws RemoteException;

	/**
	 * L&ouml;scht das angegebene Lock aus der Liste der aktiven Locks. Diese Methode sollte nur in administrativen
	 * Kontexten genutzt werden!
	 *
	 * @param dbfcl Das aus der Liste zu l&ouml;schende Lock.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public void removeLock(DBFactoryControllerLock dbfcl) throws RemoteException;

	/**
	 * Setzt die gehaltene Connection zur&uuml;ck, falls der Controller in einem entsprechenden Modus l&auml;uft.
	 *
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 *
	 * @changed OLI 27.03.2009 - Hinzugef&uuml;gt
	 *          <P>
	 *
	 */
	public void resetConnection() throws RemoteException;

	/**
	 * &Uuml;ber diese Methode kann eine Controllerproperty unter dem angegebenen Namen mit dem ebenfalls angegebenen
	 * Wert gesetzt werden.
	 *
	 * @param pn Der Name der Controllerproperty, deren Wert gesetzt werden soll.
	 * @param v  Der Wert, mit dem die Controllerproperty unter dem angegebenen Namen belegt werden soll.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public void setControllerProperty(String pn, Object v) throws RemoteException;

	/**
	 * Wird dieser Methode der Wert <TT>true</TT> &uuml;bergeben, so wird eine einzelne Connection &uuml;ber alle
	 * Aktionen des DBFC gehalten. Andernfalls wird f&uuml;r jede Operation eine neue Connection erzeugt.
	 *
	 * @param holdConnection <TT>true</TT>, wenn eine Connection f&uuml;r alle Aktionen des DBFC gehalten werden soll,
	 *                       <TT>false</TT>, wenn f&uuml;r jede Aktion eine neue Connection erzeugt werden soll.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public void setHoldConnection(boolean holdConnection) throws RemoteException;

	/**
	 * Setzt den Status der Zugriffszeitanzeige f&uuml;r Operationen des DBFactoryControllers.
	 *
	 * @param b Wird dieser Parameter mit dem Wert <TT>true</TT>, so gibt der DBFactoryController Angaben zur
	 *          Verarbeitungszeit einzelner Operationen an.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public void setShowAccesstimes(boolean b) throws RemoteException;

	/**
	 * Entsperrt das &uuml;ber die Klasse und den Key identifizierte Objekt f&uuml;r die angegebenen Benutzerkennung,
	 * falls das Objekt bereit vom angegebenen Benutzer gesperrt worden ist.
	 * <P>
	 * Ein gesperrtes Objekt wird nach einer durch die Property corent.db.xs.lock.time konfigurierten Anzahl von
	 * Millisekunden automatisch entsperrt, falls der Benutzerproze&szlig; die Sperre nicht verl&auml;ngert.
	 * <P>
	 * Die Verwaltung von Sperren funktioniert nur mit Objekten die das Interface HasKey implementieren.
	 *
	 * @param cls    Die Klasse, deren durch den Schl&uuml;ssel k identifiziertes Objekt entsperrt werden soll.
	 * @param k      Der Schl&uuml;ssel, zu dem die Sperre aufgehoben werden soll.
	 * @param userid Die Kombination aus Login des Benutzers und Name des Rechners, von dem aus der Datensatz mit einer
	 *               Sperre versehen werden soll.
	 * @return <TT>true</TT> falls das Objekt f&uuml;r den angegebenen Benutzer entsperrt werden konnte, sonst
	 *         <TT>false</TT>.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 */
	public boolean unlock(Class cls, Object k, String userid) throws RemoteException;

	/**
	 * Schreibt das angegebene Objekt in die Datenbank.
	 *
	 * @param o Das in die Datenbank zu schreibende Objekt.
	 * @return Das geschriebene Objekt.
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 * @throws SQLException    Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 *
	 * @changed OLI 16.07.2008 - Anpassung an die aktualisierte Schnittstellendefinition des Interfaces
	 *          <TT>DBFactory</TT>.
	 *          <P>
	 *
	 */
	public Object write(Object o) throws RemoteException, SQLException;

	/**
	 * Schreibt das angegebene Objekt in die Datenbank.
	 *
	 * @param cls Die Klasse, &uuml;ber deren DBFactory die Stapel&auml;nderung durchgef&uuml;hrt werden soll.
	 * @param k   Ein Vector mit den Schl&uuml;sselwerten, f&uuml;r die die Stapel&auml;nderung durchgef&uuml;hrt werden
	 *            soll.
	 * @param ta  Die Felder, die bei der mit der DBFactory verbundenen Tabelle zu mit den dazugeh&ouml;rigen Werten
	 *            aktualisiert werden sollen (Hashtable&lt;Integer, Object&gt; - AttributeId &amp; Wert).
	 * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem bei der
	 *                         Client-/Server-Kommunikation auftritt.
	 * @throws SQLException    Falls beim Zugriff auf die Datenbank ein Fehler erkannt wird.
	 */
	public void writeBatch(Class cls, Vector k, Hashtable<Integer, Object> ta) throws RemoteException, SQLException;

}
