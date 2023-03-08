/*
 * DBType.java
 *
 * 01.03.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db;

import java.sql.Types;
import java.util.Vector;

import logging.Logger;

/**
 * Mit Hilfe dieser Implementierung eines typsicheren Enum werden SQL-Datentypen repr&auml;sentiert.
 * <P>
 * Die Funktionalit&auml;t der Methoden der Klasse wird stellenweise durch den in der Klasse DBExec eingestellten Modus
 * des bearbeiteten DBMS beeinflu&szlig;t. So liefert die Methode <TT>GetSQLType(DBType, DBExecMode)</TT> z. B. im
 * HSQL-Modus den Wert "longvarchar", anstatt der bei MSSQL und mySQL &uuml;blichen Bezeichnung "text".
 *
 * @author O.Lieshoff
 *
 * @changed OLI 01.03.2004 - Hinzugef&uuml;gt.
 * @changed OLI 29.06.2009 - Formatanpassungen und Umstellung auf log4j.
 *
 */

public final class DBType {

	/* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
	private static Logger log = Logger.getLogger(DBType.class);

	/* Liste der erzeugten DBTypes. */
	private static final Vector TYPES = new Vector();

	private final boolean hasLength;
	private final boolean hasNKS;
	private final String name;

	private DBType(String name) {
		this(name, false, false);
	}

	private DBType(String name, boolean hasLength, boolean hasNKS) {
		super();
		this.name = name;
		this.hasLength = hasLength;
		this.hasNKS = hasNKS;
		TYPES.addElement(this);
	}

	/**
	 * Liefert den Namen des DBTypes.
	 *
	 * @return Der Name des DBTypes.
	 */
	public String toString() {
		return this.name;
	}

	/**
	 * Pr&uuml;ft, ob der DBType eine L&auml;ngenangaben haben kann (z. B. varchar(20)).
	 *
	 * @return <TT>true</TT>, falls der DBType eine L&auml;ngenangabe haben kann.
	 */
	public boolean hasLength() {
		return this.hasLength;
	}

	/**
	 * Pr&uuml;ft, ob der DBType eine Nachkommastellenangaben haben kann (z. B. numeric(5, 2)).
	 *
	 * @return <TT>true</TT>, falls der DBType eine Nachkommastellenangabe haben kann.
	 */
	public boolean hasNKS() {
		return this.hasNKS;
	}

	/**
	 * Liefert den Namen des SQLTyps zum DBType in Abh&auml;ngigkeit zum angegebenen DBExecMode (LONGVARCHAR wird z. B.
	 * unter MSSQL als TEXT angegeben.
	 *
	 * @param dbmode Der DBExecMode, zu dem der entsprechende SQLTyp-Name f&uuml;r den vorliegenden DBType geliefert
	 *               werden soll.
	 * @return Der Namen des SQLTyps zum DBType in Abh&auml;ngigkeit zum angegebenen DBExecMode.
	 */
	public String toSQLType(DBExecMode dbmode) {
		return GetSQLType(this, dbmode);
	}

	public static final DBType BIGINT = new DBType("BIGINT");
	public static final DBType BINARY = new DBType("BINARY");
	public static final DBType BIT = new DBType("BIT");
	public static final DBType BLOB = new DBType("BLOB");
	public static final DBType BOOLEAN = new DBType("BOOLEAN");
	public static final DBType CHAR = new DBType("CHAR", true, false);
	public static final DBType DATE = new DBType("DATE");
	public static final DBType DOUBLE = new DBType("DOUBLE");
	public static final DBType FLOAT = new DBType("FLOAT");
	public static final DBType INTEGER = new DBType("INTEGER");
	public static final DBType LONGVARBINARY = new DBType("LONGVARBINARY");
	public static final DBType LONGVARCHAR = new DBType("LONGVARCHAR");
	public static final DBType NUMERIC = new DBType("NUMERIC", true, true);
	public static final DBType SMALLINT = new DBType("SMALLINT");
	public static final DBType TIME = new DBType("TIME");
	public static final DBType TIMESTAMP = new DBType("TIMESTAMP");
	public static final DBType TINYINT = new DBType("TINYINT");
	public static final DBType VARBINARY = new DBType("VARBINARY", true, false);
	public static final DBType VARCHAR = new DBType("VARCHAR", true, false);

