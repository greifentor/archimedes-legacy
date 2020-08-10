/*
 * ListMaintenancePanelCodeGenerator.java
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
 * @changed OLI 15.07.2016 - Added.
 */

public class ListMaintenancePanelCodeGenerator
        extends GeneratedListMaintenancePanelCodeGenerator {

    private static GeneratedListMaintenancePanelCodeGenerator instance =
            new GeneratedListMaintenancePanelCodeGenerator();

    /**
     * Returns the package name for the code generator for the passed table.
     *
     * @param table The table which the package name is to create for.
     * @return The package name for the code generator for the passed table.
     *
     * @changed OLI 13.07.2016 - Added.
     */
    public static String packageName(TableModel table) {
        return instance.getPackageName(table);
    }

    /**
     * @changed OLI 13.07.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "A panel to maintain a list of member object of a(n) " + this.getDescription(tm)
                + ".";
    }

    /**
     * @changed OLI 13.07.2016 - Added.
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
            String pojoClassName = POJOCodeGenerator.className(tm);
            String pojoAttrName = this.getAttributeName(pojoClassName);
            GeneratedListMaintenancePanelCodeGenerator cg =
                    new GeneratedListMaintenancePanelCodeGenerator();
            cg.setCurrentPanel(this.currentPanelModel);
            this.imports.add(cg.getPackageName(tm));
            String superClassName = cg.getClassName(tm);
            this.imports.add("baccara.gui.generics");
            String code = "public class " + className + " extends " + superClassName + " {\n\n";
            code += this.createConstructor(tm, rtm, className, pojoClassName, pojoAttrName);
            gr.setCode(code);
            gr.setState(GeneratorResultState.SUCCESS);
        }
        return gr;
    }

    private String createConstructor(TableModel tm, TableModel rtm,
            String className, String pojoClassName, String pojoAttrName) {
        String code = "    /**\n";
        code += "     * Creates a new panel to maintain "
                + this.getDescription(rtm) + "s for a(n) " + this.getDescription(tm) + ".\n";
        code += "     *\n";
        code += "     * " + this.createParameterTag("guiBundle", "A bundle with GUI "
                + "information.") + "\n";
        code += "     * " + this.createParameterTag("parent", "The parent container of the "
                + "panel.") + "\n";
        code += "     * " + this.createParameterTag("resourcePrefix", "A prefix for resources "
                + "used by the panel.") + "\n";
        code += "     * " + this.createParameterTag("actionListener", "An action listener "
                + "which observes the panels components.") + "\n";
        code += "     * " + this.createParameterTag(pojoAttrName, "The " + this.getDescription(
                tm) + " whose " + this.getDescription(rtm) + "s are maintained.") + "\n";
        code += "     * " + this.createParameterTag("dataProvider", "An access for additional "
                + "data.") + "\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        this.imports.add("baccara.gui");
        this.imports.add("java.awt");
        this.imports.add("java.awt.event");
        code += "    public " + className  + "(GUIBundle guiBundle, Container parent, "
                + "String resourcePrefix, ActionListener actionListener, " + pojoClassName
                + " " + pojoAttrName + ", BaccaraDataProvider dataProvider) {\n";
        code += "        super(guiBundle, parent, resourcePrefix, actionListener, "
                + pojoAttrName + ", dataProvider);\n";
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
            return "BaccaraEditorPanel" + tm.getName() + "XXXXX";
        }
        return "BaccaraEditorPanel" + tm.getName() + nrm.getColumn().getTable().getName()
                + "s";
    }

    /**
     * @changed OLI 13.07.2016 - Added.
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