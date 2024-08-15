package archimedes.codegenerators.gui.vaadin.masterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class PopupNotificationClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private ServiceNameGenerator nameGenerator = new ServiceNameGenerator();

	@InjectMocks
	private PopupNotificationClassCodeGenerator unitUnderTest;

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
					"package " + BASE_PACKAGE_NAME + ".gui.vaadin.masterdata;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.component.button.Button;\n" //
							+ "import com.vaadin.flow.component.button.ButtonVariant;\n" //
							+ "import com.vaadin.flow.component.html.Div;\n" //
							+ "import com.vaadin.flow.component.icon.Icon;\n" //
							+ "import com.vaadin.flow.component.notification.Notification;\n" //
							+ "import com.vaadin.flow.component.notification.Notification.Position;\n" //
							+ "import com.vaadin.flow.component.notification.NotificationVariant;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.HorizontalLayout;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "\n";
			if (comment) {
				expected +=
						"/**\n" //
								+ " * A GUI component for showing errors.\n" //
								+ " *\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n";
			}
			expected +=
					"@Generated\n" //
							+ "public class PopupNotification {\n" //
							+ "\n" //
							+ "	public static void showError(String errorText) {\n" //
							+ "		Notification notification = new Notification();\n" //
							+ "		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);\n" //
							+ "		Div text = createText(errorText);\n" //
							+ "		Button closeButton = createCloseButton(notification);\n" //
							+ "		HorizontalLayout layout = new HorizontalLayout(text, closeButton);\n" //
							+ "		layout.setAlignItem(Alignment.CENTER);\n" //
							+ "		notification.add(layout);\n" //
							+ "		notification.setPosition(Position.TOP_STRETCH);\n" //
							+ "		notification.open();\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private static Div createText(String errorText) {\n" //
							+ "		Div div = new Div();\n" //
							+ "		div.getElement().setProperty(\"innerHTML\", \"<span>\" + errorText + \"</span>\");\n" //
							+ "		return div;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private static Button createCloseButton(Notification notification) {\n" //
							+ "		Button closeButton = new Button(new Icon(\"lumo\", \"cross\"));\n" //
							+ "		closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);\n" //
							+ "		// V24 closeButton.setAriaLabel(\"Close\");\n" //
							+ "		closeButton.getElement().setAttribute(\"aria-label\", \"Close\");\n" //
							+ "		closeButton.addClickListener(event -> {\n" //
							+ "			notification.close();\n" //
							+ "		});\n" //
							+ "		return closeButton;\n" //
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