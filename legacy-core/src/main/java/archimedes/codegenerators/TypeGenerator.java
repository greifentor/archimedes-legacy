package archimedes.codegenerators;

import java.sql.Types;

import archimedes.model.DomainModel;

/**
 * A generator for java types.
 *
 * @author ollie (12.03.2021)
 */
public class TypeGenerator {

	public String getJavaTypeString(DomainModel domain, boolean nullable) {
		if (domain == null) {
			return null;
		}
		if (domain.getDataType() == Types.BIGINT) {
			return getTypeRespectNullable(nullable, "long", "Long");
		} else if ((domain.getDataType() == Types.BIT) || (domain.getDataType() == Types.BOOLEAN)) {
			return getTypeRespectNullable(nullable, "boolean", "Boolean");
		} else if ((domain.getDataType() == Types.CHAR) || (domain.getDataType() == Types.LONGNVARCHAR)
				|| (domain.getDataType() == Types.LONGVARCHAR) || (domain.getDataType() == Types.NCHAR)
				|| (domain.getDataType() == Types.NVARCHAR) || (domain.getDataType() == Types.VARCHAR)) {
			if (domain.getLength() == 1) {
				return getTypeRespectNullable(nullable, "char", "Character");
			}
			return "String";
		} else if (domain.getDataType() == Types.DATE) {
			return "LocalDate";
		} else if ((domain.getDataType() == Types.DECIMAL) || (domain.getDataType() == Types.DOUBLE)
				|| (domain.getDataType() == Types.NUMERIC) || (domain.getDataType() == Types.REAL)) {
			return getTypeRespectNullable(nullable, "double", "Double");
		} else if (domain.getDataType() == Types.FLOAT) {
			return getTypeRespectNullable(nullable, "float", "Float");
		} else if (domain.getDataType() == Types.INTEGER) {
			return getTypeRespectNullable(nullable, "int", "Integer");
		} else if (domain.getDataType() == Types.SMALLINT) {
			return getTypeRespectNullable(nullable, "short", "Short");
		} else if (domain.getDataType() == Types.TIME) {
			return "LocalTime";
		} else if (domain.getDataType() == Types.TIMESTAMP) {
			return "LocalDateTime";
		} else if (domain.getDataType() == Types.TIMESTAMP_WITH_TIMEZONE) {
			return "ZonedDateTime";
		} else if (domain.getDataType() == Types.TINYINT) {
			return getTypeRespectNullable(nullable, "byte", "Byte");
		}
		throw new IllegalArgumentException("type " + domain.getDataType() + " cannot be converted to a Java type.");
	}

	private String getTypeRespectNullable(boolean nullable, String simpleTypeName, String wrapperTypeName) {
		if (nullable) {
			return wrapperTypeName;
		}
		return simpleTypeName;
	}

}