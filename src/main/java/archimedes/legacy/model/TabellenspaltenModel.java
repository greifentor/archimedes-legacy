/*
 * TabellenspaltenModel.java
 *
 * 20.03.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

import gengen.metadata.AttributeMetaData;
import corent.base.Attributed;
import corent.djinn.TabbedEditable;

/**
 * Dieses Interface definiert das notwendige Verhalten f&uuml;r eine
 * Tabellenspalte der Archimedes-Applikation.
 * 
 * @author ollie
 * 
 * @changed OLI 09.08.2008 - Erweiterung um die Methoden
 *          <TT>isTechnicalField()</TT> und <TT>setTechnicalField(boolean)</TT>,
 *          &uuml;ber die Tabellenspalten als technische Felder gekennzeichnet
 *          werden k&ouml;nnen.
 * @changed OLI 22.09.2008 - Erweiterung um die Methoden
 *          <TT>isListItemField()</TT> und <TT>setListItemField(boolean)</TT>.
 *          Hiermit k&ouml;nnen die Positionsfelder von flachgespeicherten
 *          Tabellen markiert werden.
 * @changed OLI 09.03.2009 - Spezifikation der Getter und Setter f&uuml;r die
 *          Attribute <TT>unique</TT> und <TT>parameter</TT>.
 * @changed OLI 14.06.2011 - Erweiterung der Spezifikation um einen Getter und
 *          einen Setter f&uuml;r das Attribute <TT>IndividualDefaultWert</TT>,
 *          das die Angabe eines individuellen Defaultwertes f&uuml;r die
 *          Tabellenspalte zul&auml;sst, der von dem der Domain abweichen kann.
 *          Definition einer Methode zum Ermitteln des aktuellen Defaultwertes.
 */

public interface TabellenspaltenModel extends Attributed, AttributeMetaData, ColumnModel, HistoryOwner, TabbedEditable {

	/** @return Der Wert des Attributs Primarykey. */
	@Deprecated
	public boolean isPrimarykey();

	/**
	 * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r das Attribut
	 * Primarykey.
	 * 
	 * @param value
	 *            Der neue Wert f&uuml;r das Attribut Primarykey.
	 */
	@Deprecated
	public void setPrimarykey(boolean value);

	/** @return Referenz auf die Tabelle zu der die Tabellenspalte geh&ouml;rt. */
	@Deprecated
	public TabellenModel getTabelle();

	/**
	 * Setzt die Zugeh&ouml;rigkeit der Tabelle auf das angegebene
	 * TabellenModel.
	 * 
	 * @param t
	 *            Die Tabelle, zu der die Tabellenspalte geh&ouml;ren soll.
	 */
	@Deprecated
	public void setTabelle(TabellenModel t);

	/**
	 * @return Ein String, der in der grafischen Anzeige der Tabellenspalte zu
	 *         deren Repr&auml;sentation gedruckt werden soll.
	 */
	public String toDiagrammString();

	/**
	 * @return <TT>true</TT>, wenn f&uuml;r die Tabellenspalte
	 *         Editordescriptoren generiert werden sollen.
	 */
	@Deprecated
	public boolean isEditormember();

	@Deprecated
	public void setEditormember(boolean editormember);

	/** @return Die Beschriftung den Editor-Label. */
	@Deprecated
	public String getLabelText();

	@Deprecated
	public void setLabelText(String labelText);

	/** @return Die Beschriftung den Editor-ToolTipText. */
	@Deprecated
	public String getToolTipText();

	@Deprecated
	public void setToolTipText(String toolTipText);

	/** @return Das Mnemonic zum Label. */
	@Deprecated
	public String getMnemonic();

	@Deprecated
	public void setMnemonic(String mnemonic);

	/**
	 * @return <TT>true</TT>, wenn die Tabellenspalte in der
	 *         Schreibbarkeitspr&uuml;fung ber&uuml;cksichtigt werden soll.
	 */
	@Deprecated
	public boolean isWriteablemember();

	@Deprecated
	public void setWriteablemember(boolean writeable);

	/**
	 * @return Die zu erwartender Menge der Auswahlm&ouml;glichkeiten im Falle
	 *         eines Referenzfeldes.
	 */
	@Deprecated
	public ReferenceWeight getReferenceWeight();

	@Deprecated
	public void setReferenceWeight(ReferenceWeight rw);

	/**
	 * @return Ein Ressourcenbezeichner (z. B. zum Generieren der
	 *         Anzeigekomponente f&uuml;r die Tabellenspalte).
	 */
	@Deprecated
	public String getRessourceIdentifier();

