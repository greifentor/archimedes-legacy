/*
 * DBFactoryUtil.java
 *
 * 06.06.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


import corent.base.*;
import corent.base.dynamic.*;
import corent.crypta.*;
import corent.dates.*;
import corent.db.*;
import corent.djinn.*;

import java.sql.*;
import java.util.*;


/**
 * Mit Hilfe der Methoden dieser Klassen k&ouml;nnen verschiedene Routine-Aktionen 
 * durchgef&uuml;hrt werden, die von den DBFactory-Implementierungen genutzt werden k&ouml;nnen.
 * <P>&Uuml;ber die Property <I>corent.db.xs.DBFactoryUtil.suppress.activatedflag.replace</I> 
 * (Boolean) kann ein Modus deaktiviert werden, bei dem beim Speichern die Statusspalte mit dem
 * korrekten Wert aus den Methoden <TT>getActivatedValue()</TT> und <TT>getDeactivatedValue()
 * </TT> belegt wird. Stattdessen wird die Boolean-Repr&auml;sentation des dieser Klasse 
 * genutzt.
 *
 * <P>Die Property <I>corent.djinn.ViewComponent.maximum</I> kann die Anzahl der in ein 
 * <TT>SelectionTableModel</TT> einzulesenden Datens&auml;tze auf den angegebenen Wert 
 * (gr&ouml;&szlig;er 0) limitieren. Die Einstellung gilt f&uuml;r alle Klassen. Wird der Name
 * der Property um einen Punkt und den Namen einer Klasse erweitert (z. B. 
 * corent.djinn.ViewComponent.maximum.ceres.dyo.scheme.Artikel f&uuml;r die Ceres-Artikelklasse)
 * gilt diese Angabe nur f&uuml;r Zugriff &uuml;ber die DBFactory der angegebenen Klasse. Diese
 * Angabe hebelt den Wert der Property <I>corent.djinn.ViewComponent.maximum</I> f&uuml;r die 
 * angegebene Klasse aus. Wird die spezialisierte Property auf den Wert 0 gesetzt, werden 
 * f&uuml;r die angegebene Klasse alle Datens&auml;tze ber&uuml;cksichtigt.
 *
 * <P>Mit Hilfe der Properties <I>corent.db.xs.DBFactoryUtil.replace.*</I> k&ouml;nnen 
 * Zeichenketten definiert werden, die beim Schreiben &uuml;ber die Write-Methode 
 * (UpdateStatement- und InsertStatement-Aufrufe) gegen andere Zeichenketten ausgetauscht 
 * werden. Die Property <I>corent.db.xs.DBFactoryUtil.replace.count</I> gibt die Anzahl der 
 * konfigurierten Austausche vor, die in der angegeben Reihenfolge ausgef&uuml;hrt werden. mit
 * Hilfe der Properties <I>corent.db.xs.DBFactoryUtil.replace.[x].pattern</I> und
 * <I>corent.db.xs.DBFactoryUtil.replace.[x].pattern</I> werden die auszutauschendne 
 * Zeichenketten definiert. Die "pattern"-Property enth&auml;lt das Muster, das gegen die 
 * &uuml;ber die "replaceto"-Property angegebene Zeichenkette ausgetauscht werden soll. Das
 * folgende Beispiel zeigt eine Konfiguration, die alle Tabs gegen die Zeichenkette "$TAB" 
 * austauscht:<PRE>
 *corent.db.xs.DBFactoryUtil.replace.count=1
 *corent.db.xs.DBFactoryUtil.replace.0.pattern=\t
 *corent.db.xs.DBFactoryUtil.replace.0.replaceto=$TAB</PRE>
 *
 * <P>Mit Hilfe der Property <I>corent.db.xs.DBFactoryUtil.long.keys</I> kann ein Modus 
 * aktiviert werden, der bei Schl&uuml;sseln einen gr&ouml;&szlig;eren Wertebereich 
 * zul&auml;&szlig;t.
 *
 * <P>&Uuml;ber die Property <I>corent.db.xs.DBFactoryUtil.user.columnname</I> kann ein Name
 * f&uuml;r die Tabellenspalte gesetzt werden, in der eine Referenznummer auf den &auml:ndernden
 * User zu einem Datensatz hinterlegt werden kann. Dies findet z. B. im Zusammenspiel mit den
 * Reconstructables Anwendung.
 *
 * <P>Mit Hilfe der Property 
 * <I>corent.db.xs.DBFactoryUtil.alternative.class.for.[Tabellenname].[Spaltenname]</I> kann 
 * eine alternative Klasse zu einer Tabellenspalte f&uuml;r Erzeugung eines SelectionTableModels
 * definiert werden. Derzeit sind nur die Klassennamen <TT>java.lang.Boolean</TT>, 
 * <TT>corent.dates.PDate</TT>, <TT>corent.dates.PTime</TT>, <TT>corent.dates.PTimestamp</TT>
 * und <TT>corent.dates.LongPTimestamp</TT> erlaubt. Hiermit kann unteranderem eine korrekte
 * Datumsangabe bei Feldern erreicht werden, die zur Bildung der SelectionTableModels per Join
 * zur eigentlich angezeigten Tabelle hinzugef&uuml;gt werden.
 *
 * <P>Im Reconstructablebereich lassen sich die Join-Klauseln &uuml;ber die Property
 * <I>corent.db.xs.DBFactoryUtil.reconstructable.TABELLENNAME.extension</I> anpassen
 *
 * @author O.Lieshoff
 *
 * @changed
 *     OLI 24.08.2007 - Erweiterung der Dokumentation hinsichtlich der Limitierung der 
 *             einzulesenden Datens&auml;tze.
 *     <P>OLI 10.01.2008 - Erweiterung der Update- und Insert-Methoden (ohne Batches) um die 
 *             F&auml;higkeit &uuml;ber Properties konfigurierbare Zeichenkettenaustausche (vor
 *             der eventuellen Kodierung) durchzuf&uuml;hren (Beschreibung siehe oben).
 *     <P>OLI 14.01.2008 - &Auml;nderung der Schl&uuml;sselbehandnlung von <TT>int</TT> auf
 *             <TT>long</TT>, um auch f&uuml;r gro&szlig;e Schl&suuml;ssel gewappnet zu 
 *             sein. Diese Option wird &uuml;ber das Setzen der Property 
 *             <I>corent.db.xs.DBFactoryUtil.long.keys</I> aktiviert.
 *     <P>OLI 15.01.2008 - Erweiterung der initialen Id auf die Konfigurierbarkeit &uuml;ber die 
 *             Property <I>corent.db.xs.DBFactoryUtil.pk.initial</I> in der Methode 
 *             <TT>Generate(Persistent, Connection, GenerateExpander)</TT>.
 *     <P>OLI 20.02.2008 - Das Schl&uuml;sselmaximum in der Methode <TT>Generate(Persistent, 
 *             Connection, GenerateExpander)</TT> greift nun nur noch, wenn der konfigurierte
 *             Maximalwert gr&ouml;&szlig; als 0 ist.
 *     <P>OLI 25.04.2008 - Anpassungen Zeichenkodierung.
 *     <P>OLI 29.04.2008 - Einbau der Konfigurierbarkeit von alternativen Klassen zur 
 *             Ber&uuml;cksichtigung in der Suchanzeige in der Methode 
 *             <TT>GetSelectionView(Persistent, String, String, Connection, boolean)</TT>. Die 
 *             alternativen Klassen k&ouml;nnen &uuml;ber eine Property systemweit anhand des
 *             qualifizierten Spaltennamens angegeben werden.
 *     <P>OLI 29.09.2008 - Umbau der <TT>GetDBString(Object)</TT>-Methode nach Anpassung der
 *             Hochkommata-Ersetzung in DBUtil.
 *     <P>OLI 30.09.2008 - Einbau von Funktionen zum Einbau von Distincts in die Selektionen.
 *     <P>
 *
 */
 
