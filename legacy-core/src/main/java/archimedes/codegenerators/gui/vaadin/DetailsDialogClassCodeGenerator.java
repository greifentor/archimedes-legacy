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
public class DetailsDialogClassCodeGenerator extends AbstractGUIVaadinClassCodeGenerator {

	public DetailsDialogClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("DetailsDialogClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		commonImportAdder = new CommonImportAdder();
		fieldDeclarations = new FieldDeclarations();
		String abstractMasterDataBaseLayoutClassName = nameGenerator.getAbstractMasterDataBaseLayoutClassName();
		String componentFactoryClassName = nameGenerator.getComponentFactoryClassName(model);
		String modelClassName = serviceNameGenerator.getModelClassName(table);
		String modelSuperClassName = getSuperclassName(table, serviceNameGenerator::getModelClassName);
		String resourceManagerInterfaceName = localizationNameGenerator.getResourceManagerInterfaceName();
		String sessionDataClassName = nameGenerator.getSessionDataClassName(model);
		List<GUIReferenceData> guiReferenceData = getGUIReferenceData(table);
		GUIColumnDataCollection guiColumnDataCollection =
				getGUIColumnDataCollection(new GUIColumnDataCollection(), table);
		List<ListGridData> listGridData = getListGridData(model, table);
		context.put("AbstractMasterDataBaseLayoutClassName", abstractMasterDataBaseLayoutClassName);
		context.put("ButtonClassName", nameGenerator.getButtonClassName(model));
		context.put("ButtonPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ClassName", getClassName(model, table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("ComponentFactoryClassName", componentFactoryClassName);
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
		context.put("ResourceManagerInterfaceName", resourceManagerInterfaceName);
		context.put("ServiceProviderClassName", nameGenerator.getServiceProviderClassName(model));
		context.put("SessionDataClassName", sessionDataClassName);
		context.put("VaadinComponentPackageName", nameGenerator.getVaadinComponentPackageName(model));
		importDeclarations.add(nameGenerator.getVaadinComponentPackageName(model), componentFactoryClassName);
		importDeclarations.add(serviceNameGenerator.getModelPackageName(model, table), modelClassName);
		if (modelSuperClassName != null) {
			importDeclarations.add(serviceNameGenerator.getModelPackageName(model, table), modelSuperClassName);
		}
		importDeclarations.add(nameGenerator.getSessionDataPackageName(model), sessionDataClassName);
		addGUIReferencesToFieldDeclarations(guiReferenceData);
		LabelPropertiesGenerator.addLabel("commons.button.cancel.text", "Abbruch");
		guiColumnDataCollection
				.getColumns()
				.forEach(
						cd -> LabelPropertiesGenerator
								.addLabel(
										nameGenerator.getDetailsLayoutClassName(model, table) + ".field."
												+ cd.getFieldNameCamelCase().toLowerCase()
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
		return nameGenerator.getDetailsDialogClassName(model, table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getDetailsDialogPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel t) {
		return !t.isOptionSet(GENERATE_MASTER_DATA_GUI) || t.isOptionSetWithValue(MEMBER_LIST, "PARENT")
				|| !t.isOptionSet(MEMBER_LIST);
	}

}