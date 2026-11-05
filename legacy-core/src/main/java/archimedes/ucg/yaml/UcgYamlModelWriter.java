package archimedes.ucg.yaml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import archimedes.ucg.yaml.model.YamlArchimedesColor;
import archimedes.ucg.yaml.model.YamlArchimedesPoint;
import archimedes.ucg.yaml.model.YamlArchimedesRelation;
import archimedes.ucg.yaml.model.YamlArchimedesSection;
import archimedes.ucg.yaml.model.YamlArchimedesTable;
import archimedes.ucg.yaml.model.YamlArchimedesTablePosition;
import archimedes.ucg.yaml.model.YamlArchimedesView;
import archimedes.ucg.yaml.model.YamlAttribute;
import archimedes.ucg.yaml.model.YamlClassDefinition;
import archimedes.ucg.yaml.model.YamlEnumDefinition;
import archimedes.ucg.yaml.model.YamlModel;
import archimedes.ucg.yaml.model.YamlProperty;
import archimedes.ucg.yaml.model.YamlType;

public class UcgYamlModelWriter {

	private static final String INDENT_STEP = "  ";
	private static final String SPECIAL_START_CHARACTERS = "-?:,[]{}#&*!|>'\"%@`";

	public void write(YamlModel model, String fileName) throws IOException {
		Files.writeString(Path.of(fileName), toYamlString(model), StandardCharsets.UTF_8);
	}

	public String toYamlString(YamlModel model) {
		StringBuilder yaml = new StringBuilder();
		writeScalar(yaml, "", "title", model.getTitle());
		writeList(yaml, "", "properties", model.getProperties(), this::writeProperty);
		writeList(yaml, "", "classes", model.getClasses(), this::writeClassDefinition);
		writeList(yaml, "", "enums", model.getEnums(), this::writeEnumDefinition);
		return yaml.toString();
	}

	public void write(YamlArchimedesSection section, String fileName) throws IOException {
		Files.writeString(Path.of(fileName), toYamlString(section), StandardCharsets.UTF_8);
	}

	public String toYamlString(YamlArchimedesSection section) {
		StringBuilder yaml = new StringBuilder();
		writeScalar(yaml, "", "title", section.getTitle());
		writeList(yaml, "", "views", section.getViews(), this::writeArchimedesView);
		writeList(yaml, "", "tables", section.getTables(), this::writeArchimedesTable);
		writeList(yaml, "", "colors", section.getColors(), this::writeArchimedesColor);
		return yaml.toString();
	}

	private void writeArchimedesView(StringBuilder yaml, String indent, YamlArchimedesView view) {
		writeScalar(yaml, indent, "name", view.getName());
		writeScalar(yaml, indent, "description", view.getDescription());
		writeFlag(yaml, indent, "main-view", view.isMainView());
		writeFlag(yaml, indent, "show-referenced-columns", view.isShowReferencedColumns());
		writeFlag(yaml, indent, "hide-technical-fields", view.isHideTechnicalFields());
		writeFlag(yaml, indent, "hide-transient-fields", view.isHideTransientFields());
		writeList(yaml, indent, "tables", view.getTables(), this::writeArchimedesTablePosition);
		writeList(yaml, indent, "relations", view.getRelations(), this::writeArchimedesRelation);
	}

	private void writeArchimedesTablePosition(StringBuilder yaml, String indent, YamlArchimedesTablePosition position) {
		writeScalar(yaml, indent, "name", position.getName());
		writeNumber(yaml, indent, "x", position.getX());
		writeNumber(yaml, indent, "y", position.getY());
	}

	private void writeArchimedesRelation(StringBuilder yaml, String indent, YamlArchimedesRelation relation) {
		writeScalar(yaml, indent, "table", relation.getTable());
		writeScalar(yaml, indent, "column", relation.getColumn());
		writeScalar(yaml, indent, "direction-0", relation.getDirection0());
		writeNumber(yaml, indent, "offset-0", relation.getOffset0());
		writeScalar(yaml, indent, "direction-1", relation.getDirection1());
		writeNumber(yaml, indent, "offset-1", relation.getOffset1());
		writeList(yaml, indent, "points", relation.getPoints(), this::writeArchimedesPoint);
	}

	private void writeArchimedesPoint(StringBuilder yaml, String indent, YamlArchimedesPoint point) {
		writeNumber(yaml, indent, "x", point.getX());
		writeNumber(yaml, indent, "y", point.getY());
	}

	private void writeArchimedesTable(StringBuilder yaml, String indent, YamlArchimedesTable table) {
		writeScalar(yaml, indent, "name", table.getName());
		writeScalar(yaml, indent, "background-color", table.getBackgroundColor());
		writeScalar(yaml, indent, "font-color", table.getFontColor());
	}

