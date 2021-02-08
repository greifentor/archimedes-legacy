/*
 * JasperReportableCSV.java
 *
 * 11.12.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.print;


/**
 * Dieses Interface definiert das notwendige Verhalten von JasperReportable-Objekten, die ihre
 * Daten aus CSV-Dateien gewinnen.
 * <P>&Uuml;ber die Methode <TT>getCSVContent(int)</TT> kann der Inhalt einer CSV-Datei an den 
 * DBFactoryController &uuml;bergeben werden, aus der der Report seine Daten bezieht. Auf diese
 * Weise kann ein Bericht mit Daten versorgt werden, ohne auf eine Datenbank zugreifen zu 
 * m&uuml;ssen. Die Zeilen des CSV-Contents m&uuml;ssen mit <TT>\n</TT> voneinander getrennt 
 * sein. Der Server sollte diese Zeilentrenner betriebssystemkonform umsetzen. Ebenso sollte der
 * CSV-Content in eine tempor&auml;re Tabelle gespeichert werden.
 * 
 * @author O.Lieshoff
 *
 */
 
public interface JasperReportableCSV extends JasperReportable {
    
    /**
     * M&ouml;gliche dynamische Parameter zum Report.
     *
     * @param reportnumber Die Nummer des Reports, der zum implementierenden Objekt generiert 
     *     werden soll.
     * @return Der Inhalt der CSV-Datei, aus der der Report seine Daten beziehen soll, falls der
     *     Bericht eine solche Datenquelle erfordert. Sonst <TT>null</TT>
     */
    public String getCSVContent(int reportnumber);

    /**
     * Liefert die Namen der Felder der CSV-Datei als String-Array.
     *
     * @return Ein String-Array mit den Feldnamen, &uuml;ber die die Spalten in der CSV-Datei im
     *     Bericht angesprochen werden sollen.
     */
    public String[] getColumnNames();
    
    /**
     * Liefert den Datensatztrenner. Hier wird ein String genutzt, um die Sequenzen wie "\n\r"
     * oder &auml;hnliche m&ouml;glich zu machen.
     *
     * @return Das Trennzeichen, mit dem Datens&auml;tze voneinander getrennt werden.
     */
    public String getRecordDelimiter();
    
    /**
     * Liefert den Datenfeldtrenner.
     *
     * @return Das Trennzeichen, mit dem Datenfelder voneinander getrennt werden.
     */
    public char getFieldDelimiter();
    
}
