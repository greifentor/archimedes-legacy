/* 
 * ObjectFactory.java
 *
 * 15.04.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

import archimedes.legacy.app.ArchimedesDescriptorFactory;
import corent.base.Direction;

/**
 * Dieses Interface definiert die Funktionen einer Archimedes-ObjectFactory.
 * Hiermit wird die Erweiterung der Datenklassen erm&ouml;glicht.
 * 
 * @author ollie
 * 
 * @changed OLI 06.10.2007 - Erweiterung um die Methoden
 *          <TT>setADF(ArchimedesDescriptorFactory)</TT> und <TT>getADF()</TT>.<BR>
 *          OLI 18.12.2007 - Erweiterung um die Methode
 *          <TT>createTabelle(ViewModel, int, int, 
 *             DiagrammModel, String)</TT>.<BR>
 * 
 */

public interface ObjectFactory {

	/** @return Ein DiagrammModel mit Defaulteinstellungen. */
	public DiagrammModel createDiagramm();

	/** @return Ein DomainModel mit Defaulteinstellungen. */
	public DomainModel createDomain();

	/**
	 * Generiert ein DomainModel anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param name
	 *            Der Name der Domain.
	 * @param dt
	 *            Der Datatype zur Domain.
	 * @param len
	 *            Eine Feldl&auml;ngenangabe.
	 * @param nks
	 *            Eine Angabe zu den Nachkommastellen der Domain.
	 * @return Die neugenerierte Domain.
	 */
	public DomainModel createDomain(String name, int dt, int len, int nks);

	/**
	 * Generiert eine Relation anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param view
	 *            Der View, in dem die Referenz erzeugt werden soll.
	 * @param t1
	 *            Die Tabellenspalte, von der die Relation ausgeht.
	 * @param direction1
	 *            Die Richtung, von der aus die Relation von der Tabelle abgehen
	 *            soll.
	 * @param offset1
	 *            Die Entfernung von der oberen bzw. linke Kante auf der Seite,
	 *            von der die Relation ausgehen soll.
	 * @param t2
	 *            Die Tabellespalte, auf die die Relation zeigt.
	 * @param direction2
	 *            Die Richtung, von der aus die Relation auf die Tabelle treffen
	 *            soll.
	 * @param offset2
	 *            Die Entfernung von der oberen bzw. linke Kante auf der Seite,
	 *            von der aus die Relation auf die Tabelle treffen soll.
	 * @return Die neugenerierte Relation.
	 */
	@Deprecated
	public RelationModel createRelation(ViewModel view, TabellenspaltenModel t1, Direction direction1, int offset1,
			TabellenspaltenModel t2, Direction direction2, int offset2);

	/**
	 * Generates a relation with the passed parameters.
	 * 
	 * @param view
	 *            The view where the reference is created for.
	 * @param t1
	 *            The column which is referencing column t2.
	 * @param direction1
	 *            The direction which the relation meets the referencing table.
	 * @param offset1
	 *            The offset of the position of the relation line at the
	 *            referencing table.
	 * @param t2
	 *            The column of the referenced table.
	 * @param direction2
	 *            The direction which the relation meets the referenced table.
	 * @param offset2
	 *            The offset of the position of the relation line at the
	 *            referenced table.
	 * @return The new relation.
	 * 
	 * @changed OLI 29.04.2013 - Added.
	 */
	abstract public RelationModel createRelation(ViewModel view, ColumnModel t1, Direction direction1, int offset1,
			ColumnModel t2, Direction direction2, int offset2);

	/**
	 * Creates a new selection member object with the passed parameter.
	 * 
	 * @param tsm
	 *            The column which should be treated as a selection member.
	 */
	abstract public SelectionMemberModel createSelectionMember(TabellenspaltenModel tsm);

	/**
	 * Creates a new sequence with the passed parameters.
	 * 
	 * @param name
	 *            The name of the sequence.
	 * @param increment
	 *            The increment of the sequence.
	 * @param startValue
	 *            The start value of the sequence.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	abstract public SequenceModel createSequence(String name, long increment, long startValue);

	/**
	 * Generiert eine Stereotype mit Defaultwerten.
	 * 
	 * @return Die neugenerierte Stereotype.
	 */
	public StereotypeModel createStereotype();

