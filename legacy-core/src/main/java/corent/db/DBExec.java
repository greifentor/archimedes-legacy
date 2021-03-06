/*
 * DBExec.java
 *
 * 01.03.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;

import corent.dates.PTimestamp;
import logging.Logger;

/**
 * Diese Klasse kapselt die Zugriff auf die Datenbank und erm&ouml;glicht auf diese Weise die Nutzung von Software unter
 * verschiedenartigen DBMS. <BR>
 * <HR SIZE=3>
 * <H3>Funktionsweise</H3>
 * <HR SIZE=1>
 * <P>
 * Mit Hilfe des <TT>JDBCDataSourceRecord</TT> k&ouml;nnen die Daten f&uuml; den Zugriff konfiguriert werden. Diese
 * Klasse ist ebenfalls ein Teil des <TT>corent.db</TT>-Packages, zu dem auch <TT>DBExec</TT> geh&ouml;rt.<BR>
 * <I>Beispiel:</I>
 * 
 * <PRE>
 * JDBCDataSourceRecord jdbcsr = new JDBCDataSourceRecord(
 * 		"com.microsoft.jdbc.sqlserver.SQLServerDriver",
 * 		"jdbc:microsoft:sqlserver://testeumel:1433;DatabaseName=TestDB",
 * 		"bmustermann",
 * 		"geheim");
 * </PRE>
 * <P>
 * Mit Hilfe eines vorkonfigurierten <TT>JDBCDataSourceRecord</TT> kann nun ein <TT>Connection</TT>-Objekt gewonnen
 * worden:<BR>
 * <I>Beispiel:</I>
 * 
 * <PRE>
 * Connection c0 = ConnectionManager.GetConnection(jdbcsr);
 * </PRE>
 *
 * <P>
 * Anschlie&szlig;end kann mit der Klasse <TT>DBExec</TT> &uuml;ber die Methode <TT>Query</TT> eine Abfrage initiiert
 * werden, oder &uuml;ber die Methode <TT>Update</TT> eine &Auml;nderung an der Datenbank (sowohl Schema als auch
 * Inhalt) vorgenommen werden.<BR>
 * <I>Beispiel:</I>
 * 
 * <PRE>
 * ResultSet rs = DBExec.Query(c0, "select Id, Inhalt from Exempel");
 * </PRE>
 *
 * <P>
 * Das <TT>ResultSet</TT> l&auml;&szlig;t sich mit den gewohnten Methoden bearbeiten. Nach dem Auslesen der Daten sollte
 * das <TT>ResultSet</TT> &uuml;ber die Methode <TT>DBExec.CloseQuery(rs)</TT> abgeschlossen werden. Dies funktioniert
 * dann auch datenbanken&uuml;bergreifend.
 * <P>
 * &Auml;nderungen werden, wie bereits angedeutet, mit Hilfe der Methode <TT>Update</TT> aus der Klasse <TT>DBExec</TT>
 * in die Datenbank eingebracht werden.<BR>
 * <I>Beispiel:</I>
 * 
 * <PRE>
 * DBExec.Update(c, "update Exempel set Inhalt='Doof' where Id=1");
 * </PRE>
 *
 * <P>
 * Um Datenbankenabfragen zu erzeugen die <B>datenbanksystem&uuml;bergreifend</B> funktionieren, gibt es in der Klasse
 * <TT>DBExec</TT> einen Satz von Methoden, die diesem Ziel zuarbeiten. Um beispielsweise ein Query zu erzeugen, dessen
 * Trefferzahl limitiert werden soll, kann die Methode <TT>LimitQuery(String, int)</TT> genutzt werden. <I>Beispiel:</I>
 * 
 * <PRE>
 * ResultSet rs = DBExec.Query(c0, DBExec.LimitQuery("select Id, Inhalt from Exempel", 100));
 * </PRE>
 *
 * <P>
 * Die vorangegangene Anweisung produziert je nach Datenbank die SQL-Anfragen:
 * 
 * <PRE>
 * select top 100 Id, Inhalt from Exempel (MSSQL, HSQL) oder
 * select Id, Inhalt from Exempel limit 100 (MYSQL).
 * </PRE>
 *
 * <P>
 * Zu &auml;hnlichen Zwecken stehen in DBExec-Klasse die Methoden <TT>Concatenation</TT>, <TT>Convert</TT>,
 * <TT>LowerCase</TT> und <TT>Truncate</TT> zur Verf&uuml;gung.
 * <P>
 * Die Anpassungen, die durch diese Methoden durchgef&uuml;hrt werden, richten sich nach dem f&uuml;r die DBExec-Klasse
 * eingestellten DBExecMode. Dieser kann explizit &uuml;ber die Methode SetMode gesetzt werden.<BR>
 * <I>Beispiel:</I>
 * 
 * <PRE>
 * DBExec.SetMode(DBExecMode.HSQL);
 * </PRE>
 *
 * <P>
 * <H3>Properties:</H3>
 * <TABLE BORDER=1>
 * <TR VALIGN=TOP>
 * <TH>Property</TH>
 * <TH>Default</TH>
 * <TH>Typ</TH>
 * <TH>Beschreibung</TH>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.db.DBExec.mssql.convert.force.isnull</TD>
 * <TD>false</TD>
 * <TD>Boolean</TD>
 * <TD>Durch Setzen dieser Property l&auml;&szlig;t sich der Aufruf der <TT>isnull</TT> im generierten Convert-Statement
 * bei Betrieb im MSSQL- und HSQL-Modus zu erzwingen.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.db.DBExec.PrintTimestamp</TD>
 * <TD>false</TD>
 * <TD>Boolean</TD>
 * <TD>Diese Property kann gesetzt werden, um die Ausgabe eines Zeitstempels vor jedem Statement zu erreichen. Die
 * Option funktioniert nur im Zusammenspiel mit dem Setzen der Property <TT>corent.db.DBExec.ShowStatements</TT>.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.db.DBExec.ShowStatements</TD>
 * <TD>false</TD>
 * <TD>Boolean</TD>
 * <TD>Durch das Setzen dieser Property werden die von DBExec an das DBMS &uuml;bertragenen Statements &uuml;ber
 * <TT>System.out</TT> angezeigt.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.db.DBExec.suppress.output.faulty.statements
 * <TD>false</TD>
 * <TD>Boolean</TD>
 * <TD>Durch das Setzen dieser Property wird die Ausgabe fehlgeschlagener Statements unterdr&uuml;ckt.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.db.DBExec.suppress.warnings</TD>
 * <TD>false</TD>
 * <TD>Boolean</TD>
 * <TD>Durch das Setzen dieser Property werden Warnungen (z. B., da&szlig; die eine oder andere Methode f&uuml;r einen
 * bestimmten Modus noch nicht implementiert ist ;o) unterdr&uuml;ckt.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.db.DBExecMode.version</TD>
 * <TD><TT>null</TT></TD>
 * <TD>String</TD>
 * <TD>Diese Property wird im Zusammenhang mit der Convert-Methode genutzt. Hintergrund war eine ge&auml;nderte Syntax
 * im Konvertieren bei mySQL 5.x.</TD>
 * </TR>
 * </TABLE>
 * <P>
 * <P>
 * <B>HINWEIS</B>: Die Implementierung von PostgreSQL-Datenbanken ist noch relativ frisch! Daher sollte beim Betrieb in
 * diesem Modus mit ausreichendem Test Aufwand einhergehen.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 01.03.2004 - Hinzugef&uuml;gt.
 * @changed OLI 05.09.2007 - Erg&auml;nzung der Dokumentation.
 * @changed OLI 13.09.2007 - Einbau der Test und Beispiel-Routine in der <TT>main</TT>-Methode. Erweiterung der Methoden
 *          <TT>LowerCase(String)</TT> und <TT>UpperCase(String)
 *         </TT> um die Unterst&uuml;tzung des DBExecMode.MSSQL.
 * @changed OLI 10.10.2007 - Korrektur des fehlerhaften Italic-Tags in der Klassendokumentation.
 * @changed OLI 08.11.2007 - Das Abfangen von Null-Feldern wird nun unter PostgreSQL f&uuml;r jedes Element der
 *          Concatenation in der Methode <TT>concatenation(Vector, String)</TT> abgefangen.
 * @changed OLI 20.01.2009 - Erweiterung um eine Methode zum Lesen der aktuellen Systemzeit des Datenbankensystems.
 * @changed OLI 27.04.2009 - Erweiterung um die Ausgabe des Statements im Falle einer SQLException in den Methoden
 *          <TT>query</TT> und <TT>update</TT>.
 * @changed OLI 19.06.2009 - Formatanpassungen und Umstellung der Ausgaben auf log4j. Die Messages gehen fort an auf dem
 *          <TT>info</TT>-Level ins log.
 * @changed OLI 27.04.2011 - Erweiterung um den Betrieb mit dem <TT>DBExecListenern</TT>.
 * @changed OLI 27.05.2011 - M&ouml;glichkeit der Unterdr&uuml;ckung der Ausgabe eines fehlgeschlagenen Statements in
 *          den Methoden <TT>update</TT> und <TT>query</TT>.
 */

