package archimedes.codegenerators.gui.vaadin;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.CommonImportAdder;
import archimedes.codegenerators.FieldDeclarations;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for the master data page layout.
 *
 * @author ollie (07.04.2022)
 */
public class MaintenanceViewClassCodeGenerator extends AbstractGUIVaadinClassCodeGenerator {

	private static final boolean MAINTENANCE_VIEW = true;

	public MaintenanceViewClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("MaintenanceViewClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		commonImportAdder = new CommonImportAdder();
		fieldDeclarations = new FieldDeclarations();
		String modelClassName = serviceNameGenerator.getModelClassName(table);
		String serviceInterfaceName = serviceNameGenerator.getServiceInterfaceName(table);
		GUIReferenceDataCollection guiReferenceDataCollection = getGUIReferenceDataCollection(table, MAINTENANCE_VIEW);
		SubclassDataCollection subclassDataCollection = getSubclassDataCollection(model, table);
		List<SubclassReferenceData> uniqueSubclassReferenceDataCollection =
				getUniqueSubclassReferenceData(model, table);
		context.put("BaseURL", getBaseURL(model, table));
		context.put("AbstractMasterDataBaseLayoutClassName", nameGenerator.getAbstractMasterDataBaseLayoutClassName());
		context.put("AbstractMasterDataBaseLayoutPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ButtonClassName", nameGenerator.getButtonClassName(model));
		context.put("ButtonFactoryClassName", nameGenerator.getButtonFactoryClassName(model));
		context.put("ButtonFactoryPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ButtonPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ClassName", getClassName(model, table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("DetailsLayoutClassName", nameGenerator.getDetailsLayoutClassName(model, table));
		context.put("GridData", getGridData(table));
		context.put("GUIReferenceDataCollection", guiReferenceDataCollection);
		context.put("HasSelectionElement", hasSelectionElements(guiReferenceDataCollection.getReferences()));
		context.put("HeaderLayoutClassName", nameGenerator.getHeaderLayoutClassName(model));
		context.put("HeaderLayoutPackageName", nameGenerator.getHeaderLayoutPackageName(model));
		context.put("HeaderAttributeName", getNameFieldName(table));
		context
				.put(
						"ItemLabelGeneratorCollectionClassName",
						nameGenerator.getItemLabelGeneratorCollectionClassName(model, table));
		context
				.put(
						"ItemLabelGeneratorCollectionPackageName",
						nameGenerator.getItemLabelGeneratorCollectionPackageName(model));
		context.put("MaintenanceLayoutClassName", nameGenerator.getMaintenanceViewClassName(model, table));
		context.put("MasterDataGUIConfigurationClassName", nameGenerator.getMasterDataGUIConfigurationClassName(model));
		context
				.put(
						"MasterDataGUIConfigurationPackageName",
						nameGenerator.getMasterDataGUIConfigurationPackageName(model));
		context.put("MasterDataButtonLayoutClassName", nameGenerator.getMasterDataButtonLayoutClassName(model));
		context.put("MasterDataButtonLayoutPackageName", nameGenerator.getMasterDataButtonLayoutPackageName(model));
		context.put("MasterDataLayoutClassName", nameGenerator.getMasterDataViewClassName(model));
		context.put("ModelClassName", modelClassName);
		context.put("PackageName", getPackageName(model, table));
		context.put("PageLayoutClassName", nameGenerator.getPageViewClassName(table));
		context.put("PluralName", nameGenerator.getPluralName(table).toLowerCase());
		context.put("ResourceManagerInterfaceName", localizationNameGenerator.getResourceManagerInterfaceName());
		context
				.put(
						"ResourceManagerPackageName",
						localizationNameGenerator.getResourceManagerPackageName(model, table));
		context.put("SessionDataClassName", nameGenerator.getSessionDataClassName(model));
		context.put("SessionDataPackageName", nameGenerator.getSessionDataPackageName(model));
		context.put("ServiceInterfaceName", serviceInterfaceName);
		context.put("SubclassDataCollection", subclassDataCollection);
		context.put("UniqueSubclassReferenceDataCollection", uniqueSubclassReferenceDataCollection);
		context.put("UserAuthorizationCheckerClassName", nameGenerator.getUserAuthorizationCheckerClassName(model));
		context.put("UserAuthorizationCheckerPackageName", nameGenerator.getUserAuthorizationCheckerPackageName(model));
		importDeclarations.add(serviceNameGenerator.getModelPackageName(model, table), modelClassName);
		importDeclarations.add(serviceNameGenerator.getServiceInterfacePackageName(model, table), serviceInterfaceName);
		addImportsFromSubClassDataCollection(subclassDataCollection);
		addImportsFromUniqueSubclassReferenceData(uniqueSubclassReferenceDataCollection);
		addGUIReferencesToFieldDeclarations(guiReferenceDataCollection.getReferences());
		cleanUpFieldDeclarationsFromSubClassReferences(subclassDataCollection);
		LabelPropertiesGenerator
				.addLabel(
						getClassName(table) + ".header.prefix.label",
						nameGenerator.getClassName(table.getName()) + " - ");
	}

	private boolean hasSelectionElements(List<GUIReferenceData> guiReferenceData) {
		return (guiReferenceData != null) && (guiReferenceData.size() > 0);
	}

	private String getBaseURL(DataModel model, TableModel table) {
		return table.isOptionSet(GUI_BASE_URL)
				? table.getOptionByName(GUI_BASE_URL).getParameter()
				: model.isOptionSet(GUI_BASE_URL)
						? model.getOptionByName(GUI_BASE_URL).getParameter()
						: model.getApplicationName().toLowerCase();
	}

	private List<GridData> getGridData(TableModel table) {
		return List
				.of(table.getColumns())
				.stream()
				.filter(column -> column.isOptionSet(GUI_EDITOR_POS))
				.map(
						column -> new GridData()
								.setFieldNameCamelCase(nameGenerator.getCamelCase(column.getName()))
								.setPosition(getPosition(column))
								.setResourceName(getResourceName(column)))
				.sorted((gd0, gd1) -> gd0.getPosition() - gd1.getPosition())
				.collect(Collectors.toList());
	}

	private int getPosition(ColumnModel column) {
		return column.isOptionSet(GUI_EDITOR_POS)
				? Integer.valueOf(column.getOptionByName(GUI_EDITOR_POS).getParameter())
				: 0;
	}

	private String getResourceName(ColumnModel column) {
		return nameGenerator.getCamelCase(column.getName()).toLowerCase();
	}

	private void addImportsFromSubClassDataCollection(SubclassDataCollection subclassDataCollection) {
		for (SubclassData subclass : subclassDataCollection.getSubclasses()) {
			importDeclarations.add(subclass.getModelPackageName(), subclass.getModelClassName());
		}
	}

	private void addImportsFromUniqueSubclassReferenceData(List<SubclassReferenceData> subclassDataCollection) {
		for (SubclassReferenceData subclassReferenceData : subclassDataCollection) {
			importDeclarations
					.add(
							subclassReferenceData.getServicePackageName(),
							subclassReferenceData.getServiceInterfaceName());
		}
	}

	private void cleanUpFieldDeclarationsFromSubClassReferences(SubclassDataCollection subclassDataCollection) {
		for (SubclassData subClass : subclassDataCollection.getSubclasses()) {
			for (SubclassReferenceData subclassReferenceData : subClass.getReferences()) {
				fieldDeclarations
						.removeAttribute(
								subclassReferenceData.getServiceAttributeName(),
								subclassReferenceData.getServiceInterfaceName());
			}
		}
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getMaintenanceViewClassName(model, table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getMaintenanceViewPackageName(model, table);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel t) {
		return !t.isOptionSet(GENERATE_MASTER_DATA_GUI) || t.isOptionSet(AbstractClassCodeGenerator.SUBCLASS);
	}

}