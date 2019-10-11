/*
 * SQLScript.java
 *
 * 12.12.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.script.sql;


import static corentx.util.Checks.*;

import java.util.*;


/**
 * A container for SQL scripts.
 *
 * @author ollie
 *
 * @changed OLI 12.12.2013 - Added.
 */

public class SQLScript {

    private List<String> added = new LinkedList<String>();
    private List<String> changed = new LinkedList<String>();
    private List<String> removed = new LinkedList<String>();

    /**
     * Adds a SQL statement to the list of the statement which are changing the data scheme.
     *
     * @param stmts The statements which changes the data scheme.
     *
     * @changed OLI 12.12.2013 - Added.
     */
    public void addChangingStatement(String... stmts) {
        ensure(stmts != null, "statements cannot be null.");
        for (String stmt : stmts) {
            this.changed.add(stmt);
        }
    }

    /**
     * Adds a SQL statement to the list of the statement which are extending the data scheme.
     *
     * @param stmts The statements which extends the data scheme.
     *
     * @changed OLI 12.12.2013 - Added.
     */
    public void addExtendingStatement(String... stmts) {
        ensure(stmts != null, "statements cannot be null.");
        for (String stmt : stmts) {
            this.added.add(stmt);
        }
    }

    /**
     * Adds a SQL statement to the list of the statement which are reducing the data scheme.
     *
     * @param stmts The statements which reduces the data scheme.
     *
     * @changed OLI 12.12.2013 - Added.
     */
    public void addReducingStatement(String... stmts) {
        ensure(stmts != null, "statements cannot be null.");
        for (String stmt : stmts) {
            this.removed.add(stmt);
        }
    }

    /**
     * Returns the SQL script for the stored lines in the right direction with the pre adding,
     * pre changing, post changing and post deleting fragments. The order is: pre adding
     * fragment, adding statements, pre changing fragment, changing statements, post changing
     * fragment, deleting statements, post deleting fragment.
     *
     * @param header The header of the script.
     * @param preExtendingFragment The SQL script fragment which is to insert before the adding
     *         statements.
     * @param preChangingFragment The SQL script fragment which is to insert before the changing
     *         statements.
     * @param postChangingFragment The SQL script fragment which is to insert after the changing
     *         statements.
     * @param postReducingFragment The SQL script fragment which is to insert after the deleting
     *         statements.
     * @return The complete SQL script.
     *
     * @changed OLI 12.12.2013 - Added.
     */
    public String createScript(String header, String preExtendingFragment,
            String preChangingFragment, String postChangingFragment, String postReducingFragment
            ) {
        String script = this.addToScript(header);
        script += this.addToScript(preExtendingFragment);
        script += this.addToScript(this.createScriptFromLines(this.getExtendingStatements()));
        script += this.addToScript(preChangingFragment);
        script += this.addToScript(this.createScriptFromLines(this.getChangingStatements()));
        script += this.addToScript(postChangingFragment);
        script += this.addToScript(this.createScriptFromLines(this.getReducingStatements()),
                (this.addToScript(postReducingFragment).isEmpty()));
        script += this.addToScript(postReducingFragment, true);
        return script;
    }

    private String addToScript(String s) {
        return this.addToScript(s, false);
    }

    private String addToScript(String s, boolean last) {
        return ((s != null) && !s.isEmpty() ? s + (!last ? "\n\n" : "") : "");
    }

    private String createScriptFromLines(String[] s) {
        String script = "";
        for (int i = 0; i < s.length; i++) {
            script += s[i];
            if (i < s.length-1) {
                script += "\n";
            }
        }
        return script;
    }

    /**
     * @changed OLI 12.12.2013 - Added.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof SQLScript)) {
            return false;
        }
        SQLScript s = (SQLScript) o;
        return this.added.equals(s.added) && this.changed.equals(s.changed)
                && this.removed.equals(s.removed);
    }

    /**
     * Returns the statements which are changing the data scheme.
     *
     * @return The statements which are changing the data scheme.
     *
     * @changed OLI 12.12.2013 - Added.
     */
    public String[] getChangingStatements() {
        return this.changed.toArray(new String[0]);
    }

    /**
     * Returns the statements which are extending the data scheme.
     *
     * @return The statements which are extending the data scheme.
     *
     * @changed OLI 12.12.2013 - Added.
     */
    public String[] getExtendingStatements() {
        return this.added.toArray(new String[0]);
    }

    /**
     * Returns the statements which are reducing the data scheme.
     *
     * @return The statements which are reducing the data scheme.
     *
     * @changed OLI 12.12.2013 - Added.
     */
    public String[] getReducingStatements() {
        return this.removed.toArray(new String[0]);
    }

    /**
     * @changed OLI 12.12.2013 - Added.
     */
    @Override public int hashCode() {
        return this.added.hashCode() + this.changed.hashCode() + this.removed.hashCode();
    }

    /**
     * Returns the number of the stored lines.
     *
     * @return The number of the stored lines.
     *
     * @changed OLI 12.12.2013 - Added.
     */
    public long size() {
        return this.added.size() + this.changed.size() + this.removed.size();
    }

    /**
     * @changed OLI 12.12.2013 - Added.
     */
    @Override public String toString() {
        return "[" + this.createScript("", "", "", "", "").replace("\n\n", "\n").replace("\n",
                ", ") + "]";
    }
}