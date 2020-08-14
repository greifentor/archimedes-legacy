package archimedes.snippets.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.model.DataModel;
import archimedes.snippets.SnippetGenerator;
import archimedes.snippets.SnippetGeneratorProvider;
import archimedes.snippets.gui.SnippetGeneratorMenuBuilder;
import baccara.gui.GUIBundle;

@ExtendWith(MockitoExtension.class)
public class SnippetGeneratorMenuBuilderTest {

	private static final String MENU_TEXT = "menu title";
	private static final String SG1_NAME = "sg1 name";
	private static final String SG1_VERSION = "sg1 version";

	@Mock
	private GUIBundle guiBundle;
	@Mock
	private Supplier<DataModel> dataModelSupplier;

	@InjectMocks
	private SnippetGeneratorMenuBuilder unitUnderTest;

	private String packageNameTmp = null;

	@BeforeEach
	void setUp() {
		packageNameTmp = SnippetGeneratorMenuBuilder.packageName;
	}

	@AfterEach
	void tearDown() {
		SnippetGeneratorMenuBuilder.packageName = packageNameTmp;
	}

	@DisplayName("Tests for method 'createSnippetGeneratorMenu(List<Object>, GUIBundle)'.")
	@Nested
	class TestsForMethod_createSnippetGeneratorMenu_ListObject_GUIBundle {

		@DisplayName("Returns an empty menu with title of ressource "
				+ "SnippetGeneratorMenuBuilder.RES_SNIPPET_GENERATOR_MENU_TITLE if no snippet generator is found.")
		@Test
		void noSnippetGeneratorsFound_ReturnsAnEmptyMenuWithTitle() throws Exception {
			// Prepare
			when(guiBundle.getResourceText(SnippetGeneratorMenuBuilder.RES_SNIPPET_GENERATOR_MENU_TITLE + ".title"))
					.thenReturn(MENU_TEXT);
			SnippetGeneratorMenuBuilder.packageName = "fantasy.package.name";
			// Run
			JMenu returned = unitUnderTest.createSnippetGeneratorMenu(new ArrayList<>(), guiBundle, dataModelSupplier);
			// Check
			assertEquals(MENU_TEXT, returned.getText());
			assertEquals(0, returned.getMenuComponentCount());
		}

		@DisplayName("Returns a menu with title of ressource "
				+ "SnippetGeneratorMenuBuilder.RES_SNIPPET_GENERATOR_MENU_TITLE and a menu item for each class found in "
				+ "the package 'archimedes.snippets.impl' (defined as default).")
		@Test
		void snippetGeneratorsInArchimedesFound_ReturnsAMenuWithTitleAndRelatedMenuItems() throws Exception {
			// Prepare
			when(guiBundle.getResourceText(SnippetGeneratorMenuBuilder.RES_SNIPPET_GENERATOR_MENU_TITLE + ".title"))
					.thenReturn(MENU_TEXT);
			// Run
			JMenu returned = unitUnderTest.createSnippetGeneratorMenu(new ArrayList<>(), guiBundle, dataModelSupplier);
			// Check
			assertEquals(MENU_TEXT, returned.getText());
			assertEquals(1, returned.getMenuComponentCount());
		}

		@DisplayName("Returns a menu with title of ressource "
				+ "SnippetGeneratorMenuBuilder.RES_SNIPPET_GENERATOR_MENU_TITLE and a menu item for each snippet generator delivered by the provider.")
		@Test
		void snippetGeneratorsOfThePassedProvidersFound_ReturnsAMenuWithTitleAndRelatedMenuItems() throws Exception {
			// Prepare
			when(guiBundle.getResourceText(SnippetGeneratorMenuBuilder.RES_SNIPPET_GENERATOR_MENU_TITLE + ".title"))
					.thenReturn(MENU_TEXT);
			SnippetGeneratorMenuBuilder.packageName = "fantasy.package.name";

			SnippetGenerator sg1 = mock(SnippetGenerator.class);
			SnippetGenerator sg2 = mock(SnippetGenerator.class);
			SnippetGenerator sg3 = mock(SnippetGenerator.class);
			SnippetGeneratorProvider sgp1 = mock(SnippetGeneratorProvider.class);
			SnippetGeneratorProvider sgp2 = mock(SnippetGeneratorProvider.class);

			when(sg1.getName()).thenReturn(SG1_NAME);
			when(sg1.getVersion()).thenReturn(SG1_VERSION);
			when(sgp1.getSnippetGenerators()).thenReturn(Arrays.asList(sg1));
			when(sgp2.getSnippetGenerators()).thenReturn(Arrays.asList(sg2, sg3));

			// Run
			JMenu returned = unitUnderTest.createSnippetGeneratorMenu(Arrays.asList(sgp1, new Object(), sgp2),
					guiBundle, dataModelSupplier);
			// Check
			assertEquals(MENU_TEXT, returned.getText());
			assertEquals(3, returned.getMenuComponentCount());
			assertEquals(SG1_NAME + " (" + SG1_VERSION + ")", ((JMenuItem) returned.getMenuComponent(0)).getText());
			assertEquals("null (null)", ((JMenuItem) returned.getMenuComponent(1)).getText());
		}

		@DisplayName("Returns a menu with title of ressource "
				+ "SnippetGeneratorMenuBuilder.RES_SNIPPET_GENERATOR_MENU_TITLE and a menu item for each snippet generator delivered by the provider.")
		@Test
		void bothTypesOfSnippetGeneratorsFound_ReturnsAMenuWithTitleAndSeparatorBetweenTypesOfSnippetGenerators()
				throws Exception {
			// Prepare
			when(guiBundle.getResourceText(SnippetGeneratorMenuBuilder.RES_SNIPPET_GENERATOR_MENU_TITLE + ".title"))
					.thenReturn(MENU_TEXT);

			SnippetGenerator sg1 = mock(SnippetGenerator.class);
			SnippetGenerator sg2 = mock(SnippetGenerator.class);
			SnippetGenerator sg3 = mock(SnippetGenerator.class);
			SnippetGeneratorProvider sgp1 = mock(SnippetGeneratorProvider.class);
			SnippetGeneratorProvider sgp2 = mock(SnippetGeneratorProvider.class);

			when(sg1.getName()).thenReturn(SG1_NAME);
			when(sg1.getVersion()).thenReturn(SG1_VERSION);
			when(sgp1.getSnippetGenerators()).thenReturn(Arrays.asList(sg1));
			when(sgp2.getSnippetGenerators()).thenReturn(Arrays.asList(sg2, sg3));

			// Run
			JMenu returned = unitUnderTest.createSnippetGeneratorMenu(Arrays.asList(sgp1, new Object(), sgp2),
					guiBundle, dataModelSupplier);
			// Check
			assertEquals(MENU_TEXT, returned.getText());
			assertEquals(5, returned.getMenuComponentCount());
			assertTrue(returned.getMenuComponent(1) instanceof JSeparator);
			assertEquals(SG1_NAME + " (" + SG1_VERSION + ")", ((JMenuItem) returned.getMenuComponent(2)).getText());
		}

	}

}