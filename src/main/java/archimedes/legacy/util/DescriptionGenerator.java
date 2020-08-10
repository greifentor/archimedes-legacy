/*
 * DescriptionGenerator.java
 *
 * 11.05.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.util;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;
import corentx.util.*;

/**
 * A generator for names of database objects in comments of source code.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.05.2017 - Added.
 */

public class DescriptionGenerator {

    /**
     * Returns a description text for the passed columns name.
     *
     * @param c The column whose name is to return as a description text.
     * @return A description text for the passed columns name.
     * @throws NullPointerException Passing a null pointer as column model.
     *
     * @changed OLI 11.05.2017 - Added (from "AbstractCodeGenerator").
     */
    public String getDescription(ColumnModel c) {
        return this.getDescription(c.getName());
    }

    /**
     * Returns a description text for the passed domains name.
     *
     * @param d The domain whose name is to return as a description text.
     * @return A description text for the passed domains name.
     * @throws NullPointerException Passing a null pointer as domain model.
     *
     * @changed OLI 11.05.2017 - Added (from "AbstractCodeGenerator").
     */
    public String getDescription(DomainModel d) {
        return this.getDescription(d.getName());
    }

    /**
     * Returns a description text for the passed string.
     *
     * @param s The string which the description is to create for.
     * @return A description text for the passed string.
     * @throws NullPointerException Passing a null pointer as string.
     *
     * @changed OLI 11.05.2017 - Added (from "AbstractCodeGenerator").
     */
    public String getDescription(String s) {
        s = s.replace(".", " ");
        for (int i = s.length()-1; i > 0; i--) {
            if ((s.charAt(i) >= 'A') && (s.charAt(i) <= 'Z') && (s.charAt(i) != ' ')) {
                s = Str.insert(s, " ", i);
            }
        }
        String c = "";
        for (String s0 : Str.splitToList(s, " ")) {
            if (c.length() > 0) {
                c += " ";
            }
            c += s0;
        }
        return c.toLowerCase();
    }

    /**
     * Returns a description text for the passed tables name.
     *
     * @param s The table whose name is to return as a description text.
     * @return A description text for the passed tables name.
     * @throws NullPointerException Passing a null pointer as table model.
     *
     * @changed OLI 11.05.2017 - Added (from "AbstractCodeGenerator").
     */
    public String getDescription(TableModel tm) {
        return this.getDescription(tm.getName());
    }

}