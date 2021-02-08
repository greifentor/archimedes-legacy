/*
 * GeneratedPOJOChangeListenerCodeGenerator.java
 *
 * 02.05.2016
 *
 * (c) by HealthCarion
 *
 */

package baccara.acf.entities;

import baccara.acf.*;
import gengen.util.*;

import java.util.*;

import archimedes.acf.*;
import archimedes.acf.report.*;
import archimedes.model.*;


/**
 * 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 02.05.2016 - Added.
 */

public class GeneratedPOJOChangeListenerCodeGenerator extends BaccaraBaseCodeGenerator {

    private static GeneratedPOJOChangeListenerCodeGenerator instance
            = new GeneratedPOJOChangeListenerCodeGenerator();

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
        return tm.getName() + "ChangeListener";
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
        return "A listener interface to observe " + this.getDescription(tm) + " objects for "
                + "changes.";
    }

    /**
     * @changed OLI 05.04.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className, 
            List<PostGeneratingCommand> postGeneratorCommands) {
        String code = "public interface " + className + " {\n\n";
        code += "    /**\n";
        code += "     * Called if an attribute of the object has been changed.\n";
        code += "     *\n";
        code += "     * @param e The event object with the data of the change.\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    abstract public void objectChanged("
                + GeneratedPOJOChangeListenerEventCodeGenerator.className(tm) + " e);\n";
        return new GeneratorResult(code, GeneratorResultState.SUCCESS);
    }

    /**
     * @changed OLI 02.05.2016 - Added.
     */
    @Override public String[] getNecessaryIncludeMarks() {
        return new String[] {TableParamIds.FIRE_ENTITY_EVENTS};
    }

}