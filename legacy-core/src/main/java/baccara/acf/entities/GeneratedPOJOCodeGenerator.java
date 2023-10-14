/*
 * GeneratedPOJOCodeGenerator.java
 *
 * 31.03.2016
 *
 * (c) by HealthCarion
 *
 */

package baccara.acf.entities;

import baccara.acf.*;
import baccara.acf.ColParamIds;
import baccara.acf.TableParamIds;
import corentx.util.*;
import gengen.util.*;

import java.util.*;

import archimedes.acf.*;
import archimedes.acf.report.*;
import archimedes.model.*;


/**
 * A code generator for entities.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 31.03.2016 - Added.
 */

public class GeneratedPOJOCodeGenerator extends BaccaraBaseCodeGenerator {

    private static GeneratedPOJOCodeGenerator instance = new GeneratedPOJOCodeGenerator();

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
     * @changed OLI 23.05.2016 - Added.
     */
    @Override public String getAlternateReferenceTableClassName(TableModel tm) {
        this.imports.add(POJOCodeGenerator.packageName(tm));
        return POJOCodeGenerator.className(tm);
    }

    /**
     * @changed OLI 05.04.2016 - Added.
     */
    @Override public String getClassName(TableModel tm) {
        return "Generated" + tm.getName();
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
        return "A POJO to represent a(n) " + this.getDescription(tm) + ".";
    }

