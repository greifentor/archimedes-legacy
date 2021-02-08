package archimedes.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CompoundPredeterminedOptionProviderTest {

	@InjectMocks
	private CompoundPredeterminedOptionProvider unitUnderTest;

	@DisplayName("Tests for the constructor.")
	@Nested
	class TestsForTheConstructor {

		@DisplayName("Sets an empty array for each option type.")
		@ParameterizedTest(name = "{index}: OptionType.{0}")
		@CsvSource(value = { "COLUMN", "DOMAIN", "MODEL", "PANEL", "TABLE" })
		void setsAnEmptyArrayForEachOptionType(OptionType optionType) {
			// Prepare
			String[] expected = new String[0];
			// Check
			String[] returned = unitUnderTest.getSelectableOptions(optionType);
			assertArrayEquals(expected, returned);
		}

	}

	@DisplayName("Tests for method 'addOptions(OptionType, String...)'.")
	@Nested
	class TestsForMethod_addOptions_OptionType_String {

		@DisplayName("Throws an exception if a null value is passed as option type.")
		@Test
		void passANullValueAsOptionType_ThrowsAnException() {
			assertThrows(NullPointerException.class, () -> unitUnderTest.addOptions(null));
		}

		@DisplayName("Throws an exception if a null value is passed as one of the options.")
		@Test
		void passANullValueAsAnOption_ThrowsAnException() {
			assertThrows(NullPointerException.class, () -> unitUnderTest.addOptions(OptionType.COLUMN, ";op", null));
		}

		@DisplayName("Throws an exception if a null value is passed as options.")
		@Test
		void passANullValueAsOptions_ThrowsAnException() {
			assertThrows(NullPointerException.class,
					() -> unitUnderTest.addOptions(OptionType.COLUMN, (String[]) null));
		}

		@DisplayName("Adds the passed options for the passed option type.")
		@Test
		void passASomeOptionsForAnOptionType_AddsThePassedOptionsForTheType() {
			// Prepare
			OptionType optionType = OptionType.DOMAIN;
			String option1 = "option1";
			String option2 = "option2";
			String option3 = "option3";
			String[] expected = { option1, option2, option3 };
			// Run
			unitUnderTest.addOptions(optionType, option1, option2, option3);
			// Check
			// Check
			String[] returned = unitUnderTest.getSelectableOptions(optionType);
			assertArrayEquals(expected, returned);
		}

		@DisplayName("Adds the passed options for the passed option type in multiple calls.")
		@Test
		void passASomeOptionsForAnOptionTypeWithMultipleCalls_AddsThePassedOptionsForTheType() {
			// Prepare
			OptionType optionType = OptionType.DOMAIN;
			String option1 = "option1";
			String option2 = "option2";
			String option3 = "option3";
			String[] expected = { option1, option2, option3 };
			// Run
			unitUnderTest.addOptions(optionType, option1);
			unitUnderTest.addOptions(optionType, option2, option3);
			// Check
			// Check
			String[] returned = unitUnderTest.getSelectableOptions(optionType);
			assertArrayEquals(expected, returned);
		}

	}

}