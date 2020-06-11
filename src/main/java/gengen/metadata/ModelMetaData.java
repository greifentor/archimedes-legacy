/*
 * ModelMetaData.java
 *
 * 06.09.2009
 *
 * (c) by O.Lieshoff
 *
 */

package gengen.metadata;


import java.util.*;


/**
 * Implementierungen dieses Interfaces ein ganzes Klassenmodell.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 06.09.2009 - Hinzugef&uuml;gt
 * @changed OLI 30.09.2009 - Erweiterung um die Spezifikation der Methode
 *         <CODE>getProjectToken()</CODE>.
 * @changed OLI 20.12.2011 - Erweiterung um die Spezifikation der Methode
 *         <CODE>getClass(String)</CODE>.
 */

public interface ModelMetaData {

    /**
     * Liefert den Autor des Modells.
     *
     * @return Der Autor des Modells.
     */
    public String getAuthor();

    /**
     * Liefert den Namen des Basis-Packages unterhalb dessen der Code der Klassen des Modells
     * abgelegt werden soll.
     *
     * @return Der Name des Basis-Packages unterhalb dessen der Code der Klassen des Modells
     *         abgelegt werden soll.
     */
    public String getBasePackageName();

    /**
     * Liefert die Klasse an der i-ten Position der Klassenliste des Modells.
     *
     * @param i Die Position der Klasse innerhalb der Klassenliste des Modells, die
     *         zur&uuml;ckgeliefert werden soll.
     * @return Die Klasse an der i-ten Position der Klassenliste des Modells.
     * @throws IndexOutOfBoundsException Falls die Position i nicht im Rahmen der Liste der
     *         Klassen des Modells liegt.
     */
    public ClassMetaData getClass(int i) throws IndexOutOfBoundsException;

    /**
     * Liefert die Klasse zum angegebenen Namen.
     *
     * @param name Der Name der Klasse, deren Referenz ermittelt werden soll.
     * @return Die Klasse, zum angebenen Namen oder <CODE>null</CODE>, falls es keine Klasse zum
     *         angegebenen Namen gibt.
     * @throws IllegalArgumentException Falls der Name, zu dem die Klasse ermittelt werden soll
     *         leer oder als <CODE>null</CODE>-Referenz &uuml;bergeben wird.
     */
    public ClassMetaData getClass(String name) throws IllegalArgumentException;

    /**
     * Liefert eine Liste der Klassen des Modells.
     *
     * @return Eine Liste der Klassen des Modells.
     */
    public List<ClassMetaData> getClasses();

    /**
     * Liefert ein K&uuml;rzel zum Projekt, dem das Datenmodell zugeordnet ist. Dieses
     * K&uuml;rzel kann innerhalb der Codegenerierung zum Modell genutzt werden, um z. B. die
     * Klassennamen spezialisierter Klassen zu erweitern.
     * <P><I><B>Hinweis:</B> Da das K&uuml;rzel explizit dazu gedacht ist, sich im Code
     * niederzuschlagen, sei angeraten, keine Sonderzeichen zuzulassen.</I>
     *
     * @return Ein K&uuml;rzel zum Projekt, dem das Datenmodell zugeordnet ist.
     *
     * @changed OLI 30.09.2009 - Hinzugef&uuml;gt.
     */
    public String getProjectToken();

    /**
     * Liefert den Hersteller (z. B. zwecks Angabe im Copyrightvermerk) zum Modell.
     *
     * @return Der Hersteller (z. B. zwecks Angabe im Copyrightvermerk) zum Modell.
     */
    public String getVendor();

}
