package archimedes.snippets.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExceptionClassSnippetGeneratorTest {

	@InjectMocks
	private ExceptionClassSnippetGenerator unitUnderTest;

	@DisplayName("Test for method 'getName()'.")
	@Nested
	class TestForMethod_getName {

		@DisplayName("Returns the correct name for the snippet generator.")
		@Test
		void returnsTheCorrectNameOfTheSnippetGenerator() {
			assertEquals("Exception Generator", unitUnderTest.getName());
		}

	}

	@DisplayName("Test for method 'getVersion()'.")
	@Nested
	class TestForMethod_getVersion {

		@DisplayName("Returns the correct version for the snippet generator.")
		@Test
		void returnsTheCorrectVersionOfTheSnippetGenerator() {
			assertEquals("0.0.1", unitUnderTest.getVersion());
		}

	}

}