package archimedes.ucg.yaml;

import static archimedes.ucg.yaml.UcgYamlNames.COLUMN_OPTION_ENUM;
import static archimedes.ucg.yaml.UcgYamlNames.COLUMN_OPTION_JAVA_TYPE;
import static archimedes.ucg.yaml.UcgYamlNames.DOMAIN_OPTION_ENUM;
import static archimedes.ucg.yaml.UcgYamlNames.PROPERTY_DECIMAL_PLACES;
import static archimedes.ucg.yaml.UcgYamlNames.PROPERTY_LENGTH;
import static archimedes.ucg.yaml.UcgYamlNames.PROPERTY_NULLABLE;
import static archimedes.ucg.yaml.UcgYamlNames.PROPERTY_PRIMARY_KEY;
import static archimedes.ucg.yaml.UcgYamlNames.PROPERTY_UNIQUE;
import static archimedes.ucg.yaml.UcgYamlNames.RESERVED_ATTRIBUTE_PROPERTY_NAMES;

import java.awt.Color;
import java.sql.Types;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import archimedes.codegenerators.TypeGenerator;
import archimedes.legacy.Archimedes;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.MainView;
import archimedes.legacy.scheme.Panel;
import archimedes.legacy.scheme.Relation;
import archimedes.legacy.scheme.Tabelle;
import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.legacy.scheme.View;
import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.GUIObjectModel;
import archimedes.model.gui.GUIRelationModel;
import archimedes.model.gui.GUIViewModel;
import archimedes.scheme.Option;
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
import corent.base.Direction;
import corent.base.StrUtil;
import corent.gui.ExtendedColor;

public class YamlModelToDiagramConverter {

	static final int DEFAULT_STRING_LENGTH = 255;
	static final String ID_ATTRIBUTE_NAME = "id";
	static final String MAIN_VIEW_NAME = "Main";

	private static final int TABLES_PER_ROW = 4;
	private static final int TABLE_X_DISTANCE = 400;
	private static final int TABLE_Y_DISTANCE = 300;
	private static final int TABLE_START_POSITION = 50;
	private static final Map<String, Integer> SQL_TYPES = createSqlTypes();

	private static Map<String, Integer> createSqlTypes() {
		Map<String, Integer> sqlTypes = new LinkedHashMap<>();
		sqlTypes.put("boolean", Types.BOOLEAN);
		sqlTypes.put("Boolean", Types.BOOLEAN);
		sqlTypes.put("byte", Types.TINYINT);
		sqlTypes.put("Byte", Types.TINYINT);
		sqlTypes.put("byte[]", Types.BLOB);
		sqlTypes.put("char", Types.CHAR);
		sqlTypes.put("Character", Types.CHAR);
		sqlTypes.put("double", Types.DOUBLE);
		sqlTypes.put("Double", Types.DOUBLE);
		sqlTypes.put("float", Types.FLOAT);
		sqlTypes.put("Float", Types.FLOAT);
		sqlTypes.put("int", Types.INTEGER);
		sqlTypes.put("Integer", Types.INTEGER);
		sqlTypes.put("LocalDate", Types.DATE);
		sqlTypes.put("LocalDateTime", Types.TIMESTAMP);
		sqlTypes.put("LocalTime", Types.TIME);
		sqlTypes.put("long", Types.BIGINT);
		sqlTypes.put("Long", Types.BIGINT);
		sqlTypes.put("short", Types.SMALLINT);
		sqlTypes.put("Short", Types.SMALLINT);
		sqlTypes.put("String", Types.VARCHAR);
		return sqlTypes;
	}

	public DiagrammModel convert(YamlModel yamlModel) {
		return convert(yamlModel, null);
	}

