/*
 * DBFactoryControllerListener.java
 *
 * 06.11.12005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


/**
 * Dieses Interface definiert die Methoden, &uuml;ber die ein Objekt verf&uuml;gen mu&szlig;,
 * wenn es sich in einem DBFactoryController als Listener registrieren will.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface DBFactoryControllerListener {
    
    /**
     * Diese Methode wird durch den DBFactoryController ausgef&uuml;hrt, bei dem sich das Objekt
     * als Listener angemeldet hat.
     *
     * @param cls Die Klasse, deren durch den Schl&uuml;ssel k das Objekt, zu dem die Nachricht
     *     gesendet worden ist, identifiziert wird.
     * @param k Der Schl&uuml;ssel, zu dem Objekt, zu dem die Nachricht versendet werden soll.
     * @param userid Die Kombination aus Login des Benutzers und Name des Rechners, von dem die
     *     Aktion, zu der die Nachricht versendet werden soll, ausgef&uuml;hrt worden ist.
     * @param mt Eine Angabe zum Typ der Operation, der die Benachrichtigung der Listener
     *     erforderlich macht.
     * @throws RemoteException Falls der Controller im Remote-Betrieb l&auml;uft und ein Problem
     *     bei der Client-/Server-Kommunikation auftritt.
     */
    public void eventFired(Class cls, Object k, String userid, DBFactoryController.MessageTyp mt
            );
    
}
