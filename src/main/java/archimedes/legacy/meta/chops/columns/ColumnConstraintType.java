/*
 * ColumnConstraintType.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.columns;


/**
 * Some constraint types for columns.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public enum ColumnConstraintType {

    // Order is relevant for come comparables (like "AlterColumnAddConstraint") which are using
    // this type. Don't change!
    PRIMARY_KEY,
    UNIQUE,
    FOREIGN_KEY,
    NOT_NULL;

}