/*
 * TableParamIds.java
 *
 * 23.08.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.param;


/**
 * Some id's for table params.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.08.2013 - Added.
 */

public class TableParamIds {

    /** Marks a table for an additional consistency check. */
    public static final String ADDITIONAL_CONSISTENCY_CHECK = "ADDITIONAL_CONSISTENCY_CHECK";
    /** Marks a table be cacheable. */
    public static final String CACHEABLE = "CACHEABLE";
    /** Marks a table to create a removed observer for. */
    public static final String CASCADE_DELETE = "CASCADE_DELETE";
    /** Marks a table which the maintenance menu item is to create for. */
    public static final String CREATE_MENU_ITEM = "CREATE_MENU_ITEM";
    /** Marks a table be dependent to a parent table. */
    public static final String DEPENDENT = "DEPENDENT";
    /** Marks a table be embedded. */
    public static final String EMBEDDED = "EMBEDDED";
    /** Marks a table which is able to fire events in case of data changes. */
    public static final String FIRE_ENTITY_EVENTS = "FIRE_ENTITY_EVENTS";
    /** Marks a table to get a referenced list as attribute. */
    public static final String INCLUDE_LIST = "INCLUDE_LIST";
    /** Marks a table to get a referenced set as attribute. */
    public static final String INCLUDE_SET = "INCLUDE_SET";
    /** Marks a table to generate code for list views. */
    public static final String LIST_VIEW = "LIST_VIEW";
    /** Marks a table to not generate bean classes for. */
    public static final String NO_BEAN = "NO_BEAN";
    /** Marks a table which is excluded from the code generation process at all. */
    public static final String NO_CODE_GENERATION = "NO_CODE_GENERATION";
    /** Marks a table which provide no delete button in the GUI. */
    public static final String NO_DELETE_BUTTON = "NO_DELETE_BUTTON";
    /** Marks a table to not generate GUI classes for. */
    public static final String NO_GUI = "NO_GUI";
    /** Marks a table to not generate GUI mapper classes for. */
    public static final String NO_MAPPER = "NO_MAPPER";
    /** Marks a table which provide no new button in the GUI. */
    public static final String NO_NEW_BUTTON = "NO_NEW_BUTTON";
    /** Marks a table to have an order column. */
    public static final String ORDER_BY = "ORDER_BY";

    protected TableParamIds() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("There is no sense to instantiate an object if "
                + "this class.");
    }

}