package archimedes.ucg.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class SimpleYamlParserTest {

	private SimpleYamlParser unitUnderTest = new SimpleYamlParser();

	private static Map<String, Object> map(Object... keysAndValues) {
		Map<String, Object> map = new LinkedHashMap<>();
		for (int i = 0; i < keysAndValues.length; i += 2) {
			map.put((String) keysAndValues[i], keysAndValues[i + 1]);
		}
		return map;
	}

	private static List<Object> list(Object... items) {
		return Arrays.asList(items);
	}

	@Nested
	class TestsOfMethod_parse_String {

		@Test
		void returnsNull_passingAnEmptyString() {
			// Run
			Object returned = unitUnderTest.parse("");
			// Check
			assertNull(returned);
		}

		@Test
		void returnsNull_passingOnlyCommentsAndDocumentMarkers() {
			// Prepare
			String yaml = "---\n# comment\n\n...\n";
			// Run
			Object returned = unitUnderTest.parse(yaml);
			// Check
			assertNull(returned);
		}

		@Test
		void returnsAMapping_passingSimpleKeyValuePairs() {
			// Prepare
			String yaml = "a: 1\nb: text with spaces\nc:\n";
			// Run
			Object returned = unitUnderTest.parse(yaml);
			// Check
			assertEquals(map("a", "1", "b", "text with spaces", "c", null), returned);
		}

		@Test
		void returnsANestedMapping_passingIndentedKeys() {
			// Prepare
			String yaml = "type:\n  name: UUID\n  inner:\n    x: y\nother: z";
			// Run
			Object returned = unitUnderTest.parse(yaml);
			// Check
			assertEquals(map("type", map("name", "UUID", "inner", map("x", "y")), "other", "z"), returned);
		}

		@Test
		void returnsASequenceOfMappings_passingIndentedSequence() {
			// Prepare
			String yaml = "items:\n  - name: a\n    value: 1\n  - name: b\nafter: x\n";
			// Run
			Object returned = unitUnderTest.parse(yaml);
			// Check
			assertEquals(map("items", list(map("name", "a", "value", "1"), map("name", "b")), "after", "x"), returned);
		}

		@Test
		void returnsASequence_passingSequenceAtSameIndentationAsTheKey() {
			// Prepare
			String yaml = "outer:\n  identifiers:\n  - A\n  - B\n  name: n\n";
			// Run
			Object returned = unitUnderTest.parse(yaml);
			// Check
			assertEquals(map("outer", map("identifiers", list("A", "B"), "name", "n")), returned);
		}

		@Test
		void returnsNestedSequences_passingSequenceInSequence() {
			// Prepare
			String yaml = "- - a\n  - b\n-\n  - c\n- d\n";
			// Run
			Object returned = unitUnderTest.parse(yaml);
			// Check
			assertEquals(list(list("a", "b"), list("c"), "d"), returned);
		}

		@Test
		void returnsAMapping_passingWindowsLineEndings() {
			// Prepare
			String yaml = "a:\r\n  - x\r\nb: y\r\n";
			// Run
			Object returned = unitUnderTest.parse(yaml);
			// Check
			assertEquals(map("a", list("x"), "b", "y"), returned);
		}

		@Test
		void returnsFlowSequenceItems_passingAFlowSequence() {
			// Prepare
			String yaml = "a: [x, 'y, z', \"q\"]\nb: []";
			// Run
			Object returned = unitUnderTest.parse(yaml);
			// Check
			assertEquals(map("a", list("x", "y, z", "q"), "b", list()), returned);
		}

		@Test
		void removesComments_passingCommentsAfterValues() {
			// Prepare
			String yaml = "# head\na: b # comment\nc: d#nocomment\n";
			// Run
			Object returned = unitUnderTest.parse(yaml);
			// Check
			assertEquals(map("a", "b", "c", "d#nocomment"), returned);
		}

		@Test
		void returnsUnquotedValues_passingQuotedStrings() {
			// Prepare
			String yaml = "a: 'it''s # no comment'\nb: \"x: \\\"y\\\"\\n\\u0041\"\n'c: d': e";
			// Run
			Object returned = unitUnderTest.parse(yaml);
			// Check
			assertEquals(map("a", "it's # no comment", "b", "x: \"y\"\nA", "c: d", "e"), returned);
		}

		@Test
		void returnsNullValues_passingNullMarkers() {
			// Prepare
			String yaml = "a: ~\nb: null\nc: 'null'";
			// Run
			Object returned = unitUnderTest.parse(yaml);
			// Check
			assertEquals(map("a", null, "b", null, "c", "null"), returned);
		}

		@Test
		void throwsAnException_passingTabIndentation() {
			// Prepare
			String yaml = "a:\n\tb: c";
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.parse(yaml));
		}

		@Test
		void throwsAnException_passingADuplicateKey() {
			// Prepare
			String yaml = "a: 1\na: 2";
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.parse(yaml));
		}

		@Test
		void throwsAnException_passingABlockScalar() {
			// Prepare
			String yaml = "a: |\n  text";
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.parse(yaml));
		}

		@Test
		void throwsAnException_passingAnUnexpectedIndentation() {
			// Prepare
			String yaml = "a: 1\n  b: 2";
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.parse(yaml));
		}

		@Test
		void throwsAnException_passingASequenceAfterAMappingOnTheSameLevel() {
			// Prepare
			String yaml = "a: 1\n- b";
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.parse(yaml));
		}

		@Test
		void throwsAnException_passingAnUnterminatedQuotedString() {
			// Prepare
			String yaml = "a: 'text";
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.parse(yaml));
		}

	}

}
