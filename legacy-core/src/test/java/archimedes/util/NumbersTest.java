package archimedes.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NumbersTest {

	@Nested
	class TestsOfMethod_isAnInteger_String {

		@Test
		void passANullValue_returnsFalse() {
			assertFalse(Numbers.isAnInteger(null));
		}

		@Test
		void passAnEmptyString_returnsFalse() {
			assertFalse(Numbers.isAnInteger(""));
		}

		@Test
		void passAnBlankString_returnsFalse() {
			assertFalse(Numbers.isAnInteger("\n\t"));
		}

		@Test
		void passAnAlphaNumericString_returnsFalse() {
			assertFalse(Numbers.isAnInteger("NCC 1701"));
		}

		@Test
		void passAnStringWithADoubleValue_returnsFalse() {
			assertFalse(Numbers.isAnInteger("8.15"));
		}

		@Test
		void passAnStringWithALongValue_returnsFalse() {
			assertFalse(Numbers.isAnInteger("" + Long.MAX_VALUE));
		}

		@Test
		void passAnStringWithAnIntegerValue_returnsTrue() {
			assertTrue(Numbers.isAnInteger("4711"));
		}

	}

}
