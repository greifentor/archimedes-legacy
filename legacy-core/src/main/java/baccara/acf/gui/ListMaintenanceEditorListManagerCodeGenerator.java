/*
 * ListMaintenanceEditorListManagerCodeGenerator.java
 *
 * 15.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.gui;

import java.util.*;

import archimedes.acf.*;
import archimedes.acf.report.*;
import archimedes.model.*;
import baccara.acf.entities.*;
import gengen.util.*;

/**
 * A code generator for the editor list managers for list maintenance panels.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 15.07.2016 - Added.
 */

public class ListMaintenanceEditorListManagerCodeGenerator
        extends GeneratedListMaintenanceEditorListManagerCodeGenerator {

    private static ListMaintenanceEditorListManagerCodeGenerator instance =
            new ListMaintenanceEditorListManagerCodeGenerator();

    /**
     * Returns the package name for the code generator for the passed table.
     *
     * @param table The table which the package name is to create for.
     * @return The package name for the code generator for the passed table.
     *
     * @changed OLI 15.07.2016 - Added.
     */
    public static String packageName(TableModel table) {
        return instance.getPackageName(table);
    }

    /**
     * @changed OLI 15.07.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "An editor list manager for the " + this.getDescription(tm) + " data.";
    }

    /**
     * @changed OLI 15.07.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        NReferenceModel nrm = tm.getNReferenceForPanel(this.currentPanelModel);
        GeneratorResult gr = new GeneratorResult("", GeneratorResultState.NOT_NECESSARY);
        if (nrm != null) { 
            TableModel rtm = nrm.getColumn().getTable();
            this.imports.add(POJOCodeGenerator.packageName(tm));
            this.imports.add(POJOCodeGenerator.packageName(rtm));
            GeneratedListMaintenanceEditorListManagerCodeGenerator cg =
                    new GeneratedListMaintenanceEditorListManagerCodeGenerator();
            cg.setCurrentPanel(this.currentPanelModel);
            this.imports.add(cg.getPackageName(tm));
            String superClassName = cg.getClassName(tm);
            String pojoClassName = POJOCodeGenerator.className(tm);
            String pojoListEntryClassName = POJOCodeGenerator.className(rtm);
            String code = "public class " + className + " extends " + superClassName + " {\n\n";
            code += this.createConstructor(tm, rtm, this.getAttributeName(pojoClassName),
                    className, pojoClassName, pojoListEntryClassName);
            gr.setCode(code);
            gr.setState(GeneratorResultState.SUCCESS);
        }
        return gr;
    }

    private String createConstructor(TableModel tm, TableModel rtm, String pojoAttrName,
            String className, String pojoClassName, String pojoListEntryClassName) {
        String code = "    /**\n";
        code += "     * Creates a new editor list manager for a " + this.getDescription(rtm)
                + " list view in a " + this.getDescription(tm) + " maintenance panel.\n";
        code += "     *\n";
        code += "     * " + this.createParameterTag(pojoAttrName, "The object which owns the "
                + "list") + "\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    public " + className  + "(" + pojoClassName + " " + pojoAttrName + ") {\n";
        code += "        super(" + pojoAttrName + ");\n";
        code += "    }\n";
        code += "\n";
        return code;
    }

    /**
     * @changed OLI 15.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        NReferenceModel nrm = tm.getNReferenceForPanel(this.currentPanelModel);
        if (nrm == null) {
            return tm.getName() + "XXXXX" + "EditorListManager";
        }
        return tm.getName() + nrm.getColumn().getTable().getName() + "EditorListManager";
    }

    /**
     * @changed OLI 15.07.2016 - Added.
     */
    @Override public String getSubPackage() {
        return "gui.business";
    }

    /**
     * @changed OLI 15.07.2016 - Added.
     */
    @Override public boolean isOneTimeFactory() {
        return true;
    }

}