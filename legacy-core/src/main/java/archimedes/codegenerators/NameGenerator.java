package archimedes.codegenerators;

import static de.ollie.dbcomp.util.Check.ensure;

import org.apache.commons.lang3.StringUtils;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class which is responsible for name generation during code is generated.
 *
 * @author ollie (12.03.2021)
 */
public class NameGenerator {

	public String getAttributeName(ColumnModel column) {
		return getAttributeName(column, false);
	}

	public String getAttributeName(ColumnModel column, boolean useQualifiedColumnName) {
		if (column == null) {
			return null;
		}
		String columnName = (useQualifiedColumnName ? column.getTable().getName() + "_" : "") + column.getName();
		ensure(!columnName.isEmpty(), "column name cannot be empty.");
		if (containsUnderScores(columnName)) {
			columnName = buildTableNameFromUnderScoreString(columnName);
		} else if (allCharactersAreUpperCase(columnName)) {
			columnName = columnName.toLowerCase();
		}
		if (startsWithUpperCaseCharacter(columnName)) {
			columnName = firstCharToLowerCase(columnName);
		}
		return columnName;
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

	public String getDBOClassName(TableModel table) {
		return table != null ? getClassName(table) + "DBO" : null;
	}

	protected String getBasePackageNameWithDotExtension(DataModel model) {
		return (model.getBasePackageName() == null) || model.getBasePackageName().isEmpty()
				? ""
				: model.getBasePackageName() + ".";
	}

}