public class DBFactoryUtil {
    
    /** Ein Integerwert f&uuml;r Boolean-Felder in der Datenbank. */
    public static int BOOLEANTRUE = 1;
    /** Der Coder zur Kodierung und Dekodierung von Daten. */
    public static Coder CODER = null;
    
    /**
     * Wandelt den &uuml;bergebenen PersistenceDescriptor und die Where-Klausel in ein 
     * Select-Statement zum Einlesen einer Objektliste um.
     *
     * @param desc Der PersistenceDescriptor, zu dem ein Select-Statement gebildet werden soll.
     * @param where Die Where-Klausel zum Statement.
     * @param dist Wenn diese Flagge gesetzt ist, wird das Select-Statement um eine 
     *         Distinct-Angabe erweitert.
     * @throws IllegalArgumentException Falls die Prim&auml;rschl&uuml;ssel-Teile aus mehreren 
     *         Tabellen stammen.
     *
     * @changed OLI 26.06.2009 - Erweiterung um die Auswertung der Property
     *         "corent.db.xs.DBFactoryUtil.reconstructable.TABELLENNAME.extension" zur
     *         differenzierten Einschr&auml;nkung von Joins auf Reconstructable-Tabellen.
     */
    public static String SelectStatement(PersistenceDescriptor desc, String where, boolean dist)
            {
        int counter = 0;
        String ltname = null;
        String maname = System.getProperty(
                "corent.db.xs.DBFactoryUtil.reconstructable.column.modifiedat", "ModifiedAt");
        String tablename = null;
        StringBuffer sb = new StringBuffer("");
        StringBuffer sb0 = new StringBuffer("");
        Vector<Integer> attributes = desc.getAttributes();
        for (int i = attributes.size()-1; i >= 0; i--) {
            ColumnRecord cr = desc.getColumn(attributes.elementAt(i).intValue());
            if (cr != null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                if (cr.isPkMember()) {
                    if (tablename == null) {
                        tablename = cr.getTablename();
                    } else if (!cr.getTablename().equalsIgnoreCase(tablename)) {
                        throw new IllegalArgumentException("Die Primaerschluessel-Mitglieder "
                                + "der muessen aus der gleichen Tabelle stammen (" + tablename 
                                + " != " + cr.getTablename() + "!");
                    }
                }
                sb.append(cr.getFullname()).append(" as field" + (counter++));
            }
        }
        sb = new StringBuffer("select ").append((dist ? "distinct " : "")).append(sb).append(
                "\nfrom ").append(tablename);
        Vector<JoinDescriptor> vjd = desc.getSelectionJoins();
        for (int i = 0, len = vjd.size(); i < len; i++) {
            ltname = vjd.elementAt(i).getLeftColumn().getTablename();
            if (!sb.toString().contains("left outer join " + ltname + " on ") && !ltname.equals(
                    tablename)) {
                if (Boolean.getBoolean("corent.db.xs.DBFactoryUtil.reconstructable." + ltname))
                        {
                    sb0 = new StringBuffer(" and (").append(ltname).append(".").append(maname
                            ).append("=-1 or ").append(ltname).append(".").append(maname
                            ).append(" is null)");
                    if (System.getProperty("corent.db.xs.DBFactoryUtil.reconstructable."
                            + ltname + ".extension") != null) {
                        sb0.append(" and ").append(System.getProperty(
                                "corent.db.xs.DBFactoryUtil.reconstructable." + ltname
                                + ".extension"));
                    }
                } else {
                    sb0 = new StringBuffer("");
                }
                sb.append(" left outer join ").append(ltname).append(" on ").append(
                        vjd.elementAt(i).getLeftColumn().getFullname()).append("=").append(
                        vjd.elementAt(i).getRightColumn().getFullname()).append((sb0.length() 
                        > 0 ? sb0.toString() : "")).append("\n");
            }
        }
        /*
        for (int i = 0, len = vjd.size(); i < len; i++) {
            if (!sb.toString().contains("left outer join " + vjd.elementAt(i).getLeftColumn(
                    ).getTablename() + " on ") && !vjd.elementAt(i).getLeftColumn(
                    ).getTablename().equals(tablename)) {
                sb.append("\nleft outer join ").append(vjd.elementAt(i).getLeftColumn(
                        ).getTablename()).append(" on ").append(vjd.elementAt(i).getLeftColumn(
                        ).getFullname()).append("=").append(vjd.elementAt(i).getRightColumn(
                        ).getFullname());
            }
        }
        */
        if ((where != null) && (where.length() > 0))  {
            sb.append("\nwhere ").append(where);
        }        
        return sb.toString();
    }
    
    /**
     * Erzeugt einen String mit den zu selektierenden Feldern des angegebenen 
     * PersistenceDescriptors.
     *
     * @param desc Der PersistenceDescriptor, zu dem ein Feldliste ermittelt werden soll.
     */
    public static String GetSelectionFields(PersistenceDescriptor desc) {
        StringBuffer sb = new StringBuffer("");
        Vector<Integer> attributes = desc.getAttributes();
        for (int i = attributes.size()-1; i >= 0; i--) {
            ColumnRecord cr = desc.getColumn(attributes.elementAt(i).intValue());
            if (cr != null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(cr.getFullname());
            }
        }
        return sb.toString();
    }
    
    /**
     * Ermittelt die Anzahl der zu selektierenden Felder aus dem &uuml;bergebenen 
     * PersistenceDescriptor.
     *
     * @param desc Der PersistenceDescriptor, zu dem die Anzahl der zu selektierenden Felder 
     *         ermittelt werden soll.
     */
    public static int GetSelectionFieldCount(PersistenceDescriptor desc) {
        return desc.getAttributes().size();
    }
    
