/*
 * PrintJobMacro.java
 * 
 * 30.10.2003
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.print;


import java.util.*; 
 

/**
 * Diese Klasse enth&auml;lt die f&uuml;r die Aufnahme eines Core-PrintJob-Macros notwendigen 
 * Daten.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public class PrintJobMacro {

    /** Der Name des Macros <I>(Default "")</I>. */
    protected String name = "";    
    /** Liste der Anweisungen des Macros <I>(Default new Vector())</I>. */
    protected Vector anweisungen = new Vector();
    
    /** 
     * Generiert ein Macro anhand der &uuml;bergebenen Daten.
     * 
     * @param name Der Name des Macros.
     */
    public PrintJobMacro(String name) {
        this(name, new Vector());
    }
    
    /** 
     * Generiert ein Macro anhand der &uuml;bergebenen Daten.
     * 
     * @param macro Ein Macro, dessen Inhalt in das neuerstellte Macro kopiert werden soll.
     */
    public PrintJobMacro(PrintJobMacro macro) {
        super();
        this.setName(macro.getName());
        this.setAnweisungen(new Vector(macro.getAnweisungen()));
    }
    
    /** 
     * Generiert ein Macro anhand der &uuml;bergebenen Daten.
     * 
     * @param name Der Name des Macros.
     * @param anweisungen Eine Liste mit Anweisungen, die das Macro enthalten soll.
     */
    public PrintJobMacro(String name, Vector anweisungen) {
        super();
        this.setName(name);
        this.setAnweisungen(anweisungen);
    }
    
    
    /* Accessoren & Mutatoren. */
    
    /**
     * Accessor f&uuml;r die Eigenschaft Name
     *
     * @return Der Wert der Eigenschaft Name
     */
    public String getName() {
        return new String(this.name);
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft Name
     *
     * @param name Der neue Wert f&uuml;r die Eigenschaft Name.
     */
    public void setName(String name) {
        if (name == null) {
            name = "";
        }
        this.name = name;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Anweisungen
     *
     * @return Der Wert der Eigenschaft Anweisungen
     */
    public Vector getAnweisungen() {
        return this.anweisungen;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft Anweisungen
     *
     * @param anweisungen Der neue Wert f&uuml;r die Eigenschaft Anweisungen.
     */
    public void setAnweisungen(Vector anweisungen) {
        if (anweisungen == null) {
            anweisungen = new Vector();
        }
        this.anweisungen = anweisungen;
    }    
    
}
