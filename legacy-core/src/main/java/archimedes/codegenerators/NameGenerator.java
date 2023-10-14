package archimedes.codegenerators;

import static de.ollie.dbcomp.util.Check.ensure;

import org.apache.commons.lang3.StringUtils;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionListProvider;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * A class which is responsible for name generation during code is generated.
 *
 * @author ollie (12.03.2021)
 */
public class NameGenerator {

	public static final NameGenerator INSTANCE = new NameGenerator();

	public static final String MODULE = "MODULE";
	public static final String PLURAL_NAME = "PLURAL_NAME";
	public static final String TECHNICAL_CONTEXT = "TECHNICAL_CONTEXT";

	public String getAttributeName(ColumnModel column) {
		return getAttributeName(column, false);
	}

	public String getAttributeName(ColumnModel column, boolean useQualifiedColumnName) {
		if (column == null) {
			return null;
		}
		String columnName = (useQualifiedColumnName ? column.getTable().getName() + "_" : "") + column.getName();
		ensure(!columnName.isEmpty(), "column name cannot be empty.");
		return getAttributeName(columnName);
	}

	public String getAttributeName(String s) {
		if (s == null) {
			return null;
		} else if (containsUnderScores(s)) {
			s = buildTableNameFromUnderScoreString(s);
		} else if (allCharactersAreUpperCase(s)) {
			s = s.toLowerCase();
		}
		if (startsWithUpperCaseCharacter(s)) {
			s = firstCharToLowerCase(s);
		}
		return s;
	}

	private boolean startsWithUpperCaseCharacter(String s) {
		String firstChar = StringUtils.left(s, 1);
		return firstChar.equals(firstChar.toUpperCase()); // NOSONAR OLI: Sonar is wrong with this case.
	}

	private String firstCharToLowerCase(String s) {
		return StringUtils.left(s, 1).toLowerCase() + (s.length() > 1 ? s.substring(1) : "");
	}

	public String getClassName(TableModel tableSO) {
		return getClassName(tableSO.getName());
	}

	public String getClassName(String s) {
		ensure(!s.isEmpty(), "string cannot be empty.");
		if (containsUnderScores(s)) {
			s = buildTableNameFromUnderScoreString(s);
		} else if (allCharactersAreUpperCase(s)) {
			s = s.toLowerCase();
		}
		if (startsWithLowerCaseCharacter(s)) {
			s = firstCharToUpperCase(s);
		}
		return s;
	}

	private boolean containsUnderScores(String s) {
		return s.contains("_");
	}

	private String buildTableNameFromUnderScoreString(String s) {
		StringBuilder name = new StringBuilder();
		String[] parts = StringUtils.split(s, "_");
		for (String p : parts) {
			if (allCharactersAreUpperCase(p)) {
				p = p.toLowerCase();
			}
			p = firstCharToUpperCase(p);
			name.append(p);
		}
		return name.toString();
	}

	private boolean allCharactersAreUpperCase(String s) {
		return s.equals(s.toUpperCase()); // NOSONAR OLI: Should check is all characters are upper case.
	}

	private boolean startsWithLowerCaseCharacter(String s) {
		String firstChar = StringUtils.left(s, 1);
		return firstChar.equals(firstChar.toLowerCase()); // NOSONAR OLI: Should check if first char is a lower case.
	}

	private String firstCharToUpperCase(String s) {
		return StringUtils.left(s, 1).toUpperCase() + (s.length() > 1 ? s.substring(1) : "");
	}

	protected String getBasePackageNameWithDotExtension(DataModel model, OptionListProvider optionListProvider) {
		return getBasePackageNameWithDotExtension(model, optionListProvider, false);
	}

	protected String getBasePackageNameWithDotExtension(DataModel model, OptionListProvider optionListProvider,
			boolean followedByEmpty) {
		String technicalContextName = "";
		if (optionListProvider != null) {
			technicalContextName = getTechnicalContextName(optionListProvider);
		}
		return (model.getBasePackageName() == null) || model.getBasePackageName().isEmpty()
				? ""
				: addDotIfNecessary(model.getBasePackageName(), followedByEmpty) + (technicalContextName.isEmpty()
						? ""
						: addDotIfNecessary(technicalContextName, followedByEmpty));
	}

	private String addDotIfNecessary(String s, boolean followedByEmpty) {
		return s + ((s != null) && !s.isEmpty() && !followedByEmpty ? "." : "");
	}

	private String getTechnicalContextName(OptionListProvider optionListProvider) {
		return optionListProvider == null
				? ""
				: OptionGetter
						.getOptionByName(optionListProvider, TECHNICAL_CONTEXT)
						.map(OptionModel::getParameter)
						.orElse("");
	}

