/*
 * ClassMetaData.java
 *
 * 06.09.2009
 *
 * (c) by O.Lieshoff
 *
 */

package gengen.metadata;


import java.util.List;


/**
 * Implementierungen dieses Interfaces repr&auml;sentieren Klassen des Klassenmodells.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 06.09.2009 - Hinzugef&uuml;gt
 * @changed OLI 18.09.2009 - Erweiterung um die Spezifikation der Methode
 *         <CODE>isReadyToCode()</CODE>.
 * @changed OLI 28.09.2009 - Erweiterung um die Spezifikation der Methode
 *         <CODE>getPackageName()</CODE>.
 * @changed OLI 06.10.2009 - Erweiterung um die Spezifikation der Methode
 *         <CODE>isOfStereotype(String)</CODE>.
 * @changed OLI 04.06.2010 - Erweiterung um die Spezifikation der Methode
 *         <CODE>getSelectionViewMetaData()</CODE>.
 * @changed OLI 19.10.2010 - Erweiterung um die Spezifikation der Methode
 *         <CODE>isFirstGenerationDone()</CODE>.
 * @changed OLI 20.12.2011 - Erweiterung um die Spezifikation der Methode
 *         <CODE>getAttribute(String)</CODE>.
 */

public interface ClassMetaData {

    /**
     * Liefert das Attribut an der i-ten Position der Attributliste der Klasse.
     *
     * @param i Die Position des Attributs innerhalb der Attributliste der Klasse, das
     *         zur&uuml;ckgeliefert werden soll.
     * @return Das Attribut an der n-ten Position der Attributliste der Klasse.
     * @throws IndexOutOfBoundsException Falls die Position i nicht im Rahmen der Liste der
     *         Attribute der Klasse liegt.
     */
    public AttributeMetaData getAttribute(int i) throws IndexOutOfBoundsException;

    /**
     * Liefert das Attribut zum angegebenen Namen.
     *
     * @param name Der Name des Attributs, dessen Referenz ermittelt werden soll.
     * @return Das Attribut, zum angebenen Namen oder <CODE>null</CODE>, falls es kein Attribut
     *         zum angegebenen Namen gibt.
     * @throws IllegalArgumentException Falls der Name, zu dem dsa Attribut ermittelt werden
     *         soll leer oder als <CODE>null</CODE>-Referenz &uuml;bergeben wird.
     */
    public AttributeMetaData getAttribute(String name) throws IllegalArgumentException;

    /**
     * Liefert eine Liste der Attribute der Klasse.
     *
     * @return Eine Liste der Attribute der Klasse.
     */
    public List<AttributeMetaData> getAttributes();

    /**
     * Liefert den Autor der Klasse.
     *
     * @return Der Autor der Klasse.
     */
    public String getAuthor();

    /**
     * Liefert eine Referenz auf das Klassenmodell, zu dem die Klasse geh&ouml;rt.
     *
     * @return Eine Referenz auf das Klassenmodell, zu dem die Klasse geh&ouml;rt.
     */
    public ModelMetaData getModel();

    /**
     * Liefert den Namen der Klasse.
     *
     * @return Der Name der Klasse.
     */
    public String getName();

    /**
     * Liefert den Namen des Packages, in dem sich den Klasse befindet.
     *
     * @return Der Name des  Packages, in dem sich den Klasse befindet.
     */
    public String getPackageName();

    /**
     * Liefert die durch das Modell festgelegten Metadaten zu den Auswahlsichten..
     *
     * @return Eine Liste der durch das Modell festgelegten Metadaten zu den Auswahlsichten.
     *
     * @changed OLI 04.06.2010 - Hinzugef&uuml;t.
     */
    public List<SelectionViewMetaData> getSelectionViewMetaData();

    /**
     * Pr&uuml;ft, ob eine erste, initiale Generierung zur Klasse erfolgt ist.
     *
     * @return <CODE>true</CODE>, wenn die Klasse bereits ihre initiale Kodierung hinter sich
     *         hat, sonst <CODE>false</CODE>.
     *
     * @changed OLI 19.10.2010 - Hinzugef&uuml;gt.
     */
    public boolean isFirstGenerationDone();

    /**
     * Pr&uuml;ft, ob die Klasse von der angegebenen Stereotype ist.
     *
     * @param sn Der Name der Stereotype, f&uuml;r die die Pr&uuml;fung stattfinden soll.
     * @return <CODE>true</CODE>, falls die Zugrh&ouml;rigkeit gegeben ist,
     *         <BR><CODE>false</CODE> sonst.
     * @throws NullPointerException Falls der Stereotypenname als <CODE>null</CODE>-Referenz
     *         &uuml;bergeben wird.
     * @precondition sn != <CODE>null</CODE>.
     *
     * @changed OLI 06.10.2009 - Hinzugef&uuml;gt.
     */
	public boolean isOfStereotype(String sn) throws NullPointerException; // NO_UCD

    /**
     * Pr&uuml;ft, ob die Klasse kodiert werden soll.
     *
     * @return <CODE>true</CODE>, wenn die Klasse kodiert werden soll, sonst <CODE>false</CODE>.
     *
     * @changed OLI 18.09.2009 - Hinzugef&uuml;gt.
     */
    public boolean isReadyToCode();

}