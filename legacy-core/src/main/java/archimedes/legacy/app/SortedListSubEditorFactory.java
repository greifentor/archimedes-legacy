/*
 * SortedListSubEditorFactory.java
 *
 * 09.07.2005
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
 * Die Factory zum SortedListSubEditor.
 *
 * @author ollie
 *
 */
 
public class SortedListSubEditorFactory implements SubEditorFactory {
    
    
    /* 
     * Diese Flagge mu&szlig; gesetzt werden, wenn die EditorDjinns im splitted-Modus erzeugt 
     * werden sollen.
     */
    private boolean splitted = false;
    /*
     * Diese Flagge ist zu setzen, wenn die von der Factory produzierten SubEditors 
     * InternalFrames zur Manipulation der Listen Inhalte einsetzen sollen.
     */
    private boolean internalFrameEdit = false;
    /* Referenz auf die ArchimedesApplication, innerhalb derer der SubEditor arbeiten soll. */
    private ArchimedesApplication app = null;
    /* Die Id des Vectors, der in dem SortedListSubEditor manipuliert werden soll. */
    private int vid = -1;
    /* Das NReferenzModel, &uuml;ber das die Manipulation des Vectors definiert wird. */
    private NReferenzModel nrm = null;
    /* Referenz auf die benutzte VectorPanelButtonFactory. */
    private VectorPanelButtonFactory vpbf = null;
    
    /**
     * Generiert eine SortedListSubEditorFactory anhand der &uuml;bergebenen Parameter.
     *
     * @param app Die ArchimedesApplication, innerhalb derer der SubEditor arbeiten soll. 
     * @param nrm Das NReferenzModel, &uuml;ber das die Manipulation des Vectors definiert wird.
     * @param vpbf Die VectorPanelButtonFactory, aus der die SubEditorFactory ihre Buttons 
     *     beziehen soll.
     * @param vid Die Id des Vectors, der in dem SortedListSubEditor manipuliert werden soll.
     * @param split Wird diese Flagge gesetzt, so werden die EditorDjinns zur gew&auml;hlten
     *     Aktion im split-Modus erzeugt.
     * @param internalFrameEdit Diese Flagge mu&szlig; gesetzt werden, wenn die Liste der 
     *     angezeigten Objekte aus selbstst&auml;ndigen Datens&auml;tzen besteht, die nicht
     *     existenzabh&auml;ngig vom Listeneigner sind. In diesem Falle werden Internal-Frames
     *     zur Manipulation der Objekte ge&ouml;ffnet.
     */
    public SortedListSubEditorFactory(ArchimedesApplication app, NReferenzModel nrm, 
            VectorPanelButtonFactory vpbf, int vid, boolean split, boolean internalFrameEdit) {
        super();
        this.app = app;
        this.internalFrameEdit = internalFrameEdit;
        this.nrm = nrm;
        this.splitted = split;
        this.vpbf = vpbf;
        this.vid = vid;
    }
    
    public SubEditor createSubEditor(Component owner, Attributed obj, Hashtable<String, 
            Component> components) {
        if (!(obj instanceof ArchimedesDynamic)) {
            throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
                    + "SortedListSubEditorFactory: ArchimedesDynamic required!");
        }
        if (!(obj instanceof ListOwner)) {
            throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
                    + "SortedListSubEditorFactory: ListOwner required!");
        }
        if (!(((ListOwner) obj).getList(this.vid) instanceof SortedVector)) {
            if (((ListOwner) obj).getList(this.vid) == null) {
                throw new NullPointerException("getList(" + this.vid + ") is null. Maybe you "
                        + "haven't altered the ListOwner implementation oder recompiled with "
                        + "the changed Udschebti class.");
            }
            throw new IllegalArgumentException(((ListOwner) obj).getList(this.vid).getClass(
                    ).getName() + " is not a valid parameter for SortedListSubEditorFactory: "
                    + "List is not a SortedVector!");
        }
        ListOwner lo = (ListOwner) obj;
        Object cv = lo.createElement(this.vid);
        return new SortedListSubEditor(this.app, lo.getList(this.vid), lo.createElement(
                this.vid), lo, this.nrm, (ArchimedesDynamic) obj, this.vpbf, (cv instanceof 
                ColumnViewable ? (((ColumnViewable) cv).getColumnnames()) : new String[] 
                {"Liste"}), this.splitted, this.internalFrameEdit, components);
    }

}
