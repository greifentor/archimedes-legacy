package archimedes.codegenerators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.sql.Types;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.DomainModel;

@ExtendWith(MockitoExtension.class)
class TypeGeneratorTest {

	@Mock
	private DomainModel type;

	@InjectMocks
	private TypeGenerator unitUnderTest;

	@Nested
	class TestsOfMethod_getJavaTypeString_DomainModel_boolean {

		@Test
		void passANullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getJavaTypeString(null, false));
		}

		@Test
		void passDomainModelOfARRAY_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.ARRAY);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfBIGINT_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "long";
			when(type.getDataType()).thenReturn(Types.BIGINT);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfBIGINTNullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Long";
			when(type.getDataType()).thenReturn(Types.BIGINT);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfBINARY_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.BINARY);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		void passDomainModelOfBIT_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Boolean";
			when(type.getDataType()).thenReturn(Types.BIT);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfBLOB_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "byte[]";
			when(type.getDataType()).thenReturn(Types.BLOB);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfBOOLEAN_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "boolean";
			when(type.getDataType()).thenReturn(Types.BOOLEAN);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfBOOLEANNullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Boolean";
			when(type.getDataType()).thenReturn(Types.BOOLEAN);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfCHAR1_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "char";
			when(type.getDataType()).thenReturn(Types.CHAR);
			when(type.getLength()).thenReturn(1);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Character";
			when(type.getDataType()).thenReturn(Types.CHAR);
			when(type.getLength()).thenReturn(1);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfCHAR50_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "String";
			when(type.getDataType()).thenReturn(Types.CHAR);
			when(type.getLength()).thenReturn(50);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "String";
			when(type.getDataType()).thenReturn(Types.CHAR);
			when(type.getLength()).thenReturn(50);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfCLOB_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.CLOB);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfDATALINK_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.DATALINK);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfDATE_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "LocalDate";
			when(type.getDataType()).thenReturn(Types.DATE);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfDATENullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "LocalDate";
			when(type.getDataType()).thenReturn(Types.DATE);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfDECIMAL_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "double";
			when(type.getDataType()).thenReturn(Types.DECIMAL);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfDECIMALNullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Double";
			when(type.getDataType()).thenReturn(Types.DECIMAL);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfDISTINCT_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.DISTINCT);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfDOUBLE_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "double";
			when(type.getDataType()).thenReturn(Types.DOUBLE);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfDOUBLENullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Double";
			when(type.getDataType()).thenReturn(Types.DOUBLE);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfFLOAT_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "float";
			when(type.getDataType()).thenReturn(Types.FLOAT);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfFLOATNullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Float";
			when(type.getDataType()).thenReturn(Types.FLOAT);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfINTEGER_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "int";
			when(type.getDataType()).thenReturn(Types.INTEGER);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfINTEGERNullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Integer";
			when(type.getDataType()).thenReturn(Types.INTEGER);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfJAVA_OBJECT_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.JAVA_OBJECT);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfLONGNVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "char";
			when(type.getDataType()).thenReturn(Types.LONGNVARCHAR);
			when(type.getLength()).thenReturn(1);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfLONGNVARCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Character";
			when(type.getDataType()).thenReturn(Types.LONGNVARCHAR);
			when(type.getLength()).thenReturn(1);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfLONGNVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "String";
			when(type.getDataType()).thenReturn(Types.LONGNVARCHAR);
			when(type.getLength()).thenReturn(50);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfLONGNVARCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "String";
			when(type.getDataType()).thenReturn(Types.LONGNVARCHAR);
			when(type.getLength()).thenReturn(50);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfLONGVARBINARY_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "byte[]";
			when(type.getDataType()).thenReturn(Types.LONGVARBINARY);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfLONGVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "char";
			when(type.getDataType()).thenReturn(Types.LONGVARCHAR);
			when(type.getLength()).thenReturn(1);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfLONGVARCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Character";
			when(type.getDataType()).thenReturn(Types.LONGVARCHAR);
			when(type.getLength()).thenReturn(1);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfLONGVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "String";
			when(type.getDataType()).thenReturn(Types.LONGVARCHAR);
			when(type.getLength()).thenReturn(50);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfLONGVARCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "String";
			when(type.getDataType()).thenReturn(Types.LONGVARCHAR);
			when(type.getLength()).thenReturn(50);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfNCHAR1_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "char";
			when(type.getDataType()).thenReturn(Types.NCHAR);
			when(type.getLength()).thenReturn(1);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfNCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Character";
			when(type.getDataType()).thenReturn(Types.NCHAR);
			when(type.getLength()).thenReturn(1);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfNCHAR50_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "String";
			when(type.getDataType()).thenReturn(Types.NCHAR);
			when(type.getLength()).thenReturn(50);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfNCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "String";
			when(type.getDataType()).thenReturn(Types.NCHAR);
			when(type.getLength()).thenReturn(50);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfNCLOB_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.NCLOB);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfNULL_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.NULL);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfNUMERIC_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "double";
			when(type.getDataType()).thenReturn(Types.NUMERIC);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfNUMERICNullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Double";
			when(type.getDataType()).thenReturn(Types.NUMERIC);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfNVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "char";
			when(type.getDataType()).thenReturn(Types.NVARCHAR);
			when(type.getLength()).thenReturn(1);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfNVARCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Character";
			when(type.getDataType()).thenReturn(Types.NVARCHAR);
			when(type.getLength()).thenReturn(1);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfNVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "String";
			when(type.getDataType()).thenReturn(Types.NVARCHAR);
			when(type.getLength()).thenReturn(50);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfNVARCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "String";
			when(type.getDataType()).thenReturn(Types.NVARCHAR);
			when(type.getLength()).thenReturn(50);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfOTHER_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.OTHER);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfREAL_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "double";
			when(type.getDataType()).thenReturn(Types.REAL);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfREALNullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Double";
			when(type.getDataType()).thenReturn(Types.REAL);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfREF_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.REF);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfREF_CURSOR_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.REF_CURSOR);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfROWID_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.ROWID);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfSMALLINT_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "short";
			when(type.getDataType()).thenReturn(Types.SMALLINT);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfSMALLINTNullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Short";
			when(type.getDataType()).thenReturn(Types.SMALLINT);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfSQLXML_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.SQLXML);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfSTRUCT_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.STRUCT);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfTIME_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "LocalTime";
			when(type.getDataType()).thenReturn(Types.TIME);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfTIMENullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "LocalTime";
			when(type.getDataType()).thenReturn(Types.TIME);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfTIME_WITH_TIMEZONE_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			when(type.getDataType()).thenReturn(Types.TIME_WITH_TIMEZONE);
			// Run
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getJavaTypeString(type, false));
		}

		@Test
		void passDomainModelOfTIMESTAMP_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "LocalDateTime";
			when(type.getDataType()).thenReturn(Types.TIMESTAMP);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfTIMESTAMPNullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "LocalDateTime";
			when(type.getDataType()).thenReturn(Types.TIMESTAMP);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfTIMESTAMP_WITH_TIMEZONE_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "ZonedDateTime";
			when(type.getDataType()).thenReturn(Types.TIMESTAMP_WITH_TIMEZONE);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfTIMESTAMP_WITH_TIMEZONENullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "ZonedDateTime";
			when(type.getDataType()).thenReturn(Types.TIMESTAMP_WITH_TIMEZONE);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfTINYINT_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "byte";
			when(type.getDataType()).thenReturn(Types.TINYINT);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfTINYINTNullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Byte";
			when(type.getDataType()).thenReturn(Types.TINYINT);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfVARBINARY_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "byte[]";
			when(type.getDataType()).thenReturn(Types.VARBINARY);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "char";
			when(type.getDataType()).thenReturn(Types.VARCHAR);
			when(type.getLength()).thenReturn(1);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfVARCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "Character";
			when(type.getDataType()).thenReturn(Types.VARCHAR);
			when(type.getLength()).thenReturn(1);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "String";
			when(type.getDataType()).thenReturn(Types.VARCHAR);
			when(type.getLength()).thenReturn(50);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, false);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passDomainModelOfVARCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
			// Prepare
			String expected = "String";
			when(type.getDataType()).thenReturn(Types.VARCHAR);
			when(type.getLength()).thenReturn(50);
			// Run
			String returned = unitUnderTest.getJavaTypeString(type, true);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_isSimpleType_String {

		@ParameterizedTest
		@CsvSource(value = {
				"boolean,true",
				"byte,true",
				"char,true",
				"double,true",
				"float,true",
				"int,true",
				"long,true",
				"short,true",
				"String,false",
				"Foo,false",
				"foo,false" })
		void identifiesSimpleTypeCorrectly(String typeName, boolean expected) {
			assertEquals(expected, unitUnderTest.isSimpleType(typeName));
		}

	}

}