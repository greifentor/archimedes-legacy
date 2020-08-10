/*
 * MetaUtil.java
 *
 * 21.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import static corentx.util.Checks.*;

import java.util.*;


/**
 * Some utility methods for working with meta data objects.  
 *
 * @author O.Lieshoff
 *
 * @changed OLI 21.12.2015 - Added.
 */

public class MetaUtil {

    private String quote = null;

    /**
     * Creates a new utility class object for working with meta data.
     *
     * @param quote The quote character for database objects.
     *
     * @changed OLI 21.12.2015 - Added.
     */
    public MetaUtil(String quote) {
        ensure(quote != null, "quote cannot be null.");
        this.quote = quote;
    }

    /**
     * Returns a comma separated string with the content of the passed string array.
     *
     * @param strings The strings which should be put to a single comma separated string.
     * @param quote Set this flag if the strings should be quoted before put to the string. 
     * @return A comma separated string with the content of the passed string array.
     *
     * @changed OLI 21.12.2015 - Added (from class "StandardSQLScriptGenerator").
     */
    public String getCommaSeparated(String[] strings, boolean quote) {
        ensure(strings != null, "string array cannot be null.");
        String r = "";
        for (String s : strings) {
            if (r.length() > 0) {
                r += ", ";
            }
            r += (quote ? this.quote(s) : s);
        }
        return r;
    }

    /**
     * Returns an array with the names of the passed meta data columns.
     *
     * @param columns The meta data columns whose names are to return.
     * @return An array with the names of the passed meta data columns.
     *
     * @changed OLI 21.12.2015 - Added (from class "StandardSQLScriptGenerator").
     */
    public String[] getNames(MetaDataColumn[] columns) {
        ensure(columns != null, "columns cannot be null.");
        List<String> l = new LinkedList<String>();
        for (MetaDataColumn c : columns) {
            l.add(c.getName());
        }
        return l.toArray(new String[0]);
    }

    /**
     * Quotes the passed string with the quote characters.
     *
     * @param s The string to quote.
     * @return The quoted string.
     *
     * @changed OLI 14.12.2015 - Added (from class "StandardSQLScriptGenerator").
     */
    public String quote(String s) {
        return this.quote.concat(s).concat(this.quote);
    }

    /**
     * Puts the name of the passed meta data named object into quotes.
     *
     * @param o The object whose name should be put into quotes.
     * @return The name of the passed meta data object put into quotes.
     *
     * @changed OLI 21.12.2015 - Added (from class "StandardSQLScriptGenerator").
     */
    public String quote(MetaDataNamedObject o) {
        ensure(o != null, "column cannot be null.");
        return this.quote(o.getName());
    }

}