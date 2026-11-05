package archimedes.ucg.yaml;

import static archimedes.ucg.yaml.UcgYamlNames.COLUMN_OPTION_ENUM;
import static archimedes.ucg.yaml.UcgYamlNames.COLUMN_OPTION_JAVA_TYPE;
import static archimedes.ucg.yaml.UcgYamlNames.DOMAIN_OPTION_ENUM;
import static archimedes.ucg.yaml.UcgYamlNames.PROPERTY_DECIMAL_PLACES;
import static archimedes.ucg.yaml.UcgYamlNames.PROPERTY_IMPORT;
import static archimedes.ucg.yaml.UcgYamlNames.PROPERTY_LENGTH;
import static archimedes.ucg.yaml.UcgYamlNames.PROPERTY_NULLABLE;
import static archimedes.ucg.yaml.UcgYamlNames.PROPERTY_PRIMARY_KEY;
import static archimedes.ucg.yaml.UcgYamlNames.PROPERTY_UNIQUE;
import static archimedes.ucg.yaml.UcgYamlNames.RESERVED_COLUMN_OPTION_NAMES;

import java.awt.Color;
import java.awt.Point;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.GUIObjectModel;
import archimedes.model.gui.GUIRelationModel;
import archimedes.model.gui.GUIViewModel;
import archimedes.ucg.yaml.model.YamlArchimedesColor;
import archimedes.ucg.yaml.model.YamlArchimedesPoint;
import archimedes.ucg.yaml.model.YamlArchimedesRelation;
import archimedes.ucg.yaml.model.YamlArchimedesSection;
import archimedes.ucg.yaml.model.YamlArchimedesTable;
import archimedes.ucg.yaml.model.YamlArchimedesTablePosition;
import archimedes.ucg.yaml.model.YamlArchimedesView;
import archimedes.ucg.yaml.model.YamlAttribute;
import corent.gui.ExtendedColor;
import archimedes.ucg.yaml.model.YamlClassDefinition;
import archimedes.ucg.yaml.model.YamlEnumDefinition;
import archimedes.ucg.yaml.model.YamlModel;
import archimedes.ucg.yaml.model.YamlProperty;
import archimedes.ucg.yaml.model.YamlType;

public class DataModelToYamlModelConverter {

	private static final Map<String, String> IMPORTS =
			Map
					.of(
							"BigDecimal",
							"java.math.BigDecimal",
							"LocalDate",
							"java.time.LocalDate",
							"LocalDateTime",
							"java.time.LocalDateTime",
							"LocalTime",
							"java.time.LocalTime",
							"UUID",
							"java.util.UUID",
							"ZonedDateTime",
							"java.time.ZonedDateTime");
	private static final List<Integer> SQL_TYPES_WITH_LENGTH =
			List.of(Types.CHAR, Types.DECIMAL, Types.NUMERIC, Types.VARBINARY, Types.VARCHAR);
	private static final List<Integer> SQL_TYPES_WITH_DECIMAL_PLACES = List.of(Types.DECIMAL, Types.NUMERIC);

	private final TypeGenerator typeGenerator;

	public DataModelToYamlModelConverter() {
		this(TypeGenerator.INSTANCE);
	}

	public DataModelToYamlModelConverter(TypeGenerator typeGenerator) {
		this.typeGenerator = typeGenerator;
	}

	public YamlModel convert(DataModel dataModel) {
		YamlModel model = new YamlModel();
		model.setTitle(emptyToNull(dataModel.getName()));
		model.setProperties(toProperties(dataModel.getOptions()));
		List<YamlClassDefinition> classes = new ArrayList<>();
		for (TableModel table : dataModel.getTables()) {
			classes.add(toClassDefinition(table));
		}
		model.setClasses(classes.isEmpty() ? null : classes);
		model.setEnums(toEnumDefinitions(dataModel.getAllDomains()));
		return model;
	}

	private List<YamlEnumDefinition> toEnumDefinitions(DomainModel[] domains) {
		List<YamlEnumDefinition> enums = new ArrayList<>();
		for (DomainModel domain : domains) {
			OptionModel enumOption = domain.getOptionByName(DOMAIN_OPTION_ENUM);
			if (enumOption != null) {
				enums.add(new YamlEnumDefinition(domain.getName(), toIdentifiers(enumOption.getParameter())));
			}
		}
		return enums.isEmpty() ? null : enums;
	}

