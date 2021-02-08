/*
 * AbstractXMLElement.java
 *
 * 08.03.2013
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.xml;


import static corentx.util.Checks.*;

import corentx.util.*;


/**
 * An abstract implementation of the <CODE>XMLElement</CODE> interface which contains methods
 * and attributes for the name. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.03.2013 - Added.
 */

abstract public class AbstractXMLElement implements XMLElement {

    private String name = null;

    /**
     * Create a new XML value with the passed parameters.
     *
     * @param name The name of the XML value.
     * @throws IllegalArgumentException Passing an empty or null string.
     *
     * @changed OLI 08.03.2013 - Added.
     */
    protected AbstractXMLElement(String name) throws IllegalArgumentException {
        super();
        ensure(name != null, "name cannot be null.");
        ensure(!name.isEmpty(), "name cannot be empty.");
        this.name = name;
    }

    /**
     * @changed OLI 08.03.2013 - Added.
     */
    @Override public String getName() {
        return this.name;
    }

    /**
     * Returns a closing tag.
     *
     * @changed OLI 08.03.2013 - Added.
     */
    protected String closeTag() {
        return "</" + this.getName() + ">";
    }

    /**
     * Returns a string with spaces in the passed number.
     *
     * @changed OLI 08.03.2013 - Added.
     */
    protected String indent(int indent) {
        return Str.pumpUp("", " ", indent, Direction.LEFT);
    }

    /**
     * Returns an opening tag.
     *
     * @changed OLI 08.03.2013 - Added.
     */
    protected String openTag() {
        return "<" + this.getName() + ">";
    }

}