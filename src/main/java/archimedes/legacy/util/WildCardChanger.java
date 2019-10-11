/*
 * WildCardChanger.java
 *
 * 24.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.util;

/**
 * Changes some wildcards related to a data model.
 *
 * @author ollie
 *
 * @changed OLI 24.10.2013 - Added.
 */

public class WildCardChanger {

    /**
     * Changes identifier with the passed data. The possible identifiers are listed below.
     *
     * <I><B>Note:</B> <CODE>null</CODE> values as passed parameters will be changed to the
     * string "&lt;NULL&gt;"</I>
     *
     * @param stmt The string which contains the wildcards to change.
     * @param tableName The name of the table ($TABLENAME$). 
     * @param columnName The name of the column ($COLUMNNAME$). 
     * @param typeName The name of the type ($TYPENAME$).
     * @return The string <CODE>stmt</CODE> with the replaced identifiers.
     *
     * @changed OLI 08.01.2009 - Added.
     * @changed OLI 10.03.2011 - Correcting the replacement with <CODE>null</CODE> values for
     *         table and type names.
     * @changed OLI 24.10.2013 - Transfered from class <CODE>Diagramm</CODE>.
     */
    public String change(String stmt, String tablename, String columnname, 
            String typename) {
        stmt = stmt.replace("$COLUMNNAME$", (columnname == null ? "<NULL>" : columnname));
        stmt = stmt.replace("$TABLENAME$", (tablename == null ? "<NULL>" : tablename));
        stmt = stmt.replace("$TYPENAME$", (typename == null ? "<NULL>" : typename));
        return stmt;
    }

}