
/*
 * DatabaseConnectionsXMLBuilder.java
 *
 * 08.10.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.scheme.xml;

import archimedes.legacy.connections.ArchimedesImportJDBCDataSourceRecord;
import archimedes.legacy.connections.DatabaseConnection;
import archimedes.legacy.connections.DatabaseConnectionProvider;
import archimedes.legacy.model.DataModel;
import corentx.xml.XMLNode;
import corentx.xml.XMLNodeFactory;

/**
 * A builder for a database connections node of a XML representation of an Archimedes diagram.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.10.2015 - Added.
 */

public class DatabaseConnectionsXMLBuilder extends AbstractXMLBuilder {

	/**
	 * Creates a new database connections XML node for the passed Archimedes diagram.
	 *
	 * @param dcp            The object model which the database connections node is to create for.
	 * @param xmlNodeFactory A factory for XML node generation.
	 * @return A XML node with the database connections of the Archimedes diagram.
	 *
	 * @changed OLI 08.10.2015 - Added.
	 */
	public XMLNode buildNodes(DatabaseConnectionProvider dcp, XMLNodeFactory xmlNodeFactory) {
		XMLNode dcns = xmlNodeFactory.createXMLRootNode("DataSources");
		XMLNode dcn = xmlNodeFactory.createXMLNode(dcns, "Import");
		ArchimedesImportJDBCDataSourceRecord dsr = (ArchimedesImportJDBCDataSourceRecord) ((DataModel) dcp)
				.getImportDataSourceRecord();
		if (dsr == null) {
			dsr = new ArchimedesImportJDBCDataSourceRecord("IMPORT", "IMPORT", "usr", "", false, false);
		}
		this.addAttribute(dcn, "name", dsr.getName());
		this.addAttribute(dcn, "dbName", dsr.getDBName());
		this.addAttribute(dcn, "driver", dsr.getDriver());
		this.addAttribute(dcn, "description", (dsr.getDescription() == null ? "-" : dsr.getDescription()));
		this.addAttribute(dcn, "domains", String.valueOf(dsr.isDomains()));
		this.addAttribute(dcn, "references", String.valueOf(dsr.isReferenzen()));
		this.addAttribute(dcn, "user", dsr.getUser());
		for (DatabaseConnection dc : dcp.getDatabaseConnections()) {
			dcn = xmlNodeFactory.createXMLNode(dcns, "DatabaseConnection");
			this.addAttribute(dcn, "name", dc.getName());
			this.addAttribute(dcn, "dbExecMode", dc.getDBMode().name());
			this.addAttribute(dcn, "driver", dc.getDriver());
			this.addAttribute(dcn, "quote", dc.getQuote());
			this.addAttribute(dcn, "setDomains", String.valueOf(dc.isSetDomains()));
			this.addAttribute(dcn, "setNotNull", String.valueOf(dc.isSetNotNull()));
			this.addAttribute(dcn, "setReferences", String.valueOf(dc.isSetReferences()));
			this.addAttribute(dcn, "url", dc.getUrl());
			this.addAttribute(dcn, "userName", dc.getUserName());
		}
		return dcns;
	}

}