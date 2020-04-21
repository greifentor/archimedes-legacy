/*
 * DBUtil.java
 *
 * 22.12.2003
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db;


import corent.base.*;

import java.sql.*;
import java.util.*;

import org.apache.log4j.*;


/**
 * Diese Klasse bietet eine Reihe von Utilities zur Arbeit mit DBMS und JDBC an.
 * <P>Das Verhalten der Methode GetObject(ResultSet, int, boolean) l&auml;&szlig;t sich durch
 * folgende Properties beeinflussen:
 * <BR>
 * <TABLE BORDER=1>
 *     <TR VALIGN=TOP>
 *         <TH>Property</TH>
 *         <TH>Default</TH>
 *         <TH>Typ</TH>
 *         <TH>Beschreibung</TH>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.db.DBUtil.DBString.suppress.quote.change</TD>
 *         <TD>false</TD>
 *         <TD>Boolean</TD>
 *         <TD>
 *             Wird diese Property gesetzt, werden die Hochkommata in String (bei Nutzung der
 *             DBString-Methode) nicht in datenbankspezifische Zeichen umgesetzt. 
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.db.DBUtil.GetObject.force<BR>.number.not.null</TD>
 *         <TD>false</TD>
 *         <TD>Boolean</TD>
 *         <TD>
 *             Wird diese Property gesetzt, werden beim Lesen von Zahlenwerten &uuml;ber die
 *             Methode <TT>GetObject(ResultSet, int, boolean)</TT> Null-Referenzen in echte,
 *             (runde) Nullwerte des entsprechenden Typs umgesetzt.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.db.DBUtil.GetObject.force<BR>.numeric.to.double</TD>
 *         <TD>false</TD>
 *         <TD>Boolean</TD>
 *         <TD>
 *             Wird diese Property gesetzt, so wird in der Methode <TT>GetObject(ResultSet, int,
 *             boolean)</TT> ein Datenfeld vom Typ <TT>numeric</TT> auch dann in ein 
 *             Double-Objekt umgesetzt, wenn das Datenfeld keine Nachkommastellen 
 *             zul&auml;&szlig;t. Sonst werden nachkommenstellenlose Numerics in Long-Objekte 
 *             transformiert.<P>
 *             <I>Hinweis:</I> Bei HSQL-Datenbanken ist diese Property immer zu setzen. Hier ist
 *             ein Fehler im Datenbanktreiber aufgefallen, der als Precision f&uuml;r Numerics
 *             immer 0 zur&uuml;ckliefert.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.db.DBUtil.GetObject.treat<BR>.longvarbinary.as.string</TD>
 *         <TD>false</TD>
 *         <TD>Boolean</TD>
 *         <TD>
 *             Diese Property kann gesetzt werden, wenn der DB-Treiber LONGVARCHAR-Felder 
 *             f&auml;lschlich als LONGVARBINARY-Felder erkennt. Diese werden dann in einen 
 *             String umgewandelt (Methode <TT>GetObject(ResultSet, int, boolean)</TT>).
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.db.DBUtil.TypeByName</TD>
 *         <TD>false</TD>
 *         <TD>Boolean</TD>
 *         <TD>
 *             Ein Setzen dieser Property schaltet die Typfindung auf den in den 
 *             ResultSetMetaDaten angegebenen Namen des Spaltendatentypen um. Sonst wird die
 *             numerische Typeninformation genutzt.
 *         </TD>
 *     </TR>
 * </TABLE>
 * <HR>
 *
 * @author O.Lieshoff
 *
 * @changed OLI 22.12.2003 - Hinzugef&uuml;gt.
 * @changed OLI 05.09.2007 - Erweiterung der Dokumentation.
 * @changed OLI 27.12.2007 - Erweiterung um die Behandlung der Property
 *         <I>corent.db.DBUtil.GetObject.force.numeric.to.double</I> (Boolean) in der Methode
 *         <TT>GetObject(ResultSet, int, boolean)</TT>.
 * @changed OLI 15.04.2008 - Erweiterung der Methode <TT>GetObject(ResultSet, int, boolean)</TT>
 *         um die M&ouml;glichkeit LONGVARBINARY-Felder als String zu behandeln.
 * @changed OLI 29.09.2008 - Erweiterung um die Umsetzung von Hochkommata.
 * @changed OLI 19.02.2009 - Die Methode <TT>DBString(String)</TT> wandelt nun auch f&uuml;r
 *         PostgreSQL Backslashes um.
 * @changed OLI 29.06.2009 - Formatanpassungen und Umstellung auf log4j.
 *
 */
 
