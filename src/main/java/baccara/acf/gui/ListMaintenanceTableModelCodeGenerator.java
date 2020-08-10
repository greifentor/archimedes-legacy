/*
 * ListMaintenanceTableModelCodeGenerator.java
 *
 * 14.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.gui;

import java.util.*;

import archimedes.acf.*;
import archimedes.acf.report.*;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.NReferenceModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;
import baccara.acf.entities.*;
import gengen.util.*;

/**
 * A code generator for list maintenance panels.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.07.2016 - Added.
 */

public class ListMaintenanceTableModelCodeGenerator
        extends GeneratedListMaintenanceTableModelCodeGenerator {

    private static ListMaintenanceTableModelCodeGenerator instance =
            new ListMaintenanceTableModelCodeGenerator();

    /**
     * Returns the package name for the code generator for the passed table.
     *
     * @param table The table which the package name is to create for.
     * @return The package name for the code generator for the passed table.
     *
     * @changed OLI 14.07.2016 - Added.
     */
    public static String packageName(TableModel table) {
        return instance.getPackageName(table);
    }

    /**
     * @changed OLI 14.07.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "An editor panel for the " + this.getDescription(tm) + " data.";
    }

    /**
     * @changed OLI 14.07.2016 - Added.
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
            GeneratedListMaintenanceTableModelCodeGenerator cg =
                    new GeneratedListMaintenanceTableModelCodeGenerator();
            cg.setCurrentPanel(this.currentPanelModel);
            this.imports.add(cg.getPackageName(tm));
            String superClassName = cg.getClassName(tm);
            String pojoClassName = POJOCodeGenerator.className(tm);
            String pojoListEntryClassName = POJOCodeGenerator.className(rtm);
            String code = "public class " + className + " extends " + superClassName + " {\n\n";
            code += this.createConstructor(tm, rtm, className, pojoClassName,
                    pojoListEntryClassName);
            gr.setCode(code);
            gr.setState(GeneratorResultState.SUCCESS);
        }
        return gr;
    }

    private String createConstructor(TableModel tm, TableModel rtm,
            String className, String pojoClassName, String pojoListEntryClassName) {
        String pn = this.getAttributeName(pojoClassName);
        String code = "    /**\n";
        code += "     * Creates a new table model for a " + this.getDescription(rtm) + " list "
                + "view in a " + this.getDescription(tm) + " maintenance panel.\n";
        code += "     *\n";
        code += "     * " + this.createParameterTag("guiBundle", "A bundle with GUI "
                + "information.") + "\n";
        code += "     * " + this.createParameterTag(pn, "The object which provides the list.")
                + "\n";
        code += "     * " + this.createParameterTag("rowObjects", "The object for the rows of "
                + "the list view.") + "\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        this.imports.add("baccara.gui");
        code += "    public " + className  + "(GUIBundle guiBundle, " + pojoClassName + " " + pn
                + ", " + pojoListEntryClassName + "[] rowObjects) {\n";
        code += "        super(guiBundle, " + pn + ", rowObjects);\n";
        code += "    }\n";
        code += "\n";
        return code;
    }

    /**
     * @changed OLI 13.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        NReferenceModel nrm = tm.getNReferenceForPanel(this.currentPanelModel);
        if (nrm == null) {
            return tm.getName() + "XXXXX" + "TableModel";
        }
        return tm.getName() + nrm.getColumn().getTable().getName() + "TableModel";
    }

    /**
     * @changed OLI 13.07.2016 - Added.
     */
    @Override public String getSubPackage() {
        return "gui.business";
    }

    /**
     * @changed OLI 14.07.2016 - Added.
     */
    @Override public boolean isOneTimeFactory() {
        return true;
    }

}