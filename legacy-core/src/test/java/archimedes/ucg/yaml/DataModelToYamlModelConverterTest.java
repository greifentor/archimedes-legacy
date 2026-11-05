package archimedes.ucg.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Color;
import java.sql.Types;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.MainView;
import archimedes.legacy.scheme.Relation;
import archimedes.legacy.scheme.Tabelle;
import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.legacy.scheme.View;
import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.GUIRelationModel;
import archimedes.model.gui.GUIViewModel;
import archimedes.scheme.Option;
import archimedes.ucg.yaml.model.YamlArchimedesColor;
import archimedes.ucg.yaml.model.YamlArchimedesPoint;
import archimedes.ucg.yaml.model.YamlArchimedesRelation;
import archimedes.ucg.yaml.model.YamlArchimedesSection;
import archimedes.ucg.yaml.model.YamlArchimedesTable;
import archimedes.ucg.yaml.model.YamlArchimedesTablePosition;
import archimedes.ucg.yaml.model.YamlArchimedesView;
import archimedes.ucg.yaml.model.YamlAttribute;
import archimedes.ucg.yaml.model.YamlClassDefinition;
import archimedes.ucg.yaml.model.YamlEnumDefinition;
import archimedes.ucg.yaml.model.YamlModel;
import archimedes.ucg.yaml.model.YamlProperty;
import archimedes.ucg.yaml.model.YamlType;
import corent.base.Direction;
import corent.gui.ExtendedColor;

public class DataModelToYamlModelConverterTest {

	private DataModelToYamlModelConverter unitUnderTest = new DataModelToYamlModelConverter();

	static Diagramm createDiagram() {
		Diagramm diagram = new Diagramm();
		diagram.addView((GUIViewModel) new MainView("Main", "", true));
		return diagram;
	}

	static Tabelle addTable(Diagramm diagram, String name) {
		Tabelle table = new Tabelle((ViewModel) diagram.getMainView(), 0, 0, diagram);
		table.setName(name);
		diagram.addTable(table);
		((ViewModel) diagram.getMainView()).addTable(table);
		return table;
	}

	static ColumnModel addColumn(Tabelle table, String name, DomainModel domain) {
		ColumnModel column = new Tabellenspalte(name, domain);
		column.setTable(table);
		table.addColumn(column);
		return column;
	}

	static Diagramm createAuthorAndBookDiagram() {
		Diagramm diagram = createDiagram();
		diagram.setName("Library");
		diagram.addOption(new Option("base-package", "de.ollie.library"));
		Domain ident = new Domain("Ident", Types.BIGINT, -1, -1);
		Domain name = new Domain("Name", Types.VARCHAR, 100, -1);
		Domain date = new Domain("Date", Types.DATE, -1, -1);
		Domain price = new Domain("Price", Types.NUMERIC, 10, 2);
		Tabelle author = addTable(diagram, "Author");
		author.addOption(new Option("gui", "true"));
		ColumnModel authorId = addColumn(author, "id", ident);
		authorId.setPrimaryKey(true);
		authorId.setNotNull(true);
		ColumnModel authorName = addColumn(author, "name", name);
		authorName.setNotNull(true);
		authorName.setUnique(true);
		authorName.addOption(new Option("editor-position", "1"));
		addColumn(author, "birthday", date);
		Tabelle book = addTable(diagram, "Book");
		ColumnModel bookId = addColumn(book, "id", ident);
		bookId.setPrimaryKey(true);
		bookId.setNotNull(true);
		ColumnModel bookAuthor = addColumn(book, "author", ident);
		bookAuthor.setNotNull(true);
		bookAuthor.setRelation(new Relation((ViewModel) diagram.getMainView(), bookAuthor, Direction.UP, 0, authorId, Direction.DOWN, 0));
		addColumn(book, "price", price).setIndividualDefaultValue("9.99");
		ColumnModel bookType = addColumn(book, "type", name);
		bookType.addOption(new Option(UcgYamlNames.COLUMN_OPTION_ENUM, ""));
		bookType.addOption(new Option(UcgYamlNames.COLUMN_OPTION_JAVA_TYPE, "BookType"));
		Domain bookTypeEnum = new Domain("BookType", Types.VARCHAR, YamlModelToDiagramConverter.DEFAULT_STRING_LENGTH, -1);
		bookTypeEnum.addOption(new Option(UcgYamlNames.DOMAIN_OPTION_ENUM, "HARDCOVER,SOFTCOVER"));
		diagram.addDomain(bookTypeEnum);
		return diagram;
	}

	static YamlModel createAuthorAndBookYamlModel() {
		YamlProperty notNullable = new YamlProperty("nullable", "false");
		YamlProperty primaryKey = new YamlProperty("primary-key", "true");
		YamlClassDefinition author =
				new YamlClassDefinition(
						"Author",
						List.of(
								new YamlAttribute("id", new YamlType("long", null), null, false, List.of(notNullable, primaryKey), false),
								new YamlAttribute(
										"birthday",
										new YamlType("LocalDate", List.of(new YamlProperty("import", "java.time.LocalDate"))),
										null,
										false,
										null,
										false),
								new YamlAttribute(
										"name",
										new YamlType("String", List.of(new YamlProperty("length", "100"))),
										null,
										false,
										List.of(notNullable, new YamlProperty("unique", "true"), new YamlProperty("editor-position", "1")),
										false)),
						List.of(new YamlProperty("gui", "true")));
		YamlClassDefinition book =
				new YamlClassDefinition(
						"Book",
						List.of(
								new YamlAttribute("id", new YamlType("long", null), null, false, List.of(notNullable, primaryKey), false),
								new YamlAttribute("author", new YamlType("Author", null), null, false, List.of(notNullable), true),
								new YamlAttribute(
										"price",
										new YamlType(
												"Double",
												List.of(new YamlProperty("length", "10"), new YamlProperty("decimal-places", "2"))),
										"9.99",
										false,
										null,
										false),
								new YamlAttribute("type", new YamlType("BookType", null), null, true, null, false)),
						null);
		return new YamlModel(
				"Library",
				List.of(author, book),
				List.of(new YamlEnumDefinition("BookType", List.of("HARDCOVER", "SOFTCOVER"))),
				List.of(new YamlProperty("base-package", "de.ollie.library")));
	}

