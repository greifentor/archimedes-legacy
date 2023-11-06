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
public class ListDetailsLayoutClassCodeGenerator extends AbstractGUIVaadinClassCodeGenerator {

	public ListDetailsLayoutClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("ListDetailsLayoutClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		commonImportAdder = new CommonImportAdder();
		fieldDeclarations = new FieldDeclarations();
		String componentFactoryClassName = nameGenerator.getComponentFactoryClassName(model);
		String modelClassName = serviceNameGenerator.getModelClassName(table);
		String modelParentClassName = nameGenerator.getClassName(getParent(table));
		String resourceManagerInterfaceName = localizationNameGenerator.getResourceManagerInterfaceName();
		String sessionDataClassName = nameGenerator.getSessionDataClassName(model);
		List<GridData> gridData = getGridData(table);
		List<GUIReferenceData> guiReferenceData = getGUIReferenceData(table);
		GUIColumnDataCollection guiColumnDataCollection =
				getGUIColumnDataCollection(new GUIColumnDataCollection(), table);
		List<ListGridData> listGridData = getListGridData(model, table);
		context.put("ButtonClassName", nameGenerator.getButtonClassName(model));
		context.put("ButtonPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ClassName", getClassName(model, table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("ComponentFactoryClassName", componentFactoryClassName);
		context.put("DetailsDialogClassName", nameGenerator.getDetailsDialogClassName(model, table));
		context.put("DetailsDialogPackageName", nameGenerator.getDetailsDialogPackageName(model));
		context.put("GridData", gridData);
		context.put("GUIColumnDataCollection", guiColumnDataCollection);
		context.put("GUIReferences", guiReferenceData);
		context.put("HasSelectionElement", hasSelectionElements(guiReferenceData, guiColumnDataCollection));
		context.put("HasListGridDataCollection", !listGridData.isEmpty());
		context.put("ListGridDataCollection", listGridData);
		context
				.put(
						"MasterDataGridFieldRendererInterfaceName",
						nameGenerator.getMasterDataGridFieldRendererInterfaceName(model));
		context.put("MasterDataGridFieldRendererPackageName", nameGenerator.getMasterDataPackageName(model));
		context.put("MasterDataGUIConfigurationClassName", nameGenerator.getMasterDataGUIConfigurationClassName(model));
		context
				.put(
						"MasterDataGUIConfigurationPackageName",
						nameGenerator.getMasterDataGUIConfigurationPackageName(model));
		context.put("ModelAttributeName", nameGenerator.getAttributeName(modelClassName));
		context.put("ModelClassName", modelClassName);
		context.put("ModelSuperClassName", modelParentClassName);
		context.put("PackageName", getPackageName(model, table));
		context.put("ParentModelClassName", modelParentClassName);
		context.put("PreferenceData", getPreferenceData(table));
		context.put("ResourceManagerInterfaceName", resourceManagerInterfaceName);
		context.put("ResourceManagerPackageName", serviceNameGenerator.getResourceManagerInterfacePackageName(model));
		context.put("SessionDataClassName", sessionDataClassName);
		context.put("SessionDataPackageName", nameGenerator.getSessionDataPackageName(model));
		importDeclarations.add(nameGenerator.getVaadinComponentPackageName(model), componentFactoryClassName);
		importDeclarations.add(serviceNameGenerator.getModelPackageName(model, table), modelClassName);
		if (modelParentClassName != null) {
			importDeclarations.add(serviceNameGenerator.getModelPackageName(model, table), modelParentClassName);
		}
		importDeclarations
				.add(
						nameGenerator.getDetailsDialogPackageName(model),
						nameGenerator.getDetailsDialogClassName(model, table));
		addGUIReferencesToFieldDeclarations(guiReferenceData);
		guiColumnDataCollection
				.getColumns()
				.forEach(
						cd -> LabelPropertiesGenerator
								.addLabel(
										getClassName(table) + ".field." + cd.getFieldNameCamelCase().toLowerCase()
												+ ".label",
										nameGenerator.getClassName(cd.getFieldNameCamelCase())));
		gridData
				.forEach(
						gd -> LabelPropertiesGenerator
								.addLabel(
										getClassName(table) + ".grid.header." + gd.getFieldNameCamelCase().toLowerCase()
												+ ".label",
										nameGenerator.getClassName(gd.getFieldNameCamelCase())));
	}

	private TableModel getParent(TableModel table) {
		return List
				.of(table.getColumns())
				.stream()
				.filter(c -> isAParent(c.getReferencedTable()))
				.map(c -> c.getReferencedTable())
				.findFirst()
				.orElse(null);
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
		return nameGenerator.getListDetailsLayoutClassName(model, table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getListDetailsLayoutPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel t) {
		return !t.isOptionSet(GENERATE_MASTER_DATA_GUI) || !t.isOptionSetWithValue(MEMBER_LIST, "MEMBER");
	}

}