	/**
	 * Setzt einen neuen Ressourcenbezeichner zur Tabellenspalte.
	 * 
	 * @param ri
	 *            Ein neuer Ressourcenbezeichner (z. B. zum Generieren der
	 *            Anzeigekomponente f&uuml;r die Tabellenspalte).
	 */
	@Deprecated
	public void setRessourceIdentifier(String ri);

	/**
	 * Diese Methode liefert den Wert <TT>true</TT> zur&uuml;ck, wenn die
	 * Tabellenspalte in der Datenbank kodiert abgelegt werden soll.
	 * 
	 * @return <TT>true</TT>, wenn die Daten der Tabellenspalte in der Datenbank
	 *         kodiert abgelegt werden sollen.
	 */
	@Deprecated
	public boolean isKodiert();

	/**
	 * Mit Hilfe dieser Methode kann die Kodierung f&uuml;r die Daten zur
	 * Tabellenspalte ein- bzw. ausgeschaltet werden.
	 * <P>
	 * <B>Vorsicht:</B> Suchanfragen auf kodierte Spalten sind problematisch.
	 * 
	 * @param b
	 *            Diese Flagge ist auf <TT>true</TT> zu setzen, wenn die Daten
	 *            in der Tabellenspalte kodiert hinterlegt werden sollen. Um
	 *            diese Funktion auszuschalten mu&szlig; die Flagge auf den Wert
	 *            <TT>false</TT> gesetzt werden.
	 */
	@Deprecated
	public void setKodiert(boolean b);

	/**
	 * Diese Methode liefert den Wert <TT>true</TT> zur&uuml;ck, wenn die
	 * Tabellenspalte in der Editorsicht abgeblendet werden soll.
	 * 
	 * @return <TT>true</TT>, wenn die Daten der Tabellenspalte in der
	 *         Editorsicht abgeblendet werden sollen.
	 */
	@Deprecated
	public boolean isDisabled();

	/**
	 * Mit Hilfe dieser Methode kann die Abblendung f&uuml;r die Daten zur
	 * Tabellenspalte ein- bzw. ausgeschaltet werden.
	 * 
	 * @param b
	 *            Diese Flagge ist auf <TT>true</TT> zu setzen, wenn die
	 *            Tabellenspalte in der Editorsicht abgeblendet werden soll.
	 */
	@Deprecated
	public void setDisabled(boolean b);

	/**
	 * Diese Methode gibt an, ob die Tabellenspalte im Datenmodell mit einem
	 * Index versehen werden soll.
	 * 
	 * @return <TT>true</TT>, wenn die Spalte indiziert werden soll,
	 *         <TT>false</TT> sonst.
	 */
	@Deprecated
	public boolean isIndexed();

	/**
	 * Setzt bzw. l&ouml;scht den Index f&uuml;r die angegebene Spalte.
	 * 
	 * @param index
	 *            <TT>true</TT>, wenn die Spalte indiziert werden soll,
	 *            <TT>false</TT> sonst.
	 */
	@Deprecated
	public void setIndexed(boolean index);

	/**
	 * Liefert die maximale Gr&ouml;&szlig;e eines Eingabefeldes in Zeichen zur
	 * Tabellenspalte (falls eine solche Angabe nicht sinnlos ist). Bei
	 * Nichtangabe wird der Wert aus den Angaben zum Datentyp des Feldes
	 * gewonnen.
	 * 
	 * @return Die maximale Breite des Eingabefeldes zur Tabellenspalte in
	 *         Zeichen.
	 */
	@Deprecated
	public int getMaxCharacters();

	/**
	 * Setzt die maximale Gr&ouml;&szlig;e eines Eingabefeldes in Zeichen zur
	 * Tabellenspalte auf den &uuml;bergebenen Wert. &Uuml;bersteigt der Wert
	 * den durch die Angaben des Datentypen m&ouml;glichen, so wird er auf das
	 * durch den Datentypen definierte Maximum zur&uuml;ckgesetzt.
	 * 
	 * @param max
	 *            Die neue maximale Anzahl der Zeichen f&uuml;r das Datenfeld.
	 */
	@Deprecated
	public void setMaxCharacters(int max);

	/**
	 * Informiert dar&uuml;ber, ob es sich bei der Tabellenspalte um ein
	 * Attribut handelt, das in globalem Zusammenhang eventuell mit anderen
	 * Datenbest&auml;nden abgeglichen wird.
	 * 
	 * @return <TT>true</TT>, falls es sich bei der Tabellenspalte um ein
	 *         globales Datum handelt (zur eventuellen Verwendung in verteilten
	 *         Datenbest&auml;nden).
	 */
	@Deprecated
	public boolean isGlobal();

