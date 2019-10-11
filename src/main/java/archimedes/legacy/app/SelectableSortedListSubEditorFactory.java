/*
 * SelectableSortedListSubEditorFactory.java
 *
 * 10.06.2006
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.app;


import java.awt.*;
import java.util.*;

import corent.base.*;
import corent.djinn.*;

import archimedes.legacy.model.*;


/**
 * Die Factory zum SelectableSortedListSubEditor.
 *
 * @author ollie
 *
 */
 
public class SelectableSortedListSubEditorFactory implements SubEditorFactory {
    
    
    /* Referenz auf die ArchimedesApplication, innerhalb derer der SubEditor arbeiten soll. */
    private ArchimedesApplication app = null;
    /* Die Id des Vectors, der in dem SortedListSubEditor manipuliert werden soll. */
    private int vid = -1;
    /* Das NReferenzModel, &uuml;ber das die Manipulation des Vectors definiert wird. */
    private NReferenzModel nrm = null;

    /**
     * Generiert eine SelectableSortedListSubEditorFactory anhand der &uuml;bergebenen 
     * Parameter.
     *
     * @param app Die ArchimedesApplication, innerhalb derer der SubEditor arbeiten soll. 
     * @param nrm Das NReferenzModel, &uuml;ber das die Manipulation des Vectors definiert wird.
     * @param vid Die Id des Vectors, der in dem SortedListSubEditor manipuliert werden soll.
     */
    public SelectableSortedListSubEditorFactory(ArchimedesApplication app, NReferenzModel nrm, 
            int vid) {
        super();
        this.app = app;
        this.nrm = nrm;
        this.vid = vid;
    }
    
    public SubEditor createSubEditor(Component owner, Attributed obj, Hashtable<String, 
            Component> components) {
        if (!(obj instanceof ArchimedesDynamic)) {
            throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
                    + "SelectableSortedListSubEditorFactory: ArchimedesDynamic required!");
        }
        if (!(obj instanceof ListOwner)) {
            throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
                    + "SelectableSortedListSubEditorFactory: ListOwner required!");
        }
        if (!(((ListOwner) obj).getList(this.vid) instanceof SortedVector)) {
            if (((ListOwner) obj).getList(this.vid) == null) {
                throw new NullPointerException("getList(" + this.vid + ") is null. Maybe you "
                        + "haven't altered the ListOwner implementation oder recompiled with "
                        + "the changed Udschebti class.");
            }
            throw new IllegalArgumentException(((ListOwner) obj).getList(this.vid).getClass(
                    ).getName() + " is not a valid parameter for SelectableSortedListSubEditor"
                    + "Factory: List is not a SortedVector!");
        }
        ListOwner lo = (ListOwner) obj;
        Object cv = lo.createElement(this.vid);
        String[] cn = (cv instanceof ColumnViewable ? (((ColumnViewable) cv).getColumnnames()) : 
                new String[] {"Liste"});
        String[] cn1 = new String[cn.length+1];
        for (int i = 0; i < cn.length; i++) {
            cn1[i] = cn[i];
        }
        cn1[cn1.length-1] = "enthalten";
        return new SelectableSortedListSubEditor(this.app, lo.getList(this.vid), 
                lo.createElement(this.vid), lo, this.nrm, (ArchimedesDynamic) obj, cn1);
    }

}
