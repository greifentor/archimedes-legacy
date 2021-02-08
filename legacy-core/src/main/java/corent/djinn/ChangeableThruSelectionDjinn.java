/*
 * ChangeableThruSelectionDjinn.java
 *
 * 12.01.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corent.djinn;


/**
 * Diese Interface mu&szlig; durch Klassen implementiert werden, die durch Aktionen der 
 * SelectionDjinns manipuliert werden sollen.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 12.01.2009 - Hinzugef&uuml;gt
 *     <P>
 *
 */

public interface ChangeableThruSelectionDjinn {

    /**
     * Diese Methode wird aufgerufen, nachdem das Objekt durch einen EditorDjinn geschrieben 
     * wurde der innerhalb eines SelectionDjinns aufgerufen worden ist. In dieser Methode 
     * k&ouml;nnen Informationen vom &uuml;bergebenen Objekt an das implementierende 
     * &uuml;bergeben werden.
     *
     * <P><I><B>Hintergrund:</B> Da das DefaultEditorDjinnPanel derzeit (Jan 09) nicht in der 
     * Lage ist das dargestellte Objekt zu &auml;ndern, ist diese Methode eine Art Workaround, 
     * der zur Freischaltung des Speichern-Buttons n&ouml;tig ist.</I>
     *
     * @param o Das ge&auml;nderte Objekt.
     * @return <TT>true</TT>, sofern die Aktion erfolgreich durchgef&uuml;hrt worden ist, sonst
     *     <TT>false</TT>.
     */
    public boolean copyAfterWrite(Object o);

}
