/*
 * Key.java
 *
 * 26.06.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


/**
 * Mit Hilfe dieses Interfaces werden Schl&uuml;ssel f&uuml;r verschiedene Anwendungen 
 * definiert.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface Key {
    
    /** 
     * @return Erzeugt eine in ein SQL-Statement inbaubare Klausel mit den Inhalten des Keys. 
     */
    public String toSQL();
    
}
