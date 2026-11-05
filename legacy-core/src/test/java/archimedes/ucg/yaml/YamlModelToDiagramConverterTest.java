package archimedes.ucg.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import archimedes.legacy.model.DiagrammModel;
import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.ucg.yaml.model.YamlArchimedesRelation;
import archimedes.ucg.yaml.model.YamlArchimedesSection;
import archimedes.ucg.yaml.model.YamlArchimedesTablePosition;
import archimedes.ucg.yaml.model.YamlArchimedesView;
import archimedes.ucg.yaml.model.YamlAttribute;
import archimedes.ucg.yaml.model.YamlClassDefinition;
import archimedes.ucg.yaml.model.YamlEnumDefinition;
import archimedes.ucg.yaml.model.YamlModel;
import archimedes.ucg.yaml.model.YamlProperty;
import archimedes.ucg.yaml.model.YamlType;

public class YamlModelToDiagramConverterTest {

	private YamlModelToDiagramConverter unitUnderTest = new YamlModelToDiagramConverter();

	private static YamlModel createModel(YamlClassDefinition... classDefinitions) {
		return new YamlModel(null, List.of(classDefinitions), null, null);
	}

	private static YamlClassDefinition createClass(String name, YamlAttribute... attributes) {
		return new YamlClassDefinition(name, List.of(attributes), null);
	}

	private static YamlAttribute createAttribute(String name, String typeName, YamlProperty... properties) {
		return new YamlAttribute(
				name,
				new YamlType(typeName, null),
				null,
				false,
				properties.length == 0 ? null : List.of(properties),
				false);
	}

	private static YamlAttribute createAttribute(String name, YamlType type) {
		return new YamlAttribute(name, type, null, false, null, false);
	}

	private static YamlAttribute createReference(String name, String className) {
		return new YamlAttribute(name, new YamlType(className, null), null, false, null, true);
	}

	private static String getDomainDescription(DomainModel domain) {
		return domain.getName() + "|" + domain.getDataType() + "|" + domain.getLength() + "|"
				+ domain.getDecimalPlace();
	}

	private static String getOptionsDescription(ColumnModel column) {
		return Arrays
				.stream(column.getOptions())
				.map(option -> option.getName() + "=" + option.getParameter())
				.collect(Collectors.joining(", "));
	}

	private ColumnModel convertAndGetColumn(YamlModel model, String tableName, String columnName) {
		return unitUnderTest.convert(model).getTableByName(tableName).getColumnByName(columnName);
	}

	private static YamlArchimedesSection createSection(YamlArchimedesView... views) {
		return new YamlArchimedesSection(null, List.of(views), null, null);
	}

	private static YamlArchimedesView createView(String name, boolean mainView,
			List<YamlArchimedesTablePosition> positions, List<YamlArchimedesRelation> relations) {
		return new YamlArchimedesView(name, null, mainView, false, false, false, positions, relations);
	}

	@Nested
	class TestsOfMethod_convert_YamlModel_YamlArchimedesSection {

		@Test
		void returnsADiagramWithTheLayoutOfTheSection_convertingADataModelWithLayoutToYamlAndBack() {
			// Prepare
			DataModelToYamlModelConverter toYamlConverter = new DataModelToYamlModelConverter();
			DiagrammModel original = DataModelToYamlModelConverterTest.createAuthorAndBookDiagramWithLayout();
			YamlArchimedesSection expected = toYamlConverter.convertToArchimedesSection(original);
			// Run
			DiagrammModel returned = unitUnderTest.convert(toYamlConverter.convert(original), expected);
			// Check
			assertEquals(expected, toYamlConverter.convertToArchimedesSection(returned));
		}

		@Test
		void returnsADiagramWithADefaultMainView_passingASectionWithoutMainView() {
			// Prepare
			YamlArchimedesSection section = createSection(createView("Details", false, null, null));
			// Run
			DiagrammModel returned = unitUnderTest.convert(new YamlModel(), section);
			// Check
			assertEquals(
					List.of(YamlModelToDiagramConverter.MAIN_VIEW_NAME, "Details"),
					returned.getViews().stream().map(view -> ((ViewModel) view).getName()).collect(Collectors.toList()));
		}

		@Test
		void throwsAnException_passingAPositionOfAnUnknownTable() {
			// Prepare
			YamlArchimedesSection section =
					createSection(createView("Main", true, List.of(new YamlArchimedesTablePosition("A", 1, 2)), null));
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.convert(new YamlModel(), section));
		}

