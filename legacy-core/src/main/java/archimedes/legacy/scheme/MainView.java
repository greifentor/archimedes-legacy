/*
 * MainView.java
 *
 * 12.02.2005
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.scheme;


import archimedes.legacy.model.*;


/**
 * Diese Klasse kennzeichnet die Hauptsicht auf das Datenmodell.<BR>
 * <HR>
 *
 * @author ollie
 *
 */
 
public class MainView extends View implements MainViewModel {

    public MainView() {
        super();
    }

    public MainView(String bezeichnung, String kommentar, boolean isShowReferencedColumns) {
        super(bezeichnung, kommentar, isShowReferencedColumns);
    }
    
}