	public DiagrammModel convert(YamlModel yamlModel, YamlArchimedesSection section) {
		Diagramm diagram = new Diagramm();
		ViewModel mainView = createViews(section, diagram);
		if (yamlModel.getTitle() != null) {
			diagram.setName(yamlModel.getTitle());
		} else if ((section != null) && (section.getTitle() != null)) {
			diagram.setName(section.getTitle());
		}
		if (yamlModel.getProperties() != null) {
			yamlModel.getProperties().forEach(property -> diagram.addOption(toOption(property)));
		}
		if (yamlModel.getEnums() != null) {
			yamlModel.getEnums().forEach(enumDefinition -> createEnumDomain(enumDefinition, diagram));
		}
		if (yamlModel.getClasses() != null) {
			Map<ColumnModel, String> referencedClassNames = new LinkedHashMap<>();
			int index = 0;
			for (YamlClassDefinition classDefinition : yamlModel.getClasses()) {
				createTable(classDefinition, index++, diagram, mainView, referencedClassNames);
			}
			Set<ColumnModel> resolving = new HashSet<>();
			for (ColumnModel column : List.copyOf(referencedClassNames.keySet())) {
				resolveReference(column, referencedClassNames, resolving, diagram, mainView);
			}
		}
		if (section != null) {
			applyColors(section, diagram);
			applyViewLayouts(section, diagram);
		}
		return diagram;
	}

	private void createEnumDomain(YamlEnumDefinition enumDefinition, DiagrammModel diagram) {
		ensureNotNull(enumDefinition, "enum definitions cannot be empty.");
		ensureNotNull(enumDefinition.getName(), "enum definitions must have a name.");
		if (diagram.getDomainByName(enumDefinition.getName()) != null) {
			throw new YamlParseException("duplicate enum name: " + enumDefinition.getName());
		}
		DomainModel domain = new Domain(enumDefinition.getName(), Types.VARCHAR, DEFAULT_STRING_LENGTH, -1);
		domain
				.addOption(
						new Option(
								DOMAIN_OPTION_ENUM,
								enumDefinition.getIdentifiers() == null
										? ""
										: String.join(",", enumDefinition.getIdentifiers())));
		diagram.addDomain(domain);
	}

	private ViewModel createViews(YamlArchimedesSection section, Diagramm diagram) {
		ViewModel mainView = null;
		if ((section != null) && (section.getViews() != null)) {
			for (YamlArchimedesView viewDefinition : section.getViews()) {
				ensureNotNull(viewDefinition, "views cannot be empty.");
				ensureNotNull(viewDefinition.getName(), "views must have a name.");
				if (diagram.getViewByName(viewDefinition.getName()) != null) {
					throw new YamlParseException("duplicate view name: " + viewDefinition.getName());
				}
				ViewModel view;
				if (viewDefinition.isMainView() && (mainView == null)) {
					view =
							new MainView(
									viewDefinition.getName(),
									viewDefinition.getDescription() == null ? "" : viewDefinition.getDescription(),
									viewDefinition.isShowReferencedColumns());
					mainView = view;
				} else {
					view = new View();
					view.setName(viewDefinition.getName());
					view.setComment(viewDefinition.getDescription() == null ? "" : viewDefinition.getDescription());
					view.setShowReferencedColumns(viewDefinition.isShowReferencedColumns());
				}
				view.setHideTechnicalFields(viewDefinition.isHideTechnicalFields());
				view.setHideTransientFields(viewDefinition.isHideTransientFields());
				diagram.addView((GUIViewModel) view);
			}
		}
		if (mainView == null) {
			mainView = new MainView(MAIN_VIEW_NAME, "Diese Sicht beinhaltet alle Tabellen des Schemas", true);
			diagram.getViews().add(0, (GUIViewModel) mainView);
		}
		return mainView;
	}

	private void applyColors(YamlArchimedesSection section, DiagrammModel diagram) {
		Map<String, Color> colors = new LinkedHashMap<>();
		if (section.getColors() != null) {
			for (YamlArchimedesColor colorDefinition : section.getColors()) {
				ensureNotNull(colorDefinition, "colors cannot be empty.");
				ensureNotNull(colorDefinition.getName(), "colors must have a name.");
				ExtendedColor color =
						new ExtendedColor(
								colorDefinition.getName(),
								colorDefinition.getRed(),
								colorDefinition.getGreen(),
								colorDefinition.getBlue());
				colors.put(colorDefinition.getName(), color);
				if (Archimedes.PALETTE != null) {
					Archimedes.PALETTE.set(colorDefinition.getName(), color);
				}
			}
		}
		if (section.getTables() != null) {
			for (YamlArchimedesTable tableDefinition : section.getTables()) {
				ensureNotNull(tableDefinition, "tables of the archimedes section cannot be empty.");
				TableModel table = getTable(diagram, tableDefinition.getName());
				Color backgroundColor = findColor(tableDefinition.getBackgroundColor(), colors);
				if (backgroundColor != null) {
					table.setBackgroundColor(backgroundColor);
				}
				Color fontColor = findColor(tableDefinition.getFontColor(), colors);
				if (fontColor != null) {
					table.setFontColor(fontColor);
				}
			}
		}
	}

