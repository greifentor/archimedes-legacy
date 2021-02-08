/*
 * DynamicObject.java
 *
 * 05.04.2005
 *
 * (c) by O.Lieshoff
 *
 */

package corent.base.dynamic;


import java.io.*;
import java.util.*;

import logging.Logger;


/**
 * Diese Musterimplementierung des Dynamic-Interfaces bezieht die Daten zum Aufbau ihrer
 * Struktur aus einer Hashtable mit speziellen Descriptor-Objekten, die dem Konstruktor der
 * Klasse &uuml;bergeben werden mu&szlig;.
 *
 * <P><H3>Properties:</H3>
 * <TABLE BORDER=1>
 *     <TR VALIGN=TOP>
 *         <TH>Property</TH>
 *         <TH>Default</TH>
 *         <TH>Typ</TH>
 *         <TH>Beschreibung</TH>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.base.dynamic.debug</TD>
 *         <TD>false</TD>
 *         <TD>boolean</TD>
 *         <TD>
 *             Wenn diese Flagge gesetzt ist, produzieren die Methodenaufrufe der Klasse ein
 *             wenig Debug-Output.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.base.dynamic.[Classname].debug</TD>
 *         <TD>false</TD>
 *         <TD>boolean</TD>
 *         <TD>
 *             Hierbei handelt es sich um eine M&ouml;glichkeit, die Debug-Information 
 *             klassenspezifisch zu konfigurieren.
 *         </TD>
 *     </TR>
 * </TABLE>
 * <P>&nbsp;
 *
 * @author O.Lieshoff
 *
 * @changed OLI 21.05.2008 - Einbau von Debug-Ausgaben in die Methode <TT>set(String, Object,
 *         boolean)</TT>.
 * @changed OLI 16.02.2009 - Erweiterung um die Methode <TT>copyAttributes</TT>.
 * @changed OLI 09.06.2009 - Erweiterung um die Methode <TT>getAttributeName(String)</TT>. In
 *         Zusammenhang: Durchf&uuml;hrung von Formatanpassungen. Ersetzen der Methode
 *         <TT>getAttributenames()</TT> gegen die Methode <TT>getAttributeNames()</TT>.
 * @changed OLI 15.06.2009 - Anpassungen an log4j.
 *
 */
 
public class DynamicObject implements Dynamic, Serializable {

    /* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
    private static Logger log = Logger.getLogger(DynamicObject.class); 

    /** Hashtable mit den Attributen des Objektes. */
    protected Hashtable<String, Object> attribute = new Hashtable<String, Object>();

    /*
     * Die Referenz auf die Hashtable mit den Descriptoren f&uuml;r die Erstellung der
     * Attribute.
     */
    private Hashtable<String, AttributeDescriptor> had = null;

    /** Generiert ein DynamicObject ohne Attribute. */
    protected DynamicObject() {
        super();
    }

    /**
     * Erzeugt ein neues DynamicObject anhand des &uuml;bergebenen AttributeDescriptors. Dieser
     * Sollte aus Speicher- und Performanzgr&uuml;nden eine Referenz auf eine statische
     * Hashtable sein, die f&uuml;r alle Objekte mit dem selben (hier im wahrsten Sinne des
     * Wortes gemeint) Descriptor bem&uuml;ht werden sollte.
     *
     * @param had Referenz auf die Hashtable mit den AttributeDescriptoren zum Object.
     */
    public DynamicObject(Hashtable<String, AttributeDescriptor> had) {
        super();
        this.buildAttributes(had);
    }

    /**
     * Das &Uuml;berschreiben dieser Methode erlaubt eine Manipulation eines zu setzenden Wertes
     * im Rahmen der set-Methode.
     *
     * @param attr Der Name des Attributes, dessen Wert ge&auml;nder werden soll.
     * @param value Der neue Wert des Attributs zum angegebenen Namen.
     * @return Der eventuell ge&auml;nderte Wert.
     */
    public Object alterValueBeforeSet(String attr, Object value) {
        return value;
    }

    /**
     * Baut die Hashtable mit den Attributen aus dem &uuml;bergebenen Descriptor auf.
     *
     * @param had Eine Hashtable mit den Attribut-Descriptoren.
     */
    protected void buildAttributes(Hashtable<String, AttributeDescriptor> had) {
        this.had = had;
        this.attribute.clear();
        for (Iterator<String> it = this.had.keySet().iterator(); it.hasNext(); ) {
            String k = it.next();
            AttributeDescriptor ad = this.had.get(k);
            if (ad != null) {
                Object value = null;
                try {
                    value = ad.getAttributeClass().newInstance();
                } catch (IllegalAccessException iae) {
                    throw new IllegalArgumentException("DynamicObject.set(String, Object): "
                            + "Class " + ad.getAttributeClass().getName() + " is not valid!",
                            iae);
                } catch (InstantiationException ie) {
                    if (ad.getAttributeInitialValue() != null) {
                        value = ad.getAttributeInitialValue();
                    } else {
                        throw new IllegalArgumentException("DynamicObject.set(String, Object): "
                                + "Class " + ad.getAttributeClass().getName() 
                                + " is not valid!", ie);
                    }
                }
                this.attribute.put(k, value);
                if (ad.getAttributeInitialValue() != null) {
                    this.set(k, ad.getAttributeInitialValue());
                }
            }
        }
    }

