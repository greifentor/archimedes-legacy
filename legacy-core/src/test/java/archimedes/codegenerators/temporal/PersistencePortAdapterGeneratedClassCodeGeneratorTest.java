package archimedes.codegenerators.temporal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class PersistencePortAdapterGeneratedClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private TemporalDataNameGenerator nameGenerator = new TemporalDataNameGenerator();

	@InjectMocks
	private PersistencePortAdapterGeneratedClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".persistence;\n" + //
					"\n" + //
					"import org.apache.logging.log4j.LogManager;\n" + //
					"import org.apache.logging.log4j.Logger;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.function.Function;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME
					+ ".persistence.converter.ATableSimpleFieldChangeToATableChangeDboConverter;\n" + //
					"import " + BASE_PACKAGE_NAME + ".persistence.dbo.ATableAttributeDBO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".persistence.dbo.ATableChangeActionDBO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".service.ATableIdSO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".service.ports.ATablePersistencePortGenerated;\n" + //
					"\n" + //
					"/**\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"public abstract class ATablePersistencePortAdapterGenerated implements ATablePersistencePortGenerated {\n"
					+ //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(ATablePersistencePortAdapterGenerated.class);\n"
					+ //
					"\n" + //
					"	protected abstract ATableSimpleFieldChangeToATableChangeDboConverter<String> simpleFieldChangeToActionDBOConverter();\n"
					+ //
					"\n" + //
					"	protected abstract void processAction(ATableChangeActionDBO action, ATableIdSO id);\n" + //
					"\n" + //
					"	protected <T> T getValue(List<ATableChangeActionDBO> actions, ATableAttributeDBO attribute,\n" + //
					"			Function<String, T> setter, T defaultValue) {\n" + //
					"		return actions\n" + //
					"				.stream()\n" + //
					"				.filter(action -> action.getAttribute() == attribute)\n" + //
					"				.map(action -> setter.apply(action.getValue()))\n" + //
					"				.findFirst()\n" + //
					"				.orElse(defaultValue);\n" + //
					"	}\n" + //
					"\n" + //
					"	protected String getDescription(List<ATableChangeActionDBO> actions) {\n" + //
					"		return getValue(actions, ATableAttributeDBO.DESCRIPTION, s -> s, \"\");\n" + //
					"	}\n" + //
					"\n" + //
					"" + //
					"	@Override\n" + //
					"	public void saveDescription(ATableIdSO id, String description) {\n" + //
					"		logger.info(\"new description send to persistence port: {}\", description);\n" + //
					"		ATableChangeActionDBO action = simpleFieldChangeToActionDBOConverter().convert(ATableAttributeDBO.DESCRIPTION, description);\n"
					+ //
					"		processAction(action, id);\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.getTableByName("A_TABLE").addOption(new Option(TemporalDataCodeFactory.TEMPORAL));
			dataModel
					.getTableByName("A_TABLE")
					.getColumnByName("Description")
					.addOption(new Option(TemporalDataCodeFactory.TEMPORAL));
			dataModel
					.getTableByName("A_TABLE")
					.getColumnByName("ADate")
					.addOption(new Option(TemporalDataCodeFactory.TEMPORAL));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}