/*
 * Checker.java
 *
 * 18.07.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.base;


import java.awt.*;


/**
 * In dieser Klasse werden verschiedene Methoden angeboten, die bestimmte Zusammenh&auml;nge
 * kontrollierbar machen.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public class Checker {

    /**
     * Pr&uuml;ft, ob eine Komponente sichtbar und aufgeblendet ist.
     *
     * @param c Die zu kontrollierende Komponente.
     * @return <TT>true</TT>, wenn die zu kontrollierende Komponente sichtbar und aufgeblendet 
     *     ist,<BR><TT>false</TT> sonst.
     */
    public static boolean IsActive(Component c) {
        return (c.isVisible() && c.isEnabled());
    }
    
}
