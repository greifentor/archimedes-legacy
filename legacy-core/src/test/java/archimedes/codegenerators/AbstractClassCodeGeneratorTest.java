package archimedes.codegenerators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;

@ExtendWith(MockitoExtension.class)
public class AbstractClassCodeGeneratorTest {

	private static final String CLASS_NAME = "ClassName";
	private static final String TEMPLATE_FILE_NAME = "template-file.name";
	private static final String TEMPLATE_PATH_NAME = "template/path/name";

	@Mock
	private AbstractCodeFactory codeFactory;
	@Mock
	private DataModel model;
	@Mock
	private TableModel table;

	private AbstractClassCodeGenerator<NameGenerator> unitUnderTest;

	@BeforeEach
	void setUp() {
		unitUnderTest = new AbstractClassCodeGenerator<NameGenerator>(
				TEMPLATE_FILE_NAME,
				TEMPLATE_PATH_NAME,
				new NameGenerator(),
				new TypeGenerator(),
				codeFactory) {
			@Override
			public String getClassName(TableModel table) {
				return CLASS_NAME;
			}

			@Override
			public String getPackageName(DataModel model, TableModel table) {
				return null;
			}

			@Override
			protected String getDefaultModuleName(DataModel dataModel) {
				return null;
			}
		};
	}

	@Nested
	class TestOfMethod_getPOJOMode_DataModel_TableModel {

