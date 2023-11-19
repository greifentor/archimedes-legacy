package archimedes.codegenerators.gui.vaadin;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Filters;
import archimedes.codegenerators.Filters.Filter;
import archimedes.codegenerators.localization.LocalizationNameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for the master data page layout.
 *
 * @author ollie (07.04.2022)
 */
public class PageViewClassCodeGenerator extends AbstractGUIVaadinClassCodeGenerator {

	private static final LocalizationNameGenerator localizationNameGenerator = new LocalizationNameGenerator();
	private static final ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public PageViewClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("PageViewClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<GridData> gridData = getGridData(table);
		SubclassDataCollection subclassDataCollection = getSubclassDataCollection(model, table);
		context.put("BaseURL", getBaseURL(model, table));
		context.put("ButtonClassName", nameGenerator.getButtonClassName(model));
		context.put("ButtonFactoryClassName", nameGenerator.getButtonFactoryClassName(model));
		context.put("ButtonFactoryPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ButtonPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ClassName", getClassName(model, table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("Filters", getFilters(model, table));
		context.put("GridData", gridData);
		context.put("HeaderLayoutClassName", nameGenerator.getHeaderLayoutClassName(model));
		context.put("HeaderLayoutPackageName", nameGenerator.getHeaderLayoutPackageName(model));
		context.put("MaintenanceViewClassName", nameGenerator.getMaintenanceViewClassName(model, table));
		context.put("MasterDataGUIConfigurationClassName", nameGenerator.getMasterDataGUIConfigurationClassName(model));
		context
				.put(
						"MasterDataGUIConfigurationPackageName",
						nameGenerator.getMasterDataGUIConfigurationPackageName(model));
		context.put("MasterDataButtonLayoutClassName", nameGenerator.getMasterDataButtonLayoutClassName(model));
		context.put("MasterDataButtonLayoutPackageName", nameGenerator.getMasterDataButtonLayoutPackageName(model));
		context.put("MasterDataViewClassName", nameGenerator.getMasterDataViewClassName(model));
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(table));
		context.put("ModelPackageName", serviceNameGenerator.getModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("PageParametersClassName", serviceNameGenerator.getPageParametersClassName());
		context.put("PageParametersPackageName", serviceNameGenerator.getPageParametersPackageName(model, table));
		context.put("PluralName", nameGenerator.getPluralName(table).toLowerCase());
		context.put("ReferencedColumnData", getGUIReferenceData(table));
		context.put("ResourceManagerInterfaceName", localizationNameGenerator.getResourceManagerInterfaceName());
		context
				.put(
						"ResourceManagerPackageName",
						localizationNameGenerator.getResourceManagerPackageName(model, table));
		context.put("SelectionDialogClassName", nameGenerator.getSelectionDialogClassName(model));
		context.put("SelectionDialogPackageName", nameGenerator.getSelectionDialogPackageName(model));
		context.put("ServiceInterfaceName", serviceNameGenerator.getServiceInterfaceName(table));
		context.put("ServiceInterfacePackageName", serviceNameGenerator.getServiceInterfacePackageName(model, table));
		context.put("SessionDataClassName", nameGenerator.getSessionDataClassName(model));
		context.put("SessionDataPackageName", nameGenerator.getSessionDataPackageName(model));
		context.put("SubclassDataCollection", subclassDataCollection);
		context.put("UserAuthorizationCheckerClassName", nameGenerator.getUserAuthorizationCheckerClassName(model));
		context.put("UserAuthorizationCheckerPackageName", nameGenerator.getUserAuthorizationCheckerPackageName(model));
		LabelPropertiesGenerator
				.addLabel(getClassName(table) + ".header.label", nameGenerator.getClassName(table.getName()));
		gridData
				.forEach(
						gd -> LabelPropertiesGenerator
								.addLabel(
										getClassName(table) + ".grid.header."
												+ gd.getFieldNameCamelCase().toLowerCase()
												+ ".label",
										nameGenerator.getClassName(gd.getFieldNameCamelCase())));
		subclassDataCollection
				.getSubclasses()
				.forEach(
						sd -> LabelPropertiesGenerator
								.addLabel(
										getClassName(table) + ".subclass.selection." + sd.getModelClassName()
												+ ".label",
										sd.getModelClassName()));
	}

	private String getBaseURL(DataModel model, TableModel table) {
		return table.isOptionSet(GUI_BASE_URL)
				? table.getOptionByName(GUI_BASE_URL).getParameter()
				: model.isOptionSet(GUI_BASE_URL)
						? model.getOptionByName(GUI_BASE_URL).getParameter()
						: model.getApplicationName().toLowerCase();
	}

	private List<Filter> getFilters(DataModel model, TableModel table) {
		return List
				.of(table.getColumnsWithOption(Filters.FILTER))
				.stream()
				.map(
						column -> new Filter()
								.setColumn(column)
								.setFieldName(nameGenerator.getCamelCase(nameGenerator.getAttributeName(column))))
				.collect(Collectors.toList());
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getPageViewClassName(table);
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
		return !t.isOptionSet(GENERATE_MASTER_DATA_GUI) || t.isOptionSet(SUBCLASS)
				|| t.isOptionSetWithValue(MEMBER_LIST, "MEMBER");
	}

}