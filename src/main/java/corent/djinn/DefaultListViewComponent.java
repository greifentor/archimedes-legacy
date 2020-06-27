/*
 * DefaultListViewComponent.java
 *
 * 18.02.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.djinn;

import java.awt.Font;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 * Eine Musterimplementierung f&uuml;r eine auf einer JList basierenden
 * ViewComponent.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public class DefaultListViewComponent implements ViewComponent {

	/*
	 * Referenz auf das JList-Objekt, das die eigentliche Anzeigekomponente
	 * beherbergt.
	 */
	private JList anzeige = null;
	/*
	 * Referenz auf die vollst&auml;ndige Liste, aus der der View einen Ausschnitt
	 * zeigt.
	 */
	private Vector liste = null;

	/**
	 * Generiert eine DefaultListViewComponent anhand der &uuml;bergebenen
	 * Parameter.
	 *
	 * @param liste Die Liste, deren Inhalt in der ViewComponent angezeigt werden
	 *              soll.
	 */
	public DefaultListViewComponent(Vector liste) {
		super();
		this.liste = liste;
		this.anzeige = new JList(new DefaultListViewListModel(liste));
		this.anzeige.setFont(new Font("monospaced", Font.PLAIN, 14));
	}

	/* Implementierung des Interfaces ViewComponent. */

	@Override
	public Vector getSelectedValues() {
		Object[] values = this.anzeige.getSelectedValues();
		Vector selected = new Vector();
		for (int i = 0; i < values.length; i++) {
			if (values[i] instanceof ListViewSelectableContainer) {
				selected.addElement(((ListViewSelectableContainer) values[i]).getContent());
			} else {
				selected.addElement(values[i]);
			}
		}
		return selected;
	}

	@Override
	public int getSelectedValuesCount() {
		return this.anzeige.getSelectedValues().length;
	}

	@Override
	public JComponent getView() {
		return new JScrollPane(this.anzeige);
	}

	@Override
	public JComponent getViewComponent() {
		return this.anzeige;
	}

	@Override
	public int updateView(Object[] criteria, String additions) throws IllegalArgumentException {
		if (((criteria != null) && (criteria.length > 0) && !(criteria[0] instanceof String))
				|| ((criteria != null) && (criteria.length > 1))) {
			throw new IllegalArgumentException("ViewComponent does only except one " + "String-criteria!");
		}
		Vector v = new Vector();
		if (this.liste != null) {
			for (int i = 0, len = this.liste.size(); i < len; i++) {
				Object o = this.liste.elementAt(i);
				if (o instanceof Selectable) {
					Selectable sel = (Selectable) o;
					if (sel.isSelected(criteria)) {
						v.addElement(sel);
					}
				} else if (criteria == null) {
					v.addElement(o);
				}
			}
		}
		this.anzeige.setModel(new DefaultListViewListModel(v));
		int lim = Integer.getInteger("corent.djinn.ViewComponent.maximum", 0);
		int count = this.anzeige.getModel().getSize();
		if ((lim > 0) && (count > lim)) {
			count = 0 - count;
		}
		return count;
	}

	@Override
	public void close() {
	}

	@Override
	public void init() {
	}

}
