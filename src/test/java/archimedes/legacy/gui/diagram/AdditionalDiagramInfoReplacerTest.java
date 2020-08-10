package archimedes.legacy.gui.diagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AdditionalDiagramInfoReplacerTest {

	@Mock
	private PropertyGetter propertyGetter;

	@InjectMocks
	private AdditionalDiagramInfoReplacer unitUnderTest;

	@DisplayName("Should return the passed string passing a property getter with an empty map.")
	@Test
	public void passPropertyGetterWithAnEmptyMap_ReturnsThePassedString() {
		// Prepare
		String expected = "a string";
		when(propertyGetter.getProperties()).thenReturn(new HashMap<>());
		// Run
		String returned = unitUnderTest.replace(expected, propertyGetter);
		// Check
		assertEquals(expected, returned);
	}

	@DisplayName("Should return the passed string if no property of the property getter matches.")
	@Test
	public void passPropertyGetterWithNotMatchingProperties_ReturnsThePassedString() {
		// Prepare
		String expected = "a string with ${property}";
		Map<String, String> m = new HashMap<>();
		m.put("name", "value");
		when(propertyGetter.getProperties()).thenReturn(m);
		// Run
		String returned = unitUnderTest.replace(expected, propertyGetter);
		// Check
		assertEquals(expected, returned);
	}

	@DisplayName("Should return the passed string with replaced property place holder if a property of the property "
			+ "getter matches the place holder.")
	@Test
	public void passPropertyGetterWithMatchingProperties_ReturnsThePassedStringWithReplacedPlaceHolder() {
		// Prepare
		String expected = "a string with value";
		String passed = "a string with ${Option.NAME}";
		Map<String, String> m = new HashMap<>();
		m.put("NAME", "value");
		when(propertyGetter.getProperties()).thenReturn(m);
		// Run
		String returned = unitUnderTest.replace(passed, propertyGetter);
		// Check
		assertEquals(expected, returned);
	}

	@DisplayName("Should return the passed string with replaced property place holder alltough its value is a null "
			+ "pointer.")
	@Test
	public void passPropertyGetterWithNullProperties_ReturnsThePassedStringWithReplacedPlaceHolderConvertingTheNullToAString() {
		// Prepare
		String expected = "a string with null";
		String passed = "a string with ${Option.NAME}";
		Map<String, String> m = new HashMap<>();
		m.put("NAME", null);
		when(propertyGetter.getProperties()).thenReturn(m);
		// Run
		String returned = unitUnderTest.replace(passed, propertyGetter);
		// Check
		assertEquals(expected, returned);
	}

}