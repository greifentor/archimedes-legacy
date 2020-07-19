/*
 * ListAttributeData.java
 *
 * 17.06.2015
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.acf;


import static corentx.util.Checks.*;

import archimedes.acf.ListAttribute.ListAttributeType;

import java.util.*;


/**
 * A container for list attribute data.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.06.2015 - Added.
 */

public class ListAttributeData {

    private String attributeDefinitionBlockCode = "";
    private ListAttribute[] listAttributes = null;

    /**
     * Creates a new list attribute data container with the passed parameters.
     *
     * @param attributeDefinitionBlockCode The code which defines the list attributes of the
     *         object.
     * @param listAtrtributes An arrays with the list attributes.
     *
     * @changed OLI 17.06.2015 - Added.
     */
    public ListAttributeData(String attributeDefinitionBlockCode, ListAttribute[] listAttributes
            ) {
        super();
        ensure(attributeDefinitionBlockCode != null, "attribute definition block code cannot be"
                + " null.");
        ensure(listAttributes != null, "list attributes array cannot be null.");
        this.attributeDefinitionBlockCode = attributeDefinitionBlockCode;
        this.listAttributes = listAttributes;
    }

    /**
     * Returns the code for the attribute definition block.
     *
     * @return The code for the attribute definition block.
     *
     * @changed OLI 17.06.2015 - Added.
     */
    public String getAttributeDefinitionBlockCode() {
        return this.attributeDefinitionBlockCode;
    }

    /**
     * Returns an array with the list attributes (with variable names).
     *
     * @return An array with the list attributes (with variable names)
     *
     * @changed OLI 17.06.2015 - Added.
     */
    public ListAttribute[] getListAttributes() {
        return this.listAttributes;
    }

    /**
     * Returns the list attribute types which are present in the list attributes.
     *
     * @return The list attribute types which are present in the list attributes.
     *
     * @changed OLI 07.10.2015 - Added.
     */
    public ListAttributeType[] getListAttributeTypes() {
        List<ListAttributeType> l = new LinkedList<ListAttributeType>();
        for (ListAttribute la : this.listAttributes) {
            if (!l.contains(la.getType())) {
                l.add(la.getType());
            }
        }
        return l.toArray(new ListAttributeType[0]);
    }

    /**
     * Checks if there are some list attributes found.
     *
     * @return <CODE>true</CODE> if there are some list attributes found.
     *
     * @changed OLI 17.06.2015 - Added.
     */
    public boolean hasListAttributes() {
        return this.getListAttributes().length > 0;
    }

}