/*
 * XMLHeaderBuilder.java
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
 * A class which builds a XML file header by given parameters.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.03.2013 - Added.
 */

public class XMLHeaderBuilder {

    private double version = 0.0D;
    private String encoding = null;
    private boolean standAlone = false;

    /**
     * Creates a builder for XML header tags for the passed parameters.
     *
     * @param version The version to set in the XML header tag.
     * @param encoding The encoding of the file.
     * @param standAlone The stand alone flag for header tag.
     * @throws IllegalArgumentException In case of precondition violation.
     * @precondition encoding != null
     * @precondition !encoding.isEmpty()
     * @precondition version >= 1.0
     *
     * @changed OLI 08.03.2013 - Added.
     */
    public XMLHeaderBuilder(double version, String encoding, boolean standAlone)
            throws IllegalArgumentException {
        super();
        ensure(encoding != null, "encoding cannot be null.");
        ensure(!encoding.isEmpty(), "encoding cannot be empty.");
        ensure(version >= 1.0, "version cannot be lesser one.");
        this.encoding = encoding;
        this.standAlone = standAlone;
        this.version = version;
    }

    /**
     * Returns a string with the XML header tag for the passed parameters.
     *
     * @return A string with the XML header tag for the passed parameters.
     *
     * @changed OLI 08.03.2013 - Added.
     */
    public String build() {
        return "<?xml version=\"" + Str.fixedDoubleToStr(this.version, 1) + "\" encoding=\""
                + this.encoding + "\" standalone=\"" + this.booleanToYesOrNo(this.standAlone)
                + "\"?>";
    }

    private String booleanToYesOrNo(boolean b) {
        return (b ? "yes" : "no");
    }

}