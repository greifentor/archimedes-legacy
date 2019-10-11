/**
 * DataColumnModel.java
 *
 * 26.04.2008
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

import archimedes.model.DomainModel;
import archimedes.model.PanelModel;
import archimedes.model.ReferenceWeight;
import archimedes.model.RelationModel;

/**
 * Dieses Interface definiert die Funktionen eines DataColumnModels im Rahmen
 * des Archimedesumfeldes.
 * 
 * <P>
 * Das Interface ist ein Produkt der Sprachumstellung der
 * Archimedes-Klassen-Bibliothek. Es l&ouml;st das Interface
 * <TT>TabellenspaltenModel</TT> ab.
 * 
 * @author ollie
 *         <P>
 * 
 * @changed OLI 26.04.2008 - Hinzugef&uuml;gt.
 * 
 */

public interface DataColumnModel extends TabellenspaltenModel {

	/**
	 * Liefert die Domain der Tabellenspalte.
	 * 
	 * @return Die Domain der Tabellenspalte.
	 */
	public DomainModel getDomain();

	/**
	 * Liefert den vollst&auml;ndigen Namen der Tabellenspalte (mit
	 * vorangestelltem und durch einen Punkt abgesetztem Tabellenamen), z. B.
	 * "Tabelle.Spalte".
	 * 
	 * @return Der vollst&auml;ndigen Namen der Tabellenspalte (mit
	 *         vorangestelltem und durch einen Punkt abgesetztem Tabellenamen).
	 */
	public String getFullName();

	/**
	 * Liefert den Namen der Tabellenspalte.
	 * 
	 * @return Der Name der Tabellenspalte.
	 */
	public String getName();

	/**
	 * Liefert eine Referenz auf das DataTableModel, zu dem die Tabellenspalte
	 * geh&ouml;rt.
	 * 
	 * @return Referenz auf die Tabelle zu der die Tabellenspalte geh&ouml;rt.
	 */
	public DataTableModel getTable();

	/**
	 * Pr&uuml;ft, ob die Tabellenspalte bei Prozessen bez&uuml;glich
	 * Codegeneration oder EditorDescriptor-Erstellung ber&uuml;cksichtigt
	 * werden soll.
	 * 
	 * <P>
	 * EditorDescriptoren werden im Zusammenspiel mit der Corent-Editorlogik
	 * eingesetzt. Wird das Archimedesmodell ohne diese Logik eingesetzt, kann
	 * das Attribut unbeachtet bleiben.
	 * 
	 * @return <TT>true</TT>, wenn die Tabellenspalte bei Codegenerierung und
	 *         EditorDescriptoren-Erstellung generiert werden sollen,
	 *         <TT>false</TT> sonst.
	 */
	public boolean isEditorMember();

	/**
	 * Liefert eine Information dar&uuml;ber, ob eine von der Tabellenspalte
	 * ausgehende Referenz angezeigt werden soll, oder nicht. Dieses Attribut
	 * ist st&auml;rker als die Konfiguration von View oder Diagramm.
	 * 
	 * @return <TT>true</TT>, wenn eine eventuell von der Tabellenspalte
	 *         ausgehende Referenz verborgen werden soll.
	 */
	public boolean isHideReference();

	/**
	 * Pr&uuml;ft, ob die aus dem DataDolumnModel resultierende Tabellenspalte
	 * NULL-Werte zulassen soll.
	 * 
	 * @return <TT>true</TT>, wenn die mit dem DataColumnModel assoziierte
	 *         Tabellenspalte NULL-Werte zulassen soll, <TT>false</TT> sonst.
	 */
	public boolean isNotNull();

	/**
	 * Pr&uuml;ft, ob es sich bei der mit dem DataColumnModel assoziierten
	 * Tabellenspalte um ein Prim&auml;rschl&uuml;sselmitglied handelt.
	 * 
	 * @return <TT>true</TT>, wenn die mit dem DataColumnModel assoziierte
	 *         Tabellenspalte ein Prim&auml;rschl&uuml;sselmitglied ist,
	 *         <TT>false</TT> sonst.
	 */
	public boolean isPrimaryKey();

