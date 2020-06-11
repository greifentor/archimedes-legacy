/* 
 * MassiveListSelectorComponentFactory.java
 *
 * 03.08.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import javax.swing.*;


/** 
 * Mit Hilfe dieses Interfaces wird das Verhalten einer Komponenten-Factory bestimmt, die einen
 * MassiveListSelector mit den notwendigen Komponenten versorgt.
 * <P>Der Selector selbst verf&uuml;gt &uuml;ber ein Anzeigefeld, das eine Angabe zum 
 * gew&auml;hlten Objekt macht, und zwei Buttons, von denen einer zum l&ouml;schen des 
 * gew&auml;hlten Objektes dient und der andere die Auswahl eines neuen Objektes 
 * erm&ouml;glicht.
 *
 * @author 
 *     <P>O.Lieshoff
 *     <P>
 *
 * @changed
 *     <P>OLI 20.02.2008 - Erweiterung um die Methoden <TT>getCls()</TT> und 
 *             <TT>setCls(Class)</TT>.
 *     <P>OLI 12.08.2008 - Erweiterung um die Spezifikation der Methode 
 *             <TT>setNullString(String)</TT>. 
 *
 */
 
public interface MassiveListSelectorComponentFactory {
    
    /** @return Der L&ouml;schen-Button zum Selektor. */
    public JButton createButtonClear();
    
    /** @return Der Auswahl-Button zum Selektor. */
    public JButton createButtonSelect();
    
    /** @return Das Anzeigefeld zum MassiveListSelector. */
    public JTextField createDisplayField();
    
    /** 
     * @return Die Anzeige, die im Anzeigefeld erscheinen soll, wenn kein Objekt gew&auml;hlt 
     *     wurde.
     */
    public String createNullString();
    
    /**
     * Liefert die Klasse der Objekte, die durch den MassiveListSelector ausgew&auml;hlt werden
     * k&ouml;nnen.
     *
     * @return Die Klasse der durch den MassiveListSelector ausw&auml;hlbaren Objekte.
     *
     * @changed
     *     OLI 20.02.2008 - Hinzugef&uuml;gt.
     *
     */
    public Class getCls();
    
    /**
     * Setzt die Klasse der durch den MassiveListSelector zu selektierenden Objekte.
     *
     * @param cls Die Klasse, der durch den MassiveListSelector auszuw&auml;hlenden Objekte.
     *
     * @changed
     *     OLI 20.02.2008 - Hinzugef&uuml;gt.
     *
     */
    public void setCls(Class cls);
    
    /**
     * Setzt den &uuml;bergebenen String als neuen Null-String f&uuml;r die ComponentFactory
     * ein.
     *
     * @param s Der neue Null-String f&uuml;r die ComponentFactory.
     *
     * @changed
     *     <P>OLI 12.08.2008 - Hinzugef&uuml;gt.
     *
     */
    public void setNullString(String s);
    
}
