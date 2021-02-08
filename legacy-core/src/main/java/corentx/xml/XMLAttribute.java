/*
 * XMLAttribute.java
 *
 * 08.10.2015
 *
 * (c) by HealthCarion
 *
 */

package corentx.xml;


import static corentx.util.Checks.*;


/**
 * A XML attribute of a XML node.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.10.2015 - Added.
 */

public class XMLAttribute implements Comparable<XMLAttribute> {

    private String name = null;
    private String value = null;

    /**
     * Creates a new XML attribute.
     *
     * @param name The name of the XML attribute.
     * @param value The value of the XML attribute.
     * @throws IllegalArgumentException Passing a null pointer at all or an empty string for the
     *         name.
     * @changed OLI 08.10.2015 - Added.
     */
    public XMLAttribute(String name, String value) {
        super();
        ensure(name != null, "name cannot be null.");
        ensure(!name.isEmpty(), "name cannot be empty.");
        ensure(value != null, "value cannot be null.");
        this.name = name;
        this.value = value;
    }

    /**
     * @changed OLI 08.10.2015 - Added.
     */
    @Override public int compareTo(XMLAttribute o) {
        return this.getName().compareTo(o.getName());
    }

    /**
     * Returns a copy of the XML attribute.
     *
     * @return A copy of the XML attribute.
     *
     * @changed OLI 08.10.2015 - Added.
     */
    public XMLAttribute copy() {
        return new XMLAttribute(this.getName(), this.getValue());
    }

    /**
     * @changed OLI 08.10.2015 - Added.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof XMLAttribute)) {
            return false;
        }
        XMLAttribute e = (XMLAttribute) o;
        return this.getName().equals(e.getName()) && this.getValue().equals(e.getValue());
    }

    /**
     * Returns the name of the XML attribute.
     *
     * @return The name of the XML attribute.
     *
     * @changed OLI 08.10.2015 - Added.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the value of the XML attribute.
     *
     * @return The value  of the XML attribute.
     *
     * @changed OLI 08.10.2015 - Added.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @changed OLI 08.10.2015 - Added.
     */
    @Override public int hashCode() {
        int result = 17;
        result = 37 * result + this.getName().hashCode();
        result = 37 * result + this.getValue().hashCode();
        return result;
    }

    /**
     * @changed OLI 08.10.2015 - Added.
     */
    @Override public String toString() {
        return "name=" + this.getName() + ",value=" + this.getValue(); 
    }

    /**
     * Returns a XML representation of the attribute.
     *
     * @return A XML representation of the attribute.
     *
     * @changed OLI 08.10.2015 - Added.
     */
    public String toXML() {
        return this.getName() + "=\"" + this.escape(this.getValue()) + "\"";
    }

    private String escape(String s) {
        s = s.replace("&", "&amp;");
        s = s.replace("\"", "&quot;");
        s = s.replace("'", "&apos;");
        s = s.replace("<", "&lt;");
        s = s.replace(">", "&gt;");
        return s;
    }

}