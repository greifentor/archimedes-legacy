package archimedes.codegenerators.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.legacy.scheme.Relation;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;
import corent.base.Direction;

@ExtendWith(MockitoExtension.class)
public class DBOConverterClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private DBOConverterClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".persistence.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".persistence.entity.ATableDBO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".core.model.ATable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO converter for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATableDBOConverter implements ToModelConverter<ATable, ATableDBO> {\n" + //
					"\n" + //
					"	public ATableDBO toDBO(ATable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATableDBO()\n" + //
					"				.setId(model.getId())\n" + //
					"				.setADate(model.getADate())\n" + //
					"				.setDescription(model.getDescription());\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<ATableDBO> toDBO(List<ATable> models) {\n" + //
					"		if (models == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable toModel(ATableDBO dbo) {\n" + //
					"		if (dbo == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATable()\n" + //
					"				.setId(dbo.getId())\n" + //
					"				.setADate(dbo.getADate())\n" + //
					"				.setDescription(dbo.getDescription());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<ATable> toModel(List<ATableDBO> dbos) {\n" + //
					"		if (dbos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_generate_String_TableModel_WithObjectReference {

		@Test
		void happyRunForASimpleObjectWithObjectReference() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".persistence.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".persistence.entity.ATableDBO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".core.model.ATable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO converter for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATableDBOConverter implements ToModelConverter<ATable, ATableDBO> {\n" + //
					"\n" + //
					"	private final AnotherTableDBOConverter anotherTableDBOConverter;\n" + //
					"\n" + //
					"	public ATableDBO toDBO(ATable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATableDBO()\n" + //
					"				.setId(model.getId())\n" + //
					"				.setRef(anotherTableDBOConverter.toDBO(model.getRef()));\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<ATableDBO> toDBO(List<ATable> models) {\n" + //
					"		if (models == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable toModel(ATableDBO dbo) {\n" + //
					"		if (dbo == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATable()\n" + //
					"				.setId(dbo.getId())\n" + //
					"				.setRef(anotherTableDBOConverter.toModel(dbo.getRef()));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<ATable> toModel(List<ATableDBO> dbos) {\n" + //
					"		if (dbos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			dataModel
					.addOption(
							new Option(
									AbstractClassCodeGenerator.REFERENCE_MODE,
									AbstractClassCodeGenerator.REFERENCE_MODE_OBJECT));
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.getColumnByName("REF").setNotNull(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithNoReference() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".persistence.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".persistence.entity.AnotherTableDBO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".core.model.AnotherTable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO converter for another_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class AnotherTableDBOConverter implements ToModelConverter<AnotherTable, AnotherTableDBO> {\n"
					+ //
					"\n" + //
					"	public AnotherTableDBO toDBO(AnotherTable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new AnotherTableDBO()\n" + //
					"				.setId(model.getId())\n" + //
					"				.setName(model.getName());\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<AnotherTableDBO> toDBO(List<AnotherTable> models) {\n" + //
					"		if (models == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public AnotherTable toModel(AnotherTableDBO dbo) {\n" + //
					"		if (dbo == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new AnotherTable()\n" + //
					"				.setId(dbo.getId())\n" + //
					"				.setName(dbo.getName());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<AnotherTable> toModel(List<AnotherTableDBO> dbos) {\n" + //
					"		if (dbos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			dataModel
					.addOption(
							new Option(
									AbstractClassCodeGenerator.REFERENCE_MODE,
									AbstractClassCodeGenerator.REFERENCE_MODE_OBJECT));
			TableModel table = dataModel.getTableByName("ANOTHER_TABLE");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_generate_String_TableModel_WithIdReference {

		@Test
		void happyRunForASimpleObjectWithAReference() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".persistence.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".persistence.entity.ATableDBO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".core.model.ATable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO converter for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATableDBOConverter implements ToModelConverter<ATable, ATableDBO> {\n" + //
					"\n" + //
					"	public ATableDBO toDBO(ATable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATableDBO()\n" + //
					"				.setId(model.getId())\n" + //
					"				.setRef(model.getRef());\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<ATableDBO> toDBO(List<ATable> models) {\n" + //
					"		if (models == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable toModel(ATableDBO dbo) {\n" + //
					"		if (dbo == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATable()\n" + //
					"				.setId(dbo.getId())\n" + //
					"				.setRef(dbo.getRef());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<ATable> toModel(List<ATableDBO> dbos) {\n" + //
					"		if (dbos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			dataModel
					.addOption(
							new Option(
									AbstractClassCodeGenerator.REFERENCE_MODE,
									AbstractClassCodeGenerator.REFERENCE_MODE_ID));
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.getColumnByName("REF").setNotNull(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_generate_String_TableModel_WithEnumAttribute {

		@Test
		void happyRun() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".persistence.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".persistence.entity.ATableDBO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".core.model.ATable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO converter for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATableDBOConverter implements ToModelConverter<ATable, ATableDBO> {\n" + //
					"\n" + //
					"	private final DescriptionDBOConverter descriptionDBOConverter;\n" + //
					"\n" + //
					"	public ATableDBO toDBO(ATable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATableDBO()\n" + //
					"				.setId(model.getId())\n" + //
					"				.setADate(model.getADate())\n" + //
					"				.setDescription(descriptionDBOConverter.toDBO(model.getDescription()));\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<ATableDBO> toDBO(List<ATable> models) {\n" + //
					"		if (models == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable toModel(ATableDBO dbo) {\n" + //
					"		if (dbo == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATable()\n" + //
					"				.setId(dbo.getId())\n" + //
					"				.setADate(dbo.getADate())\n" + //
					"				.setDescription(descriptionDBOConverter.toModel(dbo.getDescription()));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<ATable> toModel(List<ATableDBO> dbos) {\n" + //
					"		if (dbos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.getDomainByName("Description").addOption(new Option("ENUM:ONE,TWO,THREE"));
			TableModel table = dataModel.getTableByName("A_TABLE");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_generate_String_TableModel_Subclass {

		@Test
		void happyRun() {
			// Prepare
			String expected = "package base.pack.age.name.persistence.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"import base.pack.age.name.persistence.entity.ATableDBO;\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO converter for a_tables.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATableDBOConverter implements ToModelConverter<ATable, ATableDBO> {\n" + //
					"\n" + //
					"	public ATableDBO toDBO(ATable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		ATableDBO dbo = new ATableDBO();\n" + //
					"		dbo.setId(model.getId());\n" + //
					"		dbo.setADate(model.getADate());\n" + //
					"		dbo.setDescription(model.getDescription());\n" + //
					"		dbo.setValid(model.getValid());\n" + //
					"		return dbo;\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<ATableDBO> toDBO(List<ATable> models) {\n" + //
					"		if (models == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable toModel(ATableDBO dbo) {\n" + //
					"		if (dbo == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		ATable model = new ATable();\n" + //
					"		model.setId(dbo.getId());\n" + //
					"		model.setADate(dbo.getADate());\n" + //
					"		model.setDescription(dbo.getDescription());\n" + //
					"		model.setValid(dbo.getValid());\n" + //
					"		return model;\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<ATable> toModel(List<ATableDBO> dbos) {\n" + //
					"		if (dbos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.addOption(new Option(AbstractClassCodeGenerator.SUBCLASS));
			TableModel tableRef = dataModel.getTableByName("ANOTHER_TABLE");
			tableRef.addOption(new Option(AbstractClassCodeGenerator.SUPERCLASS));
			ColumnModel columnRef = tableRef.getColumnByName("ID");
			ColumnModel column = table.getColumnByName("ID");
			column
					.setRelation(
							new Relation(
									(ViewModel) dataModel.getMainView(),
									column,
									Direction.UP,
									0,
									columnRef,
									Direction.LEFT,
									0));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRun_withInheritedEnum() {
			// Prepare
			String expected = "package base.pack.age.name.persistence.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"import base.pack.age.name.persistence.entity.ATableDBO;\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO converter for a_tables.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATableDBOConverter implements ToModelConverter<ATable, ATableDBO> {\n" + //
					"\n" + //
					"	private final BooleanDBOConverter booleanDBOConverter;\n" + //
					"\n" + //
					"	public ATableDBO toDBO(ATable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		ATableDBO dbo = new ATableDBO();\n" + //
					"		dbo.setId(model.getId());\n" + //
					"		dbo.setADate(model.getADate());\n" + //
					"		dbo.setDescription(model.getDescription());\n" + //
					"		dbo.setValid(booleanDBOConverter.toDBO(model.getValid()));\n" + //
					"		return dbo;\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<ATableDBO> toDBO(List<ATable> models) {\n" + //
					"		if (models == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable toModel(ATableDBO dbo) {\n" + //
					"		if (dbo == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		ATable model = new ATable();\n" + //
					"		model.setId(dbo.getId());\n" + //
					"		model.setADate(dbo.getADate());\n" + //
					"		model.setDescription(dbo.getDescription());\n" + //
					"		model.setValid(booleanDBOConverter.toModel(dbo.getValid()));\n" + //
					"		return model;\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<ATable> toModel(List<ATableDBO> dbos) {\n" + //
					"		if (dbos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.getDomainByName("Boolean").addOption(new Option("ENUM:TRUE,FALSE"));
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.addOption(new Option(AbstractClassCodeGenerator.SUBCLASS));
			TableModel tableRef = dataModel.getTableByName("ANOTHER_TABLE");
			tableRef.addOption(new Option(AbstractClassCodeGenerator.SUPERCLASS));
			ColumnModel columnRef = tableRef.getColumnByName("ID");
			ColumnModel column = table.getColumnByName("ID");
			column
					.setRelation(
							new Relation(
									(ViewModel) dataModel.getMainView(),
									column,
									Direction.UP,
									0,
									columnRef,
									Direction.LEFT,
									0));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRun_withSuperClassWithReference() {
			// Prepare
			String expected =
					"package base.pack.age.name.persistence.converter;\n" //
							+ "\n" //
							+ "import java.util.List;\n" //
							+ "import java.util.stream.Collectors;\n" //
							+ "\n" //
							+ "import javax.inject.Named;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.RequiredArgsConstructor;\n" //
							+ "\n" //
							+ "import base.pack.age.name.persistence.entity.BenotherTableDBO;\n" //
							+ "import base.pack.age.name.core.model.BenotherTable;\n" //
							+ "\n" //
							+ "/**\n" //
							+ " * A DBO converter for benother_tables.\n" //
							+ " *\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n" //
							+ "@Generated\n" //
							+ "@Named\n" //
							+ "@RequiredArgsConstructor\n" //
							+ "public class BenotherTableDBOConverter implements ToModelConverter<BenotherTable, BenotherTableDBO> {\n" //
							+ "\n" //
							+ "	private final TableWithSpecialsDBOConverter tableWithSpecialsDBOConverter;\n" //
							+ "\n" //
							+ "	public BenotherTableDBO toDBO(BenotherTable model) {\n" //
							+ "		if (model == null) {\n" //
							+ "			return null;\n" //
							+ "		}\n" //
							+ "		BenotherTableDBO dbo = new BenotherTableDBO();\n" //
							+ "		dbo.setId(model.getId());\n" //
							+ "		dbo.setValid(model.getValid());\n" //
							+ "		dbo.setTableWithSpecials(tableWithSpecialsDBOConverter.toDBO(model.getTableWithSpecials()));\n" //
							+ "		dbo.setDescription(model.getDescription());\n" //
							+ "		return dbo;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public List<BenotherTableDBO> toDBO(List<BenotherTable> models) {\n" //
							+ "		if (models == null) {\n" //
							+ "			return null;\n" //
							+ "		}\n" //
							+ "		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public BenotherTable toModel(BenotherTableDBO dbo) {\n" //
							+ "		if (dbo == null) {\n" //
							+ "			return null;\n" //
							+ "		}\n" //
							+ "		BenotherTable model = new BenotherTable();\n" //
							+ "		model.setId(dbo.getId());\n" //
							+ "		model.setValid(dbo.getValid());\n" //
							+ "		model.setTableWithSpecials(tableWithSpecialsDBOConverter.toModel(dbo.getTableWithSpecials()));\n" //
							+ "		model.setDescription(dbo.getDescription());\n" //
							+ "		return model;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public List<BenotherTable> toModel(List<BenotherTableDBO> dbos) {\n" //
							+ "		if (dbos == null) {\n" //
							+ "			return null;\n" //
							+ "		}\n" //
							+ "		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.REFERENCE_MODE, "OBJECT"));
			// Run
			String returned =
					unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("BENOTHER_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void subSubClass() {
			// Prepare
			String expected = "package base.pack.age.name.persistence.converter;\n" //
					+ "\n" //
					+ "import java.util.List;\n" //
					+ "import java.util.stream.Collectors;\n" //
					+ "\n" //
					+ "import javax.inject.Named;\n" //
					+ "\n" //
					+ "import lombok.Generated;\n" //
					+ "import lombok.RequiredArgsConstructor;\n" //
					+ "\n" //
					+ "import base.pack.age.name.persistence.entity.BHeirHeirTableDBO;\n" //
					+ "import base.pack.age.name.core.model.BHeirHeirTable;\n" //
					+ "\n" //
					+ "/**\n" //
					+ " * A DBO converter for b_heir_heir_tables.\n" //
					+ " *\n" //
					+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
					+ " */\n" //
					+ "@Generated\n" //
					+ "@Named\n" //
					+ "@RequiredArgsConstructor\n" //
					+ "public class BHeirHeirTableDBOConverter implements ToModelConverter<BHeirHeirTable, BHeirHeirTableDBO> {\n" //
					+ "\n" //
					+ "	private final BReferencedTableDBOConverter bReferencedTableDBOConverter;\n" //
					+ "\n" //
					+ "	public BHeirHeirTableDBO toDBO(BHeirHeirTable model) {\n" //
					+ "		if (model == null) {\n" //
					+ "			return null;\n" //
					+ "		}\n" //
					+ "		BHeirHeirTableDBO dbo = new BHeirHeirTableDBO();\n" //
					+ "		dbo.setId(model.getId());\n" //
					+ "		dbo.setAdditionalDescription(model.getAdditionalDescription());\n" //
					+ "		dbo.setReference(bReferencedTableDBOConverter.toDBO(model.getReference()));\n" //
					+ "		dbo.setDescription(model.getDescription());\n" //
					+ "		dbo.setName(model.getName());\n" //
					+ "		return dbo;\n" //
					+ "	}\n" //
					+ "\n" //
					+ "	public List<BHeirHeirTableDBO> toDBO(List<BHeirHeirTable> models) {\n" //
					+ "		if (models == null) {\n" //
					+ "			return null;\n" //
					+ "		}\n" //
					+ "		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" //
					+ "	}\n" //
					+ "\n" //
					+ "	@Override\n" //
					+ "	public BHeirHeirTable toModel(BHeirHeirTableDBO dbo) {\n" //
					+ "		if (dbo == null) {\n" //
					+ "			return null;\n" //
					+ "		}\n" //
					+ "		BHeirHeirTable model = new BHeirHeirTable();\n" //
					+ "		model.setId(dbo.getId());\n" //
					+ "		model.setAdditionalDescription(dbo.getAdditionalDescription());\n" //
					+ "		model.setReference(bReferencedTableDBOConverter.toModel(dbo.getReference()));\n" //
					+ "		model.setDescription(dbo.getDescription());\n" //
					+ "		model.setName(dbo.getName());\n" //
					+ "		return model;\n" //
					+ "	}\n" //
					+ "\n" //
					+ "	@Override\n" //
					+ "	public List<BHeirHeirTable> toModel(List<BHeirHeirTableDBO> dbos) {\n" //
					+ "		if (dbos == null) {\n" //
					+ "			return null;\n" //
					+ "		}\n" //
					+ "		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" //
					+ "	}\n" //
					+ "\n" //
					+ "}";
			DataModel dataModel = readDataModel("Model-Inheritance.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.REFERENCE_MODE, "OBJECT"));
			// Run
			String returned =
					unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("B_HEIR_HEIR_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void subSubClassParent() {
			// Prepare
			String expected =
					"package base.pack.age.name.persistence.converter;\n" //
							+ "\n" //
							+ "import java.util.List;\n" //
							+ "import java.util.stream.Collectors;\n" //
							+ "\n" //
							+ "import javax.inject.Named;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.RequiredArgsConstructor;\n" //
							+ "\n" //
							+ "import base.pack.age.name.persistence.entity.BHeirHeirTableDBO;\n" //
							+ "import base.pack.age.name.persistence.entity.BHeirTableDBO;\n" //
							+ "import base.pack.age.name.core.model.BHeirHeirTable;\n" //
							+ "import base.pack.age.name.core.model.BHeirTable;\n" //
							+ "\n" //
							+ "/**\n" //
							+ " * A DBO converter for b_heir_tables.\n" //
							+ " *\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n" //
							+ "@Generated\n" //
							+ "@Named\n" //
							+ "@RequiredArgsConstructor\n" //
							+ "public class BHeirTableDBOConverter implements ToModelConverter<BHeirTable, BHeirTableDBO> {\n" //
							+ "\n" //
							+ "	private final BReferencedTableDBOConverter bReferencedTableDBOConverter;\n" //
							+ "\n" //
							+ "	private final BHeirHeirTableDBOConverter bHeirHeirTableDBOConverter;\n" //
							+ "\n" //
							+ "	public BHeirTableDBO toDBO(BHeirTable model) {\n" //
							+ "		if (model == null) {\n" //
							+ "			return null;\n" //
							+ "		}\n" //
							+ "		if (model instanceof BHeirHeirTable) {\n" //
							+ "			return bHeirHeirTableDBOConverter.toDBO((BHeirHeirTable) model);\n" //
							+ "		}\n" //
							+ "		BHeirTableDBO dbo = new BHeirTableDBO();\n" //
							+ "		dbo.setId(model.getId());\n" //
							+ "		dbo.setName(model.getName());\n" //
							+ "		dbo.setReference(bReferencedTableDBOConverter.toDBO(model.getReference()));\n" //
							+ "		dbo.setDescription(model.getDescription());\n" //
							+ "		return dbo;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public List<BHeirTableDBO> toDBO(List<BHeirTable> models) {\n" //
							+ "		if (models == null) {\n" //
							+ "			return null;\n" //
							+ "		}\n" //
							+ "		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public BHeirTable toModel(BHeirTableDBO dbo) {\n" //
							+ "		if (dbo == null) {\n" //
							+ "			return null;\n" //
							+ "		}\n" //
							+ "		if (dbo instanceof BHeirHeirTableDBO) {\n" //
							+ "			return bHeirHeirTableDBOConverter.toModel((BHeirHeirTableDBO) dbo);\n" //
							+ "		}\n" //
							+ "		BHeirTable model = new BHeirTable();\n" //
							+ "		model.setId(dbo.getId());\n" //
							+ "		model.setName(dbo.getName());\n" //
							+ "		model.setReference(bReferencedTableDBOConverter.toModel(dbo.getReference()));\n" //
							+ "		model.setDescription(dbo.getDescription());\n" //
							+ "		return model;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public List<BHeirTable> toModel(List<BHeirTableDBO> dbos) {\n" //
							+ "		if (dbos == null) {\n" //
							+ "			return null;\n" //
							+ "		}\n" //
							+ "		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			DataModel dataModel = readDataModel("Model-Inheritance.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.REFERENCE_MODE, "OBJECT"));
			// Run
			String returned =
					unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("B_HEIR_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_generate_String_TableModel_Superclass {

		@Test
		void happyRun() {
			// Prepare
			String expected = "package base.pack.age.name.persistence.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"import base.pack.age.name.persistence.entity.ATableDBO;\n" + //
					"import base.pack.age.name.persistence.entity.AnotherTableDBO;\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import base.pack.age.name.core.model.AnotherTable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO converter for another_tables.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class AnotherTableDBOConverter implements ToModelConverter<AnotherTable, AnotherTableDBO> {\n"
					+ //
					"\n" + //
					"	private final ATableDBOConverter aTableDBOConverter;\n" + //
					"\n" + //
					"	public AnotherTableDBO toDBO(AnotherTable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		if (model instanceof ATable) {\n" + //
					"			return aTableDBOConverter.toDBO((ATable) model);\n" + //
					"		}\n" + //
					"		return new AnotherTableDBO()\n" + //
					"				.setId(model.getId())\n" + //
					"				.setValid(model.getValid());\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<AnotherTableDBO> toDBO(List<AnotherTable> models) {\n" + //
					"		if (models == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public AnotherTable toModel(AnotherTableDBO dbo) {\n" + //
					"		if (dbo == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		if (dbo instanceof ATableDBO) {\n" + //
					"			return aTableDBOConverter.toModel((ATableDBO) dbo);\n" + //
					"		}\n" + //
					"		return new AnotherTable()\n" + //
					"				.setId(dbo.getId())\n" + //
					"				.setValid(dbo.getValid());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<AnotherTable> toModel(List<AnotherTableDBO> dbos) {\n" + //
					"		if (dbos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.addOption(new Option(AbstractClassCodeGenerator.SUBCLASS));
			TableModel tableRef = dataModel.getTableByName("ANOTHER_TABLE");
			tableRef.addOption(new Option(AbstractClassCodeGenerator.SUPERCLASS));
			ColumnModel columnRef = tableRef.getColumnByName("ID");
			ColumnModel column = table.getColumnByName("ID");
			column
					.setRelation(
							new Relation(
									(ViewModel) dataModel.getMainView(),
									column,
									Direction.UP,
									0,
									columnRef,
									Direction.LEFT,
									0));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableRef);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRun_NonSuperclassReferencesASubclass() {
			// Prepare
			String expected = "package base.pack.age.name.persistence.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"import base.pack.age.name.persistence.entity.AnotherTableDBO;\n" + //
					"import base.pack.age.name.core.model.AnotherTable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO converter for another_tables.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class AnotherTableDBOConverter implements ToModelConverter<AnotherTable, AnotherTableDBO> {\n"
					+ //
					"\n" + //
					"	public AnotherTableDBO toDBO(AnotherTable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new AnotherTableDBO()\n" + //
					"				.setId(model.getId())\n" + //
					"				.setValid(model.getValid());\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<AnotherTableDBO> toDBO(List<AnotherTable> models) {\n" + //
					"		if (models == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public AnotherTable toModel(AnotherTableDBO dbo) {\n" + //
					"		if (dbo == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new AnotherTable()\n" + //
					"				.setId(dbo.getId())\n" + //
					"				.setValid(dbo.getValid());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<AnotherTable> toModel(List<AnotherTableDBO> dbos) {\n" + //
					"		if (dbos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.addOption(new Option(AbstractClassCodeGenerator.SUBCLASS));
			TableModel tableRef = dataModel.getTableByName("ANOTHER_TABLE");
			ColumnModel columnRef = tableRef.getColumnByName("ID");
			ColumnModel column = table.getColumnByName("ID");
			column
					.setRelation(
							new Relation(
									(ViewModel) dataModel.getMainView(),
									column,
									Direction.UP,
									0,
									columnRef,
									Direction.LEFT,
									0));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableRef);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_generate_String_TableModel_WithElementListAttribute {

		@Test
		void happyRun() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".persistence.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".persistence.entity.ATableDBO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".core.model.ATable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO converter for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATableDBOConverter implements ToModelConverter<ATable, ATableDBO> {\n" + //
					"\n" + //
					"	private final DescriptionDBOConverter descriptionDBOConverter;\n" + //
					"\n" + //
					"	public ATableDBO toDBO(ATable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATableDBO()\n" + //
					"				.setId(model.getId())\n" + //
					"				.setADate(model.getADate())\n" + //
					"				.setDescription(descriptionDBOConverter.toDBO(model.getDescription()));\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<ATableDBO> toDBO(List<ATable> models) {\n" + //
					"		if (models == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable toModel(ATableDBO dbo) {\n" + //
					"		if (dbo == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATable()\n" + //
					"				.setId(dbo.getId())\n" + //
					"				.setADate(dbo.getADate())\n" + //
					"				.setDescription(descriptionDBOConverter.toModel(dbo.getDescription()));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<ATable> toModel(List<ATableDBO> dbos) {\n" + //
					"		if (dbos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.getDomainByName("Description").addOption(new Option("ENUM:ONE,TWO,THREE"));
			TableModel table = dataModel.getTableByName("A_TABLE");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_generate_String_TableModel_TwoReferencesToTheSameTargetTable {

		@Test
		void happyRun() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".persistence.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"import base.pack.age.name.persistence.entity.BTableDBO;\n" + //
					"import base.pack.age.name.core.model.BTable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO converter for b_tables.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class BTableDBOConverter implements ToModelConverter<BTable, BTableDBO> {\n" + //
					"\n" + //
					"	private final BAnotherTableDBOConverter bAnotherTableDBOConverter;\n" + //
					"\n" + //
					"	public BTableDBO toDBO(BTable model) {\n" + //
					"		if (model == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new BTableDBO()\n" + //
					"				.setId(model.getId())\n" + //
					"				.setRefB0(bAnotherTableDBOConverter.toDBO(model.getRefB0()))\n" + //
					"				.setRefB1(bAnotherTableDBOConverter.toDBO(model.getRefB1()));\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<BTableDBO> toDBO(List<BTable> models) {\n" + //
					"		if (models == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public BTable toModel(BTableDBO dbo) {\n" + //
					"		if (dbo == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new BTable()\n" + //
					"				.setId(dbo.getId())\n" + //
					"				.setRefB0(bAnotherTableDBOConverter.toModel(dbo.getRefB0()))\n" + //
					"				.setRefB1(bAnotherTableDBOConverter.toModel(dbo.getRefB1()));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<BTable> toModel(List<BTableDBO> dbos) {\n" + //
					"		if (dbos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			dataModel.addOption(new Option("REFERENCE_MODE", "OBJECT"));
			TableModel table = dataModel.getTableByName("B_TABLE");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_generate_String_TableModel_MemberList {

		@Nested
		class TheParent {

			private DataModel readDataModel(String fileName) {
				ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
				return reader.read("src/test/resources/examples/dm/" + fileName);
			}

			@Test
			void happyRun() {
				// Prepare
				String expected = "package de.ollie.bookstore.persistence.converter;\n" + //
						"\n" + //
						"import java.util.List;\n" + //
						"import java.util.stream.Collectors;\n" + //
						"\n" + //
						"import javax.inject.Named;\n" + //
						"\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n" + //
						"import de.ollie.bookstore.persistence.entity.BookDBO;\n" + //
						"import de.ollie.bookstore.core.model.Book;\n" + //
						"\n" + //
						"/**\n" + //
						" * A DBO converter for books.\n" + //
						" *\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"@Named\n" + //
						"@RequiredArgsConstructor\n" + //
						"public class BookDBOConverter implements ToModelConverter<Book, BookDBO> {\n" + //
						"\n" + //
						"	private final PublicationTypeDBOConverter publicationTypeDBOConverter;\n" + //
						"	private final ChapterDBOConverter chapterDBOConverter;\n" + //
						"\n" + //
						"	public BookDBO toDBO(Book model) {\n" + //
						"		if (model == null) {\n" + //
						"			return null;\n" + //
						"		}\n" + //
						"		return new BookDBO()\n" + //
						"				.setId(model.getId())\n" + //
						"				.setIsbn(model.getIsbn())\n" + //
						"				.setPublicationType(publicationTypeDBOConverter.toDBO(model.getPublicationType()))\n"
						+ //
						"				.setTitle(model.getTitle())\n" + //
						"				.setChapters(chapterDBOConverter.toDBO(model.getChapters()));\n" + //
						"	}\n" + //
						"\n" + //
						"	public List<BookDBO> toDBO(List<Book> models) {\n" + //
						"		if (models == null) {\n" + //
						"			return null;\n" + //
						"		}\n" + //
						"		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public Book toModel(BookDBO dbo) {\n" + //
						"		if (dbo == null) {\n" + //
						"			return null;\n" + //
						"		}\n" + //
						"		return new Book()\n" + //
						"				.setId(dbo.getId())\n" + //
						"				.setIsbn(dbo.getIsbn())\n" + //
						"				.setPublicationType(publicationTypeDBOConverter.toModel(dbo.getPublicationType()))\n"
						+ //
						"				.setTitle(dbo.getTitle())\n" + //
						"				.setChapters(chapterDBOConverter.toModel(dbo.getChapters()));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public List<Book> toModel(List<BookDBO> dbos) {\n" + //
						"		if (dbos == null) {\n" + //
						"			return null;\n" + //
						"		}\n" + //
						"		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" + //
						"	}\n" + //
						"\n" + //
						"}";
				DataModel dataModel = readDataModel("Example-BookStore.xml");
				dataModel.addOption(new Option("REFERENCE_MODE", "OBJECT"));
				TableModel table = dataModel.getTableByName("BOOK");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class TheMember {

			private DataModel readDataModel(String fileName) {
				ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
				return reader.read("src/test/resources/examples/dm/" + fileName);
			}

			@Test
			void happyRun() {
				// Prepare
				String expected =
						"package de.ollie.bookstore.persistence.converter;\n" //
								+ "\n" //
								+ "import java.util.List;\n" //
								+ "import java.util.stream.Collectors;\n" //
								+ "\n" //
								+ "import javax.inject.Named;\n" //
								+ "\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.RequiredArgsConstructor;\n" //
								+ "\n" //
								+ "import de.ollie.bookstore.persistence.entity.ChapterDBO;\n" //
								+ "import de.ollie.bookstore.core.model.Chapter;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * A DBO converter for chapters.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Generated\n" //
								+ "@Named\n" //
								+ "@RequiredArgsConstructor\n" //
								+ "public class ChapterDBOConverter implements ToModelConverter<Chapter, ChapterDBO> {\n" //
								+ "\n" //
								+ "	public ChapterDBO toDBO(Chapter model) {\n" //
								+ "		if (model == null) {\n" //
								+ "			return null;\n" //
								+ "		}\n" //
								+ "		return new ChapterDBO()\n" //
								+ "				.setId(model.getId())\n" //
								+ "				.setContent(model.getContent())\n" //
								+ "				.setSortOrder(model.getSortOrder())\n" //
								+ "				.setSummary(model.getSummary())\n" //
								+ "				.setTitle(model.getTitle());\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	public List<ChapterDBO> toDBO(List<Chapter> models) {\n" //
								+ "		if (models == null) {\n" //
								+ "			return null;\n" //
								+ "		}\n" //
								+ "		return models.stream().map(this::toDBO).collect(Collectors.toList());\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	public Chapter toModel(ChapterDBO dbo) {\n" //
								+ "		if (dbo == null) {\n" //
								+ "			return null;\n" //
								+ "		}\n" //
								+ "		return new Chapter()\n" //
								+ "				.setId(dbo.getId())\n" //
								+ "				.setContent(dbo.getContent())\n" //
								+ "				.setSortOrder(dbo.getSortOrder())\n" //
								+ "				.setSummary(dbo.getSummary())\n" //
								+ "				.setTitle(dbo.getTitle());\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	public List<Chapter> toModel(List<ChapterDBO> dbos) {\n" //
								+ "		if (dbos == null) {\n" //
								+ "			return null;\n" //
								+ "		}\n" //
								+ "		return dbos.stream().map(this::toModel).collect(Collectors.toList());\n" //
								+ "	}\n" //
								+ "\n" //
								+ "}";
				DataModel dataModel = readDataModel("Example-BookStore.xml");
				dataModel.addOption(new Option("REFERENCE_MODE", "OBJECT"));
				TableModel table = dataModel.getTableByName("CHAPTER");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}