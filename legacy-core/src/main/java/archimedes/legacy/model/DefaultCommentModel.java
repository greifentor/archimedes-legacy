/*
 * DefaultCommentModel.java
 *
 * 02.05.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

import archimedes.model.CommentOwner;
import corent.base.Attributed;
import corent.djinn.Selectable;
import corent.djinn.TabbedEditable;

/**
 * Diese Interface beschreibt den f&uuml;r einen DefaultComment notwendigen
 * Methodenumfang.<BR>
 * <HR>
 * 
 * @author ollie
 * 
 */

public interface DefaultCommentModel extends Attributed, CommentOwner, Comparable, TabbedEditable, Selectable {

	/**
	 * @return Das Namensmuster, nachdem bestimmt werden soll, ob der
	 *         DefaultComment f&uuml;r eine bestimmte Tabellenspalte g&uuml;ltig
	 *         sein soll. Das Muster darf mit einem Stern (Asterix) als
	 *         "Beliebig"-Platzhalter beginnen oder enden. Andere Kombinationen
	 *         m&uuml;ssen nicht akzeptiert werden.
	 */
	public String getPattern();

	/** @return Der Default-Kommentar zum Muster. */
	public String getDefaultComment();

}
