/*
 * XMLElement.java
 *
 * 08.03.2013
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.xml;


/**
 * A XML element like a node or a value.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.03.2013 - Added.
 */

public interface XMLElement {

    /**
     * Returns the name of the XML element.
     *
     * @return The name of the XML element.
     *
     * @changed OLI 08.03.2013 - Added.
     */
    abstract public String getName();

    /**
     * Returns a string representation of the element.
     *
     * @param indent The indention of the tag.
     * @return A string representation of the element.
     *
     * @changed OLI 08.03.2013 - Added.
     */
    abstract public String toXML(int initialIndent, int indent, String lineFeedSeq);

}