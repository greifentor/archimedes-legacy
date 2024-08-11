package archimedes.codegenerators.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
class PersistenceFailureExceptionClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private ServiceNameGenerator nameGenerator = new ServiceNameGenerator();

	@InjectMocks
	private PersistenceFailureExceptionClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_DataModel_DataModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = createExpected(true);
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
			// Check
			assertEquals(expected, returned);
		}

		private String createExpected(boolean comment) {
			String expected =
					"package " + BASE_PACKAGE_NAME + ".core.service.exception;\n" //
							+ "\n" //
							+ "import java.util.Arrays;\n" //
							+ "import java.util.List;\n" //
							+ "import java.util.Map;\n" //
							+ "import java.util.stream.Collectors;\n" //
							+ "\n" //
							+ "import lombok.EqualsAndHashCode;\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.Getter;\n" //
							+ "import lombok.ToString;\n" //
							+ "\n";
			if (comment) {
				expected +=
						"/**\n" //
							+ " * An exception for persistence misbehavior.\n" //
							+ " *\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n";
			}
			expected +=
					"@Generated\n" //
							+ "public class PersistenceFailureException extends ServiceException {\n" //
							+ "\n" //
							+ "	public enum Reason {\n" //
							+ "		GENERAL,\n" //
							+ "		NOT_BLANK,\n" //
							+ "		NOT_NULL,\n" //
							+ "		UNIQUE,\n" //
							+ "		VERSION;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@EqualsAndHashCode(callSuper = true)\n" //
							+ "	@Getter\n" //
							+ "	@ToString\n" //
							+ "	public static class ValidationFailure extends ServiceException.ValidationFailure<Reason> {\n" //
							+ "\n" //
							+ "		private final List<String> attributeNames;\n" //
							+ "\n" //
							+ "		public ValidationFailure(Reason reason, String className, String... attributeNames) {\n" //
							+ "			super(reason);\n" //
							+ "			this.attributeNames = Arrays.asList(attributeNames);\n" //
							+ "			getProperties().put(\"className\", className);\n" //
							+ "			getProperties()\n" //
							+ "					.put(\n" //
							+ "							\"attributeNames\",\n" //
							+ "							Arrays\n" //
							+ "									.asList(attributeNames)\n" //
							+ "									.stream()\n" //
							+ "									.sorted()\n" //
							+ "									.reduce((s0, s1) -> s0 + \", \" + s1)\n" //
							+ "									.orElse(\"-\"));\n" //
							+ "		}\n" //
							+ "\n" //
							+ "		public String getClassName() {\n" //
							+ "			return getProperties().get(\"className\");\n" //
							+ "		}\n" //
							+ "\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private final List<ValidationFailure> validationFailures;\n" //
							+ "\n" //
							+ "	public PersistenceFailureException(String id, List<ValidationFailure> validationFailures) {\n" //
							+ "		super(Map.of(\"id\", id));\n" //
							+ "		this.validationFailures = validationFailures;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public List<ServiceException.ValidationFailure<? extends Enum<?>>> getValidationFailures() {\n" //
							+ "		return validationFailures.stream().map(vf -> vf).collect(Collectors.toList());\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			return expected;
		}

		@Test
		void happyRunForASimpleObject_NoComment() {
			// Prepare
			String expected = createExpected(false);
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
			// Check
			assertEquals(expected, returned);
		}

	}

}
