/*
 * KeyListenerDjinn.java
 *
 * 17.01.2004
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.djinn;


import java.awt.event.*;
import java.util.*;
import javax.swing.*;


/**
 * Mit Hilfe dieses Djinns k&ouml;nnen Komponenten &uuml;ber spezielle KeyListener mit einander 
 * gekoppelt werden.<BR>
 * <HR>
 * 
 * @author O.Lieshoff
 *
 */ 

public class KeyListenerDjinn {

    /** Default-KeyEvent zum &Uuml;bertragen des Fokus auf eine Best&auml;tigungskomponente. */    
    public static final int VKUEBERNEHMEN = KeyEvent.VK_F12;
    /** Default-KeyEvent zum &Uuml;bertragen des Fokus auf eine Abbruchkomponente. */    
    public static final int VKVERWERFEN = KeyEvent.VK_ESCAPE;
    /** Default-KeyEvent zum Weiterschalten des Fokus in Chains und Cycles. */    
    public static final int VKWEITER = KeyEvent.VK_RIGHT;
    /** Default-KeyEvent zum Zur&uuml;ckschalten des Fokus in Chains und Cycles. */    
    public static final int VKZURUECK = KeyEvent.VK_LEFT;
 
    /**
     * Mit Hilfe dieser Methode werden die &uumL,bergebenen Komponenten derart mit einander 
     * verbunden, da&szlig; zwischen ihnen eine Navigation mit den Cursortasten m&ouml;glich 
     * wird. Wir die zyklisch-Flagge gesetzt, so wird der Eingabefokus zyklisch zwischen den 
     * Buttons verschoben (wird z. B. &uuml;ber den rechten Rand hinausnavigiert, so wird der 
     * Eingabefokus auf den am weitesten links stehenden Komponente verschoben. 
     *
     * @param komponenten Die Komponenten, die mit einanderverbunden werden sollen.
     * @param zyklisch Wird diese Flagge gesetzt, verhalten sich die Komponenten entsprechend 
     *     der Beschreibung, andernfalls ist ein Navigieren &uuml;ber der ersten bzw letzten 
     *     Komponente nicht m&ouml;glich.
     */
    public static void CreateRow(Vector komponenten, boolean zyklisch) {
        CreateRow(komponenten, zyklisch, VKZURUECK, VKWEITER);
    }
    
    /**
     * Mit Hilfe dieser Methode werden die &uumL,bergebenen Komponenten derart mit einander 
     * verbunden, da&szlig; zwischen ihnen eine Navigation mit den Cursortasten m&ouml;glich 
     * wird. Wir die zyklisch-Flagge gesetzt, so wird der Eingabefokus zyklisch zwischen den 
     * Buttons verschoben (wird z. B. &uuml;ber den rechten Rand hinausnavigiert, so wird der 
     * Eingabefokus auf den am weitesten links stehenden Komponente verschoben. 
     *
     * @param komponenten Die Komponenten, die mit einanderverbunden werden sollen.
     * @param zyklisch Wird diese Flagge gesetzt, verhalten sich die Komponenten entsprechend 
     *     der Beschreibung, andernfalls ist ein Navigieren &uuml;ber der ersten bzw letzten 
     *     Komponente nicht m&ouml;glich.
     */
    public static void CreateRow(JComponent[] komponenten, boolean zyklisch) {
        CreateRow(komponenten, zyklisch, VKZURUECK, VKWEITER);
    }
    
    /**
     * Mit Hilfe dieser Methode werden die &uumL,bergebenen Komponenten derart mit einander 
     * verbunden, da&szlig; zwischen ihnen eine Navigation mit den Cursortasten m&ouml;glich 
     * wird. Wir die zyklisch-Flagge gesetzt, so wird der Eingabefokus zyklisch zwischen den 
     * Buttons verschoben (wird z. B. &uuml;ber den rechten Rand hinausnavigiert, so wird der 
     * Eingabefokus auf den am weitesten links stehenden Komponente verschoben. 
     *
     * @param komponenten Die Komponenten, die mit einanderverbunden werden sollen.
     * @param zyklisch Wird diese Flagge gesetzt, verhalten sich die Komponenten entsprechend 
     *     der Beschreibung, andernfalls ist ein Navigieren &uuml;ber der ersten bzw letzten 
     *     Komponente nicht m&ouml;glich.
     * @param vkZurueck Ein KeyEvent-Code zum Zur&uuml;ckschalten des Fokus.
     * @param vkWeiter Ein KeyEvent-Code zum Weiterschalten des Fokus.
     */
    public static void CreateRow(Vector komponenten, boolean zyklisch, int vkZurueck, 
            int vkWeiter) {
        JComponent[] comps = new JComponent[komponenten.size()];
        for (int i = 0, len = komponenten.size(); i < len; i++) {
            comps[i] = (JComponent) komponenten.elementAt(i);
        }
        CreateRow(comps, zyklisch, vkZurueck, vkWeiter);
    }
    
