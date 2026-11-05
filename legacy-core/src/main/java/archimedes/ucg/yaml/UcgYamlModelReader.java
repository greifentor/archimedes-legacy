package archimedes.ucg.yaml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

public class UcgYamlModelReader {

	private final SimpleYamlParser parser;

	public UcgYamlModelReader() {
		this(new SimpleYamlParser());
	}

	public UcgYamlModelReader(SimpleYamlParser parser) {
		this.parser = parser;
	}

	public YamlModel read(String fileName) throws IOException {
		return readFromString(Files.readString(Path.of(fileName), StandardCharsets.UTF_8));
	}

	public YamlModel readFromString(String yaml) {
		Object root = parser.parse(yaml);
		return root == null ? new YamlModel() : toModel(root);
	}

	private YamlModel toModel(Object node) {
		Map<String, Object> map = asMap(node, "model");
		YamlModel model = new YamlModel();
		model.setTitle(getString(map, "title"));
		model.setClasses(getList(map, "classes", this::toClassDefinition));
		model.setEnums(getList(map, "enums", this::toEnumDefinition));
		model.setProperties(getList(map, "properties", this::toProperty));
		return model;
	}

	public YamlArchimedesSection readArchimedesSection(String fileName) throws IOException {
		return readArchimedesSectionFromString(Files.readString(Path.of(fileName), StandardCharsets.UTF_8));
	}

	public YamlArchimedesSection readArchimedesSectionFromString(String yaml) {
		Object root = parser.parse(yaml);
		return root == null ? new YamlArchimedesSection() : toArchimedesSection(root);
	}

	private YamlArchimedesSection toArchimedesSection(Object node) {
		Map<String, Object> map = asMap(node, "archimedes file");
		YamlArchimedesSection section = new YamlArchimedesSection();
		section.setTitle(getString(map, "title"));
		section.setViews(getList(map, "views", this::toArchimedesView));
		section.setTables(getList(map, "tables", this::toArchimedesTable));
		section.setColors(getList(map, "colors", this::toArchimedesColor));
		return section;
	}

	private YamlArchimedesView toArchimedesView(Object node) {
		Map<String, Object> map = asMap(node, "view");
		YamlArchimedesView view = new YamlArchimedesView();
		view.setName(getString(map, "name"));
		view.setDescription(getString(map, "description"));
		view.setMainView(getBoolean(map, "main-view"));
		view.setShowReferencedColumns(getBoolean(map, "show-referenced-columns"));
		view.setHideTechnicalFields(getBoolean(map, "hide-technical-fields"));
		view.setHideTransientFields(getBoolean(map, "hide-transient-fields"));
		view.setTables(getList(map, "tables", this::toArchimedesTablePosition));
		view.setRelations(getList(map, "relations", this::toArchimedesRelation));
		return view;
	}

	private YamlArchimedesTablePosition toArchimedesTablePosition(Object node) {
		Map<String, Object> map = asMap(node, "table position");
		return new YamlArchimedesTablePosition(getString(map, "name"), getInt(map, "x"), getInt(map, "y"));
	}

	private YamlArchimedesRelation toArchimedesRelation(Object node) {
		Map<String, Object> map = asMap(node, "relation");
		YamlArchimedesRelation relation = new YamlArchimedesRelation();
		relation.setTable(getString(map, "table"));
		relation.setColumn(getString(map, "column"));
		relation.setDirection0(getString(map, "direction-0"));
		relation.setOffset0(getInt(map, "offset-0"));
		relation.setDirection1(getString(map, "direction-1"));
		relation.setOffset1(getInt(map, "offset-1"));
		relation.setPoints(getList(map, "points", this::toArchimedesPoint));
		return relation;
	}

	private YamlArchimedesPoint toArchimedesPoint(Object node) {
		Map<String, Object> map = asMap(node, "point");
		return new YamlArchimedesPoint(getInt(map, "x"), getInt(map, "y"));
	}

