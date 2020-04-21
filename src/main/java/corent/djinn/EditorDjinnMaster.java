/*
 * EditorDjinnMaster.java
 *
 * 14.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;


/**
 * Dieses Interface definiert das notwendige Verhalten f&uuml;r ein Objekt, das kontrolliert auf
 * die Ereignisse des EditorDjinnPanels reagieren soll.
 * 
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 26.02.2008 - Umbau auf EditorDjinnMode-Betrieb.
 *     <P>OLI 04.06.2008 - Erweiterung um die Methode <TT>doAfterDjinnDispelled()</TT>.
 *     <P>OLI 05.06.2008 - Entfernung der Methode <TT>doAfterDjinnDispelled()</TT>. Nach der 
 *              Umstellung eines bestehenden Projektes, ist mit aufgegangen, das solche 
 *              alteingesessenen Interfaces nicht mehr erweitert werden sollten ;o).
 *
 */
 
public interface EditorDjinnMaster {
    
    /**
     * Diese Methode wird aufgerufen, nachdem der CleanUp beim Verwerfen des Editorinhaltes 
     * erfolgt ist.
     *
     * @param comps Die Liste mit den Komponenten des Djinns.
     * @return <TT>true</TT>, wenn der CleanUp erfolgreich war und der EditorDjinn geschlossen
     *     werden kann.
     */
    public boolean doAfterCleanUp(Hashtable<String, java.awt.Component> comps);
    
    /**
     * Diese Methode kommt zur Ausf&uuml;hrung nachdem das EditorDjinnPanel aufgebaut worden 
     * ist.
     *
     * @param comps Die Liste mit den Komponenten des Djinns.
     * @param mode Der Modus, in dem das Panel betrieben wird (Neuanlage, &Auml;nderung oder 
     *     Duplikation).
     */
    public void doAfterDjinnSummoned(Hashtable<String, java.awt.Component> comps, 
            EditorDjinnMode mode);
    
    /**
     * Diese Methode wird aufgerufen, bevor das EditorDjinnPanel die Anweisung zum Schliessen 
     * des Djinn gibt. Liefert diese Methode den Wert <TT>false</TT> zur&uuml;ck, so wird das
     * Editorfenster nicht geschlossen.
     *
     * @return <TT>true</TT>, wenn die Daten&uuml;bernahme erfolgreich und korrekt erfolgt ist
     *     und der EditorDjinn geschlossen werden soll. 
     */
    public boolean doAfterTransferValues();
    
    /**
     * Diese Methode wird aufgerufen, bevor das EditorDjinnPanel die Daten aus den visuellen
     * Komponenten in das bearbeitet Objekt &uuml;bertr&auml;gt. Liefert diese Methode den Wert 
     * <TT>false</TT> zur&uuml;ck, so wird das Editorfenster nicht geschlossen.
     *
     * @param comps Die Liste mit den Komponenten des Djinns.
     * @return <TT>true</TT>, wenn alle Vorbedingungen zur Daten&uuml;bernahme zutreffen und 
     *     die Daten aus dem Fenster in das Objekt &uuml;bernommen werden sollen. 
     */
    public boolean doBeforeTransferValues(Hashtable<String, java.awt.Component> comps);
    
    /** 
     * Innerhalb dieser Methode kann gepr&uuml;ft werden, ob das editierte Objekt 
     * tats&auml;chlich gel&ouml;scht werden soll.
     *
     * @param comps Die Liste mit den Komponenten des Djinns.
     * @return <TT>true</TT>, wenn das Objekt zum L&ouml;schen freigegeben und der EditorDjinn
     *     geschlossen werden soll.
     */
    public boolean doBeforeDelete(Hashtable<String, java.awt.Component> comps);
    
    /** 
     * @return <TT>true</TT>, wenn das Objekt keinen R&uuml;ckfragedialog ausl&ouml;sen soll, 
     *     wenn der Benutzer einen Wartungsdialog mit dem L&ouml;schen-Button 
     *     verl&auml;&szlig;t.
     */
    public boolean isDeleteConfirmSuppressed();
    
    /** 
     * @return <TT>true</TT>, wenn das Objekt keinen R&uuml;ckfragedialog ausl&ouml;sen soll, 
     *     wenn der Benutzer einen Wartungsdialog mit dem Abbrechen-Button verl&auml;&szlig;t.
     */
    public boolean isDiscardConfirmSuppressed();
    
}
