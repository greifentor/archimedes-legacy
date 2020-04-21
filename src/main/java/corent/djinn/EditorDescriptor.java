/**
 * EditorDescriptor.java
 *
 * 04.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.base.*;


/** 
 * Mit Hilfe dieses Interfaces kann einfacher EditorDescriptor erzeugt werden, der in eine 
 * EditorDescriptorList aufgenommen werden kann.
 * 
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 09.01.2009 - Erweiterung um die Spezifikation der Methode 
 *             <TT>setObject(Attributed)</TT>. Dabei Formatanpassungen und Sortierung der 
 *             Methoden.
 *     <P>
 *
 */
 
public interface EditorDescriptor {
    
    /**
     * Liefert die Id des Attributes, das &uuml;ber den Descriptor manipuliert werden 
     * k&ouml;nnen soll.
     * 
     * @return Die Id des Attributes, das &uuml;ber den Descriptor manipuliert werden soll. 
     */
    public int getAttributeId();
    
    /**
     * Liefert die ComponentFactory, &uuml;ber die eine Komponente zum Descriptor gewonnen 
     * werden kann.
     *
     * @return Die ComponentFactory, &uuml;ber die eine Komponente zum Descriptor gewonnen 
     *     werden kann. 
     */
    public ComponentFactory getComponentFactory();
    
    /**
     * Liefert die Referenz auf die LabelFactory, &uuml;ber die der Descriptor seine Label 
     * beziehen soll.
     *
     * @return Referenz auf die LabelFactory, &uuml;ber die der Descriptor seine Label beziehen 
     *     soll.
     */
    public LabelFactory getLabelFactory();

    /**
     * Liefert die maximale L&auml;nge des hinter dem EditorDescriptor stehenden Datenfeldes in
     * Zeichen.
     *
     * @return Die maximale L&auml;nge des hinter dem EditorDescriptor stehenden Datenfeldes in
     *     Zeichen.
     */
    public int getMaxSize();
    
    /**
     * Liefert den Namen (Context), den die Komponente zum Editordescriptor tragen soll.
     *
     * @return Der Name (Context), den die Komponente zum EditorDescriptor tragen soll. 
     */
    public String getName();
    
    /**
     * Liefert das Attributed-Objekt, das mit dem Descriptor verbunden ist und dessen Attribut
     * angezeigt werden soll. Intern ist es sinnvoll eine WeakReference zu benutzen, um das 
     * Objekt zu referenzieren!
     *
     * @return Das Attributed-Objekt, das mit dem Descriptor verbunden ist und dessen Attribut 
     *     angezeigt werden soll. Intern ist es sinnvoll eine WeakReference zu benutzen, um das 
     *     Objekt zu referenzieren! 
     */
    public Attributed getObject();
    
    /**
     * Liefert das Tab auf dem die durch den Descriptor beschriebene Komponente abgebildet 
     * werden soll. Bei EditorDjinns mit nur einem Tab, sollte konstant der Wert 0 
     * zur&uuml;ckgeliefert werden.
     *
     * @return Das Tab auf dem die durch den Descriptor beschriebene Komponente abgebildet 
     *     werden soll. Bei EditorDjinns mit nur einem Tab, sollte konstant der Wert 0 
     *     zur&uuml;ckgeliefert werden.
     */
    public int getTab();
    
    /**
     * Informiert dar&uuml;ber, ob die Tabellenspalte innerhalb eines Stapels ge&auml;ndert 
     * werden k&ouml;nnen soll.
     *
     * @return <TT>true</TT>, wenn die Tabellenspalte innerhalb eines Stapels ge&auml;ndert 
     *     werden k&ouml;nnen soll, bzw. <TT>false</TT>, wenn das nicht der Fall ist.
     */
    public boolean isAlterInBatch();
    
    /**
     * Pr&uuml;ft, ob die Komponente abgeblendet dargestellt werden soll.
     *
     * @return <TT>true</TT>, falls die Komponente abgeblendet dargestellt werden soll. 
     */
    public boolean isDisabled(); 
    
    /** 
     * Pr&uuml;ft, ob es sich bei dem durch den Descriptor beschriebenen Feld um ein Pflichtfeld
     * handelt.
     *
     * @return <TT>true</TT>, wenn es sich bei dem durch den Descriptor beschriebenen Feld um
     *     ein Pflichtfeld handelt.
     */
    public boolean isObligation();
    
    /**
     * Setzt das &uuml;bergebene Objekt als neues Objekt f&uuml;r des Descriptor ein.
     *
     * <P><I><B>Hinweis:</B> Diese Methode funktioniert derzeit noch nicht zurverl&auml;ssig im
     * Zusammenspiel mit SubEditoren ... 
     * 
     * @param attr Das Objekt, das mit dem Descriptor gekoppelt werden soll.
     *
     * @changed
     *     OLI 09.01.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public void setObject(Attributed attr);
    
}
