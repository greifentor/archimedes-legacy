/*
 * SelectionMetaData.java
 *
 * 04.06.2010
 *
 * (c) by O.Lieshoff
 *
 */

package gengen.metadata;


import java.util.*;


/**
 * Dieses Interface umfasst die Funktionen eines SelectionViewMetaData. Hiermit k&ouml;nnen
 * Metadaten f&uuml;r Suchansichten definiert werden.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.06.2010 - Hinzugef&uuml;gt.
 */

public interface SelectionViewMetaData {

    /**
     * Liefert die Liste der Attribute, die bei der Suche ber&uuml;cksichtigt werden sollen.
     *
     * @return Die Liste der Attribute, die bei der Suche ber&uuml;cksichtigt werden sollen.
     *
     * @changed OLI 04.06.2010 - Hinzugef&uuml;t.
     */
    public List<AttributeMetaData> getCheckedFields();

    /**
     * Liefert die Liste der Sortierkriterien f&uuml;r die Suchansicht.
     *
     * @return Die Liste der Sortierkriterien f&uuml;r die Suchansicht.
     *
     * @changed OLI 04.06.2010 - Hinzugef&uuml;t.
     */
    public List<OrderMetaData> getOrder();

    /**
     * Liefert die Liste der Attribute, die in der Suchansicht angezeigt werden sollen. Die
     * Liste ist in der Reihenfolge sortiert, in der sie angezeigt werden soll.
     *
     * @retrun Die Liste der Attribute, die in der Suchansicht angezeigt werden sollen.
     *
     * @changed OLI 04.06.2010 - Hinzugef&uuml;t.
     */
    public List<AttributeMetaData> getSelectionViewMember();

}
