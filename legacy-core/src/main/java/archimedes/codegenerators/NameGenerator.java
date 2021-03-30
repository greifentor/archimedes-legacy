package archimedes.codegenerators;

import static de.ollie.dbcomp.util.Check.ensure;

import org.apache.commons.lang3.StringUtils;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * A class which is responsible for name generation during code is generated.
 *
 * @author ollie (12.03.2021)
 */
public class NameGenerator {

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

	protected String getAttributeName(String s) {
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
		return firstChar.equals(firstChar.toUpperCase());
	}

	private String firstCharToLowerCase(String s) {
		return StringUtils.left(s, 1).toLowerCase() + (s.length() > 1 ? s.substring(1) : "");
	}

	public String getClassName(TableModel tableSO) {
		String tableName = tableSO.getName();
		ensure(!tableName.isEmpty(), "table name cannot be empty.");
		if (containsUnderScores(tableName)) {
			tableName = buildTableNameFromUnderScoreString(tableName);
		} else if (allCharactersAreUpperCase(tableName)) {
			tableName = tableName.toLowerCase();
		}
		if (startsWithLowerCaseCharacter(tableName)) {
			tableName = firstCharToUpperCase(tableName);
		}
		return tableName;
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

	protected String getBasePackageNameWithDotExtension(DataModel model, TableModel table) {
		String technicalContextName = getTechnicalContextName(table);
		return ((model.getBasePackageName() == null) || model.getBasePackageName().isEmpty()
				? ""
				: model.getBasePackageName() + ".")
				+ (technicalContextName.isEmpty() ? "" : technicalContextName + ".");
	}

	private String getTechnicalContextName(TableModel table) {
		return table == null
				? ""
				: OptionGetter.getOptionByName(table, TECHNICAL_CONTEXT).map(OptionModel::getParameter).orElse("");
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

}