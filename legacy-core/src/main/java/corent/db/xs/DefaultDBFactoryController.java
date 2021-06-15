/*
 * DefaultDBFactoryController.java
 *
 * 24.02.2005
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db.xs;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import corent.dates.LongPTimestamp;
import corent.dates.PTimestamp;
import corent.dates.TimestampUnit;
import corent.db.ConnectionManager;
import corent.db.DBExec;
import corent.db.DBType;
import corent.db.DBUtil;
import corent.db.JDBCDataSourceRecord;
import corent.db.OrderByDescriptor;
import corent.print.Archivable;
import corent.util.SysUtil;
import corentx.ds.Lockable;
import logging.Logger;

/**
 * Diese Musterimplementierung eines DBFactoryControllers vermittelt einen &Uuml;berblick &uuml;ber die Arbeitsweise
 * einer solchen Klasse und l&auml;&szlig;t sich in den meisten Standardsituationen einsetzen.
 * <P>
 * F&uuml;r jeden DDBFC (DefaultDBFactoryController) kann explizit eingestellt werden, ob er mit einer einzigen
 * Connection arbeiten soll, die zwischen den Aufrufen gehalten werden soll, oder ob er bei jedem Aufruf eine neue
 * Connection anfordern soll. Bei Datenbankoperationen, die &uuml;ber als Transaktionen zusammengefa&szlig;t werden
 * sollen oder die massiv auf die Datenbank zugreifen, empfiehlt es sich diese Konfiguration zu nutzen. Der Standardwert
 * wird &uuml;ber die Property <TT>corent.db.xs.DefaultDBFactoryController.holdConnection</TT> gesetzt.
 * <P>
 * Der Lock-Mechanismus kann entweder &uuml;ber eine speicherinterne Tabelle im DBFactoryController, oder &uuml;ber eine
 * Datenbanktabelle genutzt werden. Der erste Weg funktioniert nur, solange der Controller nicht im Cluster l&auml;uft.
 * In diesem Fall mu&szlig; auf jeden Fall die Variante mit der Datenbanktabelle genutzt werden. Sie wird durch das
 * Setzen der Property <TT>corent.db.xs.DefaultDBFactoryController.locksByDatabasetable</TT> aktiviert.
 * <P>
 * Zur Abbildung der Locks in einer Datenbanktabelle ist die Erzeugung einer speziellen Tabelle n&ouml;tig. Das folgende
 * <TT>CREATE</TT>-Statement zeigt die Tabelle und ihre Felder mit der Default-Namensgebung:
 * 
 * <PRE>
 * create table LockObject (
 *     LockObject varchar(255) primary key,
 *     UserId varchar(255) primary key,
 *     LastRequest numeric(14, 0)
 * );
 * </PRE>
 * 
 * Die Namen der Tabelle und ihrer Spalten k&ouml;nnen &uuml;ber die folgenden Properties angepa&szlig;t werden:
 * <TT>corent.db.xs.DefaultDBFactoryController.locks.tablename</TT>,
 * <TT>corent.db.xs.DefaultDBFactoryController.locks.column.LockObject</TT>,
 * <TT>corent.db.xs.DefaultDBFactoryController.locks.column.LastRequest</TT> und
 * <TT>corent.db.xs.DefaultDBFactoryController.locks.column.UserId</TT>.
 * <P>
 * Wird die Property <TT>corent.db.xs.DefaultDBFactoryController.debug</TT> auf den Wert <TT>true</TT> gesetzt, wird an
 * einigen Stellen der ausgef&uuml;hrten Methoden zus&auml;tzlicher Output generiert.
 * <P>
 * Die Property <TT>corent.db.xs.DefaultDBFactoryController.showAccesstimes</TT> stellt einen Defaultwert f&uuml;r die
 * Eigenschaft <I>showAccesstimes</I> zur Verf&uuml;gung.
 *
 * Die folgenden Properties dienen der Steuerung des Archiv-Drucks im Zusammenhang mit Objekten, die das Interface
 * <TT>Archivable</TT> implementieren:<BR>
 * <TT>corent.db.xs.DefaultDBFactoryController.archive.path</TT> (String, Default "./") definiert einen Pfad, in den die
 * Archiv-Exporte erfolgen sollen.<BR>
 * <TT>corent.db.xs.DefaultDBFactoryController.archive.timestamp</TT> (Boolean) kann gesetzt werden, um den Dateinamen
 * um eine millisekunden genauen Zeitstempel zu erweitern. Auf diese Weise k&ouml;nnen historische Zust&auml;nde
 * archiviert werden.<BR>
 * <TT>corent.db.xs.DefaultDBFactoryController.archive.extension</TT> (String, Default ".html") kann zur Definition
 * einer alternativen Dateinamenserweiterung der Archivdateien gesetzt werden.<BR>
 * <TT>corent.db.xs.DefaultDBFactoryController.archive.synchon</TT> (Boolean) kann gesetzt werden, um das Erzeugen der
 * Archiv-Exporte synchron ablaufen zu lassen. Hierbei kann es allerdings zu Wartezeiten beim Speichern kommen.
 *
 * <P>
 * Die Methoden zum Setzen und Lesen der <B>Controllerproperties</B> sind zur Realisation ansynchroner Abl&auml;fe
 * gedacht. Hinter den Zugriffsmethoden steht kein echtes Property-Objekt. Es handelt sich auch nicht um die
 * System.properties. Vielmehr sind die Controllerproperties ein abgeschlossener Variablenbereich, &uuml;ber den der
 * Client mit dem Server relativ frei kommunizieren kann.<BR>
 * Asynchrone Anwendungen k&ouml;nnen beispielsweise dadurch erzeugt werden, da&szlig; bestimmte Methoden der an den
 * Controller gebundenen DBFactory-Implementierungen als Threads programmiert sind und beispielsweise ihren Fortschritt
 * regelm&auml;&szlig;ig in eine Controllerproperty hinterlegen. So w&auml;re ein Client in der Lage den aktuellen
 * Fortschritt seinerseits z. B. in einer Anzeige zu aktualisieren.
 *
 * <P>
 * Mit Hilfe der Property
 * <I>corent.db.xs.DefaultDBFactoryController.CreateFilter.convert.to.str.[Tablename].[Spaltenname]</I> kann eine
 * Konvertierung von numerischen Felder in den Typ VARCHAR bei der Bildung des Filters zur Auswahl von Datens&auml;tzen
 * erzwungen werden. Dies ist z. B. bei MYSQL 5 notwendig.
 *
 * Die Zeichensatzkodierung beim Drucken von CSV-Dateien l&auml;&szlig;t sich mit Hilfe der Property
 * <I>corent.db.xs.DefaultDBFactoryController.csv.encoding</I> konfigurieren. Als Default ist die "ISO-8859-1"
 * eingestellt.
 *
 * <P>
 * <B>Hinweis:</B> Die Geschichte mit dem DBFactoryControllerListener ist noch nicht in Funktion.
 *
 * @author O.Lieshoff
 *         <P>
 *
 * @changed OLI 22.08.2007 - Erweiterung um die Archivierungsfunktionalit&auml;t. Die <TT>write</TT>-Methode reagiert
 *          nun auf Implementierungen des Interfaces <TT>Archivable</TT>. Entsprechende Objekte werden gegebenenfalls in
 *          HTML exportiert (analog zum Reportdruck). Zur serverseitigen Steuerung des Exports gibt es einen Satz neuer
 *          Properties.
 *          <P>
 *          OLI 27.08.2007 - Erweiterung um die Controllerproperties und Implementierung der Zugriffsmethoden
 *          <TT>getControllerProperty(String)</TT> und <TT>setControllerProperty(String, Object)</TT> aus dem Interface
 *          <TT>DBFactoryController</TT>.
 *          <P>
 *          OLI 04.11.2007 - Erweiterung um das Zusammenspiel mit der Erweiterung des Interfaces
 *          <TT>JasperReportable</TT> um die Methode <TT>isSaveBeforePrintingRequired()</TT>.
 *          <P>
 *          OLI 24.01.2008 - Erweiterung um die Implementierung der Methode <TT>getTransactionNumber()</TT>.
 *          <P>
 *          OLI 06.05.2008 - Erweiterung der Filterbildung um eine erzwungene Umwandlung von numerischen Werten nach
 *          VARCHAR.
 *          <P>
 *          OLI 16.07.2008 - &Auml;nderungen an der <TT>write</TT>-Methode im Rahmen der &Auml;nderung des Interfaces
 *          <TT>DBFactory</TT>
 *          <P>
 *          OLI 18.09.2008 - Anpassung an die M&ouml;glichkeit zur Unterdr&uuml;ckung von UserNotifications seitens des
 *          Interfaces <TT>UserChangesNoticeable</TT>.
 *          <P>
 *          OLI 30.09.2008 - Einbau einer M&ouml;glichkeit die tempor&auml;re Datei beim Drucken einer CSV-Datei mit
 *          einer Zeichencodierung zu belegen.
 *          <P>
 *          OLI 29.01.2009 - Erweiterung um die Methode <TT>getSelectionView(String, String, 
 *             Connection, boolean)</TT>. Daf&uuml;r ist die Methode <TT>getSelectionView(
 *             String, String, Connection)</TT> zur&uuml;ckgestellt worden.
 *          <P>
 *          OLI 27.03.2009 - Erweiterung um die Implementierung der Methode <TT>resetConnection()</TT>.
 *          <P>
 *
 */

