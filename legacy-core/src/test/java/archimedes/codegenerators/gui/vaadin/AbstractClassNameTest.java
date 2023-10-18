package archimedes.codegenerators.gui.vaadin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;

public abstract class AbstractClassNameTest {

	protected static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	protected abstract BiFunction<DataModel, TableModel, String> calledMethod();

	protected abstract String getAlternativeIdentifier();

	protected abstract String getExpectedClassNameSuffix();

	protected abstract DataModel getModel();

	protected abstract TableModel getTable();

	@Test
	void getDetailsLayoutClassName_PassTableModelWithEmptyName_ThrowsException() {
		// Prepare
		when(getTable().getName()).thenReturn("");
		// Run
		assertThrows(IllegalArgumentException.class, () -> {
			calledMethod().apply(getModel(), getTable());
		});
	}

	@Test
	void getDetailsLayoutClassName_PassNullValue_ReturnsNullValue() {
		assertNull(calledMethod().apply(getModel(), null));
	}

	@Test
	void getDetailsLayoutClassName_PassTableModelWithNameCamelCase_ReturnsACorrectClassName() {
		// Prepare
		String expected = "TestTable" + getExpectedClassNameSuffix();
		when(getTable().getName()).thenReturn("TestTable");
		// Run
		String returned = calledMethod().apply(getModel(), getTable());
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void getDetailsLayoutClassName_PassTableModelWithNameUpperCase_ReturnsACorrectClassName() {
		// Prepare
		String expected = "Table" + getExpectedClassNameSuffix();
		when(getTable().getName()).thenReturn("TABLE");
		// Run
		String returned = calledMethod().apply(getModel(), getTable());
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void getDetailsLayoutClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectClassName() {
		// Prepare
		String expected = "TableName" + getExpectedClassNameSuffix();
		when(getTable().getName()).thenReturn("TABLE_NAME");
		// Run
		String returned = calledMethod().apply(getModel(), getTable());
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void getDetailsLayoutClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectClassName() {
		// Prepare
		String expected = "TableName" + getExpectedClassNameSuffix();
		when(getTable().getName()).thenReturn("table_name");
		// Run
		String returned = calledMethod().apply(getModel(), getTable());
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void getDetailsLayoutClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectClassName() {
		// Prepare
		String expected = "TableName" + getExpectedClassNameSuffix();
		when(getTable().getName()).thenReturn("Table_Name");
		// Run
		String returned = calledMethod().apply(getModel(), getTable());
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void getDetailsLayoutClassName_PassTableModelWithNameLowerCase_ReturnsACorrectClassName() {
		// Prepare
		String expected = "Table" + getExpectedClassNameSuffix();
		when(getTable().getName()).thenReturn("table");
		// Run
		String returned = calledMethod().apply(getModel(), getTable());
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void getDetailsLayoutClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectClassName() {
		// Prepare
		String expected = "T" + getExpectedClassNameSuffix();
		when(getTable().getName()).thenReturn("T");
		// Run
		String returned = calledMethod().apply(getModel(), getTable());
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void getDetailsLayoutClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectClassName() {
		// Prepare
		String expected = "T" + getExpectedClassNameSuffix();
		when(getTable().getName()).thenReturn("t");
		// Run
		String returned = calledMethod().apply(getModel(), getTable());
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void getDetailsLayoutClassName_PassDataModelWithAlternativeSuffixOption_ReturnsACorrectClassName() {
		// Prepare
		String expected = "TableAltSuffix";
		when(getTable().getName()).thenReturn("Table");
		when(getTable().getDataModel()).thenReturn(getModel());
		doReturn(new Option(getAlternativeIdentifier(), "AltSuffix"))
				.when(getModel())
				.getOptionByName(getAlternativeIdentifier());
		// Run
		String returned = calledMethod().apply(getModel(), getTable());
		// Check
		assertEquals(expected, returned);
	}

}
