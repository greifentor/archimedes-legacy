/* 
 * NReferenzModel.java
 *
 * 19.10.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

import corent.base.Attributed;
import corent.djinn.Editable;

/**
 * Dieses Interface definiert die Funktionalit&auml;t, die ein Objekt haben
 * mu&szlig;, um als NReferenzModel genutzt werden zu k&ouml;nnen.
 * NReferenzModels beschreiben die Parameter f&uuml;r automatisch-generierte
 * Editore bzw. deren Descriptoren, die zur Manipulation von der n-Seite von
 * Referenzen im Datenmodell dienen.<BR>
 * <HR>
 * 
 * @author ollie
 * 
 */

public interface NReferenzModel extends Attributed, Editable, NReferenceModel {

	/**
	 * @return Die Tabellenspalte in der referenzierten Tabelle, auf die sich
	 *         die NReferenz bezieht.
	 */
	@Deprecated
	public TabellenspaltenModel getTabellenspalte();

	/**
	 * Setzt die angegebene Tabellenspalte als neue Tabellenspalte ein,
	 * &uuml;ber die die Referenz gebildet werden soll. Prinzipiell sollte hier
	 * nur eine Tabellenspalte angegeben, die auch innerhalb des Modells eine
	 * Referenz zwischen den angegebenen Tabellen beschreibt.
	 * 
	 * @param tsm
	 *            Die Tabellenspalte, &uuml;ber die die Referenz gebildet werden
	 *            soll.
	 */
	@Deprecated
	public void setTabellenspalte(TabellenspaltenModel tsm);

}
