/*
 * PaperFormat.java
 *
 * 13.08.2003
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.print;


import java.awt.*;

import corent.base.*;


/**
 * Mit Hilfe dieser Klasse werden Papierformate dargestellt.
 * <BR><HR>
 *
 * @author O.Lieshoff
 *
 */

public class PaperFormat {

    /* Die H&ouml;he des Papiers in cm <I>(Default 0.0d)</I>. */
    private double height = 0.0d;
    /* Die Breite des Papiers in cm <I>(Default 0.0d)</I>. */
    private double width = 0.0d;
    /* Name des Papiers <I>(Default "")</I>. */
    private String name = "";
    
    /** Generiert ein Paper-Objekt mit den Defaulteinstellungen. */
    public PaperFormat() {
        super();
    }
    
    /** 
     * Generiert ein PageFormat-Objekt anhand der &uuml;bergebenen Parameter.
     *
     * @param w Die Breite des Papierformates.
     * @param h Die H&ouml;he des Papierformates.
     * @param n Der Name des Papierformates.
     */
    public PaperFormat(double w, double h, String n) {
        super();
        this.setWidth(w);
        this.setHeight(h);
        this.setName(n);
    }
    
    /**
     * Accessor f&uuml;r die Eigenschaft Height
     *
     * @return Der Wert der Eigenschaft Height
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Height
     *
     * @param height Der neue Wert f&uuml;r die Eigenschaft Height.
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Width
     *
     * @return Der Wert der Eigenschaft Width
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Width
     *
     * @param width Der neue Wert f&uuml;r die Eigenschaft Width.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Name
     *
     * @return Der Wert der Eigenschaft Name
     */
    public String getName() {
        return new String(this.name);
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Name
     *
     * @param name Der neue Wert f&uuml;r die Eigenschaft Name.
     */
    public void setName(String name) {
        if (name == null) {
            name = "";
        }
        this.name = name;
    }

    /**
     * &Uuml;berschreibung der Standard-<TT>equals(Object)</TT>-Methode.
     *
     * @return <TT>true</TT>, wenn die Inhalte des vorliegenden und des zu vergleichenden
     *     Objectes gleich sind.
     */
    public boolean equals(Object o) {
        if (!(o instanceof PaperFormat)) {
            return false;
        }
        PaperFormat pf = (PaperFormat) o;
        return this.getName().equals(pf.getName()) && ((int) (this.getHeight() * 1000) == (int)
                (pf.getHeight() * 1000)) && ((int) (this.getWidth() * 1000) == (int) (
                pf.getWidth() * 1000));
    }
    
    /** @return Aussagekr&auml;ftige Stringrepr&auml;sentation des Objektes. */
    public String toString() {
        return this.getName() + " [" + StrUtil.FixedDoubleToStr(this.getWidth(), 2) + "cm x " 
                + StrUtil.FixedDoubleToStr(this.getHeight(), 2) + "cm]";
    }
    
    /** 
     * Diese Methode liefert zu einem &uuml;bergebenen PageAttribute.MediaType ein 
     * PaperFormat-Objekt.
     *
     * @param pamt Das PageAttribute.MediaType, zu dem das PaperFormat ermittelt werden soll.
     * @return Das zum pamt passende PaperFormat-Objekt, bzw. null, wenn kein entsprechendes 
     *     PaperFormat gefunden werden konnte.
     */
    public static PaperFormat GetFormat(PageAttributes.MediaType pamt) {
        if (pamt == PageAttributes.MediaType.A0) {
            return new PaperFormat(84.1, 118.9, "DIN A0");
        } else if (pamt == PageAttributes.MediaType.A1) {
            return new PaperFormat(59.4, 84.1, "DIN A1");
        } else if (pamt == PageAttributes.MediaType.A2) {
            return new PaperFormat(42.0, 59.4, "DIN A2");
        } else if (pamt == PageAttributes.MediaType.A3) {
            return new PaperFormat(29.7, 42.0, "DIN A3");
        } else if (pamt == PageAttributes.MediaType.A4) {
            return new PaperFormat(21.0, 29.7, "DIN A4");
        } else if (pamt == PageAttributes.MediaType.A5) {
            return new PaperFormat(14.8, 21.0, "DIN A5");
        } else if (pamt == PageAttributes.MediaType.A6) {
            return new PaperFormat(10.5, 14.8, "DIN A6");
        } else if (pamt == PageAttributes.MediaType.A7) {
            return new PaperFormat(7.4, 10.5, "DIN A7");
        } else if (pamt == PageAttributes.MediaType.A8) {
            return new PaperFormat(5.2, 7.4, "DIN A8");
        } else if (pamt == PageAttributes.MediaType.A9) {
            return new PaperFormat(3.7, 5.2, "DIN A9");
        } else if (pamt == PageAttributes.MediaType.A10) {
            return new PaperFormat(2.6, 3.7, "DIN A10");
        }
        // to be continued
        return null;
    }

}
