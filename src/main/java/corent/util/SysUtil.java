/*
 * SysUtil.java
 *
 * 19.08.2007
 *
 * (c) by O.Lieshoff
 *
 */

package corent.util;


import corent.base.*;

import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;

import javax.swing.*;


/** 
 * Diese Klasse enth&auml;lt eine Sammlung von Methoden zum Zugriff auf bestimmte 
 * Systemparameter (z. B. den Rechnernamen), die hier plattform&uuml;bergreifend implementiert
 * sind.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 26.11.2007 - Erweiterung um die Methode <TT>Execute(String)</TT>.
 * @changed OLI 10.08.2008 - Erweiterung um die Methode <TT>GetKeyCode(String)</TT>.
 * @changed OLI 05.01.2009 - Bei FreeBSD wird die Betriebssystemvariable in der Methode
 *         <TT>GetHostname</TT> nur noch dann gesetzt, wenn in der Property <I>hostname</I> kein
 *         Wert enthalten ist.
 * @changed OLI 05.01.2009 - Debugging in der Methode <TT>GetHostname()</TT>: Falls die
 *         Betriebssystemmethodik nicht funktioniert, wird der Wert aus der Property
 *         <I>hostname</I> gelesen. Nur wenn hier auch kein Wert gefunden wird, wird die
 *         Fehlermeldung angezeigt.
 * @changed OLI 17.07.2009 - Erweiterung um die Methode <TT>equalsRef(Object, Object)</TT>.
 *         Dabei: Formatanpassungen.
 *
 */

public class SysUtil {

    /**
     * Diese Methode f&uuml;hrt die equals-Methode des ersten der beiden angegebenen Objekte
     * mit dem zweiten durch. Allerdings finden <TT>null</TT>-Referenzen besondere Beachtung:
     *
     * <TABLE BORDER=1>
     *     <TR>
     *         <TD><B>oO</B></TD>
     *         <TD><B>o1</B></TD>
     *         <TD><B>Ergebnis</B></TD>
     *     </TR>
     *     <TR>
     *         <TD><B><TT>null</TT></B></TD>
     *         <TD><B><TT>null</TT></B></TD>
     *         <TD><B><TT>true</TT></B></TD>
     *     </TR>
     *     <TR>
     *         <TD><B><TT>null</TT></B></TD>
     *         <TD><B>Referenz</B></TD>
     *         <TD><B><TT>false</TT></B></TD>
     *     </TR>
     *     <TR>
     *         <TD><B>Referenz</B></TD>
     *         <TD><B><TT>null</TT></B></TD>
     *         <TD><B><TT>false</TT></B></TD>
     *     </TR>
     *     <TR>
     *         <TD><B>Referenz</B></TD>
     *         <TD><B>Referenz</B></TD>
     *         <TD><B><TT>o0.equals(o1)</TT></B></TD>
     *     </TR>
     * </TABLE>
     *
     * <P>&nbsp;
     *
     * @param o0 Die Referenz, deren <TT>equals(Object)</TT>-Methode gegebenenfalls
     *         ausgef&uuml;hrt werden soll.
     * @param o1 Die Referenz, die gegebenenfalls als Parameter bei der Ausf&uuml;hrung der
     *         Methode <TT>o0.equals(Object)</TT>-Methode ausgef&uuml;hrt werden soll.
     * @return <TT>true</TT>, falls beide Parameter eine <TT>null</TT>-Referenz sind.
     *         <BR><TT>false</TT>, wenn nur einer der beiden Parameter eine
     *         <TT>null</TT>-Referenz ist.
     *         <BR><TT>o0.equals(o1)</TT>, falls beide Referenzen <U>keine</U>
     *         <TT>null</TT>-Referenz sind.
     *
     * @changed OLI 17.07.2009 - Hinzugef&uuml;gt.
     *
     */
    public static boolean equalsRef(Object o0, Object o1) {
        if ((o0 == null) && (o1 == null)) {
            return true;
        } else if (((o0 == null) && (o1 != null)) || ((o0 != null) && (o1 == null))) {
            return false;
        }
        return o0.equals(o1);
    }

