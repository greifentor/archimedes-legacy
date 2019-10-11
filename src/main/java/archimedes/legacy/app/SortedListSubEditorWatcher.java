/*
 * SortedListSubEditorWatcher.java
 *
 * 13.05.2007
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


/**
 * Objekte, insbesondere ListOwner, k&ouml;nnen &uuml;ber die Implementierung dieses Interfaces
 * ausf&uuml;hrlich &uuml;ber die Aktionen des SortedListSubEditors informiert werden.
 *
 * @author ollie
 *
 */
 
public interface SortedListSubEditorWatcher {
    
    /**
     * Diese Methode wird aufgerufen, wenn der Watcher ein Ereignis wahrnimmt.
     *
     * @param e Das SortedListSubEditorEventObject, welches das Event beschreibt.
     * @return <TT>true</TT>, wenn die Bearbeitung der Aktion, die dem Event zugrundeliegt 
     *     weitergef&uuml;hrt werden soll, bzw. <TT>false</TT>, wenn die Aktion abgebrochen 
     *     werden soll.
     */
    public boolean dataChanged(SortedListSubEditorEventObject e);
    
}
