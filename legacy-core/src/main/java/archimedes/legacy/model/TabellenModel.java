/*
 * TabellenModel.java
 *
 * 20.03.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

import gengen.metadata.ClassMetaData;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import archimedes.gui.PaintMode;
import archimedes.gui.diagram.CoordinateConverter;
import archimedes.gui.diagram.ShapeContainer;
import archimedes.model.CommentOwner;
import archimedes.model.HistoryOwner;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.GUIObjectModel;
import corent.base.Attributed;
import corent.djinn.TabbedEditable;

/**
 * Dieses Interface definiert das Verhalten eines TabellenModels.
 * 
 * @author ollie
 * 
 * @changed OLI 04.09.2008 - Erweiterung um die Spezifikation der Methode
 *          <TT>getReferencingFields()</TT>.
 * @changed OLI 15.09.2008 - &Uuml;berarbeitung der Dokumentation. Erweiterung
 *          um die Methoden <TT>getCodeGeneratorOption()</TT> und
 *          <TT>setCodeGeneratorOption(String)</TT>.
 * @changed OLI 12.11.2008 - Erweiterung um den PaintMode in der Methode
 *          <TT>paintTabelle</TT>.
 * @changed OLI 12.05.2009 - Erweiterung um die Spezifikation der Methode
 *          <TT>makeCreateStatement(boolean, boolean, boolean, String)</TT>.
 * @changed OLI 17.05.2009 - Erweiterung um die Spezifikation der Methode
 *          <TT>makeInsertStatementCounted()</TT>
 * @changed OLI 21.05.2009 - Erweiterung um die Spezifikation der Methode
 *          <TT>getFieldnames(boolean, boolean, boolean)</TT>.
 * 
 *          deprecated OLI 26.04.2008 - Herausnahme des Interfaces im Rahmen der
 *          Verenglischung der Klassenbibliothek.
 * 
 */

