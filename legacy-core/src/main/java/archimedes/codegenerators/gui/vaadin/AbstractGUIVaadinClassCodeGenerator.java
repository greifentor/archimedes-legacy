package archimedes.codegenerators.gui.vaadin;

import java.util.List;
import java.util.stream.Collectors;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.localization.LocalizationNameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

public abstract class AbstractGUIVaadinClassCodeGenerator
		extends AbstractClassCodeGenerator<GUIVaadinNameGenerator> {

	public static final String GENERATE_MASTER_DATA_GUI = "GENERATE_MASTER_DATA_GUI";
	public static final String GUI_BASE_URL = "GUI_BASE_URL";
	public static final String GUI_EDITOR_POS = "GUI_EDITOR_POS";
	public static final String GUI_LABEL_TEXT = "GUI_LABEL_TEXT";
	public static final String NAME_FIELD = "NAME_FIELD";

	protected static final LocalizationNameGenerator localizationNameGenerator = new LocalizationNameGenerator();
	protected static final ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public AbstractGUIVaadinClassCodeGenerator(String templateFileName, AbstractCodeFactory codeFactory) {
		super(templateFileName, GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	protected GUIReferenceDataCollection getGUIReferenceDataCollection(TableModel table) {
		return new GUIReferenceDataCollection().addGUIReferenceData(getGUIReferenceData(table));
	}

	protected List<GUIReferenceData> getGUIReferenceData(TableModel table) {
		return List
				.of(table.getColumns())
				.stream()
				.filter(column -> column.isOptionSet(GUI_EDITOR_POS))
				.filter(column -> column.getReferencedTable() != null)
				.map(this::createGUIReferenceData)
				.collect(Collectors.toList());
	}

	protected GUIReferenceData createGUIReferenceData(ColumnModel column) {
		DataModel model = column.getTable().getDataModel();
		TableModel referencedTable = column.getReferencedTable();
		String serviceInterfaceName = serviceNameGenerator.getServiceInterfaceName(referencedTable);
		return new GUIReferenceData()
				.setFieldNameCamelCase(nameGenerator.getCamelCase(nameGenerator.getAttributeName(column)))
				.setReferencedModelClassName(serviceNameGenerator.getModelClassName(referencedTable))
				.setReferencedModelNameFieldName(getNameFieldName(referencedTable))
				.setReferencedModelPackageName(serviceNameGenerator.getModelPackageName(model, referencedTable))
				.setServiceAttributeName(nameGenerator.getAttributeName(serviceInterfaceName))
				.setServiceInterfaceName(serviceInterfaceName)
				.setServicePackageName(serviceNameGenerator.getServiceInterfacePackageName(model, referencedTable));
	}

	protected String getNameFieldName(TableModel table) {
		return List
				.of(table.getColumns())
				.stream()
				.filter(column -> column.isOptionSet(NAME_FIELD))
				.map(column -> nameGenerator.getCamelCase(column.getName()))
				.findFirst()
				.orElse("FIELD_NAME");
	}

	protected SubclassDataCollection getSubclassDataCollection(TableModel table) {
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
												.setModelClassName(serviceNameGenerator.getModelClassName(t))
												.setModelPackageName(
														serviceNameGenerator.getModelPackageName(t.getDataModel(), t))
												.setReferences(getSubclassReferenceData(t)))
								.collect(Collectors.toList())
								.toArray(new SubclassData[0]));
	}

	private List<SubclassReferenceData> getSubclassReferenceData(TableModel table) {
		return List
				.of(table.getColumns())
				.stream()
				.filter(column -> column.isOptionSet(GUI_EDITOR_POS))
				.filter(column -> column.getReferencedTable() != null)
				.map(this::createSubclassReferenceData)
				.collect(Collectors.toList());
	}

	protected SubclassReferenceData createSubclassReferenceData(ColumnModel column) {
		DataModel model = column.getTable().getDataModel();
		TableModel referencedTable = column.getReferencedTable();
		String serviceInterfaceName = serviceNameGenerator.getServiceInterfaceName(referencedTable);
		return new SubclassReferenceData()
				.setServiceAttributeName(nameGenerator.getAttributeName(serviceInterfaceName))
				.setServiceInterfaceName(serviceInterfaceName)
				.setServicePackageName(serviceNameGenerator.getServiceInterfacePackageName(model, referencedTable));
	}

}