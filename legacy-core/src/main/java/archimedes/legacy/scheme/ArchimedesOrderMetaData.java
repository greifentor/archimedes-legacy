/*
 * ArchimedesOrderMetaData.java
 *
 * 04.06.2010
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import gengen.metadata.*;


/**
 * Archimedesimplementierung des GenGen-OrderMetaData-Modells.
 *
 * @author ollie
 *
 * @changed OLI 04.06.2010 - Hinzugef&uuml;gt.
 */

public class ArchimedesOrderMetaData implements OrderMetaData {

    private AttributeMetaData amd = null;
    private OrderDirection od = null;

    /**
     * Generiert ein ArchimedesOrderMetaData-Objekt mit den angegebenen Daten.
     *
     * @param amd Das Attribut, auf das sich die Sortierung bezieht.
     * @param od Die Sortierreihenfolge.
     * @throws IllegalArgumentException Falls einer der Parameter als <TT>null</TT>-Referenz
     *         &uuml;bergeben wird.
     * @precondition amd != <TT>null</TT>
     * @precondition od != <TT>null</TT>
     *
     * @changed OLI 04.06.2010 - Hinzugef&uuml;gt.
     */
    public ArchimedesOrderMetaData(AttributeMetaData amd, OrderDirection od)
            throws IllegalArgumentException {
        super();
        if (amd == null) {
            throw new IllegalArgumentException("attribute meta data can not be null.");
        }
        if (od == null) {
            throw new IllegalArgumentException("order direction can not be null.");
        }
        this.amd = amd;
        this.od = od;
    }

    @Override
    /**
     * @changed OLI 04.06.2010 - Hinzugef&uuml;t.
     */
    public AttributeMetaData getAttribute() {
        return this.amd;
    }

    @Override
    /**
     * @changed OLI 04.06.2010 - Hinzugef&uuml;t.
     */
    public OrderDirection getDirection() {
        return this.od;
    }

}