	private Color findColor(String name, Map<String, Color> colors) {
		if (name == null) {
			return null;
		}
		if (colors.containsKey(name)) {
			return colors.get(name);
		}
		return Archimedes.PALETTE != null ? Archimedes.PALETTE.get(name, null) : null;
	}

	private void applyViewLayouts(YamlArchimedesSection section, DiagrammModel diagram) {
		if (section.getViews() == null) {
			return;
		}
		for (YamlArchimedesView viewDefinition : section.getViews()) {
			GUIViewModel view = (GUIViewModel) diagram.getViewByName(viewDefinition.getName());
			if (viewDefinition.getTables() != null) {
				for (YamlArchimedesTablePosition position : viewDefinition.getTables()) {
					ensureNotNull(position, "table positions of view " + viewDefinition.getName() + " cannot be empty.");
					TableModel table = getTable(diagram, position.getName());
					((GUIObjectModel) table).setXY(view, position.getX(), position.getY());
					((ViewModel) view).addTable(table);
				}
			}
			if (viewDefinition.getRelations() != null) {
				for (YamlArchimedesRelation relationDefinition : viewDefinition.getRelations()) {
					ensureNotNull(relationDefinition, "relations of view " + viewDefinition.getName() + " cannot be empty.");
					applyRelationLayout(relationDefinition, view, diagram);
				}
			}
		}
	}

	private void applyRelationLayout(YamlArchimedesRelation relationDefinition, GUIViewModel view,
			DiagrammModel diagram) {
		TableModel table = getTable(diagram, relationDefinition.getTable());
		ColumnModel column = table.getColumnByName(relationDefinition.getColumn());
		String qualifiedName = relationDefinition.getTable() + "." + relationDefinition.getColumn();
		if ((column == null) || !(column.getRelation() instanceof GUIRelationModel)) {
			throw new YamlParseException("relation layout for unknown reference attribute: " + qualifiedName);
		}
		GUIRelationModel relation = (GUIRelationModel) column.getRelation();
		relation.setDirection(view, 0, toDirection(relationDefinition.getDirection0(), qualifiedName));
		relation.setOffset(view, 0, relationDefinition.getOffset0());
		relation.setDirection(view, 1, toDirection(relationDefinition.getDirection1(), qualifiedName));
		relation.setOffset(view, 1, relationDefinition.getOffset1());
		if (relationDefinition.getPoints() != null) {
			for (YamlArchimedesPoint point : relationDefinition.getPoints()) {
				ensureNotNull(point, "points of relation " + qualifiedName + " cannot be empty.");
				relation.addPoint(view, point.getX(), point.getY());
			}
		}
	}

	private Direction toDirection(String name, String qualifiedName) {
		for (Direction direction : List.of(Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP)) {
			if (direction.toString().equalsIgnoreCase(name)) {
				return direction;
			}
		}
		throw new YamlParseException("invalid direction for relation " + qualifiedName + ": " + name);
	}

	private TableModel getTable(DiagrammModel diagram, String name) {
		TableModel table = name != null ? diagram.getTableByName(name) : null;
		if (table == null) {
			throw new YamlParseException("unknown table in archimedes section: " + name);
		}
		return table;
	}

