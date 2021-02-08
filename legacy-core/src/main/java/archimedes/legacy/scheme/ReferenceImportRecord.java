/*
 * ReferenceImportRecord.java
 *
 * 15.12.2008
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;


/**
 * Ein Container zum Festhalten einer aus dem DBMS importierten Referenzinformation.
 *
 * @author
 *     ollie
 *     <P>
 *
 * @changed
 *     OLI 15.12.2008 - Hinzugef&uuml;gt
 *     <P>
 *
 */

public class ReferenceImportRecord implements Comparable {
    
    private String fkColumnName = ""; 
    private String fkTableName = ""; 
    private String pkColumnName = ""; 
    private String pkTableName = ""; 

    /** Generiert ein ReferenceImportRecord-Objekt mit Defaultwerten. */
    public ReferenceImportRecord() {
        super();
    }
    
    /**
     * Generiert ein ReferenceImportRecord-Objekt anhand der &uuml;bergebenen Parameter.
     *
     * @param fktn Der neue Name der referenzierenden Tabelle.
     * @param fkcn Der neue Name der referenzierenden Tabellespalte.
     * @param pktn Der neue Name der referenzierten Tabelle.
     * @param pkcn Der neue Name der referenzierten Tabellespalte.
     */
    public ReferenceImportRecord(String fktn, String fkcn, String pktn, String pkcn) {
        super();
        this.fkColumnName = fkcn;
        this.fkTableName = fktn;
        this.pkColumnName = pkcn;
        this.pkTableName = pktn;
    }
    
    /**
     * Liefert den Namen der referenzierenden Tabellespalte.
     *
     * @return Name der referenzierenden Tabellespalte.
     */
    public String getFKColumnName() {
        return this.fkColumnName;
    }
    
    /**
     * Liefert den Namen der referenzierenden Tabelle.
     *
     * @return Name der referenzierenden Tabelle.
     */
    public String getFKTableName() {
        return this.fkTableName;
    }
    
    /**
     * Liefert den Namen der referenzierten Tabellespalte.
     *
     * @return Name der referenzierten Tabellespalte.
     */
    public String getPKColumnName() {
        return this.pkColumnName;
    }
    
    /**
     * Liefert den Namen der referenzierten Tabelle.
     *
     * @return Name der referenzierten Tabelle.
     */
    public String getPKTableName() {
        return this.pkTableName;
    }
    
    /**
     * Setzt den &uuml;bergebenen Namen als neuen Namen der referenzierenden Tabellespalte ein.
     *
     * @param fkcn Der neue Name der referenzierenden Tabellespalte.
     */
    public void setFKColumName(String fkcn) {
        this.fkColumnName = fkcn;
    }

    /**
     * Setzt den &uuml;bergebenen Namen als neuen Namen der referenzierenden Tabelle ein.
     *
     * @param fktn Der neue Name der referenzierenden Tabelle.
     */
    public void setFKTableName(String fktn) {
        this.fkTableName = fktn;
    }

    /**
     * Setzt den &uuml;bergebenen Namen als neuen Namen der referenzierten Tabellespalte ein.
     *
     * @param pkcn Der neue Name der referenzierten Tabellespalte.
     */
    public void setPKColumnName(String pkcn) {
        this.pkColumnName = pkcn;
    }

    /**
     * Setzt den &uuml;bergebenen Namen als neuen Namen der referenzierten Tabelle ein.
     *
     * @param pktn Der neue Name der referenzierten Tabelle.
     */
    public void setPKTableName(String pktn) {
        this.pkTableName = pktn;
    }
    
    
    /* Ueberschreiben von Methoden der Superklasse. */
    
    public int hashCode() {
        int result = 17;
        result = 37 * result + getFKColumnName().hashCode();
        result = 37 * result + getPKColumnName().hashCode();
        result = 37 * result + getFKTableName().hashCode();
        result = 37 * result + getPKTableName().hashCode();
        return result;
    }
    
    public boolean equals(Object o) {
        if (!(o instanceof ReferenceImportRecord)) {
            return false;
        }
        ReferenceImportRecord rir = (ReferenceImportRecord) o;
        return this.getFKColumnName().equals(rir.getFKColumnName())
                && this.getFKTableName().equals(rir.getFKTableName()) 
                && this.getPKColumnName().equals(rir.getPKColumnName()) 
                && this.getPKTableName().equals(rir.getPKTableName());
    }
    
    public String toString() {
        return this.getFKTableName().concat(".").concat(this.getFKColumnName()).concat(" > "
                ).concat(this.getPKTableName()).concat(".").concat(this.getPKColumnName());
    }

    
    /* Implementierung des Interfaces Comparable. */
    
    public int compareTo(Object o) {
        int e = 0;
        ReferenceImportRecord rir = (ReferenceImportRecord) o;
        e = this.getFKTableName().compareTo(rir.getFKTableName());
        if (e == 0) {
            e = this.getFKColumnName().compareTo(rir.getFKColumnName());
            if (e == 0) {
                e = this.getPKTableName().compareTo(rir.getPKTableName());
                if (e == 0) {
                    e = this.getPKColumnName().compareTo(rir.getPKColumnName());
                }
            }
        }
        return e;
    }
    
}
