/*
 * SubEditorFactory.java
 *
 * 25.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.awt.*;
import java.util.*;

import corent.base.*;


/**
 * Dieses Interface definiert das n&ouml;tige Verhalten f&uuml;r eine SubEditorFactory. Dies 
 * erm&ouml;glicht das Einbinden nicht standardisierter Panels in das Djinn-Konzept.
 *
 * @author O.Lieshoff
 *
 */
 
public interface SubEditorFactory {
    
    /** 
     * Generiert bzw. liefert einen SubEditor, der letztendlich in das Djinn-Panel eingebunden 
     * werden soll.
     *
     * @param owner Die Component, in der der EditorDjinn, dem der SubEditor angeh&ouml;rt, 
     *     abgebildet werden soll. 
     * @param obj Ein Attributed-Objekt, das in dem SubEditor bearbeitet bzw. angezeigt werden
     *     soll.
     * @param components Eine Hashtable mit den Components des &uuml;bergeordneten 
     *     EditorDjinnPanels (geschl&uuml;sselt nach ihren Contextnamen).
     * @return Eine Referenz auf den generierten SubEditor.
     */
    public SubEditor createSubEditor(Component owner, Attributed obj, Hashtable<String, 
            Component> components);
    
}
