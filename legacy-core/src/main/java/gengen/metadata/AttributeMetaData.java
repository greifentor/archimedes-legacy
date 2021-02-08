/*
 * AttributeMetaData.java
 *
 * 06.09.2009
 *
 * (c) by O.Lieshoff
 *
 */

package gengen.metadata;


/**
 * Implementierungen dieses Interfaces repr&auml;sentieren Objekt-Attribute eines
 * Klassenmodells. Diese Attribute enthalten alle Informationen, die zur Generierung von
 * Businessobjekten und Persistenzschichten notwendig sind.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 06.09.2009 - Hinzugef&uuml;gt
 * @changed OLI 30.09.2009 - Erweiterung um die Spezifikationen der Methoden
 *         <TT>isPrimaryKeyMember()</TT> und <TT>isTechnicalAttribute()</TT>.
 * @changed OLI 18.11.2010 - Erweiterung um die Methoden <TT>getMaxLength()</TT> und
 *         <TT>isNotNull</TT>.
 * @changed OLI 07.01.2011 - Erweiterung um die Methode <TT>getReferencedClass()</TT>.
 */

public interface AttributeMetaData {

    /**
     * Liefert einen Initialwert f&uuml;r das Attribut.
     *
     * @return Ein Initialwert f&uuml;r das Attribut.
     */
    public String getDefaultValue();

    /**
     * Liefert den Namen des Java-Typs zum Attribut.
     *
     * @return Der Name des Java-Typs zum Attribut.
     */
    public String getJavaType();

    /**
     * Liefert die maximale L&auml;nge in Zeichen, die der Inhalt des Attributs annehmen darf.
     *
     * @return Die maximale L&auml;nge in Zeichen, die der Inhalt des Attributs annehmen darf,
     *         bzw. <TT>0</TT>, wenn die Zeichenanzahl unbegrenzt ist.
     *
     * @changed OLI 18.11.2010 - Hinzugef&uuml;gt.
     */
    public int getMaxLength();

    /**
     * Liefert den Namen des Attributs.
     *
     * @return Der Name des Attributs.
     */
    public String getName();

    /**
     * Liefert die Klasse, die durch das Attribut referenziert wird, falls es eine solche gibt.
     *
     * @since 1.8.1
     *
     * @return Die Klasse, die durch das Attribut referenziert wird bzw. <TT>null</TT>, falls
     *         keine Klasse durch das Attribut referenziert wird.
     *
     * @changed OLI 07.01.2011 - Hinzugef&uuml;gt.
     */
    public ClassMetaData getReferencedClass();

    /**
     * Pr&uuml;ft, ob das Attribut keinen <TT>null</TT>-Wert annehmen darf.
     *
     * @return <TT>true</TT>, wenn das Attribut keinen <TT>null</TT>-Wert annehmen darf, sonst
     *         <TT>false</TT>.
     *
     * @changed OLI 18.11.2010 - Hinzugef&uuml;gt.
     */
    public boolean isNotNull();

    /**
     * Pr&uuml;ft, ob das Attribut ein Teil des Prim&auml;rschl&uuml;ssels ist.
     *
     * @return <TT>true</TT>, falls das Attribut Teil des Prim&auml;rschl&uuml;ssels ist,
     *         <TT>false</TT> sonst.
     *
     * @changed OLI 30.09.2009 - Hinzugef&uuml;gt.
     */
    public boolean isPrimaryKeyMember();

    /**
     * Pr&uuml;ft, ob das Attribut technischer Natur ist.
     *
     * @return <TT>true</TT>, falls das Attribut einen rein technischen Charakter hat.
     *
     * @changed OLI 30.09.2009 - Hinzugef&uuml;gt.
     */
    public boolean isTechnicalAttribute();

}
