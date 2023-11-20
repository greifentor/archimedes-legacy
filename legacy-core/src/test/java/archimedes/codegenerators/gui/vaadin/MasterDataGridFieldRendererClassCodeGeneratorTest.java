package archimedes.codegenerators.gui.vaadin;

import static archimedes.codegenerators.DataModelReader.EXAMPLE_XMLS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.DataModelReader;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class MasterDataGridFieldRendererClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private MasterDataGridFieldRendererClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class SimpleTable {

		@Test
		void happyRunForASimpleObject() {
			DataModel dataModel = readDataModel("Model.xml");
			assertFalse(unitUnderTest.isToIgnoreFor(dataModel, dataModel.getTableByName("A_TABLE")));
		}

	}

	@Nested
	class TableWithObjectReferences {

		@Test
		void happyRun() {
			// Prepare
			String expected = "package base.pack.age.name.gui.vaadin.masterdata.renderer;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.GuiTable;\n" + //
					"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGridFieldRenderer;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"/**\n" + //
					" * An implementation of the MasterDataGridFieldRenderer interface for gui tables.\n" + //
					" *\n" + //
					" * If necessary to override, remove 'GENERATED CODE ...' comment and annotation.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"public class GuiTableMasterDataGridFieldRenderer implements MasterDataGridFieldRenderer<GuiTable> {\n"
					+ //
					"\n" + //
					"	@Override\n" + //
					"	public Object getHeaderString(String fieldName, GuiTable model) {\n" + //
					"		if (GuiTable.REF.equals(fieldName)) {\n" + //
					"			return model.getRef() != null ? model.getRef().getName() : \"-\";\n" + //
					"		}\n" + //
					"		return null;\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public boolean hasRenderingFor(String fieldName) {\n" + //
					"		if (GuiTable.REF.equals(fieldName)) {\n" + //
					"			return true;\n" + //
					"		}\n" + //
					"		return false;\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			// Run
			String returned =
					unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("GUI_TABLE"));
			// Check
			assertEquals(expected, returned);
			assertFalse(unitUnderTest.isToIgnoreFor(dataModel, dataModel.getTableByName("GUI_TABLE")));
		}

	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class SimpleClass {

			@Test
			void happyRunForASimpleObjectWithEnumField() {
				String expected =
						"package de.ollie.bookstore.gui.vaadin.masterdata.renderer;\n" //
								+ "\n" //
								+ "import javax.inject.Inject;\n" //
								+ "import javax.inject.Named;\n" //
								+ "\n" //
								+ "import de.ollie.bookstore.core.model.Book;\n" //
								+ "import de.ollie.bookstore.core.model.PublicationType;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.component.ComponentFactory;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.masterdata.MasterDataGridFieldRenderer;\n" //
								+ "\n" //
								+ "import lombok.Generated;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * An implementation of the MasterDataGridFieldRenderer interface for books.\n" //
								+ " *\n" //
								+ " * If necessary to override, remove 'GENERATED CODE ...' comment and annotation.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Generated\n" //
								+ "@Named\n" //
								+ "public class BookMasterDataGridFieldRenderer implements MasterDataGridFieldRenderer<Book> {\n" //
								+ "\n" //
								+ "	@Inject\n" //
								+ "	private ComponentFactory componentFactory;\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	public Object getHeaderString(String fieldName, Book model) {\n" //
								+ "		if (Book.PUBLICATIONTYPE.equals(fieldName)) {\n" //
								+ "			return componentFactory.getPublicationTypeItemLabelGenerator().apply(model.getPublicationType());\n" //
								+ "		}\n" //
								+ "		return null;\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	public boolean hasRenderingFor(String fieldName) {\n" //
								+ "		if (Book.PUBLICATIONTYPE.equals(fieldName) && (componentFactory.getPublicationTypeItemLabelGenerator() != null)) {\n" //
								+ "			return true;\n" //
								+ "		}\n" //
								+ "		return false;\n" //
								+ "	}\n" //
								+ "\n" //
								+ "}";
				DataModel dataModel = DataModelReader.readDataModel("Example-BookStore.xml", EXAMPLE_XMLS);
				TableModel tableModel = dataModel.getTableByName("BOOK");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
				assertFalse(unitUnderTest.isToIgnoreFor(dataModel, dataModel.getTableByName("BOOK")));
			}

		}

	}

}
