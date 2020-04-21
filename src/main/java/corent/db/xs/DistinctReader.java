/**
 * DistinctReader.java
 *
 * 30.09.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


/**
 * Mit Hilfe dieses Interfaces k&ouml;nnen Select-Statements im Rahmen der Nutzung der Klasse
 * DBFactoryUtil (und damit der DBFactoryController-Logik) durch ein Distinct eingeschr&auml;nkt
 * werden.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 30.09.2008 - Hinzugef&uuml;gt.
 *     <P>
 *
 */
 
public interface DistinctReader {
 
    /**
     * Liefert die Information, ob das Objekt der implementierenden Klasse beim Lesen durch den
     * DBFactoryController sein Select-Statement um ein Distinct erweitern soll.
     *
     * @return <TT>true</TT>, wenn das Select-Statement im Zusammenspiel mit dem 
     *     DBFactoryController um ein Distinct erweitert werden soll; <TT>false</TT> sonst.
     */
    public boolean isReadDistinct();
    
    /**
     * Liefert die Information, ob das Objekt der implementierenden Klasse beim Selektieren 
     * durch den DBFactoryController sein Select-Statement um ein Distinct erweitern soll.
     *
     * @return <TT>true</TT>, wenn das Select-Statement im Zusammenspiel mit dem 
     *     DBFactoryController um ein Distinct erweitert werden soll; <TT>false</TT> sonst.
     */
    public boolean isSelectDistinct();
    
}
