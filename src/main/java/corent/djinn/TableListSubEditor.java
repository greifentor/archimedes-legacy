/*
 * TableListSubEditor.java
 *
 * 24.10.2007
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Diese Spezialisierung des Interfaces SubEditor kann bei der Umsetzung von SubEditoren 
 * angewandt werden, die Listen beinhalten und diese Listen in einer JTable-Komponente 
 * darstellen. Hiermit kann ein Zugriff auf den SubEditor-Inhalt erreicht werden.
 * <I>Hinweis:</I> Es sei dringend davon abgeraten, die GUI-Ereignisse des JTables abzufangen
 * und zum Bearbeiten der Liste zu verwenden. Hierzu sollte, zumindest im Zusammenhang mit der
 * Archimedes-Applikationslogik die in der Archimedes-Bibliothek vorr&auml;tigen 
 * Interfaces <TT>SortedListSubEditorMaster</TT> und <TT>SortedListSubEditorWatcher</TT> genutzt
 * werden.
 *
 * @author
 *     O.Lieshoff
 *
 * @changed
 *     OLI 24.10.2007 - Erzeugt.
 *
 */
 
public interface TableListSubEditor extends SubEditor {
    
    /**
     * Liefert eine Referenz auf die JTable-Komponente, in der die Liste dargestellt wird.
     *
     * @return Referenz auf die JTable-Komponente, in der die Liste dargestellt wird.
     */
    public javax.swing.JTable getJTable();
    
    /**
     * Liefert eine Referenz auf die Liste, die in dem SubEditor bearbeitet werden soll.
     *
     * @return Eine Referenz auf die in dem SubEditor bearbeiteten Liste.
     */
    public java.util.List getList();
    
}
