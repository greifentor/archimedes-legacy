/*
 * HistoryWriter.java
 *
 * 03.06.2007
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Dieses Interface mu&szlig; von Objekten implementiert werden, auf deren historische 
 * St&auml;nde zur&uuml;ckgegriffen werden k&ouml;nnen soll.
 *
 * @author O.Lieshoff
 *
 */
 
public interface HistoryWriter {
    
    /**
     * Diese Methode wird aufgerufen, wenn das Objekt seine Historienansicht aktivieren soll.
     */
    public void doShowHistory();
    
}
