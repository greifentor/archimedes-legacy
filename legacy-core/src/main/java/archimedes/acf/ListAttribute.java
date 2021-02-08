/*
 * ListAttribute.java
 *
 * 22.06.2015
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.acf;

import static corentx.util.Checks.*;

import archimedes.model.*;


/**
 * A container for single list attribute information.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 22.06.2015 - Added.
 */

public class ListAttribute implements Comparable {

    public enum ListAttributeType {
        LIST("java.util", "List"),
        SET("java.util", "Set");
        private String listPackageName;
        private String listClassName;
        private ListAttributeType(String listPackageName, String listClassName) {
            this.listClassName = listClassName;
            this.listPackageName = listPackageName;
        }
        public String getListClassName() {
            return this.listClassName;
        }
        public String getListPackageName() {
            return this.listPackageName;
        }
    }

    private String name = null;
    private TableModel referencedTable = null;
    private ListAttributeType type = null;

    /**
     * Creates a new list attribute with the passed parameters.
     *
     * @param name The name of the list attribute.
     * @param referencedTable The referenced table if there is one.
     * @param type The type of the list attribute.
     *
     * @changed OLI 22.06.2015 - Added.
     */
    public ListAttribute(String name, TableModel referencedTable, ListAttributeType type) {
        super();
        ensure(name != null, "list attribute name cannot be null.");
        this.name = name;
        this.referencedTable = referencedTable;
        this.type = type;
    }

    /**
     * @changed OLI 22.06.2015 - Added.
     */
    @Override public int compareTo(Object o) {
        return this.name.compareTo(((ListAttribute) o).getName());
    }

    /**
     * Returns the name of the list attribute.
     *
     * @return The name of the list attribute.
     *
     * @changed OLI 22.06.2015 - Added.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the referenced table of the list attribute, if there is one.
     *
     * @return The referenced table of the list attribute, if there is one.
     *
     * @changed OLI 22.06.2015 - Added.
     */
    public TableModel getReferencedTable() {
        return this.referencedTable;
    }

    /**
     * Returns the type of the list attribute.
     *
     * @return The type of the list attribute.
     *
     * @changed OLI 07.10.2015 - Added.
     */
    public ListAttributeType getType() {
        return this.type;
    }

}