    /**
     * Kopiert die Attribute des &uuml;bergebenen Objektes in das vorliegende Objekt.
     *
     * <P><I><B>Hinweis:</B> Die Methode kopiert lediglich Referenzen. Beim Kopieren eines nicht
     * unver&auml;nderlichen Objektes verweisen also beide DynamicObjects auf dasselbe Attribut!
     * </I>
     *
     * @param o Das DynamicObject, dessen Attributwerte in das vorliegenden Objektes kopiert
     *         werden sollen.
     * @throws AttributeNotFoundException Falls das Quellobjekt nicht wenigstens alle Attribute
     *         des vorliegenden Objektes besitzt.
     *
     * @changed OLI 17.02.2009 - Debugging: Attribute mit dem Wert <TT>null</TT> haben f&uuml;r
     *         das Werfen einer <TT>NullPointerException</TT> w&auml;hrend des Anfertigens der
     *         Sicherheitskopie gesorgt. Solche Werte werden nun &uuml;bergangen.
     *
     */
    public void copyAttributes(DynamicObject o) throws AttributeNotFoundException {
        Hashtable<String, Object> htsik = new Hashtable<String, Object>();
        List<String> attrnames = null;
        List<String> attrnames0 = null;
        String attrname = null;
        attrnames0 = this.getAttributenames();
        attrnames = o.getAttributenames();
        for (int i = 0, len = attrnames0.size(); i < len; i++) {
            if (this.get(attrnames0.get(i)) != null) {
                htsik.put(attrnames0.get(i), this.get(attrnames0.get(i)));
            }
        }
        for (int i = 0, len = attrnames0.size(); i < len; i++) {
            attrname = attrnames0.get(i);
            if (!attrnames.contains(attrname)) {
                for (int j = 0, lenj = attrnames0.size(); j < lenj; j++) {
                    this.set(attrnames0.get(j), htsik.get(attrnames0.get(j)));
                }
                throw new AttributeNotFoundException(attrname, this.getClass().getName());
            }
            this.set(attrname, o.get(attrname));
        }
    }

    /**
     * Liefert einen Attributnamen zum angegebenen String, falls einer vorhanden ist (der
     * Vergleich findet in Kleinschreibweise statt).
     *
     * @param s Der String zu dem der Attributname gefunden werden soll.
     * @return Ein Attributname zum angegebenen String, falls einer vorhanden ist (der Vergleich
     *         findet &uuml;ber Kleinschreibweise statt), bzw. <TT>null</TT>, falls kein
     *         passender Attributname zum angegebenen String gefunden werden konnte.
     * @throws NullPointerException Falls der &uuml;bergebene String ein Null-Pointer ist.
     *
     * @changed OLI 09.06.2009 - Hinzugef&uuml;gt.
     *
     * @precondition s != <TT>null</TT>
     *
     */
    public String getAttributeName(String s) {
        assert s != null : "the attribute name pattern is null";
        int i = 0;
        int leni = 0;
        List<String> lan = this.getAttributeNames();
        String an = null;
        s = s.toLowerCase();
        for (i = 0, leni = lan.size(); i < leni; i++) {
            an = lan.get(i);
            if (s.equals(an.toLowerCase())) {
                return an;
            }
        }
        return null;
    }

    @Deprecated
    /**
     * Liefert eine Liste mit den Attributnamen des Objekts.
     *
     * @return Liste mit den Attributnamen des Objekts.
     *
     * @deprecated OLI 09.06.2009 - Der Methodenname weist einen Schreibfehler auf. Statt dieser
     *         Methode ist die Methode <TT>getAttributeNames()</TT> zu nutzen.
     *
     * @see getAttributeNames()
     *
     */
    public List<String> getAttributenames() {
        /*
        Vector<String> vs = new Vector<String>();
        if (this.had != null) { 
            for (Iterator it = this.had.keySet().iterator(); it.hasNext(); ) {
                vs.addElement(it.next().toString());
            }
        }
        return vs;
        */
        return this.getAttributeNames();
    }

    /**
     * Liefert eine Liste mit den Attributnamen des Objekts.
     *
     * @return Liste mit den Attributnamen des Objekts.
     *
     * @changed OLI 09.06.2009 - Als Ersatz f&uuml;r die falschgeschriebene Variante erzeugt.
     *
     */
    public List<String> getAttributeNames() {
        Iterator it = null;
        Vector<String> vs = new Vector<String>();
        if (this.had != null) {
            for (it = this.had.keySet().iterator(); it.hasNext(); ) {
                vs.addElement(it.next().toString());
            }
        }
        return vs;
    }

    /**
     * Liefert eine Referenz auf die Hashtable mit den AttributDescriptoren des Objekts.
     *
     * @return Referenz auf die Hashtable mit den AttributeDescriptoren des Objekts.
     */
    protected Hashtable<String, AttributeDescriptor> getHAD() {
        return this.had;
    }

