/*
 * DefaultCommentUdschebti.java
 *
 * AUTOMATISCH VON ARCHIMEDES GENERIERT!!!
 *
 * 06.10.2007
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.udschebtis;


import archimedes.legacy.app.*;


/**
 * Diese Tabelle enth&auml;lt Kommentare, die in der Beschreibung auf Tabellenspalten angewandt werden sollen, auf deren Namen das angegebene Muster pa&szlig;t.
 *
 * @author ollie
 *
 */

public class DefaultCommentUdschebti extends ApplicationObject {


    private DefaultCommentUdschebti() {
        super(null, "");
    }

    /**
     * Generiert eine Instanz der Klasse mit Defaultwerten.
     *
     * @param adf Die ArchimedesDescriptorFactory, aus der die Instanz ihre Konfiguration 
     *     beziehen soll.
     * @param tn Der Name der Tabelle, auf die sich die Klasse beziehen soll.
     */
    protected DefaultCommentUdschebti(ArchimedesDescriptorFactory adf, String tn) {
        super(adf, tn);
    }

    /**
     * Generiert eine Instanz der Klasse mit Defaultwerten.
     *
     * @param adf Die ArchimedesDescriptorFactory, aus der die Instanz ihre Konfiguration 
     *     beziehen soll.
     */
    public DefaultCommentUdschebti(ArchimedesDescriptorFactory adf) {
        super(adf, "DefaultComment");
    }


    /* Accessoren & Mutatoren. */

    /** @return Der Wert der Eigenschaft Pattern. */
    public String getPattern() {
        String pattern = (String) this.get("Pattern");
        return pattern;
    }

    /**
     * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r die Eigenschaft Pattern.
     *
     * @param pattern Der neue Wert der Eigenschaft Pattern.
     */
    public void setPattern(String pattern) {
        if (pattern == null) {
            pattern = "";
        }
        this.set("Pattern", pattern);
    }

    /** @return Der Wert der Eigenschaft Comment. */
    public String getComment() {
        String comment = (String) this.get("Comment");
        return comment;
    }

    /**
     * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r die Eigenschaft Comment.
     *
     * @param comment Der neue Wert der Eigenschaft Comment.
     */
    public void setComment(String comment) {
        if (comment == null) {
            comment = "";
        }
        this.set("Comment", comment);
    }


    /* Implementierung der abstrakten Methoden der Superklasse. */

    public Object createObject() {
        return new DefaultCommentUdschebti(this.adf);
    }

}
