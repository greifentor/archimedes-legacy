/*
 * Deactivatable.java
 *
 * 22.06.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


import corent.db.*;


/**
 * Mit Hilfe dieses Interfaces werden Objekte in die Lage versetzt, sich als deaktivierbare 
 * Objekte gegen&uuml;ber der DBFactory auszuweisen. Sie werden beim Aufrufen der 
 * L&ouml;schmethode der DBFactory nicht physisch gel&oumlo;scht, sondern nur als gel&ouml;scht
 * gekennzeichnet.
 *
 * @author O.Lieshoff
 *
 */
 
public interface Deactivatable {
    
    /** Die Tabellenspalte, in der der Gel&ouml;scht-Status gespeichert wird. */
    public ColumnRecord getStatusColumn();
    
    /** @return Der Statuswert, der einen gel&ouml;schten Datensatz kennzeichnet. */
    public Object getDeactivatedValue();
    
    /** @return Der Statuswert, der einen nicht-gel&ouml;schten Datensatz kennzeichnet. */
    public Object getActivatedValue();
    
    /** 
     * Diese Methode wird w&auml;hrend der Generierung des Objektes aufgerufen. Hier kann das
     * Attribut, das &uuml;ber den Aktivierungsstatus Auskunft gibt explizit gesetzt werden
     * (wenn es nicht den Wert <TT>false</TT> haben soll).
     */
    public void activateObject();
    
    /**
     * Liefert den aktuellen Gel&ouml;scht-Status des Objektes.
     *
     * @return <TT>true</TT>, falls das Objekt gel&ouml;scht ist, <TT>false</TT> sonst.
     */
    public boolean isRemoved();
    
}
