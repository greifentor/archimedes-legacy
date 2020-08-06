/*
 * EditorListManagerCodeGenerator.java
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
 * A code generator which creates an editor list manager implementation.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 05.07.2016 - Added.
 */

public class EditorListManagerCodeGenerator extends BaccaraBaseCodeGenerator {

    private static EditorListManagerCodeGenerator instance =
            new EditorListManagerCodeGenerator();

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
        return "An editor list manager implementation for " + this.getDescription(tm) + "s.";
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        this.imports.add(POJOCodeGenerator.packageName(tm));
        this.imports.add("baccara.gui.generics");
        String pojoClassName = POJOCodeGenerator.className(tm);
        String listManagerClassName = pojoClassName + "ListManager";
        String code = "public class " + className + " implements EditorListManager<"
                + pojoClassName + "> {\n\n";
        code += "    private " + listManagerClassName + " lm = null;\n\n";
        code += this.createConstructor(tm, className, listManagerClassName);
        this.createAddElement(pojoClassName);
        this.createGetElementAt(tm, pojoClassName);
        this.createRemoveElement(pojoClassName);
        this.createSize(tm, pojoClassName);
        return new GeneratorResult(code, GeneratorResultState.SUCCESS);
    }

    private String createConstructor(TableModel tm, String className,
            String listManagerClassName) {
        String code = "    /**\n";
        code += "     * Creates a new editor list manager for " + this.getDescription(tm)
                + "s.\n";
        code += "     *\n";
        code += "     * " + this.createParameterTag("lm", "The list manager to be made "
                + "editable.") + "\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    public " + className + "(" + listManagerClassName + " lm) {\n";
        code += "        super();\n";
        code += "        this.lm = lm;\n";
        code += "    }\n";
        code += "\n";
        return code;
    }

    private void createAddElement(String pojoClassName) {
        String methodName = "addElement";
        String code = this.createOverrideComment("Generated");
        code += "    @Override public boolean " + methodName + "(" + pojoClassName + " o) {\n";
        code += "        this.lm.add" + pojoClassName + "(o);\n";
        code += "        return true;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createGetElementAt(TableModel tm, String pojoClassName) {
        String methodName = "getElementAt";
        String code = this.createOverrideComment("Generated");
        code += "    @Override public " + pojoClassName + " " + methodName + "(int pos) {\n";
        code += "        return this.lm.get" + pojoClassName + "s("
                + (tm.getOptionByName(TableParamIds.SEPARATED_DATA_STOCK) != null ? "null" : "")
                + ")[pos];\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createRemoveElement(String pojoClassName) {
        String methodName = "removeElement";
        String code = this.createOverrideComment("Generated");
        code += "    @Override public boolean " + methodName + "(" + pojoClassName + " o) {\n";
        code += "        this.lm.remove" + pojoClassName + "(o);\n";
        code += "        return true;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createSize(TableModel tm, String pojoClassName) {
        String methodName = "size";
        String code = this.createOverrideComment("Generated");
        code += "    @Override public int " + methodName + "() {\n";
        code += "        return this.lm.get" + pojoClassName + "s("
                + (tm.getOptionByName(TableParamIds.SEPARATED_DATA_STOCK) != null ? "null" : "")
                + ").length;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return tm.getName() + "EditorListManager";
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public String[] getExcludeMarks() {
        return new String[] {TableParamIds.NO_GUI, TableParamIds.NO_SELECTION_VIEW};
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
        if (tm.isOptionSet(TableParamIds.INHERITS)) {
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