    /**
     * Diese Methode liefert den Namen des Rechners zur&uuml;ck, auf dem die JVM l&auml;ft.
     * <P>Im Fall, da&szlig; die Implementierung f&uuml;r das genutzte Betriebssystem nicht
     * erfolgt ist, wird die Property <TT>hostname</TT> abgefragt. Ist auch diese nicht gesetzt
     * wird eine aussagekr&auml;ftige Fehlermeldung ausgegeben und ein Nullpointer
     * zur&uuml;ckgereicht.
     * <P><I><B>Wichtig:</B> F&uuml;r FreeBSD-Rechner ist das Setzen der Property "hostname"
     * notwendig, da das Betriebssystem ein Auslesen der Betriebssystemvariablen nicht
     * zuzulassen scheint.</I>
     *
     * @return Der Rechnername der Maschine auf dem die JVM l&auml;ft, bzw. der Inhalt der
     *         Property "hostname", oder <TT>null</TT>, wenn diese auch nicht gesetzt ist.
     *
     * @changed OLI 05.01.2009 - Debugging: Falls die Betriebssystemmethodik nicht funktioniert,
     *         wird der Wert aus der Property <I>hostname</I> gelesen. Nur wenn hier auch kein
     *         Wert gefunden wird, wird die Fehlermeldung angezeigt.
     *
     */
    // TODO OLI 10.06.2010 - Holen des Namens durch java.net.InetAddress pruefen.
    public static String GetHostname() {
        String osn = System.getProperty("os.name").toLowerCase();
        String hn = null;
        if (osn.startsWith("windows")) {
            hn = System.getenv("COMPUTERNAME");
        } else if (osn.equals("freebsd")) {
            // Das funktioniert leider nicht. Offenbar lassen sich OS-Variablen nicht auslesen.
            // (Zugriffsschutz ?!?).
            // OLI 28.05.2008
            hn = System.getenv("HOSTNAME");
        } else if (osn.equals("linux")) {
            hn = System.getenv("HOST");
        }
        if (hn == null) {
            hn = System.getProperty("hostname");
        }
        if (hn == null) {
            System.out.println("\n\n-----------------------------------------------------------"
                    + "--------------------");
            System.out.println("\nSysUtil.GetHostname() returns null or is not implemented for " 
                    + osn + " (" + hn + ")");
            System.out.println("\nSet property \"hostname\" with the hosts name!");
            System.out.println("\n-------------------------------------------------------------"
                    + "------------------");
        }
        return hn;
    }

    /**
     * Ein Aufruf dieser Methode gibt eine Liste der in der JVM befindlichen Properties als
     * Name-Wert-P&auml;rchen auf der Konsole aus bzw. liefert eine Liste mit Strings des
     * selben Inhalts.
     *
     * @param output Wird diese Flagge gesetzt, so werden die Wertep&auml;rchen auf der Console
     *         ausgegeben.
     * @return Eine Liste mit String, die im Format "Name=Wert" alle in der JVM bekannten
     *         Properties enth&auml;lt.
     */
    public static List<String> ShowProperties(boolean output) {
        class PropertyEntry implements Comparable {
            public String pn = "";
            public String pv = "";
            public PropertyEntry(String pn, String pv) {
                super();
                this.pn = pn;
                this.pv = pv;
            }
            public String toString() {
                return this.pn + "=" + this.pv;
            }
            public int compareTo(Object obj) {
                PropertyEntry pe = (PropertyEntry) obj;
                return this.pn.compareTo(pe.pn);
            }
        };
        SortedVector sv = new SortedVector();
        for (Enumeration e = System.getProperties().propertyNames(); e.hasMoreElements(); ) {
            String pn = (String) e.nextElement();
            String pv = System.getProperty(pn);
            sv.addElement(new PropertyEntry(pn, pv));
        }
        for (int i = 0, len = sv.size(); i < len; i++) {
            System.out.println(sv.elementAt(i));
        }
        return sv;
    }

    /**
     * F&uuml;hrt die angegebene Kommandozeile ohne R&uuml;cksicht auf das Ergebnis aus.
     *
     * @param cmd Das auszuf&uuml;hrende Kommando.
     *
     * @changed OLI 26.11.2007 - Hinzugef&uuml;gt.
     *
     */
    public static void Execute(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\nCAUSED BY method corent.util.SysUtil.Execute(" + cmd + ")");
        }
    }

    /**
     * Liefert einen VK-Tastaturcode zum angegebenen String. Der String muss den Bezeichner 
     * des VK-Tastaturcodes (z. B. VK_ESCAPE) enthalten.
     *
     * @param s Der String mit dem VK-Tastaturcode.
     * @return Der int-Wert zum angegebenen VK-Tastaturcode.
     * @throws IllegalArgumentException Falls der angegebene String keinen VK-Tastaturcode 
     *         zugeordnet ist.
     *
     * @changed OLI 10.08.2008 - Hinzugef&uuml;gt.
     *
     */
    public synchronized static int GetKeyCode(String s) throws IllegalArgumentException {
        try {
            KeyEvent ke = new KeyEvent(new JButton(), KeyEvent.KEY_TYPED, 0, 0,
                    KeyEvent.VK_UNDEFINED, '\0');
            Field f = KeyEvent.class.getField(s);
            return f.getInt(ke);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("ERROR: There is no key code for \"" + s + "\".");
    }

}