    /**
     * Generiert f&uuml;r das &uuml;bergebene Persistent-Object ein update-Statement.
     *
     * @param p Das Persistent-Objekt, zu dem das Update-Statement generiert werden soll.
     * @throws IllegalArgumentException Falls die Attribute aus verschiedenen Tabellen stammen.
     *
     * @changed
     *     OLI 10.01.2007 - Erweiterung um die Konfiguration eines automatischen 
     *             Zeichenkettentausches.
     *
     */
    public static String UpdateStatement(Persistent p) {
        PersistenceDescriptor desc = p.getPersistenceDescriptor();
        String pattern = null;
        String replaceto = null;
        String tablename = null;
        StringBuffer keys = new StringBuffer("");
        StringBuffer sb = new StringBuffer("");
        Vector<Integer> attributes = desc.getAttributes();
        for (Integer it : attributes) {
            ColumnRecord cr = desc.getColumn(it);           
            if (cr != null) {
                if (tablename == null) {
                    tablename = cr.getTablename();
                } else if (!cr.getTablename().equalsIgnoreCase(tablename)) {
                    System.out.println("Warnung: Die zuschreibenden Spalten muessen der "
                            + "der gleichen Tabelle angehoeren (" + tablename + " != " 
                            + cr.getTablename() + "!");
                    continue;
                    /*
                    throw new IllegalArgumentException("Die zuschreibenden Spalten muessen der "
                            + "der gleichen Tabelle angehoeren (" + tablename + " != " 
                            + cr.getTablename() + "!");
                    */
                }
                if (cr.isPkMember()) {
                    if (keys.length() > 0) {
                        keys.append(") and (");
                    }
                    Object o = p.get(cr.getAttribute());
                    if (p instanceof DynamicObject) {
                        o = ((DynamicObject) p).get(cr.getColumnname());
                    }
                    if (o instanceof PDate) {
                        o = new Integer(((PDate) o).toInt());
                    } else if (o instanceof PTime) {
                        o = new Integer(((PTime) o).toInt());
                    } else if (o instanceof PTimestamp) {
                        o = new Long(((PTimestamp) o).toLong());
                    } else if (o instanceof LongPTimestamp) {
                        o = new Long(((LongPTimestamp) o).toLong());
                    }
                    keys.append(cr.getColumnname()).append("=").append(GetDBString(o));
                }
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                Object o = null;
                if (p instanceof DynamicObject) {
                    o = ((DynamicObject) p).get(cr.getColumnname());
                } else {
                    o = p.get(cr.getAttribute());
                }
                if  ((o instanceof String) && Integer.getInteger(
                        "corent.db.xs.DBFactoryUtil.replace.count", 0) > 0) {
                    for (int i = 0; i < Integer.getInteger(
                            "corent.db.xs.DBFactoryUtil.replace.count"); i++) {
                        pattern = System.getProperty("corent.db.xs.DBFactoryUtil.replace." + i + 
                                ".pattern", ""); 
                        replaceto = System.getProperty("corent.db.xs.DBFactoryUtil.replace." 
                                + i + ".replaceto", "");
                        if (pattern.length() > 0) {
                            o = o.toString().replace(pattern, replaceto);
                        }
                    }
                    
                }
                if  ((o instanceof String) && cr.isKodiert()) {
                    if (CODER == null) {
                        System.out.println("\n\nWARNUNG: Kodierung kann nicht durchgefuehrt "
                                + "werden wenn DBFactoryUtil.CODER == null!");
                    } else {
                        o = CODER.encode(o.toString());
                    }
                }
/*                
System.out.print("\n\nStatuskontrolle:"
        + "\nProperty: " + Boolean.getBoolean("corent.db.xs.DBFactoryUtil.suppress.activatedflag.replace")
        + "\nDeactivatable: " + (p instanceof Deactivatable)
        + "\nBoolean: " + (o instanceof Boolean)
        + "\no: " + o);
        */
                if (!Boolean.getBoolean(
                        "corent.db.xs.DBFactoryUtil.suppress.activatedflag.replace") 
                        && (p instanceof Deactivatable) && (o instanceof Boolean)) {
                    Deactivatable da = (Deactivatable) p;
/*
System.out.print("\nStatusColumn: " + da.getStatusColumn());
System.out.print("\nStatusColumn: " + cr.getFullname());
*/
                    if (da.getStatusColumn().equals(cr.getFullname())) {
                        if (o == Boolean.TRUE) {
                            o = da.getActivatedValue();
                        } else {
                            o = da.getDeactivatedValue();
                        }
                    }
                }
                sb.append(cr.getColumnname()).append("=").append(GetDBString(o));
            }
        }
        sb = new StringBuffer("update ").append(tablename).append("\nset ").append(sb).append(
                "\nwhere ").append("(").append(keys).append(")");
        return sb.toString();
    }
    
    /**
     * Generiert f&uuml;r das &uuml;bergebene Persistent-Object ein update-Statement.
     *
     * @param p Das Persistent-Objekt zu dem das Insert-Statement generiert werden soll.
     * @throws IllegalArgumentException Falls die Attribute aus verschiedenen Tabellen stammen.
     *
     * @changed
     *     OLI 10.01.2007 - Erweiterung um die Konfiguration eines automatischen 
     *             Zeichenkettentausches.
     *
     */
    public static String InsertStatement(Persistent p) {
        PersistenceDescriptor desc = p.getPersistenceDescriptor();
        String pattern = null;
        String replaceto = null;
        String tablename = null;
        StringBuffer spalten = new StringBuffer("");
        StringBuffer sb = new StringBuffer("");
        Vector<Integer> attributes = desc.getAttributes();
        for (Integer it : attributes) {
            ColumnRecord cr = desc.getColumn(it);
            if (cr != null) {
                if (tablename == null) {
                    tablename = cr.getTablename();
                } else if (!cr.getTablename().equalsIgnoreCase(tablename)) {
                    System.out.println("Warnung: Die zuschreibenden Spalten muessen der "
                            + "der gleichen Tabelle angehoeren (" + tablename + " != " 
                            + cr.getTablename() + "!");
                    continue;
                    /*
                    throw new IllegalArgumentException("Die zuschreibenden Spalten muessen der "
                            + "der gleichen Tabelle angehoeren (" + tablename + " != " 
                            + cr.getTablename() + "!");
                    */
                }
                if (sb.length() > 0) {
                    sb.append(", ");
                    spalten.append(", ");
                }
                Object o = null;
                if (p instanceof DynamicObject) {
                    o = ((DynamicObject) p).get(cr.getColumnname());
                } else {
                    o = p.get(cr.getAttribute());
                }
                if (o instanceof PDate) {
                    o = new Integer(((PDate) o).toInt());
                } else if (o instanceof PTime) {
                    o = new Integer(((PTime) o).toInt());
                } else if (o instanceof PTimestamp) {
                    o = new Long(((PTimestamp) o).toLong());
                } else if (o instanceof LongPTimestamp) {
                    o = new Long(((LongPTimestamp) o).toLong());
                }
                if  ((o instanceof String) && Integer.getInteger(
                        "corent.db.xs.DBFactoryUtil.replace.count", 0) > 0) {
                    for (int i = 0; i < Integer.getInteger(
                            "corent.db.xs.DBFactoryUtil.replace.count"); i++) {
                        pattern = System.getProperty("corent.db.xs.DBFactoryUtil.replace." 
                                + i + ".pattern", ""); 
                        replaceto = System.getProperty("corent.db.xs.DBFactoryUtil.replace." 
                                + i + ".replaceto", "");
                        if (pattern.length() > 0) {
                            o = o.toString().replace(pattern, replaceto);
                        }
                    }
                    
                }
                if  ((o instanceof String) && cr.isKodiert()) {
                    if (CODER == null) {
                        System.out.println("\n\nWARNUNG: Kodierung kann nicht durchgefuehrt "
                                + "werden wenn DBFactoryUtil.CODER == null!");
                    } else {
                        o = CODER.encode(o.toString());
                    }
                }
                sb.append(GetDBString(o));
                spalten.append(cr.getColumnname());
            }
        }
        sb = new StringBuffer("insert into ").append(tablename).append(" (").append(spalten
                ).append(")\nvalues (").append(sb).append(")");
        return sb.toString();
    }
    
    /**
     * Generiert f&uuml;r das &uuml;bergebene Persistent-Object ein delete-Statement.
     *
     * @param p Das Persistent-Objekt, f&uuml;r das das Delete-Statement generiert werden soll.
     * @param forced Diese Flagge ist zu setzen, wenn das Objekt in jedem Fall physisch aus der
     *         der Datenbank entfernt werden soll (auch wenn es Deactivatable implementiert).
     * @throws IllegalArgumentException Falls die Attribute aus verschiedenen Tabellen stammen.
     */
    public static String DeleteStatement(Persistent p, boolean forced) {
        PersistenceDescriptor desc = p.getPersistenceDescriptor();
        String tablename = null;
        StringBuffer keys = new StringBuffer("");
        StringBuffer sb = new StringBuffer("");
        Vector<Integer> attributes = desc.getAttributes();
        for (Integer it : attributes) {
            ColumnRecord cr = desc.getColumn(it);
            if (cr != null) {
                if (tablename == null) {
                    tablename = cr.getTablename();
                } else if (!cr.getTablename().equalsIgnoreCase(tablename)) {
                    throw new IllegalArgumentException("Die zuschreibenden Spalten muessen der "
                            + "der gleichen Tabelle angehoeren (" + tablename + " != " 
                            + cr.getTablename() + "!");
                }
                if (cr.isPkMember()) {
                    if (keys.length() > 0) {
                        keys.append(") and (");
                    }
                    Object o = p.get(cr.getAttribute());
                    keys.append(cr.getColumnname()).append("=").append(GetDBString(o));
                }
            }
        }
        if (!forced && (p instanceof Deactivatable)) {
            Deactivatable da = (Deactivatable) p;
            sb = new StringBuffer("update ").append(tablename).append("\nset ").append(
                    da.getStatusColumn().getColumnname()).append("=").append(GetDBString(
                    da.getDeactivatedValue())).append("\nwhere (").append(keys).append(")");
        } else {
            sb = new StringBuffer("delete from ").append(tablename).append("\nwhere (").append(
                    keys).append(")");
        }
        return sb.toString();
    }
    
