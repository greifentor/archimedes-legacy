/**
 * PathFinderListener.java
 *
 * 24.07.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.files;


/**
 * Mit Hilfe dieses Interfaces kann die PathFinder-Methode aus der FileUtil-Klasse &uuml;berwacht
 * werden. Der Listener wird &uuml;ber das aktuell bearbeitete File in Kenntnis gesetzt.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 24.07.2008 - Hinzugef&uuml;gt.
 *     <P>
 *
 */
 
public interface PathFinderListener {
    
    /**
     * Diese Methode wird aufgerufen, wenn der in der PathFinder-Methode ein neues File 
     * &uuml;berpr&uuml;ft werden soll.
     *
     * @param e Ein PathFinderEvent mit den Daten zu genutzten File.
     */
    public void fileChanged(PathFinderEvent e);
    
}     
