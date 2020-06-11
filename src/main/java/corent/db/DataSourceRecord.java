/*
 * DataSourceRecord.java
 *
 * 01.03.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


/**
 * Dieses Interface definiert das Verhalten eines DataSourceRecords. Diese Records enthalten 
 * Informationen &uuml;ber zum Teil sehr verschiedenartige Datenquellen.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public interface DataSourceRecord {
    
    /** @return Ein bezeichnender Name f&uuml;r eine Datenquelle. */
    public String getName();
    
    /** 
     * &Auml;ndert den Namen der Datenquelle in den angegebenen Wert.
     *
     * @param name Der neue Name zur Datenquelle.
     */
    public void setName(String name);
    
    /** @return Eine kurze Beschreibung zur Datenquelle. */
    public String getDescription();
    
    /** 
     * &Auml;ndert die Beschreibung der Datenquelle in den angegebenen Wert.
     *
     * @param description Die neue Beschreibung zur Datenquelle.
     */
    public void setDescription(String description);
    
}
