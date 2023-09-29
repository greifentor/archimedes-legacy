package archimedes.codegenerators.service;

import static archimedes.codegenerators.DataModelReader.EXAMPLE_XMLS;
import static archimedes.codegenerators.DataModelReader.readDataModel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.DataModelReader;
import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator;
import archimedes.legacy.scheme.Relation;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.scheme.Option;
import corent.base.Direction;

@ExtendWith(MockitoExtension.class)
class ModelClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private ModelClassCodeGenerator unitUnderTest;

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected("core.model");
			DataModel dataModel = DataModelReader.readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String packageName) {
			return getExpected(null, packageName, false);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment) {
			return getExpected(prefix, packageName, suppressComment, false, false);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment,
				boolean descriptionNotNull, boolean refMode) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" + //
							"\n" + //
							"import java.time.LocalDate;\n" + //
							"\n" + //
							"import lombok.Data;\n" + //
							"import lombok.EqualsAndHashCode;\n" + //
							"import lombok.Generated;\n" + //
							"import lombok.ToString;\n" + //
							"import lombok.experimental.Accessors;\n" + //
							"\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A model for a_tables.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s += "@Accessors(chain = true)\n" + //
					"@Data\n" + //
					"@EqualsAndHashCode(callSuper = true)\n" + //
					"@Generated\n" + //
					"@ToString(callSuper = true)\n" + //
					"public class ATable extends GeneratedATable {\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable setId(Long id) {\n" + //
					"		super.setId(id);\n" + //
					"		return this;\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable setADate(LocalDate aDate) {\n" + //
					"		super.setADate(aDate);\n" + //
					"		return this;\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable setDescription(String description) {\n" + //
					"		super.setDescription(description);\n" + //
					"		return this;\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectAndAlternatePackageName() {
			// Prepare
			String alternatePackageName = "alternate.name";
			String expected = getExpected(alternatePackageName);
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(ServiceNameGenerator.ALTERNATE_MODEL_PACKAGE_NAME, alternatePackageName));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectAndTechnicalPackageName() {
			// Prepare
			String technicalContextName = "technical";
			String expected = getExpected(technicalContextName + ".core.model");
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.getTableByName("A_TABLE")
					.addOption(new Option(NameGenerator.TECHNICAL_CONTEXT, technicalContextName));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithModuleForTable() {
			// Prepare
			String prefix = "prefix";
			String expected = getExpected(prefix, "core.model", false);
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.addOption(new Option(PersistenceJPANameGenerator.MODULE, prefix));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithSuppressedComment() {
			// Prepare
			String expected = getExpected(null, "core.model", true);
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithNotNullField() {
			// Prepare
			String expected = getExpected(null, "core.model", false, true, false);
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.getColumnByName("Description").setNotNull(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		private String getExpectedPOJOModeBuilder(String packageName, String generatedValue) {
			return "package " + BASE_PACKAGE_NAME + "." + packageName + ";\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import lombok.AllArgsConstructor;\n" + //
					"import lombok.Builder;\n" + //
					"import lombok.NoArgsConstructor;\n" + //
					"import lombok.Data;\n" + //
					"import lombok.EqualsAndHashCode;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.ToString;\n" + //
					"\n" + //
					"/**\n" + //
					" * A model for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Builder\n" + //
					"@AllArgsConstructor\n" + //
					"@NoArgsConstructor\n" + //
					"@Data\n" + //
					"@EqualsAndHashCode(callSuper = true)\n" + //
					"@Generated\n" + //
					"@ToString(callSuper = true)\n" + //
					"public class ATable extends GeneratedATable {\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable setId(Long id) {\n" + //
					"		super.setId(id);\n" + //
					"		return this;\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable setADate(LocalDate aDate) {\n" + //
					"		super.setADate(aDate);\n" + //
					"		return this;\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable setDescription(String description) {\n" + //
					"		super.setDescription(description);\n" + //
					"		return this;\n" + //
					"	}\n" + //
					"\n" + //
					"}";
		}

		@Test
		void happyRunForASimpleObjectPOJOModeBUILDWithIDENTITY() {
			// Prepare
			String expected = getExpectedPOJOModeBuilder("core.model", "IDENTITY");
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.addOption(
							new Option(
									AbstractClassCodeGenerator.POJO_MODE,
									AbstractClassCodeGenerator.POJO_MODE_BUILDER));
			// Run
			dataModel
					.getTableByName("A_TABLE")
					.getColumnByName("ID")
					.addOption(new Option(AbstractClassCodeGenerator.AUTO_INCREMENT, "IDENTITY"));
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectPOJOModeBUILDWithSEQUENCE() {
			// Prepare
			String expected = getExpectedPOJOModeBuilder("core.model", "SEQUENCE");
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.addOption(
							new Option(
									AbstractClassCodeGenerator.POJO_MODE,
									AbstractClassCodeGenerator.POJO_MODE_BUILDER));
			// Run
			dataModel
					.getTableByName("A_TABLE")
					.getColumnByName("ID")
					.addOption(new Option(AbstractClassCodeGenerator.AUTO_INCREMENT, "SEQUENCE"));
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Nested
		class Subclass {

			@Test
			void modelWithSubclass() {
				// Prepare
				String expected = "package base.pack.age.name.core.model;\n" + //
						"\n" + //
						"import java.time.LocalDate;\n" + //
						"\n" + //
						"import lombok.Data;\n" + //
						"import lombok.EqualsAndHashCode;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.ToString;\n" + //
						"import lombok.experimental.Accessors;\n" + //
						"\n" + //
						"/**\n" + //
						" * A model for a_tables.\n" + //
						" *\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Accessors(chain = true)\n" + //
						"@Data\n" + //
						"@EqualsAndHashCode(callSuper = true)\n" + //
						"@Generated\n" + //
						"@ToString(callSuper = true)\n" + //
						"public class ATable extends GeneratedATable {\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public ATable setId(Long id) {\n" + //
						"		super.setId(id);\n" + //
						"		return this;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public ATable setADate(LocalDate aDate) {\n" + //
						"		super.setADate(aDate);\n" + //
						"		return this;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public ATable setDescription(String description) {\n" + //
						"		super.setDescription(description);\n" + //
						"		return this;\n" + //
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
				String returned =
						unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void modelNoSubclass() {
				// Prepare
				String expected =
						"package base.pack.age.name.core.model;\n" + //
								"\n" + //
								"import java.time.LocalDate;\n" + //
								"\n" + //
								"import lombok.Data;\n" + //
								"import lombok.EqualsAndHashCode;\n" + //
								"import lombok.Generated;\n" + //
								"import lombok.ToString;\n" + //
								"import lombok.experimental.Accessors;\n" + //
								"\n" + //
								"/**\n" + //
								" * A model for a_tables.\n" + //
								" *\n" + //
								" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
								" */\n" + //
								"@Accessors(chain = true)\n" + //
								"@Data\n" + //
								"@EqualsAndHashCode(callSuper = true)\n" + //
								"@Generated\n" + //
								"@ToString(callSuper = true)\n" + //
								"public class ATable extends GeneratedATable {\n" + //
								"\n" + //
								"	@Override\n" + //
								"	public ATable setId(Long id) {\n" + //
								"		super.setId(id);\n" + //
								"		return this;\n" + //
								"	}\n" + //
								"\n" + //
								"	@Override\n" + //
								"	public ATable setADate(LocalDate aDate) {\n" + //
								"		super.setADate(aDate);\n" + //
								"		return this;\n" + //
								"	}\n" + //
								"\n" + //
								"	@Override\n" + //
								"	public ATable setDescription(String description) {\n" + //
								"		super.setDescription(description);\n" + //
								"		return this;\n" + //
								"	}\n" + //
								"\n" + //
								"}";
				DataModel dataModel = readDataModel("Model.xml");
				// Run
				String returned =
						unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class WithInterface {

			@Test
			void twoInterfaces() {
				String expected = "package " + BASE_PACKAGE_NAME + ".core.model;\n" + //
						"\n" + //
						"import java.time.LocalDate;\n" + //
						"\n" + //
						"import interface1.package.name.TheInterface1;\n" + //
						"import interface2.package.name.TheSecondInterface;\n" + //
						"\n" + //
						"import lombok.Data;\n" + //
						"import lombok.EqualsAndHashCode;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.ToString;\n" + //
						"import lombok.experimental.Accessors;\n" + //
						"\n" + //
						"/**\n" + //
						" * A model for a_tables.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n" + //
						"@Accessors(chain = true)\n" + //
						"@Data\n" + //
						"@EqualsAndHashCode(callSuper = true)\n" + //
						"@Generated\n" + //
						"@ToString(callSuper = true)\n" + //
						"public class ATable extends GeneratedATable implements TheInterface1, TheSecondInterface {\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public ATable setId(Long id) {\n" + //
						"		super.setId(id);\n" + //
						"		return this;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public ATable setADate(LocalDate aDate) {\n" + //
						"		super.setADate(aDate);\n" + //
						"		return this;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public ATable setDescription(String description) {\n" + //
						"		super.setDescription(description);\n" + //
						"		return this;\n" + //
						"	}\n" + //
						"\n" + //
						"}";
				DataModel dataModel = readDataModel("Model.xml");
				TableModel table = dataModel.getTableByName("A_TABLE");
				table.addOption(new Option("IMPLEMENTS", "interface2.package.name.TheSecondInterface"));
				table.addOption(new Option("IMPLEMENTS", "interface1.package.name.TheInterface1"));
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

	@Nested
	class List_Composition_Parent {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected(false, false);
			DataModel dataModel = readDataModel("Example-BookStore.xml", EXAMPLE_XMLS);
			// Run
			String returned =
					unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("BOOK"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(boolean isSuperclass, boolean isExtends) {
			String s =
					"package de.ollie.bookstore.core.model;\n" + //
							"\n" + //
							"import java.util.List;\n" + //
							"\n" + //
							"import lombok.Data;\n" + //
							"import lombok.EqualsAndHashCode;\n" + //
							"import lombok.Generated;\n" + //
							"import lombok.ToString;\n" + //
							"import lombok.experimental.Accessors;\n" + //
							"\n" + //
							"/**\n" + //
							" * A model for books.\n" + //
							" *\n" + //
							" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
							" */\n" + //
							"@Accessors(chain = true)\n" + //
							"@Data\n" + //
							"@EqualsAndHashCode(callSuper = true)\n" + //
							"@Generated\n" + //
							"@ToString(callSuper = true)\n" + //
							"public class Book extends GeneratedBook {\n" + //
							"\n" + //
							"	@Override\n" + //
							"	public Book setId(long id) {\n" + //
							"		super.setId(id);\n" + //
							"		return this;\n" + //
							"	}\n" + //
							"\n" + //
							"	@Override\n" + //
							"	public Book setIsbn(String isbn) {\n" + //
							"		super.setIsbn(isbn);\n" + //
							"		return this;\n" + //
							"	}\n" + //
							"\n" + //
							"	@Override\n" + //
							"	public Book setTitle(String title) {\n" + //
							"		super.setTitle(title);\n" + //
							"		return this;\n" + //
							"	}\n" + //
							"\n" + //
							"	@Override\n" + //
							"	public Book setChapters(List<Chapter> chapters) {\n" + //
							"		super.setChapters(chapters);\n" + //
							"		return this;\n" + //
							"	}\n" + //
							"\n" + //
							"}";
			return s;
		}

	}

}