	private void writeArchimedesColor(StringBuilder yaml, String indent, YamlArchimedesColor color) {
		writeScalar(yaml, indent, "name", color.getName());
		writeNumber(yaml, indent, "red", color.getRed());
		writeNumber(yaml, indent, "green", color.getGreen());
		writeNumber(yaml, indent, "blue", color.getBlue());
	}

	private void writeNumber(StringBuilder yaml, String indent, String key, int value) {
		yaml.append(indent).append(key).append(": ").append(value).append("\n");
	}

	private void writeClassDefinition(StringBuilder yaml, String indent, YamlClassDefinition classDefinition) {
		writeScalar(yaml, indent, "name", classDefinition.getName());
		writeList(yaml, indent, "properties", classDefinition.getProperties(), this::writeProperty);
		writeList(yaml, indent, "attributes", classDefinition.getAttributes(), this::writeAttribute);
	}

	private void writeAttribute(StringBuilder yaml, String indent, YamlAttribute attribute) {
		writeScalar(yaml, indent, "name", attribute.getName());
		writeScalar(yaml, indent, "default-value", attribute.getDefaultValue());
		writeFlag(yaml, indent, "enum", attribute.isEnumType());
		writeFlag(yaml, indent, "reference", attribute.isReference());
		writeList(yaml, indent, "properties", attribute.getProperties(), this::writeProperty);
		writeType(yaml, indent, attribute.getType());
	}

	private void writeType(StringBuilder yaml, String indent, YamlType type) {
		if (type == null) {
			return;
		}
		yaml.append(indent).append("type:\n");
		String typeIndent = indent + INDENT_STEP;
		writeScalar(yaml, typeIndent, "name", type.getName());
		writeList(yaml, typeIndent, "properties", type.getProperties(), this::writeProperty);
	}

	private void writeEnumDefinition(StringBuilder yaml, String indent, YamlEnumDefinition enumDefinition) {
		writeScalar(yaml, indent, "name", enumDefinition.getName());
		writeList(
				yaml,
				indent,
				"identifiers",
				enumDefinition.getIdentifiers(),
				(builder, itemIndent, identifier) -> builder.append(itemIndent).append(formatScalar(identifier)).append("\n"));
	}

	private void writeProperty(StringBuilder yaml, String indent, YamlProperty property) {
		writeScalar(yaml, indent, "name", property.getName());
		writeScalar(yaml, indent, "value", property.getValue());
	}

	private void writeScalar(StringBuilder yaml, String indent, String key, String value) {
		if (value != null) {
			yaml.append(indent).append(key).append(": ").append(formatScalar(value)).append("\n");
		}
	}

	private void writeFlag(StringBuilder yaml, String indent, String key, boolean value) {
		if (value) {
			yaml.append(indent).append(key).append(": true\n");
		}
	}

	private <T> void writeList(StringBuilder yaml, String indent, String key, List<T> items, ItemWriter<T> itemWriter) {
		if (items == null) {
			return;
		}
		if (items.isEmpty()) {
			yaml.append(indent).append(key).append(": []\n");
			return;
		}
		yaml.append(indent).append(key).append(":\n");
		String dashIndent = indent + INDENT_STEP;
		String itemIndent = dashIndent + INDENT_STEP;
		for (T item : items) {
			StringBuilder itemYaml = new StringBuilder();
			if (item != null) {
				itemWriter.write(itemYaml, itemIndent, item);
			}
			if (itemYaml.length() == 0) {
				yaml.append(dashIndent).append("-\n");
			} else {
				yaml.append(dashIndent).append("- ").append(itemYaml, itemIndent.length(), itemYaml.length());
			}
		}
	}

	private String formatScalar(String value) {
		if (needsDoubleQuotes(value)) {
			return toDoubleQuoted(value);
		}
		if (needsQuotes(value)) {
			return "'" + value.replace("'", "''") + "'";
		}
		return value;
	}

	private boolean needsDoubleQuotes(String value) {
		for (int i = 0; i < value.length(); i++) {
			if (Character.isISOControl(value.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	private boolean needsQuotes(String value) {
		return value.isEmpty()
				|| !value.equals(value.trim())
				|| (SPECIAL_START_CHARACTERS.indexOf(value.charAt(0)) >= 0)
				|| value.contains(": ")
				|| value.contains(" #")
				|| value.endsWith(":")
				|| value.equals("~")
				|| value.equalsIgnoreCase("null");
	}

	private String toDoubleQuoted(String value) {
		StringBuilder result = new StringBuilder("\"");
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c == '\n') {
				result.append("\\n");
			} else if (c == '\t') {
				result.append("\\t");
			} else if (c == '\r') {
				result.append("\\r");
			} else if ((c == '"') || (c == '\\')) {
				result.append('\\').append(c);
			} else if (Character.isISOControl(c)) {
				result.append(String.format("\\u%04x", (int) c));
			} else {
				result.append(c);
			}
		}
		return result.append('"').toString();
	}

	@FunctionalInterface
	private interface ItemWriter<T> {

		void write(StringBuilder yaml, String indent, T item);

	}

}
