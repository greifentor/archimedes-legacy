/*
 * SortedListSubEditorMaster.java
 *
 * 19.02.2007
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.app;


import corent.djinn.*;

import java.util.*;


/**
 * Die Implementierung dieses Interfaces gibt einem Objekt einen gewissen Einflu&szlig; auf 
 * seinen Umgang mit im zueigenen Listen. Prinzipiell ist die Implementierung nur mit der 
 * gleichzeitigen Verwendung des ListOwner-Interfaces sinnvoll.
 * <P><I>Hinweis:</I> Die Methoden <TT>afterCreateObject</TT>, <TT>beforeChangeObject</TT> und
 * <TT>beforeInsertObject</TT> sind so zu implementieren, da&szlig; sie den Wert <TT>null</TT>
 * zur&uuml;ckliefern, wenn bei ihrer Bearbeitung entschieden wird, da&szlig; der jeweilige
 * Vorgang abzubrechen ist. Auf diese Weise kann dieser Umstand entsprechend von den 
 * SortedListSubEditor-Implementierungen behandelt werden.
 * <P><I>Noch ein Hinweis:</I> Auch, wenn es auf den ersten Blick eventuell merkw&uuml;rdig 
 * erscheint: das Interface wird durch den Besitzer der Liste implementiert, die in dem 
 * SortedListSubEditor angezeigt bzw. bearbeitet wird. Stelle man sich einen Personendatensatz
 * vor, der eine Liste von Adressen hat, so w&uuml;rde der Personendatensatz das Interface 
 * implementieren. 
 *
 * @author ollie 
 *
 */
 
public interface SortedListSubEditorMaster {

    /**
     * Diese Methode wird durch den SortedListSubEditor aufgerufen, nachdem ein neues 
     * Listenelement generiert worden ist, aber bevor es zum Bearbeiten angezeigt wird.
     *
     * @param id Eine Kennnummer, mit der die Listen des implementierenden Objektes auseinander
     *     gehalten werden k&ouml;nnen, falls es mehrere gibt.
     * @param o Das neu generierte Objekt, welches, nach Bearbeitung, an die Liste 
     *     angeh&auml;ngt werden soll.
     * @return Das eventuell durch die Methode ge&auml;nderte Objekt, das anschlie&szlig;end
     *     bearbeitet werden soll, oder <TT>null</TT>, wenn der Neuanlage-Vorgang abgebrochen 
     *     werden soll. F&uuml;r eine Implementierung, in der nichts passieren soll, bietet sich
     *     ein Durchreichen des Parameters <TT>o</TT> an.
     */
    public Editable afterCreateObject(int id, Editable o);
    
    /**
     * Diese Methode wird durch den SortedListSubEditor aufgerufen, nachdem ein Objekt aus der
     * Liste zur Bearbeitung ausgew&auml;hlt worden ist, aber bevor es tats&auml;chlich 
     * ge&auml;ndert wird.
     *
     * @param id Eine Kennnummer, mit der die Listen des implementierenden Objektes auseinander
     *     gehalten werden k&ouml;nnen, falls es mehrere gibt.
     * @param o Das Objekt, welches bearbeitet werden soll.
     * @return Das eventuell durch die Methode ge&auml;nderte Objekt, das anschlie&szlig;end
     *     bearbeitet werden soll, oder <TT>null</TT>, wenn der Bearbeitungs-Vorgang abgebrochen 
     *     werden soll. F&uuml;r eine Implementierung, in der nichts passieren soll, bietet sich
     *     ein Durchreichen des Parameters <TT>o</TT> an.
     */
    public Editable beforeChangeObject(int id, Editable o);
    