	public String getCamelCase(String s) {
		if (s == null) {
			return null;
		}
		if (s.isEmpty()) {
			return "";
		}
		s = s.replace("_", " ");
		StringBuilder sb = new StringBuilder(s.substring(0, 1).toUpperCase());
		for (int i = 1; i < s.length(); i++) {
			String c = String.valueOf(s.charAt(i));
			sb
					.append(
							(s.charAt(i - 1) == ' ')
									? c.toUpperCase()
									: getFirstCharToLowerCaseOnTwoDoublesPassed(c.charAt(0), s.charAt(i - 1)));
		}
		return sb.toString().replace(" ", "");
	}

	private boolean isUpperCase(char c) {
		return (c >= 'A') && (c <= 'Z');
	}

	private String getFirstCharToLowerCaseOnTwoDoublesPassed(char c0, char c1) {
		return isUpperCase(c0) && isUpperCase(c1) ? String.valueOf(c0).toLowerCase() : String.valueOf(c0);
	}

	public String getDescriptionName(String s) {
		ensure(s != null, "string cannot be null.");
		if (s.isEmpty()) {
			return s;
		}
		s = getCamelCase(s);
		StringBuilder sb = new StringBuilder(s.substring(0, 1));
		for (int i = 1; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isUpperCase(c)) {
				sb.append("_");
			}
			sb.append(c);
		}
		return sb.toString().replace("_", " ").toLowerCase();
	}

	public String getEnumIdentifier(String s) {
		if (s == null) {
			return null;
		}
		if (s.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder(s.substring(0, 1));
		for (int i = 1; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isUpperCase(c) && (s.charAt(i - 1) != ' ') && (s.charAt(i - 1) != '_')
					&& !isUpperCase(s.charAt(i - 1))) {
				sb.append("_");
			}
			sb.append(c);
		}
		return sb.toString().toUpperCase();
	}

	protected String getNameOrAlternativeFromOption(DataModel model, String defaultName, String alternateOptionName) {
		return model == null
				? defaultName
				: OptionGetter.getParameterOfOptionByName(model, alternateOptionName).map(s -> s).orElse(defaultName);
	}

	protected String getNameOrAlternativeFromOption(OptionListProvider optionListProvider, String defaultName,
			String alternateOptionName) {
		return optionListProvider == null
				? defaultName
				: OptionGetter
						.getParameterOfOptionByName(optionListProvider, alternateOptionName)
						.map(s -> s)
						.orElse(defaultName);
	}

	public String getOptionValueOrDefault(DataModel model, TableModel table, String defaultName, String optionName) {
		defaultName = getNameOrAlternativeFromOption(model, defaultName, optionName);
		return table == null ? defaultName : getNameOrAlternativeFromOption(table, defaultName, optionName);
	}

	protected String getPluralName(TableModel table) {
		OptionModel pluralName = table.getOptionByName(PLURAL_NAME);
		if (pluralName != null) {
			return pluralName.getParameter();
		}
		String tn = table.getName();
		return getPluralName(tn);
	}

	protected String getPluralName(String tn) {
		if (tn.length() < 1) {
			return tn;
		}
		if (tn.toLowerCase().endsWith("sh")) {
			tn = tn + "es";
		} else if (tn.toLowerCase().endsWith("y")) {
			tn = tn.substring(0, tn.length() - 1) + "ies";
		} else {
			tn += "s";
		}
		return tn;
	}

	public String getSimpleName(TableModel table) {
		return table.getName().replace("_", " ").toLowerCase();
	}

	protected String createPackageName(DataModel model, OptionListProvider optionListProvider, String packageName,
			String alternatePackageNameOption) {
		String prefix = "";
		if ((model != null) && (alternatePackageNameOption != null)) {
			OptionModel option = model.getOptionByName(alternatePackageNameOption);
			if ((option != null) && (option.getParameter() != null) && !option.getParameter().isEmpty()) {
				packageName = option.getParameter();
			}
		}
		if (optionListProvider != null) {
			prefix = OptionGetter
					.getOptionByName(optionListProvider, MODULE)
					.map(option -> addDotIfNecessary(option.getParameter(), false))
					.orElse("");
		}
		return model != null
				? getBasePackageNameWithDotExtension(
						model,
						optionListProvider,
						prefix.isEmpty() && packageName.isEmpty()) + prefix + packageName
				: null;
	}

	protected String createPackageName(DataModel model, String packageName, String alternatePackageNameOption) {
		if ((model != null) && (alternatePackageNameOption != null)) {
			OptionModel option = model.getOptionByName(alternatePackageNameOption);
			if ((option != null) && (option.getParameter() != null) && !option.getParameter().isEmpty()) {
				packageName = option.getParameter();
			}
		}
		return getBasePackageNameWithDotExtension(model, null, false) + packageName;
	}

}