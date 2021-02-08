/*
 * GenerationProcessReport.java
 *
 * 28.11.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.report;


import archimedes.model.*;
import baccara.gui.*;
import corentx.dates.*;
import corentx.util.*;
import archimedes.acf.*;

import static corentx.util.Checks.*;

import java.awt.Color;
import java.util.*;


/**
 * A container for the whole generation process.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 28.11.2013 - Added.
 */

public class GenerationProcessReport {

    private static final Color FAULT_COLOR = new Color(255, 0, 0);
    private static final Color SUCCESS_COLOR = new Color(32, 144, 32);
    private static final Color WARN_COLOR = new Color(255, 165, 0);

    private DoubleClassesChecker doubleClassesChecker = null;
    private Map<TableModel, CodeGeneratorReport> generatorReports =
            new Hashtable<TableModel, CodeGeneratorReport>();
    private GUIBundle guiBundle = null;
    private List<String> logLines = new LinkedList<String>();
    private List<ResourceData> resourceIdsRequired = new LinkedList<ResourceData>();
    private boolean success = false;
    private Map<String, String> generatedFileNames = new HashMap<String, String>();
    private String version = null;

    /**
     * Creates a new generation process report with the passed parameters.
     *
     * @param guiBundle A GUI bundle to get access e. g. to the resources.
     *
     * @changed OLI 29.11.2013 - Added.
     */
    public GenerationProcessReport(GUIBundle guiBundle) {
        this(guiBundle, null);
    }

    /**
     * Creates a new generation process report with the passed parameters.
     *
     * @param guiBundle A GUI bundle to get access e. g. to the resources.
     * @param version The version of the code generator which creates the report.
     *
     * @changed OLI 12.01.2016 - Added.
     */
    public GenerationProcessReport(GUIBundle guiBundle, String version) {
        super();
        this.guiBundle = guiBundle;
        this.version = version;
    }

    /**
     * Adds the table as blocked by an exclusion mark.
     *
     * @param table The table which the entry is to add for.
     * @param entry The entry data (e. g. table and file name).
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public void addBlockedByExclusionMark(TableModel table, CodeGeneratorReportTableEntry entry)
            {
        this.prepareFor(table);
        this.generatorReports.get(table).addBlockedByExclusionMark(entry); 
    }

    /**
     * Adds the table as blocked caused by missing a specific inclusion mark.
     *
     * @param table The table which the entry is to add for.
     * @param entry The entry data (e. g. table and file name).
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public void addBlockedByMissingInclusionMark(TableModel table,
            CodeGeneratorReportTableEntry entry) {
        this.prepareFor(table);
        this.generatorReports.get(table).addBlockedByMissingInclusionMark(entry); 
    }

    /**
     * Adds a log message to the report.
     *
     * @param message The log message to add.
     *
     * @changed OLI 06.07.2015 - Added.
     */
    public void addLogMessage(String message) {
        this.logLines.add(message);
    }

    /**
     * Adds the table as deprecated source code (existing file and generated one are equal).
     *
     * @param table The table which the entry is to add for.
     * @param entry The entry data (e. g. table and file name).
     *
     * @changed OLI 23.02.2015 - Added.
     */
    public void addDeprecatedSourceCode(TableModel table, CodeGeneratorReportTableEntry entry) {
        this.prepareFor(table);
        this.generatorReports.get(table).addDeprecatedSourceCode(entry);
    }

    /**
     * Adds the passed resource id if not already present in the list.
     *
     * @param resourceId The resource id to add.
     *
     * @changed OLI 06.07.2016 - Added.
     */
    public void addResourceId(ResourceData resourceId) {
        if (!this.resourceIdsRequired.contains(resourceId)) {
            this.resourceIdsRequired.add(resourceId);
        }
    }

    /**
     * Adds the table as unchanged (existing file and generated one are equal).
     *
     * @param table The table which the entry is to add for.
     * @param entry The entry data (e. g. table and file name).
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public void addUnchanged(TableModel table, CodeGeneratorReportTableEntry entry) {
        this.prepareFor(table);
        this.generatorReports.get(table).addUnchanged(entry); 
    }

    /**
     * Adds the table as unchanged (by tag) for the report.
     *
     * @param table The table which the entry is to add for.
     * @param entry The entry data (e. g. table and file name).
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public void addUnchangedByTag(TableModel table, CodeGeneratorReportTableEntry entry) {
        this.prepareFor(table);
        this.generatorReports.get(table).addUnchangedByTag(entry);
    }

    /**
     * Adds the table as written of the code generator.
     *
     * @param table The table which the entry is to add for.
     * @param entry The entry data (e. g. table and file name).
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public void addWritten(TableModel table, CodeGeneratorReportTableEntry entry) {
        this.prepareFor(table);
        this.generatorReports.get(table).addWritten(entry);
    }

    /**
     * Returns all entries whose files are ready to delete.
     *
     * @return All entries whose files are ready to delete.
     *
     * @changed OLI 19.05.2015 - Added.
     */
    public CodeGeneratorReport[] getDeprecatedEntries() {
        List<CodeGeneratorReportTableEntry> l = new LinkedList<CodeGeneratorReportTableEntry>();
        for (CodeGeneratorReport cgr : this.generatorReports.values()) {
            for (CodeGeneratorReportTableEntry cgrte : cgr.getDeprecatedSourceCode()) {
                l.add(cgrte);
            }
        }
        return l.toArray(new CodeGeneratorReport[0]);
    }

