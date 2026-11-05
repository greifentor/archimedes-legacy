package archimedes.ucg.yaml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleYamlParser {

	private static final String UNSUPPORTED_VALUE_START_CHARACTERS = "{|>&*!";

	public Object parse(String yaml) {
		List<Line> lines = toLines(yaml);
		if (lines.isEmpty()) {
			return null;
		}
		ParserRun run = new ParserRun(lines);
		Object result = run.parseBlock(lines.get(0).indent);
		if (run.position < lines.size()) {
			throw new YamlParseException("unexpected content", lines.get(run.position).number);
		}
		return result;
	}

	private List<Line> toLines(String yaml) {
		List<Line> lines = new ArrayList<>();
		String[] rawLines = yaml.split("\r\n|\r|\n", -1);
		for (int i = 0; i < rawLines.length; i++) {
			String content = removeComment(rawLines[i]);
			int indent = countLeadingSpaces(content);
			if (indent < content.length() && content.charAt(indent) == '\t') {
				throw new YamlParseException("tabs are not allowed for indentation", i + 1);
			}
			String trimmed = content.trim();
			if (!trimmed.isEmpty() && !trimmed.equals("---") && !trimmed.equals("...")) {
				lines.add(new Line(i + 1, indent, trimmed));
			}
		}
		return lines;
	}

	private String removeComment(String line) {
		char quote = 0;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (quote == '\'') {
				if (c == '\'') {
					if ((i + 1 < line.length()) && (line.charAt(i + 1) == '\'')) {
						i++;
					} else {
						quote = 0;
					}
				}
			} else if (quote == '"') {
				if (c == '\\') {
					i++;
				} else if (c == '"') {
					quote = 0;
				}
			} else if (((c == '\'') || (c == '"')) && isTokenStart(line, i)) {
				quote = c;
			} else if ((c == '#') && ((i == 0) || Character.isWhitespace(line.charAt(i - 1)))) {
				return line.substring(0, i);
			}
		}
		return line;
	}

	private static boolean isTokenStart(String line, int index) {
		if (index == 0) {
			return true;
		}
		char previous = line.charAt(index - 1);
		return Character.isWhitespace(previous) || (previous == '[') || (previous == ',');
	}

	private static int countLeadingSpaces(String s) {
		int count = 0;
		while ((count < s.length()) && (s.charAt(count) == ' ')) {
			count++;
		}
		return count;
	}

	private static boolean isSequenceItem(String content) {
		return content.equals("-") || content.startsWith("- ");
	}

	private static int findMappingColon(String content) {
		if (content.startsWith("[") || content.startsWith("{")) {
			return -1;
		}
		int start = 0;
		if (content.startsWith("'") || content.startsWith("\"")) {
			start = findClosingQuote(content, content.charAt(0)) + 1;
			if (start == 0) {
				return -1;
			}
		}
		for (int i = start; i < content.length(); i++) {
			if ((content.charAt(i) == ':')
					&& ((i + 1 == content.length()) || Character.isWhitespace(content.charAt(i + 1)))) {
				return i;
			}
		}
		return -1;
	}

	private static int findClosingQuote(String s, char quote) {
		for (int i = 1; i < s.length(); i++) {
			char c = s.charAt(i);
			if ((quote == '"') && (c == '\\')) {
				i++;
			} else if (c == quote) {
				if ((quote == '\'') && (i + 1 < s.length()) && (s.charAt(i + 1) == '\'')) {
					i++;
				} else {
					return i;
				}
			}
		}
		return -1;
	}

	private static Object parseInlineValue(String text, int lineNumber) {
		if (text.startsWith("[")) {
			return parseFlowSequence(text, lineNumber);
		}
		if (UNSUPPORTED_VALUE_START_CHARACTERS.indexOf(text.charAt(0)) >= 0) {
			throw new YamlParseException("unsupported YAML construct: " + text, lineNumber);
		}
		return parseScalar(text, lineNumber);
	}

	private static List<Object> parseFlowSequence(String text, int lineNumber) {
		if (!text.endsWith("]")) {
			throw new YamlParseException("unterminated flow sequence: " + text, lineNumber);
		}
		List<Object> list = new ArrayList<>();
		String inner = text.substring(1, text.length() - 1).trim();
		if (inner.isEmpty()) {
			return list;
		}
		int itemStart = 0;
		int i = 0;
		while (i <= inner.length()) {
			if ((i < inner.length()) && ((inner.charAt(i) == '\'') || (inner.charAt(i) == '"'))
					&& inner.substring(itemStart, i).trim().isEmpty()) {
				int closing = findClosingQuote(inner.substring(i), inner.charAt(i));
				if (closing < 0) {
					throw new YamlParseException("unterminated quoted string: " + text, lineNumber);
				}
				i += closing + 1;
			} else if ((i == inner.length()) || (inner.charAt(i) == ',')) {
				String item = inner.substring(itemStart, i).trim();
				if (item.isEmpty() || item.startsWith("[") || item.startsWith("{")) {
					throw new YamlParseException("unsupported flow sequence item: " + text, lineNumber);
				}
				list.add(parseScalar(item, lineNumber));
				itemStart = i + 1;
				i++;
			} else {
				i++;
			}
		}
		return list;
	}

	private static String parseScalar(String text, int lineNumber) {
		char first = text.charAt(0);
		if ((first == '\'') || (first == '"')) {
			int closing = findClosingQuote(text, first);
			if (closing != text.length() - 1) {
				throw new YamlParseException("invalid quoted string: " + text, lineNumber);
			}
			String inner = text.substring(1, closing);
			return first == '\'' ? inner.replace("''", "'") : unescape(inner, lineNumber);
		}
		if (text.equals("~") || text.equalsIgnoreCase("null")) {
			return null;
		}
		return text;
	}

	private static String unescape(String s, int lineNumber) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != '\\') {
				result.append(c);
				continue;
			}
			i++;
			if (i == s.length()) {
				throw new YamlParseException("invalid escape sequence at end of string", lineNumber);
			}
			char escaped = s.charAt(i);
			switch (escaped) {
			case 'n':
				result.append('\n');
				break;
			case 't':
				result.append('\t');
				break;
			case 'r':
				result.append('\r');
				break;
			case '0':
				result.append('\0');
				break;
			case '\\':
			case '"':
			case '/':
				result.append(escaped);
				break;
			case 'u':
				if (i + 4 >= s.length()) {
					throw new YamlParseException("invalid unicode escape sequence", lineNumber);
				}
				try {
					result.append((char) Integer.parseInt(s.substring(i + 1, i + 5), 16));
				} catch (NumberFormatException e) {
					throw new YamlParseException("invalid unicode escape sequence", lineNumber);
				}
				i += 4;
				break;
			default:
				throw new YamlParseException("unsupported escape sequence: \\" + escaped, lineNumber);
			}
		}
		return result.toString();
	}

	private static class Line {

		private final int number;
		private final int indent;
		private final String content;

		private Line(int number, int indent, String content) {
			this.number = number;
			this.indent = indent;
			this.content = content;
		}

	}

	private static class ParserRun {

		private final List<Line> lines;
		private int position;

		private ParserRun(List<Line> lines) {
			this.lines = lines;
		}

		private Object parseBlock(int indent) {
			if (isSequenceItem(lines.get(position).content)) {
				return parseSequence(indent);
			}
			return parseMapping(indent);
		}

		private Map<String, Object> parseMapping(int indent) {
			Map<String, Object> map = new LinkedHashMap<>();
			while (position < lines.size()) {
				Line line = lines.get(position);
				if ((line.indent < indent) || ((line.indent == indent) && isSequenceItem(line.content))) {
					break;
				}
				if (line.indent > indent) {
					throw new YamlParseException("unexpected indentation", line.number);
				}
				int colon = findMappingColon(line.content);
				if (colon < 0) {
					throw new YamlParseException("mapping entry expected: " + line.content, line.number);
				}
				String key = parseScalar(line.content.substring(0, colon).trim(), line.number);
				if (map.containsKey(key)) {
					throw new YamlParseException("duplicate key: " + key, line.number);
				}
				String rest = line.content.substring(colon + 1).trim();
				position++;
				map.put(key, rest.isEmpty() ? parseNestedValueOfMappingEntry(indent) : parseInlineValue(rest, line.number));
			}
			return map;
		}

		private Object parseNestedValueOfMappingEntry(int indent) {
			if (position >= lines.size()) {
				return null;
			}
			Line next = lines.get(position);
			if ((next.indent > indent) || ((next.indent == indent) && isSequenceItem(next.content))) {
				return parseBlock(next.indent);
			}
			return null;
		}

		private List<Object> parseSequence(int indent) {
			List<Object> list = new ArrayList<>();
			while (position < lines.size()) {
				Line line = lines.get(position);
				if ((line.indent < indent) || !isSequenceItem(line.content)) {
					break;
				}
				if (line.indent > indent) {
					throw new YamlParseException("unexpected indentation", line.number);
				}
				String afterDash = line.content.substring(1);
				int itemIndent = indent + 1 + countLeadingSpaces(afterDash);
				String rest = afterDash.trim();
				if (rest.isEmpty()) {
					position++;
					list.add(
							(position < lines.size()) && (lines.get(position).indent > indent)
									? parseBlock(lines.get(position).indent)
									: null);
				} else if (isSequenceItem(rest) || (findMappingColon(rest) >= 0)) {
					lines.set(position, new Line(line.number, itemIndent, rest));
					list.add(parseBlock(itemIndent));
				} else {
					position++;
					list.add(parseInlineValue(rest, line.number));
				}
			}
			return list;
		}

	}

}
