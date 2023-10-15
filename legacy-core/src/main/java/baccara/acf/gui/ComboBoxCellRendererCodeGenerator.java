/*
 * ComboBoxCellRendererCodeGenerator.java
 *
 * 26.07.2016
 *
 * (c) by O.Lieshoff
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
 * A code generator for combo box renderer in Baccara applications.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 26.07.2016 - Added.
 */

public class ComboBoxCellRendererCodeGenerator extends BaccaraBaseCodeGenerator {

    private static ComboBoxCellRendererCodeGenerator instance =
            new ComboBoxCellRendererCodeGenerator();

    /**
     * Returns the package name for the code generator for the passed table.
     *
     * @param table The table which the package name is to create for.
     * @return The package name for the code generator for the passed table.
     *
     * @changed OLI 26.07.2016 - Added.
     */
    public static String packageName(TableModel table) {
        return instance.getPackageName(table);
    }

    /**
     * @changed OLI 26.07.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "An editor panel for the " + this.getDescription(tm) + " data.";
    }

    /**
     * @changed OLI 26.07.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        GeneratorResult gr = new GeneratorResult("", GeneratorResultState.NOT_NECESSARY);
        if (tm.getComboStringMembers().length > 0) {
            this.imports.add("baccara.gui.components");
            String code = "public class " + className + " extends AbstractComboBoxCellRenderer<"
                    + "Object> {\n\n";
            this.createGetRenderedStringMethod(tm);
            gr.setCode(code);
            gr.setState(GeneratorResultState.SUCCESS);
        }
        return gr;
    }

    private void createGetRenderedStringMethod(TableModel tm) {
        String methodName = "getRenderedString";
        this.imports.add("baccara.gui.generics");
        this.imports.add(POJOCodeGenerator.packageName(tm));
        String pojoClassName = POJOCodeGenerator.className(tm);
        String code = this.createOverrideComment();
        code += "    @Override public String " + methodName + "(Object o) {\n";
        code += "        if (o instanceof " + pojoClassName + ") {\n";
        code += "            " + pojoClassName + " p = (" + pojoClassName + ") o;\n";
        code += "            return " + this.createComboboxStringCodeFragment(tm, "p") + ";\n";
        code += "        }\n";
        code += "        return NoSelection.OBJECT.toString();\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    /**
     * @changed OLI 26.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return tm.getName() + "ComboBoxRenderer";
    }

    /**
     * @changed OLI 26.07.2016 - Added.
     */
    @Override public String getSubPackage() {
        return "gui.renderer";
    }

    /**
     * @changed OLI 26.07.2016 - Added.
     */
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.TABLE;
    }

    /**
     * @changed OLI 26.07.2016 - Added.
     */
    @Override public boolean isOneTimeFactory() {
        return true;
    }

    /**
     * @changed OLI 20.07.2017 - Added.
     */
    @Override public boolean isSuppressGeneration(DataModel dm, TableModel tm,
            CodeUtil codeUtil) {
        if (tm.isOptionSet(TableParamIds.NO_SELECTION_VIEW)
                || tm.isOptionSet(TableParamIds.NO_COMBOBOX_RENDERER)) {
            return true;
        }
        return false;
    }

}