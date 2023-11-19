package archimedes.codegenerators.persistence.jpa;

import static archimedes.codegenerators.DataModelReader.EXAMPLE_XMLS;
import static archimedes.codegenerators.DataModelReader.readDataModel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.Relation;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.scheme.Option;
import corent.base.Direction;

@ExtendWith(MockitoExtension.class)
public class DBOClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private DBOClassCodeGenerator unitUnderTest;

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected(false, false);
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.getDomainByName("Description")
					.addOption(new Option(AbstractClassCodeGenerator.ENUM, "ONE,TWO,THREE"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(boolean isSuperclass, boolean isExtends) {
			String s = "package base.pack.age.name.persistence.entity;\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import javax.persistence.Column;\n" + //
					"import javax.persistence.Entity;\n" + //
					"import javax.persistence.EnumType;\n" + //
					"import javax.persistence.Enumerated;\n" + //
					"import javax.persistence.Id;\n";
			if (isSuperclass) {
				s += "import javax.persistence.Inheritance;\n" + //
						"import javax.persistence.InheritanceType;\n";
			}
			s += (isExtends ? "import javax.persistence.PrimaryKeyJoinColumn;\n" : "") + //
					"import javax.persistence.Table;\n" + //
					"\n" + //
					"import lombok.Data;\n" + //
					(isExtends ? "import lombok.EqualsAndHashCode;\n" : "") + //
					"import lombok.Generated;\n" + //
					(isExtends ? "import lombok.ToString;\n" : "") + //
					"import lombok.experimental.Accessors;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO for a_tables.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Accessors(chain = true)\n" + //
					"@Data\n" + //
					"@Generated\n" + //
					"@Entity(name = \"ATable\")\n" + //
					(isExtends ? "@EqualsAndHashCode(callSuper = true)\n" : "") + //
					(isExtends ? "@PrimaryKeyJoinColumn(name = \"ID\")\n" : "") + //
					(isSuperclass ? "@Inheritance(strategy = InheritanceType.JOINED)\n" : "") + //
					"@Table(name = \"A_TABLE\")\n" + //
					(isExtends ? "@ToString(callSuper = true)\n" : "") + //
					"public class ATableDBO" + (isExtends ? " extends AnotherTableDBO" : "") + " {\n" + //
					"\n";
			if (!isExtends) {
				s += "	@Id\n" + //
						"	@Column(name = \"ID\")\n" + //
						"	private Long id;\n";
			}
			s += "	@Column(name = \"ADate\")\n" + //
					"	private LocalDate aDate;\n" + //
					"	@Enumerated(EnumType.STRING)\n" + //
					"	@Column(name = \"Description\")\n" + //
					"	private DescriptionDBO description;\n" + //
					"\n" + //
					"}";
			return s;
		}

		@Test
		void happyRunForASuperclassObject() {
			// Prepare
			String expected = getExpected(true, false);
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.getDomainByName("Description")
					.addOption(new Option(AbstractClassCodeGenerator.ENUM, "ONE,TWO,THREE"));
			TableModel table = dataModel.getTableByName("A_TABLE");
			table
					.addOption(
							new Option(
									AbstractClassCodeGenerator.SUPERCLASS,
									AbstractClassCodeGenerator.INHERITANCE_MODE_JOINED));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASubclassObject() {
			// Prepare
			String expected = getExpected(false, true);
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.getDomainByName("Description")
					.addOption(new Option(AbstractClassCodeGenerator.ENUM, "ONE,TWO,THREE"));
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
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void subSubclassObject() {
			// Prepare
			String expected =
					"package base.pack.age.name.persistence.entity;\n" //
							+ "\n" //
							+ "import javax.persistence.Column;\n" //
							+ "import javax.persistence.Entity;\n" //
							+ "import javax.persistence.Id;\n" //
							+ "import javax.persistence.PrimaryKeyJoinColumn;\n" //
							+ "import javax.persistence.Table;\n" //
							+ "\n" //
							+ "import lombok.Data;\n" //
							+ "import lombok.EqualsAndHashCode;\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.ToString;\n" //
							+ "import lombok.experimental.Accessors;\n" //
							+ "\n" //
							+ "/**\n" //
							+ " * A DBO for b_heir_heir_tables.\n" //
							+ " *\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n" //
							+ "@Accessors(chain = true)\n" //
							+ "@Data\n" //
							+ "@Generated\n" //
							+ "@Entity(name = \"BHeirHeirTable\")\n" //
							+ "@EqualsAndHashCode(callSuper = true)\n" //
							+ "@PrimaryKeyJoinColumn(name = \"ID\")\n" //
							+ "@Table(name = \"B_HEIR_HEIR_TABLE\")\n" //
							+ "@ToString(callSuper = true)\n" //
							+ "public class BHeirHeirTableDBO extends BHeirTableDBO {\n" //
							+ "\n" //
							+ "	@Column(name = \"ADDITIONAL_DESCRIPTION\")\n" //
							+ "	private String additionalDescription;\n" //
							+ "\n" //
							+ "}";
			DataModel dataModel = readDataModel("Model-Inheritance.xml");
			TableModel table = dataModel.getTableByName("B_HEIR_HEIR_TABLE");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Nested
		class List_Composition_Parent {

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpectedForASimpleObject(false, false);
				DataModel dataModel = readDataModel("Example-BookStore.xml", EXAMPLE_XMLS);
				// Run
				String returned =
						unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("BOOK"));
				// Check
				assertEquals(expected, returned);
			}

			private String getExpectedForASimpleObject(boolean isSuperclass, boolean isExtends) {
				String s =
						"package de.ollie.bookstore.persistence.entity;\n" //
								+ "\n" //
								+ "import java.util.List;\n" //
								+ "\n" //
								+ "import javax.persistence.Column;\n" //
								+ "import javax.persistence.Entity;\n" //
								+ "import javax.persistence.EnumType;\n" //
								+ "import javax.persistence.Enumerated;\n" //
								+ "import javax.persistence.GeneratedValue;\n" //
								+ "import javax.persistence.GenerationType;\n" //
								+ "import javax.persistence.Id;\n" //
								+ "import javax.persistence.Table;\n" //
								+ "import javax.persistence.CascadeType;\n" //
								+ "import javax.persistence.FetchType;\n" //
								+ "import javax.persistence.JoinColumn;\n" //
								+ "import javax.persistence.OneToMany;\n" //
								+ "\n" //
								+ "import lombok.Data;\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.experimental.Accessors;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * A DBO for books.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Accessors(chain = true)\n" //
								+ "@Data\n" //
								+ "@Generated\n" //
								+ "@Entity(name = \"Book\")\n" //
								+ "@Table(name = \"BOOK\")\n" //
								+ "public class BookDBO {\n" //
								+ "\n" //
								+ "	@Id\n" //
								+ "	@GeneratedValue(strategy = GenerationType.IDENTITY)\n" //
								+ "	@Column(name = \"ID\", nullable = false)\n" //
								+ "	private long id;\n" //
								+ "	@Column(name = \"ISBN\")\n" //
								+ "	private String isbn;\n" //
								+ "	@Enumerated(EnumType.STRING)\n" //
								+ "	@Column(name = \"PUBLICATION_TYPE\", nullable = false)\n" //
								+ "	private PublicationTypeDBO publicationType;\n" //
								+ "	@Column(name = \"TITLE\", nullable = false)\n" //
								+ "	private String title;\n" //
								+ "	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)\n" //
								+ "	@JoinColumn(name = \"BOOK_ID\")\n" //
								+ "	private List<ChapterDBO> chapters;\n" //
								+ "\n" //
								+ "}";
				return s;
			}

			@Test
			void happyRunForAHeirObject() {
				// Prepare
				String expected = getExpectedForAHeirObject(false, false);
				DataModel dataModel = readDataModel("Model-Inheritance.xml");
				// Run
				String returned =
						unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("C_HEIR_TABLE"));
				// Check
				assertEquals(expected, returned);
			}

			private String getExpectedForAHeirObject(boolean isSuperclass, boolean isExtends) {
				String s =
						"package base.pack.age.name.persistence.entity;\n" //
								+ "\n" //
								+ "import java.util.List;\n" //
								+ "\n" //
								+ "import javax.persistence.Column;\n" //
								+ "import javax.persistence.Entity;\n" //
								+ "import javax.persistence.Id;\n" //
								+ "import javax.persistence.PrimaryKeyJoinColumn;\n" //
								+ "import javax.persistence.Table;\n" //
								+ "import javax.persistence.CascadeType;\n" //
								+ "import javax.persistence.FetchType;\n" //
								+ "import javax.persistence.JoinColumn;\n" //
								+ "import javax.persistence.OneToMany;\n" //
								+ "\n" //
								+ "import lombok.Data;\n" //
								+ "import lombok.EqualsAndHashCode;\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.ToString;\n" //
								+ "import lombok.experimental.Accessors;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * A DBO for c_heir_tables.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Accessors(chain = true)\n" //
								+ "@Data\n" //
								+ "@Generated\n" //
								+ "@Entity(name = \"CHeirTable\")\n" //
								+ "@EqualsAndHashCode(callSuper = true)\n" //
								+ "@PrimaryKeyJoinColumn(name = \"ID\")\n" //
								+ "@Table(name = \"C_HEIR_TABLE\")\n" //
								+ "@ToString(callSuper = true)\n" //
								+ "public class CHeirTableDBO extends CTableDBO {\n" //
								+ "\n" //
								+ "	@Column(name = \"NAME\")\n" //
								+ "	private String name;\n" //
								+ "	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)\n" //
								+ "	@JoinColumn(name = \"PARENT\")\n" //
								+ "	private List<CHeirTableMemberDBO> cHeirTableMembers;\n" //
								+ "\n" //
								+ "}";
				return s;
			}

		}

		@Nested
		class List_Composition_Member {

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpectedForASimpleObject(false, false);
				DataModel dataModel = readDataModel("Example-BookStore.xml", EXAMPLE_XMLS);
				// Run
				String returned =
						unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("CHAPTER"));
				// Check
				assertEquals(expected, returned);
			}

			private String getExpectedForASimpleObject(boolean isSuperclass, boolean isExtends) {
				String s =
						"package de.ollie.bookstore.persistence.entity;\n" //
								+ "\n" //
								+ "import lombok.ToString;\n" //
								+ "\n" //
								+ "import javax.persistence.Column;\n" //
								+ "import javax.persistence.Entity;\n" //
								+ "import javax.persistence.FetchType;\n" //
								+ "import javax.persistence.GeneratedValue;\n" //
								+ "import javax.persistence.GenerationType;\n" //
								+ "import javax.persistence.Id;\n" //
								+ "import javax.persistence.JoinColumn;\n" //
								+ "import javax.persistence.ManyToOne;\n" //
								+ "import javax.persistence.Table;\n" //
								+ "\n" //
								+ "import lombok.Data;\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.experimental.Accessors;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * A DBO for chapters.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Accessors(chain = true)\n" //
								+ "@Data\n" //
								+ "@Generated\n" //
								+ "@Entity(name = \"Chapter\")\n" //
								+ "@Table(name = \"CHAPTER\")\n" //
								+ "public class ChapterDBO {\n" //
								+ "\n" //
								+ "	@Id\n" //
								+ "	@GeneratedValue(strategy = GenerationType.IDENTITY)\n" //
								+ "	@Column(name = \"ID\", nullable = false)\n" //
								+ "	private long id;\n" //
								+ "	@Column(name = \"CONTENT\")\n" //
								+ "	@ToString.Exclude\n" //
								+ "	private String content;\n" //
								+ "	@Column(name = \"SORT_ORDER\", nullable = false)\n" //
								+ "	private int sortOrder;\n" //
								+ "	@Column(name = \"SUMMARY\")\n" //
								+ "	private String summary;\n" //
								+ "	@Column(name = \"TITLE\", nullable = false)\n" //
								+ "	private String title;\n" //
								+ "\n" //
								+ "}";
				return s;
			}

		}

	}

}