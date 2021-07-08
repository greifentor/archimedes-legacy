package archimedes.legacy.importer.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Types;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.archimedes.alexandrian.service.so.TypeSO;

@ExtendWith(MockitoExtension.class)
public class DBTypeConverterTest {

	@InjectMocks
	private DBTypeConverter unitUnderTest;

	@Test
	void passUnknownTypeId_throwsAnException() {
		assertThrows(IllegalArgumentException.class, () -> unitUnderTest.convert(Integer.MIN_VALUE, -1, -1));
	}

	@ParameterizedTest
	@CsvSource(
			value = {
					Types.BIGINT + ",0,0",
					Types.BINARY + ",0,0",
					Types.BIT + ",0,0",
					Types.BLOB + ",0,0",
					Types.BOOLEAN + ",0,0",
					Types.CHAR + ",42,0",
					Types.CLOB + ",0,0",
					Types.DATE + ",0,0",
					Types.DECIMAL + ",42,7",
					Types.LONGVARCHAR + ",0,0",
					Types.NUMERIC + ",42,7",
					Types.OTHER + ",0,0",
					Types.ROWID + ",0,0",
					Types.TIMESTAMP + ",0,0",
					Types.VARCHAR + ",42,0" })
	void passCorrectTypeData_returnsACorrectTypeSO(int sqlType, int length, int precision) {
		// Prepare
		TypeSO expected = new TypeSO().setSqlType(sqlType);
		if (length > 0) {
			expected.setLength(length);
		}
		if (precision > 0) {
			expected.setPrecision(precision);
		}
		// Run
		TypeSO returned = unitUnderTest.convert(sqlType, length, precision);
		// Check
		assertEquals(expected, returned);
	}

}
