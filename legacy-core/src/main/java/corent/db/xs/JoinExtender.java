/*
 * JoinExtender.java
 *
 * 01.03.2007
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


/**
 * &Uuml;ber die Implementierung dieses Interfaces kann das Verhalten einer DBFactory in einem
 * DBFactoryController in der Art ge&auml;ndert werden, da&szlig; der Selection ein 
 * zu&auml;tzliche Join-Klausel (im Textformat) angef&uuml;gt werden kann.
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 */
 
public interface JoinExtender<T> extends DBFactory<T> {
    
    /**
     * Liefert die zus&auml;tzliche Join-Klausel (im Textformat), um die die Selektion erweitert
     * werden soll.
     *
     * @return Eine zus&auml;tzliche Join-Klausel f&uuml;r Selektionen der DBFactory.
     */
    public String getJoinExtension();
    
}
