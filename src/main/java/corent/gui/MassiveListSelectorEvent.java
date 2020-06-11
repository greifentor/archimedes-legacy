/*
 * MassiveListSelectorEvent.java
 *
 * 03.12.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import java.util.*;


/**
 * Diese Spezialisierung des EventObjects wird durch den MassiveListSelectorListener genutzt, um
 * Events solcher Komponenten abzubilden.
 *
 * @author O.Lieshoff
 *
 */
 
public class MassiveListSelectorEvent extends EventObject {
    
    private MassiveListSelectorEventtype type = null;
    
    /**
     * Generiert ein MassiveListSelectorEvent anhand der &uuml;bergebenen Parameter.
     *
     * @param source Das Object, durch das das Event ausgel&ouml;st worden ist. Im Normalfall
     *     wird das der MassiveListSelektor sein, in dem das Event angefallen ist.
     * @param type Der Typ des Event (z.B. ITEM_SELECTED, wenn ein neues Element ausgew&auml;hlt
     *     worden ist.
     */
    public MassiveListSelectorEvent(Object source, MassiveListSelectorEventtype type) {
        super(source);
        this.type = type;
    }
    
    /**
     * Liefert den MassiveListSelectorEventtype des Events.
     *
     * @return Der MassiveListSelectorEventtype des Events.
     */
    public MassiveListSelectorEventtype getType() {
        return this.type;
    }
    
}
