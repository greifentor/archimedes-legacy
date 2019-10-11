package archimedes.legacy.importer.jdbc;

import java.sql.Types;

import de.ollie.archimedes.alexandrian.service.so.TypeSO;

/**
 * A converter for Types to DBType and vice versa.
 *
 * @author Oliver.Lieshoff
 *
 */
public class DBTypeConverter {

	/**
	 * Converts the passed Types value to an DBType enum.
	 * 
	 * @param dataType A Types value.
	 * @return A DBType for the passed Types constant.
	 * @throws IllegalArgumentException Passing an unknown value.
	 */
	public TypeSO convert(int dataType, int length, int precision) {
		if (dataType == Types.BIGINT) {
			return new TypeSO().setSqlType(dataType);
		} else if (dataType == Types.BIT) {
			return new TypeSO().setSqlType(dataType);
		} else if (dataType == Types.BLOB) {
			return new TypeSO().setSqlType(dataType);
		} else if (dataType == Types.CHAR) {
			return new TypeSO().setSqlType(dataType).setLength(length);
		} else if (dataType == Types.CLOB) {
			return new TypeSO().setSqlType(dataType);
		} else if (dataType == Types.DATE) {
			return new TypeSO().setSqlType(dataType);
		} else if (dataType == Types.DECIMAL) {
			return new TypeSO().setSqlType(dataType).setLength(length).setPrecision(precision);
		} else if (dataType == Types.INTEGER) {
			return new TypeSO().setSqlType(dataType);
		} else if (dataType == -1) {
			return new TypeSO().setSqlType(dataType);
		} else if (dataType == Types.NUMERIC) {
			return new TypeSO().setSqlType(dataType).setLength(length).setPrecision(precision);
		} else if (dataType == 1111) {
			return new TypeSO().setSqlType(dataType);
		} else if (dataType == Types.ROWID) {
			return new TypeSO().setSqlType(dataType);
		} else if (dataType == Types.TIMESTAMP) {
			return new TypeSO().setSqlType(dataType);
		} else if (dataType == Types.VARCHAR) {
			return new TypeSO().setSqlType(dataType).setLength(length);
		}
		throw new IllegalArgumentException("there is no mapping for data type value: " + dataType);
	}

}