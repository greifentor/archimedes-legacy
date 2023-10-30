package archimedes.codegenerators.gui.vaadin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.localization.LocalizationNameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

public abstract class AbstractGUIVaadinClassCodeGenerator extends AbstractClassCodeGenerator<GUIVaadinNameGenerator> {

	public static final String CUBE_APPLICATION = "CUBE_APPLICATION";
	public static final String DETAILS_LAYOUT_ONLY = "DETAILS_LAYOUT_ONLY";
	public static final String GENERATE_MASTER_DATA_GUI = "GENERATE_MASTER_DATA_GUI";
	public static final String GRID_FIELD = "GRID_FIELD";
	public static final String GUI_BASE_URL = "GUI_BASE_URL";
	public static final String GUI_EDITOR_POS = "GUI_EDITOR_POS";
	public static final String GUI_LABEL_TEXT = "GUI_LABEL_TEXT";
	public static final String NAME_FIELD = "NAME_FIELD";

	protected static final LocalizationNameGenerator localizationNameGenerator = new LocalizationNameGenerator();
	protected static final ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public AbstractGUIVaadinClassCodeGenerator(String templateFileName, AbstractCodeFactory codeFactory) {
		super(
				templateFileName,
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	protected void addGUIReferencesToFieldDeclarations(List<GUIReferenceData> guiReferenceData) {
		guiReferenceData
				.stream()
				.forEach(data -> fieldDeclarations.add(data.getServiceInterfaceName(), data.getServiceAttributeName()));
	}

	protected String getServiceInterfaceName(TableModel table) {
		TableModel supertable = getSuperclassTable(table);
		return serviceNameGenerator.getServiceInterfaceName(supertable != null ? supertable : table);
	}

	protected List<GridData> getGridData(TableModel table) {
		Function<ColumnModel, Boolean> isInGrid =
				hasGridFieldColumn(table)
						? column -> column.isOptionSet(GRID_FIELD)
						: column -> column.isOptionSet(GUI_EDITOR_POS);
		return List
				.of(table.getColumns())
				.stream()
				.filter(column -> isInGrid.apply(column))
				.map(
						column -> new GridData()
								.setFieldNameCamelCase(nameGenerator.getCamelCase(column.getName()))
								.setPosition(getPosition(column))
								.setResourceName(getResourceName(column))
								.setSimpleBoolean(isSimpleBoolean(column))
								.setWidth(getWidth(column)))
				.sorted((gd0, gd1) -> gd0.getPosition() - gd1.getPosition())
				.collect(Collectors.toList());
	}

	private boolean hasGridFieldColumn(TableModel table) {
		return table.getColumnsWithOption(GRID_FIELD).length > 0;
	}

	private int getPosition(ColumnModel column) {
		if ((column.getOptionByName(GRID_FIELD) != null)
				&& (column.getOptionByName(GRID_FIELD).getParameter() != null)) {
			return Integer.valueOf(StringUtils.split(column.getOptionByName(GRID_FIELD).getParameter(), ',')[0]);
		}
		return column.isOptionSet(GUI_EDITOR_POS)
				? Integer.valueOf(column.getOptionByName(GUI_EDITOR_POS).getParameter())
				: 0;
	}

	private String getResourceName(ColumnModel column) {
		return nameGenerator.getCamelCase(column.getName()).toLowerCase();
	}

	private Integer getWidth(ColumnModel column) {
		if ((column.getOptionByName(GRID_FIELD) != null)
				&& (column.getOptionByName(GRID_FIELD).getParameter() != null)) {
			String[] parameters = StringUtils.split(column.getOptionByName(GRID_FIELD).getParameter(), ',');
			if (parameters.length > 1) {
				return Integer.valueOf(parameters[1]);
			}
		}
		return null;
	}

	protected GUIReferenceDataCollection getGUIReferenceDataCollection(TableModel table) {
		return getGUIReferenceDataCollection(table, false);
	}

	protected GUIReferenceDataCollection getGUIReferenceDataCollection(TableModel table, boolean maintenanceView) {
		return new GUIReferenceDataCollection().addGUIReferenceData(getGUIReferenceData(table, maintenanceView));
	}

	protected List<GUIReferenceData> getGUIReferenceData(TableModel table) {
		return getGUIReferenceData(table, false);
	}

	protected List<GUIReferenceData> getGUIReferenceData(TableModel table, boolean maintenanceView) {
		return getAllColumns(new ArrayList<>(), table)
				.stream()
				.filter(column -> column.isOptionSet(GUI_EDITOR_POS))
				.filter(column -> column.getReferencedTable() != null)
				.map(column -> createGUIReferenceData(column, maintenanceView))
				.collect(Collectors.toSet())
				.stream()
				.collect(Collectors.toList());
	}

	protected GUIReferenceData createGUIReferenceData(ColumnModel column, boolean maintenanceView) {
		DataModel model = column.getTable().getDataModel();
		TableModel referencedTable = column.getReferencedTable();
		TableModel referencedSuperTable = getSuperclassTable(column.getReferencedTable());
		referencedSuperTable = referencedSuperTable != null ? referencedSuperTable : referencedTable;
		String referenceModelClassName = serviceNameGenerator.getModelClassName(referencedTable);
		String referenceModelPackageName = serviceNameGenerator.getModelPackageName(model, referencedTable);
		String serviceInterfaceName = serviceNameGenerator.getServiceInterfaceName(referencedSuperTable);
		String servicePackageName = serviceNameGenerator.getServiceInterfacePackageName(model, referencedTable);
		if (!maintenanceView) {
			importDeclarations.add(referenceModelPackageName, referenceModelClassName);
		}
		importDeclarations.add(servicePackageName, serviceInterfaceName);
		return new GUIReferenceData()
				.setFieldNameCamelCase(nameGenerator.getCamelCase(nameGenerator.getAttributeName(column)))
				.setFindAllMethodNameExtension(getFindAllMethodNameExtension(referencedTable))
				.setNullable(!column.isNotNull())
				.setReferencedModelClassName(referenceModelClassName)
				.setReferencedModelClassFieldName(nameGenerator.getAttributeName(referenceModelClassName))
				.setReferencedModelNameFieldName(getNameFieldName(referencedSuperTable))
				.setReferencedModelPackageName(referenceModelPackageName)
				.setResourceName(nameGenerator.getAttributeName(column).toLowerCase())
				.setServiceAttributeName(nameGenerator.getAttributeName(serviceInterfaceName))
				.setServiceInterfaceName(serviceInterfaceName)
				.setServicePackageName(servicePackageName)
				.setTableName(nameGenerator.getCamelCase(column.getTable().getName()));
	}

	private String getFindAllMethodNameExtension(TableModel table) {
		return nameGenerator.getCamelCase(!isSubclass(table) ? "" : table.getName());
	}

	protected String getNameFieldName(TableModel table) {
		List<ColumnModel> columns = getAllColumns(new ArrayList<ColumnModel>(), table);
		return columns
				.stream()
				.filter(column -> column.isOptionSet(NAME_FIELD))
				.map(column -> nameGenerator.getCamelCase(column.getName()))
				.findFirst()
				.orElse("FIELD_NAME");
	}

	protected SubclassDataCollection getSubclassDataCollection(DataModel model, TableModel table) {
		return new SubclassDataCollection()
				.addSubclasses(
						getSubclassTables(table)
								.stream()
								.filter(
										t -> t
												.isOptionSet(
														AbstractGUIVaadinClassCodeGenerator.GENERATE_MASTER_DATA_GUI))
								.map(
										t -> new SubclassData()
												.setDetailsLayoutClassName(
														nameGenerator.getDetailsLayoutClassName(t.getDataModel(), t))
												.setItemLabelGeneratorCollectionAttributeName(
														nameGenerator
																.getAttributeName(
																		nameGenerator
																				.getItemLabelGeneratorCollectionClassName(
																						model,
																						t)))
												.setItemLabelGeneratorCollectionClassName(
														nameGenerator
																.getItemLabelGeneratorCollectionClassName(model, t))
												.setItemLabelGeneratorCollectionPackageName(
														nameGenerator.getItemLabelGeneratorCollectionPackageName(model))
												.setModelClassName(serviceNameGenerator.getModelClassName(t))
												.setModelPackageName(
														serviceNameGenerator.getModelPackageName(t.getDataModel(), t))
												.setReferences(getSubclassReferenceData(t)))
								.collect(Collectors.toList())
								.toArray(new SubclassData[0]));
	}

	private List<SubclassReferenceData> getSubclassReferenceData(TableModel table) {
		return getAllColumns(new ArrayList<ColumnModel>(), table)
				.stream()
				.filter(column -> column.isOptionSet(GUI_EDITOR_POS))
				.filter(column -> column.getReferencedTable() != null)
				.map(this::createSubclassReferenceData)
				.collect(Collectors.toSet())
				.stream()
				.sorted((srd0, srd1) -> srd0.getServiceAttributeName().compareTo(srd1.getServiceAttributeName()))
				.collect(Collectors.toList());
	}

	protected SubclassReferenceData createSubclassReferenceData(ColumnModel column) {
		DataModel model = column.getTable().getDataModel();
		TableModel referencedTable = column.getReferencedTable();
		TableModel referencedSuperTable = getSuperclassTable(column.getReferencedTable());
		referencedSuperTable = referencedSuperTable != null ? referencedSuperTable : referencedTable;
		String serviceInterfaceName = serviceNameGenerator.getServiceInterfaceName(referencedSuperTable);
		return new SubclassReferenceData()
				.setServiceAttributeName(nameGenerator.getAttributeName(serviceInterfaceName))
				.setServiceInterfaceName(serviceInterfaceName)
				.setServicePackageName(
						serviceNameGenerator.getServiceInterfacePackageName(model, referencedSuperTable));
	}

	protected List<SubclassReferenceData> getUniqueSubclassReferenceData(DataModel model, TableModel table) {
		return getSubclassDataCollection(model, table)
				.getSubclasses()
				.stream()
				.flatMap(subclass -> subclass.getReferences().stream())
				.collect(Collectors.toSet())
				.stream()
				.sorted((r0, r1) -> r0.getServiceInterfaceName().compareTo(r1.getServiceInterfaceName()))
				.collect(Collectors.toList());
	}

}