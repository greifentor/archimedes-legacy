package archimedes.codegenerators.gui.vaadin.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class GOConverterClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private GOConverterClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".gui.vaadin.converter;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".gui.vaadin.go.ATableGO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".core.model.ATable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A GO converter for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"public class ATableGOConverter implements ToGOConverter<ATableGO, ATable> {\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATableGO toGO(ATable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATableGO()\n" + //
					"				.setId(model.getId())\n" + //
					"				.setADate(model.getADate())\n" + //
					"				.setDescription(model.getDescription());\n" + //
					"	}\n" + //
					"\n" + //
					"	public ATable toModel(ATableGO go) {\n" + //
					"		if (go == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATable()\n" + //
					"				.setId(go.getId())\n" + //
					"				.setADate(go.getADate())\n" + //
					"				.setDescription(go.getDescription());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithSimpleBoolean() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".gui.vaadin.converter;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".gui.vaadin.go.AnotherTableGO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".core.model.AnotherTable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A GO converter for another_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"public class AnotherTableGOConverter implements ToGOConverter<AnotherTableGO, AnotherTable> {\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public AnotherTableGO toGO(AnotherTable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new AnotherTableGO()\n" + //
					"				.setId(model.getId())\n" + //
					"				.setValid(model.isValid());\n" + //
					"	}\n" + //
					"\n" + //
					"	public AnotherTable toModel(AnotherTableGO go) {\n" + //
					"		if (go == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new AnotherTable()\n" + //
					"				.setId(go.getId())\n" + //
					"				.setValid(go.isValid());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.getTableByName("ANOTHER_TABLE").getColumnByName("VALID").setNotNull(true);
			// Run
			String returned =
					unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("ANOTHER_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithBoolean() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".gui.vaadin.converter;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".gui.vaadin.go.AnotherTableGO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".core.model.AnotherTable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A GO converter for another_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"public class AnotherTableGOConverter implements ToGOConverter<AnotherTableGO, AnotherTable> {\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public AnotherTableGO toGO(AnotherTable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new AnotherTableGO()\n" + //
					"				.setId(model.getId())\n" + //
					"				.setValid(model.getValid());\n" + //
					"	}\n" + //
					"\n" + //
					"	public AnotherTable toModel(AnotherTableGO go) {\n" + //
					"		if (go == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new AnotherTable()\n" + //
					"				.setId(go.getId())\n" + //
					"				.setValid(go.getValid());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned =
					unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("ANOTHER_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}