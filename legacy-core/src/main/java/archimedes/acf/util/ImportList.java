/*
 * ImportList.java
 *
 * 12.09.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.util;


import static corentx.util.Checks.*;

import java.util.*;

import corentx.util.*;


/**
 * A list of import clause names.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 12.09.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 * @changed OLI 08.05.2017 - Refactored to import objects and introduced the
 *         commented-out-option.
 */

public class ImportList {

    private String currentPackageName = null;
    private Map<String, ImportClause> imports = new HashMap<String, ImportClause>();

    /**
     * Creates a new import list with the passed parameters.
     *
     * @param currentPackageName The name of the current package.
     *
     * @changed OLI 12.09.2013 - Added.
     */
    public ImportList(String currentPackageName) {
        this.currentPackageName = currentPackageName;
    }

    /**
     * Creates a new import list with the passed parameters.
     *
     * @param currentPackageName The name of the current package.
     * @param imports A list of import clauses.
     *
     * @changed OLI 12.09.2013 - Added.
     */
    public ImportList(String currentPackageName, String... imports) {
        this.currentPackageName = currentPackageName;
        this.add(imports);
    }

    /**
     * Adds the passed import clause to the list.
     *
     * @param importClause The import clause to add.
     *
     * @changed OLI 12.09.2013 - Added.
     */
    public void add(String importClause) {
        this.add(importClause, false, false);
    }

    /**
     * Adds the passed import clause to the list.
     *
     * @param importClauses The import clause to add.
     *
     * @changed OLI 12.09.2013 - Added.
     */
    public void add(String... importClauses) {
        for (String importClause : importClauses) {
            this.add(importClause);
        }
    }

    /**
     * Adds the passed import clause to the list.
     *
     * @param importClause The import clause to add.
     * @param staticImport Set this flag if the import is a static one.
     * @param commentedOut Set this flag if the import is commented out.
     *
     * @changed OLI 08.05.2017 - Added.
     */
    public void add(String importClause, boolean staticImport, boolean commentedOut) {
        if (!this.contains(importClause) && !importClause.equals(this.currentPackageName)) {
            this.imports.put(importClause, new ImportClause(importClause, staticImport,
                    commentedOut));
        }
    }

    /**
     * Adds the passed import clause as commented out to the list.
     *
     * @param importClause The static import clause to add.
     *
     * @changed OLI 08.05.2017 - Added.
     */
    public void addCommentedOut(String importClause) {
        this.add(importClause, false, true);
    }

    /**
     * Adds the passed import clause as a static import to the list.
     *
     * @param importClause The static import clause to add.
     *
     * @changed OLI 12.09.2013 - Added.
     */
    public void addStatic(String importClause) {
        this.add(importClause, true, false);
    }

    /**
     * Checks if the passed import clause content already exists in the list.
     *
     * @param importClauseContent The import clause content to check.
     * @return <CODE>true</CODE> if an import clause with the passed content already exists.
     *
     * @changed OLI 08.05.2017 - Added.
     */
    public boolean contains(String importClauseContent) {
        return this.imports.containsKey(importClauseContent);
    }

    /**
     * Returns the import clause with the passed content.
     *
     * @param content The content of the import clause which is to return.
     * @return The import clause with the passed content or <CODE>null</CODE> if no import
     *         clause exists with the passed name.
     *
     * @changed OLI 09.05.2017 - Added.
     */
    public ImportClause getImport(String content) {
        ensure(content != null, "content cannot be null.");
        ensure(!content.isEmpty(), "content cannot be empty.");
        for (String key : this.imports.keySet()) {
            if (key.equals(content)) {
                return this.imports.get(key);
            }
        }
        return null;
    }

    /**
     * Returns the import clauses which are stored in the list.
     *
     * @return The import clauses which are stored in the list.
     *
     * @changed OLI 08.05.2017 - Added.
     */
    public ImportClause[] getImports() {
        List<ImportClause> l = new LinkedList<ImportClause>();
        List<String> keys = new SortedVector<String>();
        for (String key : this.imports.keySet()) {
            keys.add(key);
        }
        for (String key : keys) {
            l.add(this.imports.get(key));
        }
        return l.toArray(new ImportClause[0]);
    }

    /**
     * Returns the size of the import clauses list.
     *
     * @return The size of the import clauses list.
     *
     * @changed OLI 18.09.2013 - Added.
     */
    public int size() {
        return this.imports.size();
    }

    /**
     * @changed OLI 08.10.2013 - Added.
     */
    @Override public String toString() {
        String s = "";
        for (ImportClause ic : this.getImports()) {
            if (s.length() > 0) {
                s += ", ";
            }
            s += ic.getImportClauseContent();
        }
        return s;
    }

}