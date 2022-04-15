package archimedes.codegenerators.gui.vaadin.modelcheckers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.acf.checker.ModelCheckerMessage.Level;
import archimedes.codegenerators.gui.vaadin.AbstractGUIVaadinClassCodeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import baccara.gui.GUIBundle;

@ExtendWith(MockitoExtension.class)
class ModelCheckerGuiEditorPosHasAValueTest {

	private static final String MESSAGE = "message";

	@Mock
	private DataModel model;
	@Mock
	private ColumnModel column;
	@Mock
	private OptionModel option;

	@Mock
	private GUIBundle guiBundle;

	@InjectMocks
	private ModelCheckerGuiEditorPosHasAValue unitUnderTest;

	@Nested
	class TestOfMethod_check_DataModel {

		private void trainMocksToReturnOptionWithParameter(String parameter) {
			when(column.isOptionSet(AbstractGUIVaadinClassCodeGenerator.GUI_EDITOR_POS)).thenReturn(true);
			when(column.getOptionByName(AbstractGUIVaadinClassCodeGenerator.GUI_EDITOR_POS)).thenReturn(option);
			when(model.getAllColumns()).thenReturn(new ColumnModel[] { column });
			when(option.getParameter()).thenReturn(parameter);
		}

		private void trainMocksForMessage(String parameter) {
			trainMocksToReturnOptionWithParameter(parameter);
			when(
					guiBundle
							.getResourceText(
									ModelCheckerGuiEditorPosHasAValue.RES_MODEL_CHECKER_GUI_VAADIN_GUI_EDITOR_POS_HAS_NO_NUMERIC_VALUE,
									parameter)).thenReturn(MESSAGE);
		}

		@Nested
		class ReturnsAnEmptyArrayPassing {

			@Test
			void dataModelWithNoFields() {
				// Prepare
				when(model.getAllColumns()).thenReturn(new ColumnModel[0]);
				// Run
				ModelCheckerMessage[] returned = unitUnderTest.check(model);
				// Check
				assertEquals(0, returned.clone().length);
			}

			@Test
			void dataModelWithNoGUIEditorPosFields() {
				// Prepare
				when(model.getAllColumns()).thenReturn(new ColumnModel[] { column });
				// Run
				ModelCheckerMessage[] returned = unitUnderTest.check(model);
				// Check
				assertEquals(0, returned.clone().length);
			}

			@Test
			void dataModelWithCorrectGUIEditorPosFields() {
				// Prepare
				trainMocksToReturnOptionWithParameter("4711");
				// Run
				ModelCheckerMessage[] returned = unitUnderTest.check(model);
				// Check
				assertEquals(0, returned.clone().length);
			}

		}

		@Nested
		class ReturnsAnArrayWithAMessagePassing {

			@ParameterizedTest
			@NullAndEmptySource
			@CsvSource(value = { "NCC 1701", "47.11", "" + Long.MAX_VALUE })
			void dataModelWithGUIEditorPosFieldsThatHasAnEmptyParameter(String parameter) {
				// Prepare
				trainMocksForMessage(parameter);
				// Run
				ModelCheckerMessage[] returned = unitUnderTest.check(model);
				// Check
				assertEquals(1, returned.clone().length);
			}

		}

		@Nested
		class MessageIsCorrectlySetWithCorrect {

			private ModelCheckerMessage getMessage(String parameter) {
				trainMocksForMessage(parameter);
				ModelCheckerMessage[] returned = unitUnderTest.check(model);
				assertTrue(returned.length > 0);
				return returned[0];
			}

			@Test
			void level() {
				assertEquals(Level.ERROR, getMessage(null).getLevel());
			}

			@Test
			void message() {
				assertEquals(MESSAGE, getMessage(null).getMessage());
			}

			@Test
			void object() {
				assertEquals(column, getMessage(null).getObject());
			}

		}

	}

}