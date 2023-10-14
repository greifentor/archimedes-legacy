/*
 * GeneratedListMaintenanceTableModel.java
 *
 * 13.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.gui;

import java.util.*;

import archimedes.acf.*;
import archimedes.acf.param.*;
import archimedes.acf.report.*;
import archimedes.model.*;
import baccara.acf.*;
import baccara.acf.ColParamIds;
import baccara.acf.PanelParamIds;
import baccara.acf.TableParamIds;
import baccara.acf.entities.*;
import gengen.util.*;

/**
 * A code generator for list maintenance panels.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 15.07.2016 - Added.
 */

public class GeneratedListMaintenanceTableModelCodeGenerator extends BaccaraBaseCodeGenerator {

    private static GeneratedListMaintenanceTableModelCodeGenerator instance =
            new GeneratedListMaintenanceTableModelCodeGenerator();

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
        return "An editor panel for the " + this.getDescription(tm) + " data.";
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
            ColumnModel selectionColumn = this.getSelectionColumn(nrm);
            if ((selectionColumn != null) && this.isEnum(selectionColumn)) {
                this.imports.add(this.getEnumClassName(selectionColumn.getDomain()) + ";");
            }
            this.imports.add(POJOCodeGenerator.packageName(tm));
            this.imports.add(POJOCodeGenerator.packageName(rtm));
            String pojoClassName = POJOCodeGenerator.className(tm);
            String pojoListEntryClassName = POJOCodeGenerator.className(rtm);
            String pojoSelectionClassName = (selectionColumn != null ?
                    this.getType(selectionColumn) : pojoListEntryClassName);
            this.imports.add("baccara.gui.generics");
            String code = "public class " + className + " extends BaccaraTableModel<"
                    + pojoListEntryClassName + ", " + pojoSelectionClassName + "> {\n\n";
            code += "    protected " + pojoClassName + " " + this.getAttributeName(pojoClassName
                    ) + " = null;\n\n";
            code += this.createConstructor(tm, rtm, className, pojoClassName,
                    pojoListEntryClassName);
            this.createCreateRowList(pojoListEntryClassName, rtm);
            this.createCreateRowObject(pojoClassName, pojoListEntryClassName,
                    pojoSelectionClassName, rtm.isOptionSet(TableParamIds.EMBEDDED));
            this.createGetColumnCount(pojoClassName, rtm);
            this.createGetColumnName(rtm, gr);
            this.createGetColumnClass(rtm);
            // this.createGetKey(rtm, pojoListEntryClassName, pojoSelectionClassName);
            this.createGetRowValueAt(rtm, pojoListEntryClassName);
            this.createIsCellEditable(rtm);
            this.createMatches(rtm, pojoListEntryClassName, pojoSelectionClassName,
                    selectionColumn);
            this.createSetRowValueAt(rtm, pojoListEntryClassName);
            gr.setCode(code);
            gr.setState(GeneratorResultState.SUCCESS);
        }
        return gr;
    }

    private String getType(ColumnModel c) {
        String type = this.getJavaType(c);
        if (c.getReferencedTable() != null) {
            TableModel tm = c.getReferencedTable();
            this.imports.add(POJOCodeGenerator.packageName(tm));
            type = POJOCodeGenerator.className(tm);
        }
        return type;
    }

    private boolean isUniqueSelection() {
        return this.currentPanelModel.getPanelClass().contains(PanelParamIds.UNIQUE_SELECTION);
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
        code += "        super(guiBundle, rowObjects, " + this.isUniqueSelection() + ");\n";
        code += "        this." + pn + " = " + pn + ";\n";
        code += "    }\n";
        code += "\n";
        return code;
    }

    private void createCreateRowList(String pojoListEntryClassName, TableModel rtm) {
        String methodName = "createRowList";
        String code = this.createOverrideComment();
        this.imports.add("java.util");
        code += "    @Override public List<" + pojoListEntryClassName + "> " + methodName
                + "() {\n";
        if ((rtm.getCompareMembers().length > 0) || rtm.isOptionSet(TableParamIds.COMPARABLE)) {
            this.imports.add("corentx.util");
            code += "        return new SortedVector<" + pojoListEntryClassName + ">();\n";
        } else {
            code += "        return new LinkedList<" + pojoListEntryClassName + ">();\n";
        }
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createCreateRowObject(String pojoClassName, String pojoListEntryClassName,
            String pojoSelectionClassName, boolean embedded) {
        String methodName = "createRowObject";
        String code = this.createOverrideComment();
        String pn = this.getAttributeName(pojoClassName);
        code += "    @Override public " + pojoListEntryClassName + " " + methodName + "("
                + pojoSelectionClassName + " key) {\n";
        code += "        return new " + pojoListEntryClassName + "(this." + pn + 
                (!embedded ? ", key" : "") + ");\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createGetColumnCount(String pojoClassName, TableModel rtm) {
        String methodName = "getColumnCount";
        String code = this.createOverrideComment();
        code += "    @Override public int " + methodName + "() {\n";
        code += "        return " + this.getEditorMembers(rtm, true).length + ";\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private ColumnModel[] getEditorMembers(TableModel tm, boolean enabledOnly) {
        List<ColumnModel> l = new LinkedList<ColumnModel>();
        for (ColumnModel c : tm.getColumns()) {
            if (c.isEditorMember() && (!enabledOnly || !c.isEditorDisabled())) {
                l.add(c);
            }
        }
        return this.assortColumnsByEditorPosition(l.toArray(new ColumnModel[0]));
    }

    private void createGetColumnName(TableModel rtm, GeneratorResult gr) {
        String methodName = "getColumnName";
        String code = this.createOverrideComment();
        code += "    @Override public String " + methodName + "(int columnIndex) {\n";
        code += "        switch(columnIndex) {\n";
        int i = 0;
        ColumnModel[] ems = this.getEditorMembers(rtm, true);
        for (ColumnModel c : ems) {
            code += "        case " + i + ":\n";
            String rid = this.setFirstLetterToUpperCase(this.getAttributeName(
                    this.currentPanelModel.getTabTitle())) + "TableModel.column."
                    + this.getAttributeName(c) + ".title";
            code += "            return this.guiBundle.getResourceText(\""
                    + rid + "\");\n";
            gr.addResourceId(rid, c.getEditorLabelText());
            i++;
        }
        code += "        };\n";
        code += "        return \"???\";\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createGetColumnClass(TableModel rtm) {
        String methodName = "getColumnClass";
        String code = this.createOverrideComment();
        code += "    @Override public Class<?> " + methodName + "(int columnIndex) {\n";
        code += "        switch(columnIndex) {\n";
        int i = 0;
        ColumnModel[] ems = this.getEditorMembers(rtm, true);
        for (ColumnModel c : ems) {
            code += "        case " + i + ":\n";
            if (c.getDomain().isOptionSet(DomainParamIds.RESOURCED)) {
                code += "            return String.class;\n";
            } else {
                if (c.getDomain().isOptionSet(DomainParamIds.ENUM)) {
                    String s = c.getDomain().getOptionByName(DomainParamIds.ENUM).getParameter(
                            );
                    s = s.replace(DomainParamIds.ENUM,  "");
                    s = s.substring(0, s.lastIndexOf("."));
                    this.imports.add(s.trim());
                }
                if (this.isTimestamp(c)) {
                    this.imports.add("corentx.dates");
                }
                code += "            return " + this.typeUtil.getWrapperClass(this.getType(c))
                        + ".class;\n";
            }
            i++;
        }
        code += "        };\n";
        code += "        return String.class;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createIsCellEditable(TableModel rtm) {
        String methodName = "isCellEditable";
        String code = this.createOverrideComment();
        code += "    @Override public boolean " + methodName + "(int rowIndex, int columnIndex"
                + ") {\n";
        code += "        switch(columnIndex) {\n";
        int i = 0;
        ColumnModel[] ems = this.getEditorMembers(rtm, true);
        for (ColumnModel c : ems) {
            code += "        case " + i + ":\n";
            code += "            return " + (!c.isOptionSet(ColParamIds.NOT_EDITABLE)) + ";\n";
            i++;
        }
        code += "        };\n";
        code += "        return false;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    /*
    private void createGetKey(TableModel rtm, String pojoListEntryClassName,
            String pojoSelectionClassName) {
        String methodName = "getKey";
        String code = this.createOverrideComment();
        code += "    @Override public " + pojoSelectionClassName + " " + methodName + "("
                + pojoListEntryClassName + " rowObject) {\n";
        if (rtm.isNMRelation()) {
            ColumnModel c = null;
            for (ColumnModel c0 : rtm.getColumns()) {
                if (c0.getReferencedTable() != null) {
                    if (c0.getReferencedTable().getName().equals(pojoSelectionClassName)) {
                        c = c0;
                        break;
                    }
                }
            }
            ensure(c != null, "BaccaraACF", "column not found for reference key type '"
                    + pojoSelectionClassName + " in table: " + rtm.getName(),
                    "BaccaraACF.GeneratedListMaintenanceTableModelCodeGenerator."
                    + "NoRefColForRowObjectKey.message", rtm.getName(), pojoSelectionClassName);
            code += "        return rowObject." + this.createGetterName(c) + "();\n";
        } else {
            code += "        return null;\n";
        }
        code += "    }\n";
        this.storeMethod(methodName, code);
    }
    */

    private void createGetRowValueAt(TableModel rtm, String pojoListEntryClassName) {
        String methodName = "getRowValueAt";
        String code = this.createOverrideComment();
        code += "    @Override public Object " + methodName + "(" + pojoListEntryClassName
                + " rowObject, int column) {\n";
        code += "        switch(column) {\n";
        int i = 0;
        ColumnModel[] ems = this.getEditorMembers(rtm, true);
        for (ColumnModel c : ems) {
            code += "        case " + i + ":\n";
            if (c.getReferencedTable() == null) {
                code += "            return rowObject." + this.createGetterName(c) + "()"
                        + (c.getDomain().isOptionSet(DomainParamIds.RESOURCED)
                        ? ".toLocalizedString(this.guiBundle)" : "") + ";\n";
            } else {
                code += "            return ";
                String a = "";
                for (ToStringContainerModel tscm : c.getReferencedTable().getComboStringMembers(
                        )) {
                    if ((tscm.getPrefix() != null) && !tscm.getPrefix().isEmpty()) {
                        a += (a.length() > 0 ? " + " : "") + "\"" + tscm.getPrefix() + "\"";
                    }
                    a += (a.length() > 0 ? " + " : "") + "String.valueOf(rowObject."
                            + this.createGetterName(c) + "()."
                            + this.createGetterName(tscm.getColumn()) + "())";
                    if ((tscm.getSuffix() != null) && !tscm.getSuffix().isEmpty()) {
                        a += " + \"" + tscm.getSuffix() + "\"";
                    }
                }
                code += a + ";\n";
            }
            i++;
        }
        code += "        };\n";
        code += "        return null;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createMatches(TableModel rtm, String pojoListEntryClassName,
            String pojoSelectionClassName, ColumnModel selectionColumn) {
        String methodName = "matches";
        String code = this.createOverrideComment();
        code += "    @Override public boolean " + methodName + "(" + pojoListEntryClassName
                + " t, " + pojoSelectionClassName + " key) {\n";
        code += "        return t." + (selectionColumn != null
                ? this.createGetterName(selectionColumn) + "() == key" : "equals(key)") + ";\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createSetRowValueAt(TableModel rtm, String pojoListEntryClassName) {
        String methodName = "setRowValueAt";
        String code = this.createOverrideComment();
        code += "    @Override public void " + methodName + "(" + pojoListEntryClassName
                + " rowObject, int columnIndex, Object value) {\n";
        code += "        switch(columnIndex) {\n";
        int i = 0;
        ColumnModel[] ems = this.getEditorMembers(rtm, true);
        for (ColumnModel c : ems) {
            if (!c.isOptionSet(ColParamIds.NOT_EDITABLE)) {
                code += "        case " + i + ":\n";
                String type = this.typeUtil.getWrapperClass(this.getType(c));
                code += "            rowObject.set" + c.getName() + "((" + type + ") value);\n";
                code += "            return;\n";
            }
            i++;
        }
        code += "        };\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    /**
     * @changed OLI 15.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        NReferenceModel nrm = tm.getNReferenceForPanel(this.currentPanelModel);
        if (nrm == null) {
            return "Generated" + tm.getName() + "XXXXX" + "TableModel";
        }
        return "Generated" + tm.getName() + nrm.getColumn().getTable().getName()
                + "TableModel";
    }

    /**
     * @changed OLI 15.07.2016 - Added.
     */
    @Override public String[] getExcludeMarks() {
        return new String[] {TableParamIds.NO_GUI};
    }

    /**
     * @changed OLI 15.07.2016 - Added.
     */
    @Override public String getSubPackage() {
        return "gui.archgen";
    }

    /**
     * @changed OLI 15.07.2016 - Added.
     */
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.PANEL;
    }

    /**
     * @changed OLI 15.07.2016 - Added.
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
     * @changed OLI 15.07.2016 - Added.
     */
    public void setCurrentPanel(PanelModel panel) {
        this.currentPanelModel = panel;
    }

}