/*
 * Releaseable.java
 * 
 * 10.01.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.base;


/**
 * Mit Hilfe dieses Interfaces kann ein Objekt als ressourcenbindend gekennzeichnet werden. Es
 * kann f&uuml;r Klassen genutzt werden, bei deren Objekte es n&ouml;tig ist, gewisse Referenzen
 * zu l&ouml;sen, bevor sie eliminiert werden k&ouml;nnen. Dies gilt vorallem f&uuml;r 
 * Fenster-Klassen, kann aber auch f&uuml;r andere Klassen genutzt werden. In der Klasse 
 * corent.base.ReleaseUtil finden sich Funktionen mit denen das 'releasen' ganzer 
 * Objekt-Strukturen geeignet sind.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
     
public interface Releaseable {
    
    /**
     * Diese Methode entfernt alle relevanten Referenzen, auf das Objekt verweist, um so das
     * l&ouml;schen der referenzierten Objekte nicht weiter zu behindern.
     */
    public void release();
    
}
