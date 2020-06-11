/*
 * CodeGenerator.java
 *
 * 07.09.2009
 *
 * (c) by O.Lieshoff
 *
 */

package gengen.generator;


import gengen.metadata.*;


/**
 * Dieses Interface definiert den notwendigen Funktionsumfang f&uuml;r einen Codegenerator, wie
 * er beispielsweise durch den GenGen erzeugt wird.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 07.09.2009 - Hinzugef&uuml;gt
 * @changed OLI 09.09.2009 - Erweiterung um die Signatur f&uuml;r die Methode
 *         <TT>generate(ModelMetaData)</TT>
 * @changed OLI 11.09.2009 - Erweiterung um die Signatir der Methode
 *         <TT>getCompleteClassName(ClassMetaData)</TT>.
 */

public interface CodeGenerator {

    /**
     * Generiert ein Codefragment zu den angegebenen Klassenmetadaten.
     *
     * @param cmd Die Klassenmetadaten zu denen der Codegenerator ein Codefragment generieren
     *         soll.
     * @return Das Codefragment, das durch den Codegenerator erzeugt worden ist.
     * @throws Exception Falls beim Generieren der Klasse ein Fehler auftritt. Die
     *         tats&auml;chlich geworfene Exception sollte zu dem aufgetretenen Problem passen.
     * @throws NullPointerException Falls die Klassenmetadaten als <TT>null</TT>-Referenzen
     *         &uuml;bergeben werden.
     */
    public String generate(ClassMetaData cmd) throws Exception, NullPointerException;

    /**
     * Generiert Codeartefakte zum gesamten Modell. Im Regelfall wird die Implementierung dieser
     * Methode die Methode <TT>generate(ClassMetaData)</TT> f&uuml;r jede Klasse aufrufen.
     *
     * @param mmd Die Metadaten zum Klassenmodell.
     * @throws Exception Falls beim Generieren des Codes ein Fehler auftritt. Die
     *         tats&auml;chlich geworfene Exception sollte zu dem aufgetretenen Problem passen.
     * @throws NullPointerException Falls die Modellmetadaten als <TT>null</TT>-Referenzen
     *         &uuml;bergeben werden.
     *
     * @changed OLI 09.09.2009 - Hinzugef&uuml;gt.
     */
    public void generate(ModelMetaData mmd) throws Exception, NullPointerException;

    /**
     * Liefert den vollst&auml;ndigen Klassennamen, der vom CodeGenerator z. B. zur
     * Dateinamensbildung genutzt wird.
     * <P>Hiermit ist <U>nicht</U> der qualifizierte Klassenname gemeint, sondern vielmehr z. B.
     * ein mit Suffix oder Pr&auml;fix versehener Klassenname (wie z. B.
     * &lt;KLASSENNAME&gt;Uschebti).
     *
     * @param cmd Die Klassenmetadaten, zu denen der vollst&auml;ndige Klassenname gebildet
     *         werden soll.
     * @return Der vollst&auml;ndigen, durch den CodeGenerator genutzte, Klassennamen.
     * @throws Exception Falls beim Generieren des Codes ein Fehler auftritt. Die
     *         tats&auml;chlich geworfene Exception sollte zu dem aufgetretenen Problem passen.
     * @throws NullPointerException Falls die Klassenmetadaten als <TT>null</TT>-Referenzen
     *         &uuml;bergeben werden.
     *
     * @changed OLI 11.09.2009 - Hinzugef&uuml;gt.
     */
    public String getCompleteClassName(ClassMetaData cmd);

    /**
     * Liefert einen relativen Packagename f&uuml;r die erzeugten Klassen.
     *
     * @return Ein relativer Packagenamen f&uuml;r die erzeugten Klassen.
     */
    public String getPackageName();

}
