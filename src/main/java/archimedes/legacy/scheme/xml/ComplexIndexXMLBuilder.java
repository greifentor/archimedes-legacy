/*
 * ComplexIndexXMLBuilder.java
 *
 * 19.02.2016
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.legacy.scheme.xml;

import gengen.metadata.*;
import archimedes.legacy.model.IndexMetaData;
import archimedes.legacy.model.*;
import corentx.xml.*;


/**
 * A builder for a complex indices node of a XML representation of an Archimedes diagram.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.02.2016 - Added.
 */

public class ComplexIndexXMLBuilder extends AbstractXMLBuilder {

    /**
     * Creates a new complex indices node with the passed complex indices.
     *
     * @param complexIndices The complex indices which are to write to the node.
     * @param xmlNodeFactory A factory for XML node generation. 
     * @return A XML node with the options of the diagram object.
     *
     * @changed OLI 19.02.2016 - Added.
     */
    public XMLNode buildNodes(IndexMetaData[] complexIndices, XMLNodeFactory xmlNodeFactory) {
        XMLNode complexIndicesNode = xmlNodeFactory.createXMLRootNode("ComplexIndices");
        for (IndexMetaData imd : complexIndices) {
            XMLNode complexIndexNode = xmlNodeFactory.createXMLNode(complexIndicesNode, "Index"
                    );
            this.addAttribute(complexIndexNode, "name", imd.getName());
            this.addAttribute(complexIndexNode, "tableName", imd.getTable().getName());
            for (AttributeMetaData amd : imd.getColumns()) {
                XMLNode columnNode = xmlNodeFactory.createXMLNode(complexIndexNode, "Column");
                this.addAttribute(columnNode, "name",  amd.getName());
            }
        }
        return complexIndicesNode;
    }

}