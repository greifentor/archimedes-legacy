/*
 * PanelModel.java
 *
 * 10.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package archimedes.legacy.model;


/**
 * Dieses Interface definiert das Verhalten f&uuml;r ein Objekt, das die Daten f&uuml;r Panels
 * in einem Archimedes-Datenbankmodell verwaltet.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface PanelModel extends Comparable, OptionListProvider {
    
    /** @return Die Klasse des Panels, oder "" falls die Standardklasse benutzer werden soll. */
    public String getPanelClass();
    
    /** 
     * Setzt die &uuml;bergebene Klasse als neue Klasse f&uuml;r den f&uuml;r das Panel zu 
     * erzeugende SubEditor-Klasse ein.
     *
     * @param cls Die neue Klasse f&uuml;r das Panel.
     */
    public void setPanelClass(String cls);
    
    /** @return Die Nummer des Panels. */
    public int getPanelNumber();
    
    /** 
     * Setzt die &uuml;bergebene Nummer als neue Panelnummer ein.
     *
     * @param nr Die neue Panelnummer.
     */
    public void setPanelNumber(int nr);
    
    /** @return Der Titel f&uuml;r das Tab der NReferenz. */
    public String getTabTitle();
    
    /**
     * Setzt den &uuml;bergebenen String als neuen Tab-Titel f&uuml;r die NReferenz ein.
     *
     * @param t Der neue Titel f&uuml;r die NReferenz.
     */
    public void setTabTitle(String t);
    
    /** @return Das Mnemonic f&uuml;r das Tab der NReferenz. */
    public String getTabMnemonic();
    
    /**
     * Setzt den &uuml;bergebenen String als neuen Tab-Mnemonic f&uuml;r die NReferenz ein.
     *
     * @param m Das neue Mnemonic f&uuml;r die NReferenz.
     */
    public void setTabMnemonic(String m);
    
    /** @return Der ToolTipText f&uuml;r das Tab der NReferenz. */
    public String getTabToolTipText();
    
    /**
     * Setzt den &uuml;bergebenen String als neuen Tab-ToolTipText f&uuml;r die NReferenz ein.
     *
     * @param ttt Der neue ToolTipText f&uuml;r die NReferenz.
     */
    public void setTabToolTipText(String ttt);
    
}