	private void createTable(YamlClassDefinition classDefinition, int index, DiagrammModel diagram, ViewModel mainView,
			Map<ColumnModel, String> referencedClassNames) {
		ensureNotNull(classDefinition, "class definitions cannot be empty.");
		ensureNotNull(classDefinition.getName(), "class definitions must have a name.");
		if (diagram.getTableByName(classDefinition.getName()) != null) {
			throw new YamlParseException("duplicate class name: " + classDefinition.getName());
		}
		Tabelle table =
				new Tabelle(
						mainView,
						TABLE_START_POSITION + (index % TABLES_PER_ROW) * TABLE_X_DISTANCE,
						TABLE_START_POSITION + (index / TABLES_PER_ROW) * TABLE_Y_DISTANCE,
						diagram);
		table.setCodeFolder("");
		table.setDraft(false);
		table.setName(classDefinition.getName());
		if (Archimedes.PALETTE != null) {
			table.setFontColor(Archimedes.PALETTE.get("schwarz"));
			table.setBackgroundColor(Archimedes.PALETTE.get(StrUtil.FromHTML("wei&szlig;")));
		}
		Panel panel = new Panel();
		panel.setPanelNumber(0);
		table.addPanel(panel);
		if (classDefinition.getProperties() != null) {
			classDefinition.getProperties().forEach(property -> table.addOption(toOption(property)));
		}
		if (classDefinition.getAttributes() != null) {
			for (YamlAttribute attribute : classDefinition.getAttributes()) {
				ColumnModel column = createColumn(classDefinition.getName(), attribute, diagram);
				column.setPanel(panel);
				column.setTable(table);
				table.addColumn(column);
				if (attribute.isReference()) {
					referencedClassNames.put(column, attribute.getType().getName());
				}
			}
		}
		diagram.addTable(table);
		mainView.addTable(table);
	}

	private ColumnModel createColumn(String className, YamlAttribute attribute, DiagrammModel diagram) {
		ensureNotNull(attribute, "attributes of class " + className + " cannot be empty.");
		ensureNotNull(attribute.getName(), "attributes of class " + className + " must have a name.");
		String qualifiedName = className + "." + attribute.getName();
		ensureNotNull(attribute.getType(), "attribute " + qualifiedName + " must have a type.");
		ensureNotNull(attribute.getType().getName(), "type of attribute " + qualifiedName + " must have a name.");
		DomainModel domain = attribute.isReference() ? null : getDomain(attribute, diagram, qualifiedName);
		ColumnModel column =
				new Tabellenspalte(attribute.getName(), domain, isPropertySetTo(attribute, PROPERTY_PRIMARY_KEY, "true"));
		column
				.setNotNull(
						isPropertySetTo(attribute, PROPERTY_NULLABLE, "false")
								|| TypeGenerator.INSTANCE.isSimpleType(attribute.getType().getName()));
		column.setUnique(isPropertySetTo(attribute, PROPERTY_UNIQUE, "true"));
		if (attribute.getDefaultValue() != null) {
			column.setIndividualDefaultValue(attribute.getDefaultValue());
		}
		if (attribute.isEnumType()) {
			column.addOption(new Option(COLUMN_OPTION_ENUM, ""));
		}
		if (!attribute.isReference() && (attribute.isEnumType() || !SQL_TYPES.containsKey(attribute.getType().getName()))) {
			column.addOption(new Option(COLUMN_OPTION_JAVA_TYPE, attribute.getType().getName()));
		}
		if (attribute.getProperties() != null) {
			attribute
					.getProperties()
					.stream()
					.filter(property -> !RESERVED_ATTRIBUTE_PROPERTY_NAMES.contains(property.getName()))
					.forEach(property -> column.addOption(toOption(property)));
		}
		return column;
	}

	private DomainModel getDomain(YamlAttribute attribute, DiagrammModel diagram, String qualifiedName) {
		String typeName = attribute.getType().getName();
		int length = getIntegerProperty(attribute.getType(), PROPERTY_LENGTH, qualifiedName);
		int decimalPlaces = getIntegerProperty(attribute.getType(), PROPERTY_DECIMAL_PLACES, qualifiedName);
		Integer sqlType = attribute.isEnumType() ? null : SQL_TYPES.get(typeName);
		if ((sqlType == null) || (sqlType == Types.VARCHAR)) {
			return length > 0
					? getOrCreateDomain(diagram, toDomainName(typeName, String.valueOf(length)), Types.VARCHAR, length, -1)
					: getOrCreateDomain(diagram, toDomainName(typeName, ""), Types.VARCHAR, DEFAULT_STRING_LENGTH, -1);
		}
		if (sqlType == Types.CHAR) {
			return getOrCreateDomain(diagram, toDomainName(typeName, ""), Types.CHAR, 1, -1);
		}
		if ((sqlType == Types.DOUBLE) && (length > 0)) {
			int numericDecimalPlaces = Math.max(decimalPlaces, 0);
			return getOrCreateDomain(
					diagram,
					toDomainName(typeName, length + "_" + numericDecimalPlaces),
					Types.NUMERIC,
					length,
					numericDecimalPlaces);
		}
		if ((sqlType == Types.BLOB) && (length > 0)) {
			return getOrCreateDomain(diagram, toDomainName(typeName, String.valueOf(length)), Types.VARBINARY, length, -1);
		}
		return getOrCreateDomain(diagram, toDomainName(typeName, ""), sqlType, -1, -1);
	}

