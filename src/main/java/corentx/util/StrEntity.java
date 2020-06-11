/*
 * StrEntity.java
 *
 * 19.01.2012
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.util;


/**
 * Eine Sammlung von Entities, die durch dioe Methoden <CODE>fromHTML(String)</CODE> und
 * <CODE>toHTML(String)</CODE> ber&uuml;cksichtigt werden.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.01.2012 - Hinzugef&uuml;gt.
 */

public enum StrEntity {

    amp("&amp;", "&"),
    auml("&auml;", "ä"),
    Auml("&Auml;", "Ä"),
    gt("&gt;", ">"),
    lt("&lt;", "<"),
    nbsp("&nbsp;", " ", false),
    ouml("&ouml;", "ö"),
    Ouml("&Ouml;", "Ö"),
    radic("&radic;", "ø"),
    Radic("&Radic;", "Ø"),
    szlig("&szlig;", "ß"),
    uuml("&uuml;", "ü"),
    Uuml("&Uuml;", "Ü"),
    aacute("&aacute;", "á"),
    eacute("&eacute;", "é"),
    iacute("&iacute;", "í"),
    oacute("&oacute;", "ó"),
    uacute("&uacute;", "ú"),
    acirc("&acirc;", "â"),
    ecirc("&ecirc;", "ê"),
    icirc("&icirc;", "î"),
    ocirc("&ocirc;", "ô"),
    ucirc("&ucirc;", "û");

    private boolean setToHTML = true;
    private String entity = null;
    private String str = null;

    private StrEntity(String entity, String str) {
        this(entity, str, true);
    }

    private StrEntity(String entity, String str, boolean setToHTML) {
        this.entity = entity;
        this.setToHTML = setToHTML;
        this.str = str;
    }

    /**
     * Liefert den HTML-Entity-String.
     *
     * @return Der HTML-Entity-String.
     *
     * @changed OLI 19.01.2012 - Hinzugef&uuml;gt.
     */
    public String getHTMLEntity() {
        return this.entity;
    }

    /**
     * Liefert den Java-String.
     *
     * @return Der Java-String.
     *
     * @changed OLI 19.01.2012 - Hinzugef&uuml;gt.
     */
    public String getJavaString() {
        return this.str;
    }

    /**
     * Pr&uuml;ft, ob die HTML-Entity auch bei der R&uuml;ckwandlung in einen HTML-String
     * ber&uuml;cksichtigt werden soll.
     *
     * @return <CODE>true</CODE> falls die HTML-Entity auch bei der R&uuml;ckwandlung in einen
     *         HTML-String ber&uuml;cksichtigt werden soll.
     *
     * @changed OLI 19.01.2012 - Hinzugef&uuml;gt.
     */
    public boolean isToSetAtToHTML() {
        return this.setToHTML;
    }

}