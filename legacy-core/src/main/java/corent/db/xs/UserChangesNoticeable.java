/*
 * UserChangesNoticeable.java
 *
 * 23.01.2008
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.db.xs;


/**
 * Diese Interface mu&szlig; von Klassen implementiert werden, bei denen der Benutzer im Rahmen
 * von &Auml;nderungen am Datensatz &uuml;ber eine DBFactory festgehalten werden soll.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed 
 *     OLI 23.01.2008 - Hinzugef&uuml;gt.
 *     <P>OLI 11.02.2008 - Erweiterung um den Getter und den Setter f&uuml;r das Attribut 
 *             PCName.
 *     <P>OLI 18.09.2009 - Erweiterung um die Spezifikation der Methode 
 *             <TT>isSuppressUserNotification()</TT>.
 *     <P>
 *
 * @todo OLI - Erg&auml;nzung der Spezifikation der Methoden (OLI 18.09.2008).
 *
 */
 
public interface UserChangesNoticeable {
    
    public long getModificationUser();
    
    public String getPCName();

    /**
     * Liefert den Wert <TT>true</TT>, wenn das implementierende Objekt das Aktualisieren des 
     * ModificationUsers und des PCNames unterdr&uuml;cken soll.
     *
     * @return <TT>true</TT>, wenn die Aktualisierung der Felder ModificationUser und PCName
     *     unterdr&uuml;ckt werden soll.
     *
     * @changed
     *     OLI 18.09.2008 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public boolean isSuppressUserNotification();

    public void setModificationUser(long modificationuser);
    
    public void setPCName(String pcname);

}