    /**
     * Mit Hilfe dieser Methode werden die &uumL,bergebenen Komponenten derart mit einander 
     * verbunden, da&szlig; zwischen ihnen eine Navigation mit den Cursortasten m&ouml;glich 
     * wird. Wir die zyklisch-Flagge gesetzt, so wird der Eingabefokus zyklisch zwischen den 
     * Buttons verschoben (wird z. B. &uuml;ber den rechten Rand hinausnavigiert, so wird der 
     * Eingabefokus auf den am weitesten links stehenden Komponente verschoben. 
     *
     * @param komponenten Die Komponenten, die mit einanderverbunden werden sollen.
     * @param zyklisch Wird diese Flagge gesetzt, verhalten sich die Komponenten entsprechend 
     *     der Beschreibung, andernfalls ist ein Navigieren &uuml;ber der ersten bzw letzten 
     *     Komponente nicht m&ouml;glich.
     * @param vkZurueck Ein KeyEvent-Code zum Zur&uuml;ckschalten des Fokus.
     * @param vkWeiter Ein KeyEvent-Code zum Weiterschalten des Fokus.
     */
    public static void CreateRow(JComponent[] komponenten, boolean zyklisch, int vkZurueck, 
            int vkWeiter) {
        JComponent komponente = null;
        JComponent nachfolger = null;
        JComponent vorgaenger = null;
        for (int i = 0; i < komponenten.length; i++) {
            if ((i == 0) && zyklisch) {
                vorgaenger = komponenten[komponenten.length-1];
            } else if (i > 0) {
                vorgaenger = komponenten[i-1];
            }
            komponente = komponenten[i];
            if ((i == komponenten.length-1) && zyklisch) {
                nachfolger = komponenten[0];
            } else if (i < komponenten.length-1) {
                nachfolger = komponenten[i+1];
            }
            komponente.addKeyListener(new ChainKeyAdapter(vorgaenger, komponente, nachfolger,
                    vkZurueck, vkWeiter));
        }
    }

    /**
     * Generiert in Bezug auf den Eingabefokus einen Zyklus unter den angegebenen Komponenten.
     * <BR><PRE>
     * ENTER       -> Setzt den Eingabefokus auf die n&auml;chste Komponente. Im Falle der 
     *                letzten Komponente des Zyklus wird der Fokus an die erste transferiert.
     * ENTER+SHIFT -> Setzt den Eingabefokus auf die vorherige Komponente. Im Falle der 
     *                ersten Komponente des Zyklus wird der Fokus an die letzte transferiert.
     * ESCAPE      -> Setzt den Eingabefokus auf die Verwerfen-Komponente.
     * F12         -> Setzt den Eingabefokus auf die Uebernehmen-Komponente.
     * </PRE>
     * 
     * @param komponenten Die Liste der Komponenten die in den Zyklus aufgenommen werden sollen.
     * @param uebernehmen Die Uebernehmen-Komponente, die durch Dr&uuml;cken der F12-Taste 
     *     angesteuert werden soll.
     * @param verwerfen Die Verwerfen-Komponente, die durch Dr&uuml;cken der ESCAPE-Taste 
     *     angesteuert werden soll.
     */
    public static void CreateCycle(Vector komponenten, JComponent uebernehmen, 
            JComponent verwerfen) {
        CreateCycle(komponenten, uebernehmen, verwerfen, true, VKZURUECK, VKWEITER, 
                VKUEBERNEHMEN, VKVERWERFEN);
    }
    
