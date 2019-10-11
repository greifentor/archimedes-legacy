/*
 * IndexMetaDataCellRenderer.java
 *
 * 15.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.indices;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import archimedes.model.IndexMetaData;

/**
 * Ein CellRenderer zur Anzeige der <CODE>IndexMetaData</CODE>-Datens&auml;tze.
 * 
 * @author ollie
 * 
 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
 */

public class IndexMetaDataCellRenderer extends DefaultListCellRenderer {

	/**
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value != null) {
			l.setText(((IndexMetaData) value).getName());
		}
		return l;
	}

}