/*
 * COUtil.java
 *
 * 15.12.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import java.awt.*;

import javax.swing.*;


/**
 * Diese Klasse bietet eine Reihe von statischen Utility-Methoden, mit Hilfe derer die 
 * ContextOwner implementierenden Komponenten manipuliert werden k&ouml;nnen.
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 27.05.2009 - Erweiterung um die Methode <TT>FindComponent(String, Component)</TT>.
 *             Dabei: Formatanpassungen.
 *     <P>
 *
 */

public class COUtil {

    /**
     * Liefert eine Referenz auf die Komponente mit dem angegebenen (Context-)namen. Bei
     * normale Swingkomponenten wird der Name &uuml;ber die Methode <TT>getName()</TT> 
     * ermittelt. Bei <TT>ContextOwner</TT>-Komponenten wird die Methode <TT>getContext()</TT>
     * genutzt.
     *
     * @param cn Der Kontextname der Komponente, deren Referenz geliefert werden soll.
     * @param root Die Komponten in der und in deren Kindern nach der Komponente mit dem 
     *         angegebenen gesucht werden soll.
     * @param cls Die Klasse, die die ermittelte Komponente haben mu&szlig; oder <TT>null</TT>,
     *         wenn die Klasse der ermittelten komponente unerheblich ist.
     * @return Die Komponente zum angegebenen Namen.
     */
    public static Component FindComponent(String cn, Component root, Class cls) {
        Component result = null;
        Container cont = null;
        int i = 0;
        int leni = 0;
        JMenu menu = null;
        JMenuBar menuBar = null;
        JToolBar toolBar = null;
        String n = root.getName();
        if (root instanceof ContextOwner) {
            n = ((ContextOwner) root).getContext();
        }
        if (Boolean.getBoolean("corent.gui.debug")) {
            System.out.println(n + " - " + root.getClass());
        }
        if ((n != null) && n.equals(cn)) {
            if ((root != null) && ((cls == null) || cls.isInstance(root))) {
                if (Boolean.getBoolean("corent.gui.debug")) {
                    System.out.println("found " + n + " - " + root.getClass());
                }
                return root;
            }
        }
        if (root instanceof JMenuBar) {
            menuBar = (JMenuBar) root;
            for (i = 0, leni = menuBar.getMenuCount(); i < leni; i++) {
                result = FindComponent(cn, menuBar.getMenu(i), cls);
                if (result != null) {
                    break;
                }
            }
        } else if (root instanceof JMenu) {
            menu = (JMenu) root;
            for (i = 0, leni = menu.getMenuComponentCount(); i < leni; i++) {
                result = FindComponent(cn, menu.getMenuComponent(i), cls);
                if (result != null) {
                    break;
                }
            }
            if (result != null) {
                for (i = 0, leni = menu.getItemCount(); i < leni; i++) {
                    result = FindComponent(cn, menu.getItem(i), cls);
                    if (result != null) {
                        break;
                    }
                }
            }
        } else if (root instanceof JToolBar) {
            toolBar = (JToolBar) root;
            for (i = 0, leni = toolBar.getComponentCount(); i < leni; i++) {
                result = FindComponent(cn, toolBar.getComponentAtIndex(i), cls);
                if (result != null) {
                    break;
                }
            }
        } else if (root instanceof Container) {
            cont = (Container) root;
            for (i = 0, leni = cont.getComponentCount(); i < leni; i++) {
                result = FindComponent(cn, cont.getComponent(i), cls);
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Diese Methode liefert zu einem angegebene ContextOwner den vollst&auml;ndigen Namen 
     * innerhalb der aktuellen Applikation, d. h. bis hinauf zur obersten Component.
     *
     * @param c Der ContextOwner (Component) zu dem der vollst&auml;ndige Name ermittelt werden 
     *     soll. Als Typ ist hier deshalb Component gew&auml;hlt, weil die Methode rekursiv
     *     &uuml;ber Komponentenb&auml;ume l&auml;uft.
     * @return Der vollst&auml;ndige Name des ContextOwners innerhalb der Applikation.
     */
    public static String GetFullContext(Component c) {
        String s = "";
        if (c instanceof ContextOwner) {
            s = ((ContextOwner) c).getContext().replace("/", ".");
        }
        if (!s.startsWith(".") && !s.startsWith("/")) {
            if (c.getParent() != null) {
                String h = GetFullContext(c.getParent());
                if (h.length() > 0) {
                    s = h.concat((c instanceof ContextOwner ? "." : "")).concat(s);
                }
            }
        } else {
            s = s.substring(1, s.length());
        }
        return s;
    }

    /**
     * Diese Methode aktualisiert den ContextOwner mit dem angegeben Context-Namen anhand des 
     * &uuml;bergebenen RessourceManagers.
     *
     * @param c Die Wurzelkomponente, von der aus die Aktualisierung angestossen werden soll.
     * @param cn Der Context-Name des ContextOwners, der anhand des angegebenen 
     *     RessourcenManagers aktualisiert werden soll bzw. <TT>null</TT> wenn alle Komponenten
     *     unterhalb der Wurzelkomponente aktualisiert werden sollen.
     * @param rm Der RessourceManager, der die zu &auml;ndernden Inhalte bereitstellt.
     *
     * @changed
     *     OLI 27.05.2009 - Erweiterung um die Ber&uuml;cksichtigung von JMenuBars.
     *     <P>
     *
     */
    public static void Update(Component c, String cn, RessourceManager rm) {
        Update(c, cn, rm, "", 1, Boolean.getBoolean("corent.gui.debug"));
    }

    private static void Update(Component c, String cn, RessourceManager rm, String acn, int j,
            boolean debug) {
        if (c instanceof Ressourced) {
            Ressourced r = (Ressourced) c;
            if (debug) {
                System.out.print("trying to update " + r.getContext());
            }
            if ((cn == null) || acn.concat(r.getContext()).equals(cn)) {
                if (debug) {
                    System.out.println(" ... done");
                }
                r.update(rm);
            } else {
                if (debug) {
                    System.out.println(" ... FAILED");
                }
            }
        }
        if (c instanceof JMenuBar) {
            JMenuBar cont = (JMenuBar) c;
            for (int i = 0, len = cont.getMenuCount(); i < len; i++) {
                Update(cont.getMenu(i), cn, rm, acn.concat((c instanceof ContextOwner ?
                        ((ContextOwner) c).getContext() : "")).concat((c instanceof ContextOwner 
                        ? "/" : "")), j+1, debug);
            }
        } else if (c instanceof JMenu) {
            JMenu cont = (JMenu) c;
            for (int i = 0, len = cont.getMenuComponentCount(); i < len; i++) {
                Update(cont.getMenuComponent(i), cn, rm, acn.concat((c instanceof ContextOwner ?
                        ((ContextOwner) c).getContext() : "")).concat((c instanceof ContextOwner 
                        ? "/" : "")), j+1, debug);
            }
            for (int i = 0, len = cont.getItemCount(); i < len; i++) {
                Update(cont.getItem(i), cn, rm, acn.concat((c instanceof ContextOwner ? (
                        (ContextOwner) c).getContext() : "")).concat((c instanceof ContextOwner 
                        ? "/" : "")), j+1, debug);
            }
        } else if (c instanceof Container) {
            Container cont = (Container) c;
            for (int i = 0, len = cont.getComponentCount(); i < len; i++) {
                Update(cont.getComponent(i), cn, rm, acn.concat((c instanceof ContextOwner ? (
                        (ContextOwner) c).getContext() : "")).concat((c instanceof ContextOwner 
                        ? "/" : "")), j+1, debug);
            }
        }
    }

}
