/*
 * EditorDescriptorList.java
 *
 * 04.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Mit Hilfe dieses Interfaces wird die f&uuml;r eine EditorDescriptorList notwendig 
 * Funktionalit&auml;t sichergestellt. Eine EditorDescriptorList enth&auml;lt 
 * EditorDescriptoren, die Informationen dar&uuml;ber enthalten, wie die einzelnen Datenfelder 
 * bei Benutzung eines EditorDjinns darzustellen und zu behandeln sind.
 *
 * @author O.Lieshoff
 *
 */

public interface EditorDescriptorList {
    
    /** 
     * F&uuml;gt den &uuml;bergebenen EditorDescriptor an die Liste an.
     *
     * @param ed Der an die Liste anzuf&uuml;gende EditorDescriptor.
     */
    public void addElement(EditorDescriptor ed);
    
    /** 
     * L&ouml;scht das EditorDescriptor-Objekt aus der Liste.
     * 
     * @param ed Das EditorDescriptor-Objekt, das aus der Liste entfernt werden soll.
     */
    public void removeElement(EditorDescriptor ed);

    /** 
     * Liefert das EditorDescriptor-Objekt an der n-ten Position der Liste.
     * 
     * @param n Die Position der Liste, aus der das EditorDescriptor-Objekt gelesen werden soll.
     * @return Das EditorDescriptor-Objekt an der angegebenen Position.
     * @throws IndexOutOfBoundsException falls ein ung&uuml;ltiger Index angegeben wird.
     */
    public EditorDescriptor elementAt(int n) throws IndexOutOfBoundsException;

    /** @return Die Anzahl der in der Liste enthaltenen EditorDescriptor-Objekte. */
    public int size();
    
    /**
     * Liefert den Wert <TT>true</TT>, wenn f&uuml;r wenigstens einen EditorDescriptor der Liste
     * gilt: isAlterInBatch() == true.
     *
     * @return <TT>true</TT>, wenn mindestens f&uuml;r wenigstens einen EditorDescriptor der 
     *     Liste die AlterInBatch-Flagge gesetzt ist.
     */
    public boolean isAlterInBatch();
    
}
