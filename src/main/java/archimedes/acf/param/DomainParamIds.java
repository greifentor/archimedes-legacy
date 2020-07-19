/*
 * DomainParamIds.java
 *
 * 27.08.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.param;


/**
 * Some id's for domain params.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 27.08.2013 - Added.
 */

public class DomainParamIds {

    /** Marks a domain as a color representation. */
    public static final String COLOR = "COLOR";
    /** Marks a domain as enum in generated code. */
    public static final String ENUM = "ENUM";
    /** Marks a domain as not to generate any code for. */
    public static final String NO_CODE_GENERATION = "NO_CODE_GENERATION";
    /** Marks a domain as a password containing data type. */
    public static final String PASSWORD = "PASSWORD";
    /** Marks a domain as displayable in a localized form. */
    public static final String RESOURCED = "RESOURCED";
    /** Marks a domain as belonging to a sub project. */
    public static final String SUB_PROJECT = "SUB_PROJECT";

    protected DomainParamIds() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("There is no sense to instantiate an object if "
                + "this class.");
    }

}