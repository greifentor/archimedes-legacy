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
public class UserAuthorizationCheckerClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private UserAuthorizationCheckerClassCodeGenerator unitUnderTest;

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
							+ "import org.apache.logging.log4j.LogManager;\n" //
							+ "import org.apache.logging.log4j.Logger;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.router.BeforeEnterEvent;\n" //
							+ "\n" //
							+ "import base.pack.age.name.gui.SessionData;\n" //
							+ "import lombok.experimental.UtilityClass;\n" //
							+ "import lombok.Generated;\n" //
							+ "\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A class for user authorization checks. Override with your method to check this context.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s +=
					"@Generated\n" //
							+ "public class UserAuthorizationChecker {\n" //
							+ "\n"//
							+ "	private static final Logger logger = LogManager.getLogger(UserAuthorizationChecker.class);\n" //
							+ "\n" //
							+ "	public void forwardToLoginOnNoUserSetForSession(SessionData sessionData, BeforeEnterEvent beforeEnterEvent) {\n" //
							+ "		if (sessionData.getUserName() == null) {\n" //
							+ "			logger.info(\"no authorization forwarded to login page.\");\n" //
							+ "			beforeEnterEvent.forwardTo(ApplicationStartView.URL);\n" //
							+ "		}\n" //
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