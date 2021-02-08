/*
 * ListOwner.java
 *
 * 25.05.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;


/**
 * Dieses Interface definiert das Verhalten eines Objektes, das eine oder mehrere Listen 
 * besitzt, die in einem EditorDjinn anzeigbar und manipulierbar gemacht werden sollen.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface ListOwner {
    
    /**
     * Liefert eine Referenz auf die Liste mit der angegebenen Nummer.
     *
     * @param nr Die Nummer der Liste auf die eine Referenz geliefert werden soll.
     * @return Referenz auf die Liste mit der angegebenen Nummer. 
     */
    public List getList(int nr);
    
    /**
     * Liefert ein neues Element zur angegebenen Liste.
     *
     * @param nr Die Nummer der Liste zu der die Klasse der Elemente geliefert werden soll.
     * @return Das neue Element.
     */
    public Object createElement(int nr);
    
}
