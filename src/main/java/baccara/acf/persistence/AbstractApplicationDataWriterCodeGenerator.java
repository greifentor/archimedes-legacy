/*
 * AbstractApplicationDataWriterCodeGenerator.java
 *
 * 05.09.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.persistence;

import java.util.*;

import archimedes.acf.*;
import archimedes.acf.report.*;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;
import baccara.acf.*;
import gengen.util.*;

/**
 * A code generator for the basic application data writer.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 05.09.2016 - Added.
 */

public class AbstractApplicationDataWriterCodeGenerator extends BaccaraBaseCodeGenerator {

    private static AbstractApplicationDataWriterCodeGenerator instance =
            new AbstractApplicationDataWriterCodeGenerator();

    /**
     * Returns the class name for the code generator for the passed table.
     *
     * @param table The table which the class name is to create for.
     * @return The class name for the code generator for the passed table.
     *
     * @changed OLI 05.09.2016 - Added.
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
     * @changed OLI 05.09.2016 - Added.
     */
    public static String packageName(TableModel table) {
        return instance.getPackageName(table);
    }

    /**
     * @changed OLI 05.09.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return "AbstractApplicationDataWriter";
    }

    /**
     * @changed OLI 05.09.2016 - Added.
     */
    @Override public String getSubPackage() {
        return "persistence";
    }

    /**
     * @changed OLI 05.09.2016 - Added.
     */
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.MODEL;
    }

    /**
     * @changed OLI 05.09.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "A basic data writer for the application.\n"
                + " *\n"
                + " * @param <LM> The class which manages the list of data.\n"
                + " * @param <T> The type of the objects in the list.";
    }

    /**
     * @changed OLI 05.09.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        if (dm.isOptionSet(ModelParamIds.PERSISTENCE) && dm.getOptionByName(
                ModelParamIds.PERSISTENCE).getParameter().equalsIgnoreCase("JS")) {
            this.imports.add("baccara.persistence");
            String code = "abstract public class " + className + "<LM, T> extends "
                    + "AbstractDataWriter<LM, T> {\n\n";
            this.createGetImports(dm);
            return new GeneratorResult(code, GeneratorResultState.SUCCESS);
        }
        return new GeneratorResult("", GeneratorResultState.NOT_NECESSARY);
    }

    private void createGetImports(DataModel dm) {
        String methodName = "getImports";
        String code = this.createOverrideComment();
        this.imports.add("java.util");
        code += "    @Override public List<String> " + methodName + "() {\n";
        code += "        List<String> l = super.getImports();\n";
        code += "        l.add(\"Packages." + dm.getBasePackageName() + ".business\");\n";
        code += "        l.add(\"Packages." + dm.getBasePackageName() + ".enums\");\n";
        code += "        return l;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    /**
     * @changed OLI 05.09.2016 - Added.
     */
    @Override public boolean isOneTimeFactory() {
        return true;
    }

}