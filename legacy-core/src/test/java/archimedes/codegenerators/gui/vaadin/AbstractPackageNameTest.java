package archimedes.codegenerators.gui.vaadin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import archimedes.model.DataModel;
import archimedes.scheme.Option;

public abstract class AbstractPackageNameTest {

	protected static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	protected abstract Function<DataModel, String> calledMethod();

	protected abstract String getAlternativeIdentifier();

	protected abstract String getExpectedPackageName();

	protected abstract DataModel getModel();

	@Test
	void getDetailsDialogPackageName_PassANullValueAsModel_ReturnsANullValue() {
		assertNull(calledMethod().apply(null));
	}

	@Test
	void getDetailsDialogPackageName_PassANullValueAsTable_ReturnsANullValue() {
		assertEquals(getExpectedPackageName(), calledMethod().apply(getModel()));
	}

	@Test
	void getDetailsDialogPackageName_PassAValidTableModel_ReturnsACorrectPackage() {
		// Prepare
		String expected = BASE_PACKAGE_NAME + "." + getExpectedPackageName();
		when(getModel().getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
		// Run
		String returned = calledMethod().apply(getModel());
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void getDetailsDialogPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrectPackage() {
		// Prepare
		String expected = getExpectedPackageName();
		when(getModel().getBasePackageName()).thenReturn("");
		// Run
		String returned = calledMethod().apply(getModel());
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void getDetailsDialogPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrectPackage() {
		// Prepare
		String expected = getExpectedPackageName();
		when(getModel().getBasePackageName()).thenReturn(null);
		// Run
		String returned = calledMethod().apply(getModel());
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void getDetailsDialogPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
		// Prepare
		String alternativePackageName = "alternative.package";
		when(getModel().getOptionByName(getAlternativeIdentifier()))
				.thenReturn(new Option(getAlternativeIdentifier(), alternativePackageName));
		// Run & Check
		assertEquals(alternativePackageName, calledMethod().apply(getModel()));
	}

}
