package archimedes.codegenerators.gui.vaadin;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for the master data page layout.
 *
 * @author ollie (07.04.2022)
 */
public class MaintenanceLayoutClassCodeGenerator extends AbstractGUIVaadinClassCodeGenerator {

	public MaintenanceLayoutClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("MaintenanceLayoutClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
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
		context.put("GUIReferenceDataCollection", getGUIReferenceDataCollection(table));
		context.put("HeaderLayoutClassName", nameGenerator.getHeaderLayoutClassName(model));
		context.put("HeaderLayoutPackageName", nameGenerator.getHeaderLayoutPackageName(model));
		context.put("HeaderAttributeName", getNameFieldName(table));
		context.put("MaintenanceLayoutClassName", nameGenerator.getMaintenanceLayoutClassName(model, table));
		context.put("MasterDataGUIConfigurationClassName", nameGenerator.getMasterDataGUIConfigurationClassName(model));
		context
				.put(
						"MasterDataGUIConfigurationPackageName",
						nameGenerator.getMasterDataGUIConfigurationPackageName(model));
		context.put("MasterDataButtonLayoutClassName", nameGenerator.getMasterDataButtonLayoutClassName(model));
		context.put("MasterDataButtonLayoutPackageName", nameGenerator.getMasterDataButtonLayoutPackageName(model));
		context.put("MasterDataLayoutClassName", nameGenerator.getMasterDataLayoutClassName(model));
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(table));
		context.put("ModelPackageName", serviceNameGenerator.getModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("PageLayoutClassName", nameGenerator.getPageLayoutClassName(table));
		context.put("PluralName", nameGenerator.getPluralName(table).toLowerCase());
		context.put("ResourceManagerInterfaceName", localizationNameGenerator.getResourceManagerInterfaceName());
		context
				.put(
						"ResourceManagerPackageName",
						localizationNameGenerator.getResourceManagerPackageName(model, table));
		context.put("SessionDataClassName", nameGenerator.getSessionDataClassName(model));
		context.put("SessionDataPackageName", nameGenerator.getSessionDataPackageName(model));
		context.put("ServiceInterfaceName", serviceNameGenerator.getServiceInterfaceName(table));
		context.put("ServiceInterfacePackageName", serviceNameGenerator.getServiceInterfacePackageName(model, table));
		context.put("SubclassDataCollection", getSubclassDataCollection(table));
		context.put("UserAuthorizationCheckerClassName", nameGenerator.getUserAuthorizationCheckerClassName(model));
		context.put("UserAuthorizationCheckerPackageName", nameGenerator.getUserAuthorizationCheckerPackageName(model));
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

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getMaintenanceLayoutClassName(model, table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getMaintenanceLayoutPackageName(model, table);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel t) {
		return !t.isOptionSet(GENERATE_MASTER_DATA_GUI)
				|| t.isOptionSet(AbstractClassCodeGenerator.SUBCLASS);
	}

}