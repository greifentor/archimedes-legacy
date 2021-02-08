/*
 * ArchimedesTracingApplication.java
 *
 * 19.10.2005
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


/**
 * Dieses Interface erweitert die Funktionalit&auml;t einer um die F&auml;higkeit auf 
 * Tracing-Ereignisse zentral zu reagieren.<BR>
 * <HR>
 *
 * @author ollie
 *
 */
 
public interface ArchimedesTracingApplication extends ArchimedesApplication {
 
    /** Der Enum-Typ zur Anzeige der zu tracenden Aktion. */
    public enum TraceType {CHANGED, CREATED, DELETED, DUPLICATED};
    
    /** 
     * Diese Methode f&uuml;hrt die notwendigen Schritte zum tracen der angegebenen Aktion mit
     * dem &uuml;bergebenen Objekt aus.
     *
     * @param obj Das Objekt, das getraced werden soll.
     * @param tt Der TraceTyp zur Definition der zu tracenden Aktion.
     */
    public void traceEvent(Object obj, TraceType tt);
    
}