public class DefaultDBFactoryController extends UnicastRemoteObject implements DBFactoryController, Runnable {

	private static final Logger log = Logger.getLogger(DefaultDBFactoryController.class);

	/* Counter zur Identifikation der Operationen. */
	private static long OpCounter = 0;

	/*
	 * Ist diese Flagge gesetzt, wird eine Connections &uml;ber die Lebensdauer des DBFC gehalten. Andernfalls wird bei
	 * jedem Aufruf eine neue Connection erstellt.
	 */
	private boolean holdConnection = Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.holdConnection");
	/*
	 * Ist diese Flagge gesetzt, wenn zu jeder Operation eine Information &uuml;ber deren Zeitverbrauch auf der Konsole
	 * ausgegeben werden soll.
	 */
	private boolean showAccesstimes = Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.showAccesstimes");
	/* Die Connection, auf der im Falle zu haltender Connections gearbeitet wird. */
	private Connection connection = null;
	/* Die Daten zur Datenquelle. */
	private JDBCDataSourceRecord dsr = null;
	/* Die Tabelle mit den Controllerproperties. */
	private Hashtable controllerproperty = new Hashtable<String, Object>();
	/* Die Tabelle mit den nach Klassen geschl&uuml;sselten DBFactories. */
	private Hashtable<Class, DBFactory> factories = new Hashtable<Class, DBFactory>();
	/* Die Tabelle mit den Locks f&uuml;r identifizierbare Objekte. */
	private Hashtable<String, DBFactoryControllerLock> locks = new Hashtable<String, DBFactoryControllerLock>();
	/* Die Id des DBFactoryControllers (z. B. zur Verwendung im Clusterbetrieb. */
	private int id = 0;
	/*
	 * Der Thread, &uuml;ber den die Locks in regelm&auml;&szlig;igen Abst&auml;nden bereinigt werden.
	 */
	private Thread th = null;

	/**
	 * Generiert einen neuen Controller anhand des &uuml;bergebenen JDBCDataSourceRecord.
	 *
	 * @param dsr       Der JDBCDataSourceRecord mit den Daten zur Verbindung mit der Datenbank.
	 * @param factories Eine Tabelle mit Klassen-DBFactory-Tupeln, zum Zugriff auf die Datenbank.
	 * @param id        Eine Id, falls der DBFactoryController im Clusterbetrieb gefahren werden kann.
	 * @throws RemoteException ?!?
	 */
	public DefaultDBFactoryController(JDBCDataSourceRecord dsr, Hashtable<Class, DBFactory> factories, int id)
			throws RemoteException {
		this(dsr, factories);
		this.id = id;
	}

	/**
	 * Generiert einen neuen Controller anhand des &uuml;bergebenen JDBCDataSourceRecord.
	 *
	 * @param dsr       Der JDBCDataSourceRecord mit den Daten zur Verbindung mit der Datenbank.
	 * @param factories Eine Tabelle mit Klassen-DBFactory-Tupeln, zum Zugriff auf die Datenbank.
	 * @throws RemoteException ?!?
	 */
	public DefaultDBFactoryController(JDBCDataSourceRecord dsr, Hashtable<Class, DBFactory> factories)
			throws RemoteException {
		super();
		this.dsr = dsr;
		this.factories = factories;
		this.th = new Thread(this);
		this.th.start();
	}

