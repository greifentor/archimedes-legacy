/*
 * GeneratedPrimaryKeyCodeGenerator.java
 *
 * 04.05.2016
 *
 * (c) by HealthCarion
 *
 */

package baccara.acf.entities;

import gengen.util.*;

import java.util.*;

import archimedes.acf.*;
import archimedes.acf.report.*;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;
import baccara.acf.*;


/**
 * A code generator for primary keys.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.05.2016 - Added.
 */

public class GeneratedPrimaryKeyCodeGenerator extends BaccaraBaseCodeGenerator {

    private static GeneratedPrimaryKeyCodeGenerator instance =
            new GeneratedPrimaryKeyCodeGenerator();

    /**
     * Returns the class name for the code generator for the passed table.
     *
     * @param table The table which the class name is to create for.
     * @return The class name for the code generator for the passed table.
     *
     * @changed OLI 04.05.2016 - Added.
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
     * @changed OLI 04.05.2016 - Added.
     */
    public static String packageName(TableModel table) {
        return instance.getPackageName(table);
    }

    /**
     * @changed OLI 04.05.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return "Generated" + tm.getName() + "PrimaryKey";
    }

    /**
     * @changed OLI 04.05.2016 - Added.
     */
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.TABLE;
    }

    /**
     * @changed OLI 04.05.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "A primary key of a(n) " + this.getDescription(tm) + ".";
    }

    /**
     * @changed OLI 04.05.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        ColumnModel[] pkm = tm.getPrimaryKeyColumns();
        if (pkm.length == 1) {
            String pkIdType = this.getPrimaryKeyIdType(pkm[0]);
            this.imports.add("baccara.data");
            String code = "public class " + className + " implements PrimaryKey<" + pkIdType
                    + "> {\n";
            code += "\n";
            code += "    private " + pkIdType + " " + this.getAttributeName(pkm[0]) + ";\n";
            code += "\n";
            code += this.createConstructor(pkm, className, codeUtil);
            this.createGetPrimaryKeyMethod(pkm[0], className, codeUtil);
            return new GeneratorResult(code, GeneratorResultState.SUCCESS);
        }
        return new GeneratorResult("", GeneratorResultState.NOT_NECESSARY);
    }

    private String getPrimaryKeyIdType(ColumnModel pkc) {
        return this.typeUtil.getWrapperClass(this.getJavaType(pkc));
    }

    private String createConstructor(ColumnModel[] pkm, String className, CodeUtil codeUtil) {
        String code = "    /**\n";
        code += "     * Creates a new primary key with the passed id.\n";
        code += "     *\n";
        code += "     * @param id The id of the primary key.\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     *\n";
        code += "     */\n";
        code += "    public " + className + "(";
        String p = "";
        for (ColumnModel c : pkm) {
            if (p.length() > 0) {
                p += ", ";
            }
            p += this.getPrimaryKeyIdType(c) + " " + this.getAttributeName(c);
        }
        code += p;
        code += ") {\n";
        code += "        super();\n";
        for (ColumnModel c : pkm) {
            this.imports.addStatic("corentx.util.Checks.*;");
            code += "        ensure(" + this.getAttributeName(c) + " != null, \""
                    + this.getDescription(c) + " cannot be null.\");\n";
        }
        for (ColumnModel c : pkm) { 
            code += "        this." + this.getAttributeName(c) + " = " + this.getAttributeName(c
                    ) + ";\n";
        }
        code += "    }\n";
        code += "\n";
        return code;
    }

    private void createGetPrimaryKeyMethod(ColumnModel c, String className, CodeUtil codeUtil) {
        String methodName = "getId";
        String code = "    /**\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    @Override public " + this.getPrimaryKeyIdType(c) + " " + methodName
                + "() {\n";
        code += "        return this." + this.getAttributeName(c) + ";\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

}