public interface TabellenModel extends Attributed, CommentOwner, ClassMetaData, GUIObjectModel, HistoryOwner,
		TabbedEditable, TableModel {

	/**
	 * F&uuml;gt die &uuml;bergebene Tabellenspalte der Tabelle hinzu. In diesem
	 * Zusammenhang sollte die Referenz innerhalb der Tabellenspalte gesetzt
	 * werden.
	 * 
	 * @param ts
	 *            Die hinzuzuf&uuml;gende Tabellenspalte.
	 */
	@Deprecated
	public void addTabellenspalte(TabellenspaltenModel ts);

	/**
	 * Pr&uuml;ft, ob der Spaltenname in der Tabelle vorkommt.
	 * 
	 * <P>
	 * <B>Author:</B> M.Eckstein
	 * 
	 * @param columnname
	 *            der Spaltenname.
	 * @return <TT>true</TT>, falls der Spaltenname in der Tabelle vorkommt,
	 *         <TT>false</TT> sonst.
	 */
	public boolean containsColumnname(String columnname);

	/**
	 * Eine Liste aller Tabellenspalten des TabellenModels, inklusive der, die
	 * aus anderen TabellenModels ererbt sind.
	 * 
	 * @return Eine Liste aller Tabellenspalten der Tabelle inklusive der
	 *         ererbten, falls die Tabelle von anderen Tabellen erbt.
	 */
	@Deprecated
	public java.util.List<TabellenspaltenModel> getAlleTabellenspalten();

	/**
	 * Eine Liste aller Tabellenspalten des TabellenModels, inklusive der, die
	 * aus anderen TabellenModels ererbt sind. Allerdings k&ouml;nnen doppelte
	 * Schl&uuml;sselspalten, die sich aus der Vererbung ergeben, hier
	 * ausgeblendet werden.
	 * 
	 * @param doublepk
	 *            Wird diese Flagge gel&ouml;scht, so werden die vererbten
	 *            Schl&uuml;sselspalten der Subtabellen nicht mit in die Liste
	 *            &uuml;bernommen, falls es sich bei der Tabelle um eine ererbte
	 *            Tabelle handelt.
	 * @return Eine Liste aller Tabellenspalten der Tabelle inklusive der
	 *         ererbten, falls die Tabelle von anderen Tabellen erbt.
	 */
	public java.util.List<TabellenspaltenModel> getAlleTabellenspalten(boolean doublepk);

	/**
	 * Die Liste der Attribute, die bei der Codegenerierung in die Methode zur
	 * Bildung einer Datenbanksuchauswahl einfliessen sollen.
	 * 
	 * @return <STRIKE>Eine Liste mit ToStringContainern, deren Attribute beim
	 *         Bau der Methode zur Bildung einer Datenbanksuchauswahl
	 *         ber&uuml;cksichtigt werden sollen bzw. eine leere Liste, wenn
	 *         eine solche Methode nicht implementiert werden soll.</STRIKE> <BR>
	 *         Eine Liste mit den TabellenspaltenModels, die in der
	 *         Auswahlanzeige zu finden sind.
	 * 
	 * @changed OLI 28.03.2008 - &Auml;nderung der Beschreibung nach
	 *          vergeblichem Versuch aus dem Vector einen ToStringContainer zu
	 *          extrahieren.
	 * 
	 * @deprecated OLI 26.02.2016 - See "getSelectableColumns()".
	 */
	@Deprecated
	public Vector getAuswahlMembers();

	/**
	 * Wertet das Feld mit den Codegeneratoroptionen f&uuml;r die Tabelle nach
	 * dem &uuml;bergebenen Tag aus und liefert eine Liste aller in dem Tag
	 * gefundenen Strings enth&auml;lt.
	 * 
	 * <P>
	 * <I>Beispiel: <BR>
	 * <TT>&lt;SuppressListGeneration&gt;Adress,
	 * ApplicationUserGroup&lt;/SuppressListGeneration&gt;</TT> in der Tabelle
	 * <TT>ApplicationUser</TT> <BR>
	 * liefert eine List("Adress", "ApplicationUserGroup"). </I>
	 * 
	 * @param tagName
	 *            Der Name des Tags, auf das die Codegeneratoroptionenfelder
	 *            durchsucht werden sollen.
	 * @param delimiter
	 *            Ein Trennzeichenfolge zum Absetzen der einzelnen
	 *            Listeneintr&auml;ge voneinander.
	 * @return Eine Liste der Strings, die in dem angegebenen Tag hinterlegt
	 *         sind.
	 * 
	 * @changed OLI 03.06.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 * 
	 */
	public java.util.List<String> getCodeGeneratorOptionsListTag(String tagName, String delimiter);

	/**
	 * Liefert das Verzeichnis, in das der Code zur Tabelle geschrieben werden
	 * soll.
	 * 
	 * @return Das Verzeichnis, in das der Code zur Tabelle geschrieben werden
	 *         soll.
	 */
	@Deprecated
	public String getCodeVerzeichnis();

	/**
	 * Gibt einen Array mit den Spaltennamen der Tabelle zur&uuml;ck.
	 * 
	 * <P>
	 * <B>Author:</B> M.Eckstein
	 * 
	 * @return Array mit den Spaltennamen.
	 */
	public String[] getColumnnames();

	/**
	 * Generiert anhand des TabellenModels ein Create-Statement zum Erzeugen der
	 * Tabelle.
	 * 
	 * @param hasDomains
	 *            Diese Flagge ist zu setzen, wenn das Create-Statement mit
	 *            Domains erzeugt werden soll.
	 * @param referenzenSetzen
	 *            Wird diese Flagge gesetzt, werden Referenzen als Foreignkeys
	 *            kodiert.
	 * @param fkNotNullBeachten
	 *            Ist diese Flagge gesetzt, werden entsprechende
	 *            Not-Null-Statements bei der Foreignkeyanlage gesetzt.
	 * @return Ein String mit dem Create-Statement zur Tabelle.
	 * @deprecated OLI 19.08.2007 - Wird durch die Methode
	 *             <TT>makeCreateStatement(boolean, 
	 *     boolean, boolean)</TT> ersetzt.
	 * @see #makeCreateStatement(boolean, boolean, boolean)
	 */
	@Deprecated
	public String getCreateStatement(boolean hasDomains, boolean referenzenSetzen, boolean fkNotNullBeachten);

	/**
	 * Liefert eine Referenz auf das DiagrammModel, in dem sich die Tabelle
	 * befindet.
	 * 
	 * @return Referenz auf das DiagrammModel, in dem sich die Tabelle befindet.
	 */
	@Deprecated
	public DiagrammModel getDiagramm();

	/**
	 * Die Liste der Attribute, die bei der Codegenerierung in die
	 * Gleichheitsmethode (z. B. equals) einfliessen sollen.
	 * 
	 * @return Eine Liste mit Tabellenspalten, deren Attribute beim Bau der
	 *         Gleichheitsmethode ber&uuml;cksichtigt werden sollen bzw. eine
	 *         leere Liste, wenn eine solche Methode nicht implementiert werden
	 *         soll. / public Vector getEqualsMembers();
	 * 
	 *         /** Generiert einen String mit den Feldnamen der Tabelle.
	 * 
	 * @param pks
	 *            Wird diese Flagge gesetzt, werden die
	 *            Prim&auml;schl&uuml;ssel-Spalten ber&uuml;cksichtigt.
	 * @param nonpks
	 *            Wird diese Flagge gesetzt, werden die
	 *            Nichtschl&uuml;ssel-Spalten ber&uuml;cksichtigt.
	 * @return Der String mit den Feldnamen (durch Kommata getrennt).
	 */
	public String getFieldnames(boolean pks, boolean nonpks);

	/**
	 * Generiert einen String mit den Feldnamen der Tabelle.
	 * 
	 * @param pks
	 *            Wird diese Flagge gesetzt, werden die
	 *            Prim&auml;schl&uuml;ssel-Spalten ber&uuml;cksichtigt.
	 * @param nonpks
	 *            Wird diese Flagge gesetzt, werden die
	 *            Nichtschl&uuml;ssel-Spalten ber&uuml;cksichtigt.
	 * @param qualifiedNames
	 *            Diese Flagge ist zu setzen, wenn die Feldnamen qualitifiziert
	 *            angegeben werden sollen (also im Format
	 *            Tabellenname"."Feldname).
	 * @return Der String mit den Feldnamen (durch Kommata getrennt).
	 */
	public String getFieldnames(boolean pks, boolean nonpks, boolean qualifiedNames);

	/**
	 * Generiert einen String mit den Feldnamen der Tabelle bei dem die
	 * Prim&auml;rschl&uuml;sselfelder, alphabetisch sortiert, voranstehen.
	 * 
	 * @param nonpks
	 *            Wird diese Flagge gesetzt, werden die
	 *            Nichtschl&uuml;ssel-Spalten ber&uuml;cksichtigt.
	 * @param qualifiedNames
	 *            Diese Flagge ist zu setzen, wenn die Feldnamen qualitifiziert
	 *            angegeben werden sollen (also im Format
	 *            Tabellenname"."Feldname).
	 * @return Der String mit den Feldnamen (durch Kommata getrennt), bei dem
	 *         die Prim&auml;rschl&uuml;sselfelder den anderen Feldern
	 *         voranstehen.
	 */
	public String getFieldnamesPKFirst(boolean nonpks, boolean qualifiedNames);

	/**
	 * Die Liste der Attribute, die bei der Codegenerierung in die Methode zu
	 * Bildung eines Hashcodes (z. B. hashCode) einfliessen sollen.
	 * 
	 * @return Eine Liste mit Tabellenspalten, deren Attribute beim Bau der
	 *         Methode zur Bildung eines Hashcodes ber&uuml;cksichtigt werden
	 *         sollen bzw. eine leere Liste, wenn eine solche Methode nicht
	 *         implementiert werden soll. / public Vector getHashCodeMembers();
	 * 
	 *         /** Liefert die Hintergrundfarbe des Tabellenkopfes in der
	 *         grafischen Anzeige.
	 * 
	 * @return Die Farbe, die der Hintergrund der Tabelle in Anzeige und
	 *         Ausdruck haben soll.
	 */
	public Color getHintergrundfarbe();

	/**
	 * Liefert eine Referenz auf das durch den angegebenen Index beschriebene
	 * NReferenzModel.
	 * 
	 * @param i
	 *            Der Index, zu dem das NReferenzModel geliefert werden soll.
	 * @return Eine Referenz auf das NReferenzModel zum angegebenen Index.
	 */
	public NReferenzModel getNReferenzModelAt(int i);

	/**
	 * Liefert die Anzahl der NReferenzModels des TabellenModels.
	 * 
	 * @return Die Anzahl der NReferenzModels im Model.
	 */
	public int getNReferenzModelCount();

	/**
	 * Liefert eine Referenz auf die Liste mit den n-seitig-manipulierbaren
	 * Referenzen.
	 * 
	 * @return Eine Referenz auf die Liste mit den n-seitig-manipulierbaren
	 *         Referenzen. / public Vector getNReferenzen();
	 * 
	 *         /** Die Liste der Attribute, die bei der Codegenerierung in die
	 *         Methode zur Bildung einer Datenbanksuchauswahl einfliessen sollen
	 *         und dort die Sortierung der Auswahl steuern.
	 * 
	 * @return Eine Liste mit ToStringContainern, deren Attribute beim Bau der
	 *         Methode zur Bildung einer Datenbanksuchauswahl in der Sortierung
	 *         der Ausgabe ber&uuml;cksichtigt werden sollen bzw. eine leere
	 *         Liste, wenn eine solche Methode nicht implementiert werden soll.
	 *         / public Vector getOrderMembers();
	 * 
	 *         /** Liefert das TabellenModel zur&uuml;ck, das das
	 *         "Eltern"-TabellenModel f&uuml;r dieses TabellenModel ist. Falls
	 *         es f&uuml;r dieses TabellenModel nicht existiert (
	 *         this.isInherited() == false), wird <TT>null</TT>
	 *         zur&uuml;ckgeliefert. (Es wird nichts geklont.)<BR>
	 *         <I>Anmerkung: Die Eltern-Kind-Beziehung ist etwas, was es in rein
	 *         relationalen Datenmodellen nicht gibt. Es handelt sich hier um
	 *         eine Archimedes-Erweiterung des Datenmodells.</I>
	 * 
	 * @return Das "Eltern"-TabellenModel falls vorhanden, ansonsten
	 *         <TT>null</TT>.
	 */
	public TabellenModel getParentTabellenModel();

	/**
	 * Liefert den Namen des Prim&auml;rschl&uuml;ssels, falls dieser aus genau
	 * einem Attribut besteht.
	 * 
	 * <P>
	 * <B>Author:</B> M.Eckstein
	 * 
	 * @return Der Name des Prim&auml;rschl&uuml;sselattributs der Tabelle oder
	 *         <TT>null</TT>, falls es keines oder mehr als ein
	 *         Prim&auml;rschl&uuml;sselattribut gibt.
	 */
	public String getPrimaryKeyName();

	/**
	 * Liefert die TabellenspaltenModels zur&uuml;ck, die
	 * Prim&auml;rschl&uuml;sselspalten des TabellenModels sind. Falls keine
	 * vorhanden sind, wird ein leerer Vector zur&uuml;ckgeliefert. (Es wird
	 * nichts geklont.)
	 * 
	 * @return Die Prim&auml;rschl&uuml;sselspalten des TabellenModels, ggf. ein
	 *         leerer Vector.
	 */
	public Vector<TabellenspaltenModel> getPrimaryKeyTabellenspalten();

	/**
	 * Liefert eine Map mit den Tabellenspalten, die Felder der vorliegenden
	 * Tabelle referenzieren. Die Tabellenspalten bilden hierbei den
	 * Schl&uuml;sselwert f&uuml;r die Map. Der Wertteil des Tupels ist die
	 * Tabellenspalten der vorliegenden Tabelle, auf die referenziert wird.
	 * 
	 * @return Map&gt;Tabellenspalte, Tabellenspalte&lt; mit den
	 *         Tabellenspalten, die auf eine Spalte der Tabelle verweisen und
	 *         den Tabellenspalten, auf die verwiesen wird.
	 * 
	 * @changed OLI 05.09.2008 - Hinzugef&uuml;gt.
	 *          <P>
	 * 
	 */
	public Map<TabellenspaltenModel, TabellenspaltenModel> getReferencingFields();

	/**
	 * Liefert die Schriftfarbe zur grafischen Anzeige der Tabelle.
	 * 
	 * @return Die Farbe, die die Beschriftung der Tabelle in Anzeige und
	 *         Ausdruck haben soll.
	 */
	public Color getSchriftfarbe();

	/**
	 * Liefert die Anzahl der Stereotypen, denen das TabellenModel zugeordnet
	 * ist.
	 * 
	 * @return Die Anzahl der Stereotypen im Model.
	 */
	public int getStereotypenCount();

	/**
	 * Ermittelt die Referenz der zum angegebenen Namen passenden
	 * Tabellenspalte.
	 * 
	 * @param n
	 *            Der Name der gesuchten Tabellenspalte.
	 * @return Die Referenz auf die gew&uuml;nschte Tabellenspalte. Wird keine
	 *         passende Tabellenspalte gefunden, so wird eine <TT>null</TT>
	 *         -Referenz zur&uuml;ckgeliefert.
	 */
	@Deprecated
	public TabellenspaltenModel getTabellenspalte(String n);

	/**
	 * Liefert eine Referenz auf die durch den angegebenen Index beschriebene
	 * Tabellenspalte.
	 * 
	 * @param i
	 *            Der Index, zu dem die Tabellenspaltenreferenz geliefert werden
	 *            soll.
	 * @return Eine Referenz auf die Tabellenspalte zum angegebenen Index.
	 */
	public TabellenspaltenModel getTabellenspalteAt(int i);

	/**
	 * Liefert eine Kopie der Liste der Tabellenspalten des TabellenModels.
	 * 
	 * @return Eine Kopie der Liste der Tabellenspalten des TabellenModels. Es
	 *         wird nur die Liste kopiert, nicht die Objekte, die in ihr
	 *         gespeichert sind.
	 */
	@Deprecated
	public Vector getTabellenspalten();

	/**
	 * Liefert die Anzahl der Tabellenspalten des TabellenModels.
	 * 
	 * @return Die Anzahl der Tabellenspalten im Model.
	 */
	public int getTabellenspaltenCount();

	/**
	 * Liefert die X-Koordinate der Tabelle zum angegebenen View.
	 * 
	 * @param view
	 *            Der View, zu dem die Koordinate geliefert werden soll.
	 * @return Die aktuelle X-Koordinate der linken, oberen Ecke der Tabelle
	 *         bzw. <TT>Integer.MIN_VALUE</TT>, wenn die Tabelle nicht in dem
	 *         angegebenen View enthalten ist.
	 */
	public int getX(ViewModel view);

	/**
	 * Liefert die Y-Koordinate der Tabelle zum angegebenen View.
	 * 
	 * @param view
	 *            Der View, zu dem die Koordinate geliefert werden soll.
	 * @return Die aktuelle Y-Koordinate der linken, oberen Ecke der Tabelle
	 *         bzw. <TT>Integer.MIN_VALUE</TT>, wenn die Tabelle nicht in dem
	 *         angegebenen View enthalten ist.
	 */
	public int getY(ViewModel view);

	/**
	 * Liefert den Wert <TT>true</TT>, wenn f&uuml;r eine der Tabellenspalten
	 * der Tabelle die gilt: isAlterInBatch() == true.
	 * 
	 * @return <TT>true</TT>, wenn mindestens f&uuml;r eine Tabellenspalte der
	 *         Tabelle die AlterInBatch-Flagge gesetzt ist.
	 */
	public boolean isAlterInBatch();

	/**
	 * Liefert eine Information dar&uuml;ber, ob eine Tabelle sich noch im
	 * Entwicklungsstadium befindet oder, ob sie bereits im produktiven Betrieb
	 * ist.
	 * 
	 * @return <TT>true</TT>, wenn die Tabelle sich noch im Entwicklungsstadium
	 *         befindet, <TT>false</TT> sonst. / public boolean
	 *         isInDevelopmentProcess();
	 * 
	 *         /** Pr&uuml;ft, ob dieses TabellenModel die Elterntabelle vom
	 *         TabellenModel <TT>kindTabelle</TT> ist.
	 *         <P>
	 *         <I>Anmerkung: Die Eltern-Kind-Beziehung ist etwas, was es in rein
	 *         relationalen Datenmodellen nicht gibt. Es handelt sich hier um
	 *         eine Archimedes-Erweiterung des Datenmodells.</I>
	 * 
	 * @param kindTabelle
	 *            Die Kindtabelle, von der dieses Objekt evtl. Elterntabelle
	 *            ist.
	 * @return <TT>true</TT>, falls dieses Objekt Elterntabelle von kindTabelle
	 *         ist; <TT>false</TT> sonst.
	 */
	public boolean isParent(TabellenModel kindTabelle);

	/**
	 * Generiert ein Create-Statement zur Tabelle.
	 * 
	 * @param hasDomains
	 *            Diese Flagge ist zu setzen, wenn das Create-Statement mit
	 *            Domains erzeugt werden soll.
	 * @param setReferences
	 *            Wird diese Flagge gesetzt, werden Referenzen als Foreignkeys
	 *            kodiert.
	 * @param noticeFkNotNull
	 *            Ist diese Flagge gesetzt, werden entsprechende
	 *            Not-Null-Statements bei der Foreignkeyanlage gesetzt.
	 * @return Das Create-Statement, das n&ouml;tig ist, um die Tabelle neu zu
	 *         erzeugen.
	 * 
	 */
	public String makeCreateStatement(boolean hasDomains, boolean setReferences, boolean noticeFkNotNull);

	/**
	 * Generiert ein Create-Statement zur Tabelle.
	 * 
	 * @param hasDomains
	 *            Diese Flagge ist zu setzen, wenn das Create-Statement mit
	 *            Domains erzeugt werden soll.
	 * @param setReferences
	 *            Wird diese Flagge gesetzt, werden Referenzen als Foreignkeys
	 *            kodiert.
	 * @param noticeFkNotNull
	 *            Ist diese Flagge gesetzt, werden entsprechende
	 *            Not-Null-Statements bei der Foreignkeyanlage gesetzt.
	 * @param nameQuotes
	 *            Eine Zeichenfolge, die vor und nach einem Tabellen- oder
	 *            Attributsnamen in das Create-Statement eingef&uuml;gt werden
	 *            soll. Wird der Parameter mit dem Wert <TT>null</TT>
	 *            &uuml;bergeben, so wird keine Zeichenfolge angegeben.
	 * @return Das Create-Statement, das n&ouml;tig ist, um die Tabelle neu zu
	 *         erzeugen.
	 * 
	 * @changed OLI 12.05.2009 - Hinzugef&uuml;gt als Erweiterung der Methode
	 *          <TT>makeCreateStatement(boolean, boolean, boolean)</TT>.
	 */
	public String makeCreateStatement(boolean hasDomains, boolean setReferences, boolean noticeFkNotNull,
			String nameQuotes);

	/**
	 * Generiert ein Insert-Statement zur Tabelle. Die Werteintr&auml;ge werden
	 * durch Platzhalter dargestellt.
	 * 
	 * @return Das Insert-Statement, das n&ouml;tig ist, um die Tabelle neu zu
	 *         erzeugen. Die Datenfelder werden durch den String
	 *         <TT>$<I>Spaltenname</I>$</TT> mit Platzhaltern belegt. Hierbei
	 *         werden die Spaltennamen in Gro&szlig;buchstaben umgesetzt.
	 * 
	 */
	public String makeInsertStatement();

	/**
	 * Generiert ein Insert-Statement zur Tabelle. Die Werteintr&auml;ge werden
	 * in einer HashMap &uuml;bergeben.
	 * 
	 * @param values
	 *            Die Werteintr&auml;ge zum Insertstatement als
	 *            Spaltennamen-Werte-Paare. Die Spaltennamen sind hierbei in
	 *            Gro&szlig;buchstaben anzugeben.
	 * @return Das Insert-Statement mit den angegebenen Werteeintr&auml;gen.
	 * 
	 */
	public String makeInsertStatement(HashMap<String, Object> values);

	/**
	 * Generiert ein Insert-Statement zur Tabelle. Die Werteintr&auml;ge werden
	 * durch numerische Platzhalter dargestellt.
	 * 
	 * @return Das Insert-Statement, das n&ouml;tig ist, um die Tabelle neu zu
	 *         erzeugen. Die Datenfelder werden durch den String
	 *         <TT>$<I>Nummer</I>$</TT> mit Platzhaltern belegt. Die Felder
	 *         werden beginnend mit 1 durchgez&auml;hlt. Ihre Reihenfolge
	 *         entspricht der aus dem Ergebnis der Methode
	 *         <TT>getFieldnames()</TT>.
	 * 
	 * @changed OLI 17.05.2009 - Hinzugef&uuml;gt
	 *          <P>
	 * 
	 */
	public String makeInsertStatementCounted();

	/**
	 * Diese Methode malt die von der Tabelle ausgehenden Relationen (also jene,
	 * bei denen ein Attribut der Tabelle eines einer anderen Tabelle
	 * referenziert) auf den &uuml;bergebenen Graphics-Context.
	 * 
	 * @param view
	 *            Der View, f&uuml;r den die Relation gezeichnet werden soll.
	 * @param g
	 *            Der Graphics-Context, auf den die Relationen ausgegeben werden
	 *            soll.
	 * @param pntm
	 *            Der PaintMode, unter dem die Relation gezeichnet werden soll.
	 * @return Eine Liste mit den ShapeContainern der gezeichneten Relationen.
	 * 
	 * @changed OLI 12.11.2008 - Erweiterung um den PaintMode.
	 *          <P>
	 *          OLI 16.12.2008 - Erweiterung um das ComponentDiagramm.
	 *          <P>
	 * 
	 */
	public Vector paintRelationen(CoordinateConverter cd, ViewModel view, Graphics g, PaintMode pntm);

	/**
	 * Diese Methode malt die Tabelle auf den &uuml;bergebenen Graphics-Context.
	 * 
	 * @param view
	 *            Der View, f&uuml;r den die Tabellen gezeichnet werden sollen.
	 * @param g
	 *            Der Graphics-Context, auf den die Tabelle ausgegeben werden
	 *            soll.
	 * @param pntm
	 *            Der PaintMode, unter dem die Tabelle gezeichnet werden soll.
	 * @return Ein ShapeContainer mit einem Rectangle mit den Koordinaten der
	 *         Tabelle und einer Referenz auf die Tabelle.
	 * 
	 * @changed OLI 12.11.2008 - Erweiterung um den PaintMode.
	 *          <P>
	 *          OLI 16.12.2008 - Erweiterung um das ComponentDiagramm.
	 *          <P>
	 * 
	 */
	public ShapeContainer paintTabelle(CoordinateConverter cd, ViewModel view, Graphics g, PaintMode pntm);

	/**
	 * L&ouml;scht das angegebene NReferenzModel aus der Liste der
	 * NReferenzModels des TabellenModel.
	 * 
	 * @param nrm
	 *            Das zu l&ouml;schende NReferenzModel.
	 */
	public void removeNReferenz(NReferenzModel nrm);

	/**
	 * L&ouml;scht die &uuml;bergebene Tabellenspalte der Tabelle. In diesem
	 * Zusammenhang sollte die Referenz innerhalb der Tabellenspalte
	 * gel&ouml;scht werden.
	 * 
	 * @param ts
	 *            Die zul&ouml;schende Tabellenspalte.
	 */
	@Deprecated
	public void removeTabellenspalte(TabellenspaltenModel ts);

	/**
	 * Setzt ein neues Code-Verzeichnis f&uuml;r die Tabelle.
	 * 
	 * @param s
	 *            Das neue Code-verzeichnis zur Tabelle.
	 */
	@Deprecated
	public void setCodeVerzeichnis(String s);

	/**
	 * Setzt die Diagrammreferenz der Tabelle auf das &uuml;bergebene Diagramm.
	 * 
	 * @param d
	 *            Das zusetzende Diagramm.
	 */
	public void setDiagramm(DiagrammModel d);

	/**
	 * Setzt die &uuml;bergebene Farbe als neue Hintergrundfarbe f&uuml;r die
	 * Tabelle ein.
	 * 
	 * @param farbe
	 *            Die neue Hintergrundfarbe f&uuml;r die Tabelle.
	 */
	public void setHintergrundfarbe(Color farbe);

	/**
	 * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r den
	 * Entwicklungsstatus der Tabelle.
	 * 
	 * @param b
	 *            Wird dieser Wert als <TT>true</TT> &uuml;bergeben, so wird die
	 *            Tabelle als noch in der Entwicklung befindlich gekennzeichnet.
	 *            / public void setInDevelopmentProcess(boolean b);
	 * 
	 *            /** Setzt die &uuml;bergebene Farbe als neue Schriftfarbe
	 *            f&uuml;r die Tabelle ein.
	 * 
	 * @param farbe
	 *            Die neue Schriftfarbe f&uuml;r die Tabelle.
	 */
	public void setSchriftfarbe(Color farbe);

	/** For sub list editor use. */
	abstract public Vector<NReferenzModel> getNReferencesAsRef();

	abstract public List<?> getEqualsMembersAsRef();

	abstract public List<?> getCompareMembersAsRef();

	abstract public List<?> getToStringMembersAsRef();

	abstract public List<?> getComboStringMembersAsRef();

	abstract public List<?> getHashCodeMembersAsRef();

	abstract public List<?> getSelectionViewOrderMembersAsRef();

}