    /**
     * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r das angegebene Attribut ein.
     *
     * @param attr Der Name des Attributes, dessen Wert ge&auml;nder werden soll.
     * @param value Der neue Wert des Attributs zum angegebenen Namen.
     * @param change Wird diese Flagge gesetzt, wird vor dem Setzen des neuen Wertes die Methode
     *         alterValueBeforeSet(String, Object) ausgef&uuml;hrt.
     * @throws ClassCastException falls der &uuml;bergebene Wert nicht zum Typ des Attributs
     *         pa&szlig;t.
     * @throws IllegalArgumentException falls kein Attribut mit dem angegebenen Namen existiert.
     *
     * @changed OLI 21.05.2008 - Erweiterung um die Ausgabe der Debugginginformationen.
     * @changed OLI 02.06.2008 - Erweiterung um die klassenspezifische M&ouml;glichkeit, die
     *         Debug-Ausgabe zu konfigurieren. 
     *
     */
    public void set(String attr, Object value, boolean change) throws ClassCastException,
            IllegalArgumentException {
        log.debug("DynamicObject.set(\"" + attr + "\", " + value + ", " + change + ")");
        Object o = this.attribute.get(attr);
        log.debug("o=" + o);
        if ((o == null) && (!this.had.keySet().contains(attr))) {
            throw new IllegalArgumentException("DynamicObject.set(String, Object): Attribute "
                    + attr + " doesn't exists!");
        }
        AttributeDescriptor ad = this.had.get(attr);
        if (o == null) {
            if (ad != null) {
                try {
                    if (Number.class.isAssignableFrom(ad.getAttributeClass())) {
                        try {
                            java.lang.reflect.Constructor c = ad.getAttributeClass(
                                    ).getConstructor(String.class);
                            o = c.newInstance("0");
                        } catch (NoSuchMethodException nsme) {
                            nsme.printStackTrace();
                        } catch (java.lang.reflect.InvocationTargetException ite) {
                            ite.printStackTrace();
                        }
                    } else {
                        o = ad.getAttributeClass().newInstance();
                    }
                } catch (IllegalAccessException iae) {
                    throw new IllegalArgumentException("DynamicObject.set(String, Object): "
                            + "Class " + ad.getAttributeClass().getName() + " is not valid!",
                            iae);
                } catch (InstantiationException ie) {
                    if (ad.getAttributeInitialValue() != null) {
                        value = ad.getAttributeInitialValue();
                        ie.printStackTrace();
                        log.warn("Exception is caught. Value is set to default!");
                    } else {
                        throw new IllegalArgumentException("DynamicObject.set(String, Object): "
                                + "Class " + ad.getAttributeClass().getName()
                                + " is not valid!", ie);
                    }
                }
            } 
        }
        if (change) {
            value = this.alterValueBeforeSet(attr, value);
        }
        log.debug("value=" + value);
        if (ad == null) {
            throw new NullPointerException("DynamicObject.set(String, Object): "
                    + "AttributeDescriptor is null in Class " + this.getClass().getName()
                    + " for attribute " + attr + "(" + this.had + ")");
        }
        if ((value == null) || ad.getAttributeClass().isInstance(value)) {
            if (value == null) {
                log.debug("remove=" + attr);
                this.attribute.remove(attr);
            } else {
                log.debug("put=" + attr + ", " + value);
                this.attribute.put(attr, value);
                log.debug("get(" + attr + ")=" + value);
            }
            return;
        }
        log.debug("ERROR");
        throw new ClassCastException("DynamicObject.set(String, Object): Attribute "
                + attr + " (" + ad.getAttributeClass().getName()
                + ") is not compatible to class " + value.getClass().getName() + "(" + value
                + ") in subclass " + this.getClass().getName() + "!");
    }

    /**
     * Setzt die &uuml;bergebenen AttributeDescriptor-Hashtable als neue 
     * AttributeDescriptor-Hashtable f&uuml;r das Objekt ein.
     *
     * @param had Eine Hashtable mit den Attribut-Descriptoren.
     */
    public void setHAD(Hashtable<String, AttributeDescriptor> had) {
        this.had = had;
    }


    /* Implementierung des Interfaces Dynamic. */

    public Object get(String attr) throws IllegalArgumentException {
        Object o = this.attribute.get(attr);
        if (this.had == null) {
            throw new IllegalArgumentException("DynamicObject.get(String): AttributeDescriptor "
                    + "not initialized!");
        } else if ((o == null) && (!this.had.keySet().contains(attr))) {
            throw new IllegalArgumentException("DynamicObject.get(String): Attribute " + attr
                    + " doesn't exists!");
        }
        return o;
    }

    public void set(String attr, Object value) throws ClassCastException,
            IllegalArgumentException {
        set(attr, value, true);
    }

    public Class getType(String attr) throws ClassCastException, IllegalArgumentException {
        AttributeDescriptor ad = this.had.get(attr);
        if (ad == null) {
            throw new IllegalArgumentException("DynamicObject.getTypename(String): Attribute "
                    + attr + " doesn't exists!");
        }
        return ad.getAttributeClass();
    }

}
