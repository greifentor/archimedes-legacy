/*
 * ImportCreator.java
 *
 * 11.05.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.util;

import static corentx.util.Checks.*;

/**
 * A class which creates code from an import list.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.05.2017 - Added.
 */

public class ImportCreator {

    /**
     * Creates a code fragment with the import clauses from the passed import list.
     *
     * @param imports The import list whose code fragment is to create.
     * @param The code fragment with the import clauses from the passed import list (or an empty
     *         string for an empty list).
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 11.05.2017 - Added.
     */
    public String createImportCodeFragment(ImportList imports) {
        ensure(imports != null, "import list cannot be null.");
        String s = "";
        for (int i = 0; i <= 1; i++) {
            String c0 = (i == 0 ? "" : (s.length() > 0 ? "$%&" : ""));
            for (ImportClause ic : imports.getImports()) {
                String c = ic.getImportClauseContent();
                if (((i == 0) && !ic.isStaticImport()) || ((i == 1) && ic.isStaticImport())) {
                    continue;
                }
                if (!c0.equals("") && !this.getFirstPart(c).equals(this.getFirstPart(c0))) {
                    s += "\n";
                }
                s += (ic.isCommentedOut() ? "// " : "") + "import ";
                if (ic.isStaticImport()) {
                    s += "static ";
                }
                s += c + (!c.endsWith(";") ? ".*;" : "") + "\n";
                c0 = c;
            }
        }
        return s;
    }

    private String getFirstPart(String s) {
        int i = s.indexOf(".");
        if (i >= 0) {
            s = s.substring(0, i);
        }
        return s;
    }

}