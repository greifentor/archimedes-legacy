package archimedes.codegenerators.gui.vaadin;

import java.sql.Types;
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
 * @author ollie (11.04.2022)
 */
public class DetailsLayoutClassCodeGenerator extends AbstractGUIVaadinClassCodeGenerator {

	public DetailsLayoutClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("DetailsLayoutClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("AbstractMasterDataBaseLayoutClassName", nameGenerator.getAbstractMasterDataBaseLayoutClassName());
		context.put("AbstractMasterDataBaseLayoutPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ButtonFactoryClassName", nameGenerator.getButtonFactoryClassName(model));
		context.put("ButtonFactoryPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ButtonPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ClassName", getClassName(model, table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("GUIColumnData", getGUIColumnData(table));
		context.put("GUIReferences", getGUIReferenceData(table));
		context.put("MasterDataButtonLayoutClassName", nameGenerator.getMasterDataButtonLayoutClassName(model));
		context.put("MasterDataButtonLayoutPackageName", nameGenerator.getMasterDataButtonLayoutPackageName(model));
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(table));
		context.put("ModelPackageName", serviceNameGenerator.getModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("ResourceManagerInterfaceName", localizationNameGenerator.getResourceManagerInterfaceName());
		context
				.put(
						"ResourceManagerPackageName",
						localizationNameGenerator.getResourceManagerPackageName(model, table));
		context.put("SessionDataClassName", nameGenerator.getSessionDataClassName(model));
		context.put("SessionDataPackageName", nameGenerator.getSessionDataPackageName(model));
		context.put("ServiceInterfaceName", serviceNameGenerator.getServiceInterfaceName(table));
		context.put("ServiceInterfacePackageName", serviceNameGenerator.getServiceInterfacePackageName(model, table));
	}

	private List<GUIColumnData> getGUIColumnData(TableModel table) {
		return List
				.of(table.getColumns())
				.stream()
				.filter(column -> column.isOptionSet(PageLayoutClassCodeGenerator.GUI_EDITOR_POS))
				.map(
						column -> new GUIColumnData()
								.setFieldNameCamelCase(nameGenerator.getCamelCase(column.getName()))
								.setMax(getMax(column))
								.setMin(getMin(column))
								.setPosition(getPosition(column))
								.setResourceName(nameGenerator.getAttributeName(column).toLowerCase())
								.setType(getType(column)))
				.sorted((gd0, gd1) -> gd0.getPosition() - gd1.getPosition())
				.collect(Collectors.toList());
	}

	private String getMax(ColumnModel column) {
		return column.isOptionSet(AbstractClassCodeGenerator.MAX)
				? column.getOptionByName(AbstractClassCodeGenerator.MAX).getParameter()
				: "null";
	}

	private String getMin(ColumnModel column) {
		return column.isOptionSet(AbstractClassCodeGenerator.MIN)
				? column.getOptionByName(AbstractClassCodeGenerator.MIN).getParameter()
				: "null";
	}

	private int getPosition(ColumnModel column) {
		return column.isOptionSet(PageLayoutClassCodeGenerator.GUI_EDITOR_POS)
				? Integer.valueOf(column.getOptionByName(PageLayoutClassCodeGenerator.GUI_EDITOR_POS).getParameter())
				: 0;
	}

	private String getType(ColumnModel column) {
		if (column.getReferencedTable() != null) {
			return GUIColumnData.TYPE_COMBOBOX;
		} else if (column.getDomain().getDataType() == Types.INTEGER) {
			return GUIColumnData.TYPE_INTEGER;
		}
		return GUIColumnData.TYPE_STRING;
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
		return nameGenerator.getPageLayoutPackageName(model, table);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel t) {
		return !t.isOptionSet(PageLayoutClassCodeGenerator.GENERATE_MASTER_DATA_GUI);
	}

}