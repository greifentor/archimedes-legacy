/*
 * EditorDjinnPrintMaster.java
 *
 * 03.06.2007
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Dieses Interface erm&ouml;glicht es einem Objekt, den Ausdruck via Print-Button eines 
 * EditorDjinnPanels Einflu&szlig; zu nehmen.
 *
 * @author O.Lieshoff
 *
 */
 
public interface EditorDjinnPrintMaster {
    
    /** 
     * Diese Methode wird aufgerufen, bevor der Ausdruck durch das EditorDjinnPanel initiiert
     * wird.
     */
    public void doBeforePrinting();
    
    /** 
     * Diese Methode wird aufgerufen, nachdem das EditorDjinnPanel den Ausdruck ausgef&uuml;hrt
     * hat.
     */
    public void doAfterPrinting();
    
}
