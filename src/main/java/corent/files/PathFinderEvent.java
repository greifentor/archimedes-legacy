/**
 * PathFinderEvent.java
 *
 * 24.07.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.files;


/**
 * Diese Events dienen der Benachrichtigung von PathFinderListenern.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 24.07.2008 - Hinzugef&uuml;gt.
 *     <P>
 *
 */
 
public class PathFinderEvent {
    
    /** Typen zur Spezifizierung der Events. */
    public enum Type {FILECHANGED};
    
    public Type type = null;
    public String fn = null;
    
    /**
     * Generiert PathFinderEvent anhand der &uuml;bergebenen Daten.
     *
     * @param t Der Typ des Events.
     * @param fn Der Name des betroffenen Directories oder Files.
     */
    public PathFinderEvent(Type t, String fn) {
        super();
        this.setFilename(fn);
        this.setType(t);
    }
    
    public String getFilename() {
        return this.fn;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public void setFilename(String fn) {
        this.fn = fn;
    }
    
    public void setType(Type t) {
        this.type = t;
    }
    
}
