/*
 * CodeGeneratorReportTableEntry.java
 *
 * 28.11.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.report;


import archimedes.acf.*;


/**
 * A single entry record for the code generator report.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 28.11.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class CodeGeneratorReportTableEntry {

    private CodeGenerator codeGenerator = null;
    private String fileName = null;

    /**
     * Creates a new code generator report table entry with the passed data.
     *
     * @param codeGenerator The code generator which the report entry is created for.
     * @param fileName The name of the written or ignored file.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public CodeGeneratorReportTableEntry(CodeGenerator codeGenerator, String fileName) {
        super();
        this.fileName = fileName;
        this.codeGenerator = codeGenerator;
    }

    /**
     * Returns the code generator which the entry is created for.
     *
     * @return The code generator which the entry is created for.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public CodeGenerator getCodeGenerator() {
        return this.codeGenerator;
    }

    /**
     * Returns the file name of the generated source code file.
     *
     * @return The file name of the generated source code file.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    public String getFileName() {
        return this.fileName;
    }

}