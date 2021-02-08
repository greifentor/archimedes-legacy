/*
 * LabelFactory.java
 *
 * 06.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import javax.swing.*;


/**
 * Dieses Interface definiert die notwendigen Methoden, die eine LabelFactory bei der Anwendung
 * in einem EditorDjinn ben&ouml;tigt. Die LabelFactories sorgen f&uuml;r das Erzeugen eines 
 * Labels f&uuml;r einen EditorDescriptor.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface LabelFactory {
    
    /** 
     * Diese Methode generiert einen Label f&uuml;r den &uuml;bergebenen EditorDescriptor und 
     * das durch ihn referenzierte Objekt.
     *
     * @param ed Der EditorDescriptor f&uuml;r den der Label erzeugt werden soll. Die 
     *     LabelFactory sollte in der Lage sein aus dieser Auspr&auml;gung des EditorDescriptors
     *     die notwendigen Daten f&uuml;r den Label zu lesen. Am Anfang der Methode sollte auf 
     *     den richtigen Typ getestet werden.
     * @throws IllegalArgumentException Falls ein EditorDescriptor &uuml;bergeben wird, der 
     *     nicht f&uuml;r die Benutzung mit der Factory ungeeignet ist. 
     */
    public JLabel createLabel(EditorDescriptor ed) throws IllegalArgumentException;
    
}
