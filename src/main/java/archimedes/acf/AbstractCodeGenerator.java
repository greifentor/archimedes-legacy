/*
 * AbstractCodeGenerator.java
 *
 * 01.04.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf;

import java.io.File;
import java.io.IOException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import archimedes.acf.coders.annotations.MinMaxCoder;
import archimedes.acf.io.SourceFileReader;
import archimedes.acf.io.SourceFileWriter;
import archimedes.acf.param.ColParamIds;
import archimedes.acf.param.DomainParamIds;
import archimedes.acf.param.ModelParamIds;
import archimedes.acf.param.TableParamIds;
import archimedes.acf.report.CodeGeneratorReportTableEntry;
import archimedes.acf.report.GenerationProcessReport;
import archimedes.acf.report.GeneratorResult;
import archimedes.acf.report.GeneratorResultState;
import archimedes.acf.report.ResourceData;
import archimedes.acf.util.CodeGeneratorUtil;
import archimedes.acf.util.ColumnUtil;
import archimedes.acf.util.DomainUtil;
import archimedes.acf.util.ImportCreator;
import archimedes.acf.util.ImportList;
import archimedes.acf.util.ParameterUtil;
import archimedes.acf.util.SimpleTypeChecker;
import archimedes.acf.util.TypeUtil;
import archimedes.acf.util.checker.AdditionalCodeChecker;
import archimedes.acf.util.checker.ChangedChecker;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.OptionModel;
import archimedes.legacy.model.PanelModel;
import archimedes.legacy.model.SelectionAttribute;
import archimedes.legacy.model.SelectionMemberModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.ToStringContainerModel;
import archimedes.legacy.scheme.SelectionMember;
import archimedes.legacy.util.DescriptionGenerator;
import corentx.dates.PDate;
import corentx.io.FileUtil;
import corentx.util.Checks;
import corentx.util.SortedVector;
import corentx.util.Str;
import gengen.IndividualPreferences;
import gengen.util.CodeUtil;

/**
 * A basic class for code generators.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 01.04.2016 - Added.
 */

abstract public class AbstractCodeGenerator implements CodeGenerator {

	protected ChangedChecker changedChecker = new ChangedChecker();
	protected ColumnUtil columnUtil = null;
	protected DescriptionGenerator descriptionGenerator = new DescriptionGenerator();
	protected DomainUtil domainUtil = null;
	protected CodeGeneratorUtil generatorUtil = new CodeGeneratorUtil();
	protected List<String> excludeMarks = Arrays.asList(this.getExcludeMarks());
	protected TableModel exclusiveTableForCodeGeneration = null;
	protected ImportCreator importCreator = new ImportCreator();
	protected ImportList imports = null;
	protected IndividualPreferences individualPreferences = null;
	protected Map<String, MethodCode> methodCodeStorage = new Hashtable<String, MethodCode>();
	protected List<String> necessaryIncludeMarks = Arrays.asList(this.getNecessaryIncludeMarks());
	protected PanelModel currentPanelModel = null;
	protected ParameterUtil parameterUtil = new ParameterUtil();
	protected SimpleTypeChecker simpleTypeChecker = new SimpleTypeChecker();
	protected boolean temporarilySuspended = false;
	protected TypeUtil typeUtil = new TypeUtil();
	protected UnchangedByTagChecker unchangedByTagChecker = null;

	public AbstractCodeGenerator() {
		super();
		this.domainUtil = new DomainUtil(this.parameterUtil);
		this.columnUtil = new ColumnUtil(this.parameterUtil, this.domainUtil);
	}

	/**
	 * Add an import for the domain type in case it is an enum.
	 *
	 * @param d The domain whose type is to import.
	 * @throws IllegalArgumentException In case of passing a null pointer.
	 *
	 * @changed OLI 17.05.2016 - Added.
	 */
	protected void addEnumPackageIfNecessary(DomainModel d) {
		Checks.ensure(d != null, "domain cannot be null.");
		if (this.isEnum(d)) {
			String en = this.parameterUtil.getParameterStartsWith(DomainParamIds.ENUM, d);
			en = en.replace(DomainParamIds.ENUM, "").replace(":", "").trim();
			if (en.contains(".")) {
				en = en.substring(0, en.lastIndexOf("."));
				this.imports.add(en);
			}
		}
	}

	/**
	 * Adds import clauses for all foreign package enums.
	 *
	 * @param imports The imports list to extend by the foreign package enums.
	 * @param columns The columns which the imports are made for.
	 *
	 * @changed OLI 10.10.2013 - Added.
	 */
	public void addForeignEnumImports(ImportList imports, ColumnModel[] columns) {
		for (ColumnModel c : columns) {
			this.addForeignEnumImport(imports, c);
		}
	}

	/**
	 * Adds import clauses for all foreign package enums.
	 *
	 * @param imports The imports list to extend by the foreign package enums.
	 * @param column  The column which the import is made for.
	 *
	 * @changed OLI 04.11.2015 - Added.
	 */
	public void addForeignEnumImport(ImportList imports, ColumnModel column) {
		String enumClassName = this.getEnumClassName(column.getDomain());
		if (enumClassName == null) {
			return;
		}
		enumClassName = enumClassName.substring(enumClassName.indexOf(":") + 1);
		if (enumClassName.contains(".")) {
			String s = "";
			List<String> l = Str.splitToList(enumClassName, ".");
			for (int i = 0, leni = l.size(); i < leni - 1; i++) {
				if (i > 0) {
					s += ".";
				}
				s += l.get(i);
			}
			imports.add(s);
		}
	}

	/**
	 * Adds import clauses for all foreign package enums.
	 *
	 * @param imports The imports list to extend by the foreign package enums.
	 * @param dm      The domain model which the imports are made for.
	 *
	 * @changed OLI 10.10.2013 - Added.
	 */
	public void addForeignEnumImports(ImportList imports, DomainModel dm) {
		String enumClassName = this.getEnumClassName(dm);
		enumClassName = enumClassName.substring(enumClassName.indexOf(":") + 1);
		if (enumClassName.contains(".")) {
			String s = "";
			List<String> l = Str.splitToList(enumClassName, ".");
			for (int i = 0, leni = l.size(); i < leni - 1; i++) {
				if (i > 0) {
					s += ".";
				}
				s += l.get(i);
			}
			imports.add(s);
		}
	}

	/**
	 * Adds import clauses for all foreign package enums.
	 *
	 * @param imports The imports list to extend by the foreign package enums.
	 * @param tm      The table model which the imports are made for.
	 *
	 * @changed OLI 10.10.2013 - Added.
	 */
	public void addForeignEnumImports(ImportList imports, TableModel tm) {
		this.addForeignEnumImports(imports, this.getEnumColumns(tm));
	}

	/**
	 * Adds import clauses for timestamp classes if necessary.
	 *
	 * @param imports The imports list to extend by timestamp classes if necessary.
	 * @param columns The columns which the imports are made for.
	 *
	 * @changed OLI 21.10.2013 - Added.
	 */
	public void addTimestampClassImportsIfNecessary(ImportList imports, ColumnModel[] columns) {
		if (this.hasTimestampField(columns)) {
			imports.add("corentx.dates");
		}
	}

	/**
	 * Adds import clauses for timestamp classes if necessary.
	 *
	 * @param imports The imports list to extend by timestamp classes if necessary.
	 * @param tm      The table model which the imports are made for.
	 *
	 * @changed OLI 11.10.2013 - Added.
	 */
	public void addTimestampClassImportsIfNecessary(ImportList imports, TableModel tm) {
		if (this.hasTimestampField(tm)) {
			imports.add("corentx.dates");
		}
	}

	/**
	 * Allows an access and a change of the code after the code generator has code its work.
	 *
	 * @param code                  The generated code.
	 * @param dm                    The data model which the code is created for.
	 * @param tm                    The table model which the code is created for.
	 * @param codeUtil              Some utilities for the code generation.
	 * @param postGeneratorCommands The post generation commands.
	 * @return The code (may be modified by the method).
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	public String afterClassCodeGenerated(String code, DataModel dm, TableModel tm, CodeUtil codeUtil,
			List<PostGeneratingCommand> postGeneratorCommands) {
		return code;
	}

	/**
	 * Checks for the passed marks if one or more of them is contained in the exclusion marks of the code generator.
	 *
	 * @param marks The marks to check.
	 * @return <CODE>true</CODE> if at least one mark is contained by the exlusion marks.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	public boolean containsExcludeMark(String[] marks, TableModel tm) {
		return this.containsExcludeMark(Arrays.asList(marks), tm);
	}

	/**
	 * Checks for the passed marks if one or more of them is contained in the exclusion marks of the code generator.
	 *
	 * @param marks The marks to check.
	 * @return <CODE>true</CODE> if at least one mark is contained by the exlusion marks.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	public boolean containsExcludeMark(List<String> marks, TableModel tm) {
		for (String mark : marks) {
			if (this.excludeMarks.contains(mark)) {
				return true;
			}
		}
		for (OptionModel om : tm.getOptions()) {
			if (this.excludeMarks.contains(om.getName().toUpperCase().replace(" ", "_"))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks for the passed marks if one or more of them is contained in the necessarily inclusion marks of the code
	 * generator.
	 *
	 * @param marks The marks to check.
	 * @return <CODE>true</CODE> if at least one mark is contained by the necessarily inclusion marks.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	public boolean containsNecessaryIncludeMark(List<String> marks) {
		for (String mark : marks) {
			if (this.necessaryIncludeMarks.contains(mark)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates an absolute file name for the files generated by the generator.
	 *
	 * @param individualPreferences Individual preferences of the user.
	 * @param model                 The data model for which the classes are generated.
	 * @param subProjectName        A sub project name if available or an empty string if there is non.
	 * @param className             The name of the class which the generator is working for.
	 * @param packageName           The name of the package where the code is generated for.
	 * @param specialAPI            Set if to create for special API.
	 * @param tableModel            The table which the file name is created for.
	 * @return The absolute file name for the generated code.
	 *
	 * @changed OLI 29.09.2013 - Added.
	 */
	public String createAbsoluteFileName(IndividualPreferences individualPreferences, DataModel model,
			String subProjectName, String className, String packageName, boolean specialAPI, TableModel tableModel) {
		// ROOT / PROJECT / SUB-PROJECT / src/main/java / PACKAGE / className.java
		String projectName = this.getCodePath(model);
		String s = FileUtil.completePath(individualPreferences.getBaseCodePath(model.getName()));
		s += FileUtil.completePath(projectName);
		s += FileUtil.completePath((subProjectName.length() > 0 ? subProjectName + "/" + subProjectName : projectName)
				+ "-" + this.createSubProjectSuffix(specialAPI, model, tableModel));
		s += this.getSrcSubPath();
		s += FileUtil.completePath(packageName.replace(".", "/"));
		s += className + "." + this.getFileSuffix();
		return s.replace("\\", "/");
	}

	protected String createSubProjectSuffix(boolean specialAPI, DataModel model, TableModel tm) {
		String sps = this.getSubProjectSuffix(model, tm);
		if (sps.equals("api") && specialAPI && this.isAllowedForSpecialAPI()) {
			sps = "plugin-api";
		}
		return sps;
	}

	/**
	 * Returns an attribute id for the passed column.
	 *
	 * @param c The column which the attribute id is created for.
	 * @return An attribute id for the passed column.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public String createAttrIdName(ColumnModel c) {
		return this.createAttrIdName(c.getName());
	}

	/**
	 * Returns an attribute id for the passed string.
	 *
	 * @param s The string which the attribute id is created for.
	 * @return An attribute id for the passed column.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public String createAttrIdName(String s) {
		Checks.ensure(s != null, "string cannot be null.");
		return this.getDescription(s).replace(".", "").replace(" ", "_").toUpperCase();
	}

	/**
	 * Creates a class footer with a leading blank line.
	 *
	 * @return The code for the class footer.
	 *
	 * @changed OLI 29.09.2013 - Added (from "AbstractBasicCodeGenerator").
	 */
	public String createClassFooter() {
		return this.createClassFooter(true);
	}

