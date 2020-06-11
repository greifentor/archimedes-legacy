/*
 * SecurityController.java
 *
 * 15.08.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.security;


import java.awt.*;


/**
 * Der SecurityController regelt die Zugriffsrechte f&uuml;r ihm bekannte Komponenten. Mit 
 * Zugriffsrechten sind hier die Sichtbarkeitsstufen "unsichtbar", "deaktiviert" und "aktiviert"
 * gemeint. Jede Komponente, die dem Manager bekanntgemacht wird, mu&szlig; im Falle einer 
 * Aktualisierung der Zugriffsrechte entsprechend geschaltet werden.
 * <P>Vorab m&uuml;ssen Bezeichner mit Zugriffsrechten erzeugt werden, unter denen die 
 * Komponenten w&auml;hrend des Programmablaufes eingebunden werden und mit dem definierten 
 * Recht ausgestattet werden k&ouml;nnen.<BR> 
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public interface SecurityController {
    
    /** Eine Menge von Bezeichnern f&uuml;r die Zugriffsrechte. */
    public enum AccessRight {INVISIBLE, DISABLED, ENABLED};
    
    /** 
     * Das angegebene Zugriffsrecht wird dem &uuml;bergebenen Bezeichner zugewiesen. Wird danach
     * eine Komponente an den Bezeichner gebunden, so erh&auml;lt sie das angegebene Recht,
     * andernfalls ein definiertes Standardrecht.
     *
     * @param ident Der Bezeichner, dem ein Recht zu gewiesen werden soll.
     * @param right Das an den Bezeichner zu bindende Zugriffsrecht.
     */
    public void setAccessRight(String ident, AccessRight right);
    
    /**
     * Ordnet die &uuml;bergebene Komponente dem angegebenen Bezeichner zu. Wird 
     * anschlie&szlig;end die Aktualisierungsmethode des SecurityControllers aufgerufen, wird 
     * die Komponente in den Zustand des entsprechenden Rechts gesetzt.
     *
     * @param ident Der Bezeichner, dem die Komponente zugewiesen werden soll.
     * @param comp Die Komponente, die dem Bezeichner zugewiesen werden soll.
     */
    public void setComponent(String ident, Component comp);
    
    /** 
     * L&ouml;scht die angegebene Komponente aus der Liste der Komponenten, die mit dem 
     * angegebenen Recht gekoppelt sind.
     *
     * @param ident Der Bezeichner, aus dem die Komponente gel&ouml;scht werden soll.
     * @param comp Die Komponente, die aus dem Bezeichner gel&ouml;scht werden soll.
     */
    public void releaseComponent(String ident, Component comp);
    
    /** Diese Methode aktualisiert alle an Bezeichner gebundene Komponenten. */
    public void updateComponents();
    
}