public class DBExec {

	/*
	 * Das Hinzuf&uuml;gen eines neuen Modus erfordert &Auml;nderungen in den Methoden closeQuery, concatenation,
	 * convert, limitQuery und truncate.
	 */

	/**
	 * Name der Property zur Unterd&uuml;ckung der Ausgabe eines fehlerhaften Statements in der update- und der
	 * query-Methode.
	 *
	 * @changed OLI 27.05.2011 - Hinzugef&uuml;gt.
	 */
	private static final String PROP_NAME_SUPPRESS_FAULTY_STATEMENTS =
			"corent.db.DBExec.suppress.output.faulty.statements";

	/**
	 * &Uuml;ber diese Flagge kann die Ausgabe eines Zeitstempels vor jedem Statement veranla&szlig;t werden.
	 */
	private static boolean PrintTimestamp = Boolean.getBoolean("corent.db.DBExec.PrintTimestamp");
	/**
	 * Flagge, die &uuml;ber die Anzeige der Statements entscheidet <I>(Default wird gesetzt) durch die Property
	 * "corent.db.DBExec.ShowStatements")</I>. / public static boolean ShowStatements =
	 * Boolean.getBoolean("corent.db.DBExec.ShowStatements" );
	 * 
	 * /* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger.
	 */
	private final static Logger LOG = Logger.getLogger(DBExec.class);

