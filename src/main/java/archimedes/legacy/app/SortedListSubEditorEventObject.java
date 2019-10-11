/*
 * SortedListSubEditorEventObject.java
 *
 * 13.05.2007
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


import corent.base.*;

import java.awt.*;
import java.util.*;


/**
 * Diese Ableitung des EventObjects, dient der &Uuml;bertragung Events innerhalb des 
 * SortedListSubEditors.
 *
 * @author
 *     ollie
 *     <P>
 *
 * @changed
 *     OLI 15.06.2008 - Miteinbeziehen des SortedVector, der durch das aufgezeigte Ereignis 
 *             manipuliert werden soll.
 *     <P>
 *
 */
 
public class SortedListSubEditorEventObject extends EventObject {
        
    private Hashtable<String, Component> componentTable = new Hashtable<String, Component>();
    private Object obj = null;
    private SortedListSubEditorEventType type = null;
    private SortedVector sv = null;
    
    /**
     * Generiert ein SortedListSubEditorEventObject anhand der &uuml;bergebenen Parameter.
     *
     * @param source Der SubEditor, von dem das Ereignis ausging.
     * @param obj Das Objekt, das durch die Aktion des Event manipuliert worden ist. 
     * @param components Eine Hashtable&gt;String, Component&lt; mit den Components des 
     *     EditorDjinnPanels, in dem der SubEditor sich befindet. Diese sind &uuml;ber ihre
     *     Contextnamen identifizierbar.
     * @param type Der SortedListSubEditorEventType, &uuml;ber den die Art bzw. der Anlas&szlig;
     *     des Events definiert wird.
     * @param sv Der SortedVector, der in der Anzeige dargestellt wird.
     */
    public SortedListSubEditorEventObject(Object source, Object obj,
            Hashtable<String, Component> components, SortedListSubEditorEventType type, 
            SortedVector sv) {
        super(source);
        this.componentTable = components;
        this.obj = obj;
        this.sv = sv;
        this.type = type;
    }
    
    /**
     * Liefert die Liste mit den Components des EditorDjinnPanels, in dem der SubEditor sich 
     * befindet.
     *
     * @return Die Liste der Components des EditorDjinnPanels, in dem der SubEditor sich 
     *     befindet.
     */
    public Hashtable<String, Component> getComponentTable() {
        return this.componentTable;
    }
    
    /**
     * Liefert den SortedListSubEditorEventType zum Event.
     *
     * @return Der SortedListSubEditorEventType, der &uuml;ber den Ursprung des Events 
     *     informiert.
     */
    public SortedListSubEditorEventType getType() {
        return this.type;
    }
    
    /** 
     * Liefert das Objekt, das durch die zum Event geh&ouml;rende Aktion manipuliert worden ist.
     *
     * @return Das durch die Aktion zum Event manipulierte Objekt.
     */
    public Object getObject() {
        return this.obj;
    }
    
    /**
     * Liefert eine Referenz auf den durch das Event manipulierten Vector.
     *
     * @return Eine Referenz auf den durch das Event manipulierten Vector.
     *
     * @changed
     *     OLI 15.06.2008 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public SortedVector getVector() {
        return this.sv;
    }
    
}
