/*
 * GeneratedListMaintenancePanelCodeGenerator.java
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
import baccara.acf.*;
import baccara.acf.ColParamIds;
import baccara.acf.PanelParamIds;
import baccara.acf.TableParamIds;
import baccara.acf.entities.*;
import baccara.gui.generics.*;
import gengen.util.*;

/**
 * A code generator for list maintenance panels.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 13.07.2016 - Added.
 */

public class GeneratedListMaintenancePanelCodeGenerator extends BaccaraBaseCodeGenerator {

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
            ColumnModel selectionColumn = this.getSelectionColumn(nrm);
            if ((selectionColumn != null) && this.isEnum(selectionColumn)) {
                this.imports.add(this.getEnumClassName(selectionColumn.getDomain()) + ";");
            }
            this.imports.add(POJOCodeGenerator.packageName(tm));
            this.imports.add(POJOCodeGenerator.packageName(rtm));
            String pojoClassName = POJOCodeGenerator.className(tm);
            String pojoAttrName = this.getAttributeName(pojoClassName);
            String pojoListEntryClassName = POJOCodeGenerator.className(rtm);
            String pojoSelectionClassName = (selectionColumn != null ?
                    this.getJavaType(selectionColumn) : null);
            if ((nrm.getNReferencePanelType() == NReferencePanelType.SELECTABLE)
                    || ((nrm.getNReferencePanelType() == NReferencePanelType.EDITABLE)
                    && selectionColumn.getTable().isOptionSet(TableParamIds.EMBEDDED))) {
                if (selectionColumn.getReferencedTable() != null) {
                    TableModel tm0 = selectionColumn.getReferencedTable();
                    this.imports.add(POJOCodeGenerator.packageName(tm0));
                    pojoSelectionClassName = POJOCodeGenerator.className(tm0);
                }
            } else {
                pojoSelectionClassName = pojoListEntryClassName;
            }
            this.imports.add("baccara.gui.generics");
            String code = "abstract public class " + className + " extends "
                    + "AbstractBaccaraListMaintenancePanel<" + pojoClassName + ", "
                    + pojoListEntryClassName + ", " + pojoSelectionClassName + "> {\n\n";
            code += this.createConstructor(tm, rtm, className, pojoClassName, pojoAttrName, nrm
                    );
            this.createCreateEditorListManager(pojoListEntryClassName, (selectionColumn != null
                    ? selectionColumn.getTable() : rtm), pojoClassName, pojoAttrName, tm,
                    nrm);
            // this.createGetSelectionData(pojoSelectionClassName);
            this.createGetTableModel(tm, pojoListEntryClassName, pojoSelectionClassName,
                    pojoClassName, pojoAttrName);
            this.createTransferChanges(pojoListEntryClassName, pojoSelectionClassName,
                    pojoClassName, pojoAttrName, selectionColumn, rtm);
            gr.setCode(code);
            gr.setState(GeneratorResultState.SUCCESS);
        }
        return gr;
    }

    private ColumnModel getSelectionColumn(NReferenceModel nrm) {
        for (OptionModel o : this.currentPanelModel.getOptions()) {
            if (o.getName().equals(PanelParamIds.SELECTION)) {
                return nrm.getColumn().getTable().getColumnByName(o.getParameter());
            }
        }
        return null;
    }

    private String createConstructor(TableModel tm, TableModel rtm,
            String className, String pojoClassName, String pojoAttrName, NReferenceModel nrm) {
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
        code += "        super(guiBundle, parent, resourcePrefix + \"" + rtm.getName()
                + "s\", " + pojoAttrName + ", dataProvider, "
                + "new ListMaintenancePanelConfiguration(ListMaintenancePanelType."
                + this.getListMaintenancePanelType(nrm.getNReferencePanelType()) + ")";
        if (nrm.isPermitCreate()) {
            code += ".setAddPossible(true)";
        }
        if (nrm.isAlterable()) {
            code += ".setEditable(true)";
        }
        code += ");\n";
        code += "    }\n";
        code += "\n";
        return code;
    }

    private ListMaintenancePanelType getListMaintenancePanelType(NReferencePanelType type) {
        if (type == NReferencePanelType.EDITABLE) {
            return ListMaintenancePanelType.EDITABLE;
        } else if (type == NReferencePanelType.SELECTABLE) {
            return ListMaintenancePanelType.SELECTABLE;
        }
        return null;
    }

    private void createCreateEditorListManager(String pojoListEntryClassName,
            TableModel selectionTable, String pojoClassName, String pojoAttrName, TableModel tm,
            NReferenceModel nrm) {
        String methodName = "createEditorListManager";
        String code = this.createOverrideComment();
        code += "    @Override public EditorListManager<" + pojoListEntryClassName + "> "
                + methodName + "(" + pojoClassName + " " + pojoAttrName + ") {\n";
        if ((nrm.getNReferencePanelType() == NReferencePanelType.SELECTABLE)
                || ((nrm.getNReferencePanelType() == NReferencePanelType.EDITABLE)
                && selectionTable.isOptionSet(TableParamIds.EMBEDDED))) {
            ListMaintenanceEditorListManagerCodeGenerator cg =
                    new ListMaintenanceEditorListManagerCodeGenerator();
            cg.setCurrentPanel(this.currentPanelModel);
            this.imports.add(cg.getPackageName(tm));
            code += "        return new " + cg.getClassName(tm) + "(" + pojoAttrName + ");\n";
        } else {
            EditorListManagerCodeGenerator cg = new EditorListManagerCodeGenerator();
            this.imports.add(cg.getPackageName(selectionTable));
            code += "        return new " + cg.getClassName(selectionTable) + "(" + pojoAttrName
                    + ");\n";
        }
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createGetTableModel(TableModel tm, String pojoListEntryClassName,
            String pojoSelectionClassName, String pojoClassName, String pojoAttrName) {
        String methodName = "getTableModel";
        String code = this.createOverrideComment();
        code += "    @Override public BaccaraTableModel<" + pojoListEntryClassName
                + ", " + pojoSelectionClassName + "> " + methodName + "(" + pojoClassName
                + " " + pojoAttrName + ") {\n";
        ListMaintenanceTableModelCodeGenerator cg = new ListMaintenanceTableModelCodeGenerator(
                );
        cg.setCurrentPanel(this.currentPanelModel);
        this.imports.add(cg.getPackageName(tm));
        code += "        return new " + cg.getClassName(tm) + "(this.guiBundle, "
                + pojoAttrName + ", " + pojoAttrName + ".get" + pojoListEntryClassName
                + "s());\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createTransferChanges(String pojoListEntryClassName,
            String pojoSelectionClassName, String pojoClassName, String pojoAttrName, 
            ColumnModel selectionColumn, TableModel rtm) {
        String methodName = "transferChanges";
        String code = this.createOverrideComment();
        code += "    public void " + methodName + "(" + pojoClassName + " " + pojoAttrName
                + ") {\n";
        if (this.currentPanelModel.isOptionSet(PanelParamIds.CLEAN_BEFORE_TRANSFER)) {
            code += "        " + pojoAttrName + ".clear" + pojoListEntryClassName + "s();"
                    + "\n";
        }
        code += "        for (" + pojoListEntryClassName + " o : this.tableModelDisplay."
                + "getRowObjects()) {\n";
        if (this.currentPanelModel.isOptionSet(PanelParamIds.UNIQUE_SELECTION)) {
            String gn = this.createGetterName(selectionColumn) + "()";
            code += "            if (!" + pojoAttrName + ".is" + pojoSelectionClassName + "Set("
                    + "o." + gn + ")) {\n";
            code += "                " + pojoAttrName + ".add" + pojoListEntryClassName + "(o);"
                    + "\n";
            code += "            } else {\n";
            code += "                " + pojoListEntryClassName + " o0 = " + pojoAttrName
                    + ".get" + pojoListEntryClassName + "(o." + gn + ");\n";
            for (ColumnModel c : rtm.getColumns()) {
                if (!c.isOptionSet(ColParamIds.NO_SETTER) && !c.isPrimaryKey()) {
                    code += "                o0.set" + c.getName() + "(o."
                            + this.createGetterName(c) + "());\n"; 
                }
            }
            code += "            }\n";
        } else {
            code += "            " + pojoAttrName + ".add" + pojoListEntryClassName + "(o);\n";
        }
        code += "        }\n";
        if (!this.currentPanelModel.isOptionSet(PanelParamIds.CLEAN_BEFORE_TRANSFER)) {
            String gn = this.createGetterName(selectionColumn) + "()";
            code += "        " + pojoListEntryClassName + "[] os = " + pojoAttrName + ".get"
                    + pojoListEntryClassName + "s();\n";
            code += "        for (int i = os.length-1; i >= 0; i--) {\n";
            code += "            if (!this.tableModelDisplay.contains(os[i]." + gn + ")) {\n";
            code += "                " + pojoAttrName + ".remove" + pojoListEntryClassName
                    + "(os[i]);\n";
            code += "            }\n";
            code += "        }\n";
        }
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    /**
     * @changed OLI 13.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        NReferenceModel nrm = tm.getNReferenceForPanel(this.currentPanelModel);
        if (nrm == null) {
            return "AbstractEditorPanel" + tm.getName() + "XXXXX";
        }
        return "AbstractEditorPanel" + tm.getName() + nrm.getColumn().getTable().getName()
                + "s";
    }

    /**
     * @changed OLI 13.07.2016 - Added.
     */
    @Override public String[] getExcludeMarks() {
        return new String[] {TableParamIds.NO_GUI};
    }

    /**
     * @changed OLI 13.07.2016 - Added.
     */
    @Override public String getSubPackage() {
        return "gui.archgen";
    }

    /**
     * @changed OLI 13.07.2016 - Added.
     */
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.PANEL;
    }

    /**
     * @changed OLI 13.07.2016 - Added.
     */
    @Override public boolean isSuppressGeneration(DataModel dm, TableModel tm,
            CodeUtil codeUtil) {
        NReferenceModel nrm = tm.getNReferenceForPanel(this.currentPanelModel);
        if ((nrm != null) && (nrm.getNReferencePanelType() == NReferencePanelType.SELECTABLE)) {
            return false;
        }
        if ((nrm != null) && (nrm.getNReferencePanelType() == NReferencePanelType.EDITABLE)) {
            return false;
        }
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
     * @changed OLI 13.07.2016 - Added.
     */
    public void setCurrentPanel(PanelModel panel) {
        this.currentPanelModel = panel;
    }

}