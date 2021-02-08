/*
 * ViewComponent.java
 *
 * 05.02.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;
import javax.swing.*;


/**
 * Mit Hilfe dieses Interfaces wird das Verhalten einer ViewComponent festgelegt.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public interface ViewComponent {
    
    /** @return Ein Vector mit den im View selektierten Objekten. */
    public Vector getSelectedValues();
    
    /** @return Die Anzahl der aktuell selektierten Komponenten. */
    public int getSelectedValuesCount();
    
    /** 
     * @return Eine Komponente in der der Inhalt des View dargestellt wird. Dies mu&szlig; 
     *     nicht zwingend die eigentliche Komponente, wie eine JList oder &auml;hnliches sein,
     *     sondern kann z. B. auch ein JScrollPane zur&uuml;ckliefern, in dem die eigentliche
     *     Komponente untergebracht ist. 
     */
    public JComponent getView();
    
    /** 
     * @return Referenz auf die tats&auml;chliche Komponente des Views, aus der die Objekte
     *     ausgew&auml;hlt werden k&ouml;nnen.
     */
    public JComponent getViewComponent();
    
    /** 
     * Aktualisiert die View-Anzeige mit den &uuml;bergebenen Objekten. 
     *
     * @param criteria Ein Object-Array, in dem Kriterien zur Einschr&auml;nkung des Views 
     *     untergebracht werden k&ouml;nnen.
     * @param additions Zus&auml;tzliche Angaben (gegebenenfalls in SQL) zur Where-Klausel des 
     *     Views.
     * @return Anzahl der gefundenden Objekte, wenn die Property 
     *     "corent.djinn.ViewComponent.maximum" nicht initialisiert ist oder die Anzahl der 
     *     gefundenen Objekte kleiner oder gleich diesem Maximum ist. Ist die Anzahl 
     *     gr&ouml&szlig;er eines solchen Maximums wird ein negativer Wert zur&uuml;ckgegeben.
     *     Die angezeigte Objektliste beinhaltet jedoch nur eine Anzahl die dem definierten 
     *     Maximum entspricht.
     * @throws IllegalArgumentException Wenn der Inhalt der Criteria-Objekte nicht zum der
     *     Suchstrategie des Views passen.
     */
    public int updateView(Object[] criteria, String additions) throws IllegalArgumentException;
    
    /**
     * F&uuml;hrt etwaige Aktionen durch, die beim Schliessen der Komponente vonn&ouml;ten sind.
     */
    public void close();
    
    /**
     * F&uuml;hrt etwaige Aktionen durch, die nach dem Erzeugen der Komponente vonn&ouml;ten 
     * sind.
     */
    public void init();
    
}
