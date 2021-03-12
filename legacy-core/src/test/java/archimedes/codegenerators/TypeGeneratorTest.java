package archimedes.codegenerators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.sql.Types;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.DomainModel;

@ExtendWith(MockitoExtension.class)
public class TypeGeneratorTest {

	@Mock
	private DomainModel type;

	@InjectMocks
	private TypeGenerator unitUnderTest;

	@Test
	public void typeToTypeSourceModel_PassANullValue_ReturnsANullValue() {
		assertNull(this.unitUnderTest.getJavaTypeString(null, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfARRAY_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.ARRAY);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfBIGINT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "long";
		when(type.getDataType()).thenReturn(Types.BIGINT);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfBIGINTNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Long";
		when(type.getDataType()).thenReturn(Types.BIGINT);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfBINARY_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.BINARY);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	public void typeToTypeSourceModel_PassDomainModelOfBIT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Boolean";
		when(type.getDataType()).thenReturn(Types.BIT);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfBLOB_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.BLOB);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfBOOLEAN_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "boolean";
		when(type.getDataType()).thenReturn(Types.BOOLEAN);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfBOOLEANNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Boolean";
		when(type.getDataType()).thenReturn(Types.BOOLEAN);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		when(type.getDataType()).thenReturn(Types.CHAR);
		when(type.getLength()).thenReturn(1);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Character";
		when(type.getDataType()).thenReturn(Types.CHAR);
		when(type.getLength()).thenReturn(1);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		when(type.getDataType()).thenReturn(Types.CHAR);
		when(type.getLength()).thenReturn(50);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		when(type.getDataType()).thenReturn(Types.CHAR);
		when(type.getLength()).thenReturn(50);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfCLOB_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.CLOB);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfDATALINK_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.DATALINK);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfDATE_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalDate";
		when(type.getDataType()).thenReturn(Types.DATE);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfDATENullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalDate";
		when(type.getDataType()).thenReturn(Types.DATE);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfDECIMAL_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "double";
		when(type.getDataType()).thenReturn(Types.DECIMAL);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfDECIMALNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Double";
		when(type.getDataType()).thenReturn(Types.DECIMAL);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfDISTINCT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.DISTINCT);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfDOUBLE_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "double";
		when(type.getDataType()).thenReturn(Types.DOUBLE);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfDOUBLENullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Double";
		when(type.getDataType()).thenReturn(Types.DOUBLE);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfFLOAT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "float";
		when(type.getDataType()).thenReturn(Types.FLOAT);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfFLOATNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Float";
		when(type.getDataType()).thenReturn(Types.FLOAT);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfINTEGER_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "int";
		when(type.getDataType()).thenReturn(Types.INTEGER);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfINTEGERNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Integer";
		when(type.getDataType()).thenReturn(Types.INTEGER);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfJAVA_OBJECT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.JAVA_OBJECT);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfLONGNVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		when(type.getDataType()).thenReturn(Types.LONGNVARCHAR);
		when(type.getLength()).thenReturn(1);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfLONGNVARCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Character";
		when(type.getDataType()).thenReturn(Types.LONGNVARCHAR);
		when(type.getLength()).thenReturn(1);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfLONGNVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		when(type.getDataType()).thenReturn(Types.LONGNVARCHAR);
		when(type.getLength()).thenReturn(50);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfLONGNVARCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		when(type.getDataType()).thenReturn(Types.LONGNVARCHAR);
		when(type.getLength()).thenReturn(50);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfLONGVARBINARY_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.LONGVARBINARY);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfLONGVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		when(type.getDataType()).thenReturn(Types.LONGVARCHAR);
		when(type.getLength()).thenReturn(1);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfLONGVARCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Character";
		when(type.getDataType()).thenReturn(Types.LONGVARCHAR);
		when(type.getLength()).thenReturn(1);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfLONGVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		when(type.getDataType()).thenReturn(Types.LONGVARCHAR);
		when(type.getLength()).thenReturn(50);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfLONGVARCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		when(type.getDataType()).thenReturn(Types.LONGVARCHAR);
		when(type.getLength()).thenReturn(50);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfNCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		when(type.getDataType()).thenReturn(Types.NCHAR);
		when(type.getLength()).thenReturn(1);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfNCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Character";
		when(type.getDataType()).thenReturn(Types.NCHAR);
		when(type.getLength()).thenReturn(1);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfNCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		when(type.getDataType()).thenReturn(Types.NCHAR);
		when(type.getLength()).thenReturn(50);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfNCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		when(type.getDataType()).thenReturn(Types.NCHAR);
		when(type.getLength()).thenReturn(50);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfNCLOB_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.NCLOB);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfNULL_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.NULL);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfNUMERIC_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "double";
		when(type.getDataType()).thenReturn(Types.NUMERIC);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfNUMERICNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Double";
		when(type.getDataType()).thenReturn(Types.NUMERIC);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfNVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		when(type.getDataType()).thenReturn(Types.NVARCHAR);
		when(type.getLength()).thenReturn(1);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfNVARCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Character";
		when(type.getDataType()).thenReturn(Types.NVARCHAR);
		when(type.getLength()).thenReturn(1);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfNVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		when(type.getDataType()).thenReturn(Types.NVARCHAR);
		when(type.getLength()).thenReturn(50);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfNVARCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		when(type.getDataType()).thenReturn(Types.NVARCHAR);
		when(type.getLength()).thenReturn(50);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfOTHER_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.OTHER);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfREAL_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "double";
		when(type.getDataType()).thenReturn(Types.REAL);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfREALNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Double";
		when(type.getDataType()).thenReturn(Types.REAL);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfREF_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.REF);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfREF_CURSOR_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.REF_CURSOR);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfROWID_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.ROWID);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfSMALLINT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "short";
		when(type.getDataType()).thenReturn(Types.SMALLINT);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfSMALLINTNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Short";
		when(type.getDataType()).thenReturn(Types.SMALLINT);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfSQLXML_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.SQLXML);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfSTRUCT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.STRUCT);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfTIME_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalTime";
		when(type.getDataType()).thenReturn(Types.TIME);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfTIMENullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalTime";
		when(type.getDataType()).thenReturn(Types.TIME);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfTIME_WITH_TIMEZONE_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.TIME_WITH_TIMEZONE);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfTIMESTAMP_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalDateTime";
		when(type.getDataType()).thenReturn(Types.TIMESTAMP);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfTIMESTAMPNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalDateTime";
		when(type.getDataType()).thenReturn(Types.TIMESTAMP);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfTIMESTAMP_WITH_TIMEZONE_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "ZonedDateTime";
		when(type.getDataType()).thenReturn(Types.TIMESTAMP_WITH_TIMEZONE);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfTIMESTAMP_WITH_TIMEZONENullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "ZonedDateTime";
		when(type.getDataType()).thenReturn(Types.TIMESTAMP_WITH_TIMEZONE);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfTINYINT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "byte";
		when(type.getDataType()).thenReturn(Types.TINYINT);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfTINYINTNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Byte";
		when(type.getDataType()).thenReturn(Types.TINYINT);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfVARBINARY_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		when(type.getDataType()).thenReturn(Types.VARBINARY);
		// Run
		assertThrows(IllegalArgumentException.class, () -> this.unitUnderTest.getJavaTypeString(type, false));
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		when(type.getDataType()).thenReturn(Types.VARCHAR);
		when(type.getLength()).thenReturn(1);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfVARCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Character";
		when(type.getDataType()).thenReturn(Types.VARCHAR);
		when(type.getLength()).thenReturn(1);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		when(type.getDataType()).thenReturn(Types.VARCHAR);
		when(type.getLength()).thenReturn(50);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, false);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	public void typeToTypeSourceModel_PassDomainModelOfVARCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		when(type.getDataType()).thenReturn(Types.VARCHAR);
		when(type.getLength()).thenReturn(50);
		// Run
		String returned = this.unitUnderTest.getJavaTypeString(type, true);
		// Check
		assertEquals(expected, returned);
	}

}