	private Connection getConnection() throws SQLException {
		Connection c = this.connection;
		try {
			if ((this.connection == null) && this.isHoldConnection()) {
				this.connection = ConnectionManager.GetConnection(this.dsr);
			} else if ((this.connection != null) && !this.isHoldConnection()) {
				this.connection = null;
			}
			c = this.connection;
			if (c == null) {
				c = ConnectionManager.GetConnection(this.dsr);
			}
		} catch (RemoteException re) {
			re.printStackTrace();
		}
		return c;
	}

	/* Statische Methoden. */

	/**
	 * Diese Methode generiert einen Standard-Suchfilter zur Nutzung mit den Selection-Djinns.
	 *
	 * @param cols     Liste mit den Namen der Tabellenspalten, die zur Selektion herangezogen werden sollen.
	 * @param criteria Eine Liste mit den Kriterien, nach denen die Tabellenspalten durchsucht werden sollen.
	 */
	public static String CreateFilter(String[] cols, Object[] criteria) {
		Vector<String> vs = new Vector<String>();
		for (int i = 0; i < cols.length; i++) {
			vs.addElement(cols[i]);
		}
		return CreateFilter(vs, criteria);
	}

	/**
	 * Diese Methode generiert einen Standard-Suchfilter zur Nutzung mit den Selection-Djinns.
	 * <P>
	 * Ist dem Spaltennamen ein Asterix vorangestellt, so wird die Spalte als kodiert behandelt.
	 * <P>
	 * Beginnt die der erste Eintrag in der criteria-Liste mit dem Pr&auml;fix <I>"$SQL:" </I>, so wird sie nicht weiter
	 * &uuml;ber den PersistenceDescriptor manipuliert, sondern direkt an das DBMS durchgeleitet. Nach dem Pr&auml;fix
	 * sind eine Where-Klausel (ohne das Schl&uuml;sselwort "where") und/oder eine order-by-Angabe (mit
	 * Schl&uuml;sselwort "order by") erlaubt. Die einzelnen Komponenten der Klauseln k&ouml;nnen auf ein oder mehrere
	 * Eintr&auml;ge in der criteria-Liste verteilt sein.
	 *
	 * @param cols     Liste mit den Namen der Tabellenspalten, die zur Selektion herangezogen werden sollen.
	 * @param criteria Eine Liste mit den Kriterien, nach denen die Tabellenspalten durchsucht werden sollen.
	 *
	 * @changed OLI 06.05.2008 - Erweiterung um die M&ouml;glichkeit &uuml;ber die Property
	 *          <I>corent.db.xs.DefaultDBFactoryController.CreateFilter.convert.to.str...</I> eine Konvertierung von
	 *          numerischen Felder in den Typ VARCHAR bei der Bildung des Filters zu erzwingen.
	 *          <P>
	 *          OLI 27.04.2009 - Erweiterung um eine M&ouml;glichkeit die Typkonvertierung nach <TT>VARCHAR</TT>
	 *          pauschal f&uuml;r alle Felder durchf&uuml;hren zu lassen.
	 *          <P>
	 *
	 */
	private static String CreateFilter(Vector<String> cols, Object[] criteria) {
		boolean suppresslc = Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.suppress.lower.on.select");
		if ((criteria == null) || (criteria.length == 0)) {
			return "";
		}
		String s = criteria[0].toString();
		if (s.startsWith("$SQL:")) {
			s = "";
			for (int i = 0; i < criteria.length; i++) {
				s = s.concat(criteria[i].toString()).concat(" ");
			}
			return s.substring(0, s.length() - 1);
		}
		Vector<String> vs = new Vector<String>();
		s = "";
		for (int i = cols.size() - 1; i >= 0; i--) {
			s = cols.elementAt(i);
			if (s.startsWith("*")) {
				cols.removeElement(s);
				s = s.substring(1, s.length());
				vs.addElement(s);
			}
		}
		for (int i = cols.size() - 1; i >= 0; i--) {
			s = cols.elementAt(i);
			if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.CreateFilter.convert.to.str." + s)
					|| Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.CreateFilter.convert.to.str.all")) {
				cols.removeElement(s);
				cols.addElement(DBExec.Convert(DBType.VARCHAR, s, 0, 0));
			}
		}
		s = "";
		if (cols.size() > 0) {
			String c = DBExec.Concatenation(cols, ";");
			if (!suppresslc) {
				c = DBExec.LowerCase(c);
			}
			for (int i = 0; i < criteria.length; i++) {
				if (s.length() > 0) {
					s += " and ";
				}
				s += "(" + c + " like "
						+ (!suppresslc
								? DBExec.LowerCase("'%" + criteria[i].toString() + "%'")
								: "'%" + criteria[i].toString() + "%'")
						+ ")";
			}
		}
		if ((DBFactoryUtil.CODER != null) && (vs.size() > 0)) {
			String encode = "";
			String s0 = s;
			s = "";
			String c = DBExec.Concatenation(vs, ";");
			if (!suppresslc) {
				c = DBExec.LowerCase(c);
			}
			for (int i = 0; i < criteria.length; i++) {
				if (s.length() > 0) {
					s += " and ";
				}
				encode = DBFactoryUtil.CODER.encode(criteria[i].toString());
				encode = encode.substring(1, encode.length());
				s += "(" + c + " like " + DBExec.LowerCase("'%" + encode + "%'") + ")";
			}
			if (s.length() + s0.length() > 0) {
				s = "(" + s0 + (s0.length() > 0 ? " or " : "") + s + ")";
			}
		}
		// s = "(" + s + ")";
		return s;
	}

	/**
	 * Diese Klasse ist ein einfacher Container f&uuml;r die Daten einer Zugriffszeitnahme.
	 */
	private class AccesstimeRecord {
		public long opid = 0;
		public long time = 0;
		public String clsname = "UNKNOWN";
		public String opname = "";

		public AccesstimeRecord(long time, long opid, Class cls, String opname) {
			super();
			if (cls != null) {
				this.clsname = cls.getName();
			}
			this.opid = opid;
			this.opname = opname;
			this.time = time;
		}
	}

	/**
	 * Erzeugt eine Konsolenausgabe &uuml;ber den Start der angegebenen Operation und liefert die zur Zeitnahme
	 * erforderlichen Daten.
	 *
	 * @param cls    Die Klasse, auf die sich die Operation bezieht.
	 * @param opname Der Name der Operation. Hier bietet sich der Name der Methode an, innerhalb derer die Zeitnahme
	 *               durchgef&uuml;hrt wird.
	 * @return Ein Datensatz mit den f&uuml;r die Zeitnahme erforderlichen Daten.
	 */
	private AccesstimeRecord startOperation(Class cls, String opname) {
		long time = System.currentTimeMillis();
		long opid = OpCounter++;
		AccesstimeRecord atr = new AccesstimeRecord(time, opid, cls, opname);
		log.debug("OP#:" + atr.opid + ", TYPE:" + atr.opname + ", CLASS:" + atr.clsname + ", STATE:started;");
		return atr;
	}

	/**
	 * Erzeugt eine auswertende Meldung zum Zeitnahmedatensatz auf der Konsole.
	 *
	 * @param atr Der Datensatz mit den f&uuml;r die Zeitnahme erforderlichen Daten.
	 * @return Das Ergebnis der Zeitnahme in Millisekunden.
	 */
	private long stopOperation(AccesstimeRecord atr) {
		long diff = System.currentTimeMillis() - atr.time;
		log.debug("OP#:" + atr.opid + ", TYPE:" + atr.opname + ", STATE:stopped, " + "TIME:" + diff + "ms");
		return diff;
	}

	/* Implementierung des Interfaces DBFactoryController. */

	@Override
	public Object create(Class c) throws RemoteException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(c, "create(Class)");
		}
		DBFactory dbf = this.factories.get(c);
		if (dbf != null) {
			Object o = dbf.create();
			if (atr != null) {
				this.stopOperation(atr);
			}
			return o;
		}
		throw new IllegalArgumentException(
				"DBFactory for class " + c.getName() + " not found " + "in DefaultDBFactoryController.create(Class)!");
	}

	@Override
	public String createFilter(Class c, Object[] criteria) throws RemoteException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(c, "createFilter(Class, Object[])");
		}
		DBFactory dbf = this.factories.get(c);
		if (dbf != null) {
			Vector<String> vs = new Vector<String>();
			if ((criteria != null) && (criteria.length > 0)) {
				String s = criteria[0].toString();
				if (s.length() > 0) {
					s = s.concat(" ");
				}
				while (s.indexOf(" ") > -1) {
					vs.addElement(s.substring(0, s.indexOf(" ")));
					s = s.substring(s.indexOf(" ") + 1, s.length());
				}
			}
			String res = dbf.createFilter(vs.toArray(new String[] {}));
			if (atr != null) {
				this.stopOperation(atr);
			}
			return res;
		}
		throw new IllegalArgumentException(
				"DBFactory for class " + c.getName() + " not found "
						+ "in DefaultDBFactoryController.createFilter(Class, Object[])!");
	}

	/**
	 * @changed OLI 27.08.2007 - Implementierung der Methoden hinzugef&uuml;gt.
	 */
	@Override
	public Object getControllerProperty(String pn) throws RemoteException {
		return this.controllerproperty.get(pn);
	}

	@Override
	public Vector read(Class c, String w) throws IllegalArgumentException, RemoteException, SQLException {
		return this.read(c, w, null, false, false);
	}

	@Override
	public Vector read(Class c, String w, OrderByDescriptor o)
			throws IllegalArgumentException, RemoteException, SQLException {
		return this.read(c, w, o, false, false);
	}

	@Override
	public Vector read(Class c, String w, OrderByDescriptor o, boolean sl)
			throws IllegalArgumentException, RemoteException, SQLException {
		return this.read(c, w, o, sl, false);
	}

	/**
	 * @changed OLI 28.03.2008 - Ich habe den Block, in dem der eigentliche Lesezugriff stattfindet, synchronisiert. Im
	 *          Kampfeinsatz (SPMi) kommt es immer wieder vor, da&szlig; durch eine unvorteilhafte Verzahnung die
	 *          ShowStatements-Property der statischen DBExec-Instanz falsch gesetzt wird. Dies bleibt dann auch
	 *          w&auml;hrend des folgenden Programmablaufes so.
	 */
	@Override
	public Vector read(Class c, String w, OrderByDescriptor o, boolean sl, boolean includeRemoved)
			throws IllegalArgumentException, RemoteException, SQLException {
		AccesstimeRecord atr = null;
		Connection con = null;
		DBFactory dbf = null;
		Vector v = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(c, "read(Class, String, OrderByDescriptor, boolean)");
		}
		con = this.getConnection();
		dbf = this.factories.get(c);
		if (dbf != null) {
			synchronized (this) {
				// Das muss hier bleiben: Nebenlaefigkeit!
				boolean b = DBExec.IsShowStatements();
				if (sl) {
					DBExec.SetShowStatements(false);
					DBExec.PrintMessage("statement printing suppressed by " + "DefaultDBFactoryController!");
				}
				v = dbf.read(w, con, o, includeRemoved);
				DBExec.SetShowStatements(b);
				if (atr != null) {
					this.stopOperation(atr);
				}
			}
			return v;
		}
		throw new IllegalArgumentException(
				"DBFactory for class " + c.getName() + " not found "
						+ "in DefaultDBFactoryController.read(Class, String)!");
	}

	@Override
	public Object readFirst(Class c, String w) throws IllegalArgumentException, RemoteException, SQLException {
// Hier sollte noch was mit Limits gemacht werden !!!                
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(c, "readFirst(Class, String)");
		}
		Vector v = this.read(c, w);
		if (v.size() > 0) {
			if (atr != null) {
				this.stopOperation(atr);
			}
			return v.elementAt(0);
		}
		if (atr != null) {
			this.stopOperation(atr);
		}
		return null;
	}

	/**
	 * @changed OLI 18.09.2008 - Einbau der Pr&uuml;fung auf <TT>SuppressUserNotification</TT>.
	 *          <P>
	 */
	@Override
	public Object write(Object o) throws RemoteException, SQLException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation((o != null ? o.getClass() : null), "write(Object)");
		}
		Connection c = this.getConnection();
		DBFactory dbf = this.factories.get(o.getClass());
		if (o instanceof RemoteDBMember) {
			((RemoteDBMember) o).objectBeforeWrite();
		}
		if ((o instanceof UserChangesNoticeable)
				&& !Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.mode.rmi")) {
			if (!((UserChangesNoticeable) o).isSuppressUserNotification()) {
				((UserChangesNoticeable) o)
						.setModificationUser(Long.getLong("corent.db.xs.RemoteDBFactoryController.user.id", -1));
				((UserChangesNoticeable) o).setPCName(SysUtil.GetHostname());
			}
		}
		if (dbf != null) {
			o = dbf.write(o, c);
			if (dbf instanceof Cached) {
				((Cached) dbf).writeDetected(o, false, this);
			}
			if ((o instanceof CacheNotifier)
					&& Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.cache.notification")) {
				/*
				 * Table varchar(255), Id varchar(255), Changer varchar(255), ChangeDate numeric(14,0), Mode varchar(5)
				 * -- 'U' updated, 'R' removed.
				 */
				try {
					CacheNotifier cn = (CacheNotifier) o;
					String ct = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "Table",
									"Table");
					String cid = System
							.getProperty("corent.db.xs.DefaultDBFactoryController.changenotifier.column.Id", "Id");
					String cchng = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "Changer",
									"Changer");
					String cdt = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "ChangeDate",
									"ChangeDate");
					String cmd = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "Mode",
									"Mode");
					String tn = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.tablename",
									"CacheNotification");
					int count = DBExec
							.Update(
									c,
									"update " + tn + " set " + cchng + "="
											+ DBUtil.DBString(cn.getPCName() + "-" + cn.getModificationUser()) + ", "
											+ cdt + "=" + new PTimestamp().toLong() + ", " + cmd + "='U'" + " where "
											+ ct + "=" + DBUtil.DBString(cn.getTablename()) + " and " + cid + "="
											+ DBUtil.DBString(cn.getIdValue()));
					if (count == 0) {
						DBExec
								.Update(
										c,
										"insert into " + tn + " (" + ct + ", " + cid + ", " + cchng + ", " + cdt + ", "
												+ cmd + ") values (" + DBUtil.DBString(cn.getTablename()) + ", "
												+ DBUtil.DBString(cn.getIdValue()) + ", "
												+ DBUtil.DBString(cn.getPCName() + "-" + cn.getModificationUser())
												+ ", " + new PTimestamp().toLong() + ", 'U')");
					}
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			}
			if (o instanceof Archivable) {
				Archivable arc = (Archivable) o;
				if (arc.getArchiveFilename() != null) {
					String outfn = System
							.getProperty("corent.db.xs.DefaultDBFactoryController.archive.path", "./")
							.replace("\\", "/") + arc.getArchiveFilename();
					if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.archive.timestamp")) {
						outfn = outfn.concat("-" + (new LongPTimestamp().toLong()));
					}
					outfn = outfn
							.concat(
									System
											.getProperty(
													"corent.db.xs.DefaultDBFactoryController.archive.extension."
															+ arc.getArchiveMode().toString(),
													".html"));
					if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.archive.synchon")) {
						log.warn("Archive file " + outfn + " is not created. Feature is deactivated!!!");
					} else {
						log.warn("Archive file " + outfn + " is not created (asynchronous). Feature is deactivated!!!");
					}
				}
			}
			if (atr != null) {
				this.stopOperation(atr);
			}
			return o;
		}
		throw new IllegalArgumentException(
				"DBFactory for class " + o.getClass().getName()
						+ " not found in DefaultDBFactoryController.write(Object)!");
	}

	@Override
	public void writeBatch(Class cls, Vector k, Hashtable<Integer, Object> ta) throws RemoteException, SQLException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(cls, "writeBatch(Class, Vector, Hashtable<Integer,Object>" + ")");
		}
		Connection c = this.getConnection();
		DBFactory dbf = this.factories.get(cls);
		if (dbf != null) {
			dbf.writeBatch(k, ta, c);
			if (dbf instanceof Cached) {
				((Cached) dbf).writeDetected(null, true, this);
			}
			if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.cache.notification")) {
				try {
					String ct = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "Table",
									"Table");
					String cid = System
							.getProperty("corent.db.xs.DefaultDBFactoryController.changenotifier.column.Id", "Id");
					String cchng = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "Changer",
									"Changer");
					String cdt = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "ChangeDate",
									"ChangeDate");
					String cmd = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "Mode",
									"Mode");
					String tn = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.tablename",
									"CacheNotification");
					int count = DBExec
							.Update(
									c,
									"update " + tn + " set " + cchng + "='*'" + ", " + cdt + "="
											+ new PTimestamp().toLong() + ", " + cmd + "='U' where " + ct + "="
											+ cls.getName() + " and " + cid + "='*'");
					if (count == 0) {
						DBExec
								.Update(
										c,
										"insert into " + tn + " (" + ct + ", " + cid + ", " + cchng + ", " + cdt + ", "
												+ cmd + ") values (" + cls.getName() + ", '*', '*', "
												+ new PTimestamp().toLong() + ", 'U')");
					}
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			}
			if (atr != null) {
				this.stopOperation(atr);
			}
			return;
		}
		throw new IllegalArgumentException(
				"DBFactory for class " + cls.getName()
						+ " not found in DefaultDBFactoryController.writeBatch(Class, Vector, "
						+ "Hashtable<Integer, Object>)!");
	}

	@Override
	public Object generate(Class c) throws RemoteException, SQLException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(c, "generate(Class)");
		}
		Connection con = this.getConnection();
		DBFactory dbf = this.factories.get(c);
		if (dbf != null) {
			Object o = dbf.generate(con);
			if (atr != null) {
				this.stopOperation(atr);
			}
			return o;
		}
		throw new IllegalArgumentException(
				"DBFactory for class " + c.getName() + " not found "
						+ "in DefaultDBFactoryController.generate(Class)!");
	}

	@Override
	public Object duplicate(Object o) throws RemoteException, SQLException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation((o != null ? o.getClass() : null), "duplicate(Object)");
		}
		Connection c = this.getConnection();
		DBFactory dbf = this.factories.get(o.getClass());
		if (o instanceof RemoteDBMember) {
			((RemoteDBMember) o).objectBeforeDuplicate();
		}
		if (dbf != null) {
			Object res = dbf.duplicate(o, c);
			if (atr != null) {
				this.stopOperation(atr);
			}
			return res;
		}
		throw new IllegalArgumentException(
				"DBFactory for class " + o.getClass().getName()
						+ " not found in DefaultDBFactoryController.duplicate(Object)!");
	}

	@Override
	public void remove(Object o, boolean forced) throws RemoteException, SQLException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation((o != null ? o.getClass() : null), "remove(Object, " + "boolean)");
		}
		Connection c = this.getConnection();
		DBFactory dbf = this.factories.get(o.getClass());
		if ((o instanceof UserChangesNoticeable)
				&& !Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.mode.rmi")) {
			if (!((UserChangesNoticeable) o).isSuppressUserNotification()) {
				((UserChangesNoticeable) o)
						.setModificationUser(Long.getLong("corent.db.xs.RemoteDBFactoryController.user.id", -1));
				((UserChangesNoticeable) o).setPCName(SysUtil.GetHostname());
			}
		}
		if (o instanceof RemoteDBMember) {
			((RemoteDBMember) o).objectBeforeRemove();
		}
		if (dbf != null) {
			dbf.remove(o, forced, c);
			if (dbf instanceof Cached) {
				((Cached) dbf).removeDetected(o, false, this);
			}
			if ((o instanceof CacheNotifier)
					&& Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.cache.notification")) {
				try {
					CacheNotifier cn = (CacheNotifier) o;
					String ct = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "Table",
									"Table");
					String cid = System
							.getProperty("corent.db.xs.DefaultDBFactoryController.changenotifier.column.Id", "Id");
					String cchng = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "Changer",
									"Changer");
					String cdt = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "ChangeDate",
									"ChangeDate");
					String cmd = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "Mode",
									"Mode");
					String tn = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.tablename",
									"CacheNotification");
					int count = DBExec
							.Update(
									c,
									"update " + tn + " set " + cchng + "="
											+ DBUtil.DBString(cn.getPCName() + "-" + cn.getModificationUser()) + ", "
											+ cdt + "=" + new PTimestamp().toLong() + ", " + cmd + "='R'" + " where "
											+ ct + "=" + DBUtil.DBString(cn.getTablename()) + " and " + cid + "="
											+ DBUtil.DBString(cn.getIdValue()));
					if (count == 0) {
						DBExec
								.Update(
										c,
										"insert into " + tn + " (" + ct + ", " + cid + ", " + cchng + ", " + cdt + ", "
												+ cmd + ") values (" + DBUtil.DBString(cn.getTablename()) + ", "
												+ DBUtil.DBString(cn.getIdValue()) + ", "
												+ DBUtil.DBString(cn.getPCName() + "-" + cn.getModificationUser())
												+ ", " + new PTimestamp().toLong() + ", 'R')");
					}
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			}
			if (atr != null) {
				this.stopOperation(atr);
			}
			return;
		}
		throw new IllegalArgumentException(
				"DBFactory for class " + o.getClass().getName()
						+ " not found in DefaultDBFactoryController.remove(Object, boolean)!");
	}

	@Override
	public void removeBatch(Class cls, Vector k, boolean forced) throws RemoteException, SQLException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(cls, "removeBatch(Class, Vector, boolean)");
		}
		Connection c = this.getConnection();
		DBFactory dbf = this.factories.get(cls);
		if (dbf != null) {
			dbf.removeBatch(k, forced, c);
			if (dbf instanceof Cached) {
				((Cached) dbf).removeDetected(null, true, this);
			}
			if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.cache.notification")) {
				try {
					String ct = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "Table",
									"Table");
					String cid = System
							.getProperty("corent.db.xs.DefaultDBFactoryController.changenotifier.column.Id", "Id");
					String cchng = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "Changer",
									"Changer");
					String cdt = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "ChangeDate",
									"ChangeDate");
					String cmd = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.column." + "Mode",
									"Mode");
					String tn = System
							.getProperty(
									"corent.db.xs.DefaultDBFactoryController.changenotifier.tablename",
									"CacheNotification");
					int count = DBExec
							.Update(
									c,
									"update " + tn + " set " + cchng + "='*'" + ", " + cdt + "="
											+ new PTimestamp().toLong() + ", " + cmd + "='R' where " + ct + "="
											+ cls.getName() + " and " + cid + "='*'");
					if (count == 0) {
						DBExec
								.Update(
										c,
										"insert into " + tn + " (" + ct + ", " + cid + ", " + cchng + ", " + cdt + ", "
												+ cmd + ") values (" + cls.getName() + ", '*', '*', "
												+ new PTimestamp().toLong() + ", 'R')");
					}
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
			}
			if (atr != null) {
				this.stopOperation(atr);
			}
			return;
		}
		throw new IllegalArgumentException(
				"DBFactory for class " + cls.getName()
						+ " not found in DefaultDBFactoryController.removeBatch(Class, Vector, boolean)" + "!");
	}

	@Override
	public boolean isUnique(Object o) throws RemoteException, SQLException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation((o != null ? o.getClass() : null), "isUnique(Object)");
		}
		Connection c = this.getConnection();
		DBFactory dbf = this.factories.get(o.getClass());
		if (dbf != null) {
			if (o instanceof RemoteDBMember) {
				((RemoteDBMember) o).objectBeforeIsUnique();
			}
			if (atr != null) {
				this.stopOperation(atr);
			}
			return dbf.isUnique(o, c);
		}
		throw new IllegalArgumentException(
				"DBFactory for class " + o.getClass().getName()
						+ " not found in DefaultDBFactoryController.isUnique(Object)!");
	}

	/* Z&auml;hler f&uuml;r die Benamsung der tempor&auml;ren Dateien. */
	private static int TmpFileCounter = 0;

	@Override
	public boolean isHoldConnection() throws RemoteException {
		return this.holdConnection;
	}

	@Override
	public void setHoldConnection(boolean holdConnection) throws RemoteException {
		this.holdConnection = holdConnection;
	}

	@Override
	public Connection getHoldConnection() throws RemoteException {
		return this.connection;
	}

	@Override
	@Deprecated
	/**
	 * @changed OLI 29.01.2009 - Auf einen Aufruf der Methode <TT>getSelectionView(Object, String,
	 *             boolean)</TT> umgestellt.
	 *          <P>
	 *
	 * @deprecated OLI 29.01.2009 - Zugunsten der Methode <TT>getSelectionView(Object, String,
	 *             boolean)</TT> zur&uuml;ckgesetzt.
	 *
	 */
	public SelectionTableModel getSelectionView(Object o, String w) throws RemoteException, SQLException {
		AccesstimeRecord atr = null;
		SelectionTableModel stm = null;
		if (this.showAccesstimes) {
			atr = this
					.startOperation(
							(o != null ? o.getClass() : null),
							"getSelectionView(" + "Object, String, boolean)");
		}
		stm = this.getSelectionView(o, w, false);
		if (atr != null) {
			this.stopOperation(atr);
		}
		return stm;
	}

	/**
	 * @changed OLI 29.01.2009 - Auf Basis der Methode <TT>getSelectionView(Object, String)</TT> hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	@Override
	public SelectionTableModel getSelectionView(Object o, String w, boolean suppressFilling)
			throws RemoteException, SQLException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this
					.startOperation(
							(o != null ? o.getClass() : null),
							"getSelectionView(" + "Object, String, boolean)");
		}
		Connection c = this.getConnection();
		DBFactory dbf = this.factories.get(o.getClass());
		if (dbf != null) {
			String aj = null;
			if (dbf instanceof JoinExtender) {
				aj = ((JoinExtender) dbf).getJoinExtension();
			}
			SelectionTableModel stm = dbf.getSelectionView(w, aj, c, suppressFilling);
			if (atr != null) {
				this.stopOperation(atr);
			}
			return stm;
		}
		throw new IllegalArgumentException(
				"DBFactory for class " + o.getClass().getName()
						+ " not found in DefaultDBFactoryController.getSelectionView(Object)!");
	}

	@Override
	public Object doAction(Class c, int id, Object... p) throws RemoteException, SQLException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(c, "doAction(Class, int, Object...)");
		}
		Connection con = this.getConnection();
		DBFactory dbf = this.factories.get(c);
		if (dbf != null) {
			Object o = dbf.doAction(con, id, p);
			if (atr != null) {
				this.stopOperation(atr);
			}
			return o;
		}
		throw new IllegalArgumentException(
				"DBFactory for class " + c.getName() + " not found "
						+ "in DefaultDBFactoryController.doAction(Class, int, Object...)!");
	}

	@Override
	public PTimestamp getServerTime() throws RemoteException {
		return new PTimestamp();
	}

	@Override
	public long getTransactionNumber(long type) throws RemoteException {
		return -1;
	}

	@Override
	public synchronized String lock(Class cls, Object k, String userid) throws RemoteException {
		if ((k instanceof Lockable) && !((Lockable) k).isLockable()) {
			return null;
		}
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(cls, "lock(Class, Object, String)");
		}
		DefaultDBFactoryControllerLock dbfcl = new DefaultDBFactoryControllerLock(
				cls,
				k,
				(PTimestamp) new PTimestamp()
						.add(
								TimestampUnit.MINUTE,
								Integer.getInteger("corent.db.xs.DefaultDBFactoryController.lock.expires", 60)),
				userid);
		if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.locksByDatabasetable")) {
			try {
				Connection c = this.getConnection();
				String clo = System
						.getProperty("corent.db.xs.DefaultDBFactoryController.locks.column.LockObject", "LockObject");
				String clr = System
						.getProperty("corent.db.xs.DefaultDBFactoryController.locks.column.LastRequest", "LastRequest");
				String cuid =
						System.getProperty("corent.db.xs.DefaultDBFactoryController.locks.column.UserId", "UserId");
				String lck = cls.getName().toString().concat("-");
				if (k instanceof Lockable) {
					lck = lck.concat(((Lockable) k).getLockString());
				} else {
					lck = lck.concat(k.toString());
				}
				String tn = System.getProperty("corent.db.xs.DefaultDBFactoryController.locks.tablename", "LockObject");
				try {
					int count = DBExec
							.Update(
									c,
									"update " + tn + " set " + clr + "=" + dbfcl.getTimeOfExspiration().toLong()
											+ " where " + clo + "=" + DBUtil.DBString(lck) + " and " + cuid + "="
											+ DBUtil.DBString(userid));
					if (count == 0) {
						DBExec
								.Update(
										c,
										"insert into " + tn + " (" + clo + ", " + cuid + ", " + clr + ") values ("
												+ DBUtil.DBString(lck) + ", " + DBUtil.DBString(userid) + ", "
												+ dbfcl.getTimeOfExspiration().toLong() + ")");
					}
				} catch (Exception e) {
					ResultSet rs =
							DBExec.Query(c, "select " + cuid + " from " + tn + " where " + clo + "='" + lck + "'");
					String s = "UNKNOWN";
					if (rs.next()) {
						s = rs.getString(1);
					}
					DBExec.CloseQuery(rs);
					if (atr != null) {
						this.stopOperation(atr);
					}
					return (s.equals(userid) ? null : s);
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			if (atr != null) {
				this.stopOperation(atr);
			}
			return null;
		}
		DBFactoryControllerLock dbfcl0 = this.locks.get(dbfcl.getLockKey());
		if ((dbfcl0 == null) || dbfcl0.getLockedObjectUserid().equals(dbfcl.getLockedObjectUserid())) {
			this.locks.put(dbfcl.getLockKey(), dbfcl);
			if (atr != null) {
				this.stopOperation(atr);
			}
			return null;
		}
		if (atr != null) {
			this.stopOperation(atr);
		}
		return dbfcl0.getLockedObjectUserid().toString();
	}

	@Override
	public synchronized boolean unlock(Class cls, Object k, String userid) throws RemoteException {
		if ((k instanceof Lockable) && !((Lockable) k).isLockable()) {
			return true;
		}
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(cls, "unlock(Class, Object, String)");
		}
		if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.locksByDatabasetable")) {
			try {
				Connection c = this.getConnection();
				String clo = System
						.getProperty("corent.db.xs.DefaultDBFactoryController.locks.column.LockObject", "LockObject");
				String cuid =
						System.getProperty("corent.db.xs.DefaultDBFactoryController.locks.column.UserId", "UserId");
				String lck = cls.getName().toString().concat("-");
				if (k instanceof Lockable) {
					lck = lck.concat(((Lockable) k).getLockString());
				} else {
					lck = lck.concat(k.toString());
				}
				String tn = System.getProperty("corent.db.xs.DefaultDBFactoryController.locks.tablename", "LockObject");
				try {
					int count = DBExec
							.Update(
									c,
									"delete from " + tn + " where " + clo + "=" + DBUtil.DBString(lck) + " and " + cuid
											+ "=" + DBUtil.DBString(userid));
					if (atr != null) {
						this.stopOperation(atr);
					}
					return (count == 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		} else {
			DefaultDBFactoryControllerLock dbfcl = new DefaultDBFactoryControllerLock(cls, k, new PTimestamp(), userid);
			DBFactoryControllerLock dbfcl0 = this.locks.get(dbfcl.getLockKey());
			if ((dbfcl0 != null) && dbfcl0.getLockedObjectUserid().equals(dbfcl.getLockedObjectUserid())) {
				this.locks.remove(dbfcl.getLockKey());
				if (atr != null) {
					this.stopOperation(atr);
				}
				return true;
			}
		}
		if (atr != null) {
			this.stopOperation(atr);
		}
		return false;
	}

	private synchronized void clearOldLocks() throws RemoteException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(null, "clearOldLocks()");
		}
		if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.locksByDatabasetable")) {
			try {
				Connection c = this.getConnection();
				String clr = System
						.getProperty("corent.db.xs.DefaultDBFactoryController.locks.column.LastRequest", "LastRequest");
				String tn = System.getProperty("corent.db.xs.DefaultDBFactoryController.locks.tablename", "LockObject");
				try {
					DBExec.Update(c, "delete from " + tn + " where " + clr + "<=" + new PTimestamp().toLong());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		} else {
			int count = 0;
			for (Iterator it = this.locks.keySet().iterator(); it.hasNext();) {
				Object k = it.next();
				DBFactoryControllerLock dbfcl = this.locks.get(k);
				if (dbfcl.getTimeOfExspiration().toLong() <= new PTimestamp().toLong()) {
					this.locks.remove(k);
					count++;
				}
			}
		}
		if (atr != null) {
			this.stopOperation(atr);
		}
	}

	@Override
	public synchronized void clearLocks(Class cls) throws RemoteException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(cls, "clearLocks(Class)");
		}
		if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.locksByDatabasetable")) {
			try {
				Connection c = this.getConnection();
				String clr = System
						.getProperty("corent.db.xs.DefaultDBFactoryController.locks.column.LastRequest", "LastRequest");
				String tn = System.getProperty("corent.db.xs.DefaultDBFactoryController.locks.tablename", "LockObject");
				try {
					DBExec.Update(c, "delete from " + tn + " where " + clr + " like '" + cls.getName() + "%'");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		} else {
			for (Iterator it = this.locks.keySet().iterator(); it.hasNext();) {
				Object k = it.next();
				DBFactoryControllerLock dbfcl = this.locks.get(k);
				if (dbfcl.getLockedObjectClassname().equals(cls.getName())) {
					this.locks.remove(k);
				}
			}
		}
		if (atr != null) {
			this.stopOperation(atr);
		}
	}

	@Override
	public Vector<DBFactoryControllerLock> getLocks() throws RemoteException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(null, "getLocks()");
		}
		if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.locksByDatabasetable")) {
			try {
				Connection c = this.getConnection();
				String clo = System
						.getProperty("corent.db.xs.DefaultDBFactoryController.locks.column.LockObject", "LockObject");
				String clr = System
						.getProperty("corent.db.xs.DefaultDBFactoryController.locks.column.LastRequest", "LastRequest");
				String cuid =
						System.getProperty("corent.db.xs.DefaultDBFactoryController.locks.column.UserId", "UserId");
				String tn = System.getProperty("corent.db.xs.DefaultDBFactoryController.locks.tablename", "LockObject");
				Vector<DBFactoryControllerLock> v = new Vector<DBFactoryControllerLock>();
				try {
					ResultSet rs = DBExec.Query(c, "select " + clo + ", " + cuid + ", " + clr + " from " + tn);
					DefaultDBFactoryControllerLock dbfcl = null;
					while (rs.next()) {
						String lck = rs.getString(1);
						String uid = rs.getString(2);
						long lr = rs.getLong(3);
						String clsn = lck.substring(0, lck.indexOf("-"));
						String key = lck.substring(lck.indexOf("-") + 1, lck.length());
						try {
							Class cls = Class.forName(clsn);
							dbfcl = new DefaultDBFactoryControllerLock(cls, key, new PTimestamp(lr), uid);
							v.addElement(dbfcl);
						} catch (ClassNotFoundException cnfe) {
							cnfe.printStackTrace();
						}
					}
					DBExec.CloseQuery(rs);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (atr != null) {
					this.stopOperation(atr);
				}
				return v;
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		if (atr != null) {
			this.stopOperation(atr);
		}
		return new Vector<DBFactoryControllerLock>(this.locks.values());
	}

	@Override
	public void removeLock(DBFactoryControllerLock dbfcl) throws RemoteException {
		AccesstimeRecord atr = null;
		if (this.showAccesstimes) {
			atr = this.startOperation(null, "removeLock(DBFactoryControllerLock)");
		}
		if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.locksByDatabasetable")) {
			try {
				Connection c = this.getConnection();
				String clo = System
						.getProperty("corent.db.xs.DefaultDBFactoryController.locks.column.LockObject", "LockObject");
				String tn = System.getProperty("corent.db.xs.DefaultDBFactoryController.locks.tablename", "LockObject");
				try {
					DBExec.Update(c, "delete from " + tn + " where " + clo + "=" + DBUtil.DBString(dbfcl.getLockKey()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		} else {
			if (this.locks.contains(dbfcl)) {
				this.locks.remove(dbfcl.getLockKey());
			}
		}
		if (atr != null) {
			this.stopOperation(atr);
		}
	}

	/**
	 * @changed OLI 27.03.2009 - Hinzugef&uuml;gt
	 *          <P>
	 *
	 */
	@Override
	public void resetConnection() throws RemoteException {
		this.connection = null;
		try {
			ConnectionManager.ResetConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	@Override
	public void addDBFactoryControllerListener(DBFactoryControllerListener dbfcl) throws RemoteException {
	}

	@Override
	public void removeDBFactoryControllerListener(DBFactoryControllerListener dbfcl) throws RemoteException {
	}

	@Override
	public void fireEvent(Class cls, Object k, String userid, DBFactoryController.MessageTyp mt)
			throws RemoteException {
	}

	@Override
	public int getId() throws RemoteException {
		return this.id;
	}

	@Override
	public boolean isShowAccesstimes() throws RemoteException {
		return this.showAccesstimes;
	}

	/**
	 * @changed OLI 27.08.2007 - Implementierung der Methoden hinzugef&uuml;gt.
	 */
	@Override
	public void setControllerProperty(String pn, Object v) throws RemoteException {
		if (v == null) {
			this.controllerproperty.remove(pn);
		} else {
			this.controllerproperty.put(pn, v);
		}
	}

	@Override
	public void setShowAccesstimes(boolean b) throws RemoteException {
		this.showAccesstimes = b;
	}

	/* Implementierung des Interfaces Runnable. */

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(30000);
				this.clearOldLocks();
			} catch (Exception e) {
				log.error("Problem in corent.db.xs.DefaultDBFactoryController.Thread!", e);
			}
		}
	}

}
