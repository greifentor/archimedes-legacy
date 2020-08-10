/*
 * OptionXMLBuilder.java
 *
 * 18.01.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.scheme.xml;

import archimedes.legacy.model.OptionModel;
import archimedes.legacy.model.*;
import corentx.xml.*;


/**
 * A builder for an option node of a XML representation of an Archimedes diagram. This builder
 * will be used by numerous other builders.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.01.2016 - Added.
 */

public class OptionXMLBuilder extends AbstractXMLBuilder {

    /**
     * Creates a new options XML node for the passed Archimedes diagram object like a table or
     * the diagram itself.
     *
     * @param options The options which are to be transfered into a XML node construct.
     * @param xmlNodeFactory A factory for XML node generation. 
     * @return A XML node with the options of the diagram object.
     *
     * @changed OLI 18.01.2016 - Added.
     */
    public XMLNode buildNodes(OptionModel[] options, XMLNodeFactory xmlNodeFactory) {
        XMLNode optionsNode = xmlNodeFactory.createXMLRootNode("Options");
        for (OptionModel o : options) {
            XMLNode optionNode = xmlNodeFactory.createXMLNode(optionsNode, "Option");
            this.addAttribute(optionNode, "name", o.getName());
            this.addAttribute(optionNode, "parameter", o.getParameter());
        }
        return optionsNode;
    }

}