public class DBUtil {

    /* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
    private static Logger log = Logger.getLogger(DBUtil.class);

    /**
     * Diese Methode liefert einen Datenbankstring (mit Hochkommata) zur&uuml;ck, falls es sich
     * bei dem &uuml;bergebenen Java-String nicht um eine <TT>null</TT>-Referenz handelt. Sonst
     * wird der String "NULL" zur&uuml;ckgegeben.
     * <P>Beachten Sie, da&szlig; die Methode sich bez&uuml;glich der Hochkommata-Umsetzung an
     * der DBMS-Konfiguration der statischen Instanz der DBExec-Klasse orientiert.
     * <BR>Zudem ersetzt sie Backslashes in den Daten durch einen Doppelbackslash, sofern dies
     * f&uuml;r das entsprechende DBMS notwendig ist. Dieser Austausch findet nur statt, wenn
     * &uuml;ber die Property <TT>corent.db.DBUtil.DBString.suppress.quote.change</TT> ein
     * Austausch der einfacher Hochkommata gegen Anf&uuml;hrungszeichen ablehnt wird.
     *
     * @param s Der umzuwandelnde Java-String.
     * @return Die SQL-String-Entsprechung zum &uuml;bergebenen Java-String.
     *
     * @changed OLI 29.09.2008 - Erweiterung um die datenbankenspezifische Umsetzung von
     *         Hochkommata.
     * @changed OLI 01.10.2008 - Debugging an der Hochkommataumsetzung.
     *
     */
    public static String DBString(String s) {
        if (s != null) {
            if (!Boolean.getBoolean("corent.db.DBUtil.DBString.suppress.quote.change")) {
                if (DBExec.GetMode() == DBExecMode.HSQL) {
                    s = s.replace("'", "\\apos");
                } else if ((DBExec.GetMode() == DBExecMode.MSSQL) 
                        || (DBExec.GetMode() == DBExecMode.MYSQL)) {
                    s = s.replace("'", "\\'");
                } else if (DBExec.GetMode() == DBExecMode.POSTGRESQL) {
                    s = s.replace("'", "''");
                    s = s.replace("\\", "\\\\");
                } else {
                    log.info("db mode " + DBExec.GetMode() + " does not support any changes for"
                            + " quotes!");
                }
            } else {
                s = s.replace("'", "\"");
            }
            return "'" + s + "'";
        }
        return "NULL";
    }

    public static TypeChanger Tc = null;

    /**
     * Erzeugt zur angegebenen Types.Konstante einen lesbaren Typnamen. 
     *
     * @param type Die Types.Konstante, zu dem ein String ermittelt werden soll.
     * @return Eine Bezeichnung des Types.
     */
    public static String TypeToString(int type) {
        switch (type) {
            case Types.ARRAY:
                return "Types.ARRAY";
            case Types.BIGINT:
                return "Types.BIGINT";
            case Types.BINARY:
                return "Types.BINARY";
            case Types.BIT:
                return "Types.BIT";
            case Types.BLOB:
                return "Types.BLOB";
            /*
            case Types.BOOLEAN:
                return "Types.BOOLEAN";
            */
            case Types.CHAR:
                return "Types.CHAR";
            case Types.CLOB:
                return "Types.CLOB";
            /*
            case Types.DATALINK:
                return "Types.DATALINK";
            */
            case Types.DATE:
                return "Types.DATE";
            case Types.DECIMAL:
                return "Types.DECIMAL";
            case Types.DISTINCT:
                return "Types.DISTINCT";
            case Types.DOUBLE:
                return "Types.DOUBLE";
            case Types.FLOAT:
                return "Types.FLOAT";
            case Types.INTEGER:
                return "Types.INTEGER";
            case Types.JAVA_OBJECT:
                return "Types.JAVA_OBJECT";
            case Types.LONGVARBINARY:
                return "Types.LONGVARBINARY";
            case Types.LONGVARCHAR:
                return "Types.LONGVARCHAR";
            case Types.NULL:
                return "Types.NULL";
            case Types.NUMERIC:
                return "Types.NUMERIC";
            case Types.OTHER:
                return "Types.OTHER";
            case Types.REAL:
                return "Types.REAL";
            case Types.REF:
                return "Types.REF";
            case Types.SMALLINT:
                return "Types.SMALLINT";
            case Types.STRUCT:
                return "Types.STRUCT";
            case Types.TIME:
                return "Types.TIME";
            case Types.TIMESTAMP:
                return "Types.TIMESTAMP";
            case Types.TINYINT:
                return "Types.TINYINT";
            case Types.VARBINARY:
                return "Types.VARBINARY";
            case Types.VARCHAR:
                return "Types.VARCHAR";
        }
        return "undefined";
    }
    