	/**
	 * Setzt die &uuml;bergebene Domain als neue Domain f&uuml;r die
	 * Tabellenspalte ein.
	 * 
	 * @param dom
	 *            Die neue Domain zur Tabellenspalte.
	 */
	public void setDomain(DomainModel dom);

	/**
	 * Setzt den Status, den die Tabellenspalte bei der Codegenerierung und
	 * EditorDescriptoren-Erstellung einnehmen soll.
	 * 
	 * <P>
	 * EditorDescriptoren werden im Zusammenspiel mit der Corent-Editorlogik
	 * eingesetzt. Wird das Archimedesmodell ohne diese Logik eingesetzt, kann
	 * das Attribut unbeachtet bleiben.
	 * 
	 * @param editorMember
	 *            Dieser Parameter mu&szlig; mit dem Wert <TT>true</TT>
	 *            &uuml;bergeben werden, wenn die Tabellenspalte Codegenerierung
	 *            und EditorDescriptoren-Erstellung Ber&uuml;cksichtigung finden
	 *            soll.
	 */
	public void setEditorMember(boolean editorMember);

	/**
	 * Setzt bzw. l&ouml;scht die Flagge, die Auskunft &uuml;ber den Status
	 * Referenzanzeige zur Tabellenspalte gibt.
	 * 
	 * @param b
	 *            Diese Flagge mu&szlig; gesetzt werden, wenn eventuell von der
	 *            Tabellenspalte ausgehende Referenzen verborgen werden sollen.
	 */
	public void setHideReference(boolean b);

	/**
	 * Setzt den &uuml;bergebenen Namen als neuen Namen f&uuml;r die
	 * Tabellenspalte ein.
	 * 
	 * @param name
	 *            Der neue Name der Tabellenspalte.
	 */
	public void setName(String name);

	/**
	 * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r das Attribut
	 * NotNull.
	 * 
	 * @param notnull
	 *            Wird hier der Wert <TT>true</TT> &uuml;bergeben, so
	 *            l&auml;&szlig;t die mit DataColumnModel assoziierte
	 *            Tabellenspalte keine NULL-Werte zu.
	 */
	public void setNotNull(boolean notnull);

	/**
	 * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r das Attribut
	 * PrimaryKey.
	 * 
	 * @param primarykey
	 *            Wird hier der Wert <TT>true</TT> &uuml;bergeben, so wird die
	 *            mit DataColumnModel assoziierte Tabellenspalte als
	 *            Prim&auml;rschl&uuml;sselmitglied behandelt.
	 */
	public void setPrimaryKey(boolean primarykey);

	/**
	 * Setzt die Zugeh&ouml;rigkeit der Tabellenspalte auf das angegebene
	 * DataTableModel.
	 * 
	 * @param dtm
	 *            Das DataTableModel, zu dem die Tabellenspalte geh&ouml;ren
	 *            soll.
	 */
	public void setTable(DataTableModel dtm);

	/**
	 * Liefert einen String, der in der Diagrammansicht genutzt werden kann, um
	 * den Namen der Tabelle anzugeben. Im Gegensatz zu <TT>getName</TT>-Methode
	 * kann dieser String zus&auml;tzliche Symbole enthalten und bestimmte
	 * Sachverhalte zur Tabelle zu markieren (z. B. ein Sternchen um ererbte
	 * Tabellen zu kennzeichnen).
	 * 
	 * @return Ein String, der in der grafischen Anzeige der Tabellenspalte zu
	 *         deren Repr&auml;sentation gedruckt werden soll.
	 */
	public String toDiagrammString();

	/**
	 * @return Ein RelationModel, wenn die Tabellenspalte ein ForeignKey ist und
	 *         eine andere Tabellenspalte referenziert, bzw. <TT>null</TT>, wenn
	 *         dies nicht der Fall ist.
	 */
	public RelationModel getRelation();

	/**
	 * Setzt die &uuml;bergebene Relation als neue Relation f&uuml;r die
	 * Tabellenspalte ein.
	 * 
	 * @param r
	 *            Die neueinzusetzende Relation.
	 */
	public void setRelation(RelationModel r);

	/** @return Die Beschriftung den Editor-Label. */
	public String getLabelText();

	public void setLabelText(String labelText);

	/** @return Die Beschriftung den Editor-ToolTipText. */
	public String getToolTipText();

	public void setToolTipText(String toolTipText);

