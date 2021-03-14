package archimedes.codegenerators.persistence.jpa;

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
public class PersistenceJPANameGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Mock
	private ColumnModel column;
	@Mock
	private DataModel model;
	@Mock
	private TableModel table;

	@InjectMocks
	private PersistenceJPANameGenerator unitUnderTest;

	@DisplayName("tests for DBO class names")
	@Nested
	class DBOClassNameTests {

		@Test
		void getDBOClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getDBOClassName(table);
			});
		}

		@Test
		void getDBOClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getDBOClassName(null));
		}

		@Test
		void getDBOClassName_PassTableModelWithNameCamelCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TestTableDBO";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelWithNameUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableDBO";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameDBO";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameDBO";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameDBO";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelWithNameLowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableDBO";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TDBO";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TDBO";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DBO package names")
	@Nested
	class DBOPackageNameTests {

		@Test
		void getDBOPackageName_PassANullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getDBOPackageName(null));
		}

		@Test
		void getDBOPackageName_PassAValidTableModel_ReturnsACorrecDBOName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".persistence.entities";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getDBOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrecDBOName() {
			// Prepare
			String expected = "persistence.entities";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getDBOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrecDBOName() {
			// Prepare
			String expected = "persistence.entities";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getDBOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

}