	/**
	 * Creates a class footer.
	 *
	 * @param leadingBlankLine Set this flag if an additional blank line is to create before the footer lines.
	 * @return The code for the class footer.
	 *
	 * @changed OLI 29.09.2013 - Added (from "AbstractBasicCodeGenerator").
	 */
	public String createClassFooter(boolean leadingBlankLine) {
		String code = (leadingBlankLine ? "\n" : "");
		if (this.isOneTimeFactory()) {
			code += "    " + this.createTodoPlaceYourCode() + "\n";
			code += "\n";
		}
		code += "}";
		return code;

	}

	private String createTodoPlaceYourCode() {
		Checks.ensure(this.isOneTimeFactory(),
				new UnsupportedOperationException("Unchanged " + "tags can only be called for one time factories!"));
		return ChangedChecker.UNCHANGED_TEXT;
	}

	/**
	 * Returns a code fragment which contains an attribute definition block for the passed configuration.
	 * 
	 * @param abc The configuration for the attribute block generation.
	 * @return A code fragment which contains an attribute definition block for the passed configuration.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 28.04.2016 - Added.
	 */
	public String createAttributeDefinitionBlock(AttributeBlockConfiguration abc) {
		MinMaxCoder minMaxCoder = new MinMaxCoder();
		Checks.ensure(abc != null, "attribute block configuration cannot be null.");
		boolean lineCreated = false;
		String s = "";
		for (ColumnModel c : abc.getColumns()) {
			if (abc.isPrimaryKeysOnly() && !c.isPrimaryKey()) {
				continue;
			}
			if (this.checkForIgnoringColumnWhileAttibuteBlockGeneration(c)) {
				continue;
			}
			if (c.isOptionSet(ColParamIds.ASSEMBLY)) {
				continue;
			}
			s += "    ";
			String tn = this.getJavaType(c, ((c.getReferencedTable() != null) && (abc.isReferencesToKeyClass())),
					abc.isTimestampWrapped());
			if (abc.isForJPA()) {
				if (c.isTransient()) {
					s += "@Transient ";
				} else if (!this.isEnum(c)) {
					OptionModel generatedId = c.getOptionByName(ColParamIds.GENERATED_ID);
					if (c.isPrimaryKey()) {
						s += "@Id ";
					}
					if (generatedId != null) {
						String value = generatedId.getParameter().trim();
						String valueSeq = (c.getSequenceForKeyGeneration() != null
								? c.getSequenceForKeyGeneration().getName()
								: value.toLowerCase());
						s += "@SequenceGenerator(name=\"" + value + "\", sequenceName=\"" + valueSeq
								+ "\") @GeneratedValue(generator=\"" + value + "\", strategy=GenerationType.SEQUENCE) ";
					}
					if (c.isNotNull()) {
						// && !c.isPrimaryKey()) {
						if (!this.isSimpleType(tn)) {
							s += "@NotNull ";
						}
					}
					if (c.isOptionSet(ColParamIds.NOT_BLANK)) {
						/*
						 * this.imports.add("org.hibernate.validator.constraints"); s += "@NotBlank ";
						 */
						s += "@Size(min=1";
						if (c.getDomain().getLength() > 0) {
							s += ", max=" + c.getDomain().getLength();
						}
						s += ") ";
					}
					if ((c.getDomain().getLength() > 0) && !c.isOptionSet(ColParamIds.NOT_BLANK)) {
						s += "@Size(max=" + c.getDomain().getLength() + ") ";
					}
					s += minMaxCoder.getCode(c, this.isExternalRef(c));
					/*
					 * TODO: COMMENT COULD BE REMOVED. if (c.isOptionSet(ColParamIds.MIN_VALUE)) { String v =
					 * c.getOptionByName(ColParamIds.MIN_VALUE).getParameter(); // boolean noMessage =
					 * v.contains(",NO_MESSAGE"); v = v.replace(",NO_MESSAGE", ""); s += "@Min(value=" + v + ") "; // s
					 * += "@Min(value=" + v + (noMessage ? "" : ", message=\"{" // + this.getAttributeName(c.getTable())
					 * + "." // + this.getAttributeName(c) + ".is.to.less}\"") + ") "; } if
					 * (c.isOptionSet(ColParamIds.MAX_VALUE)) { String v =
					 * c.getOptionByName(ColParamIds.MAX_VALUE).getParameter(); // boolean noMessage =
					 * v.contains(",NO_MESSAGE"); v = v.replace(",NO_MESSAGE", ""); s += "@Max(value=" + v + ") "; // s
					 * += "@Max(value=" + v + (noMessage ? "" : ", message=\"{" // + this.getAttributeName(c.getTable())
					 * + "." // + this.getAttributeName(c) + ".is.to.great}\"") + ") "; }
					 */
					s += "@" + (this.isRefInModel(c) ? "ManyToOne @Join" : "") + "Column(name=" + "\"" + c.getName()
							+ "\") ";
					if (c.getDomain().getDataType() == Types.BLOB) {
						s += "@Lob @Basic(fetch=FetchType.LAZY) ";
					}
				} else {
					if (c.isNotNull()) {
						s += "@NotNull ";
					}
					s += "@Column(name=\"" + c.getName() + "\") ";
					s += "@Enumerated(EnumType.STRING) ";
				}
			}
			this.addEnumPackageIfNecessary(c.getDomain());
			s += "private " + this.getTypeAndName(c,
					((c.getReferencedTable() != null) && (abc.isReferencesToKeyClass())), abc.isTimestampWrapped());
			if (this.isRefInModel(c)) {
				this.importEntityInterfacePackageName(c.getReferencedTable());
			}
			if ((c.getDefaultValue() != null) && (c.getDefaultValue().length() > 0)) {
				s += " = " + this.getDefaultValue(c,
						((c.getReferencedTable() != null) && (abc.isReferencesToKeyClass())), abc.isTimestampWrapped());
			}
			s += ";\n";
			lineCreated = true;
		}
		return s + this.completeBlock(lineCreated);
	}

	public void importEntityInterfacePackageName(TableModel tm) {
	}

	private String completeBlock(boolean lineCreated) {
		return (lineCreated ? "\n" : "");
	}

	/**
	 * Override this method if there are some additional checks to ignore a column for attribute blocks.
	 *
	 * @param c The column to check.
	 * @return <CODE>true</CODE> if the column should be ignored for the attribute block.
	 *
	 * @changed OLI 28.04.2016 - Added.
	 */
	public boolean checkForIgnoringColumnWhileAttibuteBlockGeneration(ColumnModel c) {
		return false;
	}

	/**
	 * Returns the code for a constructor which is specified by the passed configurations.
	 *
	 * @param cc                        The configuration for the constructor generation.
	 * @param constructorImportExtender An constructor import extender if necessary.
	 * @param keyClassNameGenerator     A generator for the key class name.
	 * @return The code for a constructor which is specified by the passed configurations.
	 * @throws IllegalArgumentException Passing a null pointer as constructor configuration.
	 *
	 * @changed OLI 29.04.2016 - Added.
	 */
	public String createConstructor(ConstructorConfiguration cc, ConstructorImportExtender constructorImportExtender,
			KeyClassNameGenerator keyClassNameGenerator) {
		Checks.ensure(cc != null, "constructor configuration cannot be null.");
		Checks.ensure(keyClassNameGenerator != null, "key class name generator cannot be null.");
		String s = "    /**\n";
		s += "     * Creates a new object with the passed parameters.\n";
		s += "     *\n";
		for (ColumnModel c : cc.getColumns()) {
			if (this.isIgnoredForEmbedded(c)) {
				continue;
			}
			if (c.isOptionSet(ColParamIds.ASSEMBLY)) {
				continue;
			}
			String comment = null;
			String cn = this.getAttributeName(c);
			if (cc.isKeyClass()) {
				comment = "The value for key element " + this.getDescription(cn.replace(".", " ")) + ".";
			} else {
				comment = "The " + this.getDescription(cn) + " of the " + this.getDescription(cc.getClassName())
						+ " object.";
			}
			s += "     * " + this.createParameterTag(cn, comment) + "\n";
		}
		if (cc.getColumns().length > 0) {
			s += "     *\n";
		}
		s += "     * " + this.createMethodChangedTag() + "\n";
		s += "     */\n";
		s += "    public " + cc.getClassName() + "(";
		String a = "";
		for (ColumnModel c : cc.getColumns()) {
			if (this.isIgnoredForEmbedded(c)) {
				continue;
			}
			if (c.isOptionSet(ColParamIds.ASSEMBLY)) {
				continue;
			}
			if (a.length() > 0) {
				a += ", ";
			}
			if (cc.isEntityAnnotations()) {
				a += "@EntityAttribute(\"" + this.getAttributeNameComplex(c.getName()) + "\") ";
			}
			a += this.getTypeAndName(c, cc.isReferencesToKeyClass(), cc.isTimestampWrapped());
			if (this.isRefInModel(c)) {
				if (constructorImportExtender != null) {
					constructorImportExtender.addImports(this.imports, c);
				}
			}
			;
		}
		s += a + ") {\n";
		if (cc.isCallSuperClass()) {
			a = "";
			s += "        super(";
			for (ColumnModel c : cc.getColumns()) {
				if (this.isIgnoredForEmbedded(c)) {
					continue;
				}
				if (c.isOptionSet(ColParamIds.ASSEMBLY)) {
					continue;
				}
				if (a.length() > 0) {
					a += ", ";
				}
				a += this.generatorUtil.getAttributeName(c.getName());
			}
			s += a + ");\n";
		} else if (cc.isSimpleDataCall() && (cc.getColumns().length > 0)) {
			s += "        " + this.getKeyClassConstructorCall(cc.getColumns()[0].getTable(), keyClassNameGenerator);
			a = "";
			for (ToStringContainerModel tsc : cc.getColumns()[0].getTable().getComboStringMembers()) {
				if (a.length() > 0) {
					a += " + ";
				} else {
					a += ", ";
				}
				a += ((tsc.getPrefix() == null) || tsc.getPrefix().isEmpty() ? "" : "\"" + tsc.getPrefix() + "\" + ");
				boolean stringType = this.getJavaType(tsc.getColumn()).equals("String");
				a += (!stringType ? "String.valueOf(" : "") + this.getAttributeName(tsc.getColumn())
						+ (!stringType ? ")" : "");
				a += ((tsc.getSuffix() == null) || tsc.getSuffix().isEmpty() ? "" : " + \"" + tsc.getSuffix() + "\"");
			}
			s += a + ");\n";
		} else {
			s += "        super();\n";
			for (ColumnModel c : cc.getColumns()) {
				if (this.isIgnoredForEmbedded(c)) {
					continue;
				}
				if (c.isOptionSet(ColParamIds.ASSEMBLY)) {
					continue;
				}
				String an = this.generatorUtil.getAttributeName(c.getName());
				s += "        this." + an + " = ";
				if (cc.isTimestampToWrapperConversion() && this.isTimestamp(c.getDomain())) {
					String type = this.getJavaType(c);
					s += "(" + an + " != null ? new " + type + "(" + an + ") : null)";
				} else {
					s += an;
				}
				s += ";\n";
			}
		}
		s += "    }\n";
		s += "\n";
		return s;
	}

	/**
	 * Adds a simple equals method based on the Apache "EqualsBuilder" class to the method storage.
	 * 
	 * @changed OLI 02.05.2016 - Added.
	 */
	public void createEquals() {
		this.imports.add(this.getOrgApacheCommonsLangBuilderPackageName());
		String s = this.createOverrideComment("Generated.");
		s += "    @Override public boolean equals(Object o) {\n";
		s += "        return EqualsBuilder.reflectionEquals(this, o);\n";
		s += "    }\n";
		this.storeMethod("equals", s);
	}

