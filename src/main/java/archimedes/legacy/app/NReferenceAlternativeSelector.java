/*
 * NReferenceAlternativeSelector.java
 *
 * 02.10.2006
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


/**
 * Mit Hilfe dieses Interfaces kann ein Objekt bef&auml;higt werden, eine alternative Selektion
 * in einem NReferenceModel-Panel zuzulassen.
 *
 * @author ollie
 *
 */
 
public interface NReferenceAlternativeSelector {
    
    /**
     * Die alternative Klasse, die zur Selektion &uuml;ber den Einf&uuml;gen-Button des Panel
     * genutzt werden soll.
     *
     * @param nr Die Kennnummer des NReferenzModels, welches die Anfrage startet.
     * @return Die beschriebene Klasse bzw. <TT>null</TT>, wenn die Defaulteinstellung gelten 
     *     soll.
     */
    public Class getAlternativeSelectionClass(int nr);
    
    /**
     * Diese Methode wird nach der Auswahl durch einen alternativen Selektor auf alle 
     * ausgew&auml;hlten Objekte der Reihe nach angewandt. Das modifizierte Objekt mu&szlig;
     * als R&uuml;ckgabewert der Methode &uuml;bergeben werden. Soll keine Modifikation 
     * stattfinden, sollte die Methode wie folgt implementiert werden:
     * <PRE>
     * public Object doAfterAlternativeSelection(Object slctd, int nr, ListOwner lo) {
     *     return slctd;
     * }
     * </PRE>
     *
     * @param nr Die Kennnummer des NReferenzModels, welches die Anfrage startet.
     * @param slctd Das ausgew&auml;hlte Objekt.
     * @param lo Der ListOwner, f&uuml;r den das Objekt selektiert worden ist. Er enth&auml;t 
     *     die Liste, in die das selektierte Objekt eingef&uuml;gt werden soll. Die fehlende
     *     Typisierung auf das ListOwner-Interface ist beabsichtigt, da es Stellen gibt, an 
     *     denen NReferenceAlternativeSelector durch andere Klassen genutzt wird.
     * @return Das Objekt, welches als Resultat der Methode genutzt werden soll. Hierdurch
     *     kann das Auswahlresultat 
     */
    public Object doAfterAlternativeSelection(Object slctd, int nr, Object lo);
    
}
