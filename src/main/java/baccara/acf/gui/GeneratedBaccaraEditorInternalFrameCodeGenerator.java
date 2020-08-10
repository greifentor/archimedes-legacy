/*
 * GeneratedBaccaraEditorInternalFrameCodeGenerator.java
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
import archimedes.legacy.model.NReferenceModel;
import archimedes.legacy.model.OptionModel;
import archimedes.legacy.model.PanelModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;
import baccara.acf.*;
import baccara.acf.ColParamIds;
import baccara.acf.PanelParamIds;
import baccara.acf.TableParamIds;
import baccara.acf.entities.*;
import gengen.util.*;

/**
 * A code generator for the Baccara editor internal frame.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 05.07.2016 - Added.
 */

public class GeneratedBaccaraEditorInternalFrameCodeGenerator extends BaccaraBaseCodeGenerator {

    private static GeneratedBaccaraEditorInternalFrameCodeGenerator instance =
            new GeneratedBaccaraEditorInternalFrameCodeGenerator();

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
        GeneratorResult gr = new GeneratorResult();
        this.imports.add(BaccaraEditorPanelCodeGenerator.packageName(tm));
        this.imports.add(POJOCodeGenerator.packageName(tm));
        String pojoClassName = POJOCodeGenerator.className(tm);
        String pojoAttrName = this.getAttributeName(pojoClassName);
        String resourceId = tm.getName() + "EditorDialog";
        TableModel parentTable = this.getParentTable(tm);
        this.imports.add("baccara.gui.generics");
        String code = "public class " + className + " extends BasicEditorInternalFrame<"
                + pojoClassName + "> {\n\n";
        code += "    protected " + pojoClassName + " " + pojoAttrName + " = null;\n"; 
        code += "    protected BaccaraDataProvider dataProvider = null;\n";
        for (PanelModel pm : tm.getPanels()) {
            String editorPanelClassName = this.getEditorPanelClassName(pm, tm, parentTable);
            code += "    protected " + editorPanelClassName + " " + this.getEditorPanelName(pm
                    ) + " = null;\n";
        }
        code += "\n";
        code += this.createConstructor(codeUtil, tm, className, pojoClassName, pojoAttrName,
                resourceId);
        this.createCreateContentPanel(tm, pojoAttrName, resourceId, gr, parentTable);
        this.createGetEditedObject(pojoClassName, pojoAttrName);
        this.createTransferData(pojoClassName, pojoAttrName, tm);
        this.createTransferDataUnchecked(pojoClassName, tm);
        gr.setCode(code);
        gr.setState(GeneratorResultState.SUCCESS);
        return gr;
    }

    private TableModel getParentTable(TableModel tm) {
        if (tm.isOptionSet(TableParamIds.INHERITS)) {
            String ptn = tm.getOptionByName(TableParamIds.INHERITS).getParameter();
            TableModel ptm = tm.getDataModel().getTableByName(ptn);
            this.ensure(ptm != null, BaccaraCodeFactory.NAME, "Inherited table name '" + ptn
                    + "' is not existing in " + "data model!",
                    "InheritedTableInPanelIsNotExisting", ptn);
            return this.getParentTable(ptm);
        }
        return tm;
    }

    private String getEditorPanelClassName(PanelModel pm, TableModel tm, TableModel parentTable)
            {
        NReferenceModel nrm = tm.getNReferenceForPanel(pm);
        OptionModel om = pm.getOptionByName(PanelParamIds.INHERITED);
        if (om != null) {
            if ((om.getParameter() != null) && !om.getParameter().isEmpty()) {
                TableModel t = tm.getDataModel().getTableByName(om.getParameter());
                pm = t.getPanelByName(pm.getTabTitle());
                nrm = t.getNReferenceForPanel(pm);
            }
        }
        if (nrm != null) {
            ListMaintenancePanelCodeGenerator g = new ListMaintenancePanelCodeGenerator();
            TableModel t = tm;
            if (om != null) {
                if ((om.getParameter() != null) && !om.getParameter().isEmpty()) {
                    t = tm.getDataModel().getTableByName(om.getParameter());
                } else {
                    this.ensure(parentTable != null, BaccaraCodeFactory.NAME, "INHERITED "
                            + "option set for table with no parent: table=" + tm.getName()
                            + ", panel=" + pm.getTabTitle(),
                            "InheritedOptionIsSetForTableWithNoParent", tm.getName(),
                            pm.getTabTitle());
                    t = parentTable;
                }
            }
            g.setCurrentPanel(pm);
            this.imports.add(g.getPackageName(t));
            return g.getClassName(t);
        }
        BaccaraEditorPanelCodeGenerator g = new BaccaraEditorPanelCodeGenerator();
        TableModel t = tm;
        if (om != null) {
            if ((om.getParameter() != null) && !om.getParameter().isEmpty()) {
                t = tm.getDataModel().getTableByName(om.getParameter());
            } else {
                this.ensure(parentTable != null, BaccaraCodeFactory.NAME, "INHERITED "
                        + "option set for table with no parent: table=" + tm.getName()
                        + ", panel=" + pm.getTabTitle(),
                        "InheritedOptionIsSetForTableWithNoParent", tm.getName(),
                        pm.getTabTitle());
                t = parentTable;
            }
        }
        g.setCurrentPanel(pm);
        this.imports.add(g.getPackageName(t));
        return g.getClassName(t);
    }

    private String getEditorPanelName(PanelModel pm) {
        return "editorPanel" + this.setFirstLetterToUpperCase(this.getAttributeName(
                pm.getTabTitle()));
    }

    private String createConstructor(CodeUtil codeUtil, TableModel tm, String className,
            String pojoClassName, String pojoAttrName, String resourceId) {
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
        ColumnModel na = this.getNameAttribute(tm);
        code += "    public " + className + "(JDesktopPane desktop, GUIBundle guiBundle, "
                    + pojoClassName + " " + pojoAttrName + ", BaccaraDataProvider dataProvider"
                    + ") {\n";
        code += "        super(desktop, guiBundle, \"" + resourceId + "\", "
                + (na != null ? "(" + pojoAttrName + "." + this.createGetterName(na)
                + "() == null ? guiBundle.getResourceText(\"new.object.title\") : "
                + pojoAttrName + "." + this.createGetterName(na) + "())" : "\"\"") + ");\n";
        code += "        this.dataProvider = dataProvider;\n";
        code += "        this." + pojoAttrName + " = " + pojoAttrName + ";\n";
        code += "        this.updateContentPaneAndSetToVisible();\n";
        code += "    }\n";
        code += "\n";
        return code;
    }

    private ColumnModel getNameAttribute(TableModel t) {
        for (ColumnModel c : t.getColumns()) {
            if (c.getOptionByName(ColParamIds.NAME_ATTRIBUTE) != null) {
                return c;
            }
        }
        if (t.isOptionSet(TableParamIds.INHERITS)) {
            return this.getNameAttribute(this.getParentTable(t));
        }
        return null;
    }

    private void createCreateContentPanel(TableModel tm, String pojoAttrName,
            String resourceId, GeneratorResult gr, TableModel parentTable) {
        String methodName = "createContentPanel";
        String code = this.createOverrideComment("Generated");
        code += "    @Override protected JPanel " + methodName + "() {\n";
        if (tm.getPanels().length == 1) {
            PanelModel pm = tm.getPanels()[0];
            BaccaraEditorPanelCodeGenerator g = new BaccaraEditorPanelCodeGenerator();
            g.setCurrentPanel(pm);
            String editorPanelName = this.getEditorPanelName(pm);
            String editorPanelClassName = g.getClassName(tm);
            code += "        this." + editorPanelName + " = new " + editorPanelClassName
                    + "(this.guiBundle, this, \"" + resourceId + "\", this, this."
                    + pojoAttrName + ", this.dataProvider);\n";
            code += "        return this." + editorPanelName + ";\n";
        } else if (tm.getPanels().length > 1) {
            for (PanelModel pm : tm.getPanels()) {
                String editorPanelName = this.getEditorPanelName(pm);
                String editorPanelClassName = this.getEditorPanelClassName(pm, tm, parentTable);
                code += "        this." + editorPanelName + " = new " + editorPanelClassName
                        + "(this.guiBundle, this, \"" + resourceId + "\", this, this."
                        + pojoAttrName + ", this.dataProvider);\n";
            }
            code += "        JTabbedPane tabbedPane = new JTabbedPane();\n";
            for (PanelModel pm : tm.getPanels()) {
                String editorPanelName = this.getEditorPanelName(pm);
                String rid = resourceId + ".tab." + this.getAttributeName(pm.getTabTitle())
                        + ".title";
                code += "        tabbedPane.add(this.guiBundle.getResourceText(this.guiBundle."
                        + "getResourceIdentifier(\""
                        + rid + "\", \"General.tab." + this.getAttributeName(pm.getTabTitle())
                        + ".title\")), this." + editorPanelName + ");\n";
                // gr.addResourceId(rid, pm.getTabTitle());
            }
            code += "        JPanel p = new JPanel(this.guiBundle.createBorderLayout());\n";
            this.imports.add("java.awt");
            boolean scrollable = tm.isOptionSet(TableParamIds.SCROLLABLE);
            code += "        p.add(" + (scrollable ? "new JScrollPane(" : "") + "tabbedPane" +
                    (scrollable ? ")" : "") + ", BorderLayout.CENTER);\n";
            code += "        return p;\n";
        }
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createGetEditedObject(String pojoClassName, String pojoAttrName) {
        String methodName = "getEditedObject";
        String code = this.createOverrideComment("Generated");
        code += "    @Override protected " + pojoClassName + " " + methodName + "() {\n";
        code += "        return this." + pojoAttrName + ";\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createTransferData(String pojoClassName, String pojoAttrName, TableModel tm) {
        String methodName = "transferData";
        String code = this.createOverrideComment("Generated");
        code += "    @Override protected boolean " + methodName + "(" + pojoClassName
                + " t) {\n";
        this.imports.add("baccara.gui.generics");
        code += "        EditorPanelConsistencyViolationList vl = new "
                + "EditorPanelConsistencyViolationList();\n"; 
        code += "        try {\n";
        for (PanelModel pm : tm.getPanels()) {
            String editorPanelName = this.getEditorPanelName(pm);
            if (pm.isOptionSet(PanelParamIds.FORM)) {
                code += "            vl.add(this." + editorPanelName + ".transferChanges(t));"
                        + "\n";
            } else {
                code += "            this." + editorPanelName + ".transferChanges(t);\n";
            }
        }
        this.imports.add("baccara.gui.exceptions");
        code += "        } catch (BaccaraApplicationException e) {\n";
        this.imports.add("javax.swing");
        code += "            JOptionPane.showMessageDialog(this, "
                + "this.guiBundle.getResourceText(e.getMessage() + \".text\", e.getObjects()), "
                + "this.guiBundle.getResourceText(this.guiBundle.getResourceIdentifier("
                + "e.getMessage() + \".title\", \"errors.general.transferData.title\")), "
                + "JOptionPane.ERROR_MESSAGE);\n";
        code += "            return false;\n";
        code += "        }\n";
        code += "        return this.checkForErrors(vl.getContent());\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createTransferDataUnchecked(String pojoClassName, TableModel tm) {
        String methodName = "transferDataUnchecked";
        String code = this.createOverrideComment("Generated");
        code += "    protected void " + methodName + "(" + pojoClassName
                + " t) {\n";
        this.imports.add("baccara.gui.generics");
        for (PanelModel pm : tm.getPanels()) {
            String editorPanelName = this.getEditorPanelName(pm);
            code += "        this." + editorPanelName + ".transferChanges"
                    + (pm.isOptionSet(PanelParamIds.FORM) ? "Unchecked" : "") + "(t);\n";
        }
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return "GeneratedBaccaraEditorInternalFrame" + tm.getName();
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
        return "gui.archgen";
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