/*
 * GeneratedBaccaraEditorPanelCodeGenerator.java
 *
 * 04.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.gui;

import java.util.*;

import archimedes.acf.*;
import archimedes.acf.report.*;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.PanelModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;
import baccara.acf.*;
import baccara.acf.ColParamIds;
import baccara.acf.TableParamIds;
import baccara.acf.entities.*;
import gengen.util.*;

/**
 * A code generator for a Baccara editor panel (generated version).
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.07.2016 - Added.
 */

public class GeneratedBaccaraEditorPanelCodeGenerator extends BaccaraBaseCodeGenerator {

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
        GeneratorResult gr = new GeneratorResult();
        this.imports.add(GeneratedPOJOAttributeIdCodeGenerator.packageName(tm));
        this.imports.add(POJOCodeGenerator.packageName(tm));
        String attrIdClassName = GeneratedPOJOAttributeIdCodeGenerator.className(tm);
        String pojoClassName = POJOCodeGenerator.className(tm);
        this.imports.add("baccara.gui.generics");
        String code = "public class " + className + " extends BaccaraEditorPanel<"
                + attrIdClassName + ", " + pojoClassName + "> {\n\n";
        code += this.createConstructor(codeUtil, tm, className, pojoClassName, attrIdClassName,
                gr);
        if (this.isConsistenceCheckRequired(tm)) {
            this.createGetEditorPanelConsistenceCheckersIfRequired(tm, attrIdClassName);
        }
        this.createSetAttribute(codeUtil, tm, className, pojoClassName, attrIdClassName);
        gr.setCode(code);
        gr.setState(GeneratorResultState.SUCCESS);
        return gr;
    }

    private String createConstructor(CodeUtil codeUtil, TableModel tm, String className,
            String pojoClassName, String attrIdClassName, GeneratorResult gr) {
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
        code += "    @SuppressWarnings(\"unchecked\") public " + className
                + "(GUIBundle guiBundle, Container parent, String resourcePrefix, "
                + "ActionListener actionListener, " + pojoClassName + " " + pojoName + ", "
                + "BaccaraDataProvider dataProvider) {\n";
        code += "        super(guiBundle, parent, resourcePrefix, actionListener, " + pojoName
                + ",\n";
        String rIdPrefix = tm.getName() + "EditorDialog";
        gr.addResourceId("General.button.cancel.text", "cancel");
        gr.addResourceId("General.button.ok.text", "ok");
        /*
        gr.addResourceId(rIdPrefix + ".button.cancel.text", "cancel");
        gr.addResourceId(rIdPrefix + ".button.ok.text", "ok");
        */
        gr.addResourceId(rIdPrefix + ".title.text", "{0} (" + this.getDescription(tm) + ")");
        String a = "";
        for (ColumnModel c : this.assortColumnsByEditorPosition(
                this.getColumnsIncludingInherited(tm))) {
            if (c.isEditorMember()) {
                if (a.length() > 0) {
                    a += ",\n                ";
                } else {
                    a += "                ";
                }
                String type = this.getEditorType(c);
                String rid = c.getTable().getName() + "EditorDialog" + "."
                        + this.createAttrIdName(c);
                String ridLabel = rid + ".label";
                if (type.equals("TEXT")) {
                    gr.addResourceId(rid + ".button.cancel.text", "cancel");
                    gr.addResourceId(rid + ".button.edit.text", "edit");
                    gr.addResourceId(rid + ".button.ok.text", "ok");
                    gr.addResourceId(rid + ".title", this.getDescription(c));
                }
                if (type.equals("OBJECT")) {
                    if ((c.getReferencedTable() != null) || this.isEnum(c)) {
                        if (this.isEnum(c)) {
                            this.imports.add(this.getEnumClassName(c.getDomain()) + ";");
                        }
                        this.imports.add("javax.swing");
                        this.imports.add("baccara.gui");
                        a += "new ComponentData<" + attrIdClassName + ">(" 
                                + attrIdClassName + "." + this.createAttrIdName(c) + ", "
                                + "(java.util.List<Object>) dataProvider.get(\""
                                + c.getFullName() + "\"), " + pojoName + "."
                                + this.createGetterName(c) + "(), "
                                + "(ListCellRenderer<?>) dataProvider.get(\"" + c.getFullName()
                                + ".renderer\"), " + (!c.isNotNull()) + ", new ResourceId(\""
                                + ridLabel + "\"))";
                    }
                } else {
                    this.imports.add("baccara.gui");
                    a += "new ComponentData<" + attrIdClassName + ">(" 
                            + attrIdClassName + "." + this.createAttrIdName(c) + ", "
                            + "baccara.gui.generics.Type." + type + ", " + pojoName + "."
                            + this.createGetterName(c) + "(), new ResourceId(\"" + ridLabel + "\"))";
                }
                if (c.isOptionSet(ColParamIds.ACTION_BUTTON)) {
                    a += ".setActionButton(true)";
                }
                if (c.isEditorDisabled()) {
                    a += ".setDisabled(true)";
                }
                gr.addResourceId(ridLabel, c.getEditorLabelText());
            }
        }
        code += a + "\n        );\n";
        code += "    }\n";
        code += "\n";
        return code;
    }

    private String getEditorType(ColumnModel c) {
        if (c.getDomain().getType().equalsIgnoreCase("text")) {
            return "TEXT";
        }
        String jt = this.getJavaType(c, false, false);
        if (jt.equalsIgnoreCase("boolean")) {
            return "BOOLEAN";
        } else if (jt.equalsIgnoreCase("PDate")) {
            return "DATE";
        } else if (jt.equalsIgnoreCase("int") || jt.equalsIgnoreCase("Integer")
                || jt.equalsIgnoreCase("long") || jt.equalsIgnoreCase("Long")) {
            return "INTEGER";
        } else if (jt.equalsIgnoreCase("double") || jt.equalsIgnoreCase("Double")
                || jt.equalsIgnoreCase("float") || jt.equalsIgnoreCase("Float")) {
            return "DOUBLE";
        } else if (jt.equalsIgnoreCase("String")) {
            return "STRING";
        } else if (jt.equalsIgnoreCase("PTimestamp")) {
            return "TIMESTAMP";
        }
        return "OBJECT";
    }

    private boolean isConsistenceCheckRequired(TableModel tm) {
        for (ColumnModel c : tm.getColumns()) {
            if (c.isNotNull() || c.isOptionSet(ColParamIds.NOT_BLANK)) {
                return true;
            }
        }
        return false;
    }

    private void createGetEditorPanelConsistenceCheckersIfRequired(TableModel tm,
            String attrIdClassName) {
        String methodName = "getEditorPanelConsistenceCheckers";
        String code = this.createOverrideComment();
        this.imports.add("baccara.gui.generics");
        this.imports.add("java.util");
        code += "    @Override public java.util.List<EditorPanelConsistencyChecker<"
                + attrIdClassName + ">>" + methodName + "() {\n";
        code += "        java.util.List<EditorPanelConsistencyChecker<" + attrIdClassName
                + ">> l = new LinkedList<EditorPanelConsistencyChecker<" + attrIdClassName
                + ">>();\n";
        for (ColumnModel c : tm.getColumns()) {
            if (c.isNotNull() || c.isOptionSet(ColParamIds.NOT_BLANK)) {
                this.imports.add("baccara.gui");
                this.imports.add("baccara.gui.generics.checkers");
                String ccn = (c.isNotNull() ? "RequiredFieldNotSetEditorPanelConsistencyChecker"
                        : "RequiredFieldNotEmptyEditorPanelConsistencyChecker");
                String an = this.createAttrIdName(c);
                code += "        l.add(new " + ccn  + "<" + attrIdClassName + ">("
                        + attrIdClassName + "." + an + ", this, \"" + tm.getName() + "s\", new "
                        + "ResourceId(\"" + c.getTable().getName() + "EditorDialog." + an
                        + ".label\")));\n";
            }
        }
        code += "        return l;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createSetAttribute(CodeUtil codeUtil, TableModel tm, String className,
            String pojoClassName, String attrIdClassName) {
        String methodName = "setAttribute";
        String pojoName = this.getAttributeName(pojoClassName);
        String code = this.createOverrideComment();
        code += "    @Override public void setAttribute(" + pojoClassName + " " + pojoName 
                + ", ComponentData<" + attrIdClassName + "> componentData, Object value) {\n";
        String a = "";
        for (ColumnModel c : this.getColumnsIncludingInherited(tm)) {
            if (c.isEditorMember() && !c.isOptionSet(ColParamIds.NO_SETTER)) {
                if (a.length() > 0) {
                    a += " else ";
                } else {
                    a += "        ";
                }
                a += "if (" + attrIdClassName + "." + this.createAttrIdName(c) + ".equals("
                        + "componentData.getName())) {\n";
                String type = this.getJavaType(c, false, false);
                if (type.equals("LongPTimestamp") || type.equals("PDate") || type.equals("PTime"
                        ) || type.equals("PTimestamp")) {
                    this.imports.add("corentx.dates");
                }
                a += "            " + pojoName + ".set" + c.getName() + "((" + type
                        + ") value);\n";
                a += "        }";
            }
        }
        code += a + "\n    }\n";
        this.storeMethod(methodName, code);
    }

    /**
     * @changed OLI 04.07.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return "GeneratedBaccaraEditorPanel" + tm.getName() + this.setFirstLetterToUpperCase(
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
        return "gui.archgen";
    }

    /**
     * @changed OLI 04.07.2016 - Added.
     */
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.PANEL;
    }

    /**
     * @changed OLI 05.07.2016 - Added.
     */
    @Override public boolean isSuppressGeneration(DataModel dm, TableModel tm,
            CodeUtil codeUtil) {
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