	private YamlArchimedesTable toArchimedesTable(Object node) {
		Map<String, Object> map = asMap(node, "table");
		return new YamlArchimedesTable(
				getString(map, "name"),
				getString(map, "background-color"),
				getString(map, "font-color"));
	}

	private YamlArchimedesColor toArchimedesColor(Object node) {
		Map<String, Object> map = asMap(node, "color");
		return new YamlArchimedesColor(
				getString(map, "name"),
				getInt(map, "red"),
				getInt(map, "green"),
				getInt(map, "blue"));
	}

	private int getInt(Map<String, Object> map, String key) {
		String value = getString(map, key);
		if (value == null) {
			return 0;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new YamlParseException("integer value expected for " + key + " but found: " + value);
		}
	}

	private YamlClassDefinition toClassDefinition(Object node) {
		if (node == null) {
			return null;
		}
		Map<String, Object> map = asMap(node, "class definition");
		YamlClassDefinition classDefinition = new YamlClassDefinition();
		classDefinition.setName(getString(map, "name"));
		classDefinition.setAttributes(getList(map, "attributes", this::toAttribute));
		classDefinition.setProperties(getList(map, "properties", this::toProperty));
		return classDefinition;
	}

	private YamlAttribute toAttribute(Object node) {
		if (node == null) {
			return null;
		}
		Map<String, Object> map = asMap(node, "attribute");
		YamlAttribute attribute = new YamlAttribute();
		attribute.setName(getString(map, "name"));
		attribute.setType(toType(map.get("type")));
		attribute.setDefaultValue(getString(map, "default-value"));
		attribute.setEnumType(getBoolean(map, "enum"));
		attribute.setProperties(getList(map, "properties", this::toProperty));
		attribute.setReference(getBoolean(map, "reference"));
		return attribute;
	}

	private YamlType toType(Object node) {
		if (node == null) {
			return null;
		}
		Map<String, Object> map = asMap(node, "type");
		YamlType type = new YamlType();
		type.setName(getString(map, "name"));
		type.setProperties(getList(map, "properties", this::toProperty));
		return type;
	}

	private YamlEnumDefinition toEnumDefinition(Object node) {
		if (node == null) {
			return null;
		}
		Map<String, Object> map = asMap(node, "enum definition");
		YamlEnumDefinition enumDefinition = new YamlEnumDefinition();
		enumDefinition.setName(getString(map, "name"));
		enumDefinition.setIdentifiers(getList(map, "identifiers", identifier -> asString(identifier, "identifiers")));
		return enumDefinition;
	}

	private YamlProperty toProperty(Object node) {
		if (node == null) {
			return null;
		}
		Map<String, Object> map = asMap(node, "property");
		return new YamlProperty(getString(map, "name"), getString(map, "value"));
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> asMap(Object node, String context) {
		if (!(node instanceof Map)) {
			throw new YamlParseException("mapping expected for " + context + " but found: " + node);
		}
		return (Map<String, Object>) node;
	}

	private String getString(Map<String, Object> map, String key) {
		return asString(map.get(key), key);
	}

	private String asString(Object node, String context) {
		if ((node != null) && !(node instanceof String)) {
			throw new YamlParseException("scalar value expected for " + context + " but found: " + node);
		}
		return (String) node;
	}

	private boolean getBoolean(Map<String, Object> map, String key) {
		String value = getString(map, key);
		if ((value == null) || value.equalsIgnoreCase("false")) {
			return false;
		}
		if (value.equalsIgnoreCase("true")) {
			return true;
		}
		throw new YamlParseException("boolean value expected for " + key + " but found: " + value);
	}

	private <T> List<T> getList(Map<String, Object> map, String key, Function<Object, T> itemMapper) {
		Object node = map.get(key);
		if (node == null) {
			return null;
		}
		if (!(node instanceof List)) {
			throw new YamlParseException("sequence expected for " + key + " but found: " + node);
		}
		List<T> result = new ArrayList<>();
		for (Object item : (List<?>) node) {
			result.add(itemMapper.apply(item));
		}
		return result;
	}

}
