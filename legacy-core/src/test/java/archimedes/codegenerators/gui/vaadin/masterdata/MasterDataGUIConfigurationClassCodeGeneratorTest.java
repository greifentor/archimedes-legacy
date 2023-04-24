package archimedes.codegenerators.gui.vaadin.masterdata;

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
public class MasterDataGUIConfigurationClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private MasterDataGUIConfigurationClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin.masterdata", false, "null");
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
							+ "import org.springframework.beans.factory.annotation.Value;\n" //
							+ "import org.springframework.context.annotation.Configuration;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.Getter;\n" //
							+ "\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A configuration for the master data GUI.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s +=
					"@Configuration\n" //
							+ "@Generated\n" //
							+ "@Getter\n" //
							+ "public class MasterDataGUIConfiguration {\n" //
							+ "\n" //
							+ "	@Value(\"${gui.masterdata.background-image-file-name:master-data-background.png}\")\n" //
							+ "	private String backgroundFileName;\n" //
							+ "\n" //
							+ "	@Value(\"${gui.masterdata.button.disabled-image-file-name:button-background-disabled.png}\")\n" //
							+ "	private String buttonDisabledBackgroundFileName;\n" //
							+ "\n" //
							+ "	@Value(\"${gui.masterdata.button.disabled-border-color:gray}\")\n" //
							+ "	private String buttonDisabledBorderColor;\n" //
							+ "\n" //
							+ "	@Value(\"${gui.masterdata.button.enabled-image-file-name:button-background-enabled.png}\")\n" //
							+ "	private String buttonEnabledBackgroundFileName;\n" //
							+ "\n" //
							+ "	@Value(\"${gui.masterdata.button.enabled-border-color:blue}\")\n" //
							+ "	private String buttonEnabledBorderColor;\n" //
							+ "\n" //
							+ "}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectWithSuppressedComments() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin.masterdata", true, "null");
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
			// Check
			assertEquals(expected, returned);
		}

	}

}
