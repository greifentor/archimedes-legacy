/*
 * CodeGeneratorReport.java
 *
 * 28.11.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.report;


import java.util.*;


/**
 * A report container for the code generation process.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 28.11.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class CodeGeneratorReport {

    private enum EntryType {
        BLOCKED_BY_EXCLUSION,
        BLOCKED_BY_MISSING_INCLUSION_MARK,
        DEPRECATED_SOURCE_CODE_DETECTED,
        UNCHANGED,
        UNCHANGED_BY_TAG,
        WRITTEN
    }

    private Map<EntryType, List<CodeGeneratorReportTableEntry>> entries =
            new Hashtable<EntryType, List<CodeGeneratorReportTableEntry>>();

    /**
     * Creates a new code generator report.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public CodeGeneratorReport() {
        super();
        for (EntryType t : EntryType.values()) {
            this.entries.put(t, new LinkedList<CodeGeneratorReportTableEntry>());
        }
    }

    /**
     * Adds the table as blocked by an exclusion mark.
     *
     * @param entry The entry data (e. g. table and file name).
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public void addBlockedByExclusionMark(CodeGeneratorReportTableEntry entry) {
        this.entries.get(EntryType.BLOCKED_BY_EXCLUSION).add(entry); 
    }

    /**
     * Adds the table as blocked caused by missing a specific inclusion mark.
     *
     * @param entry The entry data (e. g. table and file name).
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public void addBlockedByMissingInclusionMark(CodeGeneratorReportTableEntry entry) {
        this.entries.get(EntryType.BLOCKED_BY_MISSING_INCLUSION_MARK).add(entry); 
    }

    /**
     * Adds the table as deprecated source code detected for the code generator.
     *
     * @param entry The entry data (e. g. table and file name).
     *
     * @changed OLI 23.02.2015 - Added.
     */
    public void addDeprecatedSourceCode(CodeGeneratorReportTableEntry entry) {
        List<CodeGeneratorReportTableEntry> l = this.entries.get(
                EntryType.DEPRECATED_SOURCE_CODE_DETECTED);
        for (CodeGeneratorReportTableEntry e : l) {
            if (e.getFileName().equals(entry.getFileName())) {
                return;
            }
        }
        l.add(entry);
    }

    /**
     * Adds the table as unchanged (existing file and generated one are equal).
     *
     * @param entry The entry data (e. g. table and file name).
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public void addUnchanged(CodeGeneratorReportTableEntry entry) {
        this.entries.get(EntryType.UNCHANGED).add(entry); 
    }

    /**
     * Adds the table as unchanged (by tag) for the report.
     *
     * @param entry The entry data (e. g. table and file name).
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public void addUnchangedByTag(CodeGeneratorReportTableEntry entry) {
        this.entries.get(EntryType.UNCHANGED_BY_TAG).add(entry); 
    }

    /**
     * Adds the table as written of the code generator.
     *
     * @param entry The entry data (e. g. table and file name).
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public void addWritten(CodeGeneratorReportTableEntry entry) {
        this.entries.get(EntryType.WRITTEN).add(entry); 
    }

    /**
     * Returns the entries of the tables which are blocked by exclusion.
     *
     * @return The entries of the tables which are blocked by exclusion.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public CodeGeneratorReportTableEntry[] getBlockedByExclusion() {
        return this.entries.get(EntryType.BLOCKED_BY_EXCLUSION).toArray(
                new CodeGeneratorReportTableEntry[0]);
    }

    /**
     * Returns the entries of the tables which are blocked by missing a specific inclusion mark..
     *
     * @return The entries of the tables which are blocked by missing a specific inclusion mark.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public CodeGeneratorReportTableEntry[] getBlockedByMissingInclusionMark() {
        return this.entries.get(EntryType.BLOCKED_BY_MISSING_INCLUSION_MARK).toArray(
                new CodeGeneratorReportTableEntry[0]);
    }

    /**
     * Returns the entries of the tables which represent deprecated source code detections.
     *
     * @return The entries of the tables which represent deprecated source code detections.
     *
     * @changed OLI 23.02.2015 - Added.
     */
    public CodeGeneratorReportTableEntry[] getDeprecatedSourceCode() {
        return this.entries.get(EntryType.DEPRECATED_SOURCE_CODE_DETECTED).toArray(
                new CodeGeneratorReportTableEntry[0]);
    }

    /**
     * Returns the entries of the tables which are unchanged.
     *
     * @return The entries of the tables which are unchanged.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public CodeGeneratorReportTableEntry[] getUnchanged() {
        return this.entries.get(EntryType.UNCHANGED).toArray(new CodeGeneratorReportTableEntry[
                0]);
    }

    /**
     * Returns the entries of the tables which are unchanged by tag.
     *
     * @return The entries of the tables which are unchanged by tag.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public CodeGeneratorReportTableEntry[] getUnchangedByTag() {
        return this.entries.get(EntryType.UNCHANGED_BY_TAG).toArray(
                new CodeGeneratorReportTableEntry[0]);
    }

    /**
     * Returns the entries of the tables which are written.
     *
     * @return The entries of the tables which are written.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public CodeGeneratorReportTableEntry[] getWritten() {
        return this.entries.get(EntryType.WRITTEN).toArray(new CodeGeneratorReportTableEntry[0]
                );
    }

}