package archimedes.legacy.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for class "StringTrimer".
 *
 * @author ollie (15.12.2019)
 */
@ExtendWith(MockitoExtension.class)
public class StringTrimerTest {

	@InjectMocks
	private StringTrimer unitUnderTest;

	@DisplayName("Tests for method 'trimLength'.")
	@Nested
	class TestsForMethod_trimLength_String_int_boolean {

		@DisplayName("Throws an exception passing a null value as string.")
		@Test
		void passANullString_ThrowsAnException() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.trimLength(null, 42, false));
		}

		@DisplayName("Throws an exception passing a value less than one as length.")
		@ParameterizedTest
		@CsvSource(value = { "0", "-1", "" + Integer.MIN_VALUE })
		void passANullValue_ThrowsAnException(int value) {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.trimLength(";oP", value, false));
		}

		@DisplayName("Returns the passed string, if passed length is greater than the strings length.")
		@ParameterizedTest
		@CsvSource(value = { "true", "false" })
		void passAStringWithLengthLessThanPassedLength_ReturnsThePassString(boolean add3Dots) {
			// Prepare
			String passed = "12345";
			int len = passed.length() + 1;
			// Run
			String returned = unitUnderTest.trimLength(passed, len, add3Dots);
			// Check
			assertThat(returned, equalTo(passed));
		}

		@DisplayName("Returns the passed string, if passed length is equal to the strings length.")
		@ParameterizedTest
		@CsvSource(value = { "true", "false" })
		void passAStringWithLengthEqualToThePassedLength_ReturnsThePassString(boolean add3Dots) {
			// Prepare
			String passed = "12345";
			int len = passed.length();
			// Run
			String returned = unitUnderTest.trimLength(passed, len, add3Dots);
			// Check
			assertThat(returned, equalTo(passed));
		}

		@DisplayName("Returns a trimmed string, if passed length is less than the strings length and three dots option "
				+ "not set.")
		@Test
		void passAStringWithLengthGreaterThanThePassedLength3DotsNotSet_ReturnsATimmedString() {
			// Prepare
			String passed = "1234567890";
			int len = 5;
			String expected = "12345";
			// Run
			String returned = unitUnderTest.trimLength(passed, len, false);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@DisplayName("Returns a trimmed string, if passed length is less than and the strings length but greater than "
				+ "3 and three dots option set.")
		@Test
		void passAStringWithLengthGreaterThanThePassedLengthAndGreaterThan3_3DotsSet_ReturnsATimmedStringEndingWith3Dots() {
			// Prepare
			String passed = "1234567890";
			int len = 5;
			String expected = "12...";
			// Run
			String returned = unitUnderTest.trimLength(passed, len, true);
			// Check
			assertThat(returned, equalTo(expected));
		}

	}

	@DisplayName("Returns a trimmed string, if passed length is less than the strings length and equals to 3 and three "
			+ "dots option set.")
	@Test
	void passAStringWithLengthGreaterThanThePassedLengthAndLengthIsEqualTo3_3DotsSet_ReturnsATrimmedStringWithNoDots() {
		// Prepare
		String passed = "123";
		int len = 2;
		String expected = "12";
		// Run
		String returned = unitUnderTest.trimLength(passed, len, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@DisplayName("Returns a trimmed string, if passed length is less than the strings length and less than 3 and three "
			+ "dots option set.")
	@Test
	void passAStringWithLengthGreaterThanThePassedLengthAndLengthIsLessThan3_3DotsSet_ReturnsATrimmedStringWithNoDots() {
		// Prepare
		String passed = "12";
		int len = 1;
		String expected = "1";
		// Run
		String returned = unitUnderTest.trimLength(passed, len, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

}