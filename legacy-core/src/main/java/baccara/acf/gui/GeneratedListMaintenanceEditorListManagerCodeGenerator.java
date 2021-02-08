/*
 * GeneratedListMaintenanceEditorListManagerCodeGenerator.java
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
import baccara.acf.PanelParamIds;
import baccara.acf.TableParamIds;
import baccara.acf.entities.*;
import corentx.util.*;
import gengen.util.*;

/**
 * A code generator for the generated editor list managers for list maintenance panels.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 15.07.2016 - Added.
 */

public class GeneratedListMaintenanceEditorListManagerCodeGenerator
        extends BaccaraBaseCodeGenerator {

    private static GeneratedListMaintenanceEditorListManagerCodeGenerator instance =
            new GeneratedListMaintenanceEditorListManagerCodeGenerator();

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
        return "An editor list manager for the " + this.getDescription(tm) + " data in list "
                + "maintenance panels.";
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
//            if (this.isEnum(selectionColumn)) {
//                this.imports.add(this.getEnumClassName(selectionColumn.getDomain()) + ";");
//            }
            this.imports.add(POJOCodeGenerator.packageName(tm));
            this.imports.add(POJOCodeGenerator.packageName(rtm));
            String pojoClassName = POJOCodeGenerator.className(tm);
            String pojoAttrName = this.getAttributeName(pojoClassName);
            String pojoListEntryClassName = POJOCodeGenerator.className(rtm);
            this.imports.add("baccara.gui.generics");
            this.imports.add("java.util");
            String code = "public class " + className + " extends AbstractEditorListManager<"
                    + pojoClassName + ", " + pojoListEntryClassName + "> {\n\n";
            code += "    private List<" + pojoListEntryClassName + "> l = new ";
            if (rtm.getComboStringMembers().length > 0) {
                this.imports.add("corentx.util");
                code += "SortedVector";
            } else {
                code += "LinkedList";
            }
            code += "<" + pojoListEntryClassName + ">();\n\n";
            // code += "    private " + pojoClassName + " " + pojoAttrName + " = null;\n\n";
            code += this.createConstructor(tm, rtm, pojoAttrName, className, pojoClassName,
                    pojoListEntryClassName);
            this.createCreateEmptyList(pojoListEntryClassName, rtm);
            this.createCreateNewElement(pojoListEntryClassName, pojoClassName, selectionColumn,
                    rtm);
            this.createGetElementsToEdit(pojoListEntryClassName, pojoClassName);
            this.createIsElementToAdd(pojoListEntryClassName, selectionColumn);
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

    private void createCreateEmptyList(String pojoListEntryClassName, TableModel rtm) {
        String methodName = "createEmptyList";
        this.imports.add("java.util");
        String code = this.createOverrideComment();
        code += "    @Override public List<" + pojoListEntryClassName + "> " + methodName
                + "() {\n";
        code += "        return new ";
        if (rtm.getComboStringMembers().length > 0) {
            code += "SortedVector";
        } else {
            code += "LinkedList";
        }
        code += "<" + pojoListEntryClassName + ">();\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createCreateNewElement(String pojoListEntryClassName, String pojoClassName,
            ColumnModel selectionColumn, TableModel rtm) {
        Checks.ensure(selectionColumn != null, "selection column is null or unknown for table '"
                + pojoClassName + "'! Set correct column name in the SELECTION option of the "
                + "panel.");
        String methodName = "createNewElement";
        String code = this.createOverrideComment();
        String an = this.getAttributeName(pojoClassName);
        String anl = this.getAttributeName(pojoListEntryClassName);
        code += "    @Override public " + pojoListEntryClassName + " " + methodName
                + "(" + pojoClassName + " " + an + ", " + pojoListEntryClassName + " " + anl
                + ") {\n";
        code += "        return new " + pojoListEntryClassName + "(" + an + (!rtm.isOptionSet(
                TableParamIds.EMBEDDED) ? ", " + anl + "."
                + this.createGetterName(selectionColumn) + "()" : "") + ");\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createGetElementsToEdit(String pojoListEntryClassName, String pojoClassName) {
        String methodName = "getElementsToEdit";
        String code = this.createOverrideComment();
        String an = this.getAttributeName(pojoClassName);
        code += "    @Override public " + pojoListEntryClassName + "[] " + methodName
                + "(" + pojoClassName + " " + an + ") {\n";
        code += "        return " + an + ".get" + pojoListEntryClassName + "s();\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createIsElementToAdd(String pojoListEntryClassName, ColumnModel selectionColumn
            ) {
        String methodName = "isElementToAdd";
        String code = this.createOverrideComment();
        String an = this.getAttributeName(pojoListEntryClassName);
        code += "    @Override public boolean " + methodName + "(" + pojoListEntryClassName
                + " " + an + ") {\n";
        if (this.isUniqueSelection()) {
            code += "        for (" + pojoListEntryClassName + " o : this.list) {\n";
            String gn = this.createGetterName(selectionColumn) + "()";
            code += "            if (o." + gn;
            boolean simpleType = this.isSimpleType(this.getJavaType(selectionColumn, true, true
                    ));
            if (simpleType) {
                code += " == ";
            } else {
                code += ".equals(";
            }
            code += an + "." + gn;
            if (!simpleType) {
                code += ")";
            }
            code += ") {\n";
            code += "                return false;\n";
            code += "            }\n";
            code += "        }\n";
        }
        code += "        return true;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private boolean isUniqueSelection() {
        return this.currentPanelModel.getPanelClass().contains(PanelParamIds.UNIQUE_SELECTION);
    }

    /**
     * @changed OLI 15.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        NReferenceModel nrm = tm.getNReferenceForPanel(this.currentPanelModel);
        if (nrm == null) {
            return "Generated" + tm.getName() + "XXXXX" + "EditorListManager";
        }
        return "Generated" + tm.getName() + nrm.getColumn().getTable().getName()
                + "EditorListManager";
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