    /**
     * @changed OLI 05.04.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        GeneratorResultState resultState = GeneratorResultState.SUCCESS;
        boolean changeListenerImplementation = tm.isOptionSet(TableParamIds.FIRE_ENTITY_EVENTS);
        boolean inherited = tm.isOptionSet(TableParamIds.INHERITS);
        TableModel[] tils = this.getListInclusions(tm);
        for (ColumnModel c : tm.getColumns()) {
            this.ensure(c.getDomain() != null, BaccaraCodeFactory.NAME, "Column '"
                    + c.getFullName() + "' has no domain assigned!", "ColumnWithOutDomain",
                    c.getName(), tm.getName());
        }
        String code = "public class " + className;
        if (inherited) {
            this.imports.add(POJOCodeGenerator.packageName(tm));
            /*
            String ptn = this.parameterUtil.getParameterStartsWith(TableParamIds.INHERITS, tm);
            ptn = ptn.replace(TableParamIds.INHERITS, "").replace(":", "").trim();
            */
            String ptn = tm.getOptionByName(TableParamIds.INHERITS).getParameter().trim();
            TableModel ptm = dm.getTableByName(ptn);
            if (ptm != null) {
                code += " extends " + POJOCodeGenerator.className(ptm);
            } else {
                return new GeneratorResult("", GeneratorResultState.FAILURE);
            }
        }
        List<String> implementations = new SortedVector<String>();
        if (dm.isOptionSet(ModelParamIds.SERIALIZABLE)) {
            this.imports.add("java.io");
            implementations.add("Serializable");
        }
        if ((tm.getPrimaryKeyColumns().length == 1) && (this.getJavaType(
                tm.getPrimaryKeyColumns()[0]).equals("long"))) {
            this.imports.add("baccara.data");
            implementations.add("LongIdProvider");
        }
        if (tm.isOptionSet(TableParamIds.SEPARATED_DATA_STOCK)) {
            this.imports.add("baccara.data");
            implementations.add("CustomerObjectLogic");
        }
        String compareClassName = null;
        if ((tm.getCompareMembers().length > 0) || tm.isOptionSet(TableParamIds.COMPARABLE)) {
            compareClassName = POJOCodeGenerator.className(tm);
            this.imports.add(POJOCodeGenerator.packageName(tm));
            implementations.add("Comparable<" + compareClassName + ">");
        }
        for (OptionModel o : tm.getOptions()) {
            if (o.getName().equals(TableParamIds.IMPLEMENTS)) {
                if (o.getParameter().startsWith("ENTITY=")) {
                    implementations.add(o.getParameter().replace("ENTITY=", ""));
                }
            }
        }
        /*
        if (!tm.isOptionSet(TableParamIds.NO_GUI)) {
            this.imports.add("baccara.generics");
            this.imports.add(GeneratedPOJOAttributeIdCodeGenerator.packageName(tm));
            code += " implements BaccaraGenericObject<"
                    + GeneratedPOJOAttributeIdCodeGenerator.className(tm) + ">";
            this.createBaccaraGenericObjectMethods(tm, codeUtil);
        }
        */
        String a = "";
        for (String i : implementations) {
            if (a.length() > 0) {
                a += ", ";
            }
            a += i;
        }
        code += (a.length() > 0 ? " implements " + a : "") + " {\n";
        AttributeBlockConfiguration abc = new AttributeBlockConfiguration(tm,
                this.getAttributeColumns(tm, inherited), codeUtil);
        code += "\n" + this.createAttributeDefinitionBlock(abc);
        if (changeListenerImplementation) {
            String listenerClassName = GeneratedPOJOChangeListenerCodeGenerator.className(tm);
            this.imports.add("java.util");
            code += "    private List<" + listenerClassName + "> listeners = new LinkedList<"
                    + listenerClassName + ">();\n\n";
        }
        if (tils.length > 0) {
            code += this.checkForListAttributes(tils) + "\n";
        }
        code += this.createSimpleConstructor(tm);
        code += this.createCopyConstructor(tm, tils, inherited);
        if (tm.isNMRelation()) {
            code += this.createRelationConstructor(tm);
        }
        GetterConfiguration gc = new GetterConfiguration(tm, codeUtil);
        if (tm.isOptionSet(TableParamIds.DEPENDENT)) {
            gc = gc.setNoPrimaryKey();
        }
        this.imports.add(PrimaryKeyCodeGenerator.packageName(tm));
        this.createGetters(gc);
        SetterConfiguration sc = new SetterConfiguration(tm, codeUtil).setTimestampAsReference(
                );
        if (changeListenerImplementation) {
            sc = sc.setChangeListenerLogicRequired();
        }
        this.createSetters(sc);
        if (inherited) {
            this.cleanupGettersAndSettersForSubClasses(tm);
        }
        if ((tm.getCompareMembers().length > 0) || tm.isOptionSet(TableParamIds.COMPARABLE)) {
            this.createCompareTo(className, tm, compareClassName);
        }
        this.createEquals();
        this.createHashCode();
        this.createToString(tm, false);
        if (changeListenerImplementation) {
            this.createChangeListenerLogic(tm);
        }
        if (tils.length > 0) {
            this.createListMethods(tm, tils, codeUtil, sc);
        }
        return new GeneratorResult(code, resultState);
    }

    /*
    private void createBaccaraGenericObjectMethods(TableModel tm, CodeUtil codeUtil) {
        this.createBaccaraGenericObjectGetAttributeByIdMethod(tm, codeUtil);
        this.createBaccaraGenericObjectIsAttributeNullableMethod(tm, codeUtil);
        this.createBaccaraGenericObjectSetAttributeByIdMethod(tm, codeUtil);
    }

    private void createBaccaraGenericObjectGetAttributeByIdMethod(TableModel tm,
            CodeUtil codeUtil) {
        String methodName = "getAttributeById";
        String code = "    /**\n";
        this.imports.add(GeneratedPOJOAttributeIdCodeGenerator.packageName(tm));
        String idClassName = GeneratedPOJOAttributeIdCodeGenerator.className(tm);
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     * /\n";
        code += "    @Override public Object " + methodName + "(" + idClassName + " id) {\n";
        boolean first = true;
        for (ColumnModel cm : tm.getColumns()) {
            if (this.isIgnoredForEmbedded(cm) || cm.isOptionSet("NO_GETTER")) {
                continue;
            }
            if (first) {
                first = false;
                code += "        if (";
            } else {
                code += "        } else if (";
            }
            code += idClassName + "." + this.createAttrIdName(cm) + ".equals(id)) {\n";
            code += "            return this." + this.createGetterName(cm) + "();\n";
        }
        if (!first) {
            code += "        }\n";
            code += "        throw new IllegalArgumentException(\"attribute with id doesn't "
                    + "exists: \" + id);\n";
        }
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createBaccaraGenericObjectIsAttributeNullableMethod(TableModel tm,
            CodeUtil codeUtil) {
        String methodName = "isAttributeNullable";
        String code = "    /**\n";
        this.imports.add(GeneratedPOJOAttributeIdCodeGenerator.packageName(tm));
        String idClassName = GeneratedPOJOAttributeIdCodeGenerator.className(tm);
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     * /\n";
        code += "    @Override public boolean " + methodName + "(" + idClassName + " id) {\n";
        boolean first = true;
        for (ColumnModel cm : tm.getColumns()) {
            if (!cm.isNotNull()) {
                if (first) {
                    first = false;
                    code += "        if ((";
                } else {
                    code += "|| (";
                }
                code += idClassName + "." + this.createAttrIdName(cm) + ".equals(id))";
            }
        }
        if (!first) {
            code += ") {\n";
            code += "            return true;\n";
            code += "        }\n";
        }
        code += "        return false;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createBaccaraGenericObjectSetAttributeByIdMethod(TableModel tm,
            CodeUtil codeUtil) {
        String methodName = "setAttributeById";
        String code = "    /**\n";
        this.imports.add(GeneratedPOJOAttributeIdCodeGenerator.packageName(tm));
        String idClassName = GeneratedPOJOAttributeIdCodeGenerator.className(tm);
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     * /\n";
        code += "    @Override public void " + methodName + "(" + idClassName + " id, "
                + "Object value) {\n";
        boolean first = true;
        for (ColumnModel cm : tm.getColumns()) {
            if (this.isIgnoredForEmbedded(cm) || cm.isOptionSet("ASSEMBLY") || cm.isOptionSet(
                    "NO_SETTER")) {
                continue;
            }
            if (first) {
                first = false;
                code += "        if (";
            } else {
                code += "        } else if (";
            }
            code += idClassName + "." + this.createAttrIdName(cm) + ".equals(id)) {\n";
            code += "            this.set" + cm.getName() + "(("
                    + this.typeUtil.getWrapperClass(this.getJavaType(cm, false, false))
                    + ") value);\n"; 
            code += "            return;\n";
        }
        if (!first) {
            code += "        }\n";
            code += "        throw new IllegalArgumentException(\"attribute with id doesn't "
                    + "exists: \" + id);\n";
        }
        code += "    }\n";
        this.storeMethod(methodName, code);
    }
    */

    private ColumnModel[] getAttributeColumns(TableModel tm, boolean inherited) {
        List<ColumnModel> l = new LinkedList<ColumnModel>();
        for (ColumnModel c : tm.getColumns()) {
            if (inherited && c.isPrimaryKey()) {
                continue;
            }
            l.add(c);
        }
        return l.toArray(new ColumnModel[0]);
    }

    private String checkForListAttributes(TableModel[] tils) {
        String code = "";
        for (TableModel tm : tils) {
            String cn = this.getReferenceClassName(tm);
            String listType = this.getListType(tm);
            code += "    protected List<" + cn + "> " + this.getListAttributeName(tm) + " = "
                    + "new " + listType + "<" + cn + ">();\n";
        }
        return code;
    }

    private String getListType(TableModel tm) {
        this.imports.add("java.util");
        String listType = null;
        if ((tm.getCompareMembers().length > 0) || tm.isOptionSet(TableParamIds.COMPARABLE))
                {
            this.imports.add("corentx.util");
            listType = "SortedVector";
        } else {
            listType = "LinkedList";
        }
        return listType;
    }

    protected String createSimpleConstructor(TableModel t) {
        String className = this.getClassName(t);
        List<ColumnModel> unchangeables = new SortedVector<ColumnModel>();
        for (ColumnModel c : t.getColumns()) {
            if (this.parameterUtil.getParameters(c).contains(ColParamIds.UNCHANGEABLE)) {
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
        s += "        super();\n";
        for (ColumnModel c : unchangeables) {
            if (c.isNotNull() && !this.isSimpleType(this.getJavaType(c))) {
                this.imports.addStatic("corentx.util.Checks");
                String an = this.getAttributeName(c);
                s += "        ensure(" + an + " != null, \"" + an + " cannot be null.\");\n";
            }
        }
        for (ColumnModel c : unchangeables) {
            String an = this.getAttributeName(c);
            s += "        this." + an + " = " + an + ";\n";
        }
        s += "    }\n";
        s += "\n";
        return s;
    }

    private String createCopyConstructor(TableModel t, TableModel[] tils, boolean inherited) {
        String className = this.getClassName(t);
        String s = "    /**\n";
        s += "     * Creates a new object as a copy of the passed one.\n";
        s += "     *\n";
        s += "     * " + this.createParameterTag("o", "The object to copy.") + "\n";
        s += "     *\n";
        s += "     * " + this.createMethodChangedTag() + "\n";
        s += "     */\n";
        s += "    public " + className + "(" + className + " o) {\n";
        s += "        super(" + (inherited ? "o" : "") + ");\n";
        for (ColumnModel c : t.getColumns()) {
            if (!c.isOptionSet(ColParamIds.NO_SETTER)) {
                String gn = this.createGetterName(c);
                s += "        this.set" + c.getName() + "(o." + gn + "());\n";
            }
        }
        for (TableModel til : tils) {
            if (til.isNMRelation()) {
                String rcn = this.getReferencingColumnName(t, til);
                String pcn = POJOCodeGenerator.className(t);
                String cn = POJOCodeGenerator.className(til);
                this.imports.add(POJOCodeGenerator.packageName(til));
                this.imports.add(POJOCodeGenerator.packageName(t));
                s += "        for (" + cn + " lo : o.get" + til.getName() + "s()) {\n";
                s += "            " + cn + " c = new " + cn + "(lo);\n";
                s += "            c.set" + rcn + "((" + pcn + ") this);\n";
                s += "            this.add" + til.getName() + "(c);\n";
                s += "        }\n";
            } else if (til.isOptionSet(TableParamIds.DEPENDENT)) {
                String rcn = this.getReferencingColumnName(t, til);
                String pcn = POJOCodeGenerator.className(t);
                String cn = POJOCodeGenerator.className(til);
                this.imports.add(POJOCodeGenerator.packageName(til));
                this.imports.add(POJOCodeGenerator.packageName(t));
                s += "        for (" + cn + " lo : o.get" + til.getName() + "s()) {\n";
                s += "            " + cn + " c = new " + cn + "(lo);\n";
                s += "            c.set" + rcn + "((" + pcn + ") this);\n";
                s += "            this.add" + til.getName() + "(c);\n";
                s += "        }\n";
            }
            // TODO
        }
        s += "    }\n";
        s += "\n";
        return s;
    }

    private String getReferencingColumnName(TableModel parent, TableModel referencer) {
        for (ColumnModel c : referencer.getColumns()) {
            if (c.getReferencedTable() != null) {
                if (c.getReferencedTable().getName().equals(parent.getName())) {
                    return c.getName();
                }
            }
        }
        return null;
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
        for (ColumnModel c : tm.getColumns()) {
            if (c.isTransient() || c.isOptionSet(ColParamIds.NO_SETTER)) {
                continue;
            }
            code += "        this.set" + c.getName() + "(" + this.getAttributeName(c) + ");\n";
        }
        code += "    }\n";
        code += "\n";
        return code;
    }

    private void cleanupGettersAndSettersForSubClasses(TableModel tm) {
        for (ColumnModel c : tm.getColumns()) {
            if (c.isPrimaryKey()) {
                this.methodCodeStorage.remove(this.createGetterName(c));
                this.methodCodeStorage.remove("set" + c.getName());
            }
        }
        this.methodCodeStorage.remove("getPrimaryKey");
    }

    private void createCompareTo(String className, TableModel tm, String compareClassName) {
        String methodName = "compareTo";
        String code = createOverrideComment("Generated.");
        code += "    @Override public int compareTo(" + compareClassName + " c) {\n";
        if (tm.getCompareMembers().length > 0) {
            boolean first = true;
            for (ColumnModel c : tm.getCompareMembers()) {
                if (!first) {
                    code += "        if (i == 0) {\n";
                }
                String jt = this.getJavaType(c);
                boolean simpleType = this.isSimpleType(jt);
                String wcn = this.typeUtil.getWrapperClass(jt);
                String gn = this.createGetterName(c) + "()";
                code += "        " + (first ? "int " : "    ") + "i = " + (simpleType ? "new "
                        + wcn + "(" : "") + "this." + gn + (simpleType ? ")" : "")
                        + ".compareTo(" + (simpleType ? "new " + wcn + "(" : "") + "c." + gn
                        + (simpleType ? ")" : "") + ");\n";
                if (!first) {
                    code += "        }\n";
                }
                first = false;
            }
            code += "        return i;\n";
        } else {
            code += "        return 0;\n";
        }
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createChangeListenerLogic(TableModel tm) {
        String listenerClassName = GeneratedPOJOChangeListenerCodeGenerator.className(tm);
        this.createAddChangeListenerMethod(tm, listenerClassName);
        this.createFireChangeListenerEventMethod(tm, listenerClassName);
        this.createRemoveChangeListenerMethod(tm, listenerClassName);
    }

    private void createAddChangeListenerMethod(TableModel tm, String listenerClassName) {
        String methodName = "add" + listenerClassName;
        String code = "    /**\n";
        code += "     * Adds the passed listener to the listeners which are observing the "
                + "object.\n";
        code += "     *\n";
        code += "     * @param l The listener to add.\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    public void " + methodName + "(" + listenerClassName + " l) {\n";
        code += "        if (!this.listeners.contains(l)) {\n";
        code += "            this.listeners.add(l);\n";
        code += "        }\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createFireChangeListenerEventMethod(TableModel tm, String listenerClassName) {
        String eventClassName = GeneratedPOJOChangeListenerEventCodeGenerator.className(tm);
        String methodName = "fire" + eventClassName;
        String code = "    /**\n";
        code += "     * Fires an event for a change in the objet.\n";
        code += "     *\n";
        code += "     * @param event The event to fire.\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    public void " + methodName + "(" + eventClassName + " event) {\n";
        code += "        try {\n";
        code += "            for (" + listenerClassName + " l : this.listeners) {\n";
        code += "                try {\n";
        code += "                    l.objectChanged(event);\n";
        code += "                } catch (Exception e) {\n";
        code += "                    System.out.println(\"error while processing change event:"
                + " \" + event);\n";
        code += "                }\n";
        code += "            }\n";
        code += "        } catch (Exception e) {\n";
        code += "            System.out.println(\"error while processing change events: \" + "
                + "event);\n";
        code += "        }\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createRemoveChangeListenerMethod(TableModel tm, String listenerClassName) {
        String methodName = "remove" + listenerClassName;
        String code = "    /**\n";
        code += "     * Removes the passed listener from the listeners which are observing the "
                + "object.\n";
        code += "     *\n";
        code += "     * @param l The listener to remove.\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    public void " + methodName + "(" + listenerClassName + " l) {\n";
        code += "        this.listeners.remove(l);\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createListMethods(TableModel tm, TableModel[] tils, CodeUtil codeUtil,
            SetterConfiguration sc) {
        for (TableModel t : tils) {
            this.createAddListElementMethod(t, codeUtil, sc);
            this.createClearListElementsMethod(t, codeUtil);
            this.createGetListElementsMethod(t, codeUtil);
            this.createRemoveListElementMethod(t, codeUtil, sc);
            if ((t.getPrimaryKeyColumns().length == 2) && t.isOptionSet(TableParamIds.DEPENDENT)
                    ) {
                this.createGetListElementByKeyMethod(tm, t);
            }
        }
    }

    private void createAddListElementMethod(TableModel t, CodeUtil codeUtil,
            SetterConfiguration sc) {
        // TODO
        String methodName = "add" + t.getName();
        String an = this.getAttributeName(t);
        String ln = "this." + this.getListAttributeName(t);
        String tn = this.getReferenceClassName(t);
        String code = "    /**\n";
        code += "     * Adds the passed object to the list of " + this.getAttributeName(t)
                + " objects.\n";
        code += "     *\n";
        code += "     * " + this.createParameterTag(an, "The object to add.") + "\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    public void " + methodName + "(" + tn + " " + an + ") {\n";
        code += "        if (!" + ln + ".contains(" + an + ")) {\n";
        code += "            " + ln + ".add(" + an + ");\n";
        if (sc.isChangeListenerLogicRequired()) {
            String attrIdClassName = GeneratedPOJOAttributeIdCodeGenerator.className(
                    sc.getTable());
            String eventClassName = GeneratedPOJOChangeListenerEventCodeGenerator.className(
                    sc.getTable());
            String mn = "fire" + eventClassName;
            code += "            this." + mn + "(new " + eventClassName + "("
                    + attrIdClassName + "."+ this.createAttrIdName(this.getListAttributeName(t))
                    + ", " + an + ", " + eventClassName + ".Type.ADD));\n";
        }
        code += "        }\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private String getReferenceClassName(TableModel tm) {
        if (this.getAlternateReferenceTableClassName(tm) != null) {
            return this.getAlternateReferenceTableClassName(tm);
        }
        return this.getClassName(tm);
    }

    private void createClearListElementsMethod(TableModel t, CodeUtil codeUtil) {
        String methodName = "clear" + t.getName() + "s";
        String ln = "this." + this.getListAttributeName(t);
        String code = "    /**\n";
        code += "     * Removes all " + this.getAttributeName(t) + " objects.\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    public void " + methodName + "() {\n";
        code += "        " + ln + ".clear();\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createGetListElementByKeyMethod(TableModel t, TableModel rt) {
        String methodName = "get" + rt.getName();
        ColumnModel kc = this.getPKRefTable(rt, t);
        Checks.ensure(kc != null, "table '" + rt.getName() + "' does not have any PK reference "
                + "to table '" + (t != null ? t.getName() : "n/a") + "'!");
        String ktn = this.getJavaType(kc, false, true);
        String rtn = this.getReferenceClassName(rt);
        String aktn = this.getAttributeName(ktn);
        String dktn = this.getDescription(ktn);
        String drtn = this.getDescription(rt);
        if (this.isEnum(kc)) {
            this.imports.add(this.getEnumClassName(kc.getDomain()) + ";");
        }
        String code = "    /**\n";
        code += "     * Returns the " + drtn + " for the passed " + dktn + ".\n";
        code += "     *\n";
        code += "     * " + this.createParameterTag(dktn, "The " + dktn + " which the " + drtn
                + " is to return for.") + "\n";
        code += "     * @return The " + drtn + " for the " + dktn + " or <CODE>null</CODE> if "
                + "none is defined.\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    public " + rtn + " " + methodName + "(" + ktn + " " + aktn + ") {\n";
        code += "        for (" + rtn + " o : this." + this.getAttributeName(rt) + "s) {\n";
        code += "            if (o." + this.createGetterName(kc) + "().equals(" + aktn
                + ")) {\n";
        code += "                return o;\n";
        code += "            }\n";
        code += "        }\n";
        code += "        return null;\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private ColumnModel getPKRefTable(TableModel rt, TableModel t) {
        for (ColumnModel c : rt.getPrimaryKeyColumns()) {
            if ((c.getReferencedTable() != t)
                    || ((c.getReferencedTable() == t) && c.isOptionSet(
                    ColParamIds.ASSIGNED_ELEMENT))) {
                return c;
            }
        }
        return null;
    }

    private void createGetListElementsMethod(TableModel t, CodeUtil codeUtil) {
        String methodName = "get" + t.getName() + "s";
        String ln = "this." + this.getListAttributeName(t);
        String tn = this.getReferenceClassName(t);
        String code = "    /**\n";
        code += "     * Returns a list of " + this.getAttributeName(t) + " objects.\n";
        code += "     *\n";
        code += "     * @return The list of " + this.getAttributeName(t) + " objects.\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    public " + tn + "[] " + methodName + "() {\n";
        code += "        List<" + tn + "> l = new " + this.getListType(t) + "<" + tn + ">();\n";
        code += "        for (" + tn + " o : " + ln + ") {\n";
        code += "            l.add(new " + tn + "(o));\n";
        code += "        }\n";
        code += "        return l.toArray(new " + tn + "[0]);\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createRemoveListElementMethod(TableModel t, CodeUtil codeUtil,
            SetterConfiguration sc) {
        String methodName = "remove" + t.getName();
        String an = this.getAttributeName(t);
        String ln = "this." + this.getListAttributeName(t);
        String tn = this.getReferenceClassName(t);
        String code = "    /**\n";
        code += "     * Removes the passed object from the list of " + this.getAttributeName(t)
                + " objects as far as it is a member.\n";
        code += "     *\n";
        code += "     * " + this.createParameterTag(an, "The object to remove.") + "\n";
        code += "     *\n";
        code += "     * " + this.createMethodChangedTag() + "\n";
        code += "     */\n";
        code += "    public void " + methodName + "(" + tn + " " + an + ") {\n";
        code += "        if (" + ln + ".contains(" + an + ")) {\n";
        code += "            " + ln + ".remove(" + an + ");\n";
        if (sc.isChangeListenerLogicRequired()) {
            String attrIdClassName = GeneratedPOJOAttributeIdCodeGenerator.className(
                    sc.getTable());
            String eventClassName = GeneratedPOJOChangeListenerEventCodeGenerator.className(
                    sc.getTable());
            String mn = "fire" + eventClassName;
            code += "            this." + mn + "(new " + eventClassName + "("
                    + attrIdClassName + "."+ this.createAttrIdName(this.getListAttributeName(t))
                    + ", " + an + ", " + eventClassName + ".Type.REMOVE));\n";
        }
        code += "        }\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

}