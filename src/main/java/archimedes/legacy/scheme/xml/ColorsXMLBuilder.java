/*
 * ColorsXMLBuilder.java
 *
 * 08.10.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.scheme.xml;


import archimedes.*;
import corent.gui.*;
import corentx.xml.*;


/**
 * A builder for a colors node of a XML representation of an Archimedes diagram.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.10.2015 - Added.
 */

public class ColorsXMLBuilder extends AbstractXMLBuilder {

    /**
     * Creates a new colors XML node for the passed Archimedes diagram.
     *
     * @param xmlNodeFactory A factory for XML node generation.
     * @param colorsToSave The colors to save. 
     * @return A XML node with the colors of the Archimedes diagram.
     *
     * @changed OLI 08.10.2015 - Added.
     */
    public XMLNode buildNodes(XMLNodeFactory xmlNodeFactory, ExtendedColor[] colorsToSave) {
        XMLNode colors = xmlNodeFactory.createXMLRootNode("Colors");
        for (ExtendedColor ec : colorsToSave) {
            XMLNode color = xmlNodeFactory.createXMLNode(colors, "Color");
            this.addAttribute(color, "name", ec.toString());
            this.addAttribute(color, "r", String.valueOf(ec.getRed()));
            this.addAttribute(color, "g", String.valueOf(ec.getGreen()));
            this.addAttribute(color, "b", String.valueOf(ec.getBlue()));
        }
        return colors;
    }

}