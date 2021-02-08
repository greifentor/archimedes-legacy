/*
 * GenericApplicationObject.java
 *
 * 09.08.2005
 *
 * (c) by ollie
 *
 */
package archimedes.legacy.app;


/**
 * Diese Erweiterung des ApplicationObject ist generisch und &uuml;berschreibt die abstrakten
 * Methoden der Superklasse.
 *
 * @author ollie
 *
 */
 
public class GenericApplicationObject extends ApplicationObject {
    
    /**
     * Generiert ein GenericApplicationObject anhand der &uuml;bergebenen Parameter.
     *
     * @param adf Die ArchimedesDescriptorFactory &uuml;ber die das Objekt seine Konfiguration
     *     bezieht.
     * @param tn Der Name der Tabelle, auf die sich das Objekt bezieht.
     */
    public GenericApplicationObject(ArchimedesDescriptorFactory adf, String tn) {
        super(adf, tn);
    }
    
    
    /* Implementierung der abstrakten Methoden der Superklasse. */
    
    public Object createObject() {
        return new GenericApplicationObject(this.adf, this.getTablename());
    }
    
    public Object createObject(Object blueprint) throws ClassCastException {
        return null;
    }
    
}
