/*
 * TabbedPaneFactory.java
 *
 * 15.01.2004
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;

import javax.swing.*;


/**
 * Diese Factory produziert ein JTabbedPane, das z. B. zur Aufnahme von Panels eines EditorDjnns 
 * dient.
 * <P>Die einzelnen Tabs m&uuml;ssen bereits mit leeren JPanels bef&uuml;llt sein, die mit 
 * GridLayouts initialisiert worden sind, um dort die eigentlichen Komponenten einf&uuml;gbar zu
 * machen.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public interface TabbedPaneFactory {
    
    /** @return Generiert ein JTabbedPane z. B. zur Einbindung in einen EditorDjinn. */
    public JTabbedPane createTabbedPane();
    
    /** @return Ein Vector mit den TabDescriptoren der Factory. */
    public Vector<TabDescriptor> getTabDescriptors();

}
