/*
 * IndexMetaDataListModel.java
 *
 * 15.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.indices;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import archimedes.legacy.model.IndexMetaData;

/**
 * Eine <CODE>ListModel</CODE>-Implementierung zur Darstellung von
 * <CODE>IndexMetaData</CODE> Implementierungen in der GUI.
 * 
 * @author ollie
 * 
 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
 */

public class IndexMetaDataListModel implements ListModel {

	private IndexMetaData[] list = null;

	/**
	 * Erzeugt ein neues ListModel auf Basis der angegebenen Liste von
	 * IndexMetaData-Objekten.
	 * 
	 * @param indices
	 *            Die Liste der <CODE>IndexMetaData</CODE>, die durch das Model
	 *            repr&auml;sentiert werden soll.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public IndexMetaDataListModel(IndexMetaData[] indices) {
		super();
		this.list = indices;
	}

	/**
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void addListDataListener(ListDataListener l) {
	}

	/**
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public Object getElementAt(int index) {
		return this.list[index];
	}

	/**
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public int getSize() {
		return this.list.length;
	}

	/**
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void removeListDataListener(ListDataListener l) {
	}

}