/*
 * ColumnMetaData.java
 *
 * 05.04.2004
 *
 * (c) by ollie
 *
 */
  
package archimedes.legacy.model;


import static corentx.util.Checks.*;
import corent.db.*;

import archimedes.legacy.*;


/**
 * Diese Klasse dient der Aufnahme von Metadaten einer Tabellenspalte.<BR>
 * <HR>
 *
 * @author ollie
 *
 */
 
public class ColumnMetaData implements Comparable {
    
    public boolean primaryKey = false;
    public int colsize = 0;
    public int nks = 0;
    public DBType datatype = null;
    public String name = null;
    public String typename = null;
    public String sqltype = null;

    private boolean notnull = false;
    private String fkConstraintName = null; 
    private String referencedTableName = null; 

    public ColumnMetaData(String name) {
        this(name, null, null, 0, 0, false);
    }

    public ColumnMetaData(String name, String typename, DBType datatype) {
        this(name, typename, datatype, 0, 0, false);
    }

    public ColumnMetaData(String name, String typename, DBType datatype, int colsize) {
        this(name, typename, datatype, colsize, 0, false);
    }

    public ColumnMetaData(String name, String typename, DBType datatype, boolean primaryKey) {
        this(name, typename, datatype, 0, 0, primaryKey);
    }

    public ColumnMetaData(String name, String typename, DBType datatype, int colsize, int nks, 
            boolean primaryKey) {
        super();
        this.colsize = colsize;
        this.datatype = datatype;
        this.name = name;
        this.nks = nks;
        this.typename = typename;
        this.primaryKey = primaryKey;
    }

    public ColumnMetaData(String name, String typename, String sqltype, boolean primaryKey,
            boolean notnull) {
        super();
        this.name = name;
        this.typename = typename;
        this.sqltype = sqltype;
        this.primaryKey = primaryKey;
        this.notnull = notnull;
    }

    public int compareTo(Object o) {
        ColumnMetaData cmd = (ColumnMetaData) o;
        if (this.primaryKey && !cmd.primaryKey) {
            return 1;
        } else if (!this.primaryKey && cmd.primaryKey) {
            return -1;
        }
        return this.name.compareTo(cmd.name);
    }

    /**
     * Returns the name of the foreign key constraint or <CODE>null</CODE> if the column isn't a
     * reference.
     *
     * @return The name of the foreign key constraint or <CODE>null</CODE> if the column isn't a
     *         reference.
     *
     * @changed OLI 25.04.2013 - Added.
     */
    public String getForeignKeyConstraintName() {
        return this.fkConstraintName;
    }

    /**
     * Returns the name of the referenced table or <CODE>null</CODE> if the column isn't a
     * reference.
     *
     * @return The name of the referenced table or <CODE>null</CODE> if the column isn't a
     *         reference.
     *
     * @changed OLI 25.04.2013 - Added.
     */
    public String getReferencedTableName() {
        return this.referencedTableName;
    }

    public String getSQLType() {
        return DBType.GetSQLType(this.datatype, Archimedes.DBMODE) + (this.datatype.hasLength()
                ? "(" + new Integer(this.colsize).toString() + (this.datatype.hasNKS() ? ", "
                + new Integer(this.nks).toString() : "") + ")" : "");
    }

    public boolean isNotNull() {
        if (this.primaryKey) {
            return true;
        }
        return this.notnull;
    }

    public boolean isReferencingToTable(String tableName) {
        return (this.referencedTableName != null) && tableName.equalsIgnoreCase(
                this.referencedTableName);
    }

    public void setNotNull(boolean nn) {
        this.notnull = nn;
    }

    /**
     * Sets the passed name as new name for the foreign key constraint.
     *
     * @param fkConstraintName The new name for the foreign key constraint or <CODE>null</CODE>
     *         if the reference should be reset.
     * @throws IllegalArgumentException Passing an empty string.
     *
     * @changed OLI 25.04.2013 - Added.
     */
    public void setForeignKeyConstraintName(String fkConstraintName)
            throws IllegalArgumentException {
        ensure((fkConstraintName == null) || !fkConstraintName.isEmpty(), "foreign key "
                + "constraint name cannot be null.");
        this.fkConstraintName = fkConstraintName;
    }

    /**
     * Sets the passed name as new name for the referenced table.
     *
     * @param referencedTableName The new name for the referenced table or <CODE>null</CODE> if
     *         the reference should be reset.
     * @throws IllegalArgumentException Passing an empty string.
     *
     * @changed OLI 25.04.2013 - Added.
     */
    public void setReferencedTableName(String referencedTableName)
            throws IllegalArgumentException {
        ensure((referencedTableName == null) || !referencedTableName.isEmpty(), "referenced "
                + "table name cannot be null.");
        this.referencedTableName = referencedTableName;
    }

    @Override public String toString() {
        return this.name + " " + this.typename + " (" + this.getSQLType() + ")" 
                + (this.primaryKey ? " PRIMARY KEY" : "") + (this.notnull ? " NOT NULL" : "");
    }

}