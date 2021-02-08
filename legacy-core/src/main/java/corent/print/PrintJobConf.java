/*
 * PrintJobConf.java
 *
 * 13.08.2003
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.print;


import java.util.*;

import corent.base.*;
import corent.djinn.*;


/**
 * Diese Klasse enth&auml;lt die Daten, die innerhalb des Dialoges zur 
 * Druckauftragkonfiguration ge&auml;ndert werden k&ouml;nnen.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 08.09.2008 - Erweiterung um das Attribut A3.
 *     <P>
 * 
 */

public class PrintJobConf implements Attributed, Editable {

    /** Bezeichner f&uuml;r den Zugriff auf das Attribut Printername. */
    public static final int ID_PRINTERNAME = 0;
    /** Bezeichner f&uuml;r den Zugriff auf das Attribut Copies. */
    public static final int ID_COPIES = 1;
    /** Bezeichner f&uuml;r den Zugriff auf das Attribut A3. */
    public static final int ID_A3 = 2;
    
    /** 
     * Name des Druckers, auf dem der Auftrag ausgef&uuml;hrt werden soll <I>(Default null)</I>.
     */
    public String printername = null;
    /** Anzahl der Kopien des PrintJobs <I>(Default 1)</I>. */
    public int copies = 1;
    
    /** Die Flagge zum Initiieren eines A3-Drucks. */
    private boolean a3 = false; 
    /* Liste mit den verf&uuml;gbaren Druckern. */
    private Vector<String> printer = new Vector<String>();
    
    /**
     * Generiert ein neues Objekt anhand der &uuml;bergebenen Parameter. 
     *
     * @param printername Der Name des Druckers, auf den der Auftrag gedruckt werden soll.
     * @param copies Die Anzahl der Kopien, die beim n&auml;chsten Ausdruck angefertigt werden 
     *     sollen.
     */
    public PrintJobConf(String printername, int copies) {
        super();
        this.setPrintername(printername);
        this.setCopies(copies);
        this.printer.addElement(printername);
    }
    
    /**
     * Generiert ein neues Objekt anhand der &uuml;bergebenen Parameter. 
     *
     * @param printername Der Name des Druckers, auf den der Auftrag gedruckt werden soll.
     * @param copies Die Anzahl der Kopien, die beim n&auml;chsten Ausdruck angefertigt werden 
     *     sollen.
     * @param printer Eine Liste mit den Namen der verf&uuml;gbaren Drucker bzw. Printerqueues.
     */
    public PrintJobConf(String printername, int copies, Vector<String> printer) {
        super();
        this.setPrintername(printername);
        this.setCopies(copies);
        this.printer = printer;
    }
    
    /**
     * Generiert ein neues Objekt anhand der &uuml;bergebenen Parameter. 
     *
     * @param printername Der Name des Druckers, auf den der Auftrag gedruckt werden soll.
     * @param copies Die Anzahl der Kopien, die beim n&auml;chsten Ausdruck angefertigt werden 
     *     sollen.
     * @param printer Eine Liste mit den Namen der verf&uuml;gbaren Drucker bzw. Printerqueues.
     * @param a3 Diese Flagge mu&szlig; gesetzt sein, wenn der Druck im DIN-A3-Format erfolgen
     *     soll.
     */
    public PrintJobConf(String printername, int copies, Vector<String> printer, boolean a3) {
        super();
        this.setA3(a3);
        this.setCopies(copies);
        this.setPrintername(printername);
        this.printer = printer;
    }
    
    /**
     * Accessor f&uuml;r die Eigenschaft A3
     *
     * @return Der Wert der Eigenschaft A3
     */
    public boolean isA3() {
        return this.a3;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft A3
     *
     * @param a3 Der neue Wert f&uuml;r die Eigenschaft A3.
     */
    public void setA3(boolean a3) {
        this.a3 = a3;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Copies
     *
     * @return Der Wert der Eigenschaft Copies
     */
    public int getCopies() {
        return this.copies;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Copies
     *
     * @param copies Der neue Wert f&uuml;r die Eigenschaft Copies.
     */
    public void setCopies(int copies) {
        this.copies = copies;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Printername
     *
     * @return Der Wert der Eigenschaft Printername
     */
    public String getPrintername() {
        return this.printername;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Printername
     *
     * @param printername Der neue Wert f&uuml;r die Eigenschaft Printername.
     */
    public void setPrintername(String printername) {
        if (printername == null) {
            printername = "";
        }
        this.printername = printername;
    }


    /* Implementierung des Interfaces Attributed. */

    public Object get(int id) throws IllegalArgumentException {
        switch (id) {
        case ID_PRINTERNAME:
            return this.getPrintername();
        case ID_COPIES:
            return new Integer(this.getCopies());
        case ID_A3:
            return new Boolean(this.isA3());
        }
        throw new IllegalArgumentException(this.getClass() + ": Es existiert kein Attribut zu " 
                + id + "!");
    }
    
    public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
        switch (id) {
        case ID_PRINTERNAME:
            this.setPrintername((String) value);
            return;
        case ID_COPIES:
            this.setCopies(((Integer) value).intValue());
            return;
        case ID_A3:
            this.setA3(((Boolean) value).booleanValue());
            return;
        }
        throw new IllegalArgumentException(this.getClass() + ": Es existiert kein Attribut zu " 
                + id + "!");
    }


    /* Implementierung des Interfaces Editable. */    

    public EditorDescriptorList getEditorDescriptorList() {
        DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
        DefaultComponentFactory dcflpt = new DefaultComponentFactory(this.printer);
        DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
        DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_PRINTERNAME, dlf, dcflpt, 
                "Printer", 'P', null, "Der Drucker, an den der Druckauftrag gesendet werden "
                + "soll"));
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_COPIES, dlf, dcf, "Kopien", 'K',
                null, "Die Anzahl der zu druckenden Kopien"));
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_A3, dlf, dcf, "A3", 'A', null,
                "Setzen Sie diese Flagge, um einen Druck im DIN-A3-Format zu initiieren."));
        return dedl;
    }
    
    /** Generiert ein leeres Objekt der selben Klasse. */
    public Object createObject() {
        return new PrintJobConf("", 0);
    }
    
    public Object createObject(Object blueprint) throws ClassCastException {
        PrintJobConf pjc = (PrintJobConf) blueprint;
        return new PrintJobConf(pjc.getPrintername(), pjc.getCopies());
    }
    
}
