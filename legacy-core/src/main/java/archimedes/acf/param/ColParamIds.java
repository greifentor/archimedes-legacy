/*
 * ParameterIds.java
 *
 * 07.08.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.param;


/**
 * This class contains the id's of the parameter which can be passed to columns in the model. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 07.08.2013 - Added.
 */

public class ColParamIds {

    /** Set this id to generate assembled attributes. */
    public static final String ASSEMBLY = "ASSEMBLY";
    /** Set this id to generate a button instead of the component related to the column. */
    public static final String BUTTON = "BUTTON";
    /** Set this id to have validity checks in the setter. */
    public static final String CHECKED_SETTER = "CHECKED_SETTER";
    /** Marks a reference which is the holder of embeddables (if class is one). */
    public static final String EMBEDDED_REFERENCE = "EMBEDDED_REFERENCE";
    /**
     * Marks a flat reference list which can be used to create lists easily by ticking the
     * members in the list.
     */
    public static final String GENERATED_ID = "GENERATED_ID";
    /** Marks attributes which are o create with listeners and custom methods therefor. */
    public static final String LISTENERS = "LISTENERS";
    /** Sets a maximum value for numeric fields. */
    public static final String MAX_VALUE = "MAX_VALUE";
    /** Sets a minimum value for numeric fields. */
    public static final String MIN_VALUE = "MIN_VALUE";
    /** Marks a name attribute e. g. for error messages. */
    public static final String NAME_ATTRIBUTE = "NAME_ATTRIBUTE";
    /** Marks a field which is not to export. */
    public static final String NOT_BLANK = "NOT_BLANK";
    /** Marks a not editable field. */
    public static final String NOT_EDITABLE = "NOT_EDITABLE";
    /** Marks a field which should no be accessible by a getter. */
    public static final String NO_GETTER = "NO_GETTER";
    /** Marks public readable fields. */
    public static final String NO_PUBLIC_READ = "NO_PUBLIC_READ";
    /** Marks public writable fields. */
    public static final String NO_PUBLIC_WRITE = "NO_PUBLIC_WRITE";
    /** Marks fields not in list member editors. */
    public static final String NOT_IN_MEMBER_EDITOR = "NOT_IN_MEMBER_EDITOR";
    /** Marks a field which should no be accessible by a setter. */
    public static final String NO_SETTER = "NO_SETTER";
    /** Marks a field which is to turn to value zero if a null is passed. */
    public static final String NULL_TO_ZERO = "NULL_TO_ZERO";
    /** Marks a field as a reference to a parent table. */
    public static final String PARENT_REF = "PARENT_REF";
    /** Marks a field which is marks a column with password contents. */
    public static final String PASSWORD = "PASSWORD";
    /** Marks a field which is to render before shown in the selection view. */
    public static final String RENDERED = "RENDERED";
    /** Marks a field which has a separator line above the component. */
    public static final String SEPARATOR = "SEPARATOR";
    /** Marks a field which has to be trimmed while setting. */
    public static final String TRIM = "TRIM";
    /** Marks a field which has to be trimmed on the left side only while setting. */
    public static final String TRIM_LEFT = "TRIM_LEFT";
    /** Marks a field which has to be trimmed on the right side only while setting. */
    public static final String TRIM_RIGHT = "TRIM_RIGHT";
    /** Marks a field which is shown as a text area. */
    public static final String TEXT_AREA = "TEXT_AREA";
    /** Marks a field which is to bind to text change listeners. */
    public static final String TEXT_CHANGE_LISTENER = "TEXT_CHANGE_LISTENER";
    /** Marks a field which can be switched from text field to text area. */
    public static final String TEXT_FIELD_TO_TEXT_AREA = "TEXT_FIELD_TO_TEXT_AREA";
    /** Marks a field which can be filled once and is unchangeable there after. */
    public static final String UNCHANGEABLE = "UNCHANGEABLE";

    protected ColParamIds() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("There is no sense to instantiate an object if "
                + "this class.");
    }

}