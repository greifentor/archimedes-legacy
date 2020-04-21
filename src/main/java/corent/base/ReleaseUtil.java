/*
 * ReleaseUtil.java
 *
 * 10.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.base;


import java.awt.*;


/**
 * Diese Klasse bietet eine Handvoll Utilities zur Anwendung mit dem Releaseable-Interface. Es 
 * werden Methoden zum rekursiven 'releasen' ganzer Objekt-Strukturen gegeben.<BR>
 * <HR>
 * 
 * @author O.Lieshoff
 *
 */
 
public class ReleaseUtil {
    
    /**
     * Mit Hilfe dieser Methode k&ouml;nnen GUI-Komponenten samt ihrer Inhalte 'released' 
     * werden. Die Methode wendet sich rekursiv an, wenn sie auf einen java.awt.Container 
     * trifft.
     *
     * @param c Der java.awt.Container der zusammen mit den in ihm enthaltenen Objekten 
     *     'released' werden soll. 
     */
    public static synchronized void ReleaseContainer(Object c) {
        if (c instanceof Container) {
            for (int i = 0; i < ((Container) c).getComponents().length; i++) {
                Object o = ((Container) c).getComponent(i);
                ReleaseContainer(o);
            }
        }
        if (c instanceof Releaseable) {
            ((Releaseable) c).release();
        }
    }
        
} 