    /**
     * Generiert f&uuml;r das &uuml;bergebene Persistent-Object ein unique-Statement.
     *
     * @param p Das Persistent-Objekt, f&uuml;r das das unique-Statement generiert werden soll.
     * @throws IllegalArgumentException Falls die Attribute aus verschiedenen Tabellen stammen.
     */
    public static String UniqueStatement(Persistent p) {
        PersistenceDescriptor desc = p.getPersistenceDescriptor();
        String tablename = null;
        StringBuffer keys = new StringBuffer("");
        StringBuffer sb = new StringBuffer("");
        Vector<Integer> attributes = desc.getAttributes();
        for (Integer it : attributes) {
            ColumnRecord cr = desc.getColumn(it);
            if (cr != null) {
                if (tablename == null) {
                    tablename = cr.getTablename();
                } else if (!cr.getTablename().equalsIgnoreCase(tablename)) {
                    throw new IllegalArgumentException("Die zuschreibenden Spalten muessen der "
                            + "der gleichen Tabelle angehoeren (" + tablename + " != " 
                            + cr.getTablename() + "!");
                }
                if (cr.isPkMember()) {
                    if (keys.length() > 0) {
                        keys.append(") and (");
                    }
                    Object o = p.get(cr.getAttribute());
                    keys.append(cr.getColumnname()).append("=").append(GetDBString(o));
                }
            }
        }
        sb = new StringBuffer("select count(*) from ").append(tablename).append("\nwhere not ("
                ).append(keys).append(")");
        String u = new UniqueProcessor(desc.getUniqueClause()).evaluate(p);
        if ((u != null) && (u.length() > 0)) {
            sb.append(" and (").append(u).append(")");
        }
        return sb.toString();
    }
    
    /**
     * Bildet aus dem &uuml;bergebenen Objekt einen datenbankvertr&auml;glichen String.
     *
     * @param o Das in einen String umzuwandelnde Objekt.
     * @return Der dem Objekt entsprechende Datenbank-String.
     *
     * @changed
     *     OLI 29.09.2008 - Umbau der Umwandlungsstrategie nach Anpassung der Methode
     *             <TT>DBUtil.DBString(String)</TT>.
     *     <P>
     *
     */
    public static String GetDBString(Object o) {
        if (o instanceof HasKey) {
            o = ((HasKey) o).getKey();
        }
        if (o instanceof Boolean) {
            return (o.equals(Boolean.FALSE) ? "0" : "" + BOOLEANTRUE);
        } else if (o instanceof Number) {
            return o.toString();
        } else if (o instanceof PDate) {
            return new Integer(((PDate) o).toInt()).toString();
        } else if (o instanceof PTimestamp) {
            return new Long(((PTimestamp) o).toLong()).toString();
        } else if (o instanceof LongPTimestamp) {
            return new Long(((LongPTimestamp) o).toLong()).toString();
        }
        return (o == null ? "NULL" : DBUtil.DBString(o.toString()));
        // return (o == null ? "NULL" : "'" + StrUtil.Replace(o.toString(), "'", "\"") + "'");
    }
    
    /**
     * Liest die Daten aus dem &uuml;bergebenen (offenen) ResultSet in ein Objekt der durch den 
     * Descriptor beschriebenen Klasse.
     *
     * @param rs Das ResultSet, aus dem die Daten gelesen werden sollen.
     * @param desc Der PersistenceDescriptor, der zum &Ouml;ffnen des ResultSet benutzt worden 
     *         ist.
     * @param newone Ein vorgeneriertes Objekt zum bef&uuml;llen (nur n&ouml;tig, falls die 
     *         Klasse nicht &uuml;ber einen parameterlosen Konstruktor verf&uuml;gt.
     * @param offset Erste Leseposition innerhalb der ResultSet-Zeile.
     */
    public static Object GetFromRS(ResultSet rs, PersistenceDescriptor desc, 
            Editable newone, int offset) throws SQLException {
        return GetFromRS(rs, desc, newone, offset, 0, false);
    }
    
    /**
     * Liest die Daten aus dem &uuml;bergebenen (offenen) ResultSet in ein Objekt der durch den 
     * Descriptor beschriebenen Klasse.
     *
     * @param rs Das ResultSet, aus dem die Daten gelesen werden sollen.
     * @param desc Der PersistenceDescriptor, der zum &Ouml;ffnen des ResultSet benutzt worden 
     *     ist.
     * @param newone Ein vorgeneriertes Objekt zum bef&uuml;llen (nur n&ouml;tig, falls die 
     *     Klasse nicht &uuml;ber einen parameterlosen Konstruktor verf&uuml;gt. 
     * @param offset Erste Leseposition innerhalb der ResultSet-Zeile.
     * @param skip Die Anzahl der Spalten, die beim Lesen nicht mit ber&uuml;cksichtigt werden
     *     sollen. Hiermit k&ouml;nnen beispielsweise die Schl&uuml;sselspalten ignoriert 
     *     werden.
     */
    public static Object GetFromRS(ResultSet rs, PersistenceDescriptor desc, 
            Editable newone, int offset, int skip) throws SQLException {
        return GetFromRS(rs, desc, newone, offset, skip, false);
    }
    
