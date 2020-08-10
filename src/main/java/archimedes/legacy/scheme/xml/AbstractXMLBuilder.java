/*
 * AbstractXMLBuilder.java
 *
 * 19.02.2016
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.legacy.scheme.xml;

import corentx.util.*;
import corentx.xml.*;


/**
 * A basic XML builder.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.02.2016 - Added.
 */

abstract public class AbstractXMLBuilder {

    /**
     * Adds an attribute with the passed name and value to the passed XML node.
     *
     * @param xmlNode The XML node which the attribute is to add to.
     * @param name The name of the new attribute.
     * @param value The value for the attribute.
     * @throws IllegalArgumentException passing a null pointer.
     *
     * @changed OLI 19.02.2016 - Added.
     */
    public void addAttribute(XMLNode xmlNode, String name, String value) {
        value = (value == null ? "" : value);
        value = value.replace("\r\n", "\n").replace("\r", "\n").replace("\n", "$BR$");
        value = Str.toHTML(value);
        xmlNode.addAttribute(name, value);
    }

}