    /** @return Liste mit allen Types als String (alphabetisch sortiert). */
    public static SortedVector GetTypes() {
        SortedVector sv = new SortedVector();
        sv.addElement("ARRAY");
        sv.addElement("BIGINT");
        sv.addElement("BINARY");
        sv.addElement("BIT");
        // sv.addElement("BOOLEAN");
        sv.addElement("BLOB");
        sv.addElement("CHAR");
        sv.addElement("CLOB");
        // sv.addElement("DATALINK");
        sv.addElement("DATE");
        sv.addElement("DECIMAL");
        sv.addElement("DISTINCT");
        sv.addElement("DOUBLE");
        sv.addElement("FLOAT");
        sv.addElement("INTEGER");
        sv.addElement("JAVA_OBJECT");
        sv.addElement("LONGVARBINARY");
        sv.addElement("LONGVARCHAR");
        sv.addElement("NULL");
        sv.addElement("NUMERIC");
        sv.addElement("OTHER");
        sv.addElement("REAL");
        sv.addElement("REF");
        sv.addElement("SMALLINT");
        sv.addElement("STRUCT");
        sv.addElement("TIME");
        sv.addElement("TIMESTAMP");
        sv.addElement("TINYINT");
        sv.addElement("VARBINARY");
        sv.addElement("VARCHAR");
        if (Tc != null) {
            Tc.extendTypes(sv);
        }
        return sv;
    }
    
    /**
     * Liefert den Types-Typen zum angegebene String bzw. eine IllegalArgumentException, wenn 
     * der String keinen g&uuml;tigen Typen enth&auml;lt.
     *
     * @param s Der String, zu dem ein Types.Typ gesucht werden soll.
     * @return Die zum &uuml;bergebenen String passende Types-Konstante.
     * @throws IllegalArgumentException falls zu s keine Konstante ermittelt werden kann.
     */
    public static int GetType(String s) {
        if (Tc != null) {
            if (Tc.isType(s)) {
                return Tc.getType(s);
            }
        }
        if (s.equalsIgnoreCase("ARRAY")) {
            return Types.ARRAY;
        } else if (s.equalsIgnoreCase("BIGINT")) {
            return Types.BIGINT;
        } else if (s.equalsIgnoreCase("BINARY")) {
            return Types.BINARY;
        } else if (s.equalsIgnoreCase("BIT")) {
            return Types.BIT;
        } else if (s.equalsIgnoreCase("BLOB")) {
            return Types.BLOB;
        /*
        } else if (s.equalsIgnoreCase("BOOLEAN")) {
            return Types.BOOLEAN;
        */
        } else if (s.equalsIgnoreCase("CHAR")) {
            return Types.CHAR;
        } else if (s.equalsIgnoreCase("CLOB")) {
            return Types.CLOB;
        /*
        } else if (s.equalsIgnoreCase("DATALINK")) {
            return Types.DATALINK;
        */
        } else if (s.equalsIgnoreCase("DATE")) {
            return Types.DATE;
        } else if (s.equalsIgnoreCase("DECIMAL")) {
            return Types.DECIMAL;
        } else if (s.equalsIgnoreCase("DISTINCT")) {
            return Types.DISTINCT;
        } else if (s.equalsIgnoreCase("DOUBLE")) {
            return Types.DOUBLE;
        } else if (s.equalsIgnoreCase("FLOAT")) {
            return Types.FLOAT;
        } else if (s.equalsIgnoreCase("INTEGER")) {
            return Types.INTEGER;
        } else if (s.equalsIgnoreCase("JAVA_OBJECT")) {
            return Types.JAVA_OBJECT;
        } else if (s.equalsIgnoreCase("LONGVARBINARY")) {
            return Types.LONGVARBINARY;
        } else if (s.equalsIgnoreCase("LONGVARCHAR")) {
            return Types.LONGVARCHAR;
        } else if (s.equalsIgnoreCase("NULL")) {
            return Types.NULL;
        } else if (s.equalsIgnoreCase("NUMERIC")) {
            return Types.NUMERIC;
        } else if (s.equalsIgnoreCase("OTHER")) {
            return Types.OTHER;
        } else if (s.equalsIgnoreCase("REAL")) {
            return Types.REAL;
        } else if (s.equalsIgnoreCase("REF")) {
            return Types.REF;
        } else if (s.equalsIgnoreCase("SMALLINT")) {
            return Types.SMALLINT;
        } else if (s.equalsIgnoreCase("STRUCT")) {
            return Types.STRUCT;
        } else if (s.equalsIgnoreCase("TIME")) {
            return Types.TIME;
        } else if (s.equalsIgnoreCase("TIMESTAMP")) {
            return Types.TIMESTAMP;
        } else if (s.equalsIgnoreCase("TINYINT")) {
            return Types.TINYINT;
        } else if (s.equalsIgnoreCase("VARBINARY")) {
            return Types.VARBINARY;
        } else if (s.equalsIgnoreCase("VARCHAR")) {
            return Types.VARCHAR;
        }
        throw new IllegalArgumentException("There is no Type for \"" + s + "\"!");
    }
    
