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
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.GlobalIdType;
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
public class GeneratedModelClassCodeGeneratorTest {
	
	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private GeneratedModelClassCodeGenerator unitUnderTest;

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected("core.model");
			DataModel dataModel = readDataModel("Model.xml");
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
							"import lombok.Generated;\n" + //
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
					"@Generated\n" + //
					"public abstract class GeneratedATable<T extends ATable> {\n" + //
					"\n" + //
					"	public static final String ID = \"ID\";\n" + //
					"	public static final String ADATE = \"ADATE\";\n" + //
					"	public static final String DESCRIPTION = \"DESCRIPTION\";\n" + //
					"\n" + //
					"	private Long id;\n" + //
					"	private LocalDate aDate;\n" + //
					"	private String description;\n" + //
					"\n" + //
					"	protected abstract T self();\n" + //
					"\n" + //
					"	public T setId(Long id) {\n" + //
					"		this.id = id;\n" + //
					"		return self();\n" + //
					"	}\n" + //
					"\n" + //
					"	public T setADate(LocalDate aDate) {\n" + //
					"		this.aDate = aDate;\n" + //
					"		return self();\n" + //
					"	}\n" + //
					"\n" + //
					"	public T setDescription(String description) {\n" + //
					"		this.description = description;\n" + //
					"		return self();\n" + //
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
			return "package " + BASE_PACKAGE_NAME + "." + packageName + ";\n" //
					+ "\n" //
					+ "import java.time.LocalDate;\n" //
					+ "\n" //
					+ "import lombok.AllArgsConstructor;\n" //
					+ "import lombok.Builder;\n" //
					+ "import lombok.NoArgsConstructor;\n" //
					+ "import lombok.Data;\n" //
					+ "import lombok.Generated;\n" //
					+ "\n" //
					+ "/**\n" //
					+ " * A model for a_tables.\n" //
					+ " *\n" //
					+ " * " + AbstractCodeGenerator.GENERATED_CODE + "\n" //
					+ " */\n" //
					+ "@Builder\n" //
					+ "@AllArgsConstructor\n" //
					+ "@NoArgsConstructor\n" //
					+ "@Data\n" //
					+ "@Generated\n" //
					+ "public abstract class GeneratedATable<T extends ATable> {\n" //
					+ "\n" //
					+ "	public static final String ID = \"ID\";\n" //
					+ "	public static final String ADATE = \"ADATE\";\n" //
					+ "	public static final String DESCRIPTION = \"DESCRIPTION\";\n" //
					+ "\n" //
					+ "	private Long id;\n" //
					+ "	private LocalDate aDate;\n" //
					+ "	private String description;\n" //
					+ "\n" //
					+ "	protected abstract T self();\n" //
					+ "\n" //
					+ "	public T setId(Long id) {\n" //
					+ "		this.id = id;\n" //
					+ "		return self();\n" //
					+ "	}\n" //
					+ "\n" //
					+ "	public T setADate(LocalDate aDate) {\n" //
					+ "		this.aDate = aDate;\n" //
					+ "		return self();\n" //
					+ "	}\n" //
					+ "\n" //
					+ "	public T setDescription(String description) {\n" //
					+ "		this.description = description;\n" //
					+ "		return self();\n" //
					+ "	}\n" //
					+ "\n" //
					+ "}";
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
				String expected =
						"package base.pack.age.name.core.model;\n" //
								+ "\n" //
								+ "import java.time.LocalDate;\n" //
								+ "\n" //
								+ "import lombok.Data;\n" //
								+ "import lombok.EqualsAndHashCode;\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.ToString;\n" //
								+ "import lombok.experimental.Accessors;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * A model for a_tables.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Accessors(chain = true)\n" //
								+ "@Data\n" //
								+ "@EqualsAndHashCode(callSuper = true)\n" //
								+ "@Generated\n" //
								+ "@ToString(callSuper = true)\n" //
								+ "public abstract class GeneratedATable<T extends ATable> extends AnotherTable {\n" //
								+ "\n" //
								+ "	public static final String ADATE = \"ADATE\";\n" //
								+ "	public static final String DESCRIPTION = \"DESCRIPTION\";\n" //
								+ "\n" //
								+ "	private LocalDate aDate;\n" //
								+ "	private String description;\n" //
								+ "\n" //
								+ "	public T setADate(LocalDate aDate) {\n" //
								+ "		this.aDate = aDate;\n" //
								+ "		return (T) self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	public T setDescription(String description) {\n" //
								+ "		this.description = description;\n" //
								+ "		return (T) self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "}";
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
			void modelWithSubSubclass() {
				// Prepare
				String expected =
						"package base.pack.age.name.core.model;\n" //
								+ "\n" //
								+ "import lombok.Data;\n" //
								+ "import lombok.EqualsAndHashCode;\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.ToString;\n" //
								+ "import lombok.experimental.Accessors;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * A model for b_heir_heir_tables.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Accessors(chain = true)\n" //
								+ "@Data\n" //
								+ "@EqualsAndHashCode(callSuper = true)\n" //
								+ "@Generated\n" //
								+ "@ToString(callSuper = true)\n" //
								+ "public abstract class GeneratedBHeirHeirTable<T extends BHeirHeirTable> extends BHeirTable {\n" //
								+ "\n" //
								+ "	public static final String ADDITIONALDESCRIPTION = \"ADDITIONALDESCRIPTION\";\n" //
								+ "\n" //
								+ "	private String additionalDescription;\n" //
								+ "\n" //
								+ "	public T setAdditionalDescription(String additionalDescription) {\n" //
								+ "		this.additionalDescription = additionalDescription;\n" //
								+ "		return (T) self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "}";
				DataModel dataModel = readDataModel("Model-Inheritance.xml");
				TableModel table = dataModel.getTableByName("B_HEIR_HEIR_TABLE");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void modelNoSubclass() {
				// Prepare
				String expected =
						"package base.pack.age.name.core.model;\n" //
								+ "\n" //
								+ "import java.time.LocalDate;\n" //
								+ "\n" //
								+ "import lombok.Data;\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.experimental.Accessors;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * A model for a_tables.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Accessors(chain = true)\n" //
								+ "@Data\n" //
								+ "@Generated\n" //
								+ "public abstract class GeneratedATable<T extends ATable> {\n" //
								+ "\n" //
								+ "	public static final String ID = \"ID\";\n" //
								+ "	public static final String ADATE = \"ADATE\";\n" //
								+ "	public static final String DESCRIPTION = \"DESCRIPTION\";\n" //
								+ "\n" //
								+ "	private Long id;\n" //
								+ "	private LocalDate aDate;\n" //
								+ "	private String description;\n" //
								+ "\n" // //
								+ "	protected abstract T self();\n" //
								+ "\n" //
								+ "	public T setId(Long id) {\n" //
								+ "		this.id = id;\n" //
								+ "		return self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	public T setADate(LocalDate aDate) {\n" //
								+ "		this.aDate = aDate;\n" //
								+ "		return self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	public T setDescription(String description) {\n" //
								+ "		this.description = description;\n" //
								+ "		return self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "}";
				DataModel dataModel = readDataModel("Model.xml");
				// Run
				String returned =
						unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class InitWith {

			@Test
			void simpleClass() {
				// Prepare
				String expected = "package base.pack.age.name.core.model;\n" //
						+ "\n" //
						+ "import lombok.Data;\n" //
						+ "import lombok.Generated;\n" //
						+ "import lombok.experimental.Accessors;\n" //
						+ "\n" //
						+ "/**\n" //
						+ " * A model for table_with_grid_fieldss.\n" //
						+ " *\n" //
						+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
						+ " */\n" //
						+ "@Accessors(chain = true)\n" //
						+ "@Data\n" //
						+ "@Generated\n" //
						+ "public abstract class GeneratedTableWithGridFields<T extends TableWithGridFields> {\n" //
						+ "\n" //
						+ "	public static final String ID = \"ID\";\n" //
						+ "	public static final String ENUMFIELD = \"ENUMFIELD\";\n" //
						+ "	public static final String FLAG = \"FLAG\";\n" //
						+ "	public static final String LONGTEXT = \"LONGTEXT\";\n" //
						+ "\n" //
						+ "	private Long id;\n" //
						+ "	private EnumType enumField = EnumType.ONE;\n" //
						+ "	private Boolean flag = true;\n" //
						+ "	private String longtext = \"a long text\";\n" //
						+ "\n" //
						+ "	protected abstract T self();\n" //
						+ "\n" //
						+ "	public T setId(Long id) {\n" //
						+ "		this.id = id;\n" //
						+ "		return self();\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public T setEnumField(EnumType enumField) {\n" //
						+ "		this.enumField = enumField;\n" //
						+ "		return self();\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public T setFlag(Boolean flag) {\n" //
						+ "		this.flag = flag;\n" //
						+ "		return self();\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public T setLongtext(String longtext) {\n" //
						+ "		this.longtext = longtext;\n" //
						+ "		return self();\n" //
						+ "	}\n" //
						+ "\n" //
						+ "}";
				DataModel dataModel = readDataModel("Model.xml");
				// Run
				TableModel table = dataModel.getTableByName("TABLE_WITH_GRID_FIELDS");
				table.getColumnByName("EnumField").addOption(new Option("INIT_WITH", "EnumType.ONE"));
				table.getColumnByName("Flag").addOption(new Option("INIT_WITH", "true"));
				table.getColumnByName("Longtext").addOption(new Option("INIT_WITH", "\"a long text\""));
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

	@Nested
	class List_Composition {

		@Nested
		class TheParent {

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
				String s = "package de.ollie.bookstore.core.model;\n" + //
						"\n" + //
						"import java.util.ArrayList;\n" + //
						"\n" + //
						"import java.util.List;\n" + //
						"\n" + //
						"import lombok.Data;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.experimental.Accessors;\n" + //
						"\n" + //
						"/**\n" + //
						" * A model for books.\n" + //
						" *\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Accessors(chain = true)\n" + //
						"@Data\n" + //
						"@Generated\n" + //
						"public abstract class GeneratedBook<T extends Book> {\n" + //
						"\n" + //
						"	public static final String ID = \"ID\";\n" + //
						"	public static final String ISBN = \"ISBN\";\n" + //
						"	public static final String PUBLICATIONTYPE = \"PUBLICATIONTYPE\";\n" + //
						"	public static final String TITLE = \"TITLE\";\n" + //
						"	public static final String CHAPTERS = \"CHAPTERS\";\n" + //
						"\n" + //
						"	private long id;\n" + //
						"	private String isbn;\n" + //
						"	private PublicationType publicationType;\n" + //
						"	private String title;\n" + //
						"	private List<Chapter> chapters = new ArrayList<>();\n" + //
						"\n" + //
						"	protected abstract T self();\n" + //
						"\n" + //
						"	public T setId(long id) {\n" + //
						"		this.id = id;\n" + //
						"		return self();\n" + //
						"	}\n" + //
						"\n" + //
						"	public T setIsbn(String isbn) {\n" + //
						"		this.isbn = isbn;\n" + //
						"		return self();\n" + //
						"	}\n" + //
						"\n" + //
						"	public T setPublicationType(PublicationType publicationType) {\n" + //
						"		this.publicationType = publicationType;\n" + //
						"		return self();\n" + //
						"	}\n" + //
						"\n" + //
						"	public T setTitle(String title) {\n" + //
						"		this.title = title;\n" + //
						"		return self();\n" + //
						"	}\n" + //
						"\n" + //
						"	public T setChapters(List<Chapter> chapters) {\n" + //
						"		this.chapters = chapters;\n" + //
						"		return self();\n" + //
						"	}\n" + //
						"\n" + //
						"}";
				return s;
			}

		}

		@Nested
		class TheMember {

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpected(false, false);
				DataModel dataModel = readDataModel("Example-BookStore.xml", EXAMPLE_XMLS);
				// Run
				String returned =
						unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("CHAPTER"));
				// Check
				assertEquals(expected, returned);
			}

			private String getExpected(boolean isSuperclass, boolean isExtends) {
				String s =
						"package de.ollie.bookstore.core.model;\n" //
								+ "\n" //
								+ "import lombok.ToString;\n" //
								+ "\n" //
								+ "import lombok.Data;\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.experimental.Accessors;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * A model for chapters.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Accessors(chain = true)\n" //
								+ "@Data\n" //
								+ "@Generated\n" //
								+ "public abstract class GeneratedChapter<T extends Chapter> {\n" //
								+ "\n" //
								+ "	public static final String ID = \"ID\";\n" //
								+ "	public static final String CONTENT = \"CONTENT\";\n" //
								+ "	public static final String SORTORDER = \"SORTORDER\";\n" //
								+ "	public static final String SUMMARY = \"SUMMARY\";\n" //
								+ "	public static final String TITLE = \"TITLE\";\n" //
								+ "\n" //
								+ "	private long id;\n" //
								+ "	@ToString.Exclude\n" //
								+ "	private String content;\n" //
								+ "	private int sortOrder;\n" //
								+ "	private String summary;\n" //
								+ "	private String title;\n" //
								+ "\n" //
								+ "	protected abstract T self();\n" //
								+ "\n" //
								+ "	public T setId(long id) {\n" //
								+ "		this.id = id;\n" //
								+ "		return self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	public T setContent(String content) {\n" //
								+ "		this.content = content;\n" //
								+ "		return self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	public T setSortOrder(int sortOrder) {\n" //
								+ "		this.sortOrder = sortOrder;\n" //
								+ "		return self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	public T setSummary(String summary) {\n" //
								+ "		this.summary = summary;\n" //
								+ "		return self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	public T setTitle(String title) {\n" //
								+ "		this.title = title;\n" //
								+ "		return self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "}";
				return s;
			}

		}

		@Nested
		class GlobalIdUUID {

			@Test
			void globalIdWithNoUUIDConfiguration() {
				// Prepare
				DataModel dataModel = readDataModel("Model.xml");
				String expected =
						"package base.pack.age.name.core.model;\n" //
								+ "\n" //
								+ "import lombok.Data;\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.experimental.Accessors;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * A model for table_with_uuids.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Accessors(chain = true)\n" //
								+ "@Data\n" //
								+ "@Generated\n" //
								+ "public abstract class GeneratedTableWithUuid<T extends TableWithUuid> {\n" //
								+ "\n" //
								+ "	public static final String ID = \"ID\";\n" //
								+ "	public static final String GLOBALID = \"GLOBALID\";\n" //
								+ "\n" //
								+ "	private Long id;\n" //
								+ "	private String globalId;\n" //
								+ "\n" //
								+ "	protected abstract T self();\n" //
								+ "\n" //
								+ "	public T setId(Long id) {\n" //
								+ "		this.id = id;\n" //
								+ "		return self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	public T setGlobalId(String globalId) {\n" //
								+ "		this.globalId = globalId;\n" //
								+ "		return self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "}";
				// Run
				String returned =
						unitUnderTest
								.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("TABLE_WITH_UUID"));
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void globalIdWithModelUUIDConfigurationInColumn() {
				// Prepare
				DataModel dataModel = readDataModel("Model.xml");
				String expected =
						"package base.pack.age.name.core.model;\n" //
								+ "\n" //
								+ "import java.util.UUID;\n" //
								+ "\n" //
								+ "import lombok.Data;\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.experimental.Accessors;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * A model for table_with_uuids.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Accessors(chain = true)\n" //
								+ "@Data\n" //
								+ "@Generated\n" //
								+ "public abstract class GeneratedTableWithUuid<T extends TableWithUuid> {\n" //
								+ "\n" //
								+ "	public static final String ID = \"ID\";\n" //
								+ "	public static final String GLOBALID = \"GLOBALID\";\n" //
								+ "\n" //
								+ "	private Long id;\n" //
								+ "	private UUID globalId = UUID.randomUUID();\n" //
								+ "\n" //
								+ "	protected abstract T self();\n" //
								+ "\n" //
								+ "	public T setId(Long id) {\n" //
								+ "		this.id = id;\n" //
								+ "		return self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	public T setGlobalId(UUID globalId) {\n" //
								+ "		this.globalId = globalId;\n" //
								+ "		return self();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "}";
				dataModel
						.getTableByName("TABLE_WITH_UUID")
						.getColumnByName("GLOBAL_ID")
						.setParameters(AbstractModelCodeGenerator.GLOBAL_ID + ":" + GlobalIdType.UUID);
				System.out
						.println(
								"\n\n" + dataModel
										.getTableByName("TABLE_WITH_UUID")
										.getColumnByName("GLOBAL_ID")
										.getOptionByName(AbstractModelCodeGenerator.GLOBAL_ID)
										.getParameter());

				// Run
				String returned =
						unitUnderTest
								.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("TABLE_WITH_UUID"));
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}