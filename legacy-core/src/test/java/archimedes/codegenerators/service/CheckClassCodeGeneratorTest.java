package archimedes.codegenerators.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
public class CheckClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private ServiceNameGenerator nameGenerator = new ServiceNameGenerator();

	@InjectMocks
	private CheckClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Test
	void happyRunForASimpleObject() {
		// Prepare
		String expected = createExpected();
		DataModel dataModel = readDataModel("Model.xml");
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
		// Check
		assertEquals(expected, returned);
	}

	private String createExpected() {
		String expected = "package " + BASE_PACKAGE_NAME + ".util;\n" + //
				"\n" + //
				"import java.util.function.Supplier;\n" + //
				"\n" + //
				"import lombok.Generated;\n" + //
				"\n" + //
				"/**\n" + //
				" * A utility class for checkers.\n" + //
				" */\n" + //
				"@Generated\n" + //
				"public class Check {\n" + //
				"\n" + //
				"	Check() {\n" + //
				"		throw new UnsupportedOperationException(\"It's an utility class! Do not instantiate!\");\n" + //
				"	}\n" + //
				"\n" + //
				"	/**\n" + //
				"	 * Checks if the passed condition is \"true\" or throws the passed exception.\n" + //
				"	 *\n" + //
				"	 * @param condition        The condition which is to check.\n" + //
				"	 * @param runtimeException The exception to throw.\n" + //
				"	 * @throws NullPointerException Passing a null value as exception and a \"false\" condition.\n" + //
				"	 */\n" + //
				"	public static void ensure(boolean condition, RuntimeException runtimeException) {\n" + //
				"		if (!condition) {\n" + //
				"			throw runtimeException;\n" + //
				"		}\n" + //
				"	}\n" + //
				"\n" + //
				"	/**\n" + //
				"	 * Checks if the passed condition is \"true\" or throws the passed exception.\n" + //
				"	 *\n" + //
				"	 * @param condition                The condition which is to check.\n" + //
				"	 * @param runtimeExceptionSupplier A supplier for the exception to throw.\n" + //
				"	 * @throws NullPointerException Passing a null value as exception supplier and a \"false\" condition.\n"
				+ //
				"	 */\n" + //
				"	public static void ensure(boolean condition, Supplier<RuntimeException> runtimeExceptionSupplier) {\n"
				+ //
				"		if (!condition) {\n" + //
				"			throw runtimeExceptionSupplier.get();\n" + //
				"		}\n" + //
				"	}\n" + //
				"\n" + //
				"	/**\n" + //
				"	 * Checks if the passed condition is \"true\" or throws an IllegalArgumentException with passed message.\n"
				+ //
				"	 *\n" + //
				"	 * @param condition The condition which is to check.\n" + //
				"	 * @param message   The message for the IllegalArgumentException.\n" + //
				"	 */\n" + //
				"	public static void ensure(boolean condition, String message) {\n" + //
				"		if (!condition) {\n" + //
				"			throw new IllegalArgumentException(message);\n" + //
				"		}\n" + //
				"	}\n" + //
				"\n" + //
				"}";
		return expected;
	}

	@Test
	void happyRunForASimpleObject_NoComment() {
		// Prepare
		String expected = createExpected();
		DataModel dataModel = readDataModel("Model.xml");
		dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
		// Check
		assertEquals(expected, returned);
	}

}