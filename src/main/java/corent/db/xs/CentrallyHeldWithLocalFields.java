/*
 * CentrallyHeldWithLocalFields.java
 *
 * 23.03.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db.xs;


import java.util.*;


/**
 * Dieses Interface mu&szlig; von Klassen implementiert werden, die zentralisiert gehalten 
 * werden und &uuml;ber eine globale Identifikationsnummer verf&uuml;gen, aber auch Felder 
 * kennen, die im lokalen Umfeld ge&auml;ndert werden k&ouml;nnen.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 23.03.2009 - Hinzugef&uuml;gt.
 *     <P>
 *
 */

public interface CentrallyHeldWithLocalFields {

    /**
     * Liefert eine Liste der Komponentennamen, die auch editierbar sein sollen, wenn das Objekt
     * zentral ist und sich im lokalen Umfeld bewegt.
     *
     * @return Eine Liste mit den Komponentennamen der lokal editierbaren Felder.
     */
    public List<String> getNonCentralFieldNames();

}