	/**
	 * Generiert eine Stereotype mit dem &uuml;bergebenen Namen der
	 * dazugeh&ouml;rigen Beschreibung.
	 * 
	 * @param name
	 *            Der Name der Stereotype.
	 * @param kommentar
	 *            Eine Beschreibung zur Stereotype.
	 * @return Die neugenerierte Stereotype.
	 */
	public StereotypeModel createStereotype(String name, String kommentar);

	/**
	 * Generiert eine neue Tabelle mit den angegebenen Parametern.
	 * 
	 * @param view
	 *            Referenz auf einen View, in den die Tabelle eingef&uuml;gt
	 *            werden soll.
	 * @param x
	 *            Die X-Koordinate der linken, oberen Ecke der Tabelle.
	 * @param y
	 *            Die Y-Koordinate der linken, oberen Ecke der Tabelle.
	 * @param d
	 *            Das Diagramm, zu dem die Tabelle geh&ouml;rt.
	 * @param s
	 *            Eine Sammlung von Property-Definitionen, die als Grundlage
	 *            f&uuml;r die neuzuerzeugende Tabelle dienen.
	 * @return Die neugenerierte Tabelle.
	 * 
	 * @changed OLI 18.12.2007 - Hinzugef&uml;gt.<BR>
	 * 
	 */
	public TabellenModel createTabelle(ViewModel view, int x, int y, DiagrammModel d, String s);

	/**
	 * Generiert eine neue Tabelle mit den angegebenen Parametern.
	 * 
	 * @param view
	 *            Referenz auf einen View, in den die Tabelle eingef&uuml;gt
	 *            werden soll.
	 * @param x
	 *            Die X-Koordinate der linken, oberen Ecke der Tabelle.
	 * @param y
	 *            Die Y-Koordinate der linken, oberen Ecke der Tabelle.
	 * @param d
	 *            Das Diagramm, zu dem die Tabelle geh&ouml;rt.
	 * @param filled
	 *            Wird diese Flagge gesetzt, so wird die erzeugte Tabelle mit
	 *            Standardeinstellungen und -spalten vorbelegt.
	 * @return Die neugenerierte Tabelle.
	 */
	/*
	 * @param template Diese Flagge mu&szlig; zus&auml;tzlich zur filled-Flagge
	 * gesetzt werden, wenn statt der festverdrahteten Standardtabelle eine aus
	 * einer Template-Datei erzeugt werden soll.
	 */
	public TabellenModel createTabelle(ViewModel view, int x, int y, DiagrammModel d, boolean filled /*
																									 * ,
																									 * boolean
																									 * template
																									 */);

	/**
	 * Generiert eine Tabellenspalte anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param n
	 *            Der Name der Tabellenspalte.
	 * @param dom
	 *            Die Domain zur Tabellenspalte.
	 * @return Die neugenerierte Tabellenspalte.
	 */
	public TabellenspaltenModel createTabellenspalte(String n, DomainModel dom);

	/**
	 * Generiert eine Tabellenspalte anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param n
	 *            Der Name der Tabellenspalte.
	 * @param dom
	 *            Die Domain zur Tabellenspalte.
	 * @param pk
	 *            Wird diese Flagge gesetzt, so handelt es sich bei der
	 *            Tabellenspalte um ein Member des Prim&auml;rschl&uuml;ssels.
	 * @return Die neugenerierte Tabellenspalte.
	 */
	public TabellenspaltenModel createTabellenspalte(String n, DomainModel dom, boolean pk);

	/**
	 * Generiert einen DefaultComment mit leerem Namensmuster und
	 * DefaultKommentar.
	 * 
	 * @return Der neugenerierte DefaultComment.
	 */
	public DefaultCommentModel createDefaultComment();