	/**
	 * Creates the code for the passed getter configuration.
	 *
	 * @param gc The getter configuration which the getter code is to create for.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 28.04.2016 - Added.
	 */
	public void createGetters(GetterConfiguration gc) {
		Checks.ensure(gc != null, "getter configuration cannot be null.");
		boolean pkDone = (this.getPrimaryKeyColumn(gc.getColumns()) == null);
		for (ColumnModel c : gc.getColumns()) {
			if (this.isIgnoredForEmbedded(c)) {
				continue;
			}
			if (c.isOptionSet(ColParamIds.NO_GETTER)) {
				continue;
			}
			String getterName = this.createGetterName(c);
			this.storeMethod(getterName, this.createGetterCode(gc, getterName, c, c.getName()));
		}
		if (!gc.isNoPrimaryKey()) {
			if (!pkDone) {
				this.storeMethod("getPrimaryKey", this.getPrimaryKeyGetterCode(gc));
			} else if (this.columnUtil.getPrimaryKeyColumns(gc.getColumns()).length > 1) {
				this.storeMethod("getPrimaryKey", this.getPrimaryKeyGetterCode(gc));
			}
		}
	}

	/**
	 * Checks the passed condition and throws a <CODE>CodeGeneratorException</CODE> in case of the condition is false.
	 * 
	 * @param condition            The condition which is checked.
	 * @param codeFactoryName      The name of the code factory which called the code generator.
	 * @param message              A plain message.
	 * @param messageResourceId    A resource for localized error messages.
	 * @param additionalParameters For the localized message.
	 *
	 * @changed OLI 08.12.2016 - Added.
	 */
	public void ensure(boolean condition, String codeFactoryName, String message, String messageResourceId,
			Object... additionalParameters) {
		Checks.ensure(condition, new CodeGeneratorException(codeFactoryName, this, message,
				this.getClass().getSimpleName() + ".error." + messageResourceId + ".message", additionalParameters));
	}

	public String getPrimaryKeyGetterCode(GetterConfiguration gc) {
		String s = "";
		boolean multiKey = this.columnUtil.getPrimaryKeyColumns(gc.getColumns()).length > 1;
		String pkClassName = gc.getTable().getName() + (multiKey ? "Id" : "PrimaryKey");
		s += this.createGetterComment("primary key");
		s += "    " + (gc.isAbstractStubOnly() ? "abstract " : (gc.isOverriden() ? "@Override " : ""));
		s += "public " + pkClassName + " getPrimaryKey() ";
		if (gc.isAbstractStubOnly()) {
			s += ";\n";
		} else {
			s += "{\n";
			s += "        return new " + pkClassName + "(";
			if (multiKey) {
				String code = "";
				for (ColumnModel c : this.getColumnsAssortedByNames(gc.getColumns())) {
					if (c.isPrimaryKey()) {
						if (code.length() > 0) {
							code += ", ";
						}
						code += "this." + this.generatorUtil.getAttributeName(c);
						if (this.isRefInModel(c)) {
							code += "." + this.createGetterName(c.getReferencedColumn()) + "()";
						}
					}
				}
				s += code;
			} else if (this.getPrimaryKeyColumn(gc.getColumns()) != null) {
				s += "this." + this.generatorUtil.getAttributeName(this.getPrimaryKeyColumn(gc.getColumns()));
			}
			s += ");\n";
			s += "    }\n";
		}
		return s;
	}

	private String createGetterCode(GetterConfiguration gc, String getterName, ColumnModel c, String fieldName) {
		String description = this.getDescription(fieldName);
		String type = this.getJavaType(c, gc.isReferencesToKeyClass(), gc.isTimestampWrapped());
		String s = this.createGetterComment(description);
		if (this.isTimestamp(c) && gc.isTimestampWrapped()) {
			type = this.typeUtil.getWrapperClass(type);
		}
		s += "    " + (c.isDeprecated() ? "@Deprecated " : "") + (gc.isOverriden() ? "@Override " : "") + "public "
				+ type + " " + getterName + "() {\n";
		String attrName = this.generatorUtil.getAttributeName(fieldName);
		String thisAttrName = "this." + attrName;
		if (this.isTimestamp(c.getDomain())) {
			this.imports.add("corentx.dates");
			if (!gc.isTimestampToWrapperConversion()) {
				s += "        return " + thisAttrName + ";\n";
			} else {
				s += "        if (" + thisAttrName + " != null) {\n";
				s += "            return new " + c.getDomain().getName() + "(" + thisAttrName + ");\n";
				s += "        }\n";
				s += "        return null;\n";
			}
		} else {
			if (c.isOptionSet(ColParamIds.ASSEMBLY)) {
				StringTokenizer st = new StringTokenizer(c.getOptionByName(ColParamIds.ASSEMBLY).getParameter(), "^");
				String a = "\"\"";
				TableModel tm = c.getTable();
				while (st.hasMoreTokens()) {
					String t = st.nextToken();
					a += (t.startsWith("$$$") ? "" : " + ");
					if (tm.getColumnByName(t) != null) {
						a += "this." + this.createGetterName(tm.getColumnByName(t)) + "()";
					} else {
						a += (t.startsWith("$$$") ? "" : "\"") + (t.startsWith("$$$") ? t.substring(3) : t)
								+ (t.startsWith("$$$") ? "" : "\"");
					}
				}
				s += "        return " + a + ";\n";
			} else {
				s += "        return " + thisAttrName + ";\n";
			}
		}
		s += "    }\n";
		return s;
	}

