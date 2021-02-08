/*
 * MassiveComboBoxMaster.java
 *
 * 15.11.2005
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


/**
 * Mit Hilfe dieses Interfaces kann ein Objekt den Aufbau der aus ihm in einem EditorDjinn
 * resultierenden MassiveComboBoxen beeinflussen.<BR>
 * <HR>
 *
 * @author ollie
 *
 */
 
public interface MassiveComboBoxMaster {
    
    /**
     * Liefert einen preselection-String f&uuml;r die Where-Klausel f&uuml;r die 
     * Datenbankanfrage, aus der die MassiveComboBox zum Attribut mit dem angegebenen Namen 
     * gebildet werden soll.
     *
     * @param name Der Name des Attributs, zu dem eine preselection-Klausel angefordert wird.
     */
    public String getPreselection(String name);
    
}
