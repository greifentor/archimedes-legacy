/*
 * ViewXMLBuilder.java
 *
 * 19.02.2016
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.legacy.scheme.xml;

import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.ViewModel;
import archimedes.legacy.model.*;
import corentx.xml.*;


/**
 * A builder for a views node of a XML representation of an Archimedes diagram.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.02.2016 - Added.
 */

public class ViewXMLBuilder extends AbstractXMLBuilder {

    /**
     * Creates a views node with the passed complex indices.
     *
     * @param views The views which are to write to the node.
     * @param xmlNodeFactory A factory for XML node generation. 
     * @return A XML node with the options of the diagram object.
     *
     * @changed OLI 19.02.2016 - Added.
     */
    public XMLNode buildNodes(ViewModel[] views, XMLNodeFactory xmlNodeFactory) {
        XMLNode viewsNode = xmlNodeFactory.createXMLRootNode("Views");
        for (ViewModel vm : views) {
            XMLNode viewNode = xmlNodeFactory.createXMLNode(viewsNode, "View");
            this.addAttribute(viewNode, "name", vm.getName());
            this.addAttribute(viewNode, "description", vm.getComment());
            this.addAttribute(viewNode, "hideTechnicalFields", String.valueOf(
                    vm.isHideTechnicalFields()));
            this.addAttribute(viewNode, "hideTransientFields", String.valueOf(
                    vm.isHideTransientFields()));
            this.addAttribute(viewNode, "mainView", String.valueOf(vm.isMainView()));
            this.addAttribute(viewNode, "showReferencedColumns", String.valueOf(
                    vm.isShowReferencedColumns()));
            for (TableModel tm : vm.getTables()) {
                XMLNode tableNode = xmlNodeFactory.createXMLNode(viewNode, "Table");
                this.addAttribute(tableNode, "name", tm.getName());
            }
        }
        return viewsNode;
    }

}