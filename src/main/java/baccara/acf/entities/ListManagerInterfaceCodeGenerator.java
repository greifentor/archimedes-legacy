/*
 * ListManagerInterfaceCodeGenerator.java
 *
 * 01.09.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.entities;

import java.util.*;

import archimedes.acf.*;
import archimedes.acf.report.*;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;
import baccara.acf.*;
import gengen.util.*;

/**
 * A code generator for list manager interfaces.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 01.09.2016 - Added.
 */

public class ListManagerInterfaceCodeGenerator extends BaccaraBaseCodeGenerator {

    private static ListManagerInterfaceCodeGenerator instance =
            new ListManagerInterfaceCodeGenerator();

    /**
     * Returns the class name for the code generator for the passed table.
     *
     * @param table The table which the class name is to create for.
     * @return The class name for the code generator for the passed table.
     *
     * @changed OLI 01.09.2016 - Added.
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
     * @changed OLI 01.09.2016 - Added.
     */
    public static String packageName(TableModel table) {
        return instance.getPackageName(table);
    }

    /**
     * @changed OLI 01.09.2016 - Added.
     */
    @Override public String getAlternateReferenceTableClassName(TableModel tm) {
        this.imports.add(POJOCodeGenerator.packageName(tm));
        return POJOCodeGenerator.className(tm);
    }

    /**
     * @changed OLI 01.09.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return tm.getName() + "ListManager";
    }

    /**
     * @changed OLI 01.09.2016 - Added.
     */
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.TABLE;
    }

    /**
     * @changed OLI 01.09.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "An interface which describes the necessary methods for an object which manages "
                + "a list of " + this.getDescription(tm) + "s\n";
    }

    /**
     * @changed OLI 01.09.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        GeneratorResultState resultState = GeneratorResultState.SUCCESS;
        if (tm.isOptionSet(TableParamIds.DEPENDENT)) {
            resultState = GeneratorResultState.NOT_NECESSARY;
        }
        boolean separatedDataStock = tm.isOptionSet(TableParamIds.SEPARATED_DATA_STOCK);
        String pojoClassName = POJOCodeGenerator.className(tm);
        String code = "public interface " + className + " {\n\n";
        this.createAddMethodHeader(tm, pojoClassName);
        this.createClearMethodHeader(tm, pojoClassName);
        this.createGetByIdMethodHeader(tm, pojoClassName);
        this.createGetMethodHeader(tm, pojoClassName, separatedDataStock);
        this.createGetNextIdMethodHeader(tm, pojoClassName, separatedDataStock);
        this.createRemoveMethodHeader(tm, pojoClassName);
        return new GeneratorResult(code, resultState);
    }

    private void createAddMethodHeader(TableModel tm, String pojoClassName) {
        String methodName = "add" + tm.getName();
        String description = this.getDescription(pojoClassName);
        String paramName = this.getAttributeName(pojoClassName); 
        String code = "     /**\n";
        code += "     * Adds the passed " + description + " to the managed list of "
                + description + "s.\n"; 
        code += "     *\n";
        code += "     * " + this.createParameterTag(paramName, "The " + description + " to add "
                + "to the list.") + "\n";
        code += "     * @throws IllegalArgumentException Passing a null pointer.\n";
        code += "     * @throws IllegalStateException If the list contains already a campaign "
                + "with the passed token or id.\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";;
        code += "     */\n";
        code += "    abstract public void " + methodName + "(" + pojoClassName + " "
                + paramName + ") throws IllegalArgumentException, IllegalStateException;\n";
        this.storeMethod(methodName, code);
    }

    private void createClearMethodHeader(TableModel tm, String pojoClassName) {
        String methodName = "clear" + tm.getName() + "s";
        String description = this.getDescription(pojoClassName);
        String code = "     /**\n";
        code += "     * Removes all " + description + "s from the lanaged list.\n"; 
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";;
        code += "     */\n";
        code += "    abstract public void " + methodName + "();\n";
        this.storeMethod(methodName, code);
    }

    private void createGetByIdMethodHeader(TableModel tm, String pojoClassName) {
        String methodName = "get" + tm.getName() + "ById";
        String description = this.getDescription(pojoClassName);
        String code = "     /**\n";
        code += "     * Returns the " + description + " with the passed id.\n"; 
        code += "     *\n";
        code += "     * " + this.createParameterTag("id", "The id of the campaign which is to "
                + "return.") + "\n";
        code += "     * @return The " + description + " with the passed id or <CODE>null</CODE>"
                + "if there is no " + description + " with the passed id.\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";;
        code += "     */\n";
        code += "    abstract public " + pojoClassName + " " + methodName + "(long id);\n";
        this.storeMethod(methodName, code);
    }

    private void createGetMethodHeader(TableModel tm, String pojoClassName,
            boolean separatedDataStock) {
        String methodName = "get" + tm.getName() + "s";
        String description = this.getDescription(pojoClassName);
        String code = "     /**\n";
        code += "     * Returns an array with the managed " + description + "s.\n"; 
        code += "     *\n";
        if (separatedDataStock) {
            code += "     * " + this.createParameterTag("customerObjectsOnly", "Set this flag "
                    + "to get the next id for a customer object.") + "\n";
        }
        code += "     * @return The next free id for a new " + description + ".\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";;
        code += "     */\n";
        code += "    abstract public " + pojoClassName + "[] " + methodName + "("
                + (separatedDataStock ? "Boolean customerObject" : "") + ");\n";
        this.storeMethod(methodName, code);
    }

    private void createGetNextIdMethodHeader(TableModel tm, String pojoClassName,
            boolean separatedDataStock) {
        String methodName = "getNext" + tm.getName() + "Id";
        String description = this.getDescription(pojoClassName);
        String code = "     /**\n";
        code += "     * Returns the next free id for a new " + description + ".\n"; 
        code += "     *\n";
        if (separatedDataStock) {
            code += "     * " + this.createParameterTag("customerObject", "Set this flag to get"
                    + " the next id for a customer object.") + "\n";
        }
        code += "     * @return The next free id for a new " + description + ".\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";;
        code += "     */\n";
        code += "    abstract public long " + methodName + "(" + (separatedDataStock ?
                "boolean customerObject" : "") + ");\n";
        this.storeMethod(methodName, code);
    }

    private void createRemoveMethodHeader(TableModel tm, String pojoClassName) {
        String methodName = "remove" + tm.getName();
        String description = this.getDescription(pojoClassName);
        String paramName = this.getAttributeName(pojoClassName); 
        String code = "     /**\n";
        code += "     * Removes the passed " + description + " from the managed list of "
                + description + "s.\n"; 
        code += "     *\n";
        code += "     * " + this.createParameterTag(paramName, "The " + description + " to "
                + "remove from the list.") + "\n";
        code += "     * @throws IllegalArgumentException Passing a null pointer.\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";;
        code += "     */\n";
        code += "    abstract public void " + methodName + "(" + pojoClassName + " "
                + paramName + ") throws IllegalArgumentException;\n";
        this.storeMethod(methodName, code);
    }

    /**
     * @changed OLI 17.05.2016 - Added.
     */
    @Override public String getSubPackage() {
        return "business";
    }

    /**
     * @changed OLI 17.05.2016 - Added.
     */
    @Override public boolean isOneTimeFactory() {
        return true;
    }

}