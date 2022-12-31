package archimedes.codegenerators.gui.vaadin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class ApplicationStarterClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private ApplicationStarterClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin", false, "null");
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment, String noKeyValue) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" //
							+ "\n" //
							+ "import org.springframework.boot.SpringApplication;\n" //
							+ "import org.springframework.boot.autoconfigure.EnableAutoConfiguration;\n" //
							+ "import org.springframework.boot.autoconfigure.SpringBootApplication;\n" //
							+ "import org.springframework.boot.autoconfigure.domain.EntityScan;\n" //
							+ "import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;\n" //
							+ "import org.springframework.context.annotation.ComponentScan;\n" //
							+ "import org.springframework.data.jpa.repository.config.EnableJpaRepositories;\n" //
							+ "\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * The starter class for the application.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s +=
					"@SpringBootApplication\n" //
							+ "@EnableAutoConfiguration\n" //
							+ "@EnableJpaRepositories(\"base.pack.age.name.persistence.repository\")\n" //
							+ "@ComponentScan(\"base.pack.age\")\n" //
							+ "@EntityScan(\"base.pack.age.name.persistence.entity\")\n" //
							+ "public class ApplicationStarter extends SpringBootServletInitializer {\n" //
							+ "\n" //
							+ "	public static void main(String[] args) {\n" //
							+ "		SpringApplication.run(ApplicationStarter.class, args);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectWithSuppressedComments() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin", true, "null");
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
			// Check
			assertEquals(expected, returned);
		}

	}

}