package archimedes.legacy.checkers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.sql.Types;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.acf.checker.ModelCheckerMessage.Level;
import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.Tabelle;
import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.legacy.scheme.View;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import baccara.gui.GUIBundle;

/**
 * Unit tests for class "ModelCheckerNoComplexPrimaryKey".
 *
 * @author ollie (31.10.2019)
 */
@ExtendWith(MockitoExtension.class)
public class ModelCheckerNoComplexPrimaryKeyTest {

	private static final String TABLE_NAME = "TableName";

	@Mock
	private GUIBundle guiBundle;

	@InjectMocks
	private ModelCheckerNoComplexPrimaryKey unitUnderTest;

	@DisplayName("Throws an exception if a null value is passed as data model.")
	@Test
	void nullValuePassedAsDataModel_ThrowsAnException() {
		assertThrows(IllegalArgumentException.class, () -> unitUnderTest.check(null));
	}

	@DisplayName("Returns an empty array if a data model without violations is passed.")
	@Test
	void passAModelWithoutViolations_ReturnsAnEmptyArray() {
		// Prepare
		DataModel dataModel = getDataModel(false, false);
		ModelCheckerMessage[] expected = new ModelCheckerMessage[0];
		// Run
		ModelCheckerMessage[] returned = this.unitUnderTest.check(dataModel);
		// Check
		assertThat(returned, equalTo(expected));
	}

	private DataModel getDataModel(boolean withViolations, boolean manyToMany) {
		Diagramm dataModel = new Diagramm();
		TableModel table = new Tabelle(new View(), 0, 0, dataModel);
		table.setName(TABLE_NAME);
		table.setManyToManyRelation(manyToMany);
		ColumnModel column1 = new Tabellenspalte("COLUMN_1", new Domain("TYPE", Types.BIGINT, 0, 0), true);
		table.addColumn(column1);
		ColumnModel column2 = new Tabellenspalte("COLUMN_2", new Domain("TYPE", Types.BIGINT, 0, 0), withViolations);
		table.addColumn(column2);
		dataModel.addTable(table);
		return dataModel;
	}

	@DisplayName("Returns an array with an error message if a data model with violations is passed.")
	@Test
	void passAModelWithViolations_ReturnsAnArrayWithAnErrorMessage() {
		// Prepare
		String message = "message " + TABLE_NAME;
		DataModel dataModel = getDataModel(true, false);
		ModelCheckerMessage[] expected = new ModelCheckerMessage[] { new ModelCheckerMessage(Level.ERROR, message) };
		when(this.guiBundle.getResourceText(ModelCheckerNoComplexPrimaryKey.RES_MODEL_CHECKER_NO_COMPLEX_PRIMARY_KEY,
				TABLE_NAME)).thenReturn(message);
		// Run
		ModelCheckerMessage[] returned = this.unitUnderTest.check(dataModel);
		// Check
		assertThat(returned[0].getLevel(), equalTo(expected[0].getLevel()));
		assertThat(returned[0].getMessage(), equalTo(expected[0].getMessage()));
	}

	@DisplayName("Returns an empty array if a data model without violations is passed for a many to many relation.")
	@Test
	void passAModelWithoutViolationsManyToManyRelation_ReturnsAnEmptyArray() {
		// Prepare
		String message = "message " + TABLE_NAME;
		DataModel dataModel = getDataModel(true, true);
		ModelCheckerMessage[] expected = new ModelCheckerMessage[0];
		// Run
		ModelCheckerMessage[] returned = this.unitUnderTest.check(dataModel);
		// Check
		assertThat(returned, equalTo(expected));
	}

}