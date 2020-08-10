/*
 * POJOCodeGenerator.java
 *
 * 17.05.2016
 *
 * (c) by HealthCarion
 *
 */

package baccara.acf.entities;

import corentx.util.*;
import corentx.util.SortedVector;
import gengen.util.*;

import java.util.*;

import archimedes.acf.*;
import archimedes.acf.report.*;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;
import baccara.acf.*;
import baccara.acf.ColParamIds;


/**
 * A code generator for POJO's.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.05.2016 - Added.
 */

public class POJOCodeGenerator extends BaccaraBaseCodeGenerator {

    private static POJOCodeGenerator instance = new POJOCodeGenerator();

    /**
     * Returns the class name for the code generator for the passed table.
     *
     * @param table The table which the class name is to create for.
     * @return The class name for the code generator for the passed table.
     *
     * @changed OLI 17.05.2016 - Added.
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
     * @changed OLI 17.05.2016 - Added.
     */
    public static String packageName(TableModel table) {
        return instance.getPackageName(table);
    }

    /**
     * @changed OLI 17.05.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return tm.getName();
    }

    /**
     * @changed OLI 17.05.2016 - Added.
     */
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.TABLE;
    }

    /**
     * @changed OLI 17.05.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "A POJO to represent a(n) " + this.getDescription(tm) + ".";
    }

    /**
     * @changed OLI 17.05.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        this.imports.add(GeneratedPOJOCodeGenerator.packageName(tm));
        String code = "public class " + className + " extends "
                + GeneratedPOJOCodeGenerator.className(tm) + " {\n\n";
        code += this.createSimpleConstructor(tm);
        code += this.createCopyConstructor(tm);
        code += this.createConstructorForComplexPrimaryKeys(tm);
        code += this.createConstructorForEmbeddedClasses(tm);
        if (tm.isNMRelation()) {
            this.createRelationConstructor(tm);
        }
        return new GeneratorResult(code, GeneratorResultState.SUCCESS);
    }

    protected String createSimpleConstructor(TableModel t) {
        String className = this.getClassName(t);
        List<ColumnModel> unchangeables = new SortedVector<ColumnModel>();
        for (ColumnModel c : t.getColumns()) {
            if (c.isOptionSet(ColParamIds.UNCHANGEABLE)) {
            // if (this.parameterUtil.getParameters(c).contains(ColParamIds.UNCHANGEABLE)) {
                unchangeables.add(c);
            }
        }
        String s = "    /**\n";
        s += "     * Creates a new object with default values.\n";
        s += "     *\n";
        if (unchangeables.size() > 0) {
            for (ColumnModel c : unchangeables) {
                s += "     * " + this.createParameterTag(c, "A " + this.getAttributeName(c)
                        + " for the object.\n");
            }
            s += "     *\n";
        }
        s += "     * " + this.createMethodChangedTag() + "\n";
        s += "     */\n";
        s += "    public " + className + "(";
        for (ColumnModel c : unchangeables) {
            String an = this.getAttributeName(c);
            String type = this.getJavaType(c, false, true);
            s += (unchangeables.get(0) != c ? ", " : "") + type + " " + an;
        }
        s += ") {\n";
        s += "        super(";
        for (ColumnModel c : unchangeables) {
            String an = this.getAttributeName(c);
            this.addEnumPackageIfNecessary(c.getDomain());
            s += (unchangeables.get(0) != c ? ", " : "") + an;
        }
        s += ");\n";
        s += "    }\n";
        s += "\n";
        return s;
    }

    private String createCopyConstructor(TableModel t) {
        String className = this.getClassName(t);
        String s = "    /**\n";
        s += "     * Creates a new object as a copy of the passed one.\n";
        s += "     *\n";
        s += "     * " + this.createParameterTag("o", "The object to copy.") + "\n";
        s += "     *\n";
        s += "     * " + this.createMethodChangedTag() + "\n";
        s += "     */\n";
        s += "    public " + className + "(" + className + " o) {\n";
        s += "        super(o);\n";
        s += "    }\n";
        s += "\n";
        return s;
    }

    private String createConstructorForComplexPrimaryKeys(TableModel t) {
        if ((t.getPrimaryKeyColumns().length <= 1)) {
            return "";
        }
        String className = this.getClassName(t);
        String s = "    /**\n";
        s += "     * Creates with the passed primary key components.\n";
        s += "     *\n";
        for (ColumnModel c : t.getPrimaryKeyColumns()) {
            s += "     * " + this.createParameterTag(this.getAttributeName(c), "The "
                    + this.getDescription(c) + " component of the primary key.") + "\n";
        }
        s += "     *\n";
        s += "     * " + this.createMethodChangedTag() + "\n";
        s += "     */\n";
        s += "    public " + className + "(";
        String a = "";
        for (ColumnModel c : t.getPrimaryKeyColumns()) {
            a += (a.length() > 0 ? ", " : "") + this.getTypeAndName(c); 
        }
        s += a + ") {\n";
        s += "        super();\n";
        for (ColumnModel c : t.getPrimaryKeyColumns()) {
            if (!c.isOptionSet(ColParamIds.NO_SETTER)) {
                s += "        this.set" + c.getName() + "(" + this.getAttributeName(c) + ");\n";
            }
        }
        s += "    }\n";
        s += "\n";
        return s;
    }

    private String createConstructorForEmbeddedClasses(TableModel tm) {
        String code = "";
        if (tm.isOptionSet(TableParamIds.EMBEDDED)) {
            ColumnModel ercm = null;
            for (ColumnModel c : tm.getColumns()) {
                if (c.isOptionSet(ColParamIds.EMBEDDED_REFERENCE)) {
                    ercm = c;
                    break;
                }
            }
            Checks.ensure(ercm != null, "embedded reference not set for table: " + tm.getName()
                    );
            Checks.ensure(ercm.getRelation() != null, "embedded reference is not referenced: "
                    + ercm.getFullName());
            String className = this.getClassName(tm);
            ColumnModel kccm = ercm.getRelation().getReferenced();
            this.imports.add(packageName(kccm.getTable()));
            String a = this.getAttributeName(ercm);
            code = "    /**\n";
            code += "     * Creates a new object with the passed data.\n";
            code += "     *\n";
            code  += "     * " + this.createParameterTag(this.getAttributeName(a), "The "
                        + this.getDescription(a) + " which the object is assigned to.") + "\n";
            code += "     *\n";
            code += "     * " + this.createMethodChangedTag() + "\n";
            code += "     */\n";
            code += "    public " + className + "(" + className(kccm.getTable()) + " " + a
                    + ") {\n";
            code += "        super();\n";
            code += "        this.set" + ercm.getName() + "(" + a + ");\n";
            code += "    }\n";
            code += "\n";
        }
        return code;
    }

    private String createRelationConstructor(TableModel tm) {
        String className = this.getClassName(tm);
        String code = this.createOverrideComment();
        code += "    public " + className + "(";
        String a = "";
        for (ColumnModel c : tm.getColumns()) {
            if (c.isTransient() || c.isOptionSet(ColParamIds.NO_SETTER)) {
                continue;
            }
            if (a.length() > 0) {
                a += ", ";
            }
            a += this.getTypeAndName(c);
        }
        code += a + ") {\n";
        a = "";
        for (ColumnModel c : tm.getColumns()) {
            if (c.isTransient() || c.isOptionSet(ColParamIds.NO_SETTER)) {
                continue;
            }
            if (a.length() > 0) {
                a += ", ";
            }
            a += this.getAttributeName(c);
        }
        code += "        super(" + a + ");\n";
        code += "    }\n";
        code += "\n";
        return code;
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