/*
 * LastModificationTracker.java
 *
 * 05.09.2006
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.app;


import corent.dates.*;


/**
 * Dieses Interface kann genutzt werden, um Klassen zu kennzeichnen, die den Zeitpunkt ihrer
 * letzten &Auml;nderung festhalten.
 *
 * @author ollie
 *
 */

public interface LastModificationTracker {

    /**
     * Diese Methode liefert den Zeitpunkt der letzten Modifikation des Objektes.
     *
     * @return Zeitpunkt der letzten Modifikation des Objektes.
     */
    public PTimestamp getLastModificationDate();

    /**
     * Setzt den angegebenen Zeitpunkt als neuen Zeitpunkt der letzten Modifikation des Objektes
     * ein.
     *
     * @param date Der Zeitpunkt der letzten Modifikation des Objektes.
     */
    public void setLastModificationDate(PTimestamp date);

}
