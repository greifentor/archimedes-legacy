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
							new TypeInfo(Types.BIT, "BIT"),
							new TypeInfo(Types.BOOLEAN, "BOOLEAN"),
							new TypeInfo(Types.NUMERIC, "NUMERIC"),
							new TypeInfo(Types.OTHER, "OTHER"),
							new TypeInfo(Types.VARCHAR, "VARCHAR"));

	public int getSQLType(String type) {
		return getType(type).map(TypeInfo::getSqlType).orElse(Types.OTHER);
	}

	private Optional<TypeInfo> getType(String type) {
		return TYPES
				.stream()
				.filter(typeInfo -> type.toUpperCase().contains(typeInfo.getName()))
				.findFirst();
	}

	public Integer getLength(String type) {
		return getType(type)
				.filter(typeInfo -> type.toUpperCase().replace(typeInfo.getName(), "").startsWith("("))
				.map(typeInfo -> 1)
				.orElse(-1);
	}

	public Integer getPrecision(String type) {
		return getType(type)
				.filter(typeInfo -> type.toUpperCase().replace(typeInfo.getName(), "").startsWith("("))
				.map(typeInfo -> 1)
				.orElse(-1);
	}

}