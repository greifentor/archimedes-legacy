/*
 * COTimestampField.java
 *
 * 05.11.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import corent.dates.*;

import java.awt.*;


/**
 * Diese Spezialisierung des TimestampFields implementiert das Interface ContextOwner.
 *
 * @author O.Lieshoff
 *
 */
 
public class COTimestampField extends TimestampField implements ContextOwner {
    
    /* Flagge f&uuml;r die harte Abblendung. */
    private boolean strongdisabled = false;
    /* Der Context zur Komponente. */
    private String context = null;
    
    /**
     * Generiert ein TimestampField anhand der &uuml;bergebenen Parameter.
     *
     * @param container Die Komponente, in dem das TimestampField abgebildet wird.
     * @param tfcf Die TimestampFieldComponentFactory, aus der das TimestampField seine 
     *     Komponenten beziehen soll. 
     * @param timestamp Der anzuzeigende Timestamp.
     * @param c Der Contextname des TimestampFields.
     */
    public COTimestampField(Container container, TimestampFieldComponentFactory tfcf, 
            TimestampModel timestamp, String c) {
        super(container, tfcf, timestamp);
        this.setContext(c);
    }

    public void setEnabled(boolean b) {
        if (b && this.strongdisabled) {
            return;
        }
        super.setEnabled(b);
    }


    /* Implementierung des Interfaces ContextOwner. */
    
    public String getContext() {
        return this.context;
    }
    
    public boolean isStrongDisabled() {
        return this.strongdisabled;
    }
    
    public void setContext(String n) {
        this.context = n;
    }
    
    public synchronized void setStrongDisabled(boolean b) {
        this.setEnabled(false);
        this.strongdisabled = b;
    }

}