    /**
     * Diese Methode wird durch den SortedListSubEditor aufgerufen, nachdem ein neues 
     * Listenelement aus einer Auswahl selektiert worden ist, aber bevor es, nach eventueller
     * Anzeige, in die Liste eingef&uuml;gt wird.
     *
     * @param id Eine Kennnummer, mit der die Listen des implementierenden Objektes auseinander
     *     gehalten werden k&ouml;nnen, falls es mehrere gibt.
     * @param o Das ausgew&auml;hlte Objekt, welches, nach Bearbeitung, in die Liste 
     *     eingef&uuml;gt werden soll.
     * @return Das eventuell durch die Methode ge&auml;nderte Objekt, das anschlie&szlig;end
     *     bearbeitet und eingef&uuml;gt werden soll, oder <TT>null</TT>, wenn der 
     *     Einf&uuml;ge-Vorgang abgebrochen werden soll. F&uuml;r eine Implementierung, in der 
     *     nichts passieren soll, bietet sich ein Durchreichen des Parameters <TT>o</TT> an.
     */
    public Editable beforeInsertObject(int id, Editable o);
    
    /**
     * &Uuml;ber diese Methode kann f&uuml;r die Auswahl beim Einf&uuml;gen neuer Elemente in 
     * die Liste eine Vorselektion angegeben werden.
     * @param id Eine Kennnummer, mit der die Listen des implementierenden Objektes auseinander
     *     gehalten werden k&ouml;nnen, falls es mehrere gibt.
     * @param o Ein leeres Objekt als Blaupause ?!?.
     * @return Ein String mit einer Klausel f&uuml;r die Einschr&auml;kung der Auswahl.
     */
    public String getPreselection(int id, Editable o);

    /**
     * Diese Methode wird aufgerufen, nachdem die Bearbeitung des Objektes abgeschlossen ist, 
     * aber bevor es letztendlich wieder in die Liste zur&uuml;ckgeschrieben wird.
     *
     * @param id Eine Kennnummer, mit der die Listen des implementierenden Objektes auseinander
     *     gehalten werden k&ouml;nnen, falls es mehrere gibt.
     * @param l Referenz auf die bearbeitete Liste.
     * @param o Das ge&auml;nderte Objekt.
     * @return <TT>true</TT>, wenn das Objekt in die Liste &uuml;bertragen werden soll, 
     *     <TT>false</TT> sonst.
     */
    public boolean isChangedObject(int id, List l, Object o);    
    
    /**
     * Diese Methode wird aufgerufen, bevor das ausgew&auml;hlte Objekt aus der Liste entfernt
     * wird.
     *
     * @param id Eine Kennnummer, mit der die Listen des implementierenden Objektes auseinander
     *     gehalten werden k&ouml;nnen, falls es mehrere gibt.
     * @param l Referenz auf die bearbeitete Liste.
     * @param o Das zu l&ouml;schende Objekt.
     * @return <TT>true</TT>, wenn das Objekt aus der Liste eliminiert werden soll, 
     *     <TT>false</TT> sonst.
     */
    public boolean isDeleteObject(int id, List l, Object o);
    
    /**
     * Diese Methode wird aufgerufen, nachdem ein oder mehrere Objekte zum Einf&uuml;gen in die
     * Liste ausgew&auml;hlt worden sind, aber bevor die Objekte in die Liste &uuml;bernommen
     * worden sind.
     *
     * @param id Eine Kennnummer, mit der die Listen des implementierenden Objektes auseinander
     *     gehalten werden k&ouml;nnen, falls es mehrere gibt.
     * @param l Referenz auf die bearbeitete Liste.
     * @param o Eine Liste mit den zur &Uuml;bernahme in die Liste ausgew&auml;hlten Objekten.
     * @return <TT>true</TT>, wenn die ausgew&auml;hlten Objekte in die Liste &uuml;bertragen 
     *     werden soll, <TT>false</TT> sonst.
     */
    public boolean isInsertObject(int id, List l, List o);
    
    /**
     * Diese Methode wird aufgerufen, nachdem die Bearbeitung des neugenerierten Objektes 
     * abgeschlossen ist, aber bevor es letztendlich wieder in die Liste zur&uuml;ckgeschrieben 
     * wird.
     *
     * @param id Eine Kennnummer, mit der die Listen des implementierenden Objektes auseinander
     *     gehalten werden k&ouml;nnen, falls es mehrere gibt.
     * @param l Referenz auf die bearbeitete Liste.
     * @param o Das neu generierte Objekt.
     * @return <TT>true</TT>, wenn das Objekt in die Liste &uuml;bertragen werden soll, 
     *     <TT>false</TT> sonst.
     */
    public boolean isNewObject(int id, List l, Object o);
    
}
