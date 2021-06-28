package archimedes.codegenerators.rest.controller.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;

@ExtendWith(MockitoExtension.class)
public class RESTControllerNameGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Mock
	private ColumnModel column;
	@Mock
	private DataModel model;
	@Mock
	private TableModel table;

	@InjectMocks
	private RESTControllerNameGenerator unitUnderTest;

	@DisplayName("tests for DTO class names")
	@Nested
	class DTOClassNameTests {

		@Test
		void getDTOClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getDTOClassName(table);
			});
		}

		@Test
		void getDTOClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getDTOClassName(null));
		}

		@Test
		void getDTOClassName_PassTableModelWithNameCamelCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TestTableDTO";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelWithNameUpperCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDTO";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTO";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTO";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTO";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelWithNameLowerCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDTO";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TDTO";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TDTO";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOPackageName_PassDataModelWithALTERNATE_DTO_CLASS_NAME_SUFFIXOption_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDto";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			doReturn(new Option(RESTControllerNameGenerator.ALTERNATE_DTO_CLASS_NAME_SUFFIX, "Dto"))
					.when(model)
					.getOptionByName(RESTControllerNameGenerator.ALTERNATE_DTO_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DTO package names")
	@Nested
	class DTOPackageNameTests {

		@Test
		void getDTOPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getDTOPackageName(null, table));
		}

		@Test
		void getDTOPackageName_PassANullValueAsTable_ReturnsDefaultPackage() {
			assertEquals("rest.dto", unitUnderTest.getDTOPackageName(model, null));
		}

		@Test
		void getDTOPackageName_PassAValidTableModel_ReturnsACorrecDTOName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".rest.dto";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getDTOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrecDTOName() {
			// Prepare
			String expected = "rest.dto";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getDTOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrecDTOName() {
			// Prepare
			String expected = "rest.dto";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getDTOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DTO converter class names")
	@Nested
	class DTOConverterClassNameTests {

		@Test
		void getDTOConverterClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getDTOConverterClassName(table);
			});
		}

		@Test
		void getDTOConverterClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getDTOConverterClassName(null));
		}

		@Test
		void getDTOConverterClassName_PassTableModelWithNameCamelCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TestTableDTOConverter";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelWithNameUpperCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDTOConverter";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTOConverter";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTOConverter";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTOConverter";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelWithNameLowerCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDTOConverter";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TDTOConverter";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TDTOConverter";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassDataModelWithMapStructMapperOption_ReturnsACorrectDTOConverterName() {
			// Prepare
			String expected = "TableDTOMapper";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(AbstractClassCodeGenerator.MAPPERS))
					.thenReturn(new Option(AbstractClassCodeGenerator.MAPPERS, "mapstruct"));
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassDataModelWithALTERNATE_DTO_MAPPER_NAME_SUFFIXOption_ReturnsACorrectDTOConverterName() {
			// Prepare
			String expected = "TableDtoMapper";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			doReturn(new Option(AbstractClassCodeGenerator.MAPPERS, "mapstruct"))
					.when(model)
					.getOptionByName(AbstractClassCodeGenerator.MAPPERS);
			doReturn(new Option(RESTControllerNameGenerator.ALTERNATE_DTOMAPPER_CLASS_NAME_SUFFIX, "DtoMapper"))
					.when(model)
					.getOptionByName(RESTControllerNameGenerator.ALTERNATE_DTOMAPPER_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DTO converter package names")
	@Nested
	class DTOConverterPackageNameTests {

		@Test
		void getDTOConverterPackageName_PassANullValueModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getDTOConverterPackageName(null, table));
		}

		@Test
		void getDTOConverterPackageName_PassANullValueTable_ReturnsDefaultValue() {
			assertEquals("rest.converter", unitUnderTest.getDTOConverterPackageName(model, null));
		}

		@Test
		void getDTOConverterPackageName_PassAValidTableModel_ReturnsACorrecDTOName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".rest.converter";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getDTOConverterPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrecDTOName() {
			// Prepare
			String expected = "rest.converter";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getDTOConverterPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrecDTOName() {
			// Prepare
			String expected = "rest.converter";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getDTOConverterPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassModelWithAlternateDTOMapperPackageName_ReturnsACorrectDTOMapperPackageName() {
			// Prepare
			String expected = "rest.mapper";
			when(model.getBasePackageName()).thenReturn(null);
			when(model.getOptionByName(RESTControllerNameGenerator.ALTERNATE_DTOMAPPER_PACKAGE_NAME))
					.thenReturn(
							new Option(RESTControllerNameGenerator.ALTERNATE_DTOMAPPER_PACKAGE_NAME, "rest.mapper"));
			// Run
			String returned = unitUnderTest.getDTOConverterPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for list DTO class names")
	@Nested
	class ListDTOClassNameTests {

		@Test
		void getListDTOClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getListDTOClassName(table);
			});
		}

		@Test
		void getListDTOClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getListDTOClassName(null));
		}

		@Test
		void getListDTOClassName_PassTableModelWithNameCamelCase_ReturnsACorrectListDTOName() {
			// Prepare
			String expected = "TestTableListDTO";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getListDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDTOClassName_PassTableModelWithNameUpperCase_ReturnsACorrectListDTOName() {
			// Prepare
			String expected = "TableListDTO";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getListDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDTOClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectListDTOName() {
			// Prepare
			String expected = "TableNameListDTO";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getListDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDTOClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectListDTOName() {
			// Prepare
			String expected = "TableNameListDTO";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getListDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDTOClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectListDTOName() {
			// Prepare
			String expected = "TableNameListDTO";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getListDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDTOClassName_PassTableModelWithNameLowerCase_ReturnsACorrectListDTOName() {
			// Prepare
			String expected = "TableListDTO";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getListDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDTOClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectListDTOName() {
			// Prepare
			String expected = "TListDTO";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getListDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDTOClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectListDTOName() {
			// Prepare
			String expected = "TListDTO";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getListDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for REST controller class names")
	@Nested
	class RESTControllerClassNameTests {

		@Test
		void getRESTControllerClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getRESTControllerClassName(table);
			});
		}

		@Test
		void getRESTControllerClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getRESTControllerClassName(null));
		}

		@Test
		void getRESTControllerClassName_PassTableModelWithNameCamelCase_ReturnsACorrectRESTControllerName() {
			// Prepare
			String expected = "TestTableRESTController";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getRESTControllerClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getRESTControllerClassName_PassTableModelWithNameUpperCase_ReturnsACorrectRESTControllerName() {
			// Prepare
			String expected = "TableRESTController";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getRESTControllerClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getRESTControllerClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectRESTControllerName() {
			// Prepare
			String expected = "TableNameRESTController";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getRESTControllerClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getRESTControllerClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectRESTControllerName() {
			// Prepare
			String expected = "TableNameRESTController";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getRESTControllerClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getRESTControllerClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectRESTControllerName() {
			// Prepare
			String expected = "TableNameRESTController";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getRESTControllerClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getRESTControllerClassName_PassTableModelWithNameLowerCase_ReturnsACorrectRESTControllerName() {
			// Prepare
			String expected = "TableRESTController";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getRESTControllerClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getRESTControllerClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectRESTControllerName() {
			// Prepare
			String expected = "TRESTController";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getRESTControllerClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getRESTControllerClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectRESTControllerName() {
			// Prepare
			String expected = "TRESTController";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getRESTControllerClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getRESTControllerClassName_PassDataModelWithALTERNATE_DTO_CLASS_NAME_SUFFIXOption_ReturnsACorrectRESTControllerName() {
			// Prepare
			String expected = "TableDto";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			doReturn(new Option(RESTControllerNameGenerator.ALTERNATE_DTO_CLASS_NAME_SUFFIX, "Dto"))
					.when(model)
					.getOptionByName(RESTControllerNameGenerator.ALTERNATE_DTO_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for REST controller package names")
	@Nested
	class RESTControllerPackageNameTests {

		@Test
		void getRESTControllerPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getRESTControllerPackageName(null, table));
		}

		@Test
		void getRESTControllerPackageName_PassANullValueAsTable_ReturnsDefaultValue() {
			assertEquals("rest.v1", unitUnderTest.getRESTControllerPackageName(model, null));
		}

		@Test
		void getRESTControllerPackageName_PassAValidTableModel_ReturnsACorrecRESTControllerName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".rest.v1";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getRESTControllerPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getRESTControllerPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrecRESTControllerName() {
			// Prepare
			String expected = "rest.v1";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getRESTControllerPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getRESTControllerPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrecRESTControllerName() {
			// Prepare
			String expected = "rest.v1";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getRESTControllerPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for URL names")
	@Nested
	class URLNameTests {

		@Test
		void getURLName_PassValidModelAndTableWithEitherExtraConfigurationInTheModelNorViaProperties_ReturnsApiV1PrefixWithURL() {
			// Prepare
			String expected = "api/v1/tablenames";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getURLName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getURLName_PassValidModelAndTableWithAlternateConfigurationInTheModel_ReturnsAlternatePrefixWithURL() {
			// Prepare
			String alternatePrefix = "api/v2";
			String expected = alternatePrefix + "/tablenames";
			OptionModel option = mock(OptionModel.class);
			when(option.getParameter()).thenReturn(alternatePrefix);
			when(table.getName()).thenReturn("TABLE_NAME");
			when(model.getOptionByName(RESTControllerNameGenerator.REST_URL_PREFIX)).thenReturn(option);
			// Run
			String returned = unitUnderTest.getURLName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getURLName_PassValidModelAndTableWithAlternateConfigurationInTheTable_ReturnsAlternatePrefixWithURL() {
			// Prepare
			String alternatePrefix = "api/v2";
			String expected = alternatePrefix + "/tablenames";
			OptionModel option = mock(OptionModel.class);
			when(option.getParameter()).thenReturn(alternatePrefix);
			when(table.getName()).thenReturn("TABLE_NAME");
			when(table.getOptionByName(RESTControllerNameGenerator.REST_URL_PREFIX)).thenReturn(option);
			// Run
			String returned = unitUnderTest.getURLName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getURLName_PassValidModelAndTableWithAlternateConfigurationInTheTableAndModel_ReturnsAlternatePrefixForTableWithURL() {
			// Prepare
			String alternatePrefixModel = "api/v2";
			String alternatePrefixTable = "api/v3";
			String expected = alternatePrefixTable + "/tablenames";
			OptionModel optionTable = mock(OptionModel.class);
			when(optionTable.getParameter()).thenReturn(alternatePrefixTable);
			OptionModel optionModel = mock(OptionModel.class);
			when(optionModel.getParameter()).thenReturn(alternatePrefixModel);
			when(table.getName()).thenReturn("TABLE_NAME");
			when(table.getOptionByName(RESTControllerNameGenerator.REST_URL_PREFIX)).thenReturn(optionTable);
			when(model.getOptionByName(RESTControllerNameGenerator.REST_URL_PREFIX)).thenReturn(optionModel);
			// Run
			String returned = unitUnderTest.getURLName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

}