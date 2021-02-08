/*
 * SequenceXMLBuilder.java
 *
 * 03.01.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.scheme.xml;

import archimedes.model.*;

import corentx.xml.*;


/**
 * A builder for a sequences node of a XML representation of an Archimedes diagram.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 03.01.2016 - Added.
 */

public class SequenceXMLBuilder extends AbstractXMLBuilder {

    /**
     * Creates a new sequences XML node for the passed Archimedes diagram.
     *
     * @param diagram The diagram model which the sequences node is to create for.
     * @param xmlNodeFactory A factory for XML node generation. 
     * @return A XML node with the sequences of the Archimedes diagram.
     *
     * @changed OLI 03.01.2016 - Added.
     */
    public XMLNode buildNodes(DataModel diagram, XMLNodeFactory xmlNodeFactory) {
        XMLNode sequences = xmlNodeFactory.createXMLRootNode("Sequences");
        for (SequenceModel sm : diagram.getSequences()) {
            XMLNode sequence = xmlNodeFactory.createXMLNode(sequences, "Sequence");
            this.addAttribute(sequence, "name", sm.getName());
            this.addAttribute(sequence, "comment", sm.getComment());
            this.addAttribute(sequence, "history", sm.getHistory());
            this.addAttribute(sequence, "startValue", String.valueOf(sm.getStartValue()));
            this.addAttribute(sequence, "increment", String.valueOf(sm.getIncrement()));
        }
        return sequences;
    }

}