/*
 * Converter.java
 *
 * 09.09.2009
 *
 * (c) by O.Lieshoff
 *
 */

package gengen.util;

import java.sql.Types;

import logging.Logger;

/**
 * Eine Sammlung von Methoden zum Thema Konvertierungen im Rahmen der Code-Generierung.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 09.09.2009 - Hinzugef&uuml;gt
 *
 */

public class Converter {

	/* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
	private static Logger log = Logger.getLogger(Converter.class);

	/**
	 * Liefert den Namen eines Javatyps zur angegebenen Kombination aus SQL-Type und Namen der Domain.
	 * <P>
	 * Die Domainnamen <TT>Boolean</TT>, <TT>PDate</TT>, <TT>PTimestamp</TT> und <TT>LongPTimestamp</TT> f&uuml;hren
	 * hierbei zu einer anderen, als der g&auml;ngigen Umwandlung.
	 * <P>
	 * Der Domain-Name <TT>PTime</TT> wird nur noch aus Kompatibilit&auml;tsgr&uuml;nden kultiviert und wird
	 * mittelfristig herausgenommen.
	 * <P>
	 * Wird ein Datumstyp genutzt, aber keine Namensraum f&uuml;r die Datumsklassen angegeben, so wird der Namensraum
	 * <TT>corentx.dates</TT> als Default genutzt.
	 *
	 * @param type          Die java.sql.Konstante zum SQL-Type, der in einen Java-Typ umgewandelt werden soll.
	 * @param name          Der Name der Domain zum SQL-Type, falls es einen solchen gibt.
	 *                      Dom&auml;nennamenabh&auml;ngige Umwandlungen werden nur dann durchgef&uuml;hert, wenn ein
	 *                      Dom&auml;nenname angegeben wird.
	 * @param dateNameSpace Der Package-Namensraum aus dem die Typen <TT>PDate</TT>, <TT>PTimestamp</TT> und
	 *                      <TT>LongPTimestamp</TT> genutzt werden sollen.
	 * @return Der Name des Java-Datentypen bzw. 'UNKNOWN', wenn kein Datentyp zu den angegebenen Parametern gefunden
	 *         werden kann.
	 *
	 * @todo OLI - Herausnahme der PTime-Umwandlung (09.09.2009).
	 */
	public static String toJavaType(int type, String name, String dateNameSpace) {
		String s = "UNKNOWN";
		if ((name != null) && name.equalsIgnoreCase("Boolean")) {
			type = Types.BOOLEAN;
		}
		if ((name != null) && (name.equalsIgnoreCase("PDate") || name.equalsIgnoreCase("PTime")
				|| name.equalsIgnoreCase("PTimestamp") || name.equalsIgnoreCase("LongPTimestamp"))) {
			if (name.equalsIgnoreCase("PTime")) {
				log.warn("Domain type 'PTime' is listed to deprecate. Better use another date " + "type insteadof.");
			}
			if (dateNameSpace == null) {
				dateNameSpace = "corentx.dates";
			}
			if (!dateNameSpace.endsWith(".")) {
				dateNameSpace = dateNameSpace.concat(".");
			}
			return dateNameSpace.concat(name);
		}
		switch (type) {
		case Types.BIGINT:
			s = "long";
			break;
		case Types.BIT:
		case Types.BOOLEAN:
			s = "boolean";
			break;
		case Types.BINARY:
		case Types.LONGVARBINARY:
		case Types.VARBINARY:
			s = "byte[]";
			break;
		case Types.CHAR:
		case Types.LONGVARCHAR:
		case Types.VARCHAR:
			s = "String";
			break;
		case Types.DATE:
			s = "java.sql.Date";
			break;
		case Types.DECIMAL:
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.NUMERIC:
			s = "double";
			break;
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.TINYINT:
			s = "int";
			break;
		case Types.REAL:
			s = "float";
			break;
		case Types.TIME:
			s = "java.sql.Time";
			break;
		case Types.TIMESTAMP:
			s = "java.sql.Timestamp";
			break;
		}
		return s;
	}

}
