/*
 * DomainsXMLBuilder.java
 *
 * 08.10.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.scheme.xml;


import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.*;
import corentx.xml.*;


/**
 * A builder for a domains node of a XML representation of an Archimedes diagram.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.10.2015 - Added.
 */

public class DomainsXMLBuilder extends AbstractXMLBuilder {

    /**
     * Creates a new domains XML node for the passed Archimedes diagram.
     *
     * @param diagram The diagram model which the domains node is to create for.
     * @param xmlNodeFactory A factory for XML node generation. 
     * @return A XML node with the domains of the Archimedes diagram.
     *
     * @changed OLI 08.10.2015 - Added.
     */
    public XMLNode buildNodes(DataModel diagram, XMLNodeFactory xmlNodeFactory) {
        XMLNode domains = xmlNodeFactory.createXMLRootNode("Domains");
        for (Object o : diagram.getAllDomains()) {
            DomainModel d = (DomainModel) o;
            XMLNode domain = xmlNodeFactory.createXMLNode(domains, "Domain");
            this.addAttribute(domain, "name", d.getName());
            this.addAttribute(domain, "comment", d.getComment());
            this.addAttribute(domain, "history", d.getHistory());
            this.addAttribute(domain, "initialValue", d.getInitialValue());
            this.addAttribute(domain, "parameters", d.getParameters());
            this.addAttribute(domain, "type", d.getType());
            this.addAttribute(domain, "dataType", String.valueOf(d.getDataType()));
            this.addAttribute(domain, "decimalPlace", String.valueOf(d.getDecimalPlace()));
            this.addAttribute(domain, "length", String.valueOf(d.getLength()));
        }
        return domains;
    }

}