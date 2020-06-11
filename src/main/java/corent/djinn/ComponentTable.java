/*
 * ComponentTable.java
 *
 * 27.10.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;

 
/**
 * Mit Hilfe dieser Klasse werden die Komponenten der einzelnen Tabs des EditorDjinns verwaltet.
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 01.10.2008 - Erweiterung um Debugausgaben (zuschaltbar &uuml;ber 
 *             "corent.djinn.debug").
 *     <P>
 */

public class ComponentTable {
    
    /* Hashtable zum Speichern der einzelnen Komponenten zu den Tabs des Djinns. */
    private Hashtable components = new Hashtable();
    
    /** Generiert eine leere ComponentTable. */
    public ComponentTable() {
        super();
    }

    /** 
     * F&uuml;gt die &uuml;bergebene DefaultEditorDjinnCell in die ComponentTable ein, wenn sie 
     * f&uuml;r das angegeben Tab noch nicht enthalten ist.
     *
     * @param tab Das Tab, auf dem die Komponente eingef&uuml;gt werden soll.
     * @param dedc Die DefaultEditorDjinnCell, aus der die Daten zur Produktion der Komponente 
     *     und deren Umfeld gewonnen werden soll.
     *
     * @changed
     *     OLI 01.10.2008 - Erweiterung um Debugausgaben (zuschaltbar &uuml;ber 
     *             "corent.djinn.debug").
     *     <P>
     */
    public void addCell(int tab, DefaultEditorDjinnCell dedc) {
        boolean debug = Boolean.getBoolean("corent.djinn.debug");
        Integer it = new Integer(tab);
        Vector v = (Vector) this.components.get(it);
        if (debug) {
            System.out.println("ComponentTable: add for tab " + tab);
        }
        if (v != null) {
            if (!v.contains(dedc)) {
                v.addElement(dedc);
            }
            if (debug) {
                System.out.println("ComponentTable:     adding new tab");
            }
        } else {
            v = new Vector();
            v.addElement(dedc);
            this.components.put(it, v);
            if (debug) {
                System.out.println("ComponentTable:     appending to existing tab");
            }
        }
    }
    
    /**
     * Ermittelt anhand der &uuml;bergebenen Parameter die DefaultEditorDjinnCell.
     *
     * @param tab Das Tab, zu dem die Komponente ermittelt werden soll.
     * @param n Die Position innerhalb des Tabs.
     * @return Die ermittelte DefaultEditorDjinnCell.
     * @throws IllegalArgumentException Falls zu der angegebenen (tab, n)-Kombination keine 
     *     DefaultEditorDjinnCell gefunden werden kann. 
     */
    public DefaultEditorDjinnCell getCell(int tab, int n) throws IllegalArgumentException {
        Vector v = (Vector) this.components.get(new Integer(tab));
        if ((v != null) && (n >= 0) && (n < v.size())) {
            return (DefaultEditorDjinnCell) v.elementAt(n);
        }
        throw new IllegalArgumentException("(tab, n) combination (" + tab + ", " + n 
                + ") is not valid");
    }
    
    /** @return Anzahl der Tabs. */
    public int getTabCount() {
        boolean debug = Boolean.getBoolean("corent.djinn.debug");
        if (debug) {
            System.out.println("ComponentTable: returning tab count: " + this.components.size()
                    );
        }
        return this.components.size();
    }

    /**
     * @param tab Das Tab zu dem die Anzahl der gespeicherten DefaultEditorDjinnCells ermittelt
     *     werden soll.
     * @return Die Anzahl der zu dem Tab gespeicherten DefaultEditorDjinnCells, bzw. 0, wenn die
     *     Liste entweder leer ist oder keine Cells enth&auml;lt.
     */
    public int getSize(int tab) {
        Vector v = (Vector) this.components.get(new Integer(tab));
        if (v != null) {
            return v.size();
        }
        return 0;
    }
    
    /**
     * @param tab Das Tab zu dem die Liste der Komponenten ermittelt werden soll.
     * @return Ein Vector mit den Komponenten des angegebenen Tabs. 
     * @throws IllegalArgumentException Falls zu der angegebenen tab keine Liste mit 
     *     DefaultEditorDjinnCell gefunden werden kann. 
     */
    public Vector getComponents(int tab) {
        Vector v = (Vector) this.components.get(new Integer(tab));
        if (v != null) {
            Vector erg = new Vector();
            for (int i = 0, len = v.size(); i < len; i++) {
                erg.addElement(((DefaultEditorDjinnCell) v.elementAt(i)).getComponent());
            }
            return erg;
        }
        throw new IllegalArgumentException("tab (" + tab + ") does not exists");
    }
    
    /**
     * L&ouml;scht die durch den angegebenen Index bezeichnete Zelle aus der Liste.
     *
     * @param tab Der Tab, aus dem die Zelle entfernt werden soll.
     * @param index Der Index, zu dem die Zelle aus der Liste gel&ouml;scht werden soll.
     */
    public void removeCell(int tab, int index) {
        Vector v = (Vector) this.components.get(new Integer(tab));
        if (v != null) {
            v.removeElementAt(index);
        }
    }
    
}
