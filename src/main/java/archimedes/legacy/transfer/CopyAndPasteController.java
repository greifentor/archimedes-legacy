/*
 * CopyAndPasteController.java
 *
 * 08.12.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.transfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.scheme.xml.ModelXMLReader;
import archimedes.legacy.scheme.xml.ObjectFactory;
import archimedes.legacy.scheme.xml.TableXMLBuilder;
import corentx.xml.XMLNode;
import corentx.xml.XMLNodeFactory;

/**
 * A class which controls the copy and paste operations.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.12.2016 - Added.
 */

abstract public class CopyAndPasteController {

	/**
	 * Converts a table to a transferable format like a string.
	 *
	 * @param tm             The table model which should be converted to the transferable string.
	 * @param xmlNodeFactory A XML node factory for the table and its components.
	 * @return A string with the data of the table which can be used to restore the table.
	 *
	 * @changed OLI 08.12.2016 - Added.
	 */
	public String tableToTransferableString(TableModel tm, XMLNodeFactory xmlNodeFactory) {
		XMLNode node = new TableXMLBuilder().buildNodes(new TableModel[] { tm }, xmlNodeFactory);
		return node.toXML(0, 4, "\n");
	}

	/**
	 * Converts a table to a transferable object with data flavor "stringFlavor".
	 *
	 * @param tm             The table model which should be converted to the transferable string.
	 * @param xmlNodeFactory A XML node factory for the table and its components.
	 * @return A string with the data of the table which can be used to restore the table.
	 *
	 * @changed OLI 09.12.2016 - Added.
	 */
	public Transferable tableToTransferable(TableModel tm, XMLNodeFactory xmlNodeFactory) {
		final String s = this.tableToTransferableString(tm, xmlNodeFactory);
		return new Transferable() {
			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.stringFlavor };
			}

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return flavor == DataFlavor.stringFlavor;
			}

			@Override
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				return s;
			}
		};
	}

	/**
	 * Converts a table to a transferable object with data flavor "stringFlavor".
	 *
	 * @param tm The table model which should be converted to the transferable string.
	 * @return A string with the data of the table which can be used to restore the table.
	 *
	 * @changed OLI 09.12.2016 - Added.
	 */
	abstract public Transferable tableToTransferable(TableModel tm);

	/**
	 * Converts tables form a transferable format table data and add them to the passed data model.
	 *
	 * @param dm            The data model which the tables are to add to.
	 * @param objectFactory An object factory which is used to create the data objects.
	 * @param s             The string with the table data.
	 * @return The read and creates table models.
	 * @throws Exception In case of an error occurs while restoring the table.
	 *
	 * @changed OLI 08.12.2016 - Added.
	 */
	public TableModel[] transferableStringToTable(DataModel dm, ObjectFactory objectFactory, String s)
			throws Exception {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Reader reader = new StringReader(s);
		InputSource is = new InputSource(reader);
		is.setEncoding("UTF-8");
		Document doc = builder.parse(is);
		return new ModelXMLReader(objectFactory).readTables(dm,
				(NodeList) xpath.evaluate("/Tables/Table", doc, XPathConstants.NODESET));
	}

	/**
	 * Converts tables form a transferable format table data and add them to the passed data model.
	 *
	 * @param dm            The data model which the tables are to add to.
	 * @param objectFactory An object factory which is used to create the data objects.
	 * @param s             The string with the table data.
	 * @return The read and creates table models.
	 * @throws Exception In case of an error occurs while restoring the table.
	 *
	 * @changed OLI 08.12.2016 - Added.
	 */
	abstract public TableModel[] transferableStringToTable(DataModel dm, String s) throws Exception;

}