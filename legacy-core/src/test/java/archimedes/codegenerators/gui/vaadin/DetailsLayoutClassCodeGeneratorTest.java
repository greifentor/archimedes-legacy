package archimedes.codegenerators.gui.vaadin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Types;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class DetailsLayoutClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private DetailsLayoutClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		private String getExpected() {
			return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
			        "\n" + //
			        "import com.vaadin.flow.component.AttachEvent;\n" + //
			        "import com.vaadin.flow.component.textfield.IntegerField;\n" + //
			        "import com.vaadin.flow.component.textfield.TextField;\n" + //
			        "\n" + //
			        "import base.pack.age.name.core.model.ATable;\n" + //
			        "import base.pack.age.name.core.service.ATableService;\n" + //
			        "import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
			        "import base.pack.age.name.gui.SessionData;\n" + //
			        "import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
			        "import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
			        "import lombok.Generated;\n" + //
			        "import lombok.RequiredArgsConstructor;\n" + //
			        "\n" + //
			        "/**\n" + //
			        " * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
			        " */\n" + //
			        "@Generated\n" + //
			        "@RequiredArgsConstructor\n" + //
			        "public class ATableDetailsLayout extends AbstractMasterDataBaseLayout {\n" + //
			        "\n" + //
			        "	public interface Observer {\n" + //
			        "		void save();\n" + //
			        "\n" + //
			        "		void remove();\n" + //
			        "	}\n" + //
			        "\n" + //
			        "	private final ButtonFactory buttonFactory;\n" + //
			        "	private final ATable model;\n" + //
			        "	private final ATableService service;\n" + //
			        "	private final ResourceManager resourceManager;\n" + //
			        "	private final SessionData session;\n" + //
			        "	private final Observer observer;\n" + //
			        "\n" + //
			        "	private IntegerField integerFieldCount;\n" + //
			        "	private TextField textFieldDescription;\n" + //
			        "\n" + //
			        "	@Override\n" + //
			        "	public void onAttach(AttachEvent attachEvent) {\n" + //
			        "		super.onAttach(attachEvent);\n" + //
			        "		createButtons();\n" + //
			        "		integerFieldCount = createIntegerField(\"count\", model.getCount(), 1, 10);\n" + //
			        "		textFieldDescription = createTextField(\"description\", model.getDescription());\n"
			        + //
			        "		getStyle().set(\"-moz-border-radius\", \"4px\");\n"
			        + //
			        "		getStyle().set(\"-webkit-border-radius\", \"4px\");\n"
			        + //
			        "		getStyle().set(\"border-radius\", \"4px\");\n"
			        + //
			        "		getStyle().set(\"border\", \"1px solid gray\");\n"
			        + //
			        "		getStyle()\n"
			        + //
			        "				.set(\n"
			        + //
			        "						\"box-shadow\",\n"
			        + //
			        "						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n"
			        + //
			        "		setMargin(false);\n"
			        + //
			        "		setWidthFull();\n"
			        + //
			        "		add(\n"
			        + //
			        "				integerFieldCount,\n"
			        + //
			        "				textFieldDescription,\n"
			        + //
			        "				getMasterDataButtonLayout(model.getId() > 0));\n"
			        + //
			        "	}\n"
			        + //
			        "\n"
			        + //
			        "	@Override\n"
			        + //
			        "	protected ButtonFactory getButtonFactory() {\n"
			        + //
			        "		return buttonFactory;\n"
			        + //
			        "	}\n"
			        + //
			        "\n"
			        + //
			        "	@Override\n"
			        + //
			        "	protected ResourceManager getResourceManager() {\n"
			        + //
			        "		return resourceManager;\n"
			        + //
			        "	}\n"
			        + //
			        "\n"
			        + //
			        "	@Override\n"
			        + //
			        "	protected SessionData getSessionData() {\n"
			        + //
			        "		return session;\n"
			        + //
			        "	}\n"
			        + //
			        "\n"
			        + //
			        "	@Override\n"
			        + //
			        "	protected String getTextFieldResourceId() {\n"
			        + //
			        "		return \"ATableDetailsLayout.details.field.{}.label\";\n"
			        + //
			        "	}\n"
			        + //
			        "\n"
			        + //
			        "	@Override\n"
			        + //
			        "	protected void remove() {\n"
			        + //
			        "		service.delete(model);\n"
			        + //
			        "		observer.remove();\n"
			        + //
			        "	}\n"
			        + //
			        "\n"
			        + //
			        "	@Override\n"
			        + //
			        "	protected void save() {\n"
			        + //
			        "		model.setCount(integerFieldCount.getValue());\n"
			        + //
			        "		model.setDescription(textFieldDescription.getValue());\n"
			        + //
			        "		service.update(model);\n"
			        + //
			        "		observer.save();\n"
			        + //
			        "	}\n"
			        + //
			        "\n"
			        + //
			        "}";
		}

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected();
			DataModel dataModel = readDataModel("Model.xml");
			TableModel tableModel = dataModel.getTableByName("A_TABLE");
			tableModel
			        .getColumnByName("Description")
			        .addOption(new Option(PageLayoutClassCodeGenerator.GUI_EDITOR_POS, "1"));
			tableModel.addColumn(new Tabellenspalte("COUNT", new Domain("Counts", Types.INTEGER, -1, -1)));
			tableModel.getColumnByName("COUNT").addOption(new Option(PageLayoutClassCodeGenerator.GUI_EDITOR_POS, "0"));
			tableModel.getColumnByName("COUNT").addOption(new Option(AbstractClassCodeGenerator.MAX, "10"));
			tableModel.getColumnByName("COUNT").addOption(new Option(AbstractClassCodeGenerator.MIN, "1"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}