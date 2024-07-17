package archimedes.codegenerators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Types;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TypesUtilTest {

	@InjectMocks
	private TypesUtil unitUnderTest;

	@Nested
	class TestsOfMethod_isNumericType_int {

		@ParameterizedTest
		@CsvSource({
				"" + Types.BIGINT,
				"" + Types.DECIMAL,
				"" + Types.DOUBLE,
				"" + Types.FLOAT,
				"" + Types.INTEGER,
				"" + Types.NUMERIC,
				"" + Types.REAL,
				"" + Types.SMALLINT,
				"" + Types.TINYINT })
		void returnsTrue_passingANumericTypeIdentifier(int type) {
			assertTrue(unitUnderTest.isNumericType(type));
		}

		@ParameterizedTest
		@CsvSource({
				"" + Types.ARRAY,
				"" + Types.BINARY,
				"" + Types.BIT,
				"" + Types.BLOB,
				"" + Types.BOOLEAN,
				"" + Types.CHAR,
				"" + Types.CLOB,
				"" + Types.DATALINK,
				"" + Types.DATE,
				"" + Types.DISTINCT,
				"" + Types.JAVA_OBJECT,
				"" + Types.LONGNVARCHAR,
				"" + Types.LONGVARBINARY,
				"" + Types.LONGVARCHAR,
				"" + Types.NCHAR,
				"" + Types.NCHAR,
				"" + Types.NULL,
				"" + Types.NVARCHAR,
				"" + Types.OTHER,
				"" + Types.REF,
				"" + Types.REF_CURSOR,
				"" + Types.ROWID,
				"" + Types.SQLXML,
				"" + Types.STRUCT,
				"" + Types.TIME,
				"" + Types.TIMESTAMP,
				"" + Types.TIMESTAMP_WITH_TIMEZONE,
				"" + Types.VARBINARY,
				"" + Types.VARCHAR })
		void returnsFalse_passingANonNumericTypeIdentifier(int type) {
			assertFalse(unitUnderTest.isNumericType(type));
		}

	}

}
