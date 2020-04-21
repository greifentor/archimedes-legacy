/*
 * GenerateExpander.java
 *
 * 14.05.2007
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


/**
 * Dieses Interface beschreibt das Verhalten, das eine Klasse haben mu&szlig;, um einen Eingriff
 * in den Generierungsproze&szlig; der DBFactory zu erm&ouml;glichen.
 *
 * @author O.Lieshoff
 *
 * @changed
 *     OLI 14.01.2008 - Parameter id von <TT>int</TT> auf <TT>long</TT> umgestellt. Dies war im
 *             Zusammenhang mit der Nutzung gro&szlig;er Schl&uuml;ssel unvermeidbar. Man 
 *             m&ouml;ge die entstehenden Unannehmlichkeiten, die durch diese Umdefinition 
 *             entstehen, entschuldigen :o).<BR>
 *
 */
 
public interface GenerateExpander {
    
    /**
     * Diese Methode erlaubt die Manipulation des generierten Datenbankschl&uuml;ssels.
     *
     * @param id Eine durch das Standardhandling ermittelte Id.
     * @return Ein Objekt mit dem manipulierten Schl&uuml;ssel. 
     *
     * @changed
     *     OLI 14.01.2008 - Parameter id von <TT>int</TT> auf <TT>long</TT> umgestellt.<BR>
     *
     */
    public Object doChangeKeys(long id);
    
    /**
     * Diese Methode enth&auml;lt die Routine, die zwischen der Gewinnung der neuen Id und dem 
     * Schreiben des erzeugten Datensatzes in die Datenbank steht.
     *
     * @param obj Das Objekt, f&uuml;r das kurzzuvor ein Schl&uuml;ssel generiert worden ist.
     * @return Das eventuell ge&auml;nderte Objekt.
     */
    public Object doGenerateExpansion(Object obj);
    
}
