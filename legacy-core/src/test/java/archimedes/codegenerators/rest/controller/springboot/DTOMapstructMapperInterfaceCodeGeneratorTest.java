package archimedes.codegenerators.rest.controller.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class DTOMapstructMapperInterfaceCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private DTOMapstructMapperInterfaceCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".rest.mapper;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import base.pack.age.name.rest.dto.ATableDTO;\n" + //
					"\n" + //
					"import org.mapstruct.Mapper;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DTO mapper for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Mapper(componentModel = \"spring\")\n" + //
					"public interface ATableDTOMapper {\n" + //
					"\n" + //
					"    ATableDTO toDto(ATable model);\n" + //
					"\n" + //
					"    ATable toModel(ATableDTO dto);\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.addOption(
							new Option(RESTControllerNameGenerator.ALTERNATE_DTOCONVERTER_PACKAGE_NAME, "rest.mapper"));
			dataModel.addOption(new Option(ServiceNameGenerator.ALTERNATE_SO_CLASS_NAME_SUFFIX, ""));
			dataModel.addOption(new Option(ServiceNameGenerator.ALTERNATE_SO_PACKAGE_NAME, "core.model"));
			dataModel.addOption(new Option(AbstractClassCodeGenerator.MAPPERS, "mapstruct"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithConverterExtension() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".rest.mapper;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import base.pack.age.name.rest.dto.ATableDTO;\n" + //
					"\n" + //
					"import org.mapstruct.Mapper;\n" + //
					"import org.springframework.core.convert.converter.Converter;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DTO mapper for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Mapper(componentModel = \"spring\")\n" + //
					"public interface ATableDTOMapper extends Converter<ATable, ATableDTO> {\n" + //
					"\n" + //
					"    ATableDTO toDto(ATable model);\n" + //
					"\n" + //
					"    ATable toModel(ATableDTO dto);\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.addOption(
							new Option(RESTControllerNameGenerator.ALTERNATE_DTOCONVERTER_PACKAGE_NAME, "rest.mapper"));
			dataModel.addOption(new Option(ServiceNameGenerator.ALTERNATE_SO_CLASS_NAME_SUFFIX, ""));
			dataModel.addOption(new Option(ServiceNameGenerator.ALTERNATE_SO_PACKAGE_NAME, "core.model"));
			dataModel.addOption(new Option(AbstractClassCodeGenerator.MAPPERS, "mapstruct:converter"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_isToIgnoreFor_DataModel_TableModel {

		@Test
		void passANullValueAsDataModel_throwsAnException() {
			// Prepare
			TableModel table = mock(TableModel.class);
			// Run & Check
			assertThrows(NullPointerException.class, () -> unitUnderTest.isToIgnoreFor(null, table));
		}

		@Test
		void passADataModelMAPPERSNotSet_returnsTrue() {
			// Prepare
			DataModel model = mock(DataModel.class);
			// Run & Check
			assertTrue(unitUnderTest.isToIgnoreFor(model, null));
		}

		@Test
		void passADataModelMAPPERSSetButNotValue_returnsTrue() {
			// Prepare
			DataModel model = mock(DataModel.class);
			when(model.getOptionByName(AbstractClassCodeGenerator.MAPPERS)).thenReturn(null);
			// Run & Check
			assertTrue(unitUnderTest.isToIgnoreFor(model, null));
		}

		@Test
		void passADataModelMAPPERSSetButNotForMapStruct_returnsTrue() {
			// Prepare
			DataModel model = mock(DataModel.class);
			when(model.getOptionByName(AbstractClassCodeGenerator.MAPPERS))
					.thenReturn(new Option(AbstractClassCodeGenerator.MAPPERS, "nonsens"));
			// Run & Check
			assertTrue(unitUnderTest.isToIgnoreFor(model, null));
		}

		@Test
		void passADataModelMAPPERSSetForMapStruct_returnsFalse() {
			// Prepare
			DataModel model = mock(DataModel.class);
			when(model.getOptionByName(AbstractClassCodeGenerator.MAPPERS))
					.thenReturn(new Option(AbstractClassCodeGenerator.MAPPERS, "mapstruct"));
			// Run & Check
			assertFalse(unitUnderTest.isToIgnoreFor(model, null));
		}

		@Test
		void passADataModelMAPPERSSetForMapStructWithConverterExtension_returnsFalse() {
			// Prepare
			DataModel model = mock(DataModel.class);
			when(model.getOptionByName(AbstractClassCodeGenerator.MAPPERS))
					.thenReturn(new Option(AbstractClassCodeGenerator.MAPPERS, "mapstruct:converter"));
			// Run & Check
			assertFalse(unitUnderTest.isToIgnoreFor(model, null));
		}

	}

}