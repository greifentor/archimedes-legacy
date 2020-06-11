/*
 * LongPTimestampSelection.java
 *
 * 11.02.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.dates;


/**
 * Diese Klasse erweitert den LongPTimestamp und &uuml;berschreibt dessen toString()-Methode.
 * Sie dient zur Anzeige von LongPTimestamps in Auswahldialogen (hier ohne Millisekunden).
 *
 * @author
 *     O.Lieshoff
 *
 * @changed
 *     OLI 11.02.2008 - Hinzugef&uuml;gt.
 *     <P>OLI 13.02.2008 - Erweiterung um den Accessor und den Mutator f&uuml;r das Attribut
 *             <TT>additionalinfo</TT>.
 *
 */
 
public class LongPTimestampSelection extends LongPTimestamp {
    
    private String additionalinfo = "-";
    
    public LongPTimestampSelection(long l, String addinfo) throws java.text.ParseException {
        super(l);
        this.additionalinfo = addinfo;
    }
    
    public String toString() {
        return super.toString().substring(0, 19) + " - " + this.additionalinfo;
    }
    
    public String getAdditionalInfo() {
        return this.additionalinfo;
    }
    
    public void setAdditionalInfo(String addinfo) {
        this.additionalinfo = addinfo;
    }
    
}
