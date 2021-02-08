/*
 * CentrallyHeld.java
 *
 * 21.01.2008
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db.xs;


/**
 * Dieses Interface mu&szlig; von Klassen implementiert werden, die zentralisiert gehalten 
 * werden und &uuml;ber eine globale Identifikationsnummer verf&uuml;gen. 
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 21.01.2008 - Hinzugef&uuml;gt.
 *     <P>OLI 23.03.2009 - Nachkommentierung der Methoden und Formatanpassungen.
 *     <P>
 *
 */

public interface CentrallyHeld {

    /**
     * Liefert die GLI (Global-Lokal-Identifikatornummer) des Objektes. Hierbei handelt es sich 
     * um eine Identifikationsnummer, die &uuml;ber alle Datenbest&auml;nde der Anwendung 
     * eindeutig ist und so z. B. zur Synchronisation von Datens&auml;tzen genutzt werden kann.
     *
     * @return Die GLI des Objekts.
     */
    public long getGLI();

    /**
     * Setzt den &uuml;bergebenen Wert als neue GLI f&uuml;r das Objekt ein. Hierbei handelt es
     * sich um eine Identifikationsnummer, die &uuml;ber alle Datenbest&auml;nde der Anwendung 
     * eindeutig ist und so z. B. zur Synchronisation von Datens&auml;tzen genutzt werden kann.
     *
     * @param gli Der neue Wert f&uuml;r die GLI des Objekts.
     */
    public void setGLI(long gli);

}