	private String toDomainName(String javaTypeName, String suffix) {
		String name = javaTypeName.replace("[]", "Array").replaceAll("[^A-Za-z0-9_]", "");
		return name.substring(0, 1).toUpperCase() + name.substring(1) + suffix;
	}

	private DomainModel getOrCreateDomain(DiagrammModel diagram, String name, int sqlType, int length,
			int decimalPlaces) {
		DomainModel domain = diagram.getDomainByName(name);
		if (domain == null) {
			domain = new Domain(name, sqlType, length, decimalPlaces);
			diagram.addDomain(domain);
		}
		return domain;
	}

	private int getIntegerProperty(YamlType type, String propertyName, String qualifiedName) {
		String value = getPropertyValue(type.getProperties(), propertyName);
		if (value == null) {
			return -1;
		}
		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException e) {
			throw new YamlParseException(
					"property '" + propertyName + "' of the type of attribute " + qualifiedName + " is not a number: "
							+ value);
		}
	}

	private void resolveReference(ColumnModel column, Map<ColumnModel, String> referencedClassNames,
			Set<ColumnModel> resolving, DiagrammModel diagram, ViewModel mainView) {
		if (column.getDomain() != null) {
			return;
		}
		String qualifiedName = column.getTable().getName() + "." + column.getName();
		if (!resolving.add(column)) {
			throw new YamlParseException("cyclic primary key reference at attribute " + qualifiedName);
		}
		String referencedClassName = referencedClassNames.get(column);
		TableModel referencedTable = diagram.getTableByName(referencedClassName);
		if (referencedTable == null) {
			throw new YamlParseException(
					"class '" + referencedClassName + "' referenced by attribute " + qualifiedName + " not found.");
		}
		ColumnModel referencedColumn = getReferencedColumn(referencedTable, qualifiedName);
		resolveReference(referencedColumn, referencedClassNames, resolving, diagram, mainView);
		column.setDomain(referencedColumn.getDomain());
		column.setRelation(new Relation(mainView, column, Direction.UP, 0, referencedColumn, Direction.DOWN, 0));
	}

	private ColumnModel getReferencedColumn(TableModel referencedTable, String qualifiedName) {
		ColumnModel[] primaryKeyColumns = referencedTable.getPrimaryKeyColumns();
		if (primaryKeyColumns.length == 1) {
			return primaryKeyColumns[0];
		}
		if (primaryKeyColumns.length == 0) {
			for (ColumnModel column : referencedTable.getColumns()) {
				if (ID_ATTRIBUTE_NAME.equalsIgnoreCase(column.getName())) {
					return column;
				}
			}
		}
		throw new YamlParseException(
				"class '" + referencedTable.getName() + "' referenced by attribute " + qualifiedName
						+ " must have exactly one primary key attribute (property '" + PROPERTY_PRIMARY_KEY
						+ "') or an attribute named '" + ID_ATTRIBUTE_NAME + "'.");
	}

	private boolean isPropertySetTo(YamlAttribute attribute, String propertyName, String value) {
		return value.equalsIgnoreCase(getPropertyValue(attribute.getProperties(), propertyName));
	}

	private String getPropertyValue(List<YamlProperty> properties, String propertyName) {
		if (properties == null) {
			return null;
		}
		return properties
				.stream()
				.filter(property -> (property != null) && propertyName.equals(property.getName()))
				.map(YamlProperty::getValue)
				.findFirst()
				.orElse(null);
	}

	private Option toOption(YamlProperty property) {
		ensureNotNull(property, "properties cannot be empty.");
		ensureNotNull(property.getName(), "properties must have a name.");
		return new Option(property.getName(), property.getValue() == null ? "" : property.getValue());
	}

	private void ensureNotNull(Object object, String message) {
		if (object == null) {
			throw new YamlParseException(message);
		}
	}

}
