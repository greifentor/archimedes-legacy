/*
 * GlobalEventManager.java
 *
 * 26.02.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.event;


import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
 * Dieser EventManager h&ouml;rt alle AWTEvents einer Applikation ab und leitet diese zur 
 * Verarbeitung an die an ihn angebundenen GlobalListener weiter.
 *
 * @author O.Lieshoff
 *
 */
 
public class GlobalEventManager implements AWTEventListener {
    
    /* Die Inkarnation des GlobalEventManagers. */
    private static GlobalEventManager GEM = new GlobalEventManager();
    
    /* Die Liste der GlobalListener, die mit Events versorgt werden sollen. */
    private Vector<GlobalListener> listener = new Vector<GlobalListener>();
    
    private GlobalEventManager() {
        super();
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.RESERVED_ID_MAX);
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK);
    }
    
    private void addGlobalListener(GlobalListener gl) {
        this.listener.addElement(gl);
    }
    
    private void removeGlobalListener(GlobalListener gl) {
        this.listener.removeElement(gl);
    }

    private void fireGlobalEvent(EventObject e) {
        int i = 0;
        for (int j = this.listener.size(); i < j; i++) {
            try {
                GlobalListener globallistener = (GlobalListener) this.listener.elementAt(i);
                if (globallistener != null) {
                    if (globallistener.accept(e)) {
                        globallistener.eventDispatched(e);
                    }
                } else {
                    this.listener.removeElementAt(i--);
                }
            } catch (Exception ex) {
            }
        }
    }
    
    
    /* Implementierung des Interfaces AWTEventListener. */
    
    public void eventDispatched(AWTEvent e) {
        this.fireGlobalEvent(e);
    }

    
    /* Statische Methoden. */
    
    /**
     * Diese Methode f&uuml;gt den &uuml;bergebenen GlobalListener an die Liste der mit dem 
     * GlobalEventManager verbundenen Listener an.
     *
     * @param gl Der anzuf&uuml;gende Listener.
     */
    public static void AddGlobalListener(GlobalListener gl) {
        GEM.addGlobalListener(gl);
    }
    
    /**
     * Diese Methode l&ouml;scht den &uuml;bergebenen GlobalListener aus der Liste der mit dem 
     * GlobalEventManager verbundenen Listenern.
     *
     * @param gl Der zu l&ouml;schende Listener.
     */
    public static void RemoveGlobalListener(GlobalListener gl) {
        GEM.removeGlobalListener(gl);
    }
    
    /**
     * Leitet ein Event, falls es akzeptiert wird, an die Listener weiter.
     *
     * @param e Das weiterzuleitende Event.
     */
    public static void FireGlobalEvent(EventObject e) {
        GEM.fireGlobalEvent(e);
    }
    
}
