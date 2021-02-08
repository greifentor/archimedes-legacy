/*
 * SQLScriptEventType.java
 *
 * 22.03.2009
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.script.sql;


/**
 * Ein Enum zur Spezifizierung der Ursachen f&uuml;r SQLScriptEvents.
 *
 * @author
 *     ollie
 *     <P>
 *
 * @changed
 *     OLI 22.03.2009 - Hinzugef&uuml;gt
 *     <P>OLI 26.03.2009 - Erweiterung um den Bezeichner <TT>BEFOREDBVERSION</TT>.
 *     <P>
 *
 */

public enum SQLScriptEventType {

    /** Event zur Neuanlage einer Tabellenspalte im Datenschema. */
    ADDCOLUMN,
    /** Event zur Neuanlage eines Index im Datenschema. */
    ADDINDEX,
    /** Event zur Neuanlage eines Prim&auml;rschl&uuml;ssels im Datenschema. */
    ADDPRIMARYKEY,
    /** Event zur &Auml;nderung einer Tabellenspalte im Datenschema. */
    ALTERCOLUMN,
    /** 
     * Event unmittelbar, bevor das Insertstatement zur Datenmodell-Version geschrieben wird.
     * Dieses Event kommt nicht zwingend bei jedem Datenmodell vor.
     */
    BEFOREDBVERSION,
    /** Event, das beim Beenden des Scriptbaus geworfen wird. */
    COMPLETEBUILDING,
    /** Event zur Neuanlage einer Tabelle im Datenschema. */
    CREATETABLE,
    /** Event zum L&ouml;schen einer Tabellespalte aus dem Datenschema. */
    DROPCOLUMN,
    /** Event zum L&ouml;schen eines Index aus dem Datenschema. */
    DROPINDEX,
    /** Event zum L&ouml;schen eines Prim&auml;rschl&uuml;ssels aus dem Datenschema. */
    DROPPRIMARYKEY,
    /** Event zum L&ouml;schen einer Tabelle aus dem Datenschema. */
    DROPTABLE,
    /** Event, das bei Start des Scriptbaus geworfen wird. */
    STARTBUILDING;

}
