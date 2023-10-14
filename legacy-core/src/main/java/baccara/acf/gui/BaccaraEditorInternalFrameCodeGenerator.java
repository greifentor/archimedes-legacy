/*
 * BaccaraEditorInternalFrameCodeGenerator.java
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
import baccara.acf.TableParamIds;
import baccara.acf.entities.*;
import gengen.util.*;

/**
 * 
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 05.07.2016 - Added.
 */

public class BaccaraEditorInternalFrameCodeGenerator extends BaccaraBaseCodeGenerator {

    private static BaccaraEditorInternalFrameCodeGenerator instance =
            new BaccaraEditorInternalFrameCodeGenerator();

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
        return "An internal frame to edit a " + this.getDescription(tm) + ".";
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        this.imports.add(GeneratedBaccaraEditorInternalFrameCodeGenerator.packageName(tm));
        this.imports.add(POJOCodeGenerator.packageName(tm));
        String pojoClassName = POJOCodeGenerator.className(tm);
        String pojoAttrName = this.getAttributeName(pojoClassName);
        String superClassName = GeneratedBaccaraEditorInternalFrameCodeGenerator.className(tm);
        this.imports.add("javax.swing");
        this.imports.add("baccara.gui");
        String code = "public class " + className + " extends " + superClassName + " {\n\n";
        code += this.createConstructor(codeUtil, tm, className, pojoClassName, pojoAttrName);
        return new GeneratorResult(code, GeneratorResultState.SUCCESS);
    }

    private String createConstructor(CodeUtil codeUtil, TableModel tm, String className,
            String pojoClassName, String pojoAttrName) {
        String code = "    /**\n";
        code += "     * Creates a new internal frame to edit " + this.getDescription(
                pojoClassName) + "s with the passed parameters.\n";
        code += "     *\n";
        code += "     * " + this.createParameterTag("desktop", "The desktop which internal "
                + "editor frame should be shown on.") + "\n";
        code += "     * " + this.createParameterTag("guiBundle", "A bundle with GUI "
                + "information.") + "\n";
        code += "     * " + this.createParameterTag(pojoAttrName, "The " + this.getDescription(
                pojoClassName) + " which is to edit.") + "\n";
        code += "     * " + this.createParameterTag("dataProvider", "A provider for additional "
                + "data.") + "\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        this.imports.add("baccara.gui");
        this.imports.add("javax.swing");
        code += "    public " + className + "(JDesktopPane desktop, GUIBundle guiBundle, "
                    + pojoClassName + " " + pojoAttrName + ", BaccaraDataProvider dataProvider"
                    + ") {\n";
        code += "        super(desktop, guiBundle, " + pojoAttrName + ", dataProvider);\n";
        code += "    }\n";
        code += "\n";
        return code;
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return "BaccaraEditorInternalFrame" + tm.getName();
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
        for (ColumnModel c : tm.getColumns()) {
            if (c.isEditorMember()) {
                return false;
            }
        }
        return true;
    }

}