    /**
     * Generiert in Bezug auf den Eingabefokus einen Zyklus unter den angegebenen Komponenten.
     * <BR><PRE>
     * ENTER       -> Setzt den Eingabefokus auf die n&auml;chste Komponente. Im Falle der 
     *                letzten Komponente des Zyklus wird der Fokus an die erste transferiert.
     * ENTER+SHIFT -> Setzt den Eingabefokus auf die vorherige Komponente. Im Falle der 
     *                ersten Komponente des Zyklus wird der Fokus an die letzte transferiert.
     * ESCAPE      -> Setzt den Eingabefokus auf die Verwerfen-Komponente.
     * F12         -> Setzt den Eingabefokus auf die Uebernehmen-Komponente.
     * </PRE>
     * 
     * @param komponenten Die Liste der Komponenten die in den Zyklus aufgenommen werden sollen.
     * @param uebernehmen Die Uebernehmen-Komponente, die durch Dr&uuml;cken der F12-Taste 
     *     angesteuert werden soll.
     * @param verwerfen Die Verwerfen-Komponente, die durch Dr&uuml;cken der ESCAPE-Taste 
     *     angesteuert werden soll.
     */
    public static void CreateCycle(JComponent[] komponenten, JComponent uebernehmen, 
            JComponent verwerfen) {
        CreateCycle(komponenten, uebernehmen, verwerfen, true, VKZURUECK, VKWEITER, 
                VKUEBERNEHMEN, VKVERWERFEN);
    }

    /**
     * Generiert in Bezug auf den Eingabefokus einen Zyklus unter den angegebenen Komponenten.
     * <BR><PRE>
     * ENTER       -> Setzt den Eingabefokus auf die n&auml;chste Komponente. Im Falle der 
     *                letzten Komponente des Zyklus wird der Fokus an die erste transferiert.
     * ENTER+SHIFT -> Setzt den Eingabefokus auf die vorherige Komponente. Im Falle der 
     *                ersten Komponente des Zyklus wird der Fokus an die letzte transferiert.
     * ESCAPE      -> Setzt den Eingabefokus auf die Verwerfen-Komponente.
     * F12         -> Setzt den Eingabefokus auf die Uebernehmen-Komponente.
     * </PRE>
     * 
     * @param komponenten Die Liste der Komponenten die in den Zyklus aufgenommen werden sollen.
     * @param uebernehmen Die Uebernehmen-Komponente, die durch Dr&uuml;cken der F12-Taste 
     *     angesteuert werden soll.
     * @param verwerfen Die Verwerfen-Komponente, die durch Dr&uuml;cken der ESCAPE-Taste 
     *     angesteuert werden soll.
     * @param coe Wird diese Flagge gesetzt, so wird beim Be&auml;tigen der 
     *     Enter-Taste, w&auml;hrend ein Button fokussiert ist, eine Action ausgel&ouml;st.  
     */
    public static void CreateCycle(JComponent[] komponenten, JComponent uebernehmen, 
            JComponent verwerfen, boolean coe) {
        Vector v = new Vector();
        for (int i = 0; i < komponenten.length; i++) {
            v.addElement(komponenten[i]);
        }
        CreateCycle(v, uebernehmen, verwerfen, coe, VKZURUECK, VKWEITER, VKUEBERNEHMEN, 
                VKVERWERFEN);
    }

    /**
     * Generiert in Bezug auf den Eingabefokus einen Zyklus unter den angegebenen Komponenten.
     * <BR><PRE>
     * ENTER       -> Setzt den Eingabefokus auf die n&auml;chste Komponente. Im Falle der 
     *                letzten Komponente des Zyklus wird der Fokus an die erste transferiert.
     * ENTER+SHIFT -> Setzt den Eingabefokus auf die vorherige Komponente. Im Falle der 
     *                ersten Komponente des Zyklus wird der Fokus an die letzte transferiert.
     * ESCAPE      -> Setzt den Eingabefokus auf die Verwerfen-Komponente.
     * F12         -> Setzt den Eingabefokus auf die Uebernehmen-Komponente.
     * </PRE>
     * 
     * @param komponenten Die Liste der Komponenten die in den Zyklus aufgenommen werden sollen.
     * @param uebernehmen Die Uebernehmen-Komponente, die durch Dr&uuml;cken der F12-Taste 
     *     angesteuert werden soll.
     * @param verwerfen Die Verwerfen-Komponente, die durch Dr&uuml;cken der ESCAPE-Taste 
     *     angesteuert werden soll.
     * @param coe Wird diese Flagge gesetzt, so wird beim Be&auml;tigen der Enter-Taste, 
     *     w&auml;hrend ein Button fokussiert ist, eine Action ausgel&ouml;st.  
     * @param vkZurueck Ein KeyEvent-Code zum Zur&uuml;ckschalten des Fokus.
     * @param vkWeiter Ein KeyEvent-Code zum Weiterschalten des Fokus.
     * @param vkUebernehmen Ein KeyEvent-Code zum &Uuml;bertragen des Fokus auf die Komponente, 
     *     bei deren Bet&auml;tigung die Daten &uuml;bernommen werden sollen.
     * @param vkVerwerfen Ein KeyEvent-Code zum &Uuml;bertragen des Fokus auf die Komponente, 
     *     bei deren Bet&auml;tigung die Daten verworfen werden sollen.
     */
    public static void CreateCycle(JComponent[] komponenten, JComponent uebernehmen, 
            JComponent verwerfen, boolean coe, int vkZurueck, int vkWeiter, int vkUebernehmen, 
            int vkVerwerfen) {
        Vector v = new Vector();
        for (int i = 0; i < komponenten.length; i++) {
            v.addElement(komponenten[i]);
        }
        CreateCycle(v, uebernehmen, verwerfen, coe, vkZurueck, vkWeiter, vkUebernehmen,
                vkVerwerfen);
    }

