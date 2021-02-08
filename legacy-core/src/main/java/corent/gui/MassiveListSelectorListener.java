/*
 * MassiveListSelectorListener.java
 * 
 * 03.12.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


/**
 * Mit Hilfe dieses Interfaces k&ouml;nnen Objekte in die Lage versetzt werden, &uuml;ber die 
 * Events von MassiveListSelectoren informiert zu werden.
 *
 * @author O.Lieshoff
 *
 */
 
public interface MassiveListSelectorListener {
    
    /**
     * Diese Methode wird aufgerufen, wenn in dem MassiveListSelector, an den der Listener 
     * angebunden, ein neues Element ausgew&auml;hlt wird.
     *
     * @param e Das MassiveListSelectorEvent, das durch die Auswahl des neuen Elements erzeugt
     *     worden ist.
     */
    public void selectionAltered(MassiveListSelectorEvent e);
    
    /**
     * Diese Methode wird aufgerufen, wenn das in dem MassiveListSelector, an den der Listener 
     * angebunden, ausgew&auml;hlte gel&ouml;scht worden ist (die Auswahl also &uuml;ber den  
     * C-Button zur&uuml;ckgesetzt worden ist.
     *
     * @param e Das MassiveListSelectorEvent, das durch das L&ouml;schen des Elements erzeugt
     *     worden ist.
     */
    public void selectionCleared(MassiveListSelectorEvent e);
    
}
