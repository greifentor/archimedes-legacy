/*
 * DataWriterCodeGenerator.java
 *
 * 05.09.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.persistence;

import java.util.List;

import archimedes.acf.CodeGeneratorType;
import archimedes.acf.PostGeneratingCommand;
import archimedes.acf.report.GeneratorResult;
import archimedes.acf.report.GeneratorResultState;
import archimedes.legacy.acf.DomainParamId;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;
import baccara.acf.BaccaraBaseCodeGenerator;
import baccara.acf.ColParamIds;
import baccara.acf.TableParamIds;
import baccara.acf.entities.ListManagerInterfaceCodeGenerator;
import baccara.acf.entities.POJOCodeGenerator;
import corentx.util.SortedVector;
import gengen.util.CodeUtil;

/**
 * A generator for the data writer of the data classes.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 05.09.2016 - Added.
 */

public class DataWriterCodeGenerator extends BaccaraBaseCodeGenerator {

	private static DataWriterCodeGenerator instance = new DataWriterCodeGenerator();

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
	@Override
	public String getAlternateReferenceTableClassName(TableModel tm) {
		this.imports.add(POJOCodeGenerator.packageName(tm));
		return POJOCodeGenerator.className(tm);
	}

	/**
	 * @changed OLI 05.09.2016 - Added.
	 */
	@Override
	public String getClassName(TableModel tm) {
		return tm.getName() + "DataWriter";
	}

	/**
	 * @changed OLI 05.09.2016 - Added.
	 */
	@Override
	public String getSubPackage() {
		return "persistence";
	}

	/**
	 * @changed OLI 05.09.2016 - Added.
	 */
	@Override
	public CodeGeneratorType getType() {
		return CodeGeneratorType.TABLE;
	}

	/**
	 * @changed OLI 05.09.2016 - Added.
	 */
	@Override
	public String generateClassComment(DataModel dm, TableModel tm, CodeUtil codeUtil, String className) {
		return "A data writer for a(n) " + this.getDescription(tm) + " JavaScript persistence.";
	}

