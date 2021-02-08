/*
 * Printable.java
 *
 * 14.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Mit Hilfe dieses Interfaces wird ein Objekt als druckbar gekennzeichnet. Innerhalb eines
 * EditorDjinns wird ein Drucken-Button angeboten, bei dessen Bet&auml;tigung die print-Methode
 * des Objektes ausgef&uuml;hrt wird.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface Printable {
    
    /** Durch Aufruf dieser Methode wird das Objekt gedruckt. */
    public void print();
    
}
