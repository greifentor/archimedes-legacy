/*
 * Traceable.java
 *
 * 19.10.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Objekte, die dieses Interface implementieren, werden durch die SelectionEditorDjinnDBFs bei 
 * bestimmten Ereignissen benachrichtigt, bei denen &Auml;nderungen am Inhalt des betroffenen
 * Objekt vorgenommen worden sind.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface Traceable {
    
    /** Diese Methode wird aufgerufen, nachdem das Traceable ge&auml;ndert worden ist. */
    public void traceChanged();
    
    /** Diese Methode wird aufgerufen, nachdem das Traceable generiert worden ist. */
    public void traceCreated();
    
    /** 
     * Diese Methode wird aufgerufen, nachdem das Traceable gel&ouml;scht oder deaktiviert 
     * worden ist. 
     */
    public void traceDeleted();
    
    /** 
     * Diese Methode wird aufgerufen, nachdem das Traceable durch Duplikation entstanden ist. 
     */
    public void traceDuplicated();
    
}
