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
public class UniqueConstraintViolationExceptionClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private ServiceNameGenerator nameGenerator = new ServiceNameGenerator();

	@InjectMocks
	private UniqueConstraintViolationExceptionClassCodeGenerator unitUnderTest;

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
		String expected = "package " + BASE_PACKAGE_NAME + ".core.service.exception;\n" + //
				"\n" + //
				"import java.util.ArrayList;\n" + //
				"import java.util.List;\n" + //
				"\n" + //
				"import lombok.Generated;\n" + //
				"\n" + //
				"@Generated\n" + //
				"public class UniqueConstraintViolationException extends RuntimeException {\n" + //
				"\n" + //
				"	private List<String> attributeNames = new ArrayList<>();\n" + //
				"	private String className;\n" + //
				"\n" + //
				"	public UniqueConstraintViolationException(String message, String className, String... attributeNames) {\n"
				+ //
				"		super(message);\n" + //
				"		this.className = className;\n" + //
				"		for (String attributeName : attributeNames) {\n" + //
				"			if ((attributeName != null) && !this.attributeNames.contains(attributeName)) {\n" + //
				"				this.attributeNames.add(attributeName);\n" + //
				"			}\n" + //
				"		}\n" + //
				"	}\n" + //
				"\n" + //
				"	public List<String> getAttributeNames() {\n" + //
				"		return List.copyOf(attributeNames);\n" + //
				"	}\n" + //
				"\n" + //
				"	public String getClassName() {\n" + //
				"		return className;\n" + //
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