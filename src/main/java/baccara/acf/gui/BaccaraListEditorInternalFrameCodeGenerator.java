/*
 * BaccaraListEditorInternalFrameCodeGenerator.java
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
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;
import baccara.acf.*;
import baccara.acf.entities.*;
import gengen.util.*;

/**
 * A code generator for the list editor internal frames.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 05.07.2016 - Added.
 */

public class BaccaraListEditorInternalFrameCodeGenerator extends BaccaraBaseCodeGenerator {

    private static BaccaraListEditorInternalFrameCodeGenerator instance =
            new BaccaraListEditorInternalFrameCodeGenerator();

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
        return "An internal frame for " + this.getDescription(tm) + " maintenance.";
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        GeneratorResult gr = new GeneratorResult();
        this.imports.add(POJOCodeGenerator.packageName(tm));
        this.imports.add("baccara.gui.generics");
        String pojoClassName = POJOCodeGenerator.className(tm);
        String listManagerClassName = pojoClassName + "ListManager";
        String code = "public class " + className + " extends BasicListEditorInternalFrame<"
                + pojoClassName + "> {\n\n";
        code += "    private BaccaraDataProvider dataProvider = null;\n\n";
        code += this.createConstructor(tm, className, listManagerClassName, gr);
        this.createCreateNewElement(pojoClassName);
        this.createEditElement(tm, pojoClassName);
        gr.setCode(code);
        gr.setState(GeneratorResultState.SUCCESS);
        return gr;
    }

    private String createConstructor(TableModel tm, String className,
            String listManagerClassName, GeneratorResult gr) {
        String code = "    /**\n";
        code += "     * Creates a new internal frame for " + this.getDescription(tm) + " list "
                + "maintenance.\n";
        code += "     *\n";
        code += "     * " + this.createParameterTag("desktop", "The desktop which the list "
                + "editor internal frame is to be shown on.") + "\n";
        code += "     * " + this.createParameterTag("guiBundle", "A bundle with GUI "
                + "information.") + "\n";
        code += "     * " + this.createParameterTag("lm", "The object which manages the list "
                + "which is to maintain in the frame.") + "\n";
        code += "     * " + this.createParameterTag("dataProvider", "A provider for additional "
                + "data.") + "\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        this.imports.add("baccara.gui");
        this.imports.add("javax.swing");
        this.imports.add(EditorListManagerCodeGenerator.packageName(tm));
        this.imports.add(BaccaraListCellRendererCodeGenerator.packageName(tm));
        String editorListManagerClassName = EditorListManagerCodeGenerator.className(tm);
        String listCellRendererClassName = BaccaraListCellRendererCodeGenerator.className(tm);
        String resourcePrefix = tm.getName() + "ListEditor";
        code += "    public " + className + "(JDesktopPane desktop, GUIBundle guiBundle, "
                + listManagerClassName + " lm, BaccaraDataProvider dataProvider) {\n";
        code += "        super(desktop, guiBundle, \"" + resourcePrefix + "\", new "
                + editorListManagerClassName + "(lm), new " + listCellRendererClassName
                + "());\n";
        gr.addResourceId(resourcePrefix + ".title.text", this.getPlural(tm.getName()));
        code += "        this.dataProvider = dataProvider;\n";
        code += "    }\n";
        code += "\n";
        return code;
    }

    private void createCreateNewElement(String pojoClassName) {
        String methodName = "createNewElement";
        String code = this.createOverrideComment("Generated");
        code += "    @Override protected " + pojoClassName + " " + methodName + "() {\n";
        code += "        " + pojoClassName + " o = new " + pojoClassName + "();\n";
        code += "        return o;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createEditElement(TableModel tm, String pojoClassName) {
        String methodName = "editElement";
        String code = this.createOverrideComment("Generated");
        this.imports.add(BaccaraEditorInternalFrameCodeGenerator.packageName(tm));
        String editorFrameClassName = BaccaraEditorInternalFrameCodeGenerator.className(tm);
        ColumnModel pk = tm.getPrimaryKeyColumns()[0];
        code += "    @Override protected void " + methodName + "(" + pojoClassName + " o) {\n";
        code += "        new " + editorFrameClassName + "(this.getDesktopPane(), "
                + "this.guiBundle, o, this.dataProvider) {\n";
        code += "            @Override protected boolean transferData("  + pojoClassName
                + " o) {\n";
        code += "                removeElement(o);\n";
        code += "                boolean transfered = super.transferData(o);\n";
        code += "                if (transfered || (o." + this.createGetterName(pk) + "() > 0))"
                + " {\n";
        code += "                    addElement(o);\n";
        code += "                }\n";
        code += "                return transfered;\n";
        code += "            }\n";
        code += "        };\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return "BaccaraListEditorInternalFrame" + tm.getName();
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
        if (tm.isOptionSet(TableParamIds.INHERITS)) {
            return true;
        }
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