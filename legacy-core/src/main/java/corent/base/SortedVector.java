/*
 * SortedVector.java
 *
 * 04.02.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.base;


import java.util.Vector;


/**
 * Diese Erweiterung der Vector-Klasse dient der sortierten Anordnung von Vectorelementen. 
 * Objekte, die in einen SortedVector gespeichert werden sollen, m&uuml;ssen das Interface 
 * Comparable implementieren.
 * 
 * @author O.Lieshoff
 * 
 */

public class SortedVector<T extends Comparable> extends Vector<T> {
    
    public SortedVector() {
        super();
    }

    /**
     * F&uuml;gt das &uuml;bergebene Object o in den SortedVector ein, wenn es sich um ein 
     * Comparable handelt.
     *
     * @param o Das Object, da&szlig; in den SortedVector &uuml;bernommen werden soll.
     * @throws ClassCastException wenn das &uuml;bergebene Object kein Comparable ist oder 
     *     von der Klasse her nicht zu den im Vector gespeicherten Objekten pa&szlig;t.
     */
    public void addElement(T o) {
        Object o0 = null;
        boolean anfuegen = true;
        for (int i = 0, len = this.size(); i < len; i++) {
            o0 = (T) this.elementAt(i);
            if (o.compareTo(o0) < 0) {
                this.insertElementAt(o, i);
                anfuegen = false;
                break;
            }
        }
        if (anfuegen) {
            this.add(o);
        }
    }

}
