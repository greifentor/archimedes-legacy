/*
 * Ordered.java
 *
 * 17.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


/**
 * Dieses Interface mu&szlig; von Objekten implementiert werden, bei denen das Einlesen von 
 * Listen aus der Datenbank in einer vorgegebene Sortierung vorgenommen werden soll.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface Ordered {
    
    /** @return Der OrderByDescriptor, durch den die Sortierung beschrieben wird. */
    public OrderByDescriptor getOrderByDescriptor();
    
}
