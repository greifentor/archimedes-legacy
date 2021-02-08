/*
 * DBFactoryControllerLock.java
 *
 * 06.11.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


import corent.dates.*;

import java.io.*;


/**
 * Dieses Interface beschreibt die Methoden, &uuml;ber die ein Objekt verf&uuml;gen mu&szlig;, 
 * das innerhalb eines DBFactoryControllers die Sperren bestimmter Objekte verwalten soll.
 *
 * @author O.Lieshoff
 *
 */
 
public interface DBFactoryControllerLock extends Serializable {
    
    /** @return Der Name der Klasse des gesperrten Objektes. */
    public String getLockedObjectClassname();
    
    /** @return Der Schl&uuml;ssel der Klasse des gesperrten Objektes. */
    public Object getLockedObjectKey();
    
    /** @return Ein Schl&uuml;ssel aus Klassenname und Objekt-Schl&uuml;ssel. */
    public String getLockKey();
    
    /** 
     * @return Die Benutzer-Login-Rechnernamen-Kombination des Benutzers, dem die Sperre 
     *     geh&ouml;rt. 
     */
    public Object getLockedObjectUserid();
    
    /** @return Der Ablaufzeitpunkt der Sperre. */
    public PTimestamp getTimeOfExspiration();
    
    /**
     * Setzt den &uuml;bergebenen Zeitstempel als neuen Zeitpunkt des Ablaufs der Sperre ein.
     *
     * @param toe Der neue Zeitpunkt, an dem die Sperre ablaufen soll.
     * @throws IllegalArgumentException falls der &uuml;bergebene PTimestamp eine null-Referenz
     *     ist.
     */
    public void setTimeOfExspiration(PTimestamp toe) throws IllegalArgumentException;
    
}