    /**
     * Liest die Daten aus dem &uuml;bergebenen (offenen) ResultSet in ein Objekt der durch den 
     * Descriptor beschriebenen Klasse.
     *
     * @param rs Das ResultSet, aus dem die Daten gelesen werden sollen.
     * @param desc Der PersistenceDescriptor, der zum &Ouml;ffnen des ResultSet benutzt worden 
     *         ist.
     * @param newone Ein vorgeneriertes Objekt zum bef&uuml;llen (nur n&ouml;tig, falls die 
     *         Klasse nicht &uuml;ber einen parameterlosen Konstruktor verf&uuml;gt. 
     * @param offset Erste Leseposition innerhalb der ResultSet-Zeile.
     * @param skip Die Anzahl der Spalten, die beim Lesen nicht mit ber&uuml;cksichtigt werden
     *         sollen. Hiermit k&ouml;nnen beispielsweise die Schl&uuml;sselspalten ignoriert 
     *         werden. 
     * @param ignorekeys DERZEIT AUSSER FUNKTION.
     */
    public static Object GetFromRS(ResultSet rs, PersistenceDescriptor desc, 
            Editable newone, int offset, int skip, boolean ignorekeys) throws SQLException {
        try {
            Attributed attr = null;
            boolean debug = Boolean.getBoolean("corent.db.xs.debug");
            int counter = offset + skip;
            Vector<Integer> attributes = desc.getAttributes();
            skip++;
            if (newone == null) {
                attr = (Attributed) desc.getFactoryClass().newInstance();
            } else {
                attr = (Attributed) newone.createObject();
            }
            for (int i = attributes.size()-skip; i >= 0; i--) {
                Integer it = attributes.elementAt(i);
                ColumnRecord cr = desc.getColumn(it.intValue());
                if (cr != null) {
                    if (debug) {
                        System.out.println("corent.db.xs.debug - DBFactoryUtil.GetFromRS(): "
                                + "reading column " + cr.getFullname());
                    }
                    Object obj = DBUtil.GetObject(rs, counter, false);
                    Object defaultvalue = null;
                    if (attr instanceof Dynamic) {
                        defaultvalue = ((Dynamic) attr).get(cr.getColumnname());
                    } else {
                        defaultvalue = attr.get(it);
                    }
                    counter++;
                    if (ignorekeys && cr.isPkMember()) {
                        continue;
                    }
                    if ((cr.getConvertTo() != null) && cr.getConvertTo().getName().equals(
                            "corent.dates.PDate")) {
                        if ((obj == null) || (((Number) obj).intValue() < 1)) {
                            obj = PDate.UNDEFINIERT;
                        } else {
                            obj = new PDate(((Number) obj).intValue());
                        }
                    } else if ((cr.getConvertTo() != null) && cr.getConvertTo().getName(
                            ).equals("corent.dates.PTimestamp")) {
                        if (obj instanceof String) {
                            obj = Double.parseDouble(obj.toString());
                        }
                        if ((obj == null) || (((Number) obj).longValue() < 1)) {
                            obj = PTimestamp.NULL;
                        } else {
                            obj = new PTimestamp(((Number) obj).longValue());
                        }
                    } else if ((cr.getConvertTo() != null) && cr.getConvertTo().getName(
                            ).equals("corent.dates.LongPTimestamp")) {
                        if (obj instanceof String) {
                            obj = Double.parseDouble(obj.toString());
                        }
                        if ((obj == null) || (((Number) obj).longValue() < 1)) {
                            obj = LongPTimestamp.NULL;
                        } else {
                            obj = new LongPTimestamp(((Number) obj).longValue());
                        }
                    } else if (defaultvalue instanceof Boolean) {
                        obj = new Boolean(((Integer) obj).intValue() != 0); 
                    } else if ((obj instanceof String) && (cr.isKodiert())) {
                        if (CODER == null) {
                            System.out.println("\n\nWARNUNG: Dekodierung kann nicht "
                                    + "durchgefuehrt werden wenn DBFactoryUtil.CODER == null!");
                        } else {
                            obj = CODER.decode(obj.toString());
                        }
                    }
                    if (debug) {
                        System.out.println("corent.db.xs.debug - " + attr.getClass().getName() 
                                + " > " + cr + " - " + it + " - " + obj);
                    }
                    if (attr instanceof Dynamic) {
                        ((Dynamic) attr).set(cr.getColumnname(), obj);
                    } else {
                        attr.set(it, obj);
                    }
                }
            }
            return attr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Liest die Daten aus dem &uuml;bergebenen (offenen) ResultSet in einen Vector mit Objekten
     * der durch den Descriptor beschriebenen Klasse.
     *
     * @param rs Das ResultSet, aus dem die Daten gelesen werden sollen.
     * @param desc Der PersistenceDescriptor, der zum &Ouml;ffnen des ResultSet benutzt worden 
     *         ist.
     * @param newone Ein vorgeneriertes Objekt zum bef&uuml;llen (nur n&ouml;tig, falls die 
     *         Klasse nicht &uuml;ber einen parameterlosen Konstruktor verf&uuml;gt. 
     */
    public static Vector GetFromRS(ResultSet rs, PersistenceDescriptor desc, Editable newone) 
            throws SQLException {
        Vector v = new Vector();
        while (rs.next()) {
            v.addElement(GetFromRS(rs, desc, newone, 1, 0, false));
        }
        return v;
    }
    
    /**
     * Diese Methode erzeugt ein SelectionTableModel zum angegebenen Objekt und schr&auml;nkt 
     * die zur Selektion angebotenen Objekte durch die angegebene Where-Klausel ein.
     * <P>Beginnt die Where-Klausel mit dem Pr&auml;fix <I>"$SQL:"</I>, so wird sie nicht weiter
     * &uuml;ber den PersistenceDescriptor manipuliert, sondern direkt an das DBMS 
     * durchgeleitet. Nach dem Pr&auml;fix sind eine Where-Klausel (ohne das Schl&uuml;sselwort
     * "where") und/oder eine order-by-Angabe (mit Schl&uuml;sselwort "order by") erlaubt.
     *
     * @param p Das Persistent-Objekt, zu dem das SelectionTableModel generiert werden soll.
     * @param w Die Where-Klausel, die die Auswahl einschr&auml;nken soll.
     * @param aj Eine Zus&auml;tzliche Join-Klausel.
     * @param c Die Connection, &uuml;ber die die Daten gewonnen werden sollen.
     * @return Ein SelectionTableModel zur benutzerseitigen Auswahl von Objekten aus einer
     *         Datenbanktabelle.
     * @throws SQLException falls bei der Zusammenarbeit mit der Datenbank ein Fehler auftritt.
     */
    public static SelectionTableModel GetSelectionView(Persistent p, String w, String aj, 
            Connection c) throws SQLException {
        return GetSelectionView(p, w, aj, c, false, false);
    }
    
    /**
     * Diese Methode erzeugt ein SelectionTableModel zum angegebenen Objekt und schr&auml;nkt 
     * die zur Selektion angebotenen Objekte durch die angegebene Where-Klausel ein.
     * <P>Beginnt die Where-Klausel mit dem Pr&auml;fix <I>"$SQL:"</I>, so wird sie nicht weiter
     * &uuml;ber den PersistenceDescriptor manipuliert, sondern direkt an das DBMS 
     * durchgeleitet. Nach dem Pr&auml;fix sind eine Where-Klausel (ohne das Schl&uuml;sselwort
     * "where") und/oder eine order-by-Angabe (mit Schl&uuml;sselwort "order by") erlaubt.
     *
     * @param p Das Persistent-Objekt, zu dem das SelectionTableModel generiert werden soll.
     * @param w Die Where-Klausel, die die Auswahl einschr&auml;nken soll.
     * @param aj Eine Zus&auml;tzliche Join-Klausel.
     * @param c Die Connection, &uuml;ber die die Daten gewonnen werden sollen.
     * @param nj Diese Flagge mu&szlig; gesetzt werden, wenn die Join-Klauseln des 
     *         PersistenceDescriptor ignoriert werden sollen. <B>Wichtig:</B> Hierbei ist 
     *         unbedingt Sorge zu tragen, da&szlig; Auswahlfelder ausschlie&szlig;lich aus der 
     *         Tabelle der Klasse stammen.
     * @param suppressFilling Eine Flagge zur Unterdr&uuml;ckung des eigentlichen Lesevorganges.
     *         Auf diese Weise kann eine valides aber leeres Model erzeugt werden.
     * @return Ein SelectionTableModel zur benutzerseitigen Auswahl von Objekten aus einer
     *         Datenbanktabelle.
     * @throws IllegalArgumentException falls sich bei eingeschalteter nj-Option ein Feld in
     *         der Auswahlansicht befindet, das sich nicht in der Prim&auml;rtabelle des 
     *         Persistent-Objektes befindet.
     * @throws SQLException falls bei der Zusammenarbeit mit der Datenbank ein Fehler auftritt.
     *
     * @changed
     *     OLI 29.04.2008 - Einbau der Konfigurierbarkeit von alternativen Klassen zur 
     *             Ber&uuml;cksichtigung in der Suchanzeige.
     *     <P>
     *
     */
    public static SelectionTableModel GetSelectionView(Persistent p, String w, String aj, 
            Connection c, boolean nj, boolean suppressFilling) throws SQLException {
        boolean coded = false;
        boolean sql = false;
        DefaultSelectionTableModel dstm = new DefaultSelectionTableModel();
        PersistenceDescriptor pd = p.getPersistenceDescriptor();
        Vector<ColumnRecord> vsvm = pd.getSelectionViewMembers();
        Vector<ColumnRecord> keys = pd.getKeys();
        Vector<Integer> sort = new Vector<Integer>();
        ColumnRecord keycolumn = keys.elementAt(0);
        Vector<JoinDescriptor> vjd = (nj ? new Vector<JoinDescriptor>() : pd.getSelectionJoins()
                );
        String alternativeClassName = null;
        String ltname = null;
        String maname = System.getProperty(
                "corent.db.xs.DBFactoryUtil.reconstructable.column.modifiedat", "ModifiedAt");
        StringBuffer sb = new StringBuffer("");
        StringBuffer sb0 = new StringBuffer("");
        for (int i = 0, len = keys.size(); i < len; i++) {
            ColumnRecord cr = keys.elementAt(i);  
            if (sb.length() > 0) {
                sb.append(", ");
            } else {
                sb.append("select ").append(((p instanceof DistinctReader) && ((DistinctReader) 
                        p).isSelectDistinct() ? "distinct " : ""));
            }
            sb.append(cr.getFullname()); 
        }
        for (int i = 0, len = vsvm.size(); i < len; i++) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(vsvm.elementAt(i).getFullname());
            if (vsvm.elementAt(i).isKodiert()) {
                dstm.setCoded(i, true);
                coded = true;
            }
            alternativeClassName = System.getProperty(
                    "corent.db.xs.DBFactoryUtil.alternative.class.for." + vsvm.elementAt(i
                    ).getFullname());
            if (nj && !vsvm.elementAt(i).getTablename().equals(keycolumn.getTablename())) {
                throw new IllegalArgumentException("ERROR: you are using a column, which is "
                        + "not within the primary table for class " + p.getClass().getName()
                        + " and no join option is set for selection view!");
            }
            try {
                if ((p instanceof Dynamic) && ((Dynamic) p).getType(vsvm.elementAt(i
                        ).getColumnname()).getName().equals("java.lang.Boolean")) { 
                    dstm.setClassForColumn(i, new Boolean(true).getClass());
                } else if ((p instanceof Dynamic) && ((Dynamic) p).getType(vsvm.elementAt(i
                        ).getColumnname()).getName().equals("corent.dates.PDate")) { 
                    dstm.setClassForColumn(i, new PDate().getClass());
                } else if ((p instanceof Dynamic) && ((Dynamic) p).getType(vsvm.elementAt(i
                        ).getColumnname()).getName().equals("corent.dates.PTime")) { 
                    dstm.setClassForColumn(i, new PTime().getClass());
                } else if ((p instanceof Dynamic) && ((Dynamic) p).getType(vsvm.elementAt(i
                        ).getColumnname()).getName().equals("corent.dates.PTimestamp")) { 
                    dstm.setClassForColumn(i, new PTimestamp().getClass());
                } else if ((p instanceof Dynamic) && ((Dynamic) p).getType(vsvm.elementAt(i
                        ).getColumnname()).getName().equals("corent.dates.LongPTimestamp")) { 
                    dstm.setClassForColumn(i, new LongPTimestamp().getClass());
                }
            } catch (IllegalArgumentException iae) {
                System.out.println("WARNING: illegal access to dynamic object. Maybe attribute "
                        + "called " + vsvm.elementAt(i).getColumnname() + " doesn't exists in "
                        + "class " + p.getClass().getName());
            }
            if (alternativeClassName != null) {
                if (alternativeClassName.equals("java.lang.Boolean")) { 
                    dstm.setClassForColumn(i, new Boolean(true).getClass());
                } else if (alternativeClassName.equals("corent.dates.PDate")) { 
                    dstm.setClassForColumn(i, new PDate().getClass());
                } else if (alternativeClassName.equals("corent.dates.PTime")) { 
                    dstm.setClassForColumn(i, new PTime().getClass());
                } else if (alternativeClassName.equals("corent.dates.PTimestamp")) { 
                    dstm.setClassForColumn(i, new PTimestamp().getClass());
                } else if (alternativeClassName.equals("corent.dates.LongPTimestamp")) { 
                    dstm.setClassForColumn(i, new LongPTimestamp().getClass());
                }
            }
        }
        sb.append("\n").append("from ").append(keycolumn.getTablename()).append("\n");
        for (int i = 0, len = vjd.size(); i < len; i++) {
            ltname = vjd.elementAt(i).getLeftColumn().getTablename();
            if (!sb.toString().contains("left outer join " + ltname + " on ") && !ltname.equals(
                    keycolumn.getTablename())) {
                if (Boolean.getBoolean("corent.db.xs.DBFactoryUtil.reconstructable." + ltname))
                        { 
                    sb0 = new StringBuffer(" and (").append(ltname).append(".").append(maname
                            ).append("=-1 or ").append(ltname).append(".").append(maname
                            ).append(" is null)");
                } else {
                    sb0 = new StringBuffer("");
                }
                sb.append(" left outer join ").append(ltname).append(" on ").append(
                        vjd.elementAt(i).getLeftColumn().getFullname()).append("=").append(
                        vjd.elementAt(i).getRightColumn().getFullname()).append((sb0.length() 
                        > 0 ? sb0.toString() : "")).append("\n");
            }
        }
        if ((aj != null) && (aj.length() > 0)) {
            sb.append(aj).append("\n");
        }
        if (!w.startsWith("$SQL:")) {
            if (p instanceof Deactivatable) {
                if ((w != null) && (w.length() > 0)) {
                    w = "(".concat(w).concat(") and ");
                }
                Deactivatable da = (Deactivatable) p;
                // System.out.println("\n>>>> " + da);
                // System.out.println(">>>> " + da.getStatusColumn());
                try {
                    w = w.concat("(").concat(da.getStatusColumn().getFullname()).concat("<>"
                            ).concat(GetDBString(da.getDeactivatedValue())).concat(")");
                } catch (NullPointerException npe) {
                    System.out.println("\n\nERROR:");
                    System.out.println("da           " + da);
                    System.out.println("statuscolumn " + (da != null ? da.getStatusColumn() 
                            : "<null>"));
                    System.out.println("fullname     " + (da.getStatusColumn() != null ? 
                            da.getStatusColumn().getFullname() : "<null>"));
                    System.out.println("da-value     " + (da.getStatusColumn() != null ? 
                            "" + da.getDeactivatedValue() : "<null>"));
                    throw new NullPointerException("CORENT: Maybe you have no standard state "
                            + "column for class " + p.getClass().getName() + " and forgotten "
                            + "to override the getStateColumn-Method in those class!\n"
                            + npe.getMessage());
                }
            }
        } else {
            sql = true;
            if (w.length() > 5) {
                w = w.substring(5, w.length()).trim();
            } else {
                w = "";
            }
        }
        if ((w != null) && (w.length() > 0)) {
            sb.append(((sql && w.startsWith("order by")) ? "" : "where ")).append(w).append("\n"
                    );
        }
        if (!sql && (p instanceof Ordered)) {
            StringBuffer orderby = new StringBuffer();
            OrderByDescriptor obd = ((Ordered) p).getOrderByDescriptor();
            for (int i = 0, len = obd.getOrderBySize(); i < len; i++) {
                OrderClause oc = obd.getOrderByAt(i);
                if (oc.getColumn() != null) {
                    if (orderby.length() > 0) {
                        orderby.append(", ");
                    }
                    orderby.append(oc.getColumn().getFullname()).append(" ").append((
                            oc.getDirection() == OrderClauseDirection.ASC ? "asc" : "desc"));
                    for (int j = 0, lenj = vsvm.size(); j < lenj; j++) {
                        if (vsvm.elementAt(j).getFullname().equals(oc.getColumn().getFullname())
                                ) {
                            sort.addElement(j);
                            break;
                        }
                    }
                }
            }
            if (orderby.length() > 0) {
                sb.append("order by ").append(orderby);
            }
        }
        int lim = Integer.getInteger("corent.djinn.ViewComponent.maximum", 0);
        if (Integer.getInteger("corent.djinn.ViewComponent.maximum." + p.getClass().getName(), 
                0) > 0) {
            lim = Integer.getInteger("corent.djinn.ViewComponent.maximum." + p.getClass(
                    ).getName(), 0);
        }
        String sbts = sb.toString();
        if (lim > 0) {
            sbts = DBExec.LimitQuery(sbts, lim+1);
        }
        if (!suppressFilling) {
            ResultSet rs = DBExec.Query(c, sbts);
            int len = vsvm.size();
            dstm.setColumnCount(len);
            int count = 0;
            int index = 0;
            int keycount = keys.size();
            String s = "";
            Vector vdstm = new Vector();
            if (coded) {
                vdstm = new SortedVector();
            }
            while (rs.next()) {
                Object key = null;
                if (keycount > 1) {
                    key = new Vector();
                    for (int i = 0; i < keycount; i++) {
                        ((Vector) key).addElement(rs.getObject(i+1));
                    }
                } else {
                    key = rs.getObject(1);
                }
                Vector vs = new Vector();
                for (int i = 0; i < len; i++) {
                    index = (keycount+1)+i;
                    s = rs.getString(index);
                    if ((CODER != null) && dstm.isCoded(i)) {
                        s = CODER.decode(s);
                    }
                    vs.addElement(s);
                }
                count++;
                if ((lim > 0) && (count > lim)) {
                    dstm.setMoreThanLimit(true);
                    break;
                }
                vdstm.addElement(new DefaultSelectionTableRow(key, vs, sort));
            }
            dstm.load(vdstm);
            DBExec.CloseQuery(rs);
        }
        return dstm;
    }
    
    
    /** 
     * Pr&uuml;ft das angegebene Persistent und Dynamic-Objekt auf Schreibbarkeit anhand der 
     * Belegung der Pflichtfelder und liefert im negativen Falle eine Liste der Felder die
     * leer sind (weshalb das Objekt als nicht Schreibbar eingestuft wird).
     *
     * @param p Das Persistent, das auch sein mu&szlig; Dynamic, um eine Pr&uuml;fung zu 
     *         erm&ouml;glichen.
     * @return Liste mit den Spaltennamen der Spalten die korrekt belegt worden sind.
     */
    public static Vector<String> GetNotEmptyColumnnames(Persistent p) {
        Vector<String> vs = new Vector<String>();
        if (p instanceof Dynamic) {
            Dynamic d = (Dynamic) p;
            Vector<String> attr = p.getPersistenceDescriptor().getNotEmptyColumnnames();
            for (int i = 0, len = attr.size(); i < len; i++) {
                String name = attr.elementAt(i);
                Object value = d.get(name);
                if ((value == null) || ((value instanceof String) && (value.equals("")))
                        || ((value instanceof Number) && (((Number) value).doubleValue() <= 0.0)
                        )) {
                    vs.addElement(name);
                } 
            }
        }
        return vs;
    }

    
    /* Implementierung des Interfaces PersistenceFactory. */
    
    /**
     * Liest eine Liste von Persistent-Objekten aus der Datenquelle, die mit der angegebenen 
     * Connection verbunden ist. Die Liste wird eingeschr&auml;nkt durch die angegebene 
     * Where-Klausel.
     *
     * @param p Das Persistent-Objekt, zu dem Daten aus der Datenbank gewonnen werden sollen.
     * @param w Die Where-Klausel, die die Auswahl einschr&auml;nken soll.
     * @param c Die Connection, &uuml;ber die die Daten gewonnen werden sollen.
     * @param o Ein alternativer OrderByDescriptor bzw. <TT>null</TT>, wenn der Default benutzt
     *         werden soll.
     * @param includeRemoved Wird diese Flagge gesetzt, werden auch gel&ouml;schte 
     *         Datens&auml;tze ber&uuml;cksichtigt, falls es sich bei der angegebenen Klasse um
     *         ein Deactivatable handelt.
     * @return Ein Vector mit den eingelesenen Objekten, bzw. ein leerer Vector, falls zu der
     *         angegebenen Where-Klausel keine Objekte gefunden werden konnten.
     * @throws SQLException falls bei der Zusammenarbeit mit der Datenbank ein Fehler auftritt.
     *
     * @changed
     *     OLI 30.09.2008 - Einbau der Pr&uuml;fung auf das Interface <TT>DistinctReader</TT>.
     *     <P>
     */
    public static Vector Read(Persistent p, String w, Connection c, OrderByDescriptor o, 
            boolean includeRemoved) throws SQLException {
        if (Boolean.getBoolean("corent.db.xs.debug")) {
            System.out.println("\nDBFactoryUtil.Read: " + w + ", " + includeRemoved);
        }
        String stmt = SelectStatement(p.getPersistenceDescriptor(), w, 
                (p instanceof DistinctReader ? ((DistinctReader) p).isReadDistinct() : false));
        Vector v = new Vector();
        if ((p instanceof Deactivatable) && !includeRemoved) {
            if ((w != null) && (w.length() > 0)) {
                stmt = stmt.concat(" and ");
            } else {
                stmt = stmt.concat("\nwhere ");
            }
            Deactivatable da = (Deactivatable) p;
            stmt = stmt.concat("(").concat(da.getStatusColumn().getFullname()).concat("<>"
                    ).concat(GetDBString(da.getDeactivatedValue())).concat(")");
        }
        String orderby = "";
        if (p instanceof Ordered) {
            OrderByDescriptor obd = ((Ordered) p).getOrderByDescriptor();
            orderby = obd.toSQL();
        }
        if (o != null) {
            orderby = o.toSQL();
        }
        if (orderby.length() > 0) {
            stmt = stmt.concat(" order by ").concat(orderby);
        }
        ResultSet rs = DBExec.Query(c, stmt);
        v = GetFromRS(rs, p.getPersistenceDescriptor(), (p instanceof Editable ? (Editable) 
                p.createObject() : null));
        DBExec.CloseQuery(rs);
        return v;
    }
    
    /**
     * @deprecated
     *     <P>OLI 20.08.2008 - Implementierung in die Methode <TT>toSQL()</TT> 
     *             des OrderByDescriptors verschoben. Zum einen ist diese nicht statisch, was
     *             schlimmstenfalls zu Nebenl&auml;ufigkeitsproblemen f&uuml;hren kann, zum 
     *             anderen geh&ouml;rt das thematisch eher in den OrderByDescriptor.
     *
     * /
    private static StringBuffer OrderByDescriptorToString(OrderByDescriptor obd) {
        StringBuffer orderby = new StringBuffer();
        for (int i = 0, len = obd.getOrderBySize(); i < len; i++) {
            OrderClause oc = obd.getOrderByAt(i);
            if (oc.getColumn() != null) {
                if (orderby.length() > 0) {
                    orderby.append(", ");
                }
                orderby.append(oc.getColumn().getFullname()).append(" ").append((
                        oc.getDirection() == OrderClauseDirection.ASC ? "asc" : "desc"));
            }
        }
        return orderby;
    }
    */

    public static boolean Write(Persistent p, Connection c) throws SQLException {
        Vector<ColumnRecord> vcr = p.getPersistenceDescriptor().getKeys(); 
        if (vcr.size() > 0) {
            if (DBExec.Update(c, UpdateStatement(p)) == 0) {
                DBExec.Update(c, InsertStatement(p));
            }
        } else {
            DBExec.Update(c, InsertStatement(p));
        }
        return true;
    }
    
    public static boolean WriteBatch(PersistenceDescriptor pd, Vector k, 
            Hashtable<Integer, Object> ta, Connection c) {
        try {
            Vector<ColumnRecord> vcr = pd.getKeys();
            if (vcr.size() > 0) {
                ColumnRecord cr = null;
                Integer it = null;
                Object value = null;
                String tn = vcr.elementAt(0).getTablename();
                StringBuffer sb = new StringBuffer();
                for (Enumeration e = ta.keys(); e.hasMoreElements(); ) {
                    it = (Integer) e.nextElement();
                    value = ta.get(it);
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    if (it.longValue() == -99) {
                        sb.append(System.getProperty(
                                "corent.db.xs.DBFactoryUtil.user.columnname", "ModificationUser"
                                )).append("=").append(value);
                    } else {    
                        cr = pd.getColumn(it.intValue());
                        sb.append(cr.getColumnname()).append("=").append(GetDBString(value));
                    }
                }
                StringBuffer stmt = new StringBuffer("update ").append(tn).append("\nset "
                        ).append(sb).append("\nwhere ").append(GenerateKeylist(vcr, k));
                DBExec.Update(c, stmt.toString());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static StringBuffer GenerateKeylist(Vector<ColumnRecord> vcr, Vector k) {
        Object value = null;
        StringBuffer key = null;
        StringBuffer sb = new StringBuffer();
        int ik = 0;
        int lenk = k.size();
        while (ik < lenk) {
            if (sb.length() > 0) {
                sb.append(" or ");
            }
            key = new StringBuffer();
            for (int i = 0, len = vcr.size(); i < len; i++) {
                ColumnRecord cr = vcr.elementAt(i);
                if (key.length() > 0) {
                    key.append(", ");
                }
                value = k.elementAt(ik);
                key.append(cr.getFullname()).append("=").append(GetDBString(value));
                ik++;
            }
            sb.append("(").append(key).append(")");
        }
        return sb;
    }
    
    /**
     * @changed 
     *     OLI 14.01.2008 - &Auml;nderung der Schl&uuml;sselbehandnlung von <TT>int</TT> auf
     *             <TT>long</TT>, um auch f&uuml;r gro&szlig;e Schl&suuml;ssel gewappnet zu 
     *             sein. Diese Option wird &uuml;ber das Setzen der Property 
     *             <I>corent.db.xs.DBFactoryUtil.long.keys</I> aktiviert.
     *     <P>OLI 15.01.2008 - Erweiterung der initialen Id auf die Konfigurierbarkeit &uuml;ber
     *             die Property <I>corent.db.xs.DBFactoryUtil.pk.initial</I>. Umbau auf 
     *             Mehrprozessf&auml;higkeit. Dabei Performanzerh&ouml;hung.
     *     <P>OLI 05.02.2008 - Nach der Findung des Prim&auml;rschl&uuml;ssels wird 
     *             gepr&uuml;ft, ob der gefundene Wert kleiner als der initiale 
     *             Schl&uuml;sselwert ist. Gegebenenfalls wird er auf den Initialwert gesetzt. 
     *             Vorher wurde der Initialwert nur gesetzt, wenn der gefundene Schl&uuml;ssel 
     *             gleich 0 war.
     *     <P>OLI 20.02.2008 - Die Maximum-Klausel bei der Schl&uuml;sselerzeugung greift nun 
     *             nur noch, wenn das Maximum gr&ouml;&szlig;er als 0 ist.
     */
    public static boolean Generate(Persistent p, Connection c, GenerateExpander ge) {
        boolean generated = false;
        boolean ok = false;
        ColumnRecord pk = null;
        Deactivatable d = null;
        int count = 0;
        int maxcount = Integer.getInteger("corent.db.xs.DBFactoryUtil.pk.try.max", 10000);
        long id = 0;
        long idinit = Long.getLong("corent.db.xs.DBFactoryUtil.pk.initial", 0);
        long idmax = Long.getLong("corent.db.xs.DBFactoryUtil.pk.maximum", 0);
        Object key = null;
        ResultSet rs = null;
        Vector<ColumnRecord> vcr = null;        
        Vector<ColumnRecord> vpk = null;        
        try {
            vcr = p.getPersistenceDescriptor().getKeys(); 
            if (vcr.size() > 0) {
                pk = vcr.elementAt(0);
                while ((maxcount > 0) && (!generated)) {
                    rs = DBExec.Query(c, "select max(" + pk.getFullname() + ") from " 
                            + pk.getTablename() + (idmax > 0 ? " where " + pk.getFullname() + 
                            "<" + idmax : ""));
                    if (rs.next()) {
                        id = rs.getLong(1);
                        if (id < idinit) {
                            id = idinit;
                        }
                        vpk = p.getPersistenceDescriptor().getKeys();
                        if (vpk.size() > 0) {
                            key = new Long(++id);
                            if (ge != null) {
                                key = ge.doChangeKeys(id);
                            }
                            if (!Boolean.getBoolean("corent.db.xs.DBFactoryUtil.long.keys")) {
                                key = new Integer(key.toString());
                            }
                            if (p instanceof Dynamic) {
                                ((Dynamic) p).set(vpk.elementAt(0).getColumnname(), key);
                            } else {
                                p.set(vpk.elementAt(0).getAttribute(), key);
                            }
                            ok = true;
                        }
                    }
                    DBExec.CloseQuery(rs);
                    if (ok) {
                        if (p instanceof Deactivatable) {
                            d = (Deactivatable) p;
                            d.activateObject();
                        }
                        if (ge != null) {
                            ge.doGenerateExpansion(p);
                        }
                        try {
                            count = DBExec.Update(c, InsertStatement(p));
                            if (count != 0) {
                                generated = true;
                            }
                        } catch (Exception e) {
                            ok = false;
                        }
                    }
                    maxcount--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Alte Version: Nicht mehrprozessfaehig und langsam.
        boolean ok = false;        
        try {
            Vector<ColumnRecord> vcr = p.getPersistenceDescriptor().getKeys(); 
            if (vcr.size() > 0) {
                ColumnRecord pk = vcr.elementAt(0);
                ResultSet rs = DBExec.Query(c, "select max(" + pk.getFullname() + ") from " 
                        + pk.getTablename());
                if (rs.next()) {
                    // int id = rs.getInt(1);
                    long id = rs.getLong(1);
                    if (id == 0) {
                        id = Long.getLong("corent.db.xs.DBFactoryUtil.pk.initial", 0);
                    }
                    Vector<ColumnRecord> vpk = p.getPersistenceDescriptor().getKeys();
                    if (vpk.size() > 0) {
                        // Object key = new Integer(++id);
                        Object key = new Long(++id);
                        if (ge != null) {
                            key = ge.doChangeKeys(id);
                        }
                        if (!Boolean.getBoolean("corent.db.xs.DBFactoryUtil.long.keys")) {
                            key = new Integer(key.toString());
                        }
                        if (p instanceof Dynamic) {
                            ((Dynamic) p).set(vpk.elementAt(0).getColumnname(), key);
                        } else {
                            p.set(vpk.elementAt(0).getAttribute(), key);
                        }
                        ok = true;
                    }
                }
                DBExec.CloseQuery(rs);
                if (ok) {
                    if (p instanceof Deactivatable) {
                        Deactivatable d = (Deactivatable) p;
                        d.activateObject();
                    }
                    if (ge != null) {
                        ge.doGenerateExpansion(p);
                    }
                    if (DBExec.Update(c, UpdateStatement(p)) == 0) {
                        DBExec.Update(c, InsertStatement(p));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        return ok;
    }
    
    public static boolean Remove(Persistent p, boolean forced, Connection c) {
        try {
            if (DBExec.Update(c, DeleteStatement(p, forced)) == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean IsUnique(Persistent p, Connection c) {
        String u = p.getPersistenceDescriptor().getUniqueClause();
        if (u != null) {
            try {
                ResultSet rs = DBExec.Query(c, UniqueStatement(p));
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count == 0) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

}