	/**
	 * Creates a getter comment with the passed description.
	 *
	 * @param description The description which should be added to the comment.
	 * @return A getter comment with the passed description.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public String createGetterComment(String description) {
		String s = "    /**\n";
		s += "     * Returns the " + description + " of the object.\n";
		s += "     *\n";
		s += "     * @return The " + description + " of the object.\n";
		s += "     *\n";
		s += "     * " + this.createMethodChangedTag() + "\n";
		s += "     */\n";
		return s;
	}

	/**
	 * Returns a String which represents a code fragment for combo box string generation.
	 *
	 * @param tm The table model which the code fragment is to generate for.
	 * @return A String which represents a code fragment for combo box string generation.
	 *
	 * @changed OLI 02.06.2016 - Added.
	 */
	public String createComboboxStringCodeFragment(TableModel tm) {
		return this.createComboboxStringCodeFragment(tm, "o");
	}

	/**
	 * Returns a String which represents a code fragment for combo box string generation.
	 *
	 * @param tm        The table model which the code fragment is to generate for.
	 * @param paramName The name of the parameter which is converted in the combobox string fragment.
	 * @return A String which represents a code fragment for combo box string generation.
	 *
	 * @changed OLI 02.06.2016 - Added.
	 */
	public String createComboboxStringCodeFragment(TableModel tm, String paramName) {
		String code = "";
		boolean firstDone = false;
		for (ToStringContainerModel tsc : tm.getComboStringMembers()) {
			if (firstDone) {
				code += " + ";
			}
			code += this.quoteAndPlusPrefixIfNotEmpty(tsc.getPrefix()) + paramName + "."
					+ this.createGetterName(tsc.getColumn()) + "()"
					+ this.quoteAndPlusSuffixIfNotEmpty(tsc.getSuffix());
			firstDone = true;
		}
		return code;
	}

	/**
	 * Returns the name of a getter for the passed column.
	 *
	 * @param column The column which the getter is to create for.
	 * @return The name of the getter for the passed column.
	 *
	 * @changed OLI 29.10.2013 - Added.
	 */
	public String createGetterName(ColumnModel column) {
		String type = this.getJavaType(column);
		if (!column.isNotNull() && column.isPrimaryKey()) {
			type = this.typeUtil.getWrapperClass(type);
		}
		return (type.equals("boolean") ? "is" : "get") + column.getName();
	}

	/**
	 * Adds a simple hashCode method based on the Apache "EqualsBuilder" class to the method storage.
	 * 
	 * @changed OLI 02.05.2016 - Added.
	 */
	public void createHashCode() {
		this.imports.add(this.getOrgApacheCommonsLangBuilderPackageName());
		String s = this.createOverrideComment("Generated.");
		s += "    @Override public int hashCode() {\n";
		s += "        return HashCodeBuilder.reflectionHashCode(this);\n";
		s += "    }\n";
		this.storeMethod("hashCode", s);
	}

	/**
	 * Creates the code for the imports.
	 *
	 * @param packageName The name of a package which is to exclude from the list.
	 * @param importList  The list of imports whose code ist to create.
	 * @return The code for the imports.
	 *
	 * @changed OLI 29.09.2013 - Added.
	 */
	public String createImports(String packageName, ImportList importList) {
		return this.importCreator.createImportCodeFragment(importList);
	}

	/**
	 * Creates a changed tag for a method with the passed comment.
	 *
	 * @return A changed tag for a method with the passed comment.
	 *
	 * @changed OLI 15.01.2016 - Added.
	 */
	public String createMethodChangedTag() {
		return "Generated (date see above).";
	}

	/**
	 * Returns an override comment for an overridden method with standard comment ("Generated").
	 *
	 * @return An override comment for an overridden method.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 29.04.2016 - Added.
	 */
	public String createOverrideComment() {
		return this.createOverrideComment("Generated");
	}

	/**
	 * Returns an override comment for an overridden method.
	 *
	 * @param comment The comment content.
	 * @return An override comment for an overridden method.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 29.04.2016 - Added.
	 */
	public String createOverrideComment(String comment) {
		Checks.ensure(comment != null, "comment cannot be null.");
		String s = "    /**\n";
		s += "     * " + this.createMethodChangedTag() + "\n";
		s += "     */\n";
		return s;
	}

	/**
	 * Returns a method comment parameter tag for the passed column.
	 *
	 * @param column  The column which the comment is to create for.
	 * @param comment The comment for the field.
	 * @return A method comment parameter tag for the passed column.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 29.04.2016 - Added.
	 */
	public String createParameterTag(ColumnModel column, String comment) {
		Checks.ensure(column != null, "column cannot be null");
		Checks.ensure(comment != null, "comment cannot be null.");
		return this.createParameterTag(this.generatorUtil.getAttributeName(column), comment);
	}

	/**
	 * Returns a method comment parameter tag for the passed field name.
	 *
	 * @param fieldName The name of the field which the comment is to create for.
	 * @param comment   The comment for the field.
	 * @return A method comment parameter tag for the passed column.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 29.04.2016 - Added.
	 */
	public String createParameterTag(String fieldName, String comment) {
		Checks.ensure(comment != null, "comment cannot be null.");
		Checks.ensure(fieldName != null, "field name cannot be null");
		return "@param " + fieldName + " " + comment;
	}

	/**
	 * Returns a setter comment for a setter generated for the passed column.
	 * 
	 * @param c        The column which the setter comment is to create for.
	 * @param codeUtil An access to the code utilities.
	 * @return A setter comment for a setter generated for the passed column.
	 * @throws IllegalArgumentException In case of passing a null pointer.
	 *
	 * @changed OLI 29.04.2016 - Added.
	 */
	public String createSetterComment(ColumnModel c, CodeUtil codeUtil) {
		Checks.ensure(c != null, "column cannot be null.");
		Checks.ensure(codeUtil != null, "code util cannot be null.");
		String s = "    /**\n";
		s += "     * Sets a new value for the " + this.getDescription(c) + " of the object." + "\n";
		s += "     *\n";
		s += "     * " + this.createParameterTag(c, "The new " + this.getDescription(c) + " for the object.") + "\n";
		s += "     *\n";
		s += "     * " + this.createMethodChangedTag() + "\n";
		s += "     */\n";
		return s;
	}

	/**
	 * Creates the setters for the passed columns in the method storage of the code generator.
	 *
	 * @param setterConfiguration The configuration for the setter code.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 29.04.2016 - Added.
	 */
	public void createSetters(SetterConfiguration setterConfiguration) {
		Checks.ensure(setterConfiguration != null, "setter configuration cannot be null.");
		for (ColumnModel c : setterConfiguration.getColumns()) {
			if (this.isIgnoredForEmbedded(c)) {
				continue;
			}
			if (c.isOptionSet(ColParamIds.ASSEMBLY)) {
				continue;
			}
			if (c.isOptionSet(ColParamIds.NO_SETTER)) {
				continue;
			}
			String s = "";
			s += this.createSetterComment(c, setterConfiguration.getCodeUtil());
			s += "    " + (c.isDeprecated() ? "@Deprecated " : "")
					+ (setterConfiguration.isOverriden() ? "@Override " : "")
					+ ((c.isOptionSet(ColParamIds.PARENT_REF) && this.getParentReferences(c.getTable()).length == 1)
							|| (c.isOptionSet(ColParamIds.EMBEDDED_REFERENCE) && c.getTable().getDataModel()
									.isOptionSet(ModelParamIds.EMBEDDED_SETTERS_PROTECTED)) ? "protected" : "public")
					+ " void set" + c.getName() + "(" + this.getTypeAndName(c) + ") {\n";
			String attrName = this.generatorUtil.getAttributeName(c);
			if (c.isOptionSet(ColParamIds.CHECKED_SETTER)) {
				// if (this.parameterUtil.getParameters(c).contains(ColParamIds.CHECKED_SETTER)) {
				if (c.isNotNull()) {
					this.imports.addStatic("corentx.util.Checks.*;");
					s += "        ensure(" + attrName + " != null, \"" + this.getDescription(c)
							+ " cannot be null.\");\n";
				}
				if ((c.isOptionSet(ColParamIds.NOT_BLANK)) && this.getJavaType(c).equals("String")) {
					// if (this.parameterUtil.getParameters(c).contains(ColParamIds.NOT_BLANK)
					// && this.getJavaType(c).equals("String")) {
					this.imports.addStatic("corentx.util.Checks.*;");
					s += "        ensure(!" + attrName + ".isEmpty(), \"" + this.getDescription(c)
							+ " cannot be empty.\");\n";
				}
			} else if (this.isParentReference(c)) {
				s += this.getSetterCodeChecksForDivergentParentReferenceSetters(s, c, attrName);
			}
			String thisAttrName = "this." + attrName;
			if (this.typeUtil.isStringDomain(c.getDomain()) && this.hasTrimOption(c)) {
				this.imports.add("org.apache.commons.lang");
				s += "        this." + attrName + " = ";
				if (c.isOptionSet(ColParamIds.TRIM)) {
					s += "StringUtils.trim(" + attrName;
				} else if (c.isOptionSet(ColParamIds.TRIM_LEFT)) {
					s += "StringUtils.stripStart(" + attrName + ", null";
				} else if (c.isOptionSet(ColParamIds.TRIM_RIGHT)) {
					s += "StringUtils.stripEnd(" + attrName + ", null";
				}
				s += ");\n";
			} else if (this.isTimestamp(c.getDomain())) {
				this.imports.add("corentx.dates");
				if (!setterConfiguration.isTimestampAsReference()) {
					boolean intWrapper = "Integer".equals(this.typeUtil.getTimestampWrapper(c.getDomain()));
					s += "        if (" + attrName + " != null) {\n";
					s += "            " + thisAttrName + " =" + (intWrapper ? " (int)" : "") + " " + attrName
							+ ".toLong();\n";
					s += "        } else {\n";
					s += "            " + thisAttrName + " = null;\n";
					s += "        }\n";
				} else {
					s += "        " + thisAttrName + " = " + attrName + ";\n";
				}
			} else {
				s = this.getSetterCodeForDivergentStandardSetters(s, c, thisAttrName, attrName);
			}
			if (setterConfiguration.isChangeListenerLogicRequired()) {
				s += this.createChangeListenerLogic(c, attrName, setterConfiguration);
			}
			s += "    }\n";
			this.storeMethod("set" + c.getName(), s);
		}
	}

	/**
	 * The method returns the logic for change listeners in entity objects. Override it if a special logic is to create.
	 *
	 * @param c                   The column which the setter is created for.
	 * @param attrName            The attribute name.
	 * @param setterConfiguration The setter configurations.
	 * @return The code for the change listener logic.
	 *
	 * @changed OLI 09.06.2016 - Added.
	 */
	public String createChangeListenerLogic(ColumnModel c, String attrName, SetterConfiguration setterConfiguration) {
		return "";
	}

	private boolean hasTrimOption(ColumnModel c) {
		for (OptionModel o : c.getOptions()) {
			if (o.getName().equals(ColParamIds.TRIM) || o.getName().equals(ColParamIds.TRIM_LEFT)
					|| o.getName().equals(ColParamIds.TRIM_RIGHT)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a string with the content of the passed string list whose entries are separated by the passed separator.
	 *
	 * @param strings   The list of string whose content is to transfer into a separated string.
	 * @param separator A separator sequence.
	 * @return A string with the content of the passed string list whose entries are separated by the passed separator.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 04.07.2017 - Added.
	 */
	public String getSeparatedString(String[] strings, String separator) {
		Checks.ensure(strings != null, "array of strings to tranfer in a separated string " + "cannot be null.");
		Checks.ensure(separator != null, "separator cannot be null.");
		StringBuffer sb = new StringBuffer("");
		for (String s : strings) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * Returns a string with the content of the passed string list whose entries are separated by the passed separator.
	 *
	 * @param strings   The list of string whose content is to transfer into a separated string.
	 * @param separator A separator sequence.
	 * @return A string with the content of the passed string list whose entries are separated by the passed separator.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 04.07.2017 - Added.
	 */
	public String getSeparatedString(List<String> strings, String separator) {
		Checks.ensure(strings != null, "array of strings to tranfer in a separated string " + "cannot be null.");
		return this.getSeparatedString(strings.toArray(new String[0]), separator);
	}

	/**
	 * Overwrite this method to get a divergent code for the checks in a parent reference setter.
	 *
	 * @param code     The current code of the setter (usually the comment and the header line.
	 * @param c        The column which the checker is created for.
	 * @param attrName The attribute name of the parameter passed to the setter.
	 * @return The code for checking parameters and other constraints before setting the passed value to the member
	 *         variable.
	 *
	 * @changed OLI 26.05.2016 - Added.
	 */
	public String getSetterCodeChecksForDivergentParentReferenceSetters(String code, ColumnModel c, String attrName) {
		this.imports.addStatic("corentx.util.Checks.*;");
		return "        ensure(this." + attrName + " == null, new " + "UnsupportedOperationException(\""
				+ this.getDescription(c) + " is not null and cannot be set again.\"));\n" + "        ensure(" + attrName
				+ " != null, \"" + this.getDescription(c) + " cannot be null.\");\n";
	}

	/**
	 * This method sets the standard code for the content of the setter method of an attribute which should have no
	 * special behavior caused by options or something like that.<BR>
	 * It can be overwritten in case of a divergent content for the standard setter.
	 * 
	 * @param code         The current code of the setter.
	 * @param c            The column which the setter is generated for.
	 * @param thisAttrName The name of the member variable which is to set by the setter.
	 * @param attrName     The name of the parameter which is passed to the setter (usually the new value for the member
	 *                     variable).
	 * @return The completed setter code (with no closing curly brace).
	 *
	 * @changed OLI 26.05.2016 - Added.
	 */
	public String getSetterCodeForDivergentStandardSetters(String code, ColumnModel c, String thisAttrName,
			String attrName) {
		return code + "        " + thisAttrName + " = " + attrName + ";\n";
	}

	/**
	 * Adds a toString method to the method storage of the code generator.
	 *
	 * @param columns The column which should be shown in the string created by the toString method.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public void createToString(ColumnModel[] columns) {
		String s = this.createOverrideComment("Generated.");
		String a = "";
		s += "    @Override public String toString() {\n";
		for (boolean b : new boolean[] { true, false }) {
			for (ColumnModel c : columns) {
				if (this.isIgnoredForEmbedded(c)) {
					continue;
				}
				if (c.isPrimaryKey() == b) {
					String attrName = c.getName().replace(".", "");
					String getterName = this.createGetterName(c);
					if (a.length() > 0) {
						a += " + \", ";
					}
					a += attrName + "=\" + this." + getterName + "()";
				}
			}
		}
		s += "        return \"" + a + ";\n";
		s += "    }\n";
		this.storeMethod("toString", s);
	}

	/**
	 * Adds a toString method to the method storage of the code generator based on the string members of the passed
	 * table.
	 *
	 * @param table The table which the toString is to create for.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public void createToString(TableModel table) {
		this.createToString(table, false);
	}

	public void createToString(TableModel table, boolean dataObject) {
		if (table.getToStringMembers().length == 0) {
			this.createToString(table.getColumns());
			return;
		}
		String code = this.createOverrideComment("Generated.");
		code += "    @Override public String toString() {\n";
		code += "        return ";
		boolean firstDone = false;
		for (ToStringContainerModel tsc : this.cleanUpColumns(table, table.getToStringMembers(), dataObject)) {
			if (firstDone) {
				code += " + ";
			}
			code += this.quoteAndPlusPrefixIfNotEmpty(tsc.getPrefix()) + "this."
					+ this.createGetterName(tsc.getColumn()) + "()"
					+ this.quoteAndPlusSuffixIfNotEmpty(tsc.getSuffix());
			firstDone = true;
		}
		code += ";\n";
		code += "    }\n";
		this.storeMethod("toString", code);
	}

	/**
	 * Sets the first letter of the passed string to upper case.
	 *
	 * @param s The string whose first letter is to set to upper case.
	 * @return The passed string with an upper case first letter.
	 *
	 * @changed OLI 08.07.2016 - Added.
	 */
	public String setFirstLetterToUpperCase(String s) {
		Checks.ensure(s != null, "string cannot be null.");
		Checks.ensure(!s.isEmpty(), "string cannot be empty.");
		return s.substring(0, 1).toUpperCase() + (s.length() > 1 ? s.substring(1) : "");
	}

	/**
	 * @changed OLI 07.08.2013 - Added.
	 */
	@Override
	public boolean generate(DataModel dm, IndividualPreferences individualPreferences,
			SourceFileWriter sourceFileWriter, SourceFileReader sourceFileReader, GenerationProcessReport report,
			List<PostGeneratingCommand> postGeneratorCommands) {
		this.setIndividualPreferences(individualPreferences);
		if (this.getType() == CodeGeneratorType.MODEL) {
			return this.generateForModels(dm, individualPreferences, sourceFileWriter, sourceFileReader, report,
					postGeneratorCommands);
		} else if (this.getType() == CodeGeneratorType.PANEL) {
			return this.generateForPanels(dm, individualPreferences, sourceFileWriter, sourceFileReader, report,
					postGeneratorCommands);
		}
		return this.generateForTables(dm, individualPreferences, sourceFileWriter, sourceFileReader, report,
				postGeneratorCommands);
	}

	private boolean generateForModels(DataModel dm, IndividualPreferences individualPreferences,
			SourceFileWriter sourceFileWriter, SourceFileReader sourceFileReader, GenerationProcessReport report,
			List<PostGeneratingCommand> postGeneratorCommands) {
		TableModel tm = new PhantomTable("MODEL", dm);
		tm.setDataModel(dm);
		if (this.getAlternateCodeFolder(dm) != null) {
			tm.setCodeFolder(this.getAlternateCodeFolder(dm));
		}
		CodeUtil codeUtil = new CodeUtil(new PDate(), individualPreferences, dm.getOwner());
		System.out.println("starting " + this.getClass().getSimpleName() + " for model: " + dm.getName());
		if (this.isSuppressGeneration(dm, null, codeUtil)) {
			this.checkExcludedButPresent(tm, dm, individualPreferences);
			return true;
		}
		this.cleanUp();
		String className = this.getClassName(tm);
		String packageName = this.getPackageName(tm);
		String fileName = this.createAbsoluteFileName(individualPreferences, dm, this.getSubProjectFolder(dm),
				className, packageName, false, tm);
		this.executeAdditionalCodeCheckers(report, fileName, null, individualPreferences, sourceFileReader, null);
		if (this.isOneTimeFactory() && this.isCodeChanged(fileName)) {
			report.addUnchangedByTag(tm, new CodeGeneratorReportTableEntry(this, fileName));
			System.out.println("Generation blocked by changes for class: " + this.getClassName(null));
			this.checkExcludedButPresent(tm, dm, individualPreferences);
			return true;
		}
		if (this.isDeprecated()) {
			if (new File(fileName).exists()) {
				report.addDeprecatedSourceCode(tm, new CodeGeneratorReportTableEntry(this, fileName));
			}
			return true;
		}
		GeneratorResult gr = this.generateCodeForClass(dm, tm, codeUtil, postGeneratorCommands, null);
		String code = gr.getCode();
		code = this.afterClassCodeGenerated(code, dm, tm, codeUtil, postGeneratorCommands);
		if (code != null) {
			if (gr.getState() != GeneratorResultState.NOT_NECESSARY) {
				report.removeGeneratedFileName(fileName);
			}
			if (this.changedChecker.hasBeenChanged(fileName, code, individualPreferences, sourceFileReader)) {
				try {
					if (gr.getState() == GeneratorResultState.SUCCESS) {
						sourceFileWriter.write(fileName, code, this.isAppendingNewContent());
						report.addWritten(tm, new CodeGeneratorReportTableEntry(this, fileName));
						report.removeGeneratedFileName(fileName);
					} else if ((gr.getState() == GeneratorResultState.NOT_NECESSARY) && new File(fileName).exists()) {
						this.checkExcludedButPresent(tm, dm, individualPreferences, report);
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
					return false;
				}
			} else if ((gr.getState() == GeneratorResultState.NOT_NECESSARY) && new File(fileName).exists()) {
				this.checkExcludedButPresent(tm, dm, individualPreferences, report);
			} else {
				report.addUnchanged(tm, new CodeGeneratorReportTableEntry(this, fileName));
				System.out.println("file unchanged (not written): " + fileName);
				report.removeGeneratedFileName(fileName);
			}
			this.executeAdditionalCodeCheckers(report, fileName, code, individualPreferences, sourceFileReader, tm);
			for (String line : gr.getLogLines()) {
				report.addLogMessage(line);
			}
		}
		if (gr.getState() == GeneratorResultState.SUCCESS) {
			for (ResourceData rid : gr.getResourceIds()) {
				report.addResourceId(rid);
			}
		}
		return true;
	}

	private boolean generateForPanels(DataModel dm, IndividualPreferences individualPreferences,
			SourceFileWriter sourceFileWriter, SourceFileReader sourceFileReader, GenerationProcessReport report,
			List<PostGeneratingCommand> postGeneratorCommands) {
		CodeUtil codeUtil = new CodeUtil(new PDate(), individualPreferences, dm.getOwner());
		for (TableModel tm : dm.getTables()) {
			if (tm.isExternalTable()) {
				continue;
			}
			if (!tm.isGenerateCode() || tm.isOptionSet(TableParamIds.NO_CODE_GENERATION)) {
				continue;
			}
			if ((this.exclusiveTableForCodeGeneration != null) && (tm != this.exclusiveTableForCodeGeneration)) {
				continue;
			}
			if (this.exclusiveTableForCodeGeneration == null) {
				if (!tm.isGenerateCode()) {
					continue;
				}
				if (!this.necessaryIncludeMarks.isEmpty()
						&& !this.containsNecessaryIncludeMark(this.parameterUtil.getParameters(tm))) {
					continue;
				}
				if (this.containsExcludeMark(this.getParameters(tm), tm)) {
					continue;
				}
			}
			for (PanelModel pm : tm.getPanels()) {
				System.out.println("starting " + this.getClass().getSimpleName() + " for table: " + tm.getName()
						+ ", panel: " + pm.getTabTitle());
				this.cleanUp();
				this.currentPanelModel = pm;
				if (this.isSuppressGeneration(dm, tm, codeUtil)) {
					continue;
				}
				String className = this.getClassName(tm);
				String packageName = this.getPackageName(tm);
				String fileName = this.createAbsoluteFileName(individualPreferences, dm, tm.getCodeFolder(), className,
						packageName, this.isSpecialAPI(tm), tm);
				if (this.isOneTimeFactory() && this.isCodeChanged(fileName)) {
					continue;
				}
				if (this.isDeprecated() || this.isSuppressGeneration(dm, tm, codeUtil)) {
					if (new File(fileName).exists()) {
						report.addDeprecatedSourceCode(tm, new CodeGeneratorReportTableEntry(this, fileName));
					}
					continue;
				}
				GeneratorResult gr = this.generateCodeForClass(dm, tm, codeUtil, postGeneratorCommands, pm);
				String code = gr.getCode();
				code = this.afterClassCodeGenerated(code, dm, tm, codeUtil, postGeneratorCommands);
				if (code != null) {
					if (gr.getState() != GeneratorResultState.NOT_NECESSARY) {
						report.removeGeneratedFileName(fileName);
					}
					if (this.changedChecker.hasBeenChanged(fileName, code, individualPreferences, sourceFileReader)) {
						try {
							if (gr.getState() == GeneratorResultState.SUCCESS) {
								sourceFileWriter.write(fileName, code, this.isAppendingNewContent());
								report.addWritten(tm, new CodeGeneratorReportTableEntry(this, fileName));
								report.removeGeneratedFileName(fileName);
							} else if ((gr.getState() == GeneratorResultState.NOT_NECESSARY)
									&& new File(fileName).exists()) {
							}
						} catch (IOException ioe) {
							ioe.printStackTrace();
							return false;
						}
					} else if ((gr.getState() == GeneratorResultState.NOT_NECESSARY) && new File(fileName).exists()) {
					} else {
						report.removeGeneratedFileName(fileName);
					}
					this.executeAdditionalCodeCheckers(report, fileName, code, individualPreferences, sourceFileReader,
							tm);
					for (String line : gr.getLogLines()) {
						report.addLogMessage(line);
					}
				}
				if (gr.getState() == GeneratorResultState.SUCCESS) {
					for (ResourceData rid : gr.getResourceIds()) {
						report.addResourceId(rid);
					}
				}
			}
		}
		return true;
	}

	private boolean generateForTables(DataModel dm, IndividualPreferences individualPreferences,
			SourceFileWriter sourceFileWriter, SourceFileReader sourceFileReader, GenerationProcessReport report,
			List<PostGeneratingCommand> postGeneratorCommands) {
		CodeUtil codeUtil = new CodeUtil(new PDate(), individualPreferences, dm.getOwner());
		for (TableModel tm : dm.getTables()) {
			if (tm.isExternalTable()) {
				continue;
			}
			if (!tm.isGenerateCode() || tm.isOptionSet(TableParamIds.NO_CODE_GENERATION)) {
				continue;
			}
			System.out.println("starting " + this.getClass().getSimpleName() + " for table: " + tm.getName());
			if ((this.exclusiveTableForCodeGeneration != null) && (tm != this.exclusiveTableForCodeGeneration)) {
				this.checkExcludedButPresent(tm, dm, individualPreferences);
				continue;
			}
			if (this.exclusiveTableForCodeGeneration == null) {
				if (!tm.isGenerateCode()) {
					this.checkExcludedButPresent(tm, dm, individualPreferences);
					continue;
				}
				if (this.isSuppressGeneration(dm, tm, codeUtil)) {
					this.checkExcludedButPresent(tm, dm, individualPreferences);
					continue;
				}
				/*
				 * if (tm.isFirstGenerationDone() && this.isOneTimeFactory()) { continue; }
				 */
				if (!this.necessaryIncludeMarks.isEmpty()
						&& !this.containsNecessaryIncludeMark(this.parameterUtil.getParameters(tm))) {
					report.addBlockedByMissingInclusionMark(tm, new CodeGeneratorReportTableEntry(this, null));
					System.out.println("Generation blocked by lack of necessary inclusion mark " + "for class: "
							+ this.getClassName(tm));
					this.checkExcludedButPresent(tm, dm, individualPreferences, report);
					continue;
				} else {
				}
				if (this.containsExcludeMark(this.getParameters(tm), tm)) {
					report.addBlockedByExclusionMark(tm, new CodeGeneratorReportTableEntry(this, null));
					System.out.println("Generation blocked by exclusion mark for class: " + this.getClassName(tm));
					this.checkExcludedButPresent(tm, dm, individualPreferences);
					continue;
				}
			}
			this.cleanUp();
			String className = this.getClassName(tm);
			String packageName = this.getPackageName(tm);
			String fileName = this.createAbsoluteFileName(individualPreferences, dm, tm.getCodeFolder(), className,
					packageName, this.isSpecialAPI(tm), tm);
			this.executeAdditionalCodeCheckers(report, fileName, null, individualPreferences, sourceFileReader, tm);
			if (this.isOneTimeFactory() && this.isCodeChanged(fileName)) {
				report.addUnchangedByTag(tm, new CodeGeneratorReportTableEntry(this, fileName));
				System.out.println("Generation blocked by changes for class: " + this.getClassName(tm));
				this.checkExcludedButPresent(tm, dm, individualPreferences);
				report.removeGeneratedFileName(fileName);
				continue;
			}
			if (this.isDeprecated() || this.isSuppressGeneration(dm, tm, codeUtil)) {
				if (new File(fileName).exists()) {
					report.addDeprecatedSourceCode(tm, new CodeGeneratorReportTableEntry(this, fileName));
				}
				continue;
			}
			GeneratorResult gr = this.generateCodeForClass(dm, tm, codeUtil, postGeneratorCommands, null);
			String code = gr.getCode();
			code = this.afterClassCodeGenerated(code, dm, tm, codeUtil, postGeneratorCommands);
			if (code != null) {
				if (gr.getState() != GeneratorResultState.NOT_NECESSARY) {
					report.removeGeneratedFileName(fileName);
				}
				if (this.changedChecker.hasBeenChanged(fileName, code, individualPreferences, sourceFileReader)) {
					try {
						if (gr.getState() == GeneratorResultState.SUCCESS) {
							sourceFileWriter.write(fileName, code, this.isAppendingNewContent());
							report.addWritten(tm, new CodeGeneratorReportTableEntry(this, fileName));
						} else if ((gr.getState() == GeneratorResultState.NOT_NECESSARY)
								&& new File(fileName).exists()) {
							this.checkExcludedButPresent(tm, dm, individualPreferences, report);
						}
					} catch (IOException ioe) {
						ioe.printStackTrace();
						return false;
					}
				} else if ((gr.getState() == GeneratorResultState.NOT_NECESSARY) && new File(fileName).exists()) {
					this.checkExcludedButPresent(tm, dm, individualPreferences, report);
				} else {
					report.addUnchanged(tm, new CodeGeneratorReportTableEntry(this, fileName));
					System.out.println("file unchanged (not written): " + fileName);
				}
				this.executeAdditionalCodeCheckers(report, fileName, code, individualPreferences, sourceFileReader, tm);
				for (String line : gr.getLogLines()) {
					report.addLogMessage(line);
				}
			}
			if (gr.getState() == GeneratorResultState.SUCCESS) {
				for (ResourceData rid : gr.getResourceIds()) {
					report.addResourceId(rid);
				}
			}
		}
		return true;
	}

	private void checkExcludedButPresent(TableModel tm, DataModel dm, IndividualPreferences individualPreferences) {
		this.checkExcludedButPresent(tm, dm, individualPreferences, null);
	}

	private void checkExcludedButPresent(TableModel tm, DataModel dm, IndividualPreferences individualPreferences,
			GenerationProcessReport report) {
		String className = this.getClassName(tm);
		String packageName = this.getPackageName(tm);
		String fileName = this.createAbsoluteFileName(individualPreferences, dm, tm.getCodeFolder(), className,
				packageName, this.isSpecialAPI(tm), tm);
		if (new File(fileName).exists()) {
			System.out.println("EXISTING BUT NOT WANTED: " + fileName);
			if (report != null) {
				report.addDeprecatedSourceCode(tm, new CodeGeneratorReportTableEntry(this, fileName));
			}
		}
	}

	protected void cleanUp() {
		this.methodCodeStorage.clear();
	}

	/**
	 * Returns an arrays of cleaned up toString containers.
	 *
	 * @param table      The table which the array is to clean up for.
	 * @param columns    The toString members.
	 * @param dataObject Set this flag if only columns should be in the returned array which are also members in the
	 *                   selection member list of the table.
	 * @return An arrays of cleaned up toString containers.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public ToStringContainerModel[] cleanUpColumns(TableModel table, ToStringContainerModel[] columns,
			boolean dataObject) {
		Checks.ensure(columns != null, "columns cannot be null.");
		Checks.ensure(table != null, "table cannot be null.");
		if (!dataObject) {
			return columns;
		}
		List<ToStringContainerModel> l = new LinkedList<ToStringContainerModel>();
		List<SelectionMemberModel> visibleColumns = new Vector<SelectionMemberModel>(
				Arrays.asList(table.getSelectableColumns()));
		for (ColumnModel pk : table.getPrimaryKeyColumns()) {
			visibleColumns.add(new SelectionMember(pk, SelectionAttribute.OPTIONAL, ""));
		}
		for (ToStringContainerModel column : columns) {
			for (SelectionMemberModel visibleColumn : visibleColumns) {
				if (visibleColumn.getColumn() == column.getColumn()) {
					l.add(column);
				}
			}
		}
		return l.toArray(new ToStringContainerModel[0]);
	}

	public String cutOffLeadingQuestionMark(String s) {
		return s.startsWith("?") ? (s.length() > 1 ? s.substring(1) : "") : s;
	}

	private void executeAdditionalCodeCheckers(GenerationProcessReport report, String fileName, String code,
			IndividualPreferences individualPreferences, SourceFileReader sourceFileReader, TableModel tm) {
		if (this.getAdditionalCodeCheckers().length > 0) {
			for (AdditionalCodeChecker acc : this.getAdditionalCodeCheckers()) {
				acc.execute(report, fileName, code, individualPreferences, sourceFileReader, this, tm);
			}
		}
	}

	protected GeneratorResult generateCodeForClass(DataModel dm, TableModel tm, CodeUtil codeUtil,
			List<PostGeneratingCommand> postGeneratorCommands, PanelModel pm) {
		if (pm != null) {
			this.currentPanelModel = pm;
		}
		String className = this.getClassName(tm);
		String packageName = this.getPackageName(tm);
		String code = codeUtil.createClassHeaderComment(className, !this.isOneTimeFactory());
		this.imports = new ImportList(packageName);
		code += "\npackage " + packageName + ";\n\n\n";
		code += "$IMPORTS$";
		// TODO
		code += codeUtil.createClassComment(this.generateClassComment(dm, tm, codeUtil, className),
				!this.isOneTimeFactory());
		code += "\n";
		GeneratorResult gr = this.generateCodeForClassBody(dm, tm, codeUtil, className, postGeneratorCommands);
		code += gr.getCode();
		code += this.createStoredMethods();
		code += this.createClassFooter();
		String i = this.createImports(packageName, this.imports);
		code = code.replace("$IMPORTS$", i + (i.length() > 0 ? "\n\n" : ""));
		return new GeneratorResult(code, gr.getState(), gr.getLogLines(), gr.getResourceIds());
	}

	/**
	 * Returns the code for a simple parameterless constructor.
	 *
	 * @param className The name of the class which the constructor is to create for.
	 * @return The code for a simple parameterless constructor.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public String createSimpleConstructor(String className) {
		String s = "    /**\n";
		s += "     * Creates a new object with default values.\n";
		s += "     *\n";
		s += "     * " + this.createMethodChangedTag() + "\n";
		s += "     */\n";
		s += "    public " + className + "() {\n";
		s += "        super();\n";
		s += "    }\n";
		s += "\n";
		return s;
	}

	/**
	 * Returns the stored methods in an alphabetical order.
	 *
	 * @return The stored methods in an alphabetical order.
	 *
	 * @changed OLI 29.04.2016 - Added.
	 */
	protected String createStoredMethods() {
		String code = "";
		List<MethodCode> methods = new SortedVector<MethodCode>();
		for (MethodCode mc : this.methodCodeStorage.values()) {
			methods.add(mc);
		}
		for (MethodCode mc : methods) {
			if (code.length() > 0) {
				code += "\n";
			}
			code += mc.getCode();
		}
		return code;
	}

	/**
	 * Returns the class comment for the class which is to generate.
	 *
	 * @param dm        The data model which the class is to create for.
	 * @param tm        The table model which the class is to create for.
	 * @param codeUtil  Some code utilities.
	 * @param className The name of the class which the comment is to create for.
	 * @return The class comment for the class which is to generate.
	 */
	abstract public String generateClassComment(DataModel dm, TableModel tm, CodeUtil codeUtil, String className);

	/**
	 * Returns a generator result for the class body.
	 *
	 * @param dm                    The data model which the code is to generate for.
	 * @param tm                    The table model which the code is to generate for.
	 * @param codeUtil              Some code utilities.
	 * @param className             The name of the class which the comment is to create for.
	 * @param postGeneratorCommands A list of post generator commands which can be filled by the code generator.
	 * @return A generator result for the class body.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	abstract public GeneratorResult generateCodeForClassBody(DataModel dm, TableModel tm, CodeUtil codeUtil,
			String className, List<PostGeneratingCommand> postGeneratorCommands);

	/**
	 * Returns a list of additional code checkers. These checkers are executed after the code has been generated and
	 * written to the file system.
	 *
	 * @return A list of additional code checkers. These checkers are executed after the code has been generated and
	 *         written to the file system.
	 *
	 * @changed OLI 04.09.2015 - Added.
	 */
	protected AdditionalCodeChecker[] getAdditionalCodeCheckers() {
		return new AdditionalCodeChecker[0];
	}

	/**
	 * May be overwritten to generate an alternate code folder.
	 *
	 * @param dm The data model whose alternate code folder is to generate.
	 * @return An alternate code folder for the data model or <CODE>null</CODE> if no alternate code folder is required.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	public String getAlternateCodeFolder(DataModel dm) {
		return null;
	}

	public ColumnModel[] getColumnsAssortedByNames(ColumnModel[] columns) {
		List<String> l = new SortedVector<String>();
		List<ColumnModel> lc = new LinkedList<ColumnModel>();
		Map<String, ColumnModel> m = new Hashtable<String, ColumnModel>();
		for (ColumnModel c : columns) {
			String cn = this.cutOffLeadingQuestionMark(c.getName());
			l.add(cn);
			m.put(cn, c);
		}
		for (String s : l) {
			lc.add(m.get(s));
		}
		return lc.toArray(new ColumnModel[0]);
	}

	/**
	 * Returns the compound name of the passed column (or the simple name if the class is no compound column). The
	 * method can be overwritten if the compound logic is required.
	 *
	 * @param column The column whose compound name is to return.
	 * @return The compound name of the passed column (or the simple name if the class is no compound column).
	 *
	 * @changed OLI 05.08.2016 - Added. / public String getCompoundName(ColumnModel c) { return c.getName(); }
	 * 
	 *          /** Can be overriden if an alternate class name should be returned for reference classes.
	 *
	 * @param tm The referenced table which the class name is to return for.
	 *
	 * @changed OLI 23.05.2016 - Added.
	 */
	public String getAlternateReferenceTableClassName(TableModel tm) {
		return null;
	}

	/**
	 * Returns an attribute name for complex name constructs (with dots inside).
	 *
	 * @param c The column which the complex name construct (with dots inside) is get for.
	 * @return An attribute name for complex name constructs (with dots inside).
	 *
	 * @changed OLI 09.08.2016 - Added.
	 */
	public String getAttributeNameComplex(ColumnModel c) {
		return this.getAttributeNameComplex(c.getName());
	}

	/**
	 * Returns an attribute name for complex name constructs (with dots inside).
	 *
	 * @param cn The complex column name to make an attribute name from.
	 * @return An attribute name for complex name constructs (with dots inside).
	 *
	 * @changed OLI 05.08.2016 - Added.
	 */
	public String getAttributeNameComplex(String cn) {
		if (cn.contains(".")) {
			List<String> l = Str.splitToList(cn, ".");
			cn = "";
			for (String s : l) {
				if (cn.length() > 0) {
					cn += ".";
				}
				cn += this.generatorUtil.getAttributeName(s);
			}
			return cn;
		}
		return this.generatorUtil.getAttributeName(cn);
	}

	/**
	 * Returns an attribute name for the passed column.
	 *
	 * @param c The column whose name is to convert to an attribute name.
	 * @return The attribute name for the column.
	 *
	 * @changed OLI 06.04.2016 - Added.
	 */
	public String getAttributeName(ColumnModel c) {
		return this.generatorUtil.getAttributeName(c.getName());
	}

	/**
	 * Returns an attribute name for the passed string.
	 *
	 * @param s The string whose name is to convert to an attribute name.
	 * @return The attribute name for the string.
	 *
	 * @changed OLI 06.04.2016 - Added.
	 */
	public String getAttributeName(String s) {
		return this.generatorUtil.getAttributeName(s);
	}

	/**
	 * Returns an attribute name for the passed table.
	 *
	 * @param t The table whose name is to convert to an attribute name.
	 * @return The attribute name for the table.
	 *
	 * @changed OLI 06.04.2016 - Added.
	 */
	public String getAttributeName(TableModel t) {
		return this.generatorUtil.getAttributeName(t.getName());
	}

	/**
	 * Returns the name of the class which is to create by the code generator.
	 *
	 * @param tm The table which the class is to create for.
	 * @return The name of the class which is to create by the code generator.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	abstract public String getClassName(TableModel tm);

	/**
	 * Returns the code path of the passed data model.
	 *
	 * @param dm The data model whose code path is to return.
	 * @return The code path of the passed data model.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	public String getCodePath(DataModel dm) {
		return dm.getBasicCodePath();
	}

	/**
	 * Returns the default value for the passed column.
	 *
	 * @param c                    The column whose default value is to return.
	 * @param referencesToKeyClass Set this flag if references have to be changed to class references.
	 * @param timestampsWrapped    Set this flag if timestamp type should be wrapped into timestamp classes.
	 * @return The default value for the passed column.
	 *
	 * @changed OLI 28.04.2016 - Added.
	 */
	protected String getDefaultValue(ColumnModel c, boolean referencesToKeyClass, boolean timestampsWrapped) {
		if (this.isEnum(c)) {
			String type = this.getJavaType(c, referencesToKeyClass, timestampsWrapped);
			String value = c.getDefaultValue().replace("'", " ").replace("\"", " ").trim();
			return type + "." + value;
		} else if (this.getJavaType(c).equals("String")) {
			String dv = c.getDefaultValue().trim();
			if (dv.startsWith("'") && (dv.length() > 0)) {
				dv = "\"" + dv.substring(1);
			}
			if (dv.endsWith("'") && (dv.length() > 0)) {
				dv = dv.substring(0, dv.length() - 1) + "\"";
			}
			if (!dv.startsWith("\"")) {
				dv = "\"" + dv;
			}
			if (!dv.endsWith("\"")) {
				dv = dv + "\"";
			}
			return dv;
		}
		return c.getDefaultValue();
	}

	/**
	 * Returns a description text for the passed columns name.
	 *
	 * @param c The column whose name is to return as a description text.
	 * @return A description text for the passed columns name.
	 *
	 * @changed OLI 29.09.2013 - Added.
	 */
	public String getDescription(ColumnModel c) {
		return this.descriptionGenerator.getDescription(c);
	}

	/**
	 * Returns a description text for the passed domains name.
	 *
	 * @param d The domain whose name is to return as a description text.
	 * @return A description text for the passed domains name.
	 *
	 * @changed OLI 29.09.2013 - Added.
	 */
	public String getDescription(DomainModel d) {
		return this.descriptionGenerator.getDescription(d);
	}

	/**
	 * Returns a description text for the passed string.
	 *
	 * @param s The string which the description is to create for.
	 * @return A description text for the passed string.
	 *
	 * @changed OLI 29.09.2013 - Added.
	 */
	public String getDescription(String s) {
		return this.descriptionGenerator.getDescription(s);
	}

	/**
	 * Returns a description text for the passed tables name.
	 *
	 * @param s The table whose name is to return as a description text.
	 * @return A description text for the passed tables name.
	 *
	 * @changed OLI 29.09.2013 - Added.
	 */
	public String getDescription(TableModel tm) {
		return this.descriptionGenerator.getDescription(tm);
	}

	/**
	 * Returns the name of the enum class for the domain.
	 *
	 * @param dm The domain whose enum class name is to return.
	 * @return The name of the enum class for the domain or <CODE>null</CODE> if there is no enum class name for the
	 *         domain.
	 *
	 * @changed OLI 01.04.2016 - Added.
	 */
	abstract public String getEnumClassName(DomainModel dm);

	/**
	 * Returns all columns from the list which are marked as enums.
	 *
	 * @param columns The column list which is to check.
	 * @return All columns from the list which are marked as enums.
	 *
	 * @changed OLI 10.10.2013 - Added.
	 */
	public ColumnModel[] getEnumColumns(ColumnModel[] columns) {
		List<ColumnModel> l = new LinkedList<ColumnModel>();
		for (ColumnModel c : columns) {
			if (this.isEnum(c)) {
				l.add(c);
			}
		}
		return l.toArray(new ColumnModel[0]);
	}

	/**
	 * Returns all columns from the table which are marked as enums.
	 *
	 * @param table The table whose columns are to check.
	 * @return All columns from the table which are marked as enums.
	 *
	 * @changed OLI 10.10.2013 - Added.
	 */
	public ColumnModel[] getEnumColumns(TableModel table) {
		return this.getEnumColumns(table.getColumns());
	}

	/**
	 * Can be overwritten to define marks which excludes the table from the generation process if found in the tables
	 * parameters.
	 * 
	 * @return An array of exclusion marks or an empty array if no marks are defined.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	public String[] getExcludeMarks() {
		return new String[0];
	}

	/**
	 * Returns the suffix for the generated file (usually "java"). Override this method if an other suffix is required
	 * for your output file.
	 *
	 * @return The suffix for the generated file (usually "java").
	 *
	 * @changed OLI 18.12.2013 - Added.
	 */
	public String getFileSuffix() {
		return "java";
	}

	/**
	 * Returns a Java type for the passed column.
	 *
	 * @param column               The column which the Java type is to return for.
	 * @param referencesToKeyClass Set this flag, if references should be represented by keys only (if unset references
	 *                             are represented by Java pointers to referenced object.
	 * @param timestampsWrapped    Set this flag if fields of timestamp domains are to wrap by timestamp classes of the
	 *                             "corentx.dates" package.
	 * @return The name of the Java type.
	 */
	public String getJavaType(ColumnModel column) {
		Checks.ensure(column != null, "column cannot be null.");
		Checks.ensure(column.getDomain() != null, "column has no domain.");
		if (column.getDomain().getDataType() == Types.TIMESTAMP) {
			this.imports.add("java.sql");
		}
		return this.typeUtil.getJavaType(column, false, false, this.isEntityGenerator(), this.parameterUtil,
				this.generatorUtil, this.imports);
	}

	/**
	 * Returns a Java type for the passed column.
	 *
	 * @param column               The column which the Java type is to return for.
	 * @param referencesToKeyClass Set this flag, if references should be represented by keys only (if unset references
	 *                             are represented by Java pointers to referenced object.
	 * @param timestampsWrapped    Set this flag if fields of timestamp domains are to wrap by timestamp classes of the
	 *                             "corentx.dates" package.
	 * @return The name of the Java type.
	 */
	public String getJavaType(ColumnModel column, boolean referencesToKeyClass, boolean timestampsWrapped) {
		if (column.getDomain().getDataType() == Types.TIMESTAMP) {
			this.imports.add("java.sql");
		} else if (!referencesToKeyClass && (column.getReferencedTable() != null)) {
			if (this.getAlternateReferenceTableClassName(column.getReferencedTable()) != null) {
				return this.getAlternateReferenceTableClassName(column.getReferencedTable());
			}
			return this.getClassName(column.getReferencedTable());
		}
		return this.typeUtil.getJavaType(column, referencesToKeyClass, timestampsWrapped, this.isEntityGenerator(),
				this.parameterUtil, this.generatorUtil, this.imports);
	}

	/**
	 * Creates a key class constructor call for the passed table.
	 *
	 * @param tm                    The table which the key class constructor call is to create for.
	 * @param keyClassNameGenerator A generator for the key class name.
	 * @return A key class constructor call for the passed table.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 29.04.2016 - Added.
	 */
	protected String getKeyClassConstructorCall(TableModel tm, KeyClassNameGenerator keyClassNameGenerator) {
		Checks.ensure(keyClassNameGenerator != null, "key class name generator cannot be null.");
		Checks.ensure(tm != null, "table cannot be null.");
		String a = "";
		String s = "super(new " + keyClassNameGenerator.getName(tm) + "(";
		for (ColumnModel c : tm.getColumns()) {
			if (c.isPrimaryKey()) {
				if (a.length() > 0) {
					a += ", ";
				}
				a += this.generatorUtil.getAttributeName(c);
			}
		}
		return s + a + ")";
	}

	/**
	 * Can be overwritten to define marks which have to be necessarily set into the parameters of a table to include the
	 * code generator to the generation process.
	 * 
	 * @return An array of necessary inclusion marks or an empty array if no marks are defined.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	public String[] getNecessaryIncludeMarks() {
		return new String[0];
	}

	/**
	 * Returns the name of the package which the class is to create into by the code generator.
	 *
	 * @param tm The table model whose the package name should be returned.
	 * @return The name of the package which the class is to create into by the code generator.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	abstract public String getPackageName(TableModel tm);

	/**
	 * Returns a list with the parameters from the passed column.
	 *
	 * @param c The column whose parameters are to return.
	 * @return A list with the parameters from the passed column.
	 *
	 * @changed OLI 10.10.2013 - Added.
	 */
	@Deprecated
	public List<String> getParameters(ColumnModel c) {
		return this.parameterUtil.getParameters(c);
	}

	/**
	 * Returns a list with the parameters from the passed model.
	 *
	 * @param m The model whose parameters are to return.
	 * @return A list with the parameters from the passed model.
	 *
	 * @changed OLI 02.07.2014 - Added.
	 */
	@Deprecated
	public List<String> getParameters(DataModel m) {
		return this.parameterUtil.getParameters(m);
	}

	/**
	 * Returns a list with the parameters from the passed domain.
	 *
	 * @param d The domain whose parameters are to return.
	 * @return A list with the parameters from the passed domain.
	 *
	 * @changed OLI 05.02.2014 - Added.
	 */
	@Deprecated
	public List<String> getParameters(DomainModel d) {
		return this.parameterUtil.getParameters(d);
	}

	/**
	 * Returns a list with the parameters from the passed panel.
	 *
	 * @param p The panel whose parameters are to return.
	 * @return A list with the parameters from the passed panel.
	 *
	 * @changed OLI 18.11.2014 - Added.
	 */
	@Deprecated
	public List<String> getParameters(PanelModel p) {
		return this.parameterUtil.getParameters(p);
	}

	/**
	 * Returns a list with the parameters from the passed string.
	 *
	 * @param s The string whose parameters are to return.
	 * @return A list with the parameters from the passed string.
	 *
	 * @changed OLI 10.10.2013 - Added.
	 */
	@Deprecated
	public List<String> getParameters(String s) {
		return this.parameterUtil.getParameters(s);
	}

	/**
	 * Returns a list with the parameters from the passed table.
	 *
	 * @param t The table whose parameters are to return.
	 * @return A list with the parameters from the passed table.
	 *
	 * @changed OLI 10.10.2013 - Added.
	 */
	@Deprecated
	public List<String> getParameters(TableModel t) {
		return this.parameterUtil.getParameters(t);
	}

	/**
	 * Returns the reference to the parent table if there is one.
	 * 
	 * @param table The table whose parent reference is to return.
	 * @return The fields which are marked as referencing a parent table or an empty array if there is no parent
	 *         reference.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 03.05.2016 - Added.
	 */
	public ColumnModel[] getParentReferences(TableModel table) {
		List<ColumnModel> l = new SortedVector<ColumnModel>();
		for (ColumnModel c : table.getColumns()) {
			if (c.isOptionSet(ColParamIds.PARENT_REF) /* this.isParentReference(c) */) {
				l.add(c);
			}
		}
		return l.toArray(new ColumnModel[0]);
	}

	public ColumnModel getPrimaryKeyColumn(ColumnModel[] columns) {
		ColumnModel[] pks = this.columnUtil.getPrimaryKeyColumns(columns);
		return (pks.length == 1 ? pks[0] : null);
	}

	public ColumnModel getPrimaryKeyColumn(TableModel table) {
		ColumnModel[] pks = this.columnUtil.getPrimaryKeyColumns(table.getColumns());
		return (pks.length >= 1 ? pks[0] : null);
	}

	/**
	 * Returns the list of all primary key columns from the passed list of columns.
	 *
	 * @param columns The list of columns whose primary key columns should be returned.
	 * @return The list of all primary key columns from the passed list.
	 *
	 * @changed OLI 11.10.2013 - Added.
	 */
	@Deprecated
	public ColumnModel[] getPrimaryKeyColumns(ColumnModel[] columns) {
		return this.columnUtil.getPrimaryKeyColumns(columns);
	}

	/**
	 * Returns the list of all primary key columns from the passed table.
	 *
	 * @param table The table whose primary key columns are to return.
	 * @return The list of all primary key columns from the passed table.
	 *
	 * @changed OLI 11.10.2013 - Added.
	 */
	public ColumnModel[] getPrimaryKeyColumns(TableModel table) {
		return this.getPrimaryKeyColumns(table.getColumns());
	}

	/**
	 * Returns the name of the org.apache.commons.lang.builder package (for EqualsBuilder and other builders).
	 *
	 * @return The name of the org.apache.commons.lang.builder package (for EqualsBuilder and other builders).
	 *
	 * @changed OLI 04.05.2016 - Added.
	 */
	public String getOrgApacheCommonsLangBuilderPackageName() {
		return "org.apache.commons.lang.builder";
	}

	/**
	 * Returns the sub path which points to the source folder.
	 *
	 * @return The sub path which points to the source folder.
	 *
	 * @changed OLI 26.11.2013 - Added.
	 */
	abstract public String getSrcSubPath();

	/**
	 * Returns the name of the sub project folder.
	 *
	 * @param dm An access to the data model which the code is to create for.
	 * @return The name of the sub project folder or an empty string if there is no special sub project folder.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	abstract public String getSubProjectFolder(DataModel dm);

	/**
	 * Returns a sub project suffix if there is one or an empty string if there is non.
	 *
	 * @param model The data model which the sub project suffix is generated for.
	 * @param tm    The table model which the code is created for.
	 * @return A sub project suffix if there is one or an empty string if there is non.
	 *
	 * @changed OLI 29.09.2013 - Added.
	 */
	abstract public String getSubProjectSuffix(DataModel model, TableModel tm);

	/**
	 * @changed OLI 16.10.2013 - Added.
	 */
	@Override
	public boolean isTemporarilySuspended() {
		return this.temporarilySuspended;
	}

	/**
	 * Checks if the passed code for the passed file has been changed by the code generator.
	 * 
	 * @param fileName              The name whose content is to compare with the passed code.
	 * @param code                  The code which is to compare with the content of the passed file.
	 * @param individualPreferences The user individual preferences.
	 * @param sourceFileReader      An access to the persisted code files.
	 * @return <CODE>true</CODE> if the passed code differs from the content of the code file.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	@Deprecated
	public boolean hasBeenChanged(String fileName, String code, IndividualPreferences individualPreferences,
			SourceFileReader sourceFileReader) {
		return this.changedChecker.hasBeenChanged(fileName, code, individualPreferences, sourceFileReader);
	}

	/**
	 * Returns a string with "Type Name" for attribute definitions.
	 *
	 * @param c The column which the type and name is created for.
	 * @return A string with "Type Name" for attribute definitions.
	 *
	 * @changed OLI 28.04.2016 - Added.
	 */
	public String getTypeAndName(ColumnModel c) {
		return this.getTypeAndName(c, false, false);
	}

	/**
	 * Returns a string with "Type Name" for attribute definitions.
	 *
	 * @param c                    The column which the type and name is created for.
	 * @param referencesToKeyClass Set this flag if references have to be changed to class references.
	 * @param timestampsWrapped    Set this flag if timestamp type should be wrapped into timestamp classes.
	 * @return A string with "Type Name" for attribute definitions.
	 *
	 * @changed OLI 28.04.2016 - Added.
	 */
	public String getTypeAndName(ColumnModel c, boolean referencesToKeyClass, boolean timestampsWrapped) {
		return this.getJavaType(c, referencesToKeyClass, timestampsWrapped) + " "
				+ this.generatorUtil.getAttributeName(c.getName().replace(".", ""));
	}

	/**
	 * Returns the wrapper class name if there is only.
	 *
	 * @param cn The class name whose wrapper class is to return.
	 * @return The wrapper class name if there is only.
	 *
	 * @changed OLI 20.11.2013 - Added.
	 *
	 * @deprecated OLI 28.04.2016
	 */
	@Deprecated
	public String getWrapperClass(String cn) {
		return this.typeUtil.getWrapperClass(cn);
	}

	/**
	 * Checks if the columns include at least one timestamp field.
	 *
	 * @return <CODE>true</CODE> if the table has at least one timestamp field.
	 *
	 * @changed OLI 11.10.2013 - Added.
	 */
	public boolean hasTimestampField(ColumnModel[] columns) {
		for (ColumnModel c : columns) {
			if (this.isTimestamp(c.getDomain())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the table has at least one timestamp field.
	 *
	 * @return <CODE>true</CODE> if the table has at least one timestamp field.
	 *
	 * @changed OLI 11.10.2013 - Added.
	 */
	public boolean hasTimestampField(TableModel tm) {
		return this.hasTimestampField(tm.getColumns());
	}

	/**
	 * Generators whose classes should never be provided to special API have to override this method and return
	 * <CODE>false</CODE>.
	 *
	 * @return If <CODE>false</CODE> is returned the generator will never provide any code for the special API.
	 *
	 * @changed OLI 22.01.2014 - Added.
	 */
	public boolean isAllowedForSpecialAPI() {
		return true;
	}

	/**
	 * Checks if the content generated by the code generator is to append to the existing file (otherwise it overrides
	 * the an existing file).
	 *
	 * @return <CODE>true</CODE> if the content generated by the code generator is to append to the existing file
	 *         (otherwise it overrides the an existing file; this is the default).
	 *
	 * @changed OLI 26.11.2013 - Added.
	 */
	public boolean isAppendingNewContent() {
		return false;
	}

	/**
	 * Checks if the file with the passed name is changed.
	 *
	 * @param fileName The name to check.
	 * @return <CODE>true</CODE> if the file is changed.
	 *
	 * @changed OLI 29.09.2013 - Added.
	 */
	public boolean isCodeChanged(String fileName) {
		return this.changedChecker.isCodeChanged(fileName);
	}

	/**
	 * Checks if a table is marked as DEPENDENT.
	 *
	 * @param t The table to check.
	 * @return <CODE>true</CODE> if the table is marked as dependent.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 03.05.2016 - Added.
	 */
	public boolean isDependent(TableModel t) {
		return t.isOptionSet(TableParamIds.DEPENDENT);
	}

	/**
	 * Checks if the code generator is deprecated. The method can be overwritten by a code generator which is not longer
	 * in use. The files which would be generated are shown in the the generation report if existing.
	 *
	 * @return <CODE>true</CODE> if the code generator is deprecated.
	 *
	 * @changed OLI 01.04.2016 - Added.
	 */
	@Override
	public boolean isDeprecated() {
		return false;
	}

	/**
	 * Should be overriden for entity generators.
	 *
	 * @return <CODE>true</CODE> if the generator creates the entity classes.
	 *
	 * @changed OLI 28.04.2016 - Added.
	 */
	public boolean isEntityGenerator() {
		return false;
	}

	/**
	 * Checks if the column is of an enum domain.
	 *
	 * @param column The column to check.
	 * @return <CODE>true</CODE> if the passed column is of an enum domain.
	 *
	 * @changed OLI 01.04.2016 - Added.
	 */
	public boolean isEnum(ColumnModel column) {
		Checks.ensure(column != null, "column to check cannot be null.");
		return this.isEnum(column.getDomain());
	}

	/**
	 * Checks if the domain is an enum.
	 *
	 * @param domain The domain to check.
	 * @return <CODE>true</CODE> if the passed domain is an enum.
	 *
	 * @changed OLI 01.04.2016 - Added.
	 */
	abstract public boolean isEnum(DomainModel domain);

	/**
	 * Checks if the passed column references a table which is not in the same data model.
	 * 
	 * @param c The column model to check.
	 * @return <CODE>true</CODE> if the passed column references a table which is not in the same data model.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 28.04.2016 - Added.
	 */
	public boolean isExternalRef(ColumnModel c) {
		Checks.ensure(c != null, "column cannot be null.");
		return (c.getReferencedTable() != null) && this.isExternalTable(c.getReferencedTable(), c.getTable());
	}

	/**
	 * Checks if the table is not in the same data model.
	 *
	 * <P>
	 * <I><B>Note:</B> The method should be overwritten in sub classes.
	 *
	 * @param t          The table to check.
	 * @param moduleName A module name to check.
	 * @return <CODE>true</CODE> if the table is not part of the data model (module with the passed name)
	 * @throws IllegalArgumentException Passing a null pointer as table.
	 *
	 * @changed OLI 28.04.2016 - Added.
	 */
	public boolean isExternalTable(TableModel t, String moduleName) {
		return t.isExternalTable();
	}

	/**
	 * Checks if the table is not in the same data model.
	 *
	 * <P>
	 * <I><B>Note:</B> The method should be overwritten in sub classes.
	 *
	 * @param t  The table to check.
	 * @param tm Another whose module is matched against those of the passed table.
	 * @return <CODE>true</CODE> if the table is not part of the data model (module of the second table)
	 * @throws IllegalArgumentException Passing a null pointer as table.
	 *
	 * @changed OLI 28.04.2016 - Added.
	 */
	public boolean isExternalTable(TableModel t, TableModel tm) {
		return t.isExternalTable();
	}

	public boolean isIgnoredForEmbedded(ColumnModel c) {
		if (c.getName().equals(this.parameterUtil.getValueParameterStartsWith(TableParamIds.ORDER_BY, c.getTable()))) {
			return true;
		}
		if (c.isOptionSet(ColParamIds.EMBEDDED_REFERENCE)
				&& !c.getTable().getDataModel().isOptionSet(ModelParamIds.EMBEDDED_SETTERS_PROTECTED)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the passed column is a parent reference.
	 * 
	 * @param c The column to check.
	 * @return <CODE>true</CODE> if the passed column is a parent reference.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 03.05.2016 - Added.
	 */
	public boolean isParentReference(ColumnModel c) {
		Checks.ensure(c != null, "column cannot be null.");
		return c.getOptionByName(ColParamIds.PARENT_REF) != null;
	}

	/**
	 * Checks if the passed column is linked to a table which is not part of the model.
	 *
	 * @param c The column to check.
	 * @return <CODE>true</CODE> if the passed column is linked to a table which is not part of the model.
	 *
	 * @changed OLI 28.04.2016 - Added.
	 */
	public boolean isRefInModel(ColumnModel c) {
		return (c.getReferencedTable() != null) && !this.isExternalTable(c.getReferencedTable(), c.getTable());
	}

	/**
	 * Checks if the passed type name is a simple type.
	 *
	 * @param typeName The name of the type to check.
	 * @return <CODE>true</CODE> if the passed type name is one of a simple type.
	 *
	 * @changed OLI 10.02.2016 - Added.
	 */
	public boolean isSimpleType(String typeName) {
		return this.simpleTypeChecker.isSimpleType(typeName);
	}

	/**
	 * Checks for special API generation.
	 *
	 * @param tm The table model which the check is done for.
	 * @return <CODE>true</CODE> if the code generator creates code for a special API (maybe more visible or something
	 *         like that).
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	public boolean isSpecialAPI(TableModel tm) {
		return false;
	}

	/**
	 * Override this method to define conditions for generation suppression.
	 *
	 * @param dm       The data model which the suppression of generation is to define for.
	 * @param tm       The table model which the suppression of generation is to define for.
	 * @param codeUtil A code utility.
	 * @return <CODE>false</CODE> if the code generation is not to suppress for the table, otherwise <CODE>true</CODE>.
	 *
	 * @changed OLI 04.04.2016 - Added.
	 */
	public boolean isSuppressGeneration(DataModel dm, TableModel tm, CodeUtil codeUtil) {
		return false;
	}

	/**
	 * Checks if the column is of a timestamp domain.
	 *
	 * @param column The column to check.
	 * @return <CODE>true</CODE> if the passed column is of a timestamp domain.
	 *
	 * @changed OLI 01.04.2016 - Added.
	 */
	public boolean isTimestamp(ColumnModel column) {
		Checks.ensure(column != null, "column to check cannot be null.");
		return this.isTimestamp(column.getDomain());
	}

	/**
	 * Checks if the domain is a timestamp.
	 *
	 * @param domain The domain to check.
	 * @return <CODE>true</CODE> if the passed domain is a timestamp.
	 *
	 * @changed OLI 01.04.2016 - Added.
	 */
	public boolean isTimestamp(DomainModel dm) {
		return this.typeUtil.isTimestampDomain(dm);
	}

	/**
	 * Sets the quotes for the passed string and adds a plus if not empty.
	 *
	 * @param s The string to quote.
	 * @return A string with the quoted passed string (and a plus character if not empty).
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public String quoteAndPlusPrefixIfNotEmpty(String s) {
		if ((s != null) && !s.isEmpty()) {
			return "\"" + s + "\" + ";
		}
		return "";
	}

	/**
	 * Sets the quotes for the passed string and adds a plus character before if not empty.
	 *
	 * @param s The string to quote.
	 * @return A string with the quoted passed string (and a plus character if not empty).
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public String quoteAndPlusSuffixIfNotEmpty(String s) {
		if ((s != null) && !s.isEmpty()) {
			return " + \"" + s + "\"";
		}
		return "";
	}

	/**
	 * @changed OLI 05.04.2016 - Added.
	 */
	@Override
	public void setIndividualPreferences(IndividualPreferences individualPreferences) {
		this.individualPreferences = individualPreferences;
	}

	/**
	 * @changed OLI 16.10.2013 - Added.
	 */
	@Override
	public void setTemporarilySuspended(boolean suspended) {
		this.temporarilySuspended = suspended;
	}

	/**
	 * @changed OLI 22.04.2016 - Added.
	 */
	@Override
	public void setUnchangedByTagFileInfo(UnchangedByTagChecker unchangedByTagChecker) {
		Checks.ensure(unchangedByTagChecker != null, "unchanged by tag checker cannot be null.");
		this.unchangedByTagChecker = unchangedByTagChecker;
	}

	/**
	 * Stores the passed method in the method storage.
	 *
	 * @param name The name of the method.
	 * @param code The code of the method.
	 * @throws IllegalArgumentException Passing a null pointer or an empty string.
	 *
	 * @changed OLI 28.04.2016 - Added.
	 */
	public void storeMethod(String name, String code) {
		this.storeMethod(name, code, false);
	}

	/**
	 * Stores the passed method in the method storage.
	 *
	 * @param name         The name of the method.
	 * @param code         The code of the method.
	 * @param staticMethod Set this flag if the method should be a static one.
	 * @throws IllegalArgumentException Passing a null pointer or an empty string.
	 *
	 * @changed OLI 28.06.2016 - Added.
	 */
	public void storeMethod(String name, String code, boolean staticMethod) {
		this.methodCodeStorage.put(name, new MethodCode(name, code, staticMethod));
	}

}