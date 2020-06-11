/*
 * TimestampFieldListener.java
 *
 * 05.11.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import corent.dates.*;


/**
 * Dieser Listener dient zum Abh&ouml;ren von TimestampFields.
 *
 * @author O.Lieshoff
 *
 */
 
public interface TimestampFieldListener {
    
    /**
     * Diese Methode wird aufgerufen, wenn das Datum des TimestampFields ge&auml;ndert worden 
     * ist.
     *
     * @param ts Der neue Wert des Timestamps des Fields.
     */
    public void dateChanged(TimestampModel ts);
    
    /**
     * Diese Methode wird aufgerufen, wenn die Uhrzeit des TimestampFields ge&auml;ndert worden 
     * ist.
     *
     * @param ts Der neue Wert des Timestamps des Fields.
     */
    public void timeChanged(TimestampModel ts);
    
}
