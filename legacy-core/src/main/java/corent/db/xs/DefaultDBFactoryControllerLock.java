/*
 * DefaultDBFactoryControllerLock.java
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
 * Diese Klasse bietet eine Musterimplementierung zum Interface DBFactoryControllerLock.
 *
 * @author O.Lieshoff
 *
 */
 
public class DefaultDBFactoryControllerLock implements DBFactoryControllerLock, Serializable {
    
    /* Die Klasse, zu der die Sperre gilt. */
    private Class cls = null;
    /* Der Schl&uuml;ssel des Objektes der angegebenen Klasse, zu der die Sperre gelten soll. */
    private Object key = null;
    /* Der Zeitpunkt, an dem die Sperre ausl&auml;uft. */
    private PTimestamp toe = null;
    /* Die Benutzerinformation, zu der Benutzer-Rechner-Kombination, die die Sperre besitzt. */
    private String userid = null;
    
    /**
     * Generiert eine Sperre mit den angegebenen Parametern.
     *
     * @param cls Die Klasse, zu der die Sperre gilt.
     * @param key Der Schl&uuml;ssel des Objektes der angegebenen Klasse, zu der die Sperre 
     *     gelten soll.
     * @param pts Der Zeitpunkt, an dem die Sperre ausl&auml;uft.
     * @param userid Die Benutzerinformation, zu der Benutzer-Rechner-Kombination, die die 
     *     Sperre besitzt.
     * @throws IllegalArgumentException falls einer der Parameter mit dem Wert <TT>null</TT>
     *     belegt ist.
     */
    public DefaultDBFactoryControllerLock(Class cls, Object key, PTimestamp pts, String userid) 
            throws IllegalArgumentException {
        super();
        if ((cls == null) || (key == null) || (pts == null) || (userid == null)) {
            throw new IllegalArgumentException("Tried to generate "
                    + "DefaultDBFactoryControllerLock with null-pointer value in parameters! [ "
                    + (cls == null ? "cls " : "") + (key == null ? "key " : "")
                    + (pts == null ? "pts " : "") + (userid == null ? "userid " : "") + "]");
        }
        this.cls = cls;
        this.key = key;
        this.toe = pts;
        this.userid = userid;
    }
    
    
    /* Ueberschreiben von Methoden der Superklasse. */
    
    public boolean equals(Object o) {
        if (!(o instanceof DefaultDBFactoryControllerLock)) {
            return false;
        }
        DefaultDBFactoryControllerLock dbfcl = (DefaultDBFactoryControllerLock) o;
        return this.getLockedObjectClassname().equals(dbfcl.getLockedObjectClassname())
                && this.getLockedObjectKey().equals(dbfcl.getLockedObjectKey())
                && this.getLockedObjectUserid().equals(dbfcl.getLockedObjectUserid())
                && (this.getTimeOfExspiration().toLong() == dbfcl.getTimeOfExspiration().toLong(
                ));
    }
    
    public String toString() {
        return this.getLockedObjectClassname() + " - " + this.getLockedObjectKey() + " -> "
                + this.getLockedObjectUserid() + " (" + this.getLockKey() + ") >> " 
                + this.getTimeOfExspiration(); 
    }
    
    
    /* Implementierung des Interfaces DBFactoryControllerLock. */
    
    public String getLockedObjectClassname() {
        return this.cls.getName();
    }
    
    public Object getLockedObjectKey() {
        return this.key;
    }
    
    public String getLockKey() {
        return this.getLockedObjectClassname() + "-" + this.getLockedObjectKey().toString();
    }

    public Object getLockedObjectUserid() {
        return this.userid;
    }
    
    public PTimestamp getTimeOfExspiration() {
        return this.toe;
    }
    
    public void setTimeOfExspiration(PTimestamp toe) throws IllegalArgumentException {
        if (toe == null) {
            throw new IllegalArgumentException("null-timestamp cannot be given for locks new "
                    + " time of expiration");
        }
        this.toe = toe;
    }
    
}
