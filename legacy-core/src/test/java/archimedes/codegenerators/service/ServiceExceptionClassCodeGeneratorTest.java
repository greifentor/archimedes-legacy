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
public class ServiceExceptionClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private ServiceNameGenerator nameGenerator = new ServiceNameGenerator();

	@InjectMocks
	private ServiceExceptionClassCodeGenerator unitUnderTest;

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
					"package " + BASE_PACKAGE_NAME + ".core.service.exception;\n" // //
							+ "\n" // //
							+ "import java.util.HashMap;\n" //
							+ "import java.util.List;\n" //
							+ "import java.util.Map;\n" //
							+ "\n" //
							+ "import base.pack.age.name.core.model.localization.LocalizationSO;\n" //
							+ "import base.pack.age.name.core.service.localization.ResourceManager;\n" //
							+ "import lombok.AccessLevel;\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.Getter;\n" //
							+ "import lombok.RequiredArgsConstructor;\n" //
							+ "\n";
			if (comment) {
				expected +=
						"/**\n" //
								+ " * An exception for service misbehavior.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n";
			}
			expected +=
					"@Generated\n" //
							+ "public abstract class ServiceException extends RuntimeException {\n" //
							+ "\n" //
							+ "	protected static final String NO_ERROR = \"NO_ERROR\";\n" //
							+ "\n" //
							+ "	@Getter\n" //
							+ "	@RequiredArgsConstructor\n" //
							+ "	protected static class ValidationFailure<T extends Enum<?>> {\n" //
							+ "\n" //
							+ "		private final T reason;\n" //
							+ "\n" //
							+ "		private Map<String, String> properties = new HashMap<>();\n" //
							+ "\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Getter(AccessLevel.PROTECTED)\n" //
							+ "	private Map<String, String> properties = new HashMap<>();\n" //
							+ "\n" //
							+ "	protected abstract List<ValidationFailure<? extends Enum<?>>> getValidationFailures();\n" //
							+ "\n" //
							+ "	protected ServiceException(Map<String, String> passedProperties) {\n" //
							+ "		passedProperties.forEach((k, v) -> properties.put(k, v));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public String getLocalizedMessage(ResourceManager resourceManager, LocalizationSO localization) {\n" //
							+ "		if (getValidationFailures().isEmpty()) {\n" //
							+ "			return resourceManager\n" //
							+ "					.getLocalizedString(getClass().getSimpleName() + \".\" + NO_ERROR + \".label\", localization);\n" //
							+ "		}\n" //
							+ "		return getValidationFailures()\n" //
							+ "				.stream()\n" //
							+ "				.map(vf -> toLocalizedMessage(vf, resourceManager, localization))\n" //
							+ "				.reduce((s0, s1) -> s0 + \"\\n\" + s1)\n" //
							+ "				.orElse(\"-\");\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private String toLocalizedMessage(ValidationFailure<? extends Enum<?>> vf, ResourceManager resourceManager,\n" //
							+ "			LocalizationSO localization) {\n" //
							+ "		String s =\n" //
							+ "				resourceManager\n" //
							+ "						.getLocalizedString(\n" //
							+ "								getClass().getSimpleName() + \".\" + vf.getReason().name() + \".label\",\n" //
							+ "								LocalizationSO.DE);\n" //
							+ "		for (String k : vf.getProperties().keySet()) {\n" //
							+ "			s = s.replace(\"{\" + k + \"}\", vf.getProperties().get(k));\n" //
							+ "		}\n" //
							+ "		for (String k : getProperties().keySet()) {\n" //
							+ "			s = s.replace(\"{\" + k + \"}\", getProperties().get(k));\n" //
							+ "		}\n" //
							+ "		return s;\n" //
							+ "	}\n" //
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