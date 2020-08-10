/*
 * SelectionAttribute.java
 *
 * 20.08.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * An enumeration with the id's of valid selection attributes.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 20.08.2013 - Added.
 */

public enum SelectionAttribute {

    HIDDEN,
    PHANTOM, // Is delivered from persistence layer but not in view.
    IMPORTANT,
    OPTIONAL;

}