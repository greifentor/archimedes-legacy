/*
 * Ressourced.java
 *
 * 15.12.2006
 *
 * (c) by O.Lieshoff
 *
 */

package corent.gui;


/**
 * Komponenten, die dieses Interface implementieren, werden in die Lage versetzt sich anhand
 * eines RessourceManagers zu aktualisieren. Dies ist unteranderem eine Grundlage, auf der die
 * Mehrsprachigkeit von Anwendungen basiert.
 *
 * @author O.Lieshoff
 */

public interface Ressourced extends ContextOwner {

    /**
     * Diese Methode aktualisiert die implementierende Klasse anhand des &uuml;bergebenen 
     * RessourceManagers.
     *
     * @param rm Der RessourceManager, anhand dessen die implementierende Klasse aktualisiert
     *     werden soll.
     */
    public void update(RessourceManager rm);

}
