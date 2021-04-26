package archimedes.codegenerators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

@ExtendWith(MockitoExtension.class)
public class NameGeneratorTest {

	@Mock
	private ColumnModel column;
	@Mock
	private DataModel model;
	@Mock
	private TableModel table;

	@InjectMocks
	private NameGenerator unitUnderTest;

	@DisplayName("tests for column names to attribute names")
	@Nested
	class AttributeNameTests {

		@Test
		void getAttributeName_PassColumnModelWithEmptyName_ThrowsException() {
			// Prepare
			when(column.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getAttributeName(column);
			});
		}

		@Test
		void getAttributeName_PassANullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getAttributeName((ColumnModel) null));
		}

		@Test
		void getAttributeName_PassAColumnNameStartsWithLowerCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "column";
			when(column.getName()).thenReturn("column");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameStartsWithUpperCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "column";
			when(column.getName()).thenReturn("Column");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameCompleteUpperCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "column";
			when(column.getName()).thenReturn("COLUMN");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameSingleUpperCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "c";
			when(column.getName()).thenReturn("C");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameSingleLowerCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "c";
			when(column.getName()).thenReturn("c");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameOnlyUpperCaseWithUnderScore_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "columnName";
			when(column.getName()).thenReturn("COLUMN_NAME");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameOnlyMixedCaseWithUnderScore_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "columnName";
			when(column.getName()).thenReturn("Column_Name");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameOnlyLowercaseWithUnderScore_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "columnName";
			when(column.getName()).thenReturn("column_name");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_SetUseQualifiedColumnName_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "tableNameColumnName";
			when(table.getName()).thenReturn("TableName");
			when(column.getTable()).thenReturn(table);
			when(column.getName()).thenReturn("column_name");
			// Run
			String returned = unitUnderTest.getAttributeName(column, true);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class PluralNameTests {

		@Test
		void getPluralName_ModelWithNoConfiguration_ReturnsPluralOfTableName() {
			// Prepare
			String expected = "TableNames";
			when(table.getName()).thenReturn("TableName");
			// Run
			String returned = unitUnderTest.getPluralName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPluralName_ModelWithNoConfigurationTableNameEndWithY_ReturnsPluralOfTableNameEndingOnIes() {
			// Prepare
			String expected = "Bunnies";
			when(table.getName()).thenReturn("Bunny");
			// Run
			String returned = unitUnderTest.getPluralName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPluralName_TableIsNull_ThrowsAnException() {
			assertThrows(NullPointerException.class, () -> unitUnderTest.getPluralName((TableModel) null));
		}

	}

	@Nested
	class TestsOfMethod_getDescriptionName_String {

		@Test
		void passANullValue_ThrowsAnException() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getDescriptionName(null));
		}

		@Test
		void passAnEmptyString_ReturnsAnEmptyString() {
			assertEquals("", unitUnderTest.getDescriptionName(""));
		}

		@Test
		void passASimpleString_ReturnsTheCorrectDescriptionName() {
			assertEquals("simple", unitUnderTest.getDescriptionName("simple"));
		}

		@Test
		void passASimpleStringStartingWithUpperCaseLetter_ReturnsTheCorrectDescriptionName() {
			assertEquals("simple", unitUnderTest.getDescriptionName("Simple"));
		}

		@Test
		void passASimpleStringHavingUpperCaseLetterInside_ReturnsTheCorrectDescriptionName() {
			assertEquals("simple name", unitUnderTest.getDescriptionName("SimpleName"));
		}

		@Test
		void passASimpleStringHavingUnderScoreInside_ReturnsTheCorrectDescriptionName() {
			assertEquals("simple name", unitUnderTest.getDescriptionName("simple_name"));
		}

	}

	@Nested
	class TestsOfMethod_getCamelCase_String {

		@Test
		void passANullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getCamelCase(null));
		}

		@Test
		void passAnEmptyString_ReturnsAEmptyString() {
			assertEquals("", unitUnderTest.getCamelCase(""));
		}

		@Test
		void passASimpleStringWithNoUpperCaseLetters_ReturnsThePassedStringStartingWithUpperCaseLetter() {
			assertEquals("Simple", unitUnderTest.getCamelCase("simple"));
		}

		@Test
		void passASimpleStringStartingWithAnUpperCaseLetters_ReturnsThePassedString() {
			assertEquals("Simple", unitUnderTest.getCamelCase("Simple"));
		}

		@Test
		void passAValidUpperCaseString_ReturnsThePassedString() {
			assertEquals("SimpleString", unitUnderTest.getCamelCase("SimpleString"));
		}

		@Test
		void passAStringWithSpaces_ReturnsACamelCaseString() {
			assertEquals("SimpleString", unitUnderTest.getCamelCase("simple string"));
		}

		@Test
		void passAStringWithUnderscores_ReturnsACamelCaseString() {
			assertEquals("SimpleString", unitUnderTest.getCamelCase("simple_string"));
		}

		@Test
		void passAnUpperCaseStringWithUnderscores_ReturnsACamelCaseString() {
			assertEquals("SimpleString", unitUnderTest.getCamelCase("SIMPLE_STRING"));
		}

	}

	@Nested
	class TestsOfMethod_getEnumIdentfier_String {

		@Test
		void passANullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getEnumIdentifier(null));
		}

		@Test
		void passAnEmptyString_ReturnsAEmptyString() {
			assertEquals("", unitUnderTest.getEnumIdentifier(""));
		}

		@Test
		void passAnUpperCaseString_ReturnsThePassedString() {
			// Prepare
			String expected = "UPPER_CASE_STRING";
			// Run
			String returned = unitUnderTest.getEnumIdentifier(expected);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passAnLowerCaseString_ReturnsThePassedString() {
			// Prepare
			String expected = "UPPER_CASE_STRING";
			// Run
			String returned = unitUnderTest.getEnumIdentifier(expected.toLowerCase());
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passAnCamelCaseString_ReturnsThePassedString() {
			// Prepare
			String expected = "UPPER_CASE_STRING";
			// Run
			String returned = unitUnderTest.getEnumIdentifier("upperCaseString");
			// Check
			assertEquals(expected, returned);
		}

	}

}