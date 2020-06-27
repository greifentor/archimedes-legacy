/*
 * DefaultDBFactory.java
 *
 * 12.06.2005
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db.xs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import corent.db.DefaultKey;
import corent.db.Identifiable;
import corent.db.OrderByDescriptor;
import corent.db.Persistent;
import logging.Logger;

/**
 * Diese Klasse implementiert die rudiment&auml;ren Methoden der DBFactory und
 * l&auml;&szlig;t sich mit wenig Aufwand an spezielle Klassen anpassen.
 * <P>
 * <B>Properties:</B> <BR>
 * <TABLE BORDER=1>
 * <TR VALIGN=TOP>
 * <TH>Property</TH>
 * <TH>Default</TH>
 * <TH>Typ</TH>
 * <TH>Beschreibung</TH>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.db.xs.AbstractDBFactory.suppress.dbfc.warning</TD>
 * <TD>false</TD>
 * <TD>Boolean</TD>
 * <TD>Wird diese Property gesetzt, so entf&auml;llt das Anzeigen der
 * Warnmeldung im Falle der Instanziierung einer AbstractDBFactory ohne
 * DBFactoryController-Referenz.</TD>
 * </TR>
 * </TABLE>
 * <P>
 *
 * @author O.Lieshoff
 *
 * @changed OLI 27.08.2007 - Erweiterung um eine Referenz auf den
 *          DBFactoryController, in dem sich die DBFactory-Implementierung
 *          befindet. In diesem Rahmen mu&szlig;te auch die Signatur des
 *          Kontruktors um eine Referenz auf den DBFactoryController erweitert
 *          werden.
 *          <P>
 *          OLI 02.09.2007 - Wiedereinf&uuml;hrung des parameterlosen
 *          Konstruktors. Dieser gibt jedoch eine gut sichtbare Warnmeldung auf
 *          der Konsole aus.
 *          <P>
 *          OLI 03.09.2007 - Die Warnhinweise, da&szlig; die AbstractDBFactory
 *          ohne die Referenz auf einen DBFactoryController instanziert worden
 *          ist, kann durch das Setzen der Property
 *          <TT>corent.db.xs.AbstractDBFactory.suppress.dbfc.warning</TT>
 *          (Boolean) ausgeschaltet werden.
 *          <P>
 *          OLI 23.05.2008 - Erweiterung um die Methode
 *          <TT>prepareDuplication(T, T)</TT>.
 *          <P>
 *          OLI 16.07.2008 - &Auml;nderungen an der <TT>write</TT>-Methode im
 *          Rahmen der &Auml;nderung des Interfaces <TT>DBFactory</TT>
 *          <P>
 *          OLI 20.01.2009 - Erweiterung um den <TT>protected</TT>-Konstruktor
 *          <TT>AbstractDBFactory(DBFactoryController, boolean)</TT> zur
 *          Verwendung in nicht abstrakten, generischen Subklassen.
 *          <P>
 *          OLI 29.01.2009 - Erweiterung um die Implementierung der Methode
 *          <TT>getSelectionView(String, String, Connection, boolean)</TT>.
 *          <P>
 *
 */

abstract public class AbstractDBFactory<T extends Persistent> implements DBFactory<T> {

	private static final Logger log = Logger.getLogger(AbstractDBFactory.class);

	/**
	 * Die Referenz auf den DBFactoryController, zu dem die AbstractDBFactory
	 * geh&ouml;rt.
	 */
	protected DBFactoryController dbfc = null;
	/** Eine Besipielinstanz der Klasse, die mit der Factory bedient werden soll. */
	protected T instance = null;

