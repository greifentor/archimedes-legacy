/*
 * ModelParamIds.java
 *
 * 17.12.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.param;

/**
 * This class contains the id's of the parameter which can be passed to the model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.12.2013 - Added.
 */

public class ModelParamIds {

    /** Embedded setter are protected. */
    public static final String EMBEDDED_SETTERS_PROTECTED = "EMBEDDED_SETTERS_PROTECTED";
    /** Marks a model with an optional resource path. */
    public static final String OPTIONAL_RESOURCE_PATH = "OPTIONAL_RESOURCE_PATH";
    /** Marks the model as temporary. */
    public static final String TEMPORARY = "TEMPORARY";

    protected ModelParamIds() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("There is no sense to instantiate an object if "
                + "this class.");
    }

}