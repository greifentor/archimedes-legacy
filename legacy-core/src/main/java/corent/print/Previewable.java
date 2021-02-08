/*
 * Previewable.java
 *
 * 10.08.2003
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.print;


import java.awt.*;


/**
 * Dieses Interface legt die geforderten Methoden f&uuml;r ein in einem Preview-Fenster 
 * anzeigbares Objekt fest.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public interface Previewable {

    /**
     * Diese Methode malt anhand des aufgearbeiteten Vector den eigentlichen Ausdruck.
     *
     * @param g Der Grafik-Kontext, auf den gedruckt werden soll.
     * @param page Die Seite, die gedruckt werden soll.
     */
    public void paint(Graphics g, int page);
    
}