	/**
	 * Generiert eine AbstractDBFactory mit Defaultwerten.
	 * <P>
	 * Dieser Konstruktor stellt keine Verbindung mit dem DBFactoryController her,
	 * zu dem die AbstractDBFactory geh&ouml;rt. Es kann hierdurch zu
	 * Funktionsst&ouml;rungen kommen.
	 *
	 * @changed OLI 02.09.2007 - Parameterloser Konstruktor mit hinreichender
	 *          Warnmeldung wiedereingef&uuml;hrt.<BR>
	 *          <P>
	 *          OLI 03.09.2007 - Die Warnmeldung, die dar&uuml;ber informiert,
	 *          da&szlig; keine DBFactoryController-Referenz besteht, kann nun
	 *          &uuml;ber das Setzen der Property
	 *          <TT>corent.db.xs.AbstractDBFactory.suppress.dbfc.warning</TT>
	 *          unterdr&uuml;ckt werden.
	 *          <P>
	 *
	 */
	public AbstractDBFactory() {
		super();
		log.warn("***** WARNING: AbstractDBFactory has been instaniated without DBFactoryController reference!");
		log.warn("*****          Calling Class is " + this.getClass().getName() + " !\n\n");
		this.instance = this.create();
	}

	/**
	 * Generiert eine AbstractDBFactory mit Defaultwerten.
	 *
	 * @param dbfc Eine Referenz auf den DBFactoryController, in dem sich die
	 *             DBFactory befindet.
	 *
	 * @changed OLI 27.08.2007 - Erweiterung um den Parameter <TT>dbfc</TT>.
	 *          <P>
	 *          OLI 03.09.2007 - Die Warnmeldung im Falle, da&szlig;
	 *          dbfc==<TT>null</TT> gilt, kann nun &uuml;ber das Setzen der Property
	 *          <TT>corent.db.xs.AbstractDBFactory.suppress.dbfc.warning</TT>
	 *          unterdr&uuml;ckt werden.
	 *          <P>
	 *
	 */
	public AbstractDBFactory(DBFactoryController dbfc) {
		super();
		if (dbfc == null) {
			log.warn(
					"***** WARNING: AbstractDBFactory has been instaniated with null-reference for DBFactoryController!");
			log.warn("*****          Calling Class is " + this.getClass().getName() + "!");
		}
		this.dbfc = dbfc;
		this.instance = this.create();
	}

	/**
	 * Generiert eine AbstractDBFactory anhand der &uuml;bergebenen Parameter.
	 * Dieser Konstruktor findet seine Anwendung in nicht abstrakten, generischen
	 * Ableitungen der Klasse.
	 *
	 * @param dbfc         Eine Referenz auf den DBFactoryController, in dem sich
	 *                     die DBFactory befindet.
	 * @param initInstance Wird diese Flagge gesetzt, so wird bereits im Konstruktor
	 *                     die Variable <TT>instance</TT> initialisiert.
	 *
	 * @changed OLI 20.01.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	protected AbstractDBFactory(DBFactoryController dbfc, boolean initInstance) {
		super();
		if (dbfc == null) {
			log.warn("**** AbstractDBFactory has been instaniated with null-reference for DBFactoryController!");
			log.warn("***** Calling Class is " + this.getClass().getName() + "!");
		}
		this.dbfc = dbfc;
		if (initInstance) {
			this.instance = this.create();
		}
	}

	/* Implementierung des Interfaces DBFactory<T>. */

	@Override
	abstract public T create();

	@Override
	abstract public String createFilter(Object[] criteria);

	@Override
	public Object doAction(Connection c, int id, Object... p) throws IllegalArgumentException, SQLException {
		throw new IllegalArgumentException("Action " + id + " is not implemented in DBFactory");
	}

	@Override
	public T duplicate(T o, Connection c) throws SQLException {
		T b = this.generate(c);
		Object pk = null;
		if (o instanceof Identifiable) {
			pk = ((Identifiable) b).getId();
		} else if (o instanceof HasKey) {
			pk = ((HasKey) b).getKey();
		}
		this.prepareDuplication(o, b);
		b = (T) b.createObject(o);
		if ((o instanceof Identifiable) && (((Identifiable) o).getId() instanceof DefaultKey)) {
			((Identifiable) b).setId(new DefaultKey(b, ((DefaultKey) pk).getAttributes()));
		} else if (o instanceof HasKey) {
			((HasKey) b).setKey(pk);
		}
		return b;
	}

