/*
 * Editable.java
 *
 * 04.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Dieses Interface stellt die notwendigen Funktionalit&auml;ten f&uuml;r die Benutzung des 
 * implementierenden Objektes in Zusammenhang mit automatischen Dialogklassen sicher.
 * 
 * @author
 *     O.Lieshoff
 *
 */ 
 
public interface Editable {
    
    /** 
     * Diese Methode liefert eine EditorDescriptorList f&uuml;r das implementierende Objekt. 
     * 
     * @return EditorDescriptorList mit den Descriptoren f&uuml;r die einzelnen Datenfelder.
     */
    public EditorDescriptorList getEditorDescriptorList();
    
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
