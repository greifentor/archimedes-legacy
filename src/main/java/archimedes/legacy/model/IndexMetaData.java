/*
 * IndexModel.java
 *
 * 14.12.2011
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;

import gengen.metadata.*;


/**
 * Dieses Interface definiert die Schnittstelle eines Index-Objekte. Mit Index ist in diesem
 * Fall ein Index auf ein Feld (oder mehrere) der Datenbank gemeint.
 *
 * <P>Die Implementierungen des Interfaces sollten alphabetisch nach Indexnamen sortierbar sein.
 *
 * @author O.lieshoff
 *
 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
 * @changed OLI 18.12.2011 - Umstellung auf Tabellen- und Tabellenspaltenobjekte.
 */

public interface IndexMetaData extends Comparable, NamedObject {

    /**
     * F&uuml;gt eine Tabellenspalte in den Index ein, die in dem Index ber&uuml;cksichtigt
     * werden soll.
     *
     * @param column Die Tabellenspalte, der an den Index angef&uuml;gt werden soll.
     * @throws IllegalArgumentException Falls ein der Vorbedingungen verletzt wird.
     * @precondition column != <CODE>null</CODE>
     *
     * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
     */
    abstract public void addColumn(AttributeMetaData column)
            throws IllegalArgumentException;

    /**
     * L&ouml;scht alle Spaltenzuordnungen f&uuml;r den Index.
     *
     * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
     */
    abstract public void clearColumns();

    /**
     * Liefert eine Liste der in dem Index ber&uuml;cksichtigten Tabellenspalten.
     *
     * @return Eine Liste der in dem Index ber&uuml;cksichtigten Tabellenspalten.
     *
     * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
     */
    abstract public AttributeMetaData[] getColumns();

    /**
     * Liefert den Namen des Index.
     *
     * @return Der Name des Index.
     *
     * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
     */
    abstract public String getName();

    /**
     * Liefert die Tabelle, f&uuml;r die der Index erstellt werden soll.
     *
     * @return Die Tabelle, f&uuml;r die der Index erstellt werden soll
     *
     * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
     */
    abstract public ClassMetaData getTable();

    /**
     * Pr&uuml;ft, ob die Tabellenspalte mit dem angegebenen Namen durch den Index referenziert
     * wird.
     *
     * @param name Der Name der Tabellenspalte, auf die gepr&uuml;ft werden soll.
     * @return <CODE>true</CODE>, wenn die Tabellenspalte mit dem angegebenen Namen durch den
     *         Index referenziert wird, <CODE>false</CODE>.
     * @throws IllegaleArgumentException Falls eine der Vorbedingungen verletzt wird.
     * @precondition name != <CODE>null</CODE>
     * @precondition !name.isEmpty()
     *
     * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
     */
    abstract public boolean isMember(String name) throws IllegalArgumentException;

    /**
     * L&oumkl;scht eine Tabellenspalte aus der Liste der f&uuml;r den Index zu
     * ber&uuml;cksichtigenden Tabellenspalten.
     *
     * @param column Die Tabellenspalte, die aus dem Index entfernt werden soll.
     * @throws IllegalArgumentException Falls ein der Vorbedingungen verletzt wird.
     * @precondition column != <CODE>null</CODE>
     *
     * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
     */
    abstract public void removeColumn(AttributeMetaData column)
            throws IllegalArgumentException;

    /**
     * Setzt einen neuen Namen f&uuml;r den Index ein.
     *
     * @param name Der neue Name zum Index.
     * @throws IllegalArgumentException Falls eine der Vorbedingungen verletzt wird.
     * @precondition name != <CODE>null</CODE>
     * @precondition !name.isEmpty()
     *
     * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
     */
    abstract public void setName(String name) throws IllegalArgumentException;

    /**
     * Setzt die angegebene Tabelle als neue Tabelle ein, f&uuml;r die der Index erzeugt werden
     * soll.
     *
     * @param table Die neue Tabelle, f&uuml;r die der Index erzeugt werden soll.
     * @throws IllegalArgumentException Falls eine der Vorbedingungen verletzt wird.
     * @precondition table != <CODE>null</CODE>
     *
     * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
     */
    abstract public void setTable(ClassMetaData table) throws IllegalArgumentException;

}