	@Override
	public T generate(Connection c) throws SQLException {
		T t = this.create();
		DBFactoryUtil.Generate(t, c, (this instanceof GenerateExpander ? (GenerateExpander) this : null));
		return t;
	}

	@Override
	public DBFactoryController getDBFactoryController() {
		return this.dbfc;
	}

	@Override
	@Deprecated
	/**
	 * @changed OLI 29.01.2009 - In einen Aufruf der Methode
	 *          <TT>getSelectionView(String, String, 
	 *             Connection, boolean)</TT>.
	 *          <P>
	 *
	 * @deprecated OLI 29.01.2009 - Wird durch die Methode
	 *             <TT>getSelectionView(String, String, 
	 *             Connection, boolean)</TT> ersetzt.
	 * 
	 */
	public SelectionTableModel getSelectionView(String w, String aj, Connection c) throws SQLException {
		return this.getSelectionView(w, aj, c, false);
	}

	/**
	 * @changed OLI 29.01.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	@Override
	public SelectionTableModel getSelectionView(String w, String aj, Connection c, boolean suppressFilling)
			throws SQLException {
		return DBFactoryUtil.GetSelectionView(this.create(), w, aj, c, false, suppressFilling);
	}

	@Override
	public boolean isUnique(T o, Connection c) throws SQLException {
		String u = o.getPersistenceDescriptor().getUniqueClause();
		if (u != null) {
			return DBFactoryUtil.IsUnique(o, c);
		}
		return true;
	}

	/**
	 * Diese Methode wird aufgerufen, bevor innerhalb einer Duplikation Daten von
	 * der Vorlage in das Duplikat &uuml;bertragen werden. Die Methode kann zu
	 * diesem Zweck &uuml;berschrieben werden.
	 *
	 * @param o Das Object, das als Vorlage f&uuml;r das Duplikat herh&auml;lt.
	 * @param g Das frisch generierte Object, das mit den Daten der Vorlage
	 *          sp&auml;ter bef&uuml;llt werden soll.
	 *
	 * @changed OLI 23.05.2008 - Hinzugef&uuml;gt.
	 *
	 */
	public void prepareDuplication(T o, T g) {
		log.warn("using standard AbstractDBFactory method.");
	}

	public Vector<T> read(String w, Connection c) throws SQLException {
		return DBFactoryUtil.Read(this.instance, w, c, null, false);
	}

	@Override
	public Vector<T> read(String w, Connection c, OrderByDescriptor o, boolean includeRemoved) throws SQLException {
		return DBFactoryUtil.Read(this.instance, w, c, o, includeRemoved);
	}

	@Override
	public void remove(T b, boolean forced, Connection c) throws SQLException {
		DBFactoryUtil.Remove(b, forced, c);
	}

	@Override
	public void removeBatch(Vector k, boolean forced, Connection c) throws SQLException {
		T o = this.create();
		if (o instanceof Persistent) {
			// DBFactoryUtil.RemoveBatch(((Persistent) o).getPersistenceDescriptor(), k,
			// forced, c);
		}
	}

	@Override
	public T write(T o, Connection c) throws SQLException {
		if ((o instanceof Identifiable) && !((Identifiable) o).hasValidKey()) {
			DBFactoryUtil.Generate(o, c, (this instanceof GenerateExpander ? (GenerateExpander) this : null));
		}
		DBFactoryUtil.Write(o, c);
		return o;
	}

	@Override
	public void writeBatch(Vector k, Hashtable<Integer, Object> ta, Connection c) throws SQLException {
		T o = this.create();
		if (o instanceof Persistent) {
			DBFactoryUtil.WriteBatch(((Persistent) o).getPersistenceDescriptor(), k, ta, c);
		}
	}

}
