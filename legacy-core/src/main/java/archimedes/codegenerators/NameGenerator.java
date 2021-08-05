package archimedes.codegenerators;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;
import org.apache.commons.lang3.StringUtils;

import static de.ollie.dbcomp.util.Check.ensure;

/**
 * A class which is responsible for name generation during code is generated.
 *
 * @author ollie (12.03.2021)
 */
public class NameGenerator {

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
		String columnName = (useQualifiedColumnName
				? column.getTable().getName() + "_"
				: "") + column.getName();
		ensure(!columnName.isEmpty(), "column name cannot be empty.");
		return getAttributeName(columnName);
	}

	public String getAttributeName(String s) {
		if (containsUnderScores(s)) {
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
		return StringUtils.left(s, 1).toLowerCase() + (s.length() > 1
				? s.substring(1)
				: "");
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
		return StringUtils.left(s, 1).toUpperCase() + (s.length() > 1
				? s.substring(1)
				: "");
	}

	protected String getBasePackageNameWithDotExtension(DataModel model, TableModel table) {
		return getBasePackageNameWithDotExtension(model, table, false);
	}

	protected String getBasePackageNameWithDotExtension(DataModel model, TableModel table, boolean followedByEmpty) {
		String technicalContextName = getTechnicalContextName(table);
		return (model.getBasePackageName() == null) || model.getBasePackageName().isEmpty()
				? ""
				: addDotIfNecessary(model.getBasePackageName(), followedByEmpty)
						+ (technicalContextName.isEmpty()
						? ""
						: addDotIfNecessary(technicalContextName, followedByEmpty));
	}

	private String addDotIfNecessary(String s, boolean followedByEmpty) {
		return s + ((s != null) && !s.isEmpty() && !followedByEmpty
				? "."
				: "");
	}

	private String getTechnicalContextName(TableModel table) {
		return table == null
				? ""
				: OptionGetter.getOptionByName(table, TECHNICAL_CONTEXT).map(OptionModel::getParameter).orElse("");
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
		return (c >= 'A') && (c < 'Z');
	}

	private String getFirstCharToLowerCaseOnTwoDoublesPassed(char c0, char c1) {
		return isUpperCase(c0) && isUpperCase(c1)
				? String.valueOf(c0).toLowerCase()
				: String.valueOf(c0);
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

	protected String getNameOrAlternativeFromOption(TableModel table, String defaultName, String alternateOptionName) {
		return (table == null) || (table.getDataModel() == null)
				? defaultName
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), alternateOptionName)
						.map(s -> s)
						.orElse(defaultName);
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
		if (tn.toLowerCase().endsWith("y")) {
			tn = tn.substring(0, tn.length() - 1) + "ies";
		} else {
			tn += "s";
		}
		return tn;
	}

	public String getSimpleName(TableModel table) {
		return table.getName().replace("_", " ").toLowerCase();
	}

	protected String createPackageName(DataModel model, TableModel table, String packageName,
	                                   String alternatePackageNameOption) {
		String prefix = "";
		if ((model != null) && (alternatePackageNameOption != null)) {
			OptionModel option = model.getOptionByName(alternatePackageNameOption);
			if ((option != null) && (option.getParameter() != null) && !option.getParameter().isEmpty()) {
				packageName = option.getParameter();
			}
		}
		if (table != null) {
			prefix =
					OptionGetter.getOptionByName(table, MODULE).map(option -> addDotIfNecessary(option.getParameter(), false))
					.orElse("");
		}
		return model != null
				? getBasePackageNameWithDotExtension(model, table, prefix.isEmpty() && packageName.isEmpty()) + prefix + packageName
				: null;
	}

}