	/** @return Das Mnemonic zum Label. */
	public String getMnemonic();

	public void setMnemonic(String mnemonic);

	/**
	 * @return <TT>true</TT>, wenn die Tabellenspalte in der
	 *         Schreibbarkeitspr&uuml;fung ber&uuml;cksichtigt werden soll.
	 */
	public boolean isWriteablemember();

	public void setWriteablemember(boolean writeable);

	/** @return Die Sortierung innerhalb eines Editorpanels. */
	public int getEditorPosition();

	public void setEditorPosition(int pos);

	/**
	 * @return Die zu erwartender Menge der Auswahlm&ouml;glichkeiten im Falle
	 *         eines Referenzfeldes.
	 */
	public ReferenceWeight getReferenceWeight();

	public void setReferenceWeight(ReferenceWeight rw);

	/** @return Das Panel, auf dem die Tabellenspalte abgebildet werden soll. */
	public PanelModel getPanel();

	/**
	 * Setzt das &uuml;bergebene Panel als neues Panel ein, auf dem die
	 * Tabellenspalte abgebildet werden soll.
	 * 
	 * @param p
	 *            Das neue Panel.
	 */
	public void setPanel(PanelModel p);

	/**
	 * @return Ein Ressourcenbezeichner (z. B. zum Generieren der
	 *         Anzeigekomponente f&uuml;r die Tabellenspalte).
	 */
	public String getRessourceIdentifier();

	/**
	 * Setzt einen neuen Ressourcenbezeichner zur Tabellenspalte.
	 * 
	 * @param ri
	 *            Ein neuer Ressourcenbezeichner (z. B. zum Generieren der
	 *            Anzeigekomponente f&uuml;r die Tabellenspalte).
	 */
	public void setRessourceIdentifier(String ri);

	/**
	 * Diese Methode liefert den Wert <TT>true</TT> zur&uuml;ck, wenn die
	 * Tabellenspalte in der Datenbank kodiert abgelegt werden soll.
	 * 
	 * @return <TT>true</TT>, wenn die Daten der Tabellenspalte in der Datenbank
	 *         kodiert abgelegt werden sollen.
	 */
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
	public void setKodiert(boolean b);

	/**
	 * Diese Methode liefert den Wert <TT>true</TT> zur&uuml;ck, wenn die
	 * Tabellenspalte in der Editorsicht abgeblendet werden soll.
	 * 
	 * @return <TT>true</TT>, wenn die Daten der Tabellenspalte in der
	 *         Editorsicht abgeblendet werden sollen.
	 */
	public boolean isDisabled();

	/**
	 * Mit Hilfe dieser Methode kann die Abblendung f&uuml;r die Daten zur
	 * Tabellenspalte ein- bzw. ausgeschaltet werden.
	 * 
	 * @param b
	 *            Diese Flagge ist auf <TT>true</TT> zu setzen, wenn die
	 *            Tabellenspalte in der Editorsicht abgeblendet werden soll.
	 */
	public void setDisabled(boolean b);

	/**
	 * Diese Methode gibt an, ob die Tabellenspalte im Datenmodell mit einem
	 * Index versehen werden soll.
	 * 
	 * @return <TT>true</TT>, wenn die Spalte indiziert werden soll,
	 *         <TT>false</TT> sonst.
	 */
	public boolean isIndexed();

	/**
	 * Setzt bzw. l&ouml;scht den Index f&uuml;r die angegebene Spalte.
	 * 
	 * @param index
	 *            <TT>true</TT>, wenn die Spalte indiziert werden soll,
	 *            <TT>false</TT> sonst.
	 */
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
	public void setGlobal(boolean g);

	/**
	 * Informiert dar&uuml;ber, ob es sich bei der Tabellenspalte um einen
	 * Identifikator handelt, der &uuml;ber mehrere Datenbest&auml;nde als
	 * Schl&uuml;ssel herhalten kann.
	 * 
	 * @return <TT>true</TT>, falls es sich bei der Tabellenspalte um ein
	 *         globales Schl&uuml;sselattribut handelt.
	 */
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
	public void setGlobalId(boolean g);