	/**
	 * &Uuml;ber diese Methode kann die Tabellenspalte als globales Attribut
	 * gekennzeichnet werden (bzw. eine Kennzeichnung aufgehoben werden).
	 * 
	 * @param g
	 *            Hier mu&szlig; der Wert <TT>true</TT> &uuml;bergeben werden,
	 *            wenn die Tabellenspalte als globales Attribut gekennzeichnet
	 *            werden soll. Um die Kennzeichnung zu l&ouml;schen, mu&szlig;
	 *            der Wert <TT>false</TT> &uuml;bergeben werden.
	 */
	@Deprecated
	public void setGlobal(boolean g);

	/**
	 * Informiert dar&uuml;ber, ob es sich bei der Tabellenspalte um einen
	 * Identifikator handelt, der &uuml;ber mehrere Datenbest&auml;nde als
	 * Schl&uuml;ssel herhalten kann.
	 * 
	 * @return <TT>true</TT>, falls es sich bei der Tabellenspalte um ein
	 *         globales Schl&uuml;sselattribut handelt.
	 */
	@Deprecated
	public boolean isGlobalId();

	/**
	 * &Uuml;ber diese Methode kann die Tabellenspalte als globales
	 * Schl&uuml;sselattribut gekennzeichnet werden (bzw. eine Kennzeichnung
	 * aufgehoben werden).
	 * 
	 * @param g
	 *            Hier mu&szlig; der Wert <TT>true</TT> &uuml;bergeben werden,
	 *            wenn die Tabellenspalte als globales Schl&uuml;sselattribut
	 *            gekennzeichnet werden soll. Um die Kennzeichnung zu
	 *            l&ouml;schen, mu&szlig; der Wert <TT>false</TT> &uuml;bergeben
	 *            werden.
	 */
	@Deprecated
	public void setGlobalId(boolean g);

	/**
	 * Informiert dar&uuml;ber, ob die Tabellenspalte innerhalb eines Stapels
	 * ge&auml;ndert werden k&ouml;nnen soll.
	 * 
	 * @return <TT>true</TT>, wenn die Tabellenspalte innerhalb eines Stapels
	 *         ge&auml;ndert werden k&ouml;nnen soll, bzw. <TT>false</TT>, wenn
	 *         das nicht der Fall ist.
	 */
	@Deprecated
	public boolean isAlterInBatch();

	/**
	 * Mit Hilfe dieser Funktion kann eine Tabellenspalte als innerhalb eines
	 * Stapels zu &auml;ndern gekennzeichnet werden, bzw. diese Kennzeichnung
	 * entfernt werden.
	 * 
	 * @param aib
	 *            Diese Flagge mu&szlig; gesetzt werden, wenn die Tabellenspalte
	 *            innerhalb eines Stapels ge&auml;ndert werden k&ouml;nnen soll,
	 *            bzw. gel&ouml;scht werden, wenn dies nicht der Fall sein soll.
	 */
	@Deprecated
	public void setAlterInBatch(boolean aib);

	/**
	 * Liefert eine Information dar&uuml;ber, ob die Tabellenspalte in einer in
	 * der Menge der referenzierbaren Spalten erscheinen soll. Dies hat nur eine
	 * Auswirkung, wenn die Tabellenspalte kein Primary-Key-Member ist.
	 * 
	 * @return <TT>true</TT>, wenn die Tabellenspalte referenzierbar sein und in
	 *         entsprechenden Auswahldialogen angezeigt werden soll.
	 */
	@Deprecated
	public boolean isCanBeReferenced();

	/**
	 * Liefert eine Information dar&uuml;ber, ob eine von der Tabellenspalte
	 * ausgehende Referenz angezeigt werden soll, oder nicht. Dieses Attribut
	 * ist st&auml;rker als die Konfiguration von View oder Diagramm.
	 * 
	 * @return <TT>true</TT>, wenn eine eventuell von der Tabellenspalte
	 *         ausgehende Referenz verborgen werden soll.
	 */
	@Deprecated
	public boolean isHideReference();

	/**
	 * Liefert einen freidefinierbaren Parameterstring zur Tabellenspalte.
	 * 
	 * @return Der freidefinierbare Parameterstring zur Tabellenspalte.
	 * 
	 * @changed OLI 09.03.2009 - Hinzugef&uuml;gt.
	 */
	@Deprecated
	public String getParameter();

	/**
	 * Setzt den &uuml;bergebenen String als neuen Parameterstring f&uuml;r die
	 * Tabellenspalte ein.
	 * 
	 * @param s
	 *            Ein neuer Parameterstring f&uuml;r die Tabellenspalte.
	 * 
	 * @changed OLI 09.03.2009 - Hinzugef&uuml;gt.
	 */
	@Deprecated
	public void setParameter(String s);

}