/*
 * DiagramParameterXMLBuilder.java
 *
 * 23.02.2016
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.scheme.xml;

import archimedes.model.DataModel;
import corentx.xml.XMLNode;
import corentx.xml.XMLNodeFactory;

/**
 * A builder for the parameters of a XML representation of an Archimedes diagram.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.02.2016 - Added.
 */

public class DiagramParameterXMLBuilder extends AbstractXMLBuilder {

	/**
	 * Creates a new parameter XML node for the passed Archimedes diagram.
	 *
	 * @param diagram        The diagram model which the parameters node is to create for.
	 * @param xmlNodeFactory A factory for XML node generation.
	 * @return A XML node with the parameters of the Archimedes diagram.
	 *
	 * @changed OLI 03.01.2016 - Added.
	 */
	public XMLNode buildNodes(DataModel diagram, XMLNodeFactory xmlNodeFactory) {
		XMLNode parameters = xmlNodeFactory.createXMLRootNode("Parameters");
		this.addAttribute(parameters, "additionalDiagramInfo", diagram.getAdditionalDiagramInfo());
		this.addAttribute(parameters, "additionalSQLScriptListener", diagram.getAdditionalSQLScriptListener());
		this.addAttribute(parameters, "applicationName", diagram.getApplicationName());
		this.addAttribute(parameters, "deprecatedTablesHidden", String.valueOf(diagram.isDeprecatedTablesHidden()));
		this.addAttribute(parameters, "author", diagram.getAuthor());
		this.addAttribute(parameters, "basePackageName", diagram.getBasePackageName());
		this.addAttribute(parameters, "codeFactoryClassName", diagram.getCodeFactoryClassName());
		this.addAttribute(parameters, "codeBasePath", diagram.getBasicCodePath());
		this.addAttribute(parameters, "comment", diagram.getComment());
		this.addAttribute(parameters, "dbVersionColumnDescriptionName", diagram.getDBVersionDescriptionColumnName());
		this.addAttribute(parameters, "dbVersionColumnVersionName", diagram.getDBVersionVersionColumnName());
		this.addAttribute(parameters, "dbVersionTableName", diagram.getDBVersionTableName());
		this.addAttribute(parameters, "disableTechnicalFields", String.valueOf(diagram.isPaintTechnicalFieldsInGray()));
		this.addAttribute(parameters, "disableTransientFields", String.valueOf(diagram.isPaintTransientFieldsInGray()));
		this.addAttribute(parameters, "domainShowMode", String.valueOf(diagram.getDomainShowMode()));
		this.addAttribute(parameters, "fontSizeDiagramHeadlines", String.valueOf(diagram.getFontSizeDiagramHeadline()));
		this.addAttribute(parameters, "fontSizeDiagramSubtitles", String.valueOf(diagram.getFontSizeSubtitles()));
		this.addAttribute(parameters, "fontSizeTableContents", String.valueOf(diagram.getFontSizeTableContents()));
		this.addAttribute(parameters, "history", diagram.getHistory());
		this.addAttribute(parameters, "markUpRequiredFieldNames", String.valueOf(diagram.isMarkUpRequiredFieldNames()));
		this.addAttribute(parameters, "name", diagram.getName());
		this.addAttribute(parameters, "owner", diagram.getOwner());
		this
				.addAttribute(
						parameters,
						"relationColorExternalTables",
						String.valueOf(diagram.getRelationColorToExternalTables()));
		this.addAttribute(parameters, "relationColorRegular", String.valueOf(diagram.getRelationColorRegular()));
		this.addAttribute(parameters, "schemeName", diagram.getSchemaName());
		this.addAttribute(parameters, "scriptAfterWrite", diagram.getAfterWriteScript());
		this.addAttribute(parameters, "showReferencedColumns", String.valueOf(diagram.isShowReferencedColumns()));
		this.addAttribute(parameters, "uscheptiClassName", diagram.getUdschebtiBaseClassName());
		this.addAttribute(parameters, "version", diagram.getVersion());
		this.addAttribute(parameters, "versionComment", diagram.getVersionComment());
		this.addAttribute(parameters, "versionDate", String.valueOf(diagram.getVersionDate()));
		return parameters;
	}

}
