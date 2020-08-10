/*
 * GeneratedPOJOAttributeIdCodeGenerator.java
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
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;
import baccara.acf.*;


/**
 * A code generators for the attribute id enum class.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 02.05.2016 - Added.
 */

public class GeneratedPOJOAttributeIdCodeGenerator extends BaccaraBaseCodeGenerator {


    private static GeneratedPOJOAttributeIdCodeGenerator instance
            = new GeneratedPOJOAttributeIdCodeGenerator();

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
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.TABLE;
    }

    /**
     * @changed OLI 05.04.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "An enum with attribute id's for " + this.getDescription(tm) + " objects.";
    }

    /**
     * @changed OLI 05.04.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className, 
            List<PostGeneratingCommand> postGeneratorCommands) {
        String code = "public enum " + className + " {\n\n";
        List<String> ids = new SortedVector<String>();
        List<TableModel> tms = new SortedVector<TableModel>();
        for (ColumnModel c : this.getColumnsIncludingInherited(tm)) {
            String id = this.createAttrIdName(c);
            if (!ids.contains(id)) {
                ids.add(id);
            }
            if (!tms.contains(c.getTable())) {
                tms.add(c.getTable());
            }
        }
        for (TableModel tm0 : tms) {
            for (TableModel t : this.getListInclusions(tm0)) {
                String id = this.createAttrIdName(this.getListAttributeName(t));
                if (!ids.contains(id)) {
                    ids.add(id);
                }
            }
        }
        for (int i = 0, leni = ids.size(); i < leni; i++) {
            code += "    " + ids.get(i) + (i+1 < leni ? "," : ";") + "\n";
        }
        return new GeneratorResult(code, GeneratorResultState.SUCCESS);
    }

    /**
     * @changed OLI 05.04.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return tm.getName() + "AttributeId";
    }

}