	/**
	 * @changed OLI 05.09.2016 - Added.
	 */
	@Override
	public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm, CodeUtil codeUtil, String className,
			List<PostGeneratingCommand> postGeneratorCommands) {
		GeneratorResultState resultState = GeneratorResultState.SUCCESS;
		boolean inherited = tm.isOptionSet(TableParamIds.INHERITS);
		String code = "";
		if (inherited || tm.isOptionSet(TableParamIds.DEPENDENT)) {
			resultState = GeneratorResultState.NOT_NECESSARY;
		} else {
			this.imports.add(ListManagerInterfaceCodeGenerator.packageName(tm));
			this.imports.add(POJOCodeGenerator.packageName(tm));
			String listManagerClassName = ListManagerInterfaceCodeGenerator.className(tm);
			String listManagerAttributeName = this.getListManagerAttributeName(listManagerClassName);
			String pojoClassName = POJOCodeGenerator.className(tm);
			this.imports.add(AbstractApplicationDataWriterCodeGenerator.packageName(tm));
			code = "public class " + className + " extends " + AbstractApplicationDataWriterCodeGenerator.className(tm)
					+ "<" + ListManagerInterfaceCodeGenerator.className(tm) + ", " + pojoClassName + "> {\n\n";
			this.createCreateRestoreJavaScriptCode(tm, pojoClassName);
			boolean separatedDataStock = tm.isOptionSet(TableParamIds.SEPARATED_DATA_STOCK);
			this.createGetData(pojoClassName, listManagerClassName, separatedDataStock);
			this.createWrite(pojoClassName, listManagerClassName, listManagerAttributeName);
		}
		return new GeneratorResult(code, resultState);
	}

	private String getListManagerAttributeName(String listManagerClassName) {
		return this.getAttributeName(listManagerClassName);
	}

	private void createCreateRestoreJavaScriptCode(TableModel tm, String pojoClassName) {
		String methodName = "createRestoreJavaScriptCode";
		String code = this.createOverrideComment();
		String an = this.getAttributeName(pojoClassName) + "s";
		this.imports.add("baccara.gui.progress");
		code += "    @Override public String createRestoreJavaScriptCode(" + pojoClassName + "[] " + an
				+ " , String loadedObjectName, "
				+ "boolean customerObjectsOnly, ProgressObserver progressObserver) {\n";
		code += "        String s = \"\";\n";
		code += "        for (" + pojoClassName + " o : " + an + ") {\n";
		code += "            if (s.length() == 0) {\n";
		code += "                s = \"var \";\n";
		code += "            }\n";
		code += "            s += \"o = new \" + o.getClass().getSimpleName() + \"();\\n\";\n";
		code += this.getAttributeAssignments(tm, "o", "            ", "o", tm);
		code += "            s += loadedObjectName + \".add" + tm.getName() + "(o);\\n\";\n";
		code += "            s += \"po.inc();\\n\\n\";\n";
		code += "            progressObserver.inc();\n";
		code += "        }\n";
		code += "        return (s.length() > 0 && !customerObjectsOnly ? loadedObjectName + " + "\".clear"
				+ tm.getName() + "s();\\n\\n\" : \"\") + \"// HEADER-END\\n\\n\" + s;\n";
		code += "    }\n";
		this.storeMethod(methodName, code);
	}

	private String getAttributeAssignments(TableModel tm, String objectName, String indent, String parentObjectName,
			TableModel parentTable) {
		String code = "";
		for (ColumnModel c : tm.getColumns()) {
			if (!c.isDeprecated() && !c.isTransient() && !c.isOptionSet(ColParamIds.NO_SETTER)) {
				if (c.getReferencedColumn() != null) {
					TableModel rt = c.getReferencedTable();
					if (rt == parentTable) {
						continue;
					}
					String lcn = ListManagerInterfaceCodeGenerator.className(rt);
					String gn = this.createGetterName(c.getReferencedColumn());
					code += indent + "s += \"" + parentObjectName + ".set" + c.getName() + "(";
					if (!c.isNotNull()) {
						code += "\" + (" + objectName + "." + this.createGetterName(c) + "() != null ? \""
								+ this.getListManagerAttributeName(lcn) + ".get" + rt.getName() + "ById(\" + "
								+ objectName + "." + this.createGetterName(c) + "()." + gn + "() + \")\" "
								+ ": \"null\") + \"";
					} else {
						code += this.getListManagerAttributeName(lcn) + ".get" + rt.getName() + "ById(\" + o."
								+ this.createGetterName(c) + "()." + gn + "() + \")";
					}
					code += ");\\n\";\n";
				} else if (c.getDomain().getName().equals("PTimestamp")) {
					code += indent + "s += \"" + parentObjectName + ".set" + c.getName() + "(new PTimestamp(\" + "
							+ parentObjectName + "." + this.createGetterName(c) + "().toLong() + \"));\\n\";\n";
				} else {
					String enumPrefix = this.getEnumTypeName(c);
					boolean stringParameter = this.getJavaType(c).equalsIgnoreCase("String");
					if (stringParameter) {
						this.imports.add("baccara.util");
					}
					String getterCall = objectName + "." + this.createGetterName(c) + "()";
					code += indent + "s += \"" + parentObjectName + ".set" + c.getName() + "(";
					if (enumPrefix.length() > 0) {
						code += "\" + (" + getterCall + " != null ? \"" + enumPrefix + ".\" + " + getterCall
								+ " : \"null\") + \"";
					} else {
						if (stringParameter) {
							code += this.getStringAttributeCheckedNotNull(c, objectName);
						} else {
							code += "\" + " + getterCall + " + \"";
						}
					}
					code += ");\\n\";\n";
				}
			}
		}
		for (TableModel it : this.getListInclusions(tm)) {
			this.imports.add(POJOCodeGenerator.packageName(it));
			String lpojocn = POJOCodeGenerator.className(it);
			code += indent + "for (" + lpojocn + " lo : " + objectName + ".get" + lpojocn + "s()) {\n";
			code += indent + "    s += \"" + parentObjectName + ".add" + lpojocn + "(new " + lpojocn + "(";
			String a = "";
			for (ColumnModel c : it.getColumns()) {
				if (c.isDeprecated() || c.isTransient() || c.isOptionSet(ColParamIds.NO_SETTER)) {
					continue;
				}
				if (a.length() > 0) {
					a += ", ";
				}
				String type = this.getJavaType(c);
				boolean stringType = type.equalsIgnoreCase("String");
				if (c.getReferencedColumn() != null) {
					TableModel rt = c.getReferencedTable();
					if (rt == tm) {
						a += "o";
					} else {
						this.imports.add(ListManagerInterfaceCodeGenerator.packageName(rt));
						String lcn = ListManagerInterfaceCodeGenerator.className(rt);
						String lman = this.getListManagerAttributeName(lcn);
						if (c.isNotNull()) {
							a += lman + ".get" + rt.getName() + "ById(\" + lo.get" + c.getName() + "()."
									+ this.createGetterName(c.getReferencedColumn()) + "() + \")";
						} else {
							a += "\";\n";
							a += indent + "    if (lo.get" + c.getName() + "() == null) {\n";
							a += indent + "        s += \"null\";\n";
							a += indent + "    } else {\n";
							a += indent + "        s += \"" + lman + ".get" + rt.getName() + "ById(\" + lo.get"
									+ c.getName() + "()." + this.createGetterName(c.getReferencedColumn())
									+ "() + \")\";\n";
							a += indent + "    }\n";
							a += indent + "    s += \"";
						}
					}
				} else if (type.equals("PTimestamp")) {
					a += "new PTimestamp(\" + lo." + this.createGetterName(c) + "().toLong()" + " + \")";
				} else if (this.isSimpleType(type.toLowerCase())) {
					a += "\" + lo." + this.createGetterName(c) + "() + \"";
				} else if (type.equals("Integer")) {
					a += "\" + lo." + this.createGetterName(c) + "() + \"";
				} else if (stringType) {
					this.imports.add("baccara.util");
					a += this.getStringAttributeCheckedNotNull(c, "lo");
				} else if (this.isEnum(c)) {
					a += this.getEnumTypeName(c) + ".\" + lo." + this.createGetterName(c) + "() + \"";
				}
			}
			code += a + "));\\n\";\n";
			code += indent + "}\n";
		}
		for (TableModel it : this.getSubClassTables(tm)) {
			this.imports.add(POJOCodeGenerator.packageName(it));
			String pojocn = POJOCodeGenerator.className(it);
			code += indent + "if (o instanceof " + pojocn + ") {\n";
			String icn = this.getListManagerAttributeName(it.getName());
			code += indent + "    " + pojocn + " " + icn + " = (" + pojocn + ") o;\n";
			code += this.getAttributeAssignments(it, icn, indent + "    ", "o", tm);
			code += indent + "}\n";
		}
		return code;
	}

	private String getEnumTypeName(ColumnModel c) {
		String enumTypeName = "";
		if (this.isEnum(c)) {
			enumTypeName = c.getDomain().getOptionByName(DomainParamId.ENUM).getParameter();
			while (enumTypeName.contains(".")) {
				enumTypeName = enumTypeName.substring(enumTypeName.indexOf(".") + 1);
			}
		}
		return enumTypeName;
	}

	private String getStringAttributeCheckedNotNull(ColumnModel cm, String objectName) {
		String gn = objectName + "." + this.createGetterName(cm) + "()";
		String s = "JS.from(\\\"\" + JS.to(" + gn + ") + \"\\\")";
		if (!cm.isNotNull()) {
			s = "\" + (" + gn + " != null ? \"" + s + "\" : null) + \"";
		}
		return s;
	}

	private TableModel[] getSubClassTables(TableModel tm) {
		List<TableModel> l = new SortedVector<TableModel>();
		for (TableModel t : tm.getDataModel().getTables()) {
			if (t.isOptionSet(TableParamIds.INHERITS)) {
				OptionModel o = t.getOptionByName(TableParamIds.INHERITS);
				if (tm.getName().equals(o.getParameter())) {
					l.add(t);
				}
			}
		}
		return l.toArray(new TableModel[0]);
	}

	private void createGetData(String pojoClassName, String listManagerClassName, boolean separatedDataStock) {
		String methodName = "getData";
		String code = this.createOverrideComment();
		code += "    @Override public " + pojoClassName + "[] getData(" + listManagerClassName
				+ " lm, boolean customerObjectsOnly) {\n";
		if (!separatedDataStock) {
			code += "        if (customerObjectsOnly) {\n";
			code += "            return new " + pojoClassName + "[0];\n";
			code += "        }\n";
		}
		code += "        return lm.get" + pojoClassName + "s(" + (separatedDataStock ? "customerObjectsOnly" : "")
				+ ");\n";
		code += "    }\n";
		this.storeMethod(methodName, code);
	}

	private void createWrite(String pojoClassName, String listManagerClassName, String listManagerAttributeName) {
		String methodName = "write";
		String code = this.createOverrideComment();
		this.imports.add("baccara.files");
		code += "    public void write(" + listManagerClassName + " lm, "
				+ "String vendorDataFileName, String customerDataFileName, TextFileWriter tfw, "
				+ "ProgressObserver progressObserver) {\n";
		code += "        this.write(lm, vendorDataFileName, customerDataFileName, tfw, " + "\""
				+ this.getAttributeName(pojoClassName) + "s\", \"" + listManagerAttributeName
				+ "\", progressObserver);\n";
		code += "    }\n";
		this.storeMethod(methodName, code);
	}

	/**
	 * @changed OLI 22.08.2017 - Added.
	 */
	@Override
	public String[] getExcludeMarks() {
		return new String[] { TableParamIds.NO_DATA_WRITER };
	}

}