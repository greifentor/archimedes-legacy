/*
 * TypeChanger.java
 *
 * 17.08.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


import corent.base.*;


/**
 * &Uuml;ber eine Implementierung dieses Interfaces kann die Typenwandlung von DBUtil erweitert
 * werden.
 *
 * @author O.Lieshoff
 *
 */
 
public interface TypeChanger {
    
    /** 
     * &Uuml;ber diese Methode l&auml;&szlig;t sich die durch den Aufruf der Methode 
     * DBUtil.GetTypes() gewonnene Informationsliste gegebenenfalls erweitern.
     *
     * @param sv Der SortedVector mit den Standardtypen.
     */
    public void extendTypes(SortedVector sv);
    
    /**
     * Pr&uuml;ft, ob der angegebene String ein g&uuml;ltiger Typname ist.
     *
     * @param tn Der zu &uuml;berpr&uuml;fende String.
     * @return <TT>true</TT>, falls es sich bei dem &uuml;bergebenen String um einen 
     *     g&uuml;tigen Typnamen handelt.
     */
    public boolean isType(String tn);
    
    /**
     * Liefert einen java.sql.Types-Bezeichner f&uuml;r den angebenen Typnamen.
     *
     * @param tn Der Typname, zu dem der java.sql.Types-Bezeichner ermittelt werden soll.
     * @return Der zu dem Typnamen passende java.sql.Types-Bezeichner.
     */
    public int getType(String tn);
    
}