    /**
     * Returns an array with the required resource id's.
     *
     * @return An array with the required resource id's.
     *
     * @changed OLI 06.07.2016 - Added.
     */
    public ResourceData[] getResourceIds() {
        return this.resourceIdsRequired.toArray(new ResourceData[0]);
    }

    /**
     * Prepares the generation process report for the passed code generator.
     *
     * @param table The table which the generation process report is to prepare for.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public void prepareFor(TableModel table) {
        if (!this.generatorReports.containsKey(table)) {
            this.generatorReports.put(table, new CodeGeneratorReport());
        }
    }

    /**
     * Removes the passed file name from the generated file names.
     *
     * @param fileName The file name to remove.
     *
     * @changed OLI 06.02.2017 - Added.
     */
    public void removeGeneratedFileName(String fileName) {
        if (fileName != null) {
            this.generatedFileNames.remove(fileName);
        }
    }

    /**
     * Sets a new double classes checker for the report.
     *
     * @param doubleClassesChecker The new double classes checker for the report.
     *
     * @changed OLI 15.12.2014 - Added.
     */
    public void setDoubleClassesChecker(DoubleClassesChecker doubleClassesChecker) {
        this.doubleClassesChecker = doubleClassesChecker;
    }

    /**
     * Sets a list wit generated file names.
     *
     * @param generatedFileNames A List with generated file names.
     *
     * @changed OLI 06.02.2017 - Added.
     */
    public void setGeneratedFileNames(List<String> generatedFileNames) {
        ensure(generatedFileNames != null, "list of generated file names cannot by null.");
        for (String s : generatedFileNames) {
            this.generatedFileNames.put(s, s);
        }
    }

