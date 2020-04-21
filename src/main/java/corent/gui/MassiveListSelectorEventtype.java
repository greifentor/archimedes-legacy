/*
 * MassiveListSelectorEventtype.java
 *
 * 03.13.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


/**
 * Dieser Enum-Typ dient der Definition von MassiveListSelectorEvent-Typen. Sie identifizieren
 * die Art eines MassiveListSelectorEvents.
 *
 * @author O.Lieshoff
 *
 */
 
public enum MassiveListSelectorEventtype {
    
    /** 
     * Dieser Typ wird erzeugt, wenn ein neues Element in dem Selector ausgew&auml;hlt worden
     * ist (nach Auswahl &uuml;ber den Selektionsbutton.
     */
    SELECTION_ALTERED,
    /**
     * Mit Hilfe dieses Typs wird ein Event indentifiziert, bei dem das selektierte Element
     * aus dem Selector gel&ouml;scht worden ist (C-Button).
     */
    SELECTION_CLEARED;
    
}