    /** 
     * Definiert, ob der angegebene Type &uuml;ber eine L&auml;ngenangabe verf&uuml;gen kann.
     *
     * @param dt Der zu &uuml;berpr&uuml;fende Datentyp.
     * @return <TT>true</TT>, wenn zu dem Datentyp eine L&auml,ngenangabe gemacht werden kann.
     */
    public static boolean IsLength(int dt) {
        switch (dt) {
            case Types.DECIMAL:
            case Types.NUMERIC:
            case Types.VARBINARY:
            case Types.VARCHAR:
                return true;
        }
        return false;
    }
    
    /** 
     * Definiert, ob der angegebene Type &uuml;ber eine Nachkommastellenangabe verf&uuml;gen 
     * kann.
     *
     * @param dt Der zu &uuml;berpr&uuml;fende Datentyp.
     * @return <TT>true</TT>, wenn zu dem Datentyp eine Nachkommastellenangabe gemacht werden 
     *     kann.
     */
    public static boolean IsNKS(int dt) {
        switch (dt) {
            case Types.DECIMAL:
            case Types.NUMERIC:
                return true;
        }
        return false;
    }
    
    /**
     * Mit Hilfe dieser Methode lassen sich aus einem ResultSet Objekt gewinnen. Mit der reinen 
     * <TT>getObject()</TT>-Methode des ResultSet ist dies so nicht m&ouml;glich.
     *
     * @param rs Das ResultSet, aus dem das Objekt gelesen werden sollen.
     * @param col Die Spalte aus der das Objekt gelesen werden soll. (Nach ResultSet-Konvention
     *     beginnend mit Spalte 1 ...).
     * @param nullsAllowed <TT>true</TT>, wenn als R&uuml;ckgabewert <TT>null</TT> zul&auml;ssig
     *     ist.
     * @return Das Objekt.
     * @throws SQLException Falls bei der Bearbeitung der Methode ein Fehler auftritt.
     * 
     * @changed
     *     OLI 27.12.2007 - Erweiterung um die Behandlung der Property 
     *             <I>corent.db.DBUtil.GetObject.force.numeric.to.double</I> (Boolean), mit 
     *             deren Hilfe eine Umsetzung von Numerics nach Double auch dann erzwungen wird,
     *             wenn sie keine Nachkommastellen haben.
     *     <P>OLI 15.04.2008 - Erweiterung um das wahlweise Lesen von LONGVARBINARY-Felder als
     *             LONGVARCHAR.
     *     <P>OLI 17.04.2008 - Erweiterung um die Property 
     *             <I>corent.db.DBUtil.GetObject.force.number.not.null</I>. Wird sie gesetzt, so
     *             werden Zahlenwerte beim Lesen einer Null-Referenz in echte (runde) Nullwerte
     *             umgesetzt.
     */
    public static Object GetObject(ResultSet rs, int col, boolean nullsAllowed)
            throws SQLException {
        int columntype = rs.getMetaData().getColumnType(col);
        Object obj = null;
            log.debug("GetObject(ResultSet, int, boolean) -> int=" + col);
        if (Boolean.getBoolean("corent.db.DBUtil.TypeByName")) {
            String tn = rs.getMetaData().getColumnTypeName(col);
            log.debug("                                   -> typename=" + tn);
            columntype = GetType(tn);
        }
        log.debug(rs.getMetaData().getColumnTypeName(col));
        try {
            switch (columntype) {
                case Types.BIGINT:
                    obj = new Long(rs.getLong(col));
                    break;
                case Types.DATE:
                    obj = rs.getDate(col);
                    break;
                case Types.DOUBLE:
                    obj = new Double(rs.getDouble(col));
                    break;
                case Types.FLOAT:
                case Types.REAL:
                    obj = new Float(rs.getFloat(col));
                    break;
                case Types.INTEGER:
                    obj = new Integer(rs.getInt(col));
                    break;
                case Types.LONGVARBINARY:
                case Types.LONGVARCHAR:
                    obj = rs.getString(col);
                    break;
                case Types.NUMERIC:
                case Types.DECIMAL:
                    String s = rs.getString(col);
                    if ((rs.getMetaData().getPrecision(col) == 0) && !Boolean.getBoolean(
                            "corent.db.DBUtil.GetObject.force.numeric.to.double")) {
                        obj = new Long(s);
                    } else {
                        obj = new Double(s);
                    }
                    break;
                case Types.SMALLINT:
                    obj = new Integer(rs.getInt(col));
                    break;
                case Types.VARCHAR:
                case Types.CHAR:
                    obj = new String(rs.getString(col));
                    break;
                default:
                    if ((columntype == Types.LONGVARBINARY) && Boolean.getBoolean(
                            "corent.db.DBUtil.GetObject.treat.longvarbinary.as.string")) {
                        obj = rs.getString(col);
                    }
                    break;
            }
        } catch (NullPointerException npe) {
            if (Boolean.getBoolean("corent.db.DBUtil.GetObject.force.number.not.null")) {
            switch (columntype) {
                case Types.BIGINT:
                    return new Long(0);
                case Types.DOUBLE:
                    return new Double(0.0);
                case Types.FLOAT:
                case Types.REAL:
                    return new Float(0.0);
                case Types.INTEGER:
                    return new Integer(0);
                case Types.NUMERIC:
                case Types.DECIMAL:
                    String s = "0";
                    if ((rs.getMetaData().getPrecision(col) == 0) && !Boolean.getBoolean(
                            "corent.db.DBUtil.GetObject.force.numeric.to.double")) {
                        return new Long(s);
                    }
                    return new Double(s);
                case Types.SMALLINT:
                    return new Integer(0);
                }
            }
            return null;
        }
        log.debug("                                   -> type=" + columntype + "("
                + TypeToString(columntype) + "), result=" + obj);
        if (rs.wasNull() && nullsAllowed) {
            return null;
        }
        return obj;
    }

    /**
     * Liefert einen String mit durch Kommata getrennten Tabellenspaltennamen des angegebenen
     * PersistenceDescriptors.
     *
     * @param pd Der PersistenceDescriptor, zu dem die Tabellenspaltennamen geliefert werden
     *         sollen.
     * @return Eine Liste mit den vollen Namen der Tabellenspalten des angegebenen
     *         PersistenceDesriptors.
     */
    public static String GetColumnnameListAsString(PersistenceDescriptor pd) {
        Vector<Integer> vat = pd.getAttributes();
        StringBuffer sb = new StringBuffer();
        for (int i = 0, len = vat.size(); i < len; i++) {
            Integer at = vat.elementAt(i);
            ColumnRecord cr = pd.getColumn(at.intValue());
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(cr.getFullname());
        }
        return sb.toString();
    }

}
