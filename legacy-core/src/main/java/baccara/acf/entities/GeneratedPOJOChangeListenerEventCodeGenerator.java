/*
 * GeneratedPOJOChangeListenerEventCodeGenerator.java
 *
 * 02.05.2016
 *
 * (c) by HealthCarion
 *
 */

package baccara.acf.entities;

import corentx.util.*;
import gengen.util.*;

import java.util.*;

import archimedes.acf.*;
import archimedes.acf.report.*;
import archimedes.model.*;
import baccara.acf.*;


/**
 * A code generator for object change listener event classes.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 02.05.2016 - Added.
 */

public class GeneratedPOJOChangeListenerEventCodeGenerator extends BaccaraBaseCodeGenerator {


    private static GeneratedPOJOChangeListenerEventCodeGenerator instance
            = new GeneratedPOJOChangeListenerEventCodeGenerator();

    /**
     * Returns the class name for the code generator for the passed table.
     *
     * @param table The table which the class name is to create for.
     * @return The class name for the code generator for the passed table.
     *
     * @changed OLI 02.05.2016 - Added.
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
     * @changed OLI 02.05.2016 - Added.
     */
    public static String packageName(TableModel table) {
        return instance.getPackageName(table);
    }

    /**
     * @changed OLI 05.04.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return tm.getName() + "ChangeEvent";
    }

    /**
     * @changed OLI 05.04.2016 - Added.
     */
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.TABLE;
    }

    /**
     * @changed OLI 05.04.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "An event container for changes in " + this.getDescription(tm) + " objects.";
    }

    /**
     * @changed OLI 05.04.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className, 
            List<PostGeneratingCommand> postGeneratorCommands) {
        String attrIdClassName = GeneratedPOJOAttributeIdCodeGenerator.className(tm);
        String code = "public class " + className + " {\n\n";
        code += "    public enum Type {\n";
        String[] e = this.getChangeEventTypes(tm);
        for (int i = 0, leni = e.length; i < leni; i++) {
            code += "        " + e[i].toUpperCase().replace(" ", "_") + (i+1 < leni ? "," : ";")
                    + "\n";
        }
        code += "    }\n\n";
        code += "    private " + attrIdClassName + " attrId = null;\n";
        code += "    private Object newValue = null;\n";
        code += "    private Type type = null;\n";
        code += "\n";
        code += "    /**\n";
        code += "     * Creates a new event with the passed parameters.\n";
        code += "     *\n";
        code += "     * @param attrId The id of the changed attribute.\n";
        code += "     * @param newValue The new value for the attribute.\n";
        code += "     * @param type The type of the event.\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    public " + className + "(" + attrIdClassName + " attrId, Object newValue, "
                + "Type type) {\n";
        code += "        super();\n";
        code += "        this.attrId = attrId;\n";
        code += "        this.newValue = newValue;\n";
        code += "        this.type = type;\n";
        code += "    }\n\n";
        this.createGetterForAttrId(attrIdClassName);
        this.createGetterForNewValue();
        this.createGetterForType();
        return new GeneratorResult(code, GeneratorResultState.SUCCESS);
    }

    private String[] getChangeEventTypes(TableModel t) {
        List<String> l = new SortedVector<String>();
        l.add("SET");
        if (this.getListInclusions(t).length > 0) {
            l.add("ADD");
            l.add("REMOVE");
        }
        return l.toArray(new String[0]);
    }

    private void createGetterForAttrId(String attrIdClassName) {
        String methodName = "getAttrId";
        String code = this.createGetterComment("attribute id");
        code += "    public " + attrIdClassName + " " + methodName + "() {\n";
        code += "        return this.attrId;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createGetterForNewValue() {
        String methodName = "getNewValue";
        String code = this.createGetterComment("new value");
        code += "    public Object " + methodName + "() {\n";
        code += "        return this.newValue;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createGetterForType() {
        String methodName = "getType";
        String code = this.createGetterComment("type");
        code += "    public Object " + methodName + "() {\n";
        code += "        return this.type;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    /**
     * @changed OLI 02.05.2016 - Added.
     */
    @Override public String[] getNecessaryIncludeMarks() {
        return new String[] {TableParamIds.FIRE_ENTITY_EVENTS};
    }

}