	/* Schalter f&uuml;r die Betriebsart. */
	private static DBExecMode DBMODE = DBExecMode.MYSQL;
	/* Statische Instanz zur statischen Ausf&uuml;hrung der Methoden. */
	private static DBExec INSTANCE = new DBExec();

	private boolean printTimestamp = PrintTimestamp;
	private boolean showStatements = true; // ShowStatements;
	private DBExecMode dbmode = DBMODE;

	/** Generiert eine Instanz der DBExec-Klasse. */
	public DBExec() {
		super();
		this.setShowStatements(Boolean.getBoolean("corent.db.DBExec.ShowStatements"));
	}

	/**
	 * Schreibt einen DBExec-Ausgabeheader mit einer Meldung auf die Konsole.
	 *
	 * @param message Die Meldung, die nach dem Header auf die Konsole gedruckt werden soll.
	 */
	public static void PrintMessage(String message) {
		INSTANCE.printMessage(message);
	}

	/**
	 * Schreibt einen DBExec-Ausgabeheader mit einer Meldung auf die Konsole.
	 *
	 * @param message Die Meldung, die nach dem Header auf die Konsole gedruckt werden soll.
	 */
	private void printMessage(String message) {
		String s = "";
		if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.showAccesstimes")) {
			s = s.concat("    ");
		}
		LOG.info(s + "DBExec" + (this.printTimestamp ? " " + new PTimestamp().toString() : "") + " > " + message);
	}

	/**
	 * F&uuml;hrt ein Update-Statement &uuml;ber die angegebene DB-Connection aus.
	 *
	 * @param connection Connection, &uuml;ber die das angegebene Statement ausgef&uuml;hrt werden soll.
	 * @param statement  Das auszuf&uuml;hrende Statement im String-Format.
	 * @return Die Anzahl der betroffenen Zeilen.
	 * @throws SQLException falls bei der Ausf&uuml;hrung des Statements ein Fehler auftritt.
	 */
	public static int Update(Connection connection, String statement) throws SQLException {
		return INSTANCE.update(connection, statement);
	}

	/**
	 * F&uuml;hrt ein Update-Statement &uuml;ber die angegebene DB-Connection aus.
	 *
	 * @param connection Connection, &uuml;ber die das angegebene Statement ausgef&uuml;hrt werden soll.
	 * @param statement  Das auszuf&uuml;hrende Statement im String-Format.
	 * @return Die Anzahl der betroffenen Zeilen.
	 * @throws SQLException falls bei der Ausf&uuml;hrung des Statements ein Fehler auftritt.
	 *
	 * @changed OLI 27.04.2009 - Erweiterung um die Ausgabe des Statements im Falle einer SQLException.
	 * @changed OLI 27.05.2011 - Herausnahme der Stacktrace-Ausgabe im Falle einer SQLException. Diese Exception wird
	 *          ohnehin weitergeleitet. Die Ausgabe kann durch die Kundenklasse erfolgen.
	 */
	private int update(Connection connection, String statement) throws SQLException {
		int count = 0;
		Statement stmt = null;
		if (this.showStatements) {
			this.printMessage(statement);
		}
		try {
			stmt = connection.createStatement();
			count = stmt.executeUpdate(statement);
		} catch (SQLException sqle) {
			if (!this.isSuppressOutputOfFaultyStatements()) {
				LOG.error("\nby stmt: " + statement);
			}
			throw sqle;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
		return count;
	}

	private boolean isSuppressOutputOfFaultyStatements() {
		return Boolean.getBoolean(PROP_NAME_SUPPRESS_FAULTY_STATEMENTS);
	}

	/**
	 * F&uuml;hrt ein Query-Statement &uuml;ber die angegebene Connection aus.
	 *
	 * @param connection Connection, &uuml;ber die das angegebene Statement ausgef&uuml;hrt werden soll.
	 * @param statement  Das auszuf&uuml;hrende Statement im String-Format.
	 * @return Das aus dem Statement resultierende ResultSet.
	 * @throws SQLException falls bei der Ausf&uuml;hrung des Statements ein Fehler auftritt.
	 */
	public static ResultSet Query(Connection connection, String statement) throws SQLException {
		return INSTANCE.query(connection, statement);
	}

	/**
	 * F&uuml;hrt ein Query-Statement &uuml;ber die angegebene Connection aus.
	 *
	 * @param connection Connection, &uuml;ber die das angegebene Statement ausgef&uuml;hrt werden soll.
	 * @param statement  Das auszuf&uuml;hrende Statement im String-Format.
	 * @return Das aus dem Statement resultierende ResultSet.
	 * @throws SQLException falls bei der Ausf&uuml;hrung des Statements ein Fehler auftritt.
	 */
	private ResultSet query(Connection connection, String statement) throws SQLException {
		return this.query(connection, statement, -1);
	}

	/**
	 * F&uuml;hrt ein Query-Statement &uuml;ber die angegebene Connection aus.
	 *
	 * @param connection Connection, &uuml;ber die das angegebene Statement ausgef&uuml;hrt werden soll.
	 * @param statement  Das auszuf&uuml;hrende Statement im String-Format.
	 * @param limit      Anzahl der maximal zu selektierenden Datens&auml;tze.
	 * @return Das aus dem Statement resultierende ResultSet.
	 * @throws SQLException falls bei der Ausf&uuml;hrung des Statements ein Fehler auftritt.
	 *
	 * @changed OLI 27.04.2009 - Erweiterung um die Ausgabe des Statements im Falle einer SQLException.
	 *
	 */
	private ResultSet query(Connection connection, String statement, long limit) throws SQLException {
		ResultSet rs = null;
		Statement stmt = null;
		if (limit > 0) {
			statement = this.limitQuery(statement, limit);
		}
		if (this.showStatements) {
			this.printMessage(statement);
		}
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(statement);
		} catch (SQLException sqle) {
			if (!this.isSuppressOutputOfFaultyStatements()) {
				LOG.error("\nby stmt: " + statement);
			}
			throw sqle;
		}
		return rs;
	}

	/**
	 * Schlie&szlig;t das &uuml;bergebene ResultSet inklusive seines Statements.
	 *
	 * @param rs Das zu schlie&szlig;ende ResultSet.
	 * @throws SQLException falls beim Schliessen ein Fehler auftritt.
	 */
	public static void CloseQuery(ResultSet rs) throws SQLException {
		INSTANCE.closeQuery(rs);
	}

	/**
	 * Schlie&szlig;t das &uuml;bergebene ResultSet inklusive seines Statements.
	 *
	 * @param rs Das zu schlie&szlig;ende ResultSet.
	 * @throws SQLException falls beim Schliessen ein Fehler auftritt.
	 */
	private void closeQuery(ResultSet rs) throws SQLException {
		if (this.dbmode != DBExecMode.MYSQL) {
			Statement stmt = rs.getStatement();
			rs.close();
			stmt.close();
		}
	}

	/**
	 * Liefert den aktuellen Betriebsmodus der statischen Instanz.
	 *
	 * @return Der aktuelle Betriebsmodus der statischen Instanz.
	 */
	static DBExecMode GetMode() {
		return INSTANCE.getMode();
	}

	/**
	 * Liefert den aktuellen Betriebsmodus der Instanz.
	 *
	 * @return Der aktuelle Betriebsmodus.
	 */
	public DBExecMode getMode() {
		return this.dbmode;
	}

	/**
	 * Setzt einen neuen Betriebsmodus.
	 *
	 * @param mode Der neue Betriebsmodus.
	 */
	public void setMode(DBExecMode mode) {
		this.dbmode = mode;
	}

	/**
	 * Pr&uuml;ft, ob die Statements im Betrieb der statischen Instanz ausgegeben werden sollen.
	 *
	 * @return Der aktuelle ShowStatements-Modus der statischen Instanz..
	 */
	public static boolean IsShowStatements() {
		return INSTANCE.isShowStatements();
	}

	/**
	 * Setzt einen neuen ShowStatements-Modus.
	 *
	 * @param mode Der neue ShowStatements-Modus.
	 */
	public static void SetShowStatements(boolean mode) {
		LOG.debug((!mode ? "don't " : "") + "show statements for static instance");
		INSTANCE.setShowStatements(mode);
	}

	/**
	 * Pr&uuml;ft, ob die Statements f&uuml;r die Instanz angezeigt werden sollen.
	 * 
	 * @return Der aktuelle ShowStatements-Modus.
	 */
	public boolean isShowStatements() {
		return this.showStatements;
	}

	/**
	 * Setzt einen neuen ShowStatements-Modus.
	 *
	 * @param mode Der neue ShowStatements-Modus.
	 */
	public void setShowStatements(boolean mode) {
		this.showStatements = mode;
	}

	/**
	 * Mit Hilfe dieser Methode kann ein Konkatenierungsausdruck f&uuml;r die in dem Vector &uuml;bergebenen Strings
	 * gebildet werden. Der tats&auml;chlich gebildete Ausdruck wird entsprechend des Betriebsmodus an die Syntax der
	 * verwendeten Datenbank angepa&szlig;t.
	 * <P>
	 * Werden in dem Vector Objekte anderer Klassen als String &uuml;bergeben, so werden sie &uuml;ber ihre
	 * <TT>toString()</TT>-Methoden angepa&szlig;t.
	 *
	 * @param strings Liste der String, die miteinander konkateniert werden sollen.
	 * @param trenner Eine Trennsequenz, die zwischen den konkatenierten Elementen eingef&uuml;gt werden soll.
	 */
	public static String Concatenation(Vector strings, String trenner) {
		return INSTANCE.concatenation(strings, trenner);
	}

	/**
	 * Mit Hilfe dieser Methode kann ein Konkatenierungsausdruck f&uuml;r die in dem Vector &uuml;bergebenen Strings
	 * gebildet werden. Der tats&auml;chlich gebildete Ausdruck wird entsprechend des Betriebsmodus an die Syntax der
	 * verwendeten Datenbank angepa&szlig;t.
	 * <P>
	 * Werden in dem Vector Objekte anderer Klassen als String &uuml;bergeben, so werden sie &uuml;ber ihre
	 * <TT>toString()</TT>-Methoden angepa&szlig;t.
	 *
	 * @param strings Liste der String, die miteinander konkateniert werden sollen.
	 * @param trenner Eine Trennsequenz, die zwischen den konkatenierten Elementen eingef&uuml;gt werden soll.
	 *
	 * @precondition strings != <TT>null</TT> &amp;&amp; trenner != <TT>null</TT>
	 *
	 * @changed OLI 08.11.2007 - Das Abfangen von Null-Feldern wird nun unter PostgreSQL f&uuml;r jedes Element der
	 *          Concatenation abgefangen.
	 */
	private String concatenation(Vector strings, String trenner) {
		assert (strings != null) : "string list can not be null";
		assert (trenner != null) : "separator can not be null";
		int i = 0;
		int leni = 0;
		String h = null;
		String k = "";
		String s = "";
		String symbol = " + ";
		if (strings.size() == 1) {
			return strings.elementAt(0).toString();
		}
		if (this.dbmode == DBExecMode.HSQL) {
			for (i = 0, leni = strings.size(); i < leni; i++) {
				if (i + 1 < leni) {
					s += "concat(" + strings.elementAt(i).toString() + ", concat('" + trenner + "', ";
					k += "))";
				} else {
					s += strings.elementAt(i).toString();
				}
			}
			s += k;
		} else if (this.dbmode == DBExecMode.MYSQL) {
			s += "concat(";
			for (i = 0, leni = strings.size(); i < leni; i++) {
				if ((i != 0) && (trenner != null)) {
					s += "'" + trenner + "', ";
				}
				s += "ifnull(" + strings.elementAt(i) + ", ''), ";
			}
			s = s.substring(0, s.length() - 2);
			s += ")";
		} else if ((this.dbmode == DBExecMode.MSSQL) || (this.dbmode == DBExecMode.POSTGRESQL)) {
			if (this.dbmode == DBExecMode.POSTGRESQL) {
				symbol = " || ";
			}
			for (i = 0, leni = strings.size(); i < leni; i++) {
				if ((i != 0) && (trenner != null)) {
					s += "'" + trenner + "'" + symbol;
				}
				h = this.convert(DBType.VARCHAR, strings.elementAt(i).toString());
				if (this.dbmode == DBExecMode.POSTGRESQL) {
					h = "coalesce(".concat(h).concat(", '')");
				}
				s += h + symbol;
			}
			s = s.substring(0, s.length() - 3);
			if (this.dbmode == DBExecMode.POSTGRESQL) {
				s = "lower(".concat(s).concat(")");
			}
		}
		return s;
	}

	/**
	 * Erweitert das &uuml;bergebene Select-Statement um eine Klausel, die die Anzahl der zu selektierenden
	 * Datens&auml;tze begrenzt.
	 *
	 * @param statement Das zu erweiternde Select-Statement.
	 * @param limit     Die maximale Anzahl der Datens&auml;tze, selektiert werden sollen.
	 */
	public static String LimitQuery(String statement, long limit) {
		return INSTANCE.limitQuery(statement, limit);
	}

	/**
	 * Erweitert das &uuml;bergebene Select-Statement um eine Klausel, die die Anzahl der zu selektierenden
	 * Datens&auml;tze begrenzt.
	 *
	 * @param statement Das zu erweiternde Select-Statement.
	 * @param limit     Die maximale Anzahl der Datens&auml;tze, selektiert werden sollen.
	 *
	 * @precondition statement != <TT>null</TT>
	 *
	 * @changed OLI 13.09.2007 - Erweiterung um den Warnhinweis, bei Nutzung der Methode im DBExecMode.HSQL.
	 *
	 */
	private String limitQuery(String statement, long limit) {
		assert statement != null : "limitQuery does not work with statement null";
		boolean changeIt = true;
		String s = null;
		String stmt = "";
		StringTokenizer st = null;
		if (limit > 0) {
			if ((this.dbmode == DBExecMode.HSQL) || (this.dbmode == DBExecMode.MYSQL)
					|| (this.dbmode == DBExecMode.POSTGRESQL)) {
				if (statement.toLowerCase().indexOf(" limit ") < 0) {
					statement = statement.trim();
					statement = statement.concat(" limit " + limit);
				}
			} else if (this.dbmode == DBExecMode.MSSQL) {
				changeIt = true;
				s = null;
				stmt = "";
				st = new StringTokenizer(statement, " \n");
				while (st.hasMoreTokens()) {
					s = st.nextToken().toLowerCase();
					if ((s.equals("select")) && changeIt) {
						stmt = stmt.concat("select ");
						s = st.nextToken().toLowerCase();
						if (!s.equals("top")) {
							stmt = stmt.concat("top " + limit + " ");
						}
						changeIt = false;
					}
					if (s.equals("union")) {
						changeIt = true;
					}
					stmt = stmt.concat(s + " ");
				}
				statement = stmt;
			}
		}
		return statement;
	}

	/**
	 * Diese Methode erzeugt f&uuml;r den angegebenen Ausdruck s eine Umwandlung in die Kleinbuchstaben (z. B.
	 * LOWER-Funktion).
	 *
	 * @param s Der Ausdruck, f&uuml;r den der Funktionsaufruf erzeugt werden soll.
	 */
	public static String LowerCase(String s) {
		return INSTANCE.lowerCase(s);
	}

	/**
	 * Diese Methode erzeugt f&uuml;r den angegebenen Ausdruck s eine Umwandlung in die Kleinbuchstaben (z. B.
	 * LOWER-Funktion).
	 *
	 * @param s Der Ausdruck, f&uuml;r den der Funktionsaufruf erzeugt werden soll.
	 *
	 * @precondition s != <TT>null</TT>
	 *
	 * @changed OLI 13.09.2007 - Erweiterung um die Unterst&uuml;tzung von MSSQL.
	 *
	 */
	private String lowerCase(String s) {
		assert s != null : "lowerCase argument can not be null";
		if ((this.dbmode == DBExecMode.HSQL) || (this.dbmode == DBExecMode.MSSQL) || (this.dbmode == DBExecMode.MYSQL)
				|| (this.dbmode == DBExecMode.POSTGRESQL)) {
			s = "lower(" + s + ")";
		}
		return s;
	}

	/**
	 * Diese Methode erzeugt eine datenbankspezifische Typkonvertierungsklausel zu den &uuml;bergebenen Parametern.
	 *
	 * @param type  Der Typ, in den der angegebene Wert konvertiert werden soll.
	 * @param value Der Wert im String-Format, der umgewandelt werden soll. Handelt es sich tats&auml;chlich um einen
	 *              String, so mu&szlig; dieser in Hochkommata gesetzt werden.
	 */
	private String convert(DBType type, String value) {
		return this.convert(type, value, 0, 0);
	}

	/**
	 * Diese Methode erzeugt eine datenbankspezifische Typkonvertierungsklausel zu den &uuml;bergebenen Parametern. Im
	 * Falle einer Umwandlung in den Typ VARCHAR und NUMERIC, k&ouml;nnen hier Angaben zur L&auml;nge des Feldes und zur
	 * L&auml;nge des Umwandlungsziels gemacht werden.
	 *
	 * @param type  Der Typ, in den der angegebene Wert konvertiert werden soll.
	 * @param value Der Wert im String-Format, der umgewandelt werden soll. Handelt es sich tats&auml;chlich um einen
	 *              String, so mu&szlig; dieser in Hochkommata gesetzt werden.
	 * @param len   L&auml;nge des Zieltyps. Dieser Parameter ist nur bei Umwandlungen in VARCHAR und NUMERIC relevant.
	 * @param nks   Die Nachkommastellen des Zieltyps. Dieser Parameter ist nur bei Umwandlungen in NUMERIC relevant.
	 */
	public static String Convert(DBType type, String value, int len, int nks) {
		return INSTANCE.convert(type, value, len, nks);
	}

	/**
	 * Diese Methode erzeugt eine datenbankspezifische Typkonvertierungsklausel zu den &uuml;bergebenen Parametern. Im
	 * Falle einer Umwandlung in den Typ VARCHAR und NUMERIC, k&ouml;nnen hier Angaben zur L&auml;nge des Feldes und zur
	 * L&auml;nge des Umwandlungsziels gemacht werden.
	 *
	 * @param type  Der Typ, in den der angegebene Wert konvertiert werden soll.
	 * @param value Der Wert im String-Format, der umgewandelt werden soll. Handelt es sich tats&auml;chlich um einen
	 *              String, so mu&szlig; dieser in Hochkommata gesetzt werden.
	 * @param len   L&auml;nge des Zieltyps. Dieser Parameter ist nur bei Umwandlungen in VARCHAR und NUMERIC relevant.
	 * @param nks   Die Nachkommastellen des Zieltyps. Dieser Parameter ist nur bei Umwandlungen in NUMERIC relevant.
	 *
	 * @precondition type != <TT>null</TT> &amp;&amp; value != <TT>null</TT>
	 *
	 */
	private String convert(DBType type, String value, int len, int nks) {
		assert type != null : "null is not a DBType that can converted to";
		assert value != null : "value argument for convert method can not be null";
		String statement = "--- ERROR ---";
		if (this.dbmode == DBExecMode.MYSQL) {
			if (System.getProperty("corent.db.DBExecMode.version") != null) {
				if (System.getProperty("corent.db.DBExecMode.version").startsWith("5") && (type == DBType.VARCHAR)) {
					statement = "convert(".concat(value).concat(", char)");
				}
			} else {
				statement = value;
			}
		} else if (this.dbmode == DBExecMode.HSQL) {
			statement = "convert(".concat(value).concat(", ");
			if (type == DBType.BIGINT) {
				statement = statement.concat("bigint");
			} else if (type == DBType.INTEGER) {
				statement = statement.concat("int");
			} else if (type == DBType.NUMERIC) {
				if (len > 0) {
					statement = statement.concat("numeric(" + len + ", " + nks + ")");
				} else {
					statement = statement.concat("numeric(18, 0)");
				}
			} else if (type == DBType.VARCHAR) {
				if (len > 0) {
					statement = statement.concat("varchar(" + len + ")");
				} else {
					statement = statement.concat("varchar");
				}
			}
			statement = statement.concat(")");
			if ((type == DBType.INTEGER) || Boolean.getBoolean("corent.db.DBExec.hsql.convert.force.isnull")) {
				statement = "ifnull(".concat(statement + ", '')");
			}
		} else if (this.dbmode == DBExecMode.MSSQL) {
			statement = "convert(";
			if (type == DBType.BIGINT) {
				statement = statement.concat("bigint");
			} else if (type == DBType.INTEGER) {
				statement = statement.concat("int");
			} else if (type == DBType.NUMERIC) {
				if (len > 0) {
					statement = statement.concat("numeric(" + len + ", " + nks + ")");
				} else {
					statement = statement.concat("numeric(18, 0)");
				}
			} else if (type == DBType.VARCHAR) {
				if (len > 0) {
					statement = statement.concat("varchar(" + len + ")");
				} else {
					statement = statement.concat("varchar(50)");
				}
			}
			statement = statement.concat(", " + value + ")");
			if ((type == DBType.INTEGER) || Boolean.getBoolean("corent.db.DBExec.mssql.convert.force.isnull")) {
				statement = "isnull(".concat(statement + ", '')");
			}
		} else if (this.dbmode == DBExecMode.POSTGRESQL) {
			statement = "cast(".concat(value.toString()).concat(" as ");
			if (type == DBType.BIGINT) {
				statement = statement.concat("bigint");
			} else if (type == DBType.INTEGER) {
				statement = statement.concat("int");
			} else if (type == DBType.NUMERIC) {
				if (len > 0) {
					statement = statement.concat("numeric(" + len + ", " + nks + ")");
				} else {
					statement = statement.concat("numeric(18, 0)");
				}
			} else if (type == DBType.VARCHAR) {
				statement = statement.concat("varchar");
			}
			statement = statement.concat(")");
		}
		return statement;
	}

}