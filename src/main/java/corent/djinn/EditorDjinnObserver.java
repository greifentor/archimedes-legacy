/*
 * EditorDjinnObserver.java
 *
 * 05.06.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;


/**
 * Objekte, die dieses Interface implementieren, werden &uuml;ber Ereignisse eine EditorDjinns
 * informiert, in dem sie aktuell angezeigt werden.
 *
 * <P><B>Hinweis:</B> Das Interface soll das Interface EditorDjinnMaster mittelfristig 
 * abl&ouml;sen.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 05.06.2008 - Hinzugef&uuml;gt.
 *     <P>
 *
 */
 
public interface EditorDjinnObserver {
    
    /**
     * Diese Methode wird aufgerufen, wenn der EditorDjinn ein durch den Observer 
     * wahrzunehmendes Ereignis feststellt.
     *
     * @param type Der Typ des EditorDjinn-Ereignisses.
     * @param params Eine Hashtable mit Parametern aus dem EditorDjinn: 
     *     <BR><I>DISPELLED</I>: null (keine Parameter).
     *     <BR><I>SUMMONED</I>: "comps" (Hashtable&lt;Component, String&gt; mit den Komponenten
     *         EditorDjinns), "mode" (der EditorDjinnMode, unter dem die Methode aufgerufen 
     *         wurde).
     */
    public void eventDetected(EditorDjinnEventType type, Hashtable<String, Object> params);
    
}
