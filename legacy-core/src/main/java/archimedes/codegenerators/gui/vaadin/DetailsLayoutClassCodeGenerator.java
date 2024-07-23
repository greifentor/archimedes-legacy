package archimedes.codegenerators.gui.vaadin;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.CommonImportAdder;
import archimedes.codegenerators.FieldDeclarations;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for the master data page layout.
 *
 * @author ollie (11.04.2022)
 */
public class DetailsLayoutClassCodeGenerator extends AbstractGUIVaadinClassCodeGenerator {

	public DetailsLayoutClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("DetailsLayoutClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		commonImportAdder = new CommonImportAdder();
		fieldDeclarations = new FieldDeclarations();
		String abstractMasterDataBaseLayoutClassName = nameGenerator.getAbstractMasterDataBaseLayoutClassName();
		String buttonFactoryClassName = nameGenerator.getButtonFactoryClassName(model);
		String componentFactoryClassName = nameGenerator.getComponentFactoryClassName(model);
		String modelClassName = serviceNameGenerator.getModelClassName(table);
		String modelSuperClassName = getSuperclassName(table, serviceNameGenerator::getModelClassName);
		String resourceManagerInterfaceName = localizationNameGenerator.getResourceManagerInterfaceName();
		String serviceInterfaceName = getServiceInterfaceName(table);
		String sessionDataClassName = nameGenerator.getSessionDataClassName(model);
		List<GUIReferenceData> guiReferenceData = getGUIReferenceData(table);
		GUIColumnDataCollection guiColumnDataCollection =
				getGUIColumnDataCollection(new GUIColumnDataCollection(), table);
		List<ListGridData> listGridData = getListGridData(model, table);
		context.put("AbstractMasterDataBaseLayoutClassName", abstractMasterDataBaseLayoutClassName);
		context.put("ButtonFactoryClassName", buttonFactoryClassName);
		context.put("ClassName", getClassName(model, table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("ComponentFactoryClassName", componentFactoryClassName);
		context.put("ExceptionsPackageName", serviceNameGenerator.getExceptionsPackageName(model));
		context.put("GUIColumnDataCollection", guiColumnDataCollection);
		context.put("GUIReferences", guiReferenceData);
		context.put("HasSelectionElement", hasSelectionElements(guiReferenceData, guiColumnDataCollection));
		context.put("HasListGridDataCollection", !listGridData.isEmpty());
		context
				.put(
						"ItemLabelGeneratorCollectionClassName",
						nameGenerator.getItemLabelGeneratorCollectionClassName(model, table));
		context
				.put(
						"ItemLabelGeneratorCollectionPackageName",
						nameGenerator.getItemLabelGeneratorCollectionPackageName(model));
		context.put("ListGridDataCollection", listGridData);
		context.put("MasterDataGUIConfigurationClassName", nameGenerator.getMasterDataGUIConfigurationClassName(model));
		context
				.put(
						"MasterDataGUIConfigurationPackageName",
						nameGenerator.getMasterDataGUIConfigurationPackageName(model));
		context.put("ModelClassName", modelClassName);
		context.put("ModelSuperClassName", modelSuperClassName);
		context.put("PackageName", getPackageName(model, table));
		context.put("PreferenceData", getPreferenceData(table));
		context.put("RemoveConfirmDialogClassName", nameGenerator.getRemoveConfirmDialogClassName(model));
		context.put("RemoveConfirmDialogPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ResourceManagerInterfaceName", resourceManagerInterfaceName);
		context.put("SessionDataClassName", sessionDataClassName);
		context.put("ServiceInterfaceName", serviceInterfaceName);
		context.put("ServiceProviderClassName", nameGenerator.getServiceProviderClassName(model));
		context.put("VaadinComponentPackageName", nameGenerator.getVaadinComponentPackageName(model));
		importDeclarations
				.add(nameGenerator.getVaadinComponentPackageName(model), abstractMasterDataBaseLayoutClassName);
		importDeclarations.add(nameGenerator.getVaadinComponentPackageName(model), buttonFactoryClassName);
		importDeclarations.add(nameGenerator.getVaadinComponentPackageName(model), componentFactoryClassName);
		importDeclarations
				.add(
						localizationNameGenerator.getResourceManagerPackageName(model, table),
						resourceManagerInterfaceName);
		importDeclarations.add(serviceNameGenerator.getModelPackageName(model, table), modelClassName);
		if (modelSuperClassName != null) {
			importDeclarations.add(serviceNameGenerator.getModelPackageName(model, table), modelSuperClassName);
		}
		// TODO OLI: Should be removed.
		importDeclarations.add(serviceNameGenerator.getServiceInterfacePackageName(model, table), serviceInterfaceName);
		importDeclarations.add(nameGenerator.getSessionDataPackageName(model), sessionDataClassName);
		addGUIReferencesToFieldDeclarations(guiReferenceData);
		guiColumnDataCollection
				.getColumns()
				.forEach(
						cd -> LabelPropertiesGenerator
								.addLabel(
										getClassName(table) + ".field." + cd.getFieldNameCamelCase().toLowerCase()
												+ ".label",
										nameGenerator.getClassName(cd.getFieldNameCamelCase())));
	}

	private List<ListGridData> getListGridData(DataModel model, TableModel table) {
		return getCompositionLists(table).stream().map(cld -> {
			String className = nameGenerator.getListDetailsLayoutClassName(model, cld.getMemberTable());
			String packageName = nameGenerator.getListDetailsLayoutPackageName(model);
			importDeclarations.add(packageName, className);
			return new ListGridData()
					.setListDetailsLayoutClassName(className)
					.setListDetailsLayoutPackageName(packageName);
		}).collect(Collectors.toList());
	}

	private boolean hasSelectionElements(List<GUIReferenceData> guiReferenceData,
			GUIColumnDataCollection guiColumnDataCollection) {
		return ((guiReferenceData != null) && (guiReferenceData.size() > 0))
				|| (guiColumnDataCollection.hasFieldType("ENUM"));
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getDetailsLayoutClassName(model, table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getPageViewPackageName(model, table);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel t) {
		return !t.isOptionSet(GENERATE_MASTER_DATA_GUI) && !t.isOptionSet(DETAILS_LAYOUT_ONLY)
				|| t.isOptionSetWithValue(MEMBER_LIST, "MEMBER");
	}

}