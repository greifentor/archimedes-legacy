/*
 * ViewComponentFactory.java
 *
 * 18.02.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Diese Klasse definiert das notwendige Verhalten einer ViewComponentFactory.
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 03.10.2008 - Erweiterung um die Spezifikation der Methode 
 *              <TT>createSelectionComponent()</TT>.
 *     <P>
 *
 */
 
public interface ViewComponentFactory {
    
    /** 
     * Liefert die Instanz einer SelectionComponent, die durch die Factory produziert worden 
     * ist.
     *
     * @return Die Instanz einer SelectionComponent, die durch die Factory produziert worden 
     *     ist, bzw. <TT>null</TT>, falls die Factory keine eigene SelectionComponent erfordert
     *     und die Standardkomponente genutzt werden soll.
     *
     * @changed
     *     OLI 03.10.2008 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public SelectionComponent createSelectionComponent();
    
    /**
     * Liefert die Instanz einer ViewComponent, die durch die Factory produziert worden ist.
     *
     * @return Die Instanz einer ViewComponent, die durch die Factory produziert worden ist. 
     */
    public ViewComponent createViewComponent();
    
    /** 
     * @return Die Klasse, deren Objekte in der View-Komponente angezeigt werden bzw. 
     *     <TT>null</TT>, wenn keine solche Klasse definiert werden kann. 
     */
    public Class getServedClass();
    
    /** 
     * @return Formatstring f&uuml;r die Limit-Anzeige, wenn keine Datens&auml;tze gefunden 
     *     wurden. 
     */
    public String getFormatNoRecordsFound();
    
    /** 
     * @return Formatstring f&uuml;r die Limit-Anzeige, wenn eine Anzahl von Datens&auml;tzen
     *      gefunden worden ist, die sich im Limit bewegt. 
     */
    public String getFormatRecordCountInLimit();
    
    /** 
     * @return Formatstring f&uuml;r die Limit-Anzeige, wenn eine Anzahl von Datens&auml;tzen
     *      gefunden worden ist, die sich au&szlig;erhalb des Limits bewegt. 
     */
    public String getFormatRecordCountOffLimit();
     
}
