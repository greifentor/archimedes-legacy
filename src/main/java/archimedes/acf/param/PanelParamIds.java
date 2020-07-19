/*
 * PanelParamIds.java
 *
 * 11.11.2014
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.param;


/**
 * Some id's for panel params.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.11.2014 - Added.
 */

public class PanelParamIds {

    /** An identifier for a custom panel. */
    public static final String CUSTOM = "CUSTOM";
    /** An identifier for a form panel. */
    public static final String FORM = "FORM";

    protected PanelParamIds() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("There is no sense to instantiate an object if "
                + "this class.");
    }

}