package archimedes.legacy.updater;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Types;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TypeConverterTest {

	private static final int LENGTH = 42;
	private static final int PRECISION = 7;
	private static final char DELIMITER = ';';

	private static final String BIGINT_SQL_TYPE = "BIGINT";
	private static final String BIT_SQL_TYPE = "BIT";
	private static final String BOOLEAN_SQL_TYPE = "BOOLEAN";
	private static final String LONGVARCHAR_SQL_TYPE = "LONGVARCHAR";
	private static final String NUMERIC_SQL_TYPE = "NUMERIC(" + LENGTH + ", " + PRECISION + ")";
	private static final String VARCHAR_SQL_TYPE = "VARCHAR(" + LENGTH + ")";

	@InjectMocks
	private TypeConverter unitUnderTest;

	@Nested
	class TestsOfMethod_getSQLType_String {

		@ParameterizedTest
		@CsvSource(delimiter = DELIMITER, value = {
				BIGINT_SQL_TYPE + DELIMITER + Types.BIGINT,
				BIT_SQL_TYPE + DELIMITER + Types.BIT,
				BOOLEAN_SQL_TYPE + DELIMITER + Types.BOOLEAN,
				LONGVARCHAR_SQL_TYPE + DELIMITER + Types.LONGVARCHAR,
				NUMERIC_SQL_TYPE + DELIMITER + Types.NUMERIC,
				VARCHAR_SQL_TYPE + DELIMITER + Types.VARCHAR })
		void passAValidTypeName_ReturnsTheMatchingTypesValue(String typeName, int expected) {
			assertEquals(expected, unitUnderTest.getSQLType(typeName));
		}

	}

	@Nested
	class TestsOfMethod_getLength_String {

		@ParameterizedTest
		@CsvSource(delimiter = DELIMITER, value = {
				BIGINT_SQL_TYPE + DELIMITER + "null",
				BIT_SQL_TYPE + DELIMITER + "null",
				BOOLEAN_SQL_TYPE + DELIMITER + "null",
				LONGVARCHAR_SQL_TYPE + DELIMITER + "null",
				NUMERIC_SQL_TYPE + DELIMITER + LENGTH,
				VARCHAR_SQL_TYPE + DELIMITER + LENGTH })
		void passAValidTypeName_ReturnsTheMatchingTypesValue(String typeName,
				@ConvertWith(NullableConverter.class) Integer expected) {
			assertEquals(expected, unitUnderTest.getLength(typeName));
		}

	}

	@Nested
	class TestsOfMethod_getPrecision_String {

		@ParameterizedTest
		@CsvSource(delimiter = DELIMITER, value = {
				BIGINT_SQL_TYPE + DELIMITER + "null",
				BIT_SQL_TYPE + DELIMITER + "null",
				BOOLEAN_SQL_TYPE + DELIMITER + "null",
				LONGVARCHAR_SQL_TYPE + DELIMITER + "null",
				NUMERIC_SQL_TYPE + DELIMITER + PRECISION,
				VARCHAR_SQL_TYPE + DELIMITER + "null" })
		void passAValidTypeName_ReturnsTheMatchingTypesValue(String typeName,
				@ConvertWith(NullableConverter.class) Integer expected) {
			assertEquals(expected, unitUnderTest.getPrecision(typeName));
		}

	}

}

class NullableConverter extends SimpleArgumentConverter {

	@Override
	protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
		if ("null".equals(source)) {
			return null;
		}
		return DefaultArgumentConverter.INSTANCE.convert(source, targetType);
	}

}