/*
 * PrimaryKeyCodeGenerator.java
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
import archimedes.model.*;
import baccara.acf.*;


/**
 * A code generator for the primary key class which could be manipulated by the user.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.05.2016 - Added.
 */

public class PrimaryKeyCodeGenerator extends BaccaraBaseCodeGenerator {

    private static PrimaryKeyCodeGenerator instance = new PrimaryKeyCodeGenerator();

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
        return tm.getName() + "PrimaryKey";
    }

    /**
     * @changed OLI 04.05.2016 - Added.
     */
    @Override public String getSubPackage() {
        return "business";
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
            this.imports.add("baccara.data");
            this.imports.add(GeneratedPrimaryKeyCodeGenerator.packageName(tm));
            String code = "public class " + className + " extends "
                    + GeneratedPrimaryKeyCodeGenerator.className(tm) + " {\n";
            code += "\n";
            code += this.createConstructor(pkm, className, codeUtil);
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
        String ps = "";
        for (ColumnModel c : pkm) {
            if (p.length() > 0) {
                p += ", ";
            }
            p += this.getPrimaryKeyIdType(c) + " " + this.getAttributeName(c);
            if (ps.length() > 0) {
                ps += ", ";
            }
            ps += this.getAttributeName(c);
        }
        code += p;
        code += ") {\n";
        code += "        super(" + ps + ");\n";
        code += "    }\n";
        code += "\n";
        return code;
    }

    /**
     * @changed OLI 04.05.2016 - Added.
     */
    @Override public boolean isOneTimeFactory() {
        return true;
    }

}