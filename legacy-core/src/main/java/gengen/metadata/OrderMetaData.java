/*
 * OrderMetaData.java
 *
 * 04.06.2010
 *
 * (c) by O.Lieshoff
 *
 */

package gengen.metadata;


/**
 * Ein Interface zur Darstellung von Sortierkriterien.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.06.2010 - Hinzugef&uuml;gt.
 */

public interface OrderMetaData {

    /**
     * Liefert das Attribut, das zur Sortierung herhalten soll.
     *
     * @return Das Attribut, das zur Sortierung herhalten soll.
     *
     * @changed OLI 04.06.2010 - Hinzugef&uuml;t.
     */
    public AttributeMetaData getAttribute();

    /**
     * Liefert die Sortierrichtung.
     *
     * @return Die Sortierreihenfolge.
     *
     * @changed OLI 04.06.2010 - Hinzugef&uuml;t.
     */
    public OrderDirection getDirection();

}
