/*
 * StereotypeXMLBuilder.java
 *
 * 03.01.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.scheme.xml;

import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.StereotypeModel;
import archimedes.legacy.model.*;

import corentx.xml.*;


/**
 * A builder for a stereotypes node of a XML representation of an Archimedes diagram.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 03.01.2016 - Added.
 */

public class StereotypeXMLBuilder extends AbstractXMLBuilder {

    /**
     * Creates a new stereotypes XML node for the passed Archimedes data model.
     *
     * @param dataModel The data model which the stereotypes node is to create for.
     * @param xmlNodeFactory A factory for XML node generation. 
     * @return A XML node with the stereotypes of the Archimedes data model.
     *
     * @changed OLI 03.01.2016 - Added.
     */
    public XMLNode buildNodes(DataModel dataModel, XMLNodeFactory xmlNodeFactory) {
        XMLNode sequences = xmlNodeFactory.createXMLRootNode("Stereotypes");
        for (StereotypeModel stm : dataModel.getStereotypes()) {
            XMLNode stereotype = xmlNodeFactory.createXMLNode(sequences, "Stereotype");
            this.addAttribute(stereotype, "name", stm.getName());
            this.addAttribute(stereotype, "comment", stm.getComment());
            this.addAttribute(stereotype, "doNotPrint", String.valueOf(stm.isDoNotPrint()));
            this.addAttribute(stereotype, "hideTable", String.valueOf(stm.isHideTable()));
            this.addAttribute(stereotype, "history", stm.getHistory());
        }
        return sequences;
    }

}