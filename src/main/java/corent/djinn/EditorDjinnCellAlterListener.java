/*
 * EditorDjinnCellAlterListener.java
 *
 * 05.11.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.awt.*;
import java.util.*;


/**
 * Mit Hilfe dieses Interfaces k&ouml;nnen Objekte, die in einem EditorDjinn angezeigt werden,
 * &uuml;ber &Auml;nderungen am Inhalt des Editors informiert werden. Innerhalb dieses 
 * Ereignisses sind sie auch in der Lage auf die Inhalte der anderen Komponenten des Djinns 
 * Einflu&szlig; zu nehmen. 
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 */
 
public interface EditorDjinnCellAlterListener {
    
    /**
     * Diese Methode wird aufgerufen, wenn der Dateninhalt einer Komponente des EditorDjinns
     * ge&auml;ndert worden ist.
     *
     * @param comp Die Komponente, deren Dateninhalt ge&auml;ndert worden ist.
     * @param comps Eine Liste mit Referenzen auf die anderen Komponenten des Djinns.
     */
    public void dataChanged(Component comp, Hashtable<String, Component> comps);
    
}