		@Test
		void throwsAnException_passingDuplicateViewNames() {
			// Prepare
			YamlArchimedesSection section = createSection(createView("V", true, null, null), createView("V", false, null, null));
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.convert(new YamlModel(), section));
		}

		@Test
		void throwsAnException_passingARelationLayoutForANonReferenceAttribute() {
			// Prepare
			YamlModel model = createModel(createClass("A", createAttribute("name", "String")));
			YamlArchimedesSection section =
					createSection(
							createView(
									"Main",
									true,
									null,
									List.of(new YamlArchimedesRelation("A", "name", "UP", 0, "DOWN", 0, null))));
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.convert(model, section));
		}

		@Test
		void throwsAnException_passingAnInvalidDirection() {
			// Prepare
			YamlModel model =
					createModel(
							createClass("Book", createReference("author", "Author")),
							createClass("Author", createAttribute("id", "long")));
			YamlArchimedesSection section =
					createSection(
							createView(
									"Main",
									true,
									null,
									List.of(new YamlArchimedesRelation("Book", "author", "NORTH", 0, "DOWN", 0, null))));
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.convert(model, section));
		}

		@Test
		void returnsADiagramWithTheTitleOfTheSection_passingAModelWithoutTitle() {
			// Prepare
			YamlArchimedesSection section = new YamlArchimedesSection("Library", null, null, null);
			// Run
			DiagrammModel returned = unitUnderTest.convert(new YamlModel(), section);
			// Check
			assertEquals("Library", returned.getName());
		}

		@Test
		void returnsADiagramWithTheTitleOfTheModel_passingAModelAndASectionWithTitles() {
			// Prepare
			YamlModel model = new YamlModel("Model Title", null, null, null);
			YamlArchimedesSection section = new YamlArchimedesSection("Section Title", null, null, null);
			// Run
			DiagrammModel returned = unitUnderTest.convert(model, section);
			// Check
			assertEquals("Model Title", returned.getName());
		}

	}

	@Nested
	class TestsOfMethod_convert_YamlModel {

		@Test
		void returnsADiagramWithAnEnumDomain_passingAnEnumDefinition() {
			// Prepare
			YamlModel model =
					new YamlModel(null, null, List.of(new YamlEnumDefinition("BookType", List.of("HARDCOVER", "SOFTCOVER"))), null);
			// Run
			DomainModel returned = unitUnderTest.convert(model).getDomainByName("BookType");
			// Check
			assertEquals(
					"HARDCOVER,SOFTCOVER",
					returned.getOptionByName(UcgYamlNames.DOMAIN_OPTION_ENUM).getParameter());
		}

		@Test
		void throwsAnException_passingDuplicateEnumNames() {
			// Prepare
			YamlModel model =
					new YamlModel(
							null,
							null,
							List.of(new YamlEnumDefinition("E", List.of("A")), new YamlEnumDefinition("E", List.of("B"))),
							null);
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.convert(model));
		}

		@Test
		void returnsADiagramWithAMainView_passingAnEmptyModel() {
			// Run
			DiagrammModel returned = unitUnderTest.convert(new YamlModel());
			// Check
			assertEquals(YamlModelToDiagramConverter.MAIN_VIEW_NAME, returned.getMainView().getName());
		}

		@Test
		void returnsADiagramWithNameAndOptions_passingTitleAndProperties() {
			// Prepare
			YamlModel model = new YamlModel("Library", null, null, List.of(new YamlProperty("base-package", "de.ollie")));
			// Run
			DiagrammModel returned = unitUnderTest.convert(model);
			// Check
			assertEquals(
					"Library|base-package=de.ollie",
					returned.getName() + "|" + returned.getOptions()[0].getName() + "=" + returned.getOptions()[0].getParameter());
		}

		@Test
		void returnsTablesForAllClassesInTheMainView_passingTheUcgTestModel() throws Exception {
			// Prepare
			YamlModel model = new UcgYamlModelReader().read(UcgYamlModelReaderTest.UCG_TEST_MODEL_FILE_NAME);
			// Run
			DiagrammModel returned = unitUnderTest.convert(model);
			// Check
			assertEquals(
					List.of("Author", "Book"),
					Arrays.stream(((ViewModel) returned.getMainView()).getTables()).map(TableModel::getName).collect(Collectors.toList()));
		}

		@Test
		void returnsARelationToTheIdAttribute_passingTheUcgTestModelWithoutPrimaryKeyProperties() throws Exception {
			// Prepare
			YamlModel model = new UcgYamlModelReader().read(UcgYamlModelReaderTest.UCG_TEST_MODEL_FILE_NAME);
			// Run
			DiagrammModel returned = unitUnderTest.convert(model);
			// Check
			assertSame(
					returned.getTableByName("Author").getColumnByName("id"),
					returned.getTableByName("Book").getColumnByName("author").getReferencedColumn());
		}

		@Test
		void returnsAReferenceColumnWithTheDomainOfThePrimaryKey_passingAReferenceDefinedBeforeItsTarget() {
			// Prepare
			YamlModel model =
					createModel(
							createClass("Book", createReference("author", "Author")),
							createClass("Author", createAttribute("key", "long", new YamlProperty("primary-key", "true"))));
			// Run
			DiagrammModel returned = unitUnderTest.convert(model);
			// Check
			assertSame(
					returned.getTableByName("Author").getColumnByName("key").getDomain(),
					returned.getTableByName("Book").getColumnByName("author").getDomain());
		}

		@Test
		void returnsANotNullPrimaryKeyColumnWithIntegerDomain_passingAnIntAttribute() {
			// Prepare
			YamlModel model = createModel(createClass("A", createAttribute("count", "int", new YamlProperty("primary-key", "true"))));
			// Run
			ColumnModel returned = convertAndGetColumn(model, "A", "count");
			// Check
			assertEquals(
					"Int|" + Types.INTEGER + "|-1|-1|true|true",
					getDomainDescription(returned.getDomain()) + "|" + returned.isNotNull() + "|" + returned.isPrimaryKey());
		}

		@Test
		void returnsAVarcharDomainWithDefaultLength_passingAStringAttributeWithoutLength() {
			// Prepare
			YamlModel model = createModel(createClass("A", createAttribute("name", "String")));
			// Run
			ColumnModel returned = convertAndGetColumn(model, "A", "name");
			// Check
			assertEquals(
					"String|" + Types.VARCHAR + "|" + YamlModelToDiagramConverter.DEFAULT_STRING_LENGTH + "|-1",
					getDomainDescription(returned.getDomain()));
		}

		@Test
		void returnsADomainNamedByJavaTypeAndLength_passingAStringAttributeWithLength() {
			// Prepare
			YamlType type = new YamlType("String", List.of(new YamlProperty("length", "100")));
			YamlModel model = createModel(createClass("A", createAttribute("name", type)));
			// Run
			ColumnModel returned = convertAndGetColumn(model, "A", "name");
			// Check
			assertEquals("String100|" + Types.VARCHAR + "|100|-1", getDomainDescription(returned.getDomain()));
		}

		@Test
		void returnsADomainNamedByJavaTypeLengthAndDecimalPlaces_passingADoubleAttributeWithLengthAndDecimalPlaces() {
			// Prepare
			YamlType type =
					new YamlType("Double", List.of(new YamlProperty("length", "10"), new YamlProperty("decimal-places", "2")));
			YamlModel model = createModel(createClass("A", createAttribute("price", type)));
			// Run
			ColumnModel returned = convertAndGetColumn(model, "A", "price");
			// Check
			assertEquals("Double10_2", returned.getDomain().getName());
		}

		@Test
		void returnsADomainNamedByTheCapitalizedJavaType_passingAPrimitiveArrayAttribute() {
			// Prepare
			YamlModel model = createModel(createClass("A", createAttribute("data", "byte[]")));
			// Run
			ColumnModel returned = convertAndGetColumn(model, "A", "data");
			// Check
			assertEquals("ByteArray|" + Types.BLOB + "|-1|-1", getDomainDescription(returned.getDomain()));
		}

		@Test
		void returnsAColumnWithTheEnumDomain_passingAnEnumAttributeAndItsEnumDefinition() {
			// Prepare
			YamlAttribute attribute = new YamlAttribute("type", new YamlType("BookType", null), null, true, null, false);
			YamlModel model =
					new YamlModel(
							null,
							List.of(createClass("A", attribute)),
							List.of(new YamlEnumDefinition("BookType", List.of("HARDCOVER", "SOFTCOVER"))),
							null);
			// Run
			DiagrammModel returned = unitUnderTest.convert(model);
			// Check
			assertSame(
					returned.getDomainByName("BookType"),
					returned.getTableByName("A").getColumnByName("type").getDomain());
		}

		@Test
		void returnsANumericDomain_passingADoubleAttributeWithLengthAndDecimalPlaces() {
			// Prepare
			YamlType type =
					new YamlType("Double", List.of(new YamlProperty("length", "10"), new YamlProperty("decimal-places", "2")));
			YamlModel model = createModel(createClass("A", createAttribute("price", type)));
			// Run
			ColumnModel returned = convertAndGetColumn(model, "A", "price");
			// Check
			assertEquals(Types.NUMERIC + "|10|2", returned.getDomain().getDataType() + "|" + returned.getDomain().getLength() + "|" + returned.getDomain().getDecimalPlace());
		}

		@Test
		void returnsAColumnWithFlagsAndOptions_passingAttributeProperties() {
			// Prepare
			YamlModel model =
					createModel(
							createClass(
									"A",
									createAttribute(
											"name",
											"String",
											new YamlProperty("nullable", "false"),
											new YamlProperty("unique", "true"),
											new YamlProperty("editor-position", "1"))));
			// Run
			ColumnModel returned = convertAndGetColumn(model, "A", "name");
			// Check
			assertEquals(
					"true|true|editor-position=1",
					returned.isNotNull() + "|" + returned.isUnique() + "|" + getOptionsDescription(returned));
		}

		@Test
		void returnsAColumnWithJavaTypeOption_passingAnAttributeWithAnUnknownJavaType() {
			// Prepare
			YamlModel model = createModel(createClass("A", createAttribute("id", "UUID")));
			// Run
			ColumnModel returned = convertAndGetColumn(model, "A", "id");
			// Check
			assertEquals(UcgYamlNames.COLUMN_OPTION_JAVA_TYPE + "=UUID", getOptionsDescription(returned));
		}

		@Test
		void returnsAColumnWithEnumAndJavaTypeOption_passingAnEnumAttribute() {
			// Prepare
			YamlAttribute attribute = new YamlAttribute("type", new YamlType("BookType", null), null, true, null, false);
			YamlModel model = createModel(createClass("A", attribute));
			// Run
			ColumnModel returned = convertAndGetColumn(model, "A", "type");
			// Check
			assertEquals(
					UcgYamlNames.COLUMN_OPTION_ENUM + "=null, " + UcgYamlNames.COLUMN_OPTION_JAVA_TYPE + "=BookType",
					getOptionsDescription(returned));
		}

		@Test
		void returnsTheTableOptions_passingClassProperties() {
			// Prepare
			YamlModel model =
					createModel(new YamlClassDefinition("A", null, List.of(new YamlProperty("gui", "true"))));
			// Run
			OptionModel returned = unitUnderTest.convert(model).getTableByName("A").getOptions()[0];
			// Check
			assertEquals("gui=true", returned.getName() + "=" + returned.getParameter());
		}

		@Test
		void returnsAnEqualYamlModel_convertingADataModelToYamlAndBackToYaml() {
			// Prepare
			DataModelToYamlModelConverter toYamlConverter = new DataModelToYamlModelConverter();
			YamlModel expected = toYamlConverter.convert(DataModelToYamlModelConverterTest.createAuthorAndBookDiagram());
			// Run
			DiagrammModel returned = unitUnderTest.convert(expected);
			// Check
			assertEquals(expected, toYamlConverter.convert(returned));
		}

		@Test
		void createsEachDomainOnlyOnce_passingAttributesOfTheSameType() {
			// Prepare
			YamlModel model = createModel(createClass("A", createAttribute("a", "String"), createAttribute("b", "String")));
			// Run
			DiagrammModel returned = unitUnderTest.convert(model);
			// Check
			assertTrue(
					returned.getTableByName("A").getColumnByName("a").getDomain() == returned
							.getTableByName("A")
							.getColumnByName("b")
							.getDomain());
		}

		@Test
		void throwsAnException_passingAReferenceToAnUnknownClass() {
			// Prepare
			YamlModel model = createModel(createClass("Book", createReference("author", "Author")));
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.convert(model));
		}

		@Test
		void throwsAnException_passingAReferenceToAClassWithoutPrimaryKeyAndId() {
			// Prepare
			YamlModel model =
					createModel(
							createClass("Book", createReference("author", "Author")),
							createClass("Author", createAttribute("name", "String")));
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.convert(model));
		}

		@Test
		void throwsAnException_passingCyclicPrimaryKeyReferences() {
			// Prepare
			YamlAttribute aId = new YamlAttribute("id", new YamlType("B", null), null, false, null, true);
			YamlAttribute bId = new YamlAttribute("id", new YamlType("A", null), null, false, null, true);
			YamlModel model = createModel(createClass("A", aId), createClass("B", bId));
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.convert(model));
		}

		@Test
		void throwsAnException_passingDuplicateClassNames() {
			// Prepare
			YamlModel model = createModel(createClass("A"), createClass("A"));
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.convert(model));
		}

		@Test
		void throwsAnException_passingAnAttributeWithoutType() {
			// Prepare
			YamlModel model = createModel(createClass("A", new YamlAttribute("a", null, null, false, null, false)));
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.convert(model));
		}

		@Test
		void throwsAnException_passingANonNumericLength() {
			// Prepare
			YamlType type = new YamlType("String", List.of(new YamlProperty("length", "long")));
			YamlModel model = createModel(createClass("A", createAttribute("a", type)));
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.convert(model));
		}

	}

}
