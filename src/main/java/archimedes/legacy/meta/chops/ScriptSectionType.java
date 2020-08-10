/*
 * ScriptSectionType.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops;


/**
 * Type for the sections of a SQL script.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public enum ScriptSectionType {

    VERSION_UPDATE,
    SCRIPT_BEFORE_ANY_CHANGES,
    CREATE_SEQUENCES,
    DROP_FOREIGNKEYS,
    ADD_COLUMNS_AND_DROP_CONSTRAINTS,
    ADD_TABLES,
    ADD_CONSTRAINTS_FOR_NEW_TABLES,
    SCRIPT_BEFORE_UPDATES,
    ALTER_COLUMNS,
    UPDATE_CONSTRAINTS,
    ADD_FOREIGN_KEYS,
    SCRIPT_AFTER_UPDATES,
    DROP_COLUMNS,
    DROP_TABLES,
    DROP_SEQUENCES,
    SCRIPT_AFTER_ALLCHANGES;

}