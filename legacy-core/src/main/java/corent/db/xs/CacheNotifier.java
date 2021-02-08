/*
 * CacheNotifier.java
 *
 * 18.05.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


/**
 * Diese Erweiterung des Interfaces <TT>UserChangesNoticeable</TT> erm&ouml;glicht das 
 * automatische Aktualisieren einer speziellen Datenbanktabelle zum Festhalten von 
 * &Auml;nderungen an gecachten Datens&auml;tzen.
 *
 * <P>Die Tabelle, die zu diesem Zweck in der Datenbank vorhanden sein mu&szlig;, hat folgende
 * Struktur:
 * <PRE>
 *create table CacheNotification(
 *    Table varchar(255),
 *    Id varchar(255),
 *    Changer varchar(255),
 *    ChangeDate numeric(14,0),
 *    Mode varchar(5), -- 'U' updated, 'R' removed.
 *    primary key(Table, Id)
 *);
 * </PRE>
 * <P>Die Namen f&uuml;r die Tabelle, sowie die Namen ihrer Spalten, sind &uuml;ber die 
 * folgenden Properties konfigurierbar:
 * 
 * <TABLE BORDER=1>
 *     <TR VALIGN=TOP>
 *         <TH>Property</TH>
 *         <TH>Default</TH>
 *         <TH>Typ</TH>
 *         <TH>Beschreibung</TH>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.db.xs.DefaultDBFactoryController.<BR>changenotifier.column.ChangeDate</TD>
 *         <TD>ChangeDate</TD>
 *         <TD>Long</TD>
 *         <TD>Die Uhrzeit der &Auml;nderung im PTimestamp-Format.</TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.db.xs.DefaultDBFactoryController.<BR>changenotifier.column.Changer</TD>
 *         <TD>Changer</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Eine Kombination aus Benutzer-Kennung (oder Id), einem Strich ("-") und dem Namen
 *             des Rechners, auf dem die &Auml;nderung durchgef&uuml;hrt worden ist.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.db.xs.DefaultDBFactoryController.<BR>changenotifier.column.Id</TD>
 *         <TD>Id</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Der Name der Tabellenspalte, in der der Name die Id des ge&auml;nderten 
 *             Datensatzes hinterlegt wird (oder '*', falls alle oder sehr viele Datens&auml;tze
 *             ge&auml;ndert worden sind).
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.db.xs.DefaultDBFactoryController.<BR>changenotifier.column.Table</TD>
 *         <TD>Table</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Der Name der Tabellenspalte, in der der Name der Tabelle hinterlegt wird, deren
 *             Datensatz ge&auml;ndert worden ist.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.db.xs.DefaultDBFactoryController.<BR>changenotifier.tablename</TD>
 *         <TD>ChangeNotification</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Der Name der Tabelle, in der die &Auml;nderungsinformationen gespeichert werden
 *             sollen. 
 *         </TD>
 *     </TR>
 * </TABLE>
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 18.05.2008 - Hinzugef&uuml;gt.
 *
 */
 
public interface CacheNotifier extends UserChangesNoticeable {

    /**
     * Liefert einen Id-Wert, der das implementierende Objekt eindeutig identifiziert. Im 
     * Normalfall sollte hier der vollst&auml;ndige Datenbankschl&uuml;ssel zur&uuml;ckgegeben
     * werden.
     *
     * @return Ein Id-Wert, der das implementierende Objekt eindeutig identifiziert.
     */
    public String getIdValue();
    
    /**
     * Liefert den Tabellennamen, in dem die Objekte der implementierenden Klasse gespeichert
     * werden.
     *
     * @return Der Name der Datenbanktabelle, in der die Objekte der implementierenden Klasse
     *     gespeichert werden.
     */
    public String getTablename();
    
}
