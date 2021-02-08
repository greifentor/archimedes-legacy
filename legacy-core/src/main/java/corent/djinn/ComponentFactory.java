/*
 * ComponentFactory.java
 *
 * 06.01.2004
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.files.*;

import java.awt.*;

import javax.swing.*;


/**
 * Mit Hilfe dieser Factory kann sich ein EditorDescriptor innerhalb eines EditorDjinns mit 
 * einer Komponente versorgen.
 *
 * @author O.Lieshoff
 *
 */
 
public interface ComponentFactory {
    
    /** 
     * Mit Hilfe dieser Methode wird zu dem durch den EditorDescriptor definierten Objekt eine 
     * passende Komponente gebildet, die zum Editieren bzw. Anzeigen von derartigen Objekten 
     * geeignet ist.
     * <P>Der ebenfalls &uuml;bergebene EditorDescriptor kann zus&auml;tzliche Informationen 
     * bereitstellen. Sollte eine bestimmter Unterklasse des EditorDescriptors n&ouml;tig sein,
     * so mu&szlig; auf die Korrektheit der Klasse des &uuml;bergebenen Descriptors gepr&uuml;ft
     * werden und gegebenenfalls eine <TT>IllegalArgumentException</TT> geworfen werden. 
     *
     * @param ed Der EditorDescriptor, f&uuml;r den die Factory t&auml;tig werden soll. Der 
     *     Descriptor kann gegebenenfalls zus&auml;tzliche Information zum Bau der Komponente 
     *     enthalten.
     * @param owner Die Komponente, in der sich das Panel befindet, von der aus die 
     *     ComponentFactory genutzt wird.
     * @param ini Die Inidatei der Applikation, &uuml;ber die sich die Komponenten mit Daten zu
     *     ihrer Rekonstruktion versorgen k&ouml;nnen.
     * @throws IllegalArgumentException Falls ein EditorDescriptor &uuml;bergeben wird, der 
     *     nicht f&uuml;r die Benutzung mit der Factory ungeeignet ist. 
     */
    public JComponent createComponent(EditorDescriptor ed, Component owner, Inifile ini) 
            throws IllegalArgumentException;
            
    /** 
     * Ein Aufruf dieser Methode lie&szlig;t den in der &uuml;bergebenen Komponente 
     * repr&auml;sentierten Wert f&uuml;r das durch den EditorDescriptor referenzierte Objekt
     * und &uuml;bertr&auml;gt ihn in das Objekt.
     *
     * @param ed Der EditorDescriptor f&uuml;r den den die Komponente ausgelesen werden soll.
     * @param comp Die Komponente, aus der der Wert gelesen werden soll.
     * @throws IllegalArgumentException Falls der EditorDescriptor und die Komponente nicht 
     *     zusammenpassen oder der EditorDescriptor zum Betrieb mit der Factory generell 
     *     ungeeignet ist.
     */
    public void transferValue(EditorDescriptor ed, JComponent comp) 
            throws IllegalArgumentException;
    
    /** 
     * Ein Aufruf dieser Methode lie&szlig;t den in der &uuml;bergebenen Komponente 
     * repr&auml;sentierten Wert f&uuml;r das durch den EditorDescriptor referenzierte Objekt
     * aus.
     *
     * @param ed Der EditorDescriptor f&uuml;r den den die Komponente ausgelesen werden soll.
     * @param comp Die Komponente, aus der der Wert gelesen werden soll.
     * @return Der ausgelesene Wert.
     * @throws IllegalArgumentException Falls der EditorDescriptor und die Komponente nicht 
     *     zusammenpassen oder der EditorDescriptor zum Betrieb mit der Factory generell 
     *     ungeeignet ist.
     */
    public Object getValue(EditorDescriptor ed, JComponent comp) 
            throws IllegalArgumentException; 
}
