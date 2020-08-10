/*
 * UserInformation.java
 *
 * 24.02.2012
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * Dieses Interface bietet Methoden f&uuml;r Klassen an, die Benutzerinformationen wie Name, 
 * Hersteller und K&uuml;rzel enthalten.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
 */

public interface UserInformation {

    /**
     * Liefert den Namen des Benutzer der Installation.
     *
     * @return Der Name des Benutzer der Installation.
     *
     *@changed OLI 24.02.2012 - Hinzugef&uuml;gt.
     */
    abstract public String getUserName();

    /**
     * Liefert das K&uuml;rzel des Benutzer der Installation.
     *
     * @return Das K&uuml;rzel des Benutzer der Installation.
     *
     *@changed OLI 24.02.2012 - Hinzugef&uuml;gt.
     */
    abstract public String getUserToken();

    /**
     * Liefert den Namen des Hersteller, z. B. zur Nutzung in Generatoren.
     *
     * @return Der Name des Herstellers.
     *
     *@changed OLI 24.02.2012 - Hinzugef&uuml;gt.
     */
    abstract public String getVendorName();

}