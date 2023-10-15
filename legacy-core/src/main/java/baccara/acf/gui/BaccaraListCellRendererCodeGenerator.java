/*
 * BaccaraListCellRendererCodeGenerator.java
 *
 * 05.07.2016
 *
 * (c) by HealthCarion
 *
 */

package baccara.acf.gui;

import java.util.*;

import archimedes.acf.*;
import archimedes.acf.report.*;
import archimedes.model.*;
import baccara.acf.*;
import baccara.acf.entities.*;
import gengen.util.*;

/**
 * A code generator for a list cell renderer for using in Baccara applications.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 05.07.2016 - Added.
 */

public class BaccaraListCellRendererCodeGenerator extends BaccaraBaseCodeGenerator {

    private static BaccaraListCellRendererCodeGenerator instance =
            new BaccaraListCellRendererCodeGenerator();

    /**
     * Returns the class name for the code generator for the passed table.
     *
     * @param table The table which the class name is to create for.
     * @return The class name for the code generator for the passed table.
     *
     * @changed OLI 05.07.2016 - Added.
     */
    public static String className(TableModel table) {
        return instance.getClassName(table);
    }

    /**
     * Returns the package name for the code generator for the passed table.
     *
     * @param table The table which the package name is to create for.
     * @return The package name for the code generator for the passed table.
     *
     * @changed OLI 05.07.2016 - Added.
     */
    public static String packageName(TableModel table) {
        return instance.getPackageName(table);
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "An editor panel for the " + this.getDescription(tm) + " data.";
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        this.imports.add(POJOCodeGenerator.packageName(tm));
        this.imports.add("baccara.gui.components");
        String pojoClassName = POJOCodeGenerator.className(tm);
        String code = "public class " + className + " extends AbstractListCellRenderer<"
                + pojoClassName + "> {\n\n";
        this.createGetTitle(tm, pojoClassName);
        return new GeneratorResult(code, GeneratorResultState.SUCCESS);
    }

    private void createGetTitle(TableModel tm, String pojoClassName) {
        String methodName = "getTitle";
        String code = this.createOverrideComment("Generated");
        code += "    @Override public String getTitle(" + pojoClassName + " t) {\n";
        OptionModel inheritedOption = tm.getOptionByName(TableParamIds.INHERITS);
        TableModel inheritedTable = null;
        if (inheritedOption != null) {
            inheritedTable = tm.getDataModel().getTableByName(inheritedOption.getParameter());
            this.imports.add(BaccaraListCellRendererCodeGenerator.packageName(inheritedTable));
            code += "        String s = new "
                    + BaccaraListCellRendererCodeGenerator.className(inheritedTable) + "()"
                    + ".getTitle(t);\n";
        }
        String a = "";
        for (ToStringContainerModel tscm : tm.getComboStringMembers()) {
            if (a.length() > 0) {
                a += " + ";
            }
            a += "\"" + tscm.getPrefix() + "\" + t." + this.createGetterName(tscm.getColumn())
                    + "() + \"" + tscm.getSuffix() + "\"";
        }
        code += "        return " + (inheritedOption != null ? "s + " : "") + a + ";\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return "BaccaraListCellRenderer" + tm.getName();
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public String[] getExcludeMarks() {
        return new String[] {TableParamIds.NO_GUI};
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public String getSubPackage() {
        return "gui.business";
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.TABLE;
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public boolean isOneTimeFactory() {
        return true;
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public boolean isSuppressGeneration(DataModel dm, TableModel tm,
            CodeUtil codeUtil) {
        if (tm.isOptionSet(TableParamIds.NO_SELECTION_VIEW)) {
            return true;
        }
        for (ColumnModel c : tm.getColumns()) {
            if (c.isEditorMember()) {
                return false;
            }
        }
        return true;
    }

}