/*
 * Cached.java
 *
 * 21.04.2008
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.db.xs;


/**
 * Dieses Interface erm&ouml;glicht die Einbindung eines Caches auf dem serverseitigen Teil 
 * einer Applikation.
 * <P>Das Interface mu&szlig; von der DBFactory implementiert werden, &uuml;ber die die 
 * betroffene Klasse persistent gehalten werden soll.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 21.04.2008 - Hinzugef&uuml;gt.
 *
 */
 
public interface Cached {
    
    /**
     * Diese Methode wird aufgerufen, wenn der DBFactoryController, in den das Cached-Objekt 
     * eingebunden ist, eine L&ouml;schvorgang durchf&uuml;hren soll. Sie wird beispielsweise 
     * bei einem Aufruf der Methoden <TT>remove(Object, boolean)</TT> und <TT>removeBatch(Class, 
     * Vector, boolean)</TT> ausgef&uuml;hrt.
     *
     * @param o Das Objekt, das gel&ouml;scht worden ist, oder <TT>null</TT>, wenn es sich um 
     *     eine Stapeloperation handelt.
     * @param batchRemove Diese Flagge wird gesetzt, wenn es sich bei der L&ouml;schoperation um 
     *     eine Stapeloperation handelt.
     * @param dbfc Eine Referenz auf den DBFactoryController, der die L&ouml;schoperation 
     *     durchf&uuml;hrt.
     *
     */
    public void removeDetected(Object o, boolean batchRemove, DBFactoryController dbfc);

    /**
     * Diese Methode wird aufgerufen, wenn der DBFactoryController, in den das Cached-Objekt 
     * eingebunden ist, eine &Auml;nderung an den Daten durchf&uuml;hren soll, denen die 
     * Persistenz der Klasse des Objektes zugrunde liegt. Sie wird beispielsweise bei einem 
     * Aufruf der Methoden <TT>write(Object)</TT> und <TT>writeBatch(Class, Vector, 
     * Hashtable&lt;Integer, Object&gt;)</TT> ausgef&uuml;hrt.
     *
     * @param o Das Objekt, das ge&auml;ndert worden ist, oder <TT>null</TT>, wenn es sich um 
     *     eine Stapeloperation handelt.
     * @param batchWrite Diese Flagge wird gesetzt, wenn es sich bei der Schreiboperation um 
     *     eine Stapeloperation handelt.
     * @param dbfc Eine Referenz auf den DBFactoryController, der den Schreibzugriff 
     *     durchf&uuml;hrt.
     *
     */
    public void writeDetected(Object o, boolean batchWrite, DBFactoryController dbfc);
        
}
