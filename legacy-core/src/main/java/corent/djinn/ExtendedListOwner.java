/*
 * ExtendedListOwner.java
 *
 * 20.05.2007
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/** 
 * Dieses Interface erweitert das ListOwner-Interface um zus&auml;tzliche Methoden, &uuml;ber 
 * die die Buttons der Listensichten f&uuml;r die angezeigten Listen gesteuert werden 
 * k&ouml;nnen.
 *
 * @author O.Lieshoff
 *
 */
 
public interface ExtendedListOwner extends ListOwner {
    
    /**
     * Diese Methode mu&szlig; den Wert <TT>true</TT> zur&uuml;ckliefern, wenn der 
     * Neuanlage-Button einer Listensicht sichtbar sein soll, bzw. <TT>false</TT>, wenn der 
     * Button nicht nutzbar sein soll.
     *
     * @param no Die Nummer der Liste im Kontext des ListOwners. 
     * @return <TT>true</TT>, wenn der Neuanlage-Button der Listensicht aktiviert sein soll,
     *     <TT>false</TT> sonst.
     */
    public boolean isCreateButtonEnabled(int no);
    
    /**
     * Diese Methode mu&szlig; den Wert <TT>true</TT> zur&uuml;ckliefern, wenn der 
     * L&ouml;schen-Button einer Listensicht sichtbar sein soll, bzw. <TT>false</TT>, wenn der 
     * Button nicht nutzbar sein soll.
     *
     * @param no Die Nummer der Liste im Kontext des ListOwners. 
     * @return <TT>true</TT>, wenn der L&ouml;schen-Button der Listensicht aktiviert sein soll,
     *     <TT>false</TT> sonst.
     */
    public boolean isDeleteButtonEnabled(int no);
    
    /**
     * Diese Methode mu&szlig; den Wert <TT>true</TT> zur&uuml;ckliefern, wenn der 
     * Bearbeiten-Button einer Listensicht sichtbar sein soll, bzw. <TT>false</TT>, wenn der 
     * Button nicht nutzbar sein soll.
     *
     * @param no Die Nummer der Liste im Kontext des ListOwners. 
     * @return <TT>true</TT>, wenn der Bearbeiten-Button der Listensicht aktiviert sein soll,
     *     <TT>false</TT> sonst.
     */
    public boolean isEditButtonEnabled(int no);
    
    /**
     * Diese Methode mu&szlig; den Wert <TT>true</TT> zur&uuml;ckliefern, wenn der 
     * Einf&uuml;gen-Button einer Listensicht sichtbar sein soll, bzw. <TT>false</TT>, wenn der 
     * Button nicht nutzbar sein soll.
     *
     * @param no Die Nummer der Liste im Kontext des ListOwners. 
     * @return <TT>true</TT>, wenn der Einf&uuml;gen-Button der Listensicht aktiviert sein soll,
     *     <TT>false</TT> sonst.
     */
    public boolean isInsertButtonEnabled(int no);
    
}