    /**
     * Generiert in Bezug auf den Eingabefokus einen Zyklus unter den angegebenen Komponenten.
     * <BR><PRE>
     * ENTER       -> Setzt den Eingabefokus auf die n&auml;chste Komponente. Im Falle der 
     *                letzten Komponente des Zyklus wird der Fokus an die erste transferiert.
     * ENTER+SHIFT -> Setzt den Eingabefokus auf die vorherige Komponente. Im Falle der 
     *                ersten Komponente des Zyklus wird der Fokus an die letzte transferiert.
     * ESCAPE      -> Setzt den Eingabefokus auf die Verwerfen-Komponente.
     * F12         -> Setzt den Eingabefokus auf die Uebernehmen-Komponente.
     * </PRE>
     * 
     * @param komponenten Die Liste der Komponenten die in den Zyklus aufgenommen werden sollen.
     * @param uebernehmen Die Uebernehmen-Komponente, die durch Dr&uuml;cken der F12-Taste 
     *     angesteuert werden soll.
     * @param verwerfen Die Verwerfen-Komponente, die durch Dr&uuml;cken der ESCAPE-Taste 
     *     angesteuert werden soll.
     * @param coe Wird diese Flagge gesetzt, so wird beim Be&auml;tigen der Enter-Taste, 
     *     w&auml;hrend ein Button fokussiert ist, eine Action ausgel&ouml;st.  
     */
    public static void CreateCycle(Vector komponenten, JComponent uebernehmen, 
            JComponent verwerfen, boolean coe) {
        CreateCycle(komponenten, uebernehmen, verwerfen, coe, VKZURUECK, VKWEITER, 
                VKUEBERNEHMEN, VKVERWERFEN);
    }
        
    /**
     * Generiert in Bezug auf den Eingabefokus einen Zyklus unter den angegebenen Komponenten.
     * <BR><PRE>
     * ENTER       -> Setzt den Eingabefokus auf die n&auml;chste Komponente. Im Falle der 
     *                letzten Komponente des Zyklus wird der Fokus an die erste transferiert.
     * ENTER+SHIFT -> Setzt den Eingabefokus auf die vorherige Komponente. Im Falle der 
     *                ersten Komponente des Zyklus wird der Fokus an die letzte transferiert.
     * ESCAPE      -> Setzt den Eingabefokus auf die Verwerfen-Komponente.
     * F12         -> Setzt den Eingabefokus auf die Uebernehmen-Komponente.
     * </PRE>
     * 
     * @param komponenten Die Liste der Komponenten die in den Zyklus aufgenommen werden sollen.
     * @param uebernehmen Die Uebernehmen-Komponente, die durch Dr&uuml;cken der F12-Taste 
     *     angesteuert werden soll.
     * @param verwerfen Die Verwerfen-Komponente, die durch Dr&uuml;cken der ESCAPE-Taste 
     *     angesteuert werden soll.
     * @param coe Wird diese Flagge gesetzt, so wird beim Be&auml;tigen der Enter-Taste, 
     *     w&auml;hrend ein Button fokussiert ist, eine Action ausgel&ouml;st.  
     * @param vkZurueck Ein KeyEvent-Code zum Zur&uuml;ckschalten des Fokus.
     * @param vkWeiter Ein KeyEvent-Code zum Weiterschalten des Fokus.
     * @param vkUebernehmen Ein KeyEvent-Code zum &Uuml;bertragen des Fokus auf die Komponente, 
     *     bei deren Bet&auml;tigung die Daten &uuml;bernommen werden sollen.
     * @param vkVerwerfen Ein KeyEvent-Code zum &Uuml;bertragen des Fokus auf die Komponente, 
     *     bei deren Bet&auml;tigung die Daten verworfen werden sollen.
     *
     * @changed
     *     <P>OLI 12.08.2008 - Debugging: Die Initialisierung der alternativen "Weiter"-Taste 
     *             mit <TT>VK_UNDEFINED</TT>, f&uuml;hrte dazu, da&szlig; auch Umlaute als 
     *             "Weiter"-Taste interpretiert worden sind. Daher in <TT>VK_ENTER</TT> 
     *             uminitialisiert ;o).
     *
     */
    public static void CreateCycle(Vector komponenten, JComponent uebernehmen, 
            JComponent verwerfen, boolean coe, int vkZurueck, int vkWeiter, int vkUebernehmen, 
            int vkVerwerfen) {
         CreateCycle(komponenten, uebernehmen, verwerfen, coe, vkZurueck, vkWeiter, 
                KeyEvent.VK_ENTER, vkUebernehmen, vkVerwerfen);
    }
    
