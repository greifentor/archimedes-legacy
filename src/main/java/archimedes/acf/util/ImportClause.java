/*
 * ImportClause.java
 *
 * 08.05.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.util;

import static corentx.util.Checks.*;

import org.apache.commons.lang3.builder.*;

/**
 * A container class for an import clause.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.05.2017 - Added.
 */

public class ImportClause {

    private boolean commentedOut = false;
    private String importClauseContent = null;
    private boolean staticImport = false;

    /**
     * Creates a new non static not out-commented import clause container with the passed
     * parameters.
     *
     * @param importClauseContent The import clause content (e. g. "java.util" (no ".*"
     *         necessary).
     * @throws IllegalArgumentException Passing a empty or a null string as import clause.
     *
     * @changed OLI 08.05.2017 - Added.
     */
    public ImportClause(String importClauseContent) {
        this(importClauseContent, false, false);
    }

    /**
     * Creates a import clause container with the passed parameters.
     *
     * @param importClauseContent The import clause content (e. g. "java.util" (no ".*"
     *         necessary).
     * @param staticImport Set this flag if the import is static.
     * @throws IllegalArgumentException Passing a empty or a null string as import clause.
     *
     * @changed OLI 08.05.2017 - Added.
     */
    public ImportClause(String importClauseContent, boolean staticImport) {
        this(importClauseContent, staticImport, false);
    }

    /**
     * Creates a import clause container with the passed parameters.
     *
     * @param importClauseContent The import clause content (e. g. "java.util" (no ".*"
     *         necessary).
     * @param staticImport Set this flag if the import is static.
     * @param commentedOut Set this flag if the import is commented out.
     *
     * @changed OLI 08.05.2017 - Added.
     */
    public ImportClause(String importClauseContent, boolean staticImport, boolean commentedOut)
            {
        super();
        ensure(importClauseContent != null, "import clause content cannot be null.");
        ensure(!importClauseContent.isEmpty(), "import cluase content cannot be empty.");
        this.commentedOut = commentedOut;
        this.importClauseContent = importClauseContent;
        this.staticImport = staticImport;
    }

    /**
     * @changed OLI 08.05.2017 - Added.
     */
    @Override public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Returns the import clause content.
     *
     * @return The import clause content.
     *
     * @changed OLI 08.05.2017 - Added.
     */
    public String getImportClauseContent() {
        return this.importClauseContent;
    }

    /**
     * Checks if the commented out flag is set for the import clause.
     *
     * @return <CODE>true</CODE> if the commented out flag is set for the import clause.
     *
     * @changed OLI 08.05.2017 - Added.
     */
    public boolean isCommentedOut() {
        return this.commentedOut;
    }

    /**
     * Checks if the static import flag is set for the import clause.
     *
     * @return <CODE>true</CODE> if the static import flag is set for the import clause.
     *
     * @changed OLI 08.05.2017 - Added.
     */
    public boolean isStaticImport() {
        return this.staticImport;
    }

    /**
     * @changed OLI 08.05.2017 - Added.
     */
    @Override public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * @changed OLI 08.05.2017 - Added.
     */
    @Override public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}