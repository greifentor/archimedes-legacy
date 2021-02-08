/*
 * DateListener.java
 *
 * 05.01.2005
 *
 * (c) by O.Lieshoff
 *
 */

package corent.event;


import corent.dates.*;


/**
 * Dieses Interface erm&ouml;glicht das Abh&ouml;ren eines Objektes, das Datumsangaben 
 * &auml;ndert und ein Abh&ouml;ren unterst&uuml;tzt.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface DateListener {

    /** 
     * Diese Methode wird aufgerufen, wenn sich das Datum in ge&auml;ndert hat.
     * 
     * @param date Das ge&auml;nderte Datum.
     */
    public void dateChanged(PDate date);

}
