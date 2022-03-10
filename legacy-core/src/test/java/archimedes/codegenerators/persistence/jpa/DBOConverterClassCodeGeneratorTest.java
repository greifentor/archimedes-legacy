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

}