		@Test
		void passDataModelAsNullValue_throwsAnException() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getPOJOMode(null, table));
		}

		@Test
		void passTableModelAsNullValue_throwsAnException() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getPOJOMode(model, null));
		}

		@Test
		void passTableModelAndDataModelWithoutOption_ReturnsModeCHAIN() {
			// Prepare
			POJOMode expected = POJOMode.CHAIN;
			// Run
			POJOMode returned = unitUnderTest.getPOJOMode(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passTableModelAndDataModel_ModeIsSetInDataModelOnly_ReturnsModeBUILDER() {
			// Prepare
			POJOMode expected = POJOMode.BUILDER;
			Option option =
					new Option(AbstractClassCodeGenerator.POJO_MODE, AbstractClassCodeGenerator.POJO_MODE_BUILDER);
			when(model.getOptionByName(AbstractClassCodeGenerator.POJO_MODE)).thenReturn(option);
			// Run
			POJOMode returned = unitUnderTest.getPOJOMode(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passTableModelAndDataModel_ModeIsSetInTableModelOnly_ReturnsModeBUILDER() {
			// Prepare
			POJOMode expected = POJOMode.BUILDER;
			Option option =
					new Option(AbstractClassCodeGenerator.POJO_MODE, AbstractClassCodeGenerator.POJO_MODE_BUILDER);
			when(table.getOptionByName(AbstractClassCodeGenerator.POJO_MODE)).thenReturn(option);
			// Run
			POJOMode returned = unitUnderTest.getPOJOMode(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passTableModelAndDataModel_ModeIsSetDifferentInModelAndTable_ReturnsTableSetting() {
			// Prepare
			POJOMode expected = POJOMode.BUILDER;
			Option optionModel =
					new Option(AbstractClassCodeGenerator.POJO_MODE, AbstractClassCodeGenerator.POJO_MODE_CHAIN);
			Option optionTable =
					new Option(AbstractClassCodeGenerator.POJO_MODE, AbstractClassCodeGenerator.POJO_MODE_BUILDER);
			when(model.getOptionByName(AbstractClassCodeGenerator.POJO_MODE)).thenReturn(optionModel);
			when(table.getOptionByName(AbstractClassCodeGenerator.POJO_MODE)).thenReturn(optionTable);
			// Run
			POJOMode returned = unitUnderTest.getPOJOMode(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestOfMethod_getReferenceMode_DataModel_TableModel {

		@Test
		void passDataModelAsNullValue_throwsAnException() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getReferenceMode(null, table));
		}

		@Test
		void passTableModelAsNullValue_throwsAnException() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getReferenceMode(model, null));
		}

		@Test
		void passTableModelAndDataModelWithoutOption_ReturnsModeID() {
			// Prepare
			ReferenceMode expected = ReferenceMode.ID;
			// Run
			ReferenceMode returned = unitUnderTest.getReferenceMode(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passTableModelAndDataModel_ModeIsSetInDataModelOnly_ReturnsModeOBJECT() {
			// Prepare
			ReferenceMode expected = ReferenceMode.OBJECT;
			Option option =
					new Option(
							AbstractClassCodeGenerator.REFERENCE_MODE,
							AbstractClassCodeGenerator.REFERENCE_MODE_OBJECT);
			when(model.getOptionByName(AbstractClassCodeGenerator.REFERENCE_MODE)).thenReturn(option);
			// Run
			ReferenceMode returned = unitUnderTest.getReferenceMode(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passTableModelAndDataModel_ModeIsSetInTableModelOnly_ReturnsModeOBJECT() {
			// Prepare
			ReferenceMode expected = ReferenceMode.OBJECT;
			Option option =
					new Option(
							AbstractClassCodeGenerator.REFERENCE_MODE,
							AbstractClassCodeGenerator.REFERENCE_MODE_OBJECT);
			when(table.getOptionByName(AbstractClassCodeGenerator.REFERENCE_MODE)).thenReturn(option);
			// Run
			ReferenceMode returned = unitUnderTest.getReferenceMode(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passTableModelAndDataModel_ModeIsSetDifferentInModelAndTable_ReturnsTableSetting() {
			// Prepare
			ReferenceMode expected = ReferenceMode.OBJECT;
			Option optionModel =
					new Option(AbstractClassCodeGenerator.REFERENCE_MODE, AbstractClassCodeGenerator.REFERENCE_MODE_ID);
			Option optionTable =
					new Option(
							AbstractClassCodeGenerator.REFERENCE_MODE,
							AbstractClassCodeGenerator.REFERENCE_MODE_OBJECT);
			when(model.getOptionByName(AbstractClassCodeGenerator.REFERENCE_MODE)).thenReturn(optionModel);
			when(table.getOptionByName(AbstractClassCodeGenerator.REFERENCE_MODE)).thenReturn(optionTable);
			// Run
			ReferenceMode returned = unitUnderTest.getReferenceMode(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestOfMethod_isCommentsOff_DataModel_TableModel {

		@Test
		void passNullValueAsModel_ReturnsFalse() {
			assertFalse(unitUnderTest.isCommentsOff(null, table));
		}

		@Test
		void passNullValueAsTable_ReturnsFalse() {
			assertFalse(unitUnderTest.isCommentsOff(model, null));
		}

		@Test
		void passModelWithNoCOMMENTSOption_ReturnsFalse() {
			// Prepare
			when(model.getOptionByName(AbstractClassCodeGenerator.COMMENTS)).thenReturn(null);
			// Run & Check
			assertFalse(unitUnderTest.isCommentsOff(model, table));
		}

		@Test
		void passModelWithEmptyValueCOMMENTSOption_ReturnsFalse() {
			// Prepare
			when(model.getOptionByName(AbstractClassCodeGenerator.COMMENTS))
					.thenReturn(new Option(AbstractClassCodeGenerator.COMMENTS, ""));
			// Run & Check
			assertFalse(unitUnderTest.isCommentsOff(model, table));
		}

		@Test
		void passModelWithNullValueCOMMENTSOption_ReturnsFalse() {
			// Prepare
			when(model.getOptionByName(AbstractClassCodeGenerator.COMMENTS))
					.thenReturn(new Option(AbstractClassCodeGenerator.COMMENTS, null));
			// Run & Check
			assertFalse(unitUnderTest.isCommentsOff(model, table));
		}

		@Test
		void passModelWithNullValueONCOMMENTSOption_ReturnsFalse() {
			// Prepare
			when(model.getOptionByName(AbstractClassCodeGenerator.COMMENTS))
					.thenReturn(new Option(AbstractClassCodeGenerator.COMMENTS, "ON"));
			// Run & Check
			assertFalse(unitUnderTest.isCommentsOff(model, table));
		}

		@Test
		void passModelWithNullValueOffCOMMENTSOption_ReturnsTrue() {
			// Prepare
			when(model.getOptionByName(AbstractClassCodeGenerator.COMMENTS))
					.thenReturn(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
			// Run & Check
			assertTrue(unitUnderTest.isCommentsOff(model, table));
		}

	}

	@Nested
	class TestsOfMethod_getContextName_TableModel {

		@Test
		void passANullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getContextName(null));
		}

		@Test
		void passATableModel_ReturnsTheClassNameOfTheTable() {
			// Prepare
			String expected = "Expected";
			String tableName = "expected";
			when(table.getName()).thenReturn(tableName);
			// Run & Check
			assertEquals(expected, unitUnderTest.getContextName(table));
		}

		@Test
		void passATableModel_ReturnsTheConfiguredContextNameForTheTable() {
			// Prepare
			String expected = "Expected";
			when(table.getName()).thenReturn("TableName");
			when(table.getOptionByName(AbstractClassCodeGenerator.CONTEXT_NAME))
					.thenReturn(new Option(AbstractClassCodeGenerator.CONTEXT_NAME, expected));
			// Run & Check
			assertEquals(expected, unitUnderTest.getContextName(table));
		}

	}

}