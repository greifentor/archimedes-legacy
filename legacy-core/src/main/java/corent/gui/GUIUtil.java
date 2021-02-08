/*
 * GUIUtil.java
 *
 * 13.01.2007
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

import javax.swing.*;


/**
 * Diese Klasse enth&auml;lt Methoden, die bei der Arbeit mit der GUI von Nutzen sein 
 * k&ouml;nnen.
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 24.09.2007 - Erweiterung um die Methode <TT>GetKeyCode(String)</TT>.
 *     <P>OLI 11.05.2008 - Erweiterung um die Methode <TT>SetEnabled(Component, boolean)</TT>.
 *
 */
 
public class GUIUtil {
    
    /**
     * Ermittelt die aktuell focussierte Komponente.
     *
     * @param c Eine Komponente aus dem Baum, f&uuml;r den die focussierte bestimmt werden soll.
     * @return Referenz auf die Komponente, die aktuell den Focus innehat.
     */
    public static Component getFocusOwner(Component c) {
        return getFocusOwnerSearcher(c);
    }
    
    private static Component getFocusOwnerSearcher(Component c) {
        if (!c.isFocusOwner()) {
            if (c instanceof JMenu) {
                JMenu cont = (JMenu) c;
                for (int i = 0, len = cont.getMenuComponentCount(); i < len; i++) {
                    c = getFocusOwnerSearcher(cont.getMenuComponent(i));
                    if (c != null) {
                        return c;
                    }
                }
                for (int i = 0, len = cont.getItemCount(); i < len; i++) {
                    c = getFocusOwnerSearcher(cont.getItem(i));
                    if (c != null) {
                        return c;
                    }
                }
            } else if (c instanceof Container) {
                Container cont = (Container) c;
                for (int i = 0, len = cont.getComponentCount(); i < len; i++) {
                    c = getFocusOwnerSearcher(cont.getComponent(i));
                    if (c != null) {
                        return c;
                    }
                }
            }
            return null;
        }
        return c;
    }
    
    /**
     * Liefert zu dem &uuml;bergebenen String, der der Name eine VK-Konstante aus dem 
     * KeyEvent-Objekt enthalten mu&szlig; den entsprechenden Wert aus dem KeyEvent-Objekt.
     *
     * @param s Der Name einer KeyEvent-VK-Konstante.
     * @return Der der Konstanten entsprechende Wert.
     * @throws IllegalArgumentException Falls der in s &uuml;bergebene Name kein g&uuml;ltiger
     *     KeyEvent-Konstantenname ist.
     */
    public static int GetKeyCode(String s) {
        if (!s.startsWith("VK_")) {
            throw new IllegalArgumentException("ERROR: GUIUtil.GetKeyCode(String) must be "
                    + "called with a KeyEvent VK_ constant name!\n" + s + " is not such a name."
                    );
        }
        Class c = KeyEvent.class;
        int erg = -1;
        try {
            Field f = c.getField(s);
            erg = f.getInt(null);
        } catch (NoSuchFieldException nsfe) {
            throw new IllegalArgumentException("ERROR: GUIUtil.GetKeyCode(String) must be "
                    + "called with a KeyEvent VK_ constant name!\n" + s + " is not a valid VK_ "
                    + " constant name.");
        } catch (Exception e) {
            throw new IllegalArgumentException("ERROR caused bei Exception:" + e);
        }
        return erg;
    }

    /**
     * Diese Methode liefert eine Referenz auf die oberste, von der angegebenen Komponente aus
     * zu erreichende Komponente (meist wohl ein Fenster ;o).
     *
     * @param c Die Komponente, zu der die oberste Komponente ermittelt werden soll.
     */
    public static Component getRootComponent(Component c) {
        while (c.getParent() != null) {
            c = c.getParent();
        }
        return c;
    }
    
    /**
     * Setzt den &uuml;bergebenen Abblendstatus f&uuml;r die &uuml;bergebene Komponente und alle
     * in ihr enthaltenen.
     *
     * @param c Die Komponente, f&uuml;r die und die von ihr enthaltenen Komponenten der 
     *     Abblendstatus gesetzt werden soll.
     * @param state Der zu setzende Abblendstatus.
     */
    public static void SetEnabled(Component c, boolean state) {
        if (c instanceof Container) {
            Container cont = (Container) c;
            for (int i = 0, len = cont.getComponentCount(); i < len; i++) {
                SetEnabled(cont.getComponent(i), state);
            }
        }
        c.setEnabled(state);
    }
    
}
