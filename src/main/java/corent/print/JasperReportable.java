/*
 * JasperReportable.java
 *
 * 17.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.print;


import corent.db.xs.*;

import java.util.*;


/**
 * Dieses Interface definiert das notwendige Verhalten von JasperReportable-Objekten.
 * <P>Jedes JasperReportable kann mehrere Reportparametrierungen f&uuml;r verschiedene Reports
 * implementieren. Die Auswahl wird &uuml;ber den Parameter <TT>reportnumber</TT> getroffen. In
 * den Implementierungen des Interfaces kann dann auf diesen Parameter reagiert werden.
 * 
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 04.11.2007 - Erweiterung um die Methode <TT>isSaveBeforePrintingRequired()</TT>. Mir
 *             ist klar, da&szlig; das ein wenig Stre&szlig; gibt; aber auf diese Weise sind die
 *             Anpassungen wenigstens zwingend.
 *     <P>OLI 26.03.2008 - Erweiterung um die Methode <TT>getStandardReportNumbers()</TT>.
 *
 */
 
public interface JasperReportable {
    
    /**
     * Liefert den Namen der jasper-Datei aus der der Report erzeugt werden soll.
     *
     * @param reportnumber Die Nummer des Reports, der zum implementierenden Objekt generiert 
     *     werden soll.
     * @return Der Name der jasper-Datei zu der der Report erzeugt werden soll. 
     */
    public String getJasperReportFilename(int reportnumber);
    
    /**
     * Der Name der PDF-Datei, falls der Report in eine solche exportiert werden soll.
     *
     * @param reportnumber Die Nummer des Reports, der zum implementierenden Objekt generiert 
     *     werden soll.
     * @return Der Name einer PDF-Datei, in die der Report exportiert werden soll, bzw. 
     *     <TT>null</TT>, wenn kein Export stattfinden soll.
     */
    public String getPDFExportFilename(int reportnumber);
    
    /**
     * M&ouml;gliche dynamische Parameter zum Report.
     *
     * @param reportnumber Die Nummer des Reports, der zum implementierenden Objekt generiert 
     *     werden soll.
     * @param dfc Eine Referenz auf den aufrufenden DBFactoryController (z. B. zur weiteren 
     *     Versorgung des Objektes mit Daten).
     * @return Eine HashMap mit den Parametern zum Report.
     */
    public HashMap getReportParams(int reportnumber, DBFactoryController dfc);
    
    /**
     * Liefert ein Array mit den Reportnumbers der Reports, die im Standardfall gedruckt werden
     * sollen (z. B. im InternalEditorDjinn-Umfeld).
     *
     * @return Ein Array (int) mit den Nummern der im Standardfall zu druckenden Reports.
     *
     * @changed
     *     OLI 26.03.2008 - Hinzugef&uuml;gt.
     *
     */
    public int[] getStandardReportNumbers();

    /**
     * Diese Methode beantwortet die Frage, ob ein Speichern des Objektes (z. B. in die 
     * Datenbank) vor dem Drucken notwendig ist.
     *
     * @return <TT>true</TT>, wenn vor dem Drucken gespeichert werden mu&szlig; bzw. 
     *     <TT>false</TT>, wenn dies nicht der Fall ist.
     *
     * @changed
     *     OLI 04.11.2007 - Hinzugef&uuml;gt.
     *
     */
    public boolean isSaveBeforePrintingRequired();
    
}