	static Diagramm createAuthorAndBookDiagramWithLayout() {
		Diagramm diagram = createAuthorAndBookDiagram();
		ViewModel mainView = (ViewModel) diagram.getMainView();
		Tabelle author = (Tabelle) diagram.getTableByName("Author");
		Tabelle book = (Tabelle) diagram.getTableByName("Book");
		author.setXY(mainView, 100, 200);
		book.setXY(mainView, 500, 200);
		author.setBackgroundColor(new ExtendedColor("hellblau", 200, 220, 255));
		author.setFontColor(new Color(0, 0, 0));
		book.setBackgroundColor(new ExtendedColor("hellblau", 200, 220, 255));
		book.setFontColor(new Color(0, 0, 0));
		View details = new View();
		details.setName("Details");
		details.setComment("Only books");
		details.setHideTechnicalFields(true);
		details.setHideTransientFields(true);
		diagram.addView(details);
		details.addTable(book);
		book.setXY((ViewModel) details, 10, 20);
		GUIRelationModel relation = (GUIRelationModel) book.getColumnByName("author").getRelation();
		relation.setDirection(diagram.getMainView(), 0, Direction.LEFT);
		relation.setOffset(diagram.getMainView(), 0, 5);
		relation.setDirection(diagram.getMainView(), 1, Direction.RIGHT);
		relation.setOffset(diagram.getMainView(), 1, 7);
		relation.addPoint(diagram.getMainView(), 300, 150);
		return diagram;
	}

	static YamlArchimedesSection createAuthorAndBookArchimedesSection() {
		return new YamlArchimedesSection(
				"Library",
				List
						.of(
								new YamlArchimedesView(
										"Main",
										null,
										true,
										true,
										false,
										false,
										List
												.of(
														new YamlArchimedesTablePosition("Author", 100, 200),
														new YamlArchimedesTablePosition("Book", 500, 200)),
										List
												.of(
														new YamlArchimedesRelation(
																"Book",
																"author",
																"LEFT",
																5,
																"RIGHT",
																7,
																List.of(new YamlArchimedesPoint(300, 150))))),
								new YamlArchimedesView(
										"Details",
										"Only books",
										false,
										false,
										true,
										true,
										List.of(new YamlArchimedesTablePosition("Book", 10, 20)),
										null)),
				List
						.of(
								new YamlArchimedesTable("Author", "hellblau", "#000000"),
								new YamlArchimedesTable("Book", "hellblau", "#000000")),
				List.of(new YamlArchimedesColor("hellblau", 200, 220, 255), new YamlArchimedesColor("#000000", 0, 0, 0)));
	}

	@Nested
	class TestsOfMethod_convertToArchimedesSection_DataModel {

		@Test
		void returnsTheViewsTablePositionsColorsAndRelationLayouts_passingADataModelWithLayout() {
			// Prepare
			YamlArchimedesSection expected = createAuthorAndBookArchimedesSection();
			// Run
			YamlArchimedesSection returned = unitUnderTest.convertToArchimedesSection(createAuthorAndBookDiagramWithLayout());
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_convert_DataModel {

		@Test
		void returnsAnEmptyModel_passingAnEmptyDataModel() {
			// Prepare
			Diagramm diagram = createDiagram();
			diagram.setName("");
			// Run
			YamlModel returned = unitUnderTest.convert(diagram);
			// Check
			assertEquals(new YamlModel(), returned);
		}

		@Test
		void returnsTheExpectedModel_passingADataModelWithTablesColumnsReferencesAndOptions() {
			// Prepare
			YamlModel expected = createAuthorAndBookYamlModel();
			// Run
			YamlModel returned = unitUnderTest.convert(createAuthorAndBookDiagram());
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void returnsATypeWithImportOfTheJavaTypeOption_passingAColumnWithAUuidJavaTypeOption() {
			// Prepare
			YamlType expected = new YamlType("UUID", List.of(new YamlProperty("import", "java.util.UUID")));
			Diagramm diagram = createDiagram();
			addColumn(addTable(diagram, "Table"), "id", new Domain("Varchar36", Types.VARCHAR, 36, -1))
					.addOption(new Option(UcgYamlNames.COLUMN_OPTION_JAVA_TYPE, "UUID"));
			// Run
			YamlModel returned = unitUnderTest.convert(diagram);
			// Check
			assertEquals(expected, returned.getClasses().get(0).getAttributes().get(0).getType());
		}

		@Test
		void throwsAnException_passingAColumnWithoutDomain() {
			// Prepare
			Diagramm diagram = createDiagram();
			addColumn(addTable(diagram, "Table"), "column", null);
			// Run & Check
			assertThrows(IllegalStateException.class, () -> unitUnderTest.convert(diagram));
		}

		@Test
		void throwsAnException_passingAColumnWithADomainWithoutJavaType() {
			// Prepare
			Diagramm diagram = createDiagram();
			addColumn(addTable(diagram, "Table"), "column", new Domain("Other", Types.OTHER, -1, -1));
			// Run & Check
			assertThrows(IllegalStateException.class, () -> unitUnderTest.convert(diagram));
		}

	}

}
