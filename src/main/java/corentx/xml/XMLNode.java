/*
 * XMLNode.java
 *
 * 08.03.2013
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.xml;


import static corentx.util.Checks.*;

import corentx.util.*;

import java.util.*;


/**
 * A class which represents a XML node (with sub node and / or values).
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.03.2013 - Added.
 */

public class XMLNode extends AbstractXMLElement {

    private List<XMLElement> subNodes = new Vector<XMLElement>();
    private Map<String, XMLAttribute> attributes = new Hashtable<String, XMLAttribute>();

    /**
     * Creates a new XML value with the passed parameters.
     *
     * @param name The name of the XML value.
     * @throws IllegalArgumentException Passing an empty or null string.
     *
     * @changed OLI 08.03.2013 - Added.
     */
    XMLNode(String name) throws IllegalArgumentException {
        this(null, name);
    }

    /**
     * Creates a new XML value with the passed parameters and adds it to the passed parent node.
     *
     * @param parent A parent node which the created node is to add. Passing null here causes
     *         no exception but also adds the created node to no parent.
     * @param name The name of the XML value.
     * @throws IllegalArgumentException Passing an empty or null string.
     *
     * @changed OLI 11.03.2013 - Added.
     */
    XMLNode(XMLNode parent, String name) throws IllegalArgumentException {
        super(name);
        if (parent != null) {
            parent.add(this);
        }
    }

    /**
     * Adds the passed XML element to the node.
     *
     * @param xmlElement The XML element to add to the node.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 08.03.2013 - Added.
     */
    public void add(XMLElement xmlElement) throws IllegalArgumentException {
        ensure(xmlElement != null, "xml element to add cannot be null.");
        this.subNodes.add(xmlElement);
    }

    /**
     * Adds an attribute with the passed name and value to the node. If there is already an
     * attribute existing with the same name, the value will be overwritten.
     *
     * @param name The name of the attribute.
     * @param value The value for the attribute.
     * @return The changed node.
     *
     * @changed OLI 08.10.2015 - Added.
     */
    public XMLNode addAttribute(String name, String value) {
        XMLAttribute attr = new XMLAttribute(name, value);
        this.attributes.put(name, attr);
        return this;
    }

    /**
     * Returns an array with the copies of the attributes of the node.
     *
     * @return An array with the copies of the attributes of the node.
     *
     * @changed OLI 08.10.2015 - Added.
     */
    public XMLAttribute[] getAttributes() {
        List<XMLAttribute> l = new SortedVector<XMLAttribute>();
        for (XMLAttribute a : this.attributes.values()) {
            l.add(a.copy());
        }
        return l.toArray(new XMLAttribute[0]);
    }

    /**
     * Returns an array with the elements of the node.
     *
     * @return An array with the elements of the node.
     *
     * @changed OLI 08.03.2013 - Added.
     */
    public XMLElement[] getElements() {
        return this.subNodes.toArray(new XMLElement[0]);
    }

    /**
     * Removes the attribute with the passed name from the node.
     *
     * @param name The name of the attribute.
     * @return The changed node.
     *
     * @changed OLI 08.10.2015 - Added.
     */
    public XMLNode removeAttribute(String name) {
        this.attributes.remove(name);
        return this;
    }

    /**
     * @changed OLI 08.03.2013 - Added.
     */
    @Override public String toXML(int initialIndent, int indent, String lineFeedSeq) {
        StringBuffer sb = new StringBuffer();
        sb.append(this.indent(initialIndent)).append(this.openTag()).append(lineFeedSeq);
        for (XMLElement element : this.subNodes) {
            sb.append(element.toXML(initialIndent + indent, indent, lineFeedSeq));
        }
        String ct = this.closeTag();
        if (ct.length() > 0) {
            sb.append(this.indent(initialIndent)).append(ct).append(lineFeedSeq);
        }
        return sb.toString();
    }

    /**
     * @changed OLI 08.03.2013 - Added.
     */
    @Override protected String closeTag() {
        if (this.subNodes.size() == 0) {
            return "";
        }
        return super.closeTag();
    }

    /**
     * @changed OLI 08.03.2013 - Added.
     */
    protected String openTag() {
        String s = "";
        for (XMLAttribute a : this.getAttributes()) {
            s += " " + a.toXML();
        }
        return "<" + this.getName() + s + (this.subNodes.size() == 0 ? " /" : "") + ">";
    }

}