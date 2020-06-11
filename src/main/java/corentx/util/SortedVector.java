/*
 * SortedVector.java
 *
 * 04.02.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.util;


import java.util.*;


/**
 * Diese Erweiterung der Vector-Klasse dient der sortierten Anordnung von Vectorelementen. 
 * Objekte, die in einen SortedVector gespeichert werden sollen, m&uuml;ssen das Interface 
 * Comparable implementieren.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 10.09.2009 - &Uuml;bernahme aus dem Package corent.base.
 *
 */

public class SortedVector<T extends Comparable> extends Vector<T> {

    private Comparator<T> comparator = null;

    /**
     * Erzeugt einen SortedVector mit Defaultwerten.
     */
    public SortedVector() {
        super();
    }

    /**
     * Erzeugt einen SortedVector aus der &uuml;bergebenen Collection. Die Elemente der
     * Collection werden entsprechend ihrer Sortierung in den Vector eingef&uuml;gt.
     *
     * @param c Die Collection, deren Elemente in den SortedVector &uuml;bernommen werden
     *         sollen.
     * @throws NullPointerException Falls die &uumlbergebene Collection eine
     *         <TT>null</TT>-Referenz ist.
     */
    public SortedVector(Collection<T> c) throws NullPointerException {
        super();
        for (Iterator i = c.iterator(); i.hasNext(); ) {
            this.addElement((T) i.next());
        }
    }

    /**
     * Erzeugt einen SortedVector mit einem alternativen Comparator nach dem die Objekte des
     * Vectors sortiert werden sollen.
     *
     * @param c Der alternative Comparator nach dem die Objekte des Vectors sortiert werden
     *         sollen.
     * @throws NullPointerException Falls der &uumlbergebene Comparator eine
     *         <TT>null</TT>-Referenz ist.
     */
    public SortedVector(Comparator<T> c) throws NullPointerException {
        super();
        this.comparator = c;
    }

    /**
     * Erzeugt einen SortedVector mit der angegebenen initialen Kapazit&auml;t.
     *
     * @param initialCapacity Die initialen Kapazit&auml;t mit der der Vector erzeugt werden
     *         soll.
     * @throws IllegalArgumentException Falls die initiale Kapazit&auml;t mit einem negativen
     *         Wert &uuml;bergeben wird.
     */
    public SortedVector(int initialCapacity) throws IllegalArgumentException {
        super(initialCapacity);
    }

    /**
     * Erzeugt einen SortedVector mit der angegebenen initialen Kapazit&auml;t und einem
     * initialen Wert f&uuml;r die Vergr&ouml;&szlig;erung der Kapazit&auml;t.
     *
     * @param initialCapacity Die initialen Kapazit&auml;t mit der der Vector erzeugt werden
     *         soll.
     * @param capacityIncrement Ein Wert f&uuml;r die Vergr&ouml;&szlig;erung der
     *         Kapazit&auml;t..
     * @throws IllegalArgumentException Falls die initiale Kapazit&auml;t mit einem negativen
     *         Wert &uuml;bergeben wird.
     */
    public SortedVector(int initialCapacity, int capacityIncrement)
            throws IllegalArgumentException {
        super(initialCapacity, capacityIncrement);
    }

    /**
     * F&uuml;gt das &uuml;bergebene Object o in den SortedVector ein, wenn es sich um ein
     * Comparable handelt.
     *
     * @param o Das Object, das in den SortedVector &uuml;bernommen werden soll.
     * @throws NullPointerException Falls das anzuf&uuml;gende Objekt eine
     *         <TT>null</TT>-Referenz ist.
     * @precondition o != <TT>null</TT>
     */
    @Override
    public boolean add(T o) throws NullPointerException {
        Object o0 = null;
        for (int i = 0, len = this.size(); i < len; i++) {
            o0 = (T) this.get(i);
            if ((this.comparator == null) && (o.compareTo(o0) < 0)) {
                this.insertElementAt(o, i);
                return true;
            } else if ((this.comparator != null) && (this.comparator.compare(o, (T) o0) < 0)) {
                this.insertElementAt(o, i);
                return true;
            }
        }
        if (o == null) {
            throw new NullPointerException("null references can not be add to SortedVectors.");
        }
        return super.add(o);
    }

    /**
     * F&uuml;gt das &uuml;bergebene Object o in den SortedVector ein, wenn es sich um ein
     * Comparable handelt.
     *
     * @param o Das Object, das in den SortedVector &uuml;bernommen werden soll.
     * @throws NullPointerException Falls das anzuf&uuml;gende Objekt eine
     *         <TT>null</TT>-Referenz ist.
     * @precondition o != <TT>null</TT>
     */
    @Override
    public void addElement(T o) throws NullPointerException {
        this.add(o);
    }

    /**
     * F&uuml;gt das &uuml;bergebene Object o in den SortedVector ein, ohne die es in den Vector
     * einzusortieren.
     *
     * @param o Das Object, da&szlig; in den SortedVector &uuml;bernommen werden soll.
     * @throws NullPointerException Falls das anzuf&uuml;gende Objekt eine
     *         <TT>null</TT>-Referenz ist.
     * @precondition o != <TT>null</TT>
     */
    public void addUnsorted(T o) throws NullPointerException {
        if (o == null) {
            throw new NullPointerException("null references can not be add to SortedVectors.");
        }
        super.add(o);
    }

}