	/**
	 * Konvertiert die angegebene java.sql.Types-Konstante in einen DBType.
	 *
	 * @param type Die zu konvertierende java.sql.Types-Konstante.
	 * @return Der zur angegebenen Konstante passende DBType. Kann die Konstante nicht zugeordnet werden, so wird der
	 *         Wert INTEGER zur&uuml;ckgegeben.
	 */
	public static DBType Convert(int type) {
		switch (type) {
		case Types.BIGINT:
			return BIGINT;
		case Types.BINARY:
			return BINARY;
		case Types.BIT:
			return BIT;
		case Types.BLOB:
			return BLOB;
		case Types.BOOLEAN:
			return BOOLEAN;
		case Types.CHAR:
			return CHAR;
		case Types.DATE:
			return DATE;
		case Types.DECIMAL:
			return NUMERIC;
		case Types.DOUBLE:
			return DOUBLE;
		case Types.FLOAT:
			return FLOAT;
		case Types.INTEGER:
			return INTEGER;
		case Types.LONGVARBINARY:
			return LONGVARBINARY;
		case Types.LONGVARCHAR:
			return LONGVARCHAR;
		case Types.NUMERIC:
			return NUMERIC;
		case Types.REAL:
			return DOUBLE;
		case Types.SMALLINT:
			return SMALLINT;
		case Types.TIME:
			return TIME;
		case Types.TIMESTAMP:
			return TIMESTAMP;
		case Types.TINYINT:
			return TINYINT;
		case Types.VARBINARY:
			return VARBINARY;
		case Types.VARCHAR:
			return VARCHAR;
		default:
			log.warn("There is no DBType for Constant " + type + "! Converted to INTEGER");
		}
		return INTEGER;
	}

	/**
	 * Konvertiert den angegebenen DBType in eine java.sql.Type-Konstante.
	 *
	 * @param dbtype Der zu konvertierende DBType.
	 * @return Die zum angegebenen DBType passende java.sql.Type-Konstante.
	 */
	public static int Convert(DBType dbtype) {
		if (dbtype == BIGINT) {
			return Types.BIGINT;
		} else if (dbtype == BINARY) {
			return Types.BINARY;
		} else if (dbtype == BIT) {
			return Types.BIT;
		} else if (dbtype == BLOB) {
			return Types.BLOB;
		} else if (dbtype == BOOLEAN) {
			return Types.BOOLEAN;
		} else if (dbtype == CHAR) {
			return Types.CHAR;
		} else if (dbtype == DATE) {
			return Types.DATE;
		} else if (dbtype == DOUBLE) {
			return Types.DOUBLE;
		} else if (dbtype == FLOAT) {
			return Types.FLOAT;
		} else if (dbtype == INTEGER) {
			return Types.INTEGER;
		} else if (dbtype == LONGVARBINARY) {
			return Types.LONGVARBINARY;
		} else if (dbtype == LONGVARCHAR) {
			return Types.LONGVARCHAR;
		} else if (dbtype == NUMERIC) {
			return Types.NUMERIC;
		} else if (dbtype == SMALLINT) {
			return Types.SMALLINT;
		} else if (dbtype == TIME) {
			return Types.TIME;
		} else if (dbtype == TIMESTAMP) {
			return Types.TIMESTAMP;
		} else if (dbtype == TINYINT) {
			return Types.TINYINT;
		} else if (dbtype == VARBINARY) {
			return Types.VARBINARY;
		}
		return Types.VARCHAR;
	}

	/**
	 * Liefert einen zum DBMode passenden SQL-Typname zum DBType.
	 *
	 * @param dbtype Der DBType, zu dem der SQL-Typname ermittelt werden soll.
	 * @param dbmode Der DBMode, f&uuml;r den der Typ gelten soll.
	 */
	public static String GetSQLType(DBType dbtype, DBExecMode dbmode) {
		if (dbtype == BIGINT) {
			return "bigint";
		} else if (dbtype == BINARY) {
			return "binary";
		} else if (dbtype == BIT) {
			return "bit";
		} else if (dbtype == BLOB) {
			return "blob";
		} else if (dbtype == BOOLEAN) {
			return "boolean";
		} else if (dbtype == CHAR) {
			return "char";
		} else if (dbtype == DATE) {
			return "date";
		} else if (dbtype == DOUBLE) {
			return "double";
		} else if (dbtype == FLOAT) {
			return "float";
		} else if (dbtype == INTEGER) {
			return "int";
		} else if (dbtype == LONGVARBINARY) {
			return "longvarbinary";
		} else if (dbtype == LONGVARCHAR) {
			if (DBExec.GetMode() == DBExecMode.HSQL) {
				return "longvarchar";
			}
			return "text";
		} else if (dbtype == NUMERIC) {
			return "numeric";
		} else if (dbtype == SMALLINT) {
			return "smallint";
		} else if (dbtype == TIME) {
			return "time";
		} else if (dbtype == TIMESTAMP) {
			return "timestamp";
		} else if (dbtype == TINYINT) {
			return "tinyint";
		} else if (dbtype == VARBINARY) {
			return "varbinary";
		}
		return "varchar";
	}

	/**
	 * Liefert einen DBType zum angegebenen String bzw. <TT>null</TT>, wenn der String auf keinen DBType pa&szlig;t.
	 *
	 * @param s Der String, zu dem der passende DBType gesucht werden soll (der String mu&szlig; den Namen des DBTypes
	 *          enthalten).
	 * @return Der zu s passende DBType, oder <TT>null</TT>, wenn s keinen Namen eines DBTypes enth&auml;lt.
	 */
	public static DBType valueOf(String s) {
		for (int i = 0, len = TYPES.size(); i < len; i++) {
			DBType dbt = (DBType) TYPES.elementAt(i);
			if (dbt.toString().equalsIgnoreCase(s)) {
				return dbt;
			}
		}
		return null;
	}

}