	/**
	 * Informiert dar&uuml;ber, ob die Tabellenspalte innerhalb eines Stapels
	 * ge&auml;ndert werden k&ouml;nnen soll.
	 * 
	 * @return <TT>true</TT>, wenn die Tabellenspalte innerhalb eines Stapels
	 *         ge&auml;ndert werden k&ouml;nnen soll, bzw. <TT>false</TT>, wenn
	 *         das nicht der Fall ist.
	 */
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
	public void setAlterInBatch(boolean aib);

	/**
	 * Liefert eine Information dar&uuml;ber, ob die Spalte den Zeitpunkt der
	 * letzten &Auml;nderung des Datensatzes festh&auml;lt.
	 * 
	 * @return <TT>true</TT>, falls die Spalte zur Speicherung eines Zeitpunktes
	 *         der letzten &Auml;nderung dient, <TT>false</TT> sonst.
	 */
	public boolean isLastModificationField();

	/**
	 * Setzt, bzw. l&ouml;scht die Flagge, die Auskunft dar&uuml;ber gibt, ob
	 * die Spalte zur Speicherung der letzten Modifikation des Datensatzes
	 * dient.
	 * 
	 * @param b
	 *            Wird hier der Wert <TT>true</TT> &uuml;bergeben, so wird die
	 *            Spalte als zur Aufnahme einer Information &uuml;ber den
	 *            Zeitpunkt der letzten &Auml;nderung gekennzeichnet.
	 */
	public void setLastModificationField(boolean b);

	/**
	 * Liefert eine Information dar&uuml;ber, ob die Spalte &uuml;ber den
	 * Gel&ouml;scht-Status des Datensatzes informiert.
	 * 
	 * @return <TT>true</TT>, falls die Spalte zur Speicherung des
	 *         Gel&ouml;schtstatus dient, <TT>false</TT> sonst.
	 */
	public boolean isRemovedStateField();

	/**
	 * Setzt, bzw. l&ouml;scht die Flagge, die Auskunft dar&uuml;ber gibt, ob
	 * die Spalte &uuml;ber den Gel&ouml;scht-Status des Datensatzes informiert.
	 * 
	 * @param b
	 *            Wird hier der Wert <TT>true</TT> &uuml;bergeben, so wird die
	 *            Spalte als zur Aufnahme des Gel&ouml;schtstatus des
	 *            Datensatzes gekennzeichnet.
	 */
	public void setRemovedStateField(boolean b);

	/**
	 * Liefert eine Information dar&uuml;ber, ob die Tabellenspalte in einer
	 * tabellen&uuml;greifenden Indexsuche ber&uuml;cksichtigt werden soll.
	 * 
	 * @return <TT>true</TT>, wenn die Tabellenspalte bei einer
	 *         tabellen&uuml;greifenden Indexsuche ber&uuml;cksichtigt werden
	 *         soll.
	 */
	public boolean isIndexSearchMember();

	/**
	 * Setzt bzw. l&ouml;scht die Flagge, die Auskunft &uuml;ber den Status der
	 * Tabellenspalte bez&uuml;glich Indexsuche gibt.
	 * 
	 * @param b
	 *            Diese Flagge mu&szlig; gesetzt werden, wenn die Tabellenspalte
	 *            von einem tabellen&uuml;greifenden Indexsuchalgorithmus
	 *            ger&uuml;cksichtigt werden soll.
	 */
	public void setIndexSearchMember(boolean b);

	/**
	 * Liefert eine Information dar&uuml;ber, ob die Tabellenspalte in einer in
	 * der Menge der referenzierbaren Spalten erscheinen soll. Dies hat nur eine
	 * Auswirkung, wenn die Tabellenspalte kein Primary-Key-Member ist.
	 * 
	 * @return <TT>true</TT>, wenn die Tabellenspalte referenzierbar sein und in
	 *         entsprechenden Auswahldialogen angezeigt werden soll.
	 */
	public boolean isCanBeReferenced();

	/**
	 * Setzt bzw. l&ouml;scht die Flagge, die Auskunft &uuml;ber den Status der
	 * Tabellenspalte bez&uuml;glich der Referenzierbarkeit gibt.
	 * 
	 * @param b
	 *            Diese Flagge mu&szlig; gesetzt werden, wenn die Tabellenspalte
	 *            referenzierbar sein soll.
	 */
	public void setCanBeReferenced(boolean b);

}
