/*
 * ConnectionManager.java
 *
 * 01.03.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import logging.Logger;

/**
 * Diese Klasse regelt den Zugriff auf Datenquellen &uuml;ber JDBC-Verbindungen.
 * 
 * <P>
 * Der Manager fordert bei jeder Anfrage eine neue Connection von der Datenbank an. Soll immer mit der gleichen
 * Connection gearbeitet werden, mu&szlig; eine Flagge &uuml;ber die statische Variable <TT>HoldConnectionFlag</TT>
 * gesetzt werden. Diese Variable wird auch &uuml;ber die Property <TT>corent.db.ConnectionManager.HoldConnection</TT>
 * initialisiert.
 * <P>
 * <I><B>WICHTIG!</B> Beachten Sie unbedingt, da&szlig; es beim Betrieb mit nur einer Connection, keine gute Idee ist,
 * nebenl&auml;fige Prozesse auf die Datenbankschicht zugreifen zu lassen. Es besteht ein hohes Verklemmungs- bzw.
 * Kollisionspotential.</I>
 *
 * @author O.Lieshoff
 *
 * @changed OLI 01.03.2004 - Hinzugef&uuml;gt.
 * @changed OLI 23.02.2009 - Erweiterung um den Modus, bei dem immer eine Connections gehalten wird.
 * @changed OLI 18.06.2009 - Formatanpassungen und Umstellung auf log4j.
 *
 */

public class ConnectionManager implements Serializable {

	/**
	 * Flagge f&uuml;r den Betriebsmodus. Wird die Flagge nicht gesetzt, so wird bei jedem Aufruf der Methode
	 * <TT>GetConnection()</TT> eine neue Connection vom DriverManager angefordert. Andernfalls wird immer die gleiche
	 * Connection benutzt. <BR>
	 * Der Defaultwert wird aus der Property "corent.db.ConnectionManager.HoldConnection" gelesen.
	 */
	private static boolean HoldConnectionFlag = Boolean.getBoolean("corent.db.ConnectionManager.HoldConnection");
	/**
	 * Flagge f&uuml;r die Anzeige von Konsolenausgaben. Der Defaultwert wird aus der Property
	 * "corent.db.ConnectionManager.ShowOutput" gelesen.
	 */
	private static boolean ShowOutput = Boolean.getBoolean("corent.db.ConnectionManager.ShowOutput");
	/** Eine gehaltene Connection, falls der Modus das erfordert. */
	private static Connection HoldConnection = null;

	private static List<String> drivers = new Vector<String>();
	/* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
	private static Logger log = Logger.getLogger(ConnectionManager.class);

	/*
	 * Z&auml;hler f&uuml;r die Anzahl der Connections, die vom ConnectionManager verteilt worden sind.
	 */
	private static long Num = 0;

	/**
	 * Erzeugt eine Connection zur konfigurierten Datenquelle.
	 *
	 * @param jdbcdsr Ein JDBCDataSourceRecord, anhand dessen eine Connection erzeugt werden soll.
	 * @throws SQLException Wenn beim Herstellen der Connection ein Fehler auftritt.
	 *
	 * @changed OLI 27.03.2009 - Vor dem Holen einer neuen Connection wird der Treiber nun deregistriert. Ansonsten
	 *          kommt es unter gewissen Konstellationen vor, da&szlig; beim Wegbrechen der Datenbankverbindung eine
	 *          Wiederaufnahme der Verbindung unm&ouml;glich ist.
	 *
	 * @todo OLI - Falls hier Performanzbeeintr&auml;chtigungen auftreten ist &uuml;ber Konzept nachzudenken, mit dem
	 *       die Performanz zu steigern ist (OLI 27.03.2009).
	 *
	 */
	public static Connection GetConnection(JDBCDataSourceRecord jdbcdsr) throws SQLException {
		Connection c = null;
		Driver d = null;
		String s = "";
		if (ShowOutput && !HoldConnectionFlag) {
			if (Boolean.getBoolean("corent.db.xs.DefaultDBFactoryController.showAccesstimes")) {
				s = "    ";
			}
			log.info(s.concat("connection no. " + (++Num) + " requested."));
		}
		if ((HoldConnection == null) || (!HoldConnectionFlag)) {
			try {
				if (drivers.contains(jdbcdsr.getDBName())) {
					d = DriverManager.getDriver(jdbcdsr.getDBName());
					DriverManager.deregisterDriver(d);
				} else {
					drivers.add(jdbcdsr.getDriver());
				}
				Class.forName(jdbcdsr.getDriver());
				c = DriverManager.getConnection(jdbcdsr.getDBName(), jdbcdsr.getUser(), jdbcdsr.getPassword());
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
			if (HoldConnectionFlag) {
				HoldConnection = c;
			}
		} else {
			c = HoldConnection;
		}
		return c;
	}

	/**
	 * Erzwingt das Erzeugen einer neuen Connection f&uuml;r den Fall, das die Connection vom ConnectionManager gehalten
	 * werden soll. Die offene Connection wird das bei nicht mehr beachtet aber auch nicht geschlossen.
	 *
	 * @throws SQLException Wenn beim Herstellen der Connection ein Fehler auftritt.
	 */
	public static void ResetConnection() throws SQLException {
		HoldConnection = null;
	}

}