    /**
     * Sets the success of the generation process.
     *
     * @param success The new state of the success of the generation process.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Returns a report in HTML format for the whole process.
     *
     * @return A report in HTML format for the whole process.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public String toHTML() {
        boolean deprecatedFilesDetected = false;
        String s = "<HTML>\n";
        s += "    <FONT FACE=\"ARIAL\">\n";
        s += "    <H1>" + this.guiBundle.getResourceText("generation.process.report.title")
                + "</H1>\n";
        s += "    <HR>\n";
        s += "    <B><FONT SIZE=+1>" + this.guiBundle.getResourceText(
                "generation.process.report.state.label") + " " + (this.success
                ? this.createColorTag(SUCCESS_COLOR) + this.guiBundle.getResourceText(
                "generation.process.report.success.label") + "</FONT>"
                : this.createColorTag(FAULT_COLOR) + this.guiBundle.getResourceText(
                "generation.process.report.failed.label") + "</FONT>") + "</FONT>";
        s += "</FONT></B><BR>\n";
        s += "    " + this.guiBundle.getResourceText("generation.process.report.version.label")
                + " " + this.version + "<BR>\n";
        s += "    " + this.guiBundle.getResourceText("generation.process.report.finished.label")
                + " " + new PTimestamp() + "\n";
        s += "    <HR>\n";
        if (this.generatedFileNames.size() > 0) {
            s += "    <H2>" + this.guiBundle.getResourceText(
                    "generation.process.report.unusedFileNames.title") + "</H2>\n";
            s += "    <P>" + this.guiBundle.getResourceText(
                "generation.process.report.unusedFileNames.number.label",
                this.generatedFileNames.size());
            s += "    <UL>\n";
            List<String> fns = new SortedVector<String>();
            for (String k : this.generatedFileNames.keySet()) {
                fns.add(k);
            }
            for (String fn : fns) {
                s += "<LI>" + this.createColorTag(WARN_COLOR) + fn + "</B></LI>\n";
            }
            s += "    </UL>\n";
            s += "    <HR>\n";
        }
        if (this.logLines.size() > 0) {
            s += "    <H2>" + this.guiBundle.getResourceText(
                    "generation.process.report.log.title") + "</H2>\n";
            s += "    <PRE>\n";
            for (String line : this.logLines) {
                s += line + "\n";
            }
            s += "    </PRE>\n";
            s += "    <HR>\n";
        }
        String[] writtenFileNames = this.getAllWrittenFileNames(); 
        if (writtenFileNames.length > 0) {
            s += "    <H2>" + this.guiBundle.getResourceText(
                    "generation.process.report.written.file.names.title") + "</H2>\n";
            s += "    <UL>\n";
            for (String fn : writtenFileNames) {
                s += "        <LI><B>" + fn + "</B></LI>\n";
            }
            s += "    </UL>\n";
            s += "    <HR>\n";
        }
        s += "    <H2>" + this.guiBundle.getResourceText(
                "generation.process.report.tables.title") + "</H2>\n";
        s += "    <UL>\n";
        for (TableModel table : this.sortTables(this.generatorReports.keySet())) {
            CodeGeneratorReport cgr = this.generatorReports.get(table);
            s += "      <LI><B>" + table.getName() + "</B>\n";
            s += "          <UL>\n";
            s += "              " + this.createReportForState("WRITTEN", cgr.getWritten(),
                    WARN_COLOR);
            s += "              " + this.createReportForState("BLOCKED_BY_EXCLUSION",
                    cgr.getBlockedByExclusion());
            s += "              " + this.createReportForState("BLOCKED_BY_MISSING_INCLUSION_"
                    + "MARK", cgr.getBlockedByMissingInclusionMark());
            s += "              " + this.createReportForState("DEPRECATED_SOURCE_CODE_DETECTED",
                    cgr.getDeprecatedSourceCode());
            s += "              " + this.createReportForState("UNCHANGED", cgr.getUnchanged());
            s += "              " + this.createReportForState("UNCHANGED_BY_TAG",
                    cgr.getUnchangedByTag());
            s += "          </UL>\n";
            deprecatedFilesDetected = deprecatedFilesDetected
                    || (cgr.getDeprecatedSourceCode().length > 0);
        }
        s += "    </UL>\n";
        if ((this.doubleClassesChecker != null) && (this.doubleClassesChecker.hasWarnings())) {
            s += "    <HR>\n";
            s += "    <H2>" + this.guiBundle.getResourceText(
                    "generation.process.double.files.title") + "</H2>\n";
            s += "    <UL>\n";
            for (String name : this.doubleClassesChecker.getFileNames()) {
                String[] pathes = this.doubleClassesChecker.getFilePathes(name);
                if (pathes.length > 1) {
                    s += "      <LI><B>" + name + "</B>\n";
                    s += "      <UL>\n";
                    for (String path : pathes) {
                        s += "             <LI>" + path + "</LI>\n";
                    }
                    s += "      </UL>\n";
                }
            }
            s += "    </UL>\n";
        }
        if (deprecatedFilesDetected) {
            s += "    <HR>\n";
            s += "    <H2>" + this.guiBundle.getResourceText(
                    "generation.process.deprecated.files.detected.title") + "</H2>\n";
            s += "    <UL>\n";
            List<String> lines = new SortedVector<String>();
            for (TableModel table : this.sortTables(this.generatorReports.keySet())) {
                CodeGeneratorReport cgr = this.generatorReports.get(table);
                for (CodeGeneratorReportTableEntry cgre : cgr.getDeprecatedSourceCode()) {
                    lines.add("      <LI><B>" + cgre.getFileName() + "</B>\n");
                }
            }
            for (String line : lines) {
                s += line;
            }
            s += "    </UL>\n";
        }
        s += "</HTML>";
        return s;
    }

    private String[] getAllWrittenFileNames() {
        List<String> l = new LinkedList<String>();
        for (TableModel table : this.sortTables(this.generatorReports.keySet())) {
            CodeGeneratorReport cgr = this.generatorReports.get(table);
            for (CodeGeneratorReportTableEntry cgrte : cgr.getWritten()) {
                if (!l.contains(cgrte.getFileName())) {
                    l.add(cgrte.getFileName());
                }
            }
        }
        return l.toArray(new String[0]);
    }

    private TableModel[] sortTables(Collection<TableModel> tables) {
        SortedVector<TableModel> result = new SortedVector<TableModel>();
        for (TableModel table : tables) {
            result.add(table);
        }
        return result.toArray(new TableModel[0]);
    }

    private String createReportForState(String stateName,
            CodeGeneratorReportTableEntry[] entries) {
        return this.createReportForState(stateName, entries, null);
    }

    private String createReportForState(String stateName,
            CodeGeneratorReportTableEntry[] entries, Color highLightColor) {
        String s = "";
        if (entries.length > 0) {
            s += "          <LI><I>" + this.createColorTag(highLightColor)
                    + this.guiBundle.getResourceText("generation.process.report.state."
                    + stateName + ".label") + ": " + (highLightColor != null ? "</FONT></B>"
                    : "") + "</I>\n";
            s += this.listToString(this.getCodeGeneratorNamesSorted(entries));
            s += "</LI>\n";
        }
        return s;
    }

    private String createColorTag(Color c) {
        String t = "";
        if (c != null) {
            t = "<B><FONT COLOR=\"rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue()
                    + ")\">";
        }
        return t;
    }

    private List<String> getCodeGeneratorNamesSorted(CodeGeneratorReportTableEntry[] entries) {
        SortedVector<String> sv = new SortedVector<String>();
        for (CodeGeneratorReportTableEntry entry : entries) {
            sv.add(this.getCodeGeneratorName(entry.getCodeGenerator()));
        }
        return sv;
    }

    private String getCodeGeneratorName(CodeGenerator codeGenerator) {
        return this.guiBundle.getResourceText("code.generators.configuration.name."
                + codeGenerator.getClass().getSimpleName() + ".title");

    }

    private String listToString(List<String> l) {
        String s = "";
        for (String s0 : l) {
            if (s.length() > 0) {
                s += ", ";
            }
            s += s0;
        }
        return s;
    }

}