/*
 * Persistent.java
 *
 * 23.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


import java.io.*;

import corent.base.*;


/**
 * Dieses Interface definiert die notwendigen Methoden, die ein Objekt haben mu&szlig;, um mit
 * einer PersistenceFactory zusammenarbeiten zu k&ouml;nnen.<BR>
 * <BR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface Persistent extends Attributed, Serializable {
    
    /** @return Der PersistenceDescriptor zur Erzeugung und Speicherung von Objekten. */
    public PersistenceDescriptor getPersistenceDescriptor();    

    /** Generiert ein leeres Objekt der selben Klasse. */
    public Object createObject();
    
    /** 
     * Generiert ein leeres Objekt der selben Klasse als Kopie des &uuml;bergebenen Objektes.
     *
     * @param blueprint Das Vorlage-Objekt, zu dem die Kopie erzeugt werden soll.
     * @throws ClassCastException Wenn das &uuml;bergebene Objekt nicht von derselben Klasse 
     *     ist, wie das vorliegende. 
     */
    public Object createObject(Object blueprint) throws ClassCastException;

}
