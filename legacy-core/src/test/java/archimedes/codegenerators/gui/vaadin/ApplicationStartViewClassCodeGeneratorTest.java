package archimedes.codegenerators.gui.vaadin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class ApplicationStartViewClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private ApplicationStartViewClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class SimpleClass {

			private String getExpected(boolean suppressComment) {
				String s = "package base.pack.age.name.gui.vaadin;\n" + //
						"\n" + //
						"import org.apache.logging.log4j.LogManager;\n" + //
						"import org.apache.logging.log4j.Logger;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.DetachEvent;\n" + //
						"import com.vaadin.flow.component.dependency.CssImport;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.router.BeforeEnterEvent;\n" + //
						"import com.vaadin.flow.router.BeforeEnterObserver;\n" + //
						"import com.vaadin.flow.router.PreserveOnRefresh;\n" + //
						"import com.vaadin.flow.router.Route;\n" + //
						"import com.vaadin.flow.spring.annotation.VaadinSessionScope;\n" + //
						"\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n";
				if (!suppressComment) {
					s += "/**\n" + //
							" * A start view for the application.\n" + //
							" *\n" + //
							" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
							" */\n";
				}
				s += "@Route(ApplicationStartView.URL)\n" + //
						"@PreserveOnRefresh\n" + //
						"@VaadinSessionScope\n" + //
						"@CssImport(\"./styles/shared-styles.css\")\n" + //
						"@CssImport(value = \"./styles/vaadin-text-field-styles.css\", themeFor = \"vaadin-text-field\")\n"
						+ //
						"@CssImport(value = \"./styles/vaadin-text-area-styles.css\", themeFor = \"vaadin-area-field\")\n"
						+ //
						"@RequiredArgsConstructor\n" + //
						"public class ApplicationStartView extends VerticalLayout implements BeforeEnterObserver {\n" + //
						"\n" + //
						"	public static final Logger LOG = LogManager.getLogger(ApplicationStartView.class);\n" + //
						"	public static final String URL = \"carp-dnd\";\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" + //
						"		LOG.info(\"before enter\");\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		LOG.info(\"attached\");\n" + //
						"		refresh();\n" + //
						"	}\n" + //
						"\n" + //
						"	private void refresh() {\n" + //
						"		removeAll();\n" + //
						"		getUI().ifPresent(ui -> ui.navigate(MainMenuView.URL));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void onDetach(DetachEvent detachEvent) {\n" + //
						"		super.onDetach(detachEvent);\n" + //
						"		LOG.info(\"detached\");\n" + //
						"	}\n" + //
						"\n" + //
						"}";
				return s;
			}

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpected(false);
				DataModel dataModel = readDataModel("Model.xml");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}
