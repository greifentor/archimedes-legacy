/*
 * XMLNodeFactory.java
 *
 * 25.02.2015
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.xml;


/**
 * A factory for XML nodes. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 25.02.2015 - Added.
 */

public class XMLNodeFactory {

    /**
     * Creates a XML node with string value and add it as a child to the passed node.
     *
     * @param parent The XML node which is used as parent. If a <CODE>null</CODE> pointer is
     *         passed for the parent the node is treated as root node.
     * @param name A name for the new node.
     * @return A new XML node with the passed name and linked as child to the passed parent.
     * @throws IllegalArgumentException Passing an empty or <CODE>null</CODE> string as name. 
     *
     * @changed OLI 25.02.2015 - Added.
     */
    public XMLNode createXMLNode(XMLNode parent, String name) throws IllegalArgumentException {
        return new XMLNode(parent, name);
    }

    /**
     * Creates a XML root node with string value and add it as a child to the passed node.
     *
     * @param name A name for the new node.
     * @return A new XML node with the passed name and linked as child to the passed parent.
     * @throws IllegalArgumentException Passing an empty or <CODE>null</CODE> string as name. 
     *
     * @changed OLI 25.02.2015 - Added.
     */
    public XMLNode createXMLRootNode(String name) throws IllegalArgumentException {
        return this.createXMLNode(null, name);
    }

}