	private List<String> toIdentifiers(String parameter) {
		List<String> identifiers = new ArrayList<>();
		if (parameter != null) {
			for (String identifier : parameter.split(",")) {
				if (!identifier.isBlank()) {
					identifiers.add(identifier.trim());
				}
			}
		}
		return identifiers.isEmpty() ? null : identifiers;
	}

	public YamlArchimedesSection convertToArchimedesSection(DataModel dataModel) {
		Map<String, YamlArchimedesColor> colors = new LinkedHashMap<>();
		List<YamlArchimedesTable> tables = new ArrayList<>();
		for (TableModel table : dataModel.getTables()) {
			String backgroundColor = registerColor(table.getBackgroundColor(), colors);
			String fontColor = registerColor(table.getFontColor(), colors);
			if ((backgroundColor != null) || (fontColor != null)) {
				tables.add(new YamlArchimedesTable(table.getName(), backgroundColor, fontColor));
			}
		}
		List<YamlArchimedesView> views = new ArrayList<>();
		for (GUIViewModel view : dataModel.getViews()) {
			views.add(toArchimedesView(view, dataModel));
		}
		return new YamlArchimedesSection(
				emptyToNull(dataModel.getName()),
				views.isEmpty() ? null : views,
				tables.isEmpty() ? null : tables,
				colors.isEmpty() ? null : new ArrayList<>(colors.values()));
	}

	private String registerColor(Color color, Map<String, YamlArchimedesColor> colors) {
		if (color == null) {
			return null;
		}
		String name =
				color instanceof ExtendedColor
						? ((ExtendedColor) color).getName()
						: String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
		colors.putIfAbsent(name, new YamlArchimedesColor(name, color.getRed(), color.getGreen(), color.getBlue()));
		return name;
	}

	private YamlArchimedesView toArchimedesView(GUIViewModel view, DataModel dataModel) {
		ViewModel viewModel = (ViewModel) view;
		List<YamlArchimedesTablePosition> positions = new ArrayList<>();
		for (TableModel table : viewModel.getTables()) {
			GUIObjectModel guiObject = (GUIObjectModel) table;
			positions.add(new YamlArchimedesTablePosition(table.getName(), guiObject.getX(view), guiObject.getY(view)));
		}
		List<YamlArchimedesRelation> relations = new ArrayList<>();
		for (TableModel table : dataModel.getTables()) {
			for (ColumnModel column : table.getColumns()) {
				if ((column.getRelation() instanceof GUIRelationModel)
						&& ((GUIRelationModel) column.getRelation()).getViews().contains(view)) {
					relations.add(toArchimedesRelation(table, column, (GUIRelationModel) column.getRelation(), view));
				}
			}
		}
		return new YamlArchimedesView(
				viewModel.getName(),
				emptyToNull(viewModel.getComment()),
				viewModel.isMainView(),
				viewModel.isShowReferencedColumns(),
				viewModel.isHideTechnicalFields(),
				viewModel.isHideTransientFields(),
				positions.isEmpty() ? null : positions,
				relations.isEmpty() ? null : relations);
	}

	private YamlArchimedesRelation toArchimedesRelation(TableModel table, ColumnModel column,
			GUIRelationModel relation, GUIViewModel view) {
		List<YamlArchimedesPoint> points = new ArrayList<>();
		List<Point> allPoints = relation.getPointsForView(view);
		for (int i = 1; i < allPoints.size() - 1; i++) {
			points.add(new YamlArchimedesPoint((int) allPoints.get(i).getX(), (int) allPoints.get(i).getY()));
		}
		return new YamlArchimedesRelation(
				table.getName(),
				column.getName(),
				String.valueOf(relation.getDirection(view, 0)),
				relation.getOffset(view, 0),
				String.valueOf(relation.getDirection(view, 1)),
				relation.getOffset(view, 1),
				points.isEmpty() ? null : points);
	}

