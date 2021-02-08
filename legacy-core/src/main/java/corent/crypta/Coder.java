/*
 * Coder.java
 *
 * 23.09.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.crypta;


/**
 * Das Coder-Interface dient zur Kodierung und Dekodierung von Daten (z. B. innerhalb der 
 * DBFactories).
 *
 * @author O.Lieshoff
 *
 */
 
public interface Coder  {
   
    /** 
     * Kodiert den angegeben String und liefert ihn in kodierter Form zur&uuml;ck.
     * 
     * @param s Der String, welcher kodiert werden soll.
     * @return Der kodierte String.
     */
    public String encode(String s);
     
    /** 
     * Dekodiert den angegeben String und liefert ihn in dekodierter Form zur&uuml;ck.
     * 
     * @param s Der String, welcher dekodiert werden soll.
     * @return Der dekodierte String.
     */
    public String decode(String s);
     
}
