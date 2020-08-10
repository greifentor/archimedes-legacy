/*
 * DefaultCopyAndPasteController.java
 *
 * 08.12.2016
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.transfer;

import java.awt.datatransfer.Transferable;

import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import corentx.xml.XMLNodeFactory;

/**
 * 
 * 
 * @author ollie
 * 
 * @changed OLI 08.12.2016 - Added.
 */

public class DefaultCopyAndPasteController extends CopyAndPasteController {

	/**
	 * @changed OLI 09.12.2016 - Added.
	 */
	@Override
	public Transferable tableToTransferable(TableModel tm) {
		return super.tableToTransferable(tm, new XMLNodeFactory());
	}

	/**
	 * Converts a table to a transferable format like a string.
	 * 
	 * @param tm
	 *            The table model which should be converted to the transferable
	 *            string.
	 * @return A string with the data of the table which can be used to restore
	 *         the table.
	 * 
	 * @changed OLI 08.12.2016 - Added.
	 */
	public String tableToTransferableString(TableModel tm) {
		return super.tableToTransferableString(tm, new XMLNodeFactory());
	}

	/**
	 * @changed OLI 08.12.2016 - Added.
	 */
	@Override
	public TableModel[] transferableStringToTable(DataModel dm, String s) throws Exception {
		return super.transferableStringToTable(dm, new ArchimedesObjectFactory(), s);
	}

}