	private YamlClassDefinition toClassDefinition(TableModel table) {
		List<YamlAttribute> attributes = new ArrayList<>();
		for (ColumnModel column : table.getColumns()) {
			attributes.add(toAttribute(column));
		}
		return new YamlClassDefinition(
				table.getName(),
				attributes.isEmpty() ? null : attributes,
				toProperties(table.getOptions()));
	}

	private YamlAttribute toAttribute(ColumnModel column) {
		YamlAttribute attribute = new YamlAttribute();
		attribute.setName(column.getName());
		attribute.setDefaultValue(emptyToNull(column.getIndividualDefaultValue()));
		attribute.setEnumType(column.isOptionSet(COLUMN_OPTION_ENUM));
		attribute.setReference(column.getReferencedTable() != null);
		attribute.setProperties(toAttributeProperties(column));
		attribute.setType(toType(column));
		return attribute;
	}

	private List<YamlProperty> toAttributeProperties(ColumnModel column) {
		List<YamlProperty> properties = new ArrayList<>();
		if (column.isNotNull()) {
			properties.add(new YamlProperty(PROPERTY_NULLABLE, "false"));
		}
		if (column.isPrimaryKey()) {
			properties.add(new YamlProperty(PROPERTY_PRIMARY_KEY, "true"));
		}
		if (column.isUnique()) {
			properties.add(new YamlProperty(PROPERTY_UNIQUE, "true"));
		}
		for (OptionModel option : column.getOptions()) {
			if (!RESERVED_COLUMN_OPTION_NAMES.contains(option.getName())) {
				properties.add(toProperty(option));
			}
		}
		return properties.isEmpty() ? null : properties;
	}

	private YamlType toType(ColumnModel column) {
		if (column.getReferencedTable() != null) {
			return new YamlType(column.getReferencedTable().getName(), null);
		}
		OptionModel javaTypeOption = column.getOptionByName(COLUMN_OPTION_JAVA_TYPE);
		if ((javaTypeOption != null) && (emptyToNull(javaTypeOption.getParameter()) != null)) {
			return createType(javaTypeOption.getParameter(), new ArrayList<>());
		}
		DomainModel domain = column.getDomain();
		if (domain == null) {
			throw new IllegalStateException("column " + getQualifiedName(column) + " has no domain.");
		}
		String javaTypeName;
		try {
			javaTypeName = typeGenerator.getJavaTypeString(domain, !column.isNotNull());
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(
					"domain '" + domain.getName() + "' of column " + getQualifiedName(column)
							+ " cannot be converted to a Java type.",
					e);
		}
		List<YamlProperty> properties = new ArrayList<>();
		if (SQL_TYPES_WITH_LENGTH.contains(domain.getDataType()) && (domain.getLength() > 0)) {
			properties.add(new YamlProperty(PROPERTY_LENGTH, String.valueOf(domain.getLength())));
		}
		if (SQL_TYPES_WITH_DECIMAL_PLACES.contains(domain.getDataType()) && (domain.getDecimalPlace() >= 0)) {
			properties.add(new YamlProperty(PROPERTY_DECIMAL_PLACES, String.valueOf(domain.getDecimalPlace())));
		}
		return createType(javaTypeName, properties);
	}

	private YamlType createType(String javaTypeName, List<YamlProperty> properties) {
		if (IMPORTS.containsKey(javaTypeName)) {
			properties.add(0, new YamlProperty(PROPERTY_IMPORT, IMPORTS.get(javaTypeName)));
		}
		return new YamlType(javaTypeName, properties.isEmpty() ? null : properties);
	}

	private String getQualifiedName(ColumnModel column) {
		return (column.getTable() != null ? column.getTable().getName() + "." : "") + column.getName();
	}

	private List<YamlProperty> toProperties(OptionModel[] options) {
		if ((options == null) || (options.length == 0)) {
			return null;
		}
		List<YamlProperty> properties = new ArrayList<>();
		for (OptionModel option : options) {
			properties.add(toProperty(option));
		}
		return properties;
	}

	private YamlProperty toProperty(OptionModel option) {
		return new YamlProperty(option.getName(), emptyToNull(option.getParameter()));
	}

	private String emptyToNull(String s) {
		return (s == null) || s.isEmpty() ? null : s;
	}

}