	/**
	 * Generiert einen DefaultComment mit dem &uuml;bergebenen Namensmuster und
	 * dem dazugeh&ouml;rigen Default-Kommentar.
	 * 
	 * @param name
	 *            Das Namensmuster des DefaultComments
	 * @param kommentar
	 *            Der Kommentar zum angegebenen Namensmuster.
	 * @return Der neugenerierte DefaultComment.
	 */
	public DefaultCommentModel createDefaultComment(String name, String kommentar);

	/**
	 * Generiert eine CodeFactory, mit deren Hilfe Programmcode zum Diagramm
	 * erzeugt werden soll.
	 * 
	 * @param cls
	 *            Ein Klassenname, zu dem die CodeFactory generiert werden soll.
	 * @return Die neugenerierte CodeFactory.
	 */
	public CodeFactory createCodeFactory(String cls);

	/**
	 * Generiert eine NReferenz.
	 * 
	 * @return Die neugenerierte NReferenz.
	 */
	public NReferenzModel createNReferenz();

	/**
	 * Generiert eine NReferenz mit Hilfe eines TabellenModels.
	 * 
	 * @param tm
	 *            Ein TabellenModel, das bei der Erzeugung des NReferenzModels
	 *            genutzt werden soll.
	 * @return Die neugenerierte NReferenz.
	 */
	public NReferenzModel createNReferenz(TabellenModel tm);

	/**
	 * Generiert einen leeren View.
	 * 
	 * @return Der neugenerierte View.
	 */
	public ViewModel createView();

	/**
	 * Generiert einen leeren MainView.
	 * 
	 * @param name
	 *            Der Name des MainViews.
	 * @param beschreibung
	 *            Eine Beschreibung zum MainView.
	 * @param isShowReferencedColumns
	 *            Diese Flagge mu&szlig: gesetzt werden, wenn der MainView die
	 *            durch Fremdschl&uuml;ssel referenzierten Tabellenspalten
	 *            anzeigen soll.
	 * @return Der neugenerierte MainView.
	 */
	public ViewModel createMainView(String name, String beschreibung, boolean isShowReferencedColumns);

	/** @return Ein neues PanelModel mit Defaultwerten. */
	public PanelModel createPanel();

	/**
	 * Returns a new option.
	 * 
	 * @return A new option with default values.
	 * 
	 * @changed OLI 15.10.2013 - Added.
	 */
	abstract public OptionModel createOption();

	/** @return Ein neues OrderMemberModel mit Defaultwerten. */
	public OrderMemberModel createOrderMember();

	/**
	 * Generiert ein neues OrderMemberModel mit der &uuml;bergebenen
	 * Tabellenspalte.
	 * 
	 * @param tsm
	 *            Das an das OrderMemberModel zu &uuml;bergebene
	 *            TabellenspaltenModel.
	 */
	public OrderMemberModel createOrderMember(TabellenspaltenModel tsm);

	/**
	 * Eine Referenz auf die ArchimedesDescriptorFactory, mit der die
	 * ObjectFactory arbeitet, falls es eine solche gibt.
	 * 
	 * @return Eine Referenz auf die ArchimedesDescriptorFactory mit der die
	 *         ObjectFactory arbeitet, oder <TT>null</TT>, falls es eine solche
	 *         nicht gibt.
	 */
	public ArchimedesDescriptorFactory getADF();

	/**
	 * Setzte eine ArchimedesDescriptorFactory f&uuml;r die ObjectFactory. Diese
	 * Option mu&szlig; nur dann genutzt werden, wenn die ObjectFactory mit
	 * Objekten arbeitet, die sich in einer Archimedes.Applikationslogik
	 * bewegen.
	 * 
	 * @param adf
	 *            Die ArchimedesDescriptorFactory, mit Hilfe derer die
	 *            ObjectFactory ihre Objekte generieren soll.
	 */
	public void setADF(ArchimedesDescriptorFactory adf);

	/**
	 * Setzt den &uuml;bergebenen Dateinamen als neuen Namen f&uuml;r die
	 * Template-Datei der ObjectFactory ein.
	 * 
	 * @param fn
	 *            Der Name der neuen Template-Datei.
	 */
	public void setTemplateFilename(String fn);

}
