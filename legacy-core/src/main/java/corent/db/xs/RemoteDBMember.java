/*
 * RemoteDBMembrt.java
 *
 * 29.09.2005
 *  
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


/**
 * Mit Hilfe dieses Interfaces, kann das implementierende Objekt auf bestimmte Ereignisse der
 * RemoteDBFactoryController reagieren.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface RemoteDBMember {
    
    /** 
     * Diese Methode wird aufgerufen, nachdem das Objekt durch einen RemoteDBFactoryController 
     * erzeugt worden ist. Diese Methode wird client-seitig ausgef&uuml;hrt.
     */
    public void objectCreated();
    
    /** 
     * Diese Methode wird aufgerufen, nachdem das Objekt durch einen RemoteDBFactoryController 
     * dupliziert worden ist. Diese Methode wird client-seitig ausgef&uuml;hrt.
     */
    public void objectDuplicated();
    
    /** 
     * Diese Methode wird aufgerufen, nachdem das Objekt durch einen RemoteDBFactoryController 
     * generiert worden ist. Diese Methode wird client-seitig ausgef&uuml;hrt.
     */
    public void objectGenerated();
    
    /** 
     * Diese Methode wird aufgerufen, nachdem das Objekt durch einen RemoteDBFactoryController 
     * eingelesen worden ist. Diese Methode wird client-seitig ausgef&uuml;hrt.
     */
    public void objectRed();
    
    /** 
     * Diese Methode wird aufgerufen, bevor das Objekt durch einen RemoteDBFactoryController 
     * dupliziert wird. Diese Methode wird server-seitig ausgef&uuml;hrt.
     */
    public void objectBeforeDuplicate();
    
    /** 
     * Diese Methode wird aufgerufen, bevor das Objekt durch einen RemoteDBFactoryController 
     * gel&ouml;scht wird. Diese Methode wird server-seitig ausgef&uuml;hrt.
     */
    public void objectBeforeRemove();
    
    /** 
     * Diese Methode wird aufgerufen, bevor das Objekt durch einen RemoteDBFactoryController 
     * geschrieben wird. Diese Methode wird server-seitig ausgef&uuml;hrt.
     */
    public void objectBeforeWrite();
    
    /** 
     * Diese Methode wird aufgerufen, bevor f&uuml;r das Objekt durch einen 
     * RemoteDBFactoryController eine Kontrolle auf Einzigartigkeit durchgef&uuml;hrt wird. 
     * Diese Methode wird server-seitig ausgef&uuml;hrt.
     */
    public void objectBeforeIsUnique();
    
}
