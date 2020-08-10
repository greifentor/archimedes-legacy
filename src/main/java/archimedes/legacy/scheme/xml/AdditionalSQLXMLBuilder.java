/*
 * AdditionalSQLXMLBuilder.java
 *
 * 06.01.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.scheme.xml;

import archimedes.legacy.model.DataModel;
import corentx.xml.XMLNode;
import corentx.xml.XMLNodeFactory;

/**
 * A builder for a additional SQL statements node of a XML representation of an Archimedes diagram.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 06.01.2016 - Added.
 */

public class AdditionalSQLXMLBuilder extends AbstractXMLBuilder {

	/**
	 * Creates a new additional SQL statements XML node for the passed Archimedes diagram.
	 *
	 * @param diagram        The diagram model which the additional SQL statements node is to create for.
	 * @param xmlNodeFactory A factory for XML node generation.
	 * @return A XML node with the stereotypes of the Archimedes diagram.
	 *
	 * @changed OLI 06.01.2016 - Added.
	 */
	public XMLNode buildNodes(DataModel diagram, XMLNodeFactory xmlNodeFactory) {
		XMLNode additionalSQLStatements = xmlNodeFactory.createXMLRootNode("AdditionalSQLStatements");
		this.addAttribute(additionalSQLStatements, "postChanging", diagram.getAdditionalSQLCodePostChangingCode());
		this.addAttribute(additionalSQLStatements, "postReducing", diagram.getAdditionalSQLCodePostReducingCode());
		this.addAttribute(additionalSQLStatements, "preChanging", diagram.getAdditionalSQLCodePreChangingCode());
		this.addAttribute(additionalSQLStatements, "preExtending", diagram.getAdditionalSQLCodePreExtendingCode());
		return additionalSQLStatements;
	}

}