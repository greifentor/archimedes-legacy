/*
 * ApplicationDataManagerCodeGenerator.java
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
import archimedes.model.*;
import baccara.acf.*;
import baccara.events.*;
import gengen.util.*;

/**
 * A code generator for application data manager for in memory data applications.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 01.09.2016 - Added.
 */

public class ApplicationDataManagerCodeGenerator extends BaccaraBaseCodeGenerator {

    private static ApplicationDataManagerCodeGenerator instance =
            new ApplicationDataManagerCodeGenerator();

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
    @Override public String getClassName(TableModel tm) {
        return "ApplicationDataManager";
    }

    /**
     * @changed OLI 01.09.2016 - Added.
     */
    @Override public String getSubPackage() {
        return "business";
    }

    /**
     * @changed OLI 01.09.2016 - Added.
     */
    @Override public CodeGeneratorType getType() {
        return CodeGeneratorType.MODEL;
    }

    /**
     * @changed OLI 01.09.2016 - Added.
     */
    @Override public String generateClassComment(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className) {
        return "A manager for the application data of in memory applications.";
    }

    /**
     * @changed OLI 01.09.2016 - Added.
     */
    @Override public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm,
            CodeUtil codeUtil, String className,
            List<PostGeneratingCommand> postGeneratorCommands) {
        if (dm.isOptionSet(ModelParamIds.PERSISTENCE) && dm.getOptionByName(
                ModelParamIds.PERSISTENCE).getParameter().equalsIgnoreCase("JS")) {
            String code = "public class " + className + " implements ";
            String a = "";
            String lists = "";
            this.imports.add("java.util");
            for (TableModel t : dm.getTables()) {
                if (!t.isOptionSet(TableParamIds.DEPENDENT) && !t.isOptionSet(
                        TableParamIds.INHERITS)) {
                    if (a.length() > 0) {
                        a += ", ";
                    }
                    this.imports.add(ListManagerInterfaceCodeGenerator.packageName(t));
                    a += ListManagerInterfaceCodeGenerator.className(t);
                    String pcn = POJOCodeGenerator.className(t);
                    boolean comparable = t.isOptionSet(TableParamIds.COMPARABLE);
                    if (comparable) {
                        this.imports.add("corentx.util");
                    }
                    lists += "    private List<" + pcn + "> " + this.getAttributeName(pcn)
                            + "s = new " + (comparable ? "Sorted" : "") + "Vector<" + pcn
                            + ">();\n";
                }
            }
            if (dm.isOptionSet(ModelParamIds.SERIALIZABLE)) {
                if (a.length() > 0) {
                    a += ", ";
                }
                this.imports.add("java.io");
                a += "Serializable";
            }
            code += a + " {\n";
            code += "\n";
            code += lists;
            code += "\n";
            this.imports.add("baccara.events");
            code += "    transient private List<ApplicationDataManagerListener> listeners = "
                    + "new LinkedList<ApplicationDataManagerListener>();\n";
            code += "\n";
            code += this.getCodeForGetById();
            code += this.getCodeForGetItems();
            code += this.getCodeForGetMaxId();
            code += this.getCodeForEnsureState();
            for (TableModel t : dm.getTables()) {
                if (!t.isOptionSet(TableParamIds.DEPENDENT) && !t.isOptionSet(
                        TableParamIds.INHERITS)) {
                    boolean separatedDataStock = t.isOptionSet(
                            TableParamIds.SEPARATED_DATA_STOCK);
                    this.imports.add(POJOCodeGenerator.packageName(t));
                    String pojoClassName = POJOCodeGenerator.className(t);
                    this.createAddMethod(t, pojoClassName, separatedDataStock);
                    this.createClearMethod(t, pojoClassName);
                    this.createGetByIdMethod(t, pojoClassName);
                    this.createGetMethod(t, pojoClassName, separatedDataStock);
                    this.createGetNextIdMethod(t, pojoClassName, separatedDataStock);
                    this.createRemoveMethod(t, pojoClassName);
                }
            }
            this.createAddApplicationDataManagerListener();
            this.createFireApplicationDataManagerEvent();
            this.createRemoveApplicationDataManagerListener();
            return new GeneratorResult(code, GeneratorResultState.SUCCESS);
        }
        return new GeneratorResult("", GeneratorResultState.NOT_NECESSARY);
    }

    private String getCodeForGetById() {
        this.imports.add("baccara.data");
        String code = "    static <T extends LongIdProvider> T getById(long id, List<T> l) {\n";
        code += "        for (T t : l) {\n";
        code += "            if (t.getId() == id) {\n";
        code += "                return (T) t;\n";
        code += "            }\n";
        code += "        }\n";
        code += "        return null;\n";
        code += "    }\n\n";
        return code;
    }

    private String getCodeForGetItems() {
        this.imports.add("baccara.data");
        String code = "    static <T> List<T> getItems(List<T> l, Boolean customerObjectsOnly) "
                + "{\n";
        code += "        List<T> l0 = new LinkedList<T>();\n";
        code += "        for (T t : l) {\n";
        code += "            if ((customerObjectsOnly != null) && (t instanceof "
                + "CustomerObjectLogic) && !new Boolean(((CustomerObjectLogic) t"
                + ").isCustomerObject()).equals(customerObjectsOnly)) {\n";
        code += "                continue;\n";
        code += "            }\n";
        code += "            l0.add(t);\n";
        code += "        }\n";
        code += "        return l0;\n";
        code += "    }\n\n";
        return code;
    }

    /*
    private String getCodeForGetItems(boolean separatedDataStock) {
        this.imports.add("baccara.data");
        String code = "    static <T" + (separatedDataStock ? " extends CustomerObjectLogic"
                : "") + "> List<T> getItems(List<T> l" + (separatedDataStock
                ? ",Boolean customerObjectsOnly" : "") + ") {\n";
        code += "        List<T> l0 = new LinkedList<T>();\n";
        code += "        for (T t : l) {\n";
        if (separatedDataStock) {
            code += "            if ((customerObjectsOnly != null) && !new Boolean("
                    + "t.isCustomerObject()).equals(customerObjectsOnly)) {\n";
            code += "                continue;\n";
            code += "            }\n";
        }
        code += "            l0.add(t);\n";
        code += "        }\n";
        code += "        return l0;\n";
        code += "    }\n\n";
        return code;
    }
    */

    /* private String getCodeForGetMaxId() {
        this.imports.add("baccara.data");
        String code = "    static long getMaxId(List<? extends LongIdProvider> l, "
                + "boolean customerObject) {\n";
        code += "        long max = (customerObject ? 100000 : 0);\n";
        code += "        for (LongIdProvider lip : l) {\n";
        code += "            if ((lip instanceof CustomerObjectLogic) "
                + "&& !new Boolean(customerObject).equals(((CustomerObjectLogic) lip"
                + ").isCustomerObject())) {\n";
        code += "                continue;\n";
        code += "            }\n";
        code += "            max = Math.max(max, lip.getId());\n";
        code += "        }\n";
        code += "        return max+1;\n";
        code += "    }\n\n";
        return code;
    } */

    private String getCodeForGetMaxId() {
        this.imports.add("baccara.data");
        String code = "    private static Map<Class<?>, Long> idCache = new Hashtable<Class<?>,"
                + " Long>();\n";
        code += "    private static Map<Class<?>, Long> idCacheCustomer ="
                + " new Hashtable<Class<?>, Long>();\n";
        code += "    static long getMaxId(List<? extends LongIdProvider> l, "
                + "boolean customerObject, Class<?> cls) {\n";
        code += "        Long max = null;\n";
        code += "        Map<Class<?>, Long> cache = (customerObject ? idCacheCustomer : "
                + "idCache);\n";
        code += "        if (cache.containsKey(cls)) {\n";
        code += "            max = cache.get(cls);\n";
        code += "        } else {\n";
        code += "            max = getMax(l, customerObject);\n";
        code += "        }\n";
        code += "        max++;\n";
        code += "        cache.put(cls, max);\n";
        code += "        return max;\n";
        code += "    }\n";
        code += "\n";
        code += "    private static long getMax(List<? extends LongIdProvider> l, "
                + "boolean customerObject) {\n";
        code += "        long max = (customerObject ? 100000 : 0);\n";
        code += "        for (LongIdProvider lip : l) {\n";
            code += "            if ((lip instanceof CustomerObjectLogic) "
                    + "&& !new Boolean(customerObject).equals(((CustomerObjectLogic) lip"
                    + ").isCustomerObject())) {\n";
            code += "                continue;\n";
            code += "            }\n";
        code += "            max = Math.max(max, lip.getId());\n";
        code += "        }\n";
        code += "        return max;\n";
        code += "    }\n\n";
        return code;
    }

    private String getCodeForEnsureState() {
        this.imports.addStatic("corentx.util.Checks");
        String code = "    private static void ensureState(boolean condition, String message) "
                + "{\n";
        code += "        ensure(condition, new IllegalStateException(message));\n";
        code += "    }\n\n";
        return code;
    }

    private void createAddMethod(TableModel t, String pojoClassName, boolean separatedDataStock)
            {
        String d = this.getDescription(pojoClassName);
        String pn = this.getAttributeName(pojoClassName);
        String methodName = "add" + pojoClassName;
        String code = this.createOverrideComment();
        String nameAttr = this.getNameAttribute(t);
        this.imports.addStatic("corentx.util.Checks");
        code += "    @Override public void " + methodName + "(" + pojoClassName + " "
                + pn + ") throws IllegalArgumentException, IllegalStateException {\n";
        code += "        ensure(" + pn + " != null, \"" + d + " to add cannot be null.\");\n";
        code += "        for (" + pojoClassName + " o : this." + pn + "s) {\n";
        code += "            ensureState(o.getId() != " + pn + ".getId(), \"" + d + " item with"
                + " id '\" + " + pn + ".getId() + \"' is already existing.\");\n";
        if (nameAttr != null) {
            code += "            ensureState(!o.get" + nameAttr + "().equals(" + pn + ".get"
                    + nameAttr + "()), \"" + d + " item with " + this.getDescription(nameAttr)
                    + " '\" + " + pn + ".get" + nameAttr + "() + \"' is already existing.\""
                    + ");\n";
        }
        code += "        }\n";
        code += "        if (" + pn + ".getId() < 1) {\n";
        code += "            " + pn + ".setId(this.getNext" + pojoClassName + "Id("
                + (separatedDataStock ? pn + ".isCustomerObject()" : "") + "));\n";
        code += "        }\n";
        code += "        this." + pn + "s.add(" + pn + ");\n";
        code += "        this.fireApplicationDataManagerAddEvent(" + pn + ");\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private String getNameAttribute(TableModel t) {
        for (ColumnModel c : t.getColumns()) {
            if (c.isOptionSet(ColParamIds.NAME_ATTRIBUTE)) {
                return c.getName();
            }
        }
        return null;
    }

    private void createClearMethod(TableModel t, String pojoClassName) {
        String pn = this.getAttributeName(pojoClassName);
        String methodName = "clear" + pojoClassName + "s";
        String code = this.createOverrideComment();
        code += "    @Override public void " + methodName + "() {\n";
        code += "        this." + pn + "s.clear();\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createGetByIdMethod(TableModel t, String pojoClassName) {
        String pn = this.getAttributeName(pojoClassName);
        String methodName = "get" + pojoClassName + "ById";
        String code = this.createOverrideComment();
        code += "    @Override public " + pojoClassName + " " + methodName + "(long id) {\n";
        code += "        return getById(id, this." + pn + "s);\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createGetMethod(TableModel t, String pojoClassName, boolean separatedDataStock)
            {
        String pn = this.getAttributeName(pojoClassName);
        String methodName = "get" + pojoClassName + "s";
        String code = this.createOverrideComment();
        code += "    @Override public " + pojoClassName + "[] " + methodName
                + "(" + (separatedDataStock ? "Boolean customerObjectsOnly" : "") + ") {\n";
        code += "        return getItems(this." + pn + "s, " + (separatedDataStock
                ? "customerObjectsOnly" : "null") + ").toArray(new " + pojoClassName
                + "[0]);\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createGetNextIdMethod(TableModel t, String pojoClassName,
            boolean separatedDataStock) {
        String pn = this.getAttributeName(pojoClassName);
        String methodName = "getNext" + pojoClassName + "Id";
        String code = this.createOverrideComment();
        code += "    @Override public long " + methodName + "(" + (separatedDataStock
                ? "boolean customerObjectsOnly" : "") + ") {\n";
        code += "        return getMaxId(this." + pn + "s, " + (separatedDataStock
                ? "customerObjectsOnly" : "false") + ", " + pojoClassName + ".class);\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createRemoveMethod(TableModel t, String pojoClassName) {
        String pn = this.getAttributeName(pojoClassName);
        String methodName = "remove" + pojoClassName;
        String code = this.createOverrideComment();
        this.imports.addStatic("corentx.util.Checks");
        code += "    @Override public void " + methodName + "(" + pojoClassName + " " + pn
                + ") {\n";
        code += "        ensure(" + pn + " != null, \"" + pn + " to remove cannot be null.\");"
                + "\n";
        code += "        this." + pn + "s.remove(" + pn + ");\n";
        code += "        this.fireApplicationDataManagerRemoveEvent(" + pn + ");\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createAddApplicationDataManagerListener() {
        String methodName = "addApplicationDataManagerListener";
        String code = this.createOverrideComment();
        code += "    public void " + methodName + "(ApplicationDataManagerListener l) {\n";
        code += "        if (this.listeners == null) {\n";
        code += "            this.listeners = new LinkedList<ApplicationDataManagerListener>();"
                + "\n";
        code += "        }\n";
        code += "        if (l != null) {\n";
        code += "            this.listeners.add(l);\n";
        code += "        }\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createFireApplicationDataManagerEvent() {
        String methodName = "fireApplicationDataManagerAddEvent";
        String code = this.createOverrideComment();
        code += "    public void " + methodName + "(Object o) {\n";
        code += "        try {\n";
        code += "            for (ApplicationDataManagerListener l : this.listeners) {\n";
        code += "                try {\n";
        code += "                    l.objectAdded(o);\n";
        code += "                } catch (Exception e) {\n";
        code += "                    System.out.println(\"error while firing add event for: \" "
                + "+ o);\n";
        code += "                }\n";
        code += "            }\n";
        code += "        } catch (Exception e) {\n";
        code += "            System.out.println(\"error while firing add events for: \" + o);"
                + "\n";
        code += "        }\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
        methodName = "fireApplicationDataManagerRemoveEvent";
        code = this.createOverrideComment();
        code += "    public void " + methodName + "(Object o) {\n";
        code += "        try {\n";
        code += "            for (ApplicationDataManagerListener l : this.listeners) {\n";
        code += "                try {\n";
        code += "                    l.objectRemoved(o);\n";
        code += "                } catch (Exception e) {\n";
        code += "                    System.out.println(\"error while firing remove event for: "
                + "\" + o);\n";
        code += "                }\n";
        code += "            }\n";
        code += "        } catch (Exception e) {\n";
        code += "            System.out.println(\"error while firing remove events for: \" + o"
                + ");\n";
        code += "        }\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    private void createRemoveApplicationDataManagerListener() {
        String methodName = "removeApplicationDataManagerListener";
        String code = this.createOverrideComment();
        code += "    public void " + methodName + "(ApplicationDataManagerListener l) {\n";
        code += "        if (this.listeners == null) {\n";
        code += "            this.listeners = new LinkedList<ApplicationDataManagerListener>();"
                + "\n";
        code += "        }\n";
        code += "        if (l != null) {\n";
        code += "            this.listeners.remove(l);\n";
        code += "        }\n";
        code += "    }\n";
        this.storeMethod(methodName, code);
    }

    /**
     * @changed OLI 01.09.2016 - Added.
     */
    @Override public boolean isOneTimeFactory() {
        return true;
    }

}