    /**
     * Generiert in Bezug auf den Eingabefokus einen Zyklus unter den angegebenen Komponenten.
     * <BR><PRE>
     * ENTER       -> Setzt den Eingabefokus auf die n&auml;chste Komponente. Im Falle der 
     *                letzten Komponente des Zyklus wird der Fokus an die erste transferiert.
     * ENTER+SHIFT -> Setzt den Eingabefokus auf die vorherige Komponente. Im Falle der 
     *                ersten Komponente des Zyklus wird der Fokus an die letzte transferiert.
     * ESCAPE      -> Setzt den Eingabefokus auf die Verwerfen-Komponente.
     * F12         -> Setzt den Eingabefokus auf die Uebernehmen-Komponente.
     * </PRE>
     * 
     * @param komponenten Die Liste der Komponenten die in den Zyklus aufgenommen werden sollen.
     * @param uebernehmen Die Uebernehmen-Komponente, die durch Dr&uuml;cken der F12-Taste 
     *     angesteuert werden soll.
     * @param verwerfen Die Verwerfen-Komponente, die durch Dr&uuml;cken der ESCAPE-Taste 
     *     angesteuert werden soll.
     * @param coe Wird diese Flagge gesetzt, so wird beim Be&auml;tigen der Enter-Taste, 
     *     w&auml;hrend ein Button fokussiert ist, eine Action ausgel&ouml;st.  
     * @param vkZurueck Ein KeyEvent-Code zum Zur&uuml;ckschalten des Fokus.
     * @param vkWeiter Ein KeyEvent-Code zum Weiterschalten des Fokus.
     * @param vkWeiterAlt Ein alternativer KeyEvent-Code zum Weiterschalten des Fokus.
     * @param vkUebernehmen Ein KeyEvent-Code zum &Uuml;bertragen des Fokus auf die Komponente, 
     *     bei deren Bet&auml;tigung die Daten &uuml;bernommen werden sollen.
     * @param vkVerwerfen Ein KeyEvent-Code zum &Uuml;bertragen des Fokus auf die Komponente, 
     *     bei deren Bet&auml;tigung die Daten verworfen werden sollen.
     */
    public static void CreateCycle(Vector komponenten, JComponent uebernehmen, 
            JComponent verwerfen, boolean coe, int vkZurueck, int vkWeiter, int vkWeiterAlt,
            int vkUebernehmen, int vkVerwerfen) {
        JComponent komponente = null;
        JComponent nachfolger = null;
        JComponent vorgaenger = null;
        for (int i = 0, len = komponenten.size(); i < len; i++) {
            if (i == 0) {
                vorgaenger = (JComponent) komponenten.elementAt(len-1);
            } else if (i > 0) {
                vorgaenger = (JComponent) komponenten.elementAt(i-1);
            }
            komponente = (JComponent) komponenten.elementAt(i);
            if (i == len-1) {
                nachfolger = (JComponent) komponenten.elementAt(0);
            } else if (i < len-1) {
                nachfolger = (JComponent) komponenten.elementAt(i+1);;
            }
            komponente.addKeyListener(new ChainKeyAdapter(vorgaenger, komponente, nachfolger, 
                    uebernehmen, verwerfen, vkZurueck, vkWeiter, vkWeiterAlt, vkUebernehmen, 
                    vkVerwerfen));
        }
    }

}
