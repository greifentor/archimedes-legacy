/*
 * BaccaraEditorPanelCodeGenerator.java
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
 * A code generator for a Baccara editor panel (changeable version).
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 05.07.2016 - Added.
 */

public class BaccaraEditorPanelCodeGenerator extends BaccaraBaseCodeGenerator {

    private static GeneratedBaccaraEditorPanelCodeGenerator instance =
            new GeneratedBaccaraEditorPanelCodeGenerator();

    /**
     * Returns the package name for the code generator for the passed table.
     *
     * @param table The table which the package name is to create for.
     * @return The package name for the code generator for the passed table.
     *
     * @changed OLI 04.07.2016 - Added.
     */
    public static String packageName(TableModel table) {
        return instance.getPackageName(table);
    }

    /**
     * @changed OLI 04.07.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "An editor panel for the " + this.getDescription(tm) + " data.";
    }

    /**
     * @changed OLI 04.07.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        this.imports.add(GeneratedBaccaraEditorPanelCodeGenerator.packageName(tm));
        this.imports.add(POJOCodeGenerator.packageName(tm));
        GeneratedBaccaraEditorPanelCodeGenerator g =
                new GeneratedBaccaraEditorPanelCodeGenerator();
        g.setCurrentPanel(this.currentPanelModel);
        String superClassName = g.getClassName(tm);
        String pojoClassName = POJOCodeGenerator.className(tm);
        String code = "public class " + className + " extends " + superClassName + " {\n\n";
        code += this.createConstructor(codeUtil, tm, className, pojoClassName);
        return new GeneratorResult(code, GeneratorResultState.SUCCESS);
    }

    private String createConstructor(CodeUtil codeUtil, TableModel tm, String className,
            String pojoClassName) {
        String code = "    /**\n";
        code += "     * Creates a new editor panel for a " + this.getDescription(tm) + " with "
                + "the passed parameters.\n";
        code += "     *\n";
        code += "     * " + this.createParameterTag("guiBundle", "A bundle with GUI "
                + "information.") + "\n";
        code += "     * " + this.createParameterTag("parent", "A reference to the component "
                + "which the panel is a member of (usually the window or frame which contains "
                + "the panel).") + "\n";
        code += "     * " + this.createParameterTag("resourcePrefix", "A prefix for the "
                + "resources of the panel.") + "\n";
        code += "     * " + this.createParameterTag("actionListener", "An action listener to "
                + "get a chance of reaction on combo box events.") + "\n";
        code += "     * " + this.createParameterTag(this.getAttributeName(tm), "The "
                + this.getDescription(tm) + " which is edited by the panel.") + "\n";
        code += "     * " + this.createParameterTag("dataProvider", "A provider for additional "
                + "data.") + "\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        this.imports.add("baccara.gui");
        this.imports.add("java.awt");
        this.imports.add("java.awt.event");
        String pojoName = this.getAttributeName(tm);
        code += "    public " + className + "(GUIBundle guiBundle, Container parent, "
                + "String resourcePrefix, ActionListener actionListener, " + pojoClassName
                + " " + pojoName + ", BaccaraDataProvider dataProvider) {\n";
        code += "        super(guiBundle, parent, resourcePrefix, actionListener, "
                + pojoName + ", dataProvider);\n";
        code += "    }\n";
        code += "\n";
        return code;
    }

    /**
     * @changed OLI 04.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return "BaccaraEditorPanel" + tm.getName()+ this.setFirstLetterToUpperCase(
                this.currentPanelModel.getTabTitle());
    }

    /**
     * @changed OLI 04.07.2016 - Added.
     */
    @Override public String[] getExcludeMarks() {
        return new String[] {TableParamIds.NO_GUI};
    }

    /**
     * @changed OLI 04.07.2016 - Added.
     */
    @Override public String getSubPackage() {
        return "gui.business";
    }

    /**
     * @changed OLI 04.07.2016 - Added.
     */
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.PANEL;
    }

    /**
     * @changed OLI 04.07.2016 - Added.
     */
    @Override public boolean isOneTimeFactory() {
        return true;
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public boolean isSuppressGeneration(DataModel dm, TableModel tm,
            CodeUtil codeUtil) {
        NReferenceModel nrm = tm.getNReferenceForPanel(this.currentPanelModel);
        for (ColumnModel c : tm.getColumns()) {
            if (c.isEditorMember()
                    && (c.getPanel().getPanelNumber() == this.currentPanelModel.getPanelNumber()
                    )) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the passed panel as new current panel for the code generator.
     *
     * @param panel The new current panel for the code generator.
     *
     * @changed OLI 08.07.2016 - Added.
     */
    public void setCurrentPanel(PanelModel panel) {
        this.currentPanelModel = panel;
    }

}