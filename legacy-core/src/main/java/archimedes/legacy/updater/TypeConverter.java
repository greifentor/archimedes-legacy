package archimedes.legacy.updater;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;

public class TypeConverter {

	@AllArgsConstructor
	@Data
	private static class TypeInfo {
		private int sqlType;
		private String name;
	}

	private static final List<TypeInfo> TYPES =
			Arrays
					.asList(
							new TypeInfo(Types.BIGINT, "BIGINT"),
							new TypeInfo(Types.BINARY, "BINARY"),
							new TypeInfo(Types.BIT, "BIT"),
							new TypeInfo(Types.BLOB, "BLOB"),
							new TypeInfo(Types.BOOLEAN, "BOOLEAN"),
							new TypeInfo(Types.DATE, "DATE"),
							new TypeInfo(Types.DECIMAL, "DECIMAL"),
							new TypeInfo(Types.DOUBLE, "DOUBLE"),
							new TypeInfo(Types.FLOAT, "FLOAT"),
							new TypeInfo(Types.INTEGER, "INTEGER"),
							new TypeInfo(Types.LONGVARBINARY, "LONGVARBINARY"),
							new TypeInfo(Types.LONGVARCHAR, "LONGVARCHAR"),
							new TypeInfo(Types.NUMERIC, "NUMERIC"),
							new TypeInfo(Types.OTHER, "OTHER"),
							new TypeInfo(Types.REAL, "REAL"),
							new TypeInfo(Types.SMALLINT, "SMALLINT"),
							new TypeInfo(Types.TIMESTAMP, "TIMESTAMP"),
							new TypeInfo(Types.TIME, "TIME"),
							new TypeInfo(Types.TINYINT, "TINYINT"),
							new TypeInfo(Types.VARBINARY, "VARBINARY"),
							new TypeInfo(Types.VARCHAR, "VARCHAR"),
							new TypeInfo(Types.CHAR, "CHAR"));

	public int getSQLType(String type) {
		return getType(type).map(TypeInfo::getSqlType).orElse(Types.OTHER);
	}

	private Optional<TypeInfo> getType(String s) {
		return TYPES.stream().filter(typeInfo -> s.toUpperCase().contains(typeInfo.getName())).findFirst();
	}

	public Integer getLength(String type) {
		return getType(type)
				.map(typeInfo -> type.toUpperCase().replace(" ", "").replace(typeInfo.getName(), ""))
				.filter(t -> t.startsWith("("))
				.map(t -> Integer.valueOf(extractFirstNumber(t)))
				.orElse(null);
	}

	private String extractFirstNumber(String s) {
		s = s.replace("(", "");
		int i = s.indexOf(',');
		if (i > 0) {
			return s.substring(0, i);
		}
		return s.replace(")", "");
	}

	public Integer getPrecision(String type) {
		return getType(type)
				.map(typeInfo -> type.toUpperCase().replace(" ", "").replace(typeInfo.getName(), ""))
				.filter(t -> t.startsWith("(") && t.contains(","))
				.map(t -> Integer.valueOf(extractSecondNumber(t)))
				.orElse(null);
	}

	private String extractSecondNumber(String s) {
		s = s.replace("(", "");
		int i = s.indexOf(',');
		return s.substring(i + 1, s.length() - 1);
	}

}