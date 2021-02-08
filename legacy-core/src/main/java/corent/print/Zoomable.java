/*
 * Zoomable.java
 *
 * 31.08.2003
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.print;


/**
 * Dieses Interface legt die geforderten Methoden f&uuml;r ein in einem Preview-Fenster 
 * anzeigbares Objekt fest, das zus&auml;tzlich auch gezoomt werden kann.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 */

public interface Zoomable extends Previewable {

    /**
     * &Auml;ndert die dpi-Angabe des angezeigten Objektes. 
     *
     * @param dpi Die neue dpi-Angabe.
     */
    public void setDPI(int dpi);
    
    /** @return Die aktuelle dpi-Angabe